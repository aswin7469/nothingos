package androidx.leanback.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$color;
import androidx.leanback.R$dimen;
import androidx.leanback.R$drawable;
import androidx.leanback.R$fraction;
import androidx.leanback.R$id;
import androidx.leanback.R$integer;
import androidx.leanback.R$layout;
import androidx.leanback.R$styleable;
/* loaded from: classes.dex */
public class SearchOrbView extends FrameLayout implements View.OnClickListener {
    private boolean mAttachedToWindow;
    private boolean mColorAnimationEnabled;
    private ValueAnimator mColorAnimator;
    private final ArgbEvaluator mColorEvaluator;
    private Colors mColors;
    private final ValueAnimator.AnimatorUpdateListener mFocusUpdateListener;
    private final float mFocusedZ;
    private final float mFocusedZoom;
    private ImageView mIcon;
    private Drawable mIconDrawable;
    private View.OnClickListener mListener;
    private final int mPulseDurationMs;
    private View mRootView;
    private final int mScaleDurationMs;
    private View mSearchOrbView;
    private ValueAnimator mShadowFocusAnimator;
    private final float mUnfocusedZ;
    private final ValueAnimator.AnimatorUpdateListener mUpdateListener;

    /* loaded from: classes.dex */
    public static class Colors {
        public int brightColor;
        public int color;
        public int iconColor;

        public Colors(int color, int brightColor, int iconColor) {
            this.color = color;
            this.brightColor = brightColor == color ? getBrightColor(color) : brightColor;
            this.iconColor = iconColor;
        }

        public static int getBrightColor(int color) {
            return Color.argb((int) ((Color.alpha(color) * 0.85f) + 38.25f), (int) ((Color.red(color) * 0.85f) + 38.25f), (int) ((Color.green(color) * 0.85f) + 38.25f), (int) ((Color.blue(color) * 0.85f) + 38.25f));
        }
    }

    void setSearchOrbZ(float fraction) {
        View view = this.mSearchOrbView;
        float f = this.mUnfocusedZ;
        ViewCompat.setZ(view, f + (fraction * (this.mFocusedZ - f)));
    }

    public SearchOrbView(Context context) {
        this(context, null);
    }

    public SearchOrbView(Context context, AttributeSet attrs) {
        this(context, attrs, R$attr.searchOrbViewStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public SearchOrbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mColorEvaluator = new ArgbEvaluator();
        this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.leanback.widget.SearchOrbView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animator) {
                SearchOrbView.this.setOrbViewColor(((Integer) animator.getAnimatedValue()).intValue());
            }
        };
        this.mFocusUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.leanback.widget.SearchOrbView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                SearchOrbView.this.setSearchOrbZ(animation.getAnimatedFraction());
            }
        };
        Resources resources = context.getResources();
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(getLayoutResourceId(), (ViewGroup) this, true);
        this.mRootView = inflate;
        this.mSearchOrbView = inflate.findViewById(R$id.search_orb);
        this.mIcon = (ImageView) this.mRootView.findViewById(R$id.icon);
        this.mFocusedZoom = context.getResources().getFraction(R$fraction.lb_search_orb_focused_zoom, 1, 1);
        this.mPulseDurationMs = context.getResources().getInteger(R$integer.lb_search_orb_pulse_duration_ms);
        this.mScaleDurationMs = context.getResources().getInteger(R$integer.lb_search_orb_scale_duration_ms);
        float dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.lb_search_orb_focused_z);
        this.mFocusedZ = dimensionPixelSize;
        this.mUnfocusedZ = context.getResources().getDimensionPixelSize(R$dimen.lb_search_orb_unfocused_z);
        int[] iArr = R$styleable.lbSearchOrbView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr, defStyleAttr, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, defStyleAttr, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.lbSearchOrbView_searchOrbIcon);
        setOrbIcon(drawable == null ? resources.getDrawable(R$drawable.lb_ic_in_app_search) : drawable);
        int color = obtainStyledAttributes.getColor(R$styleable.lbSearchOrbView_searchOrbColor, resources.getColor(R$color.lb_default_search_color));
        setOrbColors(new Colors(color, obtainStyledAttributes.getColor(R$styleable.lbSearchOrbView_searchOrbBrightColor, color), obtainStyledAttributes.getColor(R$styleable.lbSearchOrbView_searchOrbIconColor, 0)));
        obtainStyledAttributes.recycle();
        setFocusable(true);
        setClipChildren(false);
        setOnClickListener(this);
        setSoundEffectsEnabled(false);
        setSearchOrbZ(0.0f);
        ViewCompat.setZ(this.mIcon, dimensionPixelSize);
    }

    int getLayoutResourceId() {
        return R$layout.lb_search_orb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scaleOrbViewOnly(float scale) {
        this.mSearchOrbView.setScaleX(scale);
        this.mSearchOrbView.setScaleY(scale);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getFocusedZoom() {
        return this.mFocusedZoom;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        View.OnClickListener onClickListener = this.mListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    private void startShadowFocusAnimation(boolean gainFocus, int duration) {
        if (this.mShadowFocusAnimator == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mShadowFocusAnimator = ofFloat;
            ofFloat.addUpdateListener(this.mFocusUpdateListener);
        }
        if (gainFocus) {
            this.mShadowFocusAnimator.start();
        } else {
            this.mShadowFocusAnimator.reverse();
        }
        this.mShadowFocusAnimator.setDuration(duration);
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        animateOnFocus(gainFocus);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void animateOnFocus(boolean hasFocus) {
        float f = hasFocus ? this.mFocusedZoom : 1.0f;
        this.mRootView.animate().scaleX(f).scaleY(f).setDuration(this.mScaleDurationMs).start();
        startShadowFocusAnimation(hasFocus, this.mScaleDurationMs);
        enableOrbColorAnimation(hasFocus);
    }

    public void setOrbIcon(Drawable icon) {
        this.mIconDrawable = icon;
        this.mIcon.setImageDrawable(icon);
    }

    public void setOnOrbClickedListener(View.OnClickListener listener) {
        this.mListener = listener;
    }

    public void setOrbColors(Colors colors) {
        this.mColors = colors;
        this.mIcon.setColorFilter(colors.iconColor);
        if (this.mColorAnimator == null) {
            setOrbViewColor(this.mColors.color);
        } else {
            enableOrbColorAnimation(true);
        }
    }

    public void enableOrbColorAnimation(boolean enable) {
        this.mColorAnimationEnabled = enable;
        updateColorAnimator();
    }

    private void updateColorAnimator() {
        ValueAnimator valueAnimator = this.mColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.mColorAnimator = null;
        }
        if (!this.mColorAnimationEnabled || !this.mAttachedToWindow) {
            return;
        }
        ValueAnimator ofObject = ValueAnimator.ofObject(this.mColorEvaluator, Integer.valueOf(this.mColors.color), Integer.valueOf(this.mColors.brightColor), Integer.valueOf(this.mColors.color));
        this.mColorAnimator = ofObject;
        ofObject.setRepeatCount(-1);
        this.mColorAnimator.setDuration(this.mPulseDurationMs * 2);
        this.mColorAnimator.addUpdateListener(this.mUpdateListener);
        this.mColorAnimator.start();
    }

    void setOrbViewColor(int color) {
        if (this.mSearchOrbView.getBackground() instanceof GradientDrawable) {
            ((GradientDrawable) this.mSearchOrbView.getBackground()).setColor(color);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        updateColorAnimator();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.mAttachedToWindow = false;
        updateColorAnimator();
        super.onDetachedFromWindow();
    }
}
