package com.nt.settings.wifi.seeall;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.settings.R;
import com.android.wifitrackerlib.WifiEntry;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.transition.TransitionHelper;
import com.nt.settings.wifi.WizardWifiManager;
import java.util.List;
/* loaded from: classes2.dex */
public class NothingWizardAllWifiActivity extends WizardListActivity {
    private List<WifiEntry> mWifiEntries;
    private WizardWifiManager mWizardWifiManager;

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public String getRightFunctionText() {
        return null;
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public void onRightFunctionClick() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nt.settings.wifi.seeall.WizardListActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        WizardWifiManager wizardWifiManager = new WizardWifiManager(this);
        this.mWizardWifiManager = wizardWifiManager;
        wizardWifiManager.onCreate(bundle);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        this.mWizardWifiManager.setCallBack(new WizardWifiManager.WizardWifiManagerCallBack() { // from class: com.nt.settings.wifi.seeall.NothingWizardAllWifiActivity.1
            @Override // com.nt.settings.wifi.WizardWifiManager.WizardWifiManagerCallBack
            public void onProgressBarVisible(boolean z) {
                NothingWizardAllWifiActivity.this.getProgressBar().setVisibility(z ? 0 : 4);
            }

            @Override // com.nt.settings.wifi.WizardWifiManager.WizardWifiManagerCallBack
            public void updateWifiEntryList(WifiEntry wifiEntry, List<WifiEntry> list) {
                if (wifiEntry != null) {
                    list.add(0, wifiEntry);
                }
                NothingWizardAllWifiActivity.this.mWifiEntries = list;
                NothingWizardAllWifiActivity nothingWizardAllWifiActivity = NothingWizardAllWifiActivity.this;
                nothingWizardAllWifiActivity.showData(nothingWizardAllWifiActivity.mWifiEntries);
            }

            @Override // com.nt.settings.wifi.WizardWifiManager.WizardWifiManagerCallBack
            public void onConnected() {
                NothingWizardAllWifiActivity.this.startActivityForResult(WizardManagerHelper.getNextIntent(NothingWizardAllWifiActivity.this.getIntent(), -1), 1);
                TransitionHelper.applyBackwardTransition(NothingWizardAllWifiActivity.this, 2);
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        this.mWizardWifiManager.onStart();
        super.onStart();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        this.mWizardWifiManager.onResume();
        super.onResume();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mWizardWifiManager.onPause();
        super.onPause();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        this.mWizardWifiManager.onStop();
        super.onStop();
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public String getTitleText() {
        return getResources().getString(R.string.nothing_wizard_all_wifi_title);
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public String getSubTitleText() {
        return getResources().getString(R.string.nothing_wizard_all_wifi_sub_title);
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public String getLeftFunctionText() {
        if (WizardManagerHelper.isPreDeferredSetupWizard(getIntent())) {
            return null;
        }
        return getResources().getString(R.string.set_up_offline);
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public int getRippleColor() {
        return getResources().getColor(R.color.colorAccent);
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public int getItemResId() {
        return R.layout.layout_wifi_item;
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public View getFooterView() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_wifi_seeall, (ViewGroup) null, false);
        ((TextView) inflate.findViewById(R.id.layout_wifi_item_title_textview)).setText(R.string.add_new_network);
        RippleUtils.getInstance().setRippleEffect(inflate);
        inflate.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.wifi.seeall.NothingWizardAllWifiActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NothingWizardAllWifiActivity.this.lambda$getFooterView$0(view);
            }
        });
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getFooterView$0(View view) {
        this.mWizardWifiManager.launchAddNetworkFragment();
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public void onBack() {
        TransitionHelper.applyBackwardTransition(this, 2);
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public void onLeftFunctionClick() {
        startActivityForResult(WizardManagerHelper.getNextIntent(getIntent(), 1), 1);
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public void onItemShow(View view, Object obj) {
        ((NothingWizardWifiItemLayout) view).showData((WifiEntry) obj);
    }

    @Override // com.nt.settings.wifi.seeall.WizardListActivity
    public void onItemClick(View view, Object obj) {
        this.mWizardWifiManager.startConnect((WifiEntry) obj);
    }
}
