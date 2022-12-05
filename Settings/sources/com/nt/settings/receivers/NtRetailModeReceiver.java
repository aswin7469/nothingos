package com.nt.settings.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;
/* loaded from: classes2.dex */
public class NtRetailModeReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.e("NtRetailModeReceiver", "@_@ ------ Invalid broadcast received.");
        } else if (!TextUtils.isEmpty(SystemProperties.get("persist.sys.nt.wizard.locale"))) {
        } else {
            String str = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
            Log.d("NtRetailModeReceiver", "@_@ ------ initialize to persist wizard locale: " + str);
            SystemProperties.set("persist.sys.nt.wizard.locale", str);
            String string = Settings.Global.getString(context.getContentResolver(), "device_name");
            Log.d("NtRetailModeReceiver", "@_@ ------ initialize to persist device name: " + string);
            SystemProperties.set("persist.sys.nt.device.name", string);
            String string2 = Settings.Secure.getString(context.getContentResolver(), "navigation_mode");
            Log.d("NtRetailModeReceiver", "@_@ ------ initialize to persist navigation mode: " + string2);
            SystemProperties.set("persist.sys.nt.navigation.mode", string2);
        }
    }
}
