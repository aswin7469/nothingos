package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$dimen;
import androidx.leanback.R$id;
import androidx.leanback.R$layout;
import androidx.leanback.R$styleable;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class Picker extends FrameLayout {
    private int mAlphaAnimDuration;
    private final OnChildViewHolderSelectedListener mColumnChangeListener;
    final List<VerticalGridView> mColumnViews;
    ArrayList<PickerColumn> mColumns;
    private Interpolator mDecelerateInterpolator;
    private float mFocusedAlpha;
    private float mInvisibleColumnAlpha;
    private ArrayList<PickerValueListener> mListeners;
    private int mPickerItemLayoutId;
    private int mPickerItemTextViewId;
    private ViewGroup mPickerView;
    private int mSelectedColumn;
    private List<CharSequence> mSeparators;
    private float mUnfocusedAlpha;
    private float mVisibleColumnAlpha;
    private float mVisibleItems;
    private float mVisibleItemsActivated;

    /* loaded from: classes.dex */
    public interface PickerValueListener {
        void onValueChanged(Picker picker, int column);
    }

    public float getVisibleItemCount() {
        return 1.0f;
    }

    public final void setSeparator(CharSequence separator) {
        setSeparators(Arrays.asList(separator));
    }

    public final void setSeparators(List<CharSequence> separators) {
        this.mSeparators.clear();
        this.mSeparators.addAll(separators);
    }

    public final int getPickerItemLayoutId() {
        return this.mPickerItemLayoutId;
    }

    public final int getPickerItemTextViewId() {
        return this.mPickerItemTextViewId;
    }

    public Picker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.pickerStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public Picker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mColumnViews = new ArrayList();
        this.mVisibleItemsActivated = 3.0f;
        this.mVisibleItems = 1.0f;
        this.mSelectedColumn = 0;
        this.mSeparators = new ArrayList();
        this.mColumnChangeListener = new OnChildViewHolderSelectedListener() { // from class: androidx.leanback.widget.picker.Picker.1
            @Override // androidx.leanback.widget.OnChildViewHolderSelectedListener
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                int indexOf = Picker.this.mColumnViews.indexOf((VerticalGridView) parent);
                Picker.this.updateColumnAlpha(indexOf, true);
                if (child != null) {
                    Picker.this.onColumnValueChanged(indexOf, Picker.this.mColumns.get(indexOf).getMinValue() + position);
                }
            }
        };
        int[] iArr = R$styleable.lbPicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr, defStyleAttr, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, defStyleAttr, 0);
        this.mPickerItemLayoutId = obtainStyledAttributes.getResourceId(R$styleable.lbPicker_pickerItemLayout, R$layout.lb_picker_item);
        this.mPickerItemTextViewId = obtainStyledAttributes.getResourceId(R$styleable.lbPicker_pickerItemTextViewId, 0);
        obtainStyledAttributes.recycle();
        setEnabled(true);
        setDescendantFocusability(262144);
        this.mFocusedAlpha = 1.0f;
        this.mUnfocusedAlpha = 1.0f;
        this.mVisibleColumnAlpha = 0.5f;
        this.mInvisibleColumnAlpha = 0.0f;
        this.mAlphaAnimDuration = 200;
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.5f);
        this.mPickerView = (ViewGroup) ((ViewGroup) LayoutInflater.from(getContext()).inflate(R$layout.lb_picker, (ViewGroup) this, true)).findViewById(R$id.picker);
    }

    public PickerColumn getColumnAt(int colIndex) {
        ArrayList<PickerColumn> arrayList = this.mColumns;
        if (arrayList == null) {
            return null;
        }
        return arrayList.get(colIndex);
    }

    public int getColumnsCount() {
        ArrayList<PickerColumn> arrayList = this.mColumns;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public void setColumns(List<PickerColumn> columns) {
        if (this.mSeparators.size() == 0) {
            throw new IllegalStateException("Separators size is: " + this.mSeparators.size() + ". At least one separator must be provided");
        }
        if (this.mSeparators.size() == 1) {
            CharSequence charSequence = this.mSeparators.get(0);
            this.mSeparators.clear();
            this.mSeparators.add("");
            for (int i = 0; i < columns.size() - 1; i++) {
                this.mSeparators.add(charSequence);
            }
            this.mSeparators.add("");
        } else if (this.mSeparators.size() != columns.size() + 1) {
            throw new IllegalStateException("Separators size: " + this.mSeparators.size() + " mustequal the size of columns: " + columns.size() + " + 1");
        }
        this.mColumnViews.clear();
        this.mPickerView.removeAllViews();
        ArrayList<PickerColumn> arrayList = new ArrayList<>(columns);
        this.mColumns = arrayList;
        if (this.mSelectedColumn > arrayList.size() - 1) {
            this.mSelectedColumn = this.mColumns.size() - 1;
        }
        LayoutInflater from = LayoutInflater.from(getContext());
        int columnsCount = getColumnsCount();
        if (!TextUtils.isEmpty(this.mSeparators.get(0))) {
            TextView textView = (TextView) from.inflate(R$layout.lb_picker_separator, this.mPickerView, false);
            textView.setText(this.mSeparators.get(0));
            this.mPickerView.addView(textView);
        }
        int i2 = 0;
        while (i2 < columnsCount) {
            VerticalGridView verticalGridView = (VerticalGridView) from.inflate(R$layout.lb_picker_column, this.mPickerView, false);
            updateColumnSize(verticalGridView);
            verticalGridView.setWindowAlignment(0);
            verticalGridView.setHasFixedSize(false);
            verticalGridView.setFocusable(isActivated());
            verticalGridView.setItemViewCacheSize(0);
            this.mColumnViews.add(verticalGridView);
            this.mPickerView.addView(verticalGridView);
            int i3 = i2 + 1;
            if (!TextUtils.isEmpty(this.mSeparators.get(i3))) {
                TextView textView2 = (TextView) from.inflate(R$layout.lb_picker_separator, this.mPickerView, false);
                textView2.setText(this.mSeparators.get(i3));
                this.mPickerView.addView(textView2);
            }
            verticalGridView.setAdapter(new PickerScrollArrayAdapter(getPickerItemLayoutId(), getPickerItemTextViewId(), i2));
            verticalGridView.setOnChildViewHolderSelectedListener(this.mColumnChangeListener);
            i2 = i3;
        }
    }

    public void setColumnAt(int columnIndex, PickerColumn column) {
        this.mColumns.set(columnIndex, column);
        VerticalGridView verticalGridView = this.mColumnViews.get(columnIndex);
        PickerScrollArrayAdapter pickerScrollArrayAdapter = (PickerScrollArrayAdapter) verticalGridView.getAdapter();
        if (pickerScrollArrayAdapter != null) {
            pickerScrollArrayAdapter.notifyDataSetChanged();
        }
        verticalGridView.setSelectedPosition(column.getCurrentValue() - column.getMinValue());
    }

    public void setColumnValue(int columnIndex, int value, boolean runAnimation) {
        PickerColumn pickerColumn = this.mColumns.get(columnIndex);
        if (pickerColumn.getCurrentValue() != value) {
            pickerColumn.setCurrentValue(value);
            notifyValueChanged(columnIndex);
            VerticalGridView verticalGridView = this.mColumnViews.get(columnIndex);
            if (verticalGridView == null) {
                return;
            }
            int minValue = value - this.mColumns.get(columnIndex).getMinValue();
            if (runAnimation) {
                verticalGridView.setSelectedPositionSmooth(minValue);
            } else {
                verticalGridView.setSelectedPosition(minValue);
            }
        }
    }

    private void notifyValueChanged(int columnIndex) {
        ArrayList<PickerValueListener> arrayList = this.mListeners;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mListeners.get(size).onValueChanged(this, columnIndex);
            }
        }
    }

    void updateColumnAlpha(int colIndex, boolean animate) {
        VerticalGridView verticalGridView = this.mColumnViews.get(colIndex);
        int selectedPosition = verticalGridView.getSelectedPosition();
        int i = 0;
        while (i < verticalGridView.getAdapter().getItemCount()) {
            View findViewByPosition = verticalGridView.getLayoutManager().findViewByPosition(i);
            if (findViewByPosition != null) {
                setOrAnimateAlpha(findViewByPosition, selectedPosition == i, colIndex, animate);
            }
            i++;
        }
    }

    void setOrAnimateAlpha(View view, boolean selected, int colIndex, boolean animate) {
        boolean z = colIndex == this.mSelectedColumn || !hasFocus();
        if (selected) {
            if (z) {
                setOrAnimateAlpha(view, animate, this.mFocusedAlpha, -1.0f, this.mDecelerateInterpolator);
            } else {
                setOrAnimateAlpha(view, animate, this.mUnfocusedAlpha, -1.0f, this.mDecelerateInterpolator);
            }
        } else if (z) {
            setOrAnimateAlpha(view, animate, this.mVisibleColumnAlpha, -1.0f, this.mDecelerateInterpolator);
        } else {
            setOrAnimateAlpha(view, animate, this.mInvisibleColumnAlpha, -1.0f, this.mDecelerateInterpolator);
        }
    }

    private void setOrAnimateAlpha(View view, boolean animate, float destAlpha, float startAlpha, Interpolator interpolator) {
        view.animate().cancel();
        if (!animate) {
            view.setAlpha(destAlpha);
            return;
        }
        if (startAlpha >= 0.0f) {
            view.setAlpha(startAlpha);
        }
        view.animate().alpha(destAlpha).setDuration(this.mAlphaAnimDuration).setInterpolator(interpolator).start();
    }

    public void onColumnValueChanged(int columnIndex, int newValue) {
        PickerColumn pickerColumn = this.mColumns.get(columnIndex);
        if (pickerColumn.getCurrentValue() != newValue) {
            pickerColumn.setCurrentValue(newValue);
            notifyValueChanged(columnIndex);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        ViewHolder(View v, TextView textView) {
            super(v);
            this.textView = textView;
        }
    }

    /* loaded from: classes.dex */
    class PickerScrollArrayAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final int mColIndex;
        private PickerColumn mData;
        private final int mResource;
        private final int mTextViewResourceId;

        PickerScrollArrayAdapter(int resource, int textViewResourceId, int colIndex) {
            this.mResource = resource;
            this.mColIndex = colIndex;
            this.mTextViewResourceId = textViewResourceId;
            this.mData = Picker.this.mColumns.get(colIndex);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public ViewHolder mo1838onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView;
            View inflate = LayoutInflater.from(parent.getContext()).inflate(this.mResource, parent, false);
            int i = this.mTextViewResourceId;
            if (i != 0) {
                textView = (TextView) inflate.findViewById(i);
            } else {
                textView = (TextView) inflate;
            }
            return new ViewHolder(inflate, textView);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(ViewHolder holder, int position) {
            PickerColumn pickerColumn;
            TextView textView = holder.textView;
            if (textView != null && (pickerColumn = this.mData) != null) {
                textView.setText(pickerColumn.getLabelFor(pickerColumn.getMinValue() + position));
            }
            Picker picker = Picker.this;
            picker.setOrAnimateAlpha(holder.itemView, picker.mColumnViews.get(this.mColIndex).getSelectedPosition() == position, this.mColIndex, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(ViewHolder holder) {
            holder.itemView.setFocusable(Picker.this.isActivated());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            PickerColumn pickerColumn = this.mData;
            if (pickerColumn == null) {
                return 0;
            }
            return pickerColumn.getCount();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isActivated()) {
            int keyCode = event.getKeyCode();
            if (keyCode == 23 || keyCode == 66) {
                if (event.getAction() == 1) {
                    performClick();
                }
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int selectedColumn = getSelectedColumn();
        if (selectedColumn < 0 || selectedColumn >= this.mColumnViews.size()) {
            return false;
        }
        return this.mColumnViews.get(selectedColumn).requestFocus(direction, previouslyFocusedRect);
    }

    protected int getPickerItemHeightPixels() {
        return getContext().getResources().getDimensionPixelSize(R$dimen.picker_item_height);
    }

    private void updateColumnSize() {
        for (int i = 0; i < getColumnsCount(); i++) {
            updateColumnSize(this.mColumnViews.get(i));
        }
    }

    private void updateColumnSize(VerticalGridView columnView) {
        ViewGroup.LayoutParams layoutParams = columnView.getLayoutParams();
        float activatedVisibleItemCount = isActivated() ? getActivatedVisibleItemCount() : getVisibleItemCount();
        layoutParams.height = (int) ((getPickerItemHeightPixels() * activatedVisibleItemCount) + (columnView.getVerticalSpacing() * (activatedVisibleItemCount - 1.0f)));
        columnView.setLayoutParams(layoutParams);
    }

    private void updateItemFocusable() {
        boolean isActivated = isActivated();
        for (int i = 0; i < getColumnsCount(); i++) {
            VerticalGridView verticalGridView = this.mColumnViews.get(i);
            for (int i2 = 0; i2 < verticalGridView.getChildCount(); i2++) {
                verticalGridView.getChildAt(i2).setFocusable(isActivated);
            }
        }
    }

    public float getActivatedVisibleItemCount() {
        return this.mVisibleItemsActivated;
    }

    @Override // android.view.View
    public void setActivated(boolean activated) {
        if (activated == isActivated()) {
            super.setActivated(activated);
            return;
        }
        super.setActivated(activated);
        boolean hasFocus = hasFocus();
        int selectedColumn = getSelectedColumn();
        setDescendantFocusability(131072);
        if (!activated && hasFocus && isFocusable()) {
            requestFocus();
        }
        for (int i = 0; i < getColumnsCount(); i++) {
            this.mColumnViews.get(i).setFocusable(activated);
        }
        updateColumnSize();
        updateItemFocusable();
        if (activated && hasFocus && selectedColumn >= 0) {
            this.mColumnViews.get(selectedColumn).requestFocus();
        }
        setDescendantFocusability(262144);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        for (int i = 0; i < this.mColumnViews.size(); i++) {
            if (this.mColumnViews.get(i).hasFocus()) {
                setSelectedColumn(i);
            }
        }
    }

    public void setSelectedColumn(int columnIndex) {
        if (this.mSelectedColumn != columnIndex) {
            this.mSelectedColumn = columnIndex;
            for (int i = 0; i < this.mColumnViews.size(); i++) {
                updateColumnAlpha(i, true);
            }
        }
        VerticalGridView verticalGridView = this.mColumnViews.get(columnIndex);
        if (!hasFocus() || verticalGridView.hasFocus()) {
            return;
        }
        verticalGridView.requestFocus();
    }

    public int getSelectedColumn() {
        return this.mSelectedColumn;
    }
}
