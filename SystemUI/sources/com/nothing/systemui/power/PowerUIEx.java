package com.nothing.systemui.power;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Temperature;
import android.provider.Settings;
import android.util.Slog;
import com.android.systemui.power.PowerUI;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.BufferedReader;
import java.p026io.InputStreamReader;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.Job;

@Metadata(mo64986d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\u0018\u0000 22\u00020\u0001:\u000223B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010 \u001a\u00020!H\u0002J\u0006\u0010\"\u001a\u00020#J\b\u0010$\u001a\u00020%H\u0002J\u000e\u0010&\u001a\u00020!2\u0006\u0010'\u001a\u00020(J\u0016\u0010)\u001a\u00020!2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010*\u001a\u00020+J\u0012\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020-H\u0002J\u0018\u0010/\u001a\u00020!2\u0006\u0010\t\u001a\u00020\n2\u0006\u00100\u001a\u00020\u0012H\u0002J\u0018\u00101\u001a\u00020!2\u0006\u0010\t\u001a\u00020\n2\u0006\u00100\u001a\u00020\u0012H\u0002R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0013\"\u0004\b\u0017\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0013\"\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001b\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0013\"\u0004\b\u001d\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f¨\u00064"}, mo64987d2 = {"Lcom/nothing/systemui/power/PowerUIEx;", "", "context", "Landroid/content/Context;", "warnings", "Lcom/android/systemui/power/PowerUI$WarningsUI;", "handler", "Landroid/os/Handler;", "(Landroid/content/Context;Lcom/android/systemui/power/PowerUI$WarningsUI;Landroid/os/Handler;)V", "contentResolver", "Landroid/content/ContentResolver;", "getContentResolver", "()Landroid/content/ContentResolver;", "getContext", "()Landroid/content/Context;", "getHandler", "()Landroid/os/Handler;", "isCloseRx", "", "()Z", "setCloseRx", "(Z)V", "isCloseTx", "setCloseTx", "mIsTheLastValue", "getMIsTheLastValue", "setMIsTheLastValue", "mPluggedInWireless", "getMPluggedInWireless", "setMPluggedInWireless", "getWarnings", "()Lcom/android/systemui/power/PowerUI$WarningsUI;", "determineTemperatureAndHandleRxTX", "", "flashTemperature", "", "getSettingsValue", "", "notifyThrottling", "temp", "Landroid/os/Temperature;", "onReceive", "intent", "Landroid/content/Intent;", "readFile", "", "path", "setWirelessForwardCharge", "flag", "setWirelessReverseCharge", "Companion", "WarningsUI", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PowerUIEx.kt */
public final class PowerUIEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEBUG = true;
    private static final String NT_WIRELESS_FORWARD_CHARGE = "nt_wireless_forward_charge";
    private static final String SETTINGS_KEY = "nt_wireless_reverse_charge";
    private static final String TAG = "PowerUIEx";
    private static final String THERMALZONE89 = "cat sys/class/thermal/thermal_zone89/temp";
    private static final String WIRELESS_BOOST_STATUS_PATH = "/sys/class/qcom-battery/wireless_boost_en";
    private static final String WIRELESS_REVERSE_STATUS = "/sys/class/qcom-battery/wls_reverse_status";
    private static final String WIRELESS_WLS_PATH = "/sys/class/qcom-battery/wls_st38_en";
    private final ContentResolver contentResolver;
    private final Context context;
    private final Handler handler;
    private boolean isCloseRx;
    private boolean isCloseTx;
    private boolean mIsTheLastValue;
    private boolean mPluggedInWireless;
    private final PowerUI.WarningsUI warnings;

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0005À\u0006\u0001"}, mo64987d2 = {"Lcom/nothing/systemui/power/PowerUIEx$WarningsUI;", "", "dismissCriticaTemperatureWarning", "", "showCriticalTemperatureWarning", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PowerUIEx.kt */
    public interface WarningsUI {
        void dismissCriticaTemperatureWarning();

        void showCriticalTemperatureWarning();
    }

    public PowerUIEx(Context context2, PowerUI.WarningsUI warningsUI, Handler handler2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(warningsUI, "warnings");
        Intrinsics.checkNotNullParameter(handler2, "handler");
        this.context = context2;
        this.warnings = warningsUI;
        this.handler = handler2;
        ContentResolver contentResolver2 = context2.getContentResolver();
        Intrinsics.checkNotNullExpressionValue(contentResolver2, "context.getContentResolver()");
        this.contentResolver = contentResolver2;
    }

    public final PowerUI.WarningsUI getWarnings() {
        return this.warnings;
    }

    public final Context getContext() {
        return this.context;
    }

    public final ContentResolver getContentResolver() {
        return this.contentResolver;
    }

    public final Handler getHandler() {
        return this.handler;
    }

    public final boolean isCloseTx() {
        return this.isCloseTx;
    }

    public final void setCloseTx(boolean z) {
        this.isCloseTx = z;
    }

    public final boolean isCloseRx() {
        return this.isCloseRx;
    }

    public final void setCloseRx(boolean z) {
        this.isCloseRx = z;
    }

    public final boolean getMIsTheLastValue() {
        return this.mIsTheLastValue;
    }

    public final void setMIsTheLastValue(boolean z) {
        this.mIsTheLastValue = z;
    }

    public final boolean getMPluggedInWireless() {
        return this.mPluggedInWireless;
    }

    public final void setMPluggedInWireless(boolean z) {
        this.mPluggedInWireless = z;
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\r"}, mo64987d2 = {"Lcom/nothing/systemui/power/PowerUIEx$Companion;", "", "()V", "DEBUG", "", "NT_WIRELESS_FORWARD_CHARGE", "", "SETTINGS_KEY", "TAG", "THERMALZONE89", "WIRELESS_BOOST_STATUS_PATH", "WIRELESS_REVERSE_STATUS", "WIRELESS_WLS_PATH", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PowerUIEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void onReceive(Context context2, Intent intent) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        String action = intent.getAction();
        if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
            boolean z = false;
            if (intent.getIntExtra("plugged", 0) == 4) {
                z = true;
            }
            this.mPluggedInWireless = z;
            determineTemperatureAndHandleRxTX();
        } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
            determineTemperatureAndHandleRxTX();
        }
    }

    public final void notifyThrottling(Temperature temperature) {
        Intrinsics.checkNotNullParameter(temperature, "temp");
        int status = temperature.getStatus();
        if (status >= 5) {
            this.warnings.showCriticalTemperatureWarning();
            Slog.d(TAG, "SkinThermalEventListener: notifyThrottling was called , current skin status = " + status + ", temperature = " + temperature.getValue());
            return;
        }
        this.warnings.dismissCriticaTemperatureWarning();
    }

    /* access modifiers changed from: private */
    public final void setWirelessReverseCharge(ContentResolver contentResolver2, boolean z) {
        NTLogUtil.m1680d(TAG, "setWirelessReverseCharge ( " + z + ')');
        Settings.Global.putInt(contentResolver2, SETTINGS_KEY, z ? 1 : 0);
    }

    /* access modifiers changed from: private */
    public final void setWirelessForwardCharge(ContentResolver contentResolver2, boolean z) {
        NTLogUtil.m1680d(TAG, "setWirelessForwardCharge ( " + z + ')');
        Settings.Global.putInt(contentResolver2, NT_WIRELESS_FORWARD_CHARGE, z ? 1 : 0);
    }

    /* access modifiers changed from: private */
    public final int getSettingsValue() {
        try {
            return Settings.Global.getInt(this.context.getContentResolver(), SETTINGS_KEY, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private final void determineTemperatureAndHandleRxTX() {
        Job unused = BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, Dispatchers.getIO(), (CoroutineStart) null, new PowerUIEx$determineTemperatureAndHandleRxTX$1(this, (Continuation<? super PowerUIEx$determineTemperatureAndHandleRxTX$1>) null), 2, (Object) null);
    }

    public final float flashTemperature() {
        try {
            Process exec = Runtime.getRuntime().exec(THERMALZONE89);
            Intrinsics.checkNotNullExpressionValue(exec, "getRuntime().exec(THERMALZONE89)");
            exec.waitFor();
            String readLine = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
            Intrinsics.checkNotNullExpressionValue(readLine, "reader.readLine()");
            return Float.parseFloat(readLine) / 1000.0f;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004f A[SYNTHETIC, Splitter:B:23:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005c A[SYNTHETIC, Splitter:B:29:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String readFile(java.lang.String r6) {
        /*
            r5 = this;
            java.lang.String r5 = "PowerUIEx"
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            r1.<init>((java.lang.String) r6)     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            r3.<init>((java.p026io.File) r1)     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            java.io.InputStream r3 = (java.p026io.InputStream) r3     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            java.lang.String r1 = "UTF-8"
            r2.<init>((java.p026io.InputStream) r3, (java.lang.String) r1)     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            java.io.Reader r2 = (java.p026io.Reader) r2     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            r6.<init>(r2)     // Catch:{ IOException -> 0x0043, all -> 0x003e }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x003b }
            r1.<init>()     // Catch:{ IOException -> 0x003b }
        L_0x0022:
            java.lang.String r2 = r6.readLine()     // Catch:{ IOException -> 0x0039 }
            if (r2 == 0) goto L_0x002c
            r1.append((java.lang.String) r2)     // Catch:{ IOException -> 0x0039 }
            goto L_0x0022
        L_0x002c:
            r6.close()     // Catch:{ IOException -> 0x0030 }
            goto L_0x0052
        L_0x0030:
            r6 = move-exception
            java.lang.String r6 = r6.toString()
            com.nothing.systemui.util.NTLogUtil.m1680d(r5, r6)
            goto L_0x0052
        L_0x0039:
            r2 = move-exception
            goto L_0x0046
        L_0x003b:
            r2 = move-exception
            r1 = r0
            goto L_0x0046
        L_0x003e:
            r6 = move-exception
            r4 = r0
            r0 = r6
            r6 = r4
            goto L_0x005a
        L_0x0043:
            r2 = move-exception
            r6 = r0
            r1 = r6
        L_0x0046:
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0059 }
            com.nothing.systemui.util.NTLogUtil.m1680d(r5, r2)     // Catch:{ all -> 0x0059 }
            if (r6 == 0) goto L_0x0052
            r6.close()     // Catch:{ IOException -> 0x0030 }
        L_0x0052:
            if (r1 == 0) goto L_0x0058
            java.lang.String r0 = r1.toString()
        L_0x0058:
            return r0
        L_0x0059:
            r0 = move-exception
        L_0x005a:
            if (r6 == 0) goto L_0x0068
            r6.close()     // Catch:{ IOException -> 0x0060 }
            goto L_0x0068
        L_0x0060:
            r6 = move-exception
            java.lang.String r6 = r6.toString()
            com.nothing.systemui.util.NTLogUtil.m1680d(r5, r6)
        L_0x0068:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.power.PowerUIEx.readFile(java.lang.String):java.lang.String");
    }
}
