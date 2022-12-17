package com.android.settings.network.telephony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyManager;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class RoamingDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private CarrierConfigManager mCarrierConfigManager;
    private int mSubId;
    private int mType;

    public int getMetricsCategory() {
        return 1583;
    }

    public static RoamingDialogFragment newInstance(int i, int i2) {
        RoamingDialogFragment roamingDialogFragment = new RoamingDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dialog_type", i);
        bundle.putInt("sub_id_key", i2);
        roamingDialogFragment.setArguments(bundle);
        return roamingDialogFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mSubId = getArguments().getInt("sub_id_key");
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        this.mType = getArguments().getInt("dialog_type");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R$style.TelephonyToggleAlertDialog);
        int i = this.mType;
        if (i == 0) {
            int i2 = R$string.roaming_warning;
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mSubId);
            if (configForSubId != null && configForSubId.getBoolean("check_pricing_with_carrier_data_roaming_bool")) {
                i2 = R$string.roaming_check_price_warning;
            }
            builder.setMessage(getResources().getString(i2)).setTitle(getResources().getString(R$string.roaming_alert_title));
        } else if (i == 1) {
            builder.setTitle(R$string.roaming_disable_title).setMessage(R$string.roaming_disable_dialog_ciwlan_call);
        }
        builder.setIconAttribute(16843605).setPositiveButton(17039379, this).setNegativeButton(17039369, this);
        return builder.create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        TelephonyManager createForSubscriptionId = ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        if (createForSubscriptionId != null) {
            int i2 = this.mType;
            if (i2 != 0) {
                if (i2 == 1 && i == -1) {
                    createForSubscriptionId.setDataRoamingEnabled(false);
                }
            } else if (i == -1) {
                createForSubscriptionId.setDataRoamingEnabled(true);
            }
        }
    }
}
