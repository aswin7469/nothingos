package android.media;

import android.media.audiopolicy.AudioMixingRule;
import java.util.function.ToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class AudioPlaybackCaptureConfiguration$$ExternalSyntheticLambda3 implements ToIntFunction {
    public static final /* synthetic */ AudioPlaybackCaptureConfiguration$$ExternalSyntheticLambda3 INSTANCE = new AudioPlaybackCaptureConfiguration$$ExternalSyntheticLambda3();

    private /* synthetic */ AudioPlaybackCaptureConfiguration$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int intProp;
        intProp = ((AudioMixingRule.AudioMixMatchCriterion) obj).getIntProp();
        return intProp;
    }
}
