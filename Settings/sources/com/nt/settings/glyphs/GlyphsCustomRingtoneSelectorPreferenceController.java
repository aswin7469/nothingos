package com.nt.settings.glyphs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class GlyphsCustomRingtoneSelectorPreferenceController extends BasePreferenceController {
    private static final String KEY_RINGTONE = "key_glyphs_custom_ringtone_selector";
    private Context mContext;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY_RINGTONE;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsCustomRingtoneSelectorPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ArrayList arrayList = new ArrayList();
        Intent intent = ((Activity) this.mContext).getIntent();
        String stringExtra = intent.getStringExtra("android.intent.extra.ringtone.EXISTING_URI");
        intent.getStringExtra("contact_id");
        arrayList.add(new GlyphsRingtoneSelectorPreference.Ringtone(stringExtra, intent.getStringExtra("ringtone_title")));
        ((GlyphsRingtoneSelectorPreference) preferenceScreen.findPreference(getPreferenceKey())).setData(arrayList, 0);
    }
}
