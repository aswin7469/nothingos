package com.nothing.settings.softwareinfo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.settings.R$string;
import com.qualcomm.qcrilhook.QcRilHook;
import java.nio.ByteBuffer;
import org.codeaurora.telephony.utils.AsyncResult;

public class SoftVersionBroadcastReceiver extends BroadcastReceiver {
    /* access modifiers changed from: private */
    public static Context mContext;
    private static volatile QcRilHook mQcRilHook;
    private final AolHandler mAolHandle = new AolHandler();

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.telephony.action.SECRET_CODE")) {
            mContext = context.getApplicationContext();
            mQcRilHook = new QcRilHook(mContext);
            Message obtainMessage = this.mAolHandle.obtainMessage(3);
            String host = intent.getData().getHost();
            if ("386".equals(host)) {
                this.mAolHandle.sendMessage(obtainMessage);
            } else if ("8377466".equals(host)) {
                Intent intent2 = new Intent("android.intent.action.MAIN");
                intent2.setClass(context, SoftVersionDisplayActivity.class);
                intent2.setFlags(268468224);
                context.startActivity(intent2);
            }
        }
    }

    private static class AolHandler extends Handler {
        private AolHandler() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            AlertDialog.Builder builder = new AlertDialog.Builder(SoftVersionBroadcastReceiver.mContext);
            builder.setTitle(SoftVersionBroadcastReceiver.mContext.getResources().getString(R$string.ftm_dialog_title));
            builder.setCancelable(false);
            builder.setPositiveButton(SoftVersionBroadcastReceiver.mContext.getResources().getString(R$string.ftm_dialog_ok), (DialogInterface.OnClickListener) null);
            if (message.what == 3) {
                int r3 = SoftVersionBroadcastReceiver.getModemAolState();
                Log.w("SoftVersionBroadcastReceiver", "modemAolState = " + r3);
                if (r3 == 1) {
                    builder.setMessage("Ftm");
                } else if (r3 == 0) {
                    builder.setMessage("Online");
                } else {
                    builder.setMessage("Unknown");
                }
                AlertDialog create = builder.create();
                create.getWindow().setType(2003);
                create.show();
            }
        }
    }

    public static int getFtmMode() {
        Log.d("SoftVersionBroadcastReceiver", "getFtmMode start ...");
        AsyncResult sendQcRilHookMsg = mQcRilHook.sendQcRilHookMsg(524495);
        if (sendQcRilHookMsg.exception != null) {
            Log.e("SoftVersionBroadcastReceiver", "QCRIL Get compatibility key command returned Exception: " + sendQcRilHookMsg.exception);
        } else if (sendQcRilHookMsg.result != null) {
            byte b = ByteBuffer.wrap((byte[]) sendQcRilHookMsg.result).get();
            Log.w("SoftVersionBroadcastReceiver", "getFtmMode mode = " + b);
            return b;
        } else {
            Log.e("SoftVersionBroadcastReceiver", "QCRIL Get compatibility key command returned a null result.");
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public static int getModemAolState() {
        return getFtmMode();
    }
}
