package com.android.systemui.util.service.dagger;

import android.content.ComponentName;
import com.android.systemui.util.service.PackageObserver;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent
public interface PackageObserverComponent {

    @Subcomponent.Factory
    public interface Factory {
        PackageObserverComponent create(@BindsInstance ComponentName componentName);
    }

    PackageObserver getPackageObserver();
}
