package com.android.keyguard.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.RootView;
import dagger.Module;
import dagger.Provides;

@Module
public interface KeyguardBouncerModule {
    @KeyguardBouncerScope
    @Provides
    static KeyguardHostView providesKeyguardHostView(@RootView ViewGroup viewGroup, LayoutInflater layoutInflater) {
        KeyguardHostView keyguardHostView = (KeyguardHostView) layoutInflater.inflate(C1893R.layout.keyguard_host_view, viewGroup, false);
        viewGroup.addView(keyguardHostView);
        return keyguardHostView;
    }

    @KeyguardBouncerScope
    @Provides
    static KeyguardSecurityContainer providesKeyguardSecurityContainer(KeyguardHostView keyguardHostView) {
        return (KeyguardSecurityContainer) keyguardHostView.findViewById(C1893R.C1897id.keyguard_security_container);
    }

    @KeyguardBouncerScope
    @Provides
    static KeyguardSecurityViewFlipper providesKeyguardSecurityViewFlipper(KeyguardSecurityContainer keyguardSecurityContainer) {
        return (KeyguardSecurityViewFlipper) keyguardSecurityContainer.findViewById(C1893R.C1897id.view_flipper);
    }
}
