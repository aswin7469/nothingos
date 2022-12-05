package com.android.settings.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.core.PreferenceXmlParserUtils;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.RadioButtonPreference;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public abstract class RadioButtonPickerFragment extends InstrumentedPreferenceFragment implements RadioButtonPreference.OnClickListener {
    static final String EXTRA_FOR_WORK = "for_work";
    boolean mAppendStaticPreferences = false;
    private final Map<String, CandidateInfo> mCandidates = new ArrayMap();
    private int mIllustrationId;
    private int mIllustrationPreviewId;
    protected int mUserId;
    protected UserManager mUserManager;
    private VideoPreference mVideoPreference;

    protected void addStaticPreferences(PreferenceScreen preferenceScreen) {
    }

    public void bindPreferenceExtra(RadioButtonPreference radioButtonPreference, String str, CandidateInfo candidateInfo, String str2, String str3) {
    }

    protected abstract List<? extends CandidateInfo> getCandidates();

    protected abstract String getDefaultKey();

    @Override // com.android.settings.core.InstrumentedPreferenceFragment
    protected abstract int getPreferenceScreenResId();

    protected int getRadioButtonPreferenceCustomLayoutResId() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSystemDefaultKey() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSelectionPerformed(boolean z) {
    }

    protected abstract boolean setDefaultKey(String str);

    protected boolean shouldShowItemNone() {
        return false;
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        int myUserId;
        super.onAttach(context);
        this.mUserManager = (UserManager) context.getSystemService("user");
        Bundle arguments = getArguments();
        boolean z = arguments != null ? arguments.getBoolean(EXTRA_FOR_WORK) : false;
        UserHandle managedProfile = Utils.getManagedProfile(this.mUserManager);
        if (z && managedProfile != null) {
            myUserId = managedProfile.getIdentifier();
        } else {
            myUserId = UserHandle.myUserId();
        }
        this.mUserId = myUserId;
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        try {
            this.mAppendStaticPreferences = PreferenceXmlParserUtils.extractMetadata(getContext(), getPreferenceScreenResId(), 1025).get(0).getBoolean("staticPreferenceLocation");
        } catch (IOException e) {
            Log.e("RadioButtonPckrFrgmt", "Error trying to open xml file", e);
        } catch (XmlPullParserException e2) {
            Log.e("RadioButtonPckrFrgmt", "Error parsing xml", e2);
        }
        updateCandidates();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        setHasOptionsMenu(true);
        return onCreateView;
    }

    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        onRadioButtonConfirmed(radioButtonPreference.getKey());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CandidateInfo getCandidate(String str) {
        return this.mCandidates.get(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onRadioButtonConfirmed(String str) {
        boolean defaultKey = setDefaultKey(str);
        if (defaultKey) {
            updateCheckedState(str);
        }
        onSelectionPerformed(defaultKey);
    }

    public void updateCandidates() {
        this.mCandidates.clear();
        List<? extends CandidateInfo> candidates = getCandidates();
        if (candidates != null) {
            for (CandidateInfo candidateInfo : candidates) {
                this.mCandidates.put(candidateInfo.getKey(), candidateInfo);
            }
        }
        String defaultKey = getDefaultKey();
        String systemDefaultKey = getSystemDefaultKey();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        if (this.mIllustrationId != 0) {
            addIllustration(preferenceScreen);
        }
        if (!this.mAppendStaticPreferences) {
            addStaticPreferences(preferenceScreen);
        }
        int radioButtonPreferenceCustomLayoutResId = getRadioButtonPreferenceCustomLayoutResId();
        if (shouldShowItemNone()) {
            RadioButtonPreference radioButtonPreference = new RadioButtonPreference(getPrefContext());
            if (radioButtonPreferenceCustomLayoutResId > 0) {
                radioButtonPreference.setLayoutResource(radioButtonPreferenceCustomLayoutResId);
            }
            radioButtonPreference.setIcon(R.drawable.ic_remove_circle);
            radioButtonPreference.setTitle(R.string.app_list_preference_none);
            radioButtonPreference.setChecked(TextUtils.isEmpty(defaultKey));
            radioButtonPreference.setOnClickListener(this);
            preferenceScreen.addPreference(radioButtonPreference);
        }
        if (candidates != null) {
            for (CandidateInfo candidateInfo2 : candidates) {
                RadioButtonPreference radioButtonPreference2 = new RadioButtonPreference(getPrefContext());
                if (radioButtonPreferenceCustomLayoutResId > 0) {
                    radioButtonPreference2.setLayoutResource(radioButtonPreferenceCustomLayoutResId);
                }
                bindPreference(radioButtonPreference2, candidateInfo2.getKey(), candidateInfo2, defaultKey);
                bindPreferenceExtra(radioButtonPreference2, candidateInfo2.getKey(), candidateInfo2, defaultKey, systemDefaultKey);
                preferenceScreen.addPreference(radioButtonPreference2);
            }
        }
        mayCheckOnlyRadioButton();
        if (this.mAppendStaticPreferences) {
            addStaticPreferences(preferenceScreen);
        }
    }

    public RadioButtonPreference bindPreference(RadioButtonPreference radioButtonPreference, String str, CandidateInfo candidateInfo, String str2) {
        radioButtonPreference.setTitle(candidateInfo.loadLabel());
        radioButtonPreference.setIcon(Utils.getSafeIcon(candidateInfo.loadIcon()));
        radioButtonPreference.setKey(str);
        if (TextUtils.equals(str2, str)) {
            radioButtonPreference.setChecked(true);
        }
        radioButtonPreference.setEnabled(candidateInfo.enabled);
        radioButtonPreference.setOnClickListener(this);
        return radioButtonPreference;
    }

    public void updateCheckedState(String str) {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            int preferenceCount = preferenceScreen.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference = preferenceScreen.getPreference(i);
                if (preference instanceof RadioButtonPreference) {
                    RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preference;
                    if (radioButtonPreference.isChecked() != TextUtils.equals(preference.getKey(), str)) {
                        radioButtonPreference.setChecked(TextUtils.equals(preference.getKey(), str));
                    }
                }
            }
        }
    }

    public void mayCheckOnlyRadioButton() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen == null || preferenceScreen.getPreferenceCount() != 1) {
            return;
        }
        Preference preference = preferenceScreen.getPreference(0);
        if (!(preference instanceof RadioButtonPreference)) {
            return;
        }
        ((RadioButtonPreference) preference).setChecked(true);
    }

    private void addIllustration(PreferenceScreen preferenceScreen) {
        VideoPreference videoPreference = new VideoPreference(getContext());
        this.mVideoPreference = videoPreference;
        videoPreference.setVideo(this.mIllustrationId, this.mIllustrationPreviewId);
        preferenceScreen.addPreference(this.mVideoPreference);
    }
}
