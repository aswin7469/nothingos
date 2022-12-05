package com.android.settings.nfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.util.Log;
/* loaded from: classes.dex */
public abstract class BaseNfcEnabler {
    protected final Context mContext;
    private final IntentFilter mIntentFilter;
    protected final NfcAdapter mNfcAdapter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.settings.nfc.BaseNfcEnabler.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean z = true;
            if ("android.nfc.action.ADAPTER_STATE_CHANGED".equals(intent.getAction())) {
                BaseNfcEnabler.this.handleNfcStateChanged(intent.getIntExtra("android.nfc.extra.ADAPTER_STATE", 1));
            } else if ("android.os.action.CHARGING".equals(intent.getAction()) || "android.os.action.DISCHARGING".equals(intent.getAction()) || !"android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
            } else {
                int intExtra = intent.getIntExtra("plugged", 0);
                BaseNfcEnabler baseNfcEnabler = BaseNfcEnabler.this;
                if (intExtra != 4) {
                    z = false;
                }
                baseNfcEnabler.mWirelessCharging = z;
                Log.d(baseNfcEnabler.tag(), "onReceive ACTION_BATTERY_CHANGED pluggedType=" + intExtra);
                BaseNfcEnabler baseNfcEnabler2 = BaseNfcEnabler.this;
                baseNfcEnabler2.onWirelessCharging(baseNfcEnabler2.mWirelessCharging);
            }
        }
    };
    protected boolean mWirelessCharging;

    protected abstract void handleNfcStateChanged(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public void onWirelessCharging(boolean z) {
    }

    protected String tag() {
        return "BaseNfcEnabler";
    }

    public BaseNfcEnabler(Context context) {
        this.mContext = context;
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (!isNfcAvailable()) {
            this.mIntentFilter = null;
            return;
        }
        IntentFilter intentFilter = new IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED");
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
    }

    public void resume() {
        Log.d(tag(), "resume..");
        if (!isNfcAvailable()) {
            return;
        }
        handleNfcStateChanged(this.mNfcAdapter.getAdapterState());
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
    }

    public void pause() {
        if (!isNfcAvailable()) {
            return;
        }
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public boolean isWirelessCharging() {
        return this.mWirelessCharging;
    }

    public boolean isNfcAvailable() {
        return this.mNfcAdapter != null;
    }
}
