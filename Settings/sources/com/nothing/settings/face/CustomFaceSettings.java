package com.nothing.settings.face;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.p006ui.support.NtCustSwitchPreference;
import com.p007nt.facerecognition.manager.FaceMetadata;
import com.p007nt.facerecognition.manager.NtFaceRecognitionManager;
import java.util.ArrayList;
import java.util.List;

public class CustomFaceSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_security_settings_face) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return CustomFaceSettings.buildPreferenceControllers(context, (CustomFaceSettings) null, (Lifecycle) null);
        }
    };
    private final int ADD_FACE_REQUEST = 101;
    private Preference mAddFacePref;
    private boolean mAddingFace;
    private boolean mConfirmingCredentials;
    private PreferenceCategory mFaceDataPrefCategory;
    /* access modifiers changed from: private */
    public IFaceEnroll mFaceEnroll;
    private boolean mFromFaceCreateActivity;
    private final NtFaceRecognitionManager.RemovalCallback mRemovalCallback = new NtFaceRecognitionManager.RemovalCallback() {
        public void onRemovalError(FaceMetadata faceMetadata, int i, CharSequence charSequence) {
            super.onRemovalError(faceMetadata, i, charSequence);
        }

        public void onRemovalSucceeded(FaceMetadata faceMetadata, int i) {
            super.onRemovalSucceeded(faceMetadata, i);
            CustomFaceSettings.this.updatePrefStatus();
            int faceMetadatasCount = CustomFaceSettings.this.mFaceEnroll.getFaceMetadatasCount();
            FaceUtils.setFaceDataCount(CustomFaceSettings.this.getPrefContext(), faceMetadatasCount);
            if (faceMetadatasCount <= 0) {
                CustomFaceSettings.this.finish();
            }
        }
    };
    private final DialogInterface.OnClickListener mRenameConfirmListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
            CustomFaceSettings.this.updatePrefStatus();
        }
    };
    private NtCustSwitchPreference mSkipSwipePref;
    private NtCustSwitchPreference mUnlockWithMaskPref;

    public int getMetricsCategory() {
        return 1511;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean booleanExtra = getIntent().getBooleanExtra("from_create_face_settings", false);
        this.mFromFaceCreateActivity = booleanExtra;
        if (!this.mConfirmingCredentials && !booleanExtra) {
            this.mConfirmingCredentials = true;
            launchChooseOrConfirmLock();
        }
        startBootFaceRecognitionService(getPrefContext());
        initValues();
        initPreference();
        updatePrefStatus();
    }

    public void onStop() {
        super.onStop();
        if (!this.mConfirmingCredentials && !this.mAddingFace) {
            getActivity().finish();
        }
    }

    private void initValues() {
        this.mFaceEnroll = new FaceEnrollImpl(getPrefContext(), new Handler());
    }

    private void initPreference() {
        this.mFaceDataPrefCategory = (PreferenceCategory) findPreference("face_datas");
        this.mAddFacePref = findPreference("add_face");
        NtCustSwitchPreference ntCustSwitchPreference = (NtCustSwitchPreference) findPreference("skip_swipe");
        this.mSkipSwipePref = ntCustSwitchPreference;
        ntCustSwitchPreference.setChecked(isSkipSwipeEnabled());
        NtCustSwitchPreference ntCustSwitchPreference2 = (NtCustSwitchPreference) findPreference("unlock_with_mask");
        this.mUnlockWithMaskPref = ntCustSwitchPreference2;
        ntCustSwitchPreference2.setChecked(isUnlockWithMaskEnabled());
    }

    private void startBootFaceRecognitionService(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.nt.facerecognition", "com.nt.facerecognition.server.BootFaceRecognitionService"));
        intent.putExtra("start_app", "com.android.settings");
        context.startForegroundService(intent);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4 || i == 1) {
            if (i2 == 1 || i2 == -1) {
                this.mConfirmingCredentials = false;
            } else {
                finish();
            }
        } else if (i == 101) {
            this.mAddingFace = false;
            if (i2 == 1) {
                finish();
            } else if (i2 == 2) {
                updatePrefStatus();
            }
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == this.mAddFacePref) {
            Intent intent = new Intent(getPrefContext(), FaceRecognitionActivity.class);
            intent.putExtra("from_face_settings", true);
            this.mAddingFace = true;
            startActivityForResult(intent, 101);
        } else {
            NtCustSwitchPreference ntCustSwitchPreference = this.mSkipSwipePref;
            if (preference == ntCustSwitchPreference) {
                setSkipSwipeEnabled(ntCustSwitchPreference.isChecked());
            } else {
                NtCustSwitchPreference ntCustSwitchPreference2 = this.mUnlockWithMaskPref;
                if (preference == ntCustSwitchPreference2) {
                    setUnlockWithMaskEnabled(ntCustSwitchPreference2.isChecked());
                }
            }
        }
        return super.onPreferenceTreeClick(preference);
    }

    public void updatePrefStatus() {
        this.mFaceDataPrefCategory.removeAll();
        List<FaceMetadata> enrolledFaceMetadatas = this.mFaceEnroll.getEnrolledFaceMetadatas(UserHandle.myUserId());
        for (FaceMetadata faceGearPreference : enrolledFaceMetadatas) {
            this.mFaceDataPrefCategory.addPreference(new FaceGearPreference(getPrefContext(), faceGearPreference, this.mRemovalCallback, this.mRenameConfirmListener));
        }
        boolean z = enrolledFaceMetadatas.size() >= 2;
        this.mAddFacePref.setSummary((CharSequence) z ? getString(R$string.nt_face_add_data_upper_limit) : null);
        this.mAddFacePref.setEnabled(!z);
    }

    private boolean isSkipSwipeEnabled() {
        return Settings.Secure.getIntForUser(getPrefContext().getContentResolver(), "face_unlock_dismisses_keyguard", getPrefContext().getResources().getBoolean(17891660) ? 1 : 0, -2) != 0;
    }

    private void setSkipSwipeEnabled(boolean z) {
        Settings.Secure.putIntForUser(getPrefContext().getContentResolver(), "face_unlock_dismisses_keyguard", z ? 1 : 0, -2);
    }

    private boolean isUnlockWithMaskEnabled() {
        return Settings.Secure.getIntForUser(getPrefContext().getContentResolver(), "nt_face_recognition_unlock_with_mask", 0, -2) != 0;
    }

    private void setUnlockWithMaskEnabled(boolean z) {
        Settings.Secure.putIntForUser(getPrefContext().getContentResolver(), "nt_face_recognition_unlock_with_mask", z ? 1 : 0, -2);
    }

    public int getPreferenceScreenResId() {
        return R$xml.nt_security_settings_face;
    }

    public String getLogTag() {
        return CustomFaceSettings.class.getName();
    }

    private void launchChooseOrConfirmLock() {
        Intent intent = new Intent();
        if (!new ChooseLockSettingsHelper.Builder(getActivity(), this).setRequestCode(4).setTitle(getString(R$string.security_settings_fingerprint_preference_title)).setRequestGatekeeperPasswordHandle(true).setUserId(UserHandle.myUserId()).setForegroundOnly(true).setReturnCredentials(true).show()) {
            intent.setClassName("com.android.settings", ChooseLockGeneric.class.getName());
            intent.putExtra("hide_insecure_options", true);
            intent.putExtra("request_gk_pw_handle", true);
            intent.putExtra("for_face", true);
            intent.putExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
            startActivityForResult(intent, 1);
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, CustomFaceSettings customFaceSettings, Lifecycle lifecycle) {
        return new ArrayList();
    }
}
