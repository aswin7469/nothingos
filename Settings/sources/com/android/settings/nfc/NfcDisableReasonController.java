package com.android.settings.nfc;

import android.content.Context;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.widget.FooterPreference;
/* loaded from: classes.dex */
public class NfcDisableReasonController extends BasePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private final NfcAdapter mNfcAdapter;
    private NfcDisabledReasonEnabler mNfcEnabler;
    private FooterPreference mPreference;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NfcDisableReasonController(Context context, String str) {
        super(context, str);
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        FooterPreference footerPreference = (FooterPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = footerPreference;
        this.mNfcEnabler = new NfcDisabledReasonEnabler(this.mContext, footerPreference);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        NfcDisabledReasonEnabler nfcDisabledReasonEnabler = this.mNfcEnabler;
        if (nfcDisabledReasonEnabler != null) {
            nfcDisabledReasonEnabler.resume();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        NfcDisabledReasonEnabler nfcDisabledReasonEnabler = this.mNfcEnabler;
        if (nfcDisabledReasonEnabler != null) {
            nfcDisabledReasonEnabler.pause();
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        NfcDisabledReasonEnabler nfcDisabledReasonEnabler = this.mNfcEnabler;
        return (nfcDisabledReasonEnabler == null || !nfcDisabledReasonEnabler.isWirelessCharging()) ? 3 : 0;
    }

    /* loaded from: classes.dex */
    class NfcDisabledReasonEnabler extends BaseNfcEnabler {
        private final Preference mPreference;

        @Override // com.android.settings.nfc.BaseNfcEnabler
        protected String tag() {
            return "NfcDisabledReasonEnabler";
        }

        public NfcDisabledReasonEnabler(Context context, Preference preference) {
            super(context);
            this.mPreference = preference;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.settings.nfc.BaseNfcEnabler
        public void onWirelessCharging(boolean z) {
            super.onWirelessCharging(z);
            this.mPreference.setVisible(z);
            Log.d("NfcDisabledReasonEnabler", "onWirelessCharging " + z);
        }

        @Override // com.android.settings.nfc.BaseNfcEnabler
        protected void handleNfcStateChanged(int i) {
            Log.d("NfcDisabledReasonEnabler", "handleNfcStateChanged " + i);
            if (i == 2 || i == 3) {
                this.mPreference.setVisible(false);
            }
        }

        @Override // com.android.settings.nfc.BaseNfcEnabler
        public void resume() {
            if (!isNfcAvailable()) {
                return;
            }
            super.resume();
        }
    }
}
