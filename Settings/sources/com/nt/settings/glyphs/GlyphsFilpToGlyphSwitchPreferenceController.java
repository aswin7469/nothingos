package com.nt.settings.glyphs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.DialogCreatable;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class GlyphsFilpToGlyphSwitchPreferenceController extends TogglePreferenceController implements Preference.OnPreferenceClickListener, DialogCreatable {
    private static final int DIALOG_ID_BASE = 10;
    static final int DIALOG_MAGNIFICATION_MODE = 11;
    private static final String KEY_NT_USE_FILP_SWITCH = "nt_use_filp_switch";
    private static final int OFF = 0;
    private static final int ON = 1;
    private static final String TAG = "FilpToGlyphSwitch";
    private DialogHelper mDialogHelper;
    private SwitchPreference preference;

    /* loaded from: classes2.dex */
    interface DialogHelper extends DialogCreatable {
        void setDialogDelegate(DialogCreatable dialogCreatable);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        return i != 11 ? 0 : 1816;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsFilpToGlyphSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.preference = switchPreference;
        switchPreference.setOnPreferenceClickListener(this);
        this.preference.setChecked(isChecked());
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return Settings.System.getInt(this.mContext.getContentResolver(), "led_effect_gestures_flip_ebable", 0) == 1;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        return Settings.System.putInt(this.mContext.getContentResolver(), "led_effect_gestures_flip_ebable", z ? 1 : 0);
    }

    public void setDialogHelper(DialogHelper dialogHelper) {
        Log.d(TAG, "setDialogHelper");
        this.mDialogHelper = dialogHelper;
        dialogHelper.setDialogDelegate(this);
    }

    @Override // com.android.settings.DialogCreatable
    public Dialog onCreateDialog(int i) {
        return new AlertDialog.Builder(this.mContext).setTitle(R.string.nt_dialog_activate_glyph_title).setMessage(R.string.nt_dialog_activate_glyph_message).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.nt_turn_on_now, new DialogInterface.OnClickListener() { // from class: com.nt.settings.glyphs.GlyphsFilpToGlyphSwitchPreferenceController.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                GlyphsFilpToGlyphSwitchPreferenceController.this.preference.setChecked(true);
                GlyphsFilpToGlyphSwitchPreferenceController.this.setChecked(true);
            }
        }).create();
    }
}
