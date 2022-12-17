package com.nothing.settings.wifi;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.core.InstrumentedActivity;
import com.android.wifitrackerlib.WifiEntry;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.template.DescriptionMixin;
import com.google.android.setupdesign.template.HeaderMixin;
import com.google.android.setupdesign.transition.TransitionHelper;
import com.nothing.settings.wifi.WifiListAdapter;
import com.nothing.settings.wifi.WizardWifiManager;
import com.nothing.settings.wifi.seeall.WizardAllWifiActivity;
import java.util.List;

public class WizardWifiConnectActivity extends InstrumentedActivity {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable("SuwWifiConnectActivity", 3);
    /* access modifiers changed from: private */
    public WifiListAdapter mAdapter;
    private DescriptionMixin mDescriptionMixin;
    private FooterBarMixin mFooterBarMixin;
    /* access modifiers changed from: private */
    public NothingGlifRecyclerLayout mGlifLayout;
    private HeaderMixin mHeaderMixin;
    /* access modifiers changed from: private */
    public MobileNetworkHelper mMobileNetworkHelper;
    private RecyclerView mRecyclerView;
    private final View.OnClickListener mSkipClickListener = new WizardWifiConnectActivity$$ExternalSyntheticLambda0(this);
    private final BroadcastReceiver mTelephonyReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
            boolean isSimCardPresent = WizardWifiConnectActivity.this.mMobileNetworkHelper.isSimCardPresent(intExtra);
            boolean shouldEnableUseMobileButton = WizardWifiConnectActivity.this.shouldEnableUseMobileButton();
            if (WizardWifiConnectActivity.DEBUG) {
                Log.d("SuwWifiConnectActivity", "onReceive: isSimCardPresent = " + isSimCardPresent + ", shouldEnableUseMobileButton = " + shouldEnableUseMobileButton);
            }
            WizardWifiConnectActivity wizardWifiConnectActivity = WizardWifiConnectActivity.this;
            wizardWifiConnectActivity.updateUseMobileEnableState(wizardWifiConnectActivity.mMobileNetworkHelper.isSimCardPresent(intExtra));
        }
    };
    private final View.OnClickListener mUseMobileNetworkClickListener = new WizardWifiConnectActivity$$ExternalSyntheticLambda1(this);
    private WizardWifiManager mWizardWifiManager;

    public int getMetricsCategory() {
        return 0;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        startActivity(WizardManagerHelper.getNextIntent(getIntent(), 1));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(R$string.use_mobile_network_warning_title).setMessage(R$string.use_mobile_network_warning_content).setPositiveButton(R$string.use_mobile_network_warning_positive_btn, (DialogInterface.OnClickListener) new WizardWifiConnectActivity$$ExternalSyntheticLambda2(this)).setNegativeButton(R$string.use_mobile_network_warning_negative_btn, (DialogInterface.OnClickListener) new WizardWifiConnectActivity$$ExternalSyntheticLambda3()).create();
        create.show();
        create.getButton(-1).setAllCaps(false);
        create.getButton(-2).setAllCaps(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(DialogInterface dialogInterface, int i) {
        this.mMobileNetworkHelper.setWiFiEnabled(false);
        this.mMobileNetworkHelper.setDeviceProvisioningMobileDataEnabled(true);
        startActivity(WizardManagerHelper.getNextIntent(getIntent(), 101));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(R$style.Theme_SetupWizardOverlay);
        setContentView(R$layout.nt_activity_nothing_wizardwifi);
        getWidgets();
        setWidgets();
        addListener();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        this.mWizardWifiManager.onCreate(bundle);
        this.mGlifLayout.setProgressBarShown(true);
        if (this.mMobileNetworkHelper.isWiFiConnectedAndUsable()) {
            onNextStep(-1);
        }
    }

    private void getWidgets() {
        NothingGlifRecyclerLayout nothingGlifRecyclerLayout = (NothingGlifRecyclerLayout) findViewById(R$id.glif_layout);
        this.mGlifLayout = nothingGlifRecyclerLayout;
        this.mHeaderMixin = (HeaderMixin) nothingGlifRecyclerLayout.getMixin(HeaderMixin.class);
        this.mDescriptionMixin = (DescriptionMixin) this.mGlifLayout.getMixin(DescriptionMixin.class);
        this.mFooterBarMixin = (FooterBarMixin) this.mGlifLayout.getMixin(FooterBarMixin.class);
        this.mRecyclerView = this.mGlifLayout.getRecyclerView();
        this.mAdapter = new WifiListAdapter(this);
        this.mWizardWifiManager = new WizardWifiManager(this);
        this.mMobileNetworkHelper = new MobileNetworkHelper(this);
    }

    private void setWidgets() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setAdapter(this.mAdapter);
        RecyclerView recyclerView = this.mRecyclerView;
        recyclerView.removeItemDecoration(recyclerView.getItemDecorationAt(0));
        this.mHeaderMixin.setText(R$string.connect_to_wifi_title);
        this.mHeaderMixin.getTextView().setTypeface(Typeface.create("NDot57", 0));
        this.mHeaderMixin.getTextView().setTextSize(0, getResources().getDimension(R$dimen.nt_header_headline_text_size));
        this.mDescriptionMixin.setText(R$string.connect_to_wifi_subtitle);
        if (!WizardManagerHelper.isPreDeferredSetupWizard(getIntent())) {
            FooterButton.Builder buttonType = new FooterButton.Builder(this).setText(R$string.set_up_offline).setListener(this.mSkipClickListener).setButtonType(7);
            int i = R$style.SudGlifButton_Secondary;
            this.mFooterBarMixin.setSecondaryButton(buttonType.setTheme(i).build());
            this.mFooterBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R$string.use_mobile_network_button_label).setListener(this.mUseMobileNetworkClickListener).setButtonType(7).setTheme(i).build());
        }
        updateUseMobileEnableState(shouldEnableUseMobileButton());
        this.mAdapter.setNeedShowSeeAllButton(true);
    }

    private void addListener() {
        this.mAdapter.setCallBack(new WifiListAdapter.WifiListAdapterCallBack() {
            public void onItemClick(WifiEntry wifiEntry) {
                WizardWifiConnectActivity.this.connect(wifiEntry);
            }

            public void onSeeAllClick() {
                Intent intent = new Intent(WizardWifiConnectActivity.this, WizardAllWifiActivity.class);
                WizardManagerHelper.copyWizardManagerExtras(WizardWifiConnectActivity.this.getIntent(), intent);
                WizardWifiConnectActivity.this.startActivity(intent);
            }
        });
        this.mWizardWifiManager.setCallBack(new WizardWifiManager.WizardWifiManagerCallBack() {
            public void onProgressBarVisible(boolean z) {
                WizardWifiConnectActivity.this.mGlifLayout.setProgressBarShown(z);
            }

            public void updateWifiEntryList(WifiEntry wifiEntry, List<WifiEntry> list) {
                if (wifiEntry != null) {
                    list.add(0, wifiEntry);
                }
                if (!WizardWifiConnectActivity.this.mAdapter.getNeedShowSeeAllButton()) {
                    WizardWifiConnectActivity.this.mAdapter.setData(list);
                } else if (list.size() > 6) {
                    WizardWifiConnectActivity.this.mAdapter.setData(list.subList(0, 6));
                } else {
                    WizardWifiConnectActivity.this.mAdapter.setData(list);
                }
                WizardWifiConnectActivity.this.mAdapter.notifyDataSetChanged();
            }

            public void onConnected() {
                WizardWifiConnectActivity.this.onNextStep(-1);
            }
        });
    }

    public void connect(WifiEntry wifiEntry) {
        this.mWizardWifiManager.startConnect(wifiEntry);
    }

    public void onStart() {
        this.mWizardWifiManager.onStart();
        super.onStart();
        registerReceiver(this.mTelephonyReceiver, new IntentFilter("android.telephony.action.SIM_CARD_STATE_CHANGED"));
        this.mMobileNetworkHelper.setWiFiEnabled(true);
    }

    public void onResume() {
        this.mWizardWifiManager.onResume();
        super.onResume();
    }

    public void onPause() {
        this.mWizardWifiManager.onPause();
        super.onPause();
    }

    public void onStop() {
        this.mWizardWifiManager.onStop();
        super.onStop();
        unregisterReceiver(this.mTelephonyReceiver);
    }

    public void onNextStep(int i) {
        startActivity(WizardManagerHelper.getNextIntent(getIntent(), i));
        TransitionHelper.applyBackwardTransition(this, 2);
    }

    public void updateUseMobileEnableState(boolean z) {
        if (DEBUG) {
            Log.d("SuwWifiConnectActivity", "updateUseMobileEnableState: enable = " + z, new Exception());
        }
        Button primaryButtonView = this.mFooterBarMixin.getPrimaryButtonView();
        if (primaryButtonView != null) {
            primaryButtonView.setEnabled(z);
            primaryButtonView.setAlpha(z ? 1.0f : 0.5f);
        }
    }

    public boolean shouldEnableUseMobileButton() {
        boolean isWiFiRequired = this.mMobileNetworkHelper.isWiFiRequired();
        boolean isSimMissing = this.mMobileNetworkHelper.isSimMissing();
        boolean isMobileNetworkSupport = this.mMobileNetworkHelper.isMobileNetworkSupport();
        boolean isNetworkRoaming = this.mMobileNetworkHelper.isNetworkRoaming();
        if (DEBUG) {
            Log.d("SuwWifiConnectActivity", "isUseMobileButtonEnabled: wiFiRequired = [" + isWiFiRequired + "], simMissing = [" + isSimMissing + "], mobileNetworkSupport = [" + isMobileNetworkSupport + "], networkRoaming = [" + isNetworkRoaming + "]");
        }
        return !isWiFiRequired && !isSimMissing && isMobileNetworkSupport && !isNetworkRoaming;
    }
}
