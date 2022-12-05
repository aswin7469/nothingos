package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.R$string;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import javax.inject.Provider;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationSectionHeadersModule.kt */
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule {
    @NotNull
    public static final NotificationSectionHeadersModule INSTANCE = new NotificationSectionHeadersModule();

    private NotificationSectionHeadersModule() {
    }

    @NotNull
    public static final SectionHeaderControllerSubcomponent providesIncomingHeaderSubcomponent(@NotNull Provider<SectionHeaderControllerSubcomponent.Builder> builder) {
        Intrinsics.checkNotNullParameter(builder, "builder");
        return builder.mo1933get().mo1416nodeLabel("incoming header").mo1415headerText(R$string.notification_section_header_incoming).mo1414clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    @NotNull
    public static final SectionHeaderControllerSubcomponent providesAlertingHeaderSubcomponent(@NotNull Provider<SectionHeaderControllerSubcomponent.Builder> builder) {
        Intrinsics.checkNotNullParameter(builder, "builder");
        return builder.mo1933get().mo1416nodeLabel("alerting header").mo1415headerText(R$string.notification_section_header_alerting).mo1414clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    @NotNull
    public static final SectionHeaderControllerSubcomponent providesPeopleHeaderSubcomponent(@NotNull Provider<SectionHeaderControllerSubcomponent.Builder> builder) {
        Intrinsics.checkNotNullParameter(builder, "builder");
        return builder.mo1933get().mo1416nodeLabel("people header").mo1415headerText(R$string.notification_section_header_conversations).mo1414clickIntentAction("android.settings.CONVERSATION_SETTINGS").build();
    }

    @NotNull
    public static final SectionHeaderControllerSubcomponent providesSilentHeaderSubcomponent(@NotNull Provider<SectionHeaderControllerSubcomponent.Builder> builder) {
        Intrinsics.checkNotNullParameter(builder, "builder");
        return builder.mo1933get().mo1416nodeLabel("silent header").mo1415headerText(R$string.notification_section_header_gentle).mo1414clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    @NotNull
    public static final NodeController providesSilentHeaderNodeController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getNodeController();
    }

    @NotNull
    public static final SectionHeaderController providesSilentHeaderController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getHeaderController();
    }

    @NotNull
    public static final NodeController providesAlertingHeaderNodeController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getNodeController();
    }

    @NotNull
    public static final SectionHeaderController providesAlertingHeaderController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getHeaderController();
    }

    @NotNull
    public static final NodeController providesPeopleHeaderNodeController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getNodeController();
    }

    @NotNull
    public static final SectionHeaderController providesPeopleHeaderController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getHeaderController();
    }

    @NotNull
    public static final NodeController providesIncomingHeaderNodeController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getNodeController();
    }

    @NotNull
    public static final SectionHeaderController providesIncomingHeaderController(@NotNull SectionHeaderControllerSubcomponent subcomponent) {
        Intrinsics.checkNotNullParameter(subcomponent, "subcomponent");
        return subcomponent.getHeaderController();
    }
}
