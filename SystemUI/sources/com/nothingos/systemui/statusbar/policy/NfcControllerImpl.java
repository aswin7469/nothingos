package com.nothingos.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.policy.BatteryController;
import com.nothingos.systemui.statusbar.policy.NfcController;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class NfcControllerImpl implements NfcController {
    private BatteryController.BatteryStateChangeCallback mBatteryStateCallback;
    private final ArrayList<NfcController.CallBack> mCallBack = new ArrayList<>();
    private final Context mContext;
    private boolean mIsWirelessCharging;

    public NfcControllerImpl(Context context, BatteryController batteryController) {
        BatteryController.BatteryStateChangeCallback batteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.nothingos.systemui.statusbar.policy.NfcControllerImpl.1
            @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
            public void onWirelessChargingChanged(boolean z) {
                NfcControllerImpl.this.mIsWirelessCharging = z;
                NfcControllerImpl.this.fireBatteryWirelessChange();
            }
        };
        this.mBatteryStateCallback = batteryStateChangeCallback;
        this.mContext = context;
        if (batteryController != null) {
            batteryController.addCallback(batteryStateChangeCallback);
        }
    }

    @Override // com.nothingos.systemui.statusbar.policy.NfcController
    public boolean isWirelessCharging() {
        return this.mIsWirelessCharging;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(NfcController.CallBack callBack) {
        if (!this.mCallBack.contains(callBack)) {
            this.mCallBack.add(callBack);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(NfcController.CallBack callBack) {
        this.mCallBack.remove(callBack);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireBatteryWirelessChange() {
        Iterator<NfcController.CallBack> it = this.mCallBack.iterator();
        while (it.hasNext()) {
            it.next().onBatteryWirelessChange();
        }
    }
}
