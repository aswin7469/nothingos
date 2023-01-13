package com.android.p019wm.shell.pip;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserHandle;
import com.android.p019wm.shell.C3353R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* renamed from: com.android.wm.shell.pip.PipMediaController */
public class PipMediaController {
    private static final String ACTION_NEXT = "com.android.wm.shell.pip.NEXT";
    private static final String ACTION_PAUSE = "com.android.wm.shell.pip.PAUSE";
    private static final String ACTION_PLAY = "com.android.wm.shell.pip.PLAY";
    private static final String ACTION_PREV = "com.android.wm.shell.pip.PREV";
    private static final String SYSTEMUI_PERMISSION = "com.android.systemui.permission.SELF";
    private final ArrayList<ActionListener> mActionListeners = new ArrayList<>();
    private final Context mContext;
    private final HandlerExecutor mHandlerExecutor;
    private final Handler mMainHandler;
    private final BroadcastReceiver mMediaActionReceiver;
    /* access modifiers changed from: private */
    public MediaController mMediaController;
    private final MediaSessionManager mMediaSessionManager;
    private final ArrayList<MetadataListener> mMetadataListeners = new ArrayList<>();
    private RemoteAction mNextAction;
    private RemoteAction mPauseAction;
    private RemoteAction mPlayAction;
    private final MediaController.Callback mPlaybackChangedListener = new MediaController.Callback() {
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            PipMediaController.this.notifyActionsChanged();
        }

        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            PipMediaController.this.notifyMetadataChanged(mediaMetadata);
        }
    };
    private RemoteAction mPrevAction;
    private final MediaSessionManager.OnActiveSessionsChangedListener mSessionsChangedListener = new PipMediaController$$ExternalSyntheticLambda3(this);
    private final ArrayList<TokenListener> mTokenListeners = new ArrayList<>();

    /* renamed from: com.android.wm.shell.pip.PipMediaController$ActionListener */
    public interface ActionListener {
        void onMediaActionsChanged(List<RemoteAction> list);
    }

    /* renamed from: com.android.wm.shell.pip.PipMediaController$MetadataListener */
    public interface MetadataListener {
        void onMediaMetadataChanged(MediaMetadata mediaMetadata);
    }

    /* renamed from: com.android.wm.shell.pip.PipMediaController$TokenListener */
    public interface TokenListener {
        void onMediaSessionTokenChanged(MediaSession.Token token);
    }

    public PipMediaController(Context context, Handler handler) {
        C35201 r1 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (PipMediaController.this.mMediaController != null && PipMediaController.this.mMediaController.getTransportControls() != null) {
                    String action = intent.getAction();
                    action.hashCode();
                    char c = 65535;
                    switch (action.hashCode()) {
                        case 40376596:
                            if (action.equals(PipMediaController.ACTION_NEXT)) {
                                c = 0;
                                break;
                            }
                            break;
                        case 40442197:
                            if (action.equals(PipMediaController.ACTION_PLAY)) {
                                c = 1;
                                break;
                            }
                            break;
                        case 40448084:
                            if (action.equals(PipMediaController.ACTION_PREV)) {
                                c = 2;
                                break;
                            }
                            break;
                        case 1253399509:
                            if (action.equals(PipMediaController.ACTION_PAUSE)) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            PipMediaController.this.mMediaController.getTransportControls().skipToNext();
                            return;
                        case 1:
                            PipMediaController.this.mMediaController.getTransportControls().play();
                            return;
                        case 2:
                            PipMediaController.this.mMediaController.getTransportControls().skipToPrevious();
                            return;
                        case 3:
                            PipMediaController.this.mMediaController.getTransportControls().pause();
                            return;
                        default:
                            return;
                    }
                }
            }
        };
        this.mMediaActionReceiver = r1;
        this.mContext = context;
        this.mMainHandler = handler;
        this.mHandlerExecutor = new HandlerExecutor(handler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PLAY);
        intentFilter.addAction(ACTION_PAUSE);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        context.registerReceiverForAllUsers(r1, intentFilter, "com.android.systemui.permission.SELF", handler, 2);
        this.mPauseAction = getDefaultRemoteAction(C3353R.string.pip_pause, C3353R.C3355drawable.pip_ic_pause_white, ACTION_PAUSE);
        this.mPlayAction = getDefaultRemoteAction(C3353R.string.pip_play, C3353R.C3355drawable.pip_ic_play_arrow_white, ACTION_PLAY);
        this.mNextAction = getDefaultRemoteAction(C3353R.string.pip_skip_to_next, C3353R.C3355drawable.pip_ic_skip_next_white, ACTION_NEXT);
        this.mPrevAction = getDefaultRemoteAction(C3353R.string.pip_skip_to_prev, C3353R.C3355drawable.pip_ic_skip_previous_white, ACTION_PREV);
        this.mMediaSessionManager = (MediaSessionManager) context.getSystemService(MediaSessionManager.class);
    }

    public void onActivityPinned() {
        resolveActiveMediaController(this.mMediaSessionManager.getActiveSessionsForUser((ComponentName) null, UserHandle.CURRENT));
    }

    public void addActionListener(ActionListener actionListener) {
        if (!this.mActionListeners.contains(actionListener)) {
            this.mActionListeners.add(actionListener);
            actionListener.onMediaActionsChanged(getMediaActions());
        }
    }

    public void removeActionListener(ActionListener actionListener) {
        actionListener.onMediaActionsChanged(Collections.emptyList());
        this.mActionListeners.remove((Object) actionListener);
    }

    public void addMetadataListener(MetadataListener metadataListener) {
        if (!this.mMetadataListeners.contains(metadataListener)) {
            this.mMetadataListeners.add(metadataListener);
            metadataListener.onMediaMetadataChanged(getMediaMetadata());
        }
    }

    public void removeMetadataListener(MetadataListener metadataListener) {
        metadataListener.onMediaMetadataChanged((MediaMetadata) null);
        this.mMetadataListeners.remove((Object) metadataListener);
    }

    public void addTokenListener(TokenListener tokenListener) {
        if (!this.mTokenListeners.contains(tokenListener)) {
            this.mTokenListeners.add(tokenListener);
            tokenListener.onMediaSessionTokenChanged(getToken());
        }
    }

    public void removeTokenListener(TokenListener tokenListener) {
        tokenListener.onMediaSessionTokenChanged((MediaSession.Token) null);
        this.mTokenListeners.remove((Object) tokenListener);
    }

    private MediaSession.Token getToken() {
        MediaController mediaController = this.mMediaController;
        if (mediaController == null) {
            return null;
        }
        return mediaController.getSessionToken();
    }

    private MediaMetadata getMediaMetadata() {
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            return mediaController.getMetadata();
        }
        return null;
    }

    private List<RemoteAction> getMediaActions() {
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || mediaController.getPlaybackState() == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        boolean isActive = this.mMediaController.getPlaybackState().isActive();
        long actions = this.mMediaController.getPlaybackState().getActions();
        boolean z = true;
        this.mPrevAction.setEnabled((16 & actions) != 0);
        arrayList.add(this.mPrevAction);
        if (!isActive && (4 & actions) != 0) {
            arrayList.add(this.mPlayAction);
        } else if (isActive && (2 & actions) != 0) {
            arrayList.add(this.mPauseAction);
        }
        RemoteAction remoteAction = this.mNextAction;
        if ((actions & 32) == 0) {
            z = false;
        }
        remoteAction.setEnabled(z);
        arrayList.add(this.mNextAction);
        return arrayList;
    }

    private RemoteAction getDefaultRemoteAction(int i, int i2, String str) {
        String string = this.mContext.getString(i);
        Intent intent = new Intent(str);
        intent.setPackage(this.mContext.getPackageName());
        return new RemoteAction(Icon.createWithResource(this.mContext, i2), string, string, PendingIntent.getBroadcast(this.mContext, 0, intent, 201326592));
    }

    public void registerSessionListenerForCurrentUser() {
        this.mMediaSessionManager.removeOnActiveSessionsChangedListener(this.mSessionsChangedListener);
        this.mMediaSessionManager.addOnActiveSessionsChangedListener((ComponentName) null, UserHandle.CURRENT, this.mHandlerExecutor, this.mSessionsChangedListener);
    }

    /* access modifiers changed from: private */
    public void resolveActiveMediaController(List<MediaController> list) {
        ComponentName componentName;
        if (!(list == null || (componentName = (ComponentName) PipUtils.getTopPipActivity(this.mContext).first) == null)) {
            for (int i = 0; i < list.size(); i++) {
                MediaController mediaController = list.get(i);
                if (mediaController.getPackageName().equals(componentName.getPackageName())) {
                    setActiveMediaController(mediaController);
                    return;
                }
            }
        }
        setActiveMediaController((MediaController) null);
    }

    private void setActiveMediaController(MediaController mediaController) {
        MediaController mediaController2 = this.mMediaController;
        if (mediaController != mediaController2) {
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this.mPlaybackChangedListener);
            }
            this.mMediaController = mediaController;
            if (mediaController != null) {
                mediaController.registerCallback(this.mPlaybackChangedListener, this.mMainHandler);
            }
            notifyActionsChanged();
            notifyMetadataChanged(getMediaMetadata());
            notifyTokenChanged(getToken());
        }
    }

    /* access modifiers changed from: private */
    public void notifyActionsChanged() {
        if (!this.mActionListeners.isEmpty()) {
            this.mActionListeners.forEach(new PipMediaController$$ExternalSyntheticLambda2(getMediaActions()));
        }
    }

    /* access modifiers changed from: private */
    public void notifyMetadataChanged(MediaMetadata mediaMetadata) {
        if (!this.mMetadataListeners.isEmpty()) {
            this.mMetadataListeners.forEach(new PipMediaController$$ExternalSyntheticLambda0(mediaMetadata));
        }
    }

    private void notifyTokenChanged(MediaSession.Token token) {
        if (!this.mTokenListeners.isEmpty()) {
            this.mTokenListeners.forEach(new PipMediaController$$ExternalSyntheticLambda1(token));
        }
    }
}
