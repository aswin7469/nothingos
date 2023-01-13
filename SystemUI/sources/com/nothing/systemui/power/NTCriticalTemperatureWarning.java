package com.nothing.systemui.power;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.power.TemperatureController;
import com.android.systemui.util.EmergencyDialerConstants;
import com.nothing.systemui.NTDependencyEx;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0012\u0010\u001b\u001a\u00020\u001a2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0014J\b\u0010\u001e\u001a\u00020\u001aH\u0014J\u0006\u0010\u001f\u001a\u00020\u001aJ\u0006\u0010 \u001a\u00020\u001aR\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0015\u001a\u00020\u0016¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018¨\u0006!"}, mo65043d2 = {"Lcom/nothing/systemui/power/NTCriticalTemperatureWarning;", "Landroid/app/Activity;", "()V", "TAG", "", "mController", "Lcom/android/systemui/power/TemperatureController;", "mPowerMan", "Landroid/os/PowerManager;", "mShutdownDialog", "Landroid/app/AlertDialog;", "mShutdownTimeCallback", "Lcom/android/systemui/power/TemperatureController$ShutdownTimeCallback;", "getMShutdownTimeCallback", "()Lcom/android/systemui/power/TemperatureController$ShutdownTimeCallback;", "setMShutdownTimeCallback", "(Lcom/android/systemui/power/TemperatureController$ShutdownTimeCallback;)V", "mTelecomManager", "Landroid/telecom/TelecomManager;", "mTemperatureType", "", "mUpdateCallback", "Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "getMUpdateCallback", "()Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "onBackPressed", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "showShutdownDialog", "takeEmergencyCallAction", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTCriticalTemperatureWarning.kt */
public class NTCriticalTemperatureWarning extends Activity {
    /* access modifiers changed from: private */
    public final String TAG = "NTCriticalTemperatureWarning";
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private TemperatureController mController;
    /* access modifiers changed from: private */
    public PowerManager mPowerMan;
    /* access modifiers changed from: private */
    public AlertDialog mShutdownDialog;
    private TemperatureController.ShutdownTimeCallback mShutdownTimeCallback = new NTCriticalTemperatureWarning$mShutdownTimeCallback$1(this);
    private TelecomManager mTelecomManager;
    /* access modifiers changed from: private */
    public int mTemperatureType;
    private final KeyguardUpdateMonitorCallback mUpdateCallback = new NTCriticalTemperatureWarning$mUpdateCallback$1(this);

    /* access modifiers changed from: private */
    /* renamed from: showShutdownDialog$lambda-1  reason: not valid java name */
    public static final void m3510showShutdownDialog$lambda1(DialogInterface dialogInterface, int i) {
    }

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

    public final TemperatureController.ShutdownTimeCallback getMShutdownTimeCallback() {
        return this.mShutdownTimeCallback;
    }

    public final void setMShutdownTimeCallback(TemperatureController.ShutdownTimeCallback shutdownTimeCallback) {
        Intrinsics.checkNotNullParameter(shutdownTimeCallback, "<set-?>");
        this.mShutdownTimeCallback = shutdownTimeCallback;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mController = (TemperatureController) NTDependencyEx.get(TemperatureController.class);
        getWindow().addFlags(67698944);
        getWindow().getDecorView().setSystemUiVisibility(1792);
        getWindow().getAttributes().setFitInsetsTypes(0);
        getWindow().setNavigationBarContrastEnforced(false);
        getWindow().setNavigationBarColor(0);
        setContentView(C1894R.layout.critical_temperature_warning_view);
        Object systemService = getSystemService("power");
        if (systemService != null) {
            this.mPowerMan = (PowerManager) systemService;
            Object systemService2 = getSystemService("telecom");
            if (systemService2 != null) {
                this.mTelecomManager = (TelecomManager) systemService2;
                findViewById(C1894R.C1898id.emergency_call).setOnClickListener(new NTCriticalTemperatureWarning$$ExternalSyntheticLambda0(this));
                Log.d(this.TAG, "onCreate: ");
                ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mUpdateCallback);
                TemperatureController temperatureController = this.mController;
                if (temperatureController != null) {
                    temperatureController.addCallback(this.mShutdownTimeCallback);
                    return;
                }
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.telecom.TelecomManager");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.os.PowerManager");
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-0  reason: not valid java name */
    public static final void m3509onCreate$lambda0(NTCriticalTemperatureWarning nTCriticalTemperatureWarning, View view) {
        Intrinsics.checkNotNullParameter(nTCriticalTemperatureWarning, "this$0");
        nTCriticalTemperatureWarning.takeEmergencyCallAction();
    }

    public final void takeEmergencyCallAction() {
        PowerManager powerManager = this.mPowerMan;
        if (powerManager != null) {
            powerManager.userActivity(SystemClock.uptimeMillis(), true);
        }
        TelecomManager telecomManager = this.mTelecomManager;
        if (telecomManager != null) {
            Intrinsics.checkNotNull(telecomManager);
            if (telecomManager.isInCall()) {
                TelecomManager telecomManager2 = this.mTelecomManager;
                Intrinsics.checkNotNull(telecomManager2);
                telecomManager2.showInCallScreen(false);
                return;
            }
        }
        TelecomManager telecomManager3 = this.mTelecomManager;
        if (telecomManager3 != null) {
            Intrinsics.checkNotNull(telecomManager3);
            Intent putExtra = telecomManager3.createLaunchEmergencyDialerIntent((String) null).setFlags(343932928).putExtra(EmergencyDialerConstants.EXTRA_ENTRY_TYPE, 1);
            Intrinsics.checkNotNullExpressionValue(putExtra, "mTelecomManager!!.create…Y_TYPE_LOCKSCREEN_BUTTON)");
            startActivityAsUser(putExtra, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle(), new UserHandle(KeyguardUpdateMonitor.getCurrentUser()));
        }
    }

    public final KeyguardUpdateMonitorCallback getMUpdateCallback() {
        return this.mUpdateCallback;
    }

    public final void showShutdownDialog() {
        this.mShutdownDialog = new AlertDialog.Builder(this).setTitle(C1894R.string.shutdown_temperature_warning_title).setMessage(getResources().getString(C1894R.string.shutdown_temperature_warning_content, new Object[]{30L})).setCancelable(false).setPositiveButton(C1894R.string.temperature_turn_off_now, new NTCriticalTemperatureWarning$$ExternalSyntheticLambda1()).show();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.TAG, "onDestroy: ");
        TemperatureController temperatureController = this.mController;
        if (temperatureController != null) {
            temperatureController.removeCallback(this.mShutdownTimeCallback);
        }
    }
}
