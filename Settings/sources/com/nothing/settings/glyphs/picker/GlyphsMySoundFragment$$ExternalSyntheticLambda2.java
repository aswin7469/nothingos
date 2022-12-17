package com.nothing.settings.glyphs.picker;

import android.content.DialogInterface;
import android.net.Uri;
import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class GlyphsMySoundFragment$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ GlyphsMySoundFragment f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ Preference f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ GlyphsMySoundFragment$$ExternalSyntheticLambda2(GlyphsMySoundFragment glyphsMySoundFragment, Uri uri, Preference preference, String str) {
        this.f$0 = glyphsMySoundFragment;
        this.f$1 = uri;
        this.f$2 = preference;
        this.f$3 = str;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onCreateDialog$3(this.f$1, this.f$2, this.f$3, dialogInterface, i);
    }
}
