package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.model.DocumentData;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
/* loaded from: classes.dex */
public class TextKeyframeAnimation extends KeyframeAnimation<DocumentData> {
    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    /* renamed from: getValue  reason: collision with other method in class */
    /* bridge */ /* synthetic */ Object mo191getValue(Keyframe keyframe, float f) {
        return mo191getValue((Keyframe<DocumentData>) keyframe, f);
    }

    public TextKeyframeAnimation(List<Keyframe<DocumentData>> list) {
        super(list);
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    /* renamed from: getValue */
    DocumentData mo191getValue(Keyframe<DocumentData> keyframe, float f) {
        return keyframe.startValue;
    }
}
