package androidx.leanback.media;

import android.content.Context;
import androidx.leanback.media.PlaybackGlueHost;
import java.util.ArrayList;
import java.util.List;

public abstract class PlaybackGlue {
    private final Context mContext;
    private PlaybackGlueHost mPlaybackGlueHost;
    ArrayList<PlayerCallback> mPlayerCallbacks;

    public static abstract class PlayerCallback {
        public void onPlayCompleted(PlaybackGlue playbackGlue) {
        }

        public void onPlayStateChanged(PlaybackGlue playbackGlue) {
        }

        public void onPreparedStateChanged(PlaybackGlue playbackGlue) {
        }
    }

    public boolean isPlaying() {
        return false;
    }

    public boolean isPrepared() {
        return true;
    }

    public void next() {
    }

    /* access modifiers changed from: protected */
    public void onHostPause() {
    }

    /* access modifiers changed from: protected */
    public void onHostResume() {
    }

    /* access modifiers changed from: protected */
    public void onHostStart() {
    }

    /* access modifiers changed from: protected */
    public void onHostStop() {
    }

    public void pause() {
    }

    public void play() {
    }

    public void previous() {
    }

    public PlaybackGlue(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void addPlayerCallback(PlayerCallback playerCallback) {
        if (this.mPlayerCallbacks == null) {
            this.mPlayerCallbacks = new ArrayList<>();
        }
        this.mPlayerCallbacks.add(playerCallback);
    }

    public void removePlayerCallback(PlayerCallback playerCallback) {
        ArrayList<PlayerCallback> arrayList = this.mPlayerCallbacks;
        if (arrayList != null) {
            arrayList.remove((Object) playerCallback);
        }
    }

    /* access modifiers changed from: protected */
    public List<PlayerCallback> getPlayerCallbacks() {
        if (this.mPlayerCallbacks == null) {
            return null;
        }
        return new ArrayList(this.mPlayerCallbacks);
    }

    public void playWhenPrepared() {
        if (isPrepared()) {
            play();
        } else {
            addPlayerCallback(new PlayerCallback() {
                public void onPreparedStateChanged(PlaybackGlue playbackGlue) {
                    if (playbackGlue.isPrepared()) {
                        PlaybackGlue.this.removePlayerCallback(this);
                        PlaybackGlue.this.play();
                    }
                }
            });
        }
    }

    public final void setHost(PlaybackGlueHost playbackGlueHost) {
        PlaybackGlueHost playbackGlueHost2 = this.mPlaybackGlueHost;
        if (playbackGlueHost2 != playbackGlueHost) {
            if (playbackGlueHost2 != null) {
                playbackGlueHost2.attachToGlue((PlaybackGlue) null);
            }
            this.mPlaybackGlueHost = playbackGlueHost;
            if (playbackGlueHost != null) {
                playbackGlueHost.attachToGlue(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToHost(PlaybackGlueHost playbackGlueHost) {
        this.mPlaybackGlueHost = playbackGlueHost;
        playbackGlueHost.setHostCallback(new PlaybackGlueHost.HostCallback() {
            public void onHostStart() {
                PlaybackGlue.this.onHostStart();
            }

            public void onHostStop() {
                PlaybackGlue.this.onHostStop();
            }

            public void onHostResume() {
                PlaybackGlue.this.onHostResume();
            }

            public void onHostPause() {
                PlaybackGlue.this.onHostPause();
            }

            public void onHostDestroy() {
                PlaybackGlue.this.setHost((PlaybackGlueHost) null);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromHost() {
        PlaybackGlueHost playbackGlueHost = this.mPlaybackGlueHost;
        if (playbackGlueHost != null) {
            playbackGlueHost.setHostCallback((PlaybackGlueHost.HostCallback) null);
            this.mPlaybackGlueHost = null;
        }
    }

    public PlaybackGlueHost getHost() {
        return this.mPlaybackGlueHost;
    }
}
