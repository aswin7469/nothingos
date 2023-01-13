package com.android.systemui.statusbar.notification.people;

import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.NotificationPersonExtractorPlugin;
import com.android.systemui.statusbar.policy.ExtensionController;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0012\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\nH\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractorPluginBoundary;", "Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractor;", "extensionController", "Lcom/android/systemui/statusbar/policy/ExtensionController;", "(Lcom/android/systemui/statusbar/policy/ExtensionController;)V", "plugin", "Lcom/android/systemui/plugins/NotificationPersonExtractorPlugin;", "extractPerson", "Lcom/android/systemui/statusbar/notification/people/PersonModel;", "sbn", "Landroid/service/notification/StatusBarNotification;", "extractPersonKey", "", "isPersonNotification", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
public final class NotificationPersonExtractorPluginBoundary implements NotificationPersonExtractor {
    private NotificationPersonExtractorPlugin plugin;

    @Inject
    public NotificationPersonExtractorPluginBoundary(ExtensionController extensionController) {
        Intrinsics.checkNotNullParameter(extensionController, "extensionController");
        this.plugin = extensionController.newExtension(NotificationPersonExtractorPlugin.class).withPlugin(NotificationPersonExtractorPlugin.class).withCallback(new C2736x57544f0(this)).build().get();
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-0  reason: not valid java name */
    public static final void m3125_init_$lambda0(NotificationPersonExtractorPluginBoundary notificationPersonExtractorPluginBoundary, NotificationPersonExtractorPlugin notificationPersonExtractorPlugin) {
        Intrinsics.checkNotNullParameter(notificationPersonExtractorPluginBoundary, "this$0");
        notificationPersonExtractorPluginBoundary.plugin = notificationPersonExtractorPlugin;
    }

    public PersonModel extractPerson(StatusBarNotification statusBarNotification) {
        NotificationPersonExtractorPlugin.PersonData extractPerson;
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        NotificationPersonExtractorPlugin notificationPersonExtractorPlugin = this.plugin;
        if (notificationPersonExtractorPlugin == null || (extractPerson = notificationPersonExtractorPlugin.extractPerson(statusBarNotification)) == null) {
            return null;
        }
        String str = extractPerson.key;
        Intrinsics.checkNotNullExpressionValue(str, "key");
        int identifier = statusBarNotification.getUser().getIdentifier();
        CharSequence charSequence = extractPerson.name;
        Intrinsics.checkNotNullExpressionValue(charSequence, ZoneGetter.KEY_DISPLAYNAME);
        Drawable drawable = extractPerson.avatar;
        Intrinsics.checkNotNullExpressionValue(drawable, "avatar");
        Runnable runnable = extractPerson.clickRunnable;
        Intrinsics.checkNotNullExpressionValue(runnable, "clickRunnable");
        return new PersonModel(str, identifier, charSequence, drawable, runnable);
    }

    public String extractPersonKey(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        NotificationPersonExtractorPlugin notificationPersonExtractorPlugin = this.plugin;
        if (notificationPersonExtractorPlugin != null) {
            return notificationPersonExtractorPlugin.extractPersonKey(statusBarNotification);
        }
        return null;
    }

    public boolean isPersonNotification(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        NotificationPersonExtractorPlugin notificationPersonExtractorPlugin = this.plugin;
        if (notificationPersonExtractorPlugin != null) {
            return notificationPersonExtractorPlugin.isPersonNotification(statusBarNotification);
        }
        return false;
    }
}
