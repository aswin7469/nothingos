package com.android.settings.password;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class ForgotPasswordActivity extends Activity {
    public static final String TAG = "ForgotPasswordActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int intExtra = getIntent().getIntExtra("android.intent.extra.USER_ID", -1);
        if (intExtra < 0) {
            Log.e(TAG, "No valid userId supplied, exiting");
            finish();
            return;
        }
        setContentView(R$layout.forgot_password_activity);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DevicePolicyManager.class);
        ((TextView) findViewById(R$id.forgot_password_text)).setText(devicePolicyManager.getResources().getString("Settings.FORGOT_PASSWORD_TEXT", new ForgotPasswordActivity$$ExternalSyntheticLambda0(this)));
        GlifLayout glifLayout = (GlifLayout) findViewById(R$id.setup_wizard_layout);
        ((FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class)).setPrimaryButton(new FooterButton.Builder(this).setText(17039370).setListener(new ForgotPasswordActivity$$ExternalSyntheticLambda1(this)).setButtonType(4).setTheme(R$style.SudGlifButton_Primary).build());
        glifLayout.setHeaderText((CharSequence) devicePolicyManager.getResources().getString("Settings.FORGOT_PASSWORD_TITLE", new ForgotPasswordActivity$$ExternalSyntheticLambda2(this)));
        UserManager.get(this).requestQuietModeEnabled(false, UserHandle.of(intExtra), 2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreate$0() {
        return getString(R$string.forgot_password_text);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        finish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$onCreate$2() {
        return getString(R$string.forgot_password_title);
    }
}
