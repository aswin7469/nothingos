package androidx.leanback.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import androidx.leanback.C0742R;
import androidx.leanback.util.MathUtil;

public class PlaybackControlsRow extends Row {
    private long mBufferedProgressMs;
    private long mCurrentTimeMs;
    private Drawable mImageDrawable;
    private Object mItem;
    private OnPlaybackProgressCallback mListener;
    private ObjectAdapter mPrimaryActionsAdapter;
    private ObjectAdapter mSecondaryActionsAdapter;
    private long mTotalTimeMs;

    public static class OnPlaybackProgressCallback {
        public void onBufferedPositionChanged(PlaybackControlsRow playbackControlsRow, long j) {
        }

        public void onCurrentPositionChanged(PlaybackControlsRow playbackControlsRow, long j) {
        }

        public void onDurationChanged(PlaybackControlsRow playbackControlsRow, long j) {
        }
    }

    public static abstract class MultiAction extends Action {
        private Drawable[] mDrawables;
        private int mIndex;
        private String[] mLabels;
        private String[] mLabels2;

        public MultiAction(int i) {
            super((long) i);
        }

        public void setDrawables(Drawable[] drawableArr) {
            this.mDrawables = drawableArr;
            setIndex(0);
        }

        public void setLabels(String[] strArr) {
            this.mLabels = strArr;
            setIndex(0);
        }

        public void setSecondaryLabels(String[] strArr) {
            this.mLabels2 = strArr;
            setIndex(0);
        }

        public int getActionCount() {
            Drawable[] drawableArr = this.mDrawables;
            if (drawableArr != null) {
                return drawableArr.length;
            }
            String[] strArr = this.mLabels;
            if (strArr != null) {
                return strArr.length;
            }
            return 0;
        }

        public Drawable getDrawable(int i) {
            Drawable[] drawableArr = this.mDrawables;
            if (drawableArr == null) {
                return null;
            }
            return drawableArr[i];
        }

        public String getLabel(int i) {
            String[] strArr = this.mLabels;
            if (strArr == null) {
                return null;
            }
            return strArr[i];
        }

        public String getSecondaryLabel(int i) {
            String[] strArr = this.mLabels2;
            if (strArr == null) {
                return null;
            }
            return strArr[i];
        }

        public void nextIndex() {
            setIndex(this.mIndex < getActionCount() + -1 ? this.mIndex + 1 : 0);
        }

        public void setIndex(int i) {
            this.mIndex = i;
            Drawable[] drawableArr = this.mDrawables;
            if (drawableArr != null) {
                setIcon(drawableArr[i]);
            }
            String[] strArr = this.mLabels;
            if (strArr != null) {
                setLabel1(strArr[this.mIndex]);
            }
            String[] strArr2 = this.mLabels2;
            if (strArr2 != null) {
                setLabel2(strArr2[this.mIndex]);
            }
        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    public static class PlayPauseAction extends MultiAction {
        public static final int INDEX_PAUSE = 1;
        public static final int INDEX_PLAY = 0;
        @Deprecated
        public static final int PAUSE = 1;
        @Deprecated
        public static final int PLAY = 0;

        public PlayPauseAction(Context context) {
            super(C0742R.C0745id.lb_control_play_pause);
            setDrawables(new Drawable[]{PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_play), PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_pause)});
            setLabels(new String[]{context.getString(C0742R.string.lb_playback_controls_play), context.getString(C0742R.string.lb_playback_controls_pause)});
            addKeyCode(85);
            addKeyCode(126);
            addKeyCode(127);
        }
    }

    public static class FastForwardAction extends MultiAction {
        public FastForwardAction(Context context) {
            this(context, 1);
        }

        public FastForwardAction(Context context, int i) {
            super(C0742R.C0745id.lb_control_fast_forward);
            if (i >= 1) {
                Drawable[] drawableArr = new Drawable[(i + 1)];
                drawableArr[0] = PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_fast_forward);
                setDrawables(drawableArr);
                String[] strArr = new String[getActionCount()];
                strArr[0] = context.getString(C0742R.string.lb_playback_controls_fast_forward);
                String[] strArr2 = new String[getActionCount()];
                strArr2[0] = strArr[0];
                int i2 = 1;
                while (i2 <= i) {
                    int i3 = i2 + 1;
                    strArr[i2] = context.getResources().getString(C0742R.string.lb_control_display_fast_forward_multiplier, new Object[]{Integer.valueOf(i3)});
                    strArr2[i2] = context.getResources().getString(C0742R.string.lb_playback_controls_fast_forward_multiplier, new Object[]{Integer.valueOf(i3)});
                    i2 = i3;
                }
                setLabels(strArr);
                setSecondaryLabels(strArr2);
                addKeyCode(90);
                return;
            }
            throw new IllegalArgumentException("numSpeeds must be > 0");
        }
    }

    public static class RewindAction extends MultiAction {
        public RewindAction(Context context) {
            this(context, 1);
        }

        public RewindAction(Context context, int i) {
            super(C0742R.C0745id.lb_control_fast_rewind);
            if (i >= 1) {
                Drawable[] drawableArr = new Drawable[(i + 1)];
                drawableArr[0] = PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_rewind);
                setDrawables(drawableArr);
                String[] strArr = new String[getActionCount()];
                strArr[0] = context.getString(C0742R.string.lb_playback_controls_rewind);
                String[] strArr2 = new String[getActionCount()];
                strArr2[0] = strArr[0];
                int i2 = 1;
                while (i2 <= i) {
                    int i3 = i2 + 1;
                    String string = context.getResources().getString(C0742R.string.lb_control_display_rewind_multiplier, new Object[]{Integer.valueOf(i3)});
                    strArr[i2] = string;
                    strArr[i2] = string;
                    strArr2[i2] = context.getResources().getString(C0742R.string.lb_playback_controls_rewind_multiplier, new Object[]{Integer.valueOf(i3)});
                    i2 = i3;
                }
                setLabels(strArr);
                setSecondaryLabels(strArr2);
                addKeyCode(89);
                return;
            }
            throw new IllegalArgumentException("numSpeeds must be > 0");
        }
    }

    public static class SkipNextAction extends Action {
        public SkipNextAction(Context context) {
            super((long) C0742R.C0745id.lb_control_skip_next);
            setIcon(PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_skip_next));
            setLabel1(context.getString(C0742R.string.lb_playback_controls_skip_next));
            addKeyCode(87);
        }
    }

    public static class SkipPreviousAction extends Action {
        public SkipPreviousAction(Context context) {
            super((long) C0742R.C0745id.lb_control_skip_previous);
            setIcon(PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_skip_previous));
            setLabel1(context.getString(C0742R.string.lb_playback_controls_skip_previous));
            addKeyCode(88);
        }
    }

    public static class PictureInPictureAction extends Action {
        public PictureInPictureAction(Context context) {
            super((long) C0742R.C0745id.lb_control_picture_in_picture);
            setIcon(PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_picture_in_picture));
            setLabel1(context.getString(C0742R.string.lb_playback_controls_picture_in_picture));
            addKeyCode(171);
        }
    }

    public static class MoreActions extends Action {
        public MoreActions(Context context) {
            super((long) C0742R.C0745id.lb_control_more_actions);
            setIcon(context.getResources().getDrawable(C0742R.C0744drawable.lb_ic_more));
            setLabel1(context.getString(C0742R.string.lb_playback_controls_more_actions));
        }
    }

    public static abstract class ThumbsAction extends MultiAction {
        public static final int INDEX_OUTLINE = 1;
        public static final int INDEX_SOLID = 0;
        @Deprecated
        public static final int OUTLINE = 1;
        @Deprecated
        public static final int SOLID = 0;

        public ThumbsAction(int i, Context context, int i2, int i3) {
            super(i);
            setDrawables(new Drawable[]{PlaybackControlsRow.getStyledDrawable(context, i2), PlaybackControlsRow.getStyledDrawable(context, i3)});
        }
    }

    public static class ThumbsUpAction extends ThumbsAction {
        public ThumbsUpAction(Context context) {
            super(C0742R.C0745id.lb_control_thumbs_up, context, C0742R.styleable.lbPlaybackControlsActionIcons_thumb_up, C0742R.styleable.lbPlaybackControlsActionIcons_thumb_up_outline);
            String[] strArr = new String[getActionCount()];
            strArr[0] = context.getString(C0742R.string.lb_playback_controls_thumb_up);
            strArr[1] = context.getString(C0742R.string.lb_playback_controls_thumb_up_outline);
            setLabels(strArr);
        }
    }

    public static class ThumbsDownAction extends ThumbsAction {
        public ThumbsDownAction(Context context) {
            super(C0742R.C0745id.lb_control_thumbs_down, context, C0742R.styleable.lbPlaybackControlsActionIcons_thumb_down, C0742R.styleable.lbPlaybackControlsActionIcons_thumb_down_outline);
            String[] strArr = new String[getActionCount()];
            strArr[0] = context.getString(C0742R.string.lb_playback_controls_thumb_down);
            strArr[1] = context.getString(C0742R.string.lb_playback_controls_thumb_down_outline);
            setLabels(strArr);
        }
    }

    public static class RepeatAction extends MultiAction {
        @Deprecated
        public static final int ALL = 1;
        public static final int INDEX_ALL = 1;
        public static final int INDEX_NONE = 0;
        public static final int INDEX_ONE = 2;
        @Deprecated
        public static final int NONE = 0;
        @Deprecated
        public static final int ONE = 2;

        public RepeatAction(Context context) {
            this(context, PlaybackControlsRow.getIconHighlightColor(context));
        }

        public RepeatAction(Context context, int i) {
            this(context, i, i);
        }

        public RepeatAction(Context context, int i, int i2) {
            super(C0742R.C0745id.lb_control_repeat);
            BitmapDrawable bitmapDrawable;
            Drawable[] drawableArr = new Drawable[3];
            BitmapDrawable bitmapDrawable2 = (BitmapDrawable) PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_repeat);
            BitmapDrawable bitmapDrawable3 = (BitmapDrawable) PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_repeat_one);
            drawableArr[0] = bitmapDrawable2;
            BitmapDrawable bitmapDrawable4 = null;
            if (bitmapDrawable2 == null) {
                bitmapDrawable = null;
            } else {
                bitmapDrawable = new BitmapDrawable(context.getResources(), PlaybackControlsRow.createBitmap(bitmapDrawable2.getBitmap(), i));
            }
            drawableArr[1] = bitmapDrawable;
            drawableArr[2] = bitmapDrawable3 != null ? new BitmapDrawable(context.getResources(), PlaybackControlsRow.createBitmap(bitmapDrawable3.getBitmap(), i2)) : bitmapDrawable4;
            setDrawables(drawableArr);
            setLabels(new String[]{context.getString(C0742R.string.lb_playback_controls_repeat_all), context.getString(C0742R.string.lb_playback_controls_repeat_one), context.getString(C0742R.string.lb_playback_controls_repeat_none)});
        }
    }

    public static class ShuffleAction extends MultiAction {
        public static final int INDEX_OFF = 0;
        public static final int INDEX_ON = 1;
        @Deprecated
        public static final int OFF = 0;
        @Deprecated

        /* renamed from: ON */
        public static final int f148ON = 1;

        public ShuffleAction(Context context) {
            this(context, PlaybackControlsRow.getIconHighlightColor(context));
        }

        public ShuffleAction(Context context, int i) {
            super(C0742R.C0745id.lb_control_shuffle);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_shuffle);
            setDrawables(new Drawable[]{bitmapDrawable, new BitmapDrawable(context.getResources(), PlaybackControlsRow.createBitmap(bitmapDrawable.getBitmap(), i))});
            setLabels(new String[]{context.getString(C0742R.string.lb_playback_controls_shuffle_enable), context.getString(C0742R.string.lb_playback_controls_shuffle_disable)});
        }
    }

    public static class HighQualityAction extends MultiAction {
        public static final int INDEX_OFF = 0;
        public static final int INDEX_ON = 1;
        @Deprecated
        public static final int OFF = 0;
        @Deprecated

        /* renamed from: ON */
        public static final int f147ON = 1;

        public HighQualityAction(Context context) {
            this(context, PlaybackControlsRow.getIconHighlightColor(context));
        }

        public HighQualityAction(Context context, int i) {
            super(C0742R.C0745id.lb_control_high_quality);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_high_quality);
            setDrawables(new Drawable[]{bitmapDrawable, new BitmapDrawable(context.getResources(), PlaybackControlsRow.createBitmap(bitmapDrawable.getBitmap(), i))});
            setLabels(new String[]{context.getString(C0742R.string.lb_playback_controls_high_quality_enable), context.getString(C0742R.string.lb_playback_controls_high_quality_disable)});
        }
    }

    public static class ClosedCaptioningAction extends MultiAction {
        public static final int INDEX_OFF = 0;
        public static final int INDEX_ON = 1;
        @Deprecated
        public static final int OFF = 0;
        @Deprecated

        /* renamed from: ON */
        public static final int f146ON = 1;

        public ClosedCaptioningAction(Context context) {
            this(context, PlaybackControlsRow.getIconHighlightColor(context));
        }

        public ClosedCaptioningAction(Context context, int i) {
            super(C0742R.C0745id.lb_control_closed_captioning);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) PlaybackControlsRow.getStyledDrawable(context, C0742R.styleable.lbPlaybackControlsActionIcons_closed_captioning);
            setDrawables(new Drawable[]{bitmapDrawable, new BitmapDrawable(context.getResources(), PlaybackControlsRow.createBitmap(bitmapDrawable.getBitmap(), i))});
            setLabels(new String[]{context.getString(C0742R.string.lb_playback_controls_closed_captioning_enable), context.getString(C0742R.string.lb_playback_controls_closed_captioning_disable)});
        }
    }

    static Bitmap createBitmap(Bitmap bitmap, int i) {
        Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(copy);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return copy;
    }

    static int getIconHighlightColor(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C0742R.attr.playbackControlsIconHighlightColor, typedValue, true)) {
            return typedValue.data;
        }
        return context.getResources().getColor(C0742R.C0743color.lb_playback_icon_highlight_no_theme);
    }

    static Drawable getStyledDrawable(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(C0742R.attr.playbackControlsActionIcons, typedValue, false)) {
            return null;
        }
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(typedValue.data, C0742R.styleable.lbPlaybackControlsActionIcons);
        Drawable drawable = obtainStyledAttributes.getDrawable(i);
        obtainStyledAttributes.recycle();
        return drawable;
    }

    public PlaybackControlsRow(Object obj) {
        this.mItem = obj;
    }

    public PlaybackControlsRow() {
    }

    public final Object getItem() {
        return this.mItem;
    }

    public final void setImageDrawable(Drawable drawable) {
        this.mImageDrawable = drawable;
    }

    public final void setImageBitmap(Context context, Bitmap bitmap) {
        this.mImageDrawable = new BitmapDrawable(context.getResources(), bitmap);
    }

    public final Drawable getImageDrawable() {
        return this.mImageDrawable;
    }

    public final void setPrimaryActionsAdapter(ObjectAdapter objectAdapter) {
        this.mPrimaryActionsAdapter = objectAdapter;
    }

    public final void setSecondaryActionsAdapter(ObjectAdapter objectAdapter) {
        this.mSecondaryActionsAdapter = objectAdapter;
    }

    public final ObjectAdapter getPrimaryActionsAdapter() {
        return this.mPrimaryActionsAdapter;
    }

    public final ObjectAdapter getSecondaryActionsAdapter() {
        return this.mSecondaryActionsAdapter;
    }

    @Deprecated
    public void setTotalTime(int i) {
        setDuration((long) i);
    }

    @Deprecated
    public void setTotalTimeLong(long j) {
        setDuration(j);
    }

    public void setDuration(long j) {
        if (this.mTotalTimeMs != j) {
            this.mTotalTimeMs = j;
            OnPlaybackProgressCallback onPlaybackProgressCallback = this.mListener;
            if (onPlaybackProgressCallback != null) {
                onPlaybackProgressCallback.onDurationChanged(this, j);
            }
        }
    }

    @Deprecated
    public int getTotalTime() {
        return MathUtil.safeLongToInt(getTotalTimeLong());
    }

    @Deprecated
    public long getTotalTimeLong() {
        return this.mTotalTimeMs;
    }

    public long getDuration() {
        return this.mTotalTimeMs;
    }

    @Deprecated
    public void setCurrentTime(int i) {
        setCurrentTimeLong((long) i);
    }

    @Deprecated
    public void setCurrentTimeLong(long j) {
        setCurrentPosition(j);
    }

    public void setCurrentPosition(long j) {
        if (this.mCurrentTimeMs != j) {
            this.mCurrentTimeMs = j;
            OnPlaybackProgressCallback onPlaybackProgressCallback = this.mListener;
            if (onPlaybackProgressCallback != null) {
                onPlaybackProgressCallback.onCurrentPositionChanged(this, j);
            }
        }
    }

    @Deprecated
    public int getCurrentTime() {
        return MathUtil.safeLongToInt(getCurrentTimeLong());
    }

    @Deprecated
    public long getCurrentTimeLong() {
        return this.mCurrentTimeMs;
    }

    public long getCurrentPosition() {
        return this.mCurrentTimeMs;
    }

    @Deprecated
    public void setBufferedProgress(int i) {
        setBufferedPosition((long) i);
    }

    @Deprecated
    public void setBufferedProgressLong(long j) {
        setBufferedPosition(j);
    }

    public void setBufferedPosition(long j) {
        if (this.mBufferedProgressMs != j) {
            this.mBufferedProgressMs = j;
            OnPlaybackProgressCallback onPlaybackProgressCallback = this.mListener;
            if (onPlaybackProgressCallback != null) {
                onPlaybackProgressCallback.onBufferedPositionChanged(this, j);
            }
        }
    }

    @Deprecated
    public int getBufferedProgress() {
        return MathUtil.safeLongToInt(getBufferedPosition());
    }

    @Deprecated
    public long getBufferedProgressLong() {
        return this.mBufferedProgressMs;
    }

    public long getBufferedPosition() {
        return this.mBufferedProgressMs;
    }

    public Action getActionForKeyCode(int i) {
        Action actionForKeyCode = getActionForKeyCode(getPrimaryActionsAdapter(), i);
        if (actionForKeyCode != null) {
            return actionForKeyCode;
        }
        return getActionForKeyCode(getSecondaryActionsAdapter(), i);
    }

    public Action getActionForKeyCode(ObjectAdapter objectAdapter, int i) {
        if (objectAdapter == this.mPrimaryActionsAdapter || objectAdapter == this.mSecondaryActionsAdapter) {
            for (int i2 = 0; i2 < objectAdapter.size(); i2++) {
                Action action = (Action) objectAdapter.get(i2);
                if (action.respondsToKeyCode(i)) {
                    return action;
                }
            }
            return null;
        }
        throw new IllegalArgumentException("Invalid adapter");
    }

    public void setOnPlaybackProgressChangedListener(OnPlaybackProgressCallback onPlaybackProgressCallback) {
        this.mListener = onPlaybackProgressCallback;
    }
}
