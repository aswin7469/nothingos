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
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: TextInterpolator.kt */
/* loaded from: classes.dex */
public final class TextInterpolator {
    @NotNull
    private final TextPaint basePaint;
    @NotNull
    private Layout layout;
    @NotNull
    private List<Line> lines;
    private float progress;
    @NotNull
    private final TextPaint targetPaint;
    @NotNull
    private final FontInterpolator fontInterpolator = new FontInterpolator();
    @NotNull
    private final TextPaint tmpDrawPaint = new TextPaint();
    @NotNull
    private float[] tmpPositionArray = new float[20];

    public TextInterpolator(@NotNull Layout layout) {
        List<Line> emptyList;
        Intrinsics.checkNotNullParameter(layout, "layout");
        this.basePaint = new TextPaint(layout.getPaint());
        this.targetPaint = new TextPaint(layout.getPaint());
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        this.lines = emptyList;
        this.layout = layout;
        shapeText(layout);
    }

    @NotNull
    public final TextPaint getTargetPaint() {
        return this.targetPaint;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TextInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class FontRun {
        @NotNull
        private Font baseFont;
        private final int end;
        private final int start;
        @NotNull
        private Font targetFont;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FontRun)) {
                return false;
            }
            FontRun fontRun = (FontRun) obj;
            return this.start == fontRun.start && this.end == fontRun.end && Intrinsics.areEqual(this.baseFont, fontRun.baseFont) && Intrinsics.areEqual(this.targetFont, fontRun.targetFont);
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.start) * 31) + Integer.hashCode(this.end)) * 31) + this.baseFont.hashCode()) * 31) + this.targetFont.hashCode();
        }

        @NotNull
        public String toString() {
            return "FontRun(start=" + this.start + ", end=" + this.end + ", baseFont=" + this.baseFont + ", targetFont=" + this.targetFont + ')';
        }

        public FontRun(int i, int i2, @NotNull Font baseFont, @NotNull Font targetFont) {
            Intrinsics.checkNotNullParameter(baseFont, "baseFont");
            Intrinsics.checkNotNullParameter(targetFont, "targetFont");
            this.start = i;
            this.end = i2;
            this.baseFont = baseFont;
            this.targetFont = targetFont;
        }

        public final int getStart() {
            return this.start;
        }

        public final int getEnd() {
            return this.end;
        }

        @NotNull
        public final Font getBaseFont() {
            return this.baseFont;
        }

        public final void setBaseFont(@NotNull Font font) {
            Intrinsics.checkNotNullParameter(font, "<set-?>");
            this.baseFont = font;
        }

        @NotNull
        public final Font getTargetFont() {
            return this.targetFont;
        }

        public final void setTargetFont(@NotNull Font font) {
            Intrinsics.checkNotNullParameter(font, "<set-?>");
            this.targetFont = font;
        }

        public final int getLength() {
            return this.end - this.start;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TextInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class Run {
        @NotNull
        private final float[] baseX;
        @NotNull
        private final float[] baseY;
        @NotNull
        private final List<FontRun> fontRuns;
        @NotNull
        private final int[] glyphIds;
        @NotNull
        private final float[] targetX;
        @NotNull
        private final float[] targetY;

        public Run(@NotNull int[] glyphIds, @NotNull float[] baseX, @NotNull float[] baseY, @NotNull float[] targetX, @NotNull float[] targetY, @NotNull List<FontRun> fontRuns) {
            Intrinsics.checkNotNullParameter(glyphIds, "glyphIds");
            Intrinsics.checkNotNullParameter(baseX, "baseX");
            Intrinsics.checkNotNullParameter(baseY, "baseY");
            Intrinsics.checkNotNullParameter(targetX, "targetX");
            Intrinsics.checkNotNullParameter(targetY, "targetY");
            Intrinsics.checkNotNullParameter(fontRuns, "fontRuns");
            this.glyphIds = glyphIds;
            this.baseX = baseX;
            this.baseY = baseY;
            this.targetX = targetX;
            this.targetY = targetY;
            this.fontRuns = fontRuns;
        }

        @NotNull
        public final int[] getGlyphIds() {
            return this.glyphIds;
        }

        @NotNull
        public final float[] getBaseX() {
            return this.baseX;
        }

        @NotNull
        public final float[] getBaseY() {
            return this.baseY;
        }

        @NotNull
        public final float[] getTargetX() {
            return this.targetX;
        }

        @NotNull
        public final float[] getTargetY() {
            return this.targetY;
        }

        @NotNull
        public final List<FontRun> getFontRuns() {
            return this.fontRuns;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TextInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class Line {
        @NotNull
        private final List<Run> runs;

        public Line(@NotNull List<Run> runs) {
            Intrinsics.checkNotNullParameter(runs, "runs");
            this.runs = runs;
        }

        @NotNull
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

    @NotNull
    public final Layout getLayout() {
        return this.layout;
    }

    public final void setLayout(@NotNull Layout value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.layout = value;
        shapeText(value);
    }

    public final void onTargetPaintModified() {
        updatePositionsAndFonts(shapeText(getLayout(), this.targetPaint), false);
    }

    public final void rebase() {
        float f = this.progress;
        boolean z = true;
        if (f == 0.0f) {
            return;
        }
        if (f != 1.0f) {
            z = false;
        }
        if (z) {
            this.basePaint.set(this.targetPaint);
        } else {
            lerp(this.basePaint, this.targetPaint, f, this.tmpDrawPaint);
            this.basePaint.set(this.tmpDrawPaint);
        }
        for (Line line : this.lines) {
            for (Run run : line.getRuns()) {
                int length = run.getBaseX().length - 1;
                if (length >= 0) {
                    int i = 0;
                    while (true) {
                        int i2 = i + 1;
                        run.getBaseX()[i] = MathUtils.lerp(run.getBaseX()[i], run.getTargetX()[i], getProgress());
                        run.getBaseY()[i] = MathUtils.lerp(run.getBaseY()[i], run.getTargetY()[i], getProgress());
                        if (i2 > length) {
                            break;
                        }
                        i = i2;
                    }
                }
                for (FontRun fontRun : run.getFontRuns()) {
                    fontRun.setBaseFont(this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), getProgress()));
                }
            }
        }
        this.progress = 0.0f;
    }

    public final void draw(@NotNull Canvas canvas) {
        float drawOrigin;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        lerp(this.basePaint, this.targetPaint, this.progress, this.tmpDrawPaint);
        int i = 0;
        for (Object obj : this.lines) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            for (Run run : ((Line) obj).getRuns()) {
                canvas.save();
                try {
                    drawOrigin = TextInterpolatorKt.getDrawOrigin(getLayout(), i);
                    canvas.translate(drawOrigin, getLayout().getLineBaseline(i));
                    for (FontRun fontRun : run.getFontRuns()) {
                        drawFontRun(canvas, run, fontRun, this.tmpDrawPaint);
                    }
                } finally {
                    canvas.restore();
                }
            }
            i = i2;
        }
    }

    private final void shapeText(Layout layout) {
        int collectionSizeOrDefault;
        int collectionSizeOrDefault2;
        int collectionSizeOrDefault3;
        int collectionSizeOrDefault4;
        float[] fArr;
        float[] fArr2;
        ArrayList arrayList;
        float[] fArr3;
        Iterator it;
        Iterator it2;
        ArrayList arrayList2;
        float[] fArr4;
        int[] iArr;
        int i;
        Font baseFont;
        int i2;
        int i3;
        PositionedGlyphs positionedGlyphs;
        TextInterpolator textInterpolator = this;
        List<List<PositionedGlyphs>> shapeText = textInterpolator.shapeText(layout, textInterpolator.basePaint);
        List<List<PositionedGlyphs>> shapeText2 = textInterpolator.shapeText(layout, textInterpolator.targetPaint);
        if (!(shapeText.size() == shapeText2.size())) {
            throw new IllegalArgumentException("The new layout result has different line count.".toString());
        }
        Iterator it3 = shapeText.iterator();
        Iterator it4 = shapeText2.iterator();
        int i4 = 10;
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(shapeText, 10);
        collectionSizeOrDefault2 = CollectionsKt__IterablesKt.collectionSizeOrDefault(shapeText2, 10);
        ArrayList arrayList3 = new ArrayList(Math.min(collectionSizeOrDefault, collectionSizeOrDefault2));
        int i5 = 0;
        while (it3.hasNext() && it4.hasNext()) {
            Object next = it3.next();
            List list = (List) it4.next();
            List list2 = (List) next;
            Iterator it5 = list2.iterator();
            Iterator it6 = list.iterator();
            collectionSizeOrDefault3 = CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, i4);
            collectionSizeOrDefault4 = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, i4);
            ArrayList arrayList4 = new ArrayList(Math.min(collectionSizeOrDefault3, collectionSizeOrDefault4));
            while (it5.hasNext() && it6.hasNext()) {
                Object next2 = it5.next();
                PositionedGlyphs positionedGlyphs2 = (PositionedGlyphs) it6.next();
                PositionedGlyphs positionedGlyphs3 = (PositionedGlyphs) next2;
                if (!(positionedGlyphs3.glyphCount() == positionedGlyphs2.glyphCount())) {
                    throw new IllegalArgumentException(Intrinsics.stringPlus("Inconsistent glyph count at line ", Integer.valueOf(textInterpolator.lines.size())).toString());
                }
                int glyphCount = positionedGlyphs3.glyphCount();
                int[] iArr2 = new int[glyphCount];
                for (int i6 = 0; i6 < glyphCount; i6++) {
                    int glyphId = positionedGlyphs3.getGlyphId(i6);
                    if (!(glyphId == positionedGlyphs2.getGlyphId(i6))) {
                        throw new IllegalArgumentException(("Inconsistent glyph ID at " + i6 + " in line " + textInterpolator.lines.size()).toString());
                    }
                    Unit unit = Unit.INSTANCE;
                    iArr2[i6] = glyphId;
                }
                float[] fArr5 = new float[glyphCount];
                for (int i7 = 0; i7 < glyphCount; i7++) {
                    fArr5[i7] = positionedGlyphs3.getGlyphX(i7);
                }
                float[] fArr6 = new float[glyphCount];
                for (int i8 = 0; i8 < glyphCount; i8++) {
                    fArr6[i8] = positionedGlyphs3.getGlyphY(i8);
                }
                float[] fArr7 = new float[glyphCount];
                for (int i9 = 0; i9 < glyphCount; i9++) {
                    fArr7[i9] = positionedGlyphs2.getGlyphX(i9);
                }
                float[] fArr8 = new float[glyphCount];
                int i10 = i5;
                for (int i11 = 0; i11 < glyphCount; i11++) {
                    fArr8[i11] = positionedGlyphs2.getGlyphY(i11);
                }
                ArrayList arrayList5 = new ArrayList();
                Iterator it7 = it3;
                Iterator it8 = it4;
                if (glyphCount != 0) {
                    Font baseFont2 = positionedGlyphs3.getFont(0);
                    it = it5;
                    Font targetFont = positionedGlyphs2.getFont(0);
                    FontInterpolator.Companion companion = FontInterpolator.Companion;
                    it2 = it6;
                    Intrinsics.checkNotNullExpressionValue(baseFont2, "baseFont");
                    fArr4 = fArr7;
                    Intrinsics.checkNotNullExpressionValue(targetFont, "targetFont");
                    arrayList = arrayList3;
                    arrayList2 = arrayList4;
                    if (!companion.canInterpolate(baseFont2, targetFont)) {
                        throw new IllegalArgumentException(("Cannot interpolate font at 0 (" + baseFont2 + " vs " + targetFont + ')').toString());
                    }
                    if (1 < glyphCount) {
                        fArr2 = fArr8;
                        fArr3 = fArr6;
                        Font targetFont2 = targetFont;
                        int i12 = 0;
                        int i13 = 1;
                        Font baseFont3 = baseFont2;
                        i2 = i10;
                        while (true) {
                            fArr = fArr5;
                            int i14 = i13 + 1;
                            iArr = iArr2;
                            Font baseFont4 = positionedGlyphs3.getFont(i13);
                            PositionedGlyphs positionedGlyphs4 = positionedGlyphs3;
                            Font targetFont3 = positionedGlyphs2.getFont(i13);
                            if (baseFont3 != baseFont4) {
                                if (!(targetFont2 != targetFont3)) {
                                    throw new IllegalArgumentException(("Base font has changed at " + i13 + " but target font has not changed.").toString());
                                }
                                positionedGlyphs = positionedGlyphs2;
                                Intrinsics.checkNotNullExpressionValue(baseFont3, "baseFont");
                                Intrinsics.checkNotNullExpressionValue(targetFont2, "targetFont");
                                arrayList5.add(new FontRun(i12, i13, baseFont3, targetFont2));
                                int max = Math.max(i2, i13 - i12);
                                FontInterpolator.Companion companion2 = FontInterpolator.Companion;
                                Intrinsics.checkNotNullExpressionValue(baseFont4, "baseFont");
                                Intrinsics.checkNotNullExpressionValue(targetFont3, "targetFont");
                                if (!companion2.canInterpolate(baseFont4, targetFont3)) {
                                    throw new IllegalArgumentException(("Cannot interpolate font at " + i13 + " (" + baseFont4 + " vs " + targetFont3 + ')').toString());
                                }
                                targetFont2 = targetFont3;
                                i2 = max;
                                i12 = i13;
                                baseFont3 = baseFont4;
                            } else {
                                positionedGlyphs = positionedGlyphs2;
                                if (!(targetFont2 == targetFont3)) {
                                    throw new IllegalArgumentException(("Base font has not changed at " + i13 + " but target font has changed.").toString());
                                }
                            }
                            if (i14 >= glyphCount) {
                                baseFont = baseFont3;
                                i3 = i12;
                                targetFont = targetFont2;
                                break;
                            }
                            i13 = i14;
                            fArr5 = fArr;
                            iArr2 = iArr;
                            positionedGlyphs3 = positionedGlyphs4;
                            positionedGlyphs2 = positionedGlyphs;
                        }
                    } else {
                        fArr = fArr5;
                        fArr2 = fArr8;
                        fArr3 = fArr6;
                        iArr = iArr2;
                        baseFont = baseFont2;
                        i2 = i10;
                        i3 = 0;
                    }
                    Intrinsics.checkNotNullExpressionValue(baseFont, "baseFont");
                    Intrinsics.checkNotNullExpressionValue(targetFont, "targetFont");
                    arrayList5.add(new FontRun(i3, glyphCount, baseFont, targetFont));
                    i = Math.max(i2, glyphCount - i3);
                } else {
                    fArr = fArr5;
                    fArr2 = fArr8;
                    arrayList = arrayList3;
                    fArr3 = fArr6;
                    it = it5;
                    it2 = it6;
                    arrayList2 = arrayList4;
                    fArr4 = fArr7;
                    iArr = iArr2;
                    i = i10;
                }
                Run run = new Run(iArr, fArr, fArr3, fArr4, fArr2, arrayList5);
                ArrayList arrayList6 = arrayList2;
                arrayList6.add(run);
                arrayList4 = arrayList6;
                it3 = it7;
                it4 = it8;
                it5 = it;
                it6 = it2;
                arrayList3 = arrayList;
                i5 = i;
                textInterpolator = this;
            }
            ArrayList arrayList7 = arrayList3;
            arrayList7.add(new Line(arrayList4));
            arrayList3 = arrayList7;
            i5 = i5;
            it3 = it3;
            it4 = it4;
            i4 = 10;
        }
        textInterpolator.lines = arrayList3;
        int i15 = i5 * 2;
        if (textInterpolator.tmpPositionArray.length >= i15) {
            return;
        }
        textInterpolator.tmpPositionArray = new float[i15];
    }

    private final void drawFontRun(Canvas canvas, Run run, FontRun fontRun, Paint paint) {
        int start = fontRun.getStart();
        int end = fontRun.getEnd();
        if (start < end) {
            int i = 0;
            while (true) {
                int i2 = start + 1;
                int i3 = i + 1;
                this.tmpPositionArray[i] = MathUtils.lerp(run.getBaseX()[start], run.getTargetX()[start], this.progress);
                int i4 = i3 + 1;
                this.tmpPositionArray[i3] = MathUtils.lerp(run.getBaseY()[start], run.getTargetY()[start], this.progress);
                if (i2 >= end) {
                    break;
                }
                start = i2;
                i = i4;
            }
        }
        canvas.drawGlyphs(run.getGlyphIds(), fontRun.getStart(), this.tmpPositionArray, 0, fontRun.getLength(), this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), this.progress), paint);
    }

    private final void updatePositionsAndFonts(List<? extends List<PositionedGlyphs>> list, boolean z) {
        int collectionSizeOrDefault;
        int collectionSizeOrDefault2;
        int collectionSizeOrDefault3;
        int collectionSizeOrDefault4;
        if (!(list.size() == this.lines.size())) {
            throw new IllegalStateException("The new layout result has different line count.".toString());
        }
        List<Line> list2 = this.lines;
        Iterator<T> it = list2.iterator();
        Iterator<T> it2 = list.iterator();
        int i = 10;
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10);
        collectionSizeOrDefault2 = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
        ArrayList arrayList = new ArrayList(Math.min(collectionSizeOrDefault, collectionSizeOrDefault2));
        while (it.hasNext() && it2.hasNext()) {
            Object next = it.next();
            List list3 = (List) it2.next();
            List<Run> runs = ((Line) next).getRuns();
            Iterator<T> it3 = runs.iterator();
            Iterator it4 = list3.iterator();
            collectionSizeOrDefault3 = CollectionsKt__IterablesKt.collectionSizeOrDefault(runs, i);
            collectionSizeOrDefault4 = CollectionsKt__IterablesKt.collectionSizeOrDefault(list3, i);
            ArrayList arrayList2 = new ArrayList(Math.min(collectionSizeOrDefault3, collectionSizeOrDefault4));
            while (it3.hasNext() && it4.hasNext()) {
                Object next2 = it3.next();
                PositionedGlyphs positionedGlyphs = (PositionedGlyphs) it4.next();
                Run run = (Run) next2;
                if (!(positionedGlyphs.glyphCount() == run.getGlyphIds().length)) {
                    throw new IllegalArgumentException("The new layout has different glyph count.".toString());
                }
                for (FontRun fontRun : run.getFontRuns()) {
                    Font newFont = positionedGlyphs.getFont(fontRun.getStart());
                    int start = fontRun.getStart();
                    int end = fontRun.getEnd();
                    if (start < end) {
                        while (true) {
                            int i2 = start + 1;
                            if (!(positionedGlyphs.getGlyphId(fontRun.getStart()) == run.getGlyphIds()[fontRun.getStart()])) {
                                throw new IllegalArgumentException(Intrinsics.stringPlus("The new layout has different glyph ID at ", Integer.valueOf(fontRun.getStart())).toString());
                            }
                            if (!(newFont == positionedGlyphs.getFont(start))) {
                                throw new IllegalArgumentException(("The new layout has different font run. " + newFont + " vs " + positionedGlyphs.getFont(start) + " at " + start).toString());
                            } else if (i2 >= end) {
                                break;
                            } else {
                                start = i2;
                            }
                        }
                    }
                    FontInterpolator.Companion companion = FontInterpolator.Companion;
                    Intrinsics.checkNotNullExpressionValue(newFont, "newFont");
                    if (!companion.canInterpolate(newFont, fontRun.getBaseFont())) {
                        throw new IllegalArgumentException(("New font cannot be interpolated with existing font. " + newFont + ", " + fontRun.getBaseFont()).toString());
                    } else if (z) {
                        fontRun.setBaseFont(newFont);
                    } else {
                        fontRun.setTargetFont(newFont);
                    }
                }
                if (z) {
                    int length = run.getBaseX().length - 1;
                    if (length >= 0) {
                        int i3 = 0;
                        while (true) {
                            int i4 = i3 + 1;
                            run.getBaseX()[i3] = positionedGlyphs.getGlyphX(i3);
                            run.getBaseY()[i3] = positionedGlyphs.getGlyphY(i3);
                            if (i4 > length) {
                                break;
                            }
                            i3 = i4;
                        }
                    }
                } else {
                    int length2 = run.getBaseX().length - 1;
                    if (length2 >= 0) {
                        int i5 = 0;
                        while (true) {
                            int i6 = i5 + 1;
                            run.getTargetX()[i5] = positionedGlyphs.getGlyphX(i5);
                            run.getTargetY()[i5] = positionedGlyphs.getGlyphY(i5);
                            if (i6 > length2) {
                                break;
                            }
                            i5 = i6;
                        }
                    }
                }
                arrayList2.add(Unit.INSTANCE);
            }
            arrayList.add(arrayList2);
            i = 10;
        }
    }

    private final void lerp(Paint paint, Paint paint2, float f, Paint paint3) {
        paint3.set(paint);
        paint3.setTextSize(MathUtils.lerp(paint.getTextSize(), paint2.getTextSize(), f));
        paint3.setColor(ColorUtils.blendARGB(paint.getColor(), paint2.getColor(), f));
    }

    private final List<List<PositionedGlyphs>> shapeText(Layout layout, TextPaint textPaint) {
        ArrayList arrayList = new ArrayList();
        int lineCount = layout.getLineCount();
        if (lineCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                int lineStart = layout.getLineStart(i);
                int lineEnd = layout.getLineEnd(i) - lineStart;
                final ArrayList arrayList2 = new ArrayList();
                TextShaper.shapeText(layout.getText(), lineStart, lineEnd, layout.getTextDirectionHeuristic(), textPaint, new TextShaper.GlyphsConsumer() { // from class: com.android.keyguard.TextInterpolator$shapeText$3
                    @Override // android.text.TextShaper.GlyphsConsumer
                    public final void accept(int i3, int i4, PositionedGlyphs glyphs, TextPaint textPaint2) {
                        List<PositionedGlyphs> list = arrayList2;
                        Intrinsics.checkNotNullExpressionValue(glyphs, "glyphs");
                        list.add(glyphs);
                    }
                });
                arrayList.add(arrayList2);
                if (i2 >= lineCount) {
                    break;
                }
                i = i2;
            }
        }
        return arrayList;
    }
}
