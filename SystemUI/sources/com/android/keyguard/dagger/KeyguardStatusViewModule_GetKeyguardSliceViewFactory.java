package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardSliceView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardStatusViewModule_GetKeyguardSliceViewFactory implements Factory<KeyguardSliceView> {
    private final Provider<KeyguardClockSwitch> keyguardClockSwitchProvider;

    public KeyguardStatusViewModule_GetKeyguardSliceViewFactory(Provider<KeyguardClockSwitch> provider) {
        this.keyguardClockSwitchProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardSliceView mo1933get() {
        return getKeyguardSliceView(this.keyguardClockSwitchProvider.mo1933get());
    }

    public static KeyguardStatusViewModule_GetKeyguardSliceViewFactory create(Provider<KeyguardClockSwitch> provider) {
        return new KeyguardStatusViewModule_GetKeyguardSliceViewFactory(provider);
    }

    public static KeyguardSliceView getKeyguardSliceView(KeyguardClockSwitch keyguardClockSwitch) {
        return (KeyguardSliceView) Preconditions.checkNotNullFromProvides(KeyguardStatusViewModule.getKeyguardSliceView(keyguardClockSwitch));
    }
}
