package com.android.keyguard;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.text.PositionedGlyphs;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextShaper;
import android.util.MathUtils;
import com.android.internal.graphics.ColorUtils;
import com.android.keyguard.FontInterpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0003678B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J(\u0010!\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020(H\u0002J(\u0010)\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020(2\u0006\u0010+\u001a\u00020(2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010,\u001a\u00020(H\u0002J\u0006\u0010-\u001a\u00020\u001eJ\u0006\u0010.\u001a\u00020\u001eJ\u0006\u0010/\u001a\u00020\u001eJ\u0010\u00100\u001a\u00020\u001e2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002J$\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002010\u00100\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010'\u001a\u00020\u0006H\u0002J$\u00102\u001a\u00020\u001e2\u0012\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002010\u00100\u00102\u0006\u00104\u001a\u000205H\u0002R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R&\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u00038F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u0004R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\bR\u000e\u0010\u001a\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u0002\n\u0000¨\u00069"}, mo65043d2 = {"Lcom/android/keyguard/TextInterpolator;", "", "layout", "Landroid/text/Layout;", "(Landroid/text/Layout;)V", "basePaint", "Landroid/text/TextPaint;", "getBasePaint", "()Landroid/text/TextPaint;", "fontInterpolator", "Lcom/android/keyguard/FontInterpolator;", "value", "getLayout", "()Landroid/text/Layout;", "setLayout", "lines", "", "Lcom/android/keyguard/TextInterpolator$Line;", "progress", "", "getProgress", "()F", "setProgress", "(F)V", "targetPaint", "getTargetPaint", "tmpDrawPaint", "tmpPositionArray", "", "draw", "", "canvas", "Landroid/graphics/Canvas;", "drawFontRun", "c", "line", "Lcom/android/keyguard/TextInterpolator$Run;", "run", "Lcom/android/keyguard/TextInterpolator$FontRun;", "paint", "Landroid/graphics/Paint;", "lerp", "from", "to", "out", "onBasePaintModified", "onTargetPaintModified", "rebase", "shapeText", "Landroid/graphics/text/PositionedGlyphs;", "updatePositionsAndFonts", "layoutResult", "updateBase", "", "FontRun", "Line", "Run", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TextInterpolator.kt */
public final class TextInterpolator {
    private final TextPaint basePaint;
    private final FontInterpolator fontInterpolator = new FontInterpolator();
    private Layout layout;
    private List<Line> lines = CollectionsKt.emptyList();
    private float progress;
    private final TextPaint targetPaint;
    private final TextPaint tmpDrawPaint = new TextPaint();
    private float[] tmpPositionArray = new float[20];

    public TextInterpolator(Layout layout2) {
        Intrinsics.checkNotNullParameter(layout2, "layout");
        this.basePaint = new TextPaint(layout2.getPaint());
        this.targetPaint = new TextPaint(layout2.getPaint());
        this.layout = layout2;
        shapeText(layout2);
    }

    public final TextPaint getBasePaint() {
        return this.basePaint;
    }

    public final TextPaint getTargetPaint() {
        return this.targetPaint;
    }

    @Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0006HÆ\u0003J1\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u001a\u0010\u0007\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\n\"\u0004\b\u0013\u0010\f¨\u0006\u001f"}, mo65043d2 = {"Lcom/android/keyguard/TextInterpolator$FontRun;", "", "start", "", "end", "baseFont", "Landroid/graphics/fonts/Font;", "targetFont", "(IILandroid/graphics/fonts/Font;Landroid/graphics/fonts/Font;)V", "getBaseFont", "()Landroid/graphics/fonts/Font;", "setBaseFont", "(Landroid/graphics/fonts/Font;)V", "getEnd", "()I", "length", "getLength", "getStart", "getTargetFont", "setTargetFont", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: TextInterpolator.kt */
    private static final class FontRun {
        private Font baseFont;
        private final int end;
        private final int start;
        private Font targetFont;

        public static /* synthetic */ FontRun copy$default(FontRun fontRun, int i, int i2, Font font, Font font2, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                i = fontRun.start;
            }
            if ((i3 & 2) != 0) {
                i2 = fontRun.end;
            }
            if ((i3 & 4) != 0) {
                font = fontRun.baseFont;
            }
            if ((i3 & 8) != 0) {
                font2 = fontRun.targetFont;
            }
            return fontRun.copy(i, i2, font, font2);
        }

        public final int component1() {
            return this.start;
        }

        public final int component2() {
            return this.end;
        }

        public final Font component3() {
            return this.baseFont;
        }

        public final Font component4() {
            return this.targetFont;
        }

        public final FontRun copy(int i, int i2, Font font, Font font2) {
            Intrinsics.checkNotNullParameter(font, "baseFont");
            Intrinsics.checkNotNullParameter(font2, "targetFont");
            return new FontRun(i, i2, font, font2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FontRun)) {
                return false;
            }
            FontRun fontRun = (FontRun) obj;
            return this.start == fontRun.start && this.end == fontRun.end && Intrinsics.areEqual((Object) this.baseFont, (Object) fontRun.baseFont) && Intrinsics.areEqual((Object) this.targetFont, (Object) fontRun.targetFont);
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.start) * 31) + Integer.hashCode(this.end)) * 31) + this.baseFont.hashCode()) * 31) + this.targetFont.hashCode();
        }

        public String toString() {
            return "FontRun(start=" + this.start + ", end=" + this.end + ", baseFont=" + this.baseFont + ", targetFont=" + this.targetFont + ')';
        }

        public FontRun(int i, int i2, Font font, Font font2) {
            Intrinsics.checkNotNullParameter(font, "baseFont");
            Intrinsics.checkNotNullParameter(font2, "targetFont");
            this.start = i;
            this.end = i2;
            this.baseFont = font;
            this.targetFont = font2;
        }

        public final int getStart() {
            return this.start;
        }

        public final int getEnd() {
            return this.end;
        }

        public final Font getBaseFont() {
            return this.baseFont;
        }

        public final void setBaseFont(Font font) {
            Intrinsics.checkNotNullParameter(font, "<set-?>");
            this.baseFont = font;
        }

        public final Font getTargetFont() {
            return this.targetFont;
        }

        public final void setTargetFont(Font font) {
            Intrinsics.checkNotNullParameter(font, "<set-?>");
            this.targetFont = font;
        }

        public final int getLength() {
            return this.end - this.start;
        }
    }

    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0002\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\u0002\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000e¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/keyguard/TextInterpolator$Run;", "", "glyphIds", "", "baseX", "", "baseY", "targetX", "targetY", "fontRuns", "", "Lcom/android/keyguard/TextInterpolator$FontRun;", "([I[F[F[F[FLjava/util/List;)V", "getBaseX", "()[F", "getBaseY", "getFontRuns", "()Ljava/util/List;", "getGlyphIds", "()[I", "getTargetX", "getTargetY", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: TextInterpolator.kt */
    private static final class Run {
        private final float[] baseX;
        private final float[] baseY;
        private final List<FontRun> fontRuns;
        private final int[] glyphIds;
        private final float[] targetX;
        private final float[] targetY;

        public Run(int[] iArr, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, List<FontRun> list) {
            Intrinsics.checkNotNullParameter(iArr, "glyphIds");
            Intrinsics.checkNotNullParameter(fArr, "baseX");
            Intrinsics.checkNotNullParameter(fArr2, "baseY");
            Intrinsics.checkNotNullParameter(fArr3, "targetX");
            Intrinsics.checkNotNullParameter(fArr4, "targetY");
            Intrinsics.checkNotNullParameter(list, "fontRuns");
            this.glyphIds = iArr;
            this.baseX = fArr;
            this.baseY = fArr2;
            this.targetX = fArr3;
            this.targetY = fArr4;
            this.fontRuns = list;
        }

        public final int[] getGlyphIds() {
            return this.glyphIds;
        }

        public final float[] getBaseX() {
            return this.baseX;
        }

        public final float[] getBaseY() {
            return this.baseY;
        }

        public final float[] getTargetX() {
            return this.targetX;
        }

        public final float[] getTargetY() {
            return this.targetY;
        }

        public final List<FontRun> getFontRuns() {
            return this.fontRuns;
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo65043d2 = {"Lcom/android/keyguard/TextInterpolator$Line;", "", "runs", "", "Lcom/android/keyguard/TextInterpolator$Run;", "(Ljava/util/List;)V", "getRuns", "()Ljava/util/List;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: TextInterpolator.kt */
    private static final class Line {
        private final List<Run> runs;

        public Line(List<Run> list) {
            Intrinsics.checkNotNullParameter(list, "runs");
            this.runs = list;
        }

        public final List<Run> getRuns() {
            return this.runs;
        }
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
    }

    public final Layout getLayout() {
        return this.layout;
    }

    public final void setLayout(Layout layout2) {
        Intrinsics.checkNotNullParameter(layout2, "value");
        this.layout = layout2;
        shapeText(layout2);
    }

    public final void onTargetPaintModified() {
        updatePositionsAndFonts(shapeText(getLayout(), this.targetPaint), false);
    }

    public final void onBasePaintModified() {
        updatePositionsAndFonts(shapeText(getLayout(), this.basePaint), true);
    }

    public final void rebase() {
        float f = this.progress;
        boolean z = true;
        if (!(f == 0.0f)) {
            if (f != 1.0f) {
                z = false;
            }
            if (z) {
                this.basePaint.set(this.targetPaint);
            } else {
                lerp(this.basePaint, this.targetPaint, f, this.tmpDrawPaint);
                this.basePaint.set(this.tmpDrawPaint);
            }
            for (Line runs : this.lines) {
                for (Run run : runs.getRuns()) {
                    int length = run.getBaseX().length;
                    for (int i = 0; i < length; i++) {
                        run.getBaseX()[i] = MathUtils.lerp(run.getBaseX()[i], run.getTargetX()[i], this.progress);
                        run.getBaseY()[i] = MathUtils.lerp(run.getBaseY()[i], run.getTargetY()[i], this.progress);
                    }
                    for (FontRun fontRun : run.getFontRuns()) {
                        fontRun.setBaseFont(this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), this.progress));
                    }
                }
            }
            this.progress = 0.0f;
        }
    }

    public final void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        lerp(this.basePaint, this.targetPaint, this.progress, this.tmpDrawPaint);
        int i = 0;
        for (Object next : this.lines) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            for (Run run : ((Line) next).getRuns()) {
                canvas.save();
                try {
                    canvas.translate(TextInterpolatorKt.getDrawOrigin(getLayout(), i), (float) getLayout().getLineBaseline(i));
                    for (FontRun drawFontRun : run.getFontRuns()) {
                        drawFontRun(canvas, run, drawFontRun, this.tmpDrawPaint);
                    }
                } finally {
                    canvas.restore();
                }
            }
            i = i2;
        }
    }

    private final void shapeText(Layout layout2) {
        ArrayList arrayList;
        ArrayList arrayList2;
        Iterator it;
        Iterator it2;
        float[] fArr;
        float[] fArr2;
        int i;
        PositionedGlyphs positionedGlyphs;
        TextInterpolator textInterpolator = this;
        Layout layout3 = layout2;
        List<List<PositionedGlyphs>> shapeText = textInterpolator.shapeText(layout3, textInterpolator.basePaint);
        List<List<PositionedGlyphs>> shapeText2 = textInterpolator.shapeText(layout3, textInterpolator.targetPaint);
        if (shapeText.size() == shapeText2.size()) {
            Iterable iterable = shapeText;
            Iterator it3 = iterable.iterator();
            Iterable iterable2 = shapeText2;
            Iterator it4 = iterable2.iterator();
            int i2 = 10;
            ArrayList arrayList3 = new ArrayList(Math.min(CollectionsKt.collectionSizeOrDefault(iterable, 10), CollectionsKt.collectionSizeOrDefault(iterable2, 10)));
            int i3 = 0;
            while (it3.hasNext() && it4.hasNext()) {
                Iterable iterable3 = (List) it3.next();
                Iterator it5 = iterable3.iterator();
                Iterable iterable4 = (List) it4.next();
                Iterator it6 = iterable4.iterator();
                ArrayList arrayList4 = new ArrayList(Math.min(CollectionsKt.collectionSizeOrDefault(iterable3, i2), CollectionsKt.collectionSizeOrDefault(iterable4, i2)));
                while (it5.hasNext() && it6.hasNext()) {
                    Object next = it5.next();
                    PositionedGlyphs positionedGlyphs2 = (PositionedGlyphs) it6.next();
                    PositionedGlyphs positionedGlyphs3 = (PositionedGlyphs) next;
                    if (positionedGlyphs3.glyphCount() == positionedGlyphs2.glyphCount()) {
                        int glyphCount = positionedGlyphs3.glyphCount();
                        int[] iArr = new int[glyphCount];
                        int i4 = 0;
                        while (i4 < glyphCount) {
                            int glyphId = positionedGlyphs3.getGlyphId(i4);
                            if (glyphId == positionedGlyphs2.getGlyphId(i4)) {
                                Unit unit = Unit.INSTANCE;
                                iArr[i4] = glyphId;
                                i4++;
                            } else {
                                throw new IllegalArgumentException(("Inconsistent glyph ID at " + i4 + " in line " + textInterpolator.lines.size()).toString());
                            }
                        }
                        float[] fArr3 = new float[glyphCount];
                        for (int i5 = 0; i5 < glyphCount; i5++) {
                            fArr3[i5] = positionedGlyphs3.getGlyphX(i5);
                        }
                        float[] fArr4 = new float[glyphCount];
                        for (int i6 = 0; i6 < glyphCount; i6++) {
                            fArr4[i6] = positionedGlyphs3.getGlyphY(i6);
                        }
                        float[] fArr5 = new float[glyphCount];
                        for (int i7 = 0; i7 < glyphCount; i7++) {
                            fArr5[i7] = positionedGlyphs2.getGlyphX(i7);
                        }
                        float[] fArr6 = new float[glyphCount];
                        int i8 = i3;
                        for (int i9 = 0; i9 < glyphCount; i9++) {
                            fArr6[i9] = positionedGlyphs2.getGlyphY(i9);
                        }
                        List arrayList5 = new ArrayList();
                        Iterator it7 = it3;
                        Iterator it8 = it4;
                        if (glyphCount != 0) {
                            Font font = positionedGlyphs3.getFont(0);
                            it2 = it5;
                            Font font2 = positionedGlyphs2.getFont(0);
                            FontInterpolator.Companion companion = FontInterpolator.Companion;
                            it = it6;
                            Intrinsics.checkNotNullExpressionValue(font, "baseFont");
                            fArr2 = fArr5;
                            Intrinsics.checkNotNullExpressionValue(font2, "targetFont");
                            arrayList2 = arrayList3;
                            if (companion.canInterpolate(font, font2)) {
                                arrayList = arrayList4;
                                int i10 = i8;
                                int i11 = 1;
                                int i12 = 0;
                                while (i11 < glyphCount) {
                                    float[] fArr7 = fArr6;
                                    Font font3 = positionedGlyphs3.getFont(i11);
                                    PositionedGlyphs positionedGlyphs4 = positionedGlyphs3;
                                    Font font4 = positionedGlyphs2.getFont(i11);
                                    if (font != font3) {
                                        if (font2 != font4) {
                                            positionedGlyphs = positionedGlyphs2;
                                            arrayList5.add(new FontRun(i12, i11, font, font2));
                                            i10 = Math.max(i10, i11 - i12);
                                            FontInterpolator.Companion companion2 = FontInterpolator.Companion;
                                            Intrinsics.checkNotNullExpressionValue(font3, "baseFont");
                                            Intrinsics.checkNotNullExpressionValue(font4, "targetFont");
                                            if (companion2.canInterpolate(font3, font4)) {
                                                i12 = i11;
                                                font2 = font4;
                                                font = font3;
                                            } else {
                                                throw new IllegalArgumentException(("Cannot interpolate font at " + i11 + " (" + font3 + " vs " + font4 + ')').toString());
                                            }
                                        } else {
                                            throw new IllegalArgumentException(("Base font has changed at " + i11 + " but target font has not changed.").toString());
                                        }
                                    } else {
                                        positionedGlyphs = positionedGlyphs2;
                                        if (!(font2 == font4)) {
                                            throw new IllegalArgumentException(("Base font has not changed at " + i11 + " but target font has changed.").toString());
                                        }
                                    }
                                    i11++;
                                    fArr6 = fArr7;
                                    positionedGlyphs3 = positionedGlyphs4;
                                    positionedGlyphs2 = positionedGlyphs;
                                }
                                fArr = fArr6;
                                arrayList5.add(new FontRun(i12, glyphCount, font, font2));
                                i = Math.max(i10, glyphCount - i12);
                            } else {
                                throw new IllegalArgumentException(("Cannot interpolate font at 0 (" + font + " vs " + font2 + ')').toString());
                            }
                        } else {
                            fArr = fArr6;
                            arrayList2 = arrayList3;
                            it2 = it5;
                            it = it6;
                            arrayList = arrayList4;
                            fArr2 = fArr5;
                            i = i8;
                        }
                        ArrayList arrayList6 = arrayList;
                        arrayList6.add(new Run(iArr, fArr3, fArr4, fArr2, fArr, arrayList5));
                        arrayList4 = arrayList6;
                        it4 = it8;
                        it3 = it7;
                        it5 = it2;
                        it6 = it;
                        arrayList3 = arrayList2;
                        i3 = i;
                        textInterpolator = this;
                    } else {
                        throw new IllegalArgumentException(("Inconsistent glyph count at line " + this.lines.size()).toString());
                    }
                }
                Iterator it9 = it3;
                ArrayList arrayList7 = arrayList3;
                arrayList7.add(new Line(arrayList4));
                textInterpolator = textInterpolator;
                arrayList3 = arrayList7;
                i3 = i3;
                it4 = it4;
                it3 = it9;
                i2 = 10;
            }
            TextInterpolator textInterpolator2 = textInterpolator;
            textInterpolator2.lines = arrayList3;
            int i13 = i3 * 2;
            if (textInterpolator2.tmpPositionArray.length < i13) {
                textInterpolator2.tmpPositionArray = new float[i13];
                return;
            }
            return;
        }
        throw new IllegalArgumentException("The new layout result has different line count.".toString());
    }

    private final void drawFontRun(Canvas canvas, Run run, FontRun fontRun, Paint paint) {
        int start = fontRun.getStart();
        int end = fontRun.getEnd();
        int i = 0;
        while (start < end) {
            int i2 = i + 1;
            this.tmpPositionArray[i] = MathUtils.lerp(run.getBaseX()[start], run.getTargetX()[start], this.progress);
            this.tmpPositionArray[i2] = MathUtils.lerp(run.getBaseY()[start], run.getTargetY()[start], this.progress);
            start++;
            i = i2 + 1;
        }
        canvas.drawGlyphs(run.getGlyphIds(), fontRun.getStart(), this.tmpPositionArray, 0, fontRun.getLength(), this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), this.progress), paint);
    }

    private final void updatePositionsAndFonts(List<? extends List<PositionedGlyphs>> list, boolean z) {
        if (list.size() == this.lines.size()) {
            Iterable iterable = this.lines;
            Iterator it = iterable.iterator();
            Iterable iterable2 = list;
            Iterator it2 = iterable2.iterator();
            ArrayList arrayList = new ArrayList(Math.min(CollectionsKt.collectionSizeOrDefault(iterable, 10), CollectionsKt.collectionSizeOrDefault(iterable2, 10)));
            while (it.hasNext() && it2.hasNext()) {
                Iterable runs = ((Line) it.next()).getRuns();
                Iterator it3 = runs.iterator();
                Iterable iterable3 = (List) it2.next();
                Iterator it4 = iterable3.iterator();
                ArrayList arrayList2 = new ArrayList(Math.min(CollectionsKt.collectionSizeOrDefault(runs, 10), CollectionsKt.collectionSizeOrDefault(iterable3, 10)));
                while (it3.hasNext() && it4.hasNext()) {
                    Object next = it3.next();
                    PositionedGlyphs positionedGlyphs = (PositionedGlyphs) it4.next();
                    Run run = (Run) next;
                    if (positionedGlyphs.glyphCount() == run.getGlyphIds().length) {
                        for (FontRun fontRun : run.getFontRuns()) {
                            Font font = positionedGlyphs.getFont(fontRun.getStart());
                            int start = fontRun.getStart();
                            int end = fontRun.getEnd();
                            while (start < end) {
                                if (positionedGlyphs.getGlyphId(fontRun.getStart()) == run.getGlyphIds()[fontRun.getStart()]) {
                                    if (font == positionedGlyphs.getFont(start)) {
                                        start++;
                                    } else {
                                        throw new IllegalArgumentException(("The new layout has different font run. " + font + " vs " + positionedGlyphs.getFont(start) + " at " + start).toString());
                                    }
                                } else {
                                    throw new IllegalArgumentException(("The new layout has different glyph ID at " + fontRun.getStart()).toString());
                                }
                            }
                            FontInterpolator.Companion companion = FontInterpolator.Companion;
                            Intrinsics.checkNotNullExpressionValue(font, "newFont");
                            if (!companion.canInterpolate(font, fontRun.getBaseFont())) {
                                throw new IllegalArgumentException(("New font cannot be interpolated with existing font. " + font + ", " + fontRun.getBaseFont()).toString());
                            } else if (z) {
                                fontRun.setBaseFont(font);
                            } else {
                                fontRun.setTargetFont(font);
                            }
                        }
                        if (z) {
                            int length = run.getBaseX().length;
                            for (int i = 0; i < length; i++) {
                                run.getBaseX()[i] = positionedGlyphs.getGlyphX(i);
                                run.getBaseY()[i] = positionedGlyphs.getGlyphY(i);
                            }
                        } else {
                            int length2 = run.getBaseX().length;
                            for (int i2 = 0; i2 < length2; i2++) {
                                run.getTargetX()[i2] = positionedGlyphs.getGlyphX(i2);
                                run.getTargetY()[i2] = positionedGlyphs.getGlyphY(i2);
                            }
                        }
                        arrayList2.add(Unit.INSTANCE);
                    } else {
                        throw new IllegalArgumentException("The new layout has different glyph count.".toString());
                    }
                }
                arrayList.add(arrayList2);
            }
            List list2 = arrayList;
            return;
        }
        throw new IllegalStateException("The new layout result has different line count.".toString());
    }

    private final void lerp(Paint paint, Paint paint2, float f, Paint paint3) {
        paint3.set(paint);
        paint3.setTextSize(MathUtils.lerp(paint.getTextSize(), paint2.getTextSize(), f));
        paint3.setColor(ColorUtils.blendARGB(paint.getColor(), paint2.getColor(), f));
    }

    private final List<List<PositionedGlyphs>> shapeText(Layout layout2, TextPaint textPaint) {
        List<List<PositionedGlyphs>> arrayList = new ArrayList<>();
        int lineCount = layout2.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            int lineStart = layout2.getLineStart(i);
            int lineEnd = layout2.getLineEnd(i) - lineStart;
            List arrayList2 = new ArrayList();
            TextShaper.shapeText(layout2.getText(), lineStart, lineEnd, layout2.getTextDirectionHeuristic(), textPaint, new TextInterpolator$$ExternalSyntheticLambda0(arrayList2));
            arrayList.add(arrayList2);
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    /* renamed from: shapeText$lambda-24  reason: not valid java name */
    public static final void m2319shapeText$lambda24(List list, int i, int i2, PositionedGlyphs positionedGlyphs, TextPaint textPaint) {
        Intrinsics.checkNotNullParameter(list, "$runs");
        Intrinsics.checkNotNullExpressionValue(positionedGlyphs, "glyphs");
        list.add(positionedGlyphs);
    }
}
