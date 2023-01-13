package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardSliceView;
import com.android.keyguard.KeyguardStatusView;
import com.android.systemui.C1894R;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class KeyguardStatusViewModule {
    @Provides
    static KeyguardClockSwitch getKeyguardClockSwitch(KeyguardStatusView keyguardStatusView) {
        return (KeyguardClockSwitch) keyguardStatusView.findViewById(C1894R.C1898id.keyguard_clock_container);
    }

    @Provides
    static KeyguardSliceView getKeyguardSliceView(KeyguardClockSwitch keyguardClockSwitch) {
        return (KeyguardSliceView) keyguardClockSwitch.findViewById(C1894R.C1898id.keyguard_slice_view);
    }
}
