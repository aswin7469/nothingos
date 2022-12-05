package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.nt.settings.glyphs.widget.WeekCheckBox;
/* loaded from: classes2.dex */
public class WeekCheckBoxPreference extends Preference {
    private OnSelectedChangeListener listener;
    private int[] weekSelectedStatus;

    /* loaded from: classes2.dex */
    public interface OnSelectedChangeListener {
        void onSelectedChange(int[] iArr);
    }

    public WeekCheckBoxPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.weekSelectedStatus = new int[7];
        init(context);
    }

    public WeekCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.weekSelectedStatus = new int[7];
        init(context);
    }

    public WeekCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0, 0);
        this.weekSelectedStatus = new int[7];
        init(context);
    }

    public WeekCheckBoxPreference(Context context) {
        super(context);
        this.weekSelectedStatus = new int[7];
        init(context);
    }

    private void init(Context context) {
        setLayoutResource(R.layout.preference_week_check_box);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        WeekCheckBox weekCheckBox = (WeekCheckBox) preferenceViewHolder.findViewById(R.id.wcb_weeks);
        weekCheckBox.setSelectStatus(this.weekSelectedStatus);
        weekCheckBox.setOnCheckChangeListener(new WeekCheckBox.OnCheckChangeListener() { // from class: com.nt.settings.glyphs.widget.WeekCheckBoxPreference.1
            @Override // com.nt.settings.glyphs.widget.WeekCheckBox.OnCheckChangeListener
            public void onCheckChange(View view, int i, boolean z) {
                WeekCheckBoxPreference.this.weekSelectedStatus[i] = z ? 1 : 0;
                if (WeekCheckBoxPreference.this.listener == null) {
                    return;
                }
                WeekCheckBoxPreference.this.listener.onSelectedChange(WeekCheckBoxPreference.this.weekSelectedStatus);
            }
        });
    }

    public void setCheckedStatus(int[] iArr) {
        this.weekSelectedStatus = iArr;
        notifyChanged();
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.listener = onSelectedChangeListener;
    }
}
