package com.android.settings.notification;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationHistory;
import android.app.usage.IUsageStatsManager;
import android.app.usage.UsageEvents;
import android.companion.AssociationInfo;
import android.companion.ICompanionDeviceManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.ShortcutInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.service.notification.ConversationChannelWrapper;
import android.service.notification.NotificationListenerFilter;
import android.util.IconDrawableFactory;
import android.util.Log;
import com.android.internal.util.CollectionUtils;
import com.android.settingslib.R$dimen;
import com.android.settingslib.R$plurals;
import com.android.settingslib.R$string;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.notification.ConversationIconFactory;
import com.android.settingslib.utils.StringUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotificationBackend {
    static INotificationManager sINM = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    static IUsageStatsManager sUsageStatsManager = IUsageStatsManager.Stub.asInterface(ServiceManager.getService("usagestats"));

    public static class AppRow extends Row {
        public boolean banned;
        public int blockedChannelCount;
        public int bubblePreference = 0;
        public int channelCount;
        public Drawable icon;
        public CharSequence label;
        public boolean lockedImportance;
        public String pkg;
        public NotificationsSentState sentByApp;
        public Map<String, NotificationsSentState> sentByChannel;
        public Intent settingsIntent;
        public boolean showBadge;
        public boolean systemApp;
        public int uid;
        public int userId;
    }

    public static class NotificationsSentState {
        public int avgSentDaily = 0;
        public int avgSentWeekly = 0;
        public long lastSent = 0;
        public int sentCount = 0;
    }

    public AppRow loadAppRow(Context context, PackageManager packageManager, ApplicationInfo applicationInfo) {
        AppRow appRow = new AppRow();
        appRow.pkg = applicationInfo.packageName;
        appRow.uid = applicationInfo.uid;
        try {
            appRow.label = applicationInfo.loadLabel(packageManager);
        } catch (Throwable th) {
            Log.e("NotificationBackend", "Error loading application label for " + appRow.pkg, th);
            appRow.label = appRow.pkg;
        }
        appRow.icon = IconDrawableFactory.newInstance(context).getBadgedIcon(applicationInfo);
        appRow.banned = getNotificationsBanned(appRow.pkg, appRow.uid);
        appRow.showBadge = canShowBadge(appRow.pkg, appRow.uid);
        appRow.bubblePreference = getBubblePreference(appRow.pkg, appRow.uid);
        appRow.userId = UserHandle.getUserId(appRow.uid);
        appRow.blockedChannelCount = getBlockedChannelCount(appRow.pkg, appRow.uid);
        appRow.channelCount = getChannelCount(appRow.pkg, appRow.uid);
        recordAggregatedUsageEvents(context, appRow);
        return appRow;
    }

    public AppRow loadAppRow(Context context, PackageManager packageManager, PackageInfo packageInfo) {
        AppRow loadAppRow = loadAppRow(context, packageManager, packageInfo.applicationInfo);
        recordCanBeBlocked(packageInfo, loadAppRow);
        return loadAppRow;
    }

    /* access modifiers changed from: package-private */
    public void recordCanBeBlocked(PackageInfo packageInfo, AppRow appRow) {
        try {
            boolean isImportanceLocked = sINM.isImportanceLocked(packageInfo.packageName, packageInfo.applicationInfo.uid);
            appRow.lockedImportance = isImportanceLocked;
            appRow.systemApp = isImportanceLocked;
        } catch (RemoteException e) {
            Log.w("NotificationBackend", "Error calling NMS", e);
        }
        if (packageInfo.applicationInfo.targetSdkVersion > 32) {
            String[] strArr = packageInfo.requestedPermissions;
            if (strArr == null || Arrays.stream(strArr).noneMatch(new NotificationBackend$$ExternalSyntheticLambda1())) {
                appRow.lockedImportance = true;
            }
        }
    }

    public static CharSequence getDeviceList(ICompanionDeviceManager iCompanionDeviceManager, LocalBluetoothManager localBluetoothManager, String str, int i) {
        StringBuilder sb = new StringBuilder();
        try {
            List<String> mapNotNull = CollectionUtils.mapNotNull(iCompanionDeviceManager.getAssociations(str, i), new NotificationBackend$$ExternalSyntheticLambda0());
            if (mapNotNull != null) {
                boolean z = false;
                for (String str2 : mapNotNull) {
                    for (CachedBluetoothDevice next : localBluetoothManager.getCachedDeviceManager().getCachedDevicesCopy()) {
                        if (Objects.equals(str2, next.getAddress())) {
                            if (z) {
                                sb.append(", ");
                            } else {
                                z = true;
                            }
                            sb.append(next.getName());
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            Log.w("NotificationBackend", "Error calling CDM", e);
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ String lambda$getDeviceList$1(AssociationInfo associationInfo) {
        if (associationInfo.isSelfManaged()) {
            return null;
        }
        return associationInfo.getDeviceMacAddress().toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean enableSwitch(android.content.Context r3, android.content.pm.ApplicationInfo r4) {
        /*
            r2 = this;
            r0 = 0
            android.content.pm.PackageManager r3 = r3.getPackageManager()     // Catch:{ NameNotFoundException -> 0x002c }
            java.lang.String r4 = r4.packageName     // Catch:{ NameNotFoundException -> 0x002c }
            r1 = 4096(0x1000, float:5.74E-42)
            android.content.pm.PackageInfo r3 = r3.getPackageInfo(r4, r1)     // Catch:{ NameNotFoundException -> 0x002c }
            com.android.settings.notification.NotificationBackend$AppRow r4 = new com.android.settings.notification.NotificationBackend$AppRow     // Catch:{ NameNotFoundException -> 0x002c }
            r4.<init>()     // Catch:{ NameNotFoundException -> 0x002c }
            r2.recordCanBeBlocked(r3, r4)     // Catch:{ NameNotFoundException -> 0x002c }
            boolean r2 = r4.systemApp     // Catch:{ NameNotFoundException -> 0x002c }
            r3 = 1
            if (r2 == 0) goto L_0x0023
            if (r2 == 0) goto L_0x0021
            boolean r2 = r4.banned     // Catch:{ NameNotFoundException -> 0x002c }
            if (r2 == 0) goto L_0x0021
            goto L_0x0023
        L_0x0021:
            r2 = r0
            goto L_0x0024
        L_0x0023:
            r2 = r3
        L_0x0024:
            if (r2 == 0) goto L_0x002b
            boolean r2 = r4.lockedImportance     // Catch:{ NameNotFoundException -> 0x002c }
            if (r2 != 0) goto L_0x002b
            r0 = r3
        L_0x002b:
            return r0
        L_0x002c:
            r2 = move-exception
            r2.printStackTrace()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.NotificationBackend.enableSwitch(android.content.Context, android.content.pm.ApplicationInfo):boolean");
    }

    public boolean getNotificationsBanned(String str, int i) {
        try {
            return !sINM.areNotificationsEnabledForPackage(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public boolean setNotificationsEnabledForPackage(String str, int i, boolean z) {
        try {
            if (onlyHasDefaultChannel(str, i)) {
                NotificationChannel channel = getChannel(str, i, "miscellaneous", (String) null);
                channel.setImportance(z ? -1000 : 0);
                updateChannel(str, i, channel);
            }
            sINM.setNotificationsEnabledForPackage(str, i, z);
            return true;
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public boolean canShowBadge(String str, int i) {
        try {
            return sINM.canShowBadge(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public boolean setShowBadge(String str, int i, boolean z) {
        try {
            sINM.setShowBadge(str, i, z);
            return true;
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public int getBubblePreference(String str, int i) {
        try {
            return sINM.getBubblePreferenceForPackage(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return -1;
        }
    }

    public boolean setAllowBubbles(String str, int i, int i2) {
        try {
            sINM.setBubblesAllowed(str, i, i2);
            return true;
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public NotificationChannel getChannel(String str, int i, String str2, String str3) {
        if (str2 == null) {
            return null;
        }
        try {
            return sINM.getNotificationChannelForPackage(str, i, str2, str3, true);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return null;
        }
    }

    public NotificationChannelGroup getGroup(String str, int i, String str2) {
        if (str2 == null) {
            return null;
        }
        try {
            return sINM.getNotificationChannelGroupForPackage(str2, str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return null;
        }
    }

    public ParceledListSlice<NotificationChannelGroup> getGroups(String str, int i) {
        try {
            return sINM.getNotificationChannelGroupsForPackage(str, i, false);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return ParceledListSlice.emptyList();
        }
    }

    public ParceledListSlice<ConversationChannelWrapper> getConversations(String str, int i) {
        try {
            return sINM.getConversationsForPackage(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return ParceledListSlice.emptyList();
        }
    }

    public ParceledListSlice<ConversationChannelWrapper> getConversations(boolean z) {
        try {
            return sINM.getConversations(z);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return ParceledListSlice.emptyList();
        }
    }

    public boolean hasSentValidMsg(String str, int i) {
        try {
            return sINM.hasSentValidMsg(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public boolean isInInvalidMsgState(String str, int i) {
        try {
            return sINM.isInInvalidMsgState(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public boolean hasUserDemotedInvalidMsgApp(String str, int i) {
        try {
            return sINM.hasUserDemotedInvalidMsgApp(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public void setInvalidMsgAppDemoted(String str, int i, boolean z) {
        try {
            sINM.setInvalidMsgAppDemoted(str, i, z);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public boolean hasSentValidBubble(String str, int i) {
        try {
            return sINM.hasSentValidBubble(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public ParceledListSlice<NotificationChannel> getNotificationChannelsBypassingDnd(String str, int i) {
        try {
            return sINM.getNotificationChannelsBypassingDnd(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return ParceledListSlice.emptyList();
        }
    }

    public void updateChannel(String str, int i, NotificationChannel notificationChannel) {
        try {
            sINM.updateNotificationChannelForPackage(str, i, notificationChannel);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public void updateChannelGroup(String str, int i, NotificationChannelGroup notificationChannelGroup) {
        try {
            sINM.updateNotificationChannelGroupForPackage(str, i, notificationChannelGroup);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public int getDeletedChannelCount(String str, int i) {
        try {
            return sINM.getDeletedChannelCount(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return 0;
        }
    }

    public int getBlockedChannelCount(String str, int i) {
        try {
            return sINM.getBlockedChannelCount(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return 0;
        }
    }

    public boolean onlyHasDefaultChannel(String str, int i) {
        try {
            return sINM.onlyHasDefaultChannel(str, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public int getChannelCount(String str, int i) {
        try {
            return sINM.getNumNotificationChannelsForPackage(str, i, false);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return 0;
        }
    }

    public boolean shouldHideSilentStatusBarIcons(Context context) {
        try {
            return sINM.shouldHideSilentStatusIcons(context.getPackageName());
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public void setHideSilentStatusIcons(boolean z) {
        try {
            sINM.setHideSilentStatusIcons(z);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public boolean showSilentInStatusBar(String str) {
        try {
            return !sINM.shouldHideSilentStatusIcons(str);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public NotificationHistory getNotificationHistory(String str, String str2) {
        try {
            return sINM.getNotificationHistory(str, str2);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return new NotificationHistory();
        }
    }

    /* access modifiers changed from: protected */
    public void recordAggregatedUsageEvents(Context context, AppRow appRow) {
        UsageEvents usageEvents;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            usageEvents = sUsageStatsManager.queryEventsForPackageForUser(currentTimeMillis - 604800000, currentTimeMillis, appRow.userId, appRow.pkg, context.getPackageName());
        } catch (RemoteException e) {
            e.printStackTrace();
            usageEvents = null;
        }
        recordAggregatedUsageEvents(usageEvents, appRow);
    }

    /* access modifiers changed from: protected */
    public void recordAggregatedUsageEvents(UsageEvents usageEvents, AppRow appRow) {
        String str;
        appRow.sentByChannel = new HashMap();
        appRow.sentByApp = new NotificationsSentState();
        if (usageEvents != null) {
            UsageEvents.Event event = new UsageEvents.Event();
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == 12 && (str = event.mNotificationChannelId) != null) {
                    NotificationsSentState notificationsSentState = appRow.sentByChannel.get(str);
                    if (notificationsSentState == null) {
                        notificationsSentState = new NotificationsSentState();
                        appRow.sentByChannel.put(str, notificationsSentState);
                    }
                    if (event.getTimeStamp() > notificationsSentState.lastSent) {
                        notificationsSentState.lastSent = event.getTimeStamp();
                        appRow.sentByApp.lastSent = event.getTimeStamp();
                    }
                    notificationsSentState.sentCount++;
                    appRow.sentByApp.sentCount++;
                    calculateAvgSentCounts(notificationsSentState);
                }
            }
            calculateAvgSentCounts(appRow.sentByApp);
        }
    }

    public static CharSequence getSentSummary(Context context, NotificationsSentState notificationsSentState, boolean z) {
        if (notificationsSentState == null) {
            return null;
        }
        if (z) {
            if (notificationsSentState.lastSent == 0) {
                return context.getString(R$string.notifications_sent_never);
            }
            return StringUtil.formatRelativeTime(context, (double) (System.currentTimeMillis() - notificationsSentState.lastSent), true);
        } else if (notificationsSentState.avgSentDaily > 0) {
            Resources resources = context.getResources();
            int i = R$plurals.notifications_sent_daily;
            int i2 = notificationsSentState.avgSentDaily;
            return resources.getQuantityString(i, i2, new Object[]{Integer.valueOf(i2)});
        } else {
            Resources resources2 = context.getResources();
            int i3 = R$plurals.notifications_sent_weekly;
            int i4 = notificationsSentState.avgSentWeekly;
            return resources2.getQuantityString(i3, i4, new Object[]{Integer.valueOf(i4)});
        }
    }

    private void calculateAvgSentCounts(NotificationsSentState notificationsSentState) {
        if (notificationsSentState != null) {
            notificationsSentState.avgSentDaily = Math.round(((float) notificationsSentState.sentCount) / 7.0f);
            int i = notificationsSentState.sentCount;
            if (i < 7) {
                notificationsSentState.avgSentWeekly = i;
            }
        }
    }

    public ComponentName getAllowedNotificationAssistant() {
        try {
            return sINM.getAllowedNotificationAssistant();
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return null;
        }
    }

    public ComponentName getDefaultNotificationAssistant() {
        try {
            return sINM.getDefaultNotificationAssistant();
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return null;
        }
    }

    public void setNASMigrationDoneAndResetDefault(int i, boolean z) {
        try {
            sINM.setNASMigrationDoneAndResetDefault(i, z);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public boolean setNotificationAssistantGranted(ComponentName componentName) {
        try {
            sINM.setNotificationAssistantAccessGranted(componentName, true);
            if (componentName != null) {
                return componentName.equals(sINM.getAllowedNotificationAssistant());
            }
            if (sINM.getAllowedNotificationAssistant() == null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    public void createConversationNotificationChannel(String str, int i, NotificationChannel notificationChannel, String str2) {
        try {
            sINM.createConversationNotificationChannelForPackage(str, i, notificationChannel, str2);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public ShortcutInfo getConversationInfo(Context context, String str, int i, String str2) {
        List<ShortcutInfo> shortcuts = ((LauncherApps) context.getSystemService(LauncherApps.class)).getShortcuts(new LauncherApps.ShortcutQuery().setPackage(str).setQueryFlags(1041).setShortcutIds(Arrays.asList(new String[]{str2})), UserHandle.of(UserHandle.getUserId(i)));
        if (shortcuts == null || shortcuts.isEmpty()) {
            return null;
        }
        return shortcuts.get(0);
    }

    public Drawable getConversationDrawable(Context context, ShortcutInfo shortcutInfo, String str, int i, boolean z) {
        if (shortcutInfo == null) {
            return null;
        }
        return new ConversationIconFactory(context, (LauncherApps) context.getSystemService(LauncherApps.class), context.getPackageManager(), IconDrawableFactory.newInstance(context, false), context.getResources().getDimensionPixelSize(R$dimen.conversation_icon_size)).getConversationDrawable(shortcutInfo, str, i, z);
    }

    public void resetNotificationImportance() {
        try {
            sINM.unlockAllNotificationChannels();
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public NotificationListenerFilter getListenerFilter(ComponentName componentName, int i) {
        NotificationListenerFilter notificationListenerFilter;
        try {
            notificationListenerFilter = sINM.getListenerFilter(componentName, i);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            notificationListenerFilter = null;
        }
        return notificationListenerFilter != null ? notificationListenerFilter : new NotificationListenerFilter();
    }

    public void setListenerFilter(ComponentName componentName, int i, NotificationListenerFilter notificationListenerFilter) {
        try {
            sINM.setListenerFilter(componentName, i, notificationListenerFilter);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
        }
    }

    public boolean isNotificationListenerAccessGranted(ComponentName componentName) {
        try {
            return sINM.isNotificationListenerAccessGranted(componentName);
        } catch (Exception e) {
            Log.w("NotificationBackend", "Error calling NoMan", e);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void setNm(INotificationManager iNotificationManager) {
        sINM = iNotificationManager;
    }

    static class Row {
        Row() {
        }
    }
}
