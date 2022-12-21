package androidx.leanback.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.C0742R;
import androidx.leanback.system.Settings;
import androidx.leanback.transition.TransitionHelper;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.ShadowOverlayHelper;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalGridPresenter extends Presenter {
    private static final boolean DEBUG = false;
    private static final String TAG = "GridPresenter";
    private int mFocusZoomFactor;
    private boolean mKeepChildForeground;
    private int mNumColumns;
    private OnItemViewClickedListener mOnItemViewClickedListener;
    private OnItemViewSelectedListener mOnItemViewSelectedListener;
    private boolean mRoundedCornersEnabled;
    private boolean mShadowEnabled;
    ShadowOverlayHelper mShadowOverlayHelper;
    private ItemBridgeAdapter.Wrapper mShadowOverlayWrapper;
    private boolean mUseFocusDimmer;

    class VerticalGridItemBridgeAdapter extends ItemBridgeAdapter {
        VerticalGridItemBridgeAdapter() {
        }

        /* access modifiers changed from: protected */
        public void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                TransitionHelper.setTransitionGroup((ViewGroup) viewHolder.itemView, true);
            }
            if (VerticalGridPresenter.this.mShadowOverlayHelper != null) {
                VerticalGridPresenter.this.mShadowOverlayHelper.onViewCreated(viewHolder.itemView);
            }
        }

        public void onBind(final ItemBridgeAdapter.ViewHolder viewHolder) {
            if (VerticalGridPresenter.this.getOnItemViewClickedListener() != null) {
                viewHolder.mHolder.view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (VerticalGridPresenter.this.getOnItemViewClickedListener() != null) {
                            VerticalGridPresenter.this.getOnItemViewClickedListener().onItemClicked(viewHolder.mHolder, viewHolder.mItem, (RowPresenter.ViewHolder) null, null);
                        }
                    }
                });
            }
        }

        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (VerticalGridPresenter.this.getOnItemViewClickedListener() != null) {
                viewHolder.mHolder.view.setOnClickListener((View.OnClickListener) null);
            }
        }

        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.setActivated(true);
        }
    }

    public static class ViewHolder extends Presenter.ViewHolder {
        final VerticalGridView mGridView;
        boolean mInitialized;
        ItemBridgeAdapter mItemBridgeAdapter;

        public ViewHolder(VerticalGridView verticalGridView) {
            super(verticalGridView);
            this.mGridView = verticalGridView;
        }

        public VerticalGridView getGridView() {
            return this.mGridView;
        }
    }

    public VerticalGridPresenter() {
        this(3);
    }

    public VerticalGridPresenter(int i) {
        this(i, true);
    }

    public VerticalGridPresenter(int i, boolean z) {
        this.mNumColumns = -1;
        this.mShadowEnabled = true;
        this.mKeepChildForeground = true;
        this.mRoundedCornersEnabled = true;
        this.mFocusZoomFactor = i;
        this.mUseFocusDimmer = z;
    }

    public void setNumberOfColumns(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Invalid number of columns");
        } else if (this.mNumColumns != i) {
            this.mNumColumns = i;
        }
    }

    public int getNumberOfColumns() {
        return this.mNumColumns;
    }

    public final void setShadowEnabled(boolean z) {
        this.mShadowEnabled = z;
    }

    public final boolean getShadowEnabled() {
        return this.mShadowEnabled;
    }

    public boolean isUsingDefaultShadow() {
        return ShadowOverlayHelper.supportsShadow();
    }

    public final void enableChildRoundedCorners(boolean z) {
        this.mRoundedCornersEnabled = z;
    }

    public final boolean areChildRoundedCornersEnabled() {
        return this.mRoundedCornersEnabled;
    }

    public boolean isUsingZOrder(Context context) {
        return !Settings.getInstance(context).preferStaticShadows();
    }

    /* access modifiers changed from: package-private */
    public final boolean needsDefaultShadow() {
        return isUsingDefaultShadow() && getShadowEnabled();
    }

    public final int getFocusZoomFactor() {
        return this.mFocusZoomFactor;
    }

    public final boolean isFocusDimmerUsed() {
        return this.mUseFocusDimmer;
    }

    public final ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        ViewHolder createGridViewHolder = createGridViewHolder(viewGroup);
        createGridViewHolder.mInitialized = false;
        createGridViewHolder.mItemBridgeAdapter = new VerticalGridItemBridgeAdapter();
        initializeGridViewHolder(createGridViewHolder);
        if (createGridViewHolder.mInitialized) {
            return createGridViewHolder;
        }
        throw new RuntimeException("super.initializeGridViewHolder() must be called");
    }

    /* access modifiers changed from: protected */
    public ViewHolder createGridViewHolder(ViewGroup viewGroup) {
        return new ViewHolder((VerticalGridView) LayoutInflater.from(viewGroup.getContext()).inflate(C0742R.layout.lb_vertical_grid, viewGroup, false).findViewById(C0742R.C0745id.browse_grid));
    }

    /* access modifiers changed from: protected */
    public void initializeGridViewHolder(final ViewHolder viewHolder) {
        if (this.mNumColumns != -1) {
            viewHolder.getGridView().setNumColumns(this.mNumColumns);
            boolean z = true;
            viewHolder.mInitialized = true;
            Context context = viewHolder.mGridView.getContext();
            if (this.mShadowOverlayHelper == null) {
                ShadowOverlayHelper build = new ShadowOverlayHelper.Builder().needsOverlay(this.mUseFocusDimmer).needsShadow(needsDefaultShadow()).needsRoundedCorner(areChildRoundedCornersEnabled()).preferZOrder(isUsingZOrder(context)).keepForegroundDrawable(this.mKeepChildForeground).options(createShadowOverlayOptions()).build(context);
                this.mShadowOverlayHelper = build;
                if (build.needsWrapper()) {
                    this.mShadowOverlayWrapper = new ItemBridgeAdapterShadowOverlayWrapper(this.mShadowOverlayHelper);
                }
            }
            viewHolder.mItemBridgeAdapter.setWrapper(this.mShadowOverlayWrapper);
            this.mShadowOverlayHelper.prepareParentForShadow(viewHolder.mGridView);
            VerticalGridView gridView = viewHolder.getGridView();
            if (this.mShadowOverlayHelper.getShadowType() == 3) {
                z = false;
            }
            gridView.setFocusDrawingOrderEnabled(z);
            FocusHighlightHelper.setupBrowseItemFocusHighlight(viewHolder.mItemBridgeAdapter, this.mFocusZoomFactor, this.mUseFocusDimmer);
            viewHolder.getGridView().setOnChildSelectedListener(new OnChildSelectedListener() {
                public void onChildSelected(ViewGroup viewGroup, View view, int i, long j) {
                    VerticalGridPresenter.this.selectChildView(viewHolder, view);
                }
            });
            return;
        }
        throw new IllegalStateException("Number of columns must be set");
    }

    public final void setKeepChildForeground(boolean z) {
        this.mKeepChildForeground = z;
    }

    public final boolean getKeepChildForeground() {
        return this.mKeepChildForeground;
    }

    /* access modifiers changed from: protected */
    public ShadowOverlayHelper.Options createShadowOverlayOptions() {
        return ShadowOverlayHelper.Options.DEFAULT;
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        viewHolder2.mItemBridgeAdapter.setAdapter((ObjectAdapter) obj);
        viewHolder2.getGridView().setAdapter(viewHolder2.mItemBridgeAdapter);
    }

    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        viewHolder2.mItemBridgeAdapter.setAdapter((ObjectAdapter) null);
        viewHolder2.getGridView().setAdapter((RecyclerView.Adapter) null);
    }

    public final void setOnItemViewSelectedListener(OnItemViewSelectedListener onItemViewSelectedListener) {
        this.mOnItemViewSelectedListener = onItemViewSelectedListener;
    }

    public final OnItemViewSelectedListener getOnItemViewSelectedListener() {
        return this.mOnItemViewSelectedListener;
    }

    public final void setOnItemViewClickedListener(OnItemViewClickedListener onItemViewClickedListener) {
        this.mOnItemViewClickedListener = onItemViewClickedListener;
    }

    public final OnItemViewClickedListener getOnItemViewClickedListener() {
        return this.mOnItemViewClickedListener;
    }

    /* access modifiers changed from: package-private */
    public void selectChildView(ViewHolder viewHolder, View view) {
        ItemBridgeAdapter.ViewHolder viewHolder2;
        if (getOnItemViewSelectedListener() != null) {
            if (view == null) {
                viewHolder2 = null;
            } else {
                viewHolder2 = (ItemBridgeAdapter.ViewHolder) viewHolder.getGridView().getChildViewHolder(view);
            }
            if (viewHolder2 == null) {
                getOnItemViewSelectedListener().onItemSelected((Presenter.ViewHolder) null, (Object) null, (RowPresenter.ViewHolder) null, null);
            } else {
                getOnItemViewSelectedListener().onItemSelected(viewHolder2.mHolder, viewHolder2.mItem, (RowPresenter.ViewHolder) null, null);
            }
        }
    }

    public void setEntranceTransitionState(ViewHolder viewHolder, boolean z) {
        viewHolder.mGridView.setChildrenVisibility(z ? 0 : 4);
    }
}
