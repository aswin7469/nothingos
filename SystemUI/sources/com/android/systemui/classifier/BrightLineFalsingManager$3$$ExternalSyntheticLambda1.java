package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.BrightLineFalsingManager;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$3$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ BrightLineFalsingManager$3$$ExternalSyntheticLambda1 INSTANCE = new BrightLineFalsingManager$3$$ExternalSyntheticLambda1();

    private /* synthetic */ BrightLineFalsingManager$3$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        BrightLineFalsingManager.XYDt lambda$onGestureFinalized$1;
        lambda$onGestureFinalized$1 = BrightLineFalsingManager.AnonymousClass3.lambda$onGestureFinalized$1((MotionEvent) obj);
        return lambda$onGestureFinalized$1;
    }
}
