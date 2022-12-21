package androidx.leanback.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.C0742R;
import androidx.leanback.system.Settings;
import androidx.leanback.transition.TransitionHelper;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.ShadowOverlayHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;

public class ListRowPresenter extends RowPresenter {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_RECYCLED_POOL_SIZE = 24;
    private static final String TAG = "ListRowPresenter";
    private static int sExpandedRowNoHovercardBottomPadding;
    private static int sExpandedSelectedRowTopPadding;
    private static int sSelectedRowTopPadding;
    private int mBrowseRowsFadingEdgeLength;
    private int mExpandedRowHeight;
    private int mFocusZoomFactor;
    private PresenterSelector mHoverCardPresenterSelector;
    private boolean mKeepChildForeground;
    private int mNumRows;
    private HashMap<Presenter, Integer> mRecycledPoolSize;
    private boolean mRoundedCornersEnabled;
    private int mRowHeight;
    private boolean mShadowEnabled;
    ShadowOverlayHelper mShadowOverlayHelper;
    private ItemBridgeAdapter.Wrapper mShadowOverlayWrapper;
    private boolean mUseFocusDimmer;

    public boolean isUsingDefaultListSelectEffect() {
        return true;
    }

    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    public static class ViewHolder extends RowPresenter.ViewHolder {
        final HorizontalGridView mGridView;
        final HorizontalHoverCardSwitcher mHoverCardViewSwitcher = new HorizontalHoverCardSwitcher();
        ItemBridgeAdapter mItemBridgeAdapter;
        final ListRowPresenter mListRowPresenter;
        final int mPaddingBottom;
        final int mPaddingLeft;
        final int mPaddingRight;
        final int mPaddingTop;

        public ViewHolder(View view, HorizontalGridView horizontalGridView, ListRowPresenter listRowPresenter) {
            super(view);
            this.mGridView = horizontalGridView;
            this.mListRowPresenter = listRowPresenter;
            this.mPaddingTop = horizontalGridView.getPaddingTop();
            this.mPaddingBottom = horizontalGridView.getPaddingBottom();
            this.mPaddingLeft = horizontalGridView.getPaddingLeft();
            this.mPaddingRight = horizontalGridView.getPaddingRight();
        }

        public final ListRowPresenter getListRowPresenter() {
            return this.mListRowPresenter;
        }

        public final HorizontalGridView getGridView() {
            return this.mGridView;
        }

        public final ItemBridgeAdapter getBridgeAdapter() {
            return this.mItemBridgeAdapter;
        }

        public int getSelectedPosition() {
            return this.mGridView.getSelectedPosition();
        }

        public Presenter.ViewHolder getItemViewHolder(int i) {
            ItemBridgeAdapter.ViewHolder viewHolder = (ItemBridgeAdapter.ViewHolder) this.mGridView.findViewHolderForAdapterPosition(i);
            if (viewHolder == null) {
                return null;
            }
            return viewHolder.getViewHolder();
        }

        public Presenter.ViewHolder getSelectedItemViewHolder() {
            return getItemViewHolder(getSelectedPosition());
        }

        public Object getSelectedItem() {
            ItemBridgeAdapter.ViewHolder viewHolder = (ItemBridgeAdapter.ViewHolder) this.mGridView.findViewHolderForAdapterPosition(getSelectedPosition());
            if (viewHolder == null) {
                return null;
            }
            return viewHolder.getItem();
        }
    }

    public static class SelectItemViewHolderTask extends Presenter.ViewHolderTask {
        private int mItemPosition;
        Presenter.ViewHolderTask mItemTask;
        private boolean mSmoothScroll = true;

        public SelectItemViewHolderTask(int i) {
            setItemPosition(i);
        }

        public void setItemPosition(int i) {
            this.mItemPosition = i;
        }

        public int getItemPosition() {
            return this.mItemPosition;
        }

        public void setSmoothScroll(boolean z) {
            this.mSmoothScroll = z;
        }

        public boolean isSmoothScroll() {
            return this.mSmoothScroll;
        }

        public Presenter.ViewHolderTask getItemTask() {
            return this.mItemTask;
        }

        public void setItemTask(Presenter.ViewHolderTask viewHolderTask) {
            this.mItemTask = viewHolderTask;
        }

        public void run(Presenter.ViewHolder viewHolder) {
            if (viewHolder instanceof ViewHolder) {
                HorizontalGridView gridView = ((ViewHolder) viewHolder).getGridView();
                C10201 r0 = this.mItemTask != null ? new ViewHolderTask() {
                    final Presenter.ViewHolderTask itemTask;

                    {
                        this.itemTask = SelectItemViewHolderTask.this.mItemTask;
                    }

                    public void run(RecyclerView.ViewHolder viewHolder) {
                        this.itemTask.run(((ItemBridgeAdapter.ViewHolder) viewHolder).getViewHolder());
                    }
                } : null;
                if (isSmoothScroll()) {
                    gridView.setSelectedPositionSmooth(this.mItemPosition, r0);
                } else {
                    gridView.setSelectedPosition(this.mItemPosition, (ViewHolderTask) r0);
                }
            }
        }
    }

    class ListRowPresenterItemBridgeAdapter extends ItemBridgeAdapter {
        ViewHolder mRowViewHolder;

        ListRowPresenterItemBridgeAdapter(ViewHolder viewHolder) {
            this.mRowViewHolder = viewHolder;
        }

        /* access modifiers changed from: protected */
        public void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                TransitionHelper.setTransitionGroup((ViewGroup) viewHolder.itemView, true);
            }
            if (ListRowPresenter.this.mShadowOverlayHelper != null) {
                ListRowPresenter.this.mShadowOverlayHelper.onViewCreated(viewHolder.itemView);
            }
        }

        public void onBind(final ItemBridgeAdapter.ViewHolder viewHolder) {
            if (this.mRowViewHolder.getOnItemViewClickedListener() != null) {
                viewHolder.mHolder.view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ItemBridgeAdapter.ViewHolder viewHolder = (ItemBridgeAdapter.ViewHolder) ListRowPresenterItemBridgeAdapter.this.mRowViewHolder.mGridView.getChildViewHolder(viewHolder.itemView);
                        if (ListRowPresenterItemBridgeAdapter.this.mRowViewHolder.getOnItemViewClickedListener() != null) {
                            ListRowPresenterItemBridgeAdapter.this.mRowViewHolder.getOnItemViewClickedListener().onItemClicked(viewHolder.mHolder, viewHolder.mItem, ListRowPresenterItemBridgeAdapter.this.mRowViewHolder, (ListRow) ListRowPresenterItemBridgeAdapter.this.mRowViewHolder.mRow);
                        }
                    }
                });
            }
        }

        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (this.mRowViewHolder.getOnItemViewClickedListener() != null) {
                viewHolder.mHolder.view.setOnClickListener((View.OnClickListener) null);
            }
        }

        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            ListRowPresenter.this.applySelectLevelToChild(this.mRowViewHolder, viewHolder.itemView);
            this.mRowViewHolder.syncActivatedStatus(viewHolder.itemView);
        }

        public void onAddPresenter(Presenter presenter, int i) {
            this.mRowViewHolder.getGridView().getRecycledViewPool().setMaxRecycledViews(i, ListRowPresenter.this.getRecycledPoolSize(presenter));
        }
    }

    public ListRowPresenter() {
        this(2);
    }

    public ListRowPresenter(int i) {
        this(i, false);
    }

    public ListRowPresenter(int i, boolean z) {
        this.mNumRows = 1;
        this.mShadowEnabled = true;
        this.mBrowseRowsFadingEdgeLength = -1;
        this.mRoundedCornersEnabled = true;
        this.mKeepChildForeground = true;
        this.mRecycledPoolSize = new HashMap<>();
        if (FocusHighlightHelper.isValidZoomIndex(i)) {
            this.mFocusZoomFactor = i;
            this.mUseFocusDimmer = z;
            return;
        }
        throw new IllegalArgumentException("Unhandled zoom factor");
    }

    public void setRowHeight(int i) {
        this.mRowHeight = i;
    }

    public int getRowHeight() {
        return this.mRowHeight;
    }

    public void setExpandedRowHeight(int i) {
        this.mExpandedRowHeight = i;
    }

    public int getExpandedRowHeight() {
        int i = this.mExpandedRowHeight;
        return i != 0 ? i : this.mRowHeight;
    }

    public final int getFocusZoomFactor() {
        return this.mFocusZoomFactor;
    }

    @Deprecated
    public final int getZoomFactor() {
        return this.mFocusZoomFactor;
    }

    public final boolean isFocusDimmerUsed() {
        return this.mUseFocusDimmer;
    }

    public void setNumRows(int i) {
        this.mNumRows = i;
    }

    /* access modifiers changed from: protected */
    public void initializeRowViewHolder(RowPresenter.ViewHolder viewHolder) {
        super.initializeRowViewHolder(viewHolder);
        final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        Context context = viewHolder.view.getContext();
        boolean z = true;
        if (this.mShadowOverlayHelper == null) {
            ShadowOverlayHelper build = new ShadowOverlayHelper.Builder().needsOverlay(needsDefaultListSelectEffect()).needsShadow(needsDefaultShadow()).needsRoundedCorner(isUsingOutlineClipping(context) && areChildRoundedCornersEnabled()).preferZOrder(isUsingZOrder(context)).keepForegroundDrawable(this.mKeepChildForeground).options(createShadowOverlayOptions()).build(context);
            this.mShadowOverlayHelper = build;
            if (build.needsWrapper()) {
                this.mShadowOverlayWrapper = new ItemBridgeAdapterShadowOverlayWrapper(this.mShadowOverlayHelper);
            }
        }
        viewHolder2.mItemBridgeAdapter = new ListRowPresenterItemBridgeAdapter(viewHolder2);
        viewHolder2.mItemBridgeAdapter.setWrapper(this.mShadowOverlayWrapper);
        this.mShadowOverlayHelper.prepareParentForShadow(viewHolder2.mGridView);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(viewHolder2.mItemBridgeAdapter, this.mFocusZoomFactor, this.mUseFocusDimmer);
        HorizontalGridView horizontalGridView = viewHolder2.mGridView;
        if (this.mShadowOverlayHelper.getShadowType() == 3) {
            z = false;
        }
        horizontalGridView.setFocusDrawingOrderEnabled(z);
        viewHolder2.mGridView.setOnChildSelectedListener(new OnChildSelectedListener() {
            public void onChildSelected(ViewGroup viewGroup, View view, int i, long j) {
                ListRowPresenter.this.selectChildView(viewHolder2, view, true);
            }
        });
        viewHolder2.mGridView.setOnUnhandledKeyListener(new BaseGridView.OnUnhandledKeyListener() {
            public boolean onUnhandledKey(KeyEvent keyEvent) {
                return viewHolder2.getOnKeyListener() != null && viewHolder2.getOnKeyListener().onKey(viewHolder2.view, keyEvent.getKeyCode(), keyEvent);
            }
        });
        viewHolder2.mGridView.setNumRows(this.mNumRows);
    }

    /* access modifiers changed from: package-private */
    public final boolean needsDefaultListSelectEffect() {
        return isUsingDefaultListSelectEffect() && getSelectEffectEnabled();
    }

    public void setRecycledPoolSize(Presenter presenter, int i) {
        this.mRecycledPoolSize.put(presenter, Integer.valueOf(i));
    }

    public int getRecycledPoolSize(Presenter presenter) {
        if (this.mRecycledPoolSize.containsKey(presenter)) {
            return this.mRecycledPoolSize.get(presenter).intValue();
        }
        return 24;
    }

    public final void setHoverCardPresenterSelector(PresenterSelector presenterSelector) {
        this.mHoverCardPresenterSelector = presenterSelector;
    }

    public final PresenterSelector getHoverCardPresenterSelector() {
        return this.mHoverCardPresenterSelector;
    }

    /* access modifiers changed from: package-private */
    public void selectChildView(ViewHolder viewHolder, View view, boolean z) {
        if (view == null) {
            if (this.mHoverCardPresenterSelector != null) {
                viewHolder.mHoverCardViewSwitcher.unselect();
            }
            if (z && viewHolder.getOnItemViewSelectedListener() != null) {
                viewHolder.getOnItemViewSelectedListener().onItemSelected((Presenter.ViewHolder) null, (Object) null, viewHolder, viewHolder.mRow);
            }
        } else if (viewHolder.mSelected) {
            ItemBridgeAdapter.ViewHolder viewHolder2 = (ItemBridgeAdapter.ViewHolder) viewHolder.mGridView.getChildViewHolder(view);
            if (this.mHoverCardPresenterSelector != null) {
                viewHolder.mHoverCardViewSwitcher.select(viewHolder.mGridView, view, viewHolder2.mItem);
            }
            if (z && viewHolder.getOnItemViewSelectedListener() != null) {
                viewHolder.getOnItemViewSelectedListener().onItemSelected(viewHolder2.mHolder, viewHolder2.mItem, viewHolder, viewHolder.mRow);
            }
        }
    }

    private static void initStatics(Context context) {
        if (sSelectedRowTopPadding == 0) {
            sSelectedRowTopPadding = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_browse_selected_row_top_padding);
            sExpandedSelectedRowTopPadding = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_browse_expanded_selected_row_top_padding);
            sExpandedRowNoHovercardBottomPadding = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_browse_expanded_row_no_hovercard_bottom_padding);
        }
    }

    private int getSpaceUnderBaseline(ViewHolder viewHolder) {
        RowHeaderPresenter.ViewHolder headerViewHolder = viewHolder.getHeaderViewHolder();
        if (headerViewHolder == null) {
            return 0;
        }
        if (getHeaderPresenter() != null) {
            return getHeaderPresenter().getSpaceUnderBaseline(headerViewHolder);
        }
        return headerViewHolder.view.getPaddingBottom();
    }

    private void setVerticalPadding(ViewHolder viewHolder) {
        int i;
        int i2;
        if (viewHolder.isExpanded()) {
            i2 = (viewHolder.isSelected() ? sExpandedSelectedRowTopPadding : viewHolder.mPaddingTop) - getSpaceUnderBaseline(viewHolder);
            i = this.mHoverCardPresenterSelector == null ? sExpandedRowNoHovercardBottomPadding : viewHolder.mPaddingBottom;
        } else if (viewHolder.isSelected()) {
            i2 = sSelectedRowTopPadding - viewHolder.mPaddingBottom;
            i = sSelectedRowTopPadding;
        } else {
            i = viewHolder.mPaddingBottom;
            i2 = 0;
        }
        viewHolder.getGridView().setPadding(viewHolder.mPaddingLeft, i2, viewHolder.mPaddingRight, i);
    }

    /* access modifiers changed from: protected */
    public RowPresenter.ViewHolder createRowViewHolder(ViewGroup viewGroup) {
        initStatics(viewGroup.getContext());
        ListRowView listRowView = new ListRowView(viewGroup.getContext());
        setupFadingEffect(listRowView);
        if (this.mRowHeight != 0) {
            listRowView.getGridView().setRowHeight(this.mRowHeight);
        }
        return new ViewHolder(listRowView, listRowView.getGridView(), this);
    }

    /* access modifiers changed from: protected */
    public void dispatchItemSelectedListener(RowPresenter.ViewHolder viewHolder, boolean z) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        ItemBridgeAdapter.ViewHolder viewHolder3 = (ItemBridgeAdapter.ViewHolder) viewHolder2.mGridView.findViewHolderForPosition(viewHolder2.mGridView.getSelectedPosition());
        if (viewHolder3 == null) {
            super.dispatchItemSelectedListener(viewHolder, z);
        } else if (z && viewHolder.getOnItemViewSelectedListener() != null) {
            viewHolder.getOnItemViewSelectedListener().onItemSelected(viewHolder3.getViewHolder(), viewHolder3.mItem, viewHolder2, viewHolder2.getRow());
        }
    }

    /* access modifiers changed from: protected */
    public void onRowViewSelected(RowPresenter.ViewHolder viewHolder, boolean z) {
        super.onRowViewSelected(viewHolder, z);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        setVerticalPadding(viewHolder2);
        updateFooterViewSwitcher(viewHolder2);
    }

    private void updateFooterViewSwitcher(ViewHolder viewHolder) {
        View view;
        if (viewHolder.mExpanded && viewHolder.mSelected) {
            if (this.mHoverCardPresenterSelector != null) {
                viewHolder.mHoverCardViewSwitcher.init((ViewGroup) viewHolder.view, this.mHoverCardPresenterSelector);
            }
            ItemBridgeAdapter.ViewHolder viewHolder2 = (ItemBridgeAdapter.ViewHolder) viewHolder.mGridView.findViewHolderForPosition(viewHolder.mGridView.getSelectedPosition());
            if (viewHolder2 == null) {
                view = null;
            } else {
                view = viewHolder2.itemView;
            }
            selectChildView(viewHolder, view, false);
        } else if (this.mHoverCardPresenterSelector != null) {
            viewHolder.mHoverCardViewSwitcher.unselect();
        }
    }

    private void setupFadingEffect(ListRowView listRowView) {
        HorizontalGridView gridView = listRowView.getGridView();
        if (this.mBrowseRowsFadingEdgeLength < 0) {
            TypedArray obtainStyledAttributes = gridView.getContext().obtainStyledAttributes(C0742R.styleable.LeanbackTheme);
            this.mBrowseRowsFadingEdgeLength = (int) obtainStyledAttributes.getDimension(C0742R.styleable.LeanbackTheme_browseRowsFadingEdgeLength, 0.0f);
            obtainStyledAttributes.recycle();
        }
        gridView.setFadingLeftEdgeLength(this.mBrowseRowsFadingEdgeLength);
    }

    /* access modifiers changed from: protected */
    public void onRowViewExpanded(RowPresenter.ViewHolder viewHolder, boolean z) {
        super.onRowViewExpanded(viewHolder, z);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        if (getRowHeight() != getExpandedRowHeight()) {
            viewHolder2.getGridView().setRowHeight(z ? getExpandedRowHeight() : getRowHeight());
        }
        setVerticalPadding(viewHolder2);
        updateFooterViewSwitcher(viewHolder2);
    }

    /* access modifiers changed from: protected */
    public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
        super.onBindRowViewHolder(viewHolder, obj);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        ListRow listRow = (ListRow) obj;
        viewHolder2.mItemBridgeAdapter.setAdapter(listRow.getAdapter());
        viewHolder2.mGridView.setAdapter(viewHolder2.mItemBridgeAdapter);
        viewHolder2.mGridView.setContentDescription(listRow.getContentDescription());
    }

    /* access modifiers changed from: protected */
    public void onUnbindRowViewHolder(RowPresenter.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        viewHolder2.mGridView.setAdapter((RecyclerView.Adapter) null);
        viewHolder2.mItemBridgeAdapter.clear();
        super.onUnbindRowViewHolder(viewHolder);
    }

    public boolean isUsingDefaultShadow() {
        return ShadowOverlayHelper.supportsShadow();
    }

    public boolean isUsingZOrder(Context context) {
        return !Settings.getInstance(context).preferStaticShadows();
    }

    public boolean isUsingOutlineClipping(Context context) {
        return !Settings.getInstance(context).isOutlineClippingDisabled();
    }

    public final void setShadowEnabled(boolean z) {
        this.mShadowEnabled = z;
    }

    public final boolean getShadowEnabled() {
        return this.mShadowEnabled;
    }

    public final void enableChildRoundedCorners(boolean z) {
        this.mRoundedCornersEnabled = z;
    }

    public final boolean areChildRoundedCornersEnabled() {
        return this.mRoundedCornersEnabled;
    }

    /* access modifiers changed from: package-private */
    public final boolean needsDefaultShadow() {
        return isUsingDefaultShadow() && getShadowEnabled();
    }

    public final void setKeepChildForeground(boolean z) {
        this.mKeepChildForeground = z;
    }

    public final boolean isKeepChildForeground() {
        return this.mKeepChildForeground;
    }

    /* access modifiers changed from: protected */
    public ShadowOverlayHelper.Options createShadowOverlayOptions() {
        return ShadowOverlayHelper.Options.DEFAULT;
    }

    /* access modifiers changed from: protected */
    public void onSelectLevelChanged(RowPresenter.ViewHolder viewHolder) {
        super.onSelectLevelChanged(viewHolder);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        int childCount = viewHolder2.mGridView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            applySelectLevelToChild(viewHolder2, viewHolder2.mGridView.getChildAt(i));
        }
    }

    /* access modifiers changed from: protected */
    public void applySelectLevelToChild(ViewHolder viewHolder, View view) {
        ShadowOverlayHelper shadowOverlayHelper = this.mShadowOverlayHelper;
        if (shadowOverlayHelper != null && shadowOverlayHelper.needsOverlay()) {
            this.mShadowOverlayHelper.setOverlayColor(view, viewHolder.mColorDimmer.getPaint().getColor());
        }
    }

    public void freeze(RowPresenter.ViewHolder viewHolder, boolean z) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        viewHolder2.mGridView.setScrollEnabled(!z);
        viewHolder2.mGridView.setAnimateChildLayout(!z);
    }

    public void setEntranceTransitionState(RowPresenter.ViewHolder viewHolder, boolean z) {
        super.setEntranceTransitionState(viewHolder, z);
        ((ViewHolder) viewHolder).mGridView.setChildrenVisibility(z ? 0 : 4);
    }
}
