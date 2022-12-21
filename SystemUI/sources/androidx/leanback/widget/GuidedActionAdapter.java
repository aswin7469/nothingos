package androidx.leanback.widget;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;
import androidx.leanback.widget.GuidedActionAutofillSupport;
import androidx.leanback.widget.GuidedActionsStylist;
import androidx.leanback.widget.ImeKeyMonitor;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GuidedActionAdapter extends RecyclerView.Adapter {
    static final boolean DEBUG = false;
    static final boolean DEBUG_EDIT = false;
    static final String TAG = "GuidedActionAdapter";
    static final String TAG_EDIT = "EditableAction";
    private final ActionAutofillListener mActionAutofillListener;
    private final ActionEditListener mActionEditListener;
    private final ActionOnFocusListener mActionOnFocusListener;
    private final ActionOnKeyListener mActionOnKeyListener;
    final List<GuidedAction> mActions;
    private ClickListener mClickListener;
    DiffCallback<GuidedAction> mDiffCallback;
    GuidedActionAdapterGroup mGroup;
    private final boolean mIsSubAdapter;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (view != null && view.getWindowToken() != null && GuidedActionAdapter.this.mRecyclerView.isAttachedToWindow()) {
                GuidedActionsStylist.ViewHolder viewHolder = (GuidedActionsStylist.ViewHolder) GuidedActionAdapter.this.mRecyclerView.getChildViewHolder(view);
                GuidedAction action = viewHolder.getAction();
                if (action.hasTextEditable()) {
                    GuidedActionAdapter.this.mGroup.openIme(GuidedActionAdapter.this, viewHolder);
                } else if (action.hasEditableActivatorView()) {
                    GuidedActionAdapter.this.performOnActionClick(viewHolder);
                } else {
                    GuidedActionAdapter.this.handleCheckedActions(viewHolder);
                    if (action.isEnabled() && !action.infoOnly()) {
                        GuidedActionAdapter.this.performOnActionClick(viewHolder);
                    }
                }
            }
        }
    };
    final RecyclerView mRecyclerView;
    final GuidedActionsStylist mStylist;

    public interface ClickListener {
        void onGuidedActionClicked(GuidedAction guidedAction);
    }

    public interface EditListener {
        void onGuidedActionEditCanceled(GuidedAction guidedAction);

        long onGuidedActionEditedAndProceed(GuidedAction guidedAction);

        void onImeClose();

        void onImeOpen();
    }

    public interface FocusListener {
        void onGuidedActionFocused(GuidedAction guidedAction);
    }

    public GuidedActionAdapter(List<GuidedAction> list, ClickListener clickListener, FocusListener focusListener, GuidedActionsStylist guidedActionsStylist, boolean z) {
        ArrayList arrayList;
        VerticalGridView verticalGridView;
        if (list == null) {
            arrayList = new ArrayList();
        } else {
            arrayList = new ArrayList(list);
        }
        this.mActions = arrayList;
        this.mClickListener = clickListener;
        this.mStylist = guidedActionsStylist;
        this.mActionOnKeyListener = new ActionOnKeyListener();
        this.mActionOnFocusListener = new ActionOnFocusListener(focusListener);
        this.mActionEditListener = new ActionEditListener();
        this.mActionAutofillListener = new ActionAutofillListener();
        this.mIsSubAdapter = z;
        if (!z) {
            this.mDiffCallback = GuidedActionDiffCallback.getInstance();
        }
        if (z) {
            verticalGridView = guidedActionsStylist.getSubActionsGridView();
        } else {
            verticalGridView = guidedActionsStylist.getActionsGridView();
        }
        this.mRecyclerView = verticalGridView;
    }

    public void setDiffCallback(DiffCallback<GuidedAction> diffCallback) {
        this.mDiffCallback = diffCallback;
    }

    public void setActions(List<GuidedAction> list) {
        if (!this.mIsSubAdapter) {
            this.mStylist.collapseAction(false);
        }
        this.mActionOnFocusListener.unFocus();
        if (this.mDiffCallback != null) {
            final ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.mActions);
            this.mActions.clear();
            this.mActions.addAll(list);
            DiffUtil.calculateDiff(new DiffUtil.Callback() {
                public int getOldListSize() {
                    return arrayList.size();
                }

                public int getNewListSize() {
                    return GuidedActionAdapter.this.mActions.size();
                }

                public boolean areItemsTheSame(int i, int i2) {
                    return GuidedActionAdapter.this.mDiffCallback.areItemsTheSame((GuidedAction) arrayList.get(i), GuidedActionAdapter.this.mActions.get(i2));
                }

                public boolean areContentsTheSame(int i, int i2) {
                    return GuidedActionAdapter.this.mDiffCallback.areContentsTheSame((GuidedAction) arrayList.get(i), GuidedActionAdapter.this.mActions.get(i2));
                }

                public Object getChangePayload(int i, int i2) {
                    return GuidedActionAdapter.this.mDiffCallback.getChangePayload((GuidedAction) arrayList.get(i), GuidedActionAdapter.this.mActions.get(i2));
                }
            }).dispatchUpdatesTo((RecyclerView.Adapter) this);
            return;
        }
        this.mActions.clear();
        this.mActions.addAll(list);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mActions.size();
    }

    public GuidedAction getItem(int i) {
        return this.mActions.get(i);
    }

    public int indexOf(GuidedAction guidedAction) {
        return this.mActions.indexOf(guidedAction);
    }

    public GuidedActionsStylist getGuidedActionsStylist() {
        return this.mStylist;
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setFocusListener(FocusListener focusListener) {
        this.mActionOnFocusListener.setFocusListener(focusListener);
    }

    public List<GuidedAction> getActions() {
        return new ArrayList(this.mActions);
    }

    public int getItemViewType(int i) {
        return this.mStylist.getItemViewType(this.mActions.get(i));
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        GuidedActionsStylist.ViewHolder onCreateViewHolder = this.mStylist.onCreateViewHolder(viewGroup, i);
        View view = onCreateViewHolder.itemView;
        view.setOnKeyListener(this.mActionOnKeyListener);
        view.setOnClickListener(this.mOnClickListener);
        view.setOnFocusChangeListener(this.mActionOnFocusListener);
        setupListeners(onCreateViewHolder.getEditableTitleView());
        setupListeners(onCreateViewHolder.getEditableDescriptionView());
        return onCreateViewHolder;
    }

    private void setupListeners(EditText editText) {
        if (editText != null) {
            editText.setPrivateImeOptions("escapeNorth");
            editText.setOnEditorActionListener(this.mActionEditListener);
            if (editText instanceof ImeKeyMonitor) {
                ((ImeKeyMonitor) editText).setImeKeyListener(this.mActionEditListener);
            }
            if (editText instanceof GuidedActionAutofillSupport) {
                ((GuidedActionAutofillSupport) editText).setOnAutofillListener(this.mActionAutofillListener);
            }
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (i < this.mActions.size()) {
            this.mStylist.onBindViewHolder((GuidedActionsStylist.ViewHolder) viewHolder, this.mActions.get(i));
        }
    }

    public int getItemCount() {
        return this.mActions.size();
    }

    private class ActionOnFocusListener implements View.OnFocusChangeListener {
        private FocusListener mFocusListener;
        private View mSelectedView;

        ActionOnFocusListener(FocusListener focusListener) {
            this.mFocusListener = focusListener;
        }

        public void setFocusListener(FocusListener focusListener) {
            this.mFocusListener = focusListener;
        }

        public void unFocus() {
            if (this.mSelectedView != null && GuidedActionAdapter.this.mRecyclerView.isAttachedToWindow()) {
                RecyclerView.ViewHolder childViewHolder = GuidedActionAdapter.this.mRecyclerView.getChildViewHolder(this.mSelectedView);
                if (childViewHolder != null) {
                    GuidedActionAdapter.this.mStylist.onAnimateItemFocused((GuidedActionsStylist.ViewHolder) childViewHolder, false);
                    return;
                }
                Log.w(GuidedActionAdapter.TAG, "RecyclerView returned null view holder", new Throwable());
            }
        }

        public void onFocusChange(View view, boolean z) {
            if (GuidedActionAdapter.this.mRecyclerView.isAttachedToWindow()) {
                GuidedActionsStylist.ViewHolder viewHolder = (GuidedActionsStylist.ViewHolder) GuidedActionAdapter.this.mRecyclerView.getChildViewHolder(view);
                if (z) {
                    this.mSelectedView = view;
                    FocusListener focusListener = this.mFocusListener;
                    if (focusListener != null) {
                        focusListener.onGuidedActionFocused(viewHolder.getAction());
                    }
                } else if (this.mSelectedView == view) {
                    GuidedActionAdapter.this.mStylist.onAnimateItemPressedCancelled(viewHolder);
                    this.mSelectedView = null;
                }
                GuidedActionAdapter.this.mStylist.onAnimateItemFocused(viewHolder, z);
            }
        }
    }

    public GuidedActionsStylist.ViewHolder findSubChildViewHolder(View view) {
        if (!this.mRecyclerView.isAttachedToWindow()) {
            return null;
        }
        ViewParent parent = view.getParent();
        while (true) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (parent != recyclerView && parent != null && view != null) {
                view = (View) parent;
                parent = parent.getParent();
            } else if (parent == null && view != null) {
                return (GuidedActionsStylist.ViewHolder) recyclerView.getChildViewHolder(view);
            }
        }
        return parent == null ? null : null;
    }

    public void handleCheckedActions(GuidedActionsStylist.ViewHolder viewHolder) {
        GuidedAction action = viewHolder.getAction();
        int checkSetId = action.getCheckSetId();
        if (this.mRecyclerView.isAttachedToWindow() && checkSetId != 0) {
            if (checkSetId != -1) {
                int size = this.mActions.size();
                for (int i = 0; i < size; i++) {
                    GuidedAction guidedAction = this.mActions.get(i);
                    if (guidedAction != action && guidedAction.getCheckSetId() == checkSetId && guidedAction.isChecked()) {
                        guidedAction.setChecked(false);
                        GuidedActionsStylist.ViewHolder viewHolder2 = (GuidedActionsStylist.ViewHolder) this.mRecyclerView.findViewHolderForPosition(i);
                        if (viewHolder2 != null) {
                            this.mStylist.onAnimateItemChecked(viewHolder2, false);
                        }
                    }
                }
            }
            if (!action.isChecked()) {
                action.setChecked(true);
                this.mStylist.onAnimateItemChecked(viewHolder, true);
            } else if (checkSetId == -1) {
                action.setChecked(false);
                this.mStylist.onAnimateItemChecked(viewHolder, false);
            }
        }
    }

    public void performOnActionClick(GuidedActionsStylist.ViewHolder viewHolder) {
        ClickListener clickListener = this.mClickListener;
        if (clickListener != null) {
            clickListener.onGuidedActionClicked(viewHolder.getAction());
        }
    }

    private class ActionOnKeyListener implements View.OnKeyListener {
        private boolean mKeyPressed = false;

        ActionOnKeyListener() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (view == null || keyEvent == null || !GuidedActionAdapter.this.mRecyclerView.isAttachedToWindow()) {
                return false;
            }
            if (i == 23 || i == 66 || i == 160 || i == 99 || i == 100) {
                GuidedActionsStylist.ViewHolder viewHolder = (GuidedActionsStylist.ViewHolder) GuidedActionAdapter.this.mRecyclerView.getChildViewHolder(view);
                GuidedAction action = viewHolder.getAction();
                if (!action.isEnabled() || action.infoOnly()) {
                    keyEvent.getAction();
                    return true;
                }
                int action2 = keyEvent.getAction();
                if (action2 != 0) {
                    if (action2 == 1 && this.mKeyPressed) {
                        this.mKeyPressed = false;
                        GuidedActionAdapter.this.mStylist.onAnimateItemPressed(viewHolder, this.mKeyPressed);
                    }
                } else if (!this.mKeyPressed) {
                    this.mKeyPressed = true;
                    GuidedActionAdapter.this.mStylist.onAnimateItemPressed(viewHolder, this.mKeyPressed);
                }
            }
            return false;
        }
    }

    private class ActionEditListener implements TextView.OnEditorActionListener, ImeKeyMonitor.ImeKeyListener {
        ActionEditListener() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5 || i == 6) {
                GuidedActionAdapter.this.mGroup.fillAndGoNext(GuidedActionAdapter.this, textView);
                return true;
            } else if (i != 1) {
                return false;
            } else {
                GuidedActionAdapter.this.mGroup.fillAndStay(GuidedActionAdapter.this, textView);
                return true;
            }
        }

        public boolean onKeyPreIme(EditText editText, int i, KeyEvent keyEvent) {
            if (i == 4 && keyEvent.getAction() == 1) {
                GuidedActionAdapter.this.mGroup.fillAndStay(GuidedActionAdapter.this, editText);
                return true;
            } else if (i != 66 || keyEvent.getAction() != 1) {
                return false;
            } else {
                GuidedActionAdapter.this.mGroup.fillAndGoNext(GuidedActionAdapter.this, editText);
                return true;
            }
        }
    }

    private class ActionAutofillListener implements GuidedActionAutofillSupport.OnAutofillListener {
        ActionAutofillListener() {
        }

        public void onAutofill(View view) {
            GuidedActionAdapter.this.mGroup.fillAndGoNext(GuidedActionAdapter.this, (EditText) view);
        }
    }
}
