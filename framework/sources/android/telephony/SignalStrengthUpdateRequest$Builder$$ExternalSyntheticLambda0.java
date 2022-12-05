package android.telephony;

import java.util.function.Function;
/* loaded from: classes3.dex */
public final /* synthetic */ class SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda0 INSTANCE = new SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda0();

    private /* synthetic */ SignalStrengthUpdateRequest$Builder$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Integer.valueOf(((SignalThresholdInfo) obj).getSignalMeasurementType());
    }
}
