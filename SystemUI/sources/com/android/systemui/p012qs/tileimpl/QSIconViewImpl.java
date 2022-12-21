package com.android.systemui.p012qs.tileimpl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.AlphaControlledSignalTileView;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.tileimpl.QSIconViewImplEx;
import java.util.Objects;

/* renamed from: com.android.systemui.qs.tileimpl.QSIconViewImpl */
public class QSIconViewImpl extends QSIconView {
    public static final long QS_ANIM_LENGTH = 350;
    public static final String TAG = "QSIconViewImpl";
    protected boolean mAddCircleIconBg;
    private boolean mAnimationEnabled = true;
    protected ImageView mCircleIconBg;
    protected Drawable mCircleIconDrawable;
    protected FrameLayout mCircleIconFrame;
    protected int mCircleIconSize;
    private ValueAnimator mColorAnimator = new ValueAnimator();
    private QSIconViewImplEx mEx = ((QSIconViewImplEx) NTDependencyEx.get(QSIconViewImplEx.class));
    protected final View mIcon;
    protected int mIconSizePx;
    private boolean mIsFastPair;
    private boolean mIsTesla;
    private QSTile.Icon mLastIcon;
    private int mState = -1;
    private int mTint;

    /* access modifiers changed from: protected */
    public int getIconMeasureMode() {
        return 1073741824;
    }

    public QSIconViewImpl(Context context) {
        super(context);
        this.mIconSizePx = context.getResources().getDimensionPixelSize(C1893R.dimen.qs_icon_size);
        this.mIcon = createIcon();
        this.mCircleIconDrawable = this.mEx.getCircleIconDrawable(context);
        this.mColorAnimator.setDuration(350);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mCircleIconSize = this.mEx.getCircleIconSize(this.mContext);
        isIconSizeChanged();
    }

    public void disableAnimation() {
        this.mAnimationEnabled = false;
    }

    public View getIconView() {
        return this.mIcon;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int exactly = exactly(this.mIconSizePx);
        int exactly2 = exactly(this.mCircleIconSize);
        if (this.mAddCircleIconBg) {
            this.mEx.measureCircleIcon(this.mIcon, this.mCircleIconBg, this.mCircleIconFrame, exactly, exactly2);
            setMeasuredDimension(this.mCircleIconFrame.getMeasuredWidth(), this.mCircleIconFrame.getMeasuredHeight());
            return;
        }
        this.mIcon.measure(View.MeasureSpec.makeMeasureSpec(size, getIconMeasureMode()), exactly);
        setMeasuredDimension(size, this.mIcon.getMeasuredHeight());
    }

    public String toString() {
        StringBuilder append = new StringBuilder(getClass().getSimpleName()).append('[');
        append.append("state=" + this.mState);
        append.append(", tint=" + this.mTint);
        if (this.mLastIcon != null) {
            append.append(", lastIcon=" + this.mLastIcon.toString());
        }
        append.append(NavigationBarInflaterView.SIZE_MOD_END);
        return append.toString();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layout(this.mIcon, (getMeasuredWidth() - this.mIcon.getMeasuredWidth()) / 2, (getMeasuredHeight() - this.mIcon.getMeasuredHeight()) / 2);
        if (this.mAddCircleIconBg) {
            layout(this.mCircleIconBg, 0, 0);
            layout(this.mCircleIconFrame, 0, 0);
        }
    }

    public void setIcon(QSTile.State state, boolean z) {
        setIcon((ImageView) this.mIcon, state, z);
    }

    /* access modifiers changed from: protected */
    /* renamed from: updateIcon */
    public void m2957lambda$setIcon$0$comandroidsystemuiqstileimplQSIconViewImpl(ImageView imageView, QSTile.State state, boolean z) {
        Drawable drawable;
        QSTile.Icon icon = state.iconSupplier != null ? state.iconSupplier.get() : state.icon;
        if (!Objects.equals(icon, imageView.getTag(C1893R.C1897id.qs_icon_tag)) || !Objects.equals(state.slash, imageView.getTag(C1893R.C1897id.qs_slash_tag))) {
            boolean z2 = z && shouldAnimate(imageView);
            this.mLastIcon = icon;
            if (icon != null) {
                drawable = z2 ? icon.getDrawable(this.mContext) : icon.getInvisibleDrawable(this.mContext);
            } else {
                drawable = null;
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
                slashImageView.setState((QSTile.SlashState) null, drawable);
            } else {
                imageView.setImageDrawable(drawable);
            }
            imageView.setTag(C1893R.C1897id.qs_icon_tag, icon);
            imageView.setTag(C1893R.C1897id.qs_slash_tag, state.slash);
            imageView.setPadding(0, padding, 0, padding);
            if (drawable instanceof Animatable2) {
                final Animatable2 animatable2 = (Animatable2) drawable;
                animatable2.start();
                if (state.isTransient) {
                    animatable2.registerAnimationCallback(new Animatable2.AnimationCallback() {
                        public void onAnimationEnd(Drawable drawable) {
                            animatable2.start();
                        }
                    });
                }
            }
        }
    }

    private boolean shouldAnimate(ImageView imageView) {
        return this.mAnimationEnabled && imageView.isShown() && imageView.getDrawable() != null;
    }

    /* access modifiers changed from: protected */
    public void setIcon(ImageView imageView, QSTile.State state, boolean z) {
        if (state.disabledByPolicy) {
            imageView.setColorFilter(getContext().getColor(C1893R.C1894color.qs_tile_disabled_color));
        } else {
            imageView.clearColorFilter();
        }
        refreshIconSizeIfNecessary(state);
        if (state.icon != null && state.icon.skipTintBt) {
            if (imageView.getImageTintList() != null) {
                imageView.setImageTintList((ColorStateList) null);
                this.mTint = 0;
            }
            m2957lambda$setIcon$0$comandroidsystemuiqstileimplQSIconViewImpl(imageView, state, z);
        } else if (state.state != this.mState || this.mTint == 0) {
            int color = getColor(state.state);
            this.mState = state.state;
            if (this.mTint == 0 || !z || !shouldAnimate(imageView)) {
                if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
                    ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(color));
                } else {
                    setTint(imageView, color);
                }
                m2957lambda$setIcon$0$comandroidsystemuiqstileimplQSIconViewImpl(imageView, state, z);
                return;
            }
            animateGrayScale(this.mTint, color, imageView, new QSIconViewImpl$$ExternalSyntheticLambda1(this, imageView, state, z));
        } else {
            m2957lambda$setIcon$0$comandroidsystemuiqstileimplQSIconViewImpl(imageView, state, z);
        }
    }

    /* access modifiers changed from: protected */
    public int getColor(int i) {
        return getIconColorForState(getContext(), i);
    }

    private void animateGrayScale(int i, int i2, ImageView imageView, Runnable runnable) {
        if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
            ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(i2));
        }
        this.mColorAnimator.cancel();
        if (!this.mAnimationEnabled || !ValueAnimator.areAnimatorsEnabled()) {
            setTint(imageView, i2);
            runnable.run();
            return;
        }
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("color", new int[]{i, i2});
        ofInt.setEvaluator(ArgbEvaluator.getInstance());
        this.mColorAnimator.setValues(new PropertyValuesHolder[]{ofInt});
        this.mColorAnimator.removeAllListeners();
        this.mColorAnimator.addUpdateListener(new QSIconViewImpl$$ExternalSyntheticLambda0(this, imageView));
        this.mColorAnimator.addListener(new EndRunnableAnimatorListener(runnable));
        this.mColorAnimator.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$animateGrayScale$1$com-android-systemui-qs-tileimpl-QSIconViewImpl */
    public /* synthetic */ void mo36730xff879a00(ImageView imageView, ValueAnimator valueAnimator) {
        setTint(imageView, ((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void setTint(ImageView imageView, int i) {
        imageView.setImageTintList(ColorStateList.valueOf(i));
        this.mTint = i;
    }

    /* access modifiers changed from: protected */
    public View createIcon() {
        SlashImageView slashImageView = new SlashImageView(this.mContext);
        slashImageView.setId(16908294);
        slashImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return slashImageView;
    }

    /* access modifiers changed from: protected */
    public final int exactly(int i) {
        return View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }

    /* access modifiers changed from: protected */
    public final void layout(View view, int i, int i2) {
        view.layout(i, i2, view.getMeasuredWidth() + i, view.getMeasuredHeight() + i2);
    }

    public static int getIconColorForState(Context context, int i) {
        if (i == 0) {
            return Utils.applyAlpha(0.3f, Utils.getColorAttrDefaultColor(context, 16842806));
        }
        if (i == 1) {
            return Utils.getColorAttrDefaultColor(context, 16842806);
        }
        if (i == 2) {
            return Utils.getColorAttrDefaultColor(context, 17957103);
        }
        Log.e("QSIconView", "Invalid state " + i);
        return 0;
    }

    public void setShouldAddCircleIconBg(boolean z) {
        this.mAddCircleIconBg = z;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mCircleIconSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.circle_qs_icon_size);
        this.mCircleIconFrame = new FrameLayout(this.mContext);
        ImageView imageView = new ImageView(this.mContext);
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

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAddCircleIconBg) {
            this.mCircleIconFrame.removeAllViews();
        }
        removeAllViews();
    }

    public int getIconColorForState(Context context, int i, boolean z) {
        if (!z || (i != 0 && i != 1)) {
            return getIconColorForState(context, i);
        }
        return Utils.getColorAttrDefaultColor(context, 16842809);
    }

    public void setCircleIconBgColor(int i) {
        if (this.mAddCircleIconBg) {
            this.mCircleIconDrawable.setTint(i);
        }
    }

    private boolean isIconSizeChanged() {
        Resources resources = this.mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(C1893R.dimen.qs_icon_size);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(C1893R.dimen.qs_icon_size_for_fast_pair);
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
        if (state.icon != null) {
            this.mIsFastPair = state.icon.skipTintBt;
            this.mIsTesla = state.icon.isTesla;
            if (isIconSizeChanged()) {
                ViewGroup.LayoutParams layoutParams = this.mIcon.getLayoutParams();
                layoutParams.width = this.mIconSizePx;
                layoutParams.height = this.mIconSizePx;
                this.mIcon.setLayoutParams(layoutParams);
            }
        }
    }

    /* renamed from: com.android.systemui.qs.tileimpl.QSIconViewImpl$EndRunnableAnimatorListener */
    private static class EndRunnableAnimatorListener extends AnimatorListenerAdapter {
        private Runnable mRunnable;

        EndRunnableAnimatorListener(Runnable runnable) {
            this.mRunnable = runnable;
        }

        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            this.mRunnable.run();
        }

        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            this.mRunnable.run();
        }
    }
}
