package com.android.systemui.qs.tiles.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Prefs;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class InternetDialog extends SystemUIDialog implements InternetDialogController.InternetDialogCallback {
    private static final boolean DEBUG = Log.isLoggable("InternetDialog", 3);
    protected InternetAdapter mAdapter;
    private AlertDialog mAlertDialog;
    private final Executor mBackgroundExecutor;
    private Drawable mBackgroundOn;
    private boolean mCanConfigMobileData;
    protected boolean mCanConfigWifi;
    private LinearLayout mConnectedWifListLayout;
    protected WifiEntry mConnectedWifiEntry;
    private ImageView mConnectedWifiIcon;
    private TextView mConnectedWifiSummaryText;
    private TextView mConnectedWifiTitleText;
    private Context mContext;
    private int mDefaultDataSubId;
    protected View mDialogView;
    private View mDivider;
    private FrameLayout mDoneLayout;
    private LinearLayout mEthernetLayout;
    private final Handler mHandler;
    private InternetDialogController mInternetDialogController;
    private InternetDialogFactory mInternetDialogFactory;
    private LinearLayout mInternetDialogLayout;
    private TextView mInternetDialogSubTitle;
    private TextView mInternetDialogTitle;
    protected boolean mIsProgressBarVisible;
    protected boolean mIsSearchingHidden;
    private final LinearLayoutManager mLayoutManager;
    private int mListMaxHeight;
    private Switch mMobileDataToggle;
    private LinearLayout mMobileNetworkLayout;
    private TextView mMobileSummaryText;
    private TextView mMobileTitleText;
    private ProgressBar mProgressBar;
    private LinearLayout mSeeAllLayout;
    private ImageView mSignalIcon;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private LinearLayout mTurnWifiOnLayout;
    private UiEventLogger mUiEventLogger;
    private Switch mWiFiToggle;
    protected int mWifiEntriesCount;
    protected WifiManager mWifiManager;
    private RecyclerView mWifiRecyclerView;
    private LinearLayout mWifiScanNotifyLayout;
    private TextView mWifiScanNotifyText;
    private ImageView mWifiSettingsIcon;
    private TextView mWifiToggleTitleText;
    protected final Runnable mHideProgressBarRunnable = new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda24
        @Override // java.lang.Runnable
        public final void run() {
            InternetDialog.this.lambda$new$0();
        }
    };
    protected Runnable mHideSearchingRunnable = new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda16
        @Override // java.lang.Runnable
        public final void run() {
            InternetDialog.this.lambda$new$1();
        }
    };
    private final ViewTreeObserver.OnGlobalLayoutListener mInternetListLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda8
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public final void onGlobalLayout() {
            InternetDialog.this.lambda$new$2();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        setProgressBarVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mIsSearchingHidden = true;
        this.mInternetDialogSubTitle.setText(getSubtitleText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (this.mInternetDialogLayout.getHeight() > this.mListMaxHeight) {
            ViewGroup.LayoutParams layoutParams = this.mInternetDialogLayout.getLayoutParams();
            layoutParams.height = this.mListMaxHeight;
            this.mInternetDialogLayout.setLayoutParams(layoutParams);
        }
    }

    public InternetDialog(Context context, InternetDialogFactory internetDialogFactory, InternetDialogController internetDialogController, boolean z, boolean z2, boolean z3, UiEventLogger uiEventLogger, Handler handler, Executor executor) {
        super(context, R$style.Theme_SystemUI_Dialog_Internet);
        this.mDefaultDataSubId = -1;
        if (DEBUG) {
            Log.d("InternetDialog", "Init InternetDialog");
        }
        this.mContext = context;
        this.mHandler = handler;
        this.mBackgroundExecutor = executor;
        this.mInternetDialogFactory = internetDialogFactory;
        this.mInternetDialogController = internetDialogController;
        this.mSubscriptionManager = internetDialogController.getSubscriptionManager();
        this.mDefaultDataSubId = this.mInternetDialogController.getDefaultDataSubscriptionId();
        this.mTelephonyManager = this.mInternetDialogController.getTelephonyManager();
        this.mWifiManager = this.mInternetDialogController.getWifiManager();
        this.mCanConfigMobileData = z;
        this.mCanConfigWifi = z2;
        this.mLayoutManager = new LinearLayoutManager(this.mContext) { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog.1
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean canScrollVertically() {
                return false;
            }
        };
        this.mListMaxHeight = context.getResources().getDimensionPixelSize(R$dimen.internet_dialog_list_max_height);
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new InternetAdapter(this.mInternetDialogController);
        if (!z3) {
            getWindow().setType(2038);
        }
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (DEBUG) {
            Log.d("InternetDialog", "onCreate");
        }
        this.mUiEventLogger.log(InternetDialogEvent.INTERNET_DIALOG_SHOW);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(R$layout.internet_connectivity_dialog, (ViewGroup) null);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.setFitInsetsTypes(attributes.getFitInsetsTypes() & (~WindowInsets.Type.navigationBars()));
        attributes.setFitInsetsSides(WindowInsets.Side.all());
        attributes.setFitInsetsIgnoringVisibility(true);
        window.setAttributes(attributes);
        window.setContentView(this.mDialogView);
        window.setLayout(this.mContext.getResources().getDimensionPixelSize(R$dimen.internet_dialog_list_max_width), -2);
        window.setWindowAnimations(R$style.Animation_InternetDialog);
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(512);
        this.mInternetDialogLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.internet_connectivity_dialog);
        this.mInternetDialogTitle = (TextView) this.mDialogView.requireViewById(R$id.internet_dialog_title);
        this.mInternetDialogSubTitle = (TextView) this.mDialogView.requireViewById(R$id.internet_dialog_subtitle);
        this.mDivider = this.mDialogView.requireViewById(R$id.divider);
        this.mProgressBar = (ProgressBar) this.mDialogView.requireViewById(R$id.wifi_searching_progress);
        this.mEthernetLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.ethernet_layout);
        this.mMobileNetworkLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.mobile_network_layout);
        this.mTurnWifiOnLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.turn_on_wifi_layout);
        this.mWifiToggleTitleText = (TextView) this.mDialogView.requireViewById(R$id.wifi_toggle_title);
        this.mWifiScanNotifyLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.wifi_scan_notify_layout);
        this.mWifiScanNotifyText = (TextView) this.mDialogView.requireViewById(R$id.wifi_scan_notify_text);
        this.mConnectedWifListLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.wifi_connected_layout);
        this.mConnectedWifiIcon = (ImageView) this.mDialogView.requireViewById(R$id.wifi_connected_icon);
        this.mConnectedWifiTitleText = (TextView) this.mDialogView.requireViewById(R$id.wifi_connected_title);
        this.mConnectedWifiSummaryText = (TextView) this.mDialogView.requireViewById(R$id.wifi_connected_summary);
        this.mWifiSettingsIcon = (ImageView) this.mDialogView.requireViewById(R$id.wifi_settings_icon);
        this.mWifiRecyclerView = (RecyclerView) this.mDialogView.requireViewById(R$id.wifi_list_layout);
        this.mSeeAllLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.see_all_layout);
        this.mDoneLayout = (FrameLayout) this.mDialogView.requireViewById(R$id.done_layout);
        this.mSignalIcon = (ImageView) this.mDialogView.requireViewById(R$id.signal_icon);
        this.mMobileTitleText = (TextView) this.mDialogView.requireViewById(R$id.mobile_title);
        this.mMobileSummaryText = (TextView) this.mDialogView.requireViewById(R$id.mobile_summary);
        this.mMobileDataToggle = (Switch) this.mDialogView.requireViewById(R$id.mobile_toggle);
        this.mWiFiToggle = (Switch) this.mDialogView.requireViewById(R$id.wifi_toggle);
        this.mBackgroundOn = this.mContext.getDrawable(R$drawable.settingslib_switch_bar_bg_on);
        this.mInternetDialogLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.mInternetListLayoutListener);
        this.mInternetDialogTitle.setText(getDialogTitleText());
        this.mInternetDialogTitle.setGravity(8388627);
        setOnClickListener();
        this.mTurnWifiOnLayout.setBackground(null);
        this.mWifiRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mWifiRecyclerView.setAdapter(this.mAdapter);
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public void onStart() {
        super.onStart();
        if (DEBUG) {
            Log.d("InternetDialog", "onStart");
        }
        this.mInternetDialogController.onStart(this, this.mCanConfigWifi);
        if (!this.mCanConfigWifi) {
            hideWifiViews();
        }
    }

    void hideWifiViews() {
        setProgressBarVisible(false);
        this.mTurnWifiOnLayout.setVisibility(8);
        this.mConnectedWifListLayout.setVisibility(8);
        this.mWifiRecyclerView.setVisibility(8);
        this.mSeeAllLayout.setVisibility(8);
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public void onStop() {
        super.onStop();
        if (DEBUG) {
            Log.d("InternetDialog", "onStop");
        }
        this.mHandler.removeCallbacks(this.mHideProgressBarRunnable);
        this.mHandler.removeCallbacks(this.mHideSearchingRunnable);
        this.mMobileNetworkLayout.setOnClickListener(null);
        this.mMobileDataToggle.setOnCheckedChangeListener(null);
        this.mConnectedWifListLayout.setOnClickListener(null);
        this.mSeeAllLayout.setOnClickListener(null);
        this.mWiFiToggle.setOnCheckedChangeListener(null);
        this.mDoneLayout.setOnClickListener(null);
        this.mInternetDialogController.onStop();
        this.mInternetDialogFactory.destroyDialog();
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void dismissDialog() {
        if (DEBUG) {
            Log.d("InternetDialog", "dismissDialog");
        }
        this.mInternetDialogFactory.destroyDialog();
        dismiss();
    }

    void updateDialog(boolean z) {
        if (DEBUG) {
            Log.d("InternetDialog", "updateDialog");
        }
        int i = 8;
        if (this.mInternetDialogController.isAirplaneModeEnabled()) {
            this.mInternetDialogSubTitle.setVisibility(8);
        } else {
            this.mInternetDialogSubTitle.setText(getSubtitleText());
        }
        updateEthernet();
        if (z) {
            setMobileDataLayout(this.mInternetDialogController.activeNetworkIsCellular() || this.mInternetDialogController.isCarrierNetworkActive());
        }
        if (!this.mCanConfigWifi) {
            return;
        }
        showProgressBar();
        boolean isDeviceLocked = this.mInternetDialogController.isDeviceLocked();
        boolean isWifiEnabled = this.mWifiManager.isWifiEnabled();
        boolean isWifiScanEnabled = this.mInternetDialogController.isWifiScanEnabled();
        updateWifiToggle(isWifiEnabled, isDeviceLocked);
        updateConnectedWifi(isWifiEnabled, isDeviceLocked);
        updateWifiScanNotify(isWifiEnabled, isWifiScanEnabled, isDeviceLocked);
        if (!isDeviceLocked && isWifiEnabled && this.mWifiEntriesCount > 0) {
            i = 0;
        }
        this.mWifiRecyclerView.setVisibility(i);
        this.mSeeAllLayout.setVisibility(i);
    }

    private void setOnClickListener() {
        this.mMobileNetworkLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InternetDialog.this.lambda$setOnClickListener$3(view);
            }
        });
        this.mMobileDataToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda10
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                InternetDialog.this.lambda$setOnClickListener$4(compoundButton, z);
            }
        });
        this.mConnectedWifListLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InternetDialog.this.lambda$setOnClickListener$5(view);
            }
        });
        this.mSeeAllLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InternetDialog.this.lambda$setOnClickListener$6(view);
            }
        });
        this.mWiFiToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda9
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                InternetDialog.this.lambda$setOnClickListener$7(compoundButton, z);
            }
        });
        this.mDoneLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InternetDialog.this.lambda$setOnClickListener$8(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$3(View view) {
        if (!this.mInternetDialogController.isMobileDataEnabled() || this.mInternetDialogController.isDeviceLocked() || this.mInternetDialogController.activeNetworkIsCellular()) {
            return;
        }
        this.mInternetDialogController.connectCarrierNetwork();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$4(CompoundButton compoundButton, boolean z) {
        if (!z && shouldShowMobileDialog()) {
            showTurnOffMobileDialog();
        } else if (shouldShowMobileDialog()) {
        } else {
            this.mInternetDialogController.setMobileDataEnabled(this.mContext, this.mDefaultDataSubId, z, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$5(View view) {
        onClickConnectedWifi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$6(View view) {
        onClickSeeMoreButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$7(CompoundButton compoundButton, boolean z) {
        compoundButton.setChecked(z);
        this.mWifiManager.setWifiEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$8(View view) {
        dismiss();
    }

    private void updateEthernet() {
        this.mEthernetLayout.setVisibility(this.mInternetDialogController.hasEthernet() ? 0 : 8);
    }

    private void setMobileDataLayout(boolean z) {
        int i;
        int i2;
        if (this.mInternetDialogController.isAirplaneModeEnabled() || !this.mInternetDialogController.hasCarrier()) {
            this.mMobileNetworkLayout.setVisibility(8);
            return;
        }
        this.mMobileDataToggle.setChecked(this.mInternetDialogController.isMobileDataEnabled());
        int i3 = 0;
        this.mMobileNetworkLayout.setVisibility(0);
        this.mMobileTitleText.setText(getMobileNetworkTitle());
        if (!TextUtils.isEmpty(getMobileNetworkSummary())) {
            this.mMobileSummaryText.setText(Html.fromHtml(getMobileNetworkSummary(), 0));
            this.mMobileSummaryText.setVisibility(0);
        } else {
            this.mMobileSummaryText.setVisibility(8);
        }
        this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$setMobileDataLayout$10();
            }
        });
        TextView textView = this.mMobileTitleText;
        if (z) {
            i = R$style.TextAppearance_InternetDialog_Active;
        } else {
            i = R$style.TextAppearance_InternetDialog;
        }
        textView.setTextAppearance(i);
        TextView textView2 = this.mMobileSummaryText;
        if (z) {
            i2 = R$style.TextAppearance_InternetDialog_Secondary_Active;
        } else {
            i2 = R$style.TextAppearance_InternetDialog_Secondary;
        }
        textView2.setTextAppearance(i2);
        this.mMobileNetworkLayout.setBackground(z ? this.mBackgroundOn : null);
        Switch r5 = this.mMobileDataToggle;
        if (!this.mCanConfigMobileData) {
            i3 = 4;
        }
        r5.setVisibility(i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setMobileDataLayout$10() {
        final Drawable signalStrengthDrawable = getSignalStrengthDrawable();
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$setMobileDataLayout$9(signalStrengthDrawable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setMobileDataLayout$9(Drawable drawable) {
        this.mSignalIcon.setImageDrawable(drawable);
    }

    private void updateWifiToggle(boolean z, boolean z2) {
        int i;
        this.mWiFiToggle.setChecked(z);
        if (z2) {
            TextView textView = this.mWifiToggleTitleText;
            if (this.mConnectedWifiEntry != null) {
                i = R$style.TextAppearance_InternetDialog_Active;
            } else {
                i = R$style.TextAppearance_InternetDialog;
            }
            textView.setTextAppearance(i);
        }
        this.mTurnWifiOnLayout.setBackground((!z2 || this.mConnectedWifiEntry == null) ? null : this.mBackgroundOn);
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
        this.mWifiSettingsIcon.setColorFilter(this.mContext.getColor(R$color.connected_network_primary_color));
    }

    private void updateWifiScanNotify(boolean z, boolean z2, boolean z3) {
        if (z || !z2 || z3) {
            this.mWifiScanNotifyLayout.setVisibility(8);
            return;
        }
        if (TextUtils.isEmpty(this.mWifiScanNotifyText.getText())) {
            this.mWifiScanNotifyText.setText(AnnotationLinkSpan.linkify(getContext().getText(R$string.wifi_scan_notify_message), new AnnotationLinkSpan.LinkInfo("link", new View.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    InternetDialog.this.lambda$updateWifiScanNotify$11(view);
                }
            })));
            this.mWifiScanNotifyText.setMovementMethod(LinkMovementMethod.getInstance());
        }
        this.mWifiScanNotifyLayout.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWifiScanNotify$11(View view) {
        this.mInternetDialogController.launchWifiScanningSetting();
    }

    void onClickConnectedWifi() {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry == null) {
            return;
        }
        this.mInternetDialogController.launchWifiNetworkDetailsSetting(wifiEntry.getKey());
    }

    void onClickSeeMoreButton() {
        this.mInternetDialogController.launchNetworkSetting();
    }

    CharSequence getDialogTitleText() {
        return this.mInternetDialogController.getDialogTitleText();
    }

    CharSequence getSubtitleText() {
        return this.mInternetDialogController.getSubtitleText(this.mIsProgressBarVisible && !this.mIsSearchingHidden);
    }

    private Drawable getSignalStrengthDrawable() {
        return this.mInternetDialogController.getSignalStrengthDrawable();
    }

    CharSequence getMobileNetworkTitle() {
        return this.mInternetDialogController.getMobileNetworkTitle();
    }

    String getMobileNetworkSummary() {
        return this.mInternetDialogController.getMobileNetworkSummary();
    }

    protected void showProgressBar() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null || !wifiManager.isWifiEnabled() || this.mInternetDialogController.isDeviceLocked()) {
            setProgressBarVisible(false);
            return;
        }
        setProgressBarVisible(true);
        if (this.mConnectedWifiEntry != null || this.mWifiEntriesCount > 0) {
            this.mHandler.postDelayed(this.mHideProgressBarRunnable, 2000L);
        } else if (this.mIsSearchingHidden) {
        } else {
            this.mHandler.postDelayed(this.mHideSearchingRunnable, 2000L);
        }
    }

    private void setProgressBarVisible(boolean z) {
        View view;
        if (this.mWifiManager.isWifiEnabled() && (view = this.mAdapter.mHolderView) != null && view.isAttachedToWindow()) {
            this.mIsProgressBarVisible = true;
        }
        this.mIsProgressBarVisible = z;
        int i = 0;
        this.mProgressBar.setVisibility(z ? 0 : 8);
        View view2 = this.mDivider;
        if (this.mIsProgressBarVisible) {
            i = 8;
        }
        view2.setVisibility(i);
        this.mInternetDialogSubTitle.setText(getSubtitleText());
    }

    private boolean shouldShowMobileDialog() {
        return this.mInternetDialogController.isMobileDataEnabled() && !Prefs.getBoolean(this.mContext, "QsHasTurnedOffMobileData", false);
    }

    private void showTurnOffMobileDialog() {
        CharSequence mobileNetworkTitle = getMobileNetworkTitle();
        boolean isVoiceStateInService = this.mInternetDialogController.isVoiceStateInService();
        if (TextUtils.isEmpty(mobileNetworkTitle) || !isVoiceStateInService) {
            mobileNetworkTitle = this.mContext.getString(R$string.mobile_data_disable_message_default_carrier);
        }
        AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(R$string.mobile_data_disable_title).setMessage(this.mContext.getString(R$string.mobile_data_disable_message, mobileNetworkTitle)).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                InternetDialog.this.lambda$showTurnOffMobileDialog$12(dialogInterface, i);
            }
        }).setPositiveButton(17039646, new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                InternetDialog.this.lambda$showTurnOffMobileDialog$13(dialogInterface, i);
            }
        }).create();
        this.mAlertDialog = create;
        create.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                InternetDialog.this.lambda$showTurnOffMobileDialog$14(dialogInterface);
            }
        });
        this.mAlertDialog.getWindow().setType(2009);
        SystemUIDialog.setShowForAllUsers(this.mAlertDialog, true);
        SystemUIDialog.registerDismissListener(this.mAlertDialog);
        SystemUIDialog.setWindowOnTop(this.mAlertDialog);
        this.mAlertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTurnOffMobileDialog$12(DialogInterface dialogInterface, int i) {
        this.mMobileDataToggle.setChecked(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTurnOffMobileDialog$13(DialogInterface dialogInterface, int i) {
        this.mInternetDialogController.setMobileDataEnabled(this.mContext, this.mDefaultDataSubId, false, false);
        this.mMobileDataToggle.setChecked(false);
        Prefs.putBoolean(this.mContext, "QsHasTurnedOffMobileData", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTurnOffMobileDialog$14(DialogInterface dialogInterface) {
        this.mMobileDataToggle.setChecked(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRefreshCarrierInfo$15() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onRefreshCarrierInfo() {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onRefreshCarrierInfo$15();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSimStateChanged$16() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onSimStateChanged() {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onSimStateChanged$16();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCapabilitiesChanged$17() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onCapabilitiesChanged$17();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLost$18() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onLost(Network network) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onLost$18();
            }
        });
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onSubscriptionsChanged(int i) {
        this.mDefaultDataSubId = i;
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onSubscriptionsChanged$19();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSubscriptionsChanged$19() {
        updateDialog(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserMobileDataStateChanged$20() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onUserMobileDataStateChanged(boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onUserMobileDataStateChanged$20();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onServiceStateChanged$21() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onServiceStateChanged(ServiceState serviceState) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onServiceStateChanged$21();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDataConnectionStateChanged$22() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onDataConnectionStateChanged(int i, int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onDataConnectionStateChanged$22();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSignalStrengthsChanged$23() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onSignalStrengthsChanged$23();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayInfoChanged$24() {
        updateDialog(true);
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onDisplayInfoChanged$24();
            }
        });
    }

    @Override // com.android.systemui.qs.tiles.dialog.InternetDialogController.InternetDialogCallback
    public void onAccessPointsChanged(List<WifiEntry> list, WifiEntry wifiEntry) {
        this.mConnectedWifiEntry = wifiEntry;
        int size = list == null ? 0 : list.size();
        this.mWifiEntriesCount = size;
        this.mAdapter.setWifiEntries(list, size);
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                InternetDialog.this.lambda$onAccessPointsChanged$25();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAccessPointsChanged$25() {
        this.mAdapter.notifyDataSetChanged();
        updateDialog(false);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog == null || alertDialog.isShowing() || z || !isShowing()) {
            return;
        }
        dismiss();
    }

    /* loaded from: classes.dex */
    public enum InternetDialogEvent implements UiEventLogger.UiEventEnum {
        INTERNET_DIALOG_SHOW(843);
        
        private final int mId;

        InternetDialogEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
