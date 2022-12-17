package com.nothing.settings.utils;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class AnimationUtils {

    public interface TransformationListener {
        void applyTransformation(float f);
    }

    public static Animation expandTranslate(View view, boolean z, TransformationListener transformationListener) {
        view.measure(-1, -2);
        int measuredHeight = view.getMeasuredHeight();
        int height = view.getHeight();
        Log.d("AnimationUtils", "expandTranslate height = " + height + ", viewHeight:" + measuredHeight + ", needAlpha:" + z);
        final TransformationListener transformationListener2 = transformationListener;
        C20463 r2 = new TranslateAnimation(0.0f, 0.0f, (float) (-view.getMeasuredHeight()), 0.0f) {
            /* access modifiers changed from: protected */
            public void applyTransformation(float f, Transformation transformation) {
                super.applyTransformation(f, transformation);
                TransformationListener transformationListener = transformationListener2;
                if (transformationListener != null) {
                    transformationListener.applyTransformation(f);
                }
            }
        };
        view.setVisibility(0);
        if (z) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(r2);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(300);
            animationSet.setInterpolator(new LinearOutSlowInInterpolator());
            view.startAnimation(animationSet);
        } else {
            r2.setDuration(300);
            r2.setInterpolator(new LinearOutSlowInInterpolator());
            view.startAnimation(r2);
        }
        return r2;
    }

    public static Animation collapseTranslate(View view, boolean z, TransformationListener transformationListener) {
        view.measure(-1, -2);
        int measuredHeight = view.getMeasuredHeight();
        int height = view.getHeight();
        Log.d("AnimationUtils", "collapseTranslate height = " + height + ", viewHeight:" + measuredHeight + ", needAlpha:" + z);
        final TransformationListener transformationListener2 = transformationListener;
        C20474 r2 = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-view.getMeasuredHeight())) {
            /* access modifiers changed from: protected */
            public void applyTransformation(float f, Transformation transformation) {
                super.applyTransformation(f, transformation);
                TransformationListener transformationListener = transformationListener2;
                if (transformationListener != null) {
                    transformationListener.applyTransformation(f);
                }
            }
        };
        view.setVisibility(0);
        if (z) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(r2);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(300);
            animationSet.setInterpolator(new LinearOutSlowInInterpolator());
            view.startAnimation(animationSet);
        } else {
            r2.setDuration(300);
            r2.setInterpolator(new LinearOutSlowInInterpolator());
            view.startAnimation(r2);
        }
        return r2;
    }
}
