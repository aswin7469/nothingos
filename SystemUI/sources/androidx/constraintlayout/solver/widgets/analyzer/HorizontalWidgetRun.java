package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
/* loaded from: classes.dex */
public class HorizontalWidgetRun extends WidgetRun {
    private static int[] tempDimensions = new int[2];

    public HorizontalWidgetRun(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        this.start.type = DependencyNode.Type.LEFT;
        this.end.type = DependencyNode.Type.RIGHT;
        this.orientation = 0;
    }

    public String toString() {
        return "HorizontalRun " + this.widget.getDebugName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.dimension.clear();
        this.resolved = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        this.resolved = false;
        this.start.clear();
        this.start.resolved = false;
        this.end.clear();
        this.end.resolved = false;
        this.dimension.resolved = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public boolean supportsWrapComputation() {
        return this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.widget.mMatchConstraintDefaultWidth == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public void apply() {
        ConstraintWidget parent;
        ConstraintWidget parent2;
        ConstraintWidget constraintWidget = this.widget;
        if (constraintWidget.measured) {
            this.dimension.resolve(constraintWidget.getWidth());
        }
        if (!this.dimension.resolved) {
            ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = this.widget.getHorizontalDimensionBehaviour();
            this.dimensionBehavior = horizontalDimensionBehaviour;
            if (horizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
                if (horizontalDimensionBehaviour == dimensionBehaviour && (((parent2 = this.widget.getParent()) != null && parent2.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) || parent2.getHorizontalDimensionBehaviour() == dimensionBehaviour)) {
                    int width = (parent2.getWidth() - this.widget.mLeft.getMargin()) - this.widget.mRight.getMargin();
                    addTarget(this.start, parent2.horizontalRun.start, this.widget.mLeft.getMargin());
                    addTarget(this.end, parent2.horizontalRun.end, -this.widget.mRight.getMargin());
                    this.dimension.resolve(width);
                    return;
                } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    this.dimension.resolve(this.widget.getWidth());
                }
            }
        } else {
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.dimensionBehavior;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.MATCH_PARENT;
            if (dimensionBehaviour2 == dimensionBehaviour3 && (((parent = this.widget.getParent()) != null && parent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) || parent.getHorizontalDimensionBehaviour() == dimensionBehaviour3)) {
                addTarget(this.start, parent.horizontalRun.start, this.widget.mLeft.getMargin());
                addTarget(this.end, parent.horizontalRun.end, -this.widget.mRight.getMargin());
                return;
            }
        }
        DimensionDependency dimensionDependency = this.dimension;
        if (dimensionDependency.resolved) {
            ConstraintWidget constraintWidget2 = this.widget;
            if (constraintWidget2.measured) {
                ConstraintAnchor[] constraintAnchorArr = constraintWidget2.mListAnchors;
                if (constraintAnchorArr[0].mTarget != null && constraintAnchorArr[1].mTarget != null) {
                    if (constraintWidget2.isInHorizontalChain()) {
                        this.start.margin = this.widget.mListAnchors[0].getMargin();
                        this.end.margin = -this.widget.mListAnchors[1].getMargin();
                        return;
                    }
                    DependencyNode target = getTarget(this.widget.mListAnchors[0]);
                    if (target != null) {
                        addTarget(this.start, target, this.widget.mListAnchors[0].getMargin());
                    }
                    DependencyNode target2 = getTarget(this.widget.mListAnchors[1]);
                    if (target2 != null) {
                        addTarget(this.end, target2, -this.widget.mListAnchors[1].getMargin());
                    }
                    this.start.delegateToWidgetRun = true;
                    this.end.delegateToWidgetRun = true;
                    return;
                } else if (constraintAnchorArr[0].mTarget != null) {
                    DependencyNode target3 = getTarget(constraintAnchorArr[0]);
                    if (target3 == null) {
                        return;
                    }
                    addTarget(this.start, target3, this.widget.mListAnchors[0].getMargin());
                    addTarget(this.end, this.start, this.dimension.value);
                    return;
                } else if (constraintAnchorArr[1].mTarget != null) {
                    DependencyNode target4 = getTarget(constraintAnchorArr[1]);
                    if (target4 == null) {
                        return;
                    }
                    addTarget(this.end, target4, -this.widget.mListAnchors[1].getMargin());
                    addTarget(this.start, this.end, -this.dimension.value);
                    return;
                } else if ((constraintWidget2 instanceof Helper) || constraintWidget2.getParent() == null || this.widget.getAnchor(ConstraintAnchor.Type.CENTER).mTarget != null) {
                    return;
                } else {
                    addTarget(this.start, this.widget.getParent().horizontalRun.start, this.widget.getX());
                    addTarget(this.end, this.start, this.dimension.value);
                    return;
                }
            }
        }
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget3 = this.widget;
            int i = constraintWidget3.mMatchConstraintDefaultWidth;
            if (i == 2) {
                ConstraintWidget parent3 = constraintWidget3.getParent();
                if (parent3 != null) {
                    DimensionDependency dimensionDependency2 = parent3.verticalRun.dimension;
                    this.dimension.targets.add(dimensionDependency2);
                    dimensionDependency2.dependencies.add(this.dimension);
                    DimensionDependency dimensionDependency3 = this.dimension;
                    dimensionDependency3.delegateToWidgetRun = true;
                    dimensionDependency3.dependencies.add(this.start);
                    this.dimension.dependencies.add(this.end);
                }
            } else if (i == 3) {
                if (constraintWidget3.mMatchConstraintDefaultHeight == 3) {
                    this.start.updateDelegate = this;
                    this.end.updateDelegate = this;
                    VerticalWidgetRun verticalWidgetRun = constraintWidget3.verticalRun;
                    verticalWidgetRun.start.updateDelegate = this;
                    verticalWidgetRun.end.updateDelegate = this;
                    dimensionDependency.updateDelegate = this;
                    if (constraintWidget3.isInVerticalChain()) {
                        this.dimension.targets.add(this.widget.verticalRun.dimension);
                        this.widget.verticalRun.dimension.dependencies.add(this.dimension);
                        VerticalWidgetRun verticalWidgetRun2 = this.widget.verticalRun;
                        verticalWidgetRun2.dimension.updateDelegate = this;
                        this.dimension.targets.add(verticalWidgetRun2.start);
                        this.dimension.targets.add(this.widget.verticalRun.end);
                        this.widget.verticalRun.start.dependencies.add(this.dimension);
                        this.widget.verticalRun.end.dependencies.add(this.dimension);
                    } else if (this.widget.isInHorizontalChain()) {
                        this.widget.verticalRun.dimension.targets.add(this.dimension);
                        this.dimension.dependencies.add(this.widget.verticalRun.dimension);
                    } else {
                        this.widget.verticalRun.dimension.targets.add(this.dimension);
                    }
                } else {
                    DimensionDependency dimensionDependency4 = constraintWidget3.verticalRun.dimension;
                    dimensionDependency.targets.add(dimensionDependency4);
                    dimensionDependency4.dependencies.add(this.dimension);
                    this.widget.verticalRun.start.dependencies.add(this.dimension);
                    this.widget.verticalRun.end.dependencies.add(this.dimension);
                    DimensionDependency dimensionDependency5 = this.dimension;
                    dimensionDependency5.delegateToWidgetRun = true;
                    dimensionDependency5.dependencies.add(this.start);
                    this.dimension.dependencies.add(this.end);
                    this.start.targets.add(this.dimension);
                    this.end.targets.add(this.dimension);
                }
            }
        }
        ConstraintWidget constraintWidget4 = this.widget;
        ConstraintAnchor[] constraintAnchorArr2 = constraintWidget4.mListAnchors;
        if (constraintAnchorArr2[0].mTarget != null && constraintAnchorArr2[1].mTarget != null) {
            if (constraintWidget4.isInHorizontalChain()) {
                this.start.margin = this.widget.mListAnchors[0].getMargin();
                this.end.margin = -this.widget.mListAnchors[1].getMargin();
                return;
            }
            DependencyNode target5 = getTarget(this.widget.mListAnchors[0]);
            DependencyNode target6 = getTarget(this.widget.mListAnchors[1]);
            target5.addDependency(this);
            target6.addDependency(this);
            this.mRunType = WidgetRun.RunType.CENTER;
        } else if (constraintAnchorArr2[0].mTarget != null) {
            DependencyNode target7 = getTarget(constraintAnchorArr2[0]);
            if (target7 == null) {
                return;
            }
            addTarget(this.start, target7, this.widget.mListAnchors[0].getMargin());
            addTarget(this.end, this.start, 1, this.dimension);
        } else if (constraintAnchorArr2[1].mTarget != null) {
            DependencyNode target8 = getTarget(constraintAnchorArr2[1]);
            if (target8 == null) {
                return;
            }
            addTarget(this.end, target8, -this.widget.mListAnchors[1].getMargin());
            addTarget(this.start, this.end, -1, this.dimension);
        } else if ((constraintWidget4 instanceof Helper) || constraintWidget4.getParent() == null) {
        } else {
            addTarget(this.start, this.widget.getParent().horizontalRun.start, this.widget.getX());
            addTarget(this.end, this.start, 1, this.dimension);
        }
    }

    private void computeInsetRatio(int[] iArr, int i, int i2, int i3, int i4, float f, int i5) {
        int i6 = i2 - i;
        int i7 = i4 - i3;
        if (i5 != -1) {
            if (i5 == 0) {
                iArr[0] = (int) ((i7 * f) + 0.5f);
                iArr[1] = i7;
                return;
            } else if (i5 != 1) {
                return;
            } else {
                iArr[0] = i6;
                iArr[1] = (int) ((i6 * f) + 0.5f);
                return;
            }
        }
        int i8 = (int) ((i7 * f) + 0.5f);
        int i9 = (int) ((i6 / f) + 0.5f);
        if (i8 <= i6) {
            iArr[0] = i8;
            iArr[1] = i7;
        } else if (i9 > i7) {
        } else {
            iArr[0] = i6;
            iArr[1] = i9;
        }
    }

    /* renamed from: androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType;

        static {
            int[] iArr = new int[WidgetRun.RunType.values().length];
            $SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType = iArr;
            try {
                iArr[WidgetRun.RunType.START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType[WidgetRun.RunType.END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType[WidgetRun.RunType.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x02b9, code lost:
        if (r14 != 1) goto L131;
     */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun, androidx.constraintlayout.solver.widgets.analyzer.Dependency
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(Dependency dependency) {
        DimensionDependency dimensionDependency;
        float f;
        float dimensionRatio;
        float f2;
        int i;
        ConstraintWidget constraintWidget;
        int i2 = AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType[this.mRunType.ordinal()];
        if (i2 == 1) {
            updateRunStart(dependency);
        } else if (i2 == 2) {
            updateRunEnd(dependency);
        } else if (i2 == 3) {
            ConstraintWidget constraintWidget2 = this.widget;
            updateRunCenter(dependency, constraintWidget2.mLeft, constraintWidget2.mRight, 0);
            return;
        }
        if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget3 = this.widget;
            int i3 = constraintWidget3.mMatchConstraintDefaultWidth;
            if (i3 == 2) {
                ConstraintWidget parent = constraintWidget3.getParent();
                if (parent != null) {
                    if (parent.horizontalRun.dimension.resolved) {
                        this.dimension.resolve((int) ((dimensionDependency.value * this.widget.mMatchConstraintPercentWidth) + 0.5f));
                    }
                }
            } else if (i3 == 3) {
                int i4 = constraintWidget3.mMatchConstraintDefaultHeight;
                if (i4 == 0 || i4 == 3) {
                    VerticalWidgetRun verticalWidgetRun = constraintWidget3.verticalRun;
                    DependencyNode dependencyNode = verticalWidgetRun.start;
                    DependencyNode dependencyNode2 = verticalWidgetRun.end;
                    boolean z = constraintWidget3.mLeft.mTarget != null;
                    boolean z2 = constraintWidget3.mTop.mTarget != null;
                    boolean z3 = constraintWidget3.mRight.mTarget != null;
                    boolean z4 = constraintWidget3.mBottom.mTarget != null;
                    int dimensionRatioSide = constraintWidget3.getDimensionRatioSide();
                    if (z && z2 && z3 && z4) {
                        float dimensionRatio2 = this.widget.getDimensionRatio();
                        if (dependencyNode.resolved && dependencyNode2.resolved) {
                            DependencyNode dependencyNode3 = this.start;
                            if (!dependencyNode3.readyToSolve || !this.end.readyToSolve) {
                                return;
                            }
                            computeInsetRatio(tempDimensions, dependencyNode3.targets.get(0).value + this.start.margin, this.end.targets.get(0).value - this.end.margin, dependencyNode.value + dependencyNode.margin, dependencyNode2.value - dependencyNode2.margin, dimensionRatio2, dimensionRatioSide);
                            this.dimension.resolve(tempDimensions[0]);
                            this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
                            return;
                        }
                        DependencyNode dependencyNode4 = this.start;
                        if (dependencyNode4.resolved) {
                            DependencyNode dependencyNode5 = this.end;
                            if (dependencyNode5.resolved) {
                                if (!dependencyNode.readyToSolve || !dependencyNode2.readyToSolve) {
                                    return;
                                }
                                computeInsetRatio(tempDimensions, dependencyNode4.value + dependencyNode4.margin, dependencyNode5.value - dependencyNode5.margin, dependencyNode.targets.get(0).value + dependencyNode.margin, dependencyNode2.targets.get(0).value - dependencyNode2.margin, dimensionRatio2, dimensionRatioSide);
                                this.dimension.resolve(tempDimensions[0]);
                                this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
                            }
                        }
                        DependencyNode dependencyNode6 = this.start;
                        if (!dependencyNode6.readyToSolve || !this.end.readyToSolve || !dependencyNode.readyToSolve || !dependencyNode2.readyToSolve) {
                            return;
                        }
                        computeInsetRatio(tempDimensions, dependencyNode6.targets.get(0).value + this.start.margin, this.end.targets.get(0).value - this.end.margin, dependencyNode.targets.get(0).value + dependencyNode.margin, dependencyNode2.targets.get(0).value - dependencyNode2.margin, dimensionRatio2, dimensionRatioSide);
                        this.dimension.resolve(tempDimensions[0]);
                        this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
                    } else if (!z || !z3) {
                        if (z2 && z4) {
                            if (!dependencyNode.readyToSolve || !dependencyNode2.readyToSolve) {
                                return;
                            }
                            float dimensionRatio3 = this.widget.getDimensionRatio();
                            int i5 = dependencyNode.targets.get(0).value + dependencyNode.margin;
                            int i6 = dependencyNode2.targets.get(0).value - dependencyNode2.margin;
                            if (dimensionRatioSide != -1) {
                                if (dimensionRatioSide == 0) {
                                    int limitedDimension = getLimitedDimension(i6 - i5, 1);
                                    int i7 = (int) ((limitedDimension * dimensionRatio3) + 0.5f);
                                    int limitedDimension2 = getLimitedDimension(i7, 0);
                                    if (i7 != limitedDimension2) {
                                        limitedDimension = (int) ((limitedDimension2 / dimensionRatio3) + 0.5f);
                                    }
                                    this.dimension.resolve(limitedDimension2);
                                    this.widget.verticalRun.dimension.resolve(limitedDimension);
                                }
                            }
                            int limitedDimension3 = getLimitedDimension(i6 - i5, 1);
                            int i8 = (int) ((limitedDimension3 / dimensionRatio3) + 0.5f);
                            int limitedDimension4 = getLimitedDimension(i8, 0);
                            if (i8 != limitedDimension4) {
                                limitedDimension3 = (int) ((limitedDimension4 * dimensionRatio3) + 0.5f);
                            }
                            this.dimension.resolve(limitedDimension4);
                            this.widget.verticalRun.dimension.resolve(limitedDimension3);
                        }
                    } else if (!this.start.readyToSolve || !this.end.readyToSolve) {
                        return;
                    } else {
                        float dimensionRatio4 = this.widget.getDimensionRatio();
                        int i9 = this.start.targets.get(0).value + this.start.margin;
                        int i10 = this.end.targets.get(0).value - this.end.margin;
                        if (dimensionRatioSide == -1 || dimensionRatioSide == 0) {
                            int limitedDimension5 = getLimitedDimension(i10 - i9, 0);
                            int i11 = (int) ((limitedDimension5 * dimensionRatio4) + 0.5f);
                            int limitedDimension6 = getLimitedDimension(i11, 1);
                            if (i11 != limitedDimension6) {
                                limitedDimension5 = (int) ((limitedDimension6 / dimensionRatio4) + 0.5f);
                            }
                            this.dimension.resolve(limitedDimension5);
                            this.widget.verticalRun.dimension.resolve(limitedDimension6);
                        } else if (dimensionRatioSide == 1) {
                            int limitedDimension7 = getLimitedDimension(i10 - i9, 0);
                            int i12 = (int) ((limitedDimension7 / dimensionRatio4) + 0.5f);
                            int limitedDimension8 = getLimitedDimension(i12, 1);
                            if (i12 != limitedDimension8) {
                                limitedDimension7 = (int) ((limitedDimension8 * dimensionRatio4) + 0.5f);
                            }
                            this.dimension.resolve(limitedDimension7);
                            this.widget.verticalRun.dimension.resolve(limitedDimension8);
                        }
                    }
                } else {
                    int dimensionRatioSide2 = constraintWidget3.getDimensionRatioSide();
                    if (dimensionRatioSide2 == -1) {
                        ConstraintWidget constraintWidget4 = this.widget;
                        f = constraintWidget4.verticalRun.dimension.value;
                        dimensionRatio = constraintWidget4.getDimensionRatio();
                    } else if (dimensionRatioSide2 == 0) {
                        f2 = constraintWidget.verticalRun.dimension.value / this.widget.getDimensionRatio();
                        i = (int) (f2 + 0.5f);
                        this.dimension.resolve(i);
                    } else if (dimensionRatioSide2 == 1) {
                        ConstraintWidget constraintWidget5 = this.widget;
                        f = constraintWidget5.verticalRun.dimension.value;
                        dimensionRatio = constraintWidget5.getDimensionRatio();
                    } else {
                        i = 0;
                        this.dimension.resolve(i);
                    }
                    f2 = f * dimensionRatio;
                    i = (int) (f2 + 0.5f);
                    this.dimension.resolve(i);
                }
            }
        }
        DependencyNode dependencyNode7 = this.start;
        if (dependencyNode7.readyToSolve) {
            DependencyNode dependencyNode8 = this.end;
            if (!dependencyNode8.readyToSolve) {
                return;
            }
            if (dependencyNode7.resolved && dependencyNode8.resolved && this.dimension.resolved) {
                return;
            }
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                ConstraintWidget constraintWidget6 = this.widget;
                if (constraintWidget6.mMatchConstraintDefaultWidth == 0 && !constraintWidget6.isInHorizontalChain()) {
                    int i13 = this.start.targets.get(0).value;
                    DependencyNode dependencyNode9 = this.start;
                    int i14 = i13 + dependencyNode9.margin;
                    int i15 = this.end.targets.get(0).value + this.end.margin;
                    dependencyNode9.resolve(i14);
                    this.end.resolve(i15);
                    this.dimension.resolve(i15 - i14);
                    return;
                }
            }
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                int min = Math.min((this.end.targets.get(0).value + this.end.margin) - (this.start.targets.get(0).value + this.start.margin), this.dimension.wrapValue);
                ConstraintWidget constraintWidget7 = this.widget;
                int i16 = constraintWidget7.mMatchConstraintMaxWidth;
                int max = Math.max(constraintWidget7.mMatchConstraintMinWidth, min);
                if (i16 > 0) {
                    max = Math.min(i16, max);
                }
                this.dimension.resolve(max);
            }
            if (!this.dimension.resolved) {
                return;
            }
            DependencyNode dependencyNode10 = this.start.targets.get(0);
            DependencyNode dependencyNode11 = this.end.targets.get(0);
            int i17 = dependencyNode10.value + this.start.margin;
            int i18 = dependencyNode11.value + this.end.margin;
            float horizontalBiasPercent = this.widget.getHorizontalBiasPercent();
            if (dependencyNode10 == dependencyNode11) {
                i17 = dependencyNode10.value;
                i18 = dependencyNode11.value;
                horizontalBiasPercent = 0.5f;
            }
            this.start.resolve((int) (i17 + 0.5f + (((i18 - i17) - this.dimension.value) * horizontalBiasPercent)));
            this.end.resolve(this.start.value + this.dimension.value);
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public void applyToWidget() {
        DependencyNode dependencyNode = this.start;
        if (dependencyNode.resolved) {
            this.widget.setX(dependencyNode.value);
        }
    }
}
