package com.google.android.setupdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import com.google.android.setupdesign.items.ItemAdapter;
@Deprecated
/* loaded from: classes2.dex */
public class SetupWizardItemsLayout extends SetupWizardListLayout {
    public SetupWizardItemsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SetupWizardItemsLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.google.android.setupdesign.SetupWizardListLayout
    /* renamed from: getAdapter  reason: collision with other method in class */
    public ItemAdapter mo735getAdapter() {
        ListAdapter mo735getAdapter = super.mo735getAdapter();
        if (mo735getAdapter instanceof ItemAdapter) {
            return (ItemAdapter) mo735getAdapter;
        }
        return null;
    }
}
