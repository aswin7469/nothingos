package com.nothing.settings.glyphs.ringtone;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.utils.NtUtils;
import java.util.ArrayList;
import java.util.List;

public class ContactRingtoneSettingsFragment extends AbsRingtoneSettingsPreferenceFragment {
    private String mContactName;
    private RingtonesSelectorPreferenceController mController;
    private String mPageTitle;
    private int mSoundOnly;
    private Uri mUriForDefaultItem;

    public String getLogTag() {
        return "ContactRingtoneSettings";
    }

    public int getMetricsCategory() {
        return 1845;
    }

    /* access modifiers changed from: protected */
    public void onClickSetRingtone() {
        int ringtoneType = getRingtoneType();
        int i = 1;
        String title = Ringtone.getTitle(getActivity(), this.mController.getCurrentUri(), true, false);
        boolean isChecked = this.mSoundOnlySwitchPreference.isChecked();
        FragmentActivity activity = getActivity();
        if (!isChecked) {
            i = 0;
        }
        NtUtils.trackRingtoneChanged(activity, ringtoneType, title, i);
        Log.d("ContactRingtoneSettingsFragment", "mPageTitle:" + this.mPageTitle);
        if (ringtoneType == 2 && TextUtils.equals(this.mPageTitle, getContext().getString(R$string.nt_glyphs_default_notifications_title))) {
            Settings.Global.putInt(getActivity().getContentResolver(), "led_notification_mode", isChecked);
        }
        ResultPickHelper.setResult(getActivity(), this.mController.getCurrentUri(), isChecked ? 1 : 0);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onAttach(Context context) {
        initParams(getIntent());
        super.onAttach(context);
        getActivity().setVolumeControlStream(getRingtoneType());
    }

    private void initParams(Intent intent) {
        if (intent != null) {
            this.mUriForDefaultItem = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
            this.mSoundOnly = intent.getIntExtra("key_sound_only", 0);
            this.mContactName = intent.getStringExtra("contact_name");
            this.mPageTitle = intent.getStringExtra("page_title");
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> getControllers() {
        ArrayList arrayList = new ArrayList();
        RingtonesSelectorPreferenceController ringtonesSelectorPreferenceController = new RingtonesSelectorPreferenceController(getActivity(), "key_glyphs_ringtone_selector", getRingtoneType(), this.mUriForDefaultItem, this.mSoundOnly, this.mContactName);
        this.mController = ringtonesSelectorPreferenceController;
        arrayList.add(ringtonesSelectorPreferenceController);
        arrayList.add(new RingtonesSoundModePreferenceController(getActivity(), "key_glyphs_switch_sound_only", getRingtoneType(), this.mSoundOnly, new ContactRingtoneSettingsFragment$$ExternalSyntheticLambda0(this)));
        return arrayList;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getControllers$0(boolean z) {
        SwitchPreference switchPreference = this.mSoundOnlySwitchPreference;
        if (switchPreference != null) {
            switchPreference.setChecked(z);
        }
        this.mController.setSoundOnly(z);
    }

    public void onDestroy() {
        super.onDestroy();
        RingtonesSelectorPreferenceController ringtonesSelectorPreferenceController = this.mController;
        if (ringtonesSelectorPreferenceController != null) {
            ringtonesSelectorPreferenceController.onDestroy();
        }
    }

    private int getRingtoneType() {
        return getIntent().getIntExtra("android.intent.extra.ringtone.TYPE", 1);
    }
}
