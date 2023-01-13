package com.android.p019wm.shell.compatui.letterboxedu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogActionLayout */
class LetterboxEduDialogActionLayout extends FrameLayout {
    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public LetterboxEduDialogActionLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C3353R.styleable.LetterboxEduDialogActionLayout, i, i2);
        int resourceId = obtainStyledAttributes.getResourceId(C3353R.styleable.LetterboxEduDialogActionLayout_icon, 0);
        String string = obtainStyledAttributes.getString(C3353R.styleable.LetterboxEduDialogActionLayout_text);
        obtainStyledAttributes.recycle();
        View inflate = inflate(getContext(), C3353R.layout.letterbox_education_dialog_action_layout, this);
        ((ImageView) inflate.findViewById(C3353R.C3356id.letterbox_education_dialog_action_icon)).setImageResource(resourceId);
        ((TextView) inflate.findViewById(C3353R.C3356id.letterbox_education_dialog_action_text)).setText(string);
    }
}
