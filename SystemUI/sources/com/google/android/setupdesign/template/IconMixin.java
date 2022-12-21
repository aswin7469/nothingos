package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.util.HeaderAreaStyler;
import com.google.android.setupdesign.util.PartnerStyleHelper;

public class IconMixin implements Mixin {
    private final Context context;
    private final int originalHeight;
    private final ImageView.ScaleType originalScaleType;
    private final TemplateLayout templateLayout;

    public IconMixin(TemplateLayout templateLayout2, AttributeSet attributeSet, int i) {
        this.templateLayout = templateLayout2;
        Context context2 = templateLayout2.getContext();
        this.context = context2;
        ImageView view = getView();
        if (view != null) {
            this.originalHeight = view.getLayoutParams().height;
            this.originalScaleType = view.getScaleType();
        } else {
            this.originalHeight = 0;
            this.originalScaleType = null;
        }
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, C3953R.styleable.SudIconMixin, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(C3953R.styleable.SudIconMixin_android_icon, 0);
        if (resourceId != 0) {
            setIcon(resourceId);
        }
        setUpscaleIcon(obtainStyledAttributes.getBoolean(C3953R.styleable.SudIconMixin_sudUpscaleIcon, false));
        int color = obtainStyledAttributes.getColor(C3953R.styleable.SudIconMixin_sudIconTint, 0);
        if (color != 0) {
            setIconTint(color);
        }
        obtainStyledAttributes.recycle();
    }

    public void tryApplyPartnerCustomizationStyle() {
        if (PartnerStyleHelper.shouldApplyPartnerResource((View) this.templateLayout)) {
            HeaderAreaStyler.applyPartnerCustomizationIconStyle(getView(), getContainerView());
        }
    }

    public void setIcon(Drawable drawable) {
        ImageView view = getView();
        if (view != null) {
            if (drawable != null) {
                drawable.applyTheme(this.context.getTheme());
            }
            view.setImageDrawable(drawable);
            view.setVisibility(drawable != null ? 0 : 8);
            setIconContainerVisibility(view.getVisibility());
            tryApplyPartnerCustomizationStyle();
        }
    }

    public void setIcon(int i) {
        ImageView view = getView();
        if (view != null) {
            view.setImageResource(i);
            view.setVisibility(i != 0 ? 0 : 8);
            setIconContainerVisibility(view.getVisibility());
        }
    }

    public Drawable getIcon() {
        ImageView view = getView();
        if (view != null) {
            return view.getDrawable();
        }
        return null;
    }

    public void setUpscaleIcon(boolean z) {
        ImageView view = getView();
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            int maxHeight = view.getMaxHeight();
            if (!z) {
                maxHeight = this.originalHeight;
            }
            layoutParams.height = maxHeight;
            view.setLayoutParams(layoutParams);
            view.setScaleType(z ? ImageView.ScaleType.FIT_CENTER : this.originalScaleType);
        }
    }

    public void setIconTint(int i) {
        ImageView view = getView();
        if (view != null) {
            view.setColorFilter(i);
        }
    }

    public void setContentDescription(CharSequence charSequence) {
        ImageView view = getView();
        if (view != null) {
            view.setContentDescription(charSequence);
        }
    }

    public CharSequence getContentDescription() {
        ImageView view = getView();
        if (view != null) {
            return view.getContentDescription();
        }
        return null;
    }

    public void setVisibility(int i) {
        ImageView view = getView();
        if (view != null) {
            view.setVisibility(i);
            setIconContainerVisibility(i);
        }
    }

    /* access modifiers changed from: protected */
    public ImageView getView() {
        return (ImageView) this.templateLayout.findManagedViewById(C3953R.C3956id.sud_layout_icon);
    }

    /* access modifiers changed from: protected */
    public FrameLayout getContainerView() {
        return (FrameLayout) this.templateLayout.findManagedViewById(C3953R.C3956id.sud_layout_icon_container);
    }

    private void setIconContainerVisibility(int i) {
        if (getContainerView() != null) {
            getContainerView().setVisibility(i);
        }
    }
}
