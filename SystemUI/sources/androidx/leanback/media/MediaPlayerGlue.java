package androidx.leanback.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import androidx.leanback.media.PlaybackGlue;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import java.p026io.IOException;
import java.util.List;

@Deprecated
public class MediaPlayerGlue extends PlaybackControlGlue implements OnItemViewSelectedListener {
    public static final int FAST_FORWARD_REWIND_REPEAT_DELAY = 200;
    public static final int FAST_FORWARD_REWIND_STEP = 10000;
    public static final int NO_REPEAT = 0;
    public static final int REPEAT_ALL = 2;
    public static final int REPEAT_ONE = 1;
    private static final String TAG = "MediaPlayerGlue";
    private String mArtist;
    private Drawable mCover;
    Handler mHandler;
    boolean mInitialized;
    private long mLastKeyDownEvent;
    private String mMediaSourcePath;
    private Uri mMediaSourceUri;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    MediaPlayer mPlayer;
    private final PlaybackControlsRow.RepeatAction mRepeatAction;
    private Runnable mRunnable;
    private Action mSelectedAction;
    protected final PlaybackControlsRow.ThumbsDownAction mThumbsDownAction;
    protected final PlaybackControlsRow.ThumbsUpAction mThumbsUpAction;
    private String mTitle;

    public long getSupportedActions() {
        return 224;
    }

    public void setCover(Drawable drawable) {
        this.mCover = drawable;
    }

    public void setArtist(String str) {
        this.mArtist = str;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public void setVideoUrl(String str) {
        setMediaSource(str);
        onMetadataChanged();
    }

    public MediaPlayerGlue(Context context) {
        this(context, new int[]{1}, new int[]{1});
    }

    public MediaPlayerGlue(Context context, int[] iArr, int[] iArr2) {
        super(context, iArr, iArr2);
        this.mPlayer = new MediaPlayer();
        this.mHandler = new Handler();
        this.mInitialized = false;
        this.mLastKeyDownEvent = 0;
        this.mMediaSourceUri = null;
        this.mMediaSourcePath = null;
        this.mRepeatAction = new PlaybackControlsRow.RepeatAction(getContext());
        PlaybackControlsRow.ThumbsDownAction thumbsDownAction = new PlaybackControlsRow.ThumbsDownAction(getContext());
        this.mThumbsDownAction = thumbsDownAction;
        PlaybackControlsRow.ThumbsUpAction thumbsUpAction = new PlaybackControlsRow.ThumbsUpAction(getContext());
        this.mThumbsUpAction = thumbsUpAction;
        thumbsDownAction.setIndex(1);
        thumbsUpAction.setIndex(1);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToHost(PlaybackGlueHost playbackGlueHost) {
        super.onAttachedToHost(playbackGlueHost);
        if (playbackGlueHost instanceof SurfaceHolderGlueHost) {
            ((SurfaceHolderGlueHost) playbackGlueHost).setSurfaceHolderCallback(new VideoPlayerSurfaceHolderCallback());
        }
    }

    public void reset() {
        changeToUnitialized();
        this.mPlayer.reset();
    }

    /* access modifiers changed from: package-private */
    public void changeToUnitialized() {
        if (this.mInitialized) {
            this.mInitialized = false;
            List<PlaybackGlue.PlayerCallback> playerCallbacks = getPlayerCallbacks();
            if (playerCallbacks != null) {
                for (PlaybackGlue.PlayerCallback onPreparedStateChanged : playerCallbacks) {
                    onPreparedStateChanged.onPreparedStateChanged(this);
                }
            }
        }
    }

    public void release() {
        changeToUnitialized();
        this.mPlayer.release();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromHost() {
        if (getHost() instanceof SurfaceHolderGlueHost) {
            ((SurfaceHolderGlueHost) getHost()).setSurfaceHolderCallback((SurfaceHolder.Callback) null);
        }
        reset();
        release();
        super.onDetachedFromHost();
    }

    /* access modifiers changed from: protected */
    public void onCreateSecondaryActions(ArrayObjectAdapter arrayObjectAdapter) {
        arrayObjectAdapter.add(this.mRepeatAction);
        arrayObjectAdapter.add(this.mThumbsDownAction);
        arrayObjectAdapter.add(this.mThumbsUpAction);
    }

    public void setDisplay(SurfaceHolder surfaceHolder) {
        this.mPlayer.setDisplay(surfaceHolder);
    }

    public void enableProgressUpdating(boolean z) {
        Runnable runnable = this.mRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        if (z) {
            if (this.mRunnable == null) {
                this.mRunnable = new Runnable() {
                    public void run() {
                        MediaPlayerGlue.this.updateProgress();
                        MediaPlayerGlue.this.mHandler.postDelayed(this, (long) MediaPlayerGlue.this.getUpdatePeriod());
                    }
                };
            }
            this.mHandler.postDelayed(this.mRunnable, (long) getUpdatePeriod());
        }
    }

    public void onActionClicked(Action action) {
        super.onActionClicked(action);
        if (action instanceof PlaybackControlsRow.RepeatAction) {
            ((PlaybackControlsRow.RepeatAction) action).nextIndex();
        } else {
            PlaybackControlsRow.ThumbsUpAction thumbsUpAction = this.mThumbsUpAction;
            if (action != thumbsUpAction) {
                PlaybackControlsRow.ThumbsDownAction thumbsDownAction = this.mThumbsDownAction;
                if (action == thumbsDownAction) {
                    if (thumbsDownAction.getIndex() == 0) {
                        this.mThumbsDownAction.setIndex(1);
                    } else {
                        this.mThumbsDownAction.setIndex(0);
                        this.mThumbsUpAction.setIndex(1);
                    }
                }
            } else if (thumbsUpAction.getIndex() == 0) {
                this.mThumbsUpAction.setIndex(1);
            } else {
                this.mThumbsUpAction.setIndex(0);
                this.mThumbsDownAction.setIndex(1);
            }
        }
        onMetadataChanged();
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        Action action = this.mSelectedAction;
        int i2 = 0;
        if (!((((((action instanceof PlaybackControlsRow.RewindAction) || (action instanceof PlaybackControlsRow.FastForwardAction)) && this.mInitialized) && keyEvent.getKeyCode() == 23) && keyEvent.getAction() == 0) && System.currentTimeMillis() - this.mLastKeyDownEvent > 200)) {
            return super.onKey(view, i, keyEvent);
        }
        this.mLastKeyDownEvent = System.currentTimeMillis();
        int currentPosition = getCurrentPosition() + 10000;
        if (this.mSelectedAction instanceof PlaybackControlsRow.RewindAction) {
            currentPosition = getCurrentPosition() - 10000;
        }
        if (currentPosition >= 0) {
            i2 = currentPosition;
        }
        if (i2 > getMediaDuration()) {
            i2 = getMediaDuration();
        }
        seekTo(i2);
        return true;
    }

    public boolean hasValidMedia() {
        return (this.mTitle == null || (this.mMediaSourcePath == null && this.mMediaSourceUri == null)) ? false : true;
    }

    public boolean isMediaPlaying() {
        return this.mInitialized && this.mPlayer.isPlaying();
    }

    public boolean isPlaying() {
        return isMediaPlaying();
    }

    public CharSequence getMediaTitle() {
        String str = this.mTitle;
        return str != null ? str : "N/a";
    }

    public CharSequence getMediaSubtitle() {
        String str = this.mArtist;
        return str != null ? str : "N/a";
    }

    public int getMediaDuration() {
        if (this.mInitialized) {
            return this.mPlayer.getDuration();
        }
        return 0;
    }

    public Drawable getMediaArt() {
        return this.mCover;
    }

    public int getCurrentSpeedId() {
        return isMediaPlaying() ? 1 : 0;
    }

    public int getCurrentPosition() {
        if (this.mInitialized) {
            return this.mPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void play(int i) {
        if (this.mInitialized && !this.mPlayer.isPlaying()) {
            this.mPlayer.start();
            onMetadataChanged();
            onStateChanged();
            updateProgress();
        }
    }

    public void pause() {
        if (isMediaPlaying()) {
            this.mPlayer.pause();
            onStateChanged();
        }
    }

    public void setMode(int i) {
        if (i == 0) {
            this.mOnCompletionListener = null;
        } else if (i == 1) {
            this.mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
                public boolean mFirstRepeat;

                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (!this.mFirstRepeat) {
                        this.mFirstRepeat = true;
                        mediaPlayer.setOnCompletionListener((MediaPlayer.OnCompletionListener) null);
                    }
                    MediaPlayerGlue.this.play();
                }
            };
        } else if (i == 2) {
            this.mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    MediaPlayerGlue.this.play();
                }
            };
        }
    }

    /* access modifiers changed from: protected */
    public void seekTo(int i) {
        if (this.mInitialized) {
            this.mPlayer.seekTo(i);
        }
    }

    public boolean setMediaSource(Uri uri) {
        Uri uri2 = this.mMediaSourceUri;
        if (uri2 != null) {
            if (uri2.equals(uri)) {
                return false;
            }
        } else if (uri == null) {
            return false;
        }
        this.mMediaSourceUri = uri;
        this.mMediaSourcePath = null;
        prepareMediaForPlaying();
        return true;
    }

    public boolean setMediaSource(String str) {
        String str2 = this.mMediaSourcePath;
        if (str2 != null) {
            if (str2.equals(str)) {
                return false;
            }
        } else if (str == null) {
            return false;
        }
        this.mMediaSourceUri = null;
        this.mMediaSourcePath = str;
        prepareMediaForPlaying();
        return true;
    }

    private void prepareMediaForPlaying() {
        reset();
        try {
            if (this.mMediaSourceUri != null) {
                this.mPlayer.setDataSource(getContext(), this.mMediaSourceUri);
            } else {
                String str = this.mMediaSourcePath;
                if (str != null) {
                    this.mPlayer.setDataSource(str);
                } else {
                    return;
                }
            }
            this.mPlayer.setAudioStreamType(3);
            this.mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    MediaPlayerGlue.this.mInitialized = true;
                    List<PlaybackGlue.PlayerCallback> playerCallbacks = MediaPlayerGlue.this.getPlayerCallbacks();
                    if (playerCallbacks != null) {
                        for (PlaybackGlue.PlayerCallback onPreparedStateChanged : playerCallbacks) {
                            onPreparedStateChanged.onPreparedStateChanged(MediaPlayerGlue.this);
                        }
                    }
                }
            });
            MediaPlayer.OnCompletionListener onCompletionListener = this.mOnCompletionListener;
            if (onCompletionListener != null) {
                this.mPlayer.setOnCompletionListener(onCompletionListener);
            }
            this.mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    if (MediaPlayerGlue.this.getControlsRow() != null) {
                        MediaPlayerGlue.this.getControlsRow().setBufferedProgress((int) (((float) mediaPlayer.getDuration()) * (((float) i) / 100.0f)));
                    }
                }
            });
            this.mPlayer.prepareAsync();
            onStateChanged();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException((Throwable) e);
        }
    }

    public void onItemSelected(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
        if (obj instanceof Action) {
            this.mSelectedAction = (Action) obj;
        } else {
            this.mSelectedAction = null;
        }
    }

    public boolean isPrepared() {
        return this.mInitialized;
    }

    class VideoPlayerSurfaceHolderCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        }

        VideoPlayerSurfaceHolderCallback() {
        }

        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            MediaPlayerGlue.this.setDisplay(surfaceHolder);
        }

        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            MediaPlayerGlue.this.setDisplay((SurfaceHolder) null);
        }
    }
}
