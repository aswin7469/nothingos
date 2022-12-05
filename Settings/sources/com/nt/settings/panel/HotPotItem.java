package com.nt.settings.panel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.WifiDisplayStatus;
import android.net.ConnectivityManager;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiClient;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import com.android.settings.R;
import com.android.settings.wifi.tether.WifiTetherSoftApManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class HotPotItem implements SettingItemContent {
    private static final IntentFilter WIFI_INTENT_FILTER = new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED");
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final DisplayManager mDisplayManager;
    private SettingItemData mHotpotData;
    private int mSoftApState;
    private String mSubTitle;
    private final WifiManager mWifiManager;
    private WifiTetherSoftApManager mWifiTetherSoftApManager;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.nt.settings.panel.HotPotItem.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())) {
                HotPotItem.this.handleWifiApStateChanged(intent.getIntExtra("wifi_state", 14));
            }
        }
    };
    private ConnectivityManager.OnStartTetheringCallback mOnStartTetheringCallback = new ConnectivityManager.OnStartTetheringCallback() { // from class: com.nt.settings.panel.HotPotItem.2
        public void onTetheringFailed() {
            super.onTetheringFailed();
        }
    };
    private final List<SettingItemData> mData = new ArrayList();
    private final SettingItemLiveData mLiveData = new SettingItemLiveData();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public HotPotItem(Context context) {
        this.mContext = context;
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        initWifiTetherSoftApManager();
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public List<SettingItemData> getDates() {
        return this.mData;
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public SettingItemLiveData getLiveDates() {
        return this.mLiveData;
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void loadData() {
        if (this.mHotpotData == null) {
            SettingItemData settingItemData = new SettingItemData();
            this.mHotpotData = settingItemData;
            settingItemData.switchListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.panel.HotPotItem$$ExternalSyntheticLambda1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    HotPotItem.this.lambda$loadData$0(compoundButton, z);
                }
            };
            settingItemData.contentClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.HotPotItem$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HotPotItem.this.lambda$loadData$1(view);
                }
            };
            this.mData.clear();
            this.mData.add(this.mHotpotData);
        }
        this.mHotpotData.title = getTitle();
        this.mHotpotData.subTitle = getSummary();
        this.mHotpotData.isChecked = isHotSpotEnabled();
        this.mHotpotData.titleDrawable = getTitleDrawable();
        SettingItemData settingItemData2 = this.mHotpotData;
        settingItemData2.canForward = true;
        settingItemData2.hasToggle = true;
        settingItemData2.isForceUpdate = true;
        this.mLiveData.setDataChange(this.mData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$0(CompoundButton compoundButton, boolean z) {
        Log.d("HotPotItem", "onCheckedChanged " + z);
        WifiDisplayStatus wifiDisplayStatus = this.mDisplayManager.getWifiDisplayStatus();
        if (wifiDisplayStatus.getScanState() == 1) {
            this.mDisplayManager.stopWifiDisplayScan();
        }
        if (wifiDisplayStatus.getActiveDisplayState() == 1) {
            this.mDisplayManager.disconnectWifiDisplay();
        }
        if (z) {
            startTether();
        } else {
            stopTether();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$1(View view) {
        Intent intent = new Intent("android.settings.TETHER_SETTINGS");
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onStart() {
        Log.d("HotPotItem", "HotPotItem onStart");
        updateState();
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onStop() {
        Log.d("HotPotItem", "HotPotItem onStop");
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onCreate() {
        WifiTetherSoftApManager wifiTetherSoftApManager = this.mWifiTetherSoftApManager;
        if (wifiTetherSoftApManager != null) {
            wifiTetherSoftApManager.registerSoftApCallback();
        }
        Log.d("HotPotItem", "HotPotItem onCreate");
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onDestroy() {
        WifiTetherSoftApManager wifiTetherSoftApManager = this.mWifiTetherSoftApManager;
        if (wifiTetherSoftApManager != null) {
            wifiTetherSoftApManager.unRegisterSoftApCallback();
        }
        Log.d("HotPotItem", "HotPotItem onDestroy");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateState() {
        loadData();
    }

    private String getSummary() {
        return this.mSubTitle;
    }

    private String getTitle() {
        SoftApConfiguration softApConfiguration = this.mWifiManager.getSoftApConfiguration();
        return softApConfiguration != null ? softApConfiguration.getSsid() : "AndroidAP";
    }

    private Drawable getTitleDrawable() {
        return this.mContext.getResources().getDrawable(this.mHotpotData.isChecked ? R.drawable.ic_internet_wifi_tether_enabled : R.drawable.ic_internet_wifi_tether_disabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWifiApStateChanged(int i) {
        if (i == 11) {
            this.mSubTitle = this.mContext.getResources().getString(R.string.nt_wifi_hotspot_off_subtext);
            updateState();
        } else if (i != 13) {
        } else {
            this.mWifiManager.getSoftApConfiguration();
            this.mSubTitle = this.mContext.getResources().getQuantityString(R.plurals.wifi_tether_connected_summary, 0, 0);
            updateState();
        }
    }

    public boolean isHotSpotEnabled() {
        return this.mWifiManager.getWifiApState() == 13;
    }

    public void stopTether() {
        this.mConnectivityManager.stopTethering(0);
    }

    public void startTether() {
        this.mConnectivityManager.startTethering(0, true, this.mOnStartTetheringCallback, this.mHandler);
    }

    void initWifiTetherSoftApManager() {
        this.mWifiTetherSoftApManager = new WifiTetherSoftApManager(this.mWifiManager, new WifiTetherSoftApManager.WifiTetherSoftApCallback() { // from class: com.nt.settings.panel.HotPotItem.3
            @Override // com.android.settings.wifi.tether.WifiTetherSoftApManager.WifiTetherSoftApCallback
            public void onStateChanged(int i, int i2) {
                HotPotItem.this.mSoftApState = i;
                HotPotItem.this.handleWifiApStateChanged(i);
            }

            @Override // com.android.settings.wifi.tether.WifiTetherSoftApManager.WifiTetherSoftApCallback
            public void onConnectedClientsChanged(List<WifiClient> list) {
                if (HotPotItem.this.mSoftApState == 13) {
                    String str = HotPotItem.this.mWifiManager.isExtendingWifi() ? "Extending Wifi-Coverage: " : "";
                    HotPotItem hotPotItem = HotPotItem.this;
                    hotPotItem.mSubTitle = str + HotPotItem.this.mContext.getResources().getQuantityString(R.plurals.wifi_tether_connected_summary, list.size(), Integer.valueOf(list.size()));
                    HotPotItem.this.updateState();
                }
            }
        });
    }
}
