package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class ChainRun extends WidgetRun {
    private int chainStyle;
    ArrayList<WidgetRun> widgets = new ArrayList<>();

    public ChainRun(ConstraintWidget constraintWidget, int i) {
        super(constraintWidget);
        this.orientation = i;
        build();
    }

    public String toString() {
        Iterator<WidgetRun> it;
        StringBuilder sb = new StringBuilder();
        sb.append("ChainRun ");
        sb.append(this.orientation == 0 ? "horizontal : " : "vertical : ");
        String sb2 = sb.toString();
        while (this.widgets.iterator().hasNext()) {
            sb2 = ((sb2 + "<") + it.next()) + "> ";
        }
        return sb2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public boolean supportsWrapComputation() {
        int size = this.widgets.size();
        for (int i = 0; i < size; i++) {
            if (!this.widgets.get(i).supportsWrapComputation()) {
                return false;
            }
        }
        return true;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public long getWrapDimension() {
        int size = this.widgets.size();
        long j = 0;
        for (int i = 0; i < size; i++) {
            WidgetRun widgetRun = this.widgets.get(i);
            j = j + widgetRun.start.margin + widgetRun.getWrapDimension() + widgetRun.end.margin;
        }
        return j;
    }

    private void build() {
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2 = this.widget;
        ConstraintWidget previousChainMember = constraintWidget2.getPreviousChainMember(this.orientation);
        while (true) {
            ConstraintWidget constraintWidget3 = previousChainMember;
            constraintWidget = constraintWidget2;
            constraintWidget2 = constraintWidget3;
            if (constraintWidget2 == null) {
                break;
            }
            previousChainMember = constraintWidget2.getPreviousChainMember(this.orientation);
        }
        this.widget = constraintWidget;
        this.widgets.add(constraintWidget.getRun(this.orientation));
        ConstraintWidget nextChainMember = constraintWidget.getNextChainMember(this.orientation);
        while (nextChainMember != null) {
            this.widgets.add(nextChainMember.getRun(this.orientation));
            nextChainMember = nextChainMember.getNextChainMember(this.orientation);
        }
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            WidgetRun next = it.next();
            int i = this.orientation;
            if (i == 0) {
                next.widget.horizontalChainRun = this;
            } else if (i == 1) {
                next.widget.verticalChainRun = this;
            }
        }
        if ((this.orientation == 0 && ((ConstraintWidgetContainer) this.widget.getParent()).isRtl()) && this.widgets.size() > 1) {
            ArrayList<WidgetRun> arrayList = this.widgets;
            this.widget = arrayList.get(arrayList.size() - 1).widget;
        }
        this.chainStyle = this.orientation == 0 ? this.widget.getHorizontalChainStyle() : this.widget.getVerticalChainStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public void clear() {
        this.runGroup = null;
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            it.next().clear();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x01a5, code lost:
        if (r1 != r7) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x01d0, code lost:
        r9.dimension.resolve(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x01cd, code lost:
        r13 = r13 + 1;
        r7 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x01cb, code lost:
        if (r1 != r7) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0418, code lost:
        r7 = r7 - r10;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00eb  */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun, androidx.constraintlayout.solver.widgets.analyzer.Dependency
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(Dependency dependency) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        float f;
        boolean z;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        boolean z2;
        int i11;
        int i12;
        float f2;
        int i13;
        int max;
        int i14;
        int i15;
        if (!this.start.resolved || !this.end.resolved) {
            return;
        }
        ConstraintWidget parent = this.widget.getParent();
        boolean isRtl = (parent == null || !(parent instanceof ConstraintWidgetContainer)) ? false : ((ConstraintWidgetContainer) parent).isRtl();
        int i16 = this.end.value - this.start.value;
        int size = this.widgets.size();
        int i17 = 0;
        while (true) {
            i = -1;
            i2 = 8;
            if (i17 >= size) {
                i17 = -1;
                break;
            } else if (this.widgets.get(i17).widget.getVisibility() != 8) {
                break;
            } else {
                i17++;
            }
        }
        int i18 = size - 1;
        int i19 = i18;
        while (true) {
            if (i19 < 0) {
                break;
            }
            if (this.widgets.get(i19).widget.getVisibility() != 8) {
                i = i19;
                break;
            }
            i19--;
        }
        int i20 = 0;
        while (i20 < 2) {
            int i21 = 0;
            i4 = 0;
            i5 = 0;
            int i22 = 0;
            f = 0.0f;
            while (i21 < size) {
                WidgetRun widgetRun = this.widgets.get(i21);
                if (widgetRun.widget.getVisibility() != i2) {
                    i22++;
                    if (i21 > 0 && i21 >= i17) {
                        i4 += widgetRun.start.margin;
                    }
                    DimensionDependency dimensionDependency = widgetRun.dimension;
                    int i23 = dimensionDependency.value;
                    boolean z3 = widgetRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                    if (z3) {
                        int i24 = this.orientation;
                        if (i24 == 0 && !widgetRun.widget.horizontalRun.dimension.resolved) {
                            return;
                        }
                        if (i24 == 1 && !widgetRun.widget.verticalRun.dimension.resolved) {
                            return;
                        }
                        i14 = i23;
                    } else {
                        i14 = i23;
                        if (widgetRun.matchConstraintsType == 1 && i20 == 0) {
                            i15 = dimensionDependency.wrapValue;
                            i5++;
                        } else if (dimensionDependency.resolved) {
                            i15 = i14;
                        }
                        z3 = true;
                        if (z3) {
                            i5++;
                            float f3 = widgetRun.widget.mWeight[this.orientation];
                            if (f3 >= 0.0f) {
                                f += f3;
                            }
                        } else {
                            i4 += i15;
                        }
                        if (i21 < i18 && i21 < i) {
                            i4 += -widgetRun.end.margin;
                        }
                    }
                    i15 = i14;
                    if (z3) {
                    }
                    if (i21 < i18) {
                        i4 += -widgetRun.end.margin;
                    }
                }
                i21++;
                i2 = 8;
            }
            if (i4 < i16 || i5 == 0) {
                i3 = i22;
                break;
            } else {
                i20++;
                i2 = 8;
            }
        }
        i3 = 0;
        i4 = 0;
        i5 = 0;
        f = 0.0f;
        int i25 = this.start.value;
        if (isRtl) {
            i25 = this.end.value;
        }
        if (i4 > i16) {
            i25 = isRtl ? i25 + ((int) (((i4 - i16) / 2.0f) + 0.5f)) : i25 - ((int) (((i4 - i16) / 2.0f) + 0.5f));
        }
        if (i5 > 0) {
            float f4 = i16 - i4;
            int i26 = (int) ((f4 / i5) + 0.5f);
            int i27 = 0;
            int i28 = 0;
            while (i27 < size) {
                WidgetRun widgetRun2 = this.widgets.get(i27);
                int i29 = i26;
                int i30 = i4;
                if (widgetRun2.widget.getVisibility() != 8 && widgetRun2.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    DimensionDependency dimensionDependency2 = widgetRun2.dimension;
                    if (!dimensionDependency2.resolved) {
                        if (f > 0.0f) {
                            i12 = i25;
                            i13 = (int) (((widgetRun2.widget.mWeight[this.orientation] * f4) / f) + 0.5f);
                        } else {
                            i12 = i25;
                            i13 = i29;
                        }
                        if (this.orientation == 0) {
                            ConstraintWidget constraintWidget = widgetRun2.widget;
                            f2 = f4;
                            int i31 = constraintWidget.mMatchConstraintMaxWidth;
                            z2 = isRtl;
                            i11 = i3;
                            max = Math.max(constraintWidget.mMatchConstraintMinWidth, widgetRun2.matchConstraintsType == 1 ? Math.min(i13, dimensionDependency2.wrapValue) : i13);
                            if (i31 > 0) {
                                max = Math.min(i31, max);
                            }
                        } else {
                            z2 = isRtl;
                            i11 = i3;
                            f2 = f4;
                            ConstraintWidget constraintWidget2 = widgetRun2.widget;
                            int i32 = constraintWidget2.mMatchConstraintMaxHeight;
                            max = Math.max(constraintWidget2.mMatchConstraintMinHeight, widgetRun2.matchConstraintsType == 1 ? Math.min(i13, dimensionDependency2.wrapValue) : i13);
                            if (i32 > 0) {
                                max = Math.min(i32, max);
                            }
                        }
                    }
                }
                z2 = isRtl;
                i11 = i3;
                i12 = i25;
                f2 = f4;
                i27++;
                i26 = i29;
                i4 = i30;
                i25 = i12;
                f4 = f2;
                isRtl = z2;
                i3 = i11;
            }
            z = isRtl;
            i6 = i3;
            i7 = i25;
            int i33 = i4;
            if (i28 > 0) {
                i5 -= i28;
                int i34 = 0;
                for (int i35 = 0; i35 < size; i35++) {
                    WidgetRun widgetRun3 = this.widgets.get(i35);
                    if (widgetRun3.widget.getVisibility() != 8) {
                        if (i35 > 0 && i35 >= i17) {
                            i34 += widgetRun3.start.margin;
                        }
                        i34 += widgetRun3.dimension.value;
                        if (i35 < i18 && i35 < i) {
                            i34 += -widgetRun3.end.margin;
                        }
                    }
                }
                i4 = i34;
            } else {
                i4 = i33;
            }
            i9 = 2;
            if (this.chainStyle == 2 && i28 == 0) {
                i8 = 0;
                this.chainStyle = 0;
            } else {
                i8 = 0;
            }
        } else {
            z = isRtl;
            i6 = i3;
            i7 = i25;
            i8 = 0;
            i9 = 2;
        }
        if (i4 > i16) {
            this.chainStyle = i9;
        }
        if (i6 > 0 && i5 == 0 && i17 == i) {
            this.chainStyle = i9;
        }
        int i36 = this.chainStyle;
        if (i36 == 1) {
            int i37 = i6;
            if (i37 > 1) {
                i10 = (i16 - i4) / (i37 - 1);
            } else {
                i10 = i37 == 1 ? (i16 - i4) / 2 : i8;
            }
            if (i5 > 0) {
                i10 = i8;
            }
            int i38 = i7;
            for (int i39 = i8; i39 < size; i39++) {
                WidgetRun widgetRun4 = this.widgets.get(z ? size - (i39 + 1) : i39);
                if (widgetRun4.widget.getVisibility() == 8) {
                    widgetRun4.start.resolve(i38);
                    widgetRun4.end.resolve(i38);
                } else {
                    if (i39 > 0) {
                        i38 = z ? i38 - i10 : i38 + i10;
                    }
                    if (i39 > 0 && i39 >= i17) {
                        if (z) {
                            i38 -= widgetRun4.start.margin;
                        } else {
                            i38 += widgetRun4.start.margin;
                        }
                    }
                    if (z) {
                        widgetRun4.end.resolve(i38);
                    } else {
                        widgetRun4.start.resolve(i38);
                    }
                    DimensionDependency dimensionDependency3 = widgetRun4.dimension;
                    int i40 = dimensionDependency3.value;
                    if (widgetRun4.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun4.matchConstraintsType == 1) {
                        i40 = dimensionDependency3.wrapValue;
                    }
                    i38 = z ? i38 - i40 : i38 + i40;
                    if (z) {
                        widgetRun4.start.resolve(i38);
                    } else {
                        widgetRun4.end.resolve(i38);
                    }
                    widgetRun4.resolved = true;
                    if (i39 < i18 && i39 < i) {
                        if (z) {
                            i38 -= -widgetRun4.end.margin;
                        } else {
                            i38 += -widgetRun4.end.margin;
                        }
                    }
                }
            }
            return;
        }
        int i41 = i6;
        if (i36 == 0) {
            int i42 = (i16 - i4) / (i41 + 1);
            if (i5 > 0) {
                i42 = i8;
            }
            int i43 = i7;
            for (int i44 = i8; i44 < size; i44++) {
                WidgetRun widgetRun5 = this.widgets.get(z ? size - (i44 + 1) : i44);
                if (widgetRun5.widget.getVisibility() == 8) {
                    widgetRun5.start.resolve(i43);
                    widgetRun5.end.resolve(i43);
                } else {
                    int i45 = z ? i43 - i42 : i43 + i42;
                    if (i44 > 0 && i44 >= i17) {
                        if (z) {
                            i45 -= widgetRun5.start.margin;
                        } else {
                            i45 += widgetRun5.start.margin;
                        }
                    }
                    if (z) {
                        widgetRun5.end.resolve(i45);
                    } else {
                        widgetRun5.start.resolve(i45);
                    }
                    DimensionDependency dimensionDependency4 = widgetRun5.dimension;
                    int i46 = dimensionDependency4.value;
                    if (widgetRun5.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun5.matchConstraintsType == 1) {
                        i46 = Math.min(i46, dimensionDependency4.wrapValue);
                    }
                    i43 = z ? i45 - i46 : i45 + i46;
                    if (z) {
                        widgetRun5.start.resolve(i43);
                    } else {
                        widgetRun5.end.resolve(i43);
                    }
                    if (i44 < i18 && i44 < i) {
                        if (z) {
                            i43 -= -widgetRun5.end.margin;
                        } else {
                            i43 += -widgetRun5.end.margin;
                        }
                    }
                }
            }
        } else if (i36 == 2) {
            float horizontalBiasPercent = this.orientation == 0 ? this.widget.getHorizontalBiasPercent() : this.widget.getVerticalBiasPercent();
            if (z) {
                horizontalBiasPercent = 1.0f - horizontalBiasPercent;
            }
            int i47 = (int) (((i16 - i4) * horizontalBiasPercent) + 0.5f);
            if (i47 < 0 || i5 > 0) {
                i47 = i8;
            }
            int i48 = z ? i7 - i47 : i7 + i47;
            for (int i49 = i8; i49 < size; i49++) {
                WidgetRun widgetRun6 = this.widgets.get(z ? size - (i49 + 1) : i49);
                if (widgetRun6.widget.getVisibility() == 8) {
                    widgetRun6.start.resolve(i48);
                    widgetRun6.end.resolve(i48);
                } else {
                    if (i49 > 0 && i49 >= i17) {
                        if (z) {
                            i48 -= widgetRun6.start.margin;
                        } else {
                            i48 += widgetRun6.start.margin;
                        }
                    }
                    if (z) {
                        widgetRun6.end.resolve(i48);
                    } else {
                        widgetRun6.start.resolve(i48);
                    }
                    DimensionDependency dimensionDependency5 = widgetRun6.dimension;
                    int i50 = dimensionDependency5.value;
                    if (widgetRun6.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun6.matchConstraintsType == 1) {
                        i50 = dimensionDependency5.wrapValue;
                    }
                    i48 += i50;
                    if (z) {
                        widgetRun6.start.resolve(i48);
                    } else {
                        widgetRun6.end.resolve(i48);
                    }
                    if (i49 < i18 && i49 < i) {
                        if (z) {
                            i48 -= -widgetRun6.end.margin;
                        } else {
                            i48 += -widgetRun6.end.margin;
                        }
                    }
                }
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public void applyToWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            this.widgets.get(i).applyToWidget();
        }
    }

    private ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            WidgetRun widgetRun = this.widgets.get(i);
            if (widgetRun.widget.getVisibility() != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    private ConstraintWidget getLastVisibleWidget() {
        for (int size = this.widgets.size() - 1; size >= 0; size--) {
            WidgetRun widgetRun = this.widgets.get(size);
            if (widgetRun.widget.getVisibility() != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public void apply() {
        Iterator<WidgetRun> it = this.widgets.iterator();
        while (it.hasNext()) {
            it.next().apply();
        }
        int size = this.widgets.size();
        if (size < 1) {
            return;
        }
        ConstraintWidget constraintWidget = this.widgets.get(0).widget;
        ConstraintWidget constraintWidget2 = this.widgets.get(size - 1).widget;
        if (this.orientation == 0) {
            ConstraintAnchor constraintAnchor = constraintWidget.mLeft;
            ConstraintAnchor constraintAnchor2 = constraintWidget2.mRight;
            DependencyNode target = getTarget(constraintAnchor, 0);
            int margin = constraintAnchor.getMargin();
            ConstraintWidget firstVisibleWidget = getFirstVisibleWidget();
            if (firstVisibleWidget != null) {
                margin = firstVisibleWidget.mLeft.getMargin();
            }
            if (target != null) {
                addTarget(this.start, target, margin);
            }
            DependencyNode target2 = getTarget(constraintAnchor2, 0);
            int margin2 = constraintAnchor2.getMargin();
            ConstraintWidget lastVisibleWidget = getLastVisibleWidget();
            if (lastVisibleWidget != null) {
                margin2 = lastVisibleWidget.mRight.getMargin();
            }
            if (target2 != null) {
                addTarget(this.end, target2, -margin2);
            }
        } else {
            ConstraintAnchor constraintAnchor3 = constraintWidget.mTop;
            ConstraintAnchor constraintAnchor4 = constraintWidget2.mBottom;
            DependencyNode target3 = getTarget(constraintAnchor3, 1);
            int margin3 = constraintAnchor3.getMargin();
            ConstraintWidget firstVisibleWidget2 = getFirstVisibleWidget();
            if (firstVisibleWidget2 != null) {
                margin3 = firstVisibleWidget2.mTop.getMargin();
            }
            if (target3 != null) {
                addTarget(this.start, target3, margin3);
            }
            DependencyNode target4 = getTarget(constraintAnchor4, 1);
            int margin4 = constraintAnchor4.getMargin();
            ConstraintWidget lastVisibleWidget2 = getLastVisibleWidget();
            if (lastVisibleWidget2 != null) {
                margin4 = lastVisibleWidget2.mBottom.getMargin();
            }
            if (target4 != null) {
                addTarget(this.end, target4, -margin4);
            }
        }
        this.start.updateDelegate = this;
        this.end.updateDelegate = this;
    }
}
