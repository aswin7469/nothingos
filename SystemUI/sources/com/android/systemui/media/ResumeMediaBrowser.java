package com.android.systemui.media;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.media.MediaBrowserServiceCompat;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.List;

public class ResumeMediaBrowser {
    public static final String DELIMITER = ":";
    public static final int MAX_RESUMPTION_CONTROLS = 5;
    private static final String TAG = "ResumeMediaBrowser";
    private final MediaBrowserFactory mBrowserFactory;
    /* access modifiers changed from: private */
    public final Callback mCallback;
    /* access modifiers changed from: private */
    public final ComponentName mComponentName;
    private final MediaBrowser.ConnectionCallback mConnectionCallback = new MediaBrowser.ConnectionCallback() {
        public void onConnected() {
            Log.d(ResumeMediaBrowser.TAG, "Service connected for " + ResumeMediaBrowser.this.mComponentName);
            ResumeMediaBrowser.this.updateMediaController();
            if (ResumeMediaBrowser.this.isBrowserConnected()) {
                String root = ResumeMediaBrowser.this.mMediaBrowser.getRoot();
                if (!TextUtils.isEmpty(root)) {
                    if (ResumeMediaBrowser.this.mCallback != null) {
                        ResumeMediaBrowser.this.mCallback.onConnected();
                    }
                    if (ResumeMediaBrowser.this.mMediaBrowser != null) {
                        ResumeMediaBrowser.this.mMediaBrowser.subscribe(root, ResumeMediaBrowser.this.mSubscriptionCallback);
                        return;
                    }
                    return;
                }
            }
            if (ResumeMediaBrowser.this.mCallback != null) {
                ResumeMediaBrowser.this.mCallback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }

        public void onConnectionSuspended() {
            Log.d(ResumeMediaBrowser.TAG, "Connection suspended for " + ResumeMediaBrowser.this.mComponentName);
            if (ResumeMediaBrowser.this.mCallback != null) {
                ResumeMediaBrowser.this.mCallback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }

        public void onConnectionFailed() {
            Log.d(ResumeMediaBrowser.TAG, "Connection failed for " + ResumeMediaBrowser.this.mComponentName);
            if (ResumeMediaBrowser.this.mCallback != null) {
                ResumeMediaBrowser.this.mCallback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }
    };
    private final Context mContext;
    /* access modifiers changed from: private */
    public final ResumeMediaBrowserLogger mLogger;
    /* access modifiers changed from: private */
    public MediaBrowser mMediaBrowser;
    private MediaController mMediaController;
    private final MediaController.Callback mMediaControllerCallback = new SessionDestroyCallback();
    /* access modifiers changed from: private */
    public final MediaBrowser.SubscriptionCallback mSubscriptionCallback = new MediaBrowser.SubscriptionCallback() {
        public void onChildrenLoaded(String str, List<MediaBrowser.MediaItem> list) {
            if (list.size() == 0) {
                Log.d(ResumeMediaBrowser.TAG, "No children found for " + ResumeMediaBrowser.this.mComponentName);
                if (ResumeMediaBrowser.this.mCallback != null) {
                    ResumeMediaBrowser.this.mCallback.onError();
                }
            } else {
                MediaBrowser.MediaItem mediaItem = list.get(0);
                MediaDescription description = mediaItem.getDescription();
                if (!mediaItem.isPlayable() || ResumeMediaBrowser.this.mMediaBrowser == null) {
                    Log.d(ResumeMediaBrowser.TAG, "Child found but not playable for " + ResumeMediaBrowser.this.mComponentName);
                    if (ResumeMediaBrowser.this.mCallback != null) {
                        ResumeMediaBrowser.this.mCallback.onError();
                    }
                } else if (ResumeMediaBrowser.this.mCallback != null) {
                    ResumeMediaBrowser.this.mCallback.addTrack(description, ResumeMediaBrowser.this.mMediaBrowser.getServiceComponent(), ResumeMediaBrowser.this);
                }
            }
            ResumeMediaBrowser.this.disconnect();
        }

        public void onError(String str) {
            Log.d(ResumeMediaBrowser.TAG, "Subscribe error for " + ResumeMediaBrowser.this.mComponentName + ": " + str);
            if (ResumeMediaBrowser.this.mCallback != null) {
                ResumeMediaBrowser.this.mCallback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }

        public void onError(String str, Bundle bundle) {
            Log.d(ResumeMediaBrowser.TAG, "Subscribe error for " + ResumeMediaBrowser.this.mComponentName + ": " + str + ", options: " + bundle);
            if (ResumeMediaBrowser.this.mCallback != null) {
                ResumeMediaBrowser.this.mCallback.onError();
            }
            ResumeMediaBrowser.this.disconnect();
        }
    };

    public static class Callback {
        public void addTrack(MediaDescription mediaDescription, ComponentName componentName, ResumeMediaBrowser resumeMediaBrowser) {
        }

        public void onConnected() {
        }

        public void onError() {
        }
    }

    public ResumeMediaBrowser(Context context, Callback callback, ComponentName componentName, MediaBrowserFactory mediaBrowserFactory, ResumeMediaBrowserLogger resumeMediaBrowserLogger) {
        this.mContext = context;
        this.mCallback = callback;
        this.mComponentName = componentName;
        this.mBrowserFactory = mediaBrowserFactory;
        this.mLogger = resumeMediaBrowserLogger;
    }

    public void findRecentMedia() {
        disconnect();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MediaBrowserServiceCompat.BrowserRoot.EXTRA_RECENT, true);
        this.mMediaBrowser = this.mBrowserFactory.create(this.mComponentName, this.mConnectionCallback, bundle);
        updateMediaController();
        this.mLogger.logConnection(this.mComponentName, "findRecentMedia");
        this.mMediaBrowser.connect();
    }

    /* access modifiers changed from: protected */
    public void disconnect() {
        if (this.mMediaBrowser != null) {
            this.mLogger.logDisconnect(this.mComponentName);
            this.mMediaBrowser.disconnect();
        }
        this.mMediaBrowser = null;
        updateMediaController();
    }

    public void restart() {
        disconnect();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MediaBrowserServiceCompat.BrowserRoot.EXTRA_RECENT, true);
        this.mMediaBrowser = this.mBrowserFactory.create(this.mComponentName, new MediaBrowser.ConnectionCallback() {
            public void onConnected() {
                Log.d(ResumeMediaBrowser.TAG, "Connected for restart " + ResumeMediaBrowser.this.mMediaBrowser.isConnected());
                ResumeMediaBrowser.this.updateMediaController();
                if (!ResumeMediaBrowser.this.isBrowserConnected()) {
                    if (ResumeMediaBrowser.this.mCallback != null) {
                        ResumeMediaBrowser.this.mCallback.onError();
                    }
                    ResumeMediaBrowser.this.disconnect();
                    return;
                }
                MediaController createMediaController = ResumeMediaBrowser.this.createMediaController(ResumeMediaBrowser.this.mMediaBrowser.getSessionToken());
                createMediaController.getTransportControls();
                createMediaController.getTransportControls().prepare();
                createMediaController.getTransportControls().play();
                if (ResumeMediaBrowser.this.mCallback != null) {
                    ResumeMediaBrowser.this.mCallback.onConnected();
                }
            }

            public void onConnectionFailed() {
                if (ResumeMediaBrowser.this.mCallback != null) {
                    ResumeMediaBrowser.this.mCallback.onError();
                }
                ResumeMediaBrowser.this.disconnect();
            }

            public void onConnectionSuspended() {
                if (ResumeMediaBrowser.this.mCallback != null) {
                    ResumeMediaBrowser.this.mCallback.onError();
                }
                ResumeMediaBrowser.this.disconnect();
            }
        }, bundle);
        updateMediaController();
        this.mLogger.logConnection(this.mComponentName, "restart");
        this.mMediaBrowser.connect();
    }

    /* access modifiers changed from: protected */
    public MediaController createMediaController(MediaSession.Token token) {
        return new MediaController(this.mContext, token);
    }

    public MediaSession.Token getToken() {
        if (!isBrowserConnected()) {
            return null;
        }
        return this.mMediaBrowser.getSessionToken();
    }

    public PendingIntent getAppIntent() {
        return PendingIntent.getActivity(this.mContext, 0, this.mContext.getPackageManager().getLaunchIntentForPackage(this.mComponentName.getPackageName()), QuickStepContract.SYSUI_STATE_VOICE_INTERACTION_WINDOW_SHOWING);
    }

    public void testConnection() {
        disconnect();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MediaBrowserServiceCompat.BrowserRoot.EXTRA_RECENT, true);
        this.mMediaBrowser = this.mBrowserFactory.create(this.mComponentName, this.mConnectionCallback, bundle);
        updateMediaController();
        this.mLogger.logConnection(this.mComponentName, "testConnection");
        this.mMediaBrowser.connect();
    }

    /* access modifiers changed from: private */
    public void updateMediaController() {
        MediaController mediaController = this.mMediaController;
        MediaSession.Token sessionToken = mediaController != null ? mediaController.getSessionToken() : null;
        MediaSession.Token token = getToken();
        if (!((sessionToken == null && token == null) || (sessionToken != null && sessionToken.equals(token)))) {
            MediaController mediaController2 = this.mMediaController;
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this.mMediaControllerCallback);
            }
            if (token != null) {
                MediaController createMediaController = createMediaController(token);
                this.mMediaController = createMediaController;
                createMediaController.registerCallback(this.mMediaControllerCallback);
                return;
            }
            this.mMediaController = null;
        }
    }

    /* access modifiers changed from: private */
    public boolean isBrowserConnected() {
        MediaBrowser mediaBrowser = this.mMediaBrowser;
        return mediaBrowser != null && mediaBrowser.isConnected();
    }

    private class SessionDestroyCallback extends MediaController.Callback {
        private SessionDestroyCallback() {
        }

        public void onSessionDestroyed() {
            ResumeMediaBrowser.this.mLogger.logSessionDestroyed(ResumeMediaBrowser.this.isBrowserConnected(), ResumeMediaBrowser.this.mComponentName);
            ResumeMediaBrowser.this.disconnect();
        }
    }
}
