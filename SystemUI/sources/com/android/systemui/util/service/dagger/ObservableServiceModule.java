package com.android.systemui.util.service.dagger;

import android.content.res.Resources;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Main;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module(subcomponents = {PackageObserverComponent.class})
public class ObservableServiceModule {
    public static final String BASE_RECONNECT_DELAY_MS = "base_reconnect_attempts";
    public static final String MAX_RECONNECT_ATTEMPTS = "max_reconnect_attempts";
    public static final String MIN_CONNECTION_DURATION_MS = "min_connection_duration_ms";
    public static final String OBSERVER = "observer";
    public static final String SERVICE_CONNECTION = "service_connection";

    @Provides
    @Named("max_reconnect_attempts")
    static int providesMaxReconnectAttempts(@Main Resources resources) {
        return resources.getInteger(C1893R.integer.config_communalSourceMaxReconnectAttempts);
    }

    @Provides
    @Named("base_reconnect_attempts")
    static int provideBaseReconnectDelayMs(@Main Resources resources) {
        return resources.getInteger(C1893R.integer.config_communalSourceReconnectBaseDelay);
    }

    @Provides
    @Named("min_connection_duration_ms")
    static int providesMinConnectionDuration(@Main Resources resources) {
        return resources.getInteger(C1893R.integer.config_connectionMinDuration);
    }
}
