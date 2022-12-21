package com.android.keyguard.dagger;

import android.view.ViewGroup;
import com.android.keyguard.KeyguardHostViewController;
import com.android.systemui.dagger.qualifiers.RootView;
import dagger.BindsInstance;
import dagger.Subcomponent;

@KeyguardBouncerScope
@Subcomponent(modules = {KeyguardBouncerModule.class})
public interface KeyguardBouncerComponent {

    @Subcomponent.Factory
    public interface Factory {
        KeyguardBouncerComponent create(@RootView @BindsInstance ViewGroup viewGroup);
    }

    KeyguardHostViewController getKeyguardHostViewController();
}
