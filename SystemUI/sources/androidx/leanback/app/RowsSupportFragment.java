package androidx.leanback.app;

import android.animation.TimeAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.leanback.C0742R;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.BaseOnItemViewClickedListener;
import androidx.leanback.widget.BaseOnItemViewSelectedListener;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridView;
import androidx.leanback.widget.ViewHolderTask;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RowsSupportFragment extends BaseRowSupportFragment implements BrowseSupportFragment.MainFragmentRowsAdapterProvider, BrowseSupportFragment.MainFragmentAdapterProvider {
    static final int ALIGN_TOP_NOT_SET = Integer.MIN_VALUE;
    static final boolean DEBUG = false;
    static final String TAG = "RowsSupportFragment";
    boolean mAfterEntranceTransition = true;
    private int mAlignedTop = Integer.MIN_VALUE;
    private final ItemBridgeAdapter.AdapterListener mBridgeAdapterListener = new ItemBridgeAdapter.AdapterListener() {
        public void onAddPresenter(Presenter presenter, int i) {
            if (RowsSupportFragment.this.mExternalAdapterListener != null) {
                RowsSupportFragment.this.mExternalAdapterListener.onAddPresenter(presenter, i);
            }
        }

        public void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
            VerticalGridView verticalGridView = RowsSupportFragment.this.getVerticalGridView();
            if (verticalGridView != null) {
                verticalGridView.setClipChildren(false);
            }
            RowsSupportFragment.this.setupSharedViewPool(viewHolder);
            RowsSupportFragment.this.mViewsCreated = true;
            viewHolder.setExtraObject(new RowViewHolderExtra(viewHolder));
            RowsSupportFragment.setRowViewSelected(viewHolder, false, true);
            if (RowsSupportFragment.this.mExternalAdapterListener != null) {
                RowsSupportFragment.this.mExternalAdapterListener.onCreate(viewHolder);
            }
        }

        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            RowsSupportFragment.setRowViewExpanded(viewHolder, RowsSupportFragment.this.mExpand);
            RowPresenter rowPresenter = (RowPresenter) viewHolder.getPresenter();
            RowPresenter.ViewHolder rowViewHolder = rowPresenter.getRowViewHolder(viewHolder.getViewHolder());
            rowPresenter.setEntranceTransitionState(rowViewHolder, RowsSupportFragment.this.mAfterEntranceTransition);
            rowViewHolder.setOnItemViewSelectedListener(RowsSupportFragment.this.mOnItemViewSelectedListener);
            rowViewHolder.setOnItemViewClickedListener(RowsSupportFragment.this.mOnItemViewClickedListener);
            rowPresenter.freeze(rowViewHolder, RowsSupportFragment.this.mFreezeRows);
            if (RowsSupportFragment.this.mExternalAdapterListener != null) {
                RowsSupportFragment.this.mExternalAdapterListener.onAttachedToWindow(viewHolder);
            }
        }

        public void onDetachedFromWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (RowsSupportFragment.this.mSelectedViewHolder == viewHolder) {
                RowsSupportFragment.setRowViewSelected(RowsSupportFragment.this.mSelectedViewHolder, false, true);
                RowsSupportFragment.this.mSelectedViewHolder = null;
            }
            RowPresenter.ViewHolder rowViewHolder = ((RowPresenter) viewHolder.getPresenter()).getRowViewHolder(viewHolder.getViewHolder());
            rowViewHolder.setOnItemViewSelectedListener((BaseOnItemViewSelectedListener) null);
            rowViewHolder.setOnItemViewClickedListener((BaseOnItemViewClickedListener) null);
            if (RowsSupportFragment.this.mExternalAdapterListener != null) {
                RowsSupportFragment.this.mExternalAdapterListener.onDetachedFromWindow(viewHolder);
            }
        }

        public void onBind(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (RowsSupportFragment.this.mExternalAdapterListener != null) {
                RowsSupportFragment.this.mExternalAdapterListener.onBind(viewHolder);
            }
        }

        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
            RowsSupportFragment.setRowViewSelected(viewHolder, false, true);
            if (RowsSupportFragment.this.mExternalAdapterListener != null) {
                RowsSupportFragment.this.mExternalAdapterListener.onUnbind(viewHolder);
            }
        }
    };
    boolean mExpand = true;
    ItemBridgeAdapter.AdapterListener mExternalAdapterListener;
    boolean mFreezeRows;
    private MainFragmentAdapter mMainFragmentAdapter;
    private MainFragmentRowsAdapter mMainFragmentRowsAdapter;
    BaseOnItemViewClickedListener mOnItemViewClickedListener;
    BaseOnItemViewSelectedListener mOnItemViewSelectedListener;
    private ArrayList<Presenter> mPresenterMapper;
    private RecyclerView.RecycledViewPool mRecycledViewPool;
    ItemBridgeAdapter.ViewHolder mSelectedViewHolder;
    private int mSubPosition;
    boolean mViewsCreated;

    @Deprecated
    public void enableRowScaling(boolean z) {
    }

    public /* bridge */ /* synthetic */ int getSelectedPosition() {
        return super.getSelectedPosition();
    }

    public /* bridge */ /* synthetic */ View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public /* bridge */ /* synthetic */ void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public /* bridge */ /* synthetic */ void onTransitionStart() {
        super.onTransitionStart();
    }

    public /* bridge */ /* synthetic */ void setSelectedPosition(int i) {
        super.setSelectedPosition(i);
    }

    public /* bridge */ /* synthetic */ void setSelectedPosition(int i, boolean z) {
        super.setSelectedPosition(i, z);
    }

    public BrowseSupportFragment.MainFragmentAdapter getMainFragmentAdapter() {
        if (this.mMainFragmentAdapter == null) {
            this.mMainFragmentAdapter = new MainFragmentAdapter(this);
        }
        return this.mMainFragmentAdapter;
    }

    public BrowseSupportFragment.MainFragmentRowsAdapter getMainFragmentRowsAdapter() {
        if (this.mMainFragmentRowsAdapter == null) {
            this.mMainFragmentRowsAdapter = new MainFragmentRowsAdapter(this);
        }
        return this.mMainFragmentRowsAdapter;
    }

    static final class RowViewHolderExtra implements TimeAnimator.TimeListener {
        static final Interpolator sSelectAnimatorInterpolator = new DecelerateInterpolator(2.0f);
        final RowPresenter mRowPresenter;
        final Presenter.ViewHolder mRowViewHolder;
        final TimeAnimator mSelectAnimator;
        final int mSelectAnimatorDurationInUse;
        final Interpolator mSelectAnimatorInterpolatorInUse = sSelectAnimatorInterpolator;
        float mSelectLevelAnimDelta;
        float mSelectLevelAnimStart;

        RowViewHolderExtra(ItemBridgeAdapter.ViewHolder viewHolder) {
            TimeAnimator timeAnimator = new TimeAnimator();
            this.mSelectAnimator = timeAnimator;
            this.mRowPresenter = (RowPresenter) viewHolder.getPresenter();
            this.mRowViewHolder = viewHolder.getViewHolder();
            timeAnimator.setTimeListener(this);
            this.mSelectAnimatorDurationInUse = viewHolder.itemView.getResources().getInteger(C0742R.integer.lb_browse_rows_anim_duration);
        }

        public void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2) {
            if (this.mSelectAnimator.isRunning()) {
                updateSelect(j, j2);
            }
        }

        /* access modifiers changed from: package-private */
        public void updateSelect(long j, long j2) {
            float f;
            int i = this.mSelectAnimatorDurationInUse;
            if (j >= ((long) i)) {
                this.mSelectAnimator.end();
                f = 1.0f;
            } else {
                f = (float) (((double) j) / ((double) i));
            }
            Interpolator interpolator = this.mSelectAnimatorInterpolatorInUse;
            if (interpolator != null) {
                f = interpolator.getInterpolation(f);
            }
            this.mRowPresenter.setSelectLevel(this.mRowViewHolder, this.mSelectLevelAnimStart + (f * this.mSelectLevelAnimDelta));
        }

        /* access modifiers changed from: package-private */
        public void animateSelect(boolean z, boolean z2) {
            this.mSelectAnimator.end();
            float f = z ? 1.0f : 0.0f;
            if (z2) {
                this.mRowPresenter.setSelectLevel(this.mRowViewHolder, f);
            } else if (this.mRowPresenter.getSelectLevel(this.mRowViewHolder) != f) {
                float selectLevel = this.mRowPresenter.getSelectLevel(this.mRowViewHolder);
                this.mSelectLevelAnimStart = selectLevel;
                this.mSelectLevelAnimDelta = f - selectLevel;
                this.mSelectAnimator.start();
            }
        }
    }

    /* access modifiers changed from: protected */
    public VerticalGridView findGridViewFromRoot(View view) {
        return (VerticalGridView) view.findViewById(C0742R.C0745id.container_list);
    }

    public void setOnItemViewClickedListener(BaseOnItemViewClickedListener baseOnItemViewClickedListener) {
        this.mOnItemViewClickedListener = baseOnItemViewClickedListener;
        if (this.mViewsCreated) {
            throw new IllegalStateException("Item clicked listener must be set before views are created");
        }
    }

    public BaseOnItemViewClickedListener getOnItemViewClickedListener() {
        return this.mOnItemViewClickedListener;
    }

    public void setExpand(boolean z) {
        this.mExpand = z;
        VerticalGridView verticalGridView = getVerticalGridView();
        if (verticalGridView != null) {
            int childCount = verticalGridView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                setRowViewExpanded((ItemBridgeAdapter.ViewHolder) verticalGridView.getChildViewHolder(verticalGridView.getChildAt(i)), this.mExpand);
            }
        }
    }

    public void setOnItemViewSelectedListener(BaseOnItemViewSelectedListener baseOnItemViewSelectedListener) {
        this.mOnItemViewSelectedListener = baseOnItemViewSelectedListener;
        VerticalGridView verticalGridView = getVerticalGridView();
        if (verticalGridView != null) {
            int childCount = verticalGridView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                getRowViewHolder((ItemBridgeAdapter.ViewHolder) verticalGridView.getChildViewHolder(verticalGridView.getChildAt(i))).setOnItemViewSelectedListener(this.mOnItemViewSelectedListener);
            }
        }
    }

    public BaseOnItemViewSelectedListener getOnItemViewSelectedListener() {
        return this.mOnItemViewSelectedListener;
    }

    /* access modifiers changed from: package-private */
    public void onRowSelected(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int i, int i2) {
        ItemBridgeAdapter.ViewHolder viewHolder2 = this.mSelectedViewHolder;
        boolean z = true;
        if (!(viewHolder2 == viewHolder && this.mSubPosition == i2)) {
            this.mSubPosition = i2;
            if (viewHolder2 != null) {
                setRowViewSelected(viewHolder2, false, false);
            }
            ItemBridgeAdapter.ViewHolder viewHolder3 = (ItemBridgeAdapter.ViewHolder) viewHolder;
            this.mSelectedViewHolder = viewHolder3;
            if (viewHolder3 != null) {
                setRowViewSelected(viewHolder3, true, false);
            }
        }
        MainFragmentAdapter mainFragmentAdapter = this.mMainFragmentAdapter;
        if (mainFragmentAdapter != null) {
            BrowseSupportFragment.FragmentHost fragmentHost = mainFragmentAdapter.getFragmentHost();
            if (i > 0) {
                z = false;
            }
            fragmentHost.showTitleView(z);
        }
    }

    public RowPresenter.ViewHolder getRowViewHolder(int i) {
        VerticalGridView verticalGridView = getVerticalGridView();
        if (verticalGridView == null) {
            return null;
        }
        return getRowViewHolder((ItemBridgeAdapter.ViewHolder) verticalGridView.findViewHolderForAdapterPosition(i));
    }

    /* access modifiers changed from: package-private */
    public int getLayoutResourceId() {
        return C0742R.layout.lb_rows_fragment;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getVerticalGridView().setItemAlignmentViewId(C0742R.C0745id.row_content);
        getVerticalGridView().setSaveChildrenPolicy(2);
        setAlignment(this.mAlignedTop);
        this.mRecycledViewPool = null;
        this.mPresenterMapper = null;
        MainFragmentAdapter mainFragmentAdapter = this.mMainFragmentAdapter;
        if (mainFragmentAdapter != null) {
            mainFragmentAdapter.getFragmentHost().notifyViewCreated(this.mMainFragmentAdapter);
        }
    }

    public void onDestroyView() {
        this.mViewsCreated = false;
        this.mSelectedViewHolder = null;
        this.mRecycledViewPool = null;
        super.onDestroyView();
    }

    /* access modifiers changed from: package-private */
    public void setExternalAdapterListener(ItemBridgeAdapter.AdapterListener adapterListener) {
        this.mExternalAdapterListener = adapterListener;
    }

    static void setRowViewExpanded(ItemBridgeAdapter.ViewHolder viewHolder, boolean z) {
        ((RowPresenter) viewHolder.getPresenter()).setRowViewExpanded(viewHolder.getViewHolder(), z);
    }

    static void setRowViewSelected(ItemBridgeAdapter.ViewHolder viewHolder, boolean z, boolean z2) {
        ((RowViewHolderExtra) viewHolder.getExtraObject()).animateSelect(z, z2);
        ((RowPresenter) viewHolder.getPresenter()).setRowViewSelected(viewHolder.getViewHolder(), z);
    }

    /* access modifiers changed from: package-private */
    public void setupSharedViewPool(ItemBridgeAdapter.ViewHolder viewHolder) {
        RowPresenter.ViewHolder rowViewHolder = ((RowPresenter) viewHolder.getPresenter()).getRowViewHolder(viewHolder.getViewHolder());
        if (rowViewHolder instanceof ListRowPresenter.ViewHolder) {
            ListRowPresenter.ViewHolder viewHolder2 = (ListRowPresenter.ViewHolder) rowViewHolder;
            HorizontalGridView gridView = viewHolder2.getGridView();
            RecyclerView.RecycledViewPool recycledViewPool = this.mRecycledViewPool;
            if (recycledViewPool == null) {
                this.mRecycledViewPool = gridView.getRecycledViewPool();
            } else {
                gridView.setRecycledViewPool(recycledViewPool);
            }
            ItemBridgeAdapter bridgeAdapter = viewHolder2.getBridgeAdapter();
            ArrayList<Presenter> arrayList = this.mPresenterMapper;
            if (arrayList == null) {
                this.mPresenterMapper = bridgeAdapter.getPresenterMapper();
            } else {
                bridgeAdapter.setPresenterMapper(arrayList);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateAdapter() {
        super.updateAdapter();
        this.mSelectedViewHolder = null;
        this.mViewsCreated = false;
        ItemBridgeAdapter bridgeAdapter = getBridgeAdapter();
        if (bridgeAdapter != null) {
            bridgeAdapter.setAdapterListener(this.mBridgeAdapterListener);
        }
    }

    public boolean onTransitionPrepare() {
        boolean onTransitionPrepare = super.onTransitionPrepare();
        if (onTransitionPrepare) {
            freezeRows(true);
        }
        return onTransitionPrepare;
    }

    public void onTransitionEnd() {
        super.onTransitionEnd();
        freezeRows(false);
    }

    private void freezeRows(boolean z) {
        this.mFreezeRows = z;
        VerticalGridView verticalGridView = getVerticalGridView();
        if (verticalGridView != null) {
            int childCount = verticalGridView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ItemBridgeAdapter.ViewHolder viewHolder = (ItemBridgeAdapter.ViewHolder) verticalGridView.getChildViewHolder(verticalGridView.getChildAt(i));
                RowPresenter rowPresenter = (RowPresenter) viewHolder.getPresenter();
                rowPresenter.freeze(rowPresenter.getRowViewHolder(viewHolder.getViewHolder()), z);
            }
        }
    }

    public void setEntranceTransitionState(boolean z) {
        this.mAfterEntranceTransition = z;
        VerticalGridView verticalGridView = getVerticalGridView();
        if (verticalGridView != null) {
            int childCount = verticalGridView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ItemBridgeAdapter.ViewHolder viewHolder = (ItemBridgeAdapter.ViewHolder) verticalGridView.getChildViewHolder(verticalGridView.getChildAt(i));
                RowPresenter rowPresenter = (RowPresenter) viewHolder.getPresenter();
                rowPresenter.setEntranceTransitionState(rowPresenter.getRowViewHolder(viewHolder.getViewHolder()), this.mAfterEntranceTransition);
            }
        }
    }

    public void setSelectedPosition(int i, boolean z, final Presenter.ViewHolderTask viewHolderTask) {
        VerticalGridView verticalGridView = getVerticalGridView();
        if (verticalGridView != null) {
            C08832 r1 = viewHolderTask != null ? new ViewHolderTask() {
                public void run(final RecyclerView.ViewHolder viewHolder) {
                    viewHolder.itemView.post(new Runnable() {
                        public void run() {
                            viewHolderTask.run(RowsSupportFragment.getRowViewHolder((ItemBridgeAdapter.ViewHolder) viewHolder));
                        }
                    });
                }
            } : null;
            if (z) {
                verticalGridView.setSelectedPositionSmooth(i, r1);
            } else {
                verticalGridView.setSelectedPosition(i, (ViewHolderTask) r1);
            }
        }
    }

    static RowPresenter.ViewHolder getRowViewHolder(ItemBridgeAdapter.ViewHolder viewHolder) {
        if (viewHolder == null) {
            return null;
        }
        return ((RowPresenter) viewHolder.getPresenter()).getRowViewHolder(viewHolder.getViewHolder());
    }

    public boolean isScrolling() {
        if (getVerticalGridView() == null || getVerticalGridView().getScrollState() == 0) {
            return false;
        }
        return true;
    }

    public void setAlignment(int i) {
        if (i != Integer.MIN_VALUE) {
            this.mAlignedTop = i;
            VerticalGridView verticalGridView = getVerticalGridView();
            if (verticalGridView != null) {
                verticalGridView.setItemAlignmentOffset(0);
                verticalGridView.setItemAlignmentOffsetPercent(-1.0f);
                verticalGridView.setItemAlignmentOffsetWithPadding(true);
                verticalGridView.setWindowAlignmentOffset(this.mAlignedTop);
                verticalGridView.setWindowAlignmentOffsetPercent(-1.0f);
                verticalGridView.setWindowAlignment(0);
            }
        }
    }

    public RowPresenter.ViewHolder findRowViewHolderByPosition(int i) {
        if (this.mVerticalGridView == null) {
            return null;
        }
        return getRowViewHolder((ItemBridgeAdapter.ViewHolder) this.mVerticalGridView.findViewHolderForAdapterPosition(i));
    }

    public static class MainFragmentAdapter extends BrowseSupportFragment.MainFragmentAdapter<RowsSupportFragment> {
        public MainFragmentAdapter(RowsSupportFragment rowsSupportFragment) {
            super(rowsSupportFragment);
            setScalingEnabled(true);
        }

        public boolean isScrolling() {
            return ((RowsSupportFragment) getFragment()).isScrolling();
        }

        public void setExpand(boolean z) {
            ((RowsSupportFragment) getFragment()).setExpand(z);
        }

        public void setEntranceTransitionState(boolean z) {
            ((RowsSupportFragment) getFragment()).setEntranceTransitionState(z);
        }

        public void setAlignment(int i) {
            ((RowsSupportFragment) getFragment()).setAlignment(i);
        }

        public boolean onTransitionPrepare() {
            return ((RowsSupportFragment) getFragment()).onTransitionPrepare();
        }

        public void onTransitionStart() {
            ((RowsSupportFragment) getFragment()).onTransitionStart();
        }

        public void onTransitionEnd() {
            ((RowsSupportFragment) getFragment()).onTransitionEnd();
        }
    }

    public static class MainFragmentRowsAdapter extends BrowseSupportFragment.MainFragmentRowsAdapter<RowsSupportFragment> {
        public MainFragmentRowsAdapter(RowsSupportFragment rowsSupportFragment) {
            super(rowsSupportFragment);
        }

        public void setAdapter(ObjectAdapter objectAdapter) {
            ((RowsSupportFragment) getFragment()).setAdapter(objectAdapter);
        }

        public void setOnItemViewClickedListener(OnItemViewClickedListener onItemViewClickedListener) {
            ((RowsSupportFragment) getFragment()).setOnItemViewClickedListener(onItemViewClickedListener);
        }

        public void setOnItemViewSelectedListener(OnItemViewSelectedListener onItemViewSelectedListener) {
            ((RowsSupportFragment) getFragment()).setOnItemViewSelectedListener(onItemViewSelectedListener);
        }

        public void setSelectedPosition(int i, boolean z, Presenter.ViewHolderTask viewHolderTask) {
            ((RowsSupportFragment) getFragment()).setSelectedPosition(i, z, viewHolderTask);
        }

        public void setSelectedPosition(int i, boolean z) {
            ((RowsSupportFragment) getFragment()).setSelectedPosition(i, z);
        }

        public int getSelectedPosition() {
            return ((RowsSupportFragment) getFragment()).getSelectedPosition();
        }

        public RowPresenter.ViewHolder findRowViewHolderByPosition(int i) {
            return ((RowsSupportFragment) getFragment()).findRowViewHolderByPosition(i);
        }
    }
}
