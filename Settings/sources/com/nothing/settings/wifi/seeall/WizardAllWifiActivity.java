package com.nothing.settings.wifi.seeall;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.settings.R$color;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.wifitrackerlib.WifiEntry;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.transition.TransitionHelper;
import com.nothing.settings.wifi.WizardWifiManager;
import java.util.List;

public class WizardAllWifiActivity extends WizardListActivity {
    /* access modifiers changed from: private */
    public List<WifiEntry> mWifiEntries;
    private WizardWifiManager mWizardWifiManager;

    public String getRightFunctionText() {
        return null;
    }

    public void onRightFunctionClick() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        WizardWifiManager wizardWifiManager = new WizardWifiManager(this);
        this.mWizardWifiManager = wizardWifiManager;
        wizardWifiManager.onCreate(bundle);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        this.mWizardWifiManager.setCallBack(new WizardWifiManager.WizardWifiManagerCallBack() {
            public void onProgressBarVisible(boolean z) {
                WizardAllWifiActivity.this.getProgressBar().setVisibility(z ? 0 : 4);
            }

            public void updateWifiEntryList(WifiEntry wifiEntry, List<WifiEntry> list) {
                if (wifiEntry != null) {
                    list.add(0, wifiEntry);
                }
                WizardAllWifiActivity.this.mWifiEntries = list;
                WizardAllWifiActivity wizardAllWifiActivity = WizardAllWifiActivity.this;
                wizardAllWifiActivity.showData(wizardAllWifiActivity.mWifiEntries);
            }

            public void onConnected() {
                WizardAllWifiActivity wizardAllWifiActivity = WizardAllWifiActivity.this;
                wizardAllWifiActivity.startActivityForResult(WizardManagerHelper.getNextIntent(wizardAllWifiActivity.getIntent(), -1), 1);
                TransitionHelper.applyBackwardTransition(WizardAllWifiActivity.this, 2);
            }
        });
    }

    public void onStart() {
        this.mWizardWifiManager.onStart();
        super.onStart();
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
    }

    public String getTitleText() {
        return getResources().getString(R$string.nothing_wizard_all_wifi_title);
    }

    public String getSubTitleText() {
        return getResources().getString(R$string.nothing_wizard_all_wifi_sub_title);
    }

    public String getLeftFunctionText() {
        if (WizardManagerHelper.isPreDeferredSetupWizard(getIntent())) {
            return null;
        }
        return getResources().getString(R$string.set_up_offline);
    }

    public int getRippleColor() {
        return getResources().getColor(R$color.colorAccent);
    }

    public int getItemResId() {
        return R$layout.layout_wifi_item;
    }

    public View getFooterView() {
        View inflate = LayoutInflater.from(this).inflate(R$layout.layout_wifi_seeall, (ViewGroup) null, false);
        ((TextView) inflate.findViewById(R$id.layout_wifi_item_title_textview)).setText(R$string.add_new_network);
        RippleUtils.getInstance().setRippleEffect(inflate);
        inflate.setOnClickListener(new WizardAllWifiActivity$$ExternalSyntheticLambda0(this));
        return inflate;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getFooterView$0(View view) {
        this.mWizardWifiManager.launchAddNetworkFragment();
    }

    public void onBack() {
        TransitionHelper.applyBackwardTransition(this, 2);
    }

    public void onLeftFunctionClick() {
        startActivityForResult(WizardManagerHelper.getNextIntent(getIntent(), 1), 1);
    }

    public void onItemClick(View view, Object obj) {
        this.mWizardWifiManager.startConnect((WifiEntry) obj);
    }

    public void onItemShow(View view, Object obj) {
        ((WizardWifiItemLayout) view).showData((WifiEntry) obj);
    }
}
