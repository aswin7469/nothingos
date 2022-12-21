package com.nothing.systemui.p024qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyChipEvent;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DateView;
import com.android.systemui.util.ViewController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.privacy.PrivacyDialogControllerEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/* renamed from: com.nothing.systemui.qs.NTQSStatusBarController */
public class NTQSStatusBarController extends ViewController<NTQSStatusBar> {
    private static final String ACTION_MIC_MODE = "android.intent.action.MIC_MODE_UPDATE_UI";
    private static final String TAG = "NTQSStatusBarController";
    private BatteryMeterViewController mBatteryMeterViewController;
    private BatteryMeterView mBatteryRemainingIcon;
    private final String mCameraSlot;
    private boolean mChipVisible;
    /* access modifiers changed from: private */
    public Clock mClock;
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onDensityOrFontScaleChanged() {
            NTQSStatusBarController.this.mClock.onDensityOrFontScaleChanged();
            FontSizeUtils.updateFontSize(NTQSStatusBarController.this.mDate, C1893R.dimen.status_bar_clock_size);
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public DateView mDate;
    private FeatureFlags mFeatureFlags;
    private StatusIconContainer mIconContainer;
    private StatusBarIconController.TintedIconManager mIconManager;
    private ArrayList<QSSControllerListener> mListeners = null;
    private boolean mListening;
    /* access modifiers changed from: private */
    public boolean mLocationIndicatorsEnabled;
    private final String mLocationSlot;
    /* access modifiers changed from: private */
    public boolean mMicCameraIndicatorsEnabled;
    /* access modifiers changed from: private */
    public ArrayMap<String, PrivacyDialogControllerEx.MicModeInfo> mMicModeInfoList = new ArrayMap<>();
    private BroadcastReceiver mMicReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (NTQSStatusBarController.ACTION_MIC_MODE.equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra(ZoneGetter.KEY_DISPLAYNAME);
                boolean booleanExtra = intent.getBooleanExtra("show_ui", false);
                int intExtra = intent.getIntExtra("mic_mode", 0);
                boolean booleanExtra2 = intent.getBooleanExtra("speaker_mode", false);
                NTLogUtil.m1682i(NTQSStatusBarController.TAG, "pkg = " + stringExtra + ", showUI = " + booleanExtra + ",mic_mode = " + intExtra + ",speake_mode = " + booleanExtra2);
                if (!booleanExtra || TextUtils.isEmpty(stringExtra)) {
                    NTQSStatusBarController.this.mMicModeInfoList.remove(stringExtra);
                } else {
                    NTQSStatusBarController.this.mMicModeInfoList.put(stringExtra, new PrivacyDialogControllerEx.MicModeInfo(true, intExtra, booleanExtra2));
                }
            }
        }
    };
    private final String mMicSlot;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            NTQSStatusBarController.this.mUiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_CLICK);
            ((PrivacyDialogControllerEx) NTDependencyEx.get(PrivacyDialogControllerEx.class)).setMicModeInfo(NTQSStatusBarController.this.mMicModeInfoList);
            NTQSStatusBarController.this.mPrivacyDialogController.showDialog(NTQSStatusBarController.this.mContext);
        }
    };
    private PrivacyItemController.Callback mPICCallback = new PrivacyItemController.Callback() {
        List<PrivacyItem> privacyItems;

        public void onPrivacyItemsChanged(List<PrivacyItem> list) {
            this.privacyItems = list;
            NTQSStatusBarController.this.mPrivacyChip.setPrivacyList(list);
            NTQSStatusBarController.this.setChipVisibility(!list.isEmpty());
            if (list.size() == 0) {
                NTQSStatusBarController.this.mMicModeInfoList.clear();
            }
        }

        public void onFlagMicCameraChanged(boolean z) {
            if (NTQSStatusBarController.this.mMicCameraIndicatorsEnabled != z) {
                boolean unused = NTQSStatusBarController.this.mMicCameraIndicatorsEnabled = z;
                update();
            }
        }

        public void onFlagLocationChanged(boolean z) {
            if (NTQSStatusBarController.this.mLocationIndicatorsEnabled != z) {
                boolean unused = NTQSStatusBarController.this.mLocationIndicatorsEnabled = z;
                update();
            }
        }

        private void update() {
            NTQSStatusBarController.this.updatePrivacyIconSlots();
            NTQSStatusBarController.this.setChipVisibility(!this.privacyItems.isEmpty());
        }
    };
    /* access modifiers changed from: private */
    public OngoingPrivacyChip mPrivacyChip;
    private boolean mPrivacyChipLogged;
    /* access modifiers changed from: private */
    public final PrivacyDialogController mPrivacyDialogController;
    private final PrivacyItemController mPrivacyItemController;
    private final PrivacyLogger mPrivacyLogger;
    private StatusBarIconController mStatusBarIconController;
    /* access modifiers changed from: private */
    public final UiEventLogger mUiEventLogger;

    /* renamed from: com.nothing.systemui.qs.NTQSStatusBarController$QSSControllerListener */
    interface QSSControllerListener {
        void addIgnoredSlot(String... strArr);

        void removeIgnoredSlot(String... strArr);

        void setOnClickListener(View.OnClickListener onClickListener);

        void setPrivacyList(List<PrivacyItem> list);

        void setVisibility(int i);
    }

    /* access modifiers changed from: protected */
    public void onInit() {
    }

    @Inject
    NTQSStatusBarController(Context context, NTQSStatusBar nTQSStatusBar, PrivacyDialogController privacyDialogController, PrivacyItemController privacyItemController, UiEventLogger uiEventLogger, PrivacyLogger privacyLogger, ConfigurationController configurationController, @Named("nt_qs_header_battery_controller") BatteryMeterViewController batteryMeterViewController) {
        super(nTQSStatusBar);
        this.mContext = context;
        this.mBatteryMeterViewController = batteryMeterViewController;
        batteryMeterViewController.ignoreTunerUpdates();
        this.mBatteryRemainingIcon = (BatteryMeterView) ((NTQSStatusBar) this.mView).findViewById(C1893R.C1897id.qs_batteryRemainingIcon);
        this.mClock = (Clock) ((NTQSStatusBar) this.mView).findViewById(C1893R.C1897id.qs_clock);
        this.mDate = (DateView) ((NTQSStatusBar) this.mView).findViewById(C1893R.C1897id.qs_date);
        this.mPrivacyDialogController = privacyDialogController;
        this.mPrivacyItemController = privacyItemController;
        OngoingPrivacyChip ongoingPrivacyChip = (OngoingPrivacyChip) ((NTQSStatusBar) this.mView).findViewById(C1893R.C1897id.privacy_chip);
        this.mPrivacyChip = ongoingPrivacyChip;
        ongoingPrivacyChip.setTag(context.getString(C1893R.string.nt_qs_header_privacy_icons));
        this.mConfigurationController = configurationController;
        this.mIconContainer = (StatusIconContainer) ((NTQSStatusBar) this.mView).findViewById(C1893R.C1897id.qs_statusIcons);
        this.mFeatureFlags = (FeatureFlags) Dependency.get(FeatureFlags.class);
        this.mIconManager = new StatusBarIconController.TintedIconManager(this.mIconContainer, this.mFeatureFlags);
        this.mStatusBarIconController = (StatusBarIconController) Dependency.get(StatusBarIconController.class);
        this.mIconManager.setTint(-1);
        this.mUiEventLogger = uiEventLogger;
        this.mPrivacyLogger = privacyLogger;
        this.mCameraSlot = context.getResources().getString(17041558);
        this.mMicSlot = context.getResources().getString(17041570);
        this.mLocationSlot = context.getResources().getString(17041568);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mStatusBarIconController.addIconGroup(this.mIconManager);
        this.mPrivacyChip.setOnClickListener(this.mOnClickListener);
        this.mMicCameraIndicatorsEnabled = this.mPrivacyItemController.getMicCameraAvailable();
        this.mLocationIndicatorsEnabled = this.mPrivacyItemController.getLocationAvailable();
        this.mPrivacyItemController.addCallback(this.mPICCallback);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MIC_MODE);
        this.mContext.registerReceiver(this.mMicReceiver, intentFilter);
        this.mMicCameraIndicatorsEnabled = this.mPrivacyItemController.getMicCameraAvailable();
        this.mLocationIndicatorsEnabled = this.mPrivacyItemController.getLocationAvailable();
        updatePrivacyIconSlots();
        setChipVisibility(this.mChipVisible);
        this.mBatteryMeterViewController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mStatusBarIconController.removeIconGroup(this.mIconManager);
        this.mPrivacyChip.setOnClickListener((View.OnClickListener) null);
        this.mPrivacyItemController.removeCallback(this.mPICCallback);
        this.mContext.unregisterReceiver(this.mMicReceiver);
    }

    /* access modifiers changed from: private */
    public void setChipVisibility(boolean z) {
        this.mChipVisible = z;
        int i = 0;
        if (!z || !getChipEnabled()) {
            this.mPrivacyLogger.logChipVisible(false);
        } else {
            this.mPrivacyLogger.logChipVisible(true);
            if (!this.mPrivacyChipLogged && this.mListening) {
                this.mPrivacyChipLogged = true;
                this.mUiEventLogger.log(PrivacyChipEvent.ONGOING_INDICATORS_CHIP_VIEW);
            }
        }
        OngoingPrivacyChip ongoingPrivacyChip = this.mPrivacyChip;
        if (!z) {
            i = 8;
        }
        ongoingPrivacyChip.setVisibility(i);
        NTLogUtil.m1682i(TAG, "setChipVisibility " + this.mChipVisible);
        if (z) {
            this.mIconContainer.setMaxIconsToDisplay(5);
            this.mIconContainer.requestLayout();
            return;
        }
        this.mIconContainer.setMaxIconsToDisplay(7);
        this.mIconContainer.requestLayout();
    }

    public void updatePrivacyIconSlots() {
        if (getChipEnabled()) {
            if (this.mMicCameraIndicatorsEnabled) {
                this.mIconContainer.addIgnoredSlot(this.mCameraSlot);
                this.mIconContainer.addIgnoredSlot(this.mMicSlot);
            } else {
                this.mIconContainer.removeIgnoredSlot(this.mCameraSlot);
                this.mIconContainer.removeIgnoredSlot(this.mMicSlot);
            }
            if (this.mLocationIndicatorsEnabled) {
                this.mIconContainer.addIgnoredSlot(this.mLocationSlot);
            } else {
                this.mIconContainer.removeIgnoredSlot(this.mLocationSlot);
            }
        } else {
            this.mIconContainer.removeIgnoredSlot(this.mCameraSlot);
            this.mIconContainer.removeIgnoredSlot(this.mMicSlot);
            this.mIconContainer.removeIgnoredSlot(this.mLocationSlot);
        }
    }

    private boolean getChipEnabled() {
        return this.mMicCameraIndicatorsEnabled || this.mLocationIndicatorsEnabled;
    }

    private void addListener(QSSControllerListener qSSControllerListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(qSSControllerListener);
    }

    private void removeListener(QSSControllerListener qSSControllerListener) {
        ArrayList<QSSControllerListener> arrayList = this.mListeners;
        if (arrayList != null) {
            arrayList.remove((Object) qSSControllerListener);
            if (this.mListeners.size() == 0) {
                this.mListeners = null;
            }
        }
    }
}
