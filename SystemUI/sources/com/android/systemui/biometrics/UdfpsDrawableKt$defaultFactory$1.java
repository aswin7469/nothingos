package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.PathParser;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "Landroid/graphics/drawable/ShapeDrawable;", "context", "Landroid/content/Context;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UdfpsDrawable.kt */
final class UdfpsDrawableKt$defaultFactory$1 extends Lambda implements Function1<Context, ShapeDrawable> {
    public static final UdfpsDrawableKt$defaultFactory$1 INSTANCE = new UdfpsDrawableKt$defaultFactory$1();

    UdfpsDrawableKt$defaultFactory$1() {
        super(1);
    }

    public final ShapeDrawable invoke(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        ShapeDrawable shapeDrawable = new ShapeDrawable(new PathShape(PathParser.createPathFromPathData(context.getResources().getString(C1893R.string.config_udfpsIcon)), 72.0f, 72.0f));
        shapeDrawable.mutate();
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setStrokeCap(Paint.Cap.ROUND);
        shapeDrawable.getPaint().setStrokeWidth(3.0f);
        return shapeDrawable;
    }
}
