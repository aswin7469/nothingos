package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$styleable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class PinPicker extends Picker {
    public PinPicker(Context context, AttributeSet attrs) {
        this(context, attrs, R$attr.pinPickerStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public PinPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int[] iArr = R$styleable.lbPinPicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr, defStyleAttr, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, defStyleAttr, 0);
        try {
            setSeparator(" ");
            setNumberOfColumns(obtainStyledAttributes.getInt(R$styleable.lbPinPicker_columnCount, 4));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public void setNumberOfColumns(int count) {
        ArrayList arrayList = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            PickerColumn pickerColumn = new PickerColumn();
            pickerColumn.setMinValue(0);
            pickerColumn.setMaxValue(9);
            pickerColumn.setLabelFormat("%d");
            arrayList.add(pickerColumn);
        }
        setColumns(arrayList);
    }

    @Override // android.view.View
    public boolean performClick() {
        int selectedColumn = getSelectedColumn();
        if (selectedColumn == getColumnsCount() - 1) {
            return super.performClick();
        }
        setSelectedColumn(selectedColumn + 1);
        return false;
    }

    @Override // androidx.leanback.widget.picker.Picker, android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getAction() == 1 && keyCode >= 7 && keyCode <= 16) {
            setColumnValue(getSelectedColumn(), keyCode - 7, false);
            performClick();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
