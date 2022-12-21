package androidx.leanback.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.leanback.C0742R;
import androidx.leanback.widget.ControlBarPresenter;
import androidx.leanback.widget.PlaybackControlsPresenter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackControlsRowView;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;

public class PlaybackControlsRowPresenter extends PlaybackRowPresenter {
    static float sShadowZ;
    private int mBackgroundColor;
    private boolean mBackgroundColorSet;
    private Presenter mDescriptionPresenter;
    OnActionClickedListener mOnActionClickedListener;
    private final ControlBarPresenter.OnControlClickedListener mOnControlClickedListener;
    private final ControlBarPresenter.OnControlSelectedListener mOnControlSelectedListener;
    PlaybackControlsPresenter mPlaybackControlsPresenter;
    private int mProgressColor;
    private boolean mProgressColorSet;
    private boolean mSecondaryActionsHidden;
    private ControlBarPresenter mSecondaryControlsPresenter;

    static class BoundData extends PlaybackControlsPresenter.BoundData {
        ViewHolder mRowViewHolder;

        BoundData() {
        }
    }

    public class ViewHolder extends PlaybackRowPresenter.ViewHolder {
        View mBgView;
        final View mBottomSpacer;
        final ViewGroup mCard;
        final ViewGroup mCardRightPanel;
        BoundData mControlsBoundData = new BoundData();
        final ViewGroup mControlsDock;
        int mControlsDockMarginEnd;
        int mControlsDockMarginStart;
        PlaybackControlsPresenter.ViewHolder mControlsVh;
        final ViewGroup mDescriptionDock;
        public final Presenter.ViewHolder mDescriptionViewHolder;
        final ImageView mImageView;
        final PlaybackControlsRow.OnPlaybackProgressCallback mListener = new PlaybackControlsRow.OnPlaybackProgressCallback() {
            public void onCurrentPositionChanged(PlaybackControlsRow playbackControlsRow, long j) {
                PlaybackControlsRowPresenter.this.mPlaybackControlsPresenter.setCurrentTimeLong(ViewHolder.this.mControlsVh, j);
            }

            public void onDurationChanged(PlaybackControlsRow playbackControlsRow, long j) {
                PlaybackControlsRowPresenter.this.mPlaybackControlsPresenter.setTotalTimeLong(ViewHolder.this.mControlsVh, j);
            }

            public void onBufferedPositionChanged(PlaybackControlsRow playbackControlsRow, long j) {
                PlaybackControlsRowPresenter.this.mPlaybackControlsPresenter.setSecondaryProgressLong(ViewHolder.this.mControlsVh, j);
            }
        };
        BoundData mSecondaryBoundData = new BoundData();
        final ViewGroup mSecondaryControlsDock;
        Presenter.ViewHolder mSecondaryControlsVh;
        Object mSelectedItem;
        Presenter.ViewHolder mSelectedViewHolder;
        final View mSpacer;

        ViewHolder(View view, Presenter presenter) {
            super(view);
            Presenter.ViewHolder viewHolder;
            this.mCard = (ViewGroup) view.findViewById(C0742R.C0745id.controls_card);
            this.mCardRightPanel = (ViewGroup) view.findViewById(C0742R.C0745id.controls_card_right_panel);
            this.mImageView = (ImageView) view.findViewById(C0742R.C0745id.image);
            ViewGroup viewGroup = (ViewGroup) view.findViewById(C0742R.C0745id.description_dock);
            this.mDescriptionDock = viewGroup;
            this.mControlsDock = (ViewGroup) view.findViewById(C0742R.C0745id.controls_dock);
            this.mSecondaryControlsDock = (ViewGroup) view.findViewById(C0742R.C0745id.secondary_controls_dock);
            this.mSpacer = view.findViewById(C0742R.C0745id.spacer);
            this.mBottomSpacer = view.findViewById(C0742R.C0745id.bottom_spacer);
            if (presenter == null) {
                viewHolder = null;
            } else {
                viewHolder = presenter.onCreateViewHolder(viewGroup);
            }
            this.mDescriptionViewHolder = viewHolder;
            if (viewHolder != null) {
                viewGroup.addView(viewHolder.view);
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
                ControlButtonPresenterSelector controlButtonPresenterSelector = (ControlButtonPresenterSelector) objectAdapter.getPresenterSelector();
                if (z) {
                    return controlButtonPresenterSelector.getPrimaryPresenter();
                }
                return controlButtonPresenterSelector.getSecondaryPresenter();
            }
            if (objectAdapter.size() > 0) {
                obj = objectAdapter.get(0);
            }
            return objectAdapter.getPresenter(obj);
        }

        /* access modifiers changed from: package-private */
        public void setOutline(View view) {
            View view2 = this.mBgView;
            if (view2 != null) {
                RoundedRectHelper.setClipToRoundedOutline(view2, false);
                ViewCompat.setZ(this.mBgView, 0.0f);
            }
            this.mBgView = view;
            RoundedRectHelper.setClipToRoundedOutline(view, true);
            if (PlaybackControlsRowPresenter.sShadowZ == 0.0f) {
                PlaybackControlsRowPresenter.sShadowZ = (float) view.getResources().getDimensionPixelSize(C0742R.dimen.lb_playback_controls_z);
            }
            ViewCompat.setZ(view, PlaybackControlsRowPresenter.sShadowZ);
        }
    }

    public PlaybackControlsRowPresenter(Presenter presenter) {
        this.mBackgroundColor = 0;
        this.mProgressColor = 0;
        C10271 r1 = new ControlBarPresenter.OnControlSelectedListener() {
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
        C10282 r2 = new ControlBarPresenter.OnControlClickedListener() {
            public void onControlClicked(Presenter.ViewHolder viewHolder, Object obj, ControlBarPresenter.BoundData boundData) {
                ViewHolder viewHolder2 = ((BoundData) boundData).mRowViewHolder;
                if (viewHolder2.getOnItemViewClickedListener() != null) {
                    viewHolder2.getOnItemViewClickedListener().onItemClicked(viewHolder, obj, viewHolder2, viewHolder2.getRow());
                }
                if (PlaybackControlsRowPresenter.this.mOnActionClickedListener != null && (obj instanceof Action)) {
                    PlaybackControlsRowPresenter.this.mOnActionClickedListener.onActionClicked((Action) obj);
                }
            }
        };
        this.mOnControlClickedListener = r2;
        setHeaderPresenter((RowHeaderPresenter) null);
        setSelectEffectEnabled(false);
        this.mDescriptionPresenter = presenter;
        this.mPlaybackControlsPresenter = new PlaybackControlsPresenter(C0742R.layout.lb_playback_controls);
        this.mSecondaryControlsPresenter = new ControlBarPresenter(C0742R.layout.lb_control_bar);
        this.mPlaybackControlsPresenter.setOnControlSelectedListener(r1);
        this.mSecondaryControlsPresenter.setOnControlSelectedListener(r1);
        this.mPlaybackControlsPresenter.setOnControlClickedListener(r2);
        this.mSecondaryControlsPresenter.setOnControlClickedListener(r2);
    }

    public PlaybackControlsRowPresenter() {
        this((Presenter) null);
    }

    public void setOnActionClickedListener(OnActionClickedListener onActionClickedListener) {
        this.mOnActionClickedListener = onActionClickedListener;
    }

    public OnActionClickedListener getOnActionClickedListener() {
        return this.mOnActionClickedListener;
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        this.mBackgroundColorSet = true;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setProgressColor(int i) {
        this.mProgressColor = i;
        this.mProgressColorSet = true;
    }

    public int getProgressColor() {
        return this.mProgressColor;
    }

    public void setSecondaryActionsHidden(boolean z) {
        this.mSecondaryActionsHidden = z;
    }

    public boolean areSecondaryActionsHidden() {
        return this.mSecondaryActionsHidden;
    }

    public void showBottomSpace(ViewHolder viewHolder, boolean z) {
        viewHolder.mBottomSpacer.setVisibility(z ? 0 : 8);
    }

    public void showPrimaryActions(ViewHolder viewHolder) {
        this.mPlaybackControlsPresenter.showPrimaryActions(viewHolder.mControlsVh);
        if (viewHolder.view.hasFocus()) {
            this.mPlaybackControlsPresenter.resetFocus(viewHolder.mControlsVh);
        }
    }

    public void onReappear(RowPresenter.ViewHolder viewHolder) {
        showPrimaryActions((ViewHolder) viewHolder);
    }

    private int getDefaultBackgroundColor(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C0742R.attr.defaultBrandColor, typedValue, true)) {
            return context.getResources().getColor(typedValue.resourceId);
        }
        return context.getResources().getColor(C0742R.C0743color.lb_default_brand_color);
    }

    private int getDefaultProgressColor(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C0742R.attr.playbackProgressPrimaryColor, typedValue, true)) {
            return context.getResources().getColor(typedValue.resourceId);
        }
        return context.getResources().getColor(C0742R.C0743color.lb_playback_progress_color_no_theme);
    }

    /* access modifiers changed from: protected */
    public RowPresenter.ViewHolder createRowViewHolder(ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0742R.layout.lb_playback_controls_row, viewGroup, false), this.mDescriptionPresenter);
        initRow(viewHolder);
        return viewHolder;
    }

    private void initRow(final ViewHolder viewHolder) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewHolder.mControlsDock.getLayoutParams();
        viewHolder.mControlsDockMarginStart = marginLayoutParams.getMarginStart();
        viewHolder.mControlsDockMarginEnd = marginLayoutParams.getMarginEnd();
        viewHolder.mControlsVh = (PlaybackControlsPresenter.ViewHolder) this.mPlaybackControlsPresenter.onCreateViewHolder(viewHolder.mControlsDock);
        this.mPlaybackControlsPresenter.setProgressColor(viewHolder.mControlsVh, this.mProgressColorSet ? this.mProgressColor : getDefaultProgressColor(viewHolder.mControlsDock.getContext()));
        this.mPlaybackControlsPresenter.setBackgroundColor(viewHolder.mControlsVh, this.mBackgroundColorSet ? this.mBackgroundColor : getDefaultBackgroundColor(viewHolder.view.getContext()));
        viewHolder.mControlsDock.addView(viewHolder.mControlsVh.view);
        viewHolder.mSecondaryControlsVh = this.mSecondaryControlsPresenter.onCreateViewHolder(viewHolder.mSecondaryControlsDock);
        if (!this.mSecondaryActionsHidden) {
            viewHolder.mSecondaryControlsDock.addView(viewHolder.mSecondaryControlsVh.view);
        }
        ((PlaybackControlsRowView) viewHolder.view).setOnUnhandledKeyListener(new PlaybackControlsRowView.OnUnhandledKeyListener() {
            public boolean onUnhandledKey(KeyEvent keyEvent) {
                return viewHolder.getOnKeyListener() != null && viewHolder.getOnKeyListener().onKey(viewHolder.view, keyEvent.getKeyCode(), keyEvent);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
        super.onBindRowViewHolder(viewHolder, obj);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        PlaybackControlsRow playbackControlsRow = (PlaybackControlsRow) viewHolder2.getRow();
        this.mPlaybackControlsPresenter.enableSecondaryActions(this.mSecondaryActionsHidden);
        if (playbackControlsRow.getItem() == null) {
            viewHolder2.mDescriptionDock.setVisibility(8);
            viewHolder2.mSpacer.setVisibility(8);
        } else {
            viewHolder2.mDescriptionDock.setVisibility(0);
            if (viewHolder2.mDescriptionViewHolder != null) {
                this.mDescriptionPresenter.onBindViewHolder(viewHolder2.mDescriptionViewHolder, playbackControlsRow.getItem());
            }
            viewHolder2.mSpacer.setVisibility(0);
        }
        if (playbackControlsRow.getImageDrawable() == null || playbackControlsRow.getItem() == null) {
            viewHolder2.mImageView.setImageDrawable((Drawable) null);
            updateCardLayout(viewHolder2, -2);
        } else {
            viewHolder2.mImageView.setImageDrawable(playbackControlsRow.getImageDrawable());
            updateCardLayout(viewHolder2, viewHolder2.mImageView.getLayoutParams().height);
        }
        viewHolder2.mControlsBoundData.adapter = playbackControlsRow.getPrimaryActionsAdapter();
        viewHolder2.mControlsBoundData.secondaryActionsAdapter = playbackControlsRow.getSecondaryActionsAdapter();
        viewHolder2.mControlsBoundData.presenter = viewHolder2.getPresenter(true);
        viewHolder2.mControlsBoundData.mRowViewHolder = viewHolder2;
        this.mPlaybackControlsPresenter.onBindViewHolder(viewHolder2.mControlsVh, viewHolder2.mControlsBoundData);
        viewHolder2.mSecondaryBoundData.adapter = playbackControlsRow.getSecondaryActionsAdapter();
        viewHolder2.mSecondaryBoundData.presenter = viewHolder2.getPresenter(false);
        viewHolder2.mSecondaryBoundData.mRowViewHolder = viewHolder2;
        this.mSecondaryControlsPresenter.onBindViewHolder(viewHolder2.mSecondaryControlsVh, viewHolder2.mSecondaryBoundData);
        this.mPlaybackControlsPresenter.setTotalTime(viewHolder2.mControlsVh, playbackControlsRow.getTotalTime());
        this.mPlaybackControlsPresenter.setCurrentTime(viewHolder2.mControlsVh, playbackControlsRow.getCurrentTime());
        this.mPlaybackControlsPresenter.setSecondaryProgress(viewHolder2.mControlsVh, playbackControlsRow.getBufferedProgress());
        playbackControlsRow.setOnPlaybackProgressChangedListener(viewHolder2.mListener);
    }

    private void updateCardLayout(ViewHolder viewHolder, int i) {
        int i2;
        ViewGroup.LayoutParams layoutParams = viewHolder.mCardRightPanel.getLayoutParams();
        layoutParams.height = i;
        viewHolder.mCardRightPanel.setLayoutParams(layoutParams);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewHolder.mControlsDock.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) viewHolder.mDescriptionDock.getLayoutParams();
        if (i == -2) {
            layoutParams2.height = -2;
            marginLayoutParams.setMarginStart(0);
            marginLayoutParams.setMarginEnd(0);
            viewHolder.mCard.setBackground((Drawable) null);
            viewHolder.setOutline(viewHolder.mControlsDock);
            this.mPlaybackControlsPresenter.enableTimeMargins(viewHolder.mControlsVh, true);
        } else {
            layoutParams2.height = 0;
            layoutParams2.weight = 1.0f;
            marginLayoutParams.setMarginStart(viewHolder.mControlsDockMarginStart);
            marginLayoutParams.setMarginEnd(viewHolder.mControlsDockMarginEnd);
            ViewGroup viewGroup = viewHolder.mCard;
            if (this.mBackgroundColorSet) {
                i2 = this.mBackgroundColor;
            } else {
                i2 = getDefaultBackgroundColor(viewHolder.mCard.getContext());
            }
            viewGroup.setBackgroundColor(i2);
            viewHolder.setOutline(viewHolder.mCard);
            this.mPlaybackControlsPresenter.enableTimeMargins(viewHolder.mControlsVh, false);
        }
        viewHolder.mDescriptionDock.setLayoutParams(layoutParams2);
        viewHolder.mControlsDock.setLayoutParams(marginLayoutParams);
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
