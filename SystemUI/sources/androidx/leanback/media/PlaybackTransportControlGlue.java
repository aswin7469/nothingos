package androidx.leanback.media;

import android.app.StatsManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.PlaybackSeekDataProvider;
import androidx.leanback.widget.PlaybackSeekUi;
import androidx.leanback.widget.PlaybackTransportRowPresenter;
import androidx.leanback.widget.RowPresenter;
import java.lang.ref.WeakReference;

public class PlaybackTransportControlGlue<T extends PlayerAdapter> extends PlaybackBaseControlGlue<T> {
    static final boolean DEBUG = false;
    static final int MSG_UPDATE_PLAYBACK_STATE = 100;
    static final String TAG = "PlaybackTransportGlue";
    static final int UPDATE_PLAYBACK_STATE_DELAY_MS = 2000;
    static final Handler sHandler = new UpdatePlaybackStateHandler();
    final WeakReference<PlaybackBaseControlGlue> mGlueWeakReference = new WeakReference<>(this);
    final PlaybackTransportControlGlue<T>.SeekUiClient mPlaybackSeekUiClient = new SeekUiClient();
    boolean mSeekEnabled;
    PlaybackSeekDataProvider mSeekProvider;

    static class UpdatePlaybackStateHandler extends Handler {
        UpdatePlaybackStateHandler() {
        }

        public void handleMessage(Message message) {
            PlaybackTransportControlGlue playbackTransportControlGlue;
            if (message.what == 100 && (playbackTransportControlGlue = (PlaybackTransportControlGlue) ((WeakReference) message.obj).get()) != null) {
                playbackTransportControlGlue.onUpdatePlaybackState();
            }
        }
    }

    public PlaybackTransportControlGlue(Context context, T t) {
        super(context, t);
    }

    public void setControlsRow(PlaybackControlsRow playbackControlsRow) {
        super.setControlsRow(playbackControlsRow);
        sHandler.removeMessages(100, this.mGlueWeakReference);
        onUpdatePlaybackState();
    }

    /* access modifiers changed from: protected */
    public void onCreatePrimaryActions(ArrayObjectAdapter arrayObjectAdapter) {
        PlaybackControlsRow.PlayPauseAction playPauseAction = new PlaybackControlsRow.PlayPauseAction(getContext());
        this.mPlayPauseAction = playPauseAction;
        arrayObjectAdapter.add(playPauseAction);
    }

    /* access modifiers changed from: protected */
    public PlaybackRowPresenter onCreateRowPresenter() {
        C09441 r0 = new AbstractDetailsDescriptionPresenter() {
            /* access modifiers changed from: protected */
            public void onBindDescription(AbstractDetailsDescriptionPresenter.ViewHolder viewHolder, Object obj) {
                PlaybackBaseControlGlue playbackBaseControlGlue = (PlaybackBaseControlGlue) obj;
                viewHolder.getTitle().setText(playbackBaseControlGlue.getTitle());
                viewHolder.getSubtitle().setText(playbackBaseControlGlue.getSubtitle());
            }
        };
        C09452 r1 = new PlaybackTransportRowPresenter() {
            /* access modifiers changed from: protected */
            public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
                super.onBindRowViewHolder(viewHolder, obj);
                viewHolder.setOnKeyListener(PlaybackTransportControlGlue.this);
            }

            /* access modifiers changed from: protected */
            public void onUnbindRowViewHolder(RowPresenter.ViewHolder viewHolder) {
                super.onUnbindRowViewHolder(viewHolder);
                viewHolder.setOnKeyListener((View.OnKeyListener) null);
            }
        };
        r1.setDescriptionPresenter(r0);
        return r1;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToHost(PlaybackGlueHost playbackGlueHost) {
        super.onAttachedToHost(playbackGlueHost);
        if (playbackGlueHost instanceof PlaybackSeekUi) {
            ((PlaybackSeekUi) playbackGlueHost).setPlaybackSeekUiClient(this.mPlaybackSeekUiClient);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromHost() {
        super.onDetachedFromHost();
        if (getHost() instanceof PlaybackSeekUi) {
            ((PlaybackSeekUi) getHost()).setPlaybackSeekUiClient((PlaybackSeekUi.Client) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateProgress() {
        if (!this.mPlaybackSeekUiClient.mIsSeek) {
            super.onUpdateProgress();
        }
    }

    public void onActionClicked(Action action) {
        dispatchAction(action, (KeyEvent) null);
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (!(i == 4 || i == 111)) {
            switch (i) {
                case 19:
                case 20:
                case 21:
                case 22:
                    break;
                default:
                    Action actionForKeyCode = this.mControlsRow.getActionForKeyCode(this.mControlsRow.getPrimaryActionsAdapter(), i);
                    if (actionForKeyCode == null) {
                        actionForKeyCode = this.mControlsRow.getActionForKeyCode(this.mControlsRow.getSecondaryActionsAdapter(), i);
                    }
                    if (actionForKeyCode != null) {
                        if (keyEvent.getAction() != 0) {
                            return true;
                        }
                        dispatchAction(actionForKeyCode, keyEvent);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void onUpdatePlaybackStatusAfterUserAction() {
        updatePlaybackState(this.mIsPlaying);
        Handler handler = sHandler;
        handler.removeMessages(100, this.mGlueWeakReference);
        handler.sendMessageDelayed(handler.obtainMessage(100, this.mGlueWeakReference), StatsManager.DEFAULT_TIMEOUT_MILLIS);
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchAction(Action action, KeyEvent keyEvent) {
        if (action instanceof PlaybackControlsRow.PlayPauseAction) {
            boolean z = keyEvent == null || keyEvent.getKeyCode() == 85 || keyEvent.getKeyCode() == 126;
            if ((keyEvent == null || keyEvent.getKeyCode() == 85 || keyEvent.getKeyCode() == 127) && this.mIsPlaying) {
                this.mIsPlaying = false;
                pause();
            } else if (z && !this.mIsPlaying) {
                this.mIsPlaying = true;
                play();
            }
            onUpdatePlaybackStatusAfterUserAction();
            return true;
        } else if (action instanceof PlaybackControlsRow.SkipNextAction) {
            next();
            return true;
        } else if (!(action instanceof PlaybackControlsRow.SkipPreviousAction)) {
            return false;
        } else {
            previous();
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onPlayStateChanged() {
        Handler handler = sHandler;
        if (handler.hasMessages(100, this.mGlueWeakReference)) {
            handler.removeMessages(100, this.mGlueWeakReference);
            if (this.mPlayerAdapter.isPlaying() != this.mIsPlaying) {
                handler.sendMessageDelayed(handler.obtainMessage(100, this.mGlueWeakReference), StatsManager.DEFAULT_TIMEOUT_MILLIS);
            } else {
                onUpdatePlaybackState();
            }
        } else {
            onUpdatePlaybackState();
        }
        super.onPlayStateChanged();
    }

    /* access modifiers changed from: package-private */
    public void onUpdatePlaybackState() {
        this.mIsPlaying = this.mPlayerAdapter.isPlaying();
        updatePlaybackState(this.mIsPlaying);
    }

    private void updatePlaybackState(boolean z) {
        if (this.mControlsRow != null) {
            if (!z) {
                onUpdateProgress();
                this.mPlayerAdapter.setProgressUpdatingEnabled(this.mPlaybackSeekUiClient.mIsSeek);
            } else {
                this.mPlayerAdapter.setProgressUpdatingEnabled(true);
            }
            if (this.mFadeWhenPlaying && getHost() != null) {
                getHost().setControlsOverlayAutoHideEnabled(z);
            }
            if (this.mPlayPauseAction != null && this.mPlayPauseAction.getIndex() != z) {
                this.mPlayPauseAction.setIndex(z ? 1 : 0);
                notifyItemChanged((ArrayObjectAdapter) getControlsRow().getPrimaryActionsAdapter(), this.mPlayPauseAction);
            }
        }
    }

    class SeekUiClient extends PlaybackSeekUi.Client {
        boolean mIsSeek;
        long mLastUserPosition;
        boolean mPausedBeforeSeek;
        long mPositionBeforeSeek;

        SeekUiClient() {
        }

        public PlaybackSeekDataProvider getPlaybackSeekDataProvider() {
            return PlaybackTransportControlGlue.this.mSeekProvider;
        }

        public boolean isSeekEnabled() {
            return PlaybackTransportControlGlue.this.mSeekProvider != null || PlaybackTransportControlGlue.this.mSeekEnabled;
        }

        public void onSeekStarted() {
            this.mIsSeek = true;
            this.mPausedBeforeSeek = !PlaybackTransportControlGlue.this.isPlaying();
            PlaybackTransportControlGlue.this.mPlayerAdapter.setProgressUpdatingEnabled(true);
            this.mPositionBeforeSeek = PlaybackTransportControlGlue.this.mSeekProvider == null ? PlaybackTransportControlGlue.this.mPlayerAdapter.getCurrentPosition() : -1;
            this.mLastUserPosition = -1;
            PlaybackTransportControlGlue.this.pause();
        }

        public void onSeekPositionChanged(long j) {
            if (PlaybackTransportControlGlue.this.mSeekProvider == null) {
                PlaybackTransportControlGlue.this.mPlayerAdapter.seekTo(j);
            } else {
                this.mLastUserPosition = j;
            }
            if (PlaybackTransportControlGlue.this.mControlsRow != null) {
                PlaybackTransportControlGlue.this.mControlsRow.setCurrentPosition(j);
            }
        }

        public void onSeekFinished(boolean z) {
            if (!z) {
                long j = this.mLastUserPosition;
                if (j >= 0) {
                    PlaybackTransportControlGlue.this.seekTo(j);
                }
            } else {
                long j2 = this.mPositionBeforeSeek;
                if (j2 >= 0) {
                    PlaybackTransportControlGlue.this.seekTo(j2);
                }
            }
            this.mIsSeek = false;
            if (!this.mPausedBeforeSeek) {
                PlaybackTransportControlGlue.this.play();
                return;
            }
            PlaybackTransportControlGlue.this.mPlayerAdapter.setProgressUpdatingEnabled(false);
            PlaybackTransportControlGlue.this.onUpdateProgress();
        }
    }

    public final void setSeekProvider(PlaybackSeekDataProvider playbackSeekDataProvider) {
        this.mSeekProvider = playbackSeekDataProvider;
    }

    public final PlaybackSeekDataProvider getSeekProvider() {
        return this.mSeekProvider;
    }

    public final void setSeekEnabled(boolean z) {
        this.mSeekEnabled = z;
    }

    public final boolean isSeekEnabled() {
        return this.mSeekEnabled;
    }
}
