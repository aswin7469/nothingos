package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationSectionHeadersModule.kt */
/* loaded from: classes.dex */
public interface SectionHeaderControllerSubcomponent {

    /* compiled from: NotificationSectionHeadersModule.kt */
    /* loaded from: classes.dex */
    public interface Builder {
        @NotNull
        SectionHeaderControllerSubcomponent build();

        @NotNull
        /* renamed from: clickIntentAction */
        Builder mo1414clickIntentAction(@NotNull String str);

        @NotNull
        /* renamed from: headerText */
        Builder mo1415headerText(int i);

        @NotNull
        /* renamed from: nodeLabel */
        Builder mo1416nodeLabel(@NotNull String str);
    }

    @NotNull
    SectionHeaderController getHeaderController();

    @NotNull
    NodeController getNodeController();
}
