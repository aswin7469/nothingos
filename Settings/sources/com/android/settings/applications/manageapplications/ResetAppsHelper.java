package com.android.settings.applications.manageapplications;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.INotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.net.NetworkPolicyManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$array;
import com.android.settings.R$string;
import java.util.Arrays;
import java.util.List;

public class ResetAppsHelper implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    /* access modifiers changed from: private */
    public final AppOpsManager mAom;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final IPackageManager mIPm = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
    /* access modifiers changed from: private */
    public final INotificationManager mNm = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    /* access modifiers changed from: private */
    public final NetworkPolicyManager mNpm;
    /* access modifiers changed from: private */
    public final PackageManager mPm;
    private AlertDialog mResetDialog;
    /* access modifiers changed from: private */
    public final UserManager mUm;

    public ResetAppsHelper(Context context) {
        this.mContext = context;
        this.mPm = context.getPackageManager();
        this.mNpm = NetworkPolicyManager.from(context);
        this.mAom = (AppOpsManager) context.getSystemService("appops");
        this.mUm = (UserManager) context.getSystemService("user");
    }

    public void onRestoreInstanceState(Bundle bundle) {
        if (bundle != null && bundle.getBoolean("resetDialog")) {
            buildResetDialog();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.mResetDialog != null) {
            bundle.putBoolean("resetDialog", true);
        }
    }

    public void stop() {
        AlertDialog alertDialog = this.mResetDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mResetDialog = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void buildResetDialog() {
        if (this.mResetDialog == null) {
            this.mResetDialog = new AlertDialog.Builder(this.mContext).setTitle(R$string.reset_app_preferences_title).setMessage(R$string.reset_app_preferences_desc).setPositiveButton(R$string.reset_app_preferences_button, (DialogInterface.OnClickListener) this).setNegativeButton(R$string.cancel, (DialogInterface.OnClickListener) null).setOnDismissListener(this).show();
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (this.mResetDialog == dialogInterface) {
            this.mResetDialog = null;
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.mResetDialog == dialogInterface) {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    List asList = Arrays.asList(ResetAppsHelper.this.mContext.getResources().getStringArray(R$array.config_skip_reset_apps_package_name));
                    for (UserHandle identifier : ResetAppsHelper.this.mUm.getEnabledProfiles()) {
                        int identifier2 = identifier.getIdentifier();
                        List installedApplicationsAsUser = ResetAppsHelper.this.mPm.getInstalledApplicationsAsUser(512, identifier2);
                        for (int i = 0; i < installedApplicationsAsUser.size(); i++) {
                            ApplicationInfo applicationInfo = (ApplicationInfo) installedApplicationsAsUser.get(i);
                            if (!asList.contains(applicationInfo.packageName)) {
                                try {
                                    ResetAppsHelper.this.mNm.clearData(applicationInfo.packageName, applicationInfo.uid, false);
                                } catch (RemoteException unused) {
                                }
                                if (!applicationInfo.enabled) {
                                    try {
                                        if (ResetAppsHelper.this.mIPm.getApplicationEnabledSetting(applicationInfo.packageName, identifier2) == 3) {
                                            ResetAppsHelper.this.mIPm.setApplicationEnabledSetting(applicationInfo.packageName, 0, 1, identifier2, ResetAppsHelper.this.mContext.getPackageName());
                                        }
                                    } catch (RemoteException e) {
                                        Log.e("ResetAppsHelper", "Error during reset disabled apps.", e);
                                    }
                                }
                            }
                        }
                    }
                    try {
                        ResetAppsHelper.this.mIPm.resetApplicationPreferences(UserHandle.myUserId());
                    } catch (RemoteException unused2) {
                    }
                    ResetAppsHelper.this.mAom.resetAllModes();
                    int[] uidsWithPolicy = ResetAppsHelper.this.mNpm.getUidsWithPolicy(1);
                    int currentUser = ActivityManager.getCurrentUser();
                    for (int i2 : uidsWithPolicy) {
                        if (UserHandle.getUserId(i2) == currentUser) {
                            ResetAppsHelper.this.mNpm.setUidPolicy(i2, 0);
                        }
                    }
                }
            });
        }
    }
}
