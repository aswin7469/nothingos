package com.nothing.settings.glyphs.picker;

import android.net.Uri;
import androidx.preference.Preference;
import com.nothing.settings.glyphs.picker.GlyphsMySoundFragment;

/* renamed from: com.nothing.settings.glyphs.picker.GlyphsMySoundFragment$UpdateDataTask$CheckBoxDeleteSelectedCallback$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C2030xc616c732 implements Runnable {
    public final /* synthetic */ GlyphsMySoundFragment.UpdateDataTask.CheckBoxDeleteSelectedCallback f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ Uri f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ String f$4;

    public /* synthetic */ C2030xc616c732(GlyphsMySoundFragment.UpdateDataTask.CheckBoxDeleteSelectedCallback checkBoxDeleteSelectedCallback, Preference preference, Uri uri, String str, String str2) {
        this.f$0 = checkBoxDeleteSelectedCallback;
        this.f$1 = preference;
        this.f$2 = uri;
        this.f$3 = str;
        this.f$4 = str2;
    }

    public final void run() {
        this.f$0.lambda$onClickDelete$0(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
