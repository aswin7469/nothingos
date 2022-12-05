package com.android.systemui.sensorprivacy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorPrivacyManager;
import android.os.Bundle;
import android.os.Handler;
import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SensorUseStartedActivity.kt */
/* loaded from: classes.dex */
public final class SensorUseStartedActivity extends Activity implements DialogInterface.OnClickListener {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = SensorUseStartedActivity.class.getSimpleName();
    @NotNull
    private final Handler bgHandler;
    @NotNull
    private final KeyguardDismissUtil keyguardDismissUtil;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @Nullable
    private AlertDialog mDialog;
    private int sensor = -1;
    @NotNull
    private final IndividualSensorPrivacyController sensorPrivacyController;
    private IndividualSensorPrivacyController.Callback sensorPrivacyListener;
    private String sensorUsePackageName;
    private boolean unsuppressImmediately;

    @Override // android.app.Activity
    public void onBackPressed() {
    }

    public SensorUseStartedActivity(@NotNull IndividualSensorPrivacyController sensorPrivacyController, @NotNull KeyguardStateController keyguardStateController, @NotNull KeyguardDismissUtil keyguardDismissUtil, @NotNull Handler bgHandler) {
        Intrinsics.checkNotNullParameter(sensorPrivacyController, "sensorPrivacyController");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(keyguardDismissUtil, "keyguardDismissUtil");
        Intrinsics.checkNotNullParameter(bgHandler, "bgHandler");
        this.sensorPrivacyController = sensorPrivacyController;
        this.keyguardStateController = keyguardStateController;
        this.keyguardDismissUtil = keyguardDismissUtil;
        this.bgHandler = bgHandler;
    }

    /* compiled from: SensorUseStartedActivity.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setShowWhenLocked(true);
        setFinishOnTouchOutside(false);
        setResult(0);
        String stringExtra = getIntent().getStringExtra("android.intent.extra.PACKAGE_NAME");
        if (stringExtra == null) {
            return;
        }
        this.sensorUsePackageName = stringExtra;
        if (getIntent().getBooleanExtra(SensorPrivacyManager.EXTRA_ALL_SENSORS, false)) {
            this.sensor = Integer.MAX_VALUE;
            IndividualSensorPrivacyController.Callback callback = new IndividualSensorPrivacyController.Callback() { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onCreate$1
                @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController.Callback
                public final void onSensorBlockedChanged(int i, boolean z) {
                    IndividualSensorPrivacyController individualSensorPrivacyController;
                    IndividualSensorPrivacyController individualSensorPrivacyController2;
                    individualSensorPrivacyController = SensorUseStartedActivity.this.sensorPrivacyController;
                    if (!individualSensorPrivacyController.isSensorBlocked(1)) {
                        individualSensorPrivacyController2 = SensorUseStartedActivity.this.sensorPrivacyController;
                        if (individualSensorPrivacyController2.isSensorBlocked(2)) {
                            return;
                        }
                        SensorUseStartedActivity.this.finish();
                    }
                }
            };
            this.sensorPrivacyListener = callback;
            this.sensorPrivacyController.addCallback(callback);
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
            Unit unit = Unit.INSTANCE;
            this.sensor = intExtra;
            IndividualSensorPrivacyController.Callback callback2 = new IndividualSensorPrivacyController.Callback() { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onCreate$3
                @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController.Callback
                public final void onSensorBlockedChanged(int i, boolean z) {
                    int i2;
                    i2 = SensorUseStartedActivity.this.sensor;
                    if (i != i2 || z) {
                        return;
                    }
                    SensorUseStartedActivity.this.finish();
                }
            };
            this.sensorPrivacyListener = callback2;
            this.sensorPrivacyController.addCallback(callback2);
            if (!this.sensorPrivacyController.isSensorBlocked(this.sensor)) {
                finish();
                return;
            }
        }
        SensorUseDialog sensorUseDialog = new SensorUseDialog(this, this.sensor, this);
        this.mDialog = sensorUseDialog;
        Intrinsics.checkNotNull(sensorUseDialog);
        sensorUseDialog.show();
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        setSuppressed(true);
        this.unsuppressImmediately = false;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(@Nullable DialogInterface dialogInterface, int i) {
        if (i == -2) {
            this.unsuppressImmediately = false;
            String str = this.sensorUsePackageName;
            if (str != null) {
                FrameworkStatsLog.write(382, 2, str);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
                throw null;
            }
        } else if (i == -1) {
            if (this.keyguardStateController.isMethodSecure() && this.keyguardStateController.isShowing()) {
                this.keyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onClick$1
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        Handler handler;
                        handler = SensorUseStartedActivity.this.bgHandler;
                        final SensorUseStartedActivity sensorUseStartedActivity = SensorUseStartedActivity.this;
                        handler.postDelayed(new Runnable() { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onClick$1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                String str2;
                                SensorUseStartedActivity.this.disableSensorPrivacy();
                                str2 = SensorUseStartedActivity.this.sensorUsePackageName;
                                if (str2 != null) {
                                    FrameworkStatsLog.write(382, 1, str2);
                                } else {
                                    Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
                                    throw null;
                                }
                            }
                        }, 200L);
                        return false;
                    }
                }, false, true);
            } else {
                disableSensorPrivacy();
                String str2 = this.sensorUsePackageName;
                if (str2 != null) {
                    FrameworkStatsLog.write(382, 1, str2);
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
                    throw null;
                }
            }
        }
        finish();
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        if (this.unsuppressImmediately) {
            setSuppressed(false);
        } else {
            this.bgHandler.postDelayed(new Runnable() { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onStop$1
                @Override // java.lang.Runnable
                public final void run() {
                    SensorUseStartedActivity.this.setSuppressed(false);
                }
            }, 2000L);
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        IndividualSensorPrivacyController individualSensorPrivacyController = this.sensorPrivacyController;
        IndividualSensorPrivacyController.Callback callback = this.sensorPrivacyListener;
        if (callback != null) {
            individualSensorPrivacyController.removeCallback(callback);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("sensorPrivacyListener");
            throw null;
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(@Nullable Intent intent) {
        setIntent(intent);
        recreate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void disableSensorPrivacy() {
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

    /* JADX INFO: Access modifiers changed from: private */
    public final void setSuppressed(boolean z) {
        int i = this.sensor;
        if (i == Integer.MAX_VALUE) {
            this.sensorPrivacyController.suppressSensorPrivacyReminders(1, z);
            this.sensorPrivacyController.suppressSensorPrivacyReminders(2, z);
            return;
        }
        this.sensorPrivacyController.suppressSensorPrivacyReminders(i, z);
    }
}
