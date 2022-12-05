package com.android.systemui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.os.BinderInternal;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import com.nothingos.utils.SystemUIDebugConfig;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class SystemUIService extends Service {
    private final BatteryStateNotifier mBatteryStateNotifier;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final DumpHandler mDumpHandler;
    private final LogBufferFreezer mLogBufferFreezer;
    private final Handler mMainHandler;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public SystemUIService(Handler handler, DumpHandler dumpHandler, BroadcastDispatcher broadcastDispatcher, LogBufferFreezer logBufferFreezer, BatteryStateNotifier batteryStateNotifier) {
        this.mMainHandler = handler;
        this.mDumpHandler = dumpHandler;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mLogBufferFreezer = logBufferFreezer;
        this.mBatteryStateNotifier = batteryStateNotifier;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        ((SystemUIApplication) getApplication()).startServicesIfNeeded();
        this.mLogBufferFreezer.attach(this.mBroadcastDispatcher);
        if (getResources().getBoolean(R$bool.config_showNotificationForUnknownBatteryState)) {
            this.mBatteryStateNotifier.startListening();
        }
        if (Build.IS_DEBUGGABLE && SystemProperties.getBoolean("debug.crash_sysui", false)) {
            throw new RuntimeException();
        }
        if (Build.IS_DEBUGGABLE) {
            BinderInternal.nSetBinderProxyCountEnabled(true);
            BinderInternal.nSetBinderProxyCountWatermarks(1000, 900);
            BinderInternal.setBinderProxyCountCallback(new BinderInternal.BinderProxyLimitListener() { // from class: com.android.systemui.SystemUIService.1
                public void onLimitReached(int i) {
                    Slog.w("SystemUIService", "uid " + i + " sent too many Binder proxies to uid " + Process.myUid());
                }
            }, this.mMainHandler);
        }
        startServiceAsUser(new Intent(getApplicationContext(), SystemUIAuxiliaryDumpService.class), UserHandle.SYSTEM);
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (setDebugOptions(printWriter, strArr)) {
            return;
        }
        String[] strArr2 = strArr.length == 0 ? new String[]{"--dump-priority", "CRITICAL"} : strArr;
        if (strArr.length > 0 && "--setAirplaneMode".equals(strArr[0])) {
            String str = "on";
            changeAirplaneModeSystemSetting(str.equals(strArr[1]));
            StringBuilder sb = new StringBuilder();
            sb.append("setAirplaneMode ");
            if (!str.equals(strArr[1])) {
                str = "off";
            }
            sb.append(str);
            printWriter.println(sb.toString());
            return;
        }
        this.mDumpHandler.dump(fileDescriptor, printWriter, strArr2);
    }

    @SuppressLint({"MissingPermission"})
    private void changeAirplaneModeSystemSetting(final boolean z) {
        Settings.Global.putInt(getContentResolver(), "airplane_mode_on", z ? 1 : 0);
        new Thread(new Runnable() { // from class: com.android.systemui.SystemUIService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SystemUIService.this.lambda$changeAirplaneModeSystemSetting$0(z);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$changeAirplaneModeSystemSetting$0(boolean z) {
        try {
            Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
            intent.addFlags(536870912);
            intent.putExtra("state", z);
            sendBroadcastAsUser(intent, UserHandle.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean setDebugOptions(PrintWriter printWriter, String[] strArr) {
        boolean z;
        if (strArr != null && strArr.length > 0 && TextUtils.equals(strArr[0], "-d")) {
            Field[] declaredFields = SystemUIDebugConfig.class.getDeclaredFields();
            if (strArr.length == 2) {
                if ("print".equals(strArr[1])) {
                    for (Field field : declaredFields) {
                        if (Modifier.isStatic(field.getModifiers()) && (field.getType() == Boolean.TYPE || field.getType() == Boolean.class || field.getType() == Integer.TYPE || field.getType() == Integer.class)) {
                            try {
                                field.setAccessible(true);
                                printWriter.println("      " + field.getName() + " = " + field.get(null));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    }
                    return true;
                }
            } else if (strArr.length == 3) {
                String str = strArr[1];
                String str2 = strArr[2];
                if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                    return false;
                }
                String lowerCase = str2.toLowerCase();
                if ("a".equals(lowerCase) || "all".equals(lowerCase) || "*".equals(lowerCase)) {
                    str2 = "(DEBUG_).*";
                }
                String str3 = str2;
                if ("enable".equals(str)) {
                    z = true;
                } else if (!"disable".equals(str)) {
                    return false;
                } else {
                    z = false;
                }
                ArrayList arrayList = new ArrayList();
                for (Field field2 : declaredFields) {
                    if (Modifier.isStatic(field2.getModifiers()) && Pattern.matches(str3, field2.getName())) {
                        field2.setAccessible(true);
                        if (field2.getType() == Boolean.TYPE || field2.getType() == Boolean.class) {
                            try {
                                field2.setBoolean(null, z);
                                arrayList.add(field2.getName());
                                printWriter.println("setDebugOptions: " + field2.getName() + " = " + z);
                            } catch (IllegalAccessException e2) {
                                e2.printStackTrace();
                            }
                        } else if (field2.getType() == Integer.TYPE || field2.getType() == Integer.class) {
                            try {
                                field2.setInt(null, z ? 1 : 0);
                                arrayList.add(field2.getName());
                                StringBuilder sb = new StringBuilder();
                                sb.append("setDebugOptions: ");
                                sb.append(field2.getName());
                                sb.append(" = ");
                                sb.append(z ? 1 : 0);
                                printWriter.println(sb.toString());
                            } catch (IllegalAccessException e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
