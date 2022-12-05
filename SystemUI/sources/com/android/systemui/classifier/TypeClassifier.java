package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
/* loaded from: classes.dex */
public class TypeClassifier extends FalsingClassifier {
    /* JADX INFO: Access modifiers changed from: package-private */
    public TypeClassifier(FalsingDataProvider falsingDataProvider) {
        super(falsingDataProvider);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x0025, code lost:
        r9 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002c, code lost:
        if (r10 != false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0031, code lost:
        if (r10 != false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0036, code lost:
        if (r10 != false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r10 == false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0022, code lost:
        if (r10 != false) goto L10;
     */
    @Override // com.android.systemui.classifier.FalsingClassifier
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        double d3 = 0.0d;
        if (i == 13 || i == 14) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        boolean isVertical = isVertical();
        boolean isUp = isUp();
        boolean isRight = isRight();
        double d4 = 1.0d;
        switch (i) {
            case 0:
            case 2:
            case 9:
                if (isVertical) {
                }
                isVertical = true;
                break;
            case 1:
                d3 = 1.0d;
                d4 = d3;
                break;
            case 3:
            case 7:
            case 13:
            case 14:
            default:
                isVertical = true;
                break;
            case 4:
            case 8:
                if (isVertical) {
                }
                isVertical = true;
                break;
            case 5:
                if (isRight) {
                }
                isVertical = true;
                break;
            case 6:
                if (!isRight) {
                }
                isVertical = true;
                break;
            case 10:
                d4 = d3;
                break;
            case 11:
                isVertical = !isVertical;
                break;
            case 12:
                if (isVertical) {
                }
                isVertical = true;
                break;
            case 15:
                break;
        }
        return isVertical ? falsed(d4, getReason(i)) : FalsingClassifier.Result.passed(0.5d);
    }

    private String getReason(int i) {
        return String.format("{interaction=%s, vertical=%s, up=%s, right=%s}", Integer.valueOf(i), Boolean.valueOf(isVertical()), Boolean.valueOf(isUp()), Boolean.valueOf(isRight()));
    }
}
