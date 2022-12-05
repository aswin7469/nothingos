package com.nothingos.keyguard.weather;

import com.nothingos.keyguard.weather.KeyguardWeatherController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class KeyguardWeatherControllerImpl$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ KeyguardWeatherControllerImpl$$ExternalSyntheticLambda3 INSTANCE = new KeyguardWeatherControllerImpl$$ExternalSyntheticLambda3();

    private /* synthetic */ KeyguardWeatherControllerImpl$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((KeyguardWeatherController.Callback) obj).onWeatherDataChanged();
    }
}
