package com.android.settings.nfc;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.settings.R;
import com.android.settings.nfc.PaymentBackend;
/* loaded from: classes.dex */
public final class PaymentDefaultDialog extends AlertActivity implements DialogInterface.OnClickListener {
    private PaymentBackend mBackend;
    private ComponentName mNewDefault;

    /* JADX WARN: Multi-variable type inference failed */
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        try {
            this.mBackend = new PaymentBackend(this);
        } catch (NullPointerException unused) {
            finish();
        }
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("category");
        setResult(0);
        if (!buildDialog((ComponentName) intent.getParcelableExtra("component"), stringExtra)) {
            finish();
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i != -1) {
            return;
        }
        this.mBackend.setDefaultPaymentApp(this.mNewDefault);
        setResult(-1);
    }

    private boolean buildDialog(ComponentName componentName, String str) {
        if (componentName == null || str == null) {
            Log.e("PaymentDefaultDialog", "Component or category are null");
            return false;
        } else if (!"payment".equals(str)) {
            Log.e("PaymentDefaultDialog", "Don't support defaults for category " + str);
            return false;
        } else {
            PaymentBackend.PaymentAppInfo paymentAppInfo = null;
            PaymentBackend.PaymentAppInfo paymentAppInfo2 = null;
            for (PaymentBackend.PaymentAppInfo paymentAppInfo3 : this.mBackend.getPaymentAppInfos()) {
                if (componentName.equals(paymentAppInfo3.componentName)) {
                    paymentAppInfo = paymentAppInfo3;
                }
                if (paymentAppInfo3.isDefault) {
                    paymentAppInfo2 = paymentAppInfo3;
                }
            }
            if (paymentAppInfo == null) {
                Log.e("PaymentDefaultDialog", "Component " + componentName + " is not a registered payment service.");
                return false;
            }
            ComponentName defaultPaymentApp = this.mBackend.getDefaultPaymentApp();
            if (defaultPaymentApp != null && defaultPaymentApp.equals(componentName)) {
                Log.e("PaymentDefaultDialog", "Component " + componentName + " is already default.");
                return false;
            }
            this.mNewDefault = componentName;
            AlertController.AlertParams alertParams = ((AlertActivity) this).mAlertParams;
            if (paymentAppInfo2 == null) {
                alertParams.mTitle = getString(R.string.nfc_payment_set_default_label);
                alertParams.mMessage = String.format(getString(R.string.nfc_payment_set_default), sanitizePaymentAppCaption(paymentAppInfo.label.toString()));
                alertParams.mPositiveButtonText = getString(R.string.nfc_payment_btn_text_set_deault);
            } else {
                alertParams.mTitle = getString(R.string.nfc_payment_update_default_label);
                alertParams.mMessage = String.format(getString(R.string.nfc_payment_set_default_instead_of), sanitizePaymentAppCaption(paymentAppInfo.label.toString()), sanitizePaymentAppCaption(paymentAppInfo2.label.toString()));
                alertParams.mPositiveButtonText = getString(R.string.nfc_payment_btn_text_update);
            }
            alertParams.mNegativeButtonText = getString(R.string.cancel);
            alertParams.mPositiveButtonListener = this;
            alertParams.mNegativeButtonListener = this;
            setupAlert();
            return true;
        }
    }

    private String sanitizePaymentAppCaption(String str) {
        String trim = str.replace('\n', ' ').replace('\r', ' ').trim();
        return trim.length() > 40 ? trim.substring(0, 40) : trim;
    }
}
