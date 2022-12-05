package com.nt.settings.panel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.constraintlayout.widget.R$styleable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.network.NetworkProviderSettings;
import com.android.settings.slices.SliceBuilderUtils;
import com.android.settings.wifi.WifiDialogActivity;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.settings.wifi.details.WifiNetworkDetailsFragment;
import com.android.settings.wifi.slice.WifiSliceItem;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import com.nothing.experience.AppTracking;
import com.nt.settings.panel.WifiItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/* loaded from: classes2.dex */
public class WifiItem implements SettingItemContent, WifiPickerTracker.WifiPickerTrackerCallback, LifecycleOwner, WifiEntry.WifiEntryCallback {
    private final Context mContext;
    private SettingItemData mHeadItem;
    final LifecycleRegistry mLifecycleRegistry;
    private SettingItemLiveData mPinnedHeader;
    private BroadcastReceiver mReceiver;
    protected final WifiManager mWifiManager;
    protected WifiPickerTracker mWifiPickerTracker;
    protected WifiPickerTrackerHelper mWifiPickerTrackerHelper;
    private HandlerThread mWorkHT;
    private Handler mWorkHandler;
    private final List<SettingItemData> mData = new ArrayList();
    private final SettingItemLiveData mLiveData = new SettingItemLiveData();
    private int mCachedWifiState = 1;
    private boolean mIsLastHandled = true;
    private final IntentFilter mIntentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");

    /* loaded from: classes2.dex */
    public class WifiItemBroadcastReceiver extends BroadcastReceiver {
        public WifiItemBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
                WifiItem.this.mCachedWifiState = intent.getIntExtra("wifi_state", 4);
                if (WifiItem.this.mCachedWifiState != 3 && WifiItem.this.mCachedWifiState != 1) {
                    return;
                }
                boolean z = WifiItem.this.mCachedWifiState == 3;
                if (WifiItem.this.mHeadItem == null || z == WifiItem.this.mHeadItem.isChecked) {
                    WifiItem.this.mIsLastHandled = true;
                    return;
                }
                WifiItem.this.mIsLastHandled = false;
                WifiItem.this.mWorkHandler.post(new Runnable() { // from class: com.nt.settings.panel.WifiItem$WifiItemBroadcastReceiver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        WifiItem.WifiItemBroadcastReceiver.this.lambda$onReceive$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            WifiItem wifiItem = WifiItem.this;
            wifiItem.mWifiManager.setWifiEnabled(wifiItem.mHeadItem.isChecked);
        }
    }

    public WifiItem(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
        this.mLifecycleRegistry = lifecycleRegistry;
        WifiPickerTrackerHelper wifiPickerTrackerHelper = new WifiPickerTrackerHelper(lifecycleRegistry, context, this);
        this.mWifiPickerTrackerHelper = wifiPickerTrackerHelper;
        this.mWifiPickerTracker = wifiPickerTrackerHelper.getWifiPickerTracker();
        lifecycleRegistry.markState(Lifecycle.State.INITIALIZED);
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
        this.mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        this.mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
        doLoad();
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onResume() {
        this.mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        this.mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
        if (this.mReceiver == null) {
            WifiItemBroadcastReceiver wifiItemBroadcastReceiver = new WifiItemBroadcastReceiver();
            this.mReceiver = wifiItemBroadcastReceiver;
            this.mContext.registerReceiver(wifiItemBroadcastReceiver, this.mIntentFilter);
        }
        Log.i("WifiItem", "wifi item resume");
        createOrUpdatePinnedHeader();
        doLoad();
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onPause() {
        this.mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
            this.mReceiver = null;
        }
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onCreate() {
        HandlerThread handlerThread = new HandlerThread("WifiItem");
        this.mWorkHT = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new Handler(this.mWorkHT.getLooper());
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onDestroy() {
        this.mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        this.mWorkHT.quit();
    }

    @Override // androidx.lifecycle.LifecycleOwner
    /* renamed from: getLifecycle */
    public Lifecycle mo959getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker.BaseWifiTrackerCallback
    public void onWifiStateChanged() {
        Log.e("WifiItem", "onWifiStateChanged() ");
        doLoad();
    }

    @Override // com.android.wifitrackerlib.WifiPickerTracker.WifiPickerTrackerCallback
    public void onNumSavedSubscriptionsChanged() {
        Log.d("WifiItem", "onNumSavedSubscriptionsChanged() ");
    }

    @Override // com.android.wifitrackerlib.WifiPickerTracker.WifiPickerTrackerCallback
    public void onNumSavedNetworksChanged() {
        Log.d("WifiItem", "onNumSavedNetworksChanged() ");
    }

    @Override // com.android.wifitrackerlib.WifiPickerTracker.WifiPickerTrackerCallback
    public void onWifiEntriesChanged() {
        Log.d("WifiItem", "onWifiEntriesChanged() ");
        doLoad();
    }

    @Override // com.android.wifitrackerlib.WifiEntry.WifiEntryCallback
    public void onUpdated() {
        Log.d("WifiItem", "onUpdated() ");
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public SettingItemLiveData getPinnedHeaderLiveDates() {
        createOrUpdatePinnedHeader();
        return this.mPinnedHeader;
    }

    private void createOrUpdatePinnedHeader() {
        if (this.mPinnedHeader == null) {
            this.mPinnedHeader = new SettingItemLiveData();
        }
        SettingItemData settingItemData = this.mHeadItem;
        if (settingItemData == null) {
            this.mHeadItem = createHeadItem(this.mWifiManager.isWifiEnabled());
        } else {
            settingItemData.isChecked = this.mWifiManager.isWifiEnabled();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mHeadItem);
        this.mPinnedHeader.setDataChange(arrayList);
    }

    private void doLoad() {
        int i;
        SettingItemData settingItemData = this.mHeadItem;
        if ((settingItemData != null && !settingItemData.isChecked) || this.mCachedWifiState != 3 || !this.mWifiManager.isWifiEnabled()) {
            this.mData.clear();
            this.mLiveData.setDataChange(this.mData);
            return;
        }
        ArrayList arrayList = new ArrayList();
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry != null) {
            connectedWifiEntry.setListener(this);
            arrayList.add(getWifiSliceItemRow(new WifiSliceItem(this.mContext, connectedWifiEntry)));
            i = 1;
        } else {
            i = 0;
        }
        List<WifiEntry> wifiEntries = this.mWifiPickerTracker.getWifiEntries();
        for (int i2 = 0; i2 < wifiEntries.size() && i < 8; i2++) {
            arrayList.add(getWifiSliceItemRow(new WifiSliceItem(this.mContext, wifiEntries.get(i2))));
            i++;
        }
        HashSet hashSet = new HashSet(arrayList);
        hashSet.removeAll(this.mData);
        if (hashSet.size() <= 0) {
            return;
        }
        this.mData.clear();
        this.mData.addAll(arrayList);
        this.mLiveData.setDataChange(this.mData);
    }

    private SettingItemData createHeadItem(boolean z) {
        SettingItemData settingItemData = new SettingItemData();
        settingItemData.title = this.mContext.getString(R.string.wifi_settings);
        settingItemData.hasToggle = true;
        settingItemData.canForward = true;
        settingItemData.isChecked = z;
        settingItemData.switchListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.panel.WifiItem$$ExternalSyntheticLambda1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                WifiItem.this.lambda$createHeadItem$0(compoundButton, z2);
            }
        };
        settingItemData.contentClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.WifiItem$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WifiItem.this.lambda$createHeadItem$1(view);
            }
        };
        return settingItemData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createHeadItem$0(CompoundButton compoundButton, boolean z) {
        boolean z2;
        Log.d("WifiItem", "onCheckedChanged " + z);
        int i = this.mCachedWifiState;
        if (i == 4) {
            z2 = this.mWifiManager.isWifiEnabled();
        } else {
            z2 = i == 3;
        }
        if (this.mIsLastHandled && z2 != z) {
            this.mIsLastHandled = false;
            this.mWifiManager.setWifiEnabled(z);
        }
        this.mHeadItem.isChecked = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createHeadItem$1(View view) {
        String charSequence = this.mContext.getText(R.string.wifi_settings).toString();
        this.mContext.startActivity(SliceBuilderUtils.buildSearchResultPageIntent(this.mContext, NetworkProviderSettings.class.getName(), "wifi", charSequence, 603).setClassName(this.mContext.getPackageName(), SubSettings.class.getName()).setData(new Uri.Builder().appendPath("wifi").build()));
    }

    private SettingItemData getWifiSliceItemRow(final WifiSliceItem wifiSliceItem) {
        String title = wifiSliceItem.getTitle();
        Drawable wifiSliceItemLevelIcon = getWifiSliceItemLevelIcon(wifiSliceItem);
        SettingItemData settingItemData = new SettingItemData();
        settingItemData.title = String.valueOf(title);
        settingItemData.subTitle = setSubtitle(wifiSliceItem);
        settingItemData.titleDrawable = wifiSliceItemLevelIcon;
        settingItemData.actionDrawable = getEndIcon(wifiSliceItem);
        settingItemData.contentClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.WifiItem.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WifiItem.this.handleWifiItemClick(wifiSliceItem, WifiNetworkDetailsFragment.class.getName());
            }
        };
        settingItemData.actionClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.WifiItem.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WifiItem.this.handleWifiItemClick(wifiSliceItem, WifiNetworkDetailsFragment.class.getName());
            }
        };
        return settingItemData;
    }

    private String setSubtitle(WifiSliceItem wifiSliceItem) {
        if (wifiSliceItem.getConnectedState() == 1) {
            return this.mContext.getString(R.string.wifi_connecting);
        }
        if (wifiSliceItem.getConnectedState() != 2) {
            return null;
        }
        return this.mContext.getString(R.string.network_connected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWifiItemClick(WifiSliceItem wifiSliceItem, String str) {
        wifiSliceItem.getKey().hashCode();
        if (wifiSliceItem.getConnectedState() != 0) {
            Bundle bundle = new Bundle();
            bundle.putString("key_chosen_wifientry_key", wifiSliceItem.getKey());
            Intent intent = new SubSettingLauncher(this.mContext).setTitleRes(R.string.pref_title_network_details).setDestination(str).setArguments(bundle).setSourceMetricsCategory(R$styleable.Constraint_layout_goneMarginTop).toIntent();
            intent.setFlags(268435456);
            this.mContext.startActivity(intent);
        }
        if (wifiSliceItem.shouldEditBeforeConnect()) {
            this.mContext.startActivity(new Intent(this.mContext, WifiDialogActivity.class).putExtra("key_chosen_wifientry_key", wifiSliceItem.getKey()).setFlags(268435456));
        }
        WifiEntry wifiEntry = getWifiEntry(wifiSliceItem.getKey());
        if (wifiEntry == null) {
            return;
        }
        wifiEntry.connect(new WifiEntryConnectCallback(this.mContext, this.mWifiManager, wifiEntry));
    }

    public WifiEntry getWifiEntry(String str) {
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry == null || !TextUtils.equals(str, connectedWifiEntry.getKey())) {
            for (WifiEntry wifiEntry : this.mWifiPickerTracker.getWifiEntries()) {
                if (TextUtils.equals(str, wifiEntry.getKey())) {
                    return wifiEntry;
                }
            }
            return null;
        }
        return connectedWifiEntry;
    }

    protected Drawable getWifiSliceItemLevelIcon(WifiSliceItem wifiSliceItem) {
        int i;
        int level = wifiSliceItem.getLevel();
        if (level == 0) {
            i = R.drawable.ic_internet_wifi_signal_0;
        } else if (level == 1) {
            i = R.drawable.ic_internet_wifi_signal_1;
        } else if (level == 2) {
            i = R.drawable.ic_internet_wifi_signal_2;
        } else if (level == 3) {
            i = R.drawable.ic_internet_wifi_signal_3;
        } else if (level == 4) {
            i = R.drawable.ic_internet_wifi_signal_4;
        } else {
            i = R.drawable.ic_internet_wifi_signal_0;
        }
        Drawable drawable = this.mContext.getDrawable(i);
        if (wifiSliceItem.getConnectedState() != 0) {
            drawable.setAlpha(255);
        }
        return drawable;
    }

    protected Drawable getEndIcon(WifiSliceItem wifiSliceItem) {
        if (wifiSliceItem.getConnectedState() != 0) {
            return this.mContext.getResources().getDrawable(R.drawable.ic_internet_wifi_entry_setting);
        }
        if (wifiSliceItem.getSecurity() == 0) {
            return null;
        }
        return this.mContext.getResources().getDrawable(R.drawable.ic_internet_wifi_entry_lock);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class WifiEntryConnectCallback implements WifiEntry.ConnectCallback {
        final AppTracking mAppTracker;
        final Context mContext;
        final WifiEntry mWifiEntry;
        final WifiManager mWifiManager;

        WifiEntryConnectCallback(Context context, WifiManager wifiManager, WifiEntry wifiEntry) {
            this.mContext = context;
            this.mWifiEntry = wifiEntry;
            this.mWifiManager = wifiManager;
            this.mAppTracker = AppTracking.getInstance(context);
        }

        @Override // com.android.wifitrackerlib.WifiEntry.ConnectCallback
        public void onConnectResult(int i) {
            if (i == 1) {
                Intent putExtra = new Intent(this.mContext, WifiDialogActivity.class).putExtra("key_chosen_wifientry_key", this.mWifiEntry.getKey());
                putExtra.addFlags(268435456);
                this.mContext.startActivity(putExtra);
            } else if (i != 0) {
            } else {
                logWifiConnectEvent(this.mWifiManager.getConnectionInfo().getWifiStandard());
            }
        }

        private void logWifiConnectEvent(int i) {
            int i2 = i != 4 ? i != 5 ? i != 6 ? 3 : 2 : 1 : 0;
            Bundle bundle = new Bundle();
            bundle.putInt("wifi_connect", i2);
            this.mAppTracker.logProductEvent("OS_Network", bundle);
        }
    }
}
