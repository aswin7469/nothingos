package com.android.systemui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.SparseBooleanArray;
import com.android.systemui.broadcast.BroadcastDispatcher;
/* loaded from: classes2.dex */
public class CarrierConfigTracker extends BroadcastReceiver {
    private final CarrierConfigManager mCarrierConfigManager;
    private boolean mDefaultCallStrengthConfig;
    private boolean mDefaultCallStrengthConfigLoaded;
    private boolean mDefaultCarrierProvisionsWifiMergedNetworks;
    private boolean mDefaultCarrierProvisionsWifiMergedNetworksLoaded;
    private boolean mDefaultNoCallingConfig;
    private boolean mDefaultNoCallingConfigLoaded;
    private final SparseBooleanArray mCallStrengthConfigs = new SparseBooleanArray();
    private final SparseBooleanArray mNoCallingConfigs = new SparseBooleanArray();
    private final SparseBooleanArray mCarrierProvisionsWifiMergedNetworks = new SparseBooleanArray();

    public CarrierConfigTracker(Context context, BroadcastDispatcher broadcastDispatcher) {
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        broadcastDispatcher.registerReceiver(this, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        PersistableBundle configForSubId;
        if (!"android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction())) {
            return;
        }
        int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
        if (!SubscriptionManager.isValidSubscriptionId(intExtra) || (configForSubId = this.mCarrierConfigManager.getConfigForSubId(intExtra)) == null) {
            return;
        }
        synchronized (this.mCallStrengthConfigs) {
            this.mCallStrengthConfigs.put(intExtra, configForSubId.getBoolean("display_call_strength_indicator_bool"));
        }
        synchronized (this.mNoCallingConfigs) {
            this.mNoCallingConfigs.put(intExtra, configForSubId.getBoolean("use_ip_for_calling_indicator_bool"));
        }
        synchronized (this.mCarrierProvisionsWifiMergedNetworks) {
            this.mCarrierProvisionsWifiMergedNetworks.put(intExtra, configForSubId.getBoolean("carrier_provisions_wifi_merged_networks_bool"));
        }
    }

    public boolean getCallStrengthConfig(int i) {
        synchronized (this.mCallStrengthConfigs) {
            if (this.mCallStrengthConfigs.indexOfKey(i) >= 0) {
                return this.mCallStrengthConfigs.get(i);
            }
            if (!this.mDefaultCallStrengthConfigLoaded) {
                this.mDefaultCallStrengthConfig = CarrierConfigManager.getDefaultConfig().getBoolean("display_call_strength_indicator_bool");
                this.mDefaultCallStrengthConfigLoaded = true;
            }
            return this.mDefaultCallStrengthConfig;
        }
    }

    public boolean getNoCallingConfig(int i) {
        synchronized (this.mNoCallingConfigs) {
            if (this.mNoCallingConfigs.indexOfKey(i) >= 0) {
                return this.mNoCallingConfigs.get(i);
            }
            if (!this.mDefaultNoCallingConfigLoaded) {
                this.mDefaultNoCallingConfig = CarrierConfigManager.getDefaultConfig().getBoolean("use_ip_for_calling_indicator_bool");
                this.mDefaultNoCallingConfigLoaded = true;
            }
            return this.mDefaultNoCallingConfig;
        }
    }

    public boolean getCarrierProvisionsWifiMergedNetworksBool(int i) {
        synchronized (this.mCarrierProvisionsWifiMergedNetworks) {
            if (this.mCarrierProvisionsWifiMergedNetworks.indexOfKey(i) >= 0) {
                return this.mCarrierProvisionsWifiMergedNetworks.get(i);
            }
            if (!this.mDefaultCarrierProvisionsWifiMergedNetworksLoaded) {
                this.mDefaultCarrierProvisionsWifiMergedNetworks = CarrierConfigManager.getDefaultConfig().getBoolean("carrier_provisions_wifi_merged_networks_bool");
                this.mDefaultCarrierProvisionsWifiMergedNetworksLoaded = true;
            }
            return this.mDefaultCarrierProvisionsWifiMergedNetworks;
        }
    }
}
