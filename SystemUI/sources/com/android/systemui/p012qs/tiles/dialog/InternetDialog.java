package com.android.systemui.p012qs.tiles.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;
import com.android.systemui.C1893R;
import com.android.systemui.Prefs;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wifitrackerlib.WifiEntry;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSFragmentEx;
import com.nothing.systemui.p024qs.tiles.InternetTileEx;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

@SysUISingleton
/* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialog */
public class InternetDialog extends SystemUIDialog implements InternetDialogController.InternetDialogCallback, Window.Callback {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final int MAX_NETWORK_COUNT = 50;
    static final long PROGRESS_DELAY_MS = 1500;
    private static final String TAG = "InternetDialog";
    protected InternetAdapter mAdapter;
    private Button mAirplaneModeButton;
    private TextView mAirplaneModeSummaryText;
    private AlertDialog mAlertDialog;
    private final Executor mBackgroundExecutor;
    private Drawable mBackgroundOff = null;
    private Drawable mBackgroundOn;
    private boolean mCanChangeWifiState;
    private boolean mCanConfigMobileData;
    protected boolean mCanConfigWifi;
    private LinearLayout mConnectedWifListLayout;
    protected WifiEntry mConnectedWifiEntry;
    private ImageView mConnectedWifiIcon;
    private TextView mConnectedWifiSummaryText;
    private TextView mConnectedWifiTitleText;
    private Context mContext;
    private int mDefaultDataSubId = -1;
    protected View mDialogView;
    private View mDivider;
    private Button mDoneButton;
    private LinearLayout mEthernetLayout;
    private final Handler mHandler;
    protected boolean mHasMoreWifiEntries;
    protected final Runnable mHideProgressBarRunnable = new InternetDialog$$ExternalSyntheticLambda8(this);
    protected Runnable mHideSearchingRunnable = new InternetDialog$$ExternalSyntheticLambda9(this);
    private InternetDialogController mInternetDialogController;
    private InternetDialogFactory mInternetDialogFactory;
    private LinearLayout mInternetDialogLayout;
    private TextView mInternetDialogSubTitle;
    private TextView mInternetDialogTitle;
    private InternetTileEx mInternetTileEx = ((InternetTileEx) NTDependencyEx.get(InternetTileEx.class));
    protected boolean mIsProgressBarVisible;
    protected boolean mIsSearchingHidden;
    private KeyguardStateController mKeyguard;
    private Switch mMobileDataToggle;
    private LinearLayout mMobileNetworkLayout;
    private TextView mMobileSummaryText;
    private LinearLayout mMobileTitleLayout;
    private TextView mMobileTitleText;
    private View mMobileToggleDivider;
    private ProgressBar mProgressBar;
    private LinearLayout mSeeAllLayout;
    private ImageView mSignalIcon;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private LinearLayout mTurnWifiOnLayout;
    private UiEventLogger mUiEventLogger;
    private Switch mWiFiToggle;
    protected int mWifiEntriesCount;
    private int mWifiNetworkHeight;
    private RecyclerView mWifiRecyclerView;
    private LinearLayout mWifiScanNotifyLayout;
    private TextView mWifiScanNotifyText;
    private ImageView mWifiSettingsIcon;
    private TextView mWifiToggleTitleText;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-tiles-dialog-InternetDialog  reason: not valid java name */
    public /* synthetic */ void m2987lambda$new$0$comandroidsystemuiqstilesdialogInternetDialog() {
        setProgressBarVisible(false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-qs-tiles-dialog-InternetDialog  reason: not valid java name */
    public /* synthetic */ void m2988lambda$new$1$comandroidsystemuiqstilesdialogInternetDialog() {
        this.mIsSearchingHidden = true;
        this.mInternetDialogSubTitle.setText(getSubtitleText());
    }

    public InternetDialog(Context context, InternetDialogFactory internetDialogFactory, InternetDialogController internetDialogController, boolean z, boolean z2, boolean z3, UiEventLogger uiEventLogger, @Main Handler handler, @Background Executor executor, KeyguardStateController keyguardStateController) {
        super(((QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class)).getQSFragment().getView().getContext());
        if (DEBUG) {
            Log.d(TAG, "Init InternetDialog");
        }
        this.mContext = getContext();
        this.mHandler = handler;
        this.mBackgroundExecutor = executor;
        this.mInternetDialogFactory = internetDialogFactory;
        this.mInternetDialogController = internetDialogController;
        this.mSubscriptionManager = internetDialogController.getSubscriptionManager();
        this.mDefaultDataSubId = this.mInternetDialogController.getDefaultDataSubscriptionId();
        this.mTelephonyManager = this.mInternetDialogController.getTelephonyManager();
        this.mCanConfigMobileData = z;
        this.mCanConfigWifi = z2;
        this.mCanChangeWifiState = WifiEnterpriseRestrictionUtils.isChangeWifiStateAllowed(context);
        this.mKeyguard = keyguardStateController;
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new InternetAdapter(this.mInternetDialogController);
        if (!z3) {
            getWindow().setType(2038);
        }
    }

    /* JADX INFO: finally extract failed */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (DEBUG) {
            Log.d(TAG, "onCreate");
        }
        this.mUiEventLogger.log(InternetDialogEvent.INTERNET_DIALOG_SHOW);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(C1893R.layout.internet_connectivity_dialog, (ViewGroup) null);
        Window window = getWindow();
        window.setContentView(this.mDialogView);
        window.setWindowAnimations(C1893R.style.Animation_InternetDialog);
        window.setBackgroundDrawable(this.mContext.getResources().getDrawable(C1893R.C1895drawable.nt_settings_panel_rounded_top_corner_background));
        this.mInternetTileEx.updateWindowSize(this.mContext, window, this.mInternetDialogController, true);
        this.mWifiNetworkHeight = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.internet_dialog_wifi_network_height);
        this.mInternetDialogLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.internet_connectivity_dialog);
        this.mInternetDialogTitle = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.internet_dialog_title);
        this.mInternetDialogSubTitle = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.internet_dialog_subtitle);
        this.mDivider = this.mDialogView.requireViewById(C1893R.C1897id.divider);
        this.mProgressBar = (ProgressBar) this.mDialogView.requireViewById(C1893R.C1897id.wifi_searching_progress);
        this.mEthernetLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.ethernet_layout);
        this.mMobileNetworkLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.mobile_network_layout);
        this.mTurnWifiOnLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.turn_on_wifi_layout);
        this.mWifiToggleTitleText = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_toggle_title);
        this.mWifiScanNotifyLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.wifi_scan_notify_layout);
        this.mWifiScanNotifyText = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_scan_notify_text);
        this.mConnectedWifListLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.wifi_connected_layout);
        this.mConnectedWifiIcon = (ImageView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_connected_icon);
        this.mConnectedWifiTitleText = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_connected_title);
        this.mConnectedWifiSummaryText = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_connected_summary);
        this.mWifiSettingsIcon = (ImageView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_settings_icon);
        this.mWifiRecyclerView = (RecyclerView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_list_layout);
        this.mSeeAllLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.see_all_layout);
        this.mDoneButton = (Button) this.mDialogView.requireViewById(C1893R.C1897id.done_button);
        this.mMobileTitleLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.mobile_title_layout);
        this.mSignalIcon = (ImageView) this.mDialogView.requireViewById(C1893R.C1897id.signal_icon);
        this.mMobileTitleText = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.mobile_title);
        this.mMobileSummaryText = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.mobile_summary);
        this.mAirplaneModeSummaryText = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.airplane_mode_summary);
        this.mMobileToggleDivider = this.mDialogView.requireViewById(C1893R.C1897id.mobile_toggle_divider);
        this.mMobileDataToggle = (Switch) this.mDialogView.requireViewById(C1893R.C1897id.mobile_toggle);
        this.mWiFiToggle = (Switch) this.mDialogView.requireViewById(C1893R.C1897id.wifi_toggle);
        this.mBackgroundOn = this.mContext.getDrawable(C1893R.C1895drawable.settingslib_switch_bar_bg_on);
        this.mInternetDialogTitle.setText(getDialogTitleText());
        this.mInternetDialogTitle.setGravity(8388627);
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16843534});
        try {
            this.mBackgroundOff = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            setOnClickListener();
            this.mTurnWifiOnLayout.setBackground((Drawable) null);
            this.mWifiRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
            this.mWifiRecyclerView.setAdapter(this.mAdapter);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public void onStart() {
        super.onStart();
        if (DEBUG) {
            Log.d(TAG, "onStart");
        }
        this.mInternetDialogController.onStart(this, this.mCanConfigWifi);
        if (!this.mCanConfigWifi) {
            hideWifiViews();
        }
    }

    /* access modifiers changed from: package-private */
    public void hideWifiViews() {
        setProgressBarVisible(false);
        this.mTurnWifiOnLayout.setVisibility(8);
        this.mConnectedWifListLayout.setVisibility(8);
        this.mWifiRecyclerView.setVisibility(8);
        this.mSeeAllLayout.setVisibility(8);
    }

    public void onStop() {
        super.onStop();
        if (DEBUG) {
            Log.d(TAG, "onStop");
        }
        this.mHandler.removeCallbacks(this.mHideProgressBarRunnable);
        this.mHandler.removeCallbacks(this.mHideSearchingRunnable);
        this.mMobileNetworkLayout.setOnClickListener((View.OnClickListener) null);
        this.mMobileDataToggle.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        this.mConnectedWifListLayout.setOnClickListener((View.OnClickListener) null);
        this.mSeeAllLayout.setOnClickListener((View.OnClickListener) null);
        this.mWiFiToggle.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        this.mDoneButton.setOnClickListener((View.OnClickListener) null);
        this.mInternetTileEx.resetHeaderOnClickListener(this.mMobileTitleLayout, this.mTurnWifiOnLayout);
        this.mInternetDialogController.onStop();
        this.mInternetDialogFactory.destroyDialog();
    }

    public void dismissDialog() {
        if (DEBUG) {
            Log.d(TAG, "dismissDialog");
        }
        this.mInternetDialogFactory.destroyDialog();
        dismiss();
    }

    /* access modifiers changed from: package-private */
    public void updateDialog(boolean z) {
        if (DEBUG) {
            Log.d(TAG, "updateDialog");
        }
        this.mInternetDialogTitle.setText(getDialogTitleText());
        this.mInternetDialogSubTitle.setText(getSubtitleText());
        updateEthernet();
        if (z) {
            setMobileDataLayout(this.mInternetDialogController.activeNetworkIsCellular(), this.mInternetDialogController.isCarrierNetworkActive());
        }
        if (this.mCanConfigWifi) {
            showProgressBar();
            boolean isDeviceLocked = this.mInternetDialogController.isDeviceLocked();
            boolean isWifiEnabled = this.mInternetDialogController.isWifiEnabled();
            boolean isWifiScanEnabled = this.mInternetDialogController.isWifiScanEnabled();
            updateWifiToggle(isWifiEnabled, isDeviceLocked);
            updateConnectedWifi(isWifiEnabled, isDeviceLocked);
            updateWifiListAndSeeAll(isWifiEnabled, isDeviceLocked);
            updateWifiScanNotify(isWifiEnabled, isWifiScanEnabled, isDeviceLocked);
        }
    }

    private void setOnClickListener() {
        this.mMobileNetworkLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda11(this));
        this.mMobileDataToggle.setOnCheckedChangeListener(new InternetDialog$$ExternalSyntheticLambda18(this));
        this.mConnectedWifListLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda19(this));
        this.mSeeAllLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda20(this));
        this.mWiFiToggle.setOnCheckedChangeListener(new InternetDialog$$ExternalSyntheticLambda21(this));
        this.mDoneButton.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda22(this));
        this.mInternetTileEx.setHeaderOnClickListener(this, this.mMobileTitleLayout, this.mTurnWifiOnLayout, this.mInternetDialogController);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setOnClickListener$2$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36992xc283f0fd(View view) {
        if (this.mInternetDialogController.isMobileDataEnabled() && !this.mInternetDialogController.isDeviceLocked() && !this.mInternetDialogController.activeNetworkIsCellular()) {
            this.mInternetDialogController.connectCarrierNetwork();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setOnClickListener$3$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36993x4fbea27e(CompoundButton compoundButton, boolean z) {
        if (!z && shouldShowMobileDialog()) {
            showTurnOffMobileDialog();
        } else if (!shouldShowMobileDialog()) {
            this.mInternetDialogController.setMobileDataEnabled(this.mContext, this.mDefaultDataSubId, z, false);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setOnClickListener$4$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36994xdcf953ff(CompoundButton compoundButton, boolean z) {
        if (this.mInternetDialogController.isWifiEnabled() != z) {
            this.mInternetDialogController.setWifiEnabled(z);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setOnClickListener$5$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36995x6a340580(View view) {
        dismiss();
    }

    private void updateEthernet() {
        this.mEthernetLayout.setVisibility(this.mInternetDialogController.hasEthernet() ? 0 : 8);
    }

    private boolean shouldDisallowUserToDisableMobileData() {
        return this.mInternetDialogController.isMobileDataEnabled() && !this.mInternetDialogController.isNonDdsCallStateIdle();
    }

    private void setMobileDataLayout(boolean z, boolean z2) {
        int i = 0;
        boolean z3 = z || z2;
        if (DEBUG) {
            Log.d(TAG, "setMobileDataLayout, isCarrierNetworkActive = " + z2);
        }
        boolean isWifiEnabled = this.mInternetDialogController.isWifiEnabled();
        if (this.mInternetDialogController.hasActiveSubId() || (isWifiEnabled && z2)) {
            if (shouldDisallowUserToDisableMobileData()) {
                Log.d(TAG, "Do not allow mobile data switch to be turned off");
                this.mMobileDataToggle.setEnabled(false);
            } else {
                this.mMobileDataToggle.setEnabled(true);
            }
            this.mMobileNetworkLayout.setVisibility(0);
            this.mMobileDataToggle.setChecked(this.mInternetDialogController.isMobileDataEnabled());
            this.mMobileTitleText.setText(getMobileNetworkTitle());
            String mobileNetworkSummary = getMobileNetworkSummary();
            if (!TextUtils.isEmpty(mobileNetworkSummary)) {
                this.mMobileSummaryText.setText(Html.fromHtml(mobileNetworkSummary, 0));
                this.mMobileSummaryText.setVisibility(0);
            } else {
                this.mMobileSummaryText.setVisibility(8);
            }
            this.mBackgroundExecutor.execute(new InternetDialog$$ExternalSyntheticLambda16(this));
            this.mMobileTitleText.setTextAppearance(z3 ? C1893R.style.TextAppearance_InternetDialog_Active : C1893R.style.TextAppearance_InternetDialog);
            int i2 = z3 ? C1893R.style.TextAppearance_InternetDialog_Secondary_Active : C1893R.style.TextAppearance_InternetDialog_Secondary;
            this.mMobileSummaryText.setTextAppearance(i2);
            if (this.mInternetDialogController.isAirplaneModeEnabled()) {
                this.mAirplaneModeSummaryText.setVisibility(0);
                this.mAirplaneModeSummaryText.setText(this.mContext.getText(C1893R.string.airplane_mode));
                this.mAirplaneModeSummaryText.setTextAppearance(i2);
            } else {
                this.mAirplaneModeSummaryText.setVisibility(8);
            }
            this.mMobileNetworkLayout.setBackground(z3 ? this.mBackgroundOn : this.mBackgroundOff);
            TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(C1893R.style.InternetDialog_Divider_Active, new int[]{16842964});
            int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16842808);
            View view = this.mMobileToggleDivider;
            if (z3) {
                colorAttrDefaultColor = obtainStyledAttributes.getColor(0, colorAttrDefaultColor);
            }
            view.setBackgroundColor(colorAttrDefaultColor);
            obtainStyledAttributes.recycle();
            this.mMobileDataToggle.setVisibility(this.mCanConfigMobileData ? 0 : 4);
            View view2 = this.mMobileToggleDivider;
            if (!this.mCanConfigMobileData) {
                i = 4;
            }
            view2.setVisibility(i);
            return;
        }
        this.mMobileNetworkLayout.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setMobileDataLayout$7$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36991xb18aa025() {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda10(this, getSignalStrengthDrawable()));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setMobileDataLayout$6$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36990x244feea4(Drawable drawable) {
        this.mSignalIcon.setImageDrawable(drawable);
    }

    private void updateWifiToggle(boolean z, boolean z2) {
        if (this.mWiFiToggle.isChecked() != z) {
            this.mWiFiToggle.setChecked(z);
        }
        if (z2) {
            this.mWifiToggleTitleText.setTextAppearance(this.mConnectedWifiEntry != null ? C1893R.style.TextAppearance_InternetDialog_Active : C1893R.style.TextAppearance_InternetDialog);
        }
        this.mTurnWifiOnLayout.setBackground((!z2 || this.mConnectedWifiEntry == null) ? null : this.mBackgroundOn);
        if (!this.mCanChangeWifiState && this.mWiFiToggle.isEnabled()) {
            this.mWiFiToggle.setEnabled(false);
            this.mWifiToggleTitleText.setEnabled(false);
            TextView textView = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.wifi_toggle_summary);
            textView.setEnabled(false);
            textView.setVisibility(0);
        }
        this.mInternetTileEx.updateWindowSize(this.mContext, getWindow(), this.mInternetDialogController, false);
    }

    private void updateConnectedWifi(boolean z, boolean z2) {
        if (!z || this.mConnectedWifiEntry == null || z2) {
            this.mConnectedWifListLayout.setVisibility(8);
            return;
        }
        this.mConnectedWifListLayout.setVisibility(0);
        this.mConnectedWifiTitleText.setText(this.mConnectedWifiEntry.getTitle());
        this.mConnectedWifiSummaryText.setText(this.mConnectedWifiEntry.getSummary(false));
        this.mConnectedWifiIcon.setImageDrawable(this.mInternetDialogController.getInternetWifiDrawable(this.mConnectedWifiEntry));
        this.mWifiSettingsIcon.setColorFilter(this.mContext.getColor(C1893R.C1894color.connected_network_primary_color));
    }

    private void updateWifiListAndSeeAll(boolean z, boolean z2) {
        if (!z || z2) {
            this.mWifiRecyclerView.setVisibility(8);
            this.mSeeAllLayout.setVisibility(8);
            return;
        }
        int wifiListMaxCount = getWifiListMaxCount();
        if (this.mAdapter.getItemCount() > wifiListMaxCount) {
            this.mHasMoreWifiEntries = true;
        }
        this.mAdapter.setMaxEntriesCount(wifiListMaxCount);
        this.mWifiRecyclerView.setVisibility(0);
    }

    /* access modifiers changed from: package-private */
    public int getWifiListMaxCount() {
        int i = 50;
        int i2 = this.mEthernetLayout.getVisibility() == 0 ? 49 : 50;
        if (this.mMobileNetworkLayout.getVisibility() == 0) {
            i2--;
        }
        if (i2 <= 50) {
            i = i2;
        }
        return this.mConnectedWifListLayout.getVisibility() == 0 ? i - 1 : i;
    }

    private void updateWifiScanNotify(boolean z, boolean z2, boolean z3) {
        if (z || !z2 || z3) {
            this.mWifiScanNotifyLayout.setVisibility(8);
            return;
        }
        if (TextUtils.isEmpty(this.mWifiScanNotifyText.getText())) {
            InternetDialogController internetDialogController = this.mInternetDialogController;
            Objects.requireNonNull(internetDialogController);
            AnnotationLinkSpan.LinkInfo linkInfo = new AnnotationLinkSpan.LinkInfo(AnnotationLinkSpan.LinkInfo.DEFAULT_ANNOTATION, new InternetDialog$$ExternalSyntheticLambda4(internetDialogController));
            this.mWifiScanNotifyText.setText(AnnotationLinkSpan.linkify(getContext().getText(C1893R.string.wifi_scan_notify_message), linkInfo));
            this.mWifiScanNotifyText.setMovementMethod(LinkMovementMethod.getInstance());
        }
        this.mWifiScanNotifyLayout.setVisibility(0);
    }

    /* access modifiers changed from: package-private */
    public void onClickConnectedWifi(View view) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            this.mInternetDialogController.launchWifiDetailsSetting(wifiEntry.getKey(), view);
        }
    }

    /* access modifiers changed from: package-private */
    public void onClickSeeMoreButton(View view) {
        this.mInternetDialogController.launchNetworkSetting(view);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getDialogTitleText() {
        return this.mInternetDialogController.getDialogTitleText();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSubtitleText() {
        return this.mInternetDialogController.getSubtitleText(this.mIsProgressBarVisible && !this.mIsSearchingHidden);
    }

    private Drawable getSignalStrengthDrawable() {
        return this.mInternetDialogController.getSignalStrengthDrawable();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getMobileNetworkTitle() {
        return this.mInternetDialogController.getMobileNetworkTitle();
    }

    /* access modifiers changed from: package-private */
    public String getMobileNetworkSummary() {
        if (shouldDisallowUserToDisableMobileData()) {
            return this.mContext.getString(C1893R.string.mobile_data_summary_not_allowed_to_disable_data);
        }
        return this.mInternetDialogController.getMobileNetworkSummary();
    }

    /* access modifiers changed from: protected */
    public void showProgressBar() {
        if (!this.mInternetDialogController.isWifiEnabled() || this.mInternetDialogController.isDeviceLocked()) {
            setProgressBarVisible(false);
            return;
        }
        setProgressBarVisible(true);
        if (this.mConnectedWifiEntry != null || this.mWifiEntriesCount > 0) {
            this.mHandler.postDelayed(this.mHideProgressBarRunnable, 1500);
        } else if (!this.mIsSearchingHidden) {
            this.mHandler.postDelayed(this.mHideSearchingRunnable, 1500);
        }
    }

    private void setProgressBarVisible(boolean z) {
        if (this.mIsProgressBarVisible != z) {
            this.mIsProgressBarVisible = z;
            int i = 0;
            this.mProgressBar.setVisibility(z ? 0 : 8);
            this.mProgressBar.setIndeterminate(z);
            View view = this.mDivider;
            if (z) {
                i = 8;
            }
            view.setVisibility(i);
            this.mInternetDialogSubTitle.setText(getSubtitleText());
        }
    }

    private boolean shouldShowMobileDialog() {
        boolean z = Prefs.getBoolean(this.mContext, Prefs.Key.QS_HAS_TURNED_OFF_MOBILE_DATA, false);
        if (!this.mInternetDialogController.isMobileDataEnabled() || z) {
            return false;
        }
        return true;
    }

    private void showTurnOffMobileDialog() {
        boolean z;
        String str;
        CharSequence mobileNetworkTitle = getMobileNetworkTitle();
        boolean isVoiceStateInService = this.mInternetDialogController.isVoiceStateInService();
        if (TextUtils.isEmpty(mobileNetworkTitle) || !isVoiceStateInService) {
            mobileNetworkTitle = this.mContext.getString(C1893R.string.mobile_data_disable_message_default_carrier);
        }
        boolean z2 = this.mTelephonyManager.getCallState() == 0;
        try {
            if (this.mTelephonyManager.getImsRegistration(SubscriptionManager.getSlotIndex(this.mDefaultDataSubId), 1).getRegistrationTechnology() == 2) {
                z = true;
                Log.d(TAG, "isCallIdle=" + z2 + ", isImsRegisteredOverCiwlan=" + z);
                if (!z2 || !z) {
                    str = this.mContext.getString(C1893R.string.mobile_data_disable_message, new Object[]{mobileNetworkTitle});
                } else {
                    str = this.mContext.getString(C1893R.string.mobile_data_disable_ciwlan_call_message);
                }
                AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(C1893R.string.mobile_data_disable_title).setMessage(str).setNegativeButton(17039360, new InternetDialog$$ExternalSyntheticLambda23(this)).setPositiveButton(17039659, new InternetDialog$$ExternalSyntheticLambda24(this)).create();
                this.mAlertDialog = create;
                create.setOnCancelListener(new InternetDialog$$ExternalSyntheticLambda25(this));
                this.mAlertDialog.getWindow().setType(Types.SQLXML);
                SystemUIDialog.setShowForAllUsers(this.mAlertDialog, true);
                SystemUIDialog.registerDismissListener(this.mAlertDialog);
                SystemUIDialog.setWindowOnTop(this.mAlertDialog, this.mKeyguard.isShowing());
                this.mAlertDialog.show();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getRegistrationTechnology failed", e);
        }
        z = false;
        Log.d(TAG, "isCallIdle=" + z2 + ", isImsRegisteredOverCiwlan=" + z);
        if (!z2) {
        }
        str = this.mContext.getString(C1893R.string.mobile_data_disable_message, new Object[]{mobileNetworkTitle});
        AlertDialog create2 = new AlertDialog.Builder(this.mContext).setTitle(C1893R.string.mobile_data_disable_title).setMessage(str).setNegativeButton(17039360, new InternetDialog$$ExternalSyntheticLambda23(this)).setPositiveButton(17039659, new InternetDialog$$ExternalSyntheticLambda24(this)).create();
        this.mAlertDialog = create2;
        create2.setOnCancelListener(new InternetDialog$$ExternalSyntheticLambda25(this));
        this.mAlertDialog.getWindow().setType(Types.SQLXML);
        SystemUIDialog.setShowForAllUsers(this.mAlertDialog, true);
        SystemUIDialog.registerDismissListener(this.mAlertDialog);
        SystemUIDialog.setWindowOnTop(this.mAlertDialog, this.mKeyguard.isShowing());
        this.mAlertDialog.show();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showTurnOffMobileDialog$8$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36997xc3dc1cd(DialogInterface dialogInterface, int i) {
        this.mMobileDataToggle.setChecked(true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showTurnOffMobileDialog$9$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36998x9978734e(DialogInterface dialogInterface, int i) {
        this.mInternetDialogController.setMobileDataEnabled(this.mContext, this.mDefaultDataSubId, false, false);
        this.mMobileDataToggle.setChecked(false);
        Prefs.putBoolean(this.mContext, Prefs.Key.QS_HAS_TURNED_OFF_MOBILE_DATA, true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showTurnOffMobileDialog$10$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36996x849651b8(DialogInterface dialogInterface) {
        this.mMobileDataToggle.setChecked(true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onRefreshCarrierInfo$11$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36984xdfe8d7f4() {
        updateDialog(true);
    }

    public void onRefreshCarrierInfo() {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSimStateChanged$12$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36987x56714a9e() {
        updateDialog(true);
    }

    public void onSimStateChanged() {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda12(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCapabilitiesChanged$13$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36979xf4f706db() {
        updateDialog(true);
    }

    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda13(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLost$14$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36982x735f7a1e() {
        updateDialog(true);
    }

    public void onLost(Network network) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda2(this));
    }

    public void onSubscriptionsChanged(int i) {
        this.mDefaultDataSubId = i;
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSubscriptionsChanged$15$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36988x6352b2a5() {
        updateDialog(true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onUserMobileDataStateChanged$16$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36989x79d5e86a() {
        updateDialog(true);
    }

    public void onUserMobileDataStateChanged(boolean z) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda17(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onServiceStateChanged$17$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36985xf8bd1585() {
        updateDialog(true);
    }

    public void onServiceStateChanged(ServiceState serviceState) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda6(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDataConnectionStateChanged$18$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36980xe006193b() {
        updateDialog(true);
    }

    public void onDataConnectionStateChanged(int i, int i2) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda15(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSignalStrengthsChanged$19$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36986x6381b83d() {
        updateDialog(true);
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDisplayInfoChanged$20$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36981xa9764ed9() {
        updateDialog(true);
    }

    public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onNonDdsCallStateChanged$21$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36983xd4e84697() {
        updateDialog(true);
    }

    public void onNonDdsCallStateChanged(int i) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda1(this));
    }

    public void onAccessPointsChanged(List<WifiEntry> list, WifiEntry wifiEntry, boolean z) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda14(this, wifiEntry, list, z, this.mMobileNetworkLayout.getVisibility() == 0 && this.mInternetDialogController.isAirplaneModeEnabled()));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onAccessPointsChanged$22$com-android-systemui-qs-tiles-dialog-InternetDialog */
    public /* synthetic */ void mo36978x64ea57ea(WifiEntry wifiEntry, List list, boolean z, boolean z2) {
        int i;
        this.mConnectedWifiEntry = wifiEntry;
        if (list == null) {
            i = 0;
        } else {
            i = list.size();
        }
        this.mWifiEntriesCount = i;
        this.mHasMoreWifiEntries = z;
        updateDialog(z2);
        this.mAdapter.setWifiEntries(list, this.mWifiEntriesCount);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null && !alertDialog.isShowing() && !z && isShowing()) {
            dismiss();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.mInternetTileEx.updateWindowSize(this.mContext, getWindow(), this.mInternetDialogController, false);
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialog$InternetDialogEvent */
    public enum InternetDialogEvent implements UiEventLogger.UiEventEnum {
        INTERNET_DIALOG_SHOW(843);
        
        private final int mId;

        private InternetDialogEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
