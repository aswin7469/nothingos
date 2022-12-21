package com.android.systemui.statusbar.notification.collection;

import android.app.NotificationChannel;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.Pair;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coalescer.CoalescedEvent;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.notifcollection.BindEntryEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.ChannelChangedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.CleanUpEntryEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.CollectionReadyForBuildListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.EntryAddedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.EntryRemovedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.EntryUpdatedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.InitEntryEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.InternalNotifUpdater;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLoggerKt;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.notifcollection.RankingAppliedEvent;
import com.android.systemui.statusbar.notification.collection.notifcollection.RankingUpdatedEvent;
import com.android.systemui.util.Assert;
import com.android.systemui.util.time.SystemClock;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@SysUISingleton
public class NotifCollection implements Dumpable {
    private static final long INITIALIZATION_FORGIVENESS_WINDOW = TimeUnit.SECONDS.toMillis(5);
    static final int REASON_NOT_CANCELED = -1;
    public static final int REASON_UNKNOWN = 0;
    private static final String TAG = "NotifCollection";
    private boolean mAmDispatchingToOtherCode;
    private boolean mAttached = false;
    private CollectionReadyForBuildListener mBuildListener;
    private final SystemClock mClock;
    private final List<NotifDismissInterceptor> mDismissInterceptors = new ArrayList();
    private final DumpManager mDumpManager;
    private final LogBufferEulogizer mEulogizer;
    private Queue<NotifEvent> mEventQueue = new ArrayDeque();
    /* access modifiers changed from: private */
    public final HashMap<String, FutureDismissal> mFutureDismissals = new HashMap<>();
    private long mInitializedTimestamp = 0;
    private final List<NotifLifetimeExtender> mLifetimeExtenders = new ArrayList();
    /* access modifiers changed from: private */
    public final NotifCollectionLogger mLogger;
    private final Handler mMainHandler;
    private final List<NotifCollectionListener> mNotifCollectionListeners = new ArrayList();
    private final GroupCoalescer.BatchableNotificationHandler mNotifHandler = new GroupCoalescer.BatchableNotificationHandler() {
        public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            NotifCollection.this.onNotificationPosted(statusBarNotification, rankingMap);
        }

        public void onNotificationBatchPosted(List<CoalescedEvent> list) {
            NotifCollection.this.onNotificationGroupPosted(list);
        }

        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            NotifCollection.this.onNotificationRemoved(statusBarNotification, rankingMap, 0);
        }

        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            NotifCollection.this.onNotificationRemoved(statusBarNotification, rankingMap, i);
        }

        public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            NotifCollection.this.onNotificationRankingUpdate(rankingMap);
        }

        public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            NotifCollection.this.onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }

        public void onNotificationsInitialized() {
            NotifCollection.this.onNotificationsInitialized();
        }
    };
    private final NotifPipelineFlags mNotifPipelineFlags;
    private final Map<String, NotificationEntry> mNotificationSet;
    private final Collection<NotificationEntry> mReadOnlyNotificationSet;
    private final IStatusBarService mStatusBarService;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CancellationReason {
    }

    public interface DismissedByUserStatsCreator {
        DismissedByUserStats createDismissedByUserStats(NotificationEntry notificationEntry);
    }

    @Inject
    public NotifCollection(IStatusBarService iStatusBarService, SystemClock systemClock, NotifPipelineFlags notifPipelineFlags, NotifCollectionLogger notifCollectionLogger, @Main Handler handler, LogBufferEulogizer logBufferEulogizer, DumpManager dumpManager) {
        ArrayMap arrayMap = new ArrayMap();
        this.mNotificationSet = arrayMap;
        this.mReadOnlyNotificationSet = Collections.unmodifiableCollection(arrayMap.values());
        this.mStatusBarService = iStatusBarService;
        this.mClock = systemClock;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mLogger = notifCollectionLogger;
        this.mMainHandler = handler;
        this.mEulogizer = logBufferEulogizer;
        this.mDumpManager = dumpManager;
    }

    public void attach(GroupCoalescer groupCoalescer) {
        Assert.isMainThread();
        if (!this.mAttached) {
            this.mAttached = true;
            this.mDumpManager.registerDumpable(TAG, this);
            groupCoalescer.setNotificationHandler(this.mNotifHandler);
            return;
        }
        throw new RuntimeException("attach() called twice");
    }

    /* access modifiers changed from: package-private */
    public void setBuildListener(CollectionReadyForBuildListener collectionReadyForBuildListener) {
        Assert.isMainThread();
        this.mBuildListener = collectionReadyForBuildListener;
    }

    /* access modifiers changed from: package-private */
    public NotificationEntry getEntry(String str) {
        return this.mNotificationSet.get(str);
    }

    /* access modifiers changed from: package-private */
    public Collection<NotificationEntry> getAllNotifs() {
        Assert.isMainThread();
        return this.mReadOnlyNotificationSet;
    }

    /* access modifiers changed from: package-private */
    public void addCollectionListener(NotifCollectionListener notifCollectionListener) {
        Assert.isMainThread();
        this.mNotifCollectionListeners.add(notifCollectionListener);
    }

    /* access modifiers changed from: package-private */
    public void removeCollectionListener(NotifCollectionListener notifCollectionListener) {
        Assert.isMainThread();
        this.mNotifCollectionListeners.remove((Object) notifCollectionListener);
    }

    /* access modifiers changed from: package-private */
    public void addNotificationLifetimeExtender(NotifLifetimeExtender notifLifetimeExtender) {
        Assert.isMainThread();
        checkForReentrantCall();
        if (!this.mLifetimeExtenders.contains(notifLifetimeExtender)) {
            this.mLifetimeExtenders.add(notifLifetimeExtender);
            notifLifetimeExtender.setCallback(new NotifCollection$$ExternalSyntheticLambda5(this));
            return;
        }
        throw new IllegalArgumentException("Extender " + notifLifetimeExtender + " already added.");
    }

    /* access modifiers changed from: package-private */
    public void addNotificationDismissInterceptor(NotifDismissInterceptor notifDismissInterceptor) {
        Assert.isMainThread();
        checkForReentrantCall();
        if (!this.mDismissInterceptors.contains(notifDismissInterceptor)) {
            this.mDismissInterceptors.add(notifDismissInterceptor);
            notifDismissInterceptor.setCallback(new NotifCollection$$ExternalSyntheticLambda6(this));
            return;
        }
        throw new IllegalArgumentException("Interceptor " + notifDismissInterceptor + " already added.");
    }

    public void dismissNotifications(List<Pair<NotificationEntry, DismissedByUserStats>> list) {
        Assert.isMainThread();
        checkForReentrantCall();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            NotificationEntry notificationEntry = (NotificationEntry) list.get(i).first;
            DismissedByUserStats dismissedByUserStats = (DismissedByUserStats) list.get(i).second;
            Objects.requireNonNull(dismissedByUserStats);
            NotificationEntry notificationEntry2 = this.mNotificationSet.get(notificationEntry.getKey());
            if (notificationEntry2 == null) {
                this.mLogger.logNonExistentNotifDismissed(notificationEntry.getKey());
            } else if (notificationEntry != notificationEntry2) {
                throw ((IllegalStateException) this.mEulogizer.record(new IllegalStateException("Invalid entry: different stored and dismissed entries for " + notificationEntry.getKey())));
            } else if (notificationEntry.getDismissState() != NotificationEntry.DismissState.DISMISSED) {
                updateDismissInterceptors(notificationEntry);
                if (isDismissIntercepted(notificationEntry)) {
                    this.mLogger.logNotifDismissedIntercepted(notificationEntry.getKey());
                } else {
                    arrayList.add(notificationEntry);
                    if (!isCanceled(notificationEntry)) {
                        try {
                            this.mStatusBarService.onNotificationClear(notificationEntry.getSbn().getPackageName(), notificationEntry.getSbn().getUser().getIdentifier(), notificationEntry.getSbn().getKey(), dismissedByUserStats.dismissalSurface, dismissedByUserStats.dismissalSentiment, dismissedByUserStats.notificationVisibility);
                        } catch (RemoteException e) {
                            this.mLogger.logRemoteExceptionOnNotificationClear(notificationEntry.getKey(), e);
                        }
                    }
                }
            }
        }
        locallyDismissNotifications(arrayList);
        dispatchEventsAndRebuildList();
    }

    public void dismissNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats) {
        dismissNotifications(List.m1723of(new Pair(notificationEntry, dismissedByUserStats)));
    }

    public void dismissAllNotifications(int i) {
        Assert.isMainThread();
        checkForReentrantCall();
        this.mLogger.logDismissAll(i);
        try {
            this.mStatusBarService.onClearAllNotifications(i);
        } catch (RemoteException e) {
            this.mLogger.logRemoteExceptionOnClearAllNotifications(e);
        }
        ArrayList arrayList = new ArrayList(getAllNotifs());
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            NotificationEntry notificationEntry = (NotificationEntry) arrayList.get(size);
            if (!shouldDismissOnClearAll(notificationEntry, i)) {
                updateDismissInterceptors(notificationEntry);
                if (isDismissIntercepted(notificationEntry)) {
                    this.mLogger.logNotifClearAllDismissalIntercepted(notificationEntry.getKey());
                }
                arrayList.remove(size);
            }
        }
        locallyDismissNotifications(arrayList);
        dispatchEventsAndRebuildList();
    }

    private void locallyDismissNotifications(List<NotificationEntry> list) {
        ArrayList<NotificationEntry> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            NotificationEntry notificationEntry = list.get(i);
            notificationEntry.setDismissState(NotificationEntry.DismissState.DISMISSED);
            this.mLogger.logNotifDismissed(notificationEntry.getKey());
            if (isCanceled(notificationEntry)) {
                arrayList.add(notificationEntry);
            } else if (notificationEntry.getSbn().getNotification().isGroupSummary()) {
                for (NotificationEntry next : this.mNotificationSet.values()) {
                    if (shouldAutoDismissChildren(next, notificationEntry.getSbn().getGroupKey())) {
                        next.setDismissState(NotificationEntry.DismissState.PARENT_DISMISSED);
                        this.mLogger.logChildDismissed(next);
                        if (isCanceled(next)) {
                            arrayList.add(next);
                        }
                    }
                }
            }
        }
        for (NotificationEntry notificationEntry2 : arrayList) {
            this.mLogger.logDismissOnAlreadyCanceledEntry(notificationEntry2);
            tryRemoveNotification(notificationEntry2);
        }
    }

    /* access modifiers changed from: private */
    public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        Assert.isMainThread();
        postNotification(statusBarNotification, requireRanking(rankingMap, statusBarNotification.getKey()));
        applyRanking(rankingMap);
        dispatchEventsAndRebuildList();
    }

    /* access modifiers changed from: private */
    public void onNotificationGroupPosted(List<CoalescedEvent> list) {
        Assert.isMainThread();
        this.mLogger.logNotifGroupPosted(list.get(0).getSbn().getGroupKey(), list.size());
        for (CoalescedEvent next : list) {
            postNotification(next.getSbn(), next.getRanking());
        }
        dispatchEventsAndRebuildList();
    }

    /* access modifiers changed from: private */
    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        Assert.isMainThread();
        this.mLogger.logNotifRemoved(statusBarNotification.getKey(), i);
        NotificationEntry notificationEntry = this.mNotificationSet.get(statusBarNotification.getKey());
        if (notificationEntry == null) {
            this.mLogger.logNoNotificationToRemoveWithKey(statusBarNotification.getKey(), i);
            return;
        }
        notificationEntry.mCancellationReason = i;
        tryRemoveNotification(notificationEntry);
        applyRanking(rankingMap);
        dispatchEventsAndRebuildList();
    }

    /* access modifiers changed from: private */
    public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        Assert.isMainThread();
        this.mEventQueue.add(new RankingUpdatedEvent(rankingMap));
        applyRanking(rankingMap);
        dispatchEventsAndRebuildList();
    }

    /* access modifiers changed from: private */
    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        Assert.isMainThread();
        this.mEventQueue.add(new ChannelChangedEvent(str, userHandle, notificationChannel, i));
        dispatchEventsAndRebuildList();
    }

    /* access modifiers changed from: private */
    public void onNotificationsInitialized() {
        this.mInitializedTimestamp = this.mClock.uptimeMillis();
    }

    private void postNotification(StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking) {
        NotificationEntry notificationEntry = this.mNotificationSet.get(statusBarNotification.getKey());
        if (notificationEntry == null) {
            NotificationEntry notificationEntry2 = new NotificationEntry(statusBarNotification, ranking, this.mClock.uptimeMillis());
            this.mEventQueue.add(new InitEntryEvent(notificationEntry2));
            this.mEventQueue.add(new BindEntryEvent(notificationEntry2, statusBarNotification));
            this.mNotificationSet.put(statusBarNotification.getKey(), notificationEntry2);
            this.mLogger.logNotifPosted(statusBarNotification.getKey());
            this.mEventQueue.add(new EntryAddedEvent(notificationEntry2));
            return;
        }
        cancelLocalDismissal(notificationEntry);
        cancelLifetimeExtension(notificationEntry);
        cancelDismissInterception(notificationEntry);
        notificationEntry.mCancellationReason = -1;
        notificationEntry.setSbn(statusBarNotification);
        this.mEventQueue.add(new BindEntryEvent(notificationEntry, statusBarNotification));
        this.mLogger.logNotifUpdated(statusBarNotification.getKey());
        this.mEventQueue.add(new EntryUpdatedEvent(notificationEntry, true));
    }

    private boolean tryRemoveNotification(NotificationEntry notificationEntry) {
        if (this.mNotificationSet.get(notificationEntry.getKey()) != notificationEntry) {
            throw ((IllegalStateException) this.mEulogizer.record(new IllegalStateException("No notification to remove with key " + notificationEntry.getKey())));
        } else if (isCanceled(notificationEntry)) {
            if (cannotBeLifetimeExtended(notificationEntry)) {
                cancelLifetimeExtension(notificationEntry);
            } else {
                updateLifetimeExtension(notificationEntry);
            }
            if (isLifetimeExtended(notificationEntry)) {
                return false;
            }
            this.mLogger.logNotifReleased(notificationEntry.getKey());
            this.mNotificationSet.remove(notificationEntry.getKey());
            cancelDismissInterception(notificationEntry);
            this.mEventQueue.add(new EntryRemovedEvent(notificationEntry, notificationEntry.mCancellationReason));
            this.mEventQueue.add(new CleanUpEntryEvent(notificationEntry));
            handleFutureDismissal(notificationEntry);
            return true;
        } else {
            throw ((IllegalStateException) this.mEulogizer.record(new IllegalStateException("Cannot remove notification " + notificationEntry.getKey() + ": has not been marked for removal")));
        }
    }

    public NotificationEntry getGroupSummary(String str) {
        return this.mNotificationSet.values().stream().filter(new NotifCollection$$ExternalSyntheticLambda3(str)).filter(new NotifCollection$$ExternalSyntheticLambda4()).findFirst().orElse(null);
    }

    public boolean isOnlyChildInGroup(NotificationEntry notificationEntry) {
        return this.mNotificationSet.get(notificationEntry.getKey()) == notificationEntry && this.mNotificationSet.values().stream().filter(new NotifCollection$$ExternalSyntheticLambda1(notificationEntry.getSbn().getGroupKey())).filter(new NotifCollection$$ExternalSyntheticLambda2()).count() == 1;
    }

    static /* synthetic */ boolean lambda$isOnlyChildInGroup$3(NotificationEntry notificationEntry) {
        return !notificationEntry.getSbn().getNotification().isGroupSummary();
    }

    private void applyRanking(NotificationListenerService.RankingMap rankingMap) {
        for (NotificationEntry next : this.mNotificationSet.values()) {
            if (!isCanceled(next)) {
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                if (rankingMap.getRanking(next.getKey(), ranking)) {
                    next.setRanking(ranking);
                    if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
                        String overrideGroupKey = ranking.getOverrideGroupKey();
                        if (!Objects.equals(next.getSbn().getOverrideGroupKey(), overrideGroupKey)) {
                            next.getSbn().setOverrideGroupKey(overrideGroupKey);
                        }
                    }
                } else {
                    this.mLogger.logRankingMissing(next.getKey(), rankingMap);
                }
            }
        }
        this.mEventQueue.add(new RankingAppliedEvent());
    }

    private void dispatchEventsAndRebuildList() {
        Trace.beginSection("NotifCollection.dispatchEventsAndRebuildList");
        this.mAmDispatchingToOtherCode = true;
        while (!this.mEventQueue.isEmpty()) {
            this.mEventQueue.remove().dispatchTo(this.mNotifCollectionListeners);
        }
        this.mAmDispatchingToOtherCode = false;
        CollectionReadyForBuildListener collectionReadyForBuildListener = this.mBuildListener;
        if (collectionReadyForBuildListener != null) {
            collectionReadyForBuildListener.onBuildList(this.mReadOnlyNotificationSet);
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void onEndLifetimeExtension(NotifLifetimeExtender notifLifetimeExtender, NotificationEntry notificationEntry) {
        Assert.isMainThread();
        if (this.mAttached) {
            checkForReentrantCall();
            if (notificationEntry.mLifetimeExtenders.remove((Object) notifLifetimeExtender)) {
                this.mLogger.logLifetimeExtensionEnded(notificationEntry.getKey(), notifLifetimeExtender, notificationEntry.mLifetimeExtenders.size());
                if (!isLifetimeExtended(notificationEntry) && tryRemoveNotification(notificationEntry)) {
                    dispatchEventsAndRebuildList();
                    return;
                }
                return;
            }
            throw ((IllegalStateException) this.mEulogizer.record(new IllegalStateException(String.format("Cannot end lifetime extension for extender \"%s\" (%s)", notifLifetimeExtender.getName(), notifLifetimeExtender))));
        }
    }

    private void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        this.mAmDispatchingToOtherCode = true;
        for (NotifLifetimeExtender cancelLifetimeExtension : notificationEntry.mLifetimeExtenders) {
            cancelLifetimeExtension.cancelLifetimeExtension(notificationEntry);
        }
        this.mAmDispatchingToOtherCode = false;
        notificationEntry.mLifetimeExtenders.clear();
    }

    private boolean isLifetimeExtended(NotificationEntry notificationEntry) {
        return notificationEntry.mLifetimeExtenders.size() > 0;
    }

    private void updateLifetimeExtension(NotificationEntry notificationEntry) {
        notificationEntry.mLifetimeExtenders.clear();
        this.mAmDispatchingToOtherCode = true;
        for (NotifLifetimeExtender next : this.mLifetimeExtenders) {
            if (next.maybeExtendLifetime(notificationEntry, notificationEntry.mCancellationReason)) {
                this.mLogger.logLifetimeExtended(notificationEntry.getKey(), next);
                notificationEntry.mLifetimeExtenders.add(next);
            }
        }
        this.mAmDispatchingToOtherCode = false;
    }

    private void updateDismissInterceptors(NotificationEntry notificationEntry) {
        notificationEntry.mDismissInterceptors.clear();
        this.mAmDispatchingToOtherCode = true;
        for (NotifDismissInterceptor next : this.mDismissInterceptors) {
            if (next.shouldInterceptDismissal(notificationEntry)) {
                notificationEntry.mDismissInterceptors.add(next);
            }
        }
        this.mAmDispatchingToOtherCode = false;
    }

    private void cancelLocalDismissal(NotificationEntry notificationEntry) {
        if (notificationEntry.getDismissState() != NotificationEntry.DismissState.NOT_DISMISSED) {
            notificationEntry.setDismissState(NotificationEntry.DismissState.NOT_DISMISSED);
            if (notificationEntry.getSbn().getNotification().isGroupSummary()) {
                for (NotificationEntry next : this.mNotificationSet.values()) {
                    if (next.getSbn().getGroupKey().equals(notificationEntry.getSbn().getGroupKey()) && next.getDismissState() == NotificationEntry.DismissState.PARENT_DISMISSED) {
                        next.setDismissState(NotificationEntry.DismissState.NOT_DISMISSED);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onEndDismissInterception(NotifDismissInterceptor notifDismissInterceptor, NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats) {
        Assert.isMainThread();
        if (this.mAttached) {
            checkForReentrantCall();
            if (!notificationEntry.mDismissInterceptors.remove((Object) notifDismissInterceptor)) {
                throw ((IllegalStateException) this.mEulogizer.record(new IllegalStateException(String.format("Cannot end dismiss interceptor for interceptor \"%s\" (%s)", notifDismissInterceptor.getName(), notifDismissInterceptor))));
            } else if (!isDismissIntercepted(notificationEntry)) {
                dismissNotification(notificationEntry, dismissedByUserStats);
            }
        }
    }

    private void cancelDismissInterception(NotificationEntry notificationEntry) {
        this.mAmDispatchingToOtherCode = true;
        for (NotifDismissInterceptor cancelDismissInterception : notificationEntry.mDismissInterceptors) {
            cancelDismissInterception.cancelDismissInterception(notificationEntry);
        }
        this.mAmDispatchingToOtherCode = false;
        notificationEntry.mDismissInterceptors.clear();
    }

    private boolean isDismissIntercepted(NotificationEntry notificationEntry) {
        return notificationEntry.mDismissInterceptors.size() > 0;
    }

    private void checkForReentrantCall() {
        if (this.mAmDispatchingToOtherCode) {
            throw ((IllegalStateException) this.mEulogizer.record(new IllegalStateException("Reentrant call detected")));
        }
    }

    private void crashIfNotInitializing(RuntimeException runtimeException) {
        if (this.mInitializedTimestamp == 0 || this.mClock.uptimeMillis() - this.mInitializedTimestamp < INITIALIZATION_FORGIVENESS_WINDOW) {
            this.mLogger.logIgnoredError(runtimeException.getMessage());
            return;
        }
        throw ((RuntimeException) this.mEulogizer.record(runtimeException));
    }

    private static NotificationListenerService.Ranking requireRanking(NotificationListenerService.RankingMap rankingMap, String str) {
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        if (rankingMap.getRanking(str, ranking)) {
            return ranking;
        }
        throw new IllegalArgumentException("Ranking map doesn't contain key: " + str);
    }

    private boolean isCanceled(NotificationEntry notificationEntry) {
        return notificationEntry.mCancellationReason != -1;
    }

    private boolean cannotBeLifetimeExtended(NotificationEntry notificationEntry) {
        boolean z = notificationEntry.getDismissState() != NotificationEntry.DismissState.NOT_DISMISSED;
        boolean z2 = notificationEntry.mCancellationReason == 1 || notificationEntry.mCancellationReason == 2;
        if (z || z2) {
            return true;
        }
        return false;
    }

    static boolean shouldAutoDismissChildren(NotificationEntry notificationEntry, String str) {
        return notificationEntry.getSbn().getGroupKey().equals(str) && !notificationEntry.getSbn().getNotification().isGroupSummary() && !hasFlag(notificationEntry, 2) && !hasFlag(notificationEntry, 4096) && !hasFlag(notificationEntry, 32) && notificationEntry.getDismissState() != NotificationEntry.DismissState.DISMISSED;
    }

    private static boolean shouldDismissOnClearAll(NotificationEntry notificationEntry, int i) {
        return userIdMatches(notificationEntry, i) && notificationEntry.isClearable() && !hasFlag(notificationEntry, 4096) && notificationEntry.getDismissState() != NotificationEntry.DismissState.DISMISSED;
    }

    private static boolean hasFlag(NotificationEntry notificationEntry, int i) {
        return (notificationEntry.getSbn().getNotification().flags & i) != 0;
    }

    private static boolean userIdMatches(NotificationEntry notificationEntry, int i) {
        return i == -1 || notificationEntry.getSbn().getUser().getIdentifier() == -1 || notificationEntry.getSbn().getUser().getIdentifier() == i;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        ArrayList arrayList = new ArrayList(getAllNotifs());
        printWriter.println("\tNotifCollection unsorted/unfiltered notifications:");
        if (arrayList.size() == 0) {
            printWriter.println("\t\t None");
        }
        printWriter.println(ListDumper.dumpList(arrayList, true, "\t\t"));
    }

    public InternalNotifUpdater getInternalNotifUpdater(String str) {
        return new NotifCollection$$ExternalSyntheticLambda7(this, str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getInternalNotifUpdater$5$com-android-systemui-statusbar-notification-collection-NotifCollection */
    public /* synthetic */ void mo39958x181d259(String str, StatusBarNotification statusBarNotification, String str2) {
        this.mMainHandler.post(new NotifCollection$$ExternalSyntheticLambda0(this, statusBarNotification, str, str2));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateNotificationInternally */
    public void mo39957x7f371d7a(StatusBarNotification statusBarNotification, String str, String str2) {
        Assert.isMainThread();
        checkForReentrantCall();
        NotificationEntry notificationEntry = this.mNotificationSet.get(statusBarNotification.getKey());
        if (notificationEntry == null) {
            this.mLogger.logNotifInternalUpdateFailed(statusBarNotification.getKey(), str, str2);
            return;
        }
        this.mLogger.logNotifInternalUpdate(statusBarNotification.getKey(), str, str2);
        notificationEntry.setSbn(statusBarNotification);
        this.mEventQueue.add(new BindEntryEvent(notificationEntry, statusBarNotification));
        this.mLogger.logNotifUpdated(statusBarNotification.getKey());
        this.mEventQueue.add(new EntryUpdatedEvent(notificationEntry, false));
        dispatchEventsAndRebuildList();
    }

    public Runnable registerFutureDismissal(NotificationEntry notificationEntry, int i, DismissedByUserStatsCreator dismissedByUserStatsCreator) {
        FutureDismissal futureDismissal = this.mFutureDismissals.get(notificationEntry.getKey());
        if (futureDismissal != null) {
            this.mLogger.logFutureDismissalReused(futureDismissal);
            return futureDismissal;
        }
        FutureDismissal futureDismissal2 = new FutureDismissal(notificationEntry, i, dismissedByUserStatsCreator);
        this.mFutureDismissals.put(notificationEntry.getKey(), futureDismissal2);
        this.mLogger.logFutureDismissalRegistered(futureDismissal2);
        return futureDismissal2;
    }

    private void handleFutureDismissal(NotificationEntry notificationEntry) {
        FutureDismissal remove = this.mFutureDismissals.remove(notificationEntry.getKey());
        if (remove != null) {
            remove.onSystemServerCancel(notificationEntry.mCancellationReason);
        }
    }

    public class FutureDismissal implements Runnable {
        private boolean mDidRun;
        private boolean mDidSystemServerCancel;
        private final NotificationEntry mEntry;
        private final String mLabel;
        private final DismissedByUserStatsCreator mStatsCreator;
        private final NotificationEntry mSummaryToDismiss;

        private FutureDismissal(NotificationEntry notificationEntry, int i, DismissedByUserStatsCreator dismissedByUserStatsCreator) {
            this.mEntry = notificationEntry;
            this.mStatsCreator = dismissedByUserStatsCreator;
            NotificationEntry fetchSummaryToDismiss = fetchSummaryToDismiss(notificationEntry);
            this.mSummaryToDismiss = fetchSummaryToDismiss;
            this.mLabel = "<FutureDismissal@" + Integer.toHexString(hashCode()) + " entry=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " reason=" + NotifCollectionLoggerKt.cancellationReasonDebugString(i) + " summary=" + NotificationUtils.logKey((ListEntry) fetchSummaryToDismiss) + ">";
        }

        private NotificationEntry fetchSummaryToDismiss(NotificationEntry notificationEntry) {
            NotificationEntry groupSummary;
            if (!NotifCollection.this.isOnlyChildInGroup(notificationEntry) || (groupSummary = NotifCollection.this.getGroupSummary(notificationEntry.getSbn().getGroupKey())) == null || !groupSummary.isDismissable()) {
                return null;
            }
            return groupSummary;
        }

        public void onSystemServerCancel(int i) {
            Assert.isMainThread();
            if (this.mDidSystemServerCancel) {
                NotifCollection.this.mLogger.logFutureDismissalDoubleCancelledByServer(this);
                return;
            }
            NotifCollection.this.mLogger.logFutureDismissalGotSystemServerCancel(this, i);
            this.mDidSystemServerCancel = true;
        }

        private void onUiCancel() {
            NotifCollection.this.mFutureDismissals.remove(this.mEntry.getKey());
            NotificationEntry entry = NotifCollection.this.getEntry(this.mEntry.getKey());
            DismissedByUserStats createDismissedByUserStats = this.mStatsCreator.createDismissedByUserStats(this.mEntry);
            NotificationEntry notificationEntry = this.mSummaryToDismiss;
            if (notificationEntry != null) {
                NotificationEntry entry2 = NotifCollection.this.getEntry(notificationEntry.getKey());
                if (entry2 == this.mSummaryToDismiss) {
                    NotifCollection.this.mLogger.logFutureDismissalDismissing(this, "summary");
                    NotifCollection notifCollection = NotifCollection.this;
                    NotificationEntry notificationEntry2 = this.mSummaryToDismiss;
                    notifCollection.dismissNotification(notificationEntry2, this.mStatsCreator.createDismissedByUserStats(notificationEntry2));
                } else {
                    NotifCollection.this.mLogger.logFutureDismissalMismatchedEntry(this, "summary", entry2);
                }
            }
            if (this.mDidSystemServerCancel) {
                NotifCollection.this.mLogger.logFutureDismissalAlreadyCancelledByServer(this);
            } else if (entry == this.mEntry) {
                NotifCollection.this.mLogger.logFutureDismissalDismissing(this, "entry");
                NotifCollection.this.dismissNotification(this.mEntry, createDismissedByUserStats);
            } else {
                NotifCollection.this.mLogger.logFutureDismissalMismatchedEntry(this, "entry", entry);
            }
        }

        public void run() {
            Assert.isMainThread();
            if (this.mDidRun) {
                NotifCollection.this.mLogger.logFutureDismissalDoubleRun(this);
                return;
            }
            this.mDidRun = true;
            onUiCancel();
        }

        public String getLabel() {
            return this.mLabel;
        }
    }
}
