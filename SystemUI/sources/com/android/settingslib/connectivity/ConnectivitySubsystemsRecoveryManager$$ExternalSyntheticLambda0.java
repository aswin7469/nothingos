package com.android.settingslib.connectivity;

import com.android.settingslib.connectivity.ConnectivitySubsystemsRecoveryManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ConnectivitySubsystemsRecoveryManager f$0;
    public final /* synthetic */ ConnectivitySubsystemsRecoveryManager.RecoveryAvailableListener f$1;

    public /* synthetic */ ConnectivitySubsystemsRecoveryManager$$ExternalSyntheticLambda0(ConnectivitySubsystemsRecoveryManager connectivitySubsystemsRecoveryManager, ConnectivitySubsystemsRecoveryManager.RecoveryAvailableListener recoveryAvailableListener) {
        this.f$0 = connectivitySubsystemsRecoveryManager;
        this.f$1 = recoveryAvailableListener;
    }

    public final void run() {
        this.f$0.mo28446x8f9ba31(this.f$1);
    }
}
