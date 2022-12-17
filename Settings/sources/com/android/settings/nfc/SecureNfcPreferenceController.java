package com.android.settings.nfc;

import android.content.Context;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.UserManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class SecureNfcPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private final NfcAdapter mNfcAdapter;
    private SecureNfcEnabler mSecureNfcEnabler;
    private final UserManager mUserManager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public boolean hasAsyncUpdate() {
        return true;
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SecureNfcPreferenceController(Context context, String str) {
        super(context, str);
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (!isAvailable()) {
            this.mSecureNfcEnabler = null;
            return;
        }
        this.mSecureNfcEnabler = new SecureNfcEnabler(this.mContext, (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey()));
    }

    public boolean isChecked() {
        return this.mNfcAdapter.isSecureNfcEnabled();
    }

    public boolean setChecked(boolean z) {
        if (isToggleable()) {
            return this.mNfcAdapter.enableSecureNfc(z);
        }
        return false;
    }

    public int getAvailabilityStatus() {
        NfcAdapter nfcAdapter = this.mNfcAdapter;
        if (nfcAdapter != null && nfcAdapter.isSecureNfcSupported()) {
            return 0;
        }
        return 3;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_connected_devices;
    }

    public void onResume() {
        SecureNfcEnabler secureNfcEnabler = this.mSecureNfcEnabler;
        if (secureNfcEnabler != null) {
            secureNfcEnabler.resume();
        }
    }

    public void onPause() {
        SecureNfcEnabler secureNfcEnabler = this.mSecureNfcEnabler;
        if (secureNfcEnabler != null) {
            secureNfcEnabler.pause();
        }
    }

    private boolean isToggleable() {
        return !this.mUserManager.isGuestUser();
    }
}
