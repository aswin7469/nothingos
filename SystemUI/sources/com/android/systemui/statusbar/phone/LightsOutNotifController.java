package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.InsetsVisibilities;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import androidx.lifecycle.Observer;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentScope;
import com.android.systemui.util.ViewController;
import javax.inject.Inject;
import javax.inject.Named;

@StatusBarFragmentScope
public class LightsOutNotifController extends ViewController<View> {
    int mAppearance;
    private final CommandQueue.Callbacks mCallback = new CommandQueue.Callbacks() {
        public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
            if (i == LightsOutNotifController.this.mDisplayId) {
                LightsOutNotifController.this.mAppearance = i2;
                LightsOutNotifController.this.updateLightsOutView();
            }
        }
    };
    private final CommandQueue mCommandQueue;
    /* access modifiers changed from: private */
    public int mDisplayId;
    private final NotifLiveDataStore mNotifDataStore;
    private final Observer<Boolean> mObserver = new LightsOutNotifController$$ExternalSyntheticLambda0(this);
    private final WindowManager mWindowManager;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-LightsOutNotifController */
    public /* synthetic */ void mo44394xc5976051(Boolean bool) {
        updateLightsOutView();
    }

    @Inject
    LightsOutNotifController(@Named("lights_out_notif_view") View view, WindowManager windowManager, NotifLiveDataStore notifLiveDataStore, CommandQueue commandQueue) {
        super(view);
        this.mWindowManager = windowManager;
        this.mNotifDataStore = notifLiveDataStore;
        this.mCommandQueue = commandQueue;
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mNotifDataStore.getHasActiveNotifs().removeObserver(this.mObserver);
        this.mCommandQueue.removeCallback(this.mCallback);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mView.setVisibility(8);
        this.mView.setAlpha(0.0f);
        this.mDisplayId = this.mWindowManager.getDefaultDisplay().getDisplayId();
        this.mNotifDataStore.getHasActiveNotifs().addSyncObserver(this.mObserver);
        this.mCommandQueue.addCallback(this.mCallback);
        updateLightsOutView();
    }

    private boolean hasActiveNotifications() {
        return this.mNotifDataStore.getHasActiveNotifs().getValue().booleanValue();
    }

    /* access modifiers changed from: package-private */
    public void updateLightsOutView() {
        final boolean shouldShowDot = shouldShowDot();
        if (shouldShowDot != isShowingDot()) {
            float f = 0.0f;
            if (shouldShowDot) {
                this.mView.setAlpha(0.0f);
                this.mView.setVisibility(0);
            }
            ViewPropertyAnimator animate = this.mView.animate();
            if (shouldShowDot) {
                f = 1.0f;
            }
            animate.alpha(f).setDuration(shouldShowDot ? 750 : 250).setInterpolator(new AccelerateInterpolator(2.0f)).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    LightsOutNotifController.this.mView.setAlpha(shouldShowDot ? 1.0f : 0.0f);
                    LightsOutNotifController.this.mView.setVisibility(shouldShowDot ? 0 : 8);
                    LightsOutNotifController.this.mView.animate().setListener((Animator.AnimatorListener) null);
                }
            }).start();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isShowingDot() {
        return this.mView.getVisibility() == 0 && this.mView.getAlpha() == 1.0f;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldShowDot() {
        return hasActiveNotifications() && areLightsOut();
    }

    /* access modifiers changed from: package-private */
    public boolean areLightsOut() {
        return (this.mAppearance & 4) != 0;
    }
}
