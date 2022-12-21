package com.android.systemui.dreams.touch;

import android.graphics.Region;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.dagger.InputSessionComponent;
import com.android.systemui.shared.system.InputChannelCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class DreamOverlayTouchMonitor {
    /* access modifiers changed from: private */
    public final HashSet<TouchSessionImpl> mActiveTouchSessions = new HashSet<>();
    private InputSession mCurrentInputSession;
    private final Executor mExecutor;
    /* access modifiers changed from: private */
    public final Collection<DreamTouchHandler> mHandlers;
    private InputChannelCompat.InputEventListener mInputEventListener = new InputChannelCompat.InputEventListener() {
        public void onInputEvent(InputEvent inputEvent) {
            if (DreamOverlayTouchMonitor.this.mActiveTouchSessions.isEmpty()) {
                HashMap hashMap = new HashMap();
                for (DreamTouchHandler dreamTouchHandler : DreamOverlayTouchMonitor.this.mHandlers) {
                    Region obtain = Region.obtain();
                    dreamTouchHandler.getTouchInitiationRegion(obtain);
                    if (!obtain.isEmpty()) {
                        if (inputEvent instanceof MotionEvent) {
                            MotionEvent motionEvent = (MotionEvent) inputEvent;
                            if (!obtain.contains(Math.round(motionEvent.getX()), Math.round(motionEvent.getY()))) {
                            }
                        }
                    }
                    TouchSessionImpl touchSessionImpl = new TouchSessionImpl(DreamOverlayTouchMonitor.this, (TouchSessionImpl) null);
                    DreamOverlayTouchMonitor.this.mActiveTouchSessions.add(touchSessionImpl);
                    hashMap.put(dreamTouchHandler, touchSessionImpl);
                }
                hashMap.forEach(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda0());
            }
            DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda1()).flatMap(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda2()).forEach(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda3(inputEvent));
        }
    };
    private InputSessionComponent.Factory mInputSessionFactory;
    private final Lifecycle mLifecycle;
    private final LifecycleObserver mLifecycleObserver = new DefaultLifecycleObserver() {
        public void onResume(LifecycleOwner lifecycleOwner) {
            DreamOverlayTouchMonitor.this.startMonitoring();
        }

        public void onPause(LifecycleOwner lifecycleOwner) {
            DreamOverlayTouchMonitor.this.stopMonitoring();
        }
    };
    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {
        private boolean evaluate(Evaluator evaluator) {
            HashSet hashSet = new HashSet();
            boolean anyMatch = DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6(evaluator, hashSet)).anyMatch(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda7());
            if (anyMatch) {
                DreamOverlayTouchMonitor.this.isolate(hashSet);
            }
            return anyMatch;
        }

        static /* synthetic */ Boolean lambda$evaluate$2(Evaluator evaluator, Set set, TouchSessionImpl touchSessionImpl) {
            boolean anyMatch = touchSessionImpl.getGestureListeners().stream().map(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda10(evaluator)).anyMatch(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda11());
            if (anyMatch) {
                set.add(touchSessionImpl);
            }
            return Boolean.valueOf(anyMatch);
        }

        private void observe(Consumer<GestureDetector.OnGestureListener> consumer) {
            DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda1()).flatMap(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda2()).forEach(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda2(consumer));
        }

        public boolean onDown(MotionEvent motionEvent) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda5(motionEvent));
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda0(motionEvent, motionEvent2, f, f2));
        }

        public void onLongPress(MotionEvent motionEvent) {
            observe(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda8(motionEvent));
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3(motionEvent, motionEvent2, f, f2));
        }

        public void onShowPress(MotionEvent motionEvent) {
            observe(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda4(motionEvent));
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda9(motionEvent));
        }
    };

    private interface Evaluator {
        boolean evaluate(GestureDetector.OnGestureListener onGestureListener);
    }

    public static /* synthetic */ HashSet $r8$lambda$4_p_WrSG7a55MBc3xyT1qI7CInQ() {
        return new HashSet();
    }

    /* access modifiers changed from: private */
    public ListenableFuture<DreamTouchHandler.TouchSession> push(TouchSessionImpl touchSessionImpl) {
        return CallbackToFutureAdapter.getFuture(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda3(this, touchSessionImpl));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$push$1$com-android-systemui-dreams-touch-DreamOverlayTouchMonitor */
    public /* synthetic */ Object mo32638xd0f73b3(TouchSessionImpl touchSessionImpl, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda5(this, touchSessionImpl, completer));
        return "DreamOverlayTouchMonitor::push";
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$push$0$com-android-systemui-dreams-touch-DreamOverlayTouchMonitor */
    public /* synthetic */ void mo32637x70ba854(TouchSessionImpl touchSessionImpl, CallbackToFutureAdapter.Completer completer) {
        if (!this.mActiveTouchSessions.remove(touchSessionImpl)) {
            completer.set(null);
            return;
        }
        TouchSessionImpl touchSessionImpl2 = new TouchSessionImpl(this, touchSessionImpl);
        this.mActiveTouchSessions.add(touchSessionImpl2);
        completer.set(touchSessionImpl2);
    }

    /* access modifiers changed from: private */
    public ListenableFuture<DreamTouchHandler.TouchSession> pop(TouchSessionImpl touchSessionImpl) {
        return CallbackToFutureAdapter.getFuture(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda6(this, touchSessionImpl));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$pop$3$com-android-systemui-dreams-touch-DreamOverlayTouchMonitor */
    public /* synthetic */ Object mo32636x8724b574(TouchSessionImpl touchSessionImpl, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda4(this, touchSessionImpl, completer));
        return "DreamOverlayTouchMonitor::pop";
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$pop$2$com-android-systemui-dreams-touch-DreamOverlayTouchMonitor */
    public /* synthetic */ void mo32635x8120ea15(TouchSessionImpl touchSessionImpl, CallbackToFutureAdapter.Completer completer) {
        if (this.mActiveTouchSessions.remove(touchSessionImpl)) {
            touchSessionImpl.onRemoved();
            TouchSessionImpl access$900 = touchSessionImpl.getPredecessor();
            if (access$900 != null) {
                this.mActiveTouchSessions.add(access$900);
            }
            completer.set(access$900);
        }
    }

    /* access modifiers changed from: private */
    public int getSessionCount() {
        return this.mActiveTouchSessions.size();
    }

    private static class TouchSessionImpl implements DreamTouchHandler.TouchSession {
        private final HashSet<DreamTouchHandler.TouchSession.Callback> mCallbacks = new HashSet<>();
        private final HashSet<InputChannelCompat.InputEventListener> mEventListeners = new HashSet<>();
        private final HashSet<GestureDetector.OnGestureListener> mGestureListeners = new HashSet<>();
        private final TouchSessionImpl mPredecessor;
        private final DreamOverlayTouchMonitor mTouchMonitor;

        TouchSessionImpl(DreamOverlayTouchMonitor dreamOverlayTouchMonitor, TouchSessionImpl touchSessionImpl) {
            this.mPredecessor = touchSessionImpl;
            this.mTouchMonitor = dreamOverlayTouchMonitor;
        }

        public void registerCallback(DreamTouchHandler.TouchSession.Callback callback) {
            this.mCallbacks.add(callback);
        }

        public boolean registerInputListener(InputChannelCompat.InputEventListener inputEventListener) {
            return this.mEventListeners.add(inputEventListener);
        }

        public boolean registerGestureListener(GestureDetector.OnGestureListener onGestureListener) {
            return this.mGestureListeners.add(onGestureListener);
        }

        public ListenableFuture<DreamTouchHandler.TouchSession> push() {
            return this.mTouchMonitor.push(this);
        }

        public ListenableFuture<DreamTouchHandler.TouchSession> pop() {
            return this.mTouchMonitor.pop(this);
        }

        public int getActiveSessionCount() {
            return this.mTouchMonitor.getSessionCount();
        }

        public Collection<InputChannelCompat.InputEventListener> getEventListeners() {
            return this.mEventListeners;
        }

        public Collection<GestureDetector.OnGestureListener> getGestureListeners() {
            return this.mGestureListeners;
        }

        /* access modifiers changed from: private */
        public TouchSessionImpl getPredecessor() {
            return this.mPredecessor;
        }

        /* access modifiers changed from: private */
        public void onRemoved() {
            this.mCallbacks.forEach(new C2100x6ef1d0c2());
        }
    }

    /* access modifiers changed from: private */
    public void startMonitoring() {
        stopMonitoring();
        this.mCurrentInputSession = this.mInputSessionFactory.create("dreamOverlay", this.mInputEventListener, this.mOnGestureListener, true).getInputSession();
    }

    /* access modifiers changed from: private */
    public void stopMonitoring() {
        InputSession inputSession = this.mCurrentInputSession;
        if (inputSession != null) {
            inputSession.dispose();
            this.mCurrentInputSession = null;
        }
    }

    @Inject
    public DreamOverlayTouchMonitor(@Main Executor executor, Lifecycle lifecycle, InputSessionComponent.Factory factory, Set<DreamTouchHandler> set) {
        this.mHandlers = set;
        this.mInputSessionFactory = factory;
        this.mExecutor = executor;
        this.mLifecycle = lifecycle;
    }

    public void init() {
        this.mLifecycle.addObserver(this.mLifecycleObserver);
    }

    /* access modifiers changed from: private */
    public void isolate(Set<TouchSessionImpl> set) {
        Collection collection = (Collection) this.mActiveTouchSessions.stream().filter(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda0(set)).collect(Collectors.toCollection(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda1()));
        collection.forEach(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda2());
        this.mActiveTouchSessions.removeAll(collection);
    }

    static /* synthetic */ boolean lambda$isolate$4(Set set, TouchSessionImpl touchSessionImpl) {
        return !set.contains(touchSessionImpl);
    }
}
