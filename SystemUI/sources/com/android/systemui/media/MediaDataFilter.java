package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.util.time.SystemClock;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaDataFilter.kt */
/* loaded from: classes.dex */
public final class MediaDataFilter implements MediaDataManager.Listener {
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final Context context;
    @NotNull
    private final Executor executor;
    @NotNull
    private final NotificationLockscreenUserManager lockscreenUserManager;
    public MediaDataManager mediaDataManager;
    @NotNull
    private final MediaResumeListener mediaResumeListener;
    @Nullable
    private String reactivatedKey;
    @NotNull
    private final SystemClock systemClock;
    @NotNull
    private final CurrentUserTracker userTracker;
    @NotNull
    private final Set<MediaDataManager.Listener> _listeners = new LinkedHashSet();
    @NotNull
    private final LinkedHashMap<String, MediaData> allEntries = new LinkedHashMap<>();
    @NotNull
    private final LinkedHashMap<String, MediaData> userEntries = new LinkedHashMap<>();
    @NotNull
    private SmartspaceMediaData smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();

    public MediaDataFilter(@NotNull Context context, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull MediaResumeListener mediaResumeListener, @NotNull NotificationLockscreenUserManager lockscreenUserManager, @NotNull Executor executor, @NotNull SystemClock systemClock) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(mediaResumeListener, "mediaResumeListener");
        Intrinsics.checkNotNullParameter(lockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        this.context = context;
        this.broadcastDispatcher = broadcastDispatcher;
        this.mediaResumeListener = mediaResumeListener;
        this.lockscreenUserManager = lockscreenUserManager;
        this.executor = executor;
        this.systemClock = systemClock;
        CurrentUserTracker currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.media.MediaDataFilter.1
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(final int i) {
                Executor executor2 = MediaDataFilter.this.executor;
                final MediaDataFilter mediaDataFilter = MediaDataFilter.this;
                executor2.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataFilter$1$onUserSwitched$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataFilter.this.handleUserSwitched$frameworks__base__packages__SystemUI__android_common__SystemUI_core(i);
                    }
                });
            }
        };
        this.userTracker = currentUserTracker;
        currentUserTracker.startTracking();
    }

    @NotNull
    public final Set<MediaDataManager.Listener> getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        Set<MediaDataManager.Listener> set;
        set = CollectionsKt___CollectionsKt.toSet(this._listeners);
        return set;
    }

    @NotNull
    public final MediaDataManager getMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        MediaDataManager mediaDataManager = this.mediaDataManager;
        if (mediaDataManager != null) {
            return mediaDataManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
        throw null;
    }

    public final void setMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull MediaDataManager mediaDataManager) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "<set-?>");
        this.mediaDataManager = mediaDataManager;
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(@NotNull String key, @Nullable String str, @NotNull MediaData data, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        if (str != null && !Intrinsics.areEqual(str, key)) {
            this.allEntries.remove(str);
        }
        this.allEntries.put(key, data);
        if (!this.lockscreenUserManager.isCurrentProfile(data.getUserId())) {
            return;
        }
        if (str != null && !Intrinsics.areEqual(str, key)) {
            this.userEntries.remove(str);
        }
        this.userEntries.put(key, data);
        for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, key, str, data, false, z2, 8, null);
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(@NotNull String key, @NotNull SmartspaceMediaData data, boolean z) {
        SortedMap<String, MediaData> sortedMap;
        MediaData copy;
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        if (!data.isActive()) {
            Log.d("MediaDataFilter", "Inactive recommendation data. Skip triggering.");
            return;
        }
        this.smartspaceMediaData = data;
        sortedMap = MapsKt__MapsJVMKt.toSortedMap(this.userEntries, new Comparator<T>() { // from class: com.android.systemui.media.MediaDataFilter$onSmartspaceMediaDataLoaded$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                LinkedHashMap linkedHashMap;
                LinkedHashMap linkedHashMap2;
                int compareValues;
                linkedHashMap = MediaDataFilter.this.userEntries;
                MediaData mediaData = (MediaData) linkedHashMap.get((String) t);
                int i = -1;
                Comparable valueOf = mediaData == null ? -1 : Long.valueOf(mediaData.getLastActive());
                linkedHashMap2 = MediaDataFilter.this.userEntries;
                MediaData mediaData2 = (MediaData) linkedHashMap2.get((String) t2);
                if (mediaData2 != null) {
                    i = Long.valueOf(mediaData2.getLastActive());
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(valueOf, i);
                return compareValues;
            }
        });
        long timeSinceActiveForMostRecentMedia = timeSinceActiveForMostRecentMedia(sortedMap);
        long smartspace_max_age = MediaDataFilterKt.getSMARTSPACE_MAX_AGE();
        SmartspaceAction cardAction = data.getCardAction();
        if (cardAction != null) {
            long j = cardAction.getExtras().getLong("resumable_media_max_age_seconds", 0L);
            if (j > 0) {
                smartspace_max_age = TimeUnit.SECONDS.toMillis(j);
            }
        }
        boolean z2 = true;
        if (timeSinceActiveForMostRecentMedia < smartspace_max_age) {
            String lastActiveKey = sortedMap.lastKey();
            Log.d("MediaDataFilter", "reactivating " + ((Object) lastActiveKey) + " instead of smartspace");
            this.reactivatedKey = lastActiveKey;
            if (MediaPlayerData.INSTANCE.firstActiveMediaIndex() != -1) {
                z2 = false;
            }
            MediaData mediaData = sortedMap.get(lastActiveKey);
            Intrinsics.checkNotNull(mediaData);
            copy = mediaData.copy((i3 & 1) != 0 ? mediaData.userId : 0, (i3 & 2) != 0 ? mediaData.initialized : false, (i3 & 4) != 0 ? mediaData.backgroundColor : 0, (i3 & 8) != 0 ? mediaData.app : null, (i3 & 16) != 0 ? mediaData.appIcon : null, (i3 & 32) != 0 ? mediaData.artist : null, (i3 & 64) != 0 ? mediaData.song : null, (i3 & 128) != 0 ? mediaData.artwork : null, (i3 & 256) != 0 ? mediaData.actions : null, (i3 & 512) != 0 ? mediaData.actionsToShowInCompact : null, (i3 & 1024) != 0 ? mediaData.packageName : null, (i3 & 2048) != 0 ? mediaData.token : null, (i3 & 4096) != 0 ? mediaData.clickIntent : null, (i3 & 8192) != 0 ? mediaData.device : null, (i3 & 16384) != 0 ? mediaData.active : true, (i3 & 32768) != 0 ? mediaData.resumeAction : null, (i3 & 65536) != 0 ? mediaData.isLocalSession : false, (i3 & 131072) != 0 ? mediaData.resumption : false, (i3 & 262144) != 0 ? mediaData.notificationKey : null, (i3 & 524288) != 0 ? mediaData.hasCheckedForResume : false, (i3 & 1048576) != 0 ? mediaData.isPlaying : null, (i3 & 2097152) != 0 ? mediaData.isClearable : false, (i3 & 4194304) != 0 ? mediaData.lastActive : 0L);
            for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                Intrinsics.checkNotNullExpressionValue(lastActiveKey, "lastActiveKey");
                MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, lastActiveKey, lastActiveKey, copy, false, z2, 8, null);
            }
            z2 = false;
        }
        if (!data.isValid()) {
            Log.d("MediaDataFilter", "Invalid recommendation data. Skip showing the rec card");
            return;
        }
        for (MediaDataManager.Listener listener2 : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            listener2.onSmartspaceMediaDataLoaded(key, data, z2);
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.allEntries.remove(key);
        if (this.userEntries.remove(key) == null) {
            return;
        }
        for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            listener.onMediaDataRemoved(key);
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(@NotNull String key, boolean z) {
        SmartspaceMediaData copy;
        Intrinsics.checkNotNullParameter(key, "key");
        String str = this.reactivatedKey;
        if (str != null) {
            this.reactivatedKey = null;
            Log.d("MediaDataFilter", Intrinsics.stringPlus("expiring reactivated key ", str));
            MediaData mediaData = this.userEntries.get(str);
            if (mediaData != null) {
                for (MediaDataManager.Listener listener : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str, str, mediaData, z, false, 16, null);
                }
            }
        }
        if (this.smartspaceMediaData.isActive()) {
            copy = r1.copy((r18 & 1) != 0 ? r1.targetId : this.smartspaceMediaData.getTargetId(), (r18 & 2) != 0 ? r1.isActive : false, (r18 & 4) != 0 ? r1.isValid : this.smartspaceMediaData.isValid(), (r18 & 8) != 0 ? r1.packageName : null, (r18 & 16) != 0 ? r1.cardAction : null, (r18 & 32) != 0 ? r1.recommendations : null, (r18 & 64) != 0 ? r1.dismissIntent : null, (r18 & 128) != 0 ? MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA().backgroundColor : 0);
            this.smartspaceMediaData = copy;
        }
        for (MediaDataManager.Listener listener2 : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            listener2.onSmartspaceMediaDataRemoved(key, z);
        }
    }

    @VisibleForTesting
    public final void handleUserSwitched$frameworks__base__packages__SystemUI__android_common__SystemUI_core(int i) {
        List<String> mutableList;
        Set<MediaDataManager.Listener> listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        Set<String> keySet = this.userEntries.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "userEntries.keys");
        mutableList = CollectionsKt___CollectionsKt.toMutableList((Collection) keySet);
        this.userEntries.clear();
        for (String it : mutableList) {
            Log.d("MediaDataFilter", "Removing " + it + " after user change");
            for (MediaDataManager.Listener listener : listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                Intrinsics.checkNotNullExpressionValue(it, "it");
                listener.onMediaDataRemoved(it);
            }
        }
        for (Map.Entry<String, MediaData> entry : this.allEntries.entrySet()) {
            String key = entry.getKey();
            MediaData value = entry.getValue();
            if (this.lockscreenUserManager.isCurrentProfile(value.getUserId())) {
                Log.d("MediaDataFilter", "Re-adding " + key + " after user change");
                this.userEntries.put(key, value);
                for (MediaDataManager.Listener listener2 : listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener2, key, null, value, false, false, 24, null);
                }
            }
        }
    }

    public final void onSwipeToDismiss() {
        Set<String> set;
        SmartspaceMediaData copy;
        Log.d("MediaDataFilter", "Media carousel swiped away");
        Set<String> keySet = this.userEntries.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "userEntries.keys");
        set = CollectionsKt___CollectionsKt.toSet(keySet);
        for (String it : set) {
            MediaDataManager mediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            Intrinsics.checkNotNullExpressionValue(it, "it");
            mediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(it, true, true);
        }
        if (this.smartspaceMediaData.isActive()) {
            Intent dismissIntent = this.smartspaceMediaData.getDismissIntent();
            if (dismissIntent == null) {
                Log.w("MediaDataFilter", "Cannot create dismiss action click action: extras missing dismiss_intent.");
            } else if (dismissIntent.getComponent() != null && Intrinsics.areEqual(dismissIntent.getComponent().getClassName(), "com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity")) {
                this.context.startActivity(dismissIntent);
            } else {
                this.context.sendBroadcast(dismissIntent);
            }
            copy = r2.copy((r18 & 1) != 0 ? r2.targetId : this.smartspaceMediaData.getTargetId(), (r18 & 2) != 0 ? r2.isActive : false, (r18 & 4) != 0 ? r2.isValid : this.smartspaceMediaData.isValid(), (r18 & 8) != 0 ? r2.packageName : null, (r18 & 16) != 0 ? r2.cardAction : null, (r18 & 32) != 0 ? r2.recommendations : null, (r18 & 64) != 0 ? r2.dismissIntent : null, (r18 & 128) != 0 ? MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA().backgroundColor : 0);
            this.smartspaceMediaData = copy;
        }
        getMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core().dismissSmartspaceRecommendation(this.smartspaceMediaData.getTargetId(), 0L);
    }

    public final boolean hasActiveMedia() {
        boolean z;
        LinkedHashMap<String, MediaData> linkedHashMap = this.userEntries;
        if (!linkedHashMap.isEmpty()) {
            for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
                if (entry.getValue().getActive()) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        return z || this.smartspaceMediaData.isActive();
    }

    public final boolean hasAnyMedia() {
        return (this.userEntries.isEmpty() ^ true) || this.smartspaceMediaData.isActive();
    }

    public final boolean addListener(@NotNull MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this._listeners.add(listener);
    }

    public final boolean removeListener(@NotNull MediaDataManager.Listener listener) {
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
