package com.android.p019wm.shell.pip.p020tv;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.pip.tv.TvPipMenuActionButton */
public class TvPipMenuActionButton extends RelativeLayout implements View.OnClickListener {
    private final View mButtonBackgroundView;
    private final View mButtonView;
    private final ImageView mIconImageView;
    private View.OnClickListener mOnClickListener;

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public TvPipMenuActionButton(Context context) {
        this(context, (AttributeSet) null, 0, 0);
    }

    public TvPipMenuActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public TvPipMenuActionButton(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public TvPipMenuActionButton(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C3353R.layout.tv_pip_menu_action_button, this);
        this.mIconImageView = (ImageView) findViewById(C3353R.C3356id.icon);
        this.mButtonView = findViewById(C3353R.C3356id.button);
        this.mButtonBackgroundView = findViewById(C3353R.C3356id.background);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{16843033, 16843087}, i, i2);
        setImageResource(obtainStyledAttributes.getResourceId(0, 0));
        int resourceId = obtainStyledAttributes.getResourceId(1, 0);
        if (resourceId != 0) {
            setTextAndDescription(resourceId);
        }
        obtainStyledAttributes.recycle();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        View view = this.mButtonView;
        if (onClickListener == null) {
            this = null;
        }
        view.setOnClickListener(this);
    }

    public void onClick(View view) {
        View.OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        this.mIconImageView.setImageDrawable(drawable);
    }

    public void setImageResource(int i) {
        if (i != 0) {
            this.mIconImageView.setImageResource(i);
        }
    }

    public void setTextAndDescription(CharSequence charSequence) {
        this.mButtonView.setContentDescription(charSequence);
    }

    public void setTextAndDescription(int i) {
        setTextAndDescription((CharSequence) getContext().getString(i));
    }

    public void setEnabled(boolean z) {
        this.mButtonView.setEnabled(z);
    }

    public boolean isEnabled() {
        return this.mButtonView.isEnabled();
    }

    /* access modifiers changed from: package-private */
    public void setIsCustomCloseAction(boolean z) {
        int i;
        int i2;
        ImageView imageView = this.mIconImageView;
        Resources resources = getResources();
        if (z) {
            i = C3353R.C3354color.tv_pip_menu_close_icon;
        } else {
            i = C3353R.C3354color.tv_pip_menu_icon;
        }
        imageView.setImageTintList(resources.getColorStateList(i));
        View view = this.mButtonBackgroundView;
        Resources resources2 = getResources();
        if (z) {
            i2 = C3353R.C3354color.tv_pip_menu_close_icon_bg;
        } else {
            i2 = C3353R.C3354color.tv_pip_menu_icon_bg;
        }
        view.setBackgroundTintList(resources2.getColorStateList(i2));
    }

    public String toString() {
        if (this.mButtonView.getContentDescription() == null) {
            return "TvPipMenuActionButton";
        }
        return this.mButtonView.getContentDescription().toString();
    }
}
