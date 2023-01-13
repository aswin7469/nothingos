package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import com.android.systemui.C1894R;

public class NotificationIconDozeHelper extends NotificationDozeHelper {
    private int mColor = ViewCompat.MEASURED_STATE_MASK;
    private PorterDuffColorFilter mImageColorFilter = null;
    private final int mImageDarkAlpha;
    private final int mImageDarkColor = -1;

    public NotificationIconDozeHelper(Context context) {
        this.mImageDarkAlpha = context.getResources().getInteger(C1894R.integer.doze_small_icon_alpha);
    }

    public void setColor(int i) {
        this.mColor = i;
    }

    public void setImageDark(ImageView imageView, boolean z, boolean z2, long j, boolean z3) {
        if (z2) {
            if (!z3) {
                fadeImageColorFilter(imageView, z, j);
                fadeImageAlpha(imageView, z, j);
                return;
            }
            fadeGrayscale(imageView, z, j);
        } else if (!z3) {
            updateImageColorFilter(imageView, z);
            updateImageAlpha(imageView, z);
        } else {
            updateGrayscale(imageView, z);
        }
    }

    private void fadeImageColorFilter(ImageView imageView, boolean z, long j) {
        startIntensityAnimation(new NotificationIconDozeHelper$$ExternalSyntheticLambda1(this, imageView), z, j, (Animator.AnimatorListener) null);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fadeImageColorFilter$0$com-android-systemui-statusbar-notification-NotificationIconDozeHelper */
    public /* synthetic */ void mo39818x2680c1dd(ImageView imageView, ValueAnimator valueAnimator) {
        updateImageColorFilter(imageView, ((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    private void fadeImageAlpha(ImageView imageView, boolean z, long j) {
        startIntensityAnimation(new NotificationIconDozeHelper$$ExternalSyntheticLambda0(this, imageView), z, j, (Animator.AnimatorListener) null);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fadeImageAlpha$1$com-android-systemui-statusbar-notification-NotificationIconDozeHelper */
    public /* synthetic */ void mo39817xb841aef9(ImageView imageView, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        imageView.setImageAlpha((int) (((1.0f - floatValue) * 255.0f) + (((float) this.mImageDarkAlpha) * floatValue)));
    }

    private void updateImageColorFilter(ImageView imageView, boolean z) {
        updateImageColorFilter(imageView, z ? 1.0f : 0.0f);
    }

    private void updateImageColorFilter(ImageView imageView, float f) {
        int interpolateColors = NotificationUtils.interpolateColors(this.mColor, -1, f);
        PorterDuffColorFilter porterDuffColorFilter = this.mImageColorFilter;
        if (porterDuffColorFilter == null || porterDuffColorFilter.getColor() != interpolateColors) {
            this.mImageColorFilter = new PorterDuffColorFilter(interpolateColors, PorterDuff.Mode.SRC_ATOP);
        }
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            Drawable mutate = drawable.mutate();
            mutate.setColorFilter((ColorFilter) null);
            mutate.setColorFilter(this.mImageColorFilter);
        }
    }

    private void updateImageAlpha(ImageView imageView, boolean z) {
        imageView.setImageAlpha(z ? this.mImageDarkAlpha : 255);
    }
}
