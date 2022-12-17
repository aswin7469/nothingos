package com.android.settings.network.telephony;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class BackupCallingDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private int mSubId;
    private int mType;

    public int getMetricsCategory() {
        return 1927;
    }

    public static BackupCallingDialogFragment newInstance(int i, int i2) {
        BackupCallingDialogFragment backupCallingDialogFragment = new BackupCallingDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dialog_type", i);
        bundle.putInt("subId", i2);
        backupCallingDialogFragment.setArguments(bundle);
        return backupCallingDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        Context context = getContext();
        this.mType = arguments.getInt("dialog_type");
        this.mSubId = arguments.getInt("subId");
        if (this.mType == 0) {
            return new AlertDialog.Builder(context).setMessage(R$string.backup_calling_disable_dialog_ciwlan_call).setPositiveButton(17039370, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        }
        throw new IllegalArgumentException("Unknown type " + this.mType);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.mType == 0) {
            disableCiwlan();
            return;
        }
        throw new IllegalArgumentException("Unknown type " + this.mType);
    }

    private void disableCiwlan() {
        ImsMmTelManager imsMmTelManager = getImsMmTelManager();
        if (imsMmTelManager == null) {
            Log.e("BackupCallingDialogFragment", "imsMmTelMgr null");
            return;
        }
        try {
            imsMmTelManager.setCrossSimCallingEnabled(false);
        } catch (ImsException e) {
            Log.e("BackupCallingDialogFragment", "Failed to disable cross SIM calling", e);
        }
    }

    private ImsMmTelManager getImsMmTelManager() {
        ImsManager imsManager;
        if (SubscriptionManager.isUsableSubscriptionId(this.mSubId) && (imsManager = (ImsManager) getContext().getSystemService(ImsManager.class)) != null) {
            return imsManager.getImsMmTelManager(this.mSubId);
        }
        return null;
    }
}
