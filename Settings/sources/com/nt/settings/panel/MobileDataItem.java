package com.nt.settings.panel;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.telephony.MobileNetworkActivity;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.net.DataUsageController;
import com.android.settingslib.net.DataUsageUtils;
import com.android.settingslib.net.SignalStrengthUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes2.dex */
public class MobileDataItem implements SettingItemContent {
    private String TAG = "MobileDataItem";
    private Context mContext;
    private List<SettingItemData> mData;
    private boolean mIsMobileDataAvailable;
    private SettingItemLiveData mLiveData;
    private SettingItemData mMobileData;
    private final SubscriptionManager mSubscriptionManager;
    private String mSummary;
    private TelephonyCallback mTelephonyCallback;
    private final TelephonyManager mTelephonyManager;
    private HandlerThread mWorkHT;
    private Handler mWorkHandler;

    public MobileDataItem(Context context) {
        this.mContext = context;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mSubscriptionManager = subscriptionManager;
        int defaultSubscriptionId = getDefaultSubscriptionId(subscriptionManager);
        if (-1 != defaultSubscriptionId) {
            this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(defaultSubscriptionId);
        } else {
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        }
        this.mData = new ArrayList();
        this.mLiveData = new SettingItemLiveData();
    }

    protected static int getDefaultSubscriptionId(SubscriptionManager subscriptionManager) {
        SubscriptionInfo activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        if (activeSubscriptionInfo == null) {
            return -1;
        }
        return activeSubscriptionInfo.getSubscriptionId();
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
        Log.d(this.TAG, "loadData ");
        this.mContext.getMainThreadHandler().post(new Runnable() { // from class: com.nt.settings.panel.MobileDataItem$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MobileDataItem.this.lambda$loadData$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$3() {
        final int defaultSubscriptionId = getDefaultSubscriptionId(this.mSubscriptionManager);
        boolean isMobileDataEnabled = isMobileDataEnabled();
        SettingItemData settingItemData = this.mMobileData;
        if (settingItemData == null) {
            this.mIsMobileDataAvailable = isMobileDataAvailable();
            SettingItemData settingItemData2 = new SettingItemData();
            this.mMobileData = settingItemData2;
            if (defaultSubscriptionId == -1) {
                settingItemData2.hasToggle = false;
            } else {
                settingItemData2.hasToggle = true;
            }
            settingItemData2.canForward = true;
            settingItemData2.title = getTitle().toString();
            this.mMobileData.isChecked = isMobileDataEnabled;
            this.mSummary = getSummary(this.mContext, defaultSubscriptionId, isMobileDataEnabled);
            String str = this.TAG;
            Log.d(str, "if loadData isChecked = " + this.mMobileData.isChecked);
            SettingItemData settingItemData3 = this.mMobileData;
            settingItemData3.switchListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.panel.MobileDataItem$$ExternalSyntheticLambda1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    MobileDataItem.this.lambda$loadData$1(defaultSubscriptionId, compoundButton, z);
                }
            };
            settingItemData3.contentClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.MobileDataItem$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MobileDataItem.this.lambda$loadData$2(view);
                }
            };
        } else {
            if (defaultSubscriptionId == -1) {
                settingItemData.hasToggle = false;
            } else {
                settingItemData.hasToggle = true;
            }
            settingItemData.isChecked = isMobileDataEnabled;
            String str2 = this.TAG;
            Log.d(str2, "else loadData isChecked = " + this.mMobileData.isChecked);
        }
        if (isAirplaneModeEnabled()) {
            this.mData.clear();
            this.mLiveData.setDataChange(this.mData);
        } else if (!this.mIsMobileDataAvailable) {
            this.mData.clear();
            this.mLiveData.setDataChange(this.mData);
        } else {
            this.mMobileData.titleDrawable = getTitleDrawable();
            if (!Utils.isSettingsIntelligence(this.mContext)) {
                this.mMobileData.title = getTitle().toString();
                this.mMobileData.subTitle = this.mSummary;
            }
            if (this.mData.isEmpty()) {
                SettingItemData settingItemData4 = this.mMobileData;
                settingItemData4.isForceUpdate = true;
                this.mData.add(settingItemData4);
            }
            this.mLiveData.setDataChange(this.mData);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$1(final int i, CompoundButton compoundButton, final boolean z) {
        String str = this.TAG;
        Log.e(str, "onCheckedChanged " + z);
        this.mWorkHandler.post(new Runnable() { // from class: com.nt.settings.panel.MobileDataItem$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MobileDataItem.this.lambda$loadData$0(i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$0(int i, boolean z) {
        MobileNetworkUtils.setMobileDataEnabled(this.mContext, i, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$2(View view) {
        Intent intent = new Intent(this.mContext, MobileNetworkActivity.class);
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onResume() {
        Log.d(this.TAG, "onResume ");
        SubscriptionManager subscriptionManager = this.mSubscriptionManager;
        Handler handler = this.mWorkHandler;
        Objects.requireNonNull(handler);
        subscriptionManager.addOnSubscriptionsChangedListener(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.nt.settings.panel.MobileDataItem.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                if (MobileDataItem.this.mMobileData != null) {
                    MobileDataItem.this.mMobileData.isForceUpdate = true;
                    MobileDataItem mobileDataItem = MobileDataItem.this;
                    mobileDataItem.mIsMobileDataAvailable = mobileDataItem.isMobileDataAvailable();
                }
                MobileDataItem.this.loadData();
            }
        });
        TelephonyManager telephonyManager = this.mTelephonyManager;
        Handler handler2 = this.mWorkHandler;
        Objects.requireNonNull(handler2);
        telephonyManager.registerTelephonyCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler2), this.mTelephonyCallback);
        SettingItemData settingItemData = this.mMobileData;
        if (settingItemData != null) {
            settingItemData.isForceUpdate = true;
        }
        loadData();
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onPause() {
        Log.d(this.TAG, "onPause ");
        this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onCreate() {
        Log.d(this.TAG, "onCreate ");
        HandlerThread handlerThread = new HandlerThread("MobileDataItem");
        this.mWorkHT = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new Handler(this.mWorkHT.getLooper());
        this.mTelephonyCallback = new NetworkProviderTelephonyCallback();
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onDestroy() {
        Log.d(this.TAG, "onDestroy ");
        this.mWorkHT.quit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMobileDataAvailable() {
        List<SubscriptionInfo> selectableSubscriptionInfoList = SubscriptionUtil.getSelectableSubscriptionInfoList(this.mContext);
        return selectableSubscriptionInfoList != null && !selectableSubscriptionInfoList.isEmpty();
    }

    private boolean isAirplaneModeEnabled() {
        return WirelessUtils.isAirplaneModeOn(this.mContext);
    }

    private CharSequence getTitle() {
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        return activeSubscriptionInfo == null ? "" : SubscriptionUtil.getUniqueSubscriptionDisplayName(activeSubscriptionInfo, this.mContext);
    }

    private Drawable getTitleDrawable() {
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        SignalStrength signalStrength = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(defaultDataSubscriptionId).getSignalStrength();
        int level = signalStrength == null ? 0 : signalStrength.getLevel();
        int i = 5;
        if (SignalStrengthUtil.shouldInflateSignalStrength(this.mContext, defaultDataSubscriptionId)) {
            level++;
            i = 6;
        }
        return MobileNetworkUtils.getSignalStrengthIcon(this.mContext, level, i, 0, false);
    }

    boolean isMobileDataEnabled() {
        if (this.mTelephonyManager == null || getDefaultSubscriptionId(this.mSubscriptionManager) == -1) {
            return false;
        }
        return this.mTelephonyManager.isDataEnabled();
    }

    /* loaded from: classes2.dex */
    class NetworkProviderTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.DataEnabledListener {
        NetworkProviderTelephonyCallback() {
        }

        public void onDataEnabledChanged(boolean z, int i) {
            if (MobileDataItem.this.mMobileData != null) {
                MobileDataItem.this.mMobileData.isChecked = z;
                MobileDataItem.this.mMobileData.isForceUpdate = true;
            }
            MobileDataItem mobileDataItem = MobileDataItem.this;
            mobileDataItem.mSummary = mobileDataItem.getSummary(mobileDataItem.mContext, MobileDataItem.getDefaultSubscriptionId(MobileDataItem.this.mSubscriptionManager), z);
            MobileDataItem.this.loadData();
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            if (MobileDataItem.this.mMobileData != null) {
                MobileDataItem.this.mMobileData.isForceUpdate = true;
                MobileDataItem mobileDataItem = MobileDataItem.this;
                mobileDataItem.mSummary = mobileDataItem.getSummary(mobileDataItem.mContext, MobileDataItem.getDefaultSubscriptionId(MobileDataItem.this.mSubscriptionManager), MobileDataItem.this.isMobileDataEnabled());
            }
            MobileDataItem.this.loadData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSummary(Context context, int i, boolean z) {
        if (z) {
            DataUsageController.DataUsageInfo dataUsageInfo = new DataUsageController(context).getDataUsageInfo(DataUsageUtils.getMobileTemplate(context, i));
            long j = dataUsageInfo.warningLevel;
            if (j == 0) {
                return this.mContext.getString(R.string.nt_network_panel_no_data_warning_summary);
            }
            return this.mContext.getString(R.string.nt_network_panel_mobile_item_summary, com.android.settings.datausage.DataUsageUtils.formatDataUsage(context, j - dataUsageInfo.usageLevel).toString());
        }
        return this.mContext.getString(R.string.mobile_data_off_summary);
    }
}
