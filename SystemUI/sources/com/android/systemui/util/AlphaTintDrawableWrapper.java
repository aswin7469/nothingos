package com.android.systemui.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import com.android.systemui.C1893R;
import java.p026io.IOException;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlPullParserException;

public class AlphaTintDrawableWrapper extends InsetDrawable {
    private int[] mThemeAttrs;
    private ColorStateList mTint;

    public AlphaTintDrawableWrapper() {
        super((Drawable) null, 0);
    }

    AlphaTintDrawableWrapper(Drawable drawable, int[] iArr) {
        super(drawable, 0);
        this.mThemeAttrs = iArr;
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = obtainAttributes(resources, theme, attributeSet, C1893R.styleable.AlphaTintDrawableWrapper);
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        this.mThemeAttrs = obtainAttributes.extractThemeAttrs();
        updateStateFromTypedArray(obtainAttributes);
        obtainAttributes.recycle();
        applyTint();
    }

    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        int[] iArr = this.mThemeAttrs;
        if (!(iArr == null || theme == null)) {
            TypedArray resolveAttributes = theme.resolveAttributes(iArr, C1893R.styleable.AlphaTintDrawableWrapper);
            updateStateFromTypedArray(resolveAttributes);
            resolveAttributes.recycle();
        }
        applyTint();
    }

    public boolean canApplyTheme() {
        int[] iArr = this.mThemeAttrs;
        return (iArr != null && iArr.length > 0) || super.canApplyTheme();
    }

    private void updateStateFromTypedArray(TypedArray typedArray) {
        if (typedArray.hasValue(0)) {
            this.mTint = typedArray.getColorStateList(0);
        }
        if (typedArray.hasValue(1)) {
            setAlpha(Math.round(typedArray.getFloat(1, 1.0f) * 255.0f));
        }
    }

    public void setTintList(ColorStateList colorStateList) {
        super.setTintList(colorStateList);
        this.mTint = colorStateList;
    }

    private void applyTint() {
        if (getDrawable() != null && this.mTint != null) {
            getDrawable().mutate().setTintList(this.mTint);
        }
    }

    public Drawable.ConstantState getConstantState() {
        return new AlphaTintState(super.getConstantState(), this.mThemeAttrs, getAlpha(), this.mTint);
    }

    static class AlphaTintState extends Drawable.ConstantState {
        private int mAlpha;
        private ColorStateList mColorStateList;
        private int[] mThemeAttrs;
        private Drawable.ConstantState mWrappedState;

        public boolean canApplyTheme() {
            return true;
        }

        AlphaTintState(Drawable.ConstantState constantState, int[] iArr, int i, ColorStateList colorStateList) {
            this.mWrappedState = constantState;
            this.mThemeAttrs = iArr;
            this.mAlpha = i;
            this.mColorStateList = colorStateList;
        }

        public Drawable newDrawable() {
            return newDrawable((Resources) null, (Resources.Theme) null);
        }

        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            AlphaTintDrawableWrapper alphaTintDrawableWrapper = new AlphaTintDrawableWrapper(((DrawableWrapper) this.mWrappedState.newDrawable(resources, theme)).getDrawable(), this.mThemeAttrs);
            alphaTintDrawableWrapper.setTintList(this.mColorStateList);
            alphaTintDrawableWrapper.setAlpha(this.mAlpha);
            return alphaTintDrawableWrapper;
        }

        public int getChangingConfigurations() {
            return this.mWrappedState.getChangingConfigurations();
        }
    }
}
