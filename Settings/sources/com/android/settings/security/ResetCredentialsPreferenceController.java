package com.android.settings.security;

import android.content.Context;
import android.security.keystore2.AndroidKeyStoreLoadStoreParameter;
import androidx.constraintlayout.widget.R$styleable;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.security.KeyStore;
import java.security.KeyStoreException;
/* loaded from: classes.dex */
public class ResetCredentialsPreferenceController extends RestrictedEncryptionPreferenceController implements LifecycleObserver, OnResume {
    private final KeyStore mKeyStore;
    private RestrictedPreference mPreference;
    private final KeyStore mWifiKeyStore;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "credentials_reset";
    }

    public ResetCredentialsPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, "no_config_credentials");
        KeyStore keyStore;
        KeyStore keyStore2 = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception unused) {
            keyStore = null;
        }
        this.mKeyStore = keyStore;
        if (context.getUser().isSystem()) {
            try {
                KeyStore keyStore3 = KeyStore.getInstance("AndroidKeyStore");
                keyStore3.load(new AndroidKeyStoreLoadStoreParameter((int) R$styleable.Constraint_layout_goneMarginStart));
                keyStore2 = keyStore3;
            } catch (Exception unused2) {
            }
        }
        this.mWifiKeyStore = keyStore2;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0025, code lost:
        if (r1.aliases().hasMoreElements() != false) goto L11;
     */
    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onResume() {
        KeyStore keyStore;
        RestrictedPreference restrictedPreference = this.mPreference;
        if (restrictedPreference == null || restrictedPreference.isDisabledByAdmin()) {
            return;
        }
        boolean z = false;
        try {
            keyStore = this.mKeyStore;
        } catch (KeyStoreException unused) {
        }
        if (keyStore == null || !keyStore.aliases().hasMoreElements()) {
            KeyStore keyStore2 = this.mWifiKeyStore;
            if (keyStore2 != null) {
            }
            this.mPreference.setEnabled(z);
        }
        z = true;
        this.mPreference.setEnabled(z);
    }
}
