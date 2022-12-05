package com.android.settings;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.qualcomm.qcrilhook.QcRilHook;
import java.nio.ByteBuffer;
import org.codeaurora.telephony.utils.AsyncResult;
/* loaded from: classes.dex */
public class SoftVersionBroadcastReceiver extends BroadcastReceiver {
    private static Context mContext;
    private static volatile QcRilHook mQcRilHook;
    private final AolHandler mAolHandle = new AolHandler();

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null || !intent.getAction().equals("android.telephony.action.SECRET_CODE")) {
            return;
        }
        mContext = context.getApplicationContext();
        mQcRilHook = new QcRilHook(mContext);
        String host = intent.getData().getHost();
        if ("386".equals(host)) {
            this.mAolHandle.sendMessage(this.mAolHandle.obtainMessage(3));
        } else if ("727".equals(host)) {
            this.mAolHandle.sendMessage(this.mAolHandle.obtainMessage(4));
        } else if (!"8377466".equals(host)) {
        } else {
            Intent intent2 = new Intent("android.intent.action.MAIN");
            intent2.setClass(context, SoftVersionDisplayActivity.class);
            intent2.setFlags(268468224);
            context.startActivity(intent2);
        }
    }

    /* loaded from: classes.dex */
    private static class AolHandler extends Handler {
        private AolHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            AlertDialog.Builder builder = new AlertDialog.Builder(SoftVersionBroadcastReceiver.mContext);
            int i = message.what;
            if (i != 3) {
                if (i != 4) {
                    return;
                }
                builder.setTitle(SoftVersionBroadcastReceiver.mContext.getResources().getString(R.string.sar_aol_dialog_title));
                builder.setCancelable(false);
                builder.setPositiveButton(SoftVersionBroadcastReceiver.mContext.getResources().getString(R.string.sar_aol_dialog_ok), (DialogInterface.OnClickListener) null);
                SoftVersionBroadcastReceiver.dealSarAolReset();
                builder.setMessage("Finished!");
                AlertDialog create = builder.create();
                create.getWindow().setType(2003);
                create.show();
                return;
            }
            builder.setTitle(SoftVersionBroadcastReceiver.mContext.getResources().getString(R.string.ftm_dialog_title));
            builder.setCancelable(false);
            builder.setPositiveButton(SoftVersionBroadcastReceiver.mContext.getResources().getString(R.string.ftm_dialog_ok), (DialogInterface.OnClickListener) null);
            int modemAolState = SoftVersionBroadcastReceiver.getModemAolState();
            Log.w("SoftVersionBroadcastReceiver", "modemAolState = " + modemAolState);
            if (modemAolState == 1) {
                builder.setMessage("Ftm");
            } else if (modemAolState == 0) {
                builder.setMessage("Online");
            } else {
                builder.setMessage("Unknown");
            }
            AlertDialog create2 = builder.create();
            create2.getWindow().setType(2003);
            create2.show();
        }
    }

    public static int getFtmMode() {
        Log.d("SoftVersionBroadcastReceiver", "getFtmMode start ...");
        AsyncResult sendQcRilHookMsg = mQcRilHook.sendQcRilHookMsg(524495);
        if (sendQcRilHookMsg.exception == null) {
            if (sendQcRilHookMsg.result != null) {
                byte b = ByteBuffer.wrap((byte[]) sendQcRilHookMsg.result).get();
                Log.w("SoftVersionBroadcastReceiver", "getFtmMode mode = " + ((int) b));
                return b;
            }
            Log.e("SoftVersionBroadcastReceiver", "QCRIL Get compatibility key command returned a null result.");
        } else {
            Log.e("SoftVersionBroadcastReceiver", "QCRIL Get compatibility key command returned Exception: " + sendQcRilHookMsg.exception);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getModemAolState() {
        return getFtmMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dealSarAolReset() {
        int compatibilityKey = getCompatibilityKey();
        setRfmScenarioToModem(new byte[]{1, 0, 0, 0, 0, 0, 0, 0});
        setTransmitPowerToModem(new byte[]{0, 0, 0, 0, (byte) compatibilityKey, 0, 0, 0});
        SystemProperties.set("persist.service.sar.enable", "0");
        SystemProperties.set("persist.service.aol.enable", "0");
    }

    private static void setTransmitPowerToModem(byte[] bArr) {
        mQcRilHook.sendQcRilHookMsg(524496, bArr);
    }

    private static void setRfmScenarioToModem(byte[] bArr) {
        Log.d("SoftVersionBroadcastReceiver", "setRfmScenarioToModem start ...  ");
        mQcRilHook.sendQcRilHookMsg(530489, bArr);
        Log.d("SoftVersionBroadcastReceiver", "setRfmScenarioToModem end ...  ");
    }

    private static int getCompatibilityKey() {
        AsyncResult sendQcRilHookMsg = mQcRilHook.sendQcRilHookMsg(524488);
        if (sendQcRilHookMsg.exception == null) {
            if (sendQcRilHookMsg.result != null) {
                return ByteBuffer.wrap((byte[]) sendQcRilHookMsg.result).get();
            }
            Log.e("SoftVersionBroadcastReceiver", "QCRIL Get compatibility key command returned a null result.");
        } else {
            Log.e("SoftVersionBroadcastReceiver", "QCRIL Get the compatibility key list Command returned Exception: " + sendQcRilHookMsg.exception);
        }
        return 0;
    }
}
