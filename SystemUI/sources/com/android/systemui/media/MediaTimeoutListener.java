package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u00010B1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ(\u0010\u001d\u001a\u00020\u001a2\u000e\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020 \u0018\u00010\u001f2\u000e\u0010!\u001a\n\u0012\u0004\u0012\u00020 \u0018\u00010\u001fH\u0002J\u0018\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020 H\u0002J\u000e\u0010%\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u000fJ:\u0010'\u001a\u00020\u00142\u0006\u0010&\u001a\u00020\u000f2\b\u0010(\u001a\u0004\u0018\u00010\u000f2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001aH\u0016J\u0010\u0010/\u001a\u00020\u00142\u0006\u0010&\u001a\u00020\u000fH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000f\u0012\b\u0012\u00060\u0010R\u00020\u00000\u000eX\u0004¢\u0006\u0002\n\u0000R,\u0010\u0011\u001a\u0014\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00140\u0012X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R,\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00140\u0012X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0016\"\u0004\b\u001c\u0010\u0018¨\u00061"}, mo64987d2 = {"Lcom/android/systemui/media/MediaTimeoutListener;", "Lcom/android/systemui/media/MediaDataManager$Listener;", "mediaControllerFactory", "Lcom/android/systemui/media/MediaControllerFactory;", "mainExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "logger", "Lcom/android/systemui/media/MediaTimeoutLogger;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "(Lcom/android/systemui/media/MediaControllerFactory;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/media/MediaTimeoutLogger;Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/util/time/SystemClock;)V", "mediaListeners", "", "", "Lcom/android/systemui/media/MediaTimeoutListener$PlaybackStateListener;", "stateCallback", "Lkotlin/Function2;", "Landroid/media/session/PlaybackState;", "", "getStateCallback", "()Lkotlin/jvm/functions/Function2;", "setStateCallback", "(Lkotlin/jvm/functions/Function2;)V", "timeoutCallback", "", "getTimeoutCallback", "setTimeoutCallback", "areCustomActionListsEqual", "first", "", "Landroid/media/session/PlaybackState$CustomAction;", "second", "areCustomActionsEqual", "firstAction", "secondAction", "isTimedOut", "key", "onMediaDataLoaded", "oldKey", "data", "Lcom/android/systemui/media/MediaData;", "immediately", "receivedSmartspaceCardLatency", "", "isSsReactivated", "onMediaDataRemoved", "PlaybackStateListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTimeoutListener.kt */
public final class MediaTimeoutListener implements MediaDataManager.Listener {
    /* access modifiers changed from: private */
    public final MediaTimeoutLogger logger;
    /* access modifiers changed from: private */
    public final DelayableExecutor mainExecutor;
    /* access modifiers changed from: private */
    public final MediaControllerFactory mediaControllerFactory;
    /* access modifiers changed from: private */
    public final Map<String, PlaybackStateListener> mediaListeners = new LinkedHashMap();
    public Function2<? super String, ? super PlaybackState, Unit> stateCallback;
    /* access modifiers changed from: private */
    public final SystemClock systemClock;
    public Function2<? super String, ? super Boolean, Unit> timeoutCallback;

    @Inject
    public MediaTimeoutListener(MediaControllerFactory mediaControllerFactory2, @Main DelayableExecutor delayableExecutor, MediaTimeoutLogger mediaTimeoutLogger, SysuiStatusBarStateController sysuiStatusBarStateController, SystemClock systemClock2) {
        Intrinsics.checkNotNullParameter(mediaControllerFactory2, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(mediaTimeoutLogger, "logger");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        this.mediaControllerFactory = mediaControllerFactory2;
        this.mainExecutor = delayableExecutor;
        this.logger = mediaTimeoutLogger;
        this.systemClock = systemClock2;
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) {
            final /* synthetic */ MediaTimeoutListener this$0;

            {
                this.this$0 = r1;
            }

            public void onDozingChanged(boolean z) {
                if (!z) {
                    Map access$getMediaListeners$p = this.this$0.mediaListeners;
                    MediaTimeoutListener mediaTimeoutListener = this.this$0;
                    for (Map.Entry entry : access$getMediaListeners$p.entrySet()) {
                        String str = (String) entry.getKey();
                        PlaybackStateListener playbackStateListener = (PlaybackStateListener) entry.getValue();
                        if (playbackStateListener.getCancellation() != null && playbackStateListener.getExpiration() <= mediaTimeoutListener.systemClock.elapsedRealtime()) {
                            playbackStateListener.expireMediaTimeout(str, "timeout happened while dozing");
                            playbackStateListener.doTimeout();
                        }
                    }
                }
            }
        });
    }

    public final Function2<String, Boolean, Unit> getTimeoutCallback() {
        Function2<? super String, ? super Boolean, Unit> function2 = this.timeoutCallback;
        if (function2 != null) {
            return function2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("timeoutCallback");
        return null;
    }

    public final void setTimeoutCallback(Function2<? super String, ? super Boolean, Unit> function2) {
        Intrinsics.checkNotNullParameter(function2, "<set-?>");
        this.timeoutCallback = function2;
    }

    public final Function2<String, PlaybackState, Unit> getStateCallback() {
        Function2<? super String, ? super PlaybackState, Unit> function2 = this.stateCallback;
        if (function2 != null) {
            return function2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("stateCallback");
        return null;
    }

    public final void setStateCallback(Function2<? super String, ? super PlaybackState, Unit> function2) {
        Intrinsics.checkNotNullParameter(function2, "<set-?>");
        this.stateCallback = function2;
    }

    /* JADX WARNING: Failed to insert additional move for type inference */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMediaDataLoaded(java.lang.String r2, java.lang.String r3, com.android.systemui.media.MediaData r4, boolean r5, int r6, boolean r7) {
        /*
            r1 = this;
            java.lang.String r5 = "key"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r5)
            java.lang.String r5 = "data"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r5)
            java.util.Map<java.lang.String, com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener> r5 = r1.mediaListeners
            java.lang.Object r5 = r5.get(r2)
            com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener r5 = (com.android.systemui.media.MediaTimeoutListener.PlaybackStateListener) r5
            if (r5 == 0) goto L_0x0021
            boolean r6 = r5.getDestroyed()
            if (r6 != 0) goto L_0x001b
            return
        L_0x001b:
            com.android.systemui.media.MediaTimeoutLogger r6 = r1.logger
            r6.logReuseListener(r2)
            goto L_0x0022
        L_0x0021:
            r5 = 0
        L_0x0022:
            r6 = 1
            r7 = 0
            if (r3 == 0) goto L_0x002e
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2, (java.lang.Object) r3)
            if (r0 != 0) goto L_0x002e
            r0 = r6
            goto L_0x002f
        L_0x002e:
            r0 = r7
        L_0x002f:
            if (r0 == 0) goto L_0x0044
            java.util.Map<java.lang.String, com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener> r5 = r1.mediaListeners
            java.util.Map r5 = kotlin.jvm.internal.TypeIntrinsics.asMutableMap(r5)
            java.lang.Object r5 = r5.remove(r3)
            com.android.systemui.media.MediaTimeoutLogger r0 = r1.logger
            if (r5 == 0) goto L_0x0040
            goto L_0x0041
        L_0x0040:
            r6 = r7
        L_0x0041:
            r0.logMigrateListener(r3, r2, r6)
        L_0x0044:
            com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener r5 = (com.android.systemui.media.MediaTimeoutListener.PlaybackStateListener) r5
            if (r5 == 0) goto L_0x006d
            boolean r3 = r5.isPlaying()
            com.android.systemui.media.MediaTimeoutLogger r6 = r1.logger
            r6.logUpdateListener(r2, r3)
            r5.setMediaData(r4)
            r5.setKey(r2)
            java.util.Map<java.lang.String, com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener> r4 = r1.mediaListeners
            r4.put(r2, r5)
            boolean r4 = r5.isPlaying()
            if (r3 == r4) goto L_0x006c
            com.android.systemui.util.concurrency.DelayableExecutor r3 = r1.mainExecutor
            com.android.systemui.media.MediaTimeoutListener$$ExternalSyntheticLambda0 r4 = new com.android.systemui.media.MediaTimeoutListener$$ExternalSyntheticLambda0
            r4.<init>(r1, r2)
            r3.execute(r4)
        L_0x006c:
            return
        L_0x006d:
            java.util.Map<java.lang.String, com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener> r3 = r1.mediaListeners
            com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener r5 = new com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener
            r5.<init>(r1, r2, r4)
            r3.put(r2, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaTimeoutListener.onMediaDataLoaded(java.lang.String, java.lang.String, com.android.systemui.media.MediaData, boolean, int, boolean):void");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x001b, code lost:
        if (r0.isPlaying() == true) goto L_0x001f;
     */
    /* renamed from: onMediaDataLoaded$lambda-2$lambda-1  reason: not valid java name */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void m2826onMediaDataLoaded$lambda2$lambda1(com.android.systemui.media.MediaTimeoutListener r3, java.lang.String r4) {
        /*
            java.lang.String r0 = "this$0"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "$key"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            java.util.Map<java.lang.String, com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener> r0 = r3.mediaListeners
            java.lang.Object r0 = r0.get(r4)
            com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener r0 = (com.android.systemui.media.MediaTimeoutListener.PlaybackStateListener) r0
            r1 = 0
            if (r0 == 0) goto L_0x001e
            boolean r0 = r0.isPlaying()
            r2 = 1
            if (r0 != r2) goto L_0x001e
            goto L_0x001f
        L_0x001e:
            r2 = r1
        L_0x001f:
            if (r2 == 0) goto L_0x0031
            com.android.systemui.media.MediaTimeoutLogger r0 = r3.logger
            r0.logDelayedUpdate(r4)
            kotlin.jvm.functions.Function2 r3 = r3.getTimeoutCallback()
            java.lang.Boolean r0 = java.lang.Boolean.valueOf((boolean) r1)
            r3.invoke(r4, r0)
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaTimeoutListener.m2826onMediaDataLoaded$lambda2$lambda1(com.android.systemui.media.MediaTimeoutListener, java.lang.String):void");
    }

    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        PlaybackStateListener remove = this.mediaListeners.remove(str);
        if (remove != null) {
            remove.destroy();
        }
    }

    public final boolean isTimedOut(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        PlaybackStateListener playbackStateListener = this.mediaListeners.get(str);
        if (playbackStateListener != null) {
            return playbackStateListener.getTimedOut();
        }
        return false;
    }

    @Metadata(mo64986d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u00103\u001a\u000204J\u0006\u00105\u001a\u000204J\u0016\u00106\u001a\u0002042\u0006\u00107\u001a\u00020\u00032\u0006\u00108\u001a\u00020\u0003J\u0006\u00109\u001a\u00020\rJ\u0012\u0010:\u001a\u0002042\b\u0010;\u001a\u0004\u0018\u00010\u001dH\u0016J\b\u0010<\u001a\u000204H\u0016J\u001a\u0010=\u001a\u0002042\b\u0010;\u001a\u0004\u0018\u00010\u001d2\u0006\u0010>\u001a\u00020\rH\u0002J\n\u00109\u001a\u00020\r*\u00020?R\"\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u000e¢\u0006\u0002\n\u0000R$\u0010%\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\u001e\u0010*\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0010\n\u0002\u0010/\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001a\u00100\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u000f\"\u0004\b2\u0010\u0011¨\u0006@"}, mo64987d2 = {"Lcom/android/systemui/media/MediaTimeoutListener$PlaybackStateListener;", "Landroid/media/session/MediaController$Callback;", "key", "", "data", "Lcom/android/systemui/media/MediaData;", "(Lcom/android/systemui/media/MediaTimeoutListener;Ljava/lang/String;Lcom/android/systemui/media/MediaData;)V", "<set-?>", "Ljava/lang/Runnable;", "cancellation", "getCancellation", "()Ljava/lang/Runnable;", "destroyed", "", "getDestroyed", "()Z", "setDestroyed", "(Z)V", "expiration", "", "getExpiration", "()J", "setExpiration", "(J)V", "getKey", "()Ljava/lang/String;", "setKey", "(Ljava/lang/String;)V", "lastState", "Landroid/media/session/PlaybackState;", "getLastState", "()Landroid/media/session/PlaybackState;", "setLastState", "(Landroid/media/session/PlaybackState;)V", "mediaController", "Landroid/media/session/MediaController;", "value", "mediaData", "getMediaData", "()Lcom/android/systemui/media/MediaData;", "setMediaData", "(Lcom/android/systemui/media/MediaData;)V", "resumption", "getResumption", "()Ljava/lang/Boolean;", "setResumption", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "timedOut", "getTimedOut", "setTimedOut", "destroy", "", "doTimeout", "expireMediaTimeout", "mediaKey", "reason", "isPlaying", "onPlaybackStateChanged", "state", "onSessionDestroyed", "processState", "dispatchEvents", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaTimeoutListener.kt */
    private final class PlaybackStateListener extends MediaController.Callback {
        private Runnable cancellation;
        private boolean destroyed;
        private long expiration = Long.MAX_VALUE;
        private String key;
        private PlaybackState lastState;
        private MediaController mediaController;
        private MediaData mediaData;
        private Boolean resumption;
        final /* synthetic */ MediaTimeoutListener this$0;
        private boolean timedOut;

        public PlaybackStateListener(MediaTimeoutListener mediaTimeoutListener, String str, MediaData mediaData2) {
            Intrinsics.checkNotNullParameter(str, "key");
            Intrinsics.checkNotNullParameter(mediaData2, "data");
            this.this$0 = mediaTimeoutListener;
            this.key = str;
            this.mediaData = mediaData2;
            setMediaData(mediaData2);
        }

        public final String getKey() {
            return this.key;
        }

        public final void setKey(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.key = str;
        }

        public final boolean getTimedOut() {
            return this.timedOut;
        }

        public final void setTimedOut(boolean z) {
            this.timedOut = z;
        }

        public final PlaybackState getLastState() {
            return this.lastState;
        }

        public final void setLastState(PlaybackState playbackState) {
            this.lastState = playbackState;
        }

        public final Boolean getResumption() {
            return this.resumption;
        }

        public final void setResumption(Boolean bool) {
            this.resumption = bool;
        }

        public final boolean getDestroyed() {
            return this.destroyed;
        }

        public final void setDestroyed(boolean z) {
            this.destroyed = z;
        }

        public final long getExpiration() {
            return this.expiration;
        }

        public final void setExpiration(long j) {
            this.expiration = j;
        }

        public final MediaData getMediaData() {
            return this.mediaData;
        }

        public final void setMediaData(MediaData mediaData2) {
            MediaController mediaController2;
            Intrinsics.checkNotNullParameter(mediaData2, "value");
            this.destroyed = false;
            MediaController mediaController3 = this.mediaController;
            if (mediaController3 != null) {
                mediaController3.unregisterCallback(this);
            }
            this.mediaData = mediaData2;
            MediaSession.Token token = mediaData2.getToken();
            PlaybackState playbackState = null;
            if (token != null) {
                mediaController2 = this.this$0.mediaControllerFactory.create(token);
            } else {
                MediaController mediaController4 = null;
                mediaController2 = null;
            }
            this.mediaController = mediaController2;
            if (mediaController2 != null) {
                mediaController2.registerCallback(this);
            }
            MediaController mediaController5 = this.mediaController;
            if (mediaController5 != null) {
                playbackState = mediaController5.getPlaybackState();
            }
            processState(playbackState, false);
        }

        public final Runnable getCancellation() {
            return this.cancellation;
        }

        public final boolean isPlaying(int i) {
            return NotificationMediaManager.isPlayingState(i);
        }

        public final boolean isPlaying() {
            PlaybackState playbackState = this.lastState;
            if (playbackState != null) {
                return isPlaying(playbackState.getState());
            }
            return false;
        }

        public final void destroy() {
            MediaController mediaController2 = this.mediaController;
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this);
            }
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                runnable.run();
            }
            this.destroyed = true;
        }

        public void onPlaybackStateChanged(PlaybackState playbackState) {
            processState(playbackState, true);
        }

        public void onSessionDestroyed() {
            this.this$0.logger.logSessionDestroyed(this.key);
            if (Intrinsics.areEqual((Object) this.resumption, (Object) true)) {
                MediaController mediaController2 = this.mediaController;
                if (mediaController2 != null) {
                    mediaController2.unregisterCallback(this);
                    return;
                }
                return;
            }
            destroy();
        }

        /* JADX WARNING: Removed duplicated region for block: B:36:0x00a6  */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x011a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private final void processState(android.media.session.PlaybackState r8, boolean r9) {
            /*
                r7 = this;
                com.android.systemui.media.MediaTimeoutListener r0 = r7.this$0
                com.android.systemui.media.MediaTimeoutLogger r0 = r0.logger
                java.lang.String r1 = r7.key
                r0.logPlaybackState(r1, r8)
                r0 = 1
                r1 = 0
                if (r8 == 0) goto L_0x001f
                int r2 = r8.getState()
                boolean r2 = r7.isPlaying(r2)
                boolean r3 = r7.isPlaying()
                if (r2 != r3) goto L_0x001f
                r2 = r0
                goto L_0x0020
            L_0x001f:
                r2 = r1
            L_0x0020:
                android.media.session.PlaybackState r3 = r7.lastState
                r4 = 0
                if (r3 == 0) goto L_0x002e
                long r5 = r3.getActions()
                java.lang.Long r3 = java.lang.Long.valueOf((long) r5)
                goto L_0x002f
            L_0x002e:
                r3 = r4
            L_0x002f:
                if (r8 == 0) goto L_0x003a
                long r5 = r8.getActions()
                java.lang.Long r5 = java.lang.Long.valueOf((long) r5)
                goto L_0x003b
            L_0x003a:
                r5 = r4
            L_0x003b:
                boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r3, (java.lang.Object) r5)
                if (r3 == 0) goto L_0x005b
                com.android.systemui.media.MediaTimeoutListener r3 = r7.this$0
                android.media.session.PlaybackState r5 = r7.lastState
                if (r5 == 0) goto L_0x004c
                java.util.List r5 = r5.getCustomActions()
                goto L_0x004d
            L_0x004c:
                r5 = r4
            L_0x004d:
                if (r8 == 0) goto L_0x0053
                java.util.List r4 = r8.getCustomActions()
            L_0x0053:
                boolean r3 = r3.areCustomActionListsEqual(r5, r4)
                if (r3 == 0) goto L_0x005b
                r3 = r0
                goto L_0x005c
            L_0x005b:
                r3 = r1
            L_0x005c:
                java.lang.Boolean r4 = r7.resumption
                com.android.systemui.media.MediaData r5 = r7.mediaData
                boolean r5 = r5.getResumption()
                java.lang.Boolean r5 = java.lang.Boolean.valueOf((boolean) r5)
                boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r4, (java.lang.Object) r5)
                r0 = r0 ^ r4
                r7.lastState = r8
                if (r3 == 0) goto L_0x0073
                if (r2 != 0) goto L_0x008d
            L_0x0073:
                if (r8 == 0) goto L_0x008d
                if (r9 == 0) goto L_0x008d
                com.android.systemui.media.MediaTimeoutListener r3 = r7.this$0
                com.android.systemui.media.MediaTimeoutLogger r3 = r3.logger
                java.lang.String r4 = r7.key
                r3.logStateCallback(r4)
                com.android.systemui.media.MediaTimeoutListener r3 = r7.this$0
                kotlin.jvm.functions.Function2 r3 = r3.getStateCallback()
                java.lang.String r4 = r7.key
                r3.invoke(r4, r8)
            L_0x008d:
                if (r2 == 0) goto L_0x0092
                if (r0 != 0) goto L_0x0092
                return
            L_0x0092:
                com.android.systemui.media.MediaData r2 = r7.mediaData
                boolean r2 = r2.getResumption()
                java.lang.Boolean r2 = java.lang.Boolean.valueOf((boolean) r2)
                r7.resumption = r2
                boolean r2 = r7.isPlaying()
                java.lang.String r3 = ", "
                if (r2 != 0) goto L_0x011a
                com.android.systemui.media.MediaTimeoutListener r9 = r7.this$0
                com.android.systemui.media.MediaTimeoutLogger r9 = r9.logger
                java.lang.String r1 = r7.key
                java.lang.Boolean r4 = r7.resumption
                kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
                boolean r4 = r4.booleanValue()
                r9.logScheduleTimeout(r1, r2, r4)
                java.lang.Runnable r9 = r7.cancellation
                if (r9 == 0) goto L_0x00cc
                if (r0 != 0) goto L_0x00cc
                com.android.systemui.media.MediaTimeoutListener r8 = r7.this$0
                com.android.systemui.media.MediaTimeoutLogger r8 = r8.logger
                java.lang.String r7 = r7.key
                r8.logCancelIgnored(r7)
                return
            L_0x00cc:
                java.lang.String r9 = r7.key
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                java.lang.String r1 = "PLAYBACK STATE CHANGED - "
                r0.<init>((java.lang.String) r1)
                java.lang.StringBuilder r8 = r0.append((java.lang.Object) r8)
                java.lang.StringBuilder r8 = r8.append((java.lang.String) r3)
                java.lang.Boolean r0 = r7.resumption
                java.lang.StringBuilder r8 = r8.append((java.lang.Object) r0)
                java.lang.String r8 = r8.toString()
                r7.expireMediaTimeout(r9, r8)
                com.android.systemui.media.MediaData r8 = r7.mediaData
                boolean r8 = r8.getResumption()
                if (r8 == 0) goto L_0x00f7
                long r8 = com.android.systemui.media.MediaTimeoutListenerKt.getRESUME_MEDIA_TIMEOUT()
                goto L_0x00fb
            L_0x00f7:
                long r8 = com.android.systemui.media.MediaTimeoutListenerKt.getPAUSED_MEDIA_TIMEOUT()
            L_0x00fb:
                com.android.systemui.media.MediaTimeoutListener r0 = r7.this$0
                com.android.systemui.util.time.SystemClock r0 = r0.systemClock
                long r0 = r0.elapsedRealtime()
                long r0 = r0 + r8
                r7.expiration = r0
                com.android.systemui.media.MediaTimeoutListener r0 = r7.this$0
                com.android.systemui.util.concurrency.DelayableExecutor r0 = r0.mainExecutor
                com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener$$ExternalSyntheticLambda0 r1 = new com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener$$ExternalSyntheticLambda0
                r1.<init>(r7)
                java.lang.Runnable r8 = r0.executeDelayed(r1, r8)
                r7.cancellation = r8
                goto L_0x014e
            L_0x011a:
                java.lang.String r0 = r7.key
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r4 = "playback started - "
                r2.<init>((java.lang.String) r4)
                java.lang.StringBuilder r8 = r2.append((java.lang.Object) r8)
                java.lang.StringBuilder r8 = r8.append((java.lang.String) r3)
                java.lang.String r2 = r7.key
                java.lang.StringBuilder r8 = r8.append((java.lang.String) r2)
                java.lang.String r8 = r8.toString()
                r7.expireMediaTimeout(r0, r8)
                r7.timedOut = r1
                if (r9 == 0) goto L_0x014e
                com.android.systemui.media.MediaTimeoutListener r8 = r7.this$0
                kotlin.jvm.functions.Function2 r8 = r8.getTimeoutCallback()
                java.lang.String r9 = r7.key
                boolean r7 = r7.timedOut
                java.lang.Boolean r7 = java.lang.Boolean.valueOf((boolean) r7)
                r8.invoke(r9, r7)
            L_0x014e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaTimeoutListener.PlaybackStateListener.processState(android.media.session.PlaybackState, boolean):void");
        }

        /* access modifiers changed from: private */
        /* renamed from: processState$lambda-0  reason: not valid java name */
        public static final void m2827processState$lambda0(PlaybackStateListener playbackStateListener) {
            Intrinsics.checkNotNullParameter(playbackStateListener, "this$0");
            playbackStateListener.doTimeout();
        }

        public final void doTimeout() {
            this.cancellation = null;
            this.this$0.logger.logTimeout(this.key);
            this.timedOut = true;
            this.expiration = Long.MAX_VALUE;
            this.this$0.getTimeoutCallback().invoke(this.key, Boolean.valueOf(this.timedOut));
        }

        public final void expireMediaTimeout(String str, String str2) {
            Intrinsics.checkNotNullParameter(str, "mediaKey");
            Intrinsics.checkNotNullParameter(str2, "reason");
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                this.this$0.logger.logTimeoutCancelled(str, str2);
                runnable.run();
            }
            this.expiration = Long.MAX_VALUE;
            this.cancellation = null;
        }
    }

    /* access modifiers changed from: private */
    public final boolean areCustomActionListsEqual(List<PlaybackState.CustomAction> list, List<PlaybackState.CustomAction> list2) {
        if (list == list2) {
            return true;
        }
        if (list == null || list2 == null || list.size() != list2.size()) {
            return false;
        }
        for (Pair pair : SequencesKt.zip(CollectionsKt.asSequence(list), CollectionsKt.asSequence(list2))) {
            if (!areCustomActionsEqual((PlaybackState.CustomAction) pair.component1(), (PlaybackState.CustomAction) pair.component2())) {
                return false;
            }
        }
        return true;
    }

    private final boolean areCustomActionsEqual(PlaybackState.CustomAction customAction, PlaybackState.CustomAction customAction2) {
        if (!Intrinsics.areEqual((Object) customAction.getAction(), (Object) customAction2.getAction()) || !Intrinsics.areEqual((Object) customAction.getName(), (Object) customAction2.getName()) || customAction.getIcon() != customAction2.getIcon()) {
            return false;
        }
        if ((customAction.getExtras() == null) != (customAction2.getExtras() == null)) {
            return false;
        }
        if (customAction.getExtras() != null) {
            Set<String> keySet = customAction.getExtras().keySet();
            Intrinsics.checkNotNullExpressionValue(keySet, "firstAction.extras.keySet()");
            for (String str : keySet) {
                if (!Intrinsics.areEqual(customAction.getExtras().get(str), customAction2.getExtras().get(str))) {
                    return false;
                }
            }
        }
        return true;
    }
}
