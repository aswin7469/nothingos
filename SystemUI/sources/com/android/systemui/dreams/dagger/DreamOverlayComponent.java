package com.android.systemui.dreams.dagger;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.dagger.ComplicationModule;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.dreams.touch.dagger.DreamTouchModule;
import dagger.BindsInstance;
import dagger.Subcomponent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Subcomponent(modules = {DreamTouchModule.class, DreamOverlayModule.class, ComplicationModule.class})
@DreamOverlayScope
public interface DreamOverlayComponent {

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DreamOverlayScope {
    }

    @Subcomponent.Factory
    public interface Factory {
        DreamOverlayComponent create(@BindsInstance ViewModelStore viewModelStore, @BindsInstance Complication.Host host);
    }

    DreamOverlayContainerViewController getDreamOverlayContainerViewController();

    DreamOverlayTouchMonitor getDreamOverlayTouchMonitor();

    LifecycleOwner getLifecycleOwner();

    LifecycleRegistry getLifecycleRegistry();
}
