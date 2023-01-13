package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import dagger.BindsInstance;
import dagger.Subcomponent;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001:\u0001\nR\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/dagger/SectionHeaderControllerSubcomponent;", "", "headerController", "Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;", "getHeaderController", "()Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;", "nodeController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "getNodeController", "()Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "Builder", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Subcomponent(modules = {SectionHeaderBindingModule.class})
/* compiled from: NotificationSectionHeadersModule.kt */
public interface SectionHeaderControllerSubcomponent {

    @Subcomponent.Builder
    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0012\u0010\u0004\u001a\u00020\u00002\b\b\u0001\u0010\u0004\u001a\u00020\u0005H'J\u0012\u0010\u0006\u001a\u00020\u00002\b\b\u0001\u0010\u0006\u001a\u00020\u0007H'J\u0012\u0010\b\u001a\u00020\u00002\b\b\u0001\u0010\b\u001a\u00020\u0005H'ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/dagger/SectionHeaderControllerSubcomponent$Builder;", "", "build", "Lcom/android/systemui/statusbar/notification/dagger/SectionHeaderControllerSubcomponent;", "clickIntentAction", "", "headerText", "", "nodeLabel", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationSectionHeadersModule.kt */
    public interface Builder {
        SectionHeaderControllerSubcomponent build();

        @BindsInstance
        Builder clickIntentAction(String str);

        @BindsInstance
        Builder headerText(int i);

        @BindsInstance
        Builder nodeLabel(String str);
    }

    SectionHeaderController getHeaderController();

    NodeController getNodeController();
}
