package com.nothingos.systemui.statusbar.policy;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import com.android.systemui.statusbar.policy.BatteryController;
import com.nothingos.systemui.statusbar.policy.BatteryShareController;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class BatteryShareControllerImpl implements BatteryShareController {
    private final ContentObserver mContentObserver;
    private final Context mContext;
    private boolean mEnabled;
    private boolean mIsWirelessCharging;
    private final ArrayList<BatteryShareController.CallBack> mCallback = new ArrayList<>();
    private BatteryController.BatteryStateChangeCallback mBatteryStateCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.nothingos.systemui.statusbar.policy.BatteryShareControllerImpl.1
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onWirelessChargingChanged(boolean z) {
            BatteryShareControllerImpl.this.mIsWirelessCharging = z;
            BatteryShareControllerImpl.this.fireBatteryShareChange();
        }
    };

    public BatteryShareControllerImpl(Context context, BatteryController batteryController) {
        ContentObserver contentObserver = new ContentObserver(new Handler()) { // from class: com.nothingos.systemui.statusbar.policy.BatteryShareControllerImpl.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                boolean z2 = false;
                int i = Settings.Global.getInt(BatteryShareControllerImpl.this.mContext.getContentResolver(), "nt_wireless_reverse_charge", 0);
                BatteryShareControllerImpl batteryShareControllerImpl = BatteryShareControllerImpl.this;
                if (i == 1) {
                    z2 = true;
                }
                batteryShareControllerImpl.mEnabled = z2;
                BatteryShareControllerImpl.this.fireBatteryShareChange();
            }
        };
        this.mContentObserver = contentObserver;
        this.mContext = context;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("nt_wireless_reverse_charge"), false, contentObserver);
        if (batteryController != null) {
            batteryController.addCallback(this.mBatteryStateCallback);
        }
    }

    @Override // com.nothingos.systemui.statusbar.policy.BatteryShareController
    public boolean isWirelessCharging() {
        return this.mIsWirelessCharging;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(BatteryShareController.CallBack callBack) {
        if (!this.mCallback.contains(callBack)) {
            this.mCallback.add(callBack);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(BatteryShareController.CallBack callBack) {
        this.mCallback.remove(callBack);
    }

    @Override // com.nothingos.systemui.statusbar.policy.BatteryShareController
    public void setBatteryShareEnable() {
        this.mEnabled = !this.mEnabled;
        Settings.Global.putInt(this.mContext.getContentResolver(), "nt_wireless_reverse_charge", this.mEnabled ? 1 : 0);
    }

    @Override // com.nothingos.systemui.statusbar.policy.BatteryShareController
    public boolean getBatteryShareEnabled() {
        return this.mEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireBatteryShareChange() {
        Iterator<BatteryShareController.CallBack> it = this.mCallback.iterator();
        while (it.hasNext()) {
            it.next().onBatteryShareChange();
        }
    }
}
