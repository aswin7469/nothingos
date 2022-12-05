package android.animation;

import java.util.List;
/* loaded from: classes.dex */
public interface Keyframes extends Cloneable {

    /* loaded from: classes.dex */
    public interface FloatKeyframes extends Keyframes {
        float getFloatValue(float f);
    }

    /* loaded from: classes.dex */
    public interface IntKeyframes extends Keyframes {
        int getIntValue(float f);
    }

    /* renamed from: clone */
    Keyframes mo45clone();

    /* renamed from: getKeyframes */
    List<Keyframe> mo46getKeyframes();

    Class getType();

    Object getValue(float f);

    void setEvaluator(TypeEvaluator typeEvaluator);
}
