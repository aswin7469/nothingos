package com.android.keyguard;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.C1894R;
import java.util.Locale;

public class CarrierText extends TextView {
    private final boolean mShowAirplaneMode;
    private final boolean mShowMissingSim;

    public CarrierText(Context context) {
        this(context, (AttributeSet) null);
    }

    /* JADX INFO: finally extract failed */
    public CarrierText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C1894R.styleable.CarrierText, 0, 0);
        try {
            boolean z = obtainStyledAttributes.getBoolean(0, false);
            this.mShowAirplaneMode = obtainStyledAttributes.getBoolean(1, false);
            this.mShowMissingSim = obtainStyledAttributes.getBoolean(2, false);
            obtainStyledAttributes.recycle();
            setTransformationMethod(new CarrierTextTransformationMethod(this.mContext, z));
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
        } else {
            setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    public boolean getShowAirplaneMode() {
        return this.mShowAirplaneMode;
    }

    public boolean getShowMissingSim() {
        return this.mShowMissingSim;
    }

    private static class CarrierTextTransformationMethod extends SingleLineTransformationMethod {
        private final boolean mAllCaps;
        private final Locale mLocale;

        public CarrierTextTransformationMethod(Context context, boolean z) {
            this.mLocale = context.getResources().getConfiguration().locale;
            this.mAllCaps = z;
        }

        public CharSequence getTransformation(CharSequence charSequence, View view) {
            CharSequence transformation = super.getTransformation(charSequence, view);
            return (!this.mAllCaps || transformation == null) ? transformation : transformation.toString().toUpperCase(this.mLocale);
        }
    }
}
