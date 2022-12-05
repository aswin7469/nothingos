package com.android.internal.dynamicanimation.animation;

import android.util.FloatProperty;
/* loaded from: classes4.dex */
public abstract class FloatPropertyCompat<T> {
    final String mPropertyName;

    public abstract float getValue(T t);

    public abstract void setValue(T t, float f);

    public FloatPropertyCompat(String name) {
        this.mPropertyName = name;
    }

    public static <T> FloatPropertyCompat<T> createFloatPropertyCompat(final FloatProperty<T> property) {
        return new FloatPropertyCompat<T>(property.getName()) { // from class: com.android.internal.dynamicanimation.animation.FloatPropertyCompat.1
            @Override // com.android.internal.dynamicanimation.animation.FloatPropertyCompat
            public float getValue(T object) {
                return property.get(object).floatValue();
            }

            @Override // com.android.internal.dynamicanimation.animation.FloatPropertyCompat
            public void setValue(T object, float value) {
                property.setValue(object, value);
            }
        };
    }
}
