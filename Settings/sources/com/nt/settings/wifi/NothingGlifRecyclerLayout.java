package com.nt.settings.wifi;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.setupdesign.GlifRecyclerLayout;
/* loaded from: classes2.dex */
public class NothingGlifRecyclerLayout extends GlifRecyclerLayout {
    @Override // com.google.android.setupcompat.PartnerCustomizationLayout
    protected boolean enablePartnerResourceLoading() {
        return false;
    }

    public NothingGlifRecyclerLayout(Context context) {
        super(context);
    }

    public NothingGlifRecyclerLayout(Context context, int i) {
        super(context, i);
    }

    public NothingGlifRecyclerLayout(Context context, int i, int i2) {
        super(context, i, i2);
    }

    public NothingGlifRecyclerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NothingGlifRecyclerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
