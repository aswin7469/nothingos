package com.android.systemui.statusbar.notification.collection;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListDumper {
    private static final String INDENT = "  ";

    public static String dumpTree(List<ListEntry> list, NotificationInteractionTracker notificationInteractionTracker, boolean z, String str) {
        NotificationInteractionTracker notificationInteractionTracker2 = notificationInteractionTracker;
        StringBuilder sb = new StringBuilder();
        String str2 = str + INDENT;
        for (int i = 0; i < list.size(); i++) {
            ListEntry listEntry = list.get(i);
            dumpEntry(listEntry, Integer.toString(i), str, sb, true, z, notificationInteractionTracker2.hasUserInteractedWith(listEntry.getKey()));
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    dumpEntry(summary, i + ":*", str2, sb, true, z, notificationInteractionTracker2.hasUserInteractedWith(summary.getKey()));
                }
                List<NotificationEntry> children = groupEntry.getChildren();
                for (int i2 = 0; i2 < children.size(); i2++) {
                    NotificationEntry notificationEntry = children.get(i2);
                    dumpEntry(notificationEntry, i + BaseIconCache.EMPTY_CLASS_NAME + i2, str2, sb, true, z, notificationInteractionTracker2.hasUserInteractedWith(notificationEntry.getKey()));
                }
            }
        }
        return sb.toString();
    }

    public static String dumpList(List<NotificationEntry> list, boolean z, String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            dumpEntry(list.get(i), Integer.toString(i), str, sb, false, z, false);
        }
        return sb.toString();
    }

    private static void dumpEntry(ListEntry listEntry, String str, String str2, StringBuilder sb, boolean z, boolean z2, boolean z3) {
        sb.append(str2).append(NavigationBarInflaterView.SIZE_MOD_START).append(str).append("] ").append(listEntry.getKey());
        if (z) {
            sb.append(" (parent=").append(listEntry.getParent() != null ? listEntry.getParent().getKey() : null).append(NavigationBarInflaterView.KEY_CODE_END);
        }
        if (listEntry.getSection() != null) {
            sb.append(" section=").append(listEntry.getSection().getLabel());
        }
        if (z2) {
            NotificationEntry notificationEntry = (NotificationEntry) Objects.requireNonNull(listEntry.getRepresentativeEntry());
            StringBuilder sb2 = new StringBuilder();
            if (!notificationEntry.mLifetimeExtenders.isEmpty()) {
                int size = notificationEntry.mLifetimeExtenders.size();
                String[] strArr = new String[size];
                for (int i = 0; i < size; i++) {
                    strArr[i] = notificationEntry.mLifetimeExtenders.get(i).getName();
                }
                sb2.append("lifetimeExtenders=").append(Arrays.toString((Object[]) strArr)).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (!notificationEntry.mDismissInterceptors.isEmpty()) {
                int size2 = notificationEntry.mDismissInterceptors.size();
                String[] strArr2 = new String[size2];
                for (int i2 = 0; i2 < size2; i2++) {
                    strArr2[i2] = notificationEntry.mDismissInterceptors.get(i2).getName();
                }
                sb2.append("dismissInterceptors=").append(Arrays.toString((Object[]) strArr2)).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (notificationEntry.getExcludingFilter() != null) {
                sb2.append("filter=").append(notificationEntry.getExcludingFilter().getName()).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (notificationEntry.getNotifPromoter() != null) {
                sb2.append("promoter=").append(notificationEntry.getNotifPromoter().getName()).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (notificationEntry.mCancellationReason != -1) {
                sb2.append("cancellationReason=").append(notificationEntry.mCancellationReason).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (notificationEntry.getDismissState() != NotificationEntry.DismissState.NOT_DISMISSED) {
                sb2.append("dismissState=").append((Object) notificationEntry.getDismissState()).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (notificationEntry.getAttachState().getSuppressedChanges().getParent() != null) {
                sb2.append("suppressedParent=").append(notificationEntry.getAttachState().getSuppressedChanges().getParent().getKey()).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (notificationEntry.getAttachState().getSuppressedChanges().getSection() != null) {
                sb2.append("suppressedSection=").append((Object) notificationEntry.getAttachState().getSuppressedChanges().getSection()).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            if (z3) {
                sb2.append("interacted=yes ");
            }
            String sb3 = sb2.toString();
            if (!sb3.isEmpty()) {
                sb.append("\n\t").append(str2).append(sb3);
            }
        }
        sb.append("\n");
    }
}
