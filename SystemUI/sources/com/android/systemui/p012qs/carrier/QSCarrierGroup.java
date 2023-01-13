package com.android.systemui.p012qs.carrier;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.FontSizeUtils;

/* renamed from: com.android.systemui.qs.carrier.QSCarrierGroup */
public class QSCarrierGroup extends LinearLayout {
    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public QSCarrierGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public TextView getNoSimTextView() {
        return (TextView) findViewById(C1894R.C1898id.no_carrier_text);
    }

    /* access modifiers changed from: package-private */
    public QSCarrier getCarrier1View() {
        return (QSCarrier) findViewById(C1894R.C1898id.carrier1);
    }

    /* access modifiers changed from: package-private */
    public QSCarrier getCarrier2View() {
        return (QSCarrier) findViewById(C1894R.C1898id.carrier2);
    }

    /* access modifiers changed from: package-private */
    public QSCarrier getCarrier3View() {
        return (QSCarrier) findViewById(C1894R.C1898id.carrier3);
    }

    /* access modifiers changed from: package-private */
    public View getCarrierDivider1() {
        return findViewById(C1894R.C1898id.qs_carrier_divider1);
    }

    /* access modifiers changed from: package-private */
    public View getCarrierDivider2() {
        return findViewById(C1894R.C1898id.qs_carrier_divider2);
    }

    public void updateTextAppearance(int i) {
        FontSizeUtils.updateFontSizeFromStyle(getNoSimTextView(), i);
        getCarrier1View().updateTextAppearance(i);
        getCarrier2View().updateTextAppearance(i);
        getCarrier3View().updateTextAppearance(i);
    }
}
