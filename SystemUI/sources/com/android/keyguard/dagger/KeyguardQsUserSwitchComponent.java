package com.android.keyguard.dagger;

import android.widget.FrameLayout;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
/* loaded from: classes.dex */
public interface KeyguardQsUserSwitchComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        KeyguardQsUserSwitchComponent build(FrameLayout frameLayout);
    }

    KeyguardQsUserSwitchController getKeyguardQsUserSwitchController();
}
