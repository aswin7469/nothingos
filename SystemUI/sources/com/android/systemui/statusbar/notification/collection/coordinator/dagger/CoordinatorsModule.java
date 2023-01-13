package com.android.systemui.statusbar.notification.collection.coordinator.dagger;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent;
import dagger.Module;
import dagger.Provides;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/dagger/CoordinatorsModule;", "", "()V", "notifCoordinators", "Lcom/android/systemui/statusbar/notification/collection/coordinator/NotifCoordinators;", "factory", "Lcom/android/systemui/statusbar/notification/collection/coordinator/dagger/CoordinatorsSubcomponent$Factory;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module(subcomponents = {CoordinatorsSubcomponent.class})
/* compiled from: CoordinatorsModule.kt */
public final class CoordinatorsModule {
    public static final CoordinatorsModule INSTANCE = new CoordinatorsModule();

    private CoordinatorsModule() {
    }

    @SysUISingleton
    @JvmStatic
    @Provides
    public static final NotifCoordinators notifCoordinators(CoordinatorsSubcomponent.Factory factory) {
        Intrinsics.checkNotNullParameter(factory, "factory");
        return factory.create().getNotifCoordinators();
    }
}
