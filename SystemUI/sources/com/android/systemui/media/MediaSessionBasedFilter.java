package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.util.Log;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaSessionBasedFilter.kt */
/* loaded from: classes.dex */
public final class MediaSessionBasedFilter implements MediaDataManager.Listener {
    @NotNull
    private final Executor backgroundExecutor;
    @NotNull
    private final Executor foregroundExecutor;
    @NotNull
    private final MediaSessionManager sessionManager;
    @NotNull
    private final Set<MediaDataManager.Listener> listeners = new LinkedHashSet();
    @NotNull
    private final LinkedHashMap<String, List<MediaController>> packageControllers = new LinkedHashMap<>();
    @NotNull
    private final Map<String, Set<MediaSession.Token>> keyedTokens = new LinkedHashMap();
    @NotNull
    private final Set<MediaSession.Token> tokensWithNotifications = new LinkedHashSet();
    @NotNull
    private final MediaSessionBasedFilter$sessionListener$1 sessionListener = new MediaSessionManager.OnActiveSessionsChangedListener() { // from class: com.android.systemui.media.MediaSessionBasedFilter$sessionListener$1
        @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
        public void onActiveSessionsChanged(@NotNull List<MediaController> controllers) {
            Intrinsics.checkNotNullParameter(controllers, "controllers");
            MediaSessionBasedFilter.this.handleControllersChanged(controllers);
        }
    };

    /* JADX WARN: Type inference failed for: r3v5, types: [com.android.systemui.media.MediaSessionBasedFilter$sessionListener$1] */
    public MediaSessionBasedFilter(@NotNull final Context context, @NotNull MediaSessionManager sessionManager, @NotNull Executor foregroundExecutor, @NotNull Executor backgroundExecutor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(sessionManager, "sessionManager");
        Intrinsics.checkNotNullParameter(foregroundExecutor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        this.sessionManager = sessionManager;
        this.foregroundExecutor = foregroundExecutor;
        this.backgroundExecutor = backgroundExecutor;
        backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter.1
            @Override // java.lang.Runnable
            public final void run() {
                ComponentName componentName = new ComponentName(context, NotificationListenerWithPlugins.class);
                this.sessionManager.addOnActiveSessionsChangedListener(this.sessionListener, componentName);
                MediaSessionBasedFilter mediaSessionBasedFilter = this;
                List<MediaController> activeSessions = mediaSessionBasedFilter.sessionManager.getActiveSessions(componentName);
                Intrinsics.checkNotNullExpressionValue(activeSessions, "sessionManager.getActiveSessions(name)");
                mediaSessionBasedFilter.handleControllersChanged(activeSessions);
            }
        });
    }

    public final boolean addListener(@NotNull MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(@NotNull final String key, @Nullable final String str, @NotNull final MediaData data, final boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onMediaDataLoaded$1
            @Override // java.lang.Runnable
            public final void run() {
                LinkedHashMap linkedHashMap;
                ArrayList arrayList;
                Set set;
                Map map;
                Map map2;
                Set mutableSetOf;
                Map map3;
                Map map4;
                Map map5;
                Set set2;
                MediaSession.Token token = MediaData.this.getToken();
                if (token != null) {
                    set2 = this.tokensWithNotifications;
                    set2.add(token);
                }
                String str2 = str;
                boolean z3 = str2 != null && !Intrinsics.areEqual(key, str2);
                if (z3) {
                    map4 = this.keyedTokens;
                    String str3 = str;
                    Objects.requireNonNull(map4, "null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
                    Set set3 = (Set) TypeIntrinsics.asMutableMap(map4).remove(str3);
                    if (set3 != null) {
                        MediaSessionBasedFilter mediaSessionBasedFilter = this;
                        String str4 = key;
                        map5 = mediaSessionBasedFilter.keyedTokens;
                        Set set4 = (Set) map5.put(str4, set3);
                    }
                }
                MediaController mediaController = null;
                if (MediaData.this.getToken() != null) {
                    map2 = this.keyedTokens;
                    Set set5 = (Set) map2.get(key);
                    if ((set5 == null ? null : Boolean.valueOf(set5.add(MediaData.this.getToken()))) == null) {
                        MediaSessionBasedFilter mediaSessionBasedFilter2 = this;
                        MediaData mediaData = MediaData.this;
                        String str5 = key;
                        mutableSetOf = SetsKt__SetsKt.mutableSetOf(mediaData.getToken());
                        map3 = mediaSessionBasedFilter2.keyedTokens;
                        Set set6 = (Set) map3.put(str5, mutableSetOf);
                    }
                }
                linkedHashMap = this.packageControllers;
                List list = (List) linkedHashMap.get(MediaData.this.getPackageName());
                if (list == null) {
                    arrayList = null;
                } else {
                    arrayList = new ArrayList();
                    for (Object obj : list) {
                        MediaController.PlaybackInfo playbackInfo = ((MediaController) obj).getPlaybackInfo();
                        Integer valueOf = playbackInfo == null ? null : Integer.valueOf(playbackInfo.getPlaybackType());
                        if (valueOf != null && valueOf.intValue() == 2) {
                            arrayList.add(obj);
                        }
                    }
                }
                Integer valueOf2 = arrayList == null ? null : Integer.valueOf(arrayList.size());
                if (valueOf2 != null && valueOf2.intValue() == 1) {
                    mediaController = (MediaController) CollectionsKt.firstOrNull(arrayList);
                }
                if (!z3 && mediaController != null && !Intrinsics.areEqual(mediaController.getSessionToken(), MediaData.this.getToken())) {
                    set = this.tokensWithNotifications;
                    if (set.contains(mediaController.getSessionToken())) {
                        Log.d("MediaSessionBasedFilter", "filtering key=" + key + " local=" + MediaData.this.getToken() + " remote=" + mediaController.getSessionToken());
                        map = this.keyedTokens;
                        Set set7 = (Set) map.get(key);
                        Intrinsics.checkNotNull(set7);
                        if (set7.contains(mediaController.getSessionToken())) {
                            return;
                        }
                        this.dispatchMediaDataRemoved(key);
                        return;
                    }
                }
                this.dispatchMediaDataLoaded(key, str, MediaData.this, z);
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(@NotNull final String key, @NotNull final SmartspaceMediaData data, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onSmartspaceMediaDataLoaded$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaSessionBasedFilter.this.dispatchSmartspaceMediaDataLoaded(key, data);
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(@NotNull final String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onMediaDataRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                Map map;
                map = MediaSessionBasedFilter.this.keyedTokens;
                map.remove(key);
                MediaSessionBasedFilter.this.dispatchMediaDataRemoved(key);
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(@NotNull final String key, final boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$onSmartspaceMediaDataRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaSessionBasedFilter.this.dispatchSmartspaceMediaDataRemoved(key, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void dispatchMediaDataLoaded(final String str, final String str2, final MediaData mediaData, final boolean z) {
        this.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchMediaDataLoaded$1
            @Override // java.lang.Runnable
            public final void run() {
                Set set;
                Set<MediaDataManager.Listener> set2;
                set = MediaSessionBasedFilter.this.listeners;
                set2 = CollectionsKt___CollectionsKt.toSet(set);
                String str3 = str;
                String str4 = str2;
                MediaData mediaData2 = mediaData;
                boolean z2 = z;
                for (MediaDataManager.Listener listener : set2) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str3, str4, mediaData2, z2, false, 16, null);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void dispatchMediaDataRemoved(final String str) {
        this.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchMediaDataRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                Set set;
                Set<MediaDataManager.Listener> set2;
                set = MediaSessionBasedFilter.this.listeners;
                set2 = CollectionsKt___CollectionsKt.toSet(set);
                String str2 = str;
                for (MediaDataManager.Listener listener : set2) {
                    listener.onMediaDataRemoved(str2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void dispatchSmartspaceMediaDataLoaded(final String str, final SmartspaceMediaData smartspaceMediaData) {
        this.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchSmartspaceMediaDataLoaded$1
            @Override // java.lang.Runnable
            public final void run() {
                Set set;
                Set<MediaDataManager.Listener> set2;
                set = MediaSessionBasedFilter.this.listeners;
                set2 = CollectionsKt___CollectionsKt.toSet(set);
                String str2 = str;
                SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
                for (MediaDataManager.Listener listener : set2) {
                    MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(listener, str2, smartspaceMediaData2, false, 4, null);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void dispatchSmartspaceMediaDataRemoved(final String str, final boolean z) {
        this.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchSmartspaceMediaDataRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                Set set;
                Set<MediaDataManager.Listener> set2;
                set = MediaSessionBasedFilter.this.listeners;
                set2 = CollectionsKt___CollectionsKt.toSet(set);
                String str2 = str;
                boolean z2 = z;
                for (MediaDataManager.Listener listener : set2) {
                    listener.onSmartspaceMediaDataRemoved(str2, z2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleControllersChanged(List<MediaController> list) {
        this.packageControllers.clear();
        for (MediaController mediaController : list) {
            List<MediaController> list2 = this.packageControllers.get(mediaController.getPackageName());
            if ((list2 == null ? null : Boolean.valueOf(list2.add(mediaController))) == null) {
                this.packageControllers.put(mediaController.getPackageName(), CollectionsKt.mutableListOf(mediaController));
            }
        }
        Set<MediaSession.Token> set = this.tokensWithNotifications;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        for (MediaController mediaController2 : list) {
            arrayList.add(mediaController2.getSessionToken());
        }
        set.retainAll(arrayList);
    }
}
