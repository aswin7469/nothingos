package com.android.internal.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.app.AlertController;
/* loaded from: classes4.dex */
public class AppSwitchWarningActivity extends AlertActivity implements DialogInterface.OnClickListener {
    public static final String EXTRA_SOURCE_PACAKGE_NAME = "source_package_name";
    public static final String EXTRA_SOURCE_PACAKGE_UID = "source_package_uid";
    public static final String EXTRA_TARGET_PACAKGE_NAME = "target_package_name";
    public static final String EXTRA_TARGET_PACAKGE_UID = "target_package_uid";
    private static final String TAG = AppSwitchWarningActivity.class.getSimpleName();
    private String mSourcePackageName;
    private int mSourcePackageUid;
    private IntentSender mTarget;
    private String mTargetPackageName;
    private int mTargetPackageUid;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.app.AlertActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        CharSequence sourceLable;
        CharSequence targetLable;
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.mSourcePackageName = intent.getStringExtra(EXTRA_SOURCE_PACAKGE_NAME);
        this.mSourcePackageUid = intent.getIntExtra(EXTRA_SOURCE_PACAKGE_UID, 0);
        this.mTargetPackageName = intent.getStringExtra(EXTRA_TARGET_PACAKGE_NAME);
        this.mTargetPackageUid = intent.getIntExtra(EXTRA_TARGET_PACAKGE_UID, 0);
        IntentSender intentSender = (IntentSender) intent.getParcelableExtra(Intent.EXTRA_INTENT);
        this.mTarget = intentSender;
        if (this.mSourcePackageName == null || this.mTargetPackageName == null || intentSender == null) {
            String str = TAG;
            Log.wtf(str, "Invalid intent: " + intent.toString());
            finish();
        }
        try {
            ApplicationInfo targetApplicationInfo = getPackageManager().getApplicationInfo(this.mTargetPackageName, 0);
            try {
                ApplicationInfo sourceApplicationInfo = getPackageManager().getApplicationInfo(this.mSourcePackageName, 0);
                AlertController.AlertParams p = this.mAlertParams;
                if (sourceApplicationInfo != null) {
                    sourceLable = sourceApplicationInfo.loadSafeLabel(getPackageManager(), 1000.0f, 5);
                } else {
                    sourceLable = this.mSourcePackageName;
                }
                if (targetApplicationInfo != null) {
                    targetLable = targetApplicationInfo.loadSafeLabel(getPackageManager(), 1000.0f, 5);
                } else {
                    targetLable = this.mTargetPackageName;
                }
                p.mTitle = getString(R.string.app_switch_warning_title, sourceLable, targetLable);
                p.mPositiveButtonText = getString(R.string.app_switch_warning_open);
                p.mPositiveButtonListener = this;
                p.mNegativeButtonText = getString(R.string.app_switch_warning_cancel);
                p.mNegativeButtonListener = this;
                this.mAlert.installContent(this.mAlertParams);
            } catch (PackageManager.NameNotFoundException e) {
                String str2 = TAG;
                Log.e(str2, "2Could not show warning because package does not exist " + this.mSourcePackageName, e);
                finish();
            }
        } catch (PackageManager.NameNotFoundException e2) {
            String str3 = TAG;
            Log.e(str3, "1Could not show warning because package does not exist " + this.mTargetPackageName, e2);
            finish();
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        String str = TAG;
        Log.d(str, "onClick: " + which);
        switch (which) {
            case -2:
                EventLogTags.writeAppSwitchWarningCalcel(this.mSourcePackageName, this.mTargetPackageName);
                finish();
                return;
            case -1:
                IntentSender target = (IntentSender) getIntent().getParcelableExtra(Intent.EXTRA_INTENT);
                try {
                    startIntentSenderForResult(target, -1, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Error while starting intent sender", e);
                }
                EventLogTags.writeAppSwitchWarningOpen(this.mSourcePackageName, this.mTargetPackageName);
                finish();
                return;
            default:
                return;
        }
    }

    public static Intent createAppSwitchWarningIntent(Context context, String sourcePackageName, int sourcePackageUid, String targetPackageName, int targetPackageUid, IntentSender target) {
        Intent intent = new Intent();
        intent.setClass(context, AppSwitchWarningActivity.class);
        intent.putExtra(EXTRA_SOURCE_PACAKGE_NAME, sourcePackageName);
        intent.putExtra(EXTRA_SOURCE_PACAKGE_UID, sourcePackageUid);
        intent.putExtra(EXTRA_TARGET_PACAKGE_NAME, targetPackageName);
        intent.putExtra(EXTRA_TARGET_PACAKGE_UID, targetPackageUid);
        intent.putExtra(Intent.EXTRA_INTENT, target);
        return intent;
    }
}
