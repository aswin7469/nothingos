package com.android.systemui.statusbar.notification.collection.coordinator;

import dagger.Binds;
import dagger.Module;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bc\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H'ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/PrivateSensitiveContentCoordinatorModule;", "", "bindCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SensitiveContentCoordinator;", "impl", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SensitiveContentCoordinatorImpl;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* compiled from: SensitiveContentCoordinator.kt */
interface PrivateSensitiveContentCoordinatorModule {
    @Binds
    SensitiveContentCoordinator bindCoordinator(SensitiveContentCoordinatorImpl sensitiveContentCoordinatorImpl);
}
