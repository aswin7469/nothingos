package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl;
import dagger.Binds;
import dagger.Module;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b#\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H'¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/dagger/SectionHeaderBindingModule;", "", "()V", "bindsNodeController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "impl", "Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderNodeControllerImpl;", "bindsSectionHeaderController", "Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* compiled from: NotificationSectionHeadersModule.kt */
abstract class SectionHeaderBindingModule {
    @Binds
    public abstract NodeController bindsNodeController(SectionHeaderNodeControllerImpl sectionHeaderNodeControllerImpl);

    @Binds
    public abstract SectionHeaderController bindsSectionHeaderController(SectionHeaderNodeControllerImpl sectionHeaderNodeControllerImpl);
}
