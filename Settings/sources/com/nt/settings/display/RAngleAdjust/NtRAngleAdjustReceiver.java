package com.nt.settings.display.RAngleAdjust;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
/* loaded from: classes2.dex */
public class NtRAngleAdjustReceiver extends BroadcastReceiver {
    private NtRAngleAdjustController mNtRAngleAdustHelper;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("NtRAngleAdjustReceiver", "action:" + action);
        if (TextUtils.isEmpty(action)) {
            return;
        }
        this.mNtRAngleAdustHelper = NtRAngleAdjustController.getInstance(context);
        if (!"android.intent.action.PRE_BOOT_COMPLETED".equals(action)) {
            return;
        }
        initRAngleAppList();
    }

    private void initRAngleAppList() {
        this.mNtRAngleAdustHelper.initLocalRAngleAppList();
    }
}
