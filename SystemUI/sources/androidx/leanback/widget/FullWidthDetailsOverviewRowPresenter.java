package androidx.leanback.widget;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.leanback.C0742R;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.DetailsOverviewLogoPresenter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;
import androidx.recyclerview.widget.RecyclerView;

public class FullWidthDetailsOverviewRowPresenter extends RowPresenter {
    public static final int ALIGN_MODE_MIDDLE = 1;
    public static final int ALIGN_MODE_START = 0;
    static final boolean DEBUG = false;
    public static final int STATE_FULL = 1;
    public static final int STATE_HALF = 0;
    public static final int STATE_SMALL = 2;
    static final String TAG = "FullWidthDetailsRP";
    static final Handler sHandler = new Handler();
    OnActionClickedListener mActionClickedListener;
    private int mActionsBackgroundColor;
    private boolean mActionsBackgroundColorSet;
    private int mAlignmentMode;
    private int mBackgroundColor;
    private boolean mBackgroundColorSet;
    final DetailsOverviewLogoPresenter mDetailsOverviewLogoPresenter;
    final Presenter mDetailsPresenter;
    protected int mInitialState;
    private Listener mListener;
    private boolean mParticipatingEntranceTransition;

    public static abstract class Listener {
        public void onBindLogo(ViewHolder viewHolder) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean isClippingChildren() {
        return true;
    }

    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    class ActionsItemBridgeAdapter extends ItemBridgeAdapter {
        ViewHolder mViewHolder;

        ActionsItemBridgeAdapter(ViewHolder viewHolder) {
            this.mViewHolder = viewHolder;
        }

        public void onBind(final ItemBridgeAdapter.ViewHolder viewHolder) {
            if (this.mViewHolder.getOnItemViewClickedListener() != null || FullWidthDetailsOverviewRowPresenter.this.mActionClickedListener != null) {
                viewHolder.getPresenter().setOnClickListener(viewHolder.getViewHolder(), new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ActionsItemBridgeAdapter.this.mViewHolder.getOnItemViewClickedListener() != null) {
                            ActionsItemBridgeAdapter.this.mViewHolder.getOnItemViewClickedListener().onItemClicked(viewHolder.getViewHolder(), viewHolder.getItem(), ActionsItemBridgeAdapter.this.mViewHolder, ActionsItemBridgeAdapter.this.mViewHolder.getRow());
                        }
                        if (FullWidthDetailsOverviewRowPresenter.this.mActionClickedListener != null) {
                            FullWidthDetailsOverviewRowPresenter.this.mActionClickedListener.onActionClicked((Action) viewHolder.getItem());
                        }
                    }
                });
            }
        }

        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (this.mViewHolder.getOnItemViewClickedListener() != null || FullWidthDetailsOverviewRowPresenter.this.mActionClickedListener != null) {
                viewHolder.getPresenter().setOnClickListener(viewHolder.getViewHolder(), (View.OnClickListener) null);
            }
        }

        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.removeOnLayoutChangeListener(this.mViewHolder.mLayoutChangeListener);
            viewHolder.itemView.addOnLayoutChangeListener(this.mViewHolder.mLayoutChangeListener);
        }

        public void onDetachedFromWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.removeOnLayoutChangeListener(this.mViewHolder.mLayoutChangeListener);
            this.mViewHolder.checkFirstAndLastPosition(false);
        }
    }

    public class ViewHolder extends RowPresenter.ViewHolder {
        ItemBridgeAdapter mActionBridgeAdapter;
        final HorizontalGridView mActionsRow;
        final OnChildSelectedListener mChildSelectedListener;
        final ViewGroup mDetailsDescriptionFrame;
        final Presenter.ViewHolder mDetailsDescriptionViewHolder;
        final DetailsOverviewLogoPresenter.ViewHolder mDetailsLogoViewHolder;
        final View.OnLayoutChangeListener mLayoutChangeListener = new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                ViewHolder.this.checkFirstAndLastPosition(false);
            }
        };
        int mNumItems;
        final FrameLayout mOverviewFrame;
        final ViewGroup mOverviewRoot;
        protected final DetailsOverviewRow.Listener mRowListener = createRowListener();
        final RecyclerView.OnScrollListener mScrollListener;
        int mState = 0;
        final Runnable mUpdateDrawableCallback = new Runnable() {
            public void run() {
                Row row = ViewHolder.this.getRow();
                if (row != null) {
                    FullWidthDetailsOverviewRowPresenter.this.mDetailsOverviewLogoPresenter.onBindViewHolder(ViewHolder.this.mDetailsLogoViewHolder, row);
                }
            }
        };

        /* access modifiers changed from: protected */
        public DetailsOverviewRow.Listener createRowListener() {
            return new DetailsOverviewRowListener();
        }

        public class DetailsOverviewRowListener extends DetailsOverviewRow.Listener {
            public DetailsOverviewRowListener() {
            }

            public void onImageDrawableChanged(DetailsOverviewRow detailsOverviewRow) {
                FullWidthDetailsOverviewRowPresenter.sHandler.removeCallbacks(ViewHolder.this.mUpdateDrawableCallback);
                FullWidthDetailsOverviewRowPresenter.sHandler.post(ViewHolder.this.mUpdateDrawableCallback);
            }

            public void onItemChanged(DetailsOverviewRow detailsOverviewRow) {
                if (ViewHolder.this.mDetailsDescriptionViewHolder != null) {
                    FullWidthDetailsOverviewRowPresenter.this.mDetailsPresenter.onUnbindViewHolder(ViewHolder.this.mDetailsDescriptionViewHolder);
                }
                FullWidthDetailsOverviewRowPresenter.this.mDetailsPresenter.onBindViewHolder(ViewHolder.this.mDetailsDescriptionViewHolder, detailsOverviewRow.getItem());
            }

            public void onActionsAdapterChanged(DetailsOverviewRow detailsOverviewRow) {
                ViewHolder.this.bindActions(detailsOverviewRow.getActionsAdapter());
            }
        }

        /* access modifiers changed from: package-private */
        public void bindActions(ObjectAdapter objectAdapter) {
            this.mActionBridgeAdapter.setAdapter(objectAdapter);
            this.mActionsRow.setAdapter(this.mActionBridgeAdapter);
            this.mNumItems = this.mActionBridgeAdapter.getItemCount();
        }

        /* access modifiers changed from: package-private */
        public void unbindActions() {
            this.mActionBridgeAdapter.setAdapter((ObjectAdapter) null);
            this.mActionsRow.setAdapter((RecyclerView.Adapter) null);
            this.mNumItems = 0;
        }

        /* access modifiers changed from: package-private */
        public void onBind() {
            DetailsOverviewRow detailsOverviewRow = (DetailsOverviewRow) getRow();
            bindActions(detailsOverviewRow.getActionsAdapter());
            detailsOverviewRow.addListener(this.mRowListener);
        }

        /* access modifiers changed from: package-private */
        public void onUnbind() {
            unbindActions();
            ((DetailsOverviewRow) getRow()).removeListener(this.mRowListener);
            FullWidthDetailsOverviewRowPresenter.sHandler.removeCallbacks(this.mUpdateDrawableCallback);
        }

        /* access modifiers changed from: package-private */
        public void dispatchItemSelection(View view) {
            RecyclerView.ViewHolder viewHolder;
            if (isSelected()) {
                if (view != null) {
                    viewHolder = this.mActionsRow.getChildViewHolder(view);
                } else {
                    HorizontalGridView horizontalGridView = this.mActionsRow;
                    viewHolder = horizontalGridView.findViewHolderForPosition(horizontalGridView.getSelectedPosition());
                }
                ItemBridgeAdapter.ViewHolder viewHolder2 = (ItemBridgeAdapter.ViewHolder) viewHolder;
                if (viewHolder2 == null) {
                    if (getOnItemViewSelectedListener() != null) {
                        getOnItemViewSelectedListener().onItemSelected((Presenter.ViewHolder) null, (Object) null, this, getRow());
                    }
                } else if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(viewHolder2.getViewHolder(), viewHolder2.getItem(), this, getRow());
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void checkFirstAndLastPosition(boolean z) {
            RecyclerView.ViewHolder findViewHolderForPosition = this.mActionsRow.findViewHolderForPosition(this.mNumItems - 1);
            if (findViewHolderForPosition != null) {
                int right = findViewHolderForPosition.itemView.getRight();
                int width = this.mActionsRow.getWidth();
            }
            RecyclerView.ViewHolder findViewHolderForPosition2 = this.mActionsRow.findViewHolderForPosition(0);
            if (findViewHolderForPosition2 != null) {
                findViewHolderForPosition2.itemView.getLeft();
            }
        }

        public ViewHolder(View view, Presenter presenter, DetailsOverviewLogoPresenter detailsOverviewLogoPresenter) {
            super(view);
            C09953 r0 = new OnChildSelectedListener() {
                public void onChildSelected(ViewGroup viewGroup, View view, int i, long j) {
                    ViewHolder.this.dispatchItemSelection(view);
                }
            };
            this.mChildSelectedListener = r0;
            C09964 r1 = new RecyclerView.OnScrollListener() {
                public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                }

                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    ViewHolder.this.checkFirstAndLastPosition(true);
                }
            };
            this.mScrollListener = r1;
            ViewGroup viewGroup = (ViewGroup) view.findViewById(C0742R.C0745id.details_root);
            this.mOverviewRoot = viewGroup;
            FrameLayout frameLayout = (FrameLayout) view.findViewById(C0742R.C0745id.details_frame);
            this.mOverviewFrame = frameLayout;
            ViewGroup viewGroup2 = (ViewGroup) view.findViewById(C0742R.C0745id.details_overview_description);
            this.mDetailsDescriptionFrame = viewGroup2;
            HorizontalGridView horizontalGridView = (HorizontalGridView) frameLayout.findViewById(C0742R.C0745id.details_overview_actions);
            this.mActionsRow = horizontalGridView;
            horizontalGridView.setHasOverlappingRendering(false);
            horizontalGridView.setOnScrollListener(r1);
            horizontalGridView.setAdapter(this.mActionBridgeAdapter);
            horizontalGridView.setOnChildSelectedListener(r0);
            int dimensionPixelSize = view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_overview_actions_fade_size);
            horizontalGridView.setFadingRightEdgeLength(dimensionPixelSize);
            horizontalGridView.setFadingLeftEdgeLength(dimensionPixelSize);
            Presenter.ViewHolder onCreateViewHolder = presenter.onCreateViewHolder(viewGroup2);
            this.mDetailsDescriptionViewHolder = onCreateViewHolder;
            viewGroup2.addView(onCreateViewHolder.view);
            DetailsOverviewLogoPresenter.ViewHolder viewHolder = (DetailsOverviewLogoPresenter.ViewHolder) detailsOverviewLogoPresenter.onCreateViewHolder(viewGroup);
            this.mDetailsLogoViewHolder = viewHolder;
            viewGroup.addView(viewHolder.view);
        }

        public final ViewGroup getOverviewView() {
            return this.mOverviewFrame;
        }

        public final DetailsOverviewLogoPresenter.ViewHolder getLogoViewHolder() {
            return this.mDetailsLogoViewHolder;
        }

        public final Presenter.ViewHolder getDetailsDescriptionViewHolder() {
            return this.mDetailsDescriptionViewHolder;
        }

        public final ViewGroup getDetailsDescriptionFrame() {
            return this.mDetailsDescriptionFrame;
        }

        public final ViewGroup getActionsRow() {
            return this.mActionsRow;
        }

        public final int getState() {
            return this.mState;
        }
    }

    public FullWidthDetailsOverviewRowPresenter(Presenter presenter) {
        this(presenter, new DetailsOverviewLogoPresenter());
    }

    public FullWidthDetailsOverviewRowPresenter(Presenter presenter, DetailsOverviewLogoPresenter detailsOverviewLogoPresenter) {
        this.mInitialState = 0;
        this.mBackgroundColor = 0;
        this.mActionsBackgroundColor = 0;
        setHeaderPresenter((RowHeaderPresenter) null);
        setSelectEffectEnabled(false);
        this.mDetailsPresenter = presenter;
        this.mDetailsOverviewLogoPresenter = detailsOverviewLogoPresenter;
    }

    public void setOnActionClickedListener(OnActionClickedListener onActionClickedListener) {
        this.mActionClickedListener = onActionClickedListener;
    }

    public OnActionClickedListener getOnActionClickedListener() {
        return this.mActionClickedListener;
    }

    public final void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        this.mBackgroundColorSet = true;
    }

    public final int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public final void setActionsBackgroundColor(int i) {
        this.mActionsBackgroundColor = i;
        this.mActionsBackgroundColorSet = true;
    }

    public final int getActionsBackgroundColor() {
        return this.mActionsBackgroundColor;
    }

    public final boolean isParticipatingEntranceTransition() {
        return this.mParticipatingEntranceTransition;
    }

    public final void setParticipatingEntranceTransition(boolean z) {
        this.mParticipatingEntranceTransition = z;
    }

    public final void setInitialState(int i) {
        this.mInitialState = i;
    }

    public final int getInitialState() {
        return this.mInitialState;
    }

    public final void setAlignmentMode(int i) {
        this.mAlignmentMode = i;
    }

    public final int getAlignmentMode() {
        return this.mAlignmentMode;
    }

    public final void setListener(Listener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return C0742R.layout.lb_fullwidth_details_overview;
    }

    /* access modifiers changed from: protected */
    public RowPresenter.ViewHolder createRowViewHolder(ViewGroup viewGroup) {
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResourceId(), viewGroup, false), this.mDetailsPresenter, this.mDetailsOverviewLogoPresenter);
        this.mDetailsOverviewLogoPresenter.setContext(viewHolder.mDetailsLogoViewHolder, viewHolder, this);
        setState(viewHolder, this.mInitialState);
        viewHolder.mActionBridgeAdapter = new ActionsItemBridgeAdapter(viewHolder);
        FrameLayout frameLayout = viewHolder.mOverviewFrame;
        if (this.mBackgroundColorSet) {
            frameLayout.setBackgroundColor(this.mBackgroundColor);
        }
        if (this.mActionsBackgroundColorSet) {
            frameLayout.findViewById(C0742R.C0745id.details_overview_actions_background).setBackgroundColor(this.mActionsBackgroundColor);
        }
        RoundedRectHelper.setClipToRoundedOutline(frameLayout, true);
        if (!getSelectEffectEnabled()) {
            viewHolder.mOverviewFrame.setForeground((Drawable) null);
        }
        viewHolder.mActionsRow.setOnUnhandledKeyListener(new BaseGridView.OnUnhandledKeyListener() {
            public boolean onUnhandledKey(KeyEvent keyEvent) {
                if (viewHolder.getOnKeyListener() != null) {
                    return viewHolder.getOnKeyListener().onKey(viewHolder.view, keyEvent.getKeyCode(), keyEvent);
                }
                return false;
            }
        });
        return viewHolder;
    }

    /* access modifiers changed from: protected */
    public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
        super.onBindRowViewHolder(viewHolder, obj);
        DetailsOverviewRow detailsOverviewRow = (DetailsOverviewRow) obj;
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        this.mDetailsOverviewLogoPresenter.onBindViewHolder(viewHolder2.mDetailsLogoViewHolder, detailsOverviewRow);
        this.mDetailsPresenter.onBindViewHolder(viewHolder2.mDetailsDescriptionViewHolder, detailsOverviewRow.getItem());
        viewHolder2.onBind();
    }

    /* access modifiers changed from: protected */
    public void onUnbindRowViewHolder(RowPresenter.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        viewHolder2.onUnbind();
        this.mDetailsPresenter.onUnbindViewHolder(viewHolder2.mDetailsDescriptionViewHolder);
        this.mDetailsOverviewLogoPresenter.onUnbindViewHolder(viewHolder2.mDetailsLogoViewHolder);
        super.onUnbindRowViewHolder(viewHolder);
    }

    /* access modifiers changed from: protected */
    public void onSelectLevelChanged(RowPresenter.ViewHolder viewHolder) {
        super.onSelectLevelChanged(viewHolder);
        if (getSelectEffectEnabled()) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            ((ColorDrawable) viewHolder2.mOverviewFrame.getForeground().mutate()).setColor(viewHolder2.mColorDimmer.getPaint().getColor());
        }
    }

    /* access modifiers changed from: protected */
    public void onRowViewAttachedToWindow(RowPresenter.ViewHolder viewHolder) {
        super.onRowViewAttachedToWindow(viewHolder);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        this.mDetailsPresenter.onViewAttachedToWindow(viewHolder2.mDetailsDescriptionViewHolder);
        this.mDetailsOverviewLogoPresenter.onViewAttachedToWindow(viewHolder2.mDetailsLogoViewHolder);
    }

    /* access modifiers changed from: protected */
    public void onRowViewDetachedFromWindow(RowPresenter.ViewHolder viewHolder) {
        super.onRowViewDetachedFromWindow(viewHolder);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        this.mDetailsPresenter.onViewDetachedFromWindow(viewHolder2.mDetailsDescriptionViewHolder);
        this.mDetailsOverviewLogoPresenter.onViewDetachedFromWindow(viewHolder2.mDetailsLogoViewHolder);
    }

    public final void notifyOnBindLogo(ViewHolder viewHolder) {
        onLayoutOverviewFrame(viewHolder, viewHolder.getState(), true);
        onLayoutLogo(viewHolder, viewHolder.getState(), true);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onBindLogo(viewHolder);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayoutLogo(ViewHolder viewHolder, int i, boolean z) {
        View view = viewHolder.getLogoViewHolder().view;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (this.mAlignmentMode != 1) {
            marginLayoutParams.setMarginStart(view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_v2_logo_margin_start));
        } else {
            marginLayoutParams.setMarginStart(view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_v2_left) - marginLayoutParams.width);
        }
        int state = viewHolder.getState();
        if (state == 0) {
            marginLayoutParams.topMargin = view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_v2_blank_height) + view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_v2_actions_height) + view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_v2_description_margin_top);
        } else if (state != 2) {
            marginLayoutParams.topMargin = view.getResources().getDimensionPixelSize(C0742R.dimen.lb_details_v2_blank_height) - (marginLayoutParams.height / 2);
        } else {
            marginLayoutParams.topMargin = 0;
        }
        view.setLayoutParams(marginLayoutParams);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLayoutOverviewFrame(androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder r6, int r7, boolean r8) {
        /*
            r5 = this;
            r0 = 1
            r1 = 0
            r2 = 2
            if (r7 != r2) goto L_0x0007
            r7 = r0
            goto L_0x0008
        L_0x0007:
            r7 = r1
        L_0x0008:
            int r3 = r6.getState()
            if (r3 != r2) goto L_0x0010
            r2 = r0
            goto L_0x0011
        L_0x0010:
            r2 = r1
        L_0x0011:
            if (r7 != r2) goto L_0x0015
            if (r8 == 0) goto L_0x00ae
        L_0x0015:
            android.view.View r7 = r6.view
            android.content.res.Resources r7 = r7.getResources()
            androidx.leanback.widget.DetailsOverviewLogoPresenter r8 = r5.mDetailsOverviewLogoPresenter
            androidx.leanback.widget.DetailsOverviewLogoPresenter$ViewHolder r3 = r6.getLogoViewHolder()
            androidx.leanback.widget.Row r4 = r6.getRow()
            androidx.leanback.widget.DetailsOverviewRow r4 = (androidx.leanback.widget.DetailsOverviewRow) r4
            boolean r8 = r8.isBoundToImage(r3, r4)
            if (r8 == 0) goto L_0x003a
            androidx.leanback.widget.DetailsOverviewLogoPresenter$ViewHolder r8 = r6.getLogoViewHolder()
            android.view.View r8 = r8.view
            android.view.ViewGroup$LayoutParams r8 = r8.getLayoutParams()
            int r8 = r8.width
            goto L_0x003b
        L_0x003a:
            r8 = r1
        L_0x003b:
            int r5 = r5.mAlignmentMode
            if (r5 == r0) goto L_0x0051
            if (r2 == 0) goto L_0x0048
            int r5 = androidx.leanback.C0742R.dimen.lb_details_v2_logo_margin_start
            int r5 = r7.getDimensionPixelSize(r5)
            goto L_0x0062
        L_0x0048:
            int r5 = androidx.leanback.C0742R.dimen.lb_details_v2_logo_margin_start
            int r5 = r7.getDimensionPixelSize(r5)
            int r8 = r8 + r5
        L_0x004f:
            r5 = r1
            goto L_0x0062
        L_0x0051:
            if (r2 == 0) goto L_0x005b
            int r5 = androidx.leanback.C0742R.dimen.lb_details_v2_left
            int r5 = r7.getDimensionPixelSize(r5)
            int r5 = r5 - r8
            goto L_0x0062
        L_0x005b:
            int r5 = androidx.leanback.C0742R.dimen.lb_details_v2_left
            int r8 = r7.getDimensionPixelSize(r5)
            goto L_0x004f
        L_0x0062:
            android.view.ViewGroup r0 = r6.getOverviewView()
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            android.view.ViewGroup$MarginLayoutParams r0 = (android.view.ViewGroup.MarginLayoutParams) r0
            if (r2 == 0) goto L_0x0070
            r3 = r1
            goto L_0x0076
        L_0x0070:
            int r3 = androidx.leanback.C0742R.dimen.lb_details_v2_blank_height
            int r3 = r7.getDimensionPixelSize(r3)
        L_0x0076:
            r0.topMargin = r3
            r0.rightMargin = r5
            r0.leftMargin = r5
            android.view.ViewGroup r5 = r6.getOverviewView()
            r5.setLayoutParams(r0)
            android.view.ViewGroup r5 = r6.getDetailsDescriptionFrame()
            android.view.ViewGroup$LayoutParams r0 = r5.getLayoutParams()
            android.view.ViewGroup$MarginLayoutParams r0 = (android.view.ViewGroup.MarginLayoutParams) r0
            r0.setMarginStart(r8)
            r5.setLayoutParams(r0)
            android.view.ViewGroup r5 = r6.getActionsRow()
            android.view.ViewGroup$LayoutParams r6 = r5.getLayoutParams()
            android.view.ViewGroup$MarginLayoutParams r6 = (android.view.ViewGroup.MarginLayoutParams) r6
            r6.setMarginStart(r8)
            if (r2 == 0) goto L_0x00a3
            goto L_0x00a9
        L_0x00a3:
            int r8 = androidx.leanback.C0742R.dimen.lb_details_v2_actions_height
            int r1 = r7.getDimensionPixelSize(r8)
        L_0x00a9:
            r6.height = r1
            r5.setLayoutParams(r6)
        L_0x00ae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter.onLayoutOverviewFrame(androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter$ViewHolder, int, boolean):void");
    }

    public final void setState(ViewHolder viewHolder, int i) {
        if (viewHolder.getState() != i) {
            int state = viewHolder.getState();
            viewHolder.mState = i;
            onStateChanged(viewHolder, state);
        }
    }

    /* access modifiers changed from: protected */
    public void onStateChanged(ViewHolder viewHolder, int i) {
        onLayoutOverviewFrame(viewHolder, i, false);
        onLayoutLogo(viewHolder, i, false);
    }

    public void setEntranceTransitionState(RowPresenter.ViewHolder viewHolder, boolean z) {
        super.setEntranceTransitionState(viewHolder, z);
        if (this.mParticipatingEntranceTransition) {
            viewHolder.view.setVisibility(z ? 0 : 4);
        }
    }
}
