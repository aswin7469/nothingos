package com.android.keyguard;

import android.graphics.text.PositionedGlyphs;
import android.text.TextPaint;
import android.text.TextShaper;
import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TextInterpolator$$ExternalSyntheticLambda0 implements TextShaper.GlyphsConsumer {
    public final /* synthetic */ List f$0;

    public /* synthetic */ TextInterpolator$$ExternalSyntheticLambda0(List list) {
        this.f$0 = list;
    }

    public final void accept(int i, int i2, PositionedGlyphs positionedGlyphs, TextPaint textPaint) {
        TextInterpolator.m2319shapeText$lambda24(this.f$0, i, i2, positionedGlyphs, textPaint);
    }
}
