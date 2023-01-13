package com.android.systemui.statusbar.notification.collection.coordinator.dagger;

import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import dagger.Subcomponent;
import kotlin.Metadata;

@CoordinatorScope
@Subcomponent(modules = {InternalCoordinatorsModule.class})
@Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001:\u0001\u0006R\u0014\u0010\u0002\u001a\u00020\u00038gX¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/dagger/CoordinatorsSubcomponent;", "", "notifCoordinators", "Lcom/android/systemui/statusbar/notification/collection/coordinator/NotifCoordinators;", "getNotifCoordinators", "()Lcom/android/systemui/statusbar/notification/collection/coordinator/NotifCoordinators;", "Factory", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: CoordinatorsModule.kt */
public interface CoordinatorsSubcomponent {

    @Subcomponent.Factory
    @Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/dagger/CoordinatorsSubcomponent$Factory;", "", "create", "Lcom/android/systemui/statusbar/notification/collection/coordinator/dagger/CoordinatorsSubcomponent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: CoordinatorsModule.kt */
    public interface Factory {
        CoordinatorsSubcomponent create();
    }

    @Internal
    NotifCoordinators getNotifCoordinators();
}
