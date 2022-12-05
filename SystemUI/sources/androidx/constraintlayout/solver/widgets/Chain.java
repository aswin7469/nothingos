package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Chain {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        int i3;
        ChainHead[] chainHeadArr;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = i4;
            i2 = 0;
        } else {
            i2 = 2;
            i3 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002d, code lost:
        if (r8 == 2) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0040, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x003e, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x003c, code lost:
        if (r8 == 2) goto L308;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:120:0x025b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x04d4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x04e6  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x04ef  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x04f6  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0506  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x050c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:158:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x04f2  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x04e9  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x02b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:185:0x03a5  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x03a6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:233:0x03b2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:241:0x03c5  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x048c  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x04c1  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01ce  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, int i2, ChainHead chainHead) {
        boolean z;
        boolean z2;
        boolean z3;
        ConstraintWidget constraintWidget;
        ArrayList<ConstraintWidget> arrayList;
        ConstraintWidget constraintWidget2;
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        ConstraintAnchor constraintAnchor3;
        int i3;
        ConstraintWidget constraintWidget3;
        int i4;
        ConstraintAnchor constraintAnchor4;
        SolverVariable solverVariable;
        SolverVariable solverVariable2;
        ConstraintWidget constraintWidget4;
        ConstraintAnchor constraintAnchor5;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        int i5;
        ConstraintWidget constraintWidget5;
        SolverVariable solverVariable5;
        float f;
        int size;
        int i6;
        ArrayList<ConstraintWidget> arrayList2;
        char c;
        boolean z4;
        boolean z5;
        boolean z6;
        ConstraintWidget constraintWidget6;
        ConstraintWidget constraintWidget7;
        int i7;
        ConstraintWidget constraintWidget8 = chainHead.mFirst;
        ConstraintWidget constraintWidget9 = chainHead.mLast;
        ConstraintWidget constraintWidget10 = chainHead.mFirstVisibleWidget;
        ConstraintWidget constraintWidget11 = chainHead.mLastVisibleWidget;
        ConstraintWidget constraintWidget12 = chainHead.mHead;
        float f2 = chainHead.mTotalWeight;
        boolean z7 = constraintWidgetContainer.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (i == 0) {
            int i8 = constraintWidget12.mHorizontalChainStyle;
            z = i8 == 0;
            z2 = i8 == 1;
        } else {
            int i9 = constraintWidget12.mVerticalChainStyle;
            z = i9 == 0;
            z2 = i9 == 1;
        }
        ConstraintWidget constraintWidget13 = constraintWidget8;
        boolean z8 = false;
        while (true) {
            constraintWidget = null;
            if (z8) {
                break;
            }
            ConstraintAnchor constraintAnchor6 = constraintWidget13.mListAnchors[i2];
            int i10 = z3 ? 1 : 4;
            int margin = constraintAnchor6.getMargin();
            float f3 = f2;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget13.mListDimensionBehaviors[i];
            boolean z9 = z8;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            if (dimensionBehaviour == dimensionBehaviour2 && constraintWidget13.mResolvedMatchConstraintDefault[i] == 0) {
                z4 = z2;
                z5 = true;
            } else {
                z4 = z2;
                z5 = false;
            }
            ConstraintAnchor constraintAnchor7 = constraintAnchor6.mTarget;
            if (constraintAnchor7 != null && constraintWidget13 != constraintWidget8) {
                margin += constraintAnchor7.getMargin();
            }
            int i11 = margin;
            if (!z3 || constraintWidget13 == constraintWidget8 || constraintWidget13 == constraintWidget10) {
                z6 = z;
            } else {
                z6 = z;
                i10 = 5;
            }
            ConstraintAnchor constraintAnchor8 = constraintAnchor6.mTarget;
            if (constraintAnchor8 != null) {
                if (constraintWidget13 == constraintWidget10) {
                    constraintWidget6 = constraintWidget12;
                    constraintWidget7 = constraintWidget8;
                    linearSystem.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor8.mSolverVariable, i11, 6);
                } else {
                    constraintWidget6 = constraintWidget12;
                    constraintWidget7 = constraintWidget8;
                    linearSystem.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor8.mSolverVariable, i11, 7);
                }
                linearSystem.addEquality(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i11, (!z5 || z3) ? i10 : 5);
            } else {
                constraintWidget6 = constraintWidget12;
                constraintWidget7 = constraintWidget8;
            }
            if (z7) {
                if (constraintWidget13.getVisibility() == 8 || constraintWidget13.mListDimensionBehaviors[i] != dimensionBehaviour2) {
                    i7 = 0;
                } else {
                    ConstraintAnchor[] constraintAnchorArr = constraintWidget13.mListAnchors;
                    i7 = 0;
                    linearSystem.addGreaterThan(constraintAnchorArr[i2 + 1].mSolverVariable, constraintAnchorArr[i2].mSolverVariable, 0, 5);
                }
                linearSystem.addGreaterThan(constraintWidget13.mListAnchors[i2].mSolverVariable, constraintWidgetContainer.mListAnchors[i2].mSolverVariable, i7, 7);
            }
            ConstraintAnchor constraintAnchor9 = constraintWidget13.mListAnchors[i2 + 1].mTarget;
            if (constraintAnchor9 != null) {
                ConstraintWidget constraintWidget14 = constraintAnchor9.mOwner;
                ConstraintAnchor[] constraintAnchorArr2 = constraintWidget14.mListAnchors;
                if (constraintAnchorArr2[i2].mTarget != null && constraintAnchorArr2[i2].mTarget.mOwner == constraintWidget13) {
                    constraintWidget = constraintWidget14;
                }
            }
            if (constraintWidget != null) {
                constraintWidget13 = constraintWidget;
                z8 = z9;
            } else {
                z8 = true;
            }
            z = z6;
            f2 = f3;
            z2 = z4;
            constraintWidget12 = constraintWidget6;
            constraintWidget8 = constraintWidget7;
        }
        ConstraintWidget constraintWidget15 = constraintWidget12;
        float f4 = f2;
        ConstraintWidget constraintWidget16 = constraintWidget8;
        boolean z10 = z;
        boolean z11 = z2;
        if (constraintWidget11 != null) {
            int i12 = i2 + 1;
            if (constraintWidget9.mListAnchors[i12].mTarget != null) {
                ConstraintAnchor constraintAnchor10 = constraintWidget11.mListAnchors[i12];
                if ((constraintWidget11.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget11.mResolvedMatchConstraintDefault[i] == 0) && !z3) {
                    ConstraintAnchor constraintAnchor11 = constraintAnchor10.mTarget;
                    if (constraintAnchor11.mOwner == constraintWidgetContainer) {
                        linearSystem.addEquality(constraintAnchor10.mSolverVariable, constraintAnchor11.mSolverVariable, -constraintAnchor10.getMargin(), 5);
                        linearSystem.addLowerThan(constraintAnchor10.mSolverVariable, constraintWidget9.mListAnchors[i12].mTarget.mSolverVariable, -constraintAnchor10.getMargin(), 6);
                        if (z7) {
                            int i13 = i2 + 1;
                            SolverVariable solverVariable6 = constraintWidgetContainer.mListAnchors[i13].mSolverVariable;
                            ConstraintAnchor[] constraintAnchorArr3 = constraintWidget9.mListAnchors;
                            linearSystem.addGreaterThan(solverVariable6, constraintAnchorArr3[i13].mSolverVariable, constraintAnchorArr3[i13].getMargin(), 7);
                        }
                        arrayList = chainHead.mWeightedMatchConstraintsWidgets;
                        if (arrayList != null && (size = arrayList.size()) > 1) {
                            float f5 = (chainHead.mHasUndefinedWeights || chainHead.mHasComplexMatchWeights) ? f4 : chainHead.mWidgetsMatchCount;
                            float f6 = 0.0f;
                            float f7 = 0.0f;
                            ConstraintWidget constraintWidget17 = null;
                            i6 = 0;
                            while (i6 < size) {
                                ConstraintWidget constraintWidget18 = arrayList.get(i6);
                                float f8 = constraintWidget18.mWeight[i];
                                if (f8 < f6) {
                                    if (chainHead.mHasComplexMatchWeights) {
                                        ConstraintAnchor[] constraintAnchorArr4 = constraintWidget18.mListAnchors;
                                        linearSystem.addEquality(constraintAnchorArr4[i2 + 1].mSolverVariable, constraintAnchorArr4[i2].mSolverVariable, 0, 4);
                                        c = 7;
                                        arrayList2 = arrayList;
                                        i6++;
                                        arrayList = arrayList2;
                                        f6 = 0.0f;
                                    } else {
                                        f8 = 1.0f;
                                    }
                                }
                                if (f8 == f6) {
                                    ConstraintAnchor[] constraintAnchorArr5 = constraintWidget18.mListAnchors;
                                    c = 7;
                                    linearSystem.addEquality(constraintAnchorArr5[i2 + 1].mSolverVariable, constraintAnchorArr5[i2].mSolverVariable, 0, 7);
                                    arrayList2 = arrayList;
                                    i6++;
                                    arrayList = arrayList2;
                                    f6 = 0.0f;
                                } else {
                                    if (constraintWidget17 != null) {
                                        ConstraintAnchor[] constraintAnchorArr6 = constraintWidget17.mListAnchors;
                                        SolverVariable solverVariable7 = constraintAnchorArr6[i2].mSolverVariable;
                                        int i14 = i2 + 1;
                                        SolverVariable solverVariable8 = constraintAnchorArr6[i14].mSolverVariable;
                                        ConstraintAnchor[] constraintAnchorArr7 = constraintWidget18.mListAnchors;
                                        SolverVariable solverVariable9 = constraintAnchorArr7[i2].mSolverVariable;
                                        SolverVariable solverVariable10 = constraintAnchorArr7[i14].mSolverVariable;
                                        arrayList2 = arrayList;
                                        ArrayRow createRow = linearSystem.createRow();
                                        createRow.createRowEqualMatchDimensions(f7, f5, f8, solverVariable7, solverVariable8, solverVariable9, solverVariable10);
                                        linearSystem.addConstraint(createRow);
                                    } else {
                                        arrayList2 = arrayList;
                                    }
                                    constraintWidget17 = constraintWidget18;
                                    f7 = f8;
                                    i6++;
                                    arrayList = arrayList2;
                                    f6 = 0.0f;
                                }
                            }
                        }
                        int i15 = 7;
                        if (constraintWidget10 == null && (constraintWidget10 == constraintWidget11 || z3)) {
                            ConstraintAnchor constraintAnchor12 = constraintWidget16.mListAnchors[i2];
                            int i16 = i2 + 1;
                            ConstraintAnchor constraintAnchor13 = constraintWidget9.mListAnchors[i16];
                            ConstraintAnchor constraintAnchor14 = constraintAnchor12.mTarget;
                            SolverVariable solverVariable11 = constraintAnchor14 != null ? constraintAnchor14.mSolverVariable : null;
                            ConstraintAnchor constraintAnchor15 = constraintAnchor13.mTarget;
                            SolverVariable solverVariable12 = constraintAnchor15 != null ? constraintAnchor15.mSolverVariable : null;
                            ConstraintAnchor constraintAnchor16 = constraintWidget10.mListAnchors[i2];
                            ConstraintAnchor constraintAnchor17 = constraintWidget11.mListAnchors[i16];
                            if (solverVariable11 != null && solverVariable12 != null) {
                                if (i == 0) {
                                    f = constraintWidget15.mHorizontalBiasPercent;
                                } else {
                                    f = constraintWidget15.mVerticalBiasPercent;
                                }
                                linearSystem.addCentering(constraintAnchor16.mSolverVariable, solverVariable11, constraintAnchor16.getMargin(), f, solverVariable12, constraintAnchor17.mSolverVariable, constraintAnchor17.getMargin(), 5);
                            }
                        } else if (z10 || constraintWidget10 == null) {
                            int i17 = 8;
                            if (z11 && constraintWidget10 != null) {
                                int i18 = chainHead.mWidgetsMatchCount;
                                boolean z12 = i18 <= 0 && chainHead.mWidgetsCount == i18;
                                constraintWidget2 = constraintWidget10;
                                ConstraintWidget constraintWidget19 = constraintWidget2;
                                while (constraintWidget2 != null) {
                                    ConstraintWidget constraintWidget20 = constraintWidget2.mNextChainWidget[i];
                                    while (constraintWidget20 != null && constraintWidget20.getVisibility() == i17) {
                                        constraintWidget20 = constraintWidget20.mNextChainWidget[i];
                                    }
                                    if (constraintWidget2 == constraintWidget10 || constraintWidget2 == constraintWidget11 || constraintWidget20 == null) {
                                        constraintWidget3 = constraintWidget19;
                                        i4 = i17;
                                    } else {
                                        ConstraintWidget constraintWidget21 = constraintWidget20 == constraintWidget11 ? null : constraintWidget20;
                                        ConstraintAnchor constraintAnchor18 = constraintWidget2.mListAnchors[i2];
                                        SolverVariable solverVariable13 = constraintAnchor18.mSolverVariable;
                                        ConstraintAnchor constraintAnchor19 = constraintAnchor18.mTarget;
                                        if (constraintAnchor19 != null) {
                                            SolverVariable solverVariable14 = constraintAnchor19.mSolverVariable;
                                        }
                                        int i19 = i2 + 1;
                                        SolverVariable solverVariable15 = constraintWidget19.mListAnchors[i19].mSolverVariable;
                                        int margin2 = constraintAnchor18.getMargin();
                                        int margin3 = constraintWidget2.mListAnchors[i19].getMargin();
                                        if (constraintWidget21 != null) {
                                            constraintAnchor4 = constraintWidget21.mListAnchors[i2];
                                            solverVariable = constraintAnchor4.mSolverVariable;
                                            ConstraintAnchor constraintAnchor20 = constraintAnchor4.mTarget;
                                            solverVariable2 = constraintAnchor20 != null ? constraintAnchor20.mSolverVariable : null;
                                        } else {
                                            constraintAnchor4 = constraintWidget11.mListAnchors[i2];
                                            solverVariable = constraintAnchor4 != null ? constraintAnchor4.mSolverVariable : null;
                                            solverVariable2 = constraintWidget2.mListAnchors[i19].mSolverVariable;
                                        }
                                        if (constraintAnchor4 != null) {
                                            margin3 += constraintAnchor4.getMargin();
                                        }
                                        int i20 = margin3;
                                        int margin4 = constraintWidget19.mListAnchors[i19].getMargin() + margin2;
                                        int i21 = z12 ? 7 : 4;
                                        if (solverVariable13 == null || solverVariable15 == null || solverVariable == null || solverVariable2 == null) {
                                            constraintWidget4 = constraintWidget21;
                                            constraintWidget3 = constraintWidget19;
                                            i4 = 8;
                                        } else {
                                            constraintWidget4 = constraintWidget21;
                                            constraintWidget3 = constraintWidget19;
                                            i4 = 8;
                                            linearSystem.addCentering(solverVariable13, solverVariable15, margin4, 0.5f, solverVariable, solverVariable2, i20, i21);
                                        }
                                        constraintWidget20 = constraintWidget4;
                                    }
                                    if (constraintWidget2.getVisibility() == i4) {
                                        constraintWidget2 = constraintWidget3;
                                    }
                                    i17 = i4;
                                    constraintWidget19 = constraintWidget2;
                                    constraintWidget2 = constraintWidget20;
                                }
                                ConstraintAnchor constraintAnchor21 = constraintWidget10.mListAnchors[i2];
                                constraintAnchor = constraintWidget16.mListAnchors[i2].mTarget;
                                int i22 = i2 + 1;
                                constraintAnchor2 = constraintWidget11.mListAnchors[i22];
                                constraintAnchor3 = constraintWidget9.mListAnchors[i22].mTarget;
                                if (constraintAnchor != null) {
                                    i3 = 4;
                                } else if (constraintWidget10 != constraintWidget11) {
                                    i3 = 4;
                                    linearSystem.addEquality(constraintAnchor21.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor21.getMargin(), 4);
                                } else {
                                    i3 = 4;
                                    if (constraintAnchor3 != null) {
                                        linearSystem.addCentering(constraintAnchor21.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor21.getMargin(), 0.5f, constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, constraintAnchor2.getMargin(), 4);
                                    }
                                }
                                if (constraintAnchor3 != null && constraintWidget10 != constraintWidget11) {
                                    linearSystem.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
                                }
                            }
                        } else {
                            int i23 = chainHead.mWidgetsMatchCount;
                            boolean z13 = i23 > 0 && chainHead.mWidgetsCount == i23;
                            ConstraintWidget constraintWidget22 = constraintWidget10;
                            ConstraintWidget constraintWidget23 = constraintWidget22;
                            while (constraintWidget22 != null) {
                                ConstraintWidget constraintWidget24 = constraintWidget22.mNextChainWidget[i];
                                while (constraintWidget24 != null && constraintWidget24.getVisibility() == 8) {
                                    constraintWidget24 = constraintWidget24.mNextChainWidget[i];
                                }
                                if (constraintWidget24 != null || constraintWidget22 == constraintWidget11) {
                                    ConstraintAnchor constraintAnchor22 = constraintWidget22.mListAnchors[i2];
                                    SolverVariable solverVariable16 = constraintAnchor22.mSolverVariable;
                                    ConstraintAnchor constraintAnchor23 = constraintAnchor22.mTarget;
                                    SolverVariable solverVariable17 = constraintAnchor23 != null ? constraintAnchor23.mSolverVariable : null;
                                    if (constraintWidget23 != constraintWidget22) {
                                        solverVariable17 = constraintWidget23.mListAnchors[i2 + 1].mSolverVariable;
                                    } else if (constraintWidget22 == constraintWidget10 && constraintWidget23 == constraintWidget22) {
                                        ConstraintAnchor[] constraintAnchorArr8 = constraintWidget16.mListAnchors;
                                        solverVariable17 = constraintAnchorArr8[i2].mTarget != null ? constraintAnchorArr8[i2].mTarget.mSolverVariable : null;
                                    }
                                    int margin5 = constraintAnchor22.getMargin();
                                    int i24 = i2 + 1;
                                    int margin6 = constraintWidget22.mListAnchors[i24].getMargin();
                                    if (constraintWidget24 != null) {
                                        constraintAnchor5 = constraintWidget24.mListAnchors[i2];
                                        SolverVariable solverVariable18 = constraintAnchor5.mSolverVariable;
                                        solverVariable4 = constraintWidget22.mListAnchors[i24].mSolverVariable;
                                        solverVariable3 = solverVariable18;
                                    } else {
                                        constraintAnchor5 = constraintWidget9.mListAnchors[i24].mTarget;
                                        solverVariable3 = constraintAnchor5 != null ? constraintAnchor5.mSolverVariable : null;
                                        solverVariable4 = constraintWidget22.mListAnchors[i24].mSolverVariable;
                                    }
                                    if (constraintAnchor5 != null) {
                                        margin6 += constraintAnchor5.getMargin();
                                    }
                                    if (constraintWidget23 != null) {
                                        margin5 += constraintWidget23.mListAnchors[i24].getMargin();
                                    }
                                    if (solverVariable16 != null && solverVariable17 != null && solverVariable3 != null && solverVariable4 != null) {
                                        if (constraintWidget22 == constraintWidget10) {
                                            margin5 = constraintWidget10.mListAnchors[i2].getMargin();
                                        }
                                        int i25 = margin5;
                                        i5 = i15;
                                        constraintWidget5 = constraintWidget24;
                                        linearSystem.addCentering(solverVariable16, solverVariable17, i25, 0.5f, solverVariable3, solverVariable4, constraintWidget22 == constraintWidget11 ? constraintWidget11.mListAnchors[i24].getMargin() : margin6, z13 ? i15 : 5);
                                        if (constraintWidget22.getVisibility() == 8) {
                                            constraintWidget23 = constraintWidget22;
                                        }
                                        constraintWidget22 = constraintWidget5;
                                        i15 = i5;
                                    }
                                }
                                constraintWidget5 = constraintWidget24;
                                i5 = i15;
                                if (constraintWidget22.getVisibility() == 8) {
                                }
                                constraintWidget22 = constraintWidget5;
                                i15 = i5;
                            }
                        }
                        if ((z10 && !z11) || constraintWidget10 == null) {
                            return;
                        }
                        ConstraintAnchor[] constraintAnchorArr9 = constraintWidget10.mListAnchors;
                        ConstraintAnchor constraintAnchor24 = constraintAnchorArr9[i2];
                        int i26 = i2 + 1;
                        ConstraintAnchor constraintAnchor25 = constraintWidget11.mListAnchors[i26];
                        ConstraintAnchor constraintAnchor26 = constraintAnchor24.mTarget;
                        solverVariable5 = constraintAnchor26 != null ? constraintAnchor26.mSolverVariable : null;
                        ConstraintAnchor constraintAnchor27 = constraintAnchor25.mTarget;
                        SolverVariable solverVariable19 = constraintAnchor27 != null ? constraintAnchor27.mSolverVariable : null;
                        if (constraintWidget9 != constraintWidget11) {
                            ConstraintAnchor constraintAnchor28 = constraintWidget9.mListAnchors[i26].mTarget;
                            if (constraintAnchor28 != null) {
                                constraintWidget = constraintAnchor28.mSolverVariable;
                            }
                            solverVariable19 = constraintWidget;
                        }
                        if (constraintWidget10 == constraintWidget11) {
                            constraintAnchor24 = constraintAnchorArr9[i2];
                            constraintAnchor25 = constraintAnchorArr9[i26];
                        }
                        if (solverVariable5 != null || solverVariable19 == null) {
                            return;
                        }
                        linearSystem.addCentering(constraintAnchor24.mSolverVariable, solverVariable5, constraintAnchor24.getMargin(), 0.5f, solverVariable19, constraintAnchor25.mSolverVariable, constraintWidget11.mListAnchors[i26].getMargin(), 5);
                        return;
                    }
                }
                if (z3) {
                    ConstraintAnchor constraintAnchor29 = constraintAnchor10.mTarget;
                    if (constraintAnchor29.mOwner == constraintWidgetContainer) {
                        linearSystem.addEquality(constraintAnchor10.mSolverVariable, constraintAnchor29.mSolverVariable, -constraintAnchor10.getMargin(), 4);
                    }
                }
                linearSystem.addLowerThan(constraintAnchor10.mSolverVariable, constraintWidget9.mListAnchors[i12].mTarget.mSolverVariable, -constraintAnchor10.getMargin(), 6);
                if (z7) {
                }
                arrayList = chainHead.mWeightedMatchConstraintsWidgets;
                if (arrayList != null) {
                    if (chainHead.mHasUndefinedWeights) {
                    }
                    float f62 = 0.0f;
                    float f72 = 0.0f;
                    ConstraintWidget constraintWidget172 = null;
                    i6 = 0;
                    while (i6 < size) {
                    }
                }
                int i152 = 7;
                if (constraintWidget10 == null) {
                }
                if (z10) {
                }
                int i172 = 8;
                if (z11) {
                    int i182 = chainHead.mWidgetsMatchCount;
                    if (i182 <= 0) {
                    }
                    constraintWidget2 = constraintWidget10;
                    ConstraintWidget constraintWidget192 = constraintWidget2;
                    while (constraintWidget2 != null) {
                    }
                    ConstraintAnchor constraintAnchor212 = constraintWidget10.mListAnchors[i2];
                    constraintAnchor = constraintWidget16.mListAnchors[i2].mTarget;
                    int i222 = i2 + 1;
                    constraintAnchor2 = constraintWidget11.mListAnchors[i222];
                    constraintAnchor3 = constraintWidget9.mListAnchors[i222].mTarget;
                    if (constraintAnchor != null) {
                    }
                    if (constraintAnchor3 != null) {
                        linearSystem.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
                    }
                }
                if (z10) {
                }
                ConstraintAnchor[] constraintAnchorArr92 = constraintWidget10.mListAnchors;
                ConstraintAnchor constraintAnchor242 = constraintAnchorArr92[i2];
                int i262 = i2 + 1;
                ConstraintAnchor constraintAnchor252 = constraintWidget11.mListAnchors[i262];
                ConstraintAnchor constraintAnchor262 = constraintAnchor242.mTarget;
                if (constraintAnchor262 != null) {
                }
                ConstraintAnchor constraintAnchor272 = constraintAnchor252.mTarget;
                if (constraintAnchor272 != null) {
                }
                if (constraintWidget9 != constraintWidget11) {
                }
                if (constraintWidget10 == constraintWidget11) {
                }
                if (solverVariable5 != null) {
                    return;
                }
                return;
            }
        }
        if (z7) {
        }
        arrayList = chainHead.mWeightedMatchConstraintsWidgets;
        if (arrayList != null) {
        }
        int i1522 = 7;
        if (constraintWidget10 == null) {
        }
        if (z10) {
        }
        int i1722 = 8;
        if (z11) {
        }
        if (z10) {
        }
        ConstraintAnchor[] constraintAnchorArr922 = constraintWidget10.mListAnchors;
        ConstraintAnchor constraintAnchor2422 = constraintAnchorArr922[i2];
        int i2622 = i2 + 1;
        ConstraintAnchor constraintAnchor2522 = constraintWidget11.mListAnchors[i2622];
        ConstraintAnchor constraintAnchor2622 = constraintAnchor2422.mTarget;
        if (constraintAnchor2622 != null) {
        }
        ConstraintAnchor constraintAnchor2722 = constraintAnchor2522.mTarget;
        if (constraintAnchor2722 != null) {
        }
        if (constraintWidget9 != constraintWidget11) {
        }
        if (constraintWidget10 == constraintWidget11) {
        }
        if (solverVariable5 != null) {
        }
    }
}
