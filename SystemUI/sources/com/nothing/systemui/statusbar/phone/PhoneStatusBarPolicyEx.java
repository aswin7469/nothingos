package com.nothing.systemui.statusbar.phone;

import android.content.Context;
import android.net.ConnectivityManager;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.statusbar.policy.BluetoothController;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo64986d1 = {"\u00009\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\n\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fR\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0004\n\u0002\u0010\u000b¨\u0006\u0013"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/phone/PhoneStatusBarPolicyEx;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "connectivityManager", "Landroid/net/ConnectivityManager;", "macAddress", "", "onStartTetheringCallback", "com/nothing/systemui/statusbar/phone/PhoneStatusBarPolicyEx$onStartTetheringCallback$1", "Lcom/nothing/systemui/statusbar/phone/PhoneStatusBarPolicyEx$onStartTetheringCallback$1;", "isTesla", "", "controller", "Lcom/android/systemui/statusbar/policy/BluetoothController;", "startOrStopTether", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PhoneStatusBarPolicyEx.kt */
public final class PhoneStatusBarPolicyEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "PhoneStatusBarPolicyEx";
    private ConnectivityManager connectivityManager;
    private Context context;
    private final String macAddress;
    private final PhoneStatusBarPolicyEx$onStartTetheringCallback$1 onStartTetheringCallback = new PhoneStatusBarPolicyEx$onStartTetheringCallback$1();

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/phone/PhoneStatusBarPolicyEx$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PhoneStatusBarPolicyEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public PhoneStatusBarPolicyEx(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        String lowerCase = "4C:FC:AA".toLowerCase();
        Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase()");
        this.macAddress = lowerCase;
        this.context = context2;
        Object systemService = context2.getSystemService("connectivity");
        if (systemService != null) {
            this.connectivityManager = (ConnectivityManager) systemService;
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.net.ConnectivityManager");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void startOrStopTether(com.android.systemui.statusbar.policy.BluetoothController r5) {
        /*
            r4 = this;
            if (r5 == 0) goto L_0x0046
            boolean r0 = r5.isBluetoothConnected()
            if (r0 == 0) goto L_0x0046
            r0 = 1
            r1 = 0
            android.content.Context r2 = r4.context     // Catch:{ Exception -> 0x001f }
            if (r2 == 0) goto L_0x0013
            android.content.ContentResolver r2 = r2.getContentResolver()     // Catch:{ Exception -> 0x001f }
            goto L_0x0014
        L_0x0013:
            r2 = 0
        L_0x0014:
            java.lang.String r3 = "tesla_hotspot"
            int r2 = android.provider.Settings.System.getInt(r2, r3)     // Catch:{ Exception -> 0x001f }
            if (r2 != 0) goto L_0x0029
            r2 = r0
            goto L_0x002a
        L_0x001f:
            r2 = move-exception
            java.lang.String r3 = "PhoneStatusBarPolicyEx"
            java.lang.String r2 = r2.toString()
            com.nothing.systemui.util.NTLogUtil.m1680d(r3, r2)
        L_0x0029:
            r2 = r1
        L_0x002a:
            if (r2 == 0) goto L_0x0046
            boolean r5 = r4.isTesla(r5)
            if (r5 == 0) goto L_0x0046
            android.net.ConnectivityManager r5 = r4.connectivityManager
            if (r5 == 0) goto L_0x0046
            com.nothing.systemui.statusbar.phone.PhoneStatusBarPolicyEx$onStartTetheringCallback$1 r4 = r4.onStartTetheringCallback
            android.net.ConnectivityManager$OnStartTetheringCallback r4 = (android.net.ConnectivityManager.OnStartTetheringCallback) r4
            android.os.Handler r2 = new android.os.Handler
            android.os.Looper r3 = android.os.Looper.getMainLooper()
            r2.<init>(r3)
            r5.startTethering(r1, r0, r4, r2)
        L_0x0046:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.phone.PhoneStatusBarPolicyEx.startOrStopTether(com.android.systemui.statusbar.policy.BluetoothController):void");
    }

    private final boolean isTesla(BluetoothController bluetoothController) {
        Intrinsics.checkNotNull(bluetoothController);
        List<CachedBluetoothDevice> connectedDevices = bluetoothController.getConnectedDevices();
        Intrinsics.checkNotNull(connectedDevices);
        if (connectedDevices.size() > 0) {
            for (CachedBluetoothDevice address : connectedDevices) {
                String address2 = address.getAddress();
                if (address2 != null) {
                    String lowerCase = address2.toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase()");
                    if (StringsKt.startsWith$default(lowerCase, this.macAddress, false, 2, (Object) null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
