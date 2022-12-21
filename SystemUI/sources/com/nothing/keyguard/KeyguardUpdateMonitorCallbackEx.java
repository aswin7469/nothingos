package com.nothing.keyguard;

import com.android.keyguard.KeyguardUpdateMonitorCallback;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class KeyguardUpdateMonitorCallbackEx {
    private ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mCallbacks;

    public void onCriticalTemperaturWarning(boolean z) {
    }

    public void onTapWakeUp() {
    }
}
