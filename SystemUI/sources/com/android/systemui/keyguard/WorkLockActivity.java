package com.android.systemui.keyguard;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class WorkLockActivity extends Activity {
    private static final int REQUEST_CODE_CONFIRM_CREDENTIALS = 1;
    private static final String TAG = "WorkLockActivity";
    private final BroadcastDispatcher mBroadcastDispatcher;
    private KeyguardManager mKgm;
    private final BroadcastReceiver mLockEventReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int targetUserId = WorkLockActivity.this.getTargetUserId();
            if (intent.getIntExtra("android.intent.extra.user_handle", targetUserId) == targetUserId && !WorkLockActivity.this.getKeyguardManager().isDeviceLocked(targetUserId)) {
                WorkLockActivity.this.finish();
            }
        }
    };
    private PackageManager mPackageManager;
    private UserManager mUserManager;

    public void onBackPressed() {
    }

    public void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
    }

    @Inject
    public WorkLockActivity(BroadcastDispatcher broadcastDispatcher, UserManager userManager, PackageManager packageManager) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mUserManager = userManager;
        this.mPackageManager = packageManager;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mBroadcastDispatcher.registerReceiver(this.mLockEventReceiver, new IntentFilter("android.intent.action.DEVICE_LOCKED_CHANGED"), (Executor) null, UserHandle.ALL);
        if (!getKeyguardManager().isDeviceLocked(getTargetUserId())) {
            finish();
            return;
        }
        setOverlayWithDecorCaptionEnabled(true);
        setContentView(C1894R.layout.auth_biometric_background);
        Drawable badgedIcon = getBadgedIcon();
        if (badgedIcon != null) {
            ((ImageView) findViewById(C1894R.C1898id.icon)).setImageDrawable(badgedIcon);
        }
    }

    /* access modifiers changed from: protected */
    public Drawable getBadgedIcon() {
        String stringExtra = getIntent().getStringExtra("android.intent.extra.PACKAGE_NAME");
        if (stringExtra.isEmpty()) {
            return null;
        }
        try {
            UserManager userManager = this.mUserManager;
            PackageManager packageManager = this.mPackageManager;
            return userManager.getBadgedIconForUser(packageManager.getApplicationIcon(packageManager.getApplicationInfoAsUser(stringExtra, PackageManager.ApplicationInfoFlags.of(0), getTargetUserId())), UserHandle.of(getTargetUserId()));
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public void onWindowFocusChanged(boolean z) {
        if (z) {
            showConfirmCredentialActivity();
        }
    }

    /* access modifiers changed from: protected */
    public void unregisterBroadcastReceiver() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mLockEventReceiver);
    }

    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    private void showConfirmCredentialActivity() {
        Intent createConfirmDeviceCredentialIntent;
        if (!isFinishing() && getKeyguardManager().isDeviceLocked(getTargetUserId()) && (createConfirmDeviceCredentialIntent = getKeyguardManager().createConfirmDeviceCredentialIntent((CharSequence) null, (CharSequence) null, getTargetUserId(), true)) != null) {
            ActivityOptions makeBasic = ActivityOptions.makeBasic();
            makeBasic.setLaunchTaskId(getTaskId());
            PendingIntent activity = PendingIntent.getActivity(this, -1, getIntent(), 1409286144, makeBasic.toBundle());
            if (activity != null) {
                createConfirmDeviceCredentialIntent.putExtra("android.intent.extra.INTENT", activity.getIntentSender());
            }
            ActivityOptions makeBasic2 = ActivityOptions.makeBasic();
            makeBasic2.setLaunchTaskId(getTaskId());
            makeBasic2.setTaskOverlay(true, true);
            startActivityForResult(createConfirmDeviceCredentialIntent, 1, makeBasic2.toBundle());
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 != -1) {
            goToHomeScreen();
        }
    }

    private void goToHomeScreen() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public KeyguardManager getKeyguardManager() {
        if (this.mKgm == null) {
            this.mKgm = (KeyguardManager) getSystemService("keyguard");
        }
        return this.mKgm;
    }

    /* access modifiers changed from: package-private */
    public final int getTargetUserId() {
        return getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
    }
}
