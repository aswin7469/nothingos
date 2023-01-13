package com.android.systemui.media;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.util.time.SystemClock;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001BA\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u000e\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u0001J\u0015\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H\u0001¢\u0006\u0002\b/J\u0006\u00100\u001a\u00020)J\u0006\u00101\u001a\u00020)J\u0006\u00102\u001a\u00020)J\u0006\u00103\u001a\u00020)J:\u00104\u001a\u00020,2\u0006\u00105\u001a\u00020\u00152\b\u00106\u001a\u0004\u0018\u00010\u00152\u0006\u00107\u001a\u00020\u00162\u0006\u00108\u001a\u00020)2\u0006\u00109\u001a\u00020.2\u0006\u0010:\u001a\u00020)H\u0016J\u0010\u0010;\u001a\u00020,2\u0006\u00105\u001a\u00020\u0015H\u0016J \u0010<\u001a\u00020,2\u0006\u00105\u001a\u00020\u00152\u0006\u00107\u001a\u00020$2\u0006\u0010=\u001a\u00020)H\u0016J\u0018\u0010>\u001a\u00020,2\u0006\u00105\u001a\u00020\u00152\u0006\u00108\u001a\u00020)H\u0016J\u0006\u0010?\u001a\u00020,J\u000e\u0010@\u001a\u00020)2\u0006\u0010*\u001a\u00020\u0001J\u001c\u0010A\u001a\u00020B2\u0012\u0010C\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00160DH\u0002R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0012X\u0004¢\u0006\u0002\n\u0000R*\u0010\u0013\u001a\u001e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00160\u0014j\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u0016`\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\u00198@X\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u00020\u001dX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u0010\u0010\"\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R*\u0010%\u001a\u001e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00160\u0014j\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u0016`\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0004¢\u0006\u0002\n\u0000¨\u0006E"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDataFilter;", "Lcom/android/systemui/media/MediaDataManager$Listener;", "context", "Landroid/content/Context;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "broadcastSender", "Lcom/android/systemui/broadcast/BroadcastSender;", "lockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "executor", "Ljava/util/concurrent/Executor;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "logger", "Lcom/android/systemui/media/MediaUiEventLogger;", "(Landroid/content/Context;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/broadcast/BroadcastSender;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Ljava/util/concurrent/Executor;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/media/MediaUiEventLogger;)V", "_listeners", "", "allEntries", "Ljava/util/LinkedHashMap;", "", "Lcom/android/systemui/media/MediaData;", "Lkotlin/collections/LinkedHashMap;", "listeners", "", "getListeners$SystemUI_nothingRelease", "()Ljava/util/Set;", "mediaDataManager", "Lcom/android/systemui/media/MediaDataManager;", "getMediaDataManager$SystemUI_nothingRelease", "()Lcom/android/systemui/media/MediaDataManager;", "setMediaDataManager$SystemUI_nothingRelease", "(Lcom/android/systemui/media/MediaDataManager;)V", "reactivatedKey", "smartspaceMediaData", "Lcom/android/systemui/media/SmartspaceMediaData;", "userEntries", "userTracker", "Lcom/android/systemui/settings/CurrentUserTracker;", "addListener", "", "listener", "handleUserSwitched", "", "id", "", "handleUserSwitched$SystemUI_nothingRelease", "hasActiveMedia", "hasActiveMediaOrRecommendation", "hasAnyMedia", "hasAnyMediaOrRecommendation", "onMediaDataLoaded", "key", "oldKey", "data", "immediately", "receivedSmartspaceCardLatency", "isSsReactivated", "onMediaDataRemoved", "onSmartspaceMediaDataLoaded", "shouldPrioritize", "onSmartspaceMediaDataRemoved", "onSwipeToDismiss", "removeListener", "timeSinceActiveForMostRecentMedia", "", "sortedEntries", "Ljava/util/SortedMap;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaDataFilter.kt */
public final class MediaDataFilter implements MediaDataManager.Listener {
    private final Set<MediaDataManager.Listener> _listeners = new LinkedHashSet();
    private final LinkedHashMap<String, MediaData> allEntries = new LinkedHashMap<>();
    private final BroadcastDispatcher broadcastDispatcher;
    private final BroadcastSender broadcastSender;
    private final Context context;
    /* access modifiers changed from: private */
    public final Executor executor;
    private final NotificationLockscreenUserManager lockscreenUserManager;
    private final MediaUiEventLogger logger;
    public MediaDataManager mediaDataManager;
    private String reactivatedKey;
    private SmartspaceMediaData smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
    private final SystemClock systemClock;
    /* access modifiers changed from: private */
    public final LinkedHashMap<String, MediaData> userEntries = new LinkedHashMap<>();
    private final CurrentUserTracker userTracker;

    @Inject
    public MediaDataFilter(Context context2, BroadcastDispatcher broadcastDispatcher2, BroadcastSender broadcastSender2, NotificationLockscreenUserManager notificationLockscreenUserManager, @Main Executor executor2, SystemClock systemClock2, MediaUiEventLogger mediaUiEventLogger) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(broadcastSender2, "broadcastSender");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(mediaUiEventLogger, "logger");
        this.context = context2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.broadcastSender = broadcastSender2;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.executor = executor2;
        this.systemClock = systemClock2;
        this.logger = mediaUiEventLogger;
        CurrentUserTracker r2 = new CurrentUserTracker(this, broadcastDispatcher2) {
            final /* synthetic */ MediaDataFilter this$0;

            {
                this.this$0 = r1;
            }

            /* access modifiers changed from: private */
            /* renamed from: onUserSwitched$lambda-0  reason: not valid java name */
            public static final void m2786onUserSwitched$lambda0(MediaDataFilter mediaDataFilter, int i) {
                Intrinsics.checkNotNullParameter(mediaDataFilter, "this$0");
                mediaDataFilter.handleUserSwitched$SystemUI_nothingRelease(i);
            }

            public void onUserSwitched(int i) {
                this.this$0.executor.execute(new MediaDataFilter$1$$ExternalSyntheticLambda0(this.this$0, i));
            }
        };
        this.userTracker = r2;
        r2.startTracking();
    }

    public final Set<MediaDataManager.Listener> getListeners$SystemUI_nothingRelease() {
        return CollectionsKt.toSet(this._listeners);
    }

    public final MediaDataManager getMediaDataManager$SystemUI_nothingRelease() {
        MediaDataManager mediaDataManager2 = this.mediaDataManager;
        if (mediaDataManager2 != null) {
            return mediaDataManager2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
        return null;
    }

    public final void setMediaDataManager$SystemUI_nothingRelease(MediaDataManager mediaDataManager2) {
        Intrinsics.checkNotNullParameter(mediaDataManager2, "<set-?>");
        this.mediaDataManager = mediaDataManager2;
    }

    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        if (str2 != null && !Intrinsics.areEqual((Object) str2, (Object) str)) {
            this.allEntries.remove(str2);
        }
        this.allEntries.put(str, mediaData);
        if (this.lockscreenUserManager.isCurrentProfile(mediaData.getUserId())) {
            if (str2 != null && !Intrinsics.areEqual((Object) str2, (Object) str)) {
                this.userEntries.remove(str2);
            }
            this.userEntries.put(str, mediaData);
            for (MediaDataManager.Listener onMediaDataLoaded$default : getListeners$SystemUI_nothingRelease()) {
                MediaDataManager.Listener.onMediaDataLoaded$default(onMediaDataLoaded$default, str, str2, mediaData, false, 0, false, 56, (Object) null);
            }
        }
    }

    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData2, boolean z) {
        String str2 = str;
        SmartspaceMediaData smartspaceMediaData3 = smartspaceMediaData2;
        Intrinsics.checkNotNullParameter(str2, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData3, "data");
        if (!smartspaceMediaData2.isActive()) {
            Log.d("MediaDataFilter", "Inactive recommendation data. Skip triggering.");
            return;
        }
        this.smartspaceMediaData = smartspaceMediaData3;
        SortedMap sortedMap = MapsKt.toSortedMap(this.userEntries, new MediaDataFilter$onSmartspaceMediaDataLoaded$$inlined$compareBy$1(this));
        long timeSinceActiveForMostRecentMedia = timeSinceActiveForMostRecentMedia(sortedMap);
        long smartspace_max_age = MediaDataFilterKt.getSMARTSPACE_MAX_AGE();
        SmartspaceAction cardAction = smartspaceMediaData2.getCardAction();
        if (cardAction != null) {
            long j = cardAction.getExtras().getLong("resumable_media_max_age_seconds", 0);
            if (j > 0) {
                smartspace_max_age = TimeUnit.SECONDS.toMillis(j);
            }
        }
        boolean z2 = true;
        boolean z3 = !hasActiveMedia() && hasAnyMedia();
        if (timeSinceActiveForMostRecentMedia < smartspace_max_age) {
            if (z3) {
                String str3 = (String) sortedMap.lastKey();
                Log.d("MediaDataFilter", "reactivating " + str3 + " instead of smartspace");
                this.reactivatedKey = str3;
                Object obj = sortedMap.get(str3);
                Intrinsics.checkNotNull(obj);
                MediaData copy$default = MediaData.copy$default((MediaData) obj, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, true, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, (InstanceId) null, 0, 33538047, (Object) null);
                this.logger.logRecommendationActivated(copy$default.getAppUid(), copy$default.getPackageName(), copy$default.getInstanceId());
                for (MediaDataManager.Listener onMediaDataLoaded$default : getListeners$SystemUI_nothingRelease()) {
                    Intrinsics.checkNotNullExpressionValue(str3, "lastActiveKey");
                    MediaDataManager.Listener.onMediaDataLoaded$default(onMediaDataLoaded$default, str3, str3, copy$default, false, (int) (this.systemClock.currentTimeMillis() - smartspaceMediaData2.getHeadphoneConnectionTimeMillis()), true, 8, (Object) null);
                }
            }
            z2 = false;
        }
        if (!smartspaceMediaData2.isValid()) {
            Log.d("MediaDataFilter", "Invalid recommendation data. Skip showing the rec card");
            return;
        }
        this.logger.logRecommendationAdded(this.smartspaceMediaData.getPackageName(), this.smartspaceMediaData.getInstanceId());
        for (MediaDataManager.Listener onSmartspaceMediaDataLoaded : getListeners$SystemUI_nothingRelease()) {
            onSmartspaceMediaDataLoaded.onSmartspaceMediaDataLoaded(str2, smartspaceMediaData3, z2);
        }
    }

    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.allEntries.remove(str);
        if (this.userEntries.remove(str) != null) {
            for (MediaDataManager.Listener onMediaDataRemoved : getListeners$SystemUI_nothingRelease()) {
                onMediaDataRemoved.onMediaDataRemoved(str);
            }
        }
    }

    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        String str2 = this.reactivatedKey;
        if (str2 != null) {
            this.reactivatedKey = null;
            Log.d("MediaDataFilter", "expiring reactivated key " + str2);
            MediaData mediaData = this.userEntries.get(str2);
            if (mediaData != null) {
                for (MediaDataManager.Listener onMediaDataLoaded$default : getListeners$SystemUI_nothingRelease()) {
                    Intrinsics.checkNotNullExpressionValue(mediaData, "mediaData");
                    MediaDataManager.Listener.onMediaDataLoaded$default(onMediaDataLoaded$default, str2, str2, mediaData, z, 0, false, 48, (Object) null);
                }
            }
        }
        if (this.smartspaceMediaData.isActive()) {
            this.smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), 126, (Object) null);
        }
        for (MediaDataManager.Listener onSmartspaceMediaDataRemoved : getListeners$SystemUI_nothingRelease()) {
            onSmartspaceMediaDataRemoved.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    public final void handleUserSwitched$SystemUI_nothingRelease(int i) {
        Set<MediaDataManager.Listener> listeners$SystemUI_nothingRelease = getListeners$SystemUI_nothingRelease();
        Set<String> keySet = this.userEntries.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "userEntries.keys");
        List<String> mutableList = CollectionsKt.toMutableList(keySet);
        this.userEntries.clear();
        for (String str : mutableList) {
            Log.d("MediaDataFilter", "Removing " + str + " after user change");
            for (MediaDataManager.Listener onMediaDataRemoved : listeners$SystemUI_nothingRelease) {
                Intrinsics.checkNotNullExpressionValue(str, "it");
                onMediaDataRemoved.onMediaDataRemoved(str);
            }
        }
        for (Map.Entry entry : this.allEntries.entrySet()) {
            String str2 = (String) entry.getKey();
            MediaData mediaData = (MediaData) entry.getValue();
            if (this.lockscreenUserManager.isCurrentProfile(mediaData.getUserId())) {
                Log.d("MediaDataFilter", "Re-adding " + str2 + " after user change");
                this.userEntries.put(str2, mediaData);
                for (MediaDataManager.Listener onMediaDataLoaded$default : listeners$SystemUI_nothingRelease) {
                    MediaDataManager.Listener.onMediaDataLoaded$default(onMediaDataLoaded$default, str2, (String) null, mediaData, false, 0, false, 56, (Object) null);
                }
            }
        }
    }

    public final void onSwipeToDismiss() {
        Log.d("MediaDataFilter", "Media carousel swiped away");
        Set<String> keySet = this.userEntries.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "userEntries.keys");
        for (String str : CollectionsKt.toSet(keySet)) {
            MediaDataManager mediaDataManager$SystemUI_nothingRelease = getMediaDataManager$SystemUI_nothingRelease();
            Intrinsics.checkNotNullExpressionValue(str, "it");
            mediaDataManager$SystemUI_nothingRelease.setTimedOut$SystemUI_nothingRelease(str, true, true);
        }
        if (this.smartspaceMediaData.isActive()) {
            Intent dismissIntent = this.smartspaceMediaData.getDismissIntent();
            if (dismissIntent == null) {
                Log.w("MediaDataFilter", "Cannot create dismiss action click action: extras missing dismiss_intent.");
            } else if (dismissIntent.getComponent() == null || !Intrinsics.areEqual((Object) dismissIntent.getComponent().getClassName(), (Object) "com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity")) {
                this.broadcastSender.sendBroadcast(dismissIntent);
            } else {
                this.context.startActivity(dismissIntent);
            }
            this.smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), 126, (Object) null);
            getMediaDataManager$SystemUI_nothingRelease().dismissSmartspaceRecommendation(this.smartspaceMediaData.getTargetId(), 0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean hasActiveMediaOrRecommendation() {
        /*
            r4 = this;
            java.util.LinkedHashMap<java.lang.String, com.android.systemui.media.MediaData> r0 = r4.userEntries
            java.util.Map r0 = (java.util.Map) r0
            boolean r1 = r0.isEmpty()
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x000e
        L_0x000c:
            r0 = r3
            goto L_0x002f
        L_0x000e:
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0016:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x000c
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            java.lang.Object r1 = r1.getValue()
            com.android.systemui.media.MediaData r1 = (com.android.systemui.media.MediaData) r1
            boolean r1 = r1.getActive()
            if (r1 == 0) goto L_0x0016
            r0 = r2
        L_0x002f:
            if (r0 != 0) goto L_0x0043
            com.android.systemui.media.SmartspaceMediaData r0 = r4.smartspaceMediaData
            boolean r0 = r0.isActive()
            if (r0 == 0) goto L_0x0042
            com.android.systemui.media.SmartspaceMediaData r4 = r4.smartspaceMediaData
            boolean r4 = r4.isValid()
            if (r4 == 0) goto L_0x0042
            goto L_0x0043
        L_0x0042:
            r2 = r3
        L_0x0043:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDataFilter.hasActiveMediaOrRecommendation():boolean");
    }

    public final boolean hasAnyMediaOrRecommendation() {
        if (!this.userEntries.isEmpty()) {
            return true;
        }
        if (!this.smartspaceMediaData.isActive() || !this.smartspaceMediaData.isValid()) {
            return false;
        }
        return true;
    }

    public final boolean hasActiveMedia() {
        Map map = this.userEntries;
        if (map.isEmpty()) {
            return false;
        }
        for (Map.Entry value : map.entrySet()) {
            if (((MediaData) value.getValue()).getActive()) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasAnyMedia() {
        return !this.userEntries.isEmpty();
    }

    public final boolean addListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this._listeners.add(listener);
    }

    public final boolean removeListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this._listeners.remove(listener);
    }

    private final long timeSinceActiveForMostRecentMedia(SortedMap<String, MediaData> sortedMap) {
        if (sortedMap.isEmpty()) {
            return Long.MAX_VALUE;
        }
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        MediaData mediaData = sortedMap.get(sortedMap.lastKey());
        if (mediaData != null) {
            return elapsedRealtime - mediaData.getLastActive();
        }
        return Long.MAX_VALUE;
    }
}
