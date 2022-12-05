package com.nt.settings.face;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.constraintlayout.widget.R$styleable;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.password.ChooseLockGeneric;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nt.facerecognition.manager.FaceMetadata;
import com.nt.facerecognition.manager.NtFaceRecognitionManager;
import com.nt.settings.widget.NtFaceGearPreference;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class NtFaceSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_security_settings_face) { // from class: com.nt.settings.face.NtFaceSettings.3
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return NtFaceSettings.buildPreferenceControllers(context, null, null);
        }
    };
    private Preference mAddFacePref;
    private boolean mAddingFace;
    private boolean mConfirmingCredentials;
    private PreferenceCategory mFaceDataPrefCategory;
    private boolean mFromFaceCreateActivity;
    private NtIFaceRecognition mNtIFaceRecognition;
    private SwitchPreference mSkipSwipePref;
    private SwitchPreference mUnlockWithMaskPref;
    private final int ADD_FACE_REQUEST = R$styleable.Constraint_layout_goneMarginRight;
    private final NtFaceRecognitionManager.RemovalCallback mRemovalCallback = new NtFaceRecognitionManager.RemovalCallback() { // from class: com.nt.settings.face.NtFaceSettings.1
        public void onRemovalError(FaceMetadata faceMetadata, int i, CharSequence charSequence) {
            super.onRemovalError(faceMetadata, i, charSequence);
        }

        public void onRemovalSucceeded(FaceMetadata faceMetadata, int i) {
            super.onRemovalSucceeded(faceMetadata, i);
            NtFaceSettings.this.updatePrefStatus();
            int faceMetadatasCount = NtFaceSettings.this.mNtIFaceRecognition.getFaceMetadatasCount();
            NtFaceUtil.setFaceDataCount(NtFaceSettings.this.getPrefContext(), faceMetadatasCount);
            if (faceMetadatasCount <= 0) {
                NtFaceSettings.this.finish();
            }
        }
    };
    private final DialogInterface.OnClickListener mRenameConfirmListener = new DialogInterface.OnClickListener() { // from class: com.nt.settings.face.NtFaceSettings.2
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            NtFaceSettings.this.updatePrefStatus();
        }
    };

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1511;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
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

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.mConfirmingCredentials || this.mAddingFace) {
            return;
        }
        getActivity().finish();
    }

    private void initValues() {
        this.mNtIFaceRecognition = new NtFaceRecognitionImpl(getPrefContext(), new Handler());
    }

    private void initPreference() {
        this.mFaceDataPrefCategory = (PreferenceCategory) findPreference("face_datas");
        this.mAddFacePref = findPreference("add_face");
        SwitchPreference switchPreference = (SwitchPreference) findPreference("skip_swipe");
        this.mSkipSwipePref = switchPreference;
        switchPreference.setChecked(isSkipSwipeEnabled());
        SwitchPreference switchPreference2 = (SwitchPreference) findPreference("unlock_with_mask");
        this.mUnlockWithMaskPref = switchPreference2;
        switchPreference2.setChecked(isUnlockWithMaskEnabled());
    }

    private void startBootFaceRecognitionService(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.nt.facerecognition", "com.nt.facerecognition.server.BootFaceRecognitionService"));
        intent.putExtra("start_app", "com.android.settings");
        context.startForegroundService(intent);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4 || i == 1) {
            if (i2 == 1 || i2 == -1) {
                this.mConfirmingCredentials = false;
            } else {
                finish();
            }
        } else if (i != 101) {
        } else {
            this.mAddingFace = false;
            if (i2 == 1) {
                finish();
            } else if (i2 != 2) {
            } else {
                updatePrefStatus();
            }
        }
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == this.mAddFacePref) {
            Intent intent = new Intent(getPrefContext(), NtCreateFaceActivity.class);
            intent.putExtra("from_face_settings", true);
            this.mAddingFace = true;
            startActivityForResult(intent, R$styleable.Constraint_layout_goneMarginRight);
        } else {
            SwitchPreference switchPreference = this.mSkipSwipePref;
            if (preference == switchPreference) {
                setSkipSwipeEnabled(switchPreference.isChecked());
            } else {
                SwitchPreference switchPreference2 = this.mUnlockWithMaskPref;
                if (preference == switchPreference2) {
                    setUnlockWithMaskEnabled(switchPreference2.isChecked());
                }
            }
        }
        return super.onPreferenceTreeClick(preference);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePrefStatus() {
        this.mFaceDataPrefCategory.removeAll();
        List<FaceMetadata> enrolledFaceMetadatas = this.mNtIFaceRecognition.getEnrolledFaceMetadatas(UserHandle.myUserId());
        for (FaceMetadata faceMetadata : enrolledFaceMetadatas) {
            this.mFaceDataPrefCategory.addPreference(new NtFaceGearPreference(getPrefContext(), faceMetadata, this.mRemovalCallback, this.mRenameConfirmListener));
        }
        boolean z = enrolledFaceMetadatas.size() >= 2;
        this.mAddFacePref.setSummary(z ? getString(R.string.nt_face_add_data_upper_limit) : null);
        this.mAddFacePref.setEnabled(!z);
    }

    private boolean isSkipSwipeEnabled() {
        return Settings.Secure.getIntForUser(getPrefContext().getContentResolver(), "face_unlock_dismisses_keyguard", getPrefContext().getResources().getBoolean(17891556) ? 1 : 0, -2) != 0;
    }

    private void setSkipSwipeEnabled(boolean z) {
        Settings.Secure.putIntForUser(getPrefContext().getContentResolver(), "face_unlock_dismisses_keyguard", z ? 1 : 0, -2);
    }

    private boolean isUnlockWithMaskEnabled() {
        return Settings.Secure.getIntForUser(getPrefContext().getContentResolver(), "nt_face_recognition_unlock_with_mask", 1, -2) != 0;
    }

    private void setUnlockWithMaskEnabled(boolean z) {
        Settings.Secure.putIntForUser(getPrefContext().getContentResolver(), "nt_face_recognition_unlock_with_mask", z ? 1 : 0, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_security_settings_face;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return NtFaceSettings.class.getName();
    }

    private void launchChooseOrConfirmLock() {
        Intent intent = new Intent();
        if (!new ChooseLockSettingsHelper.Builder(getActivity(), this).setRequestCode(4).setTitle(getString(R.string.security_settings_fingerprint_preference_title)).setRequestGatekeeperPasswordHandle(true).setUserId(UserHandle.myUserId()).setForegroundOnly(true).setReturnCredentials(true).show()) {
            intent.setClassName("com.android.settings", ChooseLockGeneric.class.getName());
            intent.putExtra("hide_insecure_options", true);
            intent.putExtra("request_gk_pw_handle", true);
            intent.putExtra("for_face", true);
            intent.putExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
            startActivityForResult(intent, 1);
        }
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, NtFaceSettings ntFaceSettings, Lifecycle lifecycle) {
        return new ArrayList();
    }
}
