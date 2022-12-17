package com.android.settings.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Slog;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.settings.R$string;

public class NotificationAccessConfirmationActivity extends Activity implements DialogInterface {
    private ComponentName mComponentName;
    private NotificationManager mNm;
    private int mUserId;

    public void onBackPressed() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        this.mNm = (NotificationManager) getSystemService("notification");
        this.mComponentName = (ComponentName) getIntent().getParcelableExtra("component_name");
        this.mUserId = getIntent().getIntExtra("user_id", -10000);
        ComponentName componentName = this.mComponentName;
        if (componentName == null || componentName.getPackageName() == null) {
            finish();
            return;
        }
        try {
            CharSequence loadSafeLabel = getPackageManager().getApplicationInfo(this.mComponentName.getPackageName(), 0).loadSafeLabel(getPackageManager(), 1000.0f, 5);
            if (TextUtils.isEmpty(loadSafeLabel)) {
                finish();
                return;
            }
            AlertController.AlertParams alertParams = new AlertController.AlertParams(this);
            alertParams.mTitle = getString(R$string.notification_listener_security_warning_title, new Object[]{loadSafeLabel});
            alertParams.mMessage = getString(R$string.notification_listener_security_warning_summary, new Object[]{loadSafeLabel});
            alertParams.mPositiveButtonText = getString(R$string.allow);
            alertParams.mPositiveButtonListener = new NotificationAccessConfirmationActivity$$ExternalSyntheticLambda0(this);
            alertParams.mNegativeButtonText = getString(R$string.deny);
            alertParams.mNegativeButtonListener = new NotificationAccessConfirmationActivity$$ExternalSyntheticLambda1(this);
            AlertController.create(this, this, getWindow()).installContent(alertParams);
            getWindow().setCloseOnTouchOutside(false);
        } catch (PackageManager.NameNotFoundException e) {
            Slog.e("NotificationAccessConfirmationActivity", "Couldn't find app with package name for " + this.mComponentName, e);
            finish();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(DialogInterface dialogInterface, int i) {
        onAllow();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(DialogInterface dialogInterface, int i) {
        cancel();
    }

    public void onResume() {
        super.onResume();
        getWindow().addFlags(524288);
    }

    public void onPause() {
        getWindow().clearFlags(524288);
        super.onPause();
    }

    private void onAllow() {
        try {
            if (!"android.permission.BIND_NOTIFICATION_LISTENER_SERVICE".equals(getPackageManager().getServiceInfo(this.mComponentName, 0).permission)) {
                Slog.e("NotificationAccessConfirmationActivity", "Service " + this.mComponentName + " lacks permission " + "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE");
                return;
            }
            this.mNm.setNotificationListenerAccessGranted(this.mComponentName, true);
            finish();
        } catch (PackageManager.NameNotFoundException e) {
            Slog.e("NotificationAccessConfirmationActivity", "Failed to get service info for " + this.mComponentName, e);
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return AlertActivity.dispatchPopulateAccessibilityEvent(this, accessibilityEvent);
    }

    public void cancel() {
        finish();
    }

    public void dismiss() {
        if (!isFinishing()) {
            finish();
        }
    }
}
