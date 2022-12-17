package com.android.settings.network.telephony;

import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.network.telephony.ProgressDialogFragment;

public class SubscriptionActionDialogActivity extends FragmentActivity {
    protected SubscriptionManager mSubscriptionManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSubscriptionManager = (SubscriptionManager) getSystemService(SubscriptionManager.class);
        setProgressState(0);
    }

    public void finish() {
        setProgressState(0);
        super.finish();
    }

    /* access modifiers changed from: protected */
    public void showProgressDialog(String str) {
        showProgressDialog(str, false);
    }

    /* access modifiers changed from: protected */
    public void showProgressDialog(String str, boolean z) {
        ProgressDialogFragment.show(getFragmentManager(), str, (ProgressDialogFragment.OnDismissCallback) null);
        if (z) {
            setProgressState(1);
        }
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        ProgressDialogFragment.dismiss(getFragmentManager());
        setProgressState(0);
    }

    /* access modifiers changed from: protected */
    public void showErrorDialog(String str, String str2) {
        AlertDialogFragment.show(this, str, str2);
    }

    /* access modifiers changed from: protected */
    public void setProgressState(int i) {
        getSharedPreferences("sim_action_dialog_prefs", 0).edit().putInt("progress_state", i).apply();
        Log.i("SubscriptionActionDialogActivity", "setProgressState:" + i);
    }
}
