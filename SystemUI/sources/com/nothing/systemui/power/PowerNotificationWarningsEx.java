package com.nothing.systemui.power;

import android.content.Context;
import android.content.Intent;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.systemui.Dependency;
import com.android.systemui.biometrics.AuthDialog;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.keyguard.KeyguardViewMediatorEx;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\rH\u0002J\u0006\u0010\u001c\u001a\u00020\u001aJ\u0006\u0010\u001d\u001a\u00020\u001aJ\u0006\u0010\u001e\u001a\u00020\u001aJ\u0006\u0010\u001f\u001a\u00020\u001aR\u000e\u0010\u0007\u001a\u00020\bXD¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0011\"\u0004\b\u0017\u0010\u0013R\u000e\u0010\u0018\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000¨\u0006 "}, mo65043d2 = {"Lcom/nothing/systemui/power/PowerNotificationWarningsEx;", "", "context", "Landroid/content/Context;", "handler", "Landroid/os/Handler;", "(Landroid/content/Context;Landroid/os/Handler;)V", "TAG", "", "lastUpdateTime", "", "mContext", "mCriticalTempWarning", "", "mDismissRunnable", "Ljava/lang/Runnable;", "getMDismissRunnable", "()Ljava/lang/Runnable;", "setMDismissRunnable", "(Ljava/lang/Runnable;)V", "mHandler", "mShowRunnable", "getMShowRunnable", "setMShowRunnable", "updateTimeLimit", "changeAirplaneModeSystemSetting", "", "on", "dismissCriticaTemperatureWarning", "dismissCriticaTemperatureWarningView", "showCriticalTemperatureWarning", "showCriticalTemperatureWarningView", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PowerNotificationWarningsEx.kt */
public final class PowerNotificationWarningsEx {
    /* access modifiers changed from: private */
    public final String TAG = "PowerNotificationWarningsEx";
    /* access modifiers changed from: private */
    public long lastUpdateTime;
    private final Context mContext;
    private boolean mCriticalTempWarning;
    private Runnable mDismissRunnable;
    private final Handler mHandler;
    private Runnable mShowRunnable;
    private long updateTimeLimit = 500;

    public PowerNotificationWarningsEx(Context context, Handler handler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.mContext = context;
        this.mHandler = handler;
        this.mShowRunnable = new PowerNotificationWarningsEx$mShowRunnable$1(this);
        this.mDismissRunnable = new PowerNotificationWarningsEx$mDismissRunnable$1(this);
    }

    public final void showCriticalTemperatureWarning() {
        long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
        Log.i(this.TAG, " showCriticalTemperatureWarning:" + currentTimeMillis + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + this.lastUpdateTime);
        if (currentTimeMillis > this.updateTimeLimit || this.lastUpdateTime == 0) {
            Log.i(this.TAG, " showCriticalTemperatureWarningView:");
            this.lastUpdateTime = System.currentTimeMillis();
            this.mHandler.postDelayed(this.mShowRunnable, 0);
            return;
        }
        Log.i(this.TAG, " showCriticalTemperatureWarningView:" + (this.updateTimeLimit - currentTimeMillis));
        this.mHandler.postDelayed(this.mShowRunnable, this.updateTimeLimit - currentTimeMillis);
    }

    public final void dismissCriticaTemperatureWarning() {
        long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
        Log.i(this.TAG, " dismissCriticaTemperatureWarning:" + currentTimeMillis + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + this.lastUpdateTime);
        if (currentTimeMillis > this.updateTimeLimit || this.lastUpdateTime == 0) {
            Log.i(this.TAG, " dismissCriticaTemperatureWarning:");
            this.lastUpdateTime = System.currentTimeMillis();
            this.mHandler.postDelayed(this.mDismissRunnable, 0);
            return;
        }
        Log.i(this.TAG, " dismissCriticaTemperatureWarning:" + (this.updateTimeLimit - currentTimeMillis));
        this.mHandler.postDelayed(this.mDismissRunnable, this.updateTimeLimit - currentTimeMillis);
    }

    public final Runnable getMShowRunnable() {
        return this.mShowRunnable;
    }

    public final void setMShowRunnable(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "<set-?>");
        this.mShowRunnable = runnable;
    }

    public final Runnable getMDismissRunnable() {
        return this.mDismissRunnable;
    }

    public final void setMDismissRunnable(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "<set-?>");
        this.mDismissRunnable = runnable;
    }

    public final void showCriticalTemperatureWarningView() {
        Log.i(this.TAG, " showCriticalTemperatureWarningView:" + this.mCriticalTempWarning);
        if (!this.mCriticalTempWarning) {
            this.mCriticalTempWarning = true;
            changeAirplaneModeSystemSetting(true);
            Intent intent = new Intent("com.nothing.systemui.power.NTCriticalTemperatureWarning");
            intent.setFlags(268468224);
            ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).getContext().startActivityAsUser(intent, new UserHandle(KeyguardUpdateMonitor.getCurrentUser()));
            ((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).criticalTemperatureStateChange();
        }
    }

    public final void dismissCriticaTemperatureWarningView() {
        Log.i(this.TAG, " dismissCriticaTemperatureWarningView:" + this.mCriticalTempWarning);
        if (this.mCriticalTempWarning) {
            ((KeyguardViewMediatorEx) NTDependencyEx.get(KeyguardViewMediatorEx.class)).criticalTemperatureStateChange();
            this.mCriticalTempWarning = false;
        }
    }

    private final void changeAirplaneModeSystemSetting(boolean z) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "airplane_mode_on", z ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.addFlags(NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE);
        intent.putExtra(AuthDialog.KEY_BIOMETRIC_STATE, z);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }
}
