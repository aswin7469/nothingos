package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import dagger.Module;
import dagger.Provides;
import javax.inject.Provider;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0016\u0010\t\u001a\u00020\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0007J\u0012\u0010\r\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u000e\u001a\u00020\b2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0016\u0010\u000f\u001a\u00020\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0007J\u0012\u0010\u0010\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0011\u001a\u00020\b2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0016\u0010\u0012\u001a\u00020\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0007J\u0012\u0010\u0013\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\u0014\u001a\u00020\b2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0016\u0010\u0015\u001a\u00020\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0007¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/dagger/NotificationSectionHeadersModule;", "", "()V", "providesAlertingHeaderController", "Lcom/android/systemui/statusbar/notification/collection/render/SectionHeaderController;", "subcomponent", "Lcom/android/systemui/statusbar/notification/dagger/SectionHeaderControllerSubcomponent;", "providesAlertingHeaderNodeController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "providesAlertingHeaderSubcomponent", "builder", "Ljavax/inject/Provider;", "Lcom/android/systemui/statusbar/notification/dagger/SectionHeaderControllerSubcomponent$Builder;", "providesIncomingHeaderController", "providesIncomingHeaderNodeController", "providesIncomingHeaderSubcomponent", "providesPeopleHeaderController", "providesPeopleHeaderNodeController", "providesPeopleHeaderSubcomponent", "providesSilentHeaderController", "providesSilentHeaderNodeController", "providesSilentHeaderSubcomponent", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module(subcomponents = {SectionHeaderControllerSubcomponent.class})
/* compiled from: NotificationSectionHeadersModule.kt */
public final class NotificationSectionHeadersModule {
    public static final NotificationSectionHeadersModule INSTANCE = new NotificationSectionHeadersModule();

    private NotificationSectionHeadersModule() {
    }

    @SysUISingleton
    @JvmStatic
    @Provides
    public static final SectionHeaderControllerSubcomponent providesIncomingHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("incoming header").headerText(C1894R.string.notification_section_header_incoming).clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    @SysUISingleton
    @JvmStatic
    @Provides
    public static final SectionHeaderControllerSubcomponent providesAlertingHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("alerting header").headerText(C1894R.string.notification_section_header_alerting).clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    @SysUISingleton
    @JvmStatic
    @Provides
    public static final SectionHeaderControllerSubcomponent providesPeopleHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("people header").headerText(C1894R.string.notification_section_header_conversations).clickIntentAction("android.settings.CONVERSATION_SETTINGS").build();
    }

    @SysUISingleton
    @JvmStatic
    @Provides
    public static final SectionHeaderControllerSubcomponent providesSilentHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        Intrinsics.checkNotNullParameter(provider, "builder");
        return provider.get().nodeLabel("silent header").headerText(C1894R.string.notification_section_header_gentle).clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
    }

    @JvmStatic
    @Provides
    public static final NodeController providesSilentHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    @JvmStatic
    @Provides
    public static final SectionHeaderController providesSilentHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }

    @JvmStatic
    @Provides
    public static final NodeController providesAlertingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    @JvmStatic
    @Provides
    public static final SectionHeaderController providesAlertingHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }

    @JvmStatic
    @Provides
    public static final NodeController providesPeopleHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    @JvmStatic
    @Provides
    public static final SectionHeaderController providesPeopleHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }

    @JvmStatic
    @Provides
    public static final NodeController providesIncomingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getNodeController();
    }

    @JvmStatic
    @Provides
    public static final SectionHeaderController providesIncomingHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        Intrinsics.checkNotNullParameter(sectionHeaderControllerSubcomponent, "subcomponent");
        return sectionHeaderControllerSubcomponent.getHeaderController();
    }
}
