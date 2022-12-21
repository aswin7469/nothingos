package com.android.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.DozeScope;
import javax.inject.Inject;

@DozeScope
public class DozeAuthRemover implements DozeMachine.Part {
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    @Inject
    public DozeAuthRemover(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        if (state2 == DozeMachine.State.DOZE || state2 == DozeMachine.State.DOZE_AOD) {
            if (this.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser())) {
                this.mKeyguardUpdateMonitor.clearBiometricRecognized();
            }
        }
    }
}
