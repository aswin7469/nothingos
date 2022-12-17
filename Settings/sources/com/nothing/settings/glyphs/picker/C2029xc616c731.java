package com.nothing.settings.glyphs.picker;

import androidx.preference.Preference;
import com.nothing.settings.glyphs.picker.GlyphsMySoundFragment;

/* renamed from: com.nothing.settings.glyphs.picker.GlyphsMySoundFragment$UpdateDataTask$CheckBoxDeleteSelectedCallback$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C2029xc616c731 implements Runnable {
    public final /* synthetic */ GlyphsMySoundFragment.UpdateDataTask.CheckBoxDeleteSelectedCallback f$0;
    public final /* synthetic */ Preference f$1;

    public /* synthetic */ C2029xc616c731(GlyphsMySoundFragment.UpdateDataTask.CheckBoxDeleteSelectedCallback checkBoxDeleteSelectedCallback, Preference preference) {
        this.f$0 = checkBoxDeleteSelectedCallback;
        this.f$1 = preference;
    }

    public final void run() {
        this.f$0.lambda$onClickDelete$1(this.f$1);
    }
}
