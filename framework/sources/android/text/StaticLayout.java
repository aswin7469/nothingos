package android.text;

import android.graphics.Paint;
import android.graphics.text.LineBreaker;
import android.text.Layout;
import android.text.PrecomputedText;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.util.Pools;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.util.Arrays;
/* loaded from: classes3.dex */
public class StaticLayout extends Layout {
    private static final char CHAR_NEW_LINE = '\n';
    private static final int COLUMNS_ELLIPSIZE = 7;
    private static final int COLUMNS_NORMAL = 5;
    private static final int DEFAULT_MAX_LINE_HEIGHT = -1;
    private static final int DESCENT = 2;
    private static final int DIR = 0;
    private static final int DIR_SHIFT = 30;
    private static final int ELLIPSIS_COUNT = 6;
    private static final int ELLIPSIS_START = 5;
    private static final int END_HYPHEN_MASK = 7;
    private static final int EXTRA = 3;
    private static final double EXTRA_ROUNDING = 0.5d;
    private static final int HYPHEN = 4;
    private static final int HYPHEN_MASK = 255;
    private static final int START = 0;
    private static final int START_HYPHEN_BITS_SHIFT = 3;
    private static final int START_HYPHEN_MASK = 24;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final float TAB_INCREMENT = 20.0f;
    private static final int TAB_MASK = 536870912;
    static final String TAG = "StaticLayout";
    private static final int TOP = 1;
    private int mBottomPadding;
    private int mColumns;
    private boolean mEllipsized;
    private int mEllipsizedWidth;
    private int[] mLeftIndents;
    private int mLineCount;
    private Layout.Directions[] mLineDirections;
    private int[] mLines;
    private int mMaxLineHeight;
    private int mMaximumVisibleLineCount;
    private int[] mRightIndents;
    private int mTopPadding;

    /* loaded from: classes3.dex */
    public static final class Builder {
        private static final Pools.SynchronizedPool<Builder> sPool = new Pools.SynchronizedPool<>(3);
        private boolean mAddLastLineLineSpacing;
        private Layout.Alignment mAlignment;
        private int mBreakStrategy;
        private TextUtils.TruncateAt mEllipsize;
        private int mEllipsizedWidth;
        private int mEnd;
        private boolean mFallbackLineSpacing;
        private final Paint.FontMetricsInt mFontMetricsInt = new Paint.FontMetricsInt();
        private int mHyphenationFrequency;
        private boolean mIncludePad;
        private int mJustificationMode;
        private int[] mLeftIndents;
        private int mMaxLines;
        private TextPaint mPaint;
        private int[] mRightIndents;
        private float mSpacingAdd;
        private float mSpacingMult;
        private int mStart;
        private CharSequence mText;
        private TextDirectionHeuristic mTextDir;
        private int mWidth;

        private Builder() {
        }

        public static Builder obtain(CharSequence source, int start, int end, TextPaint paint, int width) {
            Builder b = sPool.acquire();
            if (b == null) {
                b = new Builder();
            }
            b.mText = source;
            b.mStart = start;
            b.mEnd = end;
            b.mPaint = paint;
            b.mWidth = width;
            b.mAlignment = Layout.Alignment.ALIGN_NORMAL;
            b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            b.mSpacingMult = 1.0f;
            b.mSpacingAdd = 0.0f;
            b.mIncludePad = true;
            b.mFallbackLineSpacing = false;
            b.mEllipsizedWidth = width;
            b.mEllipsize = null;
            b.mMaxLines = Integer.MAX_VALUE;
            b.mBreakStrategy = 0;
            b.mHyphenationFrequency = 0;
            b.mJustificationMode = 0;
            return b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void recycle(Builder b) {
            b.mPaint = null;
            b.mText = null;
            b.mLeftIndents = null;
            b.mRightIndents = null;
            sPool.release(b);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void finish() {
            this.mText = null;
            this.mPaint = null;
            this.mLeftIndents = null;
            this.mRightIndents = null;
        }

        public Builder setText(CharSequence source) {
            return setText(source, 0, source.length());
        }

        public Builder setText(CharSequence source, int start, int end) {
            this.mText = source;
            this.mStart = start;
            this.mEnd = end;
            return this;
        }

        public Builder setPaint(TextPaint paint) {
            this.mPaint = paint;
            return this;
        }

        public Builder setWidth(int width) {
            this.mWidth = width;
            if (this.mEllipsize == null) {
                this.mEllipsizedWidth = width;
            }
            return this;
        }

        public Builder setAlignment(Layout.Alignment alignment) {
            this.mAlignment = alignment;
            return this;
        }

        public Builder setTextDirection(TextDirectionHeuristic textDir) {
            this.mTextDir = textDir;
            return this;
        }

        public Builder setLineSpacing(float spacingAdd, float spacingMult) {
            this.mSpacingAdd = spacingAdd;
            this.mSpacingMult = spacingMult;
            return this;
        }

        public Builder setIncludePad(boolean includePad) {
            this.mIncludePad = includePad;
            return this;
        }

        public Builder setUseLineSpacingFromFallbacks(boolean useLineSpacingFromFallbacks) {
            this.mFallbackLineSpacing = useLineSpacingFromFallbacks;
            return this;
        }

        public Builder setEllipsizedWidth(int ellipsizedWidth) {
            this.mEllipsizedWidth = ellipsizedWidth;
            return this;
        }

        public Builder setEllipsize(TextUtils.TruncateAt ellipsize) {
            this.mEllipsize = ellipsize;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.mMaxLines = maxLines;
            return this;
        }

        public Builder setBreakStrategy(int breakStrategy) {
            this.mBreakStrategy = breakStrategy;
            return this;
        }

        public Builder setHyphenationFrequency(int hyphenationFrequency) {
            this.mHyphenationFrequency = hyphenationFrequency;
            return this;
        }

        public Builder setIndents(int[] leftIndents, int[] rightIndents) {
            this.mLeftIndents = leftIndents;
            this.mRightIndents = rightIndents;
            return this;
        }

        public Builder setJustificationMode(int justificationMode) {
            this.mJustificationMode = justificationMode;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setAddLastLineLineSpacing(boolean value) {
            this.mAddLastLineLineSpacing = value;
            return this;
        }

        public StaticLayout build() {
            StaticLayout result = new StaticLayout(this);
            recycle(this);
            return result;
        }
    }

    @Deprecated
    public StaticLayout(CharSequence source, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, 0, source.length(), paint, width, align, spacingmult, spacingadd, includepad);
    }

    @Deprecated
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, bufstart, bufend, paint, outerwidth, align, spacingmult, spacingadd, includepad, null, 0);
    }

    @Deprecated
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        this(source, bufstart, bufend, paint, outerwidth, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd, includepad, ellipsize, ellipsizedWidth, Integer.MAX_VALUE);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    @Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Layout.Alignment align, TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth, int maxLines) {
        super(r1, paint, outerwidth, align, textDir, spacingmult, spacingadd);
        Layout.SpannedEllipsizer ellipsizer;
        if (ellipsize == null) {
            ellipsizer = source;
        } else if (source instanceof Spanned) {
            ellipsizer = new Layout.SpannedEllipsizer(source);
        } else {
            ellipsizer = new Layout.Ellipsizer(source);
        }
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        Builder b = Builder.obtain(source, bufstart, bufend, paint, outerwidth).setAlignment(align).setTextDirection(textDir).setLineSpacing(spacingadd, spacingmult).setIncludePad(includepad).setEllipsizedWidth(ellipsizedWidth).setEllipsize(ellipsize).setMaxLines(maxLines);
        if (ellipsize == null) {
            this.mColumns = 5;
            this.mEllipsizedWidth = outerwidth;
        } else {
            Layout.Ellipsizer e = (Layout.Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = ellipsizedWidth;
            e.mMethod = ellipsize;
            this.mEllipsizedWidth = ellipsizedWidth;
            this.mColumns = 7;
        }
        this.mLineDirections = (Layout.Directions[]) ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(this.mColumns * 2);
        this.mMaximumVisibleLineCount = maxLines;
        generate(b, b.mIncludePad, b.mIncludePad);
        Builder.recycle(b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StaticLayout(CharSequence text) {
        super(text, null, 0, null, 0.0f, 0.0f);
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        this.mColumns = 7;
        this.mLineDirections = (Layout.Directions[]) ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(this.mColumns * 2);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private StaticLayout(Builder b) {
        super(r3, b.mPaint, b.mWidth, b.mAlignment, b.mTextDir, b.mSpacingMult, b.mSpacingAdd);
        Layout.SpannedEllipsizer ellipsizer;
        if (b.mEllipsize == null) {
            ellipsizer = b.mText;
        } else if (b.mText instanceof Spanned) {
            ellipsizer = new Layout.SpannedEllipsizer(b.mText);
        } else {
            ellipsizer = new Layout.Ellipsizer(b.mText);
        }
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        if (b.mEllipsize != null) {
            Layout.Ellipsizer e = (Layout.Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = b.mEllipsizedWidth;
            e.mMethod = b.mEllipsize;
            this.mEllipsizedWidth = b.mEllipsizedWidth;
            this.mColumns = 7;
        } else {
            this.mColumns = 5;
            this.mEllipsizedWidth = b.mWidth;
        }
        this.mLineDirections = (Layout.Directions[]) ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(this.mColumns * 2);
        this.mMaximumVisibleLineCount = b.mMaxLines;
        this.mLeftIndents = b.mLeftIndents;
        this.mRightIndents = b.mRightIndents;
        setJustificationMode(b.mJustificationMode);
        generate(b, b.mIncludePad, b.mIncludePad);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x063d, code lost:
        r8 = r82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x063f, code lost:
        if (r2 == r8) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0641, code lost:
        r11 = r84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x064b, code lost:
        if (r11.charAt(r2 - 1) != '\n') goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x065a, code lost:
        if (r9.mLineCount >= r9.mMaximumVisibleLineCount) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x065d, code lost:
        r12 = android.text.MeasuredParagraph.buildForBidi(r11, r2, r2, r79, null);
        r13 = r80;
        r13.getFontMetricsInt(r6);
        out(r11, r2, r2, r6.ascent, r6.descent, r6.top, r6.bottom, r7, r48, r49, null, null, r6, false, 0, r16, r12, r2, r89, r90, r50, null, r8, r66, r77, 0.0f, r13, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x06b4, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0654, code lost:
        r11 = r84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0325, code lost:
        if (r3 != android.text.TextUtils.TruncateAt.MARQUEE) goto L69;
     */
    /* JADX WARN: Removed duplicated region for block: B:148:0x05e7 A[LOOP:0: B:25:0x0175->B:148:0x05e7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x05d9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x02d8 A[LOOP:3: B:58:0x02d6->B:59:0x02d8, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0335 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x03a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generate(Builder b, boolean includepad, boolean trackpad) {
        int[] indents;
        Paint.FontMetricsInt fm;
        LineBreaker lineBreaker;
        float ellipsizedWidth;
        int lineBreakCapacity;
        TextUtils.TruncateAt ellipsize;
        boolean z;
        Spanned spanned;
        LineBreaker.ParagraphConstraints constraints;
        PrecomputedText.ParagraphInfo[] paragraphInfo;
        TextDirectionHeuristic textDir;
        TextPaint paint;
        int bufEnd;
        int bufStart;
        CharSequence source;
        StaticLayout staticLayout;
        float ellipsizedWidth2;
        TextUtils.TruncateAt ellipsize2;
        Paint.FontMetricsInt fm2;
        int fmAscent;
        TextDirectionHeuristic textDir2;
        TextPaint paint2;
        int bufStart2;
        CharSequence source2;
        int[] chooseHtv;
        int firstWidthLineCount;
        int firstWidth;
        int restWidth;
        LineHeightSpan[] chooseHt;
        float[] variableTabStops;
        float[] stops;
        int breakCount;
        LineBreaker lineBreaker2;
        int lineBreakCapacity2;
        int[] breaks;
        float[] lineWidths;
        float[] ascents;
        float[] descents;
        boolean[] hasTabs;
        int[] hyphenEdits;
        int i;
        int remainingLineCount;
        Spanned spanned2;
        TextUtils.TruncateAt ellipsize3;
        boolean z2;
        TextUtils.TruncateAt ellipsize4;
        float[] variableTabStops2;
        int restWidth2;
        int breakCount2;
        int spanStart;
        int breakIndex;
        MeasuredParagraph measuredPara;
        int ascent;
        int paraEnd;
        Paint.FontMetricsInt fm3;
        int restWidth3;
        StaticLayout staticLayout2 = this;
        CharSequence source3 = b.mText;
        int bufStart3 = b.mStart;
        int bufEnd2 = b.mEnd;
        TextPaint paint3 = b.mPaint;
        int outerWidth = b.mWidth;
        TextDirectionHeuristic textDir3 = b.mTextDir;
        boolean fallbackLineSpacing = b.mFallbackLineSpacing;
        float spacingmult = b.mSpacingMult;
        float spacingadd = b.mSpacingAdd;
        float ellipsizedWidth3 = b.mEllipsizedWidth;
        TextUtils.TruncateAt ellipsize5 = b.mEllipsize;
        boolean addLastLineSpacing = b.mAddLastLineLineSpacing;
        int[] breaks2 = null;
        float[] lineWidths2 = null;
        float[] ascents2 = null;
        float[] descents2 = null;
        boolean[] hasTabs2 = null;
        int[] hyphenEdits2 = null;
        staticLayout2.mLineCount = 0;
        staticLayout2.mEllipsized = false;
        staticLayout2.mMaxLineHeight = staticLayout2.mMaximumVisibleLineCount < 1 ? 0 : -1;
        int v = 0;
        boolean needMultiply = (spacingmult == 1.0f && spacingadd == 0.0f) ? false : true;
        Paint.FontMetricsInt fm4 = b.mFontMetricsInt;
        int[] indents2 = staticLayout2.mLeftIndents;
        if (indents2 != null || staticLayout2.mRightIndents != null) {
            int leftLen = indents2 == null ? 0 : indents2.length;
            int[] iArr = staticLayout2.mRightIndents;
            int rightLen = iArr == null ? 0 : iArr.length;
            int indentsLen = Math.max(leftLen, rightLen);
            int[] indents3 = new int[indentsLen];
            for (int i2 = 0; i2 < leftLen; i2++) {
                indents3[i2] = staticLayout2.mLeftIndents[i2];
            }
            int i3 = 0;
            while (i3 < rightLen) {
                indents3[i3] = indents3[i3] + staticLayout2.mRightIndents[i3];
                i3++;
                leftLen = leftLen;
            }
            indents = indents3;
        } else {
            indents = null;
        }
        LineBreaker lineBreaker3 = new LineBreaker.Builder().setBreakStrategy(b.mBreakStrategy).setHyphenationFrequency(b.mHyphenationFrequency).setJustificationMode(b.mJustificationMode).setIndents(indents).build();
        LineBreaker.ParagraphConstraints constraints2 = new LineBreaker.ParagraphConstraints();
        PrecomputedText.ParagraphInfo[] paragraphInfo2 = null;
        Spanned spanned3 = source3 instanceof Spanned ? (Spanned) source3 : null;
        if (!(source3 instanceof PrecomputedText)) {
            fm = fm4;
            lineBreaker = lineBreaker3;
            ellipsizedWidth = ellipsizedWidth3;
            lineBreakCapacity = 0;
            ellipsize = ellipsize5;
            z = false;
            spanned = spanned3;
            constraints = constraints2;
        } else {
            PrecomputedText precomputed = (PrecomputedText) source3;
            ellipsizedWidth = ellipsizedWidth3;
            spanned = spanned3;
            lineBreakCapacity = 0;
            constraints = constraints2;
            fm = fm4;
            lineBreaker = lineBreaker3;
            ellipsize = ellipsize5;
            z = false;
            int checkResult = precomputed.checkResultUsable(bufStart3, bufEnd2, textDir3, paint3, b.mBreakStrategy, b.mHyphenationFrequency);
            switch (checkResult) {
                case 1:
                    PrecomputedText.Params newParams = new PrecomputedText.Params.Builder(paint3).setBreakStrategy(b.mBreakStrategy).setHyphenationFrequency(b.mHyphenationFrequency).setTextDirection(textDir3).build();
                    paragraphInfo2 = PrecomputedText.create(precomputed, newParams).getParagraphInfo();
                    break;
                case 2:
                    paragraphInfo2 = precomputed.getParagraphInfo();
                    break;
            }
        }
        if (paragraphInfo2 != null) {
            paragraphInfo = paragraphInfo2;
        } else {
            PrecomputedText.Params param = new PrecomputedText.Params(paint3, textDir3, b.mBreakStrategy, b.mHyphenationFrequency);
            paragraphInfo = PrecomputedText.createMeasuredParagraphs(source3, param, bufStart3, bufEnd2, z);
        }
        int fmTop = 0;
        int[] chooseHtv2 = null;
        int lineBreakCapacity3 = lineBreakCapacity;
        while (true) {
            if (fmTop < paragraphInfo.length) {
                int paraStart = fmTop == 0 ? bufStart3 : paragraphInfo[fmTop - 1].paragraphEnd;
                int paraEnd2 = paragraphInfo[fmTop].paragraphEnd;
                int firstWidthLineCount2 = 1;
                int firstWidth2 = outerWidth;
                int restWidth4 = outerWidth;
                if (spanned == null) {
                    textDir2 = textDir3;
                    paint2 = paint3;
                    bufStart2 = bufStart3;
                    source2 = source3;
                    chooseHtv = chooseHtv2;
                    firstWidthLineCount = 1;
                    firstWidth = firstWidth2;
                    restWidth = restWidth4;
                    chooseHt = null;
                } else {
                    LeadingMarginSpan[] sp = (LeadingMarginSpan[]) getParagraphSpans(spanned, paraStart, paraEnd2, LeadingMarginSpan.class);
                    textDir2 = textDir3;
                    int i4 = 0;
                    while (true) {
                        paint2 = paint3;
                        if (i4 < sp.length) {
                            LeadingMarginSpan lms = sp[i4];
                            int bufStart4 = bufStart3;
                            CharSequence source4 = source3;
                            firstWidth2 -= sp[i4].getLeadingMargin(true);
                            restWidth4 -= sp[i4].getLeadingMargin(false);
                            if (lms instanceof LeadingMarginSpan.LeadingMarginSpan2) {
                                LeadingMarginSpan.LeadingMarginSpan2 lms2 = (LeadingMarginSpan.LeadingMarginSpan2) lms;
                                firstWidthLineCount2 = Math.max(firstWidthLineCount2, lms2.getLeadingMarginLineCount());
                            }
                            i4++;
                            paint3 = paint2;
                            bufStart3 = bufStart4;
                            source3 = source4;
                        } else {
                            bufStart2 = bufStart3;
                            source2 = source3;
                            LineHeightSpan[] chooseHt2 = (LineHeightSpan[]) getParagraphSpans(spanned, paraStart, paraEnd2, LineHeightSpan.class);
                            if (chooseHt2.length == 0) {
                                chooseHtv = chooseHtv2;
                                firstWidthLineCount = firstWidthLineCount2;
                                firstWidth = firstWidth2;
                                restWidth = restWidth4;
                                chooseHt = null;
                            } else {
                                if (chooseHtv2 == null || chooseHtv2.length < chooseHt2.length) {
                                    chooseHtv2 = ArrayUtils.newUnpaddedIntArray(chooseHt2.length);
                                }
                                for (int i5 = 0; i5 < chooseHt2.length; i5++) {
                                    int o = spanned.getSpanStart(chooseHt2[i5]);
                                    if (o < paraStart) {
                                        chooseHtv2[i5] = staticLayout2.getLineTop(staticLayout2.getLineForOffset(o));
                                    } else {
                                        chooseHtv2[i5] = v;
                                    }
                                }
                                chooseHtv = chooseHtv2;
                                firstWidthLineCount = firstWidthLineCount2;
                                chooseHt = chooseHt2;
                                firstWidth = firstWidth2;
                                restWidth = restWidth4;
                            }
                        }
                    }
                }
                float[] variableTabStops3 = null;
                if (spanned == null) {
                    variableTabStops = null;
                } else {
                    TabStopSpan[] spans = (TabStopSpan[]) getParagraphSpans(spanned, paraStart, paraEnd2, TabStopSpan.class);
                    if (spans.length <= 0) {
                        variableTabStops = null;
                    } else {
                        stops = new float[spans.length];
                        int i6 = 0;
                        while (true) {
                            float[] variableTabStops4 = variableTabStops3;
                            if (i6 < spans.length) {
                                stops[i6] = spans[i6].getTabStop();
                                i6++;
                                variableTabStops3 = variableTabStops4;
                            } else {
                                Arrays.sort(stops, 0, stops.length);
                                MeasuredParagraph measuredPara2 = paragraphInfo[fmTop].measured;
                                char[] chs = measuredPara2.getChars();
                                int[] spanEndCache = measuredPara2.getSpanEndCache().getRawArray();
                                int[] fmCache = measuredPara2.getFontMetrics().getRawArray();
                                constraints.setWidth(restWidth);
                                constraints.setIndent(firstWidth, firstWidthLineCount);
                                constraints.setTabStops(stops, TAB_INCREMENT);
                                int paraIndex = fmTop;
                                LineBreaker lineBreaker4 = lineBreaker;
                                LineBreaker.Result res = lineBreaker4.computeLineBreaks(measuredPara2.getMeasuredText(), constraints, staticLayout2.mLineCount);
                                breakCount = res.getLineCount();
                                if (lineBreakCapacity3 < breakCount) {
                                    lineBreaker2 = lineBreaker4;
                                    lineBreakCapacity2 = lineBreakCapacity3;
                                    breaks = breaks2;
                                    lineWidths = lineWidths2;
                                    ascents = ascents2;
                                    descents = descents2;
                                    hasTabs = hasTabs2;
                                    hyphenEdits = hyphenEdits2;
                                } else {
                                    lineBreaker2 = lineBreaker4;
                                    int[] breaks3 = new int[breakCount];
                                    float[] lineWidths3 = new float[breakCount];
                                    float[] lineWidths4 = new float[breakCount];
                                    float[] ascents3 = new float[breakCount];
                                    boolean[] hasTabs3 = new boolean[breakCount];
                                    lineBreakCapacity2 = breakCount;
                                    hyphenEdits = new int[breakCount];
                                    breaks = breaks3;
                                    lineWidths = lineWidths3;
                                    ascents = lineWidths4;
                                    descents = ascents3;
                                    hasTabs = hasTabs3;
                                }
                                i = 0;
                                while (i < breakCount) {
                                    breaks[i] = res.getLineBreakOffset(i);
                                    lineWidths[i] = res.getLineWidth(i);
                                    ascents[i] = res.getLineAscent(i);
                                    descents[i] = res.getLineDescent(i);
                                    hasTabs[i] = res.hasLineTab(i);
                                    hyphenEdits[i] = packHyphenEdit(res.getStartLineHyphenEdit(i), res.getEndLineHyphenEdit(i));
                                    i++;
                                    paragraphInfo = paragraphInfo;
                                }
                                PrecomputedText.ParagraphInfo[] paragraphInfo3 = paragraphInfo;
                                int i7 = staticLayout2.mMaximumVisibleLineCount;
                                remainingLineCount = i7 - staticLayout2.mLineCount;
                                if (ellipsize == null) {
                                    ellipsize3 = ellipsize;
                                    if (ellipsize3 != TextUtils.TruncateAt.END) {
                                        spanned2 = spanned;
                                        if (staticLayout2.mMaximumVisibleLineCount == 1) {
                                        }
                                    } else {
                                        spanned2 = spanned;
                                    }
                                    z2 = true;
                                    boolean ellipsisMayBeApplied = z2;
                                    if (remainingLineCount > 0 || remainingLineCount >= breakCount || !ellipsisMayBeApplied) {
                                        ellipsize4 = ellipsize3;
                                        variableTabStops2 = stops;
                                        restWidth2 = restWidth;
                                        breakCount2 = breakCount;
                                    } else {
                                        float width = 0.0f;
                                        boolean hasTab = false;
                                        ellipsize4 = ellipsize3;
                                        int i8 = remainingLineCount - 1;
                                        while (i8 < breakCount) {
                                            float[] variableTabStops5 = stops;
                                            if (i8 == breakCount - 1) {
                                                width += lineWidths[i8];
                                                restWidth3 = restWidth;
                                            } else {
                                                int j = i8 == 0 ? 0 : breaks[i8 - 1];
                                                while (true) {
                                                    restWidth3 = restWidth;
                                                    if (j < breaks[i8]) {
                                                        width += measuredPara2.getCharWidthAt(j);
                                                        j++;
                                                        restWidth = restWidth3;
                                                    }
                                                }
                                            }
                                            hasTab |= hasTabs[i8];
                                            i8++;
                                            stops = variableTabStops5;
                                            restWidth = restWidth3;
                                        }
                                        variableTabStops2 = stops;
                                        restWidth2 = restWidth;
                                        int i9 = remainingLineCount - 1;
                                        breaks[i9] = breaks[breakCount - 1];
                                        lineWidths[remainingLineCount - 1] = width;
                                        hasTabs[remainingLineCount - 1] = hasTab;
                                        breakCount2 = remainingLineCount;
                                    }
                                    int spanEnd = paraStart;
                                    int fmTop2 = 0;
                                    int fmBottom = 0;
                                    int fmAscent2 = 0;
                                    int fmDescent = 0;
                                    int fmCacheIndex = 0;
                                    int spanEndCacheIndex = 0;
                                    int breakIndex2 = 0;
                                    spanStart = paraStart;
                                    while (spanStart < paraEnd2) {
                                        int spanEndCacheIndex2 = spanEndCacheIndex + 1;
                                        int firstWidthLineCount3 = firstWidthLineCount;
                                        int spanEnd2 = spanEndCache[spanEndCacheIndex];
                                        int i10 = 0;
                                        int here = spanEnd;
                                        int here2 = fmCache[(fmCacheIndex * 4) + 0];
                                        int firstWidth3 = firstWidth;
                                        Paint.FontMetricsInt fm5 = fm;
                                        fm5.top = here2;
                                        boolean z3 = true;
                                        fm5.bottom = fmCache[(fmCacheIndex * 4) + 1];
                                        fm5.ascent = fmCache[(fmCacheIndex * 4) + 2];
                                        fm5.descent = fmCache[(fmCacheIndex * 4) + 3];
                                        int fmCacheIndex2 = fmCacheIndex + 1;
                                        if (fm5.top < fmTop2) {
                                            fmTop2 = fm5.top;
                                        }
                                        if (fm5.ascent < fmAscent2) {
                                            fmAscent2 = fm5.ascent;
                                        }
                                        if (fm5.descent > fmDescent) {
                                            fmDescent = fm5.descent;
                                        }
                                        if (fm5.bottom <= fmBottom) {
                                            breakIndex = breakIndex2;
                                        } else {
                                            fmBottom = fm5.bottom;
                                            breakIndex = breakIndex2;
                                        }
                                        while (true) {
                                            if (breakIndex < breakCount2) {
                                                measuredPara = measuredPara2;
                                                if (paraStart + breaks[breakIndex] < spanStart) {
                                                    breakIndex++;
                                                    measuredPara2 = measuredPara;
                                                }
                                            } else {
                                                measuredPara = measuredPara2;
                                            }
                                        }
                                        int fmTop3 = fmTop2;
                                        int fmBottom2 = fmBottom;
                                        int here3 = here;
                                        int v2 = v;
                                        int fmTop4 = breakIndex;
                                        int i11 = fmDescent;
                                        int fmAscent3 = fmAscent2;
                                        int fmAscent4 = i11;
                                        while (fmTop4 < breakCount2 && breaks[fmTop4] + paraStart <= spanEnd2) {
                                            int endPos = paraStart + breaks[fmTop4];
                                            boolean moreChars = endPos < bufEnd2 ? z3 : i10;
                                            if (fallbackLineSpacing) {
                                                ascent = Math.min(fmAscent3, Math.round(ascents[fmTop4]));
                                            } else {
                                                ascent = fmAscent3;
                                            }
                                            if (fallbackLineSpacing) {
                                                paraEnd = Math.max(fmAscent4, Math.round(descents[fmTop4]));
                                            } else {
                                                paraEnd = fmAscent4;
                                            }
                                            int paraEnd3 = paraEnd2;
                                            LineBreaker.ParagraphConstraints constraints3 = constraints;
                                            LineBreaker.Result res2 = res;
                                            TextUtils.TruncateAt ellipsize6 = ellipsize4;
                                            boolean z4 = z3;
                                            int fmDescent2 = i10;
                                            LineBreaker lineBreaker5 = lineBreaker2;
                                            int paraStart2 = paraStart;
                                            int breakIndex3 = fmTop4;
                                            int paraIndex2 = paraIndex;
                                            int remainingLineCount2 = remainingLineCount;
                                            PrecomputedText.ParagraphInfo[] paragraphInfo4 = paragraphInfo3;
                                            Spanned spanned4 = spanned2;
                                            float ellipsizedWidth4 = ellipsizedWidth;
                                            int fmDescent3 = fmBottom2;
                                            TextDirectionHeuristic textDir4 = textDir2;
                                            float[] variableTabStops6 = variableTabStops2;
                                            int fmAscent5 = v2;
                                            TextPaint paint4 = paint2;
                                            int restWidth5 = restWidth2;
                                            int restWidth6 = breakCount2;
                                            int bufEnd3 = bufEnd2;
                                            Paint.FontMetricsInt fm6 = fm5;
                                            int bufStart5 = bufStart2;
                                            int spanEnd3 = spanEnd2;
                                            CharSequence source5 = source2;
                                            int firstWidthLineCount4 = firstWidthLineCount3;
                                            int spanStart2 = spanStart;
                                            v2 = out(source2, here3, endPos, ascent, paraEnd, fmTop3, fmDescent3, fmAscent5, spacingmult, spacingadd, chooseHt, chooseHtv, fm6, hasTabs[fmTop4], hyphenEdits[fmTop4], needMultiply, measuredPara, bufEnd3, includepad, trackpad, addLastLineSpacing, chs, paraStart2, ellipsize6, ellipsizedWidth4, lineWidths[fmTop4], paint4, moreChars);
                                            if (endPos < spanEnd3) {
                                                fm3 = fm6;
                                                int fmTop5 = fm3.top;
                                                int fmBottom3 = fm3.bottom;
                                                int fmAscent6 = fm3.ascent;
                                                fmTop3 = fmTop5;
                                                fmBottom2 = fmBottom3;
                                                fmAscent3 = fmAscent6;
                                                fmAscent4 = fm3.descent;
                                            } else {
                                                fm3 = fm6;
                                                fmBottom2 = fmDescent2;
                                                fmTop3 = fmDescent2;
                                                fmAscent4 = fmDescent2;
                                                fmAscent3 = fmDescent2;
                                            }
                                            here3 = endPos;
                                            fmTop4 = breakIndex3 + 1;
                                            if (this.mLineCount >= this.mMaximumVisibleLineCount && this.mEllipsized) {
                                                return;
                                            }
                                            spanEnd2 = spanEnd3;
                                            fm5 = fm3;
                                            variableTabStops2 = variableTabStops6;
                                            firstWidthLineCount3 = firstWidthLineCount4;
                                            spanStart = spanStart2;
                                            breakCount2 = restWidth6;
                                            paraEnd2 = paraEnd3;
                                            constraints = constraints3;
                                            ellipsize4 = ellipsize6;
                                            lineBreaker2 = lineBreaker5;
                                            res = res2;
                                            i10 = fmDescent2;
                                            z3 = z4;
                                            paraStart = paraStart2;
                                            paraIndex = paraIndex2;
                                            remainingLineCount = remainingLineCount2;
                                            paragraphInfo3 = paragraphInfo4;
                                            ellipsizedWidth = ellipsizedWidth4;
                                            spanned2 = spanned4;
                                            textDir2 = textDir4;
                                            bufEnd2 = bufEnd3;
                                            bufStart2 = bufStart5;
                                            source2 = source5;
                                            restWidth2 = restWidth5;
                                            paint2 = paint4;
                                        }
                                        int breakIndex4 = fmTop4;
                                        int remainingLineCount3 = remainingLineCount;
                                        Paint.FontMetricsInt fm7 = fm5;
                                        TextDirectionHeuristic textDir5 = textDir2;
                                        float ellipsizedWidth5 = ellipsizedWidth;
                                        float[] variableTabStops7 = variableTabStops2;
                                        int fmDescent4 = fmAscent4;
                                        int fmAscent7 = fmAscent3;
                                        spanStart = spanEnd2;
                                        firstWidth = firstWidth3;
                                        fmDescent = fmDescent4;
                                        fmCacheIndex = fmCacheIndex2;
                                        firstWidthLineCount = firstWidthLineCount3;
                                        measuredPara2 = measuredPara;
                                        fmAscent2 = fmAscent7;
                                        breakCount2 = breakCount2;
                                        spanEndCacheIndex = spanEndCacheIndex2;
                                        spanEnd = here3;
                                        fmTop2 = fmTop3;
                                        fmBottom = fmBottom2;
                                        v = v2;
                                        paraEnd2 = paraEnd2;
                                        constraints = constraints;
                                        ellipsize4 = ellipsize4;
                                        lineBreaker2 = lineBreaker2;
                                        res = res;
                                        paraStart = paraStart;
                                        paraIndex = paraIndex;
                                        breakIndex2 = breakIndex4;
                                        paragraphInfo3 = paragraphInfo3;
                                        ellipsizedWidth = ellipsizedWidth5;
                                        spanned2 = spanned2;
                                        bufEnd2 = bufEnd2;
                                        bufStart2 = bufStart2;
                                        source2 = source2;
                                        fm = fm7;
                                        variableTabStops2 = variableTabStops7;
                                        restWidth2 = restWidth2;
                                        remainingLineCount = remainingLineCount3;
                                        textDir2 = textDir5;
                                        paint2 = paint2;
                                    }
                                    LineBreaker.ParagraphConstraints constraints4 = constraints;
                                    ellipsize2 = ellipsize4;
                                    int paraIndex3 = paraIndex;
                                    PrecomputedText.ParagraphInfo[] paragraphInfo5 = paragraphInfo3;
                                    LineBreaker lineBreaker6 = lineBreaker2;
                                    Spanned spanned5 = spanned2;
                                    textDir = textDir2;
                                    ellipsizedWidth2 = ellipsizedWidth;
                                    paint = paint2;
                                    bufStart = bufStart2;
                                    source = source2;
                                    fm2 = fm;
                                    staticLayout = this;
                                    bufEnd = bufEnd2;
                                    if (paraEnd2 != bufEnd) {
                                        fmTop = paraIndex3 + 1;
                                        bufEnd2 = bufEnd;
                                        fm = fm2;
                                        staticLayout2 = staticLayout;
                                        chooseHtv2 = chooseHtv;
                                        lineBreakCapacity3 = lineBreakCapacity2;
                                        breaks2 = breaks;
                                        lineWidths2 = lineWidths;
                                        ascents2 = ascents;
                                        descents2 = descents;
                                        hasTabs2 = hasTabs;
                                        hyphenEdits2 = hyphenEdits;
                                        constraints = constraints4;
                                        ellipsize = ellipsize2;
                                        lineBreaker = lineBreaker6;
                                        paragraphInfo = paragraphInfo5;
                                        ellipsizedWidth = ellipsizedWidth2;
                                        spanned = spanned5;
                                        textDir3 = textDir;
                                        paint3 = paint;
                                        bufStart3 = bufStart;
                                        source3 = source;
                                    } else {
                                        fmAscent = v;
                                    }
                                } else {
                                    spanned2 = spanned;
                                    ellipsize3 = ellipsize;
                                }
                                z2 = false;
                                boolean ellipsisMayBeApplied2 = z2;
                                if (remainingLineCount > 0) {
                                }
                                ellipsize4 = ellipsize3;
                                variableTabStops2 = stops;
                                restWidth2 = restWidth;
                                breakCount2 = breakCount;
                                int spanEnd4 = paraStart;
                                int fmTop22 = 0;
                                int fmBottom4 = 0;
                                int fmAscent22 = 0;
                                int fmDescent5 = 0;
                                int fmCacheIndex3 = 0;
                                int spanEndCacheIndex3 = 0;
                                int breakIndex22 = 0;
                                spanStart = paraStart;
                                while (spanStart < paraEnd2) {
                                }
                                LineBreaker.ParagraphConstraints constraints42 = constraints;
                                ellipsize2 = ellipsize4;
                                int paraIndex32 = paraIndex;
                                PrecomputedText.ParagraphInfo[] paragraphInfo52 = paragraphInfo3;
                                LineBreaker lineBreaker62 = lineBreaker2;
                                Spanned spanned52 = spanned2;
                                textDir = textDir2;
                                ellipsizedWidth2 = ellipsizedWidth;
                                paint = paint2;
                                bufStart = bufStart2;
                                source = source2;
                                fm2 = fm;
                                staticLayout = this;
                                bufEnd = bufEnd2;
                                if (paraEnd2 != bufEnd) {
                                }
                            }
                        }
                    }
                }
                stops = variableTabStops;
                MeasuredParagraph measuredPara22 = paragraphInfo[fmTop].measured;
                char[] chs2 = measuredPara22.getChars();
                int[] spanEndCache2 = measuredPara22.getSpanEndCache().getRawArray();
                int[] fmCache2 = measuredPara22.getFontMetrics().getRawArray();
                constraints.setWidth(restWidth);
                constraints.setIndent(firstWidth, firstWidthLineCount);
                constraints.setTabStops(stops, TAB_INCREMENT);
                int paraIndex4 = fmTop;
                LineBreaker lineBreaker42 = lineBreaker;
                LineBreaker.Result res3 = lineBreaker42.computeLineBreaks(measuredPara22.getMeasuredText(), constraints, staticLayout2.mLineCount);
                breakCount = res3.getLineCount();
                if (lineBreakCapacity3 < breakCount) {
                }
                i = 0;
                while (i < breakCount) {
                }
                PrecomputedText.ParagraphInfo[] paragraphInfo32 = paragraphInfo;
                int i72 = staticLayout2.mMaximumVisibleLineCount;
                remainingLineCount = i72 - staticLayout2.mLineCount;
                if (ellipsize == null) {
                }
                z2 = false;
                boolean ellipsisMayBeApplied22 = z2;
                if (remainingLineCount > 0) {
                }
                ellipsize4 = ellipsize3;
                variableTabStops2 = stops;
                restWidth2 = restWidth;
                breakCount2 = breakCount;
                int spanEnd42 = paraStart;
                int fmTop222 = 0;
                int fmBottom42 = 0;
                int fmAscent222 = 0;
                int fmDescent52 = 0;
                int fmCacheIndex32 = 0;
                int spanEndCacheIndex32 = 0;
                int breakIndex222 = 0;
                spanStart = paraStart;
                while (spanStart < paraEnd2) {
                }
                LineBreaker.ParagraphConstraints constraints422 = constraints;
                ellipsize2 = ellipsize4;
                int paraIndex322 = paraIndex4;
                PrecomputedText.ParagraphInfo[] paragraphInfo522 = paragraphInfo32;
                LineBreaker lineBreaker622 = lineBreaker2;
                Spanned spanned522 = spanned2;
                textDir = textDir2;
                ellipsizedWidth2 = ellipsizedWidth;
                paint = paint2;
                bufStart = bufStart2;
                source = source2;
                fm2 = fm;
                staticLayout = this;
                bufEnd = bufEnd2;
                if (paraEnd2 != bufEnd) {
                }
            } else {
                textDir = textDir3;
                paint = paint3;
                bufEnd = bufEnd2;
                bufStart = bufStart3;
                source = source3;
                staticLayout = staticLayout2;
                ellipsizedWidth2 = ellipsizedWidth;
                ellipsize2 = ellipsize;
                fm2 = fm;
                fmAscent = v;
            }
        }
    }

    private int out(CharSequence text, int start, int end, int above, int below, int top, int bottom, int v, float spacingmult, float spacingadd, LineHeightSpan[] chooseHt, int[] chooseHtv, Paint.FontMetricsInt fm, boolean hasTab, int hyphenEdit, boolean needMultiply, MeasuredParagraph measured, int bufEnd, boolean includePad, boolean trackPad, boolean addLastLineLineSpacing, char[] chs, int widthStart, TextUtils.TruncateAt ellipsize, float ellipsisWidth, float textWidth, TextPaint paint, boolean moreChars) {
        int[] lines;
        int i;
        int i2;
        int j;
        int above2;
        int below2;
        int top2;
        int bottom2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        boolean lastCharIsNewLine;
        int extra;
        int want;
        int i8;
        int j2;
        boolean z;
        int j3 = this.mLineCount;
        int i9 = this.mColumns;
        int off = j3 * i9;
        int i10 = 1;
        int want2 = off + i9 + 1;
        int[] lines2 = this.mLines;
        int dir = measured.getParagraphDir();
        if (want2 < lines2.length) {
            lines = lines2;
        } else {
            int[] grow = ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(want2));
            System.arraycopy(lines2, 0, grow, 0, lines2.length);
            this.mLines = grow;
            lines = grow;
        }
        if (j3 >= this.mLineDirections.length) {
            Layout.Directions[] grow2 = (Layout.Directions[]) ArrayUtils.newUnpaddedArray(Layout.Directions.class, GrowingArrayUtils.growSize(j3));
            Layout.Directions[] directionsArr = this.mLineDirections;
            System.arraycopy(directionsArr, 0, grow2, 0, directionsArr.length);
            this.mLineDirections = grow2;
        }
        if (chooseHt == null) {
            i = 0;
            i2 = 1;
            j = j3;
            above2 = above;
            below2 = below;
            top2 = top;
            bottom2 = bottom;
        } else {
            fm.ascent = above;
            fm.descent = below;
            fm.top = top;
            fm.bottom = bottom;
            int i11 = 0;
            while (i11 < chooseHt.length) {
                if (chooseHt[i11] instanceof LineHeightSpan.WithDensity) {
                    z = false;
                    want = want2;
                    i8 = i10;
                    j2 = j3;
                    ((LineHeightSpan.WithDensity) chooseHt[i11]).chooseHeight(text, start, end, chooseHtv[i11], v, fm, paint);
                } else {
                    want = want2;
                    i8 = i10;
                    j2 = j3;
                    z = false;
                    chooseHt[i11].chooseHeight(text, start, end, chooseHtv[i11], v, fm);
                }
                i11++;
                i10 = i8;
                want2 = want;
                j3 = j2;
            }
            i2 = i10;
            j = j3;
            i = 0;
            above2 = fm.ascent;
            below2 = fm.descent;
            top2 = fm.top;
            bottom2 = fm.bottom;
        }
        int i12 = j == 0 ? i2 : i;
        int i13 = j + 1;
        int i14 = this.mMaximumVisibleLineCount;
        int i15 = i13 == i14 ? i2 : i;
        if (ellipsize == null) {
            i3 = widthStart;
            i4 = bufEnd;
        } else {
            boolean forceEllipsis = (!moreChars || this.mLineCount + i2 != i14) ? i : i2;
            if ((((((i14 != i2 || !moreChars) && (i12 == 0 || moreChars)) || ellipsize == TextUtils.TruncateAt.MARQUEE) && (i12 != 0 || ((i15 == 0 && moreChars) || ellipsize != TextUtils.TruncateAt.END))) ? i : i2) != 0) {
                i3 = widthStart;
                i4 = bufEnd;
                calculateEllipsis(start, end, measured, widthStart, ellipsisWidth, ellipsize, j, textWidth, paint, forceEllipsis);
            } else {
                i3 = widthStart;
                i4 = bufEnd;
                int[] iArr = this.mLines;
                int i16 = this.mColumns;
                iArr[(i16 * j) + 5] = i;
                iArr[(i16 * j) + 6] = i;
            }
        }
        if (this.mEllipsized) {
            lastCharIsNewLine = true;
            i6 = start;
            i7 = 1;
        } else {
            if (i3 != i4 && i4 > 0) {
                if (text.charAt(i4 - 1) == '\n') {
                    i5 = 1;
                    int i17 = i5;
                    if (end != i4 && i17 == 0) {
                        lastCharIsNewLine = true;
                        i7 = 1;
                        i6 = start;
                    } else {
                        i6 = start;
                        i7 = 1;
                        if (i6 != i4 && i17 != 0) {
                            lastCharIsNewLine = true;
                        } else {
                            lastCharIsNewLine = false;
                        }
                    }
                }
            }
            i5 = i;
            int i172 = i5;
            if (end != i4) {
            }
            i6 = start;
            i7 = 1;
            if (i6 != i4) {
            }
            lastCharIsNewLine = false;
        }
        if (i12 != 0) {
            if (trackPad) {
                this.mTopPadding = top2 - above2;
            }
            if (includePad) {
                above2 = top2;
            }
        }
        if (lastCharIsNewLine) {
            if (trackPad) {
                this.mBottomPadding = bottom2 - below2;
            }
            if (includePad) {
                below2 = bottom2;
            }
        }
        if (needMultiply && (addLastLineLineSpacing || !lastCharIsNewLine)) {
            double ex = ((below2 - above2) * (spacingmult - 1.0f)) + spacingadd;
            if (ex >= 0.0d) {
                extra = (int) (EXTRA_ROUNDING + ex);
            } else {
                extra = -((int) ((-ex) + EXTRA_ROUNDING));
            }
        } else {
            extra = 0;
        }
        lines[off + 0] = i6;
        lines[off + 1] = v;
        lines[off + 2] = below2 + extra;
        lines[off + 3] = extra;
        if (!this.mEllipsized && i15 != 0) {
            int maxLineBelow = includePad ? bottom2 : below2;
            this.mMaxLineHeight = v + (maxLineBelow - above2);
        }
        int maxLineBelow2 = below2 - above2;
        int v2 = v + maxLineBelow2 + extra;
        int i18 = this.mColumns;
        lines[off + i18 + 0] = end;
        lines[off + i18 + i7] = v2;
        int i19 = off + 0;
        int i20 = lines[i19];
        if (hasTab) {
            i = 536870912;
        }
        lines[i19] = i20 | i;
        lines[off + 4] = hyphenEdit;
        int i21 = off + 0;
        lines[i21] = lines[i21] | (dir << 30);
        this.mLineDirections[j] = measured.getDirections(i6 - i3, end - i3);
        this.mLineCount += i7;
        return v2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0055, code lost:
        if (r13 >= r10) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0061, code lost:
        if (r21.getCharWidthAt((r13 + r19) - r22) != 0.0f) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0063, code lost:
        r13 = r13 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ad, code lost:
        if (r15 >= r10) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00bb, code lost:
        if (r21.getCharWidthAt((r15 + r19) - r22) != 0.0f) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00bd, code lost:
        r15 = r15 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void calculateEllipsis(int lineStart, int lineEnd, MeasuredParagraph measured, int widthStart, float avail, TextUtils.TruncateAt where, int line, float textWidth, TextPaint paint, boolean forceEllipsis) {
        float avail2 = avail - getTotalInsets(line);
        if (textWidth <= avail2 && !forceEllipsis) {
            int[] iArr = this.mLines;
            int i = this.mColumns;
            iArr[(i * line) + 5] = 0;
            iArr[(i * line) + 6] = 0;
            return;
        }
        float ellipsisWidth = paint.measureText(TextUtils.getEllipsisString(where));
        int ellipsisStart = 0;
        int ellipsisCount = 0;
        int len = lineEnd - lineStart;
        if (where == TextUtils.TruncateAt.START) {
            if (this.mMaximumVisibleLineCount == 1) {
                float sum = 0.0f;
                int i2 = len;
                while (true) {
                    if (i2 <= 0) {
                        break;
                    }
                    float w = measured.getCharWidthAt(((i2 - 1) + lineStart) - widthStart);
                    if (w + sum + ellipsisWidth > avail2) {
                        break;
                    }
                    sum += w;
                    i2--;
                }
                ellipsisStart = 0;
                ellipsisCount = i2;
            } else if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Start Ellipsis only supported with one line");
            }
        } else if (where == TextUtils.TruncateAt.END || where == TextUtils.TruncateAt.MARQUEE || where == TextUtils.TruncateAt.END_SMALL) {
            float sum2 = 0.0f;
            int i3 = 0;
            while (i3 < len) {
                float w2 = measured.getCharWidthAt((i3 + lineStart) - widthStart);
                if (w2 + sum2 + ellipsisWidth > avail2) {
                    break;
                }
                sum2 += w2;
                i3++;
            }
            ellipsisStart = i3;
            ellipsisCount = len - i3;
            if (forceEllipsis && ellipsisCount == 0 && len > 0) {
                ellipsisStart = len - 1;
                ellipsisCount = 1;
            }
        } else if (this.mMaximumVisibleLineCount == 1) {
            float lsum = 0.0f;
            float rsum = 0.0f;
            int right = len;
            float ravail = (avail2 - ellipsisWidth) / 2.0f;
            while (true) {
                if (right <= 0) {
                    break;
                }
                float w3 = measured.getCharWidthAt(((right - 1) + lineStart) - widthStart);
                if (w3 + rsum > ravail) {
                    break;
                }
                rsum += w3;
                right--;
            }
            float lavail = (avail2 - ellipsisWidth) - rsum;
            int left = 0;
            while (left < right) {
                float w4 = measured.getCharWidthAt((left + lineStart) - widthStart);
                if (w4 + lsum > lavail) {
                    break;
                }
                lsum += w4;
                left++;
            }
            ellipsisStart = left;
            ellipsisCount = right - left;
        } else if (Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "Middle Ellipsis only supported with one line");
        }
        this.mEllipsized = true;
        int[] iArr2 = this.mLines;
        int i4 = this.mColumns;
        iArr2[(i4 * line) + 5] = ellipsisStart;
        iArr2[(i4 * line) + 6] = ellipsisCount;
    }

    private float getTotalInsets(int line) {
        int totalIndent = 0;
        int[] iArr = this.mLeftIndents;
        if (iArr != null) {
            totalIndent = iArr[Math.min(line, iArr.length - 1)];
        }
        int[] iArr2 = this.mRightIndents;
        if (iArr2 != null) {
            totalIndent += iArr2[Math.min(line, iArr2.length - 1)];
        }
        return totalIndent;
    }

    @Override // android.text.Layout
    public int getLineForVertical(int vertical) {
        int high = this.mLineCount;
        int low = -1;
        int[] lines = this.mLines;
        while (high - low > 1) {
            int guess = (high + low) >> 1;
            if (lines[(this.mColumns * guess) + 1] > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    @Override // android.text.Layout
    public int getLineCount() {
        return this.mLineCount;
    }

    @Override // android.text.Layout
    public int getLineTop(int line) {
        return this.mLines[(this.mColumns * line) + 1];
    }

    @Override // android.text.Layout
    public int getLineExtra(int line) {
        return this.mLines[(this.mColumns * line) + 3];
    }

    @Override // android.text.Layout
    public int getLineDescent(int line) {
        return this.mLines[(this.mColumns * line) + 2];
    }

    @Override // android.text.Layout
    public int getLineStart(int line) {
        return this.mLines[(this.mColumns * line) + 0] & 536870911;
    }

    @Override // android.text.Layout
    public int getParagraphDirection(int line) {
        return this.mLines[(this.mColumns * line) + 0] >> 30;
    }

    @Override // android.text.Layout
    public boolean getLineContainsTab(int line) {
        return (this.mLines[(this.mColumns * line) + 0] & 536870912) != 0;
    }

    @Override // android.text.Layout
    public final Layout.Directions getLineDirections(int line) {
        if (line > getLineCount()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mLineDirections[line];
    }

    @Override // android.text.Layout
    public int getTopPadding() {
        return this.mTopPadding;
    }

    @Override // android.text.Layout
    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int packHyphenEdit(int start, int end) {
        return (start << 3) | end;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int unpackStartHyphenEdit(int packedHyphenEdit) {
        return (packedHyphenEdit & 24) >> 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int unpackEndHyphenEdit(int packedHyphenEdit) {
        return packedHyphenEdit & 7;
    }

    @Override // android.text.Layout
    public int getStartHyphenEdit(int lineNumber) {
        return unpackStartHyphenEdit(this.mLines[(this.mColumns * lineNumber) + 4] & 255);
    }

    @Override // android.text.Layout
    public int getEndHyphenEdit(int lineNumber) {
        return unpackEndHyphenEdit(this.mLines[(this.mColumns * lineNumber) + 4] & 255);
    }

    @Override // android.text.Layout
    public int getIndentAdjust(int line, Layout.Alignment align) {
        if (align == Layout.Alignment.ALIGN_LEFT) {
            int[] iArr = this.mLeftIndents;
            if (iArr != null) {
                return iArr[Math.min(line, iArr.length - 1)];
            }
            return 0;
        } else if (align == Layout.Alignment.ALIGN_RIGHT) {
            int[] iArr2 = this.mRightIndents;
            if (iArr2 != null) {
                return -iArr2[Math.min(line, iArr2.length - 1)];
            }
            return 0;
        } else if (align == Layout.Alignment.ALIGN_CENTER) {
            int left = 0;
            int[] iArr3 = this.mLeftIndents;
            if (iArr3 != null) {
                left = iArr3[Math.min(line, iArr3.length - 1)];
            }
            int right = 0;
            int[] iArr4 = this.mRightIndents;
            if (iArr4 != null) {
                right = iArr4[Math.min(line, iArr4.length - 1)];
            }
            return (left - right) >> 1;
        } else {
            throw new AssertionError("unhandled alignment " + align);
        }
    }

    @Override // android.text.Layout
    public int getEllipsisCount(int line) {
        int i = this.mColumns;
        if (i < 7) {
            return 0;
        }
        return this.mLines[(i * line) + 6];
    }

    @Override // android.text.Layout
    public int getEllipsisStart(int line) {
        int i = this.mColumns;
        if (i < 7) {
            return 0;
        }
        return this.mLines[(i * line) + 5];
    }

    @Override // android.text.Layout
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    @Override // android.text.Layout
    public int getHeight(boolean cap) {
        int i;
        if (cap && this.mLineCount > this.mMaximumVisibleLineCount && this.mMaxLineHeight == -1 && Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "maxLineHeight should not be -1.  maxLines:" + this.mMaximumVisibleLineCount + " lineCount:" + this.mLineCount);
        }
        return (!cap || this.mLineCount <= this.mMaximumVisibleLineCount || (i = this.mMaxLineHeight) == -1) ? super.getHeight() : i;
    }

    /* loaded from: classes3.dex */
    static class LineBreaks {
        private static final int INITIAL_SIZE = 16;
        public int[] breaks = new int[16];
        public float[] widths = new float[16];
        public float[] ascents = new float[16];
        public float[] descents = new float[16];
        public int[] flags = new int[16];

        LineBreaks() {
        }
    }
}
