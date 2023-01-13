package com.android.systemui.controls.management;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.os.UserHandle;
import android.service.controls.Control;
import android.util.Log;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsRequestReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsRequestReceiver.kt */
public final class ControlsRequestReceiver extends BroadcastReceiver {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "ControlsRequestReceiver";

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsRequestReceiver$Companion;", "", "()V", "TAG", "", "isPackageInForeground", "", "context", "Landroid/content/Context;", "packageName", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsRequestReceiver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isPackageInForeground(Context context, String str) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            try {
                int packageUid = context.getPackageManager().getPackageUid(str, 0);
                ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
                if ((activityManager != null ? activityManager.getUidImportance(packageUid) : 1000) == 100) {
                    return true;
                }
                Log.w(ControlsRequestReceiver.TAG, "Uid " + packageUid + " not in foreground");
                return false;
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w(ControlsRequestReceiver.TAG, "Package " + str + " not found");
                return false;
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (context.getPackageManager().hasSystemFeature("android.software.controls")) {
            try {
                ComponentName componentName = (ComponentName) intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME");
                try {
                    Parcelable parcelable = (Control) intent.getParcelableExtra("android.service.controls.extra.CONTROL");
                    String packageName = componentName != null ? componentName.getPackageName() : null;
                    if (packageName != null && Companion.isPackageInForeground(context, packageName)) {
                        Intent intent2 = new Intent(context, ControlsRequestDialog.class);
                        intent2.putExtra("android.intent.extra.COMPONENT_NAME", componentName);
                        intent2.putExtra("android.service.controls.extra.CONTROL", parcelable);
                        intent2.addFlags(268566528);
                        intent2.putExtra("android.intent.extra.USER_ID", context.getUserId());
                        context.startActivityAsUser(intent2, UserHandle.SYSTEM);
                    }
                } catch (ClassCastException e) {
                    Log.e(TAG, "Malformed intent extra Control", e);
                }
            } catch (ClassCastException e2) {
                Log.e(TAG, "Malformed intent extra ComponentName", e2);
            }
        }
    }
}
