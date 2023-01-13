package com.android.systemui.sensorprivacy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorPrivacyManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 *2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001*B)\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\b\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\u0019\u001a\u00020\u0018H\u0016J\u001a\u0010\u001a\u001a\u00020\u00182\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u0010H\u0016J\u0012\u0010\u001e\u001a\u00020\u00182\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0014J\b\u0010!\u001a\u00020\u0018H\u0014J\u0012\u0010\"\u001a\u00020\u00182\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\u0012\u0010#\u001a\u00020\u00182\b\u0010$\u001a\u0004\u0018\u00010%H\u0014J\b\u0010&\u001a\u00020\u0018H\u0014J\b\u0010'\u001a\u00020\u0018H\u0014J\u0010\u0010(\u001a\u00020\u00182\u0006\u0010)\u001a\u00020\u0016H\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000¨\u0006+"}, mo65043d2 = {"Lcom/android/systemui/sensorprivacy/SensorUseStartedActivity;", "Landroid/app/Activity;", "Landroid/content/DialogInterface$OnClickListener;", "Landroid/content/DialogInterface$OnDismissListener;", "sensorPrivacyController", "Lcom/android/systemui/statusbar/policy/IndividualSensorPrivacyController;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "keyguardDismissUtil", "Lcom/android/systemui/statusbar/phone/KeyguardDismissUtil;", "bgHandler", "Landroid/os/Handler;", "(Lcom/android/systemui/statusbar/policy/IndividualSensorPrivacyController;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/statusbar/phone/KeyguardDismissUtil;Landroid/os/Handler;)V", "mDialog", "Landroid/app/AlertDialog;", "sensor", "", "sensorPrivacyListener", "Lcom/android/systemui/statusbar/policy/IndividualSensorPrivacyController$Callback;", "sensorUsePackageName", "", "unsuppressImmediately", "", "disableSensorPrivacy", "", "onBackPressed", "onClick", "dialog", "Landroid/content/DialogInterface;", "which", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onDismiss", "onNewIntent", "intent", "Landroid/content/Intent;", "onStart", "onStop", "setSuppressed", "suppressed", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SensorUseStartedActivity.kt */
public final class SensorUseStartedActivity extends Activity implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    public static final int ALL_SENSORS = Integer.MAX_VALUE;
    public static final int CAMERA = 2;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String LOG_TAG = "SensorUseStartedActivity";
    public static final int MICROPHONE = 1;
    private static final long SUPPRESS_REMINDERS_REMOVAL_DELAY_MILLIS = 2000;
    private static final long UNLOCK_DELAY_MILLIS = 200;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final Handler bgHandler;
    private final KeyguardDismissUtil keyguardDismissUtil;
    private final KeyguardStateController keyguardStateController;
    private AlertDialog mDialog;
    private int sensor;
    private final IndividualSensorPrivacyController sensorPrivacyController;
    private IndividualSensorPrivacyController.Callback sensorPrivacyListener;
    private String sensorUsePackageName;
    private boolean unsuppressImmediately;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public void onBackPressed() {
    }

    @Inject
    public SensorUseStartedActivity(IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController2, KeyguardDismissUtil keyguardDismissUtil2, @Background Handler handler) {
        Intrinsics.checkNotNullParameter(individualSensorPrivacyController, "sensorPrivacyController");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(keyguardDismissUtil2, "keyguardDismissUtil");
        Intrinsics.checkNotNullParameter(handler, "bgHandler");
        this.sensorPrivacyController = individualSensorPrivacyController;
        this.keyguardStateController = keyguardStateController2;
        this.keyguardDismissUtil = keyguardDismissUtil2;
        this.bgHandler = handler;
        this.sensor = -1;
    }

    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bXT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bXT¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/sensorprivacy/SensorUseStartedActivity$Companion;", "", "()V", "ALL_SENSORS", "", "CAMERA", "LOG_TAG", "", "kotlin.jvm.PlatformType", "MICROPHONE", "SUPPRESS_REMINDERS_REMOVAL_DELAY_MILLIS", "", "UNLOCK_DELAY_MILLIS", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: SensorUseStartedActivity.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setShowWhenLocked(true);
        setFinishOnTouchOutside(false);
        setResult(0);
        String stringExtra = getIntent().getStringExtra("android.intent.extra.PACKAGE_NAME");
        if (stringExtra != null) {
            this.sensorUsePackageName = stringExtra;
            if (getIntent().getBooleanExtra(SensorPrivacyManager.EXTRA_ALL_SENSORS, false)) {
                this.sensor = Integer.MAX_VALUE;
                SensorUseStartedActivity$$ExternalSyntheticLambda3 sensorUseStartedActivity$$ExternalSyntheticLambda3 = new SensorUseStartedActivity$$ExternalSyntheticLambda3(this);
                this.sensorPrivacyListener = sensorUseStartedActivity$$ExternalSyntheticLambda3;
                this.sensorPrivacyController.addCallback(sensorUseStartedActivity$$ExternalSyntheticLambda3);
                if (!this.sensorPrivacyController.isSensorBlocked(1) && !this.sensorPrivacyController.isSensorBlocked(2)) {
                    finish();
                    return;
                }
            } else {
                int intExtra = getIntent().getIntExtra(SensorPrivacyManager.EXTRA_SENSOR, -1);
                if (intExtra == -1) {
                    finish();
                    return;
                }
                this.sensor = intExtra;
                SensorUseStartedActivity$$ExternalSyntheticLambda4 sensorUseStartedActivity$$ExternalSyntheticLambda4 = new SensorUseStartedActivity$$ExternalSyntheticLambda4(this);
                this.sensorPrivacyListener = sensorUseStartedActivity$$ExternalSyntheticLambda4;
                this.sensorPrivacyController.addCallback(sensorUseStartedActivity$$ExternalSyntheticLambda4);
                if (!this.sensorPrivacyController.isSensorBlocked(this.sensor)) {
                    finish();
                    return;
                }
            }
            AlertDialog sensorUseDialog = new SensorUseDialog(this, this.sensor, this, this);
            this.mDialog = sensorUseDialog;
            Intrinsics.checkNotNull(sensorUseDialog);
            sensorUseDialog.show();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-0  reason: not valid java name */
    public static final void m3021onCreate$lambda0(SensorUseStartedActivity sensorUseStartedActivity, int i, boolean z) {
        Intrinsics.checkNotNullParameter(sensorUseStartedActivity, "this$0");
        if (!sensorUseStartedActivity.sensorPrivacyController.isSensorBlocked(1) && !sensorUseStartedActivity.sensorPrivacyController.isSensorBlocked(2)) {
            sensorUseStartedActivity.finish();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-2  reason: not valid java name */
    public static final void m3022onCreate$lambda2(SensorUseStartedActivity sensorUseStartedActivity, int i, boolean z) {
        Intrinsics.checkNotNullParameter(sensorUseStartedActivity, "this$0");
        if (i == sensorUseStartedActivity.sensor && !z) {
            sensorUseStartedActivity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        setSuppressed(true);
        this.unsuppressImmediately = false;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        String str = null;
        if (i == -2) {
            this.unsuppressImmediately = false;
            String str2 = this.sensorUsePackageName;
            if (str2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
            } else {
                str = str2;
            }
            FrameworkStatsLog.write(382, 2, str);
        } else if (i == -1) {
            if (!this.sensorPrivacyController.requiresAuthentication() || !this.keyguardStateController.isMethodSecure() || !this.keyguardStateController.isShowing()) {
                disableSensorPrivacy();
                String str3 = this.sensorUsePackageName;
                if (str3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
                } else {
                    str = str3;
                }
                FrameworkStatsLog.write(382, 1, str);
            } else {
                this.keyguardDismissUtil.executeWhenUnlocked(new SensorUseStartedActivity$$ExternalSyntheticLambda1(this), false, true);
            }
        }
        finish();
    }

    /* access modifiers changed from: private */
    /* renamed from: onClick$lambda-4  reason: not valid java name */
    public static final boolean m3019onClick$lambda4(SensorUseStartedActivity sensorUseStartedActivity) {
        Intrinsics.checkNotNullParameter(sensorUseStartedActivity, "this$0");
        sensorUseStartedActivity.bgHandler.postDelayed(new SensorUseStartedActivity$$ExternalSyntheticLambda0(sensorUseStartedActivity), 200);
        return false;
    }

    /* access modifiers changed from: private */
    /* renamed from: onClick$lambda-4$lambda-3  reason: not valid java name */
    public static final void m3020onClick$lambda4$lambda3(SensorUseStartedActivity sensorUseStartedActivity) {
        Intrinsics.checkNotNullParameter(sensorUseStartedActivity, "this$0");
        sensorUseStartedActivity.disableSensorPrivacy();
        String str = sensorUseStartedActivity.sensorUsePackageName;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
            str = null;
        }
        FrameworkStatsLog.write(382, 1, str);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (this.unsuppressImmediately) {
            setSuppressed(false);
        } else {
            this.bgHandler.postDelayed(new SensorUseStartedActivity$$ExternalSyntheticLambda2(this), 2000);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onStop$lambda-5  reason: not valid java name */
    public static final void m3023onStop$lambda5(SensorUseStartedActivity sensorUseStartedActivity) {
        Intrinsics.checkNotNullParameter(sensorUseStartedActivity, "this$0");
        sensorUseStartedActivity.setSuppressed(false);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        IndividualSensorPrivacyController individualSensorPrivacyController = this.sensorPrivacyController;
        IndividualSensorPrivacyController.Callback callback = this.sensorPrivacyListener;
        if (callback == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sensorPrivacyListener");
            callback = null;
        }
        individualSensorPrivacyController.removeCallback(callback);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        recreate();
    }

    private final void disableSensorPrivacy() {
        int i = this.sensor;
        if (i == Integer.MAX_VALUE) {
            this.sensorPrivacyController.setSensorBlocked(3, 1, false);
            this.sensorPrivacyController.setSensorBlocked(3, 2, false);
        } else {
            this.sensorPrivacyController.setSensorBlocked(3, i, false);
        }
        this.unsuppressImmediately = true;
        setResult(-1);
    }

    private final void setSuppressed(boolean z) {
        int i = this.sensor;
        if (i == Integer.MAX_VALUE) {
            this.sensorPrivacyController.suppressSensorPrivacyReminders(1, z);
            this.sensorPrivacyController.suppressSensorPrivacyReminders(2, z);
            return;
        }
        this.sensorPrivacyController.suppressSensorPrivacyReminders(i, z);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        finish();
    }
}
