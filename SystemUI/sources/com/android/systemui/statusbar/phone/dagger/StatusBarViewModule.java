package com.android.systemui.statusbar.phone.dagger;

import com.airbnb.lottie.LottieAnimationView;
import com.android.keyguard.LockIconView;
import com.android.systemui.R$id;
import com.android.systemui.biometrics.AuthRippleView;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.TapAgainView;
/* loaded from: classes.dex */
public abstract class StatusBarViewModule {
    public static NotificationPanelView getNotificationPanelView(NotificationShadeWindowView notificationShadeWindowView) {
        return notificationShadeWindowView.getNotificationPanelView();
    }

    public static LockIconView getLockIconView(NotificationShadeWindowView notificationShadeWindowView) {
        return (LockIconView) notificationShadeWindowView.findViewById(R$id.lock_icon_view);
    }

    public static AuthRippleView getAuthRippleView(NotificationShadeWindowView notificationShadeWindowView) {
        return (AuthRippleView) notificationShadeWindowView.findViewById(R$id.auth_ripple);
    }

    public static TapAgainView getTapAgainView(NotificationPanelView notificationPanelView) {
        return notificationPanelView.getTapAgainView();
    }

    public static LottieAnimationView getLottieAnimationView(NotificationShadeWindowView notificationShadeWindowView) {
        return (LottieAnimationView) notificationShadeWindowView.findViewById(R$id.nt_udfps_lockscreen_fp_scanning);
    }
}
