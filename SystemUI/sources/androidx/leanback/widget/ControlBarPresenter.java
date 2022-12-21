package androidx.leanback.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.C0742R;
import androidx.leanback.widget.ControlBar;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.Presenter;

class ControlBarPresenter extends Presenter {
    static final int MAX_CONTROLS = 7;
    private static int sChildMarginDefault;
    private static int sControlIconWidth;
    boolean mDefaultFocusToMiddle = true;
    private int mLayoutResourceId;
    OnControlClickedListener mOnControlClickedListener;
    OnControlSelectedListener mOnControlSelectedListener;

    interface OnControlClickedListener {
        void onControlClicked(Presenter.ViewHolder viewHolder, Object obj, BoundData boundData);
    }

    interface OnControlSelectedListener {
        void onControlSelected(Presenter.ViewHolder viewHolder, Object obj, BoundData boundData);
    }

    static class BoundData {
        ObjectAdapter adapter;
        Presenter presenter;

        BoundData() {
        }
    }

    class ViewHolder extends Presenter.ViewHolder {
        ObjectAdapter mAdapter;
        ControlBar mControlBar;
        View mControlsContainer;
        BoundData mData;
        ObjectAdapter.DataObserver mDataObserver;
        Presenter mPresenter;
        SparseArray<Presenter.ViewHolder> mViewHolders = new SparseArray<>();

        ViewHolder(View view) {
            super(view);
            this.mControlsContainer = view.findViewById(C0742R.C0745id.controls_container);
            ControlBar controlBar = (ControlBar) view.findViewById(C0742R.C0745id.control_bar);
            this.mControlBar = controlBar;
            if (controlBar != null) {
                controlBar.setDefaultFocusToMiddle(ControlBarPresenter.this.mDefaultFocusToMiddle);
                this.mControlBar.setOnChildFocusedListener(new ControlBar.OnChildFocusedListener(ControlBarPresenter.this) {
                    public void onChildFocusedListener(View view, View view2) {
                        if (ControlBarPresenter.this.mOnControlSelectedListener != null) {
                            for (int i = 0; i < ViewHolder.this.mViewHolders.size(); i++) {
                                if (ViewHolder.this.mViewHolders.get(i).view == view) {
                                    ControlBarPresenter.this.mOnControlSelectedListener.onControlSelected(ViewHolder.this.mViewHolders.get(i), ViewHolder.this.getDisplayedAdapter().get(i), ViewHolder.this.mData);
                                    return;
                                }
                            }
                        }
                    }
                });
                this.mDataObserver = new ObjectAdapter.DataObserver(ControlBarPresenter.this) {
                    public void onChanged() {
                        if (ViewHolder.this.mAdapter == ViewHolder.this.getDisplayedAdapter()) {
                            ViewHolder viewHolder = ViewHolder.this;
                            viewHolder.showControls(viewHolder.mPresenter);
                        }
                    }

                    public void onItemRangeChanged(int i, int i2) {
                        if (ViewHolder.this.mAdapter == ViewHolder.this.getDisplayedAdapter()) {
                            for (int i3 = 0; i3 < i2; i3++) {
                                ViewHolder viewHolder = ViewHolder.this;
                                viewHolder.bindControlToAction(i + i3, viewHolder.mPresenter);
                            }
                        }
                    }
                };
                return;
            }
            throw new IllegalStateException("Couldn't find control_bar");
        }

        /* access modifiers changed from: package-private */
        public int getChildMarginFromCenter(Context context, int i) {
            return ControlBarPresenter.this.getChildMarginDefault(context) + ControlBarPresenter.this.getControlIconWidth(context);
        }

        /* access modifiers changed from: package-private */
        public void showControls(Presenter presenter) {
            int i;
            ObjectAdapter displayedAdapter = getDisplayedAdapter();
            int i2 = 0;
            if (displayedAdapter == null) {
                i = 0;
            } else {
                i = displayedAdapter.size();
            }
            View focusedChild = this.mControlBar.getFocusedChild();
            if (focusedChild != null && i > 0 && this.mControlBar.indexOfChild(focusedChild) >= i) {
                this.mControlBar.getChildAt(displayedAdapter.size() - 1).requestFocus();
            }
            for (int childCount = this.mControlBar.getChildCount() - 1; childCount >= i; childCount--) {
                this.mControlBar.removeViewAt(childCount);
            }
            while (i2 < i && i2 < 7) {
                bindControlToAction(i2, displayedAdapter, presenter);
                i2++;
            }
            ControlBar controlBar = this.mControlBar;
            controlBar.setChildMarginFromCenter(getChildMarginFromCenter(controlBar.getContext(), i));
        }

        /* access modifiers changed from: package-private */
        public void bindControlToAction(int i, Presenter presenter) {
            bindControlToAction(i, getDisplayedAdapter(), presenter);
        }

        private void bindControlToAction(final int i, ObjectAdapter objectAdapter, Presenter presenter) {
            final Presenter.ViewHolder viewHolder = this.mViewHolders.get(i);
            Object obj = objectAdapter.get(i);
            if (viewHolder == null) {
                viewHolder = presenter.onCreateViewHolder(this.mControlBar);
                this.mViewHolders.put(i, viewHolder);
                presenter.setOnClickListener(viewHolder, new View.OnClickListener() {
                    public void onClick(View view) {
                        Object obj = ViewHolder.this.getDisplayedAdapter().get(i);
                        if (ControlBarPresenter.this.mOnControlClickedListener != null) {
                            ControlBarPresenter.this.mOnControlClickedListener.onControlClicked(viewHolder, obj, ViewHolder.this.mData);
                        }
                    }
                });
            }
            if (viewHolder.view.getParent() == null) {
                this.mControlBar.addView(viewHolder.view);
            }
            presenter.onBindViewHolder(viewHolder, obj);
        }

        /* access modifiers changed from: package-private */
        public ObjectAdapter getDisplayedAdapter() {
            return this.mAdapter;
        }
    }

    public ControlBarPresenter(int i) {
        this.mLayoutResourceId = i;
    }

    public int getLayoutResourceId() {
        return this.mLayoutResourceId;
    }

    public void setOnControlClickedListener(OnControlClickedListener onControlClickedListener) {
        this.mOnControlClickedListener = onControlClickedListener;
    }

    public OnControlClickedListener getOnItemViewClickedListener() {
        return this.mOnControlClickedListener;
    }

    public void setOnControlSelectedListener(OnControlSelectedListener onControlSelectedListener) {
        this.mOnControlSelectedListener = onControlSelectedListener;
    }

    public OnControlSelectedListener getOnItemControlListener() {
        return this.mOnControlSelectedListener;
    }

    public void setBackgroundColor(ViewHolder viewHolder, int i) {
        viewHolder.mControlsContainer.setBackgroundColor(i);
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResourceId(), viewGroup, false));
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        BoundData boundData = (BoundData) obj;
        if (viewHolder2.mAdapter != boundData.adapter) {
            viewHolder2.mAdapter = boundData.adapter;
            if (viewHolder2.mAdapter != null) {
                viewHolder2.mAdapter.registerObserver(viewHolder2.mDataObserver);
            }
        }
        viewHolder2.mPresenter = boundData.presenter;
        viewHolder2.mData = boundData;
        viewHolder2.showControls(viewHolder2.mPresenter);
    }

    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        if (viewHolder2.mAdapter != null) {
            viewHolder2.mAdapter.unregisterObserver(viewHolder2.mDataObserver);
            viewHolder2.mAdapter = null;
        }
        viewHolder2.mData = null;
    }

    /* access modifiers changed from: package-private */
    public int getChildMarginDefault(Context context) {
        if (sChildMarginDefault == 0) {
            sChildMarginDefault = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_playback_controls_child_margin_default);
        }
        return sChildMarginDefault;
    }

    /* access modifiers changed from: package-private */
    public int getControlIconWidth(Context context) {
        if (sControlIconWidth == 0) {
            sControlIconWidth = context.getResources().getDimensionPixelSize(C0742R.dimen.lb_control_icon_width);
        }
        return sControlIconWidth;
    }

    /* access modifiers changed from: package-private */
    public void setDefaultFocusToMiddle(boolean z) {
        this.mDefaultFocusToMiddle = z;
    }
}
