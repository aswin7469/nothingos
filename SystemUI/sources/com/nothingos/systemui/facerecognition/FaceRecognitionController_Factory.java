package com.nothingos.systemui.facerecognition;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class FaceRecognitionController_Factory implements Factory<FaceRecognitionController> {
    private final Provider<Context> contextProvider;

    public FaceRecognitionController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public FaceRecognitionController mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static FaceRecognitionController_Factory create(Provider<Context> provider) {
        return new FaceRecognitionController_Factory(provider);
    }

    public static FaceRecognitionController newInstance(Context context) {
        return new FaceRecognitionController(context);
    }
}
