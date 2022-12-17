package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.accessibility.TextReadingResetController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextReadingPreferenceFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.accessibility_text_reading_options);
    private int mEntryPoint = 0;
    private FontWeightAdjustmentPreferenceController mFontWeightAdjustmentController;
    boolean mNeedResetSettings;
    List<TextReadingResetController.ResetStateListener> mResetStateListeners;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TextReadingPreferenceFragment";
    }

    public int getMetricsCategory() {
        return 1912;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mNeedResetSettings = false;
        this.mResetStateListeners = getResetStateListeners();
        if (bundle != null && bundle.getBoolean("need_reset_settings")) {
            this.mResetStateListeners.forEach(new TextReadingPreferenceFragment$$ExternalSyntheticLambda2());
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.accessibility_text_reading_options;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        updateEntryPoint();
        ArrayList arrayList = new ArrayList();
        FontSizeData fontSizeData = new FontSizeData(context);
        DisplaySizeData createDisplaySizeData = createDisplaySizeData(context);
        TextReadingPreviewController textReadingPreviewController = new TextReadingPreviewController(context, "preview", fontSizeData, createDisplaySizeData);
        textReadingPreviewController.setEntryPoint(this.mEntryPoint);
        arrayList.add(textReadingPreviewController);
        PreviewSizeSeekBarController previewSizeSeekBarController = new PreviewSizeSeekBarController(context, "font_size", fontSizeData);
        previewSizeSeekBarController.setInteractionListener(textReadingPreviewController);
        arrayList.add(previewSizeSeekBarController);
        PreviewSizeSeekBarController previewSizeSeekBarController2 = new PreviewSizeSeekBarController(context, "display_size", createDisplaySizeData);
        previewSizeSeekBarController2.setInteractionListener(textReadingPreviewController);
        arrayList.add(previewSizeSeekBarController2);
        FontWeightAdjustmentPreferenceController fontWeightAdjustmentPreferenceController = new FontWeightAdjustmentPreferenceController(context, "toggle_force_bold_text");
        this.mFontWeightAdjustmentController = fontWeightAdjustmentPreferenceController;
        fontWeightAdjustmentPreferenceController.setEntryPoint(this.mEntryPoint);
        arrayList.add(this.mFontWeightAdjustmentController);
        HighTextContrastPreferenceController highTextContrastPreferenceController = new HighTextContrastPreferenceController(context, "toggle_high_text_contrast_preference");
        highTextContrastPreferenceController.setEntryPoint(this.mEntryPoint);
        arrayList.add(highTextContrastPreferenceController);
        TextReadingResetController textReadingResetController = new TextReadingResetController(context, "reset", new TextReadingPreferenceFragment$$ExternalSyntheticLambda1(this));
        textReadingResetController.setEntryPoint(this.mEntryPoint);
        arrayList.add(textReadingResetController);
        return arrayList;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createPreferenceControllers$0(View view) {
        showDialog(1009);
    }

    public Dialog onCreateDialog(int i) {
        if (i == 1009) {
            return new AlertDialog.Builder(getPrefContext()).setTitle(R$string.accessibility_text_reading_confirm_dialog_title).setMessage(R$string.accessibility_text_reading_confirm_dialog_message).setPositiveButton(R$string.accessibility_text_reading_confirm_dialog_reset_button, (DialogInterface.OnClickListener) new TextReadingPreferenceFragment$$ExternalSyntheticLambda0(this)).setNegativeButton(R$string.cancel, (DialogInterface.OnClickListener) null).create();
        }
        throw new IllegalArgumentException("Unsupported dialogId " + i);
    }

    public int getDialogMetricsCategory(int i) {
        if (i == 1009) {
            return 1924;
        }
        return super.getDialogMetricsCategory(i);
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.mNeedResetSettings) {
            bundle.putBoolean("need_reset_settings", true);
        }
    }

    /* access modifiers changed from: package-private */
    public DisplaySizeData createDisplaySizeData(Context context) {
        return new DisplaySizeData(context);
    }

    private void updateEntryPoint() {
        Bundle arguments = getArguments();
        int i = 0;
        if (arguments == null || !arguments.containsKey("launched_from")) {
            Intent intent = getIntent();
            if (intent == null) {
                this.mEntryPoint = 0;
                return;
            }
            Set<String> categories = intent.getCategories();
            if (categories != null && categories.contains("com.android.settings.suggested.category.DISPLAY_SETTINGS")) {
                i = 2;
            }
            this.mEntryPoint = i;
            return;
        }
        this.mEntryPoint = arguments.getInt("launched_from", 0);
    }

    /* access modifiers changed from: private */
    public void onPositiveButtonClicked(DialogInterface dialogInterface, int i) {
        removeDialog(1009);
        if (this.mFontWeightAdjustmentController.isChecked()) {
            this.mNeedResetSettings = true;
            this.mFontWeightAdjustmentController.resetState();
        } else {
            this.mResetStateListeners.forEach(new TextReadingPreferenceFragment$$ExternalSyntheticLambda2());
        }
        Toast.makeText(getPrefContext(), R$string.accessibility_text_reading_reset_message, 0).show();
    }

    private List<TextReadingResetController.ResetStateListener> getResetStateListeners() {
        ArrayList arrayList = new ArrayList();
        getPreferenceControllers().forEach(new TextReadingPreferenceFragment$$ExternalSyntheticLambda3(arrayList));
        return (List) arrayList.stream().filter(new TextReadingPreferenceFragment$$ExternalSyntheticLambda4()).map(new TextReadingPreferenceFragment$$ExternalSyntheticLambda5()).collect(Collectors.toList());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getResetStateListeners$1(AbstractPreferenceController abstractPreferenceController) {
        return abstractPreferenceController instanceof TextReadingResetController.ResetStateListener;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ TextReadingResetController.ResetStateListener lambda$getResetStateListeners$2(AbstractPreferenceController abstractPreferenceController) {
        return (TextReadingResetController.ResetStateListener) abstractPreferenceController;
    }
}
