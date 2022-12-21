package androidx.leanback.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

class Scale extends Transition {
    private static final String PROPNAME_SCALE = "android:leanback:scale";

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_SCALE, Float.valueOf(transitionValues.view.getScaleX()));
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        float floatValue = ((Float) transitionValues.values.get(PROPNAME_SCALE)).floatValue();
        float floatValue2 = ((Float) transitionValues2.values.get(PROPNAME_SCALE)).floatValue();
        final View view = transitionValues.view;
        view.setScaleX(floatValue);
        view.setScaleY(floatValue);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{floatValue, floatValue2});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                view.setScaleX(floatValue);
                view.setScaleY(floatValue);
            }
        });
        return ofFloat;
    }
}
