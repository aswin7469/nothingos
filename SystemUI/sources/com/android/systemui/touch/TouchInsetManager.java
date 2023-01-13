package com.android.systemui.touch;

import android.graphics.Rect;
import android.graphics.Region;
import android.view.View;
import android.view.ViewRootImpl;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;

public class TouchInsetManager {
    private final View.OnAttachStateChangeListener mAttachListener;
    private final HashMap<TouchInsetSession, Region> mDefinedRegions = new HashMap<>();
    private final Executor mExecutor;
    private final View mRootView;

    public static class TouchInsetSession {
        private final Executor mExecutor;
        private final TouchInsetManager mManager;
        private final View.OnLayoutChangeListener mOnLayoutChangeListener = new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda2(this);
        private final HashSet<View> mTrackedViews;

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-android-systemui-touch-TouchInsetManager$TouchInsetSession */
        public /* synthetic */ void mo46356x84eaf2ea(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            updateTouchRegion();
        }

        TouchInsetSession(TouchInsetManager touchInsetManager, Executor executor) {
            this.mManager = touchInsetManager;
            this.mTrackedViews = new HashSet<>();
            this.mExecutor = executor;
        }

        public void addViewToTracking(View view) {
            this.mExecutor.execute(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda0(this, view));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$addViewToTracking$1$com-android-systemui-touch-TouchInsetManager$TouchInsetSession */
        public /* synthetic */ void mo46354xcffa8ed1(View view) {
            this.mTrackedViews.add(view);
            view.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
            updateTouchRegion();
        }

        public void removeViewFromTracking(View view) {
            this.mExecutor.execute(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda1(this, view));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$removeViewFromTracking$2$com-android-systemui-touch-TouchInsetManager$TouchInsetSession */
        public /* synthetic */ void mo46357x54fb6084(View view) {
            this.mTrackedViews.remove(view);
            view.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
            updateTouchRegion();
        }

        private void updateTouchRegion() {
            Region obtain = Region.obtain();
            this.mTrackedViews.stream().forEach(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda4(obtain));
            this.mManager.setTouchRegion(this, obtain);
            obtain.recycle();
        }

        static /* synthetic */ void lambda$updateTouchRegion$3(Region region, View view) {
            Rect rect = new Rect();
            view.getBoundsOnScreen(rect);
            region.op(rect, Region.Op.UNION);
        }

        public void clear() {
            this.mExecutor.execute(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$clear$4$com-android-systemui-touch-TouchInsetManager$TouchInsetSession */
        public /* synthetic */ void mo46355x30b0b819() {
            this.mManager.clearRegion(this);
            this.mTrackedViews.clear();
        }
    }

    public TouchInsetManager(Executor executor, View view) {
        C32251 r0 = new View.OnAttachStateChangeListener() {
            public void onViewDetachedFromWindow(View view) {
            }

            public void onViewAttachedToWindow(View view) {
                TouchInsetManager.this.updateTouchInset();
            }
        };
        this.mAttachListener = r0;
        this.mExecutor = executor;
        this.mRootView = view;
        view.addOnAttachStateChangeListener(r0);
    }

    public TouchInsetSession createSession() {
        return new TouchInsetSession(this, this.mExecutor);
    }

    public ListenableFuture<Boolean> checkWithinTouchRegion(int i, int i2) {
        return CallbackToFutureAdapter.getFuture(new TouchInsetManager$$ExternalSyntheticLambda0(this, i, i2));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$checkWithinTouchRegion$1$com-android-systemui-touch-TouchInsetManager */
    public /* synthetic */ void mo46345x676c2f3b(CallbackToFutureAdapter.Completer completer, int i, int i2) {
        completer.set(Boolean.valueOf(this.mDefinedRegions.values().stream().anyMatch(new TouchInsetManager$$ExternalSyntheticLambda1(i, i2))));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$checkWithinTouchRegion$2$com-android-systemui-touch-TouchInsetManager */
    public /* synthetic */ Object mo46346x20e3bcda(int i, int i2, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new TouchInsetManager$$ExternalSyntheticLambda4(this, completer, i, i2));
        return "DreamOverlayTouchMonitor::checkWithinTouchRegion";
    }

    /* access modifiers changed from: private */
    public void updateTouchInset() {
        ViewRootImpl viewRootImpl = this.mRootView.getViewRootImpl();
        if (viewRootImpl != null) {
            Region obtain = Region.obtain();
            for (Region op : this.mDefinedRegions.values()) {
                obtain.op(op, Region.Op.UNION);
            }
            viewRootImpl.setTouchableRegion(obtain);
            obtain.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void setTouchRegion(TouchInsetSession touchInsetSession, Region region) {
        this.mExecutor.execute(new TouchInsetManager$$ExternalSyntheticLambda2(this, touchInsetSession, Region.obtain(region)));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setTouchRegion$3$com-android-systemui-touch-TouchInsetManager */
    public /* synthetic */ void mo46348xdb199988(TouchInsetSession touchInsetSession, Region region) {
        this.mDefinedRegions.put(touchInsetSession, region);
        updateTouchInset();
    }

    /* access modifiers changed from: private */
    public void clearRegion(TouchInsetSession touchInsetSession) {
        this.mExecutor.execute(new TouchInsetManager$$ExternalSyntheticLambda3(this, touchInsetSession));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$clearRegion$4$com-android-systemui-touch-TouchInsetManager */
    public /* synthetic */ void mo46347x2e387df1(TouchInsetSession touchInsetSession) {
        Region remove = this.mDefinedRegions.remove(touchInsetSession);
        if (remove != null) {
            remove.recycle();
        }
        updateTouchInset();
    }
}
