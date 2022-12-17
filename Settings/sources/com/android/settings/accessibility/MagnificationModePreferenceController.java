package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.DialogCreatable;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.accessibility.ItemInfoArrayAdapter;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.utils.AnnotationSpan;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;
import java.util.ArrayList;
import java.util.List;

public class MagnificationModePreferenceController extends BasePreferenceController implements DialogCreatable, LifecycleObserver, OnCreate, OnResume, OnSaveInstanceState {
    private static final int DIALOG_ID_BASE = 10;
    static final int DIALOG_MAGNIFICATION_MODE = 11;
    static final int DIALOG_MAGNIFICATION_TRIPLE_TAP_WARNING = 12;
    static final String EXTRA_MODE = "mode";
    static final String PREF_KEY = "screen_magnification_mode";
    private static final String TAG = "MagnificationModePreferenceController";
    private DialogHelper mDialogHelper;
    private ShortcutPreference mLinkPreference;
    ListView mMagnificationModesListView;
    private int mModeCache = 0;
    private final List<MagnificationModeInfo> mModeInfos = new ArrayList();
    private Preference mModePreference;

    interface DialogHelper extends DialogCreatable {
        void setDialogDelegate(DialogCreatable dialogCreatable);

        void showDialog(int i);
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public int getDialogMetricsCategory(int i) {
        if (i != 11) {
            return i != 12 ? 0 : 1923;
        }
        return 1816;
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

    public MagnificationModePreferenceController(Context context, String str) {
        super(context, str);
        initModeInfos();
    }

    private void initModeInfos() {
        this.mModeInfos.add(new MagnificationModeInfo(this.mContext.getText(R$string.accessibility_magnification_mode_dialog_option_full_screen), (CharSequence) null, R$drawable.ic_illustration_fullscreen, 1));
        this.mModeInfos.add(new MagnificationModeInfo(this.mContext.getText(R$string.accessibility_magnification_mode_dialog_option_window), (CharSequence) null, R$drawable.ic_illustration_window, 2));
        this.mModeInfos.add(new MagnificationModeInfo(this.mContext.getText(R$string.accessibility_magnification_mode_dialog_option_switch), this.mContext.getText(R$string.accessibility_magnification_area_settings_mode_switch_summary), R$drawable.ic_illustration_switch, 3));
    }

    public CharSequence getSummary() {
        return MagnificationCapabilities.getSummary(this.mContext, MagnificationCapabilities.getCapabilities(this.mContext));
    }

    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            this.mModeCache = bundle.getInt(EXTRA_MODE, 0);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mModePreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mLinkPreference = (ShortcutPreference) preferenceScreen.findPreference("shortcut_preference");
        this.mModePreference.setOnPreferenceClickListener(new MagnificationModePreferenceController$$ExternalSyntheticLambda4(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayPreference$0(Preference preference) {
        this.mModeCache = MagnificationCapabilities.getCapabilities(this.mContext);
        this.mDialogHelper.showDialog(11);
        return true;
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(EXTRA_MODE, this.mModeCache);
    }

    public void setDialogHelper(DialogHelper dialogHelper) {
        this.mDialogHelper = dialogHelper;
        dialogHelper.setDialogDelegate(this);
    }

    public Dialog onCreateDialog(int i) {
        if (i == 11) {
            return createMagnificationModeDialog();
        }
        if (i != 12) {
            return null;
        }
        return createMagnificationTripleTapWarningDialog();
    }

    private Dialog createMagnificationModeDialog() {
        this.mMagnificationModesListView = AccessibilityDialogUtils.createSingleChoiceListView(this.mContext, this.mModeInfos, new MagnificationModePreferenceController$$ExternalSyntheticLambda2(this));
        this.mMagnificationModesListView.addHeaderView(LayoutInflater.from(this.mContext).inflate(R$layout.accessibility_magnification_mode_header, this.mMagnificationModesListView, false), (Object) null, false);
        this.mMagnificationModesListView.setItemChecked(computeSelectionIndex(), true);
        return AccessibilityDialogUtils.createCustomDialog(this.mContext, this.mContext.getString(R$string.accessibility_magnification_mode_dialog_title), this.mMagnificationModesListView, this.mContext.getString(R$string.save), new MagnificationModePreferenceController$$ExternalSyntheticLambda3(this), this.mContext.getString(R$string.cancel), (DialogInterface.OnClickListener) null);
    }

    /* access modifiers changed from: package-private */
    public void onMagnificationModeDialogPositiveButtonClicked(DialogInterface dialogInterface, int i) {
        int checkedItemPosition = this.mMagnificationModesListView.getCheckedItemPosition();
        if (checkedItemPosition == -1) {
            Log.w(TAG, "invalid index");
            return;
        }
        this.mModeCache = ((MagnificationModeInfo) this.mMagnificationModesListView.getItemAtPosition(checkedItemPosition)).mMagnificationMode;
        if (!isTripleTapEnabled(this.mContext) || this.mModeCache == 1) {
            updateCapabilitiesAndSummary(this.mModeCache);
        } else {
            this.mDialogHelper.showDialog(12);
        }
    }

    private void updateCapabilitiesAndSummary(int i) {
        this.mModeCache = i;
        MagnificationCapabilities.setCapabilities(this.mContext, i);
        this.mModePreference.setSummary((CharSequence) MagnificationCapabilities.getSummary(this.mContext, this.mModeCache));
    }

    /* access modifiers changed from: private */
    public void onMagnificationModeSelected(AdapterView<?> adapterView, View view, int i, long j) {
        int i2 = ((MagnificationModeInfo) this.mMagnificationModesListView.getItemAtPosition(i)).mMagnificationMode;
        if (i2 != this.mModeCache) {
            this.mModeCache = i2;
        }
    }

    private int computeSelectionIndex() {
        int size = this.mModeInfos.size();
        for (int i = 0; i < size; i++) {
            if (this.mModeInfos.get(i).mMagnificationMode == this.mModeCache) {
                return i + this.mMagnificationModesListView.getHeaderViewsCount();
            }
        }
        Log.w(TAG, "computeSelectionIndex failed");
        return 0;
    }

    static boolean isTripleTapEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_display_magnification_enabled", 0) == 1;
    }

    private Dialog createMagnificationTripleTapWarningDialog() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.magnification_triple_tap_warning_dialog, (ViewGroup) null);
        View view = inflate;
        Dialog createCustomDialog = AccessibilityDialogUtils.createCustomDialog(this.mContext, this.mContext.getString(R$string.accessibility_magnification_triple_tap_warning_title), view, this.mContext.getString(R$string.accessibility_magnification_triple_tap_warning_positive_button), new MagnificationModePreferenceController$$ExternalSyntheticLambda0(this), this.mContext.getString(R$string.accessibility_magnification_triple_tap_warning_negative_button), new MagnificationModePreferenceController$$ExternalSyntheticLambda1(this));
        updateLinkInTripleTapWarningDialog(createCustomDialog, inflate);
        return createCustomDialog;
    }

    private void updateLinkInTripleTapWarningDialog(Dialog dialog, View view) {
        TextView textView = (TextView) view.findViewById(R$id.message);
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo("link", new MagnificationModePreferenceController$$ExternalSyntheticLambda5(this, dialog));
        CharSequence linkify = AnnotationSpan.linkify(this.mContext.getText(R$string.accessibility_magnification_triple_tap_warning_message), linkInfo);
        if (textView != null) {
            textView.setText(linkify);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        dialog.setContentView(view);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLinkInTripleTapWarningDialog$1(Dialog dialog, View view) {
        updateCapabilitiesAndSummary(this.mModeCache);
        this.mLinkPreference.performClick();
        dialog.dismiss();
    }

    /* access modifiers changed from: package-private */
    public void onMagnificationTripleTapWarningDialogNegativeButtonClicked(DialogInterface dialogInterface, int i) {
        this.mModeCache = MagnificationCapabilities.getCapabilities(this.mContext);
        this.mDialogHelper.showDialog(11);
    }

    /* access modifiers changed from: package-private */
    public void onMagnificationTripleTapWarningDialogPositiveButtonClicked(DialogInterface dialogInterface, int i) {
        updateCapabilitiesAndSummary(this.mModeCache);
    }

    public void onResume() {
        updateState(this.mModePreference);
    }

    static class MagnificationModeInfo extends ItemInfoArrayAdapter.ItemInfo {
        public final int mMagnificationMode;

        MagnificationModeInfo(CharSequence charSequence, CharSequence charSequence2, int i, int i2) {
            super(charSequence, charSequence2, i);
            this.mMagnificationMode = i2;
        }
    }
}
