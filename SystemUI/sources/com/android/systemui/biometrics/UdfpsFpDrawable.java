package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/biometrics/UdfpsFpDrawable;", "Lcom/android/systemui/biometrics/UdfpsDrawable;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "draw", "", "canvas", "Landroid/graphics/Canvas;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UdfpsFpDrawable.kt */
public final class UdfpsFpDrawable extends UdfpsDrawable {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UdfpsFpDrawable(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (!isIlluminationShowing()) {
            getFingerprintDrawable().draw(canvas);
        }
    }
}
