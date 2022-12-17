package com.android.settings.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$string;
import com.android.settings.wifi.dpp.WifiDppUtils;
import com.android.settingslib.R$layout;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;

public class AddWifiNetworkPreference extends Preference {
    @VisibleForTesting
    boolean mIsAddWifiConfigAllow;
    private final Drawable mScanIconDrawable = getDrawable(R$drawable.ic_scan_24dp);

    public AddWifiNetworkPreference(Context context) {
        super(context);
        setLayoutResource(R$layout.preference_access_point);
        setWidgetLayoutResource(com.android.settings.R$layout.wifi_button_preference_widget);
        setIcon(R$drawable.ic_add_24dp);
        setTitle(R$string.wifi_add_network);
        this.mIsAddWifiConfigAllow = WifiEnterpriseRestrictionUtils.isAddWifiConfigAllowed(context);
        updatePreferenceForRestriction();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageButton imageButton = (ImageButton) preferenceViewHolder.findViewById(R$id.button_icon);
        imageButton.setImageDrawable(this.mScanIconDrawable);
        imageButton.setContentDescription(getContext().getString(R$string.wifi_dpp_scan_qr_code));
        imageButton.setOnClickListener(new AddWifiNetworkPreference$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        getContext().startActivity(WifiDppUtils.getEnrolleeQrCodeScannerIntent(getContext(), (String) null));
    }

    private Drawable getDrawable(int i) {
        try {
            return getContext().getDrawable(i);
        } catch (Resources.NotFoundException unused) {
            Log.e("AddWifiNetworkPreference", "Resource does not exist: " + i);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void updatePreferenceForRestriction() {
        if (!this.mIsAddWifiConfigAllow) {
            setEnabled(false);
            setSummary(R$string.not_allowed_by_ent);
        }
    }
}
