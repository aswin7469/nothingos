package com.android.settings.wifi;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.wifi.dpp.WifiDppUtils;
import com.android.wifitrackerlib.WifiEntry;

public class AddNetworkFragment extends InstrumentedFragment implements WifiConfigUiBase2, View.OnClickListener {
    static final int CANCEL_BUTTON_ID = 16908314;
    static final int SSID_SCANNER_BUTTON_ID = R$id.ssid_scanner_button;
    static final int SUBMIT_BUTTON_ID = 16908313;
    private Button mCancelBtn;
    private Button mSubmitBtn;
    private WifiConfigController2 mUIController;

    public Button getForgetButton() {
        return null;
    }

    public int getMetricsCategory() {
        return 1556;
    }

    public int getMode() {
        return 1;
    }

    public void setForgetButton(CharSequence charSequence) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R$layout.wifi_add_network_view, viewGroup, false);
        Button button = (Button) inflate.findViewById(16908315);
        if (button != null) {
            button.setVisibility(8);
        }
        this.mSubmitBtn = (Button) inflate.findViewById(SUBMIT_BUTTON_ID);
        this.mCancelBtn = (Button) inflate.findViewById(CANCEL_BUTTON_ID);
        this.mSubmitBtn.setOnClickListener(this);
        this.mCancelBtn.setOnClickListener(this);
        ((ImageButton) inflate.findViewById(SSID_SCANNER_BUTTON_ID)).setOnClickListener(this);
        this.mUIController = new WifiConfigController2(this, inflate, (WifiEntry) null, getMode());
        getActivity().getWindow().setSoftInputMode(16);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        this.mUIController.showSecurityFields(false, true);
    }

    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        this.mUIController.updatePassword();
    }

    public void onClick(View view) {
        if (view.getId() == SUBMIT_BUTTON_ID) {
            handleSubmitAction();
        } else if (view.getId() == CANCEL_BUTTON_ID) {
            handleCancelAction();
        } else if (view.getId() == SSID_SCANNER_BUTTON_ID) {
            startActivityForResult(WifiDppUtils.getEnrolleeQrCodeScannerIntent(view.getContext(), ((TextView) getView().findViewById(R$id.ssid)).getText().toString()), 0);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0 && i2 == -1) {
            successfullyFinish((WifiConfiguration) intent.getParcelableExtra("key_wifi_configuration"));
        }
    }

    public void dispatchSubmit() {
        handleSubmitAction();
    }

    public void setTitle(int i) {
        getActivity().setTitle(i);
    }

    public void setTitle(CharSequence charSequence) {
        getActivity().setTitle(charSequence);
    }

    public void setSubmitButton(CharSequence charSequence) {
        this.mSubmitBtn.setText(charSequence);
    }

    public void setCancelButton(CharSequence charSequence) {
        this.mCancelBtn.setText(charSequence);
    }

    public Button getSubmitButton() {
        return this.mSubmitBtn;
    }

    /* access modifiers changed from: package-private */
    public void handleSubmitAction() {
        successfullyFinish(this.mUIController.getConfig());
    }

    private void successfullyFinish(WifiConfiguration wifiConfiguration) {
        Intent intent = new Intent();
        FragmentActivity activity = getActivity();
        intent.putExtra("wifi_config_key", wifiConfiguration);
        activity.setResult(-1, intent);
        activity.finish();
    }

    /* access modifiers changed from: package-private */
    public void handleCancelAction() {
        FragmentActivity activity = getActivity();
        activity.setResult(0);
        activity.finish();
    }
}
