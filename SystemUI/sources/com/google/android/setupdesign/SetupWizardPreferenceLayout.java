package com.google.android.setupdesign;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.setupdesign.template.RecyclerMixin;

public class SetupWizardPreferenceLayout extends SetupWizardRecyclerLayout {
    public SetupWizardPreferenceLayout(Context context) {
        super(context);
    }

    public SetupWizardPreferenceLayout(Context context, int i, int i2) {
        super(context, i, i2);
    }

    public SetupWizardPreferenceLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SetupWizardPreferenceLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = C3963R.C3966id.sud_layout_content;
        }
        return super.findContainer(i);
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.recyclerMixin.getRecyclerView();
    }

    /* access modifiers changed from: protected */
    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = C3963R.layout.sud_preference_template;
        }
        return super.onInflateTemplate(layoutInflater, i);
    }

    /* access modifiers changed from: protected */
    public void onTemplateInflated() {
        this.recyclerMixin = new RecyclerMixin(this, (RecyclerView) LayoutInflater.from(getContext()).inflate(C3963R.layout.sud_preference_recycler_view, this, false));
    }
}
