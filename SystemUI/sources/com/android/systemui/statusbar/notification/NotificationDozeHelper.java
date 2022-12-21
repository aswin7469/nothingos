package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import java.util.function.Consumer;

public class NotificationDozeHelper {
    private static final int DOZE_ANIMATOR_TAG = 2131427877;
    private final ColorMatrix mGrayscaleColorMatrix = new ColorMatrix();

    public void fadeGrayscale(final ImageView imageView, final boolean z, long j) {
        startIntensityAnimation(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                NotificationDozeHelper.this.updateGrayscale(imageView, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        }, z, j, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                if (!z) {
                    imageView.setColorFilter((ColorFilter) null);
                }
            }
        });
    }

    public void updateGrayscale(ImageView imageView, boolean z) {
        updateGrayscale(imageView, z ? 1.0f : 0.0f);
    }

    public void updateGrayscale(ImageView imageView, float f) {
        if (f > 0.0f) {
            updateGrayscaleMatrix(f);
            imageView.setColorFilter(new ColorMatrixColorFilter(this.mGrayscaleColorMatrix));
            return;
        }
        imageView.setColorFilter((ColorFilter) null);
    }

    public void startIntensityAnimation(ValueAnimator.AnimatorUpdateListener animatorUpdateListener, boolean z, long j, Animator.AnimatorListener animatorListener) {
        float f = 0.0f;
        float f2 = z ? 0.0f : 1.0f;
        if (z) {
            f = 1.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f2, f});
        ofFloat.addUpdateListener(animatorUpdateListener);
        ofFloat.setDuration(500);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.setStartDelay(j);
        if (animatorListener != null) {
            ofFloat.addListener(animatorListener);
        }
        ofFloat.start();
    }

    public void setDozing(Consumer<Float> consumer, boolean z, boolean z2, long j, final View view) {
        if (z2) {
            startIntensityAnimation(new NotificationDozeHelper$$ExternalSyntheticLambda0(consumer), z, j, new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    view.setTag(C1893R.C1897id.doze_intensity_tag, (Object) null);
                }

                public void onAnimationStart(Animator animator) {
                    view.setTag(C1893R.C1897id.doze_intensity_tag, animator);
                }
            });
            return;
        }
        Animator animator = (Animator) view.getTag(C1893R.C1897id.doze_intensity_tag);
        if (animator != null) {
            animator.cancel();
        }
        consumer.accept(Float.valueOf(z ? 1.0f : 0.0f));
    }

    public void updateGrayscaleMatrix(float f) {
        this.mGrayscaleColorMatrix.setSaturation(1.0f - f);
    }

    public ColorMatrix getGrayscaleColorMatrix() {
        return this.mGrayscaleColorMatrix;
    }
}
