package com.android.settingslib.volume;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.p026io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MediaSessions {
    /* access modifiers changed from: private */
    public static final String TAG = Util.logTag(MediaSessions.class);
    private static final boolean USE_SERVICE_LABEL = false;
    /* access modifiers changed from: private */
    public final Callbacks mCallbacks;
    private final Context mContext;
    /* access modifiers changed from: private */
    public final C1860H mHandler;
    private final HandlerExecutor mHandlerExecutor;
    private boolean mInit;
    /* access modifiers changed from: private */
    public final MediaSessionManager mMgr;
    private final Map<MediaSession.Token, MediaControllerRecord> mRecords = new HashMap();
    private final MediaSessionManager.RemoteSessionCallback mRemoteSessionCallback = new MediaSessionManager.RemoteSessionCallback() {
        public void onVolumeChanged(MediaSession.Token token, int i) {
            MediaSessions.this.mHandler.obtainMessage(2, i, 0, token).sendToTarget();
        }

        public void onDefaultRemoteSessionChanged(MediaSession.Token token) {
            MediaSessions.this.mHandler.obtainMessage(3, token).sendToTarget();
        }
    };
    private final MediaSessionManager.OnActiveSessionsChangedListener mSessionsListener = new MediaSessionManager.OnActiveSessionsChangedListener() {
        public void onActiveSessionsChanged(List<MediaController> list) {
            MediaSessions.this.onActiveSessionsUpdatedH(list);
        }
    };

    public interface Callbacks {
        void onRemoteRemoved(MediaSession.Token token);

        void onRemoteUpdate(MediaSession.Token token, String str, MediaController.PlaybackInfo playbackInfo);

        void onRemoteVolumeChanged(MediaSession.Token token, int i);
    }

    public MediaSessions(Context context, Looper looper, Callbacks callbacks) {
        this.mContext = context;
        C1860H h = new C1860H(looper);
        this.mHandler = h;
        this.mHandlerExecutor = new HandlerExecutor(h);
        this.mMgr = (MediaSessionManager) context.getSystemService("media_session");
        this.mCallbacks = callbacks;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(getClass().getSimpleName() + " state:");
        printWriter.print("  mInit: ");
        printWriter.println(this.mInit);
        printWriter.print("  mRecords.size: ");
        printWriter.println(this.mRecords.size());
        int i = 0;
        for (MediaControllerRecord mediaControllerRecord : this.mRecords.values()) {
            i++;
            dump(i, printWriter, mediaControllerRecord.controller);
        }
    }

    public void init() {
        if (C1857D.BUG) {
            Log.d(TAG, "init");
        }
        this.mMgr.addOnActiveSessionsChangedListener(this.mSessionsListener, (ComponentName) null, this.mHandler);
        this.mInit = true;
        postUpdateSessions();
        this.mMgr.registerRemoteSessionCallback(this.mHandlerExecutor, this.mRemoteSessionCallback);
    }

    /* access modifiers changed from: protected */
    public void postUpdateSessions() {
        if (this.mInit) {
            this.mHandler.sendEmptyMessage(1);
        }
    }

    public void destroy() {
        if (C1857D.BUG) {
            Log.d(TAG, "destroy");
        }
        this.mInit = false;
        this.mMgr.removeOnActiveSessionsChangedListener(this.mSessionsListener);
        this.mMgr.unregisterRemoteSessionCallback(this.mRemoteSessionCallback);
    }

    public void setVolume(MediaSession.Token token, int i) {
        MediaControllerRecord mediaControllerRecord = this.mRecords.get(token);
        if (mediaControllerRecord == null) {
            Log.w(TAG, "setVolume: No record found for token " + token);
            return;
        }
        if (C1857D.BUG) {
            Log.d(TAG, "Setting level to " + i);
        }
        mediaControllerRecord.controller.setVolumeTo(i, 0);
    }

    /* access modifiers changed from: private */
    public void onRemoteVolumeChangedH(MediaSession.Token token, int i) {
        MediaController mediaController = new MediaController(this.mContext, token);
        if (C1857D.BUG) {
            Log.d(TAG, "remoteVolumeChangedH " + mediaController.getPackageName() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + Util.audioManagerFlagsToString(i));
        }
        this.mCallbacks.onRemoteVolumeChanged(mediaController.getSessionToken(), i);
    }

    /* access modifiers changed from: private */
    public void onUpdateRemoteSessionListH(MediaSession.Token token) {
        String str = null;
        MediaController mediaController = token != null ? new MediaController(this.mContext, token) : null;
        if (mediaController != null) {
            str = mediaController.getPackageName();
        }
        if (C1857D.BUG) {
            Log.d(TAG, "onUpdateRemoteSessionListH " + str);
        }
        postUpdateSessions();
    }

    /* access modifiers changed from: protected */
    public void onActiveSessionsUpdatedH(List<MediaController> list) {
        if (C1857D.BUG) {
            Log.d(TAG, "onActiveSessionsUpdatedH n=" + list.size());
        }
        HashSet<MediaSession.Token> hashSet = new HashSet<>(this.mRecords.keySet());
        for (MediaController next : list) {
            MediaSession.Token sessionToken = next.getSessionToken();
            MediaController.PlaybackInfo playbackInfo = next.getPlaybackInfo();
            hashSet.remove(sessionToken);
            if (!this.mRecords.containsKey(sessionToken)) {
                MediaControllerRecord mediaControllerRecord = new MediaControllerRecord(next);
                mediaControllerRecord.name = getControllerName(next);
                this.mRecords.put(sessionToken, mediaControllerRecord);
                next.registerCallback(mediaControllerRecord, this.mHandler);
            }
            MediaControllerRecord mediaControllerRecord2 = this.mRecords.get(sessionToken);
            if (isRemote(playbackInfo)) {
                updateRemoteH(sessionToken, mediaControllerRecord2.name, playbackInfo);
                mediaControllerRecord2.sentRemote = true;
            }
        }
        for (MediaSession.Token token : hashSet) {
            MediaControllerRecord mediaControllerRecord3 = this.mRecords.get(token);
            mediaControllerRecord3.controller.unregisterCallback(mediaControllerRecord3);
            this.mRecords.remove(token);
            if (C1857D.BUG) {
                Log.d(TAG, "Removing " + mediaControllerRecord3.name + " sentRemote=" + mediaControllerRecord3.sentRemote);
            }
            if (mediaControllerRecord3.sentRemote) {
                this.mCallbacks.onRemoteRemoved(token);
                mediaControllerRecord3.sentRemote = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public static boolean isRemote(MediaController.PlaybackInfo playbackInfo) {
        return playbackInfo != null && playbackInfo.getPlaybackType() == 2;
    }

    /* access modifiers changed from: protected */
    public String getControllerName(MediaController mediaController) {
        PackageManager packageManager = this.mContext.getPackageManager();
        String packageName = mediaController.getPackageName();
        try {
            String trim = Objects.toString(packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager), "").trim();
            return trim.length() > 0 ? trim : packageName;
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void updateRemoteH(MediaSession.Token token, String str, MediaController.PlaybackInfo playbackInfo) {
        Callbacks callbacks = this.mCallbacks;
        if (callbacks != null) {
            callbacks.onRemoteUpdate(token, str, playbackInfo);
        }
    }

    private static void dump(int i, PrintWriter printWriter, MediaController mediaController) {
        printWriter.println("  Controller " + i + ": " + mediaController.getPackageName());
        Bundle extras = mediaController.getExtras();
        long flags = mediaController.getFlags();
        MediaMetadata metadata = mediaController.getMetadata();
        MediaController.PlaybackInfo playbackInfo = mediaController.getPlaybackInfo();
        PlaybackState playbackState = mediaController.getPlaybackState();
        List<MediaSession.QueueItem> queue = mediaController.getQueue();
        CharSequence queueTitle = mediaController.getQueueTitle();
        int ratingType = mediaController.getRatingType();
        PendingIntent sessionActivity = mediaController.getSessionActivity();
        printWriter.println("    PlaybackState: " + Util.playbackStateToString(playbackState));
        printWriter.println("    PlaybackInfo: " + Util.playbackInfoToString(playbackInfo));
        if (metadata != null) {
            printWriter.println("  MediaMetadata.desc=" + metadata.getDescription());
        }
        printWriter.println("    RatingType: " + ratingType);
        printWriter.println("    Flags: " + flags);
        if (extras != null) {
            printWriter.println("    Extras:");
            for (String str : extras.keySet()) {
                printWriter.println("      " + str + "=" + extras.get(str));
            }
        }
        if (queueTitle != null) {
            printWriter.println("    QueueTitle: " + queueTitle);
        }
        if (queue != null && !queue.isEmpty()) {
            printWriter.println("    Queue:");
            for (MediaSession.QueueItem queueItem : queue) {
                printWriter.println("      " + queueItem);
            }
        }
        if (playbackInfo != null) {
            printWriter.println("    sessionActivity: " + sessionActivity);
        }
    }

    private final class MediaControllerRecord extends MediaController.Callback {
        public final MediaController controller;
        public String name;
        public boolean sentRemote;

        private MediaControllerRecord(MediaController mediaController) {
            this.controller = mediaController;
        }

        /* renamed from: cb */
        private String m239cb(String str) {
            return str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.controller.getPackageName() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        }

        public void onAudioInfoChanged(MediaController.PlaybackInfo playbackInfo) {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onAudioInfoChanged") + Util.playbackInfoToString(playbackInfo) + " sentRemote=" + this.sentRemote);
            }
            boolean access$300 = MediaSessions.isRemote(playbackInfo);
            if (!access$300 && this.sentRemote) {
                MediaSessions.this.mCallbacks.onRemoteRemoved(this.controller.getSessionToken());
                this.sentRemote = false;
            } else if (access$300) {
                MediaSessions.this.updateRemoteH(this.controller.getSessionToken(), this.name, playbackInfo);
                this.sentRemote = true;
            }
        }

        public void onExtrasChanged(Bundle bundle) {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onExtrasChanged") + bundle);
            }
        }

        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onMetadataChanged") + Util.mediaMetadataToString(mediaMetadata));
            }
        }

        public void onPlaybackStateChanged(PlaybackState playbackState) {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onPlaybackStateChanged") + Util.playbackStateToString(playbackState));
            }
        }

        public void onQueueChanged(List<MediaSession.QueueItem> list) {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onQueueChanged") + list);
            }
        }

        public void onQueueTitleChanged(CharSequence charSequence) {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onQueueTitleChanged") + charSequence);
            }
        }

        public void onSessionDestroyed() {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onSessionDestroyed"));
            }
        }

        public void onSessionEvent(String str, Bundle bundle) {
            if (C1857D.BUG) {
                Log.d(MediaSessions.TAG, m239cb("onSessionEvent") + "event=" + str + " extras=" + bundle);
            }
        }
    }

    /* renamed from: com.android.settingslib.volume.MediaSessions$H */
    private final class C1860H extends Handler {
        private static final int REMOTE_VOLUME_CHANGED = 2;
        private static final int UPDATE_REMOTE_SESSION_LIST = 3;
        private static final int UPDATE_SESSIONS = 1;

        private C1860H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                MediaSessions mediaSessions = MediaSessions.this;
                mediaSessions.onActiveSessionsUpdatedH(mediaSessions.mMgr.getActiveSessions((ComponentName) null));
            } else if (i == 2) {
                MediaSessions.this.onRemoteVolumeChangedH((MediaSession.Token) message.obj, message.arg1);
            } else if (i == 3) {
                MediaSessions.this.onUpdateRemoteSessionListH((MediaSession.Token) message.obj);
            }
        }
    }
}
