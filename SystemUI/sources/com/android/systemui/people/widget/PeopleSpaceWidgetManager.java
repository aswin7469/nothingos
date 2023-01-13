package com.android.systemui.people.widget;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.backup.BackupManager;
import android.app.people.ConversationChannel;
import android.app.people.IPeopleManager;
import android.app.people.PeopleManager;
import android.app.people.PeopleSpaceTile;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.service.notification.ConversationChannelWrapper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.people.NotificationHelper;
import com.android.systemui.people.PeopleBackupFollowUpJob;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.people.PeopleTileViewHelper;
import com.android.systemui.people.SharedPreferencesHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.nothing.systemui.people.widget.PeopleSpaceWidgetManagerEx;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

@SysUISingleton
public class PeopleSpaceWidgetManager {
    private static final boolean DEBUG = false;
    private static final String TAG = "PeopleSpaceWidgetMgr";
    public static Map<PeopleTileKey, TileConversationListener> mListeners = new HashMap();
    public static Map<Integer, PeopleSpaceTile> mTiles = new HashMap();
    /* access modifiers changed from: private */
    public AppWidgetManager mAppWidgetManager;
    private BackupManager mBackupManager;
    protected final BroadcastReceiver mBaseBroadcastReceiver = new BroadcastReceiver() {
        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onReceive$0$com-android-systemui-people-widget-PeopleSpaceWidgetManager$2 */
        public /* synthetic */ void mo35201x8227a59a(Intent intent) {
            PeopleSpaceWidgetManager.this.updateWidgetsFromBroadcastInBackground(intent.getAction());
        }

        public void onReceive(Context context, Intent intent) {
            PeopleSpaceWidgetManager.this.mBgExecutor.execute(new PeopleSpaceWidgetManager$2$$ExternalSyntheticLambda0(this, intent));
        }
    };
    /* access modifiers changed from: private */
    public Executor mBgExecutor;
    private BroadcastDispatcher mBroadcastDispatcher;
    private Optional<Bubbles> mBubblesOptional;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final PeopleSpaceWidgetManagerEx mEx;
    private INotificationManager mINotificationManager;
    private IPeopleManager mIPeopleManager;
    private LauncherApps mLauncherApps;
    private final NotificationListener.NotificationHandler mListener = new NotificationListener.NotificationHandler() {
        public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        }

        public void onNotificationsInitialized() {
        }

        public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            PeopleSpaceWidgetManager.this.mEx.updateWidgetsWithNotificationChangedDelay(statusBarNotification, PeopleSpaceUtils.NotificationAction.POSTED);
        }

        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            PeopleSpaceWidgetManager.this.mEx.updateWidgetsWithNotificationChangedDelay(statusBarNotification, PeopleSpaceUtils.NotificationAction.REMOVED);
        }

        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            PeopleSpaceWidgetManager.this.mEx.updateWidgetsWithNotificationChangedDelay(statusBarNotification, PeopleSpaceUtils.NotificationAction.REMOVED);
        }

        public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            if (notificationChannel.isConversation()) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                peopleSpaceWidgetManager.updateWidgets(peopleSpaceWidgetManager.mAppWidgetManager.getAppWidgetIds(new ComponentName(PeopleSpaceWidgetManager.this.mContext, PeopleSpaceWidgetProvider.class)));
            }
        }
    };
    private final Object mLock = new Object();
    private PeopleSpaceWidgetManager mManager;
    private CommonNotifCollection mNotifCollection;
    private Map<String, Set<String>> mNotificationKeyToWidgetIdsMatchedByUri = new HashMap();
    private NotificationManager mNotificationManager;
    private PackageManager mPackageManager;
    private PeopleManager mPeopleManager;
    private boolean mRegisteredReceivers;
    private SharedPreferences mSharedPrefs;
    public UiEventLogger mUiEventLogger = new UiEventLoggerImpl();
    private UserManager mUserManager;

    @Inject
    public PeopleSpaceWidgetManager(Context context, LauncherApps launcherApps, CommonNotifCollection commonNotifCollection, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, NotificationManager notificationManager, BroadcastDispatcher broadcastDispatcher, @Background Executor executor) {
        this.mContext = context;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mLauncherApps = launcherApps;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPeopleManager = (PeopleManager) context.getSystemService(PeopleManager.class);
        this.mNotifCollection = commonNotifCollection;
        this.mPackageManager = packageManager;
        this.mINotificationManager = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = new BackupManager(context);
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBgExecutor = executor;
        this.mEx = new PeopleSpaceWidgetManagerEx(this);
    }

    public void init() {
        synchronized (this.mLock) {
            if (!this.mRegisteredReceivers) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED");
                intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
                intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
                intentFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
                intentFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
                intentFilter.addAction("android.intent.action.USER_UNLOCKED");
                this.mBroadcastDispatcher.registerReceiver(this.mBaseBroadcastReceiver, intentFilter, (Executor) null, UserHandle.ALL);
                IntentFilter intentFilter2 = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
                intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
                intentFilter2.addDataScheme("package");
                this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter2);
                IntentFilter intentFilter3 = new IntentFilter("android.intent.action.BOOT_COMPLETED");
                intentFilter3.setPriority(1000);
                this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter3);
                this.mRegisteredReceivers = true;
            }
        }
    }

    public class TileConversationListener implements PeopleManager.ConversationListener {
        public TileConversationListener() {
        }

        public void onConversationUpdate(ConversationChannel conversationChannel) {
            PeopleSpaceWidgetManager.this.mBgExecutor.execute(new C2300x16303646(this, conversationChannel));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConversationUpdate$0$com-android-systemui-people-widget-PeopleSpaceWidgetManager$TileConversationListener */
        public /* synthetic */ void mo35203x9b1799fc(ConversationChannel conversationChannel) {
            PeopleSpaceWidgetManager.this.updateWidgetsWithConversationChanged(conversationChannel);
        }
    }

    PeopleSpaceWidgetManager(Context context, AppWidgetManager appWidgetManager, IPeopleManager iPeopleManager, PeopleManager peopleManager, LauncherApps launcherApps, CommonNotifCollection commonNotifCollection, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, BackupManager backupManager, INotificationManager iNotificationManager, NotificationManager notificationManager, @Background Executor executor) {
        this.mContext = context;
        this.mAppWidgetManager = appWidgetManager;
        this.mIPeopleManager = iPeopleManager;
        this.mPeopleManager = peopleManager;
        this.mLauncherApps = launcherApps;
        this.mNotifCollection = commonNotifCollection;
        this.mPackageManager = packageManager;
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = backupManager;
        this.mINotificationManager = iNotificationManager;
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mBgExecutor = executor;
        this.mEx = new PeopleSpaceWidgetManagerEx(this);
    }

    public void updateWidgets(int[] iArr) {
        this.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda7(this, iArr));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWidgetsInBackground */
    public void mo35184x4693b0ae(int[] iArr) {
        try {
            if (iArr.length != 0) {
                synchronized (this.mLock) {
                    updateSingleConversationWidgets(iArr);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "failed to update widgets", e);
        }
    }

    public void updateSingleConversationWidgets(int[] iArr) {
        HashMap hashMap = new HashMap();
        for (int i : iArr) {
            PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
            if (tileForExistingWidget == null) {
                Log.e(TAG, "Matching conversation not found for shortcut ID");
            }
            mo35180x9a3cfb8a(i, tileForExistingWidget);
            hashMap.put(Integer.valueOf(i), tileForExistingWidget);
            if (tileForExistingWidget != null) {
                registerConversationListenerIfNeeded(i, new PeopleTileKey(tileForExistingWidget));
            }
        }
        PeopleSpaceUtils.getDataFromContactsOnBackgroundThread(this.mContext, this.mManager, hashMap, iArr);
    }

    private void updateAppWidgetViews(int i, PeopleSpaceTile peopleSpaceTile, Bundle bundle) {
        PeopleTileKey keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
        if (!PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
            Log.e(TAG, "Cannot update invalid widget");
            return;
        }
        this.mAppWidgetManager.updateAppWidget(i, PeopleTileViewHelper.createRemoteViews(this.mContext, peopleSpaceTile, i, bundle, keyFromStorageByWidgetId));
    }

    public void updateAppWidgetOptionsAndViewOptional(int i, Optional<PeopleSpaceTile> optional) {
        if (optional.isPresent()) {
            mo35180x9a3cfb8a(i, optional.get());
        }
    }

    /* renamed from: updateAppWidgetOptionsAndView */
    public void mo35180x9a3cfb8a(int i, PeopleSpaceTile peopleSpaceTile) {
        synchronized (mTiles) {
            mTiles.put(Integer.valueOf(i), peopleSpaceTile);
        }
        updateAppWidgetViews(i, peopleSpaceTile, this.mAppWidgetManager.getAppWidgetOptions(i));
    }

    public PeopleSpaceTile getTileForExistingWidget(int i) {
        try {
            return getTileForExistingWidgetThrowing(i);
        } catch (Exception e) {
            Log.e(TAG, "failed to retrieve tile for widget ID " + i, e);
            return null;
        }
    }

    private PeopleSpaceTile getTileForExistingWidgetThrowing(int i) throws PackageManager.NameNotFoundException {
        PeopleSpaceTile peopleSpaceTile;
        synchronized (mTiles) {
            peopleSpaceTile = mTiles.get(Integer.valueOf(i));
        }
        if (peopleSpaceTile != null) {
            return peopleSpaceTile;
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return getTileFromPersistentStorage(new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt(PeopleSpaceUtils.USER_ID, -1), sharedPreferences.getString("package_name", "")), i, true);
    }

    public PeopleSpaceTile getTileFromPersistentStorage(PeopleTileKey peopleTileKey, int i, boolean z) throws PackageManager.NameNotFoundException {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            Log.e(TAG, "PeopleTileKey invalid: " + peopleTileKey.toString());
            return null;
        }
        IPeopleManager iPeopleManager = this.mIPeopleManager;
        if (iPeopleManager == null || this.mLauncherApps == null) {
            Log.d(TAG, "System services are null");
            return null;
        }
        try {
            ConversationChannel conversation = iPeopleManager.getConversation(peopleTileKey.getPackageName(), peopleTileKey.getUserId(), peopleTileKey.getShortcutId());
            if (conversation == null) {
                return null;
            }
            PeopleSpaceTile.Builder builder = new PeopleSpaceTile.Builder(conversation, this.mLauncherApps);
            String string = this.mSharedPrefs.getString(String.valueOf(i), (String) null);
            if (z && string != null && builder.build().getContactUri() == null) {
                builder.setContactUri(Uri.parse(string));
            }
            return getTileWithCurrentState(builder.build(), "android.intent.action.BOOT_COMPLETED");
        } catch (RemoteException e) {
            Log.e(TAG, "getTileFromPersistentStorage failing", e);
            return null;
        }
    }

    public void updateWidgetsWithNotificationChanged(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        this.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda5(this, statusBarNotification, notificationAction, this.mNotifCollection.getAllNotifs()));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWidgetsWithNotificationChangedInBackground */
    public void mo35185xc690c0c0(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction, Collection<NotificationEntry> collection) {
        try {
            PeopleTileKey peopleTileKey = new PeopleTileKey(statusBarNotification.getShortcutId(), statusBarNotification.getUser().getIdentifier(), statusBarNotification.getPackageName());
            if (PeopleTileKey.isValid(peopleTileKey)) {
                if (this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class)).length == 0) {
                    Log.d(TAG, "No app widget ids returned");
                    return;
                }
                synchronized (this.mLock) {
                    Set<String> matchingKeyWidgetIds = getMatchingKeyWidgetIds(peopleTileKey);
                    matchingKeyWidgetIds.addAll(getMatchingUriWidgetIds(statusBarNotification, notificationAction));
                    updateWidgetIdsBasedOnNotifications(matchingKeyWidgetIds, collection);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "updateWidgetsWithNotificationChangedInBackground failing", e);
        }
    }

    private void updateWidgetIdsBasedOnNotifications(Set<String> set, Collection<NotificationEntry> collection) {
        if (!set.isEmpty()) {
            try {
                ((Map) set.stream().map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda1()).collect(Collectors.toMap(Function.identity(), new PeopleSpaceWidgetManager$$ExternalSyntheticLambda2(this, groupConversationNotifications(collection))))).forEach(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda3(this));
            } catch (Exception e) {
                Log.e(TAG, "updateWidgetIdsBasedOnNotifications failing", e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateWidgetIdsBasedOnNotifications$2$com-android-systemui-people-widget-PeopleSpaceWidgetManager */
    public /* synthetic */ Optional mo35182xafd3b74b(Map map, Integer num) {
        return getAugmentedTileForExistingWidget(num.intValue(), map);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateWidgetIdsBasedOnNotifications$3$com-android-systemui-people-widget-PeopleSpaceWidgetManager */
    public /* synthetic */ void mo35183x6a4957cc(Integer num, Optional optional) {
        updateAppWidgetOptionsAndViewOptional(num.intValue(), optional);
    }

    public PeopleSpaceTile augmentTileFromNotificationEntryManager(PeopleSpaceTile peopleSpaceTile, Optional<Integer> optional) {
        return augmentTileFromNotifications(peopleSpaceTile, new PeopleTileKey(peopleSpaceTile), peopleSpaceTile.getContactUri() != null ? peopleSpaceTile.getContactUri().toString() : null, groupConversationNotifications(this.mNotifCollection.getAllNotifs()), optional);
    }

    public Map<PeopleTileKey, Set<NotificationEntry>> groupConversationNotifications(Collection<NotificationEntry> collection) {
        return (Map) collection.stream().filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda13(this)).collect(Collectors.groupingBy(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda14(), Collectors.mapping(Function.identity(), Collectors.toSet())));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$groupConversationNotifications$4$com-android-systemui-people-widget-PeopleSpaceWidgetManager */
    public /* synthetic */ boolean mo35181x6a1f398c(NotificationEntry notificationEntry) {
        return NotificationHelper.isValid(notificationEntry) && NotificationHelper.isMissedCallOrHasContent(notificationEntry) && !NotificationHelper.shouldFilterOut(this.mBubblesOptional, notificationEntry);
    }

    public PeopleSpaceTile augmentTileFromNotifications(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey, String str, Map<PeopleTileKey, Set<NotificationEntry>> map, Optional<Integer> optional) {
        boolean z = this.mPackageManager.checkPermission("android.permission.READ_CONTACTS", peopleSpaceTile.getPackageName()) == 0;
        List<NotificationEntry> arrayList = new ArrayList<>();
        if (z) {
            arrayList = PeopleSpaceUtils.getNotificationsByUri(this.mPackageManager, str, map);
            arrayList.isEmpty();
        }
        Set set = map.get(peopleTileKey);
        if (set == null) {
            set = new HashSet();
        }
        if (set.isEmpty() && arrayList.isEmpty()) {
            return PeopleSpaceUtils.removeNotificationFields(peopleSpaceTile);
        }
        set.addAll(arrayList);
        return PeopleSpaceUtils.augmentTileFromNotification(this.mContext, peopleSpaceTile, peopleTileKey, NotificationHelper.getHighestPriorityNotification(set), PeopleSpaceUtils.getMessagesCount(set), optional, this.mBackupManager);
    }

    public Optional<PeopleSpaceTile> getAugmentedTileForExistingWidget(int i, Map<PeopleTileKey, Set<NotificationEntry>> map) {
        PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
        if (tileForExistingWidget == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(augmentTileFromNotifications(tileForExistingWidget, new PeopleTileKey(tileForExistingWidget), this.mSharedPrefs.getString(String.valueOf(i), (String) null), map, Optional.m1751of(Integer.valueOf(i))));
    }

    public Set<String> getMatchingKeyWidgetIds(PeopleTileKey peopleTileKey) {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            return new HashSet();
        }
        return new HashSet(this.mSharedPrefs.getStringSet(peopleTileKey.toString(), new HashSet()));
    }

    private Set<String> getMatchingUriWidgetIds(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        if (notificationAction.equals(PeopleSpaceUtils.NotificationAction.POSTED)) {
            Set<String> fetchMatchingUriWidgetIds = fetchMatchingUriWidgetIds(statusBarNotification);
            if (fetchMatchingUriWidgetIds != null && !fetchMatchingUriWidgetIds.isEmpty()) {
                this.mNotificationKeyToWidgetIdsMatchedByUri.put(statusBarNotification.getKey(), fetchMatchingUriWidgetIds);
                return fetchMatchingUriWidgetIds;
            }
        } else {
            Set<String> remove = this.mNotificationKeyToWidgetIdsMatchedByUri.remove(statusBarNotification.getKey());
            if (remove != null && !remove.isEmpty()) {
                return remove;
            }
        }
        return new HashSet();
    }

    private Set<String> fetchMatchingUriWidgetIds(StatusBarNotification statusBarNotification) {
        String contactUri;
        if (!NotificationHelper.shouldMatchNotificationByUri(statusBarNotification) || (contactUri = NotificationHelper.getContactUri(statusBarNotification)) == null) {
            return null;
        }
        HashSet hashSet = new HashSet(this.mSharedPrefs.getStringSet(contactUri, new HashSet()));
        if (hashSet.isEmpty()) {
            return null;
        }
        return hashSet;
    }

    public void updateWidgetsWithConversationChanged(ConversationChannel conversationChannel) {
        ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
        synchronized (this.mLock) {
            for (String parseInt : getMatchingKeyWidgetIds(new PeopleTileKey(shortcutInfo.getId(), shortcutInfo.getUserId(), shortcutInfo.getPackage()))) {
                updateStorageAndViewWithConversationData(conversationChannel, Integer.parseInt(parseInt));
            }
        }
    }

    private void updateStorageAndViewWithConversationData(ConversationChannel conversationChannel, int i) {
        PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
        if (tileForExistingWidget != null) {
            PeopleSpaceTile.Builder builder = tileForExistingWidget.toBuilder();
            ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
            Uri uri = null;
            if (shortcutInfo.getPersons() != null && shortcutInfo.getPersons().length > 0) {
                Person person = shortcutInfo.getPersons()[0];
                if (person.getUri() != null) {
                    uri = Uri.parse(person.getUri());
                }
            }
            CharSequence label = shortcutInfo.getLabel();
            if (label != null) {
                builder.setUserName(label);
            }
            Icon convertDrawableToIcon = PeopleSpaceTile.convertDrawableToIcon(this.mLauncherApps.getShortcutIconDrawable(shortcutInfo, 0));
            if (convertDrawableToIcon != null) {
                builder.setUserIcon(convertDrawableToIcon);
            }
            NotificationChannel notificationChannel = conversationChannel.getNotificationChannel();
            if (notificationChannel != null) {
                builder.setIsImportantConversation(notificationChannel.isImportantConversation());
            }
            builder.setContactUri(uri).setStatuses(conversationChannel.getStatuses()).setLastInteractionTimestamp(conversationChannel.getLastEventTimestamp());
            mo35180x9a3cfb8a(i, builder.build());
        }
    }

    public void attach(NotificationListener notificationListener) {
        notificationListener.addNotificationHandler(this.mListener);
    }

    public void onAppWidgetOptionsChanged(int i, Bundle bundle) {
        PeopleTileKey peopleTileKeyFromBundle = AppWidgetOptionsHelper.getPeopleTileKeyFromBundle(bundle);
        if (PeopleTileKey.isValid(peopleTileKeyFromBundle)) {
            AppWidgetOptionsHelper.removePeopleTileKey(this.mAppWidgetManager, i);
            addNewWidget(i, peopleTileKeyFromBundle);
        }
        updateWidgets(new int[]{i});
    }

    public void addNewWidget(int i, PeopleTileKey peopleTileKey) {
        PeopleTileKey keyFromStorageByWidgetId;
        try {
            PeopleSpaceTile tileFromPersistentStorage = getTileFromPersistentStorage(peopleTileKey, i, false);
            if (tileFromPersistentStorage != null) {
                PeopleSpaceTile augmentTileFromNotificationEntryManager = augmentTileFromNotificationEntryManager(tileFromPersistentStorage, Optional.m1751of(Integer.valueOf(i)));
                synchronized (this.mLock) {
                    keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
                }
                if (PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
                    deleteWidgets(new int[]{i});
                } else {
                    this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_ADDED);
                }
                synchronized (this.mLock) {
                    PeopleSpaceUtils.setSharedPreferencesStorageForTile(this.mContext, peopleTileKey, i, augmentTileFromNotificationEntryManager.getContactUri(), this.mBackupManager);
                }
                registerConversationListenerIfNeeded(i, peopleTileKey);
                try {
                    this.mLauncherApps.cacheShortcuts(augmentTileFromNotificationEntryManager.getPackageName(), Collections.singletonList(augmentTileFromNotificationEntryManager.getId()), augmentTileFromNotificationEntryManager.getUserHandle(), 2);
                } catch (Exception e) {
                    Log.w(TAG, "failed to cache shortcut", e);
                }
                this.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda4(this, i, augmentTileFromNotificationEntryManager));
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e(TAG, "Cannot add widget since app was uninstalled");
        }
    }

    public void registerConversationListenerIfNeeded(int i, PeopleTileKey peopleTileKey) {
        if (PeopleTileKey.isValid(peopleTileKey)) {
            TileConversationListener tileConversationListener = new TileConversationListener();
            synchronized (mListeners) {
                if (!mListeners.containsKey(peopleTileKey)) {
                    mListeners.put(peopleTileKey, tileConversationListener);
                    this.mPeopleManager.registerConversationListener(peopleTileKey.getPackageName(), peopleTileKey.getUserId(), peopleTileKey.getShortcutId(), tileConversationListener, this.mContext.getMainExecutor());
                }
            }
        }
    }

    private PeopleTileKey getKeyFromStorageByWidgetId(int i) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt(PeopleSpaceUtils.USER_ID, -1), sharedPreferences.getString("package_name", ""));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005e, code lost:
        r8 = r11.mLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0060, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        com.android.systemui.people.PeopleSpaceUtils.removeSharedPreferencesStorageForTile(r11.mContext, r6, r3, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0066, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006f, code lost:
        if (r5.contains(java.lang.String.valueOf(r3)) == false) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0076, code lost:
        if (r5.size() != 1) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0078, code lost:
        unregisterConversationListener(r6, r3);
        uncacheConversationShortcut(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007e, code lost:
        r2 = r2 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void deleteWidgets(int[] r12) {
        /*
            r11 = this;
            int r0 = r12.length
            r1 = 0
            r2 = r1
        L_0x0003:
            if (r2 >= r0) goto L_0x0087
            r3 = r12[r2]
            com.android.internal.logging.UiEventLogger r4 = r11.mUiEventLogger
            com.android.systemui.people.PeopleSpaceUtils$PeopleSpaceWidgetEvent r5 = com.android.systemui.people.PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_DELETED
            r4.log(r5)
            java.lang.Object r4 = r11.mLock
            monitor-enter(r4)
            android.content.Context r5 = r11.mContext     // Catch:{ all -> 0x0084 }
            java.lang.String r6 = java.lang.String.valueOf((int) r3)     // Catch:{ all -> 0x0084 }
            android.content.SharedPreferences r5 = r5.getSharedPreferences(r6, r1)     // Catch:{ all -> 0x0084 }
            com.android.systemui.people.widget.PeopleTileKey r6 = new com.android.systemui.people.widget.PeopleTileKey     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = "shortcut_id"
            r8 = 0
            java.lang.String r7 = r5.getString(r7, r8)     // Catch:{ all -> 0x0084 }
            java.lang.String r9 = "user_id"
            r10 = -1
            int r9 = r5.getInt(r9, r10)     // Catch:{ all -> 0x0084 }
            java.lang.String r10 = "package_name"
            java.lang.String r5 = r5.getString(r10, r8)     // Catch:{ all -> 0x0084 }
            r6.<init>(r7, r9, r5)     // Catch:{ all -> 0x0084 }
            boolean r5 = com.android.systemui.people.widget.PeopleTileKey.isValid(r6)     // Catch:{ all -> 0x0084 }
            if (r5 != 0) goto L_0x003f
            monitor-exit(r4)     // Catch:{ all -> 0x0084 }
            return
        L_0x003f:
            java.util.HashSet r5 = new java.util.HashSet     // Catch:{ all -> 0x0084 }
            android.content.SharedPreferences r7 = r11.mSharedPrefs     // Catch:{ all -> 0x0084 }
            java.lang.String r9 = r6.toString()     // Catch:{ all -> 0x0084 }
            java.util.HashSet r10 = new java.util.HashSet     // Catch:{ all -> 0x0084 }
            r10.<init>()     // Catch:{ all -> 0x0084 }
            java.util.Set r7 = r7.getStringSet(r9, r10)     // Catch:{ all -> 0x0084 }
            r5.<init>(r7)     // Catch:{ all -> 0x0084 }
            android.content.SharedPreferences r7 = r11.mSharedPrefs     // Catch:{ all -> 0x0084 }
            java.lang.String r9 = java.lang.String.valueOf((int) r3)     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = r7.getString(r9, r8)     // Catch:{ all -> 0x0084 }
            monitor-exit(r4)     // Catch:{ all -> 0x0084 }
            java.lang.Object r8 = r11.mLock
            monitor-enter(r8)
            android.content.Context r4 = r11.mContext     // Catch:{ all -> 0x0081 }
            com.android.systemui.people.PeopleSpaceUtils.removeSharedPreferencesStorageForTile(r4, r6, r3, r7)     // Catch:{ all -> 0x0081 }
            monitor-exit(r8)     // Catch:{ all -> 0x0081 }
            java.lang.String r4 = java.lang.String.valueOf((int) r3)
            boolean r4 = r5.contains(r4)
            if (r4 == 0) goto L_0x007e
            int r4 = r5.size()
            r5 = 1
            if (r4 != r5) goto L_0x007e
            r11.unregisterConversationListener(r6, r3)
            r11.uncacheConversationShortcut(r6)
        L_0x007e:
            int r2 = r2 + 1
            goto L_0x0003
        L_0x0081:
            r11 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0081 }
            throw r11
        L_0x0084:
            r11 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0084 }
            throw r11
        L_0x0087:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.deleteWidgets(int[]):void");
    }

    private void unregisterConversationListener(PeopleTileKey peopleTileKey, int i) {
        synchronized (mListeners) {
            TileConversationListener tileConversationListener = mListeners.get(peopleTileKey);
            if (tileConversationListener != null) {
                mListeners.remove(peopleTileKey);
                this.mPeopleManager.unregisterConversationListener(tileConversationListener);
            }
        }
    }

    private void uncacheConversationShortcut(PeopleTileKey peopleTileKey) {
        try {
            this.mLauncherApps.uncacheShortcuts(peopleTileKey.getPackageName(), Collections.singletonList(peopleTileKey.getShortcutId()), UserHandle.of(peopleTileKey.getUserId()), 2);
        } catch (Exception e) {
            Log.d(TAG, "failed to uncache shortcut", e);
        }
    }

    public boolean requestPinAppWidget(ShortcutInfo shortcutInfo, Bundle bundle) {
        RemoteViews preview = getPreview(shortcutInfo.getId(), shortcutInfo.getUserHandle(), shortcutInfo.getPackage(), bundle);
        if (preview == null) {
            Log.w(TAG, "Skipping pinning widget: no tile for shortcutId: " + shortcutInfo.getId());
            return false;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("appWidgetPreview", preview);
        PendingIntent pendingIntent = PeopleSpaceWidgetPinnedReceiver.getPendingIntent(this.mContext, shortcutInfo);
        return this.mAppWidgetManager.requestPinAppWidget(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class), bundle2, pendingIntent);
    }

    public List<PeopleSpaceTile> getPriorityTiles() throws Exception {
        return PeopleSpaceUtils.getSortedTiles(this.mIPeopleManager, this.mLauncherApps, this.mUserManager, this.mINotificationManager.getConversations(true).getList().stream().filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda0()).map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda6()));
    }

    static /* synthetic */ boolean lambda$getPriorityTiles$6(ConversationChannelWrapper conversationChannelWrapper) {
        return conversationChannelWrapper.getNotificationChannel() != null && conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public List<PeopleSpaceTile> getRecentTiles() throws Exception {
        return PeopleSpaceUtils.getSortedTiles(this.mIPeopleManager, this.mLauncherApps, this.mUserManager, Stream.concat(this.mINotificationManager.getConversations(false).getList().stream().filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda8()).map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda9()), this.mIPeopleManager.getRecentConversations().getList().stream().map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda10())));
    }

    static /* synthetic */ boolean lambda$getRecentTiles$8(ConversationChannelWrapper conversationChannelWrapper) {
        return conversationChannelWrapper.getNotificationChannel() == null || !conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public RemoteViews getPreview(String str, UserHandle userHandle, String str2, Bundle bundle) {
        try {
            PeopleSpaceTile tile = PeopleSpaceUtils.getTile(this.mIPeopleManager.getConversation(str2, userHandle.getIdentifier(), str), this.mLauncherApps);
            if (tile == null) {
                return null;
            }
            PeopleSpaceTile augmentTileFromNotificationEntryManager = augmentTileFromNotificationEntryManager(tile, Optional.empty());
            return PeopleTileViewHelper.createRemoteViews(this.mContext, augmentTileFromNotificationEntryManager, 0, bundle, new PeopleTileKey(augmentTileFromNotificationEntryManager));
        } catch (Exception e) {
            Log.w(TAG, "failed to get conversation or tile", e);
            return null;
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    void updateWidgetsFromBroadcastInBackground(java.lang.String r10) {
        /*
            r9 = this;
            android.appwidget.AppWidgetManager r0 = r9.mAppWidgetManager
            android.content.ComponentName r1 = new android.content.ComponentName
            android.content.Context r2 = r9.mContext
            java.lang.Class<com.android.systemui.people.widget.PeopleSpaceWidgetProvider> r3 = com.android.systemui.people.widget.PeopleSpaceWidgetProvider.class
            r1.<init>(r2, r3)
            int[] r0 = r0.getAppWidgetIds(r1)
            if (r0 != 0) goto L_0x0012
            return
        L_0x0012:
            int r1 = r0.length
            r2 = 0
            r3 = r2
        L_0x0015:
            if (r3 >= r1) goto L_0x006c
            r4 = r0[r3]
            r5 = 0
            java.lang.Object r6 = r9.mLock     // Catch:{ NameNotFoundException -> 0x0038 }
            monitor-enter(r6)     // Catch:{ NameNotFoundException -> 0x0038 }
            android.app.people.PeopleSpaceTile r7 = r9.getTileForExistingWidgetThrowing(r4)     // Catch:{ all -> 0x0035 }
            if (r7 != 0) goto L_0x002c
            java.lang.String r7 = "PeopleSpaceWidgetMgr"
            java.lang.String r8 = "Matching conversation not found for shortcut ID"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x0035 }
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            goto L_0x0066
        L_0x002c:
            android.app.people.PeopleSpaceTile r5 = r9.getTileWithCurrentState(r7, r10)     // Catch:{ all -> 0x0035 }
            r9.mo35180x9a3cfb8a(r4, r5)     // Catch:{ all -> 0x0035 }
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            goto L_0x0066
        L_0x0035:
            r7 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            throw r7     // Catch:{ NameNotFoundException -> 0x0038 }
        L_0x0038:
            r6 = move-exception
            java.lang.String r7 = "PeopleSpaceWidgetMgr"
            java.lang.String r8 = "package no longer found for tile"
            android.util.Log.e(r7, r8, r6)
            android.content.Context r6 = r9.mContext
            java.lang.Class<android.app.job.JobScheduler> r7 = android.app.job.JobScheduler.class
            java.lang.Object r6 = r6.getSystemService(r7)
            android.app.job.JobScheduler r6 = (android.app.job.JobScheduler) r6
            if (r6 == 0) goto L_0x0057
            r7 = 74823873(0x475b8c1, float:2.8884446E-36)
            android.app.job.JobInfo r6 = r6.getPendingJob(r7)
            if (r6 == 0) goto L_0x0057
            goto L_0x0066
        L_0x0057:
            java.lang.Object r6 = r9.mLock
            monitor-enter(r6)
            r9.mo35180x9a3cfb8a(r4, r5)     // Catch:{ all -> 0x0069 }
            monitor-exit(r6)     // Catch:{ all -> 0x0069 }
            r5 = 1
            int[] r5 = new int[r5]
            r5[r2] = r4
            r9.deleteWidgets(r5)
        L_0x0066:
            int r3 = r3 + 1
            goto L_0x0015
        L_0x0069:
            r9 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0069 }
            throw r9
        L_0x006c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.updateWidgetsFromBroadcastInBackground(java.lang.String):void");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.app.people.PeopleSpaceTile getTileWithCurrentState(android.app.people.PeopleSpaceTile r3, java.lang.String r4) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
            r2 = this;
            android.app.people.PeopleSpaceTile$Builder r0 = r3.toBuilder()
            int r1 = r4.hashCode()
            switch(r1) {
                case -1238404651: goto L_0x0052;
                case -1001645458: goto L_0x0048;
                case -864107122: goto L_0x003e;
                case -19011148: goto L_0x0034;
                case 798292259: goto L_0x002a;
                case 833559602: goto L_0x0020;
                case 1290767157: goto L_0x0016;
                case 2106958107: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x005c
        L_0x000c:
            java.lang.String r1 = "android.app.action.INTERRUPTION_FILTER_CHANGED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 0
            goto L_0x005d
        L_0x0016:
            java.lang.String r1 = "android.intent.action.PACKAGES_UNSUSPENDED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 2
            goto L_0x005d
        L_0x0020:
            java.lang.String r1 = "android.intent.action.USER_UNLOCKED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 5
            goto L_0x005d
        L_0x002a:
            java.lang.String r1 = "android.intent.action.BOOT_COMPLETED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 7
            goto L_0x005d
        L_0x0034:
            java.lang.String r1 = "android.intent.action.LOCALE_CHANGED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 6
            goto L_0x005d
        L_0x003e:
            java.lang.String r1 = "android.intent.action.MANAGED_PROFILE_AVAILABLE"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 3
            goto L_0x005d
        L_0x0048:
            java.lang.String r1 = "android.intent.action.PACKAGES_SUSPENDED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 1
            goto L_0x005d
        L_0x0052:
            java.lang.String r1 = "android.intent.action.MANAGED_PROFILE_UNAVAILABLE"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 4
            goto L_0x005d
        L_0x005c:
            r4 = -1
        L_0x005d:
            switch(r4) {
                case 0: goto L_0x0088;
                case 1: goto L_0x0080;
                case 2: goto L_0x0080;
                case 3: goto L_0x0078;
                case 4: goto L_0x0078;
                case 5: goto L_0x0078;
                case 6: goto L_0x008f;
                default: goto L_0x0060;
            }
        L_0x0060:
            boolean r4 = r2.getUserQuieted(r3)
            android.app.people.PeopleSpaceTile$Builder r4 = r0.setIsUserQuieted(r4)
            boolean r3 = r2.getPackageSuspended(r3)
            android.app.people.PeopleSpaceTile$Builder r3 = r4.setIsPackageSuspended(r3)
            int r2 = r2.getNotificationPolicyState()
            r3.setNotificationPolicyState(r2)
            goto L_0x008f
        L_0x0078:
            boolean r2 = r2.getUserQuieted(r3)
            r0.setIsUserQuieted(r2)
            goto L_0x008f
        L_0x0080:
            boolean r2 = r2.getPackageSuspended(r3)
            r0.setIsPackageSuspended(r2)
            goto L_0x008f
        L_0x0088:
            int r2 = r2.getNotificationPolicyState()
            r0.setNotificationPolicyState(r2)
        L_0x008f:
            android.app.people.PeopleSpaceTile r2 = r0.build()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.getTileWithCurrentState(android.app.people.PeopleSpaceTile, java.lang.String):android.app.people.PeopleSpaceTile");
    }

    private boolean getPackageSuspended(PeopleSpaceTile peopleSpaceTile) throws PackageManager.NameNotFoundException {
        boolean z = !TextUtils.isEmpty(peopleSpaceTile.getPackageName()) && this.mPackageManager.isPackageSuspended(peopleSpaceTile.getPackageName());
        this.mPackageManager.getApplicationInfoAsUser(peopleSpaceTile.getPackageName(), 128, PeopleSpaceUtils.getUserId(peopleSpaceTile));
        return z;
    }

    private boolean getUserQuieted(PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile.getUserHandle() != null && this.mUserManager.isQuietModeEnabled(peopleSpaceTile.getUserHandle());
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getNotificationPolicyState() {
        /*
            r4 = this;
            android.app.NotificationManager r0 = r4.mNotificationManager
            android.app.NotificationManager$Policy r0 = r0.getNotificationPolicy()
            int r1 = r0.suppressedVisualEffects
            boolean r1 = android.app.NotificationManager.Policy.areAllVisualEffectsSuppressed(r1)
            r2 = 1
            if (r1 != 0) goto L_0x0010
            return r2
        L_0x0010:
            android.app.NotificationManager r4 = r4.mNotificationManager
            int r4 = r4.getCurrentInterruptionFilter()
            if (r4 == r2) goto L_0x0047
            r1 = 2
            if (r4 == r1) goto L_0x001c
            goto L_0x0046
        L_0x001c:
            boolean r4 = r0.allowConversations()
            if (r4 == 0) goto L_0x002d
            int r4 = r0.priorityConversationSenders
            if (r4 != r2) goto L_0x0027
            return r2
        L_0x0027:
            int r4 = r0.priorityConversationSenders
            if (r4 != r1) goto L_0x002d
            r4 = 4
            goto L_0x002e
        L_0x002d:
            r4 = 0
        L_0x002e:
            boolean r3 = r0.allowMessages()
            if (r3 == 0) goto L_0x0043
            int r0 = r0.allowMessagesFrom()
            if (r0 == r2) goto L_0x0040
            if (r0 == r1) goto L_0x003d
            return r2
        L_0x003d:
            r4 = r4 | 8
            return r4
        L_0x0040:
            r4 = r4 | 16
            return r4
        L_0x0043:
            if (r4 == 0) goto L_0x0046
            return r4
        L_0x0046:
            return r1
        L_0x0047:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.getNotificationPolicyState():int");
    }

    public void remapWidgets(int[] iArr, int[] iArr2) {
        HashMap hashMap = new HashMap();
        for (int i = 0; i < iArr.length; i++) {
            hashMap.put(String.valueOf(iArr[i]), String.valueOf(iArr2[i]));
        }
        remapWidgetFiles(hashMap);
        remapSharedFile(hashMap);
        remapFollowupFile(hashMap);
        int[] appWidgetIds = this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class));
        Bundle bundle = new Bundle();
        bundle.putBoolean("appWidgetRestoreCompleted", true);
        for (int updateAppWidgetOptions : appWidgetIds) {
            this.mAppWidgetManager.updateAppWidgetOptions(updateAppWidgetOptions, bundle);
        }
        updateWidgets(appWidgetIds);
    }

    public void remapWidgetFiles(Map<String, String> map) {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            String valueOf = String.valueOf(next.getKey());
            String valueOf2 = String.valueOf(next.getValue());
            if (!Objects.equals(valueOf, valueOf2)) {
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(valueOf, 0);
                PeopleTileKey peopleTileKey = SharedPreferencesHelper.getPeopleTileKey(sharedPreferences);
                if (PeopleTileKey.isValid(peopleTileKey)) {
                    hashMap.put(valueOf2, peopleTileKey);
                    SharedPreferencesHelper.clear(sharedPreferences);
                }
            }
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            SharedPreferencesHelper.setPeopleTileKey(this.mContext.getSharedPreferences((String) entry.getKey(), 0), (PeopleTileKey) entry.getValue());
        }
    }

    public void remapSharedFile(Map<String, String> map) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        for (Map.Entry next : defaultSharedPreferences.getAll().entrySet()) {
            String str = (String) next.getKey();
            int i = C22993.f325xd18c5f3a[PeopleBackupHelper.getEntryType(next).ordinal()];
            if (i == 1) {
                String str2 = map.get(str);
                if (TextUtils.isEmpty(str2)) {
                    Log.w(TAG, "Key is widget id without matching new id, skipping: " + str);
                } else {
                    try {
                        edit.putString(str2, (String) next.getValue());
                    } catch (Exception e) {
                        Log.e(TAG, "malformed entry value: " + next.getValue(), e);
                    }
                    edit.remove(str);
                }
            } else if (i == 2 || i == 3) {
                try {
                    edit.putStringSet(str, getNewWidgets((Set) next.getValue(), map));
                } catch (Exception e2) {
                    Log.e(TAG, "malformed entry value: " + next.getValue(), e2);
                    edit.remove(str);
                }
            } else if (i == 4) {
                Log.e(TAG, "Key not identified:" + str);
            }
        }
        edit.apply();
    }

    /* renamed from: com.android.systemui.people.widget.PeopleSpaceWidgetManager$3 */
    static /* synthetic */ class C22993 {

        /* renamed from: $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType */
        static final /* synthetic */ int[] f325xd18c5f3a;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType[] r0 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f325xd18c5f3a = r0
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.WIDGET_ID     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f325xd18c5f3a     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.PEOPLE_TILE_KEY     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f325xd18c5f3a     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.CONTACT_URI     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f325xd18c5f3a     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.C22993.<clinit>():void");
        }
    }

    public void remapFollowupFile(Map<String, String> map) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(PeopleBackupFollowUpJob.SHARED_FOLLOW_UP, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for (Map.Entry next : sharedPreferences.getAll().entrySet()) {
            String str = (String) next.getKey();
            try {
                edit.putStringSet(str, getNewWidgets((Set) next.getValue(), map));
            } catch (Exception e) {
                Log.e(TAG, "malformed entry value: " + next.getValue(), e);
                edit.remove(str);
            }
        }
        edit.apply();
    }

    private Set<String> getNewWidgets(Set<String> set, Map<String, String> map) {
        Stream<String> stream = set.stream();
        Objects.requireNonNull(map);
        return (Set) stream.map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda11(map)).filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda12()).collect(Collectors.toSet());
    }

    static /* synthetic */ boolean lambda$getNewWidgets$11(String str) {
        return !TextUtils.isEmpty(str);
    }
}
