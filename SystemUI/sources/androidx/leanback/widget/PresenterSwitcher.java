package androidx.leanback.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.widget.Presenter;

public abstract class PresenterSwitcher {
    private Presenter mCurrentPresenter;
    private Presenter.ViewHolder mCurrentViewHolder;
    private ViewGroup mParent;
    private PresenterSelector mPresenterSelector;

    /* access modifiers changed from: protected */
    public abstract void insertView(View view);

    /* access modifiers changed from: protected */
    public void onViewSelected(View view) {
    }

    public void init(ViewGroup viewGroup, PresenterSelector presenterSelector) {
        clear();
        this.mParent = viewGroup;
        this.mPresenterSelector = presenterSelector;
    }

    public void select(Object obj) {
        switchView(obj);
        showView(true);
    }

    public void unselect() {
        showView(false);
    }

    public final ViewGroup getParentViewGroup() {
        return this.mParent;
    }

    private void showView(boolean z) {
        Presenter.ViewHolder viewHolder = this.mCurrentViewHolder;
        if (viewHolder != null) {
            showView(viewHolder.view, z);
        }
    }

    private void switchView(Object obj) {
        Presenter presenter = this.mPresenterSelector.getPresenter(obj);
        Presenter presenter2 = this.mCurrentPresenter;
        if (presenter != presenter2) {
            showView(false);
            clear();
            this.mCurrentPresenter = presenter;
            if (presenter != null) {
                Presenter.ViewHolder onCreateViewHolder = presenter.onCreateViewHolder(this.mParent);
                this.mCurrentViewHolder = onCreateViewHolder;
                insertView(onCreateViewHolder.view);
            } else {
                return;
            }
        } else if (presenter2 != null) {
            presenter2.onUnbindViewHolder(this.mCurrentViewHolder);
        } else {
            return;
        }
        this.mCurrentPresenter.onBindViewHolder(this.mCurrentViewHolder, obj);
        onViewSelected(this.mCurrentViewHolder.view);
    }

    /* access modifiers changed from: protected */
    public void showView(View view, boolean z) {
        view.setVisibility(z ? 0 : 8);
    }

    public void clear() {
        Presenter presenter = this.mCurrentPresenter;
        if (presenter != null) {
            presenter.onUnbindViewHolder(this.mCurrentViewHolder);
            this.mParent.removeView(this.mCurrentViewHolder.view);
            this.mCurrentViewHolder = null;
            this.mCurrentPresenter = null;
        }
    }
}
