package android.sysprop;

import java.util.function.Function;
/* loaded from: classes3.dex */
public final /* synthetic */ class TelephonyProperties$$ExternalSyntheticLambda5 implements Function {
    public static final /* synthetic */ TelephonyProperties$$ExternalSyntheticLambda5 INSTANCE = new TelephonyProperties$$ExternalSyntheticLambda5();

    private /* synthetic */ TelephonyProperties$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        String tryParseString;
        tryParseString = TelephonyProperties.tryParseString((String) obj);
        return tryParseString;
    }
}
