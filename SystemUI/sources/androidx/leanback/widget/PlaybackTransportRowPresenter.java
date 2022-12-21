package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.leanback.C0742R;
import androidx.leanback.widget.ControlBarPresenter;
import androidx.leanback.widget.PlaybackControlsPresenter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.PlaybackSeekDataProvider;
import androidx.leanback.widget.PlaybackSeekUi;
import androidx.leanback.widget.PlaybackTransportRowView;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SeekBar;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.Arrays;

public class PlaybackTransportRowPresenter extends PlaybackRowPresenter {
    float mDefaultSeekIncrement = 0.01f;
    Presenter mDescriptionPresenter;
    OnActionClickedListener mOnActionClickedListener;
    private final ControlBarPresenter.OnControlClickedListener mOnControlClickedListener;
    private final ControlBarPresenter.OnControlSelectedListener mOnControlSelectedListener;
    ControlBarPresenter mPlaybackControlsPresenter;
    int mProgressColor = 0;
    boolean mProgressColorSet;
    ControlBarPresenter mSecondaryControlsPresenter;
    int mSecondaryProgressColor = 0;
    boolean mSecondaryProgressColorSet;

    static class BoundData extends PlaybackControlsPresenter.BoundData {
        ViewHolder mRowViewHolder;

        BoundData() {
        }
    }

    public class ViewHolder extends PlaybackRowPresenter.ViewHolder implements PlaybackSeekUi {
        BoundData mControlsBoundData = new BoundData();
        final ViewGroup mControlsDock;
        ControlBarPresenter.ViewHolder mControlsVh;
        final TextView mCurrentTime;
        long mCurrentTimeInMs = Long.MIN_VALUE;
        final ViewGroup mDescriptionDock;
        final Presenter.ViewHolder mDescriptionViewHolder;
        final ImageView mImageView;
        boolean mInSeek;
        final PlaybackControlsRow.OnPlaybackProgressCallback mListener = new PlaybackControlsRow.OnPlaybackProgressCallback() {
            public void onCurrentPositionChanged(PlaybackControlsRow playbackControlsRow, long j) {
                ViewHolder.this.setCurrentPosition(j);
            }

            public void onDurationChanged(PlaybackControlsRow playbackControlsRow, long j) {
                ViewHolder.this.setTotalTime(j);
            }

            public void onBufferedPositionChanged(PlaybackControlsRow playbackControlsRow, long j) {
                ViewHolder.this.setBufferedPosition(j);
            }
        };
        PlaybackControlsRow.PlayPauseAction mPlayPauseAction;
        long[] mPositions;
        int mPositionsLength;
        final SeekBar mProgressBar;
        BoundData mSecondaryBoundData = new BoundData();
        final ViewGroup mSecondaryControlsDock;
        ControlBarPresenter.ViewHolder mSecondaryControlsVh;
        long mSecondaryProgressInMs;
        PlaybackSeekUi.Client mSeekClient;
        PlaybackSeekDataProvider mSeekDataProvider;
        Object mSelectedItem;
        Presenter.ViewHolder mSelectedViewHolder;
        final StringBuilder mTempBuilder = new StringBuilder();
        int mThumbHeroIndex = -1;
        PlaybackSeekDataProvider.ResultCallback mThumbResult = new PlaybackSeekDataProvider.ResultCallback() {
            public void onThumbnailLoaded(Bitmap bitmap, int i) {
                int childCount = i - (ViewHolder.this.mThumbHeroIndex - (ViewHolder.this.mThumbsBar.getChildCount() / 2));
                if (childCount >= 0 && childCount < ViewHolder.this.mThumbsBar.getChildCount()) {
                    ViewHolder.this.mThumbsBar.setThumbBitmap(childCount, bitmap);
                }
            }
        };
        final ThumbsBar mThumbsBar;
        final TextView mTotalTime;
        long mTotalTimeInMs = Long.MIN_VALUE;

        /* access modifiers changed from: package-private */
        public void updateProgressInSeek(boolean z) {
            long j;
            long j2 = this.mCurrentTimeInMs;
            int i = this.mPositionsLength;
            long j3 = 0;
            if (i > 0) {
                int i2 = 0;
                int binarySearch = Arrays.binarySearch(this.mPositions, 0, i, j2);
                if (z) {
                    if (binarySearch < 0) {
                        int i3 = -1 - binarySearch;
                        if (i3 <= this.mPositionsLength - 1) {
                            j = this.mPositions[i3];
                            i2 = i3;
                        } else {
                            j = this.mTotalTimeInMs;
                            if (i3 > 0) {
                                i2 = i3 - 1;
                            }
                        }
                        j3 = j;
                    } else if (binarySearch < this.mPositionsLength - 1) {
                        i2 = binarySearch + 1;
                        j3 = this.mPositions[i2];
                    } else {
                        j3 = this.mTotalTimeInMs;
                        i2 = binarySearch;
                    }
                } else if (binarySearch < 0) {
                    int i4 = -1 - binarySearch;
                    if (i4 > 0) {
                        i2 = i4 - 1;
                        j3 = this.mPositions[i2];
                    }
                } else if (binarySearch > 0) {
                    i2 = binarySearch - 1;
                    j3 = this.mPositions[i2];
                }
                updateThumbsInSeek(i2, z);
            } else {
                long defaultSeekIncrement = (long) (((float) this.mTotalTimeInMs) * PlaybackTransportRowPresenter.this.getDefaultSeekIncrement());
                if (!z) {
                    defaultSeekIncrement = -defaultSeekIncrement;
                }
                long j4 = j2 + defaultSeekIncrement;
                long j5 = this.mTotalTimeInMs;
                if (j4 > j5) {
                    j3 = j5;
                } else if (j4 >= 0) {
                    j3 = j4;
                }
            }
            this.mProgressBar.setProgress((int) ((((double) j3) / ((double) this.mTotalTimeInMs)) * 2.147483647E9d));
            this.mSeekClient.onSeekPositionChanged(j3);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x0084 A[LOOP:2: B:28:0x0084->B:29:0x0086, LOOP_START, PHI: r7 
          PHI: (r7v1 int) = (r7v0 int), (r7v2 int) binds: [B:27:0x0082, B:29:0x0086] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0090 A[LOOP:3: B:30:0x0090->B:31:0x0092, LOOP_START, PHI: r5 
          PHI: (r5v5 int) = (r5v3 int), (r5v6 int) binds: [B:27:0x0082, B:31:0x0092] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x00a4 A[LOOP:4: B:32:0x009c->B:34:0x00a4, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x00b1 A[LOOP:5: B:36:0x00af->B:37:0x00b1, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x00ac A[EDGE_INSN: B:45:0x00ac->B:35:0x00ac ?: BREAK  , SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void updateThumbsInSeek(int r12, boolean r13) {
            /*
                r11 = this;
                int r0 = r11.mThumbHeroIndex
                if (r0 != r12) goto L_0x0005
                return
            L_0x0005:
                androidx.leanback.widget.ThumbsBar r0 = r11.mThumbsBar
                int r0 = r0.getChildCount()
                if (r0 < 0) goto L_0x00ba
                r1 = r0 & 1
                if (r1 == 0) goto L_0x00ba
                int r1 = r0 / 2
                int r2 = r12 - r1
                r3 = 0
                int r2 = java.lang.Math.max((int) r2, (int) r3)
                int r4 = r12 + r1
                int r5 = r11.mPositionsLength
                r6 = 1
                int r5 = r5 - r6
                int r4 = java.lang.Math.min((int) r4, (int) r5)
                int r5 = r11.mThumbHeroIndex
                if (r5 >= 0) goto L_0x002b
                r7 = r2
            L_0x0029:
                r5 = r4
                goto L_0x0080
            L_0x002b:
                if (r12 <= r5) goto L_0x002f
                r13 = r6
                goto L_0x0030
            L_0x002f:
                r13 = r3
            L_0x0030:
                int r5 = r5 - r1
                int r5 = java.lang.Math.max((int) r5, (int) r3)
                int r7 = r11.mThumbHeroIndex
                int r7 = r7 + r1
                int r8 = r11.mPositionsLength
                int r8 = r8 - r6
                int r7 = java.lang.Math.min((int) r7, (int) r8)
                if (r13 == 0) goto L_0x0061
                int r7 = r7 + r6
                int r5 = java.lang.Math.max((int) r7, (int) r2)
                r7 = r2
            L_0x0047:
                int r8 = r5 + -1
                if (r7 > r8) goto L_0x005f
                androidx.leanback.widget.ThumbsBar r8 = r11.mThumbsBar
                int r9 = r7 - r12
                int r9 = r9 + r1
                int r10 = r11.mThumbHeroIndex
                int r10 = r7 - r10
                int r10 = r10 + r1
                android.graphics.Bitmap r10 = r8.getThumbBitmap(r10)
                r8.setThumbBitmap(r9, r10)
                int r7 = r7 + 1
                goto L_0x0047
            L_0x005f:
                r7 = r5
                goto L_0x0029
            L_0x0061:
                int r5 = r5 - r6
                int r5 = java.lang.Math.min((int) r5, (int) r4)
                r7 = r4
            L_0x0067:
                int r8 = r5 + 1
                if (r7 < r8) goto L_0x007f
                androidx.leanback.widget.ThumbsBar r8 = r11.mThumbsBar
                int r9 = r7 - r12
                int r9 = r9 + r1
                int r10 = r11.mThumbHeroIndex
                int r10 = r7 - r10
                int r10 = r10 + r1
                android.graphics.Bitmap r10 = r8.getThumbBitmap(r10)
                r8.setThumbBitmap(r9, r10)
                int r7 = r7 + -1
                goto L_0x0067
            L_0x007f:
                r7 = r2
            L_0x0080:
                r11.mThumbHeroIndex = r12
                if (r13 == 0) goto L_0x0090
            L_0x0084:
                if (r7 > r5) goto L_0x009c
                androidx.leanback.widget.PlaybackSeekDataProvider r12 = r11.mSeekDataProvider
                androidx.leanback.widget.PlaybackSeekDataProvider$ResultCallback r13 = r11.mThumbResult
                r12.getThumbnail(r7, r13)
                int r7 = r7 + 1
                goto L_0x0084
            L_0x0090:
                if (r5 < r7) goto L_0x009c
                androidx.leanback.widget.PlaybackSeekDataProvider r12 = r11.mSeekDataProvider
                androidx.leanback.widget.PlaybackSeekDataProvider$ResultCallback r13 = r11.mThumbResult
                r12.getThumbnail(r5, r13)
                int r5 = r5 + -1
                goto L_0x0090
            L_0x009c:
                int r12 = r11.mThumbHeroIndex
                int r13 = r1 - r12
                int r13 = r13 + r2
                r5 = 0
                if (r3 >= r13) goto L_0x00ac
                androidx.leanback.widget.ThumbsBar r12 = r11.mThumbsBar
                r12.setThumbBitmap(r3, r5)
                int r3 = r3 + 1
                goto L_0x009c
            L_0x00ac:
                int r1 = r1 + r4
                int r1 = r1 - r12
                int r1 = r1 + r6
            L_0x00af:
                if (r1 >= r0) goto L_0x00b9
                androidx.leanback.widget.ThumbsBar r12 = r11.mThumbsBar
                r12.setThumbBitmap(r1, r5)
                int r1 = r1 + 1
                goto L_0x00af
            L_0x00b9:
                return
            L_0x00ba:
                java.lang.RuntimeException r11 = new java.lang.RuntimeException
                r11.<init>()
                throw r11
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.PlaybackTransportRowPresenter.ViewHolder.updateThumbsInSeek(int, boolean):void");
        }

        /* access modifiers changed from: package-private */
        public boolean onForward() {
            if (!startSeek()) {
                return false;
            }
            updateProgressInSeek(true);
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean onBackward() {
            if (!startSeek()) {
                return false;
            }
            updateProgressInSeek(false);
            return true;
        }

        public ViewHolder(View view, Presenter presenter) {
            super(view);
            Presenter.ViewHolder viewHolder;
            this.mImageView = (ImageView) view.findViewById(C0742R.C0745id.image);
            ViewGroup viewGroup = (ViewGroup) view.findViewById(C0742R.C0745id.description_dock);
            this.mDescriptionDock = viewGroup;
            this.mCurrentTime = (TextView) view.findViewById(C0742R.C0745id.current_time);
            this.mTotalTime = (TextView) view.findViewById(C0742R.C0745id.total_time);
            SeekBar seekBar = (SeekBar) view.findViewById(C0742R.C0745id.playback_progress);
            this.mProgressBar = seekBar;
            seekBar.setOnClickListener(new View.OnClickListener(PlaybackTransportRowPresenter.this) {
                public void onClick(View view) {
                    PlaybackTransportRowPresenter.this.onProgressBarClicked(ViewHolder.this);
                }
            });
            seekBar.setOnKeyListener(new View.OnKeyListener(PlaybackTransportRowPresenter.this) {
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (i != 4) {
                        if (i != 66) {
                            if (i != 69) {
                                if (i != 81) {
                                    if (i != 111) {
                                        if (i != 89) {
                                            if (i != 90) {
                                                switch (i) {
                                                    case 19:
                                                    case 20:
                                                        return ViewHolder.this.mInSeek;
                                                    case 21:
                                                        break;
                                                    case 22:
                                                        break;
                                                    case 23:
                                                        break;
                                                    default:
                                                        return false;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (keyEvent.getAction() == 0) {
                                    ViewHolder.this.onForward();
                                }
                                return true;
                            }
                            if (keyEvent.getAction() == 0) {
                                ViewHolder.this.onBackward();
                            }
                            return true;
                        }
                        if (!ViewHolder.this.mInSeek) {
                            return false;
                        }
                        if (keyEvent.getAction() == 1) {
                            ViewHolder.this.stopSeek(false);
                        }
                        return true;
                    }
                    if (!ViewHolder.this.mInSeek) {
                        return false;
                    }
                    if (keyEvent.getAction() == 1) {
                        ViewHolder.this.stopSeek(!ViewHolder.this.mProgressBar.isAccessibilityFocused());
                    }
                    return true;
                }
            });
            seekBar.setAccessibilitySeekListener(new SeekBar.AccessibilitySeekListener(PlaybackTransportRowPresenter.this) {
                public boolean onAccessibilitySeekForward() {
                    return ViewHolder.this.onForward();
                }

                public boolean onAccessibilitySeekBackward() {
                    return ViewHolder.this.onBackward();
                }
            });
            seekBar.setMax(Integer.MAX_VALUE);
            this.mControlsDock = (ViewGroup) view.findViewById(C0742R.C0745id.controls_dock);
            this.mSecondaryControlsDock = (ViewGroup) view.findViewById(C0742R.C0745id.secondary_controls_dock);
            if (presenter == null) {
                viewHolder = null;
            } else {
                viewHolder = presenter.onCreateViewHolder(viewGroup);
            }
            this.mDescriptionViewHolder = viewHolder;
            if (viewHolder != null) {
                viewGroup.addView(viewHolder.view);
            }
            this.mThumbsBar = (ThumbsBar) view.findViewById(C0742R.C0745id.thumbs_row);
        }

        public final Presenter.ViewHolder getDescriptionViewHolder() {
            return this.mDescriptionViewHolder;
        }

        public void setPlaybackSeekUiClient(PlaybackSeekUi.Client client) {
            this.mSeekClient = client;
        }

        /* access modifiers changed from: package-private */
        public boolean startSeek() {
            if (this.mInSeek) {
                return true;
            }
            PlaybackSeekUi.Client client = this.mSeekClient;
            if (client == null || !client.isSeekEnabled() || this.mTotalTimeInMs <= 0) {
                return false;
            }
            this.mInSeek = true;
            this.mSeekClient.onSeekStarted();
            PlaybackSeekDataProvider playbackSeekDataProvider = this.mSeekClient.getPlaybackSeekDataProvider();
            this.mSeekDataProvider = playbackSeekDataProvider;
            long[] seekPositions = playbackSeekDataProvider != null ? playbackSeekDataProvider.getSeekPositions() : null;
            this.mPositions = seekPositions;
            if (seekPositions != null) {
                int binarySearch = Arrays.binarySearch(seekPositions, this.mTotalTimeInMs);
                if (binarySearch >= 0) {
                    this.mPositionsLength = binarySearch + 1;
                } else {
                    this.mPositionsLength = -1 - binarySearch;
                }
            } else {
                this.mPositionsLength = 0;
            }
            this.mControlsVh.view.setVisibility(8);
            this.mSecondaryControlsVh.view.setVisibility(4);
            this.mDescriptionViewHolder.view.setVisibility(4);
            this.mThumbsBar.setVisibility(0);
            return true;
        }

        /* access modifiers changed from: package-private */
        public void stopSeek(boolean z) {
            if (this.mInSeek) {
                this.mInSeek = false;
                this.mSeekClient.onSeekFinished(z);
                PlaybackSeekDataProvider playbackSeekDataProvider = this.mSeekDataProvider;
                if (playbackSeekDataProvider != null) {
                    playbackSeekDataProvider.reset();
                }
                this.mThumbHeroIndex = -1;
                this.mThumbsBar.clearThumbBitmaps();
                this.mSeekDataProvider = null;
                this.mPositions = null;
                this.mPositionsLength = 0;
                this.mControlsVh.view.setVisibility(0);
                this.mSecondaryControlsVh.view.setVisibility(0);
                this.mDescriptionViewHolder.view.setVisibility(0);
                this.mThumbsBar.setVisibility(4);
            }
        }

        /* access modifiers changed from: package-private */
        public void dispatchItemSelection() {
            if (isSelected()) {
                if (this.mSelectedViewHolder == null) {
                    if (getOnItemViewSelectedListener() != null) {
                        getOnItemViewSelectedListener().onItemSelected((Presenter.ViewHolder) null, (Object) null, this, getRow());
                    }
                } else if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(this.mSelectedViewHolder, this.mSelectedItem, this, getRow());
                }
            }
        }

        /* access modifiers changed from: package-private */
        public Presenter getPresenter(boolean z) {
            ObjectAdapter objectAdapter;
            if (z) {
                objectAdapter = ((PlaybackControlsRow) getRow()).getPrimaryActionsAdapter();
            } else {
                objectAdapter = ((PlaybackControlsRow) getRow()).getSecondaryActionsAdapter();
            }
            Object obj = null;
            if (objectAdapter == null) {
                return null;
            }
            if (objectAdapter.getPresenterSelector() instanceof ControlButtonPresenterSelector) {
                return ((ControlButtonPresenterSelector) objectAdapter.getPresenterSelector()).getSecondaryPresenter();
            }
            if (objectAdapter.size() > 0) {
                obj = objectAdapter.get(0);
            }
            return objectAdapter.getPresenter(obj);
        }

        public final TextView getDurationView() {
            return this.mTotalTime;
        }

        /* access modifiers changed from: protected */
        public void onSetDurationLabel(long j) {
            if (this.mTotalTime != null) {
                PlaybackTransportRowPresenter.formatTime(j, this.mTempBuilder);
                this.mTotalTime.setText(this.mTempBuilder.toString());
            }
        }

        /* access modifiers changed from: package-private */
        public void setTotalTime(long j) {
            if (this.mTotalTimeInMs != j) {
                this.mTotalTimeInMs = j;
                onSetDurationLabel(j);
            }
        }

        public final TextView getCurrentPositionView() {
            return this.mCurrentTime;
        }

        /* access modifiers changed from: protected */
        public void onSetCurrentPositionLabel(long j) {
            if (this.mCurrentTime != null) {
                PlaybackTransportRowPresenter.formatTime(j, this.mTempBuilder);
                this.mCurrentTime.setText(this.mTempBuilder.toString());
            }
        }

        /* access modifiers changed from: package-private */
        public void setCurrentPosition(long j) {
            if (j != this.mCurrentTimeInMs) {
                this.mCurrentTimeInMs = j;
                onSetCurrentPositionLabel(j);
            }
            if (!this.mInSeek) {
                long j2 = this.mTotalTimeInMs;
                this.mProgressBar.setProgress(j2 > 0 ? (int) ((((double) this.mCurrentTimeInMs) / ((double) j2)) * 2.147483647E9d) : 0);
            }
        }

        /* access modifiers changed from: package-private */
        public void setBufferedPosition(long j) {
            this.mSecondaryProgressInMs = j;
            this.mProgressBar.setSecondaryProgress((int) ((((double) j) / ((double) this.mTotalTimeInMs)) * 2.147483647E9d));
        }
    }

    static void formatTime(long j, StringBuilder sb) {
        sb.setLength(0);
        if (j < 0) {
            sb.append("--");
            return;
        }
        long j2 = j / 1000;
        long j3 = j2 / 60;
        long j4 = j3 / 60;
        long j5 = j2 - (j3 * 60);
        long j6 = j3 - (60 * j4);
        if (j4 > 0) {
            sb.append(j4).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            if (j6 < 10) {
                sb.append('0');
            }
        }
        sb.append(j6).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        if (j5 < 10) {
            sb.append('0');
        }
        sb.append(j5);
    }

    public PlaybackTransportRowPresenter() {
        C10311 r1 = new ControlBarPresenter.OnControlSelectedListener() {
            public void onControlSelected(Presenter.ViewHolder viewHolder, Object obj, ControlBarPresenter.BoundData boundData) {
                ViewHolder viewHolder2 = ((BoundData) boundData).mRowViewHolder;
                if (viewHolder2.mSelectedViewHolder != viewHolder || viewHolder2.mSelectedItem != obj) {
                    viewHolder2.mSelectedViewHolder = viewHolder;
                    viewHolder2.mSelectedItem = obj;
                    viewHolder2.dispatchItemSelection();
                }
            }
        };
        this.mOnControlSelectedListener = r1;
        C10322 r2 = new ControlBarPresenter.OnControlClickedListener() {
            public void onControlClicked(Presenter.ViewHolder viewHolder, Object obj, ControlBarPresenter.BoundData boundData) {
                ViewHolder viewHolder2 = ((BoundData) boundData).mRowViewHolder;
                if (viewHolder2.getOnItemViewClickedListener() != null) {
                    viewHolder2.getOnItemViewClickedListener().onItemClicked(viewHolder, obj, viewHolder2, viewHolder2.getRow());
                }
                if (PlaybackTransportRowPresenter.this.mOnActionClickedListener != null && (obj instanceof Action)) {
                    PlaybackTransportRowPresenter.this.mOnActionClickedListener.onActionClicked((Action) obj);
                }
            }
        };
        this.mOnControlClickedListener = r2;
        setHeaderPresenter((RowHeaderPresenter) null);
        setSelectEffectEnabled(false);
        ControlBarPresenter controlBarPresenter = new ControlBarPresenter(C0742R.layout.lb_control_bar);
        this.mPlaybackControlsPresenter = controlBarPresenter;
        controlBarPresenter.setDefaultFocusToMiddle(false);
        ControlBarPresenter controlBarPresenter2 = new ControlBarPresenter(C0742R.layout.lb_control_bar);
        this.mSecondaryControlsPresenter = controlBarPresenter2;
        controlBarPresenter2.setDefaultFocusToMiddle(false);
        this.mPlaybackControlsPresenter.setOnControlSelectedListener(r1);
        this.mSecondaryControlsPresenter.setOnControlSelectedListener(r1);
        this.mPlaybackControlsPresenter.setOnControlClickedListener(r2);
        this.mSecondaryControlsPresenter.setOnControlClickedListener(r2);
    }

    public void setDescriptionPresenter(Presenter presenter) {
        this.mDescriptionPresenter = presenter;
    }

    public void setOnActionClickedListener(OnActionClickedListener onActionClickedListener) {
        this.mOnActionClickedListener = onActionClickedListener;
    }

    public OnActionClickedListener getOnActionClickedListener() {
        return this.mOnActionClickedListener;
    }

    public void setProgressColor(int i) {
        this.mProgressColor = i;
        this.mProgressColorSet = true;
    }

    public int getProgressColor() {
        return this.mProgressColor;
    }

    public void setSecondaryProgressColor(int i) {
        this.mSecondaryProgressColor = i;
        this.mSecondaryProgressColorSet = true;
    }

    public int getSecondaryProgressColor() {
        return this.mSecondaryProgressColor;
    }

    public void onReappear(RowPresenter.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        if (viewHolder2.view.hasFocus()) {
            viewHolder2.mProgressBar.requestFocus();
        }
    }

    private static int getDefaultProgressColor(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C0742R.attr.playbackProgressPrimaryColor, typedValue, true)) {
            return context.getResources().getColor(typedValue.resourceId);
        }
        return context.getResources().getColor(C0742R.C0743color.lb_playback_progress_color_no_theme);
    }

    private static int getDefaultSecondaryProgressColor(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C0742R.attr.playbackProgressSecondaryColor, typedValue, true)) {
            return context.getResources().getColor(typedValue.resourceId);
        }
        return context.getResources().getColor(C0742R.C0743color.lb_playback_progress_secondary_color_no_theme);
    }

    /* access modifiers changed from: protected */
    public RowPresenter.ViewHolder createRowViewHolder(ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0742R.layout.lb_playback_transport_controls_row, viewGroup, false), this.mDescriptionPresenter);
        initRow(viewHolder);
        return viewHolder;
    }

    private void initRow(final ViewHolder viewHolder) {
        int i;
        int i2;
        viewHolder.mControlsVh = (ControlBarPresenter.ViewHolder) this.mPlaybackControlsPresenter.onCreateViewHolder(viewHolder.mControlsDock);
        SeekBar seekBar = viewHolder.mProgressBar;
        if (this.mProgressColorSet) {
            i = this.mProgressColor;
        } else {
            i = getDefaultProgressColor(viewHolder.mControlsDock.getContext());
        }
        seekBar.setProgressColor(i);
        SeekBar seekBar2 = viewHolder.mProgressBar;
        if (this.mSecondaryProgressColorSet) {
            i2 = this.mSecondaryProgressColor;
        } else {
            i2 = getDefaultSecondaryProgressColor(viewHolder.mControlsDock.getContext());
        }
        seekBar2.setSecondaryProgressColor(i2);
        viewHolder.mControlsDock.addView(viewHolder.mControlsVh.view);
        viewHolder.mSecondaryControlsVh = (ControlBarPresenter.ViewHolder) this.mSecondaryControlsPresenter.onCreateViewHolder(viewHolder.mSecondaryControlsDock);
        viewHolder.mSecondaryControlsDock.addView(viewHolder.mSecondaryControlsVh.view);
        ((PlaybackTransportRowView) viewHolder.view.findViewById(C0742R.C0745id.transport_row)).setOnUnhandledKeyListener(new PlaybackTransportRowView.OnUnhandledKeyListener() {
            public boolean onUnhandledKey(KeyEvent keyEvent) {
                if (viewHolder.getOnKeyListener() != null) {
                    return viewHolder.getOnKeyListener().onKey(viewHolder.view, keyEvent.getKeyCode(), keyEvent);
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
        super.onBindRowViewHolder(viewHolder, obj);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        PlaybackControlsRow playbackControlsRow = (PlaybackControlsRow) viewHolder2.getRow();
        if (playbackControlsRow.getItem() == null) {
            viewHolder2.mDescriptionDock.setVisibility(8);
        } else {
            viewHolder2.mDescriptionDock.setVisibility(0);
            if (viewHolder2.mDescriptionViewHolder != null) {
                this.mDescriptionPresenter.onBindViewHolder(viewHolder2.mDescriptionViewHolder, playbackControlsRow.getItem());
            }
        }
        if (playbackControlsRow.getImageDrawable() == null) {
            viewHolder2.mImageView.setVisibility(8);
        } else {
            viewHolder2.mImageView.setVisibility(0);
        }
        viewHolder2.mImageView.setImageDrawable(playbackControlsRow.getImageDrawable());
        viewHolder2.mControlsBoundData.adapter = playbackControlsRow.getPrimaryActionsAdapter();
        viewHolder2.mControlsBoundData.presenter = viewHolder2.getPresenter(true);
        viewHolder2.mControlsBoundData.mRowViewHolder = viewHolder2;
        this.mPlaybackControlsPresenter.onBindViewHolder(viewHolder2.mControlsVh, viewHolder2.mControlsBoundData);
        viewHolder2.mSecondaryBoundData.adapter = playbackControlsRow.getSecondaryActionsAdapter();
        viewHolder2.mSecondaryBoundData.presenter = viewHolder2.getPresenter(false);
        viewHolder2.mSecondaryBoundData.mRowViewHolder = viewHolder2;
        this.mSecondaryControlsPresenter.onBindViewHolder(viewHolder2.mSecondaryControlsVh, viewHolder2.mSecondaryBoundData);
        viewHolder2.setTotalTime(playbackControlsRow.getDuration());
        viewHolder2.setCurrentPosition(playbackControlsRow.getCurrentPosition());
        viewHolder2.setBufferedPosition(playbackControlsRow.getBufferedPosition());
        playbackControlsRow.setOnPlaybackProgressChangedListener(viewHolder2.mListener);
    }

    /* access modifiers changed from: protected */
    public void onUnbindRowViewHolder(RowPresenter.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        PlaybackControlsRow playbackControlsRow = (PlaybackControlsRow) viewHolder2.getRow();
        if (viewHolder2.mDescriptionViewHolder != null) {
            this.mDescriptionPresenter.onUnbindViewHolder(viewHolder2.mDescriptionViewHolder);
        }
        this.mPlaybackControlsPresenter.onUnbindViewHolder(viewHolder2.mControlsVh);
        this.mSecondaryControlsPresenter.onUnbindViewHolder(viewHolder2.mSecondaryControlsVh);
        playbackControlsRow.setOnPlaybackProgressChangedListener((PlaybackControlsRow.OnPlaybackProgressCallback) null);
        super.onUnbindRowViewHolder(viewHolder);
    }

    /* access modifiers changed from: protected */
    public void onProgressBarClicked(ViewHolder viewHolder) {
        if (viewHolder != null) {
            if (viewHolder.mPlayPauseAction == null) {
                viewHolder.mPlayPauseAction = new PlaybackControlsRow.PlayPauseAction(viewHolder.view.getContext());
            }
            if (viewHolder.getOnItemViewClickedListener() != null) {
                viewHolder.getOnItemViewClickedListener().onItemClicked(viewHolder, viewHolder.mPlayPauseAction, viewHolder, viewHolder.getRow());
            }
            OnActionClickedListener onActionClickedListener = this.mOnActionClickedListener;
            if (onActionClickedListener != null) {
                onActionClickedListener.onActionClicked(viewHolder.mPlayPauseAction);
            }
        }
    }

    public void setDefaultSeekIncrement(float f) {
        this.mDefaultSeekIncrement = f;
    }

    public float getDefaultSeekIncrement() {
        return this.mDefaultSeekIncrement;
    }

    /* access modifiers changed from: protected */
    public void onRowViewSelected(RowPresenter.ViewHolder viewHolder, boolean z) {
        super.onRowViewSelected(viewHolder, z);
        if (z) {
            ((ViewHolder) viewHolder).dispatchItemSelection();
        }
    }

    /* access modifiers changed from: protected */
    public void onRowViewAttachedToWindow(RowPresenter.ViewHolder viewHolder) {
        super.onRowViewAttachedToWindow(viewHolder);
        Presenter presenter = this.mDescriptionPresenter;
        if (presenter != null) {
            presenter.onViewAttachedToWindow(((ViewHolder) viewHolder).mDescriptionViewHolder);
        }
    }

    /* access modifiers changed from: protected */
    public void onRowViewDetachedFromWindow(RowPresenter.ViewHolder viewHolder) {
        super.onRowViewDetachedFromWindow(viewHolder);
        Presenter presenter = this.mDescriptionPresenter;
        if (presenter != null) {
            presenter.onViewDetachedFromWindow(((ViewHolder) viewHolder).mDescriptionViewHolder);
        }
    }
}
