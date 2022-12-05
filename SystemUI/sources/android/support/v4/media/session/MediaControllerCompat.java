package android.support.v4.media.session;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import androidx.core.app.BundleCompat;
import androidx.media.AudioAttributesCompat;
import androidx.versionedparcelable.ParcelUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public final class MediaControllerCompat {
    private final MediaControllerImpl mImpl;
    @SuppressLint({"BanConcurrentHashMap"})
    private final ConcurrentHashMap<Callback, Boolean> mRegisteredCallbacks = new ConcurrentHashMap<>();
    private final MediaSessionCompat.Token mToken;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface MediaControllerImpl {
        MediaMetadataCompat getMetadata();

        PlaybackStateCompat getPlaybackState();

        PendingIntent getSessionActivity();

        TransportControls getTransportControls();

        void registerCallback(Callback callback, Handler handler);

        void unregisterCallback(Callback callback);
    }

    public MediaControllerCompat(Context context, MediaSessionCompat.Token sessionToken) {
        if (sessionToken == null) {
            throw new IllegalArgumentException("sessionToken must not be null");
        }
        this.mToken = sessionToken;
        if (Build.VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaControllerImplApi21(context, sessionToken);
        } else {
            this.mImpl = new MediaControllerImplBase(sessionToken);
        }
    }

    public TransportControls getTransportControls() {
        return this.mImpl.getTransportControls();
    }

    public PlaybackStateCompat getPlaybackState() {
        return this.mImpl.getPlaybackState();
    }

    public MediaMetadataCompat getMetadata() {
        return this.mImpl.getMetadata();
    }

    public PendingIntent getSessionActivity() {
        return this.mImpl.getSessionActivity();
    }

    public void registerCallback(Callback callback) {
        registerCallback(callback, null);
    }

    public void registerCallback(Callback callback, Handler handler) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        if (this.mRegisteredCallbacks.putIfAbsent(callback, Boolean.TRUE) != null) {
            Log.w("MediaControllerCompat", "the callback has already been registered");
            return;
        }
        if (handler == null) {
            handler = new Handler();
        }
        callback.setHandler(handler);
        this.mImpl.registerCallback(callback, handler);
    }

    public void unregisterCallback(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        if (this.mRegisteredCallbacks.remove(callback) == null) {
            Log.w("MediaControllerCompat", "the callback has never been registered");
            return;
        }
        try {
            this.mImpl.unregisterCallback(callback);
        } finally {
            callback.setHandler(null);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Callback implements IBinder.DeathRecipient {
        final MediaController.Callback mCallbackFwk;
        MessageHandler mHandler;
        IMediaControllerCallback mIControllerCallback;

        public void onAudioInfoChanged(PlaybackInfo info) {
        }

        public void onCaptioningEnabledChanged(boolean enabled) {
        }

        public void onExtrasChanged(Bundle extras) {
        }

        public void onMetadataChanged(MediaMetadataCompat metadata) {
        }

        public void onPlaybackStateChanged(PlaybackStateCompat state) {
        }

        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
        }

        public void onQueueTitleChanged(CharSequence title) {
        }

        public void onRepeatModeChanged(int repeatMode) {
        }

        public void onSessionDestroyed() {
        }

        public void onSessionEvent(String event, Bundle extras) {
        }

        public void onSessionReady() {
        }

        public void onShuffleModeChanged(int shuffleMode) {
        }

        public Callback() {
            if (Build.VERSION.SDK_INT >= 21) {
                this.mCallbackFwk = new MediaControllerCallbackApi21(this);
                return;
            }
            this.mCallbackFwk = null;
            this.mIControllerCallback = new StubCompat(this);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            postToHandler(8, null, null);
        }

        void setHandler(Handler handler) {
            if (handler == null) {
                MessageHandler messageHandler = this.mHandler;
                if (messageHandler == null) {
                    return;
                }
                messageHandler.mRegistered = false;
                messageHandler.removeCallbacksAndMessages(null);
                this.mHandler = null;
                return;
            }
            MessageHandler messageHandler2 = new MessageHandler(handler.getLooper());
            this.mHandler = messageHandler2;
            messageHandler2.mRegistered = true;
        }

        void postToHandler(int what, Object obj, Bundle data) {
            MessageHandler messageHandler = this.mHandler;
            if (messageHandler != null) {
                Message obtainMessage = messageHandler.obtainMessage(what, obj);
                obtainMessage.setData(data);
                obtainMessage.sendToTarget();
            }
        }

        /* loaded from: classes.dex */
        private static class MediaControllerCallbackApi21 extends MediaController.Callback {
            private final WeakReference<Callback> mCallback;

            MediaControllerCallbackApi21(Callback callback) {
                this.mCallback = new WeakReference<>(callback);
            }

            @Override // android.media.session.MediaController.Callback
            public void onSessionDestroyed() {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onSessionDestroyed();
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onSessionEvent(String event, Bundle extras) {
                MediaSessionCompat.ensureClassLoader(extras);
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    if (callback.mIControllerCallback != null && Build.VERSION.SDK_INT < 23) {
                        return;
                    }
                    callback.onSessionEvent(event, extras);
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onPlaybackStateChanged(PlaybackState stateObj) {
                Callback callback = this.mCallback.get();
                if (callback == null || callback.mIControllerCallback != null) {
                    return;
                }
                callback.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(stateObj));
            }

            @Override // android.media.session.MediaController.Callback
            public void onMetadataChanged(MediaMetadata metadataObj) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(metadataObj));
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onQueueChanged(List<MediaSession.QueueItem> queue) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(queue));
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onQueueTitleChanged(CharSequence title) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onQueueTitleChanged(title);
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onExtrasChanged(Bundle extras) {
                MediaSessionCompat.ensureClassLoader(extras);
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onExtrasChanged(extras);
                }
            }

            @Override // android.media.session.MediaController.Callback
            public void onAudioInfoChanged(MediaController.PlaybackInfo info) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onAudioInfoChanged(new PlaybackInfo(info.getPlaybackType(), AudioAttributesCompat.wrap(info.getAudioAttributes()), info.getVolumeControl(), info.getMaxVolume(), info.getCurrentVolume()));
                }
            }
        }

        /* loaded from: classes.dex */
        private static class StubCompat extends IMediaControllerCallback.Stub {
            private final WeakReference<Callback> mCallback;

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onShuffleModeChangedRemoved(boolean enabled) throws RemoteException {
            }

            StubCompat(Callback callback) {
                this.mCallback = new WeakReference<>(callback);
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onEvent(String event, Bundle extras) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(1, event, extras);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onSessionDestroyed() throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(8, null, null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onPlaybackStateChanged(PlaybackStateCompat state) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(2, state, null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onMetadataChanged(MediaMetadataCompat metadata) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(3, metadata, null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(5, queue, null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onQueueTitleChanged(CharSequence title) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(6, title, null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onCaptioningEnabledChanged(boolean enabled) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(11, Boolean.valueOf(enabled), null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onRepeatModeChanged(int repeatMode) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(9, Integer.valueOf(repeatMode), null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onShuffleModeChanged(int shuffleMode) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(12, Integer.valueOf(shuffleMode), null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onExtrasChanged(Bundle extras) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(7, extras, null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onVolumeInfoChanged(ParcelableVolumeInfo info) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(4, info != null ? new PlaybackInfo(info.volumeType, info.audioStream, info.controlType, info.maxVolume, info.currentVolume) : null, null);
                }
            }

            @Override // android.support.v4.media.session.IMediaControllerCallback
            public void onSessionReady() throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(13, null, null);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class MessageHandler extends Handler {
            boolean mRegistered = false;

            MessageHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (!this.mRegistered) {
                    return;
                }
                switch (msg.what) {
                    case 1:
                        Bundle data = msg.getData();
                        MediaSessionCompat.ensureClassLoader(data);
                        Callback.this.onSessionEvent((String) msg.obj, data);
                        return;
                    case 2:
                        Callback.this.onPlaybackStateChanged((PlaybackStateCompat) msg.obj);
                        return;
                    case 3:
                        Callback.this.onMetadataChanged((MediaMetadataCompat) msg.obj);
                        return;
                    case 4:
                        Callback.this.onAudioInfoChanged((PlaybackInfo) msg.obj);
                        return;
                    case 5:
                        Callback.this.onQueueChanged((List) msg.obj);
                        return;
                    case 6:
                        Callback.this.onQueueTitleChanged((CharSequence) msg.obj);
                        return;
                    case 7:
                        Bundle bundle = (Bundle) msg.obj;
                        MediaSessionCompat.ensureClassLoader(bundle);
                        Callback.this.onExtrasChanged(bundle);
                        return;
                    case 8:
                        Callback.this.onSessionDestroyed();
                        return;
                    case 9:
                        Callback.this.onRepeatModeChanged(((Integer) msg.obj).intValue());
                        return;
                    case 10:
                    default:
                        return;
                    case 11:
                        Callback.this.onCaptioningEnabledChanged(((Boolean) msg.obj).booleanValue());
                        return;
                    case 12:
                        Callback.this.onShuffleModeChanged(((Integer) msg.obj).intValue());
                        return;
                    case 13:
                        Callback.this.onSessionReady();
                        return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class TransportControls {
        public abstract void pause();

        public abstract void play();

        public abstract void stop();

        TransportControls() {
        }
    }

    /* loaded from: classes.dex */
    public static final class PlaybackInfo {
        private final AudioAttributesCompat mAudioAttrsCompat;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mPlaybackType;
        private final int mVolumeControl;

        PlaybackInfo(int type, int stream, int control, int max, int current) {
            this(type, new AudioAttributesCompat.Builder().setLegacyStreamType(stream).build(), control, max, current);
        }

        PlaybackInfo(int type, AudioAttributesCompat attrsCompat, int control, int max, int current) {
            this.mPlaybackType = type;
            this.mAudioAttrsCompat = attrsCompat;
            this.mVolumeControl = control;
            this.mMaxVolume = max;
            this.mCurrentVolume = current;
        }
    }

    /* loaded from: classes.dex */
    static class MediaControllerImplBase implements MediaControllerImpl {
        private IMediaSession mBinder;
        private TransportControls mTransportControls;

        MediaControllerImplBase(MediaSessionCompat.Token token) {
            this.mBinder = IMediaSession.Stub.asInterface((IBinder) token.getToken());
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void registerCallback(Callback callback, Handler handler) {
            if (callback == null) {
                throw new IllegalArgumentException("callback may not be null.");
            }
            try {
                this.mBinder.asBinder().linkToDeath(callback, 0);
                this.mBinder.registerCallbackListener(callback.mIControllerCallback);
                callback.postToHandler(13, null, null);
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in registerCallback.", e);
                callback.postToHandler(8, null, null);
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public void unregisterCallback(Callback callback) {
            if (callback == null) {
                throw new IllegalArgumentException("callback may not be null.");
            }
            try {
                this.mBinder.unregisterCallbackListener(callback.mIControllerCallback);
                this.mBinder.asBinder().unlinkToDeath(callback, 0);
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in unregisterCallback.", e);
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public TransportControls getTransportControls() {
            if (this.mTransportControls == null) {
                this.mTransportControls = new TransportControlsBase(this.mBinder);
            }
            return this.mTransportControls;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PlaybackStateCompat getPlaybackState() {
            try {
                return this.mBinder.getPlaybackState();
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", e);
                return null;
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public MediaMetadataCompat getMetadata() {
            try {
                return this.mBinder.getMetadata();
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in getMetadata.", e);
                return null;
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PendingIntent getSessionActivity() {
            try {
                return this.mBinder.getLaunchPendingIntent();
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in getSessionActivity.", e);
                return null;
            }
        }
    }

    /* loaded from: classes.dex */
    static class TransportControlsBase extends TransportControls {
        private IMediaSession mBinder;

        public TransportControlsBase(IMediaSession binder) {
            this.mBinder = binder;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void play() {
            try {
                this.mBinder.play();
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in play.", e);
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void pause() {
            try {
                this.mBinder.pause();
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in pause.", e);
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void stop() {
            try {
                this.mBinder.stop();
            } catch (RemoteException e) {
                Log.e("MediaControllerCompat", "Dead object in stop.", e);
            }
        }
    }

    /* loaded from: classes.dex */
    static class MediaControllerImplApi21 implements MediaControllerImpl {
        protected final MediaController mControllerFwk;
        final MediaSessionCompat.Token mSessionToken;
        final Object mLock = new Object();
        private final List<Callback> mPendingCallbacks = new ArrayList();
        private HashMap<Callback, ExtraCallback> mCallbackMap = new HashMap<>();

        MediaControllerImplApi21(Context context, MediaSessionCompat.Token sessionToken) {
            this.mSessionToken = sessionToken;
            this.mControllerFwk = new MediaController(context, (MediaSession.Token) sessionToken.getToken());
            if (sessionToken.getExtraBinder() == null) {
                requestExtraBinder();
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public final void registerCallback(Callback callback, Handler handler) {
            this.mControllerFwk.registerCallback(callback.mCallbackFwk, handler);
            synchronized (this.mLock) {
                if (this.mSessionToken.getExtraBinder() != null) {
                    ExtraCallback extraCallback = new ExtraCallback(callback);
                    this.mCallbackMap.put(callback, extraCallback);
                    callback.mIControllerCallback = extraCallback;
                    try {
                        this.mSessionToken.getExtraBinder().registerCallbackListener(extraCallback);
                        callback.postToHandler(13, null, null);
                    } catch (RemoteException e) {
                        Log.e("MediaControllerCompat", "Dead object in registerCallback.", e);
                    }
                } else {
                    callback.mIControllerCallback = null;
                    this.mPendingCallbacks.add(callback);
                }
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public final void unregisterCallback(Callback callback) {
            this.mControllerFwk.unregisterCallback(callback.mCallbackFwk);
            synchronized (this.mLock) {
                if (this.mSessionToken.getExtraBinder() != null) {
                    try {
                        ExtraCallback remove = this.mCallbackMap.remove(callback);
                        if (remove != null) {
                            callback.mIControllerCallback = null;
                            this.mSessionToken.getExtraBinder().unregisterCallbackListener(remove);
                        }
                    } catch (RemoteException e) {
                        Log.e("MediaControllerCompat", "Dead object in unregisterCallback.", e);
                    }
                } else {
                    this.mPendingCallbacks.remove(callback);
                }
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public TransportControls getTransportControls() {
            return new TransportControlsApi21(this.mControllerFwk.getTransportControls());
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PlaybackStateCompat getPlaybackState() {
            if (this.mSessionToken.getExtraBinder() != null) {
                try {
                    return this.mSessionToken.getExtraBinder().getPlaybackState();
                } catch (RemoteException e) {
                    Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", e);
                }
            }
            PlaybackState playbackState = this.mControllerFwk.getPlaybackState();
            if (playbackState != null) {
                return PlaybackStateCompat.fromPlaybackState(playbackState);
            }
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public MediaMetadataCompat getMetadata() {
            MediaMetadata metadata = this.mControllerFwk.getMetadata();
            if (metadata != null) {
                return MediaMetadataCompat.fromMediaMetadata(metadata);
            }
            return null;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl
        public PendingIntent getSessionActivity() {
            return this.mControllerFwk.getSessionActivity();
        }

        public void sendCommand(String command, Bundle params, ResultReceiver cb) {
            this.mControllerFwk.sendCommand(command, params, cb);
        }

        private void requestExtraBinder() {
            sendCommand("android.support.v4.media.session.command.GET_EXTRA_BINDER", null, new ExtraBinderRequestResultReceiver(this));
        }

        void processPendingCallbacksLocked() {
            if (this.mSessionToken.getExtraBinder() == null) {
                return;
            }
            for (Callback callback : this.mPendingCallbacks) {
                ExtraCallback extraCallback = new ExtraCallback(callback);
                this.mCallbackMap.put(callback, extraCallback);
                callback.mIControllerCallback = extraCallback;
                try {
                    this.mSessionToken.getExtraBinder().registerCallbackListener(extraCallback);
                    callback.postToHandler(13, null, null);
                } catch (RemoteException e) {
                    Log.e("MediaControllerCompat", "Dead object in registerCallback.", e);
                }
            }
            this.mPendingCallbacks.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class ExtraBinderRequestResultReceiver extends ResultReceiver {
            private WeakReference<MediaControllerImplApi21> mMediaControllerImpl;

            ExtraBinderRequestResultReceiver(MediaControllerImplApi21 mediaControllerImpl) {
                super(null);
                this.mMediaControllerImpl = new WeakReference<>(mediaControllerImpl);
            }

            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                MediaControllerImplApi21 mediaControllerImplApi21 = this.mMediaControllerImpl.get();
                if (mediaControllerImplApi21 == null || resultData == null) {
                    return;
                }
                synchronized (mediaControllerImplApi21.mLock) {
                    mediaControllerImplApi21.mSessionToken.setExtraBinder(IMediaSession.Stub.asInterface(BundleCompat.getBinder(resultData, "android.support.v4.media.session.EXTRA_BINDER")));
                    mediaControllerImplApi21.mSessionToken.setSession2Token(ParcelUtils.getVersionedParcelable(resultData, "android.support.v4.media.session.SESSION_TOKEN2"));
                    mediaControllerImplApi21.processPendingCallbacksLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class ExtraCallback extends Callback.StubCompat {
            ExtraCallback(Callback callback) {
                super(callback);
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onSessionDestroyed() throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onMetadataChanged(MediaMetadataCompat metadata) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onQueueTitleChanged(CharSequence title) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onExtrasChanged(Bundle extras) throws RemoteException {
                throw new AssertionError();
            }

            @Override // android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat, android.support.v4.media.session.IMediaControllerCallback
            public void onVolumeInfoChanged(ParcelableVolumeInfo info) throws RemoteException {
                throw new AssertionError();
            }
        }
    }

    /* loaded from: classes.dex */
    static class TransportControlsApi21 extends TransportControls {
        protected final MediaController.TransportControls mControlsFwk;

        TransportControlsApi21(MediaController.TransportControls controlsFwk) {
            this.mControlsFwk = controlsFwk;
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void play() {
            this.mControlsFwk.play();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void pause() {
            this.mControlsFwk.pause();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.TransportControls
        public void stop() {
            this.mControlsFwk.stop();
        }
    }
}
