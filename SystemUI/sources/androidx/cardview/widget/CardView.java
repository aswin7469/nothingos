package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.cardview.R$attr;
import androidx.cardview.R$color;
import androidx.cardview.R$style;
import androidx.cardview.R$styleable;
import androidx.core.view.ViewCompat;
/* loaded from: classes.dex */
public class CardView extends FrameLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = {16842801};
    private static final CardViewImpl IMPL;
    private final CardViewDelegate mCardViewDelegate;
    private boolean mCompatPadding;
    final Rect mContentPadding;
    private boolean mPreventCornerOverlap;
    final Rect mShadowBounds;
    int mUserSetMinHeight;
    int mUserSetMinWidth;

    @Override // android.view.View
    public void setPadding(int left, int top, int right, int bottom) {
    }

    @Override // android.view.View
    public void setPaddingRelative(int start, int top, int end, int bottom) {
    }

    static {
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            IMPL = new CardViewApi21Impl();
        } else if (i >= 17) {
            IMPL = new CardViewApi17Impl();
        } else {
            IMPL = new CardViewBaseImpl();
        }
        IMPL.initStatic();
    }

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, R$attr.cardViewStyle);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int color;
        ColorStateList valueOf;
        Rect rect = new Rect();
        this.mContentPadding = rect;
        this.mShadowBounds = new Rect();
        CardViewDelegate cardViewDelegate = new CardViewDelegate() { // from class: androidx.cardview.widget.CardView.1
            private Drawable mCardBackground;

            @Override // androidx.cardview.widget.CardViewDelegate
            public void setCardBackground(Drawable drawable) {
                this.mCardBackground = drawable;
                CardView.this.setBackgroundDrawable(drawable);
            }

            @Override // androidx.cardview.widget.CardViewDelegate
            public boolean getUseCompatPadding() {
                return CardView.this.getUseCompatPadding();
            }

            @Override // androidx.cardview.widget.CardViewDelegate
            public boolean getPreventCornerOverlap() {
                return CardView.this.getPreventCornerOverlap();
            }

            @Override // androidx.cardview.widget.CardViewDelegate
            public void setShadowPadding(int left, int top, int right, int bottom) {
                CardView.this.mShadowBounds.set(left, top, right, bottom);
                CardView cardView = CardView.this;
                Rect rect2 = cardView.mContentPadding;
                CardView.super.setPadding(left + rect2.left, top + rect2.top, right + rect2.right, bottom + rect2.bottom);
            }

            @Override // androidx.cardview.widget.CardViewDelegate
            public void setMinWidthHeightInternal(int width, int height) {
                CardView cardView = CardView.this;
                if (width > cardView.mUserSetMinWidth) {
                    CardView.super.setMinimumWidth(width);
                }
                CardView cardView2 = CardView.this;
                if (height > cardView2.mUserSetMinHeight) {
                    CardView.super.setMinimumHeight(height);
                }
            }

            @Override // androidx.cardview.widget.CardViewDelegate
            public Drawable getCardBackground() {
                return this.mCardBackground;
            }

            @Override // androidx.cardview.widget.CardViewDelegate
            public View getCardView() {
                return CardView.this;
            }
        };
        this.mCardViewDelegate = cardViewDelegate;
        int[] iArr = R$styleable.CardView;
        int i = R$style.CardView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr, defStyleAttr, i);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, defStyleAttr, i);
        int i2 = R$styleable.CardView_cardBackgroundColor;
        if (obtainStyledAttributes.hasValue(i2)) {
            valueOf = obtainStyledAttributes.getColorStateList(i2);
        } else {
            TypedArray obtainStyledAttributes2 = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            int color2 = obtainStyledAttributes2.getColor(0, 0);
            obtainStyledAttributes2.recycle();
            float[] fArr = new float[3];
            Color.colorToHSV(color2, fArr);
            if (fArr[2] > 0.5f) {
                color = getResources().getColor(R$color.cardview_light_background);
            } else {
                color = getResources().getColor(R$color.cardview_dark_background);
            }
            valueOf = ColorStateList.valueOf(color);
        }
        ColorStateList colorStateList = valueOf;
        float dimension = obtainStyledAttributes.getDimension(R$styleable.CardView_cardCornerRadius, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(R$styleable.CardView_cardElevation, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(R$styleable.CardView_cardMaxElevation, 0.0f);
        this.mCompatPadding = obtainStyledAttributes.getBoolean(R$styleable.CardView_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = obtainStyledAttributes.getBoolean(R$styleable.CardView_cardPreventCornerOverlap, true);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPadding, 0);
        rect.left = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingLeft, dimensionPixelSize);
        rect.top = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingTop, dimensionPixelSize);
        rect.right = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingRight, dimensionPixelSize);
        rect.bottom = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingBottom, dimensionPixelSize);
        float f = dimension2 > dimension3 ? dimension2 : dimension3;
        this.mUserSetMinWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_android_minWidth, 0);
        this.mUserSetMinHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_android_minHeight, 0);
        obtainStyledAttributes.recycle();
        IMPL.initialize(cardViewDelegate, context, colorStateList, dimension, dimension2, f);
    }

    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        this.mContentPadding.set(left, top, right, bottom);
        IMPL.updatePadding(this.mCardViewDelegate);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CardViewImpl cardViewImpl = IMPL;
        if (!(cardViewImpl instanceof CardViewApi21Impl)) {
            int mode = View.MeasureSpec.getMode(widthMeasureSpec);
            if (mode == Integer.MIN_VALUE || mode == 1073741824) {
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(cardViewImpl.getMinWidth(this.mCardViewDelegate)), View.MeasureSpec.getSize(widthMeasureSpec)), mode);
            }
            int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
            if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(cardViewImpl.getMinHeight(this.mCardViewDelegate)), View.MeasureSpec.getSize(heightMeasureSpec)), mode2);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.view.View
    public void setMinimumWidth(int minWidth) {
        this.mUserSetMinWidth = minWidth;
        super.setMinimumWidth(minWidth);
    }

    @Override // android.view.View
    public void setMinimumHeight(int minHeight) {
        this.mUserSetMinHeight = minHeight;
        super.setMinimumHeight(minHeight);
    }

    public ColorStateList getCardBackgroundColor() {
        return IMPL.getBackgroundColor(this.mCardViewDelegate);
    }

    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    public void setRadius(float radius) {
        IMPL.setRadius(this.mCardViewDelegate, radius);
    }

    public float getRadius() {
        return IMPL.getRadius(this.mCardViewDelegate);
    }

    public float getCardElevation() {
        return IMPL.getElevation(this.mCardViewDelegate);
    }

    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(this.mCardViewDelegate);
    }

    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }
}
