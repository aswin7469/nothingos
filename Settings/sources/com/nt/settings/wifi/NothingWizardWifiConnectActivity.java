package com.nt.settings.wifi;

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
import androidx.constraintlayout.widget.R$styleable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.core.InstrumentedActivity;
import com.android.wifitrackerlib.WifiEntry;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifRecyclerLayout;
import com.google.android.setupdesign.template.DescriptionMixin;
import com.google.android.setupdesign.template.HeaderMixin;
import com.google.android.setupdesign.transition.TransitionHelper;
import com.nt.settings.wifi.WifiListAdapter;
import com.nt.settings.wifi.WizardWifiManager;
import com.nt.settings.wifi.seeall.NothingWizardAllWifiActivity;
import java.util.List;
/* loaded from: classes2.dex */
public class NothingWizardWifiConnectActivity extends InstrumentedActivity {
    private static final boolean DEBUG = Log.isLoggable("SuwWifiConnectActivity", 3);
    private WifiListAdapter mAdapter;
    private DescriptionMixin mDescriptionMixin;
    private FooterBarMixin mFooterBarMixin;
    private GlifRecyclerLayout mGlifLayout;
    private HeaderMixin mHeaderMixin;
    private NothingMobileNetworkHelper mMobileNetworkHelper;
    private RecyclerView mRecyclerView;
    private WizardWifiManager mWizardWifiManager;
    private final BroadcastReceiver mTelephonyReceiver = new BroadcastReceiver() { // from class: com.nt.settings.wifi.NothingWizardWifiConnectActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
            boolean isSimCardPresent = NothingWizardWifiConnectActivity.this.mMobileNetworkHelper.isSimCardPresent(intExtra);
            boolean shouldEnableUseMobileButton = NothingWizardWifiConnectActivity.this.shouldEnableUseMobileButton();
            if (NothingWizardWifiConnectActivity.DEBUG) {
                Log.d("SuwWifiConnectActivity", "onReceive: isSimCardPresent = " + isSimCardPresent + ", shouldEnableUseMobileButton = " + shouldEnableUseMobileButton);
            }
            NothingWizardWifiConnectActivity nothingWizardWifiConnectActivity = NothingWizardWifiConnectActivity.this;
            nothingWizardWifiConnectActivity.updateUseMobileEnableState(nothingWizardWifiConnectActivity.mMobileNetworkHelper.isSimCardPresent(intExtra));
        }
    };
    private final View.OnClickListener mSkipClickListener = new View.OnClickListener() { // from class: com.nt.settings.wifi.NothingWizardWifiConnectActivity$$ExternalSyntheticLambda2
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NothingWizardWifiConnectActivity.this.lambda$new$0(view);
        }
    };
    private final View.OnClickListener mUseMobileNetworkClickListener = new View.OnClickListener() { // from class: com.nt.settings.wifi.NothingWizardWifiConnectActivity$$ExternalSyntheticLambda3
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NothingWizardWifiConnectActivity.this.lambda$new$3(view);
        }
    };

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        startActivity(WizardManagerHelper.getNextIntent(getIntent(), 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(R.string.use_mobile_network_warning_title).setMessage(R.string.use_mobile_network_warning_content).setPositiveButton(R.string.use_mobile_network_warning_positive_btn, new DialogInterface.OnClickListener() { // from class: com.nt.settings.wifi.NothingWizardWifiConnectActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                NothingWizardWifiConnectActivity.this.lambda$new$1(dialogInterface, i);
            }
        }).setNegativeButton(R.string.use_mobile_network_warning_negative_btn, NothingWizardWifiConnectActivity$$ExternalSyntheticLambda1.INSTANCE).create();
        create.show();
        create.getButton(-2).setAllCaps(false);
        create.getButton(-1).setAllCaps(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(DialogInterface dialogInterface, int i) {
        this.mMobileNetworkHelper.setWiFiEnabled(false);
        this.mMobileNetworkHelper.setDeviceProvisioningMobileDataEnabled(true);
        startActivity(WizardManagerHelper.getNextIntent(getIntent(), R$styleable.Constraint_layout_goneMarginRight));
    }

    @Override // com.android.settings.core.InstrumentedActivity, com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(R.style.Theme_SetupWizardOverlay);
        setContentView(R.layout.activity_nothing_wizardwifi);
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
        GlifRecyclerLayout glifRecyclerLayout = (GlifRecyclerLayout) findViewById(R.id.glif_layout);
        this.mGlifLayout = glifRecyclerLayout;
        this.mHeaderMixin = (HeaderMixin) glifRecyclerLayout.getMixin(HeaderMixin.class);
        this.mDescriptionMixin = (DescriptionMixin) this.mGlifLayout.getMixin(DescriptionMixin.class);
        this.mFooterBarMixin = (FooterBarMixin) this.mGlifLayout.getMixin(FooterBarMixin.class);
        this.mRecyclerView = this.mGlifLayout.getRecyclerView();
        this.mAdapter = new WifiListAdapter(this);
        this.mWizardWifiManager = new WizardWifiManager(this);
        this.mMobileNetworkHelper = new NothingMobileNetworkHelper(this);
    }

    private void setWidgets() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setAdapter(this.mAdapter);
        RecyclerView recyclerView = this.mRecyclerView;
        recyclerView.removeItemDecoration(recyclerView.getItemDecorationAt(0));
        this.mHeaderMixin.setText(R.string.connect_to_wifi_title);
        this.mHeaderMixin.getTextView().setTypeface(Typeface.create("NDot57", 0));
        this.mHeaderMixin.getTextView().setTextSize(0, getResources().getDimension(R.dimen.header_headline_text_size));
        this.mDescriptionMixin.setText(R.string.connect_to_wifi_subtitle);
        if (!WizardManagerHelper.isPreDeferredSetupWizard(getIntent())) {
            FooterButton.Builder buttonType = new FooterButton.Builder(this).setText(R.string.set_up_offline).setListener(this.mSkipClickListener).setButtonType(7);
            int i = R.style.SudGlifButton_Secondary;
            this.mFooterBarMixin.setSecondaryButton(buttonType.setTheme(i).build());
            this.mFooterBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.use_mobile_network_button_label).setListener(this.mUseMobileNetworkClickListener).setButtonType(7).setTheme(i).build());
        }
        updateUseMobileEnableState(shouldEnableUseMobileButton());
        this.mAdapter.setNeedShowSeeAllButton(true);
    }

    private void addListener() {
        this.mAdapter.setCallBack(new WifiListAdapter.WifiListAdapterCallBack() { // from class: com.nt.settings.wifi.NothingWizardWifiConnectActivity.2
            @Override // com.nt.settings.wifi.WifiListAdapter.WifiListAdapterCallBack
            public void onItemClick(WifiEntry wifiEntry) {
                NothingWizardWifiConnectActivity.this.connect(wifiEntry);
            }

            @Override // com.nt.settings.wifi.WifiListAdapter.WifiListAdapterCallBack
            public void onSeeAllClick() {
                Intent intent = new Intent(NothingWizardWifiConnectActivity.this, NothingWizardAllWifiActivity.class);
                WizardManagerHelper.copyWizardManagerExtras(NothingWizardWifiConnectActivity.this.getIntent(), intent);
                NothingWizardWifiConnectActivity.this.startActivity(intent);
            }
        });
        this.mWizardWifiManager.setCallBack(new WizardWifiManager.WizardWifiManagerCallBack() { // from class: com.nt.settings.wifi.NothingWizardWifiConnectActivity.3
            @Override // com.nt.settings.wifi.WizardWifiManager.WizardWifiManagerCallBack
            public void onProgressBarVisible(boolean z) {
                NothingWizardWifiConnectActivity.this.mGlifLayout.setProgressBarShown(z);
            }

            @Override // com.nt.settings.wifi.WizardWifiManager.WizardWifiManagerCallBack
            public void updateWifiEntryList(WifiEntry wifiEntry, List<WifiEntry> list) {
                if (wifiEntry != null) {
                    list.add(0, wifiEntry);
                }
                if (!NothingWizardWifiConnectActivity.this.mAdapter.getNeedShowSeeAllButton()) {
                    NothingWizardWifiConnectActivity.this.mAdapter.setData(list);
                } else if (list.size() > 6) {
                    NothingWizardWifiConnectActivity.this.mAdapter.setData(list.subList(0, 6));
                } else {
                    NothingWizardWifiConnectActivity.this.mAdapter.setData(list);
                }
                NothingWizardWifiConnectActivity.this.mAdapter.notifyDataSetChanged();
            }

            @Override // com.nt.settings.wifi.WizardWifiManager.WizardWifiManagerCallBack
            public void onConnected() {
                NothingWizardWifiConnectActivity.this.onNextStep(-1);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connect(WifiEntry wifiEntry) {
        this.mWizardWifiManager.startConnect(wifiEntry);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        this.mWizardWifiManager.onStart();
        super.onStart();
        registerReceiver(this.mTelephonyReceiver, new IntentFilter("android.telephony.action.SIM_CARD_STATE_CHANGED"));
        this.mMobileNetworkHelper.setWiFiEnabled(true);
        this.mMobileNetworkHelper.setDeviceProvisioningMobileDataEnabled(false);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        this.mWizardWifiManager.onResume();
        super.onResume();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mWizardWifiManager.onPause();
        super.onPause();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        this.mWizardWifiManager.onStop();
        super.onStop();
        unregisterReceiver(this.mTelephonyReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNextStep(int i) {
        startActivity(WizardManagerHelper.getNextIntent(getIntent(), i));
        TransitionHelper.applyBackwardTransition(this, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseMobileEnableState(boolean z) {
        if (DEBUG) {
            Log.d("SuwWifiConnectActivity", "updateUseMobileEnableState: enable = " + z, new Exception());
        }
        Button primaryButtonView = this.mFooterBarMixin.getPrimaryButtonView();
        if (primaryButtonView == null) {
            return;
        }
        primaryButtonView.setEnabled(z);
        primaryButtonView.setAlpha(z ? 1.0f : 0.5f);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
