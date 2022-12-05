package com.android.systemui.qs.tileimpl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.AlphaControlledSignalTileView;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class QSIconViewImpl extends QSIconView {
    protected boolean mAddCircleIconBg;
    protected ImageView mCircleIconBg;
    protected Drawable mCircleIconDrawable;
    protected FrameLayout mCircleIconFrame;
    protected int mCircleIconSize;
    protected int mIconSizePx;
    private boolean mIsFastPair;
    private boolean mIsTesla;
    private QSTile.Icon mLastIcon;
    private int mTint;
    private boolean mAnimationEnabled = true;
    private int mState = -1;
    protected final View mIcon = createIcon();

    protected int getIconMeasureMode() {
        return 1073741824;
    }

    public QSIconViewImpl(Context context) {
        super(context);
        this.mIconSizePx = context.getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        this.mCircleIconDrawable = context.getResources().getDrawable(R$drawable.circle_tile_icon_bg);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mCircleIconSize = ((ViewGroup) this).mContext.getResources().getDimensionPixelSize(R$dimen.circle_qs_icon_size);
        isIconSizeChanged();
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public void disableAnimation() {
        this.mAnimationEnabled = false;
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public View getIconView() {
        return this.mIcon;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int exactly = exactly(this.mIconSizePx);
        int exactly2 = exactly(this.mCircleIconSize);
        if (this.mAddCircleIconBg) {
            this.mIcon.measure(exactly, exactly);
            this.mCircleIconBg.measure(exactly2, exactly2);
            this.mCircleIconFrame.measure(exactly2, exactly2);
            setMeasuredDimension(this.mCircleIconFrame.getMeasuredWidth(), this.mCircleIconFrame.getMeasuredHeight());
            return;
        }
        this.mIcon.measure(View.MeasureSpec.makeMeasureSpec(size, getIconMeasureMode()), exactly);
        setMeasuredDimension(size, this.mIcon.getMeasuredHeight());
    }

    @Override // android.view.View
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append('[');
        sb.append("state=" + this.mState);
        sb.append(", tint=" + this.mTint);
        if (this.mLastIcon != null) {
            sb.append(", lastIcon=" + this.mLastIcon.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layout(this.mIcon, (getMeasuredWidth() - this.mIcon.getMeasuredWidth()) / 2, (getMeasuredHeight() - this.mIcon.getMeasuredHeight()) / 2);
        if (this.mAddCircleIconBg) {
            layout(this.mCircleIconBg, 0, 0);
            layout(this.mCircleIconFrame, 0, 0);
        }
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public void setIcon(QSTile.State state, boolean z) {
        setIcon((ImageView) this.mIcon, state, z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: updateIcon */
    public void lambda$setIcon$0(ImageView imageView, QSTile.State state, boolean z) {
        Drawable drawable;
        Supplier<QSTile.Icon> supplier = state.iconSupplier;
        QSTile.Icon icon = supplier != null ? supplier.get() : state.icon;
        int i = R$id.qs_icon_tag;
        if (!Objects.equals(icon, imageView.getTag(i)) || !Objects.equals(state.slash, imageView.getTag(R$id.qs_slash_tag))) {
            boolean z2 = z && shouldAnimate(imageView);
            this.mLastIcon = icon;
            if (icon == null) {
                drawable = null;
            } else if (z2) {
                drawable = icon.getDrawable(((ViewGroup) this).mContext);
            } else {
                drawable = icon.getInvisibleDrawable(((ViewGroup) this).mContext);
            }
            int padding = icon != null ? icon.getPadding() : 0;
            if (drawable != null) {
                if (drawable.getConstantState() != null) {
                    drawable = drawable.getConstantState().newDrawable();
                }
                drawable.setAutoMirrored(false);
                drawable.setLayoutDirection(getLayoutDirection());
            }
            if (imageView instanceof SlashImageView) {
                SlashImageView slashImageView = (SlashImageView) imageView;
                slashImageView.setAnimationEnabled(z2);
                slashImageView.setState(null, drawable);
            } else {
                imageView.setImageDrawable(drawable);
            }
            imageView.setTag(i, icon);
            imageView.setTag(R$id.qs_slash_tag, state.slash);
            imageView.setPadding(0, padding, 0, padding);
            if (!(drawable instanceof Animatable2)) {
                return;
            }
            final Animatable2 animatable2 = (Animatable2) drawable;
            animatable2.start();
            if (!state.isTransient) {
                return;
            }
            animatable2.registerAnimationCallback(new Animatable2.AnimationCallback() { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl.1
                @Override // android.graphics.drawable.Animatable2.AnimationCallback
                public void onAnimationEnd(Drawable drawable2) {
                    animatable2.start();
                }
            });
        }
    }

    private boolean shouldAnimate(ImageView imageView) {
        return this.mAnimationEnabled && imageView.isShown() && imageView.getDrawable() != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setIcon(final ImageView imageView, final QSTile.State state, final boolean z) {
        if (state.disabledByPolicy) {
            imageView.setColorFilter(getContext().getColor(R$color.qs_tile_disabled_color));
        } else {
            imageView.clearColorFilter();
        }
        refreshIconSizeIfNecessary(state);
        QSTile.Icon icon = state.icon;
        if (icon != null && icon.skipTintBt) {
            if (imageView.getImageTintList() != null) {
                imageView.setImageTintList(null);
                this.mTint = 0;
            }
            lambda$setIcon$0(imageView, state, z);
            return;
        }
        int i = state.state;
        if (i != this.mState || this.mTint == 0) {
            int color = getColor(i);
            this.mState = state.state;
            if (this.mTint != 0 && z && shouldAnimate(imageView)) {
                animateGrayScale(this.mTint, color, imageView, new Runnable() { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        QSIconViewImpl.this.lambda$setIcon$0(imageView, state, z);
                    }
                });
                this.mTint = color;
                return;
            }
            if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
                ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(color));
            } else {
                setTint(imageView, color);
            }
            this.mTint = color;
            lambda$setIcon$0(imageView, state, z);
            return;
        }
        lambda$setIcon$0(imageView, state, z);
    }

    protected int getColor(int i) {
        return getIconColorForState(getContext(), i);
    }

    private void animateGrayScale(int i, int i2, final ImageView imageView, final Runnable runnable) {
        if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
            ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(i2));
        }
        if (this.mAnimationEnabled && ValueAnimator.areAnimatorsEnabled()) {
            final float alpha = Color.alpha(i);
            final float alpha2 = Color.alpha(i2);
            final float red = Color.red(i);
            final float red2 = Color.red(i2);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setDuration(350L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    QSIconViewImpl.lambda$animateGrayScale$1(alpha, alpha2, red, red2, imageView, valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    runnable.run();
                }
            });
            ofFloat.start();
            return;
        }
        setTint(imageView, i2);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateGrayScale$1(float f, float f2, float f3, float f4, ImageView imageView, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        int i = (int) (f3 + ((f4 - f3) * animatedFraction));
        setTint(imageView, Color.argb((int) (f + ((f2 - f) * animatedFraction)), i, i, i));
    }

    public static void setTint(ImageView imageView, int i) {
        imageView.setImageTintList(ColorStateList.valueOf(i));
    }

    protected View createIcon() {
        SlashImageView slashImageView = new SlashImageView(((ViewGroup) this).mContext);
        slashImageView.setId(16908294);
        slashImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return slashImageView;
    }

    protected final int exactly(int i) {
        return View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }

    protected final void layout(View view, int i, int i2) {
        view.layout(i, i2, view.getMeasuredWidth() + i, view.getMeasuredHeight() + i2);
    }

    public static int getIconColorForState(Context context, int i) {
        if (i != 0) {
            if (i == 1) {
                return Utils.getColorAttrDefaultColor(context, 16842806);
            }
            if (i == 2) {
                return Utils.getColorAttrDefaultColor(context, 16842809);
            }
            Log.e("QSIconView", "Invalid state " + i);
            return 0;
        }
        return Utils.applyAlpha(0.3f, Utils.getColorAttrDefaultColor(context, 16842806));
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public void setShouldAddCircleIconBg(boolean z) {
        this.mAddCircleIconBg = z;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mCircleIconSize = ((ViewGroup) this).mContext.getResources().getDimensionPixelSize(R$dimen.circle_qs_icon_size);
        this.mCircleIconFrame = new FrameLayout(((ViewGroup) this).mContext);
        ImageView imageView = new ImageView(((ViewGroup) this).mContext);
        this.mCircleIconBg = imageView;
        if (this.mAddCircleIconBg) {
            imageView.setImageDrawable(this.mCircleIconDrawable);
            this.mCircleIconFrame.addView(this.mCircleIconBg);
            FrameLayout frameLayout = this.mCircleIconFrame;
            View view = this.mIcon;
            int i = this.mIconSizePx;
            frameLayout.addView(view, new FrameLayout.LayoutParams(i, i, 17));
            addView(this.mCircleIconFrame);
            return;
        }
        addView(this.mIcon);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAddCircleIconBg) {
            this.mCircleIconFrame.removeAllViews();
        }
        removeAllViews();
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public void setCircleIconBgColor(int i) {
        if (this.mAddCircleIconBg) {
            this.mCircleIconDrawable.setTint(i);
        }
    }

    private boolean isIconSizeChanged() {
        Resources resources = ((ViewGroup) this).mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.qs_icon_size);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.qs_icon_size_for_fast_pair);
        int i = this.mCircleIconSize;
        boolean z = this.mIsFastPair;
        if (z && !this.mIsTesla && this.mIconSizePx != dimensionPixelSize2) {
            this.mIconSizePx = dimensionPixelSize2;
            return true;
        } else if (this.mIsTesla && this.mIconSizePx != i) {
            this.mIconSizePx = i;
            return true;
        } else if (z || this.mIconSizePx == dimensionPixelSize) {
            return false;
        } else {
            this.mIconSizePx = dimensionPixelSize;
            return true;
        }
    }

    private void refreshIconSizeIfNecessary(QSTile.State state) {
        QSTile.Icon icon = state.icon;
        if (icon != null) {
            this.mIsFastPair = icon.skipTintBt;
            this.mIsTesla = icon.isTesla;
            if (!isIconSizeChanged()) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = this.mIcon.getLayoutParams();
            int i = this.mIconSizePx;
            layoutParams.width = i;
            layoutParams.height = i;
            this.mIcon.setLayoutParams(layoutParams);
        }
    }
}
