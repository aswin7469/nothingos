package androidx.leanback.widget;

import android.animation.PropertyValuesHolder;
import android.util.Property;
import androidx.leanback.widget.Parallax;
import androidx.leanback.widget.ParallaxTarget;
import java.util.ArrayList;
import java.util.List;

public abstract class ParallaxEffect {
    final List<Parallax.PropertyMarkerValue> mMarkerValues = new ArrayList(2);
    final List<ParallaxTarget> mTargets = new ArrayList(4);
    final List<Float> mTotalWeights = new ArrayList(2);
    final List<Float> mWeights = new ArrayList(2);

    /* access modifiers changed from: package-private */
    public abstract Number calculateDirectValue(Parallax parallax);

    /* access modifiers changed from: package-private */
    public abstract float calculateFraction(Parallax parallax);

    ParallaxEffect() {
    }

    public final List<Parallax.PropertyMarkerValue> getPropertyRanges() {
        return this.mMarkerValues;
    }

    public final List<Float> getWeights() {
        return this.mWeights;
    }

    public final void setPropertyRanges(Parallax.PropertyMarkerValue... propertyMarkerValueArr) {
        this.mMarkerValues.clear();
        for (Parallax.PropertyMarkerValue add : propertyMarkerValueArr) {
            this.mMarkerValues.add(add);
        }
    }

    public final void setWeights(float... fArr) {
        int length = fArr.length;
        int i = 0;
        while (true) {
            float f = 0.0f;
            if (i >= length) {
                this.mWeights.clear();
                this.mTotalWeights.clear();
                for (float f2 : fArr) {
                    this.mWeights.add(Float.valueOf(f2));
                    f += f2;
                    this.mTotalWeights.add(Float.valueOf(f));
                }
                return;
            } else if (fArr[i] > 0.0f) {
                i++;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public final ParallaxEffect weights(float... fArr) {
        setWeights(fArr);
        return this;
    }

    public final void addTarget(ParallaxTarget parallaxTarget) {
        this.mTargets.add(parallaxTarget);
    }

    public final ParallaxEffect target(ParallaxTarget parallaxTarget) {
        this.mTargets.add(parallaxTarget);
        return this;
    }

    public final ParallaxEffect target(Object obj, PropertyValuesHolder propertyValuesHolder) {
        this.mTargets.add(new ParallaxTarget.PropertyValuesHolderTarget(obj, propertyValuesHolder));
        return this;
    }

    public final <T, V extends Number> ParallaxEffect target(T t, Property<T, V> property) {
        this.mTargets.add(new ParallaxTarget.DirectPropertyTarget(t, property));
        return this;
    }

    public final List<ParallaxTarget> getTargets() {
        return this.mTargets;
    }

    public final void removeTarget(ParallaxTarget parallaxTarget) {
        this.mTargets.remove((Object) parallaxTarget);
    }

    public final void performMapping(Parallax parallax) {
        if (this.mMarkerValues.size() >= 2) {
            if (this instanceof IntEffect) {
                parallax.verifyIntProperties();
            } else {
                parallax.verifyFloatProperties();
            }
            Number number = null;
            float f = 0.0f;
            boolean z = false;
            for (int i = 0; i < this.mTargets.size(); i++) {
                ParallaxTarget parallaxTarget = this.mTargets.get(i);
                if (parallaxTarget.isDirectMapping()) {
                    if (number == null) {
                        number = calculateDirectValue(parallax);
                    }
                    parallaxTarget.directUpdate(number);
                } else {
                    if (!z) {
                        f = calculateFraction(parallax);
                        z = true;
                    }
                    parallaxTarget.update(f);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final float getFractionWithWeightAdjusted(float f, int i) {
        if (this.mMarkerValues.size() < 3) {
            return f;
        }
        if (this.mWeights.size() == this.mMarkerValues.size() - 1) {
            List<Float> list = this.mTotalWeights;
            float floatValue = list.get(list.size() - 1).floatValue();
            float floatValue2 = (f * this.mWeights.get(i - 1).floatValue()) / floatValue;
            if (i >= 2) {
                return floatValue2 + (this.mTotalWeights.get(i - 2).floatValue() / floatValue);
            }
            return floatValue2;
        }
        float size = (float) (this.mMarkerValues.size() - 1);
        float f2 = f / size;
        return i >= 2 ? f2 + (((float) (i - 1)) / size) : f2;
    }

    static final class IntEffect extends ParallaxEffect {
        IntEffect() {
        }

        /* access modifiers changed from: package-private */
        public Number calculateDirectValue(Parallax parallax) {
            if (this.mMarkerValues.size() != 2) {
                throw new RuntimeException("Must use two marker values for direct mapping");
            } else if (((Parallax.PropertyMarkerValue) this.mMarkerValues.get(0)).getProperty() == ((Parallax.PropertyMarkerValue) this.mMarkerValues.get(1)).getProperty()) {
                int markerValue = ((Parallax.IntPropertyMarkerValue) this.mMarkerValues.get(0)).getMarkerValue(parallax);
                int markerValue2 = ((Parallax.IntPropertyMarkerValue) this.mMarkerValues.get(1)).getMarkerValue(parallax);
                if (markerValue > markerValue2) {
                    int i = markerValue2;
                    markerValue2 = markerValue;
                    markerValue = i;
                }
                Integer num = ((Parallax.IntProperty) ((Parallax.PropertyMarkerValue) this.mMarkerValues.get(0)).getProperty()).get(parallax);
                if (num.intValue() < markerValue) {
                    return Integer.valueOf(markerValue);
                }
                if (num.intValue() > markerValue2) {
                    return Integer.valueOf(markerValue2);
                }
                return num;
            } else {
                throw new RuntimeException("Marker value must use same Property for direct mapping");
            }
        }

        /* access modifiers changed from: package-private */
        public float calculateFraction(Parallax parallax) {
            float f;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            while (i < this.mMarkerValues.size()) {
                Parallax.IntPropertyMarkerValue intPropertyMarkerValue = (Parallax.IntPropertyMarkerValue) this.mMarkerValues.get(i);
                int index = ((Parallax.IntProperty) intPropertyMarkerValue.getProperty()).getIndex();
                int markerValue = intPropertyMarkerValue.getMarkerValue(parallax);
                int intPropertyValue = parallax.getIntPropertyValue(index);
                if (i == 0) {
                    if (intPropertyValue >= markerValue) {
                        return 0.0f;
                    }
                } else if (i2 == index && i3 < markerValue) {
                    throw new IllegalStateException("marker value of same variable must be descendant order");
                } else if (intPropertyValue == Integer.MAX_VALUE) {
                    return getFractionWithWeightAdjusted(((float) (i3 - i4)) / parallax.getMaxValue(), i);
                } else {
                    if (intPropertyValue >= markerValue) {
                        if (i2 != index) {
                            if (i4 != Integer.MIN_VALUE) {
                                i3 += intPropertyValue - i4;
                            } else {
                                f = 1.0f - (((float) (intPropertyValue - markerValue)) / parallax.getMaxValue());
                                return getFractionWithWeightAdjusted(f, i);
                            }
                        }
                        f = ((float) (i3 - intPropertyValue)) / ((float) (i3 - markerValue));
                        return getFractionWithWeightAdjusted(f, i);
                    }
                }
                i++;
                i3 = markerValue;
                i2 = index;
                i4 = intPropertyValue;
            }
            return 1.0f;
        }
    }

    static final class FloatEffect extends ParallaxEffect {
        FloatEffect() {
        }

        /* access modifiers changed from: package-private */
        public Number calculateDirectValue(Parallax parallax) {
            if (this.mMarkerValues.size() != 2) {
                throw new RuntimeException("Must use two marker values for direct mapping");
            } else if (((Parallax.PropertyMarkerValue) this.mMarkerValues.get(0)).getProperty() == ((Parallax.PropertyMarkerValue) this.mMarkerValues.get(1)).getProperty()) {
                float markerValue = ((Parallax.FloatPropertyMarkerValue) this.mMarkerValues.get(0)).getMarkerValue(parallax);
                float markerValue2 = ((Parallax.FloatPropertyMarkerValue) this.mMarkerValues.get(1)).getMarkerValue(parallax);
                if (markerValue > markerValue2) {
                    float f = markerValue2;
                    markerValue2 = markerValue;
                    markerValue = f;
                }
                Float f2 = ((Parallax.FloatProperty) ((Parallax.PropertyMarkerValue) this.mMarkerValues.get(0)).getProperty()).get(parallax);
                if (f2.floatValue() < markerValue) {
                    return Float.valueOf(markerValue);
                }
                if (f2.floatValue() > markerValue2) {
                    return Float.valueOf(markerValue2);
                }
                return f2;
            } else {
                throw new RuntimeException("Marker value must use same Property for direct mapping");
            }
        }

        /* access modifiers changed from: package-private */
        public float calculateFraction(Parallax parallax) {
            float f;
            int i = 0;
            int i2 = 0;
            float f2 = 0.0f;
            float f3 = 0.0f;
            while (i < this.mMarkerValues.size()) {
                Parallax.FloatPropertyMarkerValue floatPropertyMarkerValue = (Parallax.FloatPropertyMarkerValue) this.mMarkerValues.get(i);
                int index = ((Parallax.FloatProperty) floatPropertyMarkerValue.getProperty()).getIndex();
                float markerValue = floatPropertyMarkerValue.getMarkerValue(parallax);
                float floatPropertyValue = parallax.getFloatPropertyValue(index);
                if (i == 0) {
                    if (floatPropertyValue >= markerValue) {
                        return 0.0f;
                    }
                } else if (i2 == index && f2 < markerValue) {
                    throw new IllegalStateException("marker value of same variable must be descendant order");
                } else if (floatPropertyValue == Float.MAX_VALUE) {
                    return getFractionWithWeightAdjusted((f2 - f3) / parallax.getMaxValue(), i);
                } else {
                    if (floatPropertyValue >= markerValue) {
                        if (i2 != index) {
                            if (f3 != -3.4028235E38f) {
                                f2 += floatPropertyValue - f3;
                            } else {
                                f = 1.0f - ((floatPropertyValue - markerValue) / parallax.getMaxValue());
                                return getFractionWithWeightAdjusted(f, i);
                            }
                        }
                        f = (f2 - floatPropertyValue) / (f2 - markerValue);
                        return getFractionWithWeightAdjusted(f, i);
                    }
                }
                i++;
                f2 = markerValue;
                i2 = index;
                f3 = floatPropertyValue;
            }
            return 1.0f;
        }
    }
}
