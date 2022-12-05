package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.util.Log;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaTimeoutListener;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaTimeoutListener.kt */
/* loaded from: classes.dex */
public final class MediaTimeoutListener implements MediaDataManager.Listener {
    @NotNull
    private final DelayableExecutor mainExecutor;
    @NotNull
    private final MediaControllerFactory mediaControllerFactory;
    @NotNull
    private final Map<String, PlaybackStateListener> mediaListeners = new LinkedHashMap();
    public Function2<? super String, ? super Boolean, Unit> timeoutCallback;

    public MediaTimeoutListener(@NotNull MediaControllerFactory mediaControllerFactory, @NotNull DelayableExecutor mainExecutor) {
        Intrinsics.checkNotNullParameter(mediaControllerFactory, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(mainExecutor, "mainExecutor");
        this.mediaControllerFactory = mediaControllerFactory;
        this.mainExecutor = mainExecutor;
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    @NotNull
    public final Function2<String, Boolean, Unit> getTimeoutCallback() {
        Function2 function2 = this.timeoutCallback;
        if (function2 != null) {
            return function2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("timeoutCallback");
        throw null;
    }

    public final void setTimeoutCallback(@NotNull Function2<? super String, ? super Boolean, Unit> function2) {
        Intrinsics.checkNotNullParameter(function2, "<set-?>");
        this.timeoutCallback = function2;
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(@NotNull final String key, @Nullable String str, @NotNull MediaData data, boolean z, boolean z2) {
        Object obj;
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        PlaybackStateListener playbackStateListener = this.mediaListeners.get(key);
        if (playbackStateListener == null) {
            obj = null;
        } else if (!playbackStateListener.getDestroyed()) {
            return;
        } else {
            Log.d("MediaTimeout", Intrinsics.stringPlus("Reusing destroyed listener ", key));
            obj = playbackStateListener;
        }
        boolean z3 = false;
        if (str != null && !Intrinsics.areEqual(key, str)) {
            Map<String, PlaybackStateListener> map = this.mediaListeners;
            Objects.requireNonNull(map, "null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
            obj = TypeIntrinsics.asMutableMap(map).remove(str);
            if (obj != null) {
                Log.d("MediaTimeout", "migrating key " + ((Object) str) + " to " + key + ", for resumption");
            } else {
                Log.w("MediaTimeout", "Old key " + ((Object) str) + " for player " + key + " doesn't exist. Continuing...");
            }
        }
        PlaybackStateListener playbackStateListener2 = (PlaybackStateListener) obj;
        if (playbackStateListener2 != null) {
            Boolean playing = playbackStateListener2.getPlaying();
            if (playing != null) {
                z3 = playing.booleanValue();
            }
            Log.d("MediaTimeout", "updating listener for " + key + ", was playing? " + z3);
            playbackStateListener2.setMediaData(data);
            playbackStateListener2.setKey(key);
            this.mediaListeners.put(key, playbackStateListener2);
            if (Intrinsics.areEqual(Boolean.valueOf(z3), playbackStateListener2.getPlaying())) {
                return;
            }
            this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaTimeoutListener$onMediaDataLoaded$2$1
                @Override // java.lang.Runnable
                public final void run() {
                    Map map2;
                    map2 = MediaTimeoutListener.this.mediaListeners;
                    MediaTimeoutListener.PlaybackStateListener playbackStateListener3 = (MediaTimeoutListener.PlaybackStateListener) map2.get(key);
                    if (Intrinsics.areEqual(playbackStateListener3 == null ? null : playbackStateListener3.getPlaying(), Boolean.TRUE)) {
                        Log.d("MediaTimeout", Intrinsics.stringPlus("deliver delayed playback state for ", key));
                        MediaTimeoutListener.this.getTimeoutCallback().mo1950invoke(key, Boolean.FALSE);
                    }
                }
            });
            return;
        }
        this.mediaListeners.put(key, new PlaybackStateListener(this, key, data));
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        PlaybackStateListener remove = this.mediaListeners.remove(key);
        if (remove == null) {
            return;
        }
        remove.destroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MediaTimeoutListener.kt */
    /* loaded from: classes.dex */
    public final class PlaybackStateListener extends MediaController.Callback {
        @Nullable
        private Runnable cancellation;
        private boolean destroyed;
        @NotNull
        private String key;
        @Nullable
        private MediaController mediaController;
        @NotNull
        private MediaData mediaData;
        @Nullable
        private Boolean playing;
        @Nullable
        private Boolean resumption;
        final /* synthetic */ MediaTimeoutListener this$0;
        private boolean timedOut;

        public PlaybackStateListener(@NotNull MediaTimeoutListener this$0, @NotNull String key, MediaData data) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(data, "data");
            this.this$0 = this$0;
            this.key = key;
            this.mediaData = data;
            setMediaData(data);
        }

        @NotNull
        public final String getKey() {
            return this.key;
        }

        public final void setKey(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.key = str;
        }

        public final boolean getTimedOut() {
            return this.timedOut;
        }

        public final void setTimedOut(boolean z) {
            this.timedOut = z;
        }

        @Nullable
        public final Boolean getPlaying() {
            return this.playing;
        }

        public final boolean getDestroyed() {
            return this.destroyed;
        }

        public final void setMediaData(@NotNull MediaData value) {
            Intrinsics.checkNotNullParameter(value, "value");
            this.destroyed = false;
            MediaController mediaController = this.mediaController;
            if (mediaController != null) {
                mediaController.unregisterCallback(this);
            }
            this.mediaData = value;
            PlaybackState playbackState = null;
            MediaController create = value.getToken() != null ? this.this$0.mediaControllerFactory.create(this.mediaData.getToken()) : null;
            this.mediaController = create;
            if (create != null) {
                create.registerCallback(this);
            }
            MediaController mediaController2 = this.mediaController;
            if (mediaController2 != null) {
                playbackState = mediaController2.getPlaybackState();
            }
            processState(playbackState, false);
        }

        public final void destroy() {
            MediaController mediaController = this.mediaController;
            if (mediaController != null) {
                mediaController.unregisterCallback(this);
            }
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                runnable.run();
            }
            this.destroyed = true;
        }

        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(@Nullable PlaybackState playbackState) {
            processState(playbackState, true);
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionDestroyed() {
            Log.d("MediaTimeout", Intrinsics.stringPlus("Session destroyed for ", this.key));
            if (Intrinsics.areEqual(this.resumption, Boolean.TRUE)) {
                MediaController mediaController = this.mediaController;
                if (mediaController == null) {
                    return;
                }
                mediaController.unregisterCallback(this);
                return;
            }
            destroy();
        }

        private final void processState(PlaybackState playbackState, boolean z) {
            long paused_media_timeout;
            Log.v("MediaTimeout", "processState " + this.key + ": " + playbackState);
            boolean z2 = playbackState != null && NotificationMediaManager.isPlayingState(playbackState.getState());
            boolean areEqual = true ^ Intrinsics.areEqual(this.resumption, Boolean.valueOf(this.mediaData.getResumption()));
            if (!Intrinsics.areEqual(this.playing, Boolean.valueOf(z2)) || this.playing == null || areEqual) {
                this.playing = Boolean.valueOf(z2);
                this.resumption = Boolean.valueOf(this.mediaData.getResumption());
                if (!z2) {
                    Log.v("MediaTimeout", "schedule timeout for " + this.key + " playing " + z2 + ", " + this.resumption);
                    if (this.cancellation != null && !areEqual) {
                        Log.d("MediaTimeout", "cancellation already exists, continuing.");
                        return;
                    }
                    String str = this.key;
                    expireMediaTimeout(str, "PLAYBACK STATE CHANGED - " + playbackState + ", " + this.resumption);
                    if (this.mediaData.getResumption()) {
                        paused_media_timeout = MediaTimeoutListenerKt.getRESUME_MEDIA_TIMEOUT();
                    } else {
                        paused_media_timeout = MediaTimeoutListenerKt.getPAUSED_MEDIA_TIMEOUT();
                    }
                    DelayableExecutor delayableExecutor = this.this$0.mainExecutor;
                    final MediaTimeoutListener mediaTimeoutListener = this.this$0;
                    this.cancellation = delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener$processState$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaTimeoutListener.PlaybackStateListener.this.cancellation = null;
                            Log.v("MediaTimeout", Intrinsics.stringPlus("Execute timeout for ", MediaTimeoutListener.PlaybackStateListener.this.getKey()));
                            MediaTimeoutListener.PlaybackStateListener.this.setTimedOut(true);
                            mediaTimeoutListener.getTimeoutCallback().mo1950invoke(MediaTimeoutListener.PlaybackStateListener.this.getKey(), Boolean.valueOf(MediaTimeoutListener.PlaybackStateListener.this.getTimedOut()));
                        }
                    }, paused_media_timeout);
                    return;
                }
                String str2 = this.key;
                expireMediaTimeout(str2, "playback started - " + playbackState + ", " + this.key);
                this.timedOut = false;
                if (!z) {
                    return;
                }
                this.this$0.getTimeoutCallback().mo1950invoke(this.key, Boolean.valueOf(this.timedOut));
            }
        }

        private final void expireMediaTimeout(String str, String str2) {
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                Log.v("MediaTimeout", "media timeout cancelled for  " + str + ", reason: " + str2);
                runnable.run();
            }
            this.cancellation = null;
        }
    }
}
