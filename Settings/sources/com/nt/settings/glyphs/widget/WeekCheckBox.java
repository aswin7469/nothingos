package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Space;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class WeekCheckBox extends LinearLayout {
    private OnCheckChangeListener listener;
    private String[] weeks;

    /* loaded from: classes2.dex */
    public interface OnCheckChangeListener {
        void onCheckChange(View view, int i, boolean z);
    }

    public WeekCheckBox(Context context) {
        this(context, null);
    }

    public WeekCheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WeekCheckBox(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public WeekCheckBox(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.weeks = new String[]{"M", "T", "W", "T", "F", "S", "S"};
        init(context, attributeSet, i, i2);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        Resources resources = context.getResources();
        int i3 = R.dimen.nt_preference_horizontal_margin;
        setPadding(resources.getDimensionPixelOffset(i3), context.getResources().getDimensionPixelOffset(i3), context.getResources().getDimensionPixelOffset(i3), context.getResources().getDimensionPixelOffset(i3));
        removeAllViews();
        for (int i4 = 0; i4 < this.weeks.length; i4++) {
            if (i4 != 0) {
                addView(createSpace(context));
            }
            addView(createItem(context, this.weeks[i4], i4 + 1000));
        }
    }

    private Space createSpace(Context context) {
        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        return space;
    }

    private CheckBox createItem(final Context context, String str, final int i) {
        final CheckBox checkBox = new CheckBox(context);
        checkBox.setBackground(context.getResources().getDrawable(R.drawable.nt_week_check_selector));
        checkBox.setTextColor(context.getResources().getColorStateList(R.color.nt_week_check_text_selector));
        checkBox.setText(str);
        checkBox.setId(i);
        checkBox.setTextSize(0, context.getResources().getDimensionPixelSize(R.dimen.glyphs_bedtime_checkbox_title_size));
        checkBox.setTypeface(Typeface.create("NDot57", 0));
        checkBox.setGravity(17);
        checkBox.setButtonDrawable((Drawable) null);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(dp2Px(context, 40), dp2Px(context, 40)));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.glyphs.widget.WeekCheckBox.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (WeekCheckBox.this.listener == null) {
                    return;
                }
                WeekCheckBox.this.listener.onCheckChange(checkBox, i - 1000, z);
                checkBox.setBackground(context.getResources().getDrawable(z ? R.drawable.nt_week_check_pre : R.drawable.nt_week_check_nor));
            }
        });
        return checkBox;
    }

    private int dp2Px(Context context, int i) {
        return (int) ((i * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void setSelectStatus(int[] iArr) {
        if (iArr == null) {
            return;
        }
        for (int i = 0; i < iArr.length; i++) {
            ((CheckBox) findViewById(i + 1000)).setChecked(iArr[i] > 0);
        }
    }

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.listener = onCheckChangeListener;
    }
}
