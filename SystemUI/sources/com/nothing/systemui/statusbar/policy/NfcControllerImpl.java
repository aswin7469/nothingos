package com.nothing.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.BatteryController;
import com.nothing.systemui.statusbar.policy.NfcController;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;

@SysUISingleton
public class NfcControllerImpl implements NfcController {
    private BatteryController.BatteryStateChangeCallback mBatteryStateCallback;
    private final ArrayList<NfcController.CallBack> mCallBack = new ArrayList<>();
    private final Context mContext;
    /* access modifiers changed from: private */
    public boolean mIsWirelessCharging;

    @Inject
    public NfcControllerImpl(Context context, BatteryController batteryController) {
        C42281 r0 = new BatteryController.BatteryStateChangeCallback() {
            public void onWirelessChargingChanged(boolean z) {
                boolean unused = NfcControllerImpl.this.mIsWirelessCharging = z;
                NfcControllerImpl.this.fireBatteryWirelessChange();
            }
        };
        this.mBatteryStateCallback = r0;
        this.mContext = context;
        if (batteryController != null) {
            batteryController.addCallback(r0);
        }
    }

    public boolean isWirelessCharging() {
        return this.mIsWirelessCharging;
    }

    public void addCallback(NfcController.CallBack callBack) {
        if (!this.mCallBack.contains(callBack)) {
            this.mCallBack.add(callBack);
        }
    }

    public void removeCallback(NfcController.CallBack callBack) {
        this.mCallBack.remove((Object) callBack);
    }

    /* access modifiers changed from: private */
    public void fireBatteryWirelessChange() {
        Iterator<NfcController.CallBack> it = this.mCallBack.iterator();
        while (it.hasNext()) {
            it.next().onBatteryWirelessChange();
        }
    }
}
