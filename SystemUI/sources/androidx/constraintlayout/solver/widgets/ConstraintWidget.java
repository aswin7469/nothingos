package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ConstraintWidget {
    public static float DEFAULT_BIAS = 0.5f;
    public ChainRun horizontalChainRun;
    private boolean inPlaceholder;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    private Object mCompanionWidget;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    boolean mHorizontalWrapVisited;
    public boolean mIsHeightWrapContent;
    public boolean mIsWidthWrapContent;
    boolean mLeftHasCentered;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    protected int mMinHeight;
    protected int mMinWidth;
    boolean mRightHasCentered;
    boolean mTopHasCentered;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    boolean mVerticalWrapVisited;
    public ChainRun verticalChainRun;
    public boolean measured = false;
    public WidgetRun[] run = new WidgetRun[2];
    public HorizontalWidgetRun horizontalRun = new HorizontalWidgetRun(this);
    public VerticalWidgetRun verticalRun = new VerticalWidgetRun(this);
    public boolean[] isTerminalWidget = {true, true};
    public int[] wrapMeasure = {0, 0};
    boolean mResolvedHasRatio = false;
    public int mHorizontalResolution = -1;
    public int mVerticalResolution = -1;
    public int mMatchConstraintDefaultWidth = 0;
    public int mMatchConstraintDefaultHeight = 0;
    public int[] mResolvedMatchConstraintDefault = new int[2];
    public int mMatchConstraintMinWidth = 0;
    public int mMatchConstraintMaxWidth = 0;
    public float mMatchConstraintPercentWidth = 1.0f;
    public int mMatchConstraintMinHeight = 0;
    public int mMatchConstraintMaxHeight = 0;
    public float mMatchConstraintPercentHeight = 1.0f;
    int mResolvedDimensionRatioSide = -1;
    float mResolvedDimensionRatio = 1.0f;
    private int[] mMaxDimension = {Integer.MAX_VALUE, Integer.MAX_VALUE};
    private float mCircleConstraintAngle = 0.0f;
    private boolean hasBaseline = false;
    private boolean mInVirtuaLayout = false;
    public ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
    public ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
    public ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
    public ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
    ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
    protected ArrayList<ConstraintAnchor> mAnchors = new ArrayList<>();
    public ConstraintWidget mParent = null;
    int mWidth = 0;
    int mHeight = 0;
    public float mDimensionRatio = 0.0f;
    protected int mDimensionRatioSide = -1;
    protected int mX = 0;
    protected int mY = 0;
    int mRelX = 0;
    int mRelY = 0;
    protected int mOffsetX = 0;
    protected int mOffsetY = 0;
    int mBaselineDistance = 0;
    private int mContainerItemSkip = 0;
    private int mVisibility = 0;
    private String mDebugName = null;
    private String mType = null;
    boolean mOptimizerMeasurable = false;
    boolean mGroupsToSolver = false;
    int mHorizontalChainStyle = 0;
    int mVerticalChainStyle = 0;
    public float[] mWeight = {-1.0f, -1.0f};
    protected ConstraintWidget[] mListNextMatchConstraintsWidget = {null, null};
    protected ConstraintWidget[] mNextChainWidget = {null, null};
    ConstraintWidget mHorizontalNextWidget = null;
    ConstraintWidget mVerticalNextWidget = null;

    /* loaded from: classes.dex */
    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public WidgetRun getRun(int i) {
        if (i == 0) {
            return this.horizontalRun;
        }
        if (i != 1) {
            return null;
        }
        return this.verticalRun;
    }

    public void setInVirtualLayout(boolean z) {
        this.mInVirtuaLayout = z;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public void setMaxWidth(int i) {
        this.mMaxDimension[0] = i;
    }

    public void setMaxHeight(int i) {
        this.mMaxDimension[1] = i;
    }

    public void setHasBaseline(boolean z) {
        this.hasBaseline = z;
    }

    public void setInPlaceholder(boolean z) {
        this.inPlaceholder = z;
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        dimensionBehaviourArr[0] = dimensionBehaviour;
        dimensionBehaviourArr[1] = dimensionBehaviour;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedHasRatio = false;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mOptimizerMeasurable = false;
        this.mGroupsToSolver = false;
        boolean[] zArr = this.isTerminalWidget;
        zArr[0] = true;
        zArr[1] = true;
        this.mInVirtuaLayout = false;
    }

    public ConstraintWidget() {
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors = new DimensionBehaviour[]{dimensionBehaviour, dimensionBehaviour};
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        addAnchors();
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int i) {
        ConstraintAnchor.Type type = ConstraintAnchor.Type.CENTER;
        immediateConnect(type, constraintWidget, type, i, 0);
        this.mCircleConstraintAngle = f;
    }

    public void setVisibility(int i) {
        this.mVisibility = i;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String str) {
        this.mDebugName = str;
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (this.mType != null) {
            str = "type: " + this.mType + " ";
        } else {
            str = str2;
        }
        sb.append(str);
        if (this.mDebugName != null) {
            str2 = "id: " + this.mDebugName + " ";
        }
        sb.append(str2);
        sb.append("(");
        sb.append(this.mX);
        sb.append(", ");
        sb.append(this.mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(")");
        return sb.toString();
    }

    public int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget != null && (constraintWidget instanceof ConstraintWidgetContainer)) {
            return ((ConstraintWidgetContainer) constraintWidget).mPaddingLeft + this.mX;
        }
        return this.mX;
    }

    public int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget != null && (constraintWidget instanceof ConstraintWidgetContainer)) {
            return ((ConstraintWidgetContainer) constraintWidget).mPaddingTop + this.mY;
        }
        return this.mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getLength(int i) {
        if (i == 0) {
            return getWidth();
        }
        if (i != 1) {
            return 0;
        }
        return getHeight();
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getLeft() {
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public int getHorizontalMargin() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        int i = 0;
        if (constraintAnchor != null) {
            i = 0 + constraintAnchor.mMargin;
        }
        ConstraintAnchor constraintAnchor2 = this.mRight;
        return constraintAnchor2 != null ? i + constraintAnchor2.mMargin : i;
    }

    public int getVerticalMargin() {
        int i = 0;
        if (this.mLeft != null) {
            i = 0 + this.mTop.mMargin;
        }
        return this.mRight != null ? i + this.mBottom.mMargin : i;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public float getBiasPercent(int i) {
        if (i == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (i != 1) {
            return -1.0f;
        }
        return this.mVerticalBiasPercent;
    }

    public boolean hasBaseline() {
        return this.hasBaseline;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public void setX(int i) {
        this.mX = i;
    }

    public void setY(int i) {
        this.mY = i;
    }

    public void setOrigin(int i, int i2) {
        this.mX = i;
        this.mY = i2;
    }

    public void setWidth(int i) {
        this.mWidth = i;
        int i2 = this.mMinWidth;
        if (i < i2) {
            this.mWidth = i2;
        }
    }

    public void setHeight(int i) {
        this.mHeight = i;
        int i2 = this.mMinHeight;
        if (i < i2) {
            this.mHeight = i2;
        }
    }

    public void setHorizontalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultWidth = i;
        this.mMatchConstraintMinWidth = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxWidth = i3;
        this.mMatchConstraintPercentWidth = f;
        if (f <= 0.0f || f >= 1.0f || i != 0) {
            return;
        }
        this.mMatchConstraintDefaultWidth = 2;
    }

    public void setVerticalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultHeight = i;
        this.mMatchConstraintMinHeight = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxHeight = i3;
        this.mMatchConstraintPercentHeight = f;
        if (f <= 0.0f || f >= 1.0f || i != 0) {
            return;
        }
        this.mMatchConstraintDefaultHeight = 2;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x0084 -> B:31:0x0085). Please submit an issue!!! */
    public void setDimensionRatio(String str) {
        float f;
        int i = 0;
        if (str == null || str.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int i2 = -1;
        int length = str.length();
        int indexOf = str.indexOf(44);
        int i3 = 0;
        if (indexOf > 0 && indexOf < length - 1) {
            String substring = str.substring(0, indexOf);
            if (substring.equalsIgnoreCase("W")) {
                i2 = 0;
            } else if (substring.equalsIgnoreCase("H")) {
                i2 = 1;
            }
            i3 = indexOf + 1;
        }
        int indexOf2 = str.indexOf(58);
        if (indexOf2 >= 0 && indexOf2 < length - 1) {
            String substring2 = str.substring(i3, indexOf2);
            String substring3 = str.substring(indexOf2 + 1);
            if (substring2.length() > 0 && substring3.length() > 0) {
                float parseFloat = Float.parseFloat(substring2);
                float parseFloat2 = Float.parseFloat(substring3);
                if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                    if (i2 == 1) {
                        f = Math.abs(parseFloat2 / parseFloat);
                    } else {
                        f = Math.abs(parseFloat / parseFloat2);
                    }
                }
            }
            f = i;
        } else {
            String substring4 = str.substring(i3);
            if (substring4.length() > 0) {
                f = Float.parseFloat(substring4);
            }
            f = i;
        }
        i = (f > i ? 1 : (f == i ? 0 : -1));
        if (i <= 0) {
            return;
        }
        this.mDimensionRatio = f;
        this.mDimensionRatioSide = i2;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setMinWidth(int i) {
        if (i < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = i;
        }
    }

    public void setMinHeight(int i) {
        if (i < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = i;
        }
    }

    public void setFrame(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = i3 - i;
        int i8 = i4 - i2;
        this.mX = i;
        this.mY = i2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour2 = DimensionBehaviour.FIXED;
        if (dimensionBehaviour == dimensionBehaviour2 && i7 < (i6 = this.mWidth)) {
            i7 = i6;
        }
        if (dimensionBehaviourArr[1] == dimensionBehaviour2 && i8 < (i5 = this.mHeight)) {
            i8 = i5;
        }
        this.mWidth = i7;
        this.mHeight = i8;
        int i9 = this.mMinHeight;
        if (i8 < i9) {
            this.mHeight = i9;
        }
        int i10 = this.mMinWidth;
        if (i7 >= i10) {
            return;
        }
        this.mWidth = i10;
    }

    public void setHorizontalDimension(int i, int i2) {
        this.mX = i;
        int i3 = i2 - i;
        this.mWidth = i3;
        int i4 = this.mMinWidth;
        if (i3 < i4) {
            this.mWidth = i4;
        }
    }

    public void setVerticalDimension(int i, int i2) {
        this.mY = i;
        int i3 = i2 - i;
        this.mHeight = i3;
        int i4 = this.mMinHeight;
        if (i3 < i4) {
            this.mHeight = i4;
        }
    }

    public void setBaselineDistance(int i) {
        this.mBaselineDistance = i;
        this.hasBaseline = i > 0;
    }

    public void setCompanionWidget(Object obj) {
        this.mCompanionWidget = obj;
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setHorizontalChainStyle(int i) {
        this.mHorizontalChainStyle = i;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int i) {
        this.mVerticalChainStyle = i;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i, int i2) {
        getAnchor(type).connect(constraintWidget.getAnchor(type2), i, i2, true);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i) {
        if (constraintAnchor.getOwner() == this) {
            connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), i);
        }
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i) {
        ConstraintAnchor.Type type3;
        ConstraintAnchor.Type type4;
        boolean z;
        ConstraintAnchor.Type type5 = ConstraintAnchor.Type.CENTER;
        if (type == type5) {
            if (type2 == type5) {
                ConstraintAnchor.Type type6 = ConstraintAnchor.Type.LEFT;
                ConstraintAnchor anchor = getAnchor(type6);
                ConstraintAnchor.Type type7 = ConstraintAnchor.Type.RIGHT;
                ConstraintAnchor anchor2 = getAnchor(type7);
                ConstraintAnchor.Type type8 = ConstraintAnchor.Type.TOP;
                ConstraintAnchor anchor3 = getAnchor(type8);
                ConstraintAnchor.Type type9 = ConstraintAnchor.Type.BOTTOM;
                ConstraintAnchor anchor4 = getAnchor(type9);
                boolean z2 = true;
                if ((anchor == null || !anchor.isConnected()) && (anchor2 == null || !anchor2.isConnected())) {
                    connect(type6, constraintWidget, type6, 0);
                    connect(type7, constraintWidget, type7, 0);
                    z = true;
                } else {
                    z = false;
                }
                if ((anchor3 == null || !anchor3.isConnected()) && (anchor4 == null || !anchor4.isConnected())) {
                    connect(type8, constraintWidget, type8, 0);
                    connect(type9, constraintWidget, type9, 0);
                } else {
                    z2 = false;
                }
                if (z && z2) {
                    getAnchor(type5).connect(constraintWidget.getAnchor(type5), 0);
                    return;
                } else if (z) {
                    ConstraintAnchor.Type type10 = ConstraintAnchor.Type.CENTER_X;
                    getAnchor(type10).connect(constraintWidget.getAnchor(type10), 0);
                    return;
                } else if (!z2) {
                    return;
                } else {
                    ConstraintAnchor.Type type11 = ConstraintAnchor.Type.CENTER_Y;
                    getAnchor(type11).connect(constraintWidget.getAnchor(type11), 0);
                    return;
                }
            }
            ConstraintAnchor.Type type12 = ConstraintAnchor.Type.LEFT;
            if (type2 == type12 || type2 == ConstraintAnchor.Type.RIGHT) {
                connect(type12, constraintWidget, type2, 0);
                connect(ConstraintAnchor.Type.RIGHT, constraintWidget, type2, 0);
                getAnchor(type5).connect(constraintWidget.getAnchor(type2), 0);
                return;
            }
            ConstraintAnchor.Type type13 = ConstraintAnchor.Type.TOP;
            if (type2 != type13 && type2 != ConstraintAnchor.Type.BOTTOM) {
                return;
            }
            connect(type13, constraintWidget, type2, 0);
            connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, type2, 0);
            getAnchor(type5).connect(constraintWidget.getAnchor(type2), 0);
            return;
        }
        ConstraintAnchor.Type type14 = ConstraintAnchor.Type.CENTER_X;
        if (type == type14 && (type2 == (type4 = ConstraintAnchor.Type.LEFT) || type2 == ConstraintAnchor.Type.RIGHT)) {
            ConstraintAnchor anchor5 = getAnchor(type4);
            ConstraintAnchor anchor6 = constraintWidget.getAnchor(type2);
            ConstraintAnchor anchor7 = getAnchor(ConstraintAnchor.Type.RIGHT);
            anchor5.connect(anchor6, 0);
            anchor7.connect(anchor6, 0);
            getAnchor(type14).connect(anchor6, 0);
            return;
        }
        ConstraintAnchor.Type type15 = ConstraintAnchor.Type.CENTER_Y;
        if (type == type15 && (type2 == (type3 = ConstraintAnchor.Type.TOP) || type2 == ConstraintAnchor.Type.BOTTOM)) {
            ConstraintAnchor anchor8 = constraintWidget.getAnchor(type2);
            getAnchor(type3).connect(anchor8, 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor8, 0);
            getAnchor(type15).connect(anchor8, 0);
        } else if (type == type14 && type2 == type14) {
            ConstraintAnchor.Type type16 = ConstraintAnchor.Type.LEFT;
            getAnchor(type16).connect(constraintWidget.getAnchor(type16), 0);
            ConstraintAnchor.Type type17 = ConstraintAnchor.Type.RIGHT;
            getAnchor(type17).connect(constraintWidget.getAnchor(type17), 0);
            getAnchor(type14).connect(constraintWidget.getAnchor(type2), 0);
        } else if (type == type15 && type2 == type15) {
            ConstraintAnchor.Type type18 = ConstraintAnchor.Type.TOP;
            getAnchor(type18).connect(constraintWidget.getAnchor(type18), 0);
            ConstraintAnchor.Type type19 = ConstraintAnchor.Type.BOTTOM;
            getAnchor(type19).connect(constraintWidget.getAnchor(type19), 0);
            getAnchor(type15).connect(constraintWidget.getAnchor(type2), 0);
        } else {
            ConstraintAnchor anchor9 = getAnchor(type);
            ConstraintAnchor anchor10 = constraintWidget.getAnchor(type2);
            if (!anchor9.isValidConnection(anchor10)) {
                return;
            }
            ConstraintAnchor.Type type20 = ConstraintAnchor.Type.BASELINE;
            if (type == type20) {
                ConstraintAnchor anchor11 = getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor anchor12 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                if (anchor11 != null) {
                    anchor11.reset();
                }
                if (anchor12 != null) {
                    anchor12.reset();
                }
                i = 0;
            } else if (type == ConstraintAnchor.Type.TOP || type == ConstraintAnchor.Type.BOTTOM) {
                ConstraintAnchor anchor13 = getAnchor(type20);
                if (anchor13 != null) {
                    anchor13.reset();
                }
                ConstraintAnchor anchor14 = getAnchor(type5);
                if (anchor14.getTarget() != anchor10) {
                    anchor14.reset();
                }
                ConstraintAnchor opposite = getAnchor(type).getOpposite();
                ConstraintAnchor anchor15 = getAnchor(type15);
                if (anchor15.isConnected()) {
                    opposite.reset();
                    anchor15.reset();
                }
            } else if (type == ConstraintAnchor.Type.LEFT || type == ConstraintAnchor.Type.RIGHT) {
                ConstraintAnchor anchor16 = getAnchor(type5);
                if (anchor16.getTarget() != anchor10) {
                    anchor16.reset();
                }
                ConstraintAnchor opposite2 = getAnchor(type).getOpposite();
                ConstraintAnchor anchor17 = getAnchor(type14);
                if (anchor17.isConnected()) {
                    opposite2.reset();
                    anchor17.reset();
                }
            }
            anchor9.connect(anchor10, i);
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int size = this.mAnchors.size();
            for (int i = 0; i < size; i++) {
                this.mAnchors.get(i).reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            case 1:
                return this.mLeft;
            case 2:
                return this.mTop;
            case 3:
                return this.mRight;
            case 4:
                return this.mBottom;
            case 5:
                return this.mBaseline;
            case 6:
                return this.mCenter;
            case 7:
                return this.mCenterX;
            case 8:
                return this.mCenterY;
            case 9:
                return null;
            default:
                throw new AssertionError(type.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public DimensionBehaviour getDimensionBehaviour(int i) {
        if (i == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (i != 1) {
            return null;
        }
        return getVerticalDimensionBehaviour();
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
    }

    public boolean isInHorizontalChain() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 == null || constraintAnchor2.mTarget != constraintAnchor) {
            ConstraintAnchor constraintAnchor3 = this.mRight;
            ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
            return constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3;
        }
        return true;
    }

    public ConstraintWidget getPreviousChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i != 0) {
            if (i != 1 || (constraintAnchor2 = (constraintAnchor = this.mTop).mTarget) == null || constraintAnchor2.mTarget != constraintAnchor) {
                return null;
            }
            return constraintAnchor2.mOwner;
        }
        ConstraintAnchor constraintAnchor3 = this.mLeft;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        if (constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3) {
            return constraintAnchor4.mOwner;
        }
        return null;
    }

    public ConstraintWidget getNextChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i != 0) {
            if (i != 1 || (constraintAnchor2 = (constraintAnchor = this.mBottom).mTarget) == null || constraintAnchor2.mTarget != constraintAnchor) {
                return null;
            }
            return constraintAnchor2.mOwner;
        }
        ConstraintAnchor constraintAnchor3 = this.mRight;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        if (constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3) {
            return constraintAnchor4.mOwner;
        }
        return null;
    }

    public boolean isInVerticalChain() {
        ConstraintAnchor constraintAnchor = this.mTop;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 == null || constraintAnchor2.mTarget != constraintAnchor) {
            ConstraintAnchor constraintAnchor3 = this.mBottom;
            ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
            return constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3;
        }
        return true;
    }

    private boolean isChainHead(int i) {
        int i2 = i * 2;
        ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
        if (constraintAnchorArr[i2].mTarget != null && constraintAnchorArr[i2].mTarget.mTarget != constraintAnchorArr[i2]) {
            int i3 = i2 + 1;
            if (constraintAnchorArr[i3].mTarget != null && constraintAnchorArr[i3].mTarget.mTarget == constraintAnchorArr[i3]) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:176:0x0413, code lost:
        if (r13.mVisibility == 8) goto L190;
     */
    /* JADX WARN: Removed duplicated region for block: B:112:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0253  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0273  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0371  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x03cf  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0493  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x04c6  */
    /* JADX WARN: Removed duplicated region for block: B:187:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x04bc  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x048d  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0355  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0261  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0247 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void addToSolver(LinearSystem linearSystem) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        SolverVariable solverVariable;
        char c;
        int i;
        int i2;
        boolean z5;
        int i3;
        int i4;
        boolean z6;
        boolean z7;
        DimensionBehaviour dimensionBehaviour;
        boolean z8;
        SolverVariable solverVariable2;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        SolverVariable solverVariable5;
        SolverVariable solverVariable6;
        boolean z9;
        DependencyNode dependencyNode;
        LinearSystem linearSystem2;
        SolverVariable solverVariable7;
        SolverVariable solverVariable8;
        SolverVariable solverVariable9;
        int i5;
        int i6;
        int i7;
        int i8;
        SolverVariable solverVariable10;
        SolverVariable solverVariable11;
        ConstraintWidget constraintWidget;
        boolean z10;
        int i9;
        int i10;
        char c2;
        int i11;
        boolean isInHorizontalChain;
        boolean isInVerticalChain;
        ConstraintWidget constraintWidget2 = this;
        SolverVariable createObjectVariable = linearSystem.createObjectVariable(constraintWidget2.mLeft);
        SolverVariable createObjectVariable2 = linearSystem.createObjectVariable(constraintWidget2.mRight);
        SolverVariable createObjectVariable3 = linearSystem.createObjectVariable(constraintWidget2.mTop);
        SolverVariable createObjectVariable4 = linearSystem.createObjectVariable(constraintWidget2.mBottom);
        SolverVariable createObjectVariable5 = linearSystem.createObjectVariable(constraintWidget2.mBaseline);
        Metrics metrics = LinearSystem.sMetrics;
        HorizontalWidgetRun horizontalWidgetRun = constraintWidget2.horizontalRun;
        DependencyNode dependencyNode2 = horizontalWidgetRun.start;
        if (dependencyNode2.resolved && horizontalWidgetRun.end.resolved) {
            VerticalWidgetRun verticalWidgetRun = constraintWidget2.verticalRun;
            if (verticalWidgetRun.start.resolved && verticalWidgetRun.end.resolved) {
                linearSystem.addEquality(createObjectVariable, dependencyNode2.value);
                linearSystem.addEquality(createObjectVariable2, constraintWidget2.horizontalRun.end.value);
                linearSystem.addEquality(createObjectVariable3, constraintWidget2.verticalRun.start.value);
                linearSystem.addEquality(createObjectVariable4, constraintWidget2.verticalRun.end.value);
                linearSystem.addEquality(createObjectVariable5, constraintWidget2.verticalRun.baseline.value);
                ConstraintWidget constraintWidget3 = constraintWidget2.mParent;
                if (constraintWidget3 == null) {
                    return;
                }
                boolean z11 = constraintWidget3 != null && constraintWidget3.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
                boolean z12 = constraintWidget3 != null && constraintWidget3.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT;
                if (z11 && constraintWidget2.isTerminalWidget[0] && !isInHorizontalChain()) {
                    linearSystem.addGreaterThan(linearSystem.createObjectVariable(constraintWidget2.mParent.mRight), createObjectVariable2, 0, 7);
                }
                if (!z12 || !constraintWidget2.isTerminalWidget[1] || isInVerticalChain()) {
                    return;
                }
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(constraintWidget2.mParent.mBottom), createObjectVariable4, 0, 7);
                return;
            }
        }
        ConstraintWidget constraintWidget4 = constraintWidget2.mParent;
        if (constraintWidget4 != null) {
            boolean z13 = constraintWidget4 != null && constraintWidget4.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
            z = constraintWidget4 != null && constraintWidget4.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT;
            if (constraintWidget2.isChainHead(0)) {
                ((ConstraintWidgetContainer) constraintWidget2.mParent).addChain(constraintWidget2, 0);
                isInHorizontalChain = true;
            } else {
                isInHorizontalChain = isInHorizontalChain();
            }
            if (constraintWidget2.isChainHead(1)) {
                ((ConstraintWidgetContainer) constraintWidget2.mParent).addChain(constraintWidget2, 1);
                isInVerticalChain = true;
            } else {
                isInVerticalChain = isInVerticalChain();
            }
            if (!isInHorizontalChain && z13 && constraintWidget2.mVisibility != 8 && constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(constraintWidget2.mParent.mRight), createObjectVariable2, 0, 1);
            }
            if (!isInVerticalChain && z && constraintWidget2.mVisibility != 8 && constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null && constraintWidget2.mBaseline == null) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(constraintWidget2.mParent.mBottom), createObjectVariable4, 0, 1);
            }
            z2 = z13;
            z4 = isInHorizontalChain;
            z3 = isInVerticalChain;
        } else {
            z = false;
            z2 = false;
            z3 = false;
            z4 = false;
        }
        int i12 = constraintWidget2.mWidth;
        int i13 = constraintWidget2.mMinWidth;
        if (i12 >= i13) {
            i13 = i12;
        }
        int i14 = constraintWidget2.mHeight;
        int i15 = constraintWidget2.mMinHeight;
        if (i14 >= i15) {
            i15 = i14;
        }
        DimensionBehaviour[] dimensionBehaviourArr = constraintWidget2.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour2 = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour3 = DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z14 = dimensionBehaviour2 != dimensionBehaviour3;
        boolean z15 = dimensionBehaviourArr[1] != dimensionBehaviour3;
        int i16 = constraintWidget2.mDimensionRatioSide;
        constraintWidget2.mResolvedDimensionRatioSide = i16;
        float f = constraintWidget2.mDimensionRatio;
        constraintWidget2.mResolvedDimensionRatio = f;
        int i17 = i13;
        int i18 = constraintWidget2.mMatchConstraintDefaultWidth;
        int i19 = i15;
        int i20 = constraintWidget2.mMatchConstraintDefaultHeight;
        if (f > 0.0f) {
            solverVariable = createObjectVariable;
            if (constraintWidget2.mVisibility != 8) {
                if (dimensionBehaviourArr[0] == dimensionBehaviour3 && i18 == 0) {
                    i18 = 3;
                }
                if (dimensionBehaviourArr[1] == dimensionBehaviour3 && i20 == 0) {
                    c2 = 0;
                    i20 = 3;
                } else {
                    c2 = 0;
                }
                if (dimensionBehaviourArr[c2] == dimensionBehaviour3 && dimensionBehaviourArr[1] == dimensionBehaviour3) {
                    i11 = 3;
                    if (i18 == 3 && i20 == 3) {
                        constraintWidget2.setupDimensionRatio(z2, z, z14, z15);
                        c = 0;
                        i = i18;
                        i2 = i20;
                        i3 = i17;
                        i4 = i19;
                        z5 = true;
                        int[] iArr = constraintWidget2.mResolvedMatchConstraintDefault;
                        iArr[c] = i;
                        iArr[1] = i2;
                        constraintWidget2.mResolvedHasRatio = z5;
                        if (!z5 && ((i10 = constraintWidget2.mResolvedDimensionRatioSide) == 0 || i10 == -1)) {
                            z6 = true;
                            DimensionBehaviour dimensionBehaviour4 = constraintWidget2.mListDimensionBehaviors[0];
                            DimensionBehaviour dimensionBehaviour5 = DimensionBehaviour.WRAP_CONTENT;
                            boolean z16 = dimensionBehaviour4 != dimensionBehaviour5 && (constraintWidget2 instanceof ConstraintWidgetContainer);
                            int i21 = z16 ? 0 : i3;
                            boolean z17 = !constraintWidget2.mCenter.isConnected();
                            SolverVariable solverVariable12 = null;
                            if (constraintWidget2.mHorizontalResolution != 2) {
                                HorizontalWidgetRun horizontalWidgetRun2 = constraintWidget2.horizontalRun;
                                DependencyNode dependencyNode3 = horizontalWidgetRun2.start;
                                if (!dependencyNode3.resolved || !horizontalWidgetRun2.end.resolved) {
                                    SolverVariable solverVariable13 = solverVariable;
                                    ConstraintWidget constraintWidget5 = constraintWidget2.mParent;
                                    SolverVariable createObjectVariable6 = constraintWidget5 != null ? linearSystem.createObjectVariable(constraintWidget5.mRight) : null;
                                    ConstraintWidget constraintWidget6 = constraintWidget2.mParent;
                                    z9 = z2;
                                    z7 = z;
                                    solverVariable2 = createObjectVariable5;
                                    solverVariable3 = createObjectVariable4;
                                    solverVariable4 = createObjectVariable3;
                                    solverVariable5 = createObjectVariable2;
                                    dimensionBehaviour = dimensionBehaviour5;
                                    solverVariable6 = solverVariable13;
                                    z8 = z5;
                                    applyConstraints(linearSystem, true, z9, z7, constraintWidget2.isTerminalWidget[0], constraintWidget6 != null ? linearSystem.createObjectVariable(constraintWidget6.mLeft) : null, createObjectVariable6, constraintWidget2.mListDimensionBehaviors[0], z16, constraintWidget2.mLeft, constraintWidget2.mRight, constraintWidget2.mX, i21, constraintWidget2.mMinWidth, constraintWidget2.mMaxDimension[0], constraintWidget2.mHorizontalBiasPercent, z6, z4, z3, i, i2, constraintWidget2.mMatchConstraintMinWidth, constraintWidget2.mMatchConstraintMaxWidth, constraintWidget2.mMatchConstraintPercentWidth, z17);
                                } else {
                                    SolverVariable solverVariable14 = solverVariable;
                                    linearSystem.addEquality(solverVariable14, dependencyNode3.value);
                                    linearSystem.addEquality(createObjectVariable2, constraintWidget2.horizontalRun.end.value);
                                    if (constraintWidget2.mParent != null && z2 && constraintWidget2.isTerminalWidget[0] && !isInHorizontalChain()) {
                                        linearSystem.addGreaterThan(linearSystem.createObjectVariable(constraintWidget2.mParent.mRight), createObjectVariable2, 0, 7);
                                    }
                                    z7 = z;
                                    dimensionBehaviour = dimensionBehaviour5;
                                    z8 = z5;
                                    z9 = z2;
                                    solverVariable5 = createObjectVariable2;
                                    solverVariable6 = solverVariable14;
                                    solverVariable2 = createObjectVariable5;
                                    solverVariable3 = createObjectVariable4;
                                    solverVariable4 = createObjectVariable3;
                                    VerticalWidgetRun verticalWidgetRun2 = constraintWidget2.verticalRun;
                                    dependencyNode = verticalWidgetRun2.start;
                                    if (dependencyNode.resolved || !verticalWidgetRun2.end.resolved) {
                                        linearSystem2 = linearSystem;
                                        solverVariable7 = solverVariable2;
                                        solverVariable8 = solverVariable3;
                                        solverVariable9 = solverVariable4;
                                        i5 = 7;
                                        i6 = 0;
                                        i7 = 1;
                                        i8 = 1;
                                    } else {
                                        linearSystem2 = linearSystem;
                                        solverVariable9 = solverVariable4;
                                        linearSystem2.addEquality(solverVariable9, dependencyNode.value);
                                        solverVariable8 = solverVariable3;
                                        linearSystem2.addEquality(solverVariable8, constraintWidget2.verticalRun.end.value);
                                        solverVariable7 = solverVariable2;
                                        linearSystem2.addEquality(solverVariable7, constraintWidget2.verticalRun.baseline.value);
                                        ConstraintWidget constraintWidget7 = constraintWidget2.mParent;
                                        if (constraintWidget7 == null || z3 || !z7) {
                                            i5 = 7;
                                            i6 = 0;
                                            i7 = 1;
                                        } else {
                                            i7 = 1;
                                            if (constraintWidget2.isTerminalWidget[1]) {
                                                i5 = 7;
                                                i6 = 0;
                                                linearSystem2.addGreaterThan(linearSystem2.createObjectVariable(constraintWidget7.mBottom), solverVariable8, 0, 7);
                                            } else {
                                                i5 = 7;
                                                i6 = 0;
                                            }
                                        }
                                        i8 = i6;
                                    }
                                    if ((constraintWidget2.mVerticalResolution != 2 ? i6 : i8) == 0) {
                                        int i22 = (constraintWidget2.mListDimensionBehaviors[i7] != dimensionBehaviour || !(constraintWidget2 instanceof ConstraintWidgetContainer)) ? i6 : i7;
                                        if (i22 != 0) {
                                            i4 = i6;
                                        }
                                        boolean z18 = (!z8 || !((i9 = constraintWidget2.mResolvedDimensionRatioSide) == i7 || i9 == -1)) ? i6 : i7;
                                        ConstraintWidget constraintWidget8 = constraintWidget2.mParent;
                                        SolverVariable createObjectVariable7 = constraintWidget8 != null ? linearSystem2.createObjectVariable(constraintWidget8.mBottom) : null;
                                        ConstraintWidget constraintWidget9 = constraintWidget2.mParent;
                                        if (constraintWidget9 != null) {
                                            solverVariable12 = linearSystem2.createObjectVariable(constraintWidget9.mTop);
                                        }
                                        int i23 = constraintWidget2.mBaselineDistance <= 0 ? 8 : 8;
                                        linearSystem2.addEquality(solverVariable7, solverVariable9, getBaselineDistance(), i5);
                                        ConstraintAnchor constraintAnchor = constraintWidget2.mBaseline.mTarget;
                                        if (constraintAnchor != null) {
                                            linearSystem2.addEquality(solverVariable7, linearSystem2.createObjectVariable(constraintAnchor), i6, i5);
                                            if (z7) {
                                                linearSystem2.addGreaterThan(createObjectVariable7, linearSystem2.createObjectVariable(constraintWidget2.mBottom), i6, 5);
                                            }
                                            z10 = i6;
                                            solverVariable10 = solverVariable8;
                                            solverVariable11 = solverVariable9;
                                            applyConstraints(linearSystem, false, z7, z9, constraintWidget2.isTerminalWidget[i7], solverVariable12, createObjectVariable7, constraintWidget2.mListDimensionBehaviors[i7], i22, constraintWidget2.mTop, constraintWidget2.mBottom, constraintWidget2.mY, i4, constraintWidget2.mMinHeight, constraintWidget2.mMaxDimension[i7], constraintWidget2.mVerticalBiasPercent, z18, z3, z4, i2, i, constraintWidget2.mMatchConstraintMinHeight, constraintWidget2.mMatchConstraintMaxHeight, constraintWidget2.mMatchConstraintPercentHeight, z10);
                                        } else {
                                            if (constraintWidget2.mVisibility == i23) {
                                                linearSystem2.addEquality(solverVariable7, solverVariable9, i6, i5);
                                            }
                                            z10 = z17;
                                            solverVariable10 = solverVariable8;
                                            solverVariable11 = solverVariable9;
                                            applyConstraints(linearSystem, false, z7, z9, constraintWidget2.isTerminalWidget[i7], solverVariable12, createObjectVariable7, constraintWidget2.mListDimensionBehaviors[i7], i22, constraintWidget2.mTop, constraintWidget2.mBottom, constraintWidget2.mY, i4, constraintWidget2.mMinHeight, constraintWidget2.mMaxDimension[i7], constraintWidget2.mVerticalBiasPercent, z18, z3, z4, i2, i, constraintWidget2.mMatchConstraintMinHeight, constraintWidget2.mMatchConstraintMaxHeight, constraintWidget2.mMatchConstraintPercentHeight, z10);
                                        }
                                    } else {
                                        solverVariable10 = solverVariable8;
                                        solverVariable11 = solverVariable9;
                                    }
                                    if (!z8) {
                                        constraintWidget = this;
                                        if (constraintWidget.mResolvedDimensionRatioSide == 1) {
                                            linearSystem.addRatio(solverVariable10, solverVariable11, solverVariable5, solverVariable6, constraintWidget.mResolvedDimensionRatio, 7);
                                        } else {
                                            linearSystem.addRatio(solverVariable5, solverVariable6, solverVariable10, solverVariable11, constraintWidget.mResolvedDimensionRatio, 7);
                                        }
                                    } else {
                                        constraintWidget = this;
                                    }
                                    if (constraintWidget.mCenter.isConnected()) {
                                        return;
                                    }
                                    linearSystem.addCenterPoint(constraintWidget, constraintWidget.mCenter.getTarget().getOwner(), (float) Math.toRadians(constraintWidget.mCircleConstraintAngle + 90.0f), constraintWidget.mCenter.getMargin());
                                    return;
                                }
                            } else {
                                z7 = z;
                                dimensionBehaviour = dimensionBehaviour5;
                                z8 = z5;
                                solverVariable2 = createObjectVariable5;
                                solverVariable3 = createObjectVariable4;
                                solverVariable4 = createObjectVariable3;
                                solverVariable5 = createObjectVariable2;
                                solverVariable6 = solverVariable;
                                z9 = z2;
                            }
                            constraintWidget2 = this;
                            VerticalWidgetRun verticalWidgetRun22 = constraintWidget2.verticalRun;
                            dependencyNode = verticalWidgetRun22.start;
                            if (dependencyNode.resolved) {
                            }
                            linearSystem2 = linearSystem;
                            solverVariable7 = solverVariable2;
                            solverVariable8 = solverVariable3;
                            solverVariable9 = solverVariable4;
                            i5 = 7;
                            i6 = 0;
                            i7 = 1;
                            i8 = 1;
                            if ((constraintWidget2.mVerticalResolution != 2 ? i6 : i8) == 0) {
                            }
                            if (!z8) {
                            }
                            if (constraintWidget.mCenter.isConnected()) {
                            }
                        }
                        z6 = false;
                        DimensionBehaviour dimensionBehaviour42 = constraintWidget2.mListDimensionBehaviors[0];
                        DimensionBehaviour dimensionBehaviour52 = DimensionBehaviour.WRAP_CONTENT;
                        if (dimensionBehaviour42 != dimensionBehaviour52) {
                        }
                        if (z16) {
                        }
                        boolean z172 = !constraintWidget2.mCenter.isConnected();
                        SolverVariable solverVariable122 = null;
                        if (constraintWidget2.mHorizontalResolution != 2) {
                        }
                        constraintWidget2 = this;
                        VerticalWidgetRun verticalWidgetRun222 = constraintWidget2.verticalRun;
                        dependencyNode = verticalWidgetRun222.start;
                        if (dependencyNode.resolved) {
                        }
                        linearSystem2 = linearSystem;
                        solverVariable7 = solverVariable2;
                        solverVariable8 = solverVariable3;
                        solverVariable9 = solverVariable4;
                        i5 = 7;
                        i6 = 0;
                        i7 = 1;
                        i8 = 1;
                        if ((constraintWidget2.mVerticalResolution != 2 ? i6 : i8) == 0) {
                        }
                        if (!z8) {
                        }
                        if (constraintWidget.mCenter.isConnected()) {
                        }
                    }
                } else {
                    i11 = 3;
                }
                if (dimensionBehaviourArr[0] == dimensionBehaviour3 && i18 == i11) {
                    constraintWidget2.mResolvedDimensionRatioSide = 0;
                    int i24 = (int) (f * i14);
                    if (dimensionBehaviourArr[1] != dimensionBehaviour3) {
                        i3 = i24;
                        i2 = i20;
                        i = 4;
                        z5 = false;
                        c = 0;
                        i4 = i19;
                    } else {
                        i = i18;
                        z5 = true;
                        i2 = i20;
                        i4 = i19;
                        c = 0;
                        i3 = i24;
                    }
                } else {
                    if (dimensionBehaviourArr[1] == dimensionBehaviour3 && i20 == 3) {
                        constraintWidget2.mResolvedDimensionRatioSide = 1;
                        if (i16 == -1) {
                            constraintWidget2.mResolvedDimensionRatio = 1.0f / f;
                        }
                        int i25 = (int) (constraintWidget2.mResolvedDimensionRatio * i12);
                        c = 0;
                        if (dimensionBehaviourArr[0] != dimensionBehaviour3) {
                            i = i18;
                            z5 = false;
                            i3 = i17;
                            i2 = 4;
                            i4 = i25;
                        } else {
                            i4 = i25;
                            i = i18;
                            i2 = i20;
                            i3 = i17;
                            z5 = true;
                        }
                    }
                    c = 0;
                    i = i18;
                    i2 = i20;
                    i3 = i17;
                    i4 = i19;
                    z5 = true;
                }
                int[] iArr2 = constraintWidget2.mResolvedMatchConstraintDefault;
                iArr2[c] = i;
                iArr2[1] = i2;
                constraintWidget2.mResolvedHasRatio = z5;
                if (!z5) {
                    z6 = true;
                    DimensionBehaviour dimensionBehaviour422 = constraintWidget2.mListDimensionBehaviors[0];
                    DimensionBehaviour dimensionBehaviour522 = DimensionBehaviour.WRAP_CONTENT;
                    if (dimensionBehaviour422 != dimensionBehaviour522) {
                    }
                    if (z16) {
                    }
                    boolean z1722 = !constraintWidget2.mCenter.isConnected();
                    SolverVariable solverVariable1222 = null;
                    if (constraintWidget2.mHorizontalResolution != 2) {
                    }
                    constraintWidget2 = this;
                    VerticalWidgetRun verticalWidgetRun2222 = constraintWidget2.verticalRun;
                    dependencyNode = verticalWidgetRun2222.start;
                    if (dependencyNode.resolved) {
                    }
                    linearSystem2 = linearSystem;
                    solverVariable7 = solverVariable2;
                    solverVariable8 = solverVariable3;
                    solverVariable9 = solverVariable4;
                    i5 = 7;
                    i6 = 0;
                    i7 = 1;
                    i8 = 1;
                    if ((constraintWidget2.mVerticalResolution != 2 ? i6 : i8) == 0) {
                    }
                    if (!z8) {
                    }
                    if (constraintWidget.mCenter.isConnected()) {
                    }
                }
                z6 = false;
                DimensionBehaviour dimensionBehaviour4222 = constraintWidget2.mListDimensionBehaviors[0];
                DimensionBehaviour dimensionBehaviour5222 = DimensionBehaviour.WRAP_CONTENT;
                if (dimensionBehaviour4222 != dimensionBehaviour5222) {
                }
                if (z16) {
                }
                boolean z17222 = !constraintWidget2.mCenter.isConnected();
                SolverVariable solverVariable12222 = null;
                if (constraintWidget2.mHorizontalResolution != 2) {
                }
                constraintWidget2 = this;
                VerticalWidgetRun verticalWidgetRun22222 = constraintWidget2.verticalRun;
                dependencyNode = verticalWidgetRun22222.start;
                if (dependencyNode.resolved) {
                }
                linearSystem2 = linearSystem;
                solverVariable7 = solverVariable2;
                solverVariable8 = solverVariable3;
                solverVariable9 = solverVariable4;
                i5 = 7;
                i6 = 0;
                i7 = 1;
                i8 = 1;
                if ((constraintWidget2.mVerticalResolution != 2 ? i6 : i8) == 0) {
                }
                if (!z8) {
                }
                if (constraintWidget.mCenter.isConnected()) {
                }
            }
        } else {
            solverVariable = createObjectVariable;
        }
        c = 0;
        i = i18;
        i2 = i20;
        z5 = false;
        i3 = i17;
        i4 = i19;
        int[] iArr22 = constraintWidget2.mResolvedMatchConstraintDefault;
        iArr22[c] = i;
        iArr22[1] = i2;
        constraintWidget2.mResolvedHasRatio = z5;
        if (!z5) {
        }
        z6 = false;
        DimensionBehaviour dimensionBehaviour42222 = constraintWidget2.mListDimensionBehaviors[0];
        DimensionBehaviour dimensionBehaviour52222 = DimensionBehaviour.WRAP_CONTENT;
        if (dimensionBehaviour42222 != dimensionBehaviour52222) {
        }
        if (z16) {
        }
        boolean z172222 = !constraintWidget2.mCenter.isConnected();
        SolverVariable solverVariable122222 = null;
        if (constraintWidget2.mHorizontalResolution != 2) {
        }
        constraintWidget2 = this;
        VerticalWidgetRun verticalWidgetRun222222 = constraintWidget2.verticalRun;
        dependencyNode = verticalWidgetRun222222.start;
        if (dependencyNode.resolved) {
        }
        linearSystem2 = linearSystem;
        solverVariable7 = solverVariable2;
        solverVariable8 = solverVariable3;
        solverVariable9 = solverVariable4;
        i5 = 7;
        i6 = 0;
        i7 = 1;
        i8 = 1;
        if ((constraintWidget2.mVerticalResolution != 2 ? i6 : i8) == 0) {
        }
        if (!z8) {
        }
        if (constraintWidget.mCenter.isConnected()) {
        }
    }

    public void setupDimensionRatio(boolean z, boolean z2, boolean z3, boolean z4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z3 && !z4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z3 && z4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            int i = this.mMatchConstraintMinWidth;
            if (i > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (i != 0 || this.mMatchConstraintMinHeight <= 0) {
            } else {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:205:0x03d4, code lost:
        if (r0[1] == r1) goto L207;
     */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0353  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x030a  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x02da  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x03a2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:214:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01c2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0382 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02e8  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x030e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyConstraints(LinearSystem linearSystem, boolean z, boolean z2, boolean z3, boolean z4, SolverVariable solverVariable, SolverVariable solverVariable2, DimensionBehaviour dimensionBehaviour, boolean z5, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i, int i2, int i3, int i4, float f, boolean z6, boolean z7, boolean z8, int i5, int i6, int i7, int i8, float f2, boolean z9) {
        int i9;
        boolean z10;
        int i10;
        int i11;
        int i12;
        int i13;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        SolverVariable solverVariable5;
        int i14;
        boolean z11;
        boolean z12;
        SolverVariable createObjectVariable;
        SolverVariable createObjectVariable2;
        ConstraintAnchor constraintAnchor3;
        boolean z13;
        boolean z14;
        int i15;
        int i16;
        int i17;
        boolean z15;
        boolean z16;
        boolean z17;
        SolverVariable solverVariable6;
        int i18;
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2;
        int i19;
        ConstraintWidget constraintWidget3;
        int i20;
        SolverVariable solverVariable7;
        int i21;
        int i22;
        int i23;
        boolean z18;
        int i24;
        int i25;
        boolean z19;
        SolverVariable solverVariable8;
        int i26;
        int i27 = i8;
        SolverVariable createObjectVariable3 = linearSystem.createObjectVariable(constraintAnchor);
        SolverVariable createObjectVariable4 = linearSystem.createObjectVariable(constraintAnchor2);
        SolverVariable createObjectVariable5 = linearSystem.createObjectVariable(constraintAnchor.getTarget());
        SolverVariable createObjectVariable6 = linearSystem.createObjectVariable(constraintAnchor2.getTarget());
        LinearSystem.getMetrics();
        boolean isConnected = constraintAnchor.isConnected();
        boolean isConnected2 = constraintAnchor2.isConnected();
        boolean isConnected3 = this.mCenter.isConnected();
        int i28 = isConnected2 ? (isConnected ? 1 : 0) + 1 : isConnected ? 1 : 0;
        if (isConnected3) {
            i28++;
        }
        int i29 = z6 ? 3 : i5;
        int i30 = AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[dimensionBehaviour.ordinal()];
        if (i30 == 1 || i30 == 2 || i30 == 3 || i30 != 4) {
            i9 = i29;
        } else {
            i9 = i29;
            if (i9 != 4) {
                z10 = true;
                if (this.mVisibility != 8) {
                    i10 = 0;
                    z10 = false;
                } else {
                    i10 = i2;
                }
                if (z9) {
                    if (!isConnected && !isConnected2 && !isConnected3) {
                        linearSystem.addEquality(createObjectVariable3, i);
                    } else if (isConnected && !isConnected2) {
                        i11 = 7;
                        linearSystem.addEquality(createObjectVariable3, createObjectVariable5, constraintAnchor.getMargin(), 7);
                        if (!z10) {
                            if (z5) {
                                linearSystem.addEquality(createObjectVariable4, createObjectVariable3, 0, 3);
                                if (i3 > 0) {
                                    linearSystem.addGreaterThan(createObjectVariable4, createObjectVariable3, i3, 7);
                                }
                                if (i4 < Integer.MAX_VALUE) {
                                    linearSystem.addLowerThan(createObjectVariable4, createObjectVariable3, i4, 7);
                                }
                            } else {
                                linearSystem.addEquality(createObjectVariable4, createObjectVariable3, i10, i11);
                            }
                            i13 = i28;
                            solverVariable3 = createObjectVariable5;
                            solverVariable4 = createObjectVariable4;
                            z11 = z10;
                            solverVariable5 = createObjectVariable6;
                            z12 = z4;
                        } else if (i28 == 2 || z6 || !(i9 == 1 || i9 == 0)) {
                            int i31 = i7 == -2 ? i10 : i7;
                            int i32 = i27 == -2 ? i10 : i27;
                            if (i10 > 0 && i9 != 1) {
                                i10 = 0;
                            }
                            if (i31 > 0) {
                                linearSystem.addGreaterThan(createObjectVariable4, createObjectVariable3, i31, 7);
                                i10 = Math.max(i10, i31);
                            }
                            if (i32 > 0) {
                                i12 = 7;
                                if (!z2 || i9 != 1) {
                                    linearSystem.addLowerThan(createObjectVariable4, createObjectVariable3, i32, 7);
                                }
                                i10 = Math.min(i10, i32);
                            } else {
                                i12 = 7;
                            }
                            if (i9 == 1) {
                                if (z2) {
                                    linearSystem.addEquality(createObjectVariable4, createObjectVariable3, i10, i12);
                                } else if (z7) {
                                    linearSystem.addEquality(createObjectVariable4, createObjectVariable3, i10, 5);
                                    linearSystem.addLowerThan(createObjectVariable4, createObjectVariable3, i10, i12);
                                } else {
                                    linearSystem.addEquality(createObjectVariable4, createObjectVariable3, i10, 5);
                                    linearSystem.addLowerThan(createObjectVariable4, createObjectVariable3, i10, i12);
                                }
                                i27 = i32;
                                i13 = i28;
                                solverVariable3 = createObjectVariable5;
                                solverVariable4 = createObjectVariable4;
                                z11 = z10;
                                z12 = z4;
                                i14 = i31;
                                solverVariable5 = createObjectVariable6;
                            } else if (i9 == 2) {
                                ConstraintAnchor.Type type = constraintAnchor.getType();
                                ConstraintAnchor.Type type2 = ConstraintAnchor.Type.TOP;
                                if (type == type2 || constraintAnchor.getType() == ConstraintAnchor.Type.BOTTOM) {
                                    createObjectVariable = linearSystem.createObjectVariable(this.mParent.getAnchor(type2));
                                    createObjectVariable2 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.BOTTOM));
                                } else {
                                    createObjectVariable = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.LEFT));
                                    createObjectVariable2 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                                }
                                SolverVariable solverVariable9 = createObjectVariable;
                                int i33 = i32;
                                i13 = i28;
                                int i34 = i31;
                                solverVariable5 = createObjectVariable6;
                                solverVariable3 = createObjectVariable5;
                                solverVariable4 = createObjectVariable4;
                                linearSystem.addConstraint(linearSystem.createRow().createRowDimensionRatio(createObjectVariable4, createObjectVariable3, createObjectVariable2, solverVariable9, f2));
                                z12 = z4;
                                i27 = i33;
                                i14 = i34;
                                z11 = false;
                            } else {
                                int i35 = i32;
                                i13 = i28;
                                solverVariable3 = createObjectVariable5;
                                solverVariable4 = createObjectVariable4;
                                int i36 = i31;
                                solverVariable5 = createObjectVariable6;
                                i27 = i35;
                                i14 = i36;
                                z11 = z10;
                                z12 = true;
                            }
                            if (z9 || z7) {
                                SolverVariable solverVariable10 = solverVariable4;
                                if (i13 >= 2 || !z2 || !z12) {
                                    return;
                                }
                                linearSystem.addGreaterThan(createObjectVariable3, solverVariable, 0, 7);
                                boolean z20 = z || this.mBaseline.mTarget == null;
                                if (!z && (constraintAnchor3 = this.mBaseline.mTarget) != null) {
                                    ConstraintWidget constraintWidget4 = constraintAnchor3.mOwner;
                                    if (constraintWidget4.mDimensionRatio != 0.0f) {
                                        DimensionBehaviour[] dimensionBehaviourArr = constraintWidget4.mListDimensionBehaviors;
                                        DimensionBehaviour dimensionBehaviour2 = dimensionBehaviourArr[0];
                                        DimensionBehaviour dimensionBehaviour3 = DimensionBehaviour.MATCH_CONSTRAINT;
                                        if (dimensionBehaviour2 == dimensionBehaviour3) {
                                            z20 = true;
                                        }
                                    }
                                    z20 = false;
                                }
                                if (!z20) {
                                    return;
                                }
                                linearSystem.addGreaterThan(solverVariable2, solverVariable10, 0, 7);
                                return;
                            }
                            if ((isConnected || isConnected2 || isConnected3) && (!isConnected || isConnected2)) {
                                if (!isConnected && isConnected2) {
                                    linearSystem.addEquality(solverVariable4, solverVariable5, -constraintAnchor2.getMargin(), 7);
                                    if (z2) {
                                        linearSystem.addGreaterThan(createObjectVariable3, solverVariable, 0, 5);
                                    }
                                } else if (isConnected && isConnected2) {
                                    ConstraintWidget constraintWidget5 = constraintAnchor.mTarget.mOwner;
                                    ConstraintWidget constraintWidget6 = constraintAnchor2.mTarget.mOwner;
                                    ConstraintWidget parent = getParent();
                                    if (z11) {
                                        if (i9 == 0) {
                                            if (i27 == 0 && i14 == 0) {
                                                z19 = false;
                                                i24 = 7;
                                                i25 = 7;
                                                z18 = true;
                                            } else {
                                                z18 = false;
                                                i24 = 5;
                                                i25 = 5;
                                                z19 = true;
                                            }
                                            if ((constraintWidget5 instanceof Barrier) || (constraintWidget6 instanceof Barrier)) {
                                                z14 = z19;
                                                z15 = z18;
                                                i15 = 4;
                                                i17 = 5;
                                                i16 = i24;
                                                z13 = false;
                                            } else {
                                                z15 = z18;
                                                i17 = 5;
                                                i16 = i24;
                                                z13 = false;
                                                boolean z21 = z19;
                                                i15 = i25;
                                                z14 = z21;
                                            }
                                        } else if (i9 == 1) {
                                            z13 = true;
                                            z14 = true;
                                            i15 = 4;
                                            i16 = 7;
                                            i17 = 5;
                                            z15 = false;
                                        } else if (i9 == 3) {
                                            if (this.mResolvedDimensionRatioSide == -1) {
                                                if (z8) {
                                                    z13 = true;
                                                    z14 = true;
                                                    i15 = 5;
                                                    i16 = 7;
                                                    if (!z2) {
                                                        i17 = 4;
                                                    }
                                                } else {
                                                    z13 = true;
                                                    z14 = true;
                                                    i15 = 5;
                                                    i16 = 7;
                                                    i17 = 7;
                                                }
                                                z15 = true;
                                            } else if (z6) {
                                                if (!(i6 == 2 || i6 == 1)) {
                                                    i22 = 5;
                                                    i23 = 7;
                                                } else {
                                                    i22 = 4;
                                                    i23 = 5;
                                                }
                                                i15 = i22;
                                                i16 = i23;
                                                z13 = true;
                                                z14 = true;
                                            } else {
                                                if (i27 > 0) {
                                                    z13 = true;
                                                    z14 = true;
                                                    i15 = 5;
                                                } else if (i27 != 0 || i14 != 0) {
                                                    z13 = true;
                                                    z14 = true;
                                                    i15 = 4;
                                                } else if (!z8) {
                                                    z13 = true;
                                                    z14 = true;
                                                    i15 = 7;
                                                } else {
                                                    i16 = (constraintWidget5 == parent || constraintWidget6 == parent) ? 5 : 4;
                                                    z13 = true;
                                                    z14 = true;
                                                    i15 = 4;
                                                }
                                                i16 = 5;
                                            }
                                            i17 = 5;
                                            z15 = true;
                                        } else {
                                            z13 = false;
                                            z14 = false;
                                        }
                                        if (z13 || solverVariable3 != solverVariable5 || constraintWidget5 == parent) {
                                            z16 = z13;
                                            z17 = true;
                                        } else {
                                            z17 = false;
                                            z16 = false;
                                        }
                                        if (!z14) {
                                            solverVariable6 = solverVariable3;
                                            i19 = i9;
                                            constraintWidget3 = parent;
                                            constraintWidget = constraintWidget6;
                                            i18 = i15;
                                            constraintWidget2 = constraintWidget5;
                                            linearSystem.addCentering(createObjectVariable3, solverVariable3, constraintAnchor.getMargin(), f, solverVariable5, solverVariable4, constraintAnchor2.getMargin(), i17);
                                        } else {
                                            solverVariable6 = solverVariable3;
                                            i18 = i15;
                                            constraintWidget = constraintWidget6;
                                            constraintWidget2 = constraintWidget5;
                                            i19 = i9;
                                            constraintWidget3 = parent;
                                        }
                                        SolverVariable solverVariable11 = solverVariable6;
                                        if (!z16) {
                                            i20 = (!z2 || solverVariable11 == solverVariable5 || z11 || (!(constraintWidget2 instanceof Barrier) && !(constraintWidget instanceof Barrier))) ? i16 : 6;
                                            linearSystem.addGreaterThan(createObjectVariable3, solverVariable11, constraintAnchor.getMargin(), i20);
                                            linearSystem.addLowerThan(solverVariable4, solverVariable5, -constraintAnchor2.getMargin(), i20);
                                        } else {
                                            i20 = i16;
                                        }
                                        if (z17) {
                                            if (!z15 || (z8 && !z3)) {
                                                i21 = i18;
                                            } else {
                                                int i37 = (constraintWidget2 == constraintWidget3 || constraintWidget == constraintWidget3) ? 6 : i18;
                                                if ((constraintWidget2 instanceof Guideline) || (constraintWidget instanceof Guideline)) {
                                                    i37 = 5;
                                                }
                                                if ((constraintWidget2 instanceof Barrier) || (constraintWidget instanceof Barrier)) {
                                                    i37 = 5;
                                                }
                                                int i38 = i18;
                                                if (z8) {
                                                    i37 = 5;
                                                }
                                                i21 = Math.max(i37, i38);
                                            }
                                            if (z2) {
                                                i21 = Math.min(i20, i21);
                                            }
                                            linearSystem.addEquality(createObjectVariable3, solverVariable11, constraintAnchor.getMargin(), i21);
                                            linearSystem.addEquality(solverVariable4, solverVariable5, -constraintAnchor2.getMargin(), i21);
                                        }
                                        if (z2) {
                                            int margin = solverVariable == solverVariable11 ? constraintAnchor.getMargin() : 0;
                                            if (solverVariable11 != solverVariable) {
                                                linearSystem.addGreaterThan(createObjectVariable3, solverVariable, margin, 5);
                                            }
                                        }
                                        if (z2 && z11) {
                                            solverVariable7 = solverVariable4;
                                            if (i3 == 0 && i14 == 0) {
                                                if (!z11 && i19 == 3) {
                                                    linearSystem.addGreaterThan(solverVariable7, createObjectVariable3, 0, 7);
                                                } else {
                                                    linearSystem.addGreaterThan(solverVariable7, createObjectVariable3, 0, 5);
                                                }
                                            }
                                            if (z2 || !z12) {
                                                return;
                                            }
                                            if (constraintAnchor2.mTarget != null) {
                                                i26 = constraintAnchor2.getMargin();
                                                solverVariable8 = solverVariable2;
                                            } else {
                                                solverVariable8 = solverVariable2;
                                                i26 = 0;
                                            }
                                            if (solverVariable5 == solverVariable8) {
                                                return;
                                            }
                                            linearSystem.addGreaterThan(solverVariable8, solverVariable7, i26, 5);
                                            return;
                                        }
                                    } else {
                                        z13 = true;
                                        z14 = true;
                                    }
                                    i15 = 4;
                                    i16 = 5;
                                    i17 = 5;
                                    z15 = false;
                                    if (z13) {
                                    }
                                    z16 = z13;
                                    z17 = true;
                                    if (!z14) {
                                    }
                                    SolverVariable solverVariable112 = solverVariable6;
                                    if (!z16) {
                                    }
                                    if (z17) {
                                    }
                                    if (z2) {
                                    }
                                    if (z2) {
                                        solverVariable7 = solverVariable4;
                                        if (i3 == 0) {
                                            if (!z11) {
                                            }
                                            linearSystem.addGreaterThan(solverVariable7, createObjectVariable3, 0, 5);
                                        }
                                        if (z2) {
                                            return;
                                        }
                                        return;
                                    }
                                }
                            }
                            solverVariable7 = solverVariable4;
                            if (z2) {
                            }
                        } else {
                            int max = Math.max(i7, i10);
                            if (i27 > 0) {
                                max = Math.min(i27, max);
                            }
                            linearSystem.addEquality(createObjectVariable4, createObjectVariable3, max, 7);
                            z12 = z4;
                            i13 = i28;
                            solverVariable3 = createObjectVariable5;
                            solverVariable4 = createObjectVariable4;
                            solverVariable5 = createObjectVariable6;
                            z11 = false;
                        }
                        i14 = i7;
                        if (z9) {
                        }
                        SolverVariable solverVariable102 = solverVariable4;
                        if (i13 >= 2) {
                            return;
                        }
                        return;
                    }
                }
                i11 = 7;
                if (!z10) {
                }
                i14 = i7;
                if (z9) {
                }
                SolverVariable solverVariable1022 = solverVariable4;
                if (i13 >= 2) {
                }
            }
        }
        z10 = false;
        if (this.mVisibility != 8) {
        }
        if (z9) {
        }
        i11 = 7;
        if (!z10) {
        }
        i14 = i7;
        if (z9) {
        }
        SolverVariable solverVariable10222 = solverVariable4;
        if (i13 >= 2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.constraintlayout.solver.widgets.ConstraintWidget$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type;
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour;

        static {
            int[] iArr = new int[DimensionBehaviour.values().length];
            $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour = iArr;
            try {
                iArr[DimensionBehaviour.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[DimensionBehaviour.WRAP_CONTENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[DimensionBehaviour.MATCH_PARENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[DimensionBehaviour.MATCH_CONSTRAINT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[ConstraintAnchor.Type.values().length];
            $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type = iArr2;
            try {
                iArr2[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[ConstraintAnchor.Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    public void updateFromSolver(LinearSystem linearSystem) {
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        HorizontalWidgetRun horizontalWidgetRun = this.horizontalRun;
        DependencyNode dependencyNode = horizontalWidgetRun.start;
        if (dependencyNode.resolved) {
            DependencyNode dependencyNode2 = horizontalWidgetRun.end;
            if (dependencyNode2.resolved) {
                objectVariableValue = dependencyNode.value;
                objectVariableValue3 = dependencyNode2.value;
            }
        }
        VerticalWidgetRun verticalWidgetRun = this.verticalRun;
        DependencyNode dependencyNode3 = verticalWidgetRun.start;
        if (dependencyNode3.resolved) {
            DependencyNode dependencyNode4 = verticalWidgetRun.end;
            if (dependencyNode4.resolved) {
                objectVariableValue2 = dependencyNode3.value;
                objectVariableValue4 = dependencyNode4.value;
            }
        }
        int i = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue4 = 0;
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
        }
        setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, objectVariableValue4);
    }

    public void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        this.mHorizontalResolution = constraintWidget.mHorizontalResolution;
        this.mVerticalResolution = constraintWidget.mVerticalResolution;
        this.mMatchConstraintDefaultWidth = constraintWidget.mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = constraintWidget.mMatchConstraintDefaultHeight;
        int[] iArr = this.mResolvedMatchConstraintDefault;
        int[] iArr2 = constraintWidget.mResolvedMatchConstraintDefault;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.mMatchConstraintMinWidth = constraintWidget.mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = constraintWidget.mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = constraintWidget.mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = constraintWidget.mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = constraintWidget.mMatchConstraintPercentHeight;
        this.mIsWidthWrapContent = constraintWidget.mIsWidthWrapContent;
        this.mIsHeightWrapContent = constraintWidget.mIsHeightWrapContent;
        this.mResolvedDimensionRatioSide = constraintWidget.mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = constraintWidget.mResolvedDimensionRatio;
        int[] iArr3 = constraintWidget.mMaxDimension;
        this.mMaxDimension = Arrays.copyOf(iArr3, iArr3.length);
        this.mCircleConstraintAngle = constraintWidget.mCircleConstraintAngle;
        this.hasBaseline = constraintWidget.hasBaseline;
        this.inPlaceholder = constraintWidget.inPlaceholder;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = (DimensionBehaviour[]) Arrays.copyOf(this.mListDimensionBehaviors, 2);
        ConstraintWidget constraintWidget2 = null;
        this.mParent = this.mParent == null ? null : hashMap.get(constraintWidget.mParent);
        this.mWidth = constraintWidget.mWidth;
        this.mHeight = constraintWidget.mHeight;
        this.mDimensionRatio = constraintWidget.mDimensionRatio;
        this.mDimensionRatioSide = constraintWidget.mDimensionRatioSide;
        this.mX = constraintWidget.mX;
        this.mY = constraintWidget.mY;
        this.mRelX = constraintWidget.mRelX;
        this.mRelY = constraintWidget.mRelY;
        this.mOffsetX = constraintWidget.mOffsetX;
        this.mOffsetY = constraintWidget.mOffsetY;
        this.mBaselineDistance = constraintWidget.mBaselineDistance;
        this.mMinWidth = constraintWidget.mMinWidth;
        this.mMinHeight = constraintWidget.mMinHeight;
        this.mHorizontalBiasPercent = constraintWidget.mHorizontalBiasPercent;
        this.mVerticalBiasPercent = constraintWidget.mVerticalBiasPercent;
        this.mCompanionWidget = constraintWidget.mCompanionWidget;
        this.mContainerItemSkip = constraintWidget.mContainerItemSkip;
        this.mVisibility = constraintWidget.mVisibility;
        this.mDebugName = constraintWidget.mDebugName;
        this.mType = constraintWidget.mType;
        this.mDistToTop = constraintWidget.mDistToTop;
        this.mDistToLeft = constraintWidget.mDistToLeft;
        this.mDistToRight = constraintWidget.mDistToRight;
        this.mDistToBottom = constraintWidget.mDistToBottom;
        this.mLeftHasCentered = constraintWidget.mLeftHasCentered;
        this.mRightHasCentered = constraintWidget.mRightHasCentered;
        this.mTopHasCentered = constraintWidget.mTopHasCentered;
        this.mBottomHasCentered = constraintWidget.mBottomHasCentered;
        this.mHorizontalWrapVisited = constraintWidget.mHorizontalWrapVisited;
        this.mVerticalWrapVisited = constraintWidget.mVerticalWrapVisited;
        this.mOptimizerMeasurable = constraintWidget.mOptimizerMeasurable;
        this.mGroupsToSolver = constraintWidget.mGroupsToSolver;
        this.mHorizontalChainStyle = constraintWidget.mHorizontalChainStyle;
        this.mVerticalChainStyle = constraintWidget.mVerticalChainStyle;
        this.mHorizontalChainFixedPosition = constraintWidget.mHorizontalChainFixedPosition;
        this.mVerticalChainFixedPosition = constraintWidget.mVerticalChainFixedPosition;
        float[] fArr = this.mWeight;
        float[] fArr2 = constraintWidget.mWeight;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.mListNextMatchConstraintsWidget;
        ConstraintWidget[] constraintWidgetArr2 = constraintWidget.mListNextMatchConstraintsWidget;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.mNextChainWidget;
        ConstraintWidget[] constraintWidgetArr4 = constraintWidget.mNextChainWidget;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget3 = constraintWidget.mHorizontalNextWidget;
        this.mHorizontalNextWidget = constraintWidget3 == null ? null : hashMap.get(constraintWidget3);
        ConstraintWidget constraintWidget4 = constraintWidget.mVerticalNextWidget;
        if (constraintWidget4 != null) {
            constraintWidget2 = hashMap.get(constraintWidget4);
        }
        this.mVerticalNextWidget = constraintWidget2;
    }

    public void updateFromRuns(boolean z, boolean z2) {
        int i;
        int i2;
        boolean isResolved = z & this.horizontalRun.isResolved();
        boolean isResolved2 = z2 & this.verticalRun.isResolved();
        HorizontalWidgetRun horizontalWidgetRun = this.horizontalRun;
        int i3 = horizontalWidgetRun.start.value;
        VerticalWidgetRun verticalWidgetRun = this.verticalRun;
        int i4 = verticalWidgetRun.start.value;
        int i5 = horizontalWidgetRun.end.value;
        int i6 = verticalWidgetRun.end.value;
        int i7 = i6 - i4;
        if (i5 - i3 < 0 || i7 < 0 || i3 == Integer.MIN_VALUE || i3 == Integer.MAX_VALUE || i4 == Integer.MIN_VALUE || i4 == Integer.MAX_VALUE || i5 == Integer.MIN_VALUE || i5 == Integer.MAX_VALUE || i6 == Integer.MIN_VALUE || i6 == Integer.MAX_VALUE) {
            i5 = 0;
            i3 = 0;
            i6 = 0;
            i4 = 0;
        }
        int i8 = i5 - i3;
        int i9 = i6 - i4;
        if (isResolved) {
            this.mX = i3;
        }
        if (isResolved2) {
            this.mY = i4;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (isResolved) {
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i8 < (i2 = this.mWidth)) {
                i8 = i2;
            }
            this.mWidth = i8;
            int i10 = this.mMinWidth;
            if (i8 < i10) {
                this.mWidth = i10;
            }
        }
        if (!isResolved2) {
            return;
        }
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i9 < (i = this.mHeight)) {
            i9 = i;
        }
        this.mHeight = i9;
        int i11 = this.mMinHeight;
        if (i9 >= i11) {
            return;
        }
        this.mHeight = i11;
    }
}
