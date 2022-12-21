package tech.nothing.settingslib.mobile;

import android.telephony.ServiceState;
import android.telephony.SignalStrength;

public interface SignalSmooth {
    boolean delayNotifyOos(ServiceState serviceState);

    int getSmoothLevel(SignalStrength signalStrength);

    void setListening(boolean z);

    boolean smoothSignal(SignalStrength signalStrength);
}
