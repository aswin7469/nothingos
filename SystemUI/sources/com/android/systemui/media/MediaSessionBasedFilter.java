package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000{\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007*\u0001\u0016\u0018\u00002\u00020\u0001B+\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0001J*\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\f2\b\u0010\u001f\u001a\u0004\u0018\u00010\f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001aH\u0002J\u0010\u0010#\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\fH\u0002J\u0018\u0010$\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010 \u001a\u00020%H\u0002J\u0018\u0010&\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\"\u001a\u00020\u001aH\u0002J\u0016\u0010'\u001a\u00020\u001d2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00130)H\u0002J:\u0010*\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\f2\b\u0010\u001f\u001a\u0004\u0018\u00010\f2\u0006\u0010+\u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001aH\u0016J\u0010\u0010/\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\fH\u0016J \u00100\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010+\u001a\u00020%2\u0006\u00101\u001a\u00020\u001aH\u0016J\u0018\u00102\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\"\u001a\u00020\u001aH\u0016J\u000e\u00103\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0001R\u000e\u0010\b\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R \u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u000bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\rX\u0004¢\u0006\u0002\n\u0000R6\u0010\u0010\u001a*\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00120\u0011j\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0012`\u0014X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0004\n\u0002\u0010\u0017R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0004¢\u0006\u0002\n\u0000¨\u00064"}, mo65043d2 = {"Lcom/android/systemui/media/MediaSessionBasedFilter;", "Lcom/android/systemui/media/MediaDataManager$Listener;", "context", "Landroid/content/Context;", "sessionManager", "Landroid/media/session/MediaSessionManager;", "foregroundExecutor", "Ljava/util/concurrent/Executor;", "backgroundExecutor", "(Landroid/content/Context;Landroid/media/session/MediaSessionManager;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)V", "keyedTokens", "", "", "", "Landroid/media/session/MediaSession$Token;", "listeners", "packageControllers", "Ljava/util/LinkedHashMap;", "", "Landroid/media/session/MediaController;", "Lkotlin/collections/LinkedHashMap;", "sessionListener", "com/android/systemui/media/MediaSessionBasedFilter$sessionListener$1", "Lcom/android/systemui/media/MediaSessionBasedFilter$sessionListener$1;", "tokensWithNotifications", "addListener", "", "listener", "dispatchMediaDataLoaded", "", "key", "oldKey", "info", "Lcom/android/systemui/media/MediaData;", "immediately", "dispatchMediaDataRemoved", "dispatchSmartspaceMediaDataLoaded", "Lcom/android/systemui/media/SmartspaceMediaData;", "dispatchSmartspaceMediaDataRemoved", "handleControllersChanged", "controllers", "", "onMediaDataLoaded", "data", "receivedSmartspaceCardLatency", "", "isSsReactivated", "onMediaDataRemoved", "onSmartspaceMediaDataLoaded", "shouldPrioritize", "onSmartspaceMediaDataRemoved", "removeListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter implements MediaDataManager.Listener {
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;
    private final Map<String, Set<MediaSession.Token>> keyedTokens = new LinkedHashMap();
    private final Set<MediaDataManager.Listener> listeners = new LinkedHashSet();
    private final LinkedHashMap<String, List<MediaController>> packageControllers = new LinkedHashMap<>();
    private final MediaSessionBasedFilter$sessionListener$1 sessionListener = new MediaSessionBasedFilter$sessionListener$1(this);
    private final MediaSessionManager sessionManager;
    private final Set<MediaSession.Token> tokensWithNotifications = new LinkedHashSet();

    @Inject
    public MediaSessionBasedFilter(Context context, MediaSessionManager mediaSessionManager, @Main Executor executor, @Background Executor executor2) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mediaSessionManager, "sessionManager");
        Intrinsics.checkNotNullParameter(executor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(executor2, "backgroundExecutor");
        this.sessionManager = mediaSessionManager;
        this.foregroundExecutor = executor;
        this.backgroundExecutor = executor2;
        executor2.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda2(context, this));
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-0  reason: not valid java name */
    public static final void m2822_init_$lambda0(Context context, MediaSessionBasedFilter mediaSessionBasedFilter) {
        Intrinsics.checkNotNullParameter(context, "$context");
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        ComponentName componentName = new ComponentName(context, NotificationListenerWithPlugins.class);
        mediaSessionBasedFilter.sessionManager.addOnActiveSessionsChangedListener(mediaSessionBasedFilter.sessionListener, componentName);
        List<MediaController> activeSessions = mediaSessionBasedFilter.sessionManager.getActiveSessions(componentName);
        Intrinsics.checkNotNullExpressionValue(activeSessions, "sessionManager.getActiveSessions(name)");
        mediaSessionBasedFilter.handleControllersChanged(activeSessions);
    }

    public final boolean addListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    public final boolean removeListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.remove(listener);
    }

    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda1(mediaData, str2, str, this, z));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: android.media.session.MediaController} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* renamed from: onMediaDataLoaded$lambda-6  reason: not valid java name */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void m2827onMediaDataLoaded$lambda6(com.android.systemui.media.MediaData r9, java.lang.String r10, java.lang.String r11, com.android.systemui.media.MediaSessionBasedFilter r12, boolean r13) {
        /*
            java.lang.String r0 = "$data"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
            java.lang.String r0 = "$key"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "this$0"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            android.media.session.MediaSession$Token r0 = r9.getToken()
            if (r0 == 0) goto L_0x001b
            java.util.Set<android.media.session.MediaSession$Token> r1 = r12.tokensWithNotifications
            r1.add(r0)
        L_0x001b:
            r0 = 1
            r1 = 0
            if (r10 == 0) goto L_0x0027
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r11, (java.lang.Object) r10)
            if (r2 != 0) goto L_0x0027
            r2 = r0
            goto L_0x0028
        L_0x0027:
            r2 = r1
        L_0x0028:
            if (r2 == 0) goto L_0x0040
            java.util.Map<java.lang.String, java.util.Set<android.media.session.MediaSession$Token>> r3 = r12.keyedTokens
            java.util.Map r3 = kotlin.jvm.internal.TypeIntrinsics.asMutableMap(r3)
            java.lang.Object r3 = r3.remove(r10)
            java.util.Set r3 = (java.util.Set) r3
            if (r3 == 0) goto L_0x0040
            java.util.Map<java.lang.String, java.util.Set<android.media.session.MediaSession$Token>> r4 = r12.keyedTokens
            java.lang.Object r3 = r4.put(r11, r3)
            java.util.Set r3 = (java.util.Set) r3
        L_0x0040:
            android.media.session.MediaSession$Token r3 = r9.getToken()
            if (r3 == 0) goto L_0x0070
            java.util.Map<java.lang.String, java.util.Set<android.media.session.MediaSession$Token>> r3 = r12.keyedTokens
            java.lang.Object r3 = r3.get(r11)
            java.util.Set r3 = (java.util.Set) r3
            if (r3 == 0) goto L_0x005c
            android.media.session.MediaSession$Token r4 = r9.getToken()
            boolean r3 = r3.add(r4)
            java.lang.Boolean.valueOf((boolean) r3)
            goto L_0x0070
        L_0x005c:
            android.media.session.MediaSession$Token[] r3 = new android.media.session.MediaSession.Token[r0]
            android.media.session.MediaSession$Token r4 = r9.getToken()
            r3[r1] = r4
            java.util.Set r3 = kotlin.collections.SetsKt.mutableSetOf(r3)
            java.util.Map<java.lang.String, java.util.Set<android.media.session.MediaSession$Token>> r4 = r12.keyedTokens
            java.lang.Object r3 = r4.put(r11, r3)
            java.util.Set r3 = (java.util.Set) r3
        L_0x0070:
            java.util.LinkedHashMap<java.lang.String, java.util.List<android.media.session.MediaController>> r3 = r12.packageControllers
            java.lang.String r4 = r9.getPackageName()
            java.lang.Object r3 = r3.get(r4)
            java.util.List r3 = (java.util.List) r3
            r4 = 0
            if (r3 == 0) goto L_0x00b2
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.util.Collection r5 = (java.util.Collection) r5
            java.util.Iterator r3 = r3.iterator()
        L_0x008c:
            boolean r6 = r3.hasNext()
            if (r6 == 0) goto L_0x00af
            java.lang.Object r6 = r3.next()
            r7 = r6
            android.media.session.MediaController r7 = (android.media.session.MediaController) r7
            android.media.session.MediaController$PlaybackInfo r7 = r7.getPlaybackInfo()
            if (r7 == 0) goto L_0x00a8
            int r7 = r7.getPlaybackType()
            r8 = 2
            if (r7 != r8) goto L_0x00a8
            r7 = r0
            goto L_0x00a9
        L_0x00a8:
            r7 = r1
        L_0x00a9:
            if (r7 == 0) goto L_0x008c
            r5.add(r6)
            goto L_0x008c
        L_0x00af:
            java.util.List r5 = (java.util.List) r5
            goto L_0x00b3
        L_0x00b2:
            r5 = r4
        L_0x00b3:
            if (r5 == 0) goto L_0x00bc
            int r3 = r5.size()
            if (r3 != r0) goto L_0x00bc
            goto L_0x00bd
        L_0x00bc:
            r0 = r1
        L_0x00bd:
            if (r0 == 0) goto L_0x00c6
            java.lang.Object r0 = kotlin.collections.CollectionsKt.firstOrNull(r5)
            r4 = r0
            android.media.session.MediaController r4 = (android.media.session.MediaController) r4
        L_0x00c6:
            if (r2 != 0) goto L_0x012e
            if (r4 == 0) goto L_0x012e
            android.media.session.MediaSession$Token r0 = r4.getSessionToken()
            android.media.session.MediaSession$Token r1 = r9.getToken()
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r1)
            if (r0 != 0) goto L_0x012e
            java.util.Set<android.media.session.MediaSession$Token> r0 = r12.tokensWithNotifications
            android.media.session.MediaSession$Token r1 = r4.getSessionToken()
            boolean r0 = r0.contains(r1)
            if (r0 != 0) goto L_0x00e5
            goto L_0x012e
        L_0x00e5:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r13 = "filtering key="
            r10.<init>((java.lang.String) r13)
            java.lang.StringBuilder r10 = r10.append((java.lang.String) r11)
            java.lang.String r13 = " local="
            java.lang.StringBuilder r10 = r10.append((java.lang.String) r13)
            android.media.session.MediaSession$Token r9 = r9.getToken()
            java.lang.StringBuilder r9 = r10.append((java.lang.Object) r9)
            java.lang.String r10 = " remote="
            java.lang.StringBuilder r9 = r9.append((java.lang.String) r10)
            android.media.session.MediaSession$Token r10 = r4.getSessionToken()
            java.lang.StringBuilder r9 = r9.append((java.lang.Object) r10)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "MediaSessionBasedFilter"
            android.util.Log.d(r10, r9)
            java.util.Map<java.lang.String, java.util.Set<android.media.session.MediaSession$Token>> r9 = r12.keyedTokens
            java.lang.Object r9 = r9.get(r11)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r9)
            java.util.Set r9 = (java.util.Set) r9
            android.media.session.MediaSession$Token r10 = r4.getSessionToken()
            boolean r9 = r9.contains(r10)
            if (r9 != 0) goto L_0x0131
            r12.dispatchMediaDataRemoved(r11)
            goto L_0x0131
        L_0x012e:
            r12.dispatchMediaDataLoaded(r11, r10, r9, r13)
        L_0x0131:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaSessionBasedFilter.m2827onMediaDataLoaded$lambda6(com.android.systemui.media.MediaData, java.lang.String, java.lang.String, com.android.systemui.media.MediaSessionBasedFilter, boolean):void");
    }

    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda3(this, str, smartspaceMediaData));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSmartspaceMediaDataLoaded$lambda-7  reason: not valid java name */
    public static final void m2829onSmartspaceMediaDataLoaded$lambda7(MediaSessionBasedFilter mediaSessionBasedFilter, String str, SmartspaceMediaData smartspaceMediaData) {
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "$data");
        mediaSessionBasedFilter.dispatchSmartspaceMediaDataLoaded(str, smartspaceMediaData);
    }

    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda6(this, str));
    }

    /* access modifiers changed from: private */
    /* renamed from: onMediaDataRemoved$lambda-8  reason: not valid java name */
    public static final void m2828onMediaDataRemoved$lambda8(MediaSessionBasedFilter mediaSessionBasedFilter, String str) {
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        mediaSessionBasedFilter.keyedTokens.remove(str);
        mediaSessionBasedFilter.dispatchMediaDataRemoved(str);
    }

    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda8(this, str, z));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSmartspaceMediaDataRemoved$lambda-9  reason: not valid java name */
    public static final void m2830onSmartspaceMediaDataRemoved$lambda9(MediaSessionBasedFilter mediaSessionBasedFilter, String str, boolean z) {
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        mediaSessionBasedFilter.dispatchSmartspaceMediaDataRemoved(str, z);
    }

    private final void dispatchMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda7(this, str, str2, mediaData, z));
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchMediaDataLoaded$lambda-11  reason: not valid java name */
    public static final void m2823dispatchMediaDataLoaded$lambda11(MediaSessionBasedFilter mediaSessionBasedFilter, String str, String str2, MediaData mediaData, boolean z) {
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        Intrinsics.checkNotNullParameter(mediaData, "$info");
        for (MediaDataManager.Listener onMediaDataLoaded$default : CollectionsKt.toSet(mediaSessionBasedFilter.listeners)) {
            MediaDataManager.Listener.onMediaDataLoaded$default(onMediaDataLoaded$default, str, str2, mediaData, z, 0, false, 48, (Object) null);
        }
    }

    private final void dispatchMediaDataRemoved(String str) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda0(this, str));
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchMediaDataRemoved$lambda-13  reason: not valid java name */
    public static final void m2824dispatchMediaDataRemoved$lambda13(MediaSessionBasedFilter mediaSessionBasedFilter, String str) {
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        for (MediaDataManager.Listener onMediaDataRemoved : CollectionsKt.toSet(mediaSessionBasedFilter.listeners)) {
            onMediaDataRemoved.onMediaDataRemoved(str);
        }
    }

    private final void dispatchSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda4(this, str, smartspaceMediaData));
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchSmartspaceMediaDataLoaded$lambda-15  reason: not valid java name */
    public static final void m2825dispatchSmartspaceMediaDataLoaded$lambda15(MediaSessionBasedFilter mediaSessionBasedFilter, String str, SmartspaceMediaData smartspaceMediaData) {
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "$info");
        for (MediaDataManager.Listener onSmartspaceMediaDataLoaded$default : CollectionsKt.toSet(mediaSessionBasedFilter.listeners)) {
            MediaDataManager.Listener.onSmartspaceMediaDataLoaded$default(onSmartspaceMediaDataLoaded$default, str, smartspaceMediaData, false, 4, (Object) null);
        }
    }

    private final void dispatchSmartspaceMediaDataRemoved(String str, boolean z) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$$ExternalSyntheticLambda5(this, str, z));
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchSmartspaceMediaDataRemoved$lambda-17  reason: not valid java name */
    public static final void m2826dispatchSmartspaceMediaDataRemoved$lambda17(MediaSessionBasedFilter mediaSessionBasedFilter, String str, boolean z) {
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        for (MediaDataManager.Listener onSmartspaceMediaDataRemoved : CollectionsKt.toSet(mediaSessionBasedFilter.listeners)) {
            onSmartspaceMediaDataRemoved.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    /* access modifiers changed from: private */
    public final void handleControllersChanged(List<MediaController> list) {
        this.packageControllers.clear();
        Iterable<MediaController> iterable = list;
        for (MediaController mediaController : iterable) {
            List list2 = this.packageControllers.get(mediaController.getPackageName());
            if (list2 != null) {
                Boolean.valueOf(list2.add(mediaController));
            } else {
                MediaSessionBasedFilter mediaSessionBasedFilter = this;
                List put = this.packageControllers.put(mediaController.getPackageName(), CollectionsKt.mutableListOf(mediaController));
            }
        }
        Set<MediaSession.Token> set = this.tokensWithNotifications;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (MediaController sessionToken : iterable) {
            arrayList.add(sessionToken.getSessionToken());
        }
        set.retainAll((List) arrayList);
    }
}
