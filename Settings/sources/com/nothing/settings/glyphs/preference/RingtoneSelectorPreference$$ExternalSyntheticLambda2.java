package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.net.Uri;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RingtoneSelectorPreference$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ RingtoneSelectorPreference f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ Uri f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ RingtoneSelectorPreference$$ExternalSyntheticLambda2(RingtoneSelectorPreference ringtoneSelectorPreference, Context context, Uri uri, String str) {
        this.f$0 = ringtoneSelectorPreference;
        this.f$1 = context;
        this.f$2 = uri;
        this.f$3 = str;
    }

    public final void run() {
        this.f$0.lambda$playRingTone$3(this.f$1, this.f$2, this.f$3);
    }
}
