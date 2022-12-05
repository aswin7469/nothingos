package com.nothingos.keyguard.weather;

import com.android.systemui.statusbar.policy.CallbackController;
/* loaded from: classes2.dex */
public interface KeyguardWeatherController extends CallbackController<Callback> {

    /* loaded from: classes2.dex */
    public interface Callback {
        default void onWeatherDataChanged() {
        }

        default void onWeatherSwitchChanged(boolean z) {
        }
    }

    WeatherData getWeatherData();

    boolean isKeyguardWeatherOn();
}
