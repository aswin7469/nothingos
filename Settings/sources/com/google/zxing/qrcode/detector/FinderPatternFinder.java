package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public class FinderPatternFinder {
    private boolean hasSkipped;
    private final BitMatrix image;
    private final ResultPointCallback resultPointCallback;
    private final List<FinderPattern> possibleCenters = new ArrayList();
    private final int[] crossCheckStateCount = new int[5];

    public FinderPatternFinder(BitMatrix bitMatrix, ResultPointCallback resultPointCallback) {
        this.image = bitMatrix;
        this.resultPointCallback = resultPointCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final FinderPatternInfo find(Map<DecodeHintType, ?> map) throws NotFoundException {
        boolean z = map != null && map.containsKey(DecodeHintType.TRY_HARDER);
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int i = (height * 3) / 228;
        if (i < 3 || z) {
            i = 3;
        }
        int[] iArr = new int[5];
        int i2 = i - 1;
        boolean z2 = false;
        while (i2 < height && !z2) {
            iArr[0] = 0;
            iArr[1] = 0;
            iArr[2] = 0;
            iArr[3] = 0;
            iArr[4] = 0;
            int i3 = 0;
            int i4 = 0;
            while (i3 < width) {
                if (this.image.get(i3, i2)) {
                    if ((i4 & 1) == 1) {
                        i4++;
                    }
                    iArr[i4] = iArr[i4] + 1;
                } else if ((i4 & 1) != 0) {
                    iArr[i4] = iArr[i4] + 1;
                } else if (i4 == 4) {
                    if (foundPatternCross(iArr)) {
                        if (handlePossibleCenter(iArr, i2, i3)) {
                            if (this.hasSkipped) {
                                z2 = haveMultiplyConfirmedCenters();
                            } else {
                                int findRowSkip = findRowSkip();
                                if (findRowSkip > iArr[2]) {
                                    i2 += (findRowSkip - iArr[2]) - 2;
                                    i3 = width - 1;
                                }
                            }
                            iArr[0] = 0;
                            iArr[1] = 0;
                            iArr[2] = 0;
                            iArr[3] = 0;
                            iArr[4] = 0;
                            i4 = 0;
                            i = 2;
                        } else {
                            iArr[0] = iArr[2];
                            iArr[1] = iArr[3];
                            iArr[2] = iArr[4];
                            iArr[3] = 1;
                            iArr[4] = 0;
                        }
                    } else {
                        iArr[0] = iArr[2];
                        iArr[1] = iArr[3];
                        iArr[2] = iArr[4];
                        iArr[3] = 1;
                        iArr[4] = 0;
                    }
                    i4 = 3;
                } else {
                    i4++;
                    iArr[i4] = iArr[i4] + 1;
                }
                i3++;
            }
            if (foundPatternCross(iArr) && handlePossibleCenter(iArr, i2, width)) {
                i = iArr[0];
                if (this.hasSkipped) {
                    z2 = haveMultiplyConfirmedCenters();
                }
            }
            i2 += i;
        }
        FinderPattern[] selectBestPatterns = selectBestPatterns();
        ResultPoint.orderBestPatterns(selectBestPatterns);
        return new FinderPatternInfo(selectBestPatterns);
    }

    private static float centerFromEnd(int[] iArr, int i) {
        return ((i - iArr[4]) - iArr[3]) - (iArr[2] / 2.0f);
    }

    protected static boolean foundPatternCross(int[] iArr) {
        int i;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < 5; i4++) {
            int i5 = iArr[i4];
            if (i5 == 0) {
                return false;
            }
            i3 += i5;
        }
        return i3 >= 7 && Math.abs(i - (iArr[0] << 8)) < (i2 = (i = (i3 << 8) / 7) / 2) && Math.abs(i - (iArr[1] << 8)) < i2 && Math.abs((i * 3) - (iArr[2] << 8)) < i2 * 3 && Math.abs(i - (iArr[3] << 8)) < i2 && Math.abs(i - (iArr[4] << 8)) < i2;
    }

    private int[] getCrossCheckStateCount() {
        int[] iArr = this.crossCheckStateCount;
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        iArr[4] = 0;
        return iArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x003a, code lost:
        if (r9[1] <= r12) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003f, code lost:
        if (r2 < 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0045, code lost:
        if (r0.get(r11, r2) == false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0049, code lost:
        if (r9[0] > r12) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x004b, code lost:
        r9[0] = r9[0] + 1;
        r2 = r2 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0055, code lost:
        if (r9[0] <= r12) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0057, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0058, code lost:
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0059, code lost:
        if (r10 >= r1) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x005f, code lost:
        if (r0.get(r11, r10) == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0061, code lost:
        r9[2] = r9[2] + 1;
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0069, code lost:
        if (r10 != r1) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006b, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x006d, code lost:
        if (r10 >= r1) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0073, code lost:
        if (r0.get(r11, r10) != false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0077, code lost:
        if (r9[3] >= r12) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0079, code lost:
        r9[3] = r9[3] + 1;
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0081, code lost:
        if (r10 == r1) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0085, code lost:
        if (r9[3] < r12) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0089, code lost:
        if (r10 >= r1) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x008f, code lost:
        if (r0.get(r11, r10) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0093, code lost:
        if (r9[4] >= r12) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0095, code lost:
        r9[4] = r9[4] + 1;
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x009f, code lost:
        if (r9[4] < r12) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00a1, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b8, code lost:
        if ((java.lang.Math.abs(((((r9[0] + r9[1]) + r9[2]) + r9[3]) + r9[4]) - r13) * 5) < (r13 * 2)) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00ba, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00bf, code lost:
        if (foundPatternCross(r9) == false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00c5, code lost:
        return centerFromEnd(r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:?, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:?, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:?, code lost:
        return Float.NaN;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private float crossCheckVertical(int i, int i2, int i3, int i4) {
        BitMatrix bitMatrix = this.image;
        int height = bitMatrix.getHeight();
        int[] crossCheckStateCount = getCrossCheckStateCount();
        int i5 = i;
        while (i5 >= 0 && bitMatrix.get(i2, i5)) {
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i5--;
        }
        if (i5 < 0) {
            return Float.NaN;
        }
        while (i5 >= 0 && !bitMatrix.get(i2, i5) && crossCheckStateCount[1] <= i3) {
            crossCheckStateCount[1] = crossCheckStateCount[1] + 1;
            i5--;
        }
        return Float.NaN;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x003a, code lost:
        if (r9[1] <= r12) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003f, code lost:
        if (r2 < 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0045, code lost:
        if (r0.get(r2, r11) == false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0049, code lost:
        if (r9[0] > r12) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x004b, code lost:
        r9[0] = r9[0] + 1;
        r2 = r2 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0055, code lost:
        if (r9[0] <= r12) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0057, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0058, code lost:
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0059, code lost:
        if (r10 >= r1) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x005f, code lost:
        if (r0.get(r10, r11) == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0061, code lost:
        r9[2] = r9[2] + 1;
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0069, code lost:
        if (r10 != r1) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006b, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x006d, code lost:
        if (r10 >= r1) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0073, code lost:
        if (r0.get(r10, r11) != false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0077, code lost:
        if (r9[3] >= r12) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0079, code lost:
        r9[3] = r9[3] + 1;
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0081, code lost:
        if (r10 == r1) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0085, code lost:
        if (r9[3] < r12) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0089, code lost:
        if (r10 >= r1) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x008f, code lost:
        if (r0.get(r10, r11) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0093, code lost:
        if (r9[4] >= r12) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0095, code lost:
        r9[4] = r9[4] + 1;
        r10 = r10 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x009f, code lost:
        if (r9[4] < r12) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00a1, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b7, code lost:
        if ((java.lang.Math.abs(((((r9[0] + r9[1]) + r9[2]) + r9[3]) + r9[4]) - r13) * 5) < r13) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b9, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00be, code lost:
        if (foundPatternCross(r9) == false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00c4, code lost:
        return centerFromEnd(r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:?, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:?, code lost:
        return Float.NaN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:?, code lost:
        return Float.NaN;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private float crossCheckHorizontal(int i, int i2, int i3, int i4) {
        BitMatrix bitMatrix = this.image;
        int width = bitMatrix.getWidth();
        int[] crossCheckStateCount = getCrossCheckStateCount();
        int i5 = i;
        while (i5 >= 0 && bitMatrix.get(i5, i2)) {
            crossCheckStateCount[2] = crossCheckStateCount[2] + 1;
            i5--;
        }
        if (i5 < 0) {
            return Float.NaN;
        }
        while (i5 >= 0 && !bitMatrix.get(i5, i2) && crossCheckStateCount[1] <= i3) {
            crossCheckStateCount[1] = crossCheckStateCount[1] + 1;
            i5--;
        }
        return Float.NaN;
    }

    protected final boolean handlePossibleCenter(int[] iArr, int i, int i2) {
        boolean z = false;
        int i3 = iArr[0] + iArr[1] + iArr[2] + iArr[3] + iArr[4];
        int centerFromEnd = (int) centerFromEnd(iArr, i2);
        float crossCheckVertical = crossCheckVertical(i, centerFromEnd, iArr[2], i3);
        if (!Float.isNaN(crossCheckVertical)) {
            float crossCheckHorizontal = crossCheckHorizontal(centerFromEnd, (int) crossCheckVertical, iArr[2], i3);
            if (!Float.isNaN(crossCheckHorizontal)) {
                float f = i3 / 7.0f;
                int i4 = 0;
                while (true) {
                    if (i4 >= this.possibleCenters.size()) {
                        break;
                    }
                    FinderPattern finderPattern = this.possibleCenters.get(i4);
                    if (finderPattern.aboutEquals(f, crossCheckVertical, crossCheckHorizontal)) {
                        this.possibleCenters.set(i4, finderPattern.combineEstimate(crossCheckVertical, crossCheckHorizontal, f));
                        z = true;
                        break;
                    }
                    i4++;
                }
                if (!z) {
                    FinderPattern finderPattern2 = new FinderPattern(crossCheckHorizontal, crossCheckVertical, f);
                    this.possibleCenters.add(finderPattern2);
                    ResultPointCallback resultPointCallback = this.resultPointCallback;
                    if (resultPointCallback != null) {
                        resultPointCallback.foundPossibleResultPoint(finderPattern2);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private int findRowSkip() {
        if (this.possibleCenters.size() <= 1) {
            return 0;
        }
        FinderPattern finderPattern = null;
        for (FinderPattern finderPattern2 : this.possibleCenters) {
            if (finderPattern2.getCount() >= 2) {
                if (finderPattern != null) {
                    this.hasSkipped = true;
                    return ((int) (Math.abs(finderPattern.getX() - finderPattern2.getX()) - Math.abs(finderPattern.getY() - finderPattern2.getY()))) / 2;
                }
                finderPattern = finderPattern2;
            }
        }
        return 0;
    }

    private boolean haveMultiplyConfirmedCenters() {
        int size = this.possibleCenters.size();
        float f = 0.0f;
        float f2 = 0.0f;
        int i = 0;
        for (FinderPattern finderPattern : this.possibleCenters) {
            if (finderPattern.getCount() >= 2) {
                i++;
                f2 += finderPattern.getEstimatedModuleSize();
            }
        }
        if (i < 3) {
            return false;
        }
        float f3 = f2 / size;
        for (FinderPattern finderPattern2 : this.possibleCenters) {
            f += Math.abs(finderPattern2.getEstimatedModuleSize() - f3);
        }
        return f <= f2 * 0.05f;
    }

    private FinderPattern[] selectBestPatterns() throws NotFoundException {
        float f;
        int size = this.possibleCenters.size();
        if (size < 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        float f2 = 0.0f;
        if (size > 3) {
            float f3 = 0.0f;
            float f4 = 0.0f;
            for (FinderPattern finderPattern : this.possibleCenters) {
                float estimatedModuleSize = finderPattern.getEstimatedModuleSize();
                f3 += estimatedModuleSize;
                f4 += estimatedModuleSize * estimatedModuleSize;
            }
            float f5 = f3 / size;
            float sqrt = (float) Math.sqrt((f4 / f) - (f5 * f5));
            Collections.sort(this.possibleCenters, new FurthestFromAverageComparator(f5));
            float max = Math.max(0.2f * f5, sqrt);
            int i = 0;
            while (i < this.possibleCenters.size() && this.possibleCenters.size() > 3) {
                if (Math.abs(this.possibleCenters.get(i).getEstimatedModuleSize() - f5) > max) {
                    this.possibleCenters.remove(i);
                    i--;
                }
                i++;
            }
        }
        if (this.possibleCenters.size() > 3) {
            for (FinderPattern finderPattern2 : this.possibleCenters) {
                f2 += finderPattern2.getEstimatedModuleSize();
            }
            Collections.sort(this.possibleCenters, new CenterComparator(f2 / this.possibleCenters.size()));
            List<FinderPattern> list = this.possibleCenters;
            list.subList(3, list.size()).clear();
        }
        return new FinderPattern[]{this.possibleCenters.get(0), this.possibleCenters.get(1), this.possibleCenters.get(2)};
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class FurthestFromAverageComparator implements Comparator<FinderPattern>, Serializable {
        private final float average;

        private FurthestFromAverageComparator(float f) {
            this.average = f;
        }

        @Override // java.util.Comparator
        public int compare(FinderPattern finderPattern, FinderPattern finderPattern2) {
            float abs = Math.abs(finderPattern2.getEstimatedModuleSize() - this.average);
            float abs2 = Math.abs(finderPattern.getEstimatedModuleSize() - this.average);
            if (abs < abs2) {
                return -1;
            }
            return abs == abs2 ? 0 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class CenterComparator implements Comparator<FinderPattern>, Serializable {
        private final float average;

        private CenterComparator(float f) {
            this.average = f;
        }

        @Override // java.util.Comparator
        public int compare(FinderPattern finderPattern, FinderPattern finderPattern2) {
            if (finderPattern2.getCount() == finderPattern.getCount()) {
                float abs = Math.abs(finderPattern2.getEstimatedModuleSize() - this.average);
                float abs2 = Math.abs(finderPattern.getEstimatedModuleSize() - this.average);
                if (abs < abs2) {
                    return 1;
                }
                return abs == abs2 ? 0 : -1;
            }
            return finderPattern2.getCount() - finderPattern.getCount();
        }
    }
}
