package androidx.leanback.media;

import android.content.Context;
import android.net.wifi.EasyConnectStatusCallback;
import android.view.KeyEvent;
import android.view.View;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackControlsRowPresenter;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.RowPresenter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PlaybackBannerControlGlue<T extends PlayerAdapter> extends PlaybackBaseControlGlue<T> {
    public static final int ACTION_CUSTOM_LEFT_FIRST = 1;
    public static final int ACTION_CUSTOM_RIGHT_FIRST = 4096;
    public static final int ACTION_FAST_FORWARD = 128;
    public static final int ACTION_PLAY_PAUSE = 64;
    public static final int ACTION_REWIND = 32;
    public static final int ACTION_SKIP_TO_NEXT = 256;
    public static final int ACTION_SKIP_TO_PREVIOUS = 16;
    private static final int NUMBER_OF_SEEK_SPEEDS = 5;
    public static final int PLAYBACK_SPEED_FAST_L0 = 10;
    public static final int PLAYBACK_SPEED_FAST_L1 = 11;
    public static final int PLAYBACK_SPEED_FAST_L2 = 12;
    public static final int PLAYBACK_SPEED_FAST_L3 = 13;
    public static final int PLAYBACK_SPEED_FAST_L4 = 14;
    public static final int PLAYBACK_SPEED_INVALID = -1;
    public static final int PLAYBACK_SPEED_NORMAL = 1;
    public static final int PLAYBACK_SPEED_PAUSED = 0;
    private static final String TAG = "PlaybackBannerControlGlue";
    private PlaybackControlsRow.FastForwardAction mFastForwardAction;
    private final int[] mFastForwardSpeeds;
    private boolean mIsCustomizedFastForwardSupported;
    private boolean mIsCustomizedRewindSupported;
    private int mPlaybackSpeed;
    private PlaybackControlsRow.RewindAction mRewindAction;
    private final int[] mRewindSpeeds;
    private PlaybackControlsRow.SkipNextAction mSkipNextAction;
    private PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;
    private long mStartPosition;
    private long mStartTime;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ACTION_ {
    }

    public PlaybackBannerControlGlue(Context context, int[] iArr, T t) {
        this(context, iArr, iArr, t);
    }

    public PlaybackBannerControlGlue(Context context, int[] iArr, int[] iArr2, T t) {
        super(context, t);
        this.mPlaybackSpeed = 0;
        this.mStartPosition = 0;
        if (iArr.length == 0 || iArr.length > 5) {
            throw new IllegalArgumentException("invalid fastForwardSpeeds array size");
        }
        this.mFastForwardSpeeds = iArr;
        if (iArr2.length == 0 || iArr2.length > 5) {
            throw new IllegalArgumentException("invalid rewindSpeeds array size");
        }
        this.mRewindSpeeds = iArr2;
        if ((this.mPlayerAdapter.getSupportedActions() & 128) != 0) {
            this.mIsCustomizedFastForwardSupported = true;
        }
        if ((this.mPlayerAdapter.getSupportedActions() & 32) != 0) {
            this.mIsCustomizedRewindSupported = true;
        }
    }

    public void setControlsRow(PlaybackControlsRow playbackControlsRow) {
        super.setControlsRow(playbackControlsRow);
        onUpdatePlaybackState();
    }

    /* access modifiers changed from: protected */
    public void onCreatePrimaryActions(ArrayObjectAdapter arrayObjectAdapter) {
        PlaybackControlsRow.SkipNextAction skipNextAction;
        PlaybackControlsRow.FastForwardAction fastForwardAction;
        PlaybackControlsRow.RewindAction rewindAction;
        PlaybackControlsRow.SkipPreviousAction skipPreviousAction;
        long supportedActions = getSupportedActions();
        int i = ((16 & supportedActions) > 0 ? 1 : ((16 & supportedActions) == 0 ? 0 : -1));
        if (i != 0 && this.mSkipPreviousAction == null) {
            PlaybackControlsRow.SkipPreviousAction skipPreviousAction2 = new PlaybackControlsRow.SkipPreviousAction(getContext());
            this.mSkipPreviousAction = skipPreviousAction2;
            arrayObjectAdapter.add(skipPreviousAction2);
        } else if (i == 0 && (skipPreviousAction = this.mSkipPreviousAction) != null) {
            arrayObjectAdapter.remove(skipPreviousAction);
            this.mSkipPreviousAction = null;
        }
        int i2 = ((32 & supportedActions) > 0 ? 1 : ((32 & supportedActions) == 0 ? 0 : -1));
        if (i2 != 0 && this.mRewindAction == null) {
            PlaybackControlsRow.RewindAction rewindAction2 = new PlaybackControlsRow.RewindAction(getContext(), this.mRewindSpeeds.length);
            this.mRewindAction = rewindAction2;
            arrayObjectAdapter.add(rewindAction2);
        } else if (i2 == 0 && (rewindAction = this.mRewindAction) != null) {
            arrayObjectAdapter.remove(rewindAction);
            this.mRewindAction = null;
        }
        int i3 = ((64 & supportedActions) > 0 ? 1 : ((64 & supportedActions) == 0 ? 0 : -1));
        if (i3 != 0 && this.mPlayPauseAction == null) {
            this.mPlayPauseAction = new PlaybackControlsRow.PlayPauseAction(getContext());
            PlaybackControlsRow.PlayPauseAction playPauseAction = new PlaybackControlsRow.PlayPauseAction(getContext());
            this.mPlayPauseAction = playPauseAction;
            arrayObjectAdapter.add(playPauseAction);
        } else if (i3 == 0 && this.mPlayPauseAction != null) {
            arrayObjectAdapter.remove(this.mPlayPauseAction);
            this.mPlayPauseAction = null;
        }
        int i4 = ((128 & supportedActions) > 0 ? 1 : ((128 & supportedActions) == 0 ? 0 : -1));
        if (i4 != 0 && this.mFastForwardAction == null) {
            this.mFastForwardAction = new PlaybackControlsRow.FastForwardAction(getContext(), this.mFastForwardSpeeds.length);
            PlaybackControlsRow.FastForwardAction fastForwardAction2 = new PlaybackControlsRow.FastForwardAction(getContext(), this.mFastForwardSpeeds.length);
            this.mFastForwardAction = fastForwardAction2;
            arrayObjectAdapter.add(fastForwardAction2);
        } else if (i4 == 0 && (fastForwardAction = this.mFastForwardAction) != null) {
            arrayObjectAdapter.remove(fastForwardAction);
            this.mFastForwardAction = null;
        }
        int i5 = ((supportedActions & 256) > 0 ? 1 : ((supportedActions & 256) == 0 ? 0 : -1));
        if (i5 != 0 && this.mSkipNextAction == null) {
            PlaybackControlsRow.SkipNextAction skipNextAction2 = new PlaybackControlsRow.SkipNextAction(getContext());
            this.mSkipNextAction = skipNextAction2;
            arrayObjectAdapter.add(skipNextAction2);
        } else if (i5 == 0 && (skipNextAction = this.mSkipNextAction) != null) {
            arrayObjectAdapter.remove(skipNextAction);
            this.mSkipNextAction = null;
        }
    }

    /* access modifiers changed from: protected */
    public PlaybackRowPresenter onCreateRowPresenter() {
        return new PlaybackControlsRowPresenter(new AbstractDetailsDescriptionPresenter() {
            /* access modifiers changed from: protected */
            public void onBindDescription(AbstractDetailsDescriptionPresenter.ViewHolder viewHolder, Object obj) {
                PlaybackBannerControlGlue playbackBannerControlGlue = (PlaybackBannerControlGlue) obj;
                viewHolder.getTitle().setText(playbackBannerControlGlue.getTitle());
                viewHolder.getSubtitle().setText(playbackBannerControlGlue.getSubtitle());
            }
        }) {
            /* access modifiers changed from: protected */
            public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
                super.onBindRowViewHolder(viewHolder, obj);
                viewHolder.setOnKeyListener(PlaybackBannerControlGlue.this);
            }

            /* access modifiers changed from: protected */
            public void onUnbindRowViewHolder(RowPresenter.ViewHolder viewHolder) {
                super.onUnbindRowViewHolder(viewHolder);
                viewHolder.setOnKeyListener((View.OnKeyListener) null);
            }
        };
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
                    if (actionForKeyCode == null) {
                        return false;
                    }
                    if (keyEvent.getAction() == 0) {
                        dispatchAction(actionForKeyCode, keyEvent);
                    }
                    return true;
            }
        }
        int i2 = this.mPlaybackSpeed;
        if (!(i2 >= 10 || i2 <= -10)) {
            return false;
        }
        play();
        onUpdatePlaybackStatusAfterUserAction();
        if (i == 4 || i == 111) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void onUpdatePlaybackStatusAfterUserAction() {
        updatePlaybackState(this.mIsPlaying);
    }

    private void incrementFastForwardPlaybackSpeed() {
        int i = this.mPlaybackSpeed;
        switch (i) {
            case 10:
            case 11:
            case 12:
            case 13:
                this.mPlaybackSpeed = i + 1;
                return;
            default:
                this.mPlaybackSpeed = 10;
                return;
        }
    }

    private void decrementRewindPlaybackSpeed() {
        int i = this.mPlaybackSpeed;
        switch (i) {
            case EasyConnectStatusCallback.EASY_CONNECT_EVENT_FAILURE_URI_GENERATION:
            case EasyConnectStatusCallback.EASY_CONNECT_EVENT_FAILURE_ENROLLEE_REJECTED_CONFIGURATION:
            case EasyConnectStatusCallback.EASY_CONNECT_EVENT_FAILURE_ENROLLEE_AUTHENTICATION:
            case -10:
                this.mPlaybackSpeed = i - 1;
                return;
            default:
                this.mPlaybackSpeed = -10;
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchAction(Action action, KeyEvent keyEvent) {
        boolean z = false;
        if (action == this.mPlayPauseAction) {
            boolean z2 = keyEvent == null || keyEvent.getKeyCode() == 85 || keyEvent.getKeyCode() == 126;
            if (keyEvent == null || keyEvent.getKeyCode() == 85 || keyEvent.getKeyCode() == 127) {
                z = true;
            }
            if (z) {
                int i = this.mPlaybackSpeed;
                if (!z2 ? i != 0 : i == 1) {
                    pause();
                    onUpdatePlaybackStatusAfterUserAction();
                    return true;
                }
            }
            if (z2 && this.mPlaybackSpeed != 1) {
                play();
            }
            onUpdatePlaybackStatusAfterUserAction();
            return true;
        } else if (action == this.mSkipNextAction) {
            next();
            return true;
        } else if (action == this.mSkipPreviousAction) {
            previous();
            return true;
        } else if (action == this.mFastForwardAction) {
            if (!this.mPlayerAdapter.isPrepared() || this.mPlaybackSpeed >= getMaxForwardSpeedId()) {
                return true;
            }
            if (this.mIsCustomizedFastForwardSupported) {
                this.mIsPlaying = true;
                this.mPlayerAdapter.fastForward();
            } else {
                fakePause();
            }
            incrementFastForwardPlaybackSpeed();
            onUpdatePlaybackStatusAfterUserAction();
            return true;
        } else if (action != this.mRewindAction) {
            return false;
        } else {
            if (!this.mPlayerAdapter.isPrepared() || this.mPlaybackSpeed <= (-getMaxRewindSpeedId())) {
                return true;
            }
            if (this.mIsCustomizedFastForwardSupported) {
                this.mIsPlaying = true;
                this.mPlayerAdapter.rewind();
            } else {
                fakePause();
            }
            decrementRewindPlaybackSpeed();
            onUpdatePlaybackStatusAfterUserAction();
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onPlayStateChanged() {
        onUpdatePlaybackState();
        super.onPlayStateChanged();
    }

    /* access modifiers changed from: protected */
    public void onPlayCompleted() {
        super.onPlayCompleted();
        this.mIsPlaying = false;
        this.mPlaybackSpeed = 0;
        this.mStartPosition = getCurrentPosition();
        this.mStartTime = System.currentTimeMillis();
        onUpdatePlaybackState();
    }

    /* access modifiers changed from: package-private */
    public void onUpdatePlaybackState() {
        updatePlaybackState(this.mIsPlaying);
    }

    private void updatePlaybackState(boolean z) {
        if (this.mControlsRow != null) {
            int i = 0;
            if (!z) {
                onUpdateProgress();
                this.mPlayerAdapter.setProgressUpdatingEnabled(false);
            } else {
                this.mPlayerAdapter.setProgressUpdatingEnabled(true);
            }
            if (this.mFadeWhenPlaying && getHost() != null) {
                getHost().setControlsOverlayAutoHideEnabled(z);
            }
            ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) getControlsRow().getPrimaryActionsAdapter();
            if (!(this.mPlayPauseAction == null || this.mPlayPauseAction.getIndex() == z)) {
                this.mPlayPauseAction.setIndex(z ? 1 : 0);
                notifyItemChanged(arrayObjectAdapter, this.mPlayPauseAction);
            }
            PlaybackControlsRow.FastForwardAction fastForwardAction = this.mFastForwardAction;
            if (fastForwardAction != null) {
                int i2 = this.mPlaybackSpeed;
                int i3 = i2 >= 10 ? (i2 - 10) + 1 : 0;
                if (fastForwardAction.getIndex() != i3) {
                    this.mFastForwardAction.setIndex(i3);
                    notifyItemChanged(arrayObjectAdapter, this.mFastForwardAction);
                }
            }
            PlaybackControlsRow.RewindAction rewindAction = this.mRewindAction;
            if (rewindAction != null) {
                int i4 = this.mPlaybackSpeed;
                if (i4 <= -10) {
                    i = ((-i4) - 10) + 1;
                }
                if (rewindAction.getIndex() != i) {
                    this.mRewindAction.setIndex(i);
                    notifyItemChanged(arrayObjectAdapter, this.mRewindAction);
                }
            }
        }
    }

    public int[] getFastForwardSpeeds() {
        return this.mFastForwardSpeeds;
    }

    public int[] getRewindSpeeds() {
        return this.mRewindSpeeds;
    }

    private int getMaxForwardSpeedId() {
        return (this.mFastForwardSpeeds.length - 1) + 10;
    }

    private int getMaxRewindSpeedId() {
        return (this.mRewindSpeeds.length - 1) + 10;
    }

    public long getCurrentPosition() {
        int i;
        int i2 = this.mPlaybackSpeed;
        if (i2 == 0 || i2 == 1) {
            return this.mPlayerAdapter.getCurrentPosition();
        }
        if (i2 >= 10) {
            if (this.mIsCustomizedFastForwardSupported) {
                return this.mPlayerAdapter.getCurrentPosition();
            }
            i = getFastForwardSpeeds()[i2 - 10];
        } else if (i2 > -10) {
            return -1;
        } else {
            if (this.mIsCustomizedRewindSupported) {
                return this.mPlayerAdapter.getCurrentPosition();
            }
            i = -getRewindSpeeds()[(-i2) - 10];
        }
        long currentTimeMillis = this.mStartPosition + ((System.currentTimeMillis() - this.mStartTime) * ((long) i));
        if (currentTimeMillis > getDuration()) {
            this.mPlaybackSpeed = 0;
            long duration = getDuration();
            this.mPlayerAdapter.seekTo(duration);
            this.mStartPosition = 0;
            pause();
            return duration;
        } else if (currentTimeMillis >= 0) {
            return currentTimeMillis;
        } else {
            this.mPlaybackSpeed = 0;
            this.mPlayerAdapter.seekTo(0);
            this.mStartPosition = 0;
            pause();
            return 0;
        }
    }

    public void play() {
        if (this.mPlayerAdapter.isPrepared()) {
            if (this.mPlaybackSpeed != 0 || this.mPlayerAdapter.getCurrentPosition() < this.mPlayerAdapter.getDuration()) {
                this.mStartPosition = getCurrentPosition();
            } else {
                this.mStartPosition = 0;
            }
            this.mStartTime = System.currentTimeMillis();
            this.mIsPlaying = true;
            this.mPlaybackSpeed = 1;
            this.mPlayerAdapter.seekTo(this.mStartPosition);
            super.play();
            onUpdatePlaybackState();
        }
    }

    public void pause() {
        this.mIsPlaying = false;
        this.mPlaybackSpeed = 0;
        this.mStartPosition = getCurrentPosition();
        this.mStartTime = System.currentTimeMillis();
        super.pause();
        onUpdatePlaybackState();
    }

    private void fakePause() {
        this.mIsPlaying = true;
        this.mStartPosition = getCurrentPosition();
        this.mStartTime = System.currentTimeMillis();
        super.pause();
        onUpdatePlaybackState();
    }
}
