package com.android.systemui.p012qs;

import android.icu.text.DateFormat;
import android.util.FloatProperty;
import android.util.MathUtils;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.widget.Key;
import java.util.ArrayList;
import java.util.List;
import sun.util.locale.LanguageTag;

/* renamed from: com.android.systemui.qs.TouchAnimator */
public class TouchAnimator {
    /* access modifiers changed from: private */
    public static final FloatProperty<TouchAnimator> POSITION = new FloatProperty<TouchAnimator>("position") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(TouchAnimator touchAnimator, float f) {
            touchAnimator.setPosition(f);
        }

        public Float get(TouchAnimator touchAnimator) {
            return Float.valueOf(touchAnimator.mLastT);
        }
    };
    private final float mEndDelay;
    private final Interpolator mInterpolator;
    private final KeyframeSet[] mKeyframeSets;
    /* access modifiers changed from: private */
    public float mLastT;
    private final Listener mListener;
    private final float mSpan;
    private final float mStartDelay;
    private final Object[] mTargets;

    /* renamed from: com.android.systemui.qs.TouchAnimator$Listener */
    public interface Listener {
        void onAnimationAtEnd();

        void onAnimationAtStart();

        void onAnimationStarted();
    }

    /* renamed from: com.android.systemui.qs.TouchAnimator$ListenerAdapter */
    public static class ListenerAdapter implements Listener {
        public void onAnimationAtEnd() {
        }

        public void onAnimationAtStart() {
        }

        public void onAnimationStarted() {
        }
    }

    private TouchAnimator(Object[] objArr, KeyframeSet[] keyframeSetArr, float f, float f2, Interpolator interpolator, Listener listener) {
        this.mLastT = -1.0f;
        this.mTargets = objArr;
        this.mKeyframeSets = keyframeSetArr;
        this.mStartDelay = f;
        this.mEndDelay = f2;
        this.mSpan = (1.0f - f2) - f;
        this.mInterpolator = interpolator;
        this.mListener = listener;
    }

    public void setPosition(float f) {
        if (!Float.isNaN(f)) {
            float constrain = MathUtils.constrain((f - this.mStartDelay) / this.mSpan, 0.0f, 1.0f);
            Interpolator interpolator = this.mInterpolator;
            if (interpolator != null) {
                constrain = interpolator.getInterpolation(constrain);
            }
            float f2 = this.mLastT;
            if (constrain != f2) {
                Listener listener = this.mListener;
                if (listener != null) {
                    if (constrain == 1.0f) {
                        listener.onAnimationAtEnd();
                    } else if (constrain == 0.0f) {
                        listener.onAnimationAtStart();
                    } else if (f2 <= 0.0f || f2 == 1.0f) {
                        listener.onAnimationStarted();
                    }
                    this.mLastT = constrain;
                }
                int i = 0;
                while (true) {
                    Object[] objArr = this.mTargets;
                    if (i < objArr.length) {
                        this.mKeyframeSets[i].setValue(constrain, objArr[i]);
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: com.android.systemui.qs.TouchAnimator$Builder */
    public static class Builder {
        private float mEndDelay;
        private Interpolator mInterpolator;
        private Listener mListener;
        private float mStartDelay;
        private List<Object> mTargets = new ArrayList();
        private List<KeyframeSet> mValues = new ArrayList();

        public Builder addFloat(Object obj, String str, float... fArr) {
            add(obj, KeyframeSet.ofFloat(getProperty(obj, str, Float.TYPE), fArr));
            return this;
        }

        public Builder addInt(Object obj, String str, int... iArr) {
            add(obj, KeyframeSet.ofInt(getProperty(obj, str, Integer.TYPE), iArr));
            return this;
        }

        private void add(Object obj, KeyframeSet keyframeSet) {
            this.mTargets.add(obj);
            this.mValues.add(keyframeSet);
        }

        private static Property getProperty(Object obj, String str, Class<?> cls) {
            if (obj instanceof View) {
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1225497657:
                        if (str.equals(Key.TRANSLATION_X)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1225497656:
                        if (str.equals(Key.TRANSLATION_Y)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1225497655:
                        if (str.equals(Key.TRANSLATION_Z)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -908189618:
                        if (str.equals(Key.SCALE_X)) {
                            c = 3;
                            break;
                        }
                        break;
                    case -908189617:
                        if (str.equals(Key.SCALE_Y)) {
                            c = 4;
                            break;
                        }
                        break;
                    case -40300674:
                        if (str.equals(Key.ROTATION)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 120:
                        if (str.equals(LanguageTag.PRIVATEUSE)) {
                            c = 6;
                            break;
                        }
                        break;
                    case 121:
                        if (str.equals(DateFormat.YEAR)) {
                            c = 7;
                            break;
                        }
                        break;
                    case 92909918:
                        if (str.equals(Key.ALPHA)) {
                            c = 8;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        return View.TRANSLATION_X;
                    case 1:
                        return View.TRANSLATION_Y;
                    case 2:
                        return View.TRANSLATION_Z;
                    case 3:
                        return View.SCALE_X;
                    case 4:
                        return View.SCALE_Y;
                    case 5:
                        return View.ROTATION;
                    case 6:
                        return View.X;
                    case 7:
                        return View.Y;
                    case 8:
                        return View.ALPHA;
                }
            }
            if (!(obj instanceof TouchAnimator) || !"position".equals(str)) {
                return Property.of(obj.getClass(), cls, str);
            }
            return TouchAnimator.POSITION;
        }

        public Builder setStartDelay(float f) {
            this.mStartDelay = f;
            return this;
        }

        public Builder setEndDelay(float f) {
            this.mEndDelay = f;
            return this;
        }

        public Builder setInterpolator(Interpolator interpolator) {
            this.mInterpolator = interpolator;
            return this;
        }

        public Builder setListener(Listener listener) {
            this.mListener = listener;
            return this;
        }

        public TouchAnimator build() {
            List<Object> list = this.mTargets;
            Object[] array = list.toArray(new Object[list.size()]);
            List<KeyframeSet> list2 = this.mValues;
            return new TouchAnimator(array, (KeyframeSet[]) list2.toArray(new KeyframeSet[list2.size()]), this.mStartDelay, this.mEndDelay, this.mInterpolator, this.mListener);
        }
    }

    /* renamed from: com.android.systemui.qs.TouchAnimator$KeyframeSet */
    private static abstract class KeyframeSet {
        private final float mFrameWidth;
        private final int mSize;

        /* access modifiers changed from: protected */
        public abstract void interpolate(int i, float f, Object obj);

        public KeyframeSet(int i) {
            this.mSize = i;
            this.mFrameWidth = 1.0f / ((float) (i - 1));
        }

        /* access modifiers changed from: package-private */
        public void setValue(float f, Object obj) {
            int constrain = MathUtils.constrain((int) Math.ceil((double) (f / this.mFrameWidth)), 1, this.mSize - 1);
            float f2 = this.mFrameWidth;
            interpolate(constrain, (f - (((float) (constrain - 1)) * f2)) / f2, obj);
        }

        public static KeyframeSet ofInt(Property property, int... iArr) {
            return new IntKeyframeSet(property, iArr);
        }

        public static KeyframeSet ofFloat(Property property, float... fArr) {
            return new FloatKeyframeSet(property, fArr);
        }
    }

    /* renamed from: com.android.systemui.qs.TouchAnimator$FloatKeyframeSet */
    private static class FloatKeyframeSet<T> extends KeyframeSet {
        private final Property<T, Float> mProperty;
        private final float[] mValues;

        public FloatKeyframeSet(Property<T, Float> property, float[] fArr) {
            super(fArr.length);
            this.mProperty = property;
            this.mValues = fArr;
        }

        /* access modifiers changed from: protected */
        public void interpolate(int i, float f, Object obj) {
            float[] fArr = this.mValues;
            float f2 = fArr[i - 1];
            this.mProperty.set(obj, Float.valueOf(f2 + ((fArr[i] - f2) * f)));
        }
    }

    /* renamed from: com.android.systemui.qs.TouchAnimator$IntKeyframeSet */
    private static class IntKeyframeSet<T> extends KeyframeSet {
        private final Property<T, Integer> mProperty;
        private final int[] mValues;

        public IntKeyframeSet(Property<T, Integer> property, int[] iArr) {
            super(iArr.length);
            this.mProperty = property;
            this.mValues = iArr;
        }

        /* access modifiers changed from: protected */
        public void interpolate(int i, float f, Object obj) {
            int[] iArr = this.mValues;
            int i2 = iArr[i - 1];
            this.mProperty.set(obj, Integer.valueOf((int) (((float) i2) + (((float) (iArr[i] - i2)) * f))));
        }
    }
}
