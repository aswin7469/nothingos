package com.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R$bool;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class EncryptionStatusPreferenceController extends BasePreferenceController {
    public static final String PREF_KEY_ENCRYPTION_DETAIL_PAGE = "encryption_and_credentials_encryption_status";
    public static final String PREF_KEY_ENCRYPTION_SECURITY_PAGE = "encryption_and_credential";
    private final UserManager mUserManager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public EncryptionStatusPreferenceController(Context context, String str) {
        super(context, str);
        this.mUserManager = (UserManager) context.getSystemService("user");
    }

    public int getAvailabilityStatus() {
        return (!TextUtils.equals(getPreferenceKey(), PREF_KEY_ENCRYPTION_DETAIL_PAGE) || this.mContext.getResources().getBoolean(R$bool.config_show_encryption_and_credentials_encryption_status)) ? 0 : 3;
    }

    public void updateState(Preference preference) {
        if (LockPatternUtils.isDeviceEncryptionEnabled()) {
            preference.setSummary(R$string.encrypted_summary);
        } else {
            preference.setSummary(R$string.not_encrypted_summary);
        }
    }
}
