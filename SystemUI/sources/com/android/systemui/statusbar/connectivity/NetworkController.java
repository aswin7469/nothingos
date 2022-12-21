package com.android.systemui.statusbar.connectivity;

import com.android.settingslib.net.DataUsageController;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.DataSaverController;

public interface NetworkController extends CallbackController<SignalCallback>, DemoMode {

    public interface EmergencyListener {
        void setEmergencyCallsOnly(boolean z);
    }

    void addEmergencyListener(EmergencyListener emergencyListener);

    AccessPointController getAccessPointController();

    DataSaverController getDataSaverController();

    DataUsageController getMobileDataController();

    String getMobileDataNetworkName();

    int getNumberSubscriptions();

    boolean hasEmergencyCryptKeeperText();

    boolean hasMobileDataFeature();

    boolean hasVoiceCallingFeature();

    boolean isMobileDataNetworkInService();

    boolean isRadioOn();

    void removeEmergencyListener(EmergencyListener emergencyListener);

    void setQSHost(QSTileHost qSTileHost);

    void setWifiEnabled(boolean z);
}
