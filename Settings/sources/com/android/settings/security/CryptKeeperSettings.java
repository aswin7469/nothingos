package com.android.settings.security;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.settings.CryptKeeperConfirm;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.password.ChooseLockSettingsHelper;
/* loaded from: classes.dex */
public class CryptKeeperSettings extends InstrumentedPreferenceFragment {
    private View mBatteryWarning;
    private View mContentView;
    private Button mInitiateButton;
    private IntentFilter mIntentFilter;
    private View mPowerWarning;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.settings.security.CryptKeeperSettings.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                int i = 0;
                int intExtra = intent.getIntExtra("level", 0);
                int intExtra2 = intent.getIntExtra("plugged", 0);
                int intExtra3 = intent.getIntExtra("invalid_charger", 0);
                boolean z = true;
                boolean z2 = intExtra >= 80;
                boolean z3 = (intExtra2 & 7) != 0 && intExtra3 == 0;
                Button button = CryptKeeperSettings.this.mInitiateButton;
                if (!z2 || !z3) {
                    z = false;
                }
                button.setEnabled(z);
                CryptKeeperSettings.this.mPowerWarning.setVisibility(z3 ? 8 : 0);
                View view = CryptKeeperSettings.this.mBatteryWarning;
                if (z2) {
                    i = 8;
                }
                view.setVisibility(i);
            }
        }
    };
    private View.OnClickListener mInitiateListener = new View.OnClickListener() { // from class: com.android.settings.security.CryptKeeperSettings.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (!CryptKeeperSettings.this.runKeyguardConfirmation(55)) {
                new AlertDialog.Builder(CryptKeeperSettings.this.getActivity()).setTitle(R.string.crypt_keeper_dialog_need_password_title).setMessage(R.string.crypt_keeper_dialog_need_password_message).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create().show();
            }
        }
    };

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 32;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = layoutInflater.inflate(R.layout.crypt_keeper_settings, (ViewGroup) null);
        IntentFilter intentFilter = new IntentFilter();
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        Button button = (Button) this.mContentView.findViewById(R.id.initiate_encrypt);
        this.mInitiateButton = button;
        button.setOnClickListener(this.mInitiateListener);
        this.mInitiateButton.setEnabled(false);
        this.mPowerWarning = this.mContentView.findViewById(R.id.warning_unplugged);
        this.mBatteryWarning = this.mContentView.findViewById(R.id.warning_low_charge);
        return this.mContentView;
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.mIntentReceiver, this.mIntentFilter);
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mIntentReceiver);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        DevicePolicyManager devicePolicyManager;
        super.onActivityCreated(bundle);
        FragmentActivity activity = getActivity();
        if ("android.app.action.START_ENCRYPTION".equals(activity.getIntent().getAction()) && (devicePolicyManager = (DevicePolicyManager) activity.getSystemService("device_policy")) != null && devicePolicyManager.getStorageEncryptionStatus() != 1) {
            activity.finish();
        }
        activity.setTitle(R.string.crypt_keeper_encrypt_title);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean runKeyguardConfirmation(int i) {
        if (new LockPatternUtils(getActivity()).getKeyguardStoredPasswordQuality(UserHandle.myUserId()) == 0) {
            showFinalConfirmation(1, "".getBytes());
            return true;
        }
        return new ChooseLockSettingsHelper.Builder(getActivity(), this).setRequestCode(i).setTitle(getActivity().getResources().getText(R.string.crypt_keeper_encrypt_title)).setReturnCredentials(true).show();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 55 && i2 == -1 && intent != null) {
            int intExtra = intent.getIntExtra("type", -1);
            LockscreenCredential parcelableExtra = intent.getParcelableExtra("password");
            if (parcelableExtra == null || parcelableExtra.isNone()) {
                return;
            }
            showFinalConfirmation(intExtra, parcelableExtra.getCredential());
        }
    }

    private void showFinalConfirmation(int i, byte[] bArr) {
        Preference preference = new Preference(getPreferenceManager().getContext());
        preference.setFragment(CryptKeeperConfirm.class.getName());
        preference.setTitle(R.string.crypt_keeper_confirm_title);
        addEncryptionInfoToPreference(preference, i, bArr);
        ((SettingsActivity) getActivity()).onPreferenceStartFragment((PreferenceFragmentCompat) null, preference);
    }

    private void addEncryptionInfoToPreference(Preference preference, int i, byte[] bArr) {
        if (((DevicePolicyManager) getActivity().getSystemService("device_policy")).getDoNotAskCredentialsOnBoot()) {
            preference.getExtras().putInt("type", 1);
            preference.getExtras().putByteArray("password", "".getBytes());
            return;
        }
        preference.getExtras().putInt("type", i);
        preference.getExtras().putByteArray("password", bArr);
    }
}
