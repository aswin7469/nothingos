package com.android.systemui.media;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.row.HybridGroupManager;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$ObjectRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaDataManager.kt */
/* loaded from: classes.dex */
public final class MediaDataManager implements Dumpable, BcSmartspaceDataPlugin.SmartspaceTargetListener {
    @NotNull
    private final ActivityStarter activityStarter;
    private boolean allowMediaRecommendations;
    @NotNull
    private final MediaDataManager$appChangeReceiver$1 appChangeReceiver;
    @NotNull
    private final Executor backgroundExecutor;
    private final int bgColor;
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final Context context;
    @NotNull
    private final DelayableExecutor foregroundExecutor;
    @NotNull
    private final Set<Listener> internalListeners;
    @NotNull
    private final MediaControllerFactory mediaControllerFactory;
    @NotNull
    private final MediaDataFilter mediaDataFilter;
    @NotNull
    private final LinkedHashMap<String, MediaData> mediaEntries;
    @NotNull
    private SmartspaceMediaData smartspaceMediaData;
    @NotNull
    private final SmartspaceMediaDataProvider smartspaceMediaDataProvider;
    @Nullable
    private SmartspaceSession smartspaceSession;
    @NotNull
    private final SystemClock systemClock;
    private final int themeText;
    @NotNull
    private final TunerService tunerService;
    private boolean useMediaResumption;
    private final boolean useQsMediaPlayer;
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    public static final String SMARTSPACE_UI_SURFACE_LABEL = "media_data_manager";
    @NotNull
    public static final String EXTRAS_MEDIA_SOURCE_PACKAGE_NAME = "package_name";
    public static final int MAX_COMPACT_ACTIONS = 3;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v12, types: [com.android.systemui.media.MediaDataManager$appChangeReceiver$1, android.content.BroadcastReceiver] */
    public MediaDataManager(@NotNull Context context, @NotNull Executor backgroundExecutor, @NotNull DelayableExecutor foregroundExecutor, @NotNull MediaControllerFactory mediaControllerFactory, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull DumpManager dumpManager, @NotNull MediaTimeoutListener mediaTimeoutListener, @NotNull MediaResumeListener mediaResumeListener, @NotNull MediaSessionBasedFilter mediaSessionBasedFilter, @NotNull MediaDeviceManager mediaDeviceManager, @NotNull MediaDataCombineLatest mediaDataCombineLatest, @NotNull MediaDataFilter mediaDataFilter, @NotNull ActivityStarter activityStarter, @NotNull SmartspaceMediaDataProvider smartspaceMediaDataProvider, boolean z, boolean z2, @NotNull SystemClock systemClock, @NotNull TunerService tunerService) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(foregroundExecutor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(mediaControllerFactory, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(mediaTimeoutListener, "mediaTimeoutListener");
        Intrinsics.checkNotNullParameter(mediaResumeListener, "mediaResumeListener");
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "mediaSessionBasedFilter");
        Intrinsics.checkNotNullParameter(mediaDeviceManager, "mediaDeviceManager");
        Intrinsics.checkNotNullParameter(mediaDataCombineLatest, "mediaDataCombineLatest");
        Intrinsics.checkNotNullParameter(mediaDataFilter, "mediaDataFilter");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(smartspaceMediaDataProvider, "smartspaceMediaDataProvider");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        this.context = context;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.mediaControllerFactory = mediaControllerFactory;
        this.broadcastDispatcher = broadcastDispatcher;
        this.mediaDataFilter = mediaDataFilter;
        this.activityStarter = activityStarter;
        this.smartspaceMediaDataProvider = smartspaceMediaDataProvider;
        this.useMediaResumption = z;
        this.useQsMediaPlayer = z2;
        this.systemClock = systemClock;
        this.tunerService = tunerService;
        this.themeText = Utils.getColorAttr(context, 16842806).getDefaultColor();
        this.bgColor = context.getColor(17170502);
        this.internalListeners = new LinkedHashSet();
        this.mediaEntries = new LinkedHashMap<>();
        this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
        this.allowMediaRecommendations = com.android.systemui.util.Utils.allowMediaRecommendations(context);
        ?? r2 = new BroadcastReceiver() { // from class: com.android.systemui.media.MediaDataManager$appChangeReceiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(@NotNull Context context2, @NotNull Intent intent) {
                String[] stringArrayExtra;
                String encodedSchemeSpecificPart;
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                String action = intent.getAction();
                if (action != null) {
                    int hashCode = action.hashCode();
                    if (hashCode == -1001645458) {
                        if (!action.equals("android.intent.action.PACKAGES_SUSPENDED") || (stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list")) == null) {
                            return;
                        }
                        MediaDataManager mediaDataManager = MediaDataManager.this;
                        for (String it : stringArrayExtra) {
                            Intrinsics.checkNotNullExpressionValue(it, "it");
                            mediaDataManager.removeAllForPackage(it);
                        }
                        return;
                    }
                    if (hashCode != -757780528) {
                        if (hashCode != 525384130 || !action.equals("android.intent.action.PACKAGE_REMOVED")) {
                            return;
                        }
                    } else if (!action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                        return;
                    }
                    Uri data = intent.getData();
                    if (data == null || (encodedSchemeSpecificPart = data.getEncodedSchemeSpecificPart()) == null) {
                        return;
                    }
                    MediaDataManager.this.removeAllForPackage(encodedSchemeSpecificPart);
                }
            }
        };
        this.appChangeReceiver = r2;
        dumpManager.registerDumpable("MediaDataManager", this);
        addInternalListener(mediaTimeoutListener);
        addInternalListener(mediaResumeListener);
        addInternalListener(mediaSessionBasedFilter);
        mediaSessionBasedFilter.addListener(mediaDeviceManager);
        mediaSessionBasedFilter.addListener(mediaDataCombineLatest);
        mediaDeviceManager.addListener(mediaDataCombineLatest);
        mediaDataCombineLatest.addListener(mediaDataFilter);
        mediaTimeoutListener.setTimeoutCallback(new AnonymousClass1());
        mediaResumeListener.setManager(this);
        mediaDataFilter.setMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this);
        broadcastDispatcher.registerReceiver(r2, new IntentFilter("android.intent.action.PACKAGES_SUSPENDED"), null, UserHandle.ALL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(r2, intentFilter);
        smartspaceMediaDataProvider.registerListener(this);
        Object systemService = context.getSystemService(SmartspaceManager.class);
        Intrinsics.checkNotNullExpressionValue(systemService, "context.getSystemService(SmartspaceManager::class.java)");
        SmartspaceSession createSmartspaceSession = ((SmartspaceManager) systemService).createSmartspaceSession(new SmartspaceConfig.Builder(context, SMARTSPACE_UI_SURFACE_LABEL).build());
        this.smartspaceSession = createSmartspaceSession;
        if (createSmartspaceSession != null) {
            createSmartspaceSession.addOnTargetsAvailableListener(Executors.newCachedThreadPool(), new SmartspaceSession.OnTargetsAvailableListener() { // from class: com.android.systemui.media.MediaDataManager$2$1
                public final void onTargetsAvailable(List<SmartspaceTarget> targets) {
                    SmartspaceMediaDataProvider smartspaceMediaDataProvider2;
                    smartspaceMediaDataProvider2 = MediaDataManager.this.smartspaceMediaDataProvider;
                    Intrinsics.checkNotNullExpressionValue(targets, "targets");
                    smartspaceMediaDataProvider2.onTargetsAvailable(targets);
                }
            });
        }
        SmartspaceSession smartspaceSession = this.smartspaceSession;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
        tunerService.addTunable(new TunerService.Tunable() { // from class: com.android.systemui.media.MediaDataManager.4
            {
                MediaDataManager.this = this;
            }

            @Override // com.android.systemui.tuner.TunerService.Tunable
            public void onTuningChanged(@Nullable String str, @Nullable String str2) {
                MediaDataManager mediaDataManager = MediaDataManager.this;
                mediaDataManager.allowMediaRecommendations = com.android.systemui.util.Utils.allowMediaRecommendations(mediaDataManager.context);
                if (!MediaDataManager.this.allowMediaRecommendations) {
                    MediaDataManager mediaDataManager2 = MediaDataManager.this;
                    mediaDataManager2.dismissSmartspaceRecommendation(mediaDataManager2.getSmartspaceMediaData().getTargetId(), 0L);
                }
            }
        }, "qs_media_recommend");
    }

    /* compiled from: MediaDataManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @NotNull
    public final SmartspaceMediaData getSmartspaceMediaData() {
        return this.smartspaceMediaData;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public MediaDataManager(@NotNull Context context, @NotNull Executor backgroundExecutor, @NotNull DelayableExecutor foregroundExecutor, @NotNull MediaControllerFactory mediaControllerFactory, @NotNull DumpManager dumpManager, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull MediaTimeoutListener mediaTimeoutListener, @NotNull MediaResumeListener mediaResumeListener, @NotNull MediaSessionBasedFilter mediaSessionBasedFilter, @NotNull MediaDeviceManager mediaDeviceManager, @NotNull MediaDataCombineLatest mediaDataCombineLatest, @NotNull MediaDataFilter mediaDataFilter, @NotNull ActivityStarter activityStarter, @NotNull SmartspaceMediaDataProvider smartspaceMediaDataProvider, @NotNull SystemClock clock, @NotNull TunerService tunerService) {
        this(context, backgroundExecutor, foregroundExecutor, mediaControllerFactory, broadcastDispatcher, dumpManager, mediaTimeoutListener, mediaResumeListener, mediaSessionBasedFilter, mediaDeviceManager, mediaDataCombineLatest, mediaDataFilter, activityStarter, smartspaceMediaDataProvider, com.android.systemui.util.Utils.useMediaResumption(context), com.android.systemui.util.Utils.useQsMediaPlayer(context), clock, tunerService);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(foregroundExecutor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(mediaControllerFactory, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(mediaTimeoutListener, "mediaTimeoutListener");
        Intrinsics.checkNotNullParameter(mediaResumeListener, "mediaResumeListener");
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "mediaSessionBasedFilter");
        Intrinsics.checkNotNullParameter(mediaDeviceManager, "mediaDeviceManager");
        Intrinsics.checkNotNullParameter(mediaDataCombineLatest, "mediaDataCombineLatest");
        Intrinsics.checkNotNullParameter(mediaDataFilter, "mediaDataFilter");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(smartspaceMediaDataProvider, "smartspaceMediaDataProvider");
        Intrinsics.checkNotNullParameter(clock, "clock");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MediaDataManager.kt */
    /* renamed from: com.android.systemui.media.MediaDataManager$1 */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function2<String, Boolean, Unit> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1() {
            super(2);
            MediaDataManager.this = r1;
        }

        @Override // kotlin.jvm.functions.Function2
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1950invoke(String str, Boolean bool) {
            invoke(str, bool.booleanValue());
            return Unit.INSTANCE;
        }

        public final void invoke(@NotNull String key, boolean z) {
            Intrinsics.checkNotNullParameter(key, "key");
            MediaDataManager.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(MediaDataManager.this, key, z, false, 4, null);
        }
    }

    public final void onNotificationAdded(@NotNull String key, @NotNull StatusBarNotification sbn) {
        MediaData mediaData;
        MediaData copy;
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(sbn, "sbn");
        if (this.useQsMediaPlayer && MediaDataManagerKt.isMediaNotification(sbn)) {
            Assert.isMainThread();
            String packageName = sbn.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName, "sbn.packageName");
            String findExistingEntry = findExistingEntry(key, packageName);
            if (findExistingEntry == null) {
                mediaData = MediaDataManagerKt.LOADING;
                String packageName2 = sbn.getPackageName();
                Intrinsics.checkNotNullExpressionValue(packageName2, "sbn.packageName");
                copy = mediaData.copy((i3 & 1) != 0 ? mediaData.userId : 0, (i3 & 2) != 0 ? mediaData.initialized : false, (i3 & 4) != 0 ? mediaData.backgroundColor : 0, (i3 & 8) != 0 ? mediaData.app : null, (i3 & 16) != 0 ? mediaData.appIcon : null, (i3 & 32) != 0 ? mediaData.artist : null, (i3 & 64) != 0 ? mediaData.song : null, (i3 & 128) != 0 ? mediaData.artwork : null, (i3 & 256) != 0 ? mediaData.actions : null, (i3 & 512) != 0 ? mediaData.actionsToShowInCompact : null, (i3 & 1024) != 0 ? mediaData.packageName : packageName2, (i3 & 2048) != 0 ? mediaData.token : null, (i3 & 4096) != 0 ? mediaData.clickIntent : null, (i3 & 8192) != 0 ? mediaData.device : null, (i3 & 16384) != 0 ? mediaData.active : false, (i3 & 32768) != 0 ? mediaData.resumeAction : null, (i3 & 65536) != 0 ? mediaData.isLocalSession : false, (i3 & 131072) != 0 ? mediaData.resumption : false, (i3 & 262144) != 0 ? mediaData.notificationKey : null, (i3 & 524288) != 0 ? mediaData.hasCheckedForResume : false, (i3 & 1048576) != 0 ? mediaData.isPlaying : null, (i3 & 2097152) != 0 ? mediaData.isClearable : false, (i3 & 4194304) != 0 ? mediaData.lastActive : 0L);
                this.mediaEntries.put(key, copy);
            } else if (!Intrinsics.areEqual(findExistingEntry, key)) {
                MediaData remove = this.mediaEntries.remove(findExistingEntry);
                Intrinsics.checkNotNull(remove);
                this.mediaEntries.put(key, remove);
            }
            loadMediaData(key, sbn, findExistingEntry);
            return;
        }
        onNotificationRemoved(key);
    }

    public final void removeAllForPackage(String str) {
        Assert.isMainThread();
        LinkedHashMap<String, MediaData> linkedHashMap = this.mediaEntries;
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
            if (Intrinsics.areEqual(entry.getValue().getPackageName(), str)) {
                linkedHashMap2.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry entry2 : linkedHashMap2.entrySet()) {
            removeEntry((String) entry2.getKey());
        }
    }

    public final void setResumeAction(@NotNull String key, @Nullable Runnable runnable) {
        Intrinsics.checkNotNullParameter(key, "key");
        MediaData mediaData = this.mediaEntries.get(key);
        if (mediaData == null) {
            return;
        }
        mediaData.setResumeAction(runnable);
        mediaData.setHasCheckedForResume(true);
    }

    public final void addResumptionControls(final int i, @NotNull final MediaDescription desc, @NotNull final Runnable action, @NotNull final MediaSession.Token token, @NotNull final String appName, @NotNull final PendingIntent appIntent, @NotNull final String packageName) {
        MediaData mediaData;
        MediaData copy;
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(appIntent, "appIntent");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        if (!this.mediaEntries.containsKey(packageName)) {
            mediaData = MediaDataManagerKt.LOADING;
            copy = mediaData.copy((i3 & 1) != 0 ? mediaData.userId : 0, (i3 & 2) != 0 ? mediaData.initialized : false, (i3 & 4) != 0 ? mediaData.backgroundColor : 0, (i3 & 8) != 0 ? mediaData.app : null, (i3 & 16) != 0 ? mediaData.appIcon : null, (i3 & 32) != 0 ? mediaData.artist : null, (i3 & 64) != 0 ? mediaData.song : null, (i3 & 128) != 0 ? mediaData.artwork : null, (i3 & 256) != 0 ? mediaData.actions : null, (i3 & 512) != 0 ? mediaData.actionsToShowInCompact : null, (i3 & 1024) != 0 ? mediaData.packageName : packageName, (i3 & 2048) != 0 ? mediaData.token : null, (i3 & 4096) != 0 ? mediaData.clickIntent : null, (i3 & 8192) != 0 ? mediaData.device : null, (i3 & 16384) != 0 ? mediaData.active : false, (i3 & 32768) != 0 ? mediaData.resumeAction : action, (i3 & 65536) != 0 ? mediaData.isLocalSession : false, (i3 & 131072) != 0 ? mediaData.resumption : false, (i3 & 262144) != 0 ? mediaData.notificationKey : null, (i3 & 524288) != 0 ? mediaData.hasCheckedForResume : true, (i3 & 1048576) != 0 ? mediaData.isPlaying : null, (i3 & 2097152) != 0 ? mediaData.isClearable : false, (i3 & 4194304) != 0 ? mediaData.lastActive : 0L);
            this.mediaEntries.put(packageName, copy);
        }
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$addResumptionControls$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataManager.this.loadMediaDataInBgForResumption(i, desc, action, token, appName, appIntent, packageName);
            }
        });
    }

    private final String findExistingEntry(String str, String str2) {
        if (this.mediaEntries.containsKey(str)) {
            return str;
        }
        if (!this.mediaEntries.containsKey(str2)) {
            return null;
        }
        return str2;
    }

    private final void loadMediaData(final String str, final StatusBarNotification statusBarNotification, final String str2) {
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$loadMediaData$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataManager.this.loadMediaDataInBg(str, statusBarNotification, str2);
            }
        });
    }

    public final void addListener(@NotNull Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.mediaDataFilter.addListener(listener);
    }

    public final void removeListener(@NotNull Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.mediaDataFilter.removeListener(listener);
    }

    private final boolean addInternalListener(Listener listener) {
        return this.internalListeners.add(listener);
    }

    private final void notifyMediaDataLoaded(String str, String str2, MediaData mediaData) {
        for (Listener listener : this.internalListeners) {
            Listener.DefaultImpls.onMediaDataLoaded$default(listener, str, str2, mediaData, false, false, 24, null);
        }
    }

    private final void notifySmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData) {
        for (Listener listener : this.internalListeners) {
            Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(listener, str, smartspaceMediaData, false, 4, null);
        }
    }

    private final void notifyMediaDataRemoved(String str) {
        for (Listener listener : this.internalListeners) {
            listener.onMediaDataRemoved(str);
        }
    }

    public final void notifySmartspaceMediaDataRemoved(String str, boolean z) {
        for (Listener listener : this.internalListeners) {
            listener.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    public static /* synthetic */ void setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(MediaDataManager mediaDataManager, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 4) != 0) {
            z2 = false;
        }
        mediaDataManager.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(str, z, z2);
    }

    public final void setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull String key, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(key, "key");
        MediaData mediaData = this.mediaEntries.get(key);
        if (mediaData == null) {
            return;
        }
        if (mediaData.getActive() == (!z) && !z2) {
            if (!mediaData.getResumption()) {
                return;
            }
            Log.d("MediaDataManager", Intrinsics.stringPlus("timing out resume player ", key));
            dismissMediaData(key, 0L);
            return;
        }
        mediaData.setActive(!z);
        Log.d("MediaDataManager", "Updating " + key + " timedOut: " + z);
        onMediaDataLoaded(key, key, mediaData);
    }

    public final void removeEntry(String str) {
        this.mediaEntries.remove(str);
        notifyMediaDataRemoved(str);
    }

    public final boolean dismissMediaData(@NotNull final String key, long j) {
        Intrinsics.checkNotNullParameter(key, "key");
        boolean z = this.mediaEntries.get(key) != null;
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$dismissMediaData$1
            @Override // java.lang.Runnable
            public final void run() {
                LinkedHashMap linkedHashMap;
                MediaSession.Token token;
                MediaControllerFactory mediaControllerFactory;
                linkedHashMap = MediaDataManager.this.mediaEntries;
                MediaData mediaData = (MediaData) linkedHashMap.get(key);
                if (mediaData == null) {
                    return;
                }
                MediaDataManager mediaDataManager = MediaDataManager.this;
                if (!mediaData.isLocalSession() || (token = mediaData.getToken()) == null) {
                    return;
                }
                mediaControllerFactory = mediaDataManager.mediaControllerFactory;
                mediaControllerFactory.create(token).getTransportControls().stop();
            }
        });
        this.foregroundExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$dismissMediaData$2
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataManager.this.removeEntry(key);
            }
        }, j);
        return z;
    }

    public final void dismissSmartspaceRecommendation(@NotNull String key, long j) {
        SmartspaceMediaData copy;
        Intrinsics.checkNotNullParameter(key, "key");
        if (!Intrinsics.areEqual(this.smartspaceMediaData.getTargetId(), key)) {
            return;
        }
        Log.d("MediaDataManager", "Dismissing Smartspace media target");
        if (this.smartspaceMediaData.isActive()) {
            copy = r0.copy((r18 & 1) != 0 ? r0.targetId : this.smartspaceMediaData.getTargetId(), (r18 & 2) != 0 ? r0.isActive : false, (r18 & 4) != 0 ? r0.isValid : false, (r18 & 8) != 0 ? r0.packageName : null, (r18 & 16) != 0 ? r0.cardAction : null, (r18 & 32) != 0 ? r0.recommendations : null, (r18 & 64) != 0 ? r0.dismissIntent : null, (r18 & 128) != 0 ? MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA().backgroundColor : 0);
            this.smartspaceMediaData = copy;
        }
        this.foregroundExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$dismissSmartspaceRecommendation$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataManager mediaDataManager = MediaDataManager.this;
                mediaDataManager.notifySmartspaceMediaDataRemoved(mediaDataManager.getSmartspaceMediaData().getTargetId(), true);
            }
        }, j);
    }

    public final void loadMediaDataInBgForResumption(final int i, final MediaDescription mediaDescription, final Runnable runnable, final MediaSession.Token token, final String str, final PendingIntent pendingIntent, final String str2) {
        if (TextUtils.isEmpty(mediaDescription.getTitle())) {
            Log.e("MediaDataManager", "Description incomplete");
            this.mediaEntries.remove(str2);
            return;
        }
        Log.d("MediaDataManager", "adding track for " + i + " from browser: " + mediaDescription);
        Bitmap iconBitmap = mediaDescription.getIconBitmap();
        if (iconBitmap == null && mediaDescription.getIconUri() != null) {
            Uri iconUri = mediaDescription.getIconUri();
            Intrinsics.checkNotNull(iconUri);
            iconBitmap = loadBitmapFromUri(iconUri);
        }
        final Icon createWithBitmap = iconBitmap != null ? Icon.createWithBitmap(iconBitmap) : null;
        final MediaAction resumeMediaAction = getResumeMediaAction(runnable);
        final long elapsedRealtime = this.systemClock.elapsedRealtime();
        this.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBgForResumption$1
            @Override // java.lang.Runnable
            public final void run() {
                int i2;
                List listOf;
                List listOf2;
                MediaDataManager mediaDataManager = MediaDataManager.this;
                String str3 = str2;
                int i3 = i;
                i2 = mediaDataManager.bgColor;
                String str4 = str;
                CharSequence subtitle = mediaDescription.getSubtitle();
                CharSequence title = mediaDescription.getTitle();
                Icon icon = createWithBitmap;
                listOf = CollectionsKt__CollectionsJVMKt.listOf(resumeMediaAction);
                listOf2 = CollectionsKt__CollectionsJVMKt.listOf(0);
                String str5 = str2;
                mediaDataManager.onMediaDataLoaded(str3, null, new MediaData(i3, true, i2, str4, null, subtitle, title, icon, listOf, listOf2, str5, token, pendingIntent, null, false, runnable, false, true, str5, true, null, false, elapsedRealtime, 3211264, null));
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v22, types: [java.lang.CharSequence, T] */
    /* JADX WARN: Type inference failed for: r3v32, types: [java.util.List, T] */
    /* JADX WARN: Type inference failed for: r3v33, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r3v34 */
    /* JADX WARN: Type inference failed for: r3v35 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r4v24, types: [java.lang.CharSequence, T] */
    public final void loadMediaDataInBg(final String str, final StatusBarNotification statusBarNotification, final String str2) {
        Icon createWithBitmap;
        List<Integer> mutableList;
        T t;
        Notification.Action[] actionArr;
        int i;
        Icon icon;
        final MediaSession.Token token = (MediaSession.Token) statusBarNotification.getNotification().extras.getParcelable("android.mediaSession");
        MediaController create = this.mediaControllerFactory.create(token);
        MediaMetadata metadata = create.getMetadata();
        final Notification notification = statusBarNotification.getNotification();
        Intrinsics.checkNotNullExpressionValue(notification, "sbn.notification");
        Bitmap bitmap = metadata == null ? null : metadata.getBitmap("android.media.metadata.ART");
        if (bitmap == null) {
            bitmap = metadata == null ? null : metadata.getBitmap("android.media.metadata.ALBUM_ART");
        }
        if (bitmap == null && metadata != null) {
            bitmap = loadBitmapFromUri(metadata);
        }
        if (bitmap == null) {
            createWithBitmap = notification.getLargeIcon();
        } else {
            createWithBitmap = Icon.createWithBitmap(bitmap);
        }
        final Icon icon2 = createWithBitmap;
        int i2 = 0;
        if (icon2 != null && bitmap == null) {
            if (icon2.getType() == 1 || icon2.getType() == 5) {
                icon2.getBitmap();
            } else {
                Drawable loadDrawable = icon2.loadDrawable(this.context);
                Intrinsics.checkNotNullExpressionValue(loadDrawable, "artWorkIcon.loadDrawable(context)");
                Canvas canvas = new Canvas(Bitmap.createBitmap(loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888));
                loadDrawable.setBounds(0, 0, loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight());
                loadDrawable.draw(canvas);
            }
        }
        final String loadHeaderAppName = Notification.Builder.recoverBuilder(this.context, notification).loadHeaderAppName();
        final Icon smallIcon = statusBarNotification.getNotification().getSmallIcon();
        final Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        T string = metadata == null ? 0 : metadata.getString("android.media.metadata.DISPLAY_TITLE");
        ref$ObjectRef.element = string;
        if (string == 0) {
            ref$ObjectRef.element = metadata == null ? 0 : metadata.getString("android.media.metadata.TITLE");
        }
        if (ref$ObjectRef.element == 0) {
            ref$ObjectRef.element = HybridGroupManager.resolveTitle(notification);
        }
        final Ref$ObjectRef ref$ObjectRef2 = new Ref$ObjectRef();
        T string2 = metadata == null ? 0 : metadata.getString("android.media.metadata.ARTIST");
        ref$ObjectRef2.element = string2;
        if (string2 == 0) {
            ref$ObjectRef2.element = HybridGroupManager.resolveText(notification);
        }
        final ArrayList arrayList = new ArrayList();
        Notification.Action[] actionArr2 = notification.actions;
        final Ref$ObjectRef ref$ObjectRef3 = new Ref$ObjectRef();
        int[] intArray = notification.extras.getIntArray("android.compactActions");
        if (intArray == null) {
            t = 0;
        } else {
            mutableList = ArraysKt___ArraysKt.toMutableList(intArray);
            t = mutableList;
        }
        if (t == 0) {
            t = new ArrayList();
        }
        ref$ObjectRef3.element = t;
        int size = ((List) t).size();
        int i3 = MAX_COMPACT_ACTIONS;
        if (size > i3) {
            Log.e("MediaDataManager", "Too many compact actions for " + str + ", limiting to first " + i3);
            i2 = 0;
            ref$ObjectRef3.element = ((List) ref$ObjectRef3.element).subList(0, i3);
        }
        if (actionArr2 != null) {
            int length = actionArr2.length;
            int i4 = i2;
            while (i4 < length) {
                final Notification.Action action = actionArr2[i4];
                int i5 = i4 + 1;
                if (action.getIcon() == null) {
                    actionArr = actionArr2;
                    StringBuilder sb = new StringBuilder();
                    i = length;
                    sb.append("No icon for action ");
                    sb.append(i4);
                    sb.append(' ');
                    sb.append((Object) action.title);
                    Log.i("MediaDataManager", sb.toString());
                    ((List) ref$ObjectRef3.element).remove(Integer.valueOf(i4));
                } else {
                    actionArr = actionArr2;
                    i = length;
                    Runnable runnable = action.actionIntent != null ? new Runnable() { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBg$runnable$1

                        /* compiled from: MediaDataManager.kt */
                        /* renamed from: com.android.systemui.media.MediaDataManager$loadMediaDataInBg$runnable$1$2  reason: invalid class name */
                        /* loaded from: classes.dex */
                        static final class AnonymousClass2 implements Runnable {
                            public static final AnonymousClass2 INSTANCE = new AnonymousClass2();

                            AnonymousClass2() {
                            }

                            @Override // java.lang.Runnable
                            public final void run() {
                            }
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            ActivityStarter activityStarter;
                            if (action.isAuthenticationRequired()) {
                                activityStarter = this.activityStarter;
                                final MediaDataManager mediaDataManager = this;
                                final Notification.Action action2 = action;
                                activityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBg$runnable$1.1
                                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                                    public final boolean onDismiss() {
                                        boolean sendPendingIntent;
                                        MediaDataManager mediaDataManager2 = MediaDataManager.this;
                                        PendingIntent pendingIntent = action2.actionIntent;
                                        Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
                                        sendPendingIntent = mediaDataManager2.sendPendingIntent(pendingIntent);
                                        return sendPendingIntent;
                                    }
                                }, AnonymousClass2.INSTANCE, true);
                                return;
                            }
                            MediaDataManager mediaDataManager2 = this;
                            PendingIntent pendingIntent = action.actionIntent;
                            Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
                            mediaDataManager2.sendPendingIntent(pendingIntent);
                        }
                    } : null;
                    Icon icon3 = action.getIcon();
                    Integer valueOf = icon3 == null ? null : Integer.valueOf(icon3.getType());
                    if (valueOf != null && valueOf.intValue() == 2) {
                        String packageName = statusBarNotification.getPackageName();
                        Icon icon4 = action.getIcon();
                        Intrinsics.checkNotNull(icon4);
                        icon = Icon.createWithResource(packageName, icon4.getResId());
                    } else {
                        icon = action.getIcon();
                    }
                    arrayList.add(new MediaAction(icon.setTint(this.themeText), runnable, action.title));
                }
                i4 = i5;
                actionArr2 = actionArr;
                length = i;
            }
        }
        MediaController.PlaybackInfo playbackInfo = create.getPlaybackInfo();
        Integer valueOf2 = playbackInfo == null ? null : Integer.valueOf(playbackInfo.getPlaybackType());
        boolean z = valueOf2 != null && valueOf2.intValue() == 1;
        PlaybackState playbackState = create.getPlaybackState();
        Boolean valueOf3 = playbackState == null ? null : Boolean.valueOf(NotificationMediaManager.isPlayingState(playbackState.getState()));
        final long elapsedRealtime = this.systemClock.elapsedRealtime();
        final boolean z2 = z;
        final Boolean bool = valueOf3;
        this.foregroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBg$1
            @Override // java.lang.Runnable
            public final void run() {
                LinkedHashMap linkedHashMap;
                LinkedHashMap linkedHashMap2;
                LinkedHashMap linkedHashMap3;
                int i6;
                linkedHashMap = MediaDataManager.this.mediaEntries;
                MediaData mediaData = (MediaData) linkedHashMap.get(str);
                Boolean bool2 = null;
                Runnable resumeAction = mediaData == null ? null : mediaData.getResumeAction();
                linkedHashMap2 = MediaDataManager.this.mediaEntries;
                MediaData mediaData2 = (MediaData) linkedHashMap2.get(str);
                if (mediaData2 != null) {
                    bool2 = Boolean.valueOf(mediaData2.getHasCheckedForResume());
                }
                boolean areEqual = Intrinsics.areEqual(bool2, Boolean.TRUE);
                linkedHashMap3 = MediaDataManager.this.mediaEntries;
                MediaData mediaData3 = (MediaData) linkedHashMap3.get(str);
                boolean active = mediaData3 == null ? true : mediaData3.getActive();
                MediaDataManager mediaDataManager = MediaDataManager.this;
                String str3 = str;
                String str4 = str2;
                int normalizedUserId = statusBarNotification.getNormalizedUserId();
                i6 = MediaDataManager.this.bgColor;
                String packageName2 = statusBarNotification.getPackageName();
                Intrinsics.checkNotNullExpressionValue(packageName2, "sbn.packageName");
                mediaDataManager.onMediaDataLoaded(str3, str4, new MediaData(normalizedUserId, true, i6, loadHeaderAppName, smallIcon, ref$ObjectRef2.element, ref$ObjectRef.element, icon2, arrayList, ref$ObjectRef3.element, packageName2, token, notification.contentIntent, null, active, resumeAction, z2, false, str, areEqual, bool, statusBarNotification.isClearable(), elapsedRealtime, 131072, null));
            }
        });
    }

    private final Bitmap loadBitmapFromUri(MediaMetadata mediaMetadata) {
        String[] strArr;
        strArr = MediaDataManagerKt.ART_URIS;
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String str = strArr[i];
            i++;
            String string = mediaMetadata.getString(str);
            if (!TextUtils.isEmpty(string)) {
                Uri parse = Uri.parse(string);
                Intrinsics.checkNotNullExpressionValue(parse, "parse(uriString)");
                Bitmap loadBitmapFromUri = loadBitmapFromUri(parse);
                if (loadBitmapFromUri != null) {
                    Log.d("MediaDataManager", Intrinsics.stringPlus("loaded art from ", str));
                    return loadBitmapFromUri;
                }
            }
        }
        return null;
    }

    public final boolean sendPendingIntent(PendingIntent pendingIntent) {
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            Log.d("MediaDataManager", "Intent canceled", e);
            return false;
        }
    }

    private final Bitmap loadBitmapFromUri(Uri uri) {
        if (uri.getScheme() == null) {
            return null;
        }
        if (!uri.getScheme().equals("content") && !uri.getScheme().equals("android.resource") && !uri.getScheme().equals("file")) {
            return null;
        }
        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.context.getContentResolver(), uri), MediaDataManager$loadBitmapFromUri$1.INSTANCE);
        } catch (IOException e) {
            Log.e("MediaDataManager", "Unable to load bitmap", e);
            return null;
        } catch (RuntimeException e2) {
            Log.e("MediaDataManager", "Unable to load bitmap", e2);
            return null;
        }
    }

    private final MediaAction getResumeMediaAction(Runnable runnable) {
        return new MediaAction(Icon.createWithResource(this.context, R$drawable.lb_ic_play).setTint(this.themeText), runnable, this.context.getString(R$string.controls_media_resume));
    }

    public final void onMediaDataLoaded(@NotNull String key, @Nullable String str, @NotNull MediaData data) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        Assert.isMainThread();
        if (this.mediaEntries.containsKey(key)) {
            this.mediaEntries.put(key, data);
            notifyMediaDataLoaded(key, str, data);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
    public void onSmartspaceTargetsUpdated(@NotNull List<? extends Parcelable> targets) {
        SmartspaceMediaData copy;
        Intrinsics.checkNotNullParameter(targets, "targets");
        if (!this.allowMediaRecommendations) {
            Log.d("MediaDataManager", "Smartspace recommendation is disabled in Settings.");
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : targets) {
            if (obj instanceof SmartspaceTarget) {
                arrayList.add(obj);
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            if (!this.smartspaceMediaData.isActive()) {
                return;
            }
            Log.d("MediaDataManager", "Set Smartspace media to be inactive for the data update");
            copy = r3.copy((r18 & 1) != 0 ? r3.targetId : this.smartspaceMediaData.getTargetId(), (r18 & 2) != 0 ? r3.isActive : false, (r18 & 4) != 0 ? r3.isValid : false, (r18 & 8) != 0 ? r3.packageName : null, (r18 & 16) != 0 ? r3.cardAction : null, (r18 & 32) != 0 ? r3.recommendations : null, (r18 & 64) != 0 ? r3.dismissIntent : null, (r18 & 128) != 0 ? MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA().backgroundColor : 0);
            this.smartspaceMediaData = copy;
            notifySmartspaceMediaDataRemoved(copy.getTargetId(), false);
        } else if (size == 1) {
            SmartspaceTarget smartspaceTarget = (SmartspaceTarget) arrayList.get(0);
            if (Intrinsics.areEqual(this.smartspaceMediaData.getTargetId(), smartspaceTarget.getSmartspaceTargetId())) {
                return;
            }
            Log.d("MediaDataManager", "Forwarding Smartspace media update.");
            SmartspaceMediaData smartspaceMediaData = toSmartspaceMediaData(smartspaceTarget, true);
            this.smartspaceMediaData = smartspaceMediaData;
            notifySmartspaceMediaDataLoaded(smartspaceMediaData.getTargetId(), this.smartspaceMediaData);
        } else {
            Log.wtf("MediaDataManager", "More than 1 Smartspace Media Update. Resetting the status...");
            notifySmartspaceMediaDataRemoved(this.smartspaceMediaData.getTargetId(), false);
            this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
        }
    }

    public final void onNotificationRemoved(@NotNull String key) {
        List listOf;
        List listOf2;
        MediaData copy;
        Intrinsics.checkNotNullParameter(key, "key");
        Assert.isMainThread();
        MediaData remove = this.mediaEntries.remove(key);
        if (this.useMediaResumption) {
            Boolean bool = null;
            if ((remove == null ? null : remove.getResumeAction()) != null) {
                if (remove != null) {
                    bool = Boolean.valueOf(remove.isLocalSession());
                }
                if (bool.booleanValue()) {
                    Log.d("MediaDataManager", "Not removing " + key + " because resumable");
                    Runnable resumeAction = remove.getResumeAction();
                    Intrinsics.checkNotNull(resumeAction);
                    listOf = CollectionsKt__CollectionsJVMKt.listOf(getResumeMediaAction(resumeAction));
                    boolean z = false;
                    listOf2 = CollectionsKt__CollectionsJVMKt.listOf(0);
                    copy = remove.copy((i3 & 1) != 0 ? remove.userId : 0, (i3 & 2) != 0 ? remove.initialized : false, (i3 & 4) != 0 ? remove.backgroundColor : 0, (i3 & 8) != 0 ? remove.app : null, (i3 & 16) != 0 ? remove.appIcon : null, (i3 & 32) != 0 ? remove.artist : null, (i3 & 64) != 0 ? remove.song : null, (i3 & 128) != 0 ? remove.artwork : null, (i3 & 256) != 0 ? remove.actions : listOf, (i3 & 512) != 0 ? remove.actionsToShowInCompact : listOf2, (i3 & 1024) != 0 ? remove.packageName : null, (i3 & 2048) != 0 ? remove.token : null, (i3 & 4096) != 0 ? remove.clickIntent : null, (i3 & 8192) != 0 ? remove.device : null, (i3 & 16384) != 0 ? remove.active : false, (i3 & 32768) != 0 ? remove.resumeAction : null, (i3 & 65536) != 0 ? remove.isLocalSession : false, (i3 & 131072) != 0 ? remove.resumption : true, (i3 & 262144) != 0 ? remove.notificationKey : null, (i3 & 524288) != 0 ? remove.hasCheckedForResume : false, (i3 & 1048576) != 0 ? remove.isPlaying : null, (i3 & 2097152) != 0 ? remove.isClearable : true, (i3 & 4194304) != 0 ? remove.lastActive : 0L);
                    String packageName = remove.getPackageName();
                    if (this.mediaEntries.put(packageName, copy) == null) {
                        z = true;
                    }
                    if (z) {
                        notifyMediaDataLoaded(packageName, key, copy);
                        return;
                    }
                    notifyMediaDataRemoved(key);
                    notifyMediaDataLoaded(packageName, packageName, copy);
                    return;
                }
            }
        }
        if (remove != null) {
            notifyMediaDataRemoved(key);
        }
    }

    public final void setMediaResumptionEnabled(boolean z) {
        if (this.useMediaResumption == z) {
            return;
        }
        this.useMediaResumption = z;
        if (z) {
            return;
        }
        LinkedHashMap<String, MediaData> linkedHashMap = this.mediaEntries;
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
            if (!entry.getValue().getActive()) {
                linkedHashMap2.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry entry2 : linkedHashMap2.entrySet()) {
            this.mediaEntries.remove(entry2.getKey());
            notifyMediaDataRemoved((String) entry2.getKey());
        }
    }

    public final void onSwipeToDismiss() {
        this.mediaDataFilter.onSwipeToDismiss();
    }

    public final boolean hasActiveMedia() {
        return this.mediaDataFilter.hasActiveMedia();
    }

    public final boolean hasAnyMedia() {
        return this.mediaDataFilter.hasAnyMedia();
    }

    /* compiled from: MediaDataManager.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, boolean z2);

        void onMediaDataRemoved(@NotNull String str);

        void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z);

        void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z);

        /* compiled from: MediaDataManager.kt */
        /* loaded from: classes.dex */
        public static final class DefaultImpls {
            public static void onMediaDataRemoved(@NotNull Listener listener, @NotNull String key) {
                Intrinsics.checkNotNullParameter(listener, "this");
                Intrinsics.checkNotNullParameter(key, "key");
            }

            public static void onSmartspaceMediaDataLoaded(@NotNull Listener listener, @NotNull String key, @NotNull SmartspaceMediaData data, boolean z) {
                Intrinsics.checkNotNullParameter(listener, "this");
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(data, "data");
            }

            public static void onSmartspaceMediaDataRemoved(@NotNull Listener listener, @NotNull String key, boolean z) {
                Intrinsics.checkNotNullParameter(listener, "this");
                Intrinsics.checkNotNullParameter(key, "key");
            }

            public static /* synthetic */ void onMediaDataLoaded$default(Listener listener, String str, String str2, MediaData mediaData, boolean z, boolean z2, int i, Object obj) {
                if (obj == null) {
                    if ((i & 8) != 0) {
                        z = true;
                    }
                    boolean z3 = z;
                    if ((i & 16) != 0) {
                        z2 = false;
                    }
                    listener.onMediaDataLoaded(str, str2, mediaData, z3, z2);
                    return;
                }
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onMediaDataLoaded");
            }

            public static /* synthetic */ void onSmartspaceMediaDataLoaded$default(Listener listener, String str, SmartspaceMediaData smartspaceMediaData, boolean z, int i, Object obj) {
                if (obj == null) {
                    if ((i & 4) != 0) {
                        z = false;
                    }
                    listener.onSmartspaceMediaDataLoaded(str, smartspaceMediaData, z);
                    return;
                }
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onSmartspaceMediaDataLoaded");
            }
        }
    }

    private final SmartspaceMediaData toSmartspaceMediaData(SmartspaceTarget smartspaceTarget, boolean z) {
        Intent intent;
        SmartspaceMediaData copy;
        if (smartspaceTarget.getBaseAction() == null || smartspaceTarget.getBaseAction().getExtras() == null) {
            intent = null;
        } else {
            Parcelable parcelable = smartspaceTarget.getBaseAction().getExtras().getParcelable("dismiss_intent");
            Objects.requireNonNull(parcelable, "null cannot be cast to non-null type android.content.Intent");
            intent = (Intent) parcelable;
        }
        Intent intent2 = intent;
        String packageName = packageName(smartspaceTarget);
        if (packageName != null) {
            String smartspaceTargetId = smartspaceTarget.getSmartspaceTargetId();
            Intrinsics.checkNotNullExpressionValue(smartspaceTargetId, "target.smartspaceTargetId");
            SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
            List iconGrid = smartspaceTarget.getIconGrid();
            Intrinsics.checkNotNullExpressionValue(iconGrid, "target.iconGrid");
            return new SmartspaceMediaData(smartspaceTargetId, z, true, packageName, baseAction, iconGrid, intent2, 0);
        }
        SmartspaceMediaData empty_smartspace_media_data = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
        String smartspaceTargetId2 = smartspaceTarget.getSmartspaceTargetId();
        Intrinsics.checkNotNullExpressionValue(smartspaceTargetId2, "target.smartspaceTargetId");
        copy = empty_smartspace_media_data.copy((r18 & 1) != 0 ? empty_smartspace_media_data.targetId : smartspaceTargetId2, (r18 & 2) != 0 ? empty_smartspace_media_data.isActive : z, (r18 & 4) != 0 ? empty_smartspace_media_data.isValid : false, (r18 & 8) != 0 ? empty_smartspace_media_data.packageName : null, (r18 & 16) != 0 ? empty_smartspace_media_data.cardAction : null, (r18 & 32) != 0 ? empty_smartspace_media_data.recommendations : null, (r18 & 64) != 0 ? empty_smartspace_media_data.dismissIntent : intent2, (r18 & 128) != 0 ? empty_smartspace_media_data.backgroundColor : 0);
        return copy;
    }

    private final String packageName(SmartspaceTarget smartspaceTarget) {
        String string;
        List<SmartspaceAction> iconGrid = smartspaceTarget.getIconGrid();
        if (iconGrid == null || iconGrid.isEmpty()) {
            Log.w("MediaDataManager", "Empty or null media recommendation list.");
            return null;
        }
        for (SmartspaceAction smartspaceAction : iconGrid) {
            Bundle extras = smartspaceAction.getExtras();
            if (extras != null && (string = extras.getString(EXTRAS_MEDIA_SOURCE_PACKAGE_NAME)) != null) {
                return string;
            }
        }
        Log.w("MediaDataManager", "No valid package name is provided.");
        return null;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println(Intrinsics.stringPlus("internalListeners: ", this.internalListeners));
        pw.println(Intrinsics.stringPlus("externalListeners: ", this.mediaDataFilter.getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        pw.println(Intrinsics.stringPlus("mediaEntries: ", this.mediaEntries));
        pw.println(Intrinsics.stringPlus("useMediaResumption: ", Boolean.valueOf(this.useMediaResumption)));
    }
}
