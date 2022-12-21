package androidx.leanback.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

@Deprecated
abstract class BaseRowFragment extends Fragment {
    private static final String CURRENT_SELECTED_POSITION = "currentSelectedPosition";
    private ObjectAdapter mAdapter;
    final ItemBridgeAdapter mBridgeAdapter = new ItemBridgeAdapter();
    LateSelectionObserver mLateSelectionObserver = new LateSelectionObserver();
    private boolean mPendingTransitionPrepare;
    private PresenterSelector mPresenterSelector;
    private final OnChildViewHolderSelectedListener mRowSelectedListener = new OnChildViewHolderSelectedListener() {
        public void onChildViewHolderSelected(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int i, int i2) {
            if (!BaseRowFragment.this.mLateSelectionObserver.mIsLateSelection) {
                BaseRowFragment.this.mSelectedPosition = i;
                BaseRowFragment.this.onRowSelected(recyclerView, viewHolder, i, i2);
            }
        }
    };
    int mSelectedPosition = -1;
    VerticalGridView mVerticalGridView;

    /* access modifiers changed from: package-private */
    public abstract int getLayoutResourceId();

    /* access modifiers changed from: package-private */
    public void onRowSelected(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int i, int i2) {
    }

    BaseRowFragment() {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(getLayoutResourceId(), viewGroup, false);
        this.mVerticalGridView = findGridViewFromRoot(inflate);
        if (this.mPendingTransitionPrepare) {
            this.mPendingTransitionPrepare = false;
            onTransitionPrepare();
        }
        return inflate;
    }

    /* access modifiers changed from: package-private */
    public VerticalGridView findGridViewFromRoot(View view) {
        return (VerticalGridView) view;
    }

    public void onViewCreated(View view, Bundle bundle) {
        if (bundle != null) {
            this.mSelectedPosition = bundle.getInt(CURRENT_SELECTED_POSITION, -1);
        }
        setAdapterAndSelection();
        this.mVerticalGridView.setOnChildViewHolderSelectedListener(this.mRowSelectedListener);
    }

    final class LateSelectionObserver extends RecyclerView.AdapterDataObserver {
        boolean mIsLateSelection = false;

        LateSelectionObserver() {
        }

        public void onChanged() {
            performLateSelection();
        }

        public void onItemRangeInserted(int i, int i2) {
            performLateSelection();
        }

        /* access modifiers changed from: package-private */
        public void startLateSelection() {
            this.mIsLateSelection = true;
            BaseRowFragment.this.mBridgeAdapter.registerAdapterDataObserver(this);
        }

        /* access modifiers changed from: package-private */
        public void performLateSelection() {
            clear();
            if (BaseRowFragment.this.mVerticalGridView != null) {
                BaseRowFragment.this.mVerticalGridView.setSelectedPosition(BaseRowFragment.this.mSelectedPosition);
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            if (this.mIsLateSelection) {
                this.mIsLateSelection = false;
                BaseRowFragment.this.mBridgeAdapter.unregisterAdapterDataObserver(this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAdapterAndSelection() {
        if (this.mAdapter != null) {
            RecyclerView.Adapter adapter = this.mVerticalGridView.getAdapter();
            ItemBridgeAdapter itemBridgeAdapter = this.mBridgeAdapter;
            if (adapter != itemBridgeAdapter) {
                this.mVerticalGridView.setAdapter(itemBridgeAdapter);
            }
            if (this.mBridgeAdapter.getItemCount() == 0 && this.mSelectedPosition >= 0) {
                this.mLateSelectionObserver.startLateSelection();
                return;
            }
            int i = this.mSelectedPosition;
            if (i >= 0) {
                this.mVerticalGridView.setSelectedPosition(i);
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mLateSelectionObserver.clear();
        VerticalGridView verticalGridView = this.mVerticalGridView;
        if (verticalGridView != null) {
            verticalGridView.swapAdapter((RecyclerView.Adapter) null, true);
            this.mVerticalGridView = null;
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(CURRENT_SELECTED_POSITION, this.mSelectedPosition);
    }

    public final void setPresenterSelector(PresenterSelector presenterSelector) {
        if (this.mPresenterSelector != presenterSelector) {
            this.mPresenterSelector = presenterSelector;
            updateAdapter();
        }
    }

    public final PresenterSelector getPresenterSelector() {
        return this.mPresenterSelector;
    }

    public final void setAdapter(ObjectAdapter objectAdapter) {
        if (this.mAdapter != objectAdapter) {
            this.mAdapter = objectAdapter;
            updateAdapter();
        }
    }

    public final ObjectAdapter getAdapter() {
        return this.mAdapter;
    }

    public final ItemBridgeAdapter getBridgeAdapter() {
        return this.mBridgeAdapter;
    }

    public void setSelectedPosition(int i) {
        setSelectedPosition(i, true);
    }

    public int getSelectedPosition() {
        return this.mSelectedPosition;
    }

    public void setSelectedPosition(int i, boolean z) {
        if (this.mSelectedPosition != i) {
            this.mSelectedPosition = i;
            if (this.mVerticalGridView != null && !this.mLateSelectionObserver.mIsLateSelection) {
                if (z) {
                    this.mVerticalGridView.setSelectedPositionSmooth(i);
                } else {
                    this.mVerticalGridView.setSelectedPosition(i);
                }
            }
        }
    }

    public final VerticalGridView getVerticalGridView() {
        return this.mVerticalGridView;
    }

    /* access modifiers changed from: package-private */
    public void updateAdapter() {
        this.mBridgeAdapter.setAdapter(this.mAdapter);
        this.mBridgeAdapter.setPresenter(this.mPresenterSelector);
        if (this.mVerticalGridView != null) {
            setAdapterAndSelection();
        }
    }

    /* access modifiers changed from: package-private */
    public Object getItem(Row row, int i) {
        if (row instanceof ListRow) {
            return ((ListRow) row).getAdapter().get(i);
        }
        return null;
    }

    public boolean onTransitionPrepare() {
        VerticalGridView verticalGridView = this.mVerticalGridView;
        if (verticalGridView != null) {
            verticalGridView.setAnimateChildLayout(false);
            this.mVerticalGridView.setScrollEnabled(false);
            return true;
        }
        this.mPendingTransitionPrepare = true;
        return false;
    }

    public void onTransitionStart() {
        VerticalGridView verticalGridView = this.mVerticalGridView;
        if (verticalGridView != null) {
            verticalGridView.setPruneChild(false);
            this.mVerticalGridView.setLayoutFrozen(true);
            this.mVerticalGridView.setFocusSearchDisabled(true);
        }
    }

    public void onTransitionEnd() {
        VerticalGridView verticalGridView = this.mVerticalGridView;
        if (verticalGridView != null) {
            verticalGridView.setLayoutFrozen(false);
            this.mVerticalGridView.setAnimateChildLayout(true);
            this.mVerticalGridView.setPruneChild(true);
            this.mVerticalGridView.setFocusSearchDisabled(false);
            this.mVerticalGridView.setScrollEnabled(true);
        }
    }

    public void setAlignment(int i) {
        VerticalGridView verticalGridView = this.mVerticalGridView;
        if (verticalGridView != null) {
            verticalGridView.setItemAlignmentOffset(0);
            this.mVerticalGridView.setItemAlignmentOffsetPercent(-1.0f);
            this.mVerticalGridView.setWindowAlignmentOffset(i);
            this.mVerticalGridView.setWindowAlignmentOffsetPercent(-1.0f);
            this.mVerticalGridView.setWindowAlignment(0);
        }
    }
}
