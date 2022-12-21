package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.util.HeaderAreaStyler;
import com.google.android.setupdesign.util.PartnerStyleHelper;

public class DescriptionMixin implements Mixin {
    private static final String TAG = "DescriptionMixin";
    private final TemplateLayout templateLayout;

    public DescriptionMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        TypedArray obtainStyledAttributes = templateLayout2.getContext().obtainStyledAttributes(attributeSet, C3953R.styleable.SudDescriptionMixin, i, 0);
        CharSequence text = obtainStyledAttributes.getText(C3953R.styleable.SudDescriptionMixin_sudDescriptionText);
        if (text != null) {
            setText(text);
        }
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(C3953R.styleable.SudDescriptionMixin_sudDescriptionTextColor);
        if (colorStateList != null) {
            setTextColor(colorStateList);
        }
        obtainStyledAttributes.recycle();
    }

    public void tryApplyPartnerCustomizationStyle() {
        TextView textView = (TextView) this.templateLayout.findManagedViewById(C3953R.C3956id.sud_layout_subtitle);
        if (textView != null && PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
            HeaderAreaStyler.applyPartnerCustomizationDescriptionHeavyStyle(textView);
        }
    }

    public TextView getTextView() {
        return (TextView) this.templateLayout.findManagedViewById(C3953R.C3956id.sud_layout_subtitle);
    }

    public void setText(int i) {
        TextView textView = getTextView();
        if (textView == null || i == 0) {
            Log.w(TAG, "Fail to set text due to either invalid resource id or text view not found.");
            return;
        }
        textView.setText(i);
        setVisibility(0);
    }

    public void setText(CharSequence charSequence) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setText(charSequence);
            setVisibility(0);
        }
    }

    public CharSequence getText() {
        TextView textView = getTextView();
        if (textView != null) {
            return textView.getText();
        }
        return null;
    }

    public void setVisibility(int i) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setVisibility(i);
        }
    }

    public void setTextColor(ColorStateList colorStateList) {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public ColorStateList getTextColor() {
        TextView textView = getTextView();
        if (textView != null) {
            return textView.getTextColors();
        }
        return null;
    }

    public void adjustLegacyDescriptionItem() {
        TextView textView = getTextView();
        Context context = textView.getContext();
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin + ((int) context.getResources().getDimension(C3953R.dimen.sud_description_margin_top_extra)), marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin + ((int) context.getResources().getDimension(C3953R.dimen.sud_description_margin_bottom_extra)));
        }
    }
}
