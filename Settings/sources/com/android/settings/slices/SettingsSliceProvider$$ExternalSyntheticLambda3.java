package com.android.settings.slices;

import android.net.Uri;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SettingsSliceProvider$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SettingsSliceProvider f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ SettingsSliceProvider$$ExternalSyntheticLambda3(SettingsSliceProvider settingsSliceProvider, Uri uri) {
        this.f$0 = settingsSliceProvider;
        this.f$1 = uri;
    }

    public final void run() {
        this.f$0.lambda$onSliceUnpinned$1(this.f$1);
    }
}
