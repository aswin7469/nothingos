package androidx.dynamicanimation.animation;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayList;

public class AnimationHandler {
    private static final long FRAME_DELAY_MS = 10;
    private static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal<>();
    final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList<>();
    private final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    long mCurrentFrameTime = 0;
    private final SimpleArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new SimpleArrayMap<>();
    public float mDurationScale = 1.0f;
    public DurationScaleChangeListener mDurationScaleChangeListener;
    private boolean mListDirty = false;
    /* access modifiers changed from: private */
    public final Runnable mRunnable = new AnimationHandler$$ExternalSyntheticLambda0(this);
    /* access modifiers changed from: private */
    public FrameCallbackScheduler mScheduler;

    interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    public interface DurationScaleChangeListener {
        boolean register();

        boolean unregister();
    }

    private class AnimationCallbackDispatcher {
        private AnimationCallbackDispatcher() {
        }

        /* access modifiers changed from: package-private */
        public void dispatchAnimationFrame() {
            AnimationHandler.this.mCurrentFrameTime = SystemClock.uptimeMillis();
            AnimationHandler animationHandler = AnimationHandler.this;
            animationHandler.doAnimationFrame(animationHandler.mCurrentFrameTime);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                AnimationHandler.this.mScheduler.postFrameCallback(AnimationHandler.this.mRunnable);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$androidx-dynamicanimation-animation-AnimationHandler */
    public /* synthetic */ void mo14204x83fff5a8() {
        this.mCallbackDispatcher.dispatchAnimationFrame();
    }

    static AnimationHandler getInstance() {
        ThreadLocal<AnimationHandler> threadLocal = sAnimatorHandler;
        if (threadLocal.get() == null) {
            threadLocal.set(new AnimationHandler(new FrameCallbackScheduler16()));
        }
        return threadLocal.get();
    }

    public AnimationHandler(FrameCallbackScheduler frameCallbackScheduler) {
        this.mScheduler = frameCallbackScheduler;
    }

    /* access modifiers changed from: package-private */
    public void addAnimationFrameCallback(AnimationFrameCallback animationFrameCallback, long j) {
        if (this.mAnimationCallbacks.size() == 0) {
            this.mScheduler.postFrameCallback(this.mRunnable);
            this.mDurationScale = ValueAnimator.getDurationScale();
            if (this.mDurationScaleChangeListener == null) {
                this.mDurationScaleChangeListener = new DurationScaleChangeListener33();
            }
            this.mDurationScaleChangeListener.register();
        }
        if (!this.mAnimationCallbacks.contains(animationFrameCallback)) {
            this.mAnimationCallbacks.add(animationFrameCallback);
        }
        if (j > 0) {
            this.mDelayedCallbackStartTime.put(animationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + j));
        }
    }

    /* access modifiers changed from: package-private */
    public void removeCallback(AnimationFrameCallback animationFrameCallback) {
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        int indexOf = this.mAnimationCallbacks.indexOf(animationFrameCallback);
        if (indexOf >= 0) {
            this.mAnimationCallbacks.set(indexOf, null);
            this.mListDirty = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void doAnimationFrame(long j) {
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i = 0; i < this.mAnimationCallbacks.size(); i++) {
            AnimationFrameCallback animationFrameCallback = this.mAnimationCallbacks.get(i);
            if (animationFrameCallback != null && isCallbackDue(animationFrameCallback, uptimeMillis)) {
                animationFrameCallback.doAnimationFrame(j);
            }
        }
        cleanUpList();
    }

    /* access modifiers changed from: package-private */
    public boolean isCurrentThread() {
        return this.mScheduler.isCurrentThread();
    }

    private boolean isCallbackDue(AnimationFrameCallback animationFrameCallback, long j) {
        Long l = this.mDelayedCallbackStartTime.get(animationFrameCallback);
        if (l == null) {
            return true;
        }
        if (l.longValue() >= j) {
            return false;
        }
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        return true;
    }

    private void cleanUpList() {
        if (this.mListDirty) {
            for (int size = this.mAnimationCallbacks.size() - 1; size >= 0; size--) {
                if (this.mAnimationCallbacks.get(size) == null) {
                    this.mAnimationCallbacks.remove(size);
                }
            }
            if (this.mAnimationCallbacks.size() == 0) {
                this.mDurationScaleChangeListener.unregister();
            }
            this.mListDirty = false;
        }
    }

    /* access modifiers changed from: package-private */
    public FrameCallbackScheduler getScheduler() {
        return this.mScheduler;
    }

    static final class FrameCallbackScheduler16 implements FrameCallbackScheduler {
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private final Looper mLooper = Looper.myLooper();

        FrameCallbackScheduler16() {
        }

        public void postFrameCallback(Runnable runnable) {
            this.mChoreographer.postFrameCallback(new C0634xde8d857b(runnable));
        }

        public boolean isCurrentThread() {
            return Thread.currentThread() == this.mLooper.getThread();
        }
    }

    static class FrameCallbackScheduler14 implements FrameCallbackScheduler {
        private final Handler mHandler = new Handler(Looper.myLooper());
        private long mLastFrameTime;

        FrameCallbackScheduler14() {
        }

        public void postFrameCallback(Runnable runnable) {
            this.mHandler.postDelayed(new C0633x6beb63f9(this, runnable), Math.max(AnimationHandler.FRAME_DELAY_MS - (SystemClock.uptimeMillis() - this.mLastFrameTime), 0));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$postFrameCallback$0$androidx-dynamicanimation-animation-AnimationHandler$FrameCallbackScheduler14 */
        public /* synthetic */ void mo14212x7ba8ba(Runnable runnable) {
            this.mLastFrameTime = SystemClock.uptimeMillis();
            runnable.run();
        }

        public boolean isCurrentThread() {
            return Thread.currentThread() == this.mHandler.getLooper().getThread();
        }
    }

    public float getDurationScale() {
        return this.mDurationScale;
    }

    public class DurationScaleChangeListener33 implements DurationScaleChangeListener {
        ValueAnimator.DurationScaleChangeListener mListener;

        public DurationScaleChangeListener33() {
        }

        public boolean register() {
            if (this.mListener != null) {
                return true;
            }
            C0632xf577eeeb animationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0 = new C0632xf577eeeb(this);
            this.mListener = animationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0;
            return ValueAnimator.registerDurationScaleChangeListener(animationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$register$0$androidx-dynamicanimation-animation-AnimationHandler$DurationScaleChangeListener33 */
        public /* synthetic */ void mo14210xb804c881(float f) {
            AnimationHandler.this.mDurationScale = f;
        }

        public boolean unregister() {
            boolean unregisterDurationScaleChangeListener = ValueAnimator.unregisterDurationScaleChangeListener(this.mListener);
            this.mListener = null;
            return unregisterDurationScaleChangeListener;
        }
    }
}
