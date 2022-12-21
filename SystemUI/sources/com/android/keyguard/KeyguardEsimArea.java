package com.android.keyguard;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.UserHandle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.euicc.EuiccManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.systemui.C1893R;
import java.sql.Types;

class KeyguardEsimArea extends Button implements View.OnClickListener {
    private static final String ACTION_DISABLE_ESIM = "com.android.keyguard.disable_esim";
    private static final String PERMISSION_SELF = "com.android.systemui.permission.SELF";
    private static final String TAG = "KeyguardEsimArea";
    private EuiccManager mEuiccManager;
    private BroadcastReceiver mReceiver;
    private int mSubscriptionId;

    public KeyguardEsimArea(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 16974425);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int resultCode;
                if (KeyguardEsimArea.ACTION_DISABLE_ESIM.equals(intent.getAction()) && (resultCode = getResultCode()) != 0) {
                    Log.e(KeyguardEsimArea.TAG, "Error disabling esim, result code = " + resultCode);
                    AlertDialog create = new AlertDialog.Builder(KeyguardEsimArea.this.mContext).setMessage(C1893R.string.error_disable_esim_msg).setTitle(C1893R.string.error_disable_esim_title).setCancelable(false).setPositiveButton(C1893R.string.f262ok, (DialogInterface.OnClickListener) null).create();
                    create.getWindow().setType(Types.SQLXML);
                    create.show();
                }
            }
        };
        this.mEuiccManager = (EuiccManager) context.getSystemService("euicc");
        setOnClickListener(this);
    }

    public void setSubscriptionId(int i) {
        this.mSubscriptionId = i;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter(ACTION_DISABLE_ESIM), "com.android.systemui.permission.SELF", (Handler) null, 2);
    }

    public static boolean isEsimLocked(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        if (((EuiccManager) context.getSystemService("euicc")).isEnabled() && (activeSubscriptionInfo = SubscriptionManager.from(context).getActiveSubscriptionInfo(i)) != null && activeSubscriptionInfo.isEmbedded()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mContext.unregisterReceiver(this.mReceiver);
        super.onDetachedFromWindow();
    }

    public void onClick(View view) {
        SubscriptionInfo activeSubscriptionInfo = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfo(this.mSubscriptionId);
        if (activeSubscriptionInfo == null) {
            Log.e(TAG, "No active subscription with subscriptionId: " + this.mSubscriptionId);
            return;
        }
        Intent intent = new Intent(ACTION_DISABLE_ESIM);
        intent.setPackage(this.mContext.getPackageName());
        this.mEuiccManager.switchToSubscription(-1, activeSubscriptionInfo.getPortIndex(), PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, 167772160, UserHandle.SYSTEM));
    }
}
