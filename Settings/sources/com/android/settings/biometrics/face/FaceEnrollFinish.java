package com.android.settings.biometrics.face;

import android.os.Bundle;
import android.view.View;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;

public class FaceEnrollFinish extends BiometricEnrollBase {
    public int getMetricsCategory() {
        return 1508;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.face_enroll_finish);
        setHeaderText(R$string.security_settings_face_enroll_finish_title);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R$string.security_settings_face_enroll_done).setListener(new FaceEnrollFinish$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(R$style.SudGlifButton_Primary).build());
    }

    public void onNextButtonClick(View view) {
        setResult(1);
        finish();
    }
}
