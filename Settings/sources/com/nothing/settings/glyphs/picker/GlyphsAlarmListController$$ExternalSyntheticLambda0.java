package com.nothing.settings.glyphs.picker;

import androidx.preference.Preference;
import com.nothing.settings.glyphs.preference.PrimaryCheckBoxPreference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class GlyphsAlarmListController$$ExternalSyntheticLambda0 implements PrimaryCheckBoxPreference.OnSelectedListener {
    public final /* synthetic */ GlyphsAlarmListController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ GlyphsAlarmListController$$ExternalSyntheticLambda0(GlyphsAlarmListController glyphsAlarmListController, int i) {
        this.f$0 = glyphsAlarmListController;
        this.f$1 = i;
    }

    public final void onCheckChange(Preference preference, boolean z) {
        this.f$0.lambda$updateDataInner$0(this.f$1, preference, z);
    }
}
