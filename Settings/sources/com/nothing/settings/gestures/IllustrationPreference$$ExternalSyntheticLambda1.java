package com.nothing.settings.gestures;

import android.net.Uri;
import android.util.Log;
import com.airbnb.lottie.LottieListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class IllustrationPreference$$ExternalSyntheticLambda1 implements LottieListener {
    public final /* synthetic */ Uri f$0;

    public /* synthetic */ IllustrationPreference$$ExternalSyntheticLambda1(Uri uri) {
        this.f$0 = uri;
    }

    public final void onResult(Object obj) {
        Log.w("IllustrationPreference", "Invalid illustration image uri: " + this.f$0, (Throwable) obj);
    }
}
