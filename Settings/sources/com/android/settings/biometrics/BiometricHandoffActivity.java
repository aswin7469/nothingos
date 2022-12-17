package com.android.settings.biometrics;

import android.os.Bundle;
import android.view.View;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;

public class BiometricHandoffActivity extends BiometricEnrollBase {
    private FooterButton mPrimaryFooterButton;

    public int getMetricsCategory() {
        return 1894;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.biometric_handoff);
        setHeaderText(R$string.biometric_settings_hand_back_to_guardian);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(getPrimaryFooterButton());
    }

    /* access modifiers changed from: protected */
    public FooterButton getPrimaryFooterButton() {
        if (this.mPrimaryFooterButton == null) {
            this.mPrimaryFooterButton = new FooterButton.Builder(this).setText(R$string.biometric_settings_hand_back_to_guardian_ok).setButtonType(5).setListener(new BiometricHandoffActivity$$ExternalSyntheticLambda0(this)).setTheme(R$style.SudGlifButton_Primary).build();
        }
        return this.mPrimaryFooterButton;
    }

    /* access modifiers changed from: protected */
    public void onNextButtonClick(View view) {
        setResult(-1);
        finish();
    }
}
