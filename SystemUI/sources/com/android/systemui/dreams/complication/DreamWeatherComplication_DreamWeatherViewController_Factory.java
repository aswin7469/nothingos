package com.android.systemui.dreams.complication;

import android.widget.TextView;
import com.android.systemui.dreams.complication.DreamWeatherComplication;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamWeatherComplication_DreamWeatherViewController_Factory implements Factory<DreamWeatherComplication.DreamWeatherViewController> {
    private final Provider<LockscreenSmartspaceController> smartspaceControllerProvider;
    private final Provider<TextView> viewProvider;

    public DreamWeatherComplication_DreamWeatherViewController_Factory(Provider<TextView> provider, Provider<LockscreenSmartspaceController> provider2) {
        this.viewProvider = provider;
        this.smartspaceControllerProvider = provider2;
    }

    public DreamWeatherComplication.DreamWeatherViewController get() {
        return newInstance(this.viewProvider.get(), this.smartspaceControllerProvider.get());
    }

    public static DreamWeatherComplication_DreamWeatherViewController_Factory create(Provider<TextView> provider, Provider<LockscreenSmartspaceController> provider2) {
        return new DreamWeatherComplication_DreamWeatherViewController_Factory(provider, provider2);
    }

    public static DreamWeatherComplication.DreamWeatherViewController newInstance(TextView textView, LockscreenSmartspaceController lockscreenSmartspaceController) {
        return new DreamWeatherComplication.DreamWeatherViewController(textView, lockscreenSmartspaceController);
    }
}
