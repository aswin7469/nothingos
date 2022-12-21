package com.android.systemui.statusbar.notification.icon;

import android.app.Notification;
import android.app.Person;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.widget.ImageView;
import androidx.core.app.NotificationCompat;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000m\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0004*\u0001\n\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u001c\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00190\u001d2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u001e\u001a\u00020\u001b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u001f\u001a\u00020\u0012H\u0002J \u0010 \u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010!\u001a\u00020\u00192\u0006\u0010\"\u001a\u00020#H\u0002J\u0016\u0010$\u001a\u00020\u00122\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00100&H\u0016J \u0010'\u001a\u00020\u001b2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020#2\u0006\u0010!\u001a\u00020\u0019H\u0002J\u000e\u0010(\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u0010)\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0002R\u0010\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u000e¢\u0006\u0002\n\u0000¨\u0006*"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/icon/IconManager;", "Lcom/android/systemui/statusbar/notification/icon/ConversationIconManager;", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "launcherApps", "Landroid/content/pm/LauncherApps;", "iconBuilder", "Lcom/android/systemui/statusbar/notification/icon/IconBuilder;", "(Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;Landroid/content/pm/LauncherApps;Lcom/android/systemui/statusbar/notification/icon/IconBuilder;)V", "entryListener", "com/android/systemui/statusbar/notification/icon/IconManager$entryListener$1", "Lcom/android/systemui/statusbar/notification/icon/IconManager$entryListener$1;", "sensitivityListener", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry$OnSensitivityChangedListener;", "unimportantConversationKeys", "", "", "attach", "", "createIcons", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "createPeopleAvatar", "Landroid/graphics/drawable/Icon;", "getIconDescriptor", "Lcom/android/internal/statusbar/StatusBarIcon;", "redact", "", "getIconDescriptors", "Lkotlin/Pair;", "isImportantConversation", "recalculateForImportantConversationChange", "setIcon", "iconDescriptor", "iconView", "Lcom/android/systemui/statusbar/StatusBarIconView;", "setUnimportantConversations", "keys", "", "showsConversation", "updateIcons", "updateIconsSafe", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: IconManager.kt */
public final class IconManager implements ConversationIconManager {
    private final IconManager$entryListener$1 entryListener = new IconManager$entryListener$1(this);
    private final IconBuilder iconBuilder;
    private final LauncherApps launcherApps;
    private final CommonNotifCollection notifCollection;
    /* access modifiers changed from: private */
    public final NotificationEntry.OnSensitivityChangedListener sensitivityListener = new IconManager$$ExternalSyntheticLambda0(this);
    private Set<String> unimportantConversationKeys = SetsKt.emptySet();

    @Inject
    public IconManager(CommonNotifCollection commonNotifCollection, LauncherApps launcherApps2, IconBuilder iconBuilder2) {
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(launcherApps2, "launcherApps");
        Intrinsics.checkNotNullParameter(iconBuilder2, "iconBuilder");
        this.notifCollection = commonNotifCollection;
        this.launcherApps = launcherApps2;
        this.iconBuilder = iconBuilder2;
    }

    public final void attach() {
        this.notifCollection.addCollectionListener(this.entryListener);
    }

    /* access modifiers changed from: private */
    /* renamed from: sensitivityListener$lambda-0  reason: not valid java name */
    public static final void m3119sensitivityListener$lambda0(IconManager iconManager, NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(iconManager, "this$0");
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        iconManager.updateIconsSafe(notificationEntry);
    }

    /* access modifiers changed from: private */
    public final void recalculateForImportantConversationChange() {
        for (NotificationEntry next : this.notifCollection.getAllNotifs()) {
            Intrinsics.checkNotNullExpressionValue(next, "entry");
            boolean isImportantConversation = isImportantConversation(next);
            if (next.getIcons().getAreIconsAvailable() && isImportantConversation != next.getIcons().isImportantConversation()) {
                updateIconsSafe(next);
            }
            next.getIcons().setImportantConversation(isImportantConversation);
        }
    }

    public final void createIcons(NotificationEntry notificationEntry) throws InflationException {
        StatusBarIconView statusBarIconView;
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        StatusBarIconView createIconView = this.iconBuilder.createIconView(notificationEntry);
        createIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        StatusBarIconView createIconView2 = this.iconBuilder.createIconView(notificationEntry);
        createIconView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView2.setVisibility(4);
        StatusBarIconView createIconView3 = this.iconBuilder.createIconView(notificationEntry);
        createIconView3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView3.setIncreasedSize(true);
        if (notificationEntry.getSbn().getNotification().isMediaNotification()) {
            statusBarIconView = this.iconBuilder.createIconView(notificationEntry);
            statusBarIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            statusBarIconView = null;
            StatusBarIconView statusBarIconView2 = null;
        }
        Pair<StatusBarIcon, StatusBarIcon> iconDescriptors = getIconDescriptors(notificationEntry);
        StatusBarIcon component1 = iconDescriptors.component1();
        StatusBarIcon component2 = iconDescriptors.component2();
        try {
            setIcon(notificationEntry, component1, createIconView);
            setIcon(notificationEntry, component2, createIconView2);
            setIcon(notificationEntry, component2, createIconView3);
            if (statusBarIconView != null) {
                setIcon(notificationEntry, component1, statusBarIconView);
            }
            notificationEntry.setIcons(IconPack.buildPack(createIconView, createIconView2, createIconView3, statusBarIconView, notificationEntry.getIcons()));
        } catch (InflationException e) {
            notificationEntry.setIcons(IconPack.buildEmptyPack(notificationEntry.getIcons()));
            throw e;
        }
    }

    public final void updateIcons(NotificationEntry notificationEntry) throws InflationException {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (notificationEntry.getIcons().getAreIconsAvailable()) {
            notificationEntry.getIcons().setSmallIconDescriptor((StatusBarIcon) null);
            notificationEntry.getIcons().setPeopleAvatarDescriptor((StatusBarIcon) null);
            Pair<StatusBarIcon, StatusBarIcon> iconDescriptors = getIconDescriptors(notificationEntry);
            StatusBarIcon component1 = iconDescriptors.component1();
            StatusBarIcon component2 = iconDescriptors.component2();
            StatusBarIconView statusBarIcon = notificationEntry.getIcons().getStatusBarIcon();
            if (statusBarIcon != null) {
                statusBarIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component1, statusBarIcon);
            }
            StatusBarIconView shelfIcon = notificationEntry.getIcons().getShelfIcon();
            if (shelfIcon != null) {
                shelfIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component1, shelfIcon);
            }
            StatusBarIconView aodIcon = notificationEntry.getIcons().getAodIcon();
            if (aodIcon != null) {
                aodIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component2, aodIcon);
            }
            StatusBarIconView centeredIcon = notificationEntry.getIcons().getCenteredIcon();
            if (centeredIcon != null) {
                centeredIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component2, centeredIcon);
            }
        }
    }

    private final void updateIconsSafe(NotificationEntry notificationEntry) {
        try {
            updateIcons(notificationEntry);
        } catch (InflationException e) {
            Log.e("IconManager", "Unable to update icon", e);
        }
    }

    private final Pair<StatusBarIcon, StatusBarIcon> getIconDescriptors(NotificationEntry notificationEntry) throws InflationException {
        StatusBarIcon iconDescriptor = getIconDescriptor(notificationEntry, false);
        return new Pair<>(iconDescriptor, notificationEntry.isSensitive() ? getIconDescriptor(notificationEntry, true) : iconDescriptor);
    }

    private final StatusBarIcon getIconDescriptor(NotificationEntry notificationEntry, boolean z) throws InflationException {
        Icon icon;
        Notification notification = notificationEntry.getSbn().getNotification();
        boolean z2 = isImportantConversation(notificationEntry) && !z;
        StatusBarIcon peopleAvatarDescriptor = notificationEntry.getIcons().getPeopleAvatarDescriptor();
        StatusBarIcon smallIconDescriptor = notificationEntry.getIcons().getSmallIconDescriptor();
        if (z2 && peopleAvatarDescriptor != null) {
            return peopleAvatarDescriptor;
        }
        if (!z2 && smallIconDescriptor != null) {
            return smallIconDescriptor;
        }
        if (z2) {
            icon = createPeopleAvatar(notificationEntry);
        } else {
            icon = notification.getSmallIcon();
        }
        Icon icon2 = icon;
        if (icon2 != null) {
            UserHandle user = notificationEntry.getSbn().getUser();
            String packageName = notificationEntry.getSbn().getPackageName();
            int i = notification.iconLevel;
            int i2 = notification.number;
            IconBuilder iconBuilder2 = this.iconBuilder;
            Intrinsics.checkNotNullExpressionValue(notification, "n");
            StatusBarIcon statusBarIcon = new StatusBarIcon(user, packageName, icon2, i, i2, iconBuilder2.getIconContentDescription(notification));
            if (isImportantConversation(notificationEntry)) {
                if (z2) {
                    notificationEntry.getIcons().setPeopleAvatarDescriptor(statusBarIcon);
                } else {
                    notificationEntry.getIcons().setSmallIconDescriptor(statusBarIcon);
                }
            }
            return statusBarIcon;
        }
        throw new InflationException("No icon in notification from " + notificationEntry.getSbn().getPackageName());
    }

    private final void setIcon(NotificationEntry notificationEntry, StatusBarIcon statusBarIcon, StatusBarIconView statusBarIconView) throws InflationException {
        statusBarIconView.setShowsConversation(showsConversation(notificationEntry, statusBarIconView, statusBarIcon));
        statusBarIconView.setTag(C1893R.C1897id.icon_is_pre_L, Boolean.valueOf(notificationEntry.targetSdk < 21));
        if (!statusBarIconView.set(statusBarIcon)) {
            throw new InflationException("Couldn't create icon " + statusBarIcon);
        }
    }

    private final Icon createPeopleAvatar(NotificationEntry notificationEntry) throws InflationException {
        ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
        Icon shortcutIcon = conversationShortcutInfo != null ? this.launcherApps.getShortcutIcon(conversationShortcutInfo) : null;
        if (shortcutIcon == null) {
            Bundle bundle = notificationEntry.getSbn().getNotification().extras;
            Intrinsics.checkNotNullExpressionValue(bundle, "entry.sbn.notification.extras");
            List messagesFromBundleArray = Notification.MessagingStyle.Message.getMessagesFromBundleArray(bundle.getParcelableArray(NotificationCompat.EXTRA_MESSAGES));
            Person person = (Person) bundle.getParcelable("android.messagingUser");
            int size = messagesFromBundleArray.size() - 1;
            if (size >= 0) {
                while (true) {
                    int i = size - 1;
                    Notification.MessagingStyle.Message message = (Notification.MessagingStyle.Message) messagesFromBundleArray.get(size);
                    Person senderPerson = message.getSenderPerson();
                    if (senderPerson != null && senderPerson != person) {
                        Person senderPerson2 = message.getSenderPerson();
                        Intrinsics.checkNotNull(senderPerson2);
                        shortcutIcon = senderPerson2.getIcon();
                        break;
                    } else if (i < 0) {
                        break;
                    } else {
                        size = i;
                    }
                }
            }
        }
        if (shortcutIcon == null) {
            shortcutIcon = notificationEntry.getSbn().getNotification().getLargeIcon();
        }
        if (shortcutIcon == null) {
            shortcutIcon = notificationEntry.getSbn().getNotification().getSmallIcon();
        }
        if (shortcutIcon != null) {
            return shortcutIcon;
        }
        throw new InflationException("No icon in notification from " + notificationEntry.getSbn().getPackageName());
    }

    private final boolean showsConversation(NotificationEntry notificationEntry, StatusBarIconView statusBarIconView, StatusBarIcon statusBarIcon) {
        boolean z = statusBarIconView == notificationEntry.getIcons().getShelfIcon() || statusBarIconView == notificationEntry.getIcons().getAodIcon();
        boolean equals = statusBarIcon.icon.equals(notificationEntry.getSbn().getNotification().getSmallIcon());
        if (!isImportantConversation(notificationEntry) || equals) {
            return false;
        }
        if (!z || !notificationEntry.isSensitive()) {
            return true;
        }
        return false;
    }

    private final boolean isImportantConversation(NotificationEntry notificationEntry) {
        return notificationEntry.getRanking().getChannel() != null && notificationEntry.getRanking().getChannel().isImportantConversation() && !this.unimportantConversationKeys.contains(notificationEntry.getKey());
    }

    public void setUnimportantConversations(Collection<String> collection) {
        Intrinsics.checkNotNullParameter(collection, "keys");
        Set<String> set = CollectionsKt.toSet(collection);
        boolean z = !Intrinsics.areEqual((Object) this.unimportantConversationKeys, (Object) set);
        this.unimportantConversationKeys = set;
        if (z) {
            recalculateForImportantConversationChange();
        }
    }
}
