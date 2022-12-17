package com.android.settings.connecteddevice;

import android.content.Context;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class NfcAndPaymentFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nfc_and_payment_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            if (((UserManager) context.getSystemService(UserManager.class)).getUserInfo(UserHandle.myUserId()).isGuest()) {
                return false;
            }
            return context.getPackageManager().hasSystemFeature("android.hardware.nfc");
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "NfcAndPaymentFragment";
    }

    public int getMetricsCategory() {
        return 1828;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.nfc_and_payment_settings;
    }

    public int getHelpResource() {
        return R$string.help_uri_nfc_and_payment_settings;
    }
}
