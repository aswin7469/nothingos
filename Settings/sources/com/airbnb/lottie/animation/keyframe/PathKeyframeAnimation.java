package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;
/* loaded from: classes.dex */
public class PathKeyframeAnimation extends KeyframeAnimation<PointF> {
    private PathKeyframe pathMeasureKeyframe;
    private final PointF point = new PointF();
    private final float[] pos = new float[2];
    private PathMeasure pathMeasure = new PathMeasure();

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    /* renamed from: getValue  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Object mo179getValue(Keyframe keyframe, float f) {
        return mo179getValue((Keyframe<PointF>) keyframe, f);
    }

    public PathKeyframeAnimation(List<? extends Keyframe<PointF>> list) {
        super(list);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    /* renamed from: getValue */
    public PointF mo179getValue(Keyframe<PointF> keyframe, float f) {
        PointF pointF;
        PathKeyframe pathKeyframe = (PathKeyframe) keyframe;
        Path path = pathKeyframe.getPath();
        if (path == null) {
            return keyframe.startValue;
        }
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0 && (pointF = (PointF) lottieValueCallback.getValueInternal(pathKeyframe.startFrame, pathKeyframe.endFrame.floatValue(), pathKeyframe.startValue, pathKeyframe.endValue, getLinearCurrentKeyframeProgress(), f, getProgress())) != null) {
            return pointF;
        }
        if (this.pathMeasureKeyframe != pathKeyframe) {
            this.pathMeasure.setPath(path, false);
            this.pathMeasureKeyframe = pathKeyframe;
        }
        PathMeasure pathMeasure = this.pathMeasure;
        pathMeasure.getPosTan(f * pathMeasure.getLength(), this.pos, null);
        PointF pointF2 = this.point;
        float[] fArr = this.pos;
        pointF2.set(fArr[0], fArr[1]);
        return this.point;
    }
}
