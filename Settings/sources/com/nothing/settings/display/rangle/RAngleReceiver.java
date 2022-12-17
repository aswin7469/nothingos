package com.nothing.settings.display.rangle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class RAngleReceiver extends BroadcastReceiver {
    private RAngleController mController;

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("RAngle", "RAngleReceiver::onReceive action:" + action);
        if (!TextUtils.isEmpty(action)) {
            this.mController = RAngleController.getInstance(context);
            if ("android.intent.action.PRE_BOOT_COMPLETED".equals(action)) {
                initRAngleAppList();
            }
        }
    }

    private void initRAngleAppList() {
        this.mController.initLocalRAngleAppList();
    }
}
