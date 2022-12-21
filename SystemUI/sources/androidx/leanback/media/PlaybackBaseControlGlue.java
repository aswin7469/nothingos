package androidx.leanback.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import androidx.leanback.media.PlaybackGlue;
import androidx.leanback.media.PlaybackGlueHost;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ControlButtonPresenterSelector;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.PresenterSelector;
import java.util.List;

public abstract class PlaybackBaseControlGlue<T extends PlayerAdapter> extends PlaybackGlue implements OnActionClickedListener, View.OnKeyListener {
    public static final int ACTION_CUSTOM_LEFT_FIRST = 1;
    public static final int ACTION_CUSTOM_RIGHT_FIRST = 4096;
    public static final int ACTION_FAST_FORWARD = 128;
    public static final int ACTION_PLAY_PAUSE = 64;
    public static final int ACTION_REPEAT = 512;
    public static final int ACTION_REWIND = 32;
    public static final int ACTION_SHUFFLE = 1024;
    public static final int ACTION_SKIP_TO_NEXT = 256;
    public static final int ACTION_SKIP_TO_PREVIOUS = 16;
    static final boolean DEBUG = false;
    static final String TAG = "PlaybackTransportGlue";
    final PlayerAdapter.Callback mAdapterCallback;
    boolean mBuffering = false;
    PlaybackControlsRow mControlsRow;
    PlaybackRowPresenter mControlsRowPresenter;
    Drawable mCover;
    int mErrorCode;
    String mErrorMessage;
    boolean mErrorSet = false;
    boolean mFadeWhenPlaying = true;
    boolean mIsPlaying = false;
    PlaybackControlsRow.PlayPauseAction mPlayPauseAction;
    final T mPlayerAdapter;
    PlaybackGlueHost.PlayerCallback mPlayerCallback;
    CharSequence mSubtitle;
    CharSequence mTitle;
    int mVideoHeight = 0;
    int mVideoWidth = 0;

    public abstract void onActionClicked(Action action);

    /* access modifiers changed from: protected */
    public void onCreatePrimaryActions(ArrayObjectAdapter arrayObjectAdapter) {
    }

    /* access modifiers changed from: protected */
    public abstract PlaybackRowPresenter onCreateRowPresenter();

    /* access modifiers changed from: protected */
    public void onCreateSecondaryActions(ArrayObjectAdapter arrayObjectAdapter) {
    }

    public abstract boolean onKey(View view, int i, KeyEvent keyEvent);

    public PlaybackBaseControlGlue(Context context, T t) {
        super(context);
        C09391 r2 = new PlayerAdapter.Callback() {
            public void onPlayStateChanged(PlayerAdapter playerAdapter) {
                PlaybackBaseControlGlue.this.onPlayStateChanged();
            }

            public void onCurrentPositionChanged(PlayerAdapter playerAdapter) {
                PlaybackBaseControlGlue.this.onUpdateProgress();
            }

            public void onBufferedPositionChanged(PlayerAdapter playerAdapter) {
                PlaybackBaseControlGlue.this.onUpdateBufferedProgress();
            }

            public void onDurationChanged(PlayerAdapter playerAdapter) {
                PlaybackBaseControlGlue.this.onUpdateDuration();
            }

            public void onPlayCompleted(PlayerAdapter playerAdapter) {
                PlaybackBaseControlGlue.this.onPlayCompleted();
            }

            public void onPreparedStateChanged(PlayerAdapter playerAdapter) {
                PlaybackBaseControlGlue.this.onPreparedStateChanged();
            }

            public void onVideoSizeChanged(PlayerAdapter playerAdapter, int i, int i2) {
                PlaybackBaseControlGlue.this.mVideoWidth = i;
                PlaybackBaseControlGlue.this.mVideoHeight = i2;
                if (PlaybackBaseControlGlue.this.mPlayerCallback != null) {
                    PlaybackBaseControlGlue.this.mPlayerCallback.onVideoSizeChanged(i, i2);
                }
            }

            public void onError(PlayerAdapter playerAdapter, int i, String str) {
                PlaybackBaseControlGlue.this.mErrorSet = true;
                PlaybackBaseControlGlue.this.mErrorCode = i;
                PlaybackBaseControlGlue.this.mErrorMessage = str;
                if (PlaybackBaseControlGlue.this.mPlayerCallback != null) {
                    PlaybackBaseControlGlue.this.mPlayerCallback.onError(i, str);
                }
            }

            public void onBufferingStateChanged(PlayerAdapter playerAdapter, boolean z) {
                PlaybackBaseControlGlue.this.mBuffering = z;
                if (PlaybackBaseControlGlue.this.mPlayerCallback != null) {
                    PlaybackBaseControlGlue.this.mPlayerCallback.onBufferingStateChanged(z);
                }
            }

            public void onMetadataChanged(PlayerAdapter playerAdapter) {
                PlaybackBaseControlGlue.this.onMetadataChanged();
            }
        };
        this.mAdapterCallback = r2;
        this.mPlayerAdapter = t;
        t.setCallback(r2);
    }

    public final T getPlayerAdapter() {
        return this.mPlayerAdapter;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToHost(PlaybackGlueHost playbackGlueHost) {
        super.onAttachedToHost(playbackGlueHost);
        playbackGlueHost.setOnKeyInterceptListener(this);
        playbackGlueHost.setOnActionClickedListener(this);
        onCreateDefaultControlsRow();
        onCreateDefaultRowPresenter();
        playbackGlueHost.setPlaybackRowPresenter(getPlaybackRowPresenter());
        playbackGlueHost.setPlaybackRow(getControlsRow());
        this.mPlayerCallback = playbackGlueHost.getPlayerCallback();
        onAttachHostCallback();
        this.mPlayerAdapter.onAttachedToHost(playbackGlueHost);
    }

    /* access modifiers changed from: package-private */
    public void onAttachHostCallback() {
        int i;
        PlaybackGlueHost.PlayerCallback playerCallback = this.mPlayerCallback;
        if (playerCallback != null) {
            int i2 = this.mVideoWidth;
            if (!(i2 == 0 || (i = this.mVideoHeight) == 0)) {
                playerCallback.onVideoSizeChanged(i2, i);
            }
            if (this.mErrorSet) {
                this.mPlayerCallback.onError(this.mErrorCode, this.mErrorMessage);
            }
            this.mPlayerCallback.onBufferingStateChanged(this.mBuffering);
        }
    }

    /* access modifiers changed from: package-private */
    public void onDetachHostCallback() {
        this.mErrorSet = false;
        this.mErrorCode = 0;
        this.mErrorMessage = null;
        PlaybackGlueHost.PlayerCallback playerCallback = this.mPlayerCallback;
        if (playerCallback != null) {
            playerCallback.onBufferingStateChanged(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onHostStart() {
        this.mPlayerAdapter.setProgressUpdatingEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onHostStop() {
        this.mPlayerAdapter.setProgressUpdatingEnabled(false);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromHost() {
        onDetachHostCallback();
        this.mPlayerCallback = null;
        this.mPlayerAdapter.onDetachedFromHost();
        this.mPlayerAdapter.setProgressUpdatingEnabled(false);
        super.onDetachedFromHost();
    }

    /* access modifiers changed from: package-private */
    public void onCreateDefaultControlsRow() {
        if (this.mControlsRow == null) {
            setControlsRow(new PlaybackControlsRow(this));
        }
    }

    /* access modifiers changed from: package-private */
    public void onCreateDefaultRowPresenter() {
        if (this.mControlsRowPresenter == null) {
            setPlaybackRowPresenter(onCreateRowPresenter());
        }
    }

    public void setControlsOverlayAutoHideEnabled(boolean z) {
        this.mFadeWhenPlaying = z;
        if (!z && getHost() != null) {
            getHost().setControlsOverlayAutoHideEnabled(false);
        }
    }

    public boolean isControlsOverlayAutoHideEnabled() {
        return this.mFadeWhenPlaying;
    }

    public void setControlsRow(PlaybackControlsRow playbackControlsRow) {
        this.mControlsRow = playbackControlsRow;
        playbackControlsRow.setCurrentPosition(-1);
        this.mControlsRow.setDuration(-1);
        this.mControlsRow.setBufferedPosition(-1);
        if (this.mControlsRow.getPrimaryActionsAdapter() == null) {
            ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter((PresenterSelector) new ControlButtonPresenterSelector());
            onCreatePrimaryActions(arrayObjectAdapter);
            this.mControlsRow.setPrimaryActionsAdapter(arrayObjectAdapter);
        }
        if (this.mControlsRow.getSecondaryActionsAdapter() == null) {
            ArrayObjectAdapter arrayObjectAdapter2 = new ArrayObjectAdapter((PresenterSelector) new ControlButtonPresenterSelector());
            onCreateSecondaryActions(arrayObjectAdapter2);
            getControlsRow().setSecondaryActionsAdapter(arrayObjectAdapter2);
        }
        updateControlsRow();
    }

    public void setPlaybackRowPresenter(PlaybackRowPresenter playbackRowPresenter) {
        this.mControlsRowPresenter = playbackRowPresenter;
    }

    public PlaybackControlsRow getControlsRow() {
        return this.mControlsRow;
    }

    public PlaybackRowPresenter getPlaybackRowPresenter() {
        return this.mControlsRowPresenter;
    }

    private void updateControlsRow() {
        onMetadataChanged();
    }

    public final boolean isPlaying() {
        return this.mPlayerAdapter.isPlaying();
    }

    public void play() {
        this.mPlayerAdapter.play();
    }

    public void pause() {
        this.mPlayerAdapter.pause();
    }

    public void next() {
        this.mPlayerAdapter.next();
    }

    public void previous() {
        this.mPlayerAdapter.previous();
    }

    protected static void notifyItemChanged(ArrayObjectAdapter arrayObjectAdapter, Object obj) {
        int indexOf = arrayObjectAdapter.indexOf(obj);
        if (indexOf >= 0) {
            arrayObjectAdapter.notifyArrayItemRangeChanged(indexOf, 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateProgress() {
        PlaybackControlsRow playbackControlsRow = this.mControlsRow;
        if (playbackControlsRow != null) {
            playbackControlsRow.setCurrentPosition(this.mPlayerAdapter.isPrepared() ? getCurrentPosition() : -1);
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateBufferedProgress() {
        PlaybackControlsRow playbackControlsRow = this.mControlsRow;
        if (playbackControlsRow != null) {
            playbackControlsRow.setBufferedPosition(this.mPlayerAdapter.getBufferedPosition());
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateDuration() {
        PlaybackControlsRow playbackControlsRow = this.mControlsRow;
        if (playbackControlsRow != null) {
            playbackControlsRow.setDuration(this.mPlayerAdapter.isPrepared() ? this.mPlayerAdapter.getDuration() : -1);
        }
    }

    public final long getDuration() {
        return this.mPlayerAdapter.getDuration();
    }

    public long getCurrentPosition() {
        return this.mPlayerAdapter.getCurrentPosition();
    }

    public final long getBufferedPosition() {
        return this.mPlayerAdapter.getBufferedPosition();
    }

    public final boolean isPrepared() {
        return this.mPlayerAdapter.isPrepared();
    }

    /* access modifiers changed from: protected */
    public void onPreparedStateChanged() {
        onUpdateDuration();
        List<PlaybackGlue.PlayerCallback> playerCallbacks = getPlayerCallbacks();
        if (playerCallbacks != null) {
            int size = playerCallbacks.size();
            for (int i = 0; i < size; i++) {
                playerCallbacks.get(i).onPreparedStateChanged(this);
            }
        }
    }

    public void setArt(Drawable drawable) {
        if (this.mCover != drawable) {
            this.mCover = drawable;
            this.mControlsRow.setImageDrawable(drawable);
            if (getHost() != null) {
                getHost().notifyPlaybackRowChanged();
            }
        }
    }

    public Drawable getArt() {
        return this.mCover;
    }

    public void setSubtitle(CharSequence charSequence) {
        if (!TextUtils.equals(charSequence, this.mSubtitle)) {
            this.mSubtitle = charSequence;
            if (getHost() != null) {
                getHost().notifyPlaybackRowChanged();
            }
        }
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public void setTitle(CharSequence charSequence) {
        if (!TextUtils.equals(charSequence, this.mTitle)) {
            this.mTitle = charSequence;
            if (getHost() != null) {
                getHost().notifyPlaybackRowChanged();
            }
        }
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    /* access modifiers changed from: protected */
    public void onMetadataChanged() {
        PlaybackControlsRow playbackControlsRow = this.mControlsRow;
        if (playbackControlsRow != null) {
            playbackControlsRow.setImageDrawable(getArt());
            this.mControlsRow.setDuration(getDuration());
            this.mControlsRow.setCurrentPosition(getCurrentPosition());
            if (getHost() != null) {
                getHost().notifyPlaybackRowChanged();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPlayStateChanged() {
        List<PlaybackGlue.PlayerCallback> playerCallbacks = getPlayerCallbacks();
        if (playerCallbacks != null) {
            int size = playerCallbacks.size();
            for (int i = 0; i < size; i++) {
                playerCallbacks.get(i).onPlayStateChanged(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPlayCompleted() {
        List<PlaybackGlue.PlayerCallback> playerCallbacks = getPlayerCallbacks();
        if (playerCallbacks != null) {
            int size = playerCallbacks.size();
            for (int i = 0; i < size; i++) {
                playerCallbacks.get(i).onPlayCompleted(this);
            }
        }
    }

    public final void seekTo(long j) {
        this.mPlayerAdapter.seekTo(j);
    }

    public long getSupportedActions() {
        return this.mPlayerAdapter.getSupportedActions();
    }
}
