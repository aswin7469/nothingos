package com.nt.settings.glyphs;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nt.settings.glyphs.pick.GlyphsPickResultHelper;
import com.nt.settings.glyphs.ringtone.GlyphsRingtoneSettingsPreferenceFragment;
import com.nt.settings.glyphs.ringtone.GlyphsRingtonesSelectorPreferenceController;
import com.nt.settings.glyphs.ringtone.GlyphsRingtonesSoundModePreferenceController;
import com.nt.settings.utils.NtUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsContactRingtoneSettingsFragment extends GlyphsRingtoneSettingsPreferenceFragment {
    private String mContactName;
    private GlyphsRingtonesSelectorPreferenceController mController;
    private int mSoundOnly;
    private Uri mUriForDefaultItem;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nt.settings.glyphs.ringtone.GlyphsRingtoneSettingsPreferenceFragment, com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "ContactRingtoneSettings";
    }

    @Override // com.nt.settings.glyphs.ringtone.GlyphsRingtoneSettingsPreferenceFragment, com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1845;
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [boolean, int] */
    @Override // com.nt.settings.glyphs.ringtone.GlyphsRingtoneSettingsPreferenceFragment
    protected void onClickSetRingtone() {
        int ringtoneType = getRingtoneType();
        String title = Ringtone.getTitle(getActivity(), this.mController.getCurrentUri(), true, false);
        ?? isChecked = this.mSoundOnlySwitchPreference.isChecked();
        NtUtils.trackRingtoneChanged(getActivity(), ringtoneType, title, isChecked == true ? 1 : 0);
        if (ringtoneType == 2) {
            Settings.Global.putInt(getActivity().getContentResolver(), "led_notification_mode", isChecked);
        }
        GlyphsPickResultHelper.setResult(getActivity(), this.mController.getCurrentUri(), this.mSoundOnlySwitchPreference.isChecked() ? 1 : 0);
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        initParams(getIntent());
        super.onAttach(context);
    }

    private void initParams(Intent intent) {
        if (intent == null) {
            return;
        }
        this.mUriForDefaultItem = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
        this.mSoundOnly = intent.getIntExtra("key_sound_only", 0);
        this.mContactName = intent.getStringExtra("contact_name");
    }

    @Override // com.nt.settings.glyphs.ringtone.GlyphsRingtoneSettingsPreferenceFragment
    protected List<AbstractPreferenceController> getControllers() {
        ArrayList arrayList = new ArrayList();
        GlyphsRingtonesSelectorPreferenceController glyphsRingtonesSelectorPreferenceController = new GlyphsRingtonesSelectorPreferenceController(getActivity(), "key_glyphs_ringtone_selector", getRingtoneType(), this.mUriForDefaultItem, this.mSoundOnly, this.mContactName);
        this.mController = glyphsRingtonesSelectorPreferenceController;
        arrayList.add(glyphsRingtonesSelectorPreferenceController);
        arrayList.add(new GlyphsRingtonesSoundModePreferenceController(getActivity(), "key_glyphs_switch_sound_only", getRingtoneType(), this.mSoundOnly, new GlyphsRingtonesSoundModePreferenceController.OnSoundModeChangeListener() { // from class: com.nt.settings.glyphs.GlyphsContactRingtoneSettingsFragment.1
            @Override // com.nt.settings.glyphs.ringtone.GlyphsRingtonesSoundModePreferenceController.OnSoundModeChangeListener
            public void onSoundModeChange(boolean z) {
                if (((GlyphsRingtoneSettingsPreferenceFragment) GlyphsContactRingtoneSettingsFragment.this).mSoundOnlySwitchPreference != null) {
                    ((GlyphsRingtoneSettingsPreferenceFragment) GlyphsContactRingtoneSettingsFragment.this).mSoundOnlySwitchPreference.setChecked(z);
                }
                GlyphsContactRingtoneSettingsFragment.this.mController.setSoundOnly(z);
            }
        }));
        return arrayList;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        GlyphsRingtonesSelectorPreferenceController glyphsRingtonesSelectorPreferenceController = this.mController;
        if (glyphsRingtonesSelectorPreferenceController != null) {
            glyphsRingtonesSelectorPreferenceController.onDestroy();
        }
    }

    private int getRingtoneType() {
        return getIntent().getIntExtra("android.intent.extra.ringtone.TYPE", 1);
    }
}
