package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.MathUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0018B)\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R$\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller;", "Lcom/android/systemui/statusbar/LockScreenShadeOverScroller;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "context", "Landroid/content/Context;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "nsslController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "(Lcom/android/systemui/statusbar/policy/ConfigurationController;Landroid/content/Context;Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;)V", "value", "", "expansionDragDownAmount", "getExpansionDragDownAmount", "()F", "setExpansionDragDownAmount", "(F)V", "maxOverScrollAmount", "", "totalDistanceForFullShadeTransition", "overScroll", "", "updateResources", "Factory", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SingleShadeLockScreenOverScroller.kt */
public final class SingleShadeLockScreenOverScroller implements LockScreenShadeOverScroller {
    private final Context context;
    private float expansionDragDownAmount;
    private int maxOverScrollAmount;
    private final NotificationStackScrollLayoutController nsslController;
    private final SysuiStatusBarStateController statusBarStateController;
    private int totalDistanceForFullShadeTransition;

    @AssistedFactory
    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bç\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller$Factory;", "", "create", "Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller;", "nsslController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SingleShadeLockScreenOverScroller.kt */
    public interface Factory {
        SingleShadeLockScreenOverScroller create(NotificationStackScrollLayoutController notificationStackScrollLayoutController);
    }

    @AssistedInject
    public SingleShadeLockScreenOverScroller(ConfigurationController configurationController, Context context2, SysuiStatusBarStateController sysuiStatusBarStateController, @Assisted NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "nsslController");
        this.context = context2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.nsslController = notificationStackScrollLayoutController;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ SingleShadeLockScreenOverScroller this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
            }
        });
    }

    /* access modifiers changed from: private */
    public final void updateResources() {
        Resources resources = this.context.getResources();
        this.totalDistanceForFullShadeTransition = resources.getDimensionPixelSize(C1893R.dimen.lockscreen_shade_qs_transition_distance);
        this.maxOverScrollAmount = resources.getDimensionPixelSize(C1893R.dimen.lockscreen_shade_max_over_scroll_amount);
    }

    public float getExpansionDragDownAmount() {
        return this.expansionDragDownAmount;
    }

    public void setExpansionDragDownAmount(float f) {
        if (!(f == this.expansionDragDownAmount)) {
            this.expansionDragDownAmount = f;
            overScroll();
        }
    }

    private final void overScroll() {
        float f;
        if (this.statusBarStateController.getState() == 1) {
            float height = (float) this.nsslController.getHeight();
            f = Interpolators.getOvershootInterpolation(MathUtils.saturate(getExpansionDragDownAmount() / height), 0.6f, ((float) this.totalDistanceForFullShadeTransition) / height) * ((float) this.maxOverScrollAmount);
        } else {
            f = 0.0f;
        }
        this.nsslController.setOverScrollAmount((int) f);
    }
}
