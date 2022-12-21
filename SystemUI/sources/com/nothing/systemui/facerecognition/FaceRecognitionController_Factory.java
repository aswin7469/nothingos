package com.nothing.systemui.facerecognition;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class FaceRecognitionController_Factory implements Factory<FaceRecognitionController> {
    private final Provider<Context> contextProvider;

    public FaceRecognitionController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public FaceRecognitionController get() {
        return newInstance(this.contextProvider.get());
    }

    public static FaceRecognitionController_Factory create(Provider<Context> provider) {
        return new FaceRecognitionController_Factory(provider);
    }

    public static FaceRecognitionController newInstance(Context context) {
        return new FaceRecognitionController(context);
    }
}
