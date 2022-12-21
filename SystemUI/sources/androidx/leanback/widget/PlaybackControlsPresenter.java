package androidx.leanback.widget;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.leanback.C0742R;
import androidx.leanback.util.MathUtil;
import androidx.leanback.widget.ControlBarPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.Presenter;
import com.android.settingslib.accessibility.AccessibilityUtils;

class PlaybackControlsPresenter extends ControlBarPresenter {
    private static int sChildMarginBigger;
    private static int sChildMarginBiggest;
    private boolean mMoreActionsEnabled = true;

    static class BoundData extends ControlBarPresenter.BoundData {
        ObjectAdapter secondaryActionsAdapter;

        BoundData() {
        }
    }

    class ViewHolder extends ControlBarPresenter.ViewHolder {
        final TextView mCurrentTime;
        long mCurrentTimeInMs = -1;
        int mCurrentTimeMarginStart;
        StringBuilder mCurrentTimeStringBuilder = new StringBuilder();
        ObjectAdapter mMoreActionsAdapter;
        final FrameLayout mMoreActionsDock;
        ObjectAdapter.DataObserver mMoreActionsObserver;
        boolean mMoreActionsShowing;
        Presenter.ViewHolder mMoreActionsViewHolder;
        final ProgressBar mProgressBar;
        long mSecondaryProgressInMs = -1;
        final TextView mTotalTime;
        long mTotalTimeInMs = -1;
        int mTotalTimeMarginEnd;
        StringBuilder mTotalTimeStringBuilder = new StringBuilder();

        ViewHolder(View view) {
            super(view);
            this.mMoreActionsDock = (FrameLayout) view.findViewById(C0742R.C0745id.more_actions_dock);
            TextView textView = (TextView) view.findViewById(C0742R.C0745id.current_time);
            this.mCurrentTime = textView;
            TextView textView2 = (TextView) view.findViewById(C0742R.C0745id.total_time);
            this.mTotalTime = textView2;
            this.mProgressBar = (ProgressBar) view.findViewById(C0742R.C0745id.playback_progress);
            this.mMoreActionsObserver = new ObjectAdapter.DataObserver(PlaybackControlsPresenter.this) {
                public void onChanged() {
                    if (ViewHolder.this.mMoreActionsShowing) {
                        ViewHolder viewHolder = ViewHolder.this;
                        viewHolder.showControls(viewHolder.mPresenter);
                    }
                }

                public void onItemRangeChanged(int i, int i2) {
                    if (ViewHolder.this.mMoreActionsShowing) {
                        for (int i3 = 0; i3 < i2; i3++) {
                            ViewHolder viewHolder = ViewHolder.this;
                            viewHolder.bindControlToAction(i + i3, viewHolder.mPresenter);
                        }
                    }
                }
            };
            this.mCurrentTimeMarginStart = ((ViewGroup.MarginLayoutParams) textView.getLayoutParams()).getMarginStart();
            this.mTotalTimeMarginEnd = ((ViewGroup.MarginLayoutParams) textView2.getLayoutParams()).getMarginEnd();
        }

        /* access modifiers changed from: package-private */
        public void showMoreActions(boolean z) {
            if (z) {
                if (this.mMoreActionsViewHolder == null) {
                    PlaybackControlsRow.MoreActions moreActions = new PlaybackControlsRow.MoreActions(this.mMoreActionsDock.getContext());
                    this.mMoreActionsViewHolder = this.mPresenter.onCreateViewHolder(this.mMoreActionsDock);
                    this.mPresenter.onBindViewHolder(this.mMoreActionsViewHolder, moreActions);
                    this.mPresenter.setOnClickListener(this.mMoreActionsViewHolder, new View.OnClickListener() {
                        public void onClick(View view) {
                            ViewHolder.this.toggleMoreActions();
                        }
                    });
                }
                if (this.mMoreActionsViewHolder.view.getParent() == null) {
                    this.mMoreActionsDock.addView(this.mMoreActionsViewHolder.view);
                    return;
                }
                return;
            }
            Presenter.ViewHolder viewHolder = this.mMoreActionsViewHolder;
            if (viewHolder != null && viewHolder.view.getParent() != null) {
                this.mMoreActionsDock.removeView(this.mMoreActionsViewHolder.view);
            }
        }

        /* access modifiers changed from: package-private */
        public void toggleMoreActions() {
            this.mMoreActionsShowing = !this.mMoreActionsShowing;
            showControls(this.mPresenter);
        }

        /* access modifiers changed from: package-private */
        public ObjectAdapter getDisplayedAdapter() {
            return this.mMoreActionsShowing ? this.mMoreActionsAdapter : this.mAdapter;
        }

        /* access modifiers changed from: package-private */
        public int getChildMarginFromCenter(Context context, int i) {
            int childMarginDefault;
            int controlIconWidth = PlaybackControlsPresenter.this.getControlIconWidth(context);
            if (i < 4) {
                childMarginDefault = PlaybackControlsPresenter.this.getChildMarginBiggest(context);
            } else if (i < 6) {
                childMarginDefault = PlaybackControlsPresenter.this.getChildMarginBigger(context);
            } else {
                childMarginDefault = PlaybackControlsPresenter.this.getChildMarginDefault(context);
            }
            return controlIconWidth + childMarginDefault;
        }

        /* access modifiers changed from: package-private */
        public void setTotalTime(long j) {
            if (j <= 0) {
                this.mTotalTime.setVisibility(8);
                this.mProgressBar.setVisibility(8);
                return;
            }
            this.mTotalTime.setVisibility(0);
            this.mProgressBar.setVisibility(0);
            this.mTotalTimeInMs = j;
            PlaybackControlsPresenter.formatTime(j / 1000, this.mTotalTimeStringBuilder);
            this.mTotalTime.setText(this.mTotalTimeStringBuilder.toString());
            this.mProgressBar.setMax(Integer.MAX_VALUE);
        }

        /* access modifiers changed from: package-private */
        public long getTotalTime() {
            return this.mTotalTimeInMs;
        }

        /* access modifiers changed from: package-private */
        public void setCurrentTime(long j) {
            long j2 = j / 1000;
            if (j != this.mCurrentTimeInMs) {
                this.mCurrentTimeInMs = j;
                PlaybackControlsPresenter.formatTime(j2, this.mCurrentTimeStringBuilder);
                this.mCurrentTime.setText(this.mCurrentTimeStringBuilder.toString());
            }
            this.mProgressBar.setProgress((int) ((((double) this.mCurrentTimeInMs) / ((double) this.mTotalTimeInMs)) * 2.147483647E9d));
        }

        /* access modifiers changed from: package-private */
        public long getCurrentTime() {
            return this.mTotalTimeInMs;
        }

        /* access modifiers changed from: package-private */
        public void setSecondaryProgress(long j) {
            this.mSecondaryProgressInMs = j;
            this.mProgressBar.setSecondaryProgress((int) ((((double) j) / ((double) this.mTotalTimeInMs)) * 2.147483647E9d));
        }

        /* access modifiers changed from: package-private */
        public long getSecondaryProgress() {
            return this.mSecondaryProgressInMs;
        }
    }

    static void formatTime(long j, StringBuilder sb) {
        long j2 = j / 60;
        long j3 = j2 / 60;
        long j4 = j - (j2 * 60);
        long j5 = j2 - (60 * j3);
        sb.setLength(0);
        if (j3 > 0) {
            sb.append(j3).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            if (j5 < 10) {
                sb.append('0');
            }
        }
        sb.append(j5).append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        if (j4 < 10) {
            sb.append('0');
        }
        sb.append(j4);
    }

    public PlaybackControlsPresenter(int i) {
        super(i);
    }

    public void enableSecondaryActions(boolean z) {
        this.mMoreActionsEnabled = z;
    }

    public boolean areMoreActionsEnabled() {
        return this.mMoreActionsEnabled;
    }

    public void setProgressColor(ViewHolder viewHolder, int i) {
        ((LayerDrawable) viewHolder.mProgressBar.getProgressDrawable()).setDrawableByLayerId(16908301, new ClipDrawable(new ColorDrawable(i), 3, 1));
    }

    public void setTotalTime(ViewHolder viewHolder, int i) {
        setTotalTimeLong(viewHolder, (long) i);
    }

    public void setTotalTimeLong(ViewHolder viewHolder, long j) {
        viewHolder.setTotalTime(j);
    }

    public int getTotalTime(ViewHolder viewHolder) {
        return MathUtil.safeLongToInt(getTotalTimeLong(viewHolder));
    }

    public long getTotalTimeLong(ViewHolder viewHolder) {
        return viewHolder.getTotalTime();
    }

    public void setCurrentTime(ViewHolder viewHolder, int i) {
        setCurrentTimeLong(viewHolder, (long) i);
    }

    public void setCurrentTimeLong(ViewHolder viewHolder, long j) {
        viewHolder.setCurrentTime(j);
    }

    public int getCurrentTime(ViewHolder viewHolder) {
        return MathUtil.safeLongToInt(getCurrentTimeLong(viewHolder));
    }

    public long getCurrentTimeLong(ViewHolder viewHolder) {
        return viewHolder.getCurrentTime();
    }

    public void setSecondaryProgress(ViewHolder viewHolder, int i) {
        setSecondaryProgressLong(viewHolder, (long) i);
    }

    public void setSecondaryProgressLong(ViewHolder viewHolder, long j) {
        viewHolder.setSecondaryProgress(j);
    }

    public int getSecondaryProgress(ViewHolder viewHolder) {
        return MathUtil.safeLongToInt(getSecondaryProgressLong(viewHolder));
    }

    public long getSecondaryProgressLong(ViewHolder viewHolder) {
        return viewHolder.getSecondaryProgress();
    }

    public void showPrimaryActions(ViewHolder viewHolder) {
        if (viewHolder.mMoreActionsShowing) {
            viewHolder.toggleMoreActions();
        }
    }

    public void resetFocus(ViewHolder viewHolder) {
        viewHolder.mControlBar.requestFocus();
    }

    public void enableTimeMargins(ViewHolder viewHolder, boolean z) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewHolder.mCurrentTime.getLayoutParams();
        int i = 0;
        marginLayoutParams.setMarginStart(z ? viewHolder.mCurrentTimeMarginStart : 0);
        viewHolder.mCurrentTime.setLayoutParams(marginLayoutParams);
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) viewHolder.mTotalTime.getLayoutParams();
        if (z) {
            i = viewHolder.mTotalTimeMarginEnd;
        }
        marginLayoutParams2.setMarginEnd(i);
        viewHolder.mTotalTime.setLayoutParams(marginLayoutParams2);
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResourceId(), viewGroup, false));
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        BoundData boundData = (BoundData) obj;
        if (viewHolder2.mMoreActionsAdapter != boundData.secondaryActionsAdapter) {
            viewHolder2.mMoreActionsAdapter = boundData.secondaryActionsAdapter;
            viewHolder2.mMoreActionsAdapter.registerObserver(viewHolder2.mMoreActionsObserver);
            viewHolder2.mMoreActionsShowing = false;
        }
        super.onBindViewHolder(viewHolder, obj);
        viewHolder2.showMoreActions(this.mMoreActionsEnabled);
    }

    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        super.onUnbindViewHolder(viewHolder);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        if (viewHolder2.mMoreActionsAdapter != null) {
            viewHolder2.mMoreActionsAdapter.unregisterObserver(viewHolder2.mMoreActionsObserver);
            viewHolder2.mMoreActionsAdapter = null;
        }
    }

    /* access modifiers changed from: package-private */
    public int getChildMarginBigger(Context context) {
        if (sChildMarginBigger == 0) {
            sChildMarginBigger = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_playback_controls_child_margin_bigger);
        }
        return sChildMarginBigger;
    }

    /* access modifiers changed from: package-private */
    public int getChildMarginBiggest(Context context) {
        if (sChildMarginBiggest == 0) {
            sChildMarginBiggest = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_playback_controls_child_margin_biggest);
        }
        return sChildMarginBiggest;
    }
}
