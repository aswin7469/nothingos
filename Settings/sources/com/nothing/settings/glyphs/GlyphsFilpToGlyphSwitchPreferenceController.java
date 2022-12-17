package com.nothing.settings.glyphs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.DialogCreatable;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class GlyphsFilpToGlyphSwitchPreferenceController extends TogglePreferenceController implements Preference.OnPreferenceClickListener, DialogCreatable {
    private static final int DIALOG_ID_BASE = 10;
    static final int DIALOG_MAGNIFICATION_MODE = 11;
    private static final String KEY_NT_USE_FILP_SWITCH = "nt_use_filp_switch";
    private static final String LED_EFFECT_GESTURES_FLIP_ENABLE = "led_effect_gestures_flip_ebable";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f262ON = 1;
    private static final String TAG = "FilpToGlyph";
    private DialogHelper mDialogHelper;
    private SwitchPreference preference;

    public interface DialogHelper extends DialogCreatable {
        void setDialogDelegate(DialogCreatable dialogCreatable);
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public int getDialogMetricsCategory(int i) {
        return i != 11 ? 0 : 1816;
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean onPreferenceClick(Preference preference2) {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsFilpToGlyphSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SwitchPreference switchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.preference = switchPreference;
        switchPreference.setOnPreferenceClickListener(this);
        this.preference.setChecked(isChecked());
    }

    public boolean isChecked() {
        return Settings.System.getInt(this.mContext.getContentResolver(), LED_EFFECT_GESTURES_FLIP_ENABLE, 0) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.System.putInt(this.mContext.getContentResolver(), LED_EFFECT_GESTURES_FLIP_ENABLE, z ? 1 : 0);
    }

    public void setDialogHelper(DialogHelper dialogHelper) {
        this.mDialogHelper = dialogHelper;
        dialogHelper.setDialogDelegate(this);
    }

    public Dialog onCreateDialog(int i) {
        return new AlertDialog.Builder(this.mContext).setTitle(R$string.nt_dialog_activate_glyph_title).setMessage(R$string.nt_dialog_activate_glyph_message).setNegativeButton(R$string.cancel, (DialogInterface.OnClickListener) null).setPositiveButton(R$string.nt_turn_on_now, (DialogInterface.OnClickListener) new C2006x8ee0065d(this)).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        this.preference.setChecked(true);
        setChecked(true);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_sound;
    }
}
