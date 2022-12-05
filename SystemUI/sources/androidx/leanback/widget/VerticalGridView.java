package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$styleable;
/* loaded from: classes.dex */
public class VerticalGridView extends BaseGridView {
    public VerticalGridView(Context context) {
        this(context, null);
    }

    public VerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLayoutManager.setOrientation(1);
        initAttributes(context, attrs);
    }

    @SuppressLint({"CustomViewStyleable"})
    protected void initAttributes(Context context, AttributeSet attrs) {
        initBaseGridViewAttributes(context, attrs);
        int[] iArr = R$styleable.lbVerticalGridView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, 0, 0);
        setColumnWidth(obtainStyledAttributes);
        setNumColumns(obtainStyledAttributes.getInt(R$styleable.lbVerticalGridView_numberOfColumns, 1));
        obtainStyledAttributes.recycle();
    }

    void setColumnWidth(TypedArray array) {
        int i = R$styleable.lbVerticalGridView_columnWidth;
        if (array.peekValue(i) != null) {
            setColumnWidth(array.getLayoutDimension(i, 0));
        }
    }

    public void setNumColumns(int numColumns) {
        this.mLayoutManager.setNumRows(numColumns);
        requestLayout();
    }

    public void setColumnWidth(int width) {
        this.mLayoutManager.setRowHeight(width);
        requestLayout();
    }
}
