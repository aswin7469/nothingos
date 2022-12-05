package com.android.settings.applications.specialaccess;

import android.content.Context;
import android.nfc.cardemulation.CardEmulation;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.nfc.BaseNfcEnabler;
/* loaded from: classes.dex */
public class PaymentSettingsEnabler extends BaseNfcEnabler {
    private final CardEmulation mCardEmuManager = CardEmulation.getInstance(this.mNfcAdapter);
    boolean mIsPaymentAvailable = false;
    private final Preference mPreference;

    @Override // com.android.settings.nfc.BaseNfcEnabler
    protected String tag() {
        return "PaymentSettingsEnabler";
    }

    public PaymentSettingsEnabler(Context context, Preference preference) {
        super(context);
        this.mPreference = preference;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.nfc.BaseNfcEnabler
    public void onWirelessCharging(boolean z) {
        super.onWirelessCharging(z);
        this.mPreference.setVisible(!z);
    }

    @Override // com.android.settings.nfc.BaseNfcEnabler
    protected void handleNfcStateChanged(int i) {
        if (i == 1) {
            this.mPreference.setSummary(R.string.nfc_and_payment_settings_payment_off_nfc_off_summary);
            this.mPreference.setEnabled(false);
        } else if (i != 3) {
        } else {
            if (this.mIsPaymentAvailable) {
                this.mPreference.setSummary((CharSequence) null);
                this.mPreference.setEnabled(true);
                return;
            }
            this.mPreference.setSummary(R.string.nfc_and_payment_settings_no_payment_installed_summary);
            this.mPreference.setEnabled(false);
        }
    }

    @Override // com.android.settings.nfc.BaseNfcEnabler
    public void resume() {
        if (!isNfcAvailable()) {
            return;
        }
        if (this.mCardEmuManager.getServices("payment").isEmpty()) {
            this.mIsPaymentAvailable = false;
        } else {
            this.mIsPaymentAvailable = true;
        }
        super.resume();
    }
}
