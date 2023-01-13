package com.android.systemui.hdmi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.p014tv.TvBottomSheetActivity;
import javax.inject.Inject;

public class HdmiCecSetMenuLanguageActivity extends TvBottomSheetActivity implements View.OnClickListener {
    private static final String TAG = "HdmiCecSetMenuLanguageActivity";
    private final HdmiCecSetMenuLanguageHelper mHdmiCecSetMenuLanguageHelper;

    @Inject
    public HdmiCecSetMenuLanguageActivity(HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper) {
        this.mHdmiCecSetMenuLanguageHelper = hdmiCecSetMenuLanguageHelper;
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        this.mHdmiCecSetMenuLanguageHelper.setLocale(getIntent().getStringExtra("android.hardware.hdmi.extra.LOCALE"));
        if (this.mHdmiCecSetMenuLanguageHelper.isLocaleDenylisted()) {
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        initUI(getString(C1894R.string.hdmi_cec_set_menu_language_title, new Object[]{this.mHdmiCecSetMenuLanguageHelper.getLocale().getDisplayLanguage()}), getString(C1894R.string.hdmi_cec_set_menu_language_description));
    }

    public void onClick(View view) {
        if (view.getId() == C1894R.C1898id.bottom_sheet_positive_button) {
            this.mHdmiCecSetMenuLanguageHelper.acceptLocale();
        } else {
            this.mHdmiCecSetMenuLanguageHelper.declineLocale();
        }
        finish();
    }

    /* access modifiers changed from: package-private */
    public void initUI(CharSequence charSequence, CharSequence charSequence2) {
        Button button = (Button) findViewById(C1894R.C1898id.bottom_sheet_positive_button);
        Button button2 = (Button) findViewById(C1894R.C1898id.bottom_sheet_negative_button);
        ((TextView) findViewById(C1894R.C1898id.bottom_sheet_title)).setText(charSequence);
        ((TextView) findViewById(C1894R.C1898id.bottom_sheet_body)).setText(charSequence2);
        ((ImageView) findViewById(C1894R.C1898id.bottom_sheet_icon)).setImageResource(17302849);
        ((ImageView) findViewById(C1894R.C1898id.bottom_sheet_second_icon)).setVisibility(8);
        button.setText(C1894R.string.hdmi_cec_set_menu_language_accept);
        button.setOnClickListener(this);
        button2.setText(C1894R.string.hdmi_cec_set_menu_language_decline);
        button2.setOnClickListener(this);
        button2.requestFocus();
    }
}
