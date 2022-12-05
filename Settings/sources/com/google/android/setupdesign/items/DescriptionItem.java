package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.google.android.setupdesign.R$id;
import com.google.android.setupdesign.util.DescriptionStyler;
@Deprecated
/* loaded from: classes2.dex */
public class DescriptionItem extends Item {
    private boolean partnerDescriptionHeavyStyle = false;
    private boolean partnerDescriptionLightStyle = false;

    public DescriptionItem() {
    }

    public DescriptionItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean shouldApplyPartnerDescriptionHeavyStyle() {
        return this.partnerDescriptionHeavyStyle;
    }

    public boolean shouldApplyPartnerDescriptionLightStyle() {
        return this.partnerDescriptionLightStyle;
    }

    @Override // com.google.android.setupdesign.items.Item, com.google.android.setupdesign.items.IItem
    public void onBindView(View view) {
        super.onBindView(view);
        TextView textView = (TextView) view.findViewById(R$id.sud_items_title);
        if (shouldApplyPartnerDescriptionHeavyStyle()) {
            DescriptionStyler.applyPartnerCustomizationHeavyStyle(textView);
        } else if (!shouldApplyPartnerDescriptionLightStyle()) {
        } else {
            DescriptionStyler.applyPartnerCustomizationLightStyle(textView);
        }
    }
}
