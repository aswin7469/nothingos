package androidx.dynamicanimation.animation;
/* loaded from: classes.dex */
public abstract class FloatPropertyCompat<T> {
    final String mPropertyName;

    public abstract float getValue(T object);

    public abstract void setValue(T object, float value);

    public FloatPropertyCompat(String name) {
        this.mPropertyName = name;
    }
}
