package com.nothing.systemui.statusbar.policy;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.BatteryController;
import com.nothing.systemui.statusbar.policy.BatteryShareController;
import com.nothingos.systemui.statusbar.policy.NTWirelessChargingTracker;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;

@SysUISingleton
public class BatteryShareControllerImpl implements BatteryShareController {
    private static final String SETTINGS_KEY = "nt_wireless_reverse_charge";
    private BatteryController.BatteryStateChangeCallback mBatteryStateCallback = new BatteryController.BatteryStateChangeCallback() {
        public void onWirelessChargingChanged(boolean z) {
            boolean unused = BatteryShareControllerImpl.this.mIsWirelessCharging = z;
            BatteryShareControllerImpl.this.fireBatteryShareChange();
        }
    };
    private final ArrayList<BatteryShareController.CallBack> mCallback = new ArrayList<>();
    private final ContentObserver mContentObserver;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public boolean mEnabled;
    /* access modifiers changed from: private */
    public boolean mIsWirelessCharging;

    @Inject
    public BatteryShareControllerImpl(Context context, BatteryController batteryController) {
        C42262 r0 = new ContentObserver(new Handler()) {
            public void onChange(boolean z) {
                super.onChange(z);
                boolean z2 = false;
                int i = Settings.Global.getInt(BatteryShareControllerImpl.this.mContext.getContentResolver(), BatteryShareControllerImpl.SETTINGS_KEY, 0);
                BatteryShareControllerImpl batteryShareControllerImpl = BatteryShareControllerImpl.this;
                if (i == 1) {
                    z2 = true;
                }
                boolean unused = batteryShareControllerImpl.mEnabled = z2;
                BatteryShareControllerImpl.this.fireBatteryShareChange();
                if (BatteryShareControllerImpl.this.mEnabled) {
                    NTWirelessChargingTracker.getInstance(BatteryShareControllerImpl.this.mContext).startRecordBatteryShareTime();
                } else {
                    NTWirelessChargingTracker.getInstance(BatteryShareControllerImpl.this.mContext).endRecordBatteryShareTime();
                }
            }
        };
        this.mContentObserver = r0;
        this.mContext = context;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor(SETTINGS_KEY), false, r0);
        if (batteryController != null) {
            batteryController.addCallback(this.mBatteryStateCallback);
        }
    }

    public boolean isWirelessCharging() {
        return this.mIsWirelessCharging;
    }

    public void addCallback(BatteryShareController.CallBack callBack) {
        if (!this.mCallback.contains(callBack)) {
            this.mCallback.add(callBack);
        }
    }

    public void removeCallback(BatteryShareController.CallBack callBack) {
        this.mCallback.remove((Object) callBack);
    }

    public void setBatteryShareEnable() {
        this.mEnabled = !this.mEnabled;
        Settings.Global.putInt(this.mContext.getContentResolver(), SETTINGS_KEY, this.mEnabled ? 1 : 0);
    }

    public boolean getBatteryShareEnabled() {
        return this.mEnabled;
    }

    /* access modifiers changed from: private */
    public void fireBatteryShareChange() {
        Iterator<BatteryShareController.CallBack> it = this.mCallback.iterator();
        while (it.hasNext()) {
            it.next().onBatteryShareChange();
        }
    }
}
