package android.telephony;

import java.util.function.ToIntFunction;
/* loaded from: classes3.dex */
public final /* synthetic */ class SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda1 implements ToIntFunction {
    public static final /* synthetic */ SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda1 INSTANCE = new SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda1();

    private /* synthetic */ SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        return ((SignalThresholdInfo) obj).getRadioAccessNetworkType();
    }
}
