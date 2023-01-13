package com.android.systemui.statusbar.policy;

import android.content.res.Configuration;

public interface ConfigurationController extends CallbackController<ConfigurationListener> {

    public interface ConfigurationListener {
        void onConfigChanged(Configuration configuration) {
        }

        void onDensityOrFontScaleChanged() {
        }

        void onLayoutDirectionChanged(boolean z) {
        }

        void onLocaleListChanged() {
        }

        void onMaxBoundsChanged() {
        }

        void onOrientationChanged(int i) {
        }

        void onSmallestScreenWidthChanged() {
        }

        void onThemeChanged() {
        }

        void onUiModeChanged() {
        }

        void onUiModeChangedDelayCheck() {
        }
    }

    int getOrientation();

    boolean isLayoutRtl();

    void notifyThemeChanged();

    void onConfigurationChanged(Configuration configuration);
}
