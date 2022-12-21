package com.nothing.systemui.keyguard.weather;

import com.android.systemui.statusbar.policy.CallbackController;

public interface KeyguardWeatherController extends CallbackController<Callback> {

    public interface Callback {
        void onWeatherDataChanged() {
        }

        void onWeatherSwitchChanged(boolean z) {
        }
    }

    WeatherData getWeatherData();

    boolean isKeyguardCelsiusUnitOn();

    boolean isKeyguardWeatherOn();
}
