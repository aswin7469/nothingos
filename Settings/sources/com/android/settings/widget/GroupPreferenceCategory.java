package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.PreferenceCategory;
/* loaded from: classes.dex */
public class GroupPreferenceCategory extends PreferenceCategory {
    private int mGroupId = -1;

    public GroupPreferenceCategory(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public GroupPreferenceCategory(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public GroupPreferenceCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public GroupPreferenceCategory(Context context) {
        super(context);
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public void setGroupId(int i) {
        this.mGroupId = i;
    }
}
