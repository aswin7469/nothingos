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
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.List;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: IconManager.kt */
/* loaded from: classes.dex */
public final class IconManager {
    @NotNull
    private final IconBuilder iconBuilder;
    @NotNull
    private final LauncherApps launcherApps;
    @NotNull
    private final CommonNotifCollection notifCollection;
    @NotNull
    private final IconManager$entryListener$1 entryListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.icon.IconManager$entryListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryInit(@NotNull NotificationEntry entry) {
            NotificationEntry.OnSensitivityChangedListener onSensitivityChangedListener;
            Intrinsics.checkNotNullParameter(entry, "entry");
            onSensitivityChangedListener = IconManager.this.sensitivityListener;
            entry.addOnSensitivityChangedListener(onSensitivityChangedListener);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onEntryCleanUp(@NotNull NotificationEntry entry) {
            NotificationEntry.OnSensitivityChangedListener onSensitivityChangedListener;
            Intrinsics.checkNotNullParameter(entry, "entry");
            onSensitivityChangedListener = IconManager.this.sensitivityListener;
            entry.removeOnSensitivityChangedListener(onSensitivityChangedListener);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public void onRankingApplied() {
            CommonNotifCollection commonNotifCollection;
            boolean isImportantConversation;
            commonNotifCollection = IconManager.this.notifCollection;
            for (NotificationEntry entry : commonNotifCollection.getAllNotifs()) {
                IconManager iconManager = IconManager.this;
                Intrinsics.checkNotNullExpressionValue(entry, "entry");
                isImportantConversation = iconManager.isImportantConversation(entry);
                if (entry.getIcons().getAreIconsAvailable() && isImportantConversation != entry.getIcons().isImportantConversation()) {
                    IconManager.this.updateIconsSafe(entry);
                }
                entry.getIcons().setImportantConversation(isImportantConversation);
            }
        }
    };
    @NotNull
    private final NotificationEntry.OnSensitivityChangedListener sensitivityListener = new NotificationEntry.OnSensitivityChangedListener() { // from class: com.android.systemui.statusbar.notification.icon.IconManager$sensitivityListener$1
        @Override // com.android.systemui.statusbar.notification.collection.NotificationEntry.OnSensitivityChangedListener
        public final void onSensitivityChanged(@NotNull NotificationEntry entry) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            IconManager.this.updateIconsSafe(entry);
        }
    };

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.notification.icon.IconManager$entryListener$1] */
    public IconManager(@NotNull CommonNotifCollection notifCollection, @NotNull LauncherApps launcherApps, @NotNull IconBuilder iconBuilder) {
        Intrinsics.checkNotNullParameter(notifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        Intrinsics.checkNotNullParameter(iconBuilder, "iconBuilder");
        this.notifCollection = notifCollection;
        this.launcherApps = launcherApps;
        this.iconBuilder = iconBuilder;
    }

    public final void attach() {
        this.notifCollection.addCollectionListener(this.entryListener);
    }

    public final void createIcons(@NotNull NotificationEntry entry) throws InflationException {
        StatusBarIconView statusBarIconView;
        Intrinsics.checkNotNullParameter(entry, "entry");
        StatusBarIconView createIconView = this.iconBuilder.createIconView(entry);
        createIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        StatusBarIconView createIconView2 = this.iconBuilder.createIconView(entry);
        createIconView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView2.setVisibility(4);
        StatusBarIconView createIconView3 = this.iconBuilder.createIconView(entry);
        createIconView3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView3.setIncreasedSize(true);
        if (entry.getSbn().getNotification().isMediaNotification()) {
            statusBarIconView = this.iconBuilder.createIconView(entry);
            statusBarIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            statusBarIconView = null;
        }
        Pair<StatusBarIcon, StatusBarIcon> iconDescriptors = getIconDescriptors(entry);
        StatusBarIcon component1 = iconDescriptors.component1();
        StatusBarIcon component2 = iconDescriptors.component2();
        try {
            setIcon(entry, component1, createIconView);
            setIcon(entry, component2, createIconView2);
            setIcon(entry, component2, createIconView3);
            if (statusBarIconView != null) {
                setIcon(entry, component1, statusBarIconView);
            }
            entry.setIcons(IconPack.buildPack(createIconView, createIconView2, createIconView3, statusBarIconView, entry.getIcons()));
        } catch (InflationException e) {
            entry.setIcons(IconPack.buildEmptyPack(entry.getIcons()));
            throw e;
        }
    }

    public final void updateIcons(@NotNull NotificationEntry entry) throws InflationException {
        Intrinsics.checkNotNullParameter(entry, "entry");
        if (!entry.getIcons().getAreIconsAvailable()) {
            return;
        }
        entry.getIcons().setSmallIconDescriptor(null);
        entry.getIcons().setPeopleAvatarDescriptor(null);
        Pair<StatusBarIcon, StatusBarIcon> iconDescriptors = getIconDescriptors(entry);
        StatusBarIcon component1 = iconDescriptors.component1();
        StatusBarIcon component2 = iconDescriptors.component2();
        StatusBarIconView statusBarIcon = entry.getIcons().getStatusBarIcon();
        if (statusBarIcon != null) {
            statusBarIcon.setNotification(entry.getSbn());
            setIcon(entry, component1, statusBarIcon);
        }
        StatusBarIconView shelfIcon = entry.getIcons().getShelfIcon();
        if (shelfIcon != null) {
            shelfIcon.setNotification(entry.getSbn());
            setIcon(entry, component1, shelfIcon);
        }
        StatusBarIconView aodIcon = entry.getIcons().getAodIcon();
        if (aodIcon != null) {
            aodIcon.setNotification(entry.getSbn());
            setIcon(entry, component2, aodIcon);
        }
        StatusBarIconView centeredIcon = entry.getIcons().getCenteredIcon();
        if (centeredIcon == null) {
            return;
        }
        centeredIcon.setNotification(entry.getSbn());
        setIcon(entry, component2, centeredIcon);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateIconsSafe(NotificationEntry notificationEntry) {
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
        Icon smallIcon;
        Notification n = notificationEntry.getSbn().getNotification();
        boolean z2 = isImportantConversation(notificationEntry) && !z;
        StatusBarIcon peopleAvatarDescriptor = notificationEntry.getIcons().getPeopleAvatarDescriptor();
        StatusBarIcon smallIconDescriptor = notificationEntry.getIcons().getSmallIconDescriptor();
        if (!z2 || peopleAvatarDescriptor == null) {
            if (!z2 && smallIconDescriptor != null) {
                return smallIconDescriptor;
            }
            if (z2) {
                smallIcon = createPeopleAvatar(notificationEntry);
            } else {
                smallIcon = n.getSmallIcon();
            }
            Icon icon = smallIcon;
            if (icon == null) {
                throw new InflationException(Intrinsics.stringPlus("No icon in notification from ", notificationEntry.getSbn().getPackageName()));
            }
            UserHandle user = notificationEntry.getSbn().getUser();
            String packageName = notificationEntry.getSbn().getPackageName();
            int i = n.iconLevel;
            int i2 = n.number;
            IconBuilder iconBuilder = this.iconBuilder;
            Intrinsics.checkNotNullExpressionValue(n, "n");
            StatusBarIcon statusBarIcon = new StatusBarIcon(user, packageName, icon, i, i2, iconBuilder.getIconContentDescription(n));
            if (isImportantConversation(notificationEntry)) {
                if (z2) {
                    notificationEntry.getIcons().setPeopleAvatarDescriptor(statusBarIcon);
                } else {
                    notificationEntry.getIcons().setSmallIconDescriptor(statusBarIcon);
                }
            }
            return statusBarIcon;
        }
        return peopleAvatarDescriptor;
    }

    private final void setIcon(NotificationEntry notificationEntry, StatusBarIcon statusBarIcon, StatusBarIconView statusBarIconView) throws InflationException {
        statusBarIconView.setShowsConversation(showsConversation(notificationEntry, statusBarIconView, statusBarIcon));
        statusBarIconView.setTag(R$id.icon_is_pre_L, Boolean.valueOf(notificationEntry.targetSdk < 21));
        if (statusBarIconView.set(statusBarIcon)) {
            return;
        }
        throw new InflationException(Intrinsics.stringPlus("Couldn't create icon ", statusBarIcon));
    }

    private final Icon createPeopleAvatar(NotificationEntry notificationEntry) throws InflationException {
        ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
        Icon shortcutIcon = conversationShortcutInfo != null ? this.launcherApps.getShortcutIcon(conversationShortcutInfo) : null;
        if (shortcutIcon == null) {
            Bundle bundle = notificationEntry.getSbn().getNotification().extras;
            Intrinsics.checkNotNullExpressionValue(bundle, "entry.sbn.notification.extras");
            List<Notification.MessagingStyle.Message> messagesFromBundleArray = Notification.MessagingStyle.Message.getMessagesFromBundleArray(bundle.getParcelableArray("android.messages"));
            Person person = (Person) bundle.getParcelable("android.messagingUser");
            int size = messagesFromBundleArray.size() - 1;
            if (size >= 0) {
                while (true) {
                    int i = size - 1;
                    Notification.MessagingStyle.Message message = messagesFromBundleArray.get(size);
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
        throw new InflationException(Intrinsics.stringPlus("No icon in notification from ", notificationEntry.getSbn().getPackageName()));
    }

    private final boolean showsConversation(NotificationEntry notificationEntry, StatusBarIconView statusBarIconView, StatusBarIcon statusBarIcon) {
        boolean z = statusBarIconView == notificationEntry.getIcons().getShelfIcon() || statusBarIconView == notificationEntry.getIcons().getAodIcon();
        boolean equals = statusBarIcon.icon.equals(notificationEntry.getSbn().getNotification().getSmallIcon());
        if (!isImportantConversation(notificationEntry) || equals) {
            return false;
        }
        return !z || !notificationEntry.isSensitive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isImportantConversation(NotificationEntry notificationEntry) {
        return notificationEntry.getRanking().getChannel() != null && notificationEntry.getRanking().getChannel().isImportantConversation();
    }
}
