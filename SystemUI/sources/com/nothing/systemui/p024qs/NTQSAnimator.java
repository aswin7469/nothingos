package com.nothing.systemui.p024qs;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import androidx.constraintlayout.motion.widget.Key;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.PagedTileLayout;
import com.android.systemui.p012qs.QSExpansionPathInterpolator;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.p012qs.QSSecurityFooter;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.QuickQSPanel;
import com.android.systemui.p012qs.QuickQSPanelController;
import com.android.systemui.p012qs.QuickStatusBarHeader;
import com.android.systemui.p012qs.SideLabelTileLayout;
import com.android.systemui.p012qs.TouchAnimator;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.p012qs.tileimpl.HeightOverrideable;
import com.android.systemui.p012qs.tileimpl.IgnorableChildLinearLayout;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.tuner.TunerService;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@QSScope
/* renamed from: com.nothing.systemui.qs.NTQSAnimator */
public class NTQSAnimator implements QSHost.Callback, PagedTileLayout.PageListener, TouchAnimator.Listener, View.OnLayoutChangeListener, View.OnAttachStateChangeListener, TunerService.Tunable {
    private static final String ALLOW_FANCY_ANIMATION = "sysui_qs_fancy_anim";
    public static final int CIRCLE_TILES_NUM = 2;
    private static final boolean DEBUG = false;
    public static final float EXPANDED_TILE_DELAY = 0.86f;
    private static final String MOVE_FULL_ROWS = "sysui_qs_move_whole_rows";
    private static final long QQS_FADE_IN_DURATION = 200;
    private static final long QQS_FADE_OUT_DURATION = 50;
    public static final long QS_COLLAPSE_DURATION = 225;
    public static final long QS_EXPAND_DURATION = 300;
    public static final float QS_EXPAND_ONE_THIRD = 0.333f;
    public static final float QS_EXPAND_TWO_THIRDS = 0.667f;
    public static final float SHORT_PARALLAX_AMOUNT = 0.1f;
    private static final String TAG = "NTQSAnimator";
    private TouchAnimator mAllPagesDelayedAnimator;
    private final ArrayList<View> mAllViews = new ArrayList<>();
    private boolean mAllowFancy;
    private TouchAnimator mBrightnessAnimator;
    private int mBtIndex = 0;
    private ArrayList<IgnorableChildLinearLayout> mCircleLabel = new ArrayList<>();
    private ArrayList<IgnorableChildLinearLayout> mCircleLabelNonFirst = new ArrayList<>();
    private QSExpansionPathInterpolator mCircleQSExpansionPathInterpolator = new QSExpansionPathInterpolator();
    private final Executor mExecutor;
    private TouchAnimator mFirstPageAnimator;
    private TouchAnimator mFirstPageDelayedAnimator;
    private boolean mFullRows;
    private final QSTileHost mHost;
    private TouchAnimator mIconInOutNonFirstAnimator;
    private TouchAnimator mIconXAnimator;
    private TouchAnimator mIconXNonFirstAnimator;
    private TouchAnimator mIconYAnimator;
    private TouchAnimator mLabelContainerXAnimator;
    private TouchAnimator mLabelContainerYAnimator;
    private TouchAnimator mLabelInOutAnimator;
    private TouchAnimator mLabelSecondLabelInOutAnimator;
    private float mLastPosition;
    private Handler mMainHandler;
    private boolean mNeedsAnimatorUpdate = false;
    private boolean mNonFirstCircleTileAnimating;
    private HeightExpansionAnimator mNonFirstCircleTileHeightAnimator;
    private float mNonFirstCircleTileTranslationX;
    private float mNonFirstCircleTileTranslationY;
    private final TouchAnimator.Listener mNonFirstPageListener = new TouchAnimator.ListenerAdapter() {
        public void onAnimationAtEnd() {
            NTQSAnimator.this.mQuickQsPanel.setVisibility(4);
        }

        public void onAnimationStarted() {
            NTQSAnimator.this.mQuickQsPanel.setVisibility(0);
        }
    };
    private ArrayList<View> mNonFirstQsOrQqsViews = new ArrayList<>();
    private TouchAnimator mNonfirstPageAnimator;
    private TouchAnimator mNonfirstPageDelayedAnimator;
    private TouchAnimator mNtNonFirstPageAnimator;
    private int mNumQuickTiles;
    private boolean mOnFirstPage = true;
    private boolean mOnKeyguard;
    private HeightExpansionAnimator mOtherTilesExpandAnimator;
    private PagedTileLayout mPagedLayout;
    private HeightExpansionAnimator mQQSCircleTileHeightAnimator;
    private TouchAnimator mQQSNonFirstAnimator;
    private HeightExpansionAnimator mQQSTileHeightAnimator;
    private QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    private final C2301QS mQs;
    private final QSPanelController mQsPanelController;
    private final QuickQSPanelController mQuickQSPanelController;
    /* access modifiers changed from: private */
    public final QuickQSPanel mQuickQsPanel;
    private final ArrayList<View> mQuickQsViews = new ArrayList<>();
    private final QuickStatusBarHeader mQuickStatusBarHeader;
    private final QSSecurityFooter mSecurityFooter;
    private boolean mShowCollapsedOnKeyguard;
    private int mSignalIndex = 0;
    private boolean mSingleLineMode;
    private boolean mToExpand = true;
    private boolean mToShowing;
    private boolean mTranslateWhileExpanding;
    private TouchAnimator mTranslationXAnimator;
    private TouchAnimator mTranslationYAnimator;
    private final TunerService mTunerService;
    private final Runnable mUpdateAnimators = new NTQSAnimator$$ExternalSyntheticLambda0(this);

    @Inject
    public NTQSAnimator(C2301QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, QSSecurityFooter qSSecurityFooter, @Main Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator, @Main Handler handler) {
        this.mQs = qs;
        this.mQuickQsPanel = quickQSPanel;
        this.mQsPanelController = qSPanelController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mQuickStatusBarHeader = quickStatusBarHeader;
        this.mSecurityFooter = qSSecurityFooter;
        this.mHost = qSTileHost;
        this.mExecutor = executor;
        this.mTunerService = tunerService;
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        qSTileHost.addCallback(this);
        qSPanelController.addOnAttachStateChangeListener(this);
        qs.getView().addOnLayoutChangeListener(this);
        if (qSPanelController.isAttachedToWindow()) {
            onViewAttachedToWindow((View) null);
        }
        QSPanel.QSTileLayout tileLayout = qSPanelController.getTileLayout();
        if (tileLayout instanceof PagedTileLayout) {
            this.mPagedLayout = (PagedTileLayout) tileLayout;
        } else {
            NTLogUtil.m1684w(TAG, "QS Not using page layout");
        }
        qSPanelController.setPageListener(this);
        this.mMainHandler = handler;
    }

    public void onRtlChanged() {
        updateAnimators();
    }

    public void requestAnimatorUpdate() {
        this.mNeedsAnimatorUpdate = true;
    }

    public void setOnKeyguard(boolean z) {
        this.mOnKeyguard = z;
        updateQQSVisibility();
        if (this.mOnKeyguard) {
            clearAnimationState();
        }
    }

    /* access modifiers changed from: package-private */
    public void startAlphaAnimation(boolean z) {
        if (z != this.mToShowing) {
            this.mToShowing = z;
            if (z) {
                CrossFadeHelper.fadeIn(this.mQs.getView(), 200, 0);
            } else {
                CrossFadeHelper.fadeOut(this.mQs.getView(), QQS_FADE_OUT_DURATION, 0, (Runnable) null);
            }
        }
    }

    public void setShowCollapsedOnKeyguard(boolean z) {
        this.mShowCollapsedOnKeyguard = z;
        updateQQSVisibility();
        setCurrentPosition();
    }

    private void setCurrentPosition() {
        setPosition(this.mLastPosition);
    }

    private void updateQQSVisibility() {
        this.mQuickQsPanel.setVisibility((!this.mOnKeyguard || this.mShowCollapsedOnKeyguard) ? 0 : 4);
    }

    public void onViewAttachedToWindow(View view) {
        this.mTunerService.addTunable(this, ALLOW_FANCY_ANIMATION, MOVE_FULL_ROWS);
    }

    public void onViewDetachedFromWindow(View view) {
        this.mHost.removeCallback(this);
        this.mTunerService.removeTunable(this);
    }

    public void onTuningChanged(String str, String str2) {
        if (ALLOW_FANCY_ANIMATION.equals(str)) {
            boolean parseIntegerSwitch = TunerService.parseIntegerSwitch(str2, true);
            this.mAllowFancy = parseIntegerSwitch;
            if (!parseIntegerSwitch) {
                clearAnimationState();
            }
        } else if (MOVE_FULL_ROWS.equals(str)) {
            this.mFullRows = TunerService.parseIntegerSwitch(str2, true);
        }
        updateAnimators();
    }

    public void onPageChanged(boolean z, int i) {
        NTLogUtil.m1680d(TAG, "onPageChanged: mOnFirstPage " + this.mOnFirstPage + " isFirst " + z);
        if (this.mOnFirstPage != z) {
            if (!z) {
                clearAnimationState();
            }
            this.mOnFirstPage = z;
            updateAnimators();
        }
    }

    private void translateContent(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        int i7 = (i5 - i3) - i;
        builder.addFloat(view, Key.TRANSLATION_X, 0.0f, (float) i7);
        builder.addFloat(view2, Key.TRANSLATION_X, (float) (-i7), 0.0f);
        int i8 = (i6 - i4) - i2;
        builder2.addFloat(view, Key.TRANSLATION_Y, 0.0f, (float) i8);
        builder2.addFloat(view2, Key.TRANSLATION_Y, (float) (-i8), 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Don't wrap MOVE or CONST insns: 0x0748: MOVE  (r0v78 int) = (r50v0 int)
        	at jadx.core.dex.instructions.args.InsnArg.wrapArg(InsnArg.java:164)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.assignInline(CodeShrinkVisitor.java:133)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:118)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:65)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:43)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:122)
        	at jadx.core.dex.visitors.regions.TernaryMod.visitRegion(TernaryMod.java:34)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:73)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterative(DepthRegionTraversal.java:27)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:31)
        */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x07d0  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0951  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0136  */
    private void updateAnimators() {
        /*
            r52 = this;
            r9 = r52
            r10 = 0
            r9.mNeedsAnimatorUpdate = r10
            com.android.systemui.qs.TouchAnimator$Builder r11 = new com.android.systemui.qs.TouchAnimator$Builder
            r11.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r12 = new com.android.systemui.qs.TouchAnimator$Builder
            r12.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r13 = new com.android.systemui.qs.TouchAnimator$Builder
            r13.<init>()
            com.android.systemui.qs.QSTileHost r0 = r9.mHost
            java.util.Collection r0 = r0.getTiles()
            r14 = 2
            int[] r15 = new int[r14]
            int[] r8 = new int[r14]
            r52.clearAnimationState()
            java.util.ArrayList<android.view.View> r1 = r9.mAllViews
            r1.clear()
            java.util.ArrayList<android.view.View> r1 = r9.mQuickQsViews
            r1.clear()
            r7 = 0
            r9.mQQSTileHeightAnimator = r7
            r9.mOtherTilesExpandAnimator = r7
            r9.mQQSCircleTileHeightAnimator = r7
            r9.mLabelInOutAnimator = r7
            r9.mLabelSecondLabelInOutAnimator = r7
            r9.mLabelContainerXAnimator = r7
            r9.mLabelContainerYAnimator = r7
            r9.mIconXAnimator = r7
            r9.mIconYAnimator = r7
            com.android.systemui.qs.TouchAnimator$Builder r6 = new com.android.systemui.qs.TouchAnimator$Builder
            r6.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r5 = new com.android.systemui.qs.TouchAnimator$Builder
            r5.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r4 = new com.android.systemui.qs.TouchAnimator$Builder
            r4.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r3 = new com.android.systemui.qs.TouchAnimator$Builder
            r3.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r16 = new com.android.systemui.qs.TouchAnimator$Builder
            r16.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r17 = new com.android.systemui.qs.TouchAnimator$Builder
            r17.<init>()
            r9.mIconInOutNonFirstAnimator = r7
            r9.mQQSNonFirstAnimator = r7
            r9.mIconXNonFirstAnimator = r7
            r9.mNonFirstCircleTileHeightAnimator = r7
            r9.mNonFirstCircleTileAnimating = r10
            com.android.systemui.qs.TouchAnimator$Builder r2 = new com.android.systemui.qs.TouchAnimator$Builder
            r2.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r1 = new com.android.systemui.qs.TouchAnimator$Builder
            r1.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r10 = new com.android.systemui.qs.TouchAnimator$Builder
            r10.<init>()
            com.android.systemui.qs.TouchAnimator$Builder r14 = new com.android.systemui.qs.TouchAnimator$Builder
            r14.<init>()
            java.util.ArrayList<com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout> r7 = r9.mCircleLabel
            r7.clear()
            java.util.ArrayList<com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout> r7 = r9.mCircleLabelNonFirst
            r7.clear()
            java.util.ArrayList<android.view.View> r7 = r9.mNonFirstQsOrQqsViews
            r7.clear()
            com.android.systemui.qs.QuickQSPanel r7 = r9.mQuickQsPanel
            int r7 = r7.getNumQuickTiles()
            r9.mNumQuickTiles = r7
            com.android.systemui.qs.QSPanelController r7 = r9.mQsPanelController
            com.android.systemui.qs.QSPanel$QSTileLayout r7 = r7.getTileLayout()
            r20 = r3
            java.util.ArrayList<android.view.View> r3 = r9.mAllViews
            r21 = r4
            r4 = r7
            android.view.View r4 = (android.view.View) r4
            r3.add(r4)
            com.android.systemui.plugins.qs.QS r3 = r9.mQs
            android.view.View r3 = r3.getView()
            if (r3 == 0) goto L_0x00b7
            com.android.systemui.plugins.qs.QS r3 = r9.mQs
            android.view.View r3 = r3.getView()
            int r3 = r3.getMeasuredHeight()
            goto L_0x00b8
        L_0x00b7:
            r3 = 0
        L_0x00b8:
            com.android.systemui.plugins.qs.QS r4 = r9.mQs
            android.view.View r4 = r4.getHeader()
            int r4 = r4.getBottom()
            int r3 = r3 - r4
            com.android.systemui.plugins.qs.QS r4 = r9.mQs
            android.view.View r4 = r4.getHeader()
            int r4 = r4.getPaddingBottom()
            int r3 = r3 + r4
            boolean r4 = r9.mTranslateWhileExpanding
            if (r4 != 0) goto L_0x00d8
            float r3 = (float) r3
            r4 = 1036831949(0x3dcccccd, float:0.1)
            float r3 = r3 * r4
            int r3 = (int) r3
        L_0x00d8:
            r4 = r3
            boolean r3 = r9.mOnFirstPage
            r22 = r14
            java.lang.String r14 = "translationY"
            r23 = r10
            if (r3 == 0) goto L_0x00f6
            r3 = 2
            float[] r10 = new float[r3]
            float r3 = (float) r4
            r26 = r4
            r4 = 0
            r10[r4] = r3
            r3 = 0
            r18 = 1
            r10[r18] = r3
            r11.addFloat(r7, r14, r10)
            goto L_0x0102
        L_0x00f6:
            r26 = r4
            r4 = 0
            r3 = r7
            android.view.ViewGroup r3 = (android.view.ViewGroup) r3
            r3.setClipChildren(r4)
            r3.setClipToPadding(r4)
        L_0x0102:
            boolean r3 = r7 instanceof com.android.systemui.p012qs.PagedTileLayout
            if (r3 == 0) goto L_0x0126
            r3 = r7
            com.android.systemui.qs.PagedTileLayout r3 = (com.android.systemui.p012qs.PagedTileLayout) r3
            int r10 = r3.getCurrentPageNumber()
            int r27 = r3.getPageTilesNum(r4)
            if (r10 == 0) goto L_0x0122
            int r10 = r10 * r27
            r4 = 2
            int r10 = r10 + r4
            int r3 = r3.getNumVisibleTiles()
            int r3 = r3 + r10
            r4 = r10
            r10 = r27
            r27 = r3
            goto L_0x012a
        L_0x0122:
            r10 = r27
            r4 = 0
            goto L_0x0128
        L_0x0126:
            r4 = 0
            r10 = 0
        L_0x0128:
            r27 = 0
        L_0x012a:
            com.android.systemui.qs.QSPanelController r3 = r9.mQsPanelController
            boolean r3 = r3.areThereTiles()
            r28 = r10
            java.lang.String r10 = "alpha"
            if (r3 == 0) goto L_0x07bd
            java.util.Iterator r29 = r0.iterator()
            r0 = 0
            r3 = 0
        L_0x013c:
            boolean r30 = r29.hasNext()
            if (r30 == 0) goto L_0x07bd
            java.lang.Object r30 = r29.next()
            r31 = r0
            r0 = r30
            com.android.systemui.plugins.qs.QSTile r0 = (com.android.systemui.plugins.p011qs.QSTile) r0
            r30 = r4
            com.android.systemui.qs.QSPanelController r4 = r9.mQsPanelController
            com.android.systemui.plugins.qs.QSTileView r4 = r4.getTileView((com.android.systemui.plugins.p011qs.QSTile) r0)
            r32 = r6
            java.lang.String r6 = "NTQSAnimator"
            if (r4 != 0) goto L_0x018d
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r33 = r7
            java.lang.String r7 = "tileView is null "
            r4.<init>((java.lang.String) r7)
            java.lang.String r0 = r0.getTileSpec()
            java.lang.StringBuilder r0 = r4.append((java.lang.String) r0)
            java.lang.String r0 = r0.toString()
            com.nothing.systemui.util.NTLogUtil.m1681e(r6, r0)
            r42 = r1
            r43 = r2
            r35 = r11
            r41 = r15
            r44 = r27
            r45 = r30
            r36 = r32
            r46 = r33
            r15 = r3
        L_0x0184:
            r30 = r5
            r33 = r10
            r27 = r21
            r10 = r8
            goto L_0x03b5
        L_0x018d:
            r33 = r7
            com.android.systemui.plugins.qs.QSIconView r7 = r4.getIcon()
            android.view.View r7 = r7.getIconView()
            r34 = r7
            com.android.systemui.plugins.qs.QS r7 = r9.mQs
            android.view.View r7 = r7.getView()
            r35 = r11
            com.android.systemui.qs.QuickQSPanelController r11 = r9.mQuickQSPanelController
            com.android.systemui.qs.QSPanel$QSTileLayout r11 = r11.getTileLayout()
            int r11 = r11.getNumVisibleTiles()
            if (r3 >= r11) goto L_0x0592
            boolean r11 = r9.mAllowFancy
            if (r11 == 0) goto L_0x0592
            com.android.systemui.qs.QuickQSPanelController r11 = r9.mQuickQSPanelController
            com.android.systemui.plugins.qs.QSTileView r11 = r11.getTileView((com.android.systemui.plugins.p011qs.QSTile) r0)
            java.lang.String r0 = r0.getTileSpec()
            boolean r34 = com.nothing.systemui.p024qs.QSTileHostEx.isFirstTile(r0)
            boolean r36 = com.nothing.systemui.p024qs.QSTileHostEx.isSecondTile(r0)
            boolean r0 = com.nothing.systemui.p024qs.QSTileHostEx.isBluetoothTile(r0)
            if (r34 != 0) goto L_0x01cf
            if (r0 == 0) goto L_0x01cc
            goto L_0x01cf
        L_0x01cc:
            r37 = 0
            goto L_0x01d1
        L_0x01cf:
            r37 = 1
        L_0x01d1:
            r38 = r3
            if (r0 == 0) goto L_0x01dc
            com.android.systemui.qs.QSPanelController r3 = r9.mQsPanelController
            int r3 = r3.getBtTileIndex(r4)
            goto L_0x01dd
        L_0x01dc:
            r3 = -1
        L_0x01dd:
            r39 = r4
            if (r0 == 0) goto L_0x01f8
            int r4 = r9.mBtIndex
            if (r4 == 0) goto L_0x01f8
            if (r3 == r4) goto L_0x01f8
            com.android.systemui.qs.QSPanelController r3 = r9.mQsPanelController
            com.android.systemui.plugins.qs.QSTileView r4 = r3.getBtTile(r4)
            if (r4 != 0) goto L_0x01f6
            java.lang.String r0 = "updateAnimators: tileView is null skip "
            com.nothing.systemui.util.NTLogUtil.m1680d(r6, r0)
            goto L_0x0204
        L_0x01f6:
            r6 = r4
            goto L_0x01fa
        L_0x01f8:
            r6 = r39
        L_0x01fa:
            if (r34 == 0) goto L_0x0216
            int r3 = r9.mSignalIndex
            int r4 = com.nothing.systemui.p024qs.QSTileHostEx.getFirstExpectedIndexInVP()
            if (r3 == r4) goto L_0x0216
        L_0x0204:
            r42 = r1
            r43 = r2
            r41 = r15
            r44 = r27
            r45 = r30
            r36 = r32
            r46 = r33
            r15 = r38
            goto L_0x0184
        L_0x0216:
            java.lang.String r3 = "translationX"
            if (r36 == 0) goto L_0x0228
            int r4 = r9.mSignalIndex
            r40 = r11
            int r11 = com.nothing.systemui.p024qs.QSTileHostEx.getSecondExpectedIndexInVP()
            if (r4 == r11) goto L_0x0226
            goto L_0x022a
        L_0x0226:
            r0 = 1
            goto L_0x0231
        L_0x0228:
            r40 = r11
        L_0x022a:
            if (r0 == 0) goto L_0x0399
            int r4 = r9.mBtIndex
            if (r4 == 0) goto L_0x0399
            goto L_0x0226
        L_0x0231:
            r9.mNonFirstCircleTileAnimating = r0
            if (r36 == 0) goto L_0x0244
            com.android.systemui.qs.QuickQSPanelController r4 = r9.mQuickQSPanelController
            java.lang.String r11 = com.nothing.systemui.p024qs.QSTileHostEx.getFirstSpec()
            com.android.systemui.plugins.qs.QSTile r11 = r4.getTile(r11)
            com.android.systemui.plugins.qs.QSTileView r11 = r4.getTileView((com.android.systemui.plugins.p011qs.QSTile) r11)
            goto L_0x0246
        L_0x0244:
            r11 = r40
        L_0x0246:
            r9.getRelativePosition(r15, r11, r7)
            r9.getRelativePosition(r8, r6, r7)
            r4 = r8[r0]
            r34 = r15[r0]
            int r34 = r4 - r34
            r0 = 0
            r4 = r8[r0]
            r18 = r15[r0]
            int r4 = r4 - r18
            com.android.systemui.qs.QuickStatusBarHeader r0 = r9.mQuickStatusBarHeader
            int r0 = r0.getOffsetTranslation()
            int r0 = r34 - r0
            r36 = r8
            r41 = r15
            r8 = 2
            float[] r15 = new float[r8]
            int r0 = -r0
            float r0 = (float) r0
            r18 = 0
            r15[r18] = r0
            r0 = 0
            r24 = 1
            r15[r24] = r0
            r12.addFloat(r6, r14, r15)
            float[] r15 = new float[r8]
            int r8 = -r4
            float r8 = (float) r8
            r15[r18] = r8
            r15[r24] = r0
            r13.addFloat(r6, r3, r15)
            if (r11 == 0) goto L_0x0382
            android.view.View r0 = r11.getLabelContainer()
            com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout r0 = (com.android.systemui.p012qs.tileimpl.IgnorableChildLinearLayout) r0
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r3 = r9.mNonFirstCircleTileHeightAnimator
            if (r3 != 0) goto L_0x029c
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r3 = new com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator
            int r8 = r11.getHeight()
            int r15 = r6.getHeight()
            r3.<init>(r9, r8, r15)
            r9.mNonFirstCircleTileHeightAnimator = r3
        L_0x029c:
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r3 = r9.mNonFirstCircleTileHeightAnimator
            r3.addView(r6)
            com.android.systemui.plugins.qs.QSIconView r3 = r6.getIcon()
            r8 = 2
            float[] r15 = new float[r8]
            r15 = {0, 1065353216} // fill-array
            r2.addFloat(r3, r10, r15)
            float[] r3 = new float[r8]
            r3 = {1065353216, 0} // fill-array
            com.android.systemui.qs.TouchAnimator$Builder r3 = r1.addFloat(r11, r10, r3)
            com.nothing.systemui.qs.NTQSAnimator$1 r15 = new com.nothing.systemui.qs.NTQSAnimator$1
            r15.<init>(r11, r0)
            r3.setListener(r15)
            float[] r0 = new float[r8]
            r0 = {0, 1065353216} // fill-array
            r5.addFloat(r6, r10, r0)
            android.view.View r0 = r6.getLabel()
            float[] r3 = new float[r8]
            r3 = {0, 1065353216} // fill-array
            r5.addFloat(r0, r10, r3)
            android.view.View r0 = r6.getSecondaryLabel()
            float[] r3 = new float[r8]
            r3 = {0, 1065353216} // fill-array
            r5.addFloat(r0, r10, r3)
            android.view.ViewParent r0 = r6.getParent()
            if (r0 == 0) goto L_0x02ff
            android.view.ViewParent r0 = r6.getParent()
            boolean r0 = r0 instanceof com.nothing.systemui.p024qs.CirclePagedTileLayout
            if (r0 == 0) goto L_0x02ff
            android.view.ViewParent r0 = r6.getParent()
            com.nothing.systemui.qs.CirclePagedTileLayout r0 = (com.nothing.systemui.p024qs.CirclePagedTileLayout) r0
            com.android.systemui.qs.PageIndicator r0 = r0.getPageIndicator()
            float[] r3 = new float[r8]
            r3 = {0, 1065353216} // fill-array
            r5.addFloat(r0, r10, r3)
        L_0x02ff:
            android.view.View r3 = r11.getLabelContainer()
            android.view.View r8 = r6.getLabelContainer()
            r15 = r31
            r0 = r52
            r42 = r1
            r1 = r3
            r3 = r2
            r2 = r8
            r43 = r3
            r44 = r27
            r8 = r38
            r3 = r7
            r27 = r21
            r15 = r26
            r45 = r30
            r21 = r4
            r15 = r5
            r5 = r34
            r30 = r15
            r15 = r32
            r32 = r6
            r6 = r41
            r19 = r7
            r46 = r33
            r7 = r27
            r33 = r10
            r10 = r36
            r36 = r15
            r15 = r8
            r8 = r20
            r0.translateCircleLabelContainerNonFirst(r1, r2, r3, r4, r5, r6, r7, r8)
            com.android.systemui.plugins.qs.QSIconView r1 = r11.getIcon()
            com.android.systemui.plugins.qs.QSIconView r2 = r32.getIcon()
            r3 = r19
            r7 = r23
            r8 = r17
            r0.translateCircleContentNonFirst(r1, r2, r3, r4, r5, r6, r7, r8)
            java.util.ArrayList<android.view.View> r0 = r9.mAllViews
            android.view.View r1 = r32.getLabel()
            r0.add(r1)
            java.util.ArrayList<android.view.View> r0 = r9.mAllViews
            android.view.View r1 = r32.getSecondaryLabel()
            r0.add(r1)
            java.util.ArrayList<android.view.View> r0 = r9.mAllViews
            r8 = r32
            r0.add(r8)
            java.util.ArrayList<com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout> r0 = r9.mCircleLabelNonFirst
            android.view.View r1 = r8.getLabelContainer()
            com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout r1 = (com.android.systemui.p012qs.tileimpl.IgnorableChildLinearLayout) r1
            r0.add(r1)
            java.util.ArrayList<android.view.View> r0 = r9.mNonFirstQsOrQqsViews
            boolean r0 = r0.contains(r11)
            if (r0 != 0) goto L_0x037e
            java.util.ArrayList<android.view.View> r0 = r9.mNonFirstQsOrQqsViews
            r0.add(r11)
        L_0x037e:
            int r3 = r15 + 1
            r8 = r10
            goto L_0x03b7
        L_0x0382:
            r42 = r1
            r43 = r2
            r44 = r27
            r45 = r30
            r46 = r33
            r15 = r38
            r30 = r5
            r33 = r10
            r27 = r21
            r10 = r36
            r36 = r32
            goto L_0x03b5
        L_0x0399:
            r42 = r1
            r43 = r2
            r19 = r7
            r41 = r15
            r44 = r27
            r45 = r30
            r36 = r32
            r46 = r33
            r15 = r38
            r30 = r5
            r33 = r10
            r27 = r21
            r10 = r8
            r8 = r6
            if (r40 != 0) goto L_0x03d1
        L_0x03b5:
            r8 = r10
            r3 = r15
        L_0x03b7:
            r21 = r27
            r5 = r30
            r0 = r31
            r10 = r33
            r11 = r35
            r6 = r36
            r15 = r41
            r1 = r42
            r2 = r43
            r27 = r44
            r4 = r45
            r7 = r46
            goto L_0x013c
        L_0x03d1:
            boolean r1 = r9.mOnFirstPage
            if (r1 != 0) goto L_0x03de
            if (r34 != 0) goto L_0x03d9
            if (r0 == 0) goto L_0x03de
        L_0x03d9:
            java.util.ArrayList<android.view.View> r0 = r9.mNonFirstQsOrQqsViews
            r0.add(r8)
        L_0x03de:
            r7 = r19
            r6 = r40
            r11 = r41
            r9.getRelativePosition(r11, r6, r7)
            r9.getRelativePosition(r10, r8, r7)
            r0 = 1
            r1 = r10[r0]
            r2 = r11[r0]
            int r19 = r1 - r2
            r1 = 0
            r2 = r10[r1]
            r4 = r11[r1]
            int r5 = r2 - r4
            com.android.systemui.qs.QuickStatusBarHeader r2 = r9.mQuickStatusBarHeader
            int r2 = r2.getOffsetTranslation()
            int r2 = r19 - r2
            r4 = 2
            float[] r0 = new float[r4]
            r21 = 0
            r0[r1] = r21
            float r1 = (float) r2
            r24 = 1
            r0[r24] = r1
            r12.addFloat(r6, r14, r0)
            float[] r0 = new float[r4]
            int r1 = -r2
            float r1 = (float) r1
            r2 = 0
            r0[r2] = r1
            r0[r24] = r21
            r12.addFloat(r8, r14, r0)
            float[] r0 = new float[r4]
            r0[r2] = r21
            float r1 = (float) r5
            r0[r24] = r1
            r13.addFloat(r6, r3, r0)
            float[] r0 = new float[r4]
            int r1 = -r5
            float r1 = (float) r1
            r0[r2] = r1
            r0[r24] = r21
            r13.addFloat(r8, r3, r0)
            if (r37 == 0) goto L_0x044b
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = r9.mQQSCircleTileHeightAnimator
            if (r0 != 0) goto L_0x0445
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = new com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator
            int r1 = r6.getHeight()
            int r2 = r8.getHeight()
            r0.<init>(r9, r1, r2)
            r9.mQQSCircleTileHeightAnimator = r0
        L_0x0445:
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = r9.mQQSCircleTileHeightAnimator
            r0.addView(r6)
            goto L_0x046c
        L_0x044b:
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = r9.mQQSTileHeightAnimator
            if (r0 != 0) goto L_0x0463
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = new com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator
            int r1 = r6.getHeight()
            int r2 = r8.getHeight()
            r0.<init>(r9, r1, r2)
            r9.mQQSTileHeightAnimator = r0
            int r0 = r6.getHeight()
            goto L_0x0465
        L_0x0463:
            r0 = r31
        L_0x0465:
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r1 = r9.mQQSTileHeightAnimator
            r1.addView(r6)
            r31 = r0
        L_0x046c:
            if (r37 == 0) goto L_0x050c
            android.view.View r0 = r6.getLabel()
            r1 = 2
            float[] r2 = new float[r1]
            r2 = {1065353216, 0} // fill-array
            r3 = r33
            r4 = r36
            r4.addFloat(r0, r3, r2)
            android.view.View r0 = r6.getLabel()
            float[] r2 = new float[r1]
            r2 = {0, 1065353216} // fill-array
            r1 = r30
            r1.addFloat(r0, r3, r2)
            android.view.View r0 = r6.getSecondaryLabel()
            r32 = r4
            r2 = 2
            float[] r4 = new float[r2]
            r4 = {0, 1065353216} // fill-array
            r1.addFloat(r0, r3, r4)
            android.view.ViewParent r0 = r8.getParent()
            if (r0 == 0) goto L_0x04bc
            android.view.ViewParent r0 = r8.getParent()
            boolean r0 = r0 instanceof com.nothing.systemui.p024qs.CirclePagedTileLayout
            if (r0 == 0) goto L_0x04bc
            android.view.ViewParent r0 = r8.getParent()
            com.nothing.systemui.qs.CirclePagedTileLayout r0 = (com.nothing.systemui.p024qs.CirclePagedTileLayout) r0
            com.android.systemui.qs.PageIndicator r0 = r0.getPageIndicator()
            float[] r4 = new float[r2]
            r4 = {0, 1065353216} // fill-array
            r1.addFloat(r0, r3, r4)
        L_0x04bc:
            android.view.View r2 = r6.getLabelContainer()
            android.view.View r4 = r8.getLabelContainer()
            r0 = r52
            r47 = r1
            r1 = r2
            r2 = r4
            r4 = r3
            r3 = r7
            r36 = r10
            r48 = r32
            r10 = r4
            r4 = r5
            r21 = r5
            r5 = r19
            r40 = r6
            r6 = r11
            r30 = r7
            r7 = r27
            r32 = r8
            r8 = r20
            r0.translateLabelContainer(r1, r2, r3, r4, r5, r6, r7, r8)
            com.android.systemui.plugins.qs.QSIconView r1 = r40.getIcon()
            com.android.systemui.plugins.qs.QSIconView r2 = r32.getIcon()
            r3 = r30
            r4 = r21
            r7 = r16
            r8 = r17
            r0.translateContent(r1, r2, r3, r4, r5, r6, r7, r8)
            java.util.ArrayList<android.view.View> r0 = r9.mAllViews
            android.view.View r1 = r40.getLabel()
            r0.add(r1)
            java.util.ArrayList<com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout> r0 = r9.mCircleLabel
            android.view.View r1 = r40.getLabelContainer()
            com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout r1 = (com.android.systemui.p012qs.tileimpl.IgnorableChildLinearLayout) r1
            r0.add(r1)
            goto L_0x053d
        L_0x050c:
            r21 = r5
            r40 = r6
            r32 = r8
            r47 = r30
            r48 = r36
            r30 = r7
            r36 = r10
            r10 = r33
            com.android.systemui.plugins.qs.QSIconView r1 = r40.getIcon()
            com.android.systemui.plugins.qs.QSIconView r2 = r32.getIcon()
            r0 = r52
            r3 = r30
            r4 = r21
            r5 = r19
            r6 = r11
            r7 = r13
            r8 = r12
            r0.translateContent(r1, r2, r3, r4, r5, r6, r7, r8)
            android.view.View r1 = r40.getLabelContainer()
            android.view.View r2 = r32.getLabelContainer()
            r0.translateContent(r1, r2, r3, r4, r5, r6, r7, r8)
        L_0x053d:
            android.view.View r1 = r40.getSecondaryIcon()
            android.view.View r2 = r32.getSecondaryIcon()
            r0 = r52
            r3 = r30
            r4 = r21
            r5 = r19
            r6 = r11
            r7 = r13
            r8 = r12
            r0.translateContent(r1, r2, r3, r4, r5, r6, r7, r8)
            if (r37 != 0) goto L_0x0565
            android.view.View r0 = r40.getSecondaryLabel()
            r1 = 2
            float[] r2 = new float[r1]
            r2 = {0, 1065353216} // fill-array
            r8 = r35
            r8.addFloat(r0, r10, r2)
            goto L_0x0567
        L_0x0565:
            r8 = r35
        L_0x0567:
            java.util.ArrayList<android.view.View> r0 = r9.mQuickQsViews
            r4 = r32
            r0.add(r4)
            java.util.ArrayList<android.view.View> r0 = r9.mAllViews
            r1 = r40
            r0.add(r1)
            java.util.ArrayList<android.view.View> r0 = r9.mAllViews
            android.view.View r1 = r1.getSecondaryLabel()
            r0.add(r1)
            r2 = r9
            r49 = r22
            r22 = r26
            r1 = r28
            r0 = r31
            r32 = r36
            r5 = r42
            r26 = r44
            r30 = r45
            r9 = r8
            goto L_0x079a
        L_0x0592:
            r42 = r1
            r43 = r2
            r39 = r4
            r47 = r5
            r36 = r8
            r11 = r15
            r44 = r27
            r45 = r30
            r48 = r32
            r46 = r33
            r8 = r35
            r15 = r3
            r30 = r7
            r27 = r21
            boolean r0 = r9.mFullRows
            if (r0 == 0) goto L_0x05e2
            boolean r0 = r9.isIconInAnimatedRow(r15)
            if (r0 == 0) goto L_0x05e2
            r0 = 2
            float[] r1 = new float[r0]
            r7 = r26
            int r0 = -r7
            float r0 = (float) r0
            r2 = 0
            r1[r2] = r0
            r0 = 0
            r2 = 1
            r1[r2] = r0
            r4 = r39
            r8.addFloat(r4, r14, r1)
            java.util.ArrayList<android.view.View> r0 = r9.mAllViews
            r1 = r34
            r0.add(r1)
            r2 = r4
            r9 = r8
            r49 = r22
            r1 = r28
            r32 = r36
            r5 = r42
            r26 = r44
            r30 = r45
            r22 = r7
            goto L_0x0795
        L_0x05e2:
            r7 = r26
            r4 = r39
            com.android.systemui.qs.QuickQSPanel r0 = r9.mQuickQsPanel
            com.android.systemui.qs.QSPanel$QSTileLayout r0 = r0.getTileLayout()
            r6 = r0
            com.android.systemui.qs.SideLabelTileLayout r6 = (com.android.systemui.p012qs.SideLabelTileLayout) r6
            r5 = r30
            r9.getRelativePosition(r11, r6, r5)
            r3 = r36
            r9.getRelativePosition(r3, r4, r5)
            r0 = 1
            r1 = r3[r0]
            r2 = r11[r0]
            int r0 = r6.getPhantomTopPosition(r15)
            int r2 = r2 + r0
            int r1 = r1 - r2
            boolean r0 = r9.mOnFirstPage
            r2 = r45
            r26 = r7
            if (r0 != 0) goto L_0x060e
            if (r15 >= r2) goto L_0x061d
        L_0x060e:
            r0 = 2
            float[] r7 = new float[r0]
            int r0 = -r1
            float r0 = (float) r0
            r1 = 0
            r7[r1] = r0
            r0 = 0
            r1 = 1
            r7[r1] = r0
            r12.addFloat(r4, r14, r7)
        L_0x061d:
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = r9.mOtherTilesExpandAnimator
            if (r0 != 0) goto L_0x062f
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = new com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator
            int r1 = r4.getHeight()
            r7 = r31
            r0.<init>(r9, r7, r1)
            r9.mOtherTilesExpandAnimator = r0
            goto L_0x0631
        L_0x062f:
            r7 = r31
        L_0x0631:
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = r9.mOtherTilesExpandAnimator
            r0.addView(r4)
            r0 = 1
            r4.setClipChildren(r0)
            r4.setClipToPadding(r0)
            android.view.View r0 = r4.getSecondaryLabel()
            r31 = r7
            r1 = 2
            float[] r7 = new float[r1]
            r7 = {0, 1065353216} // fill-array
            r8.addFloat(r0, r10, r7)
            boolean r0 = r9.mOnFirstPage
            if (r0 != 0) goto L_0x0785
            if (r15 < r2) goto L_0x0785
            r7 = r44
            if (r15 >= r7) goto L_0x0774
            int r0 = r15 + -2
            int r0 = r0 % r28
            int r0 = r0 + r1
            com.android.systemui.plugins.qs.QSTileView r1 = r6.getTile(r0)
            if (r1 == 0) goto L_0x066d
            r9.getRelativePosition(r11, r1, r5)
            r19 = 1
            r21 = r3[r19]
            r25 = r11[r19]
            int r21 = r21 - r25
            goto L_0x067b
        L_0x066d:
            r19 = 1
            r21 = r3[r19]
            r30 = r11[r19]
            int r19 = r6.getPhantomTopPosition(r0)
            int r30 = r30 + r19
            int r21 = r21 - r30
        L_0x067b:
            r19 = r21
            if (r1 == 0) goto L_0x0716
            r21 = r0
            java.util.ArrayList<android.view.View> r0 = r9.mNonFirstQsOrQqsViews
            r0.add(r1)
            r45 = r2
            r0 = 2
            float[] r2 = new float[r0]
            r2 = {1065353216, 0} // fill-array
            r9 = r42
            r9.addFloat(r1, r10, r2)
            float[] r2 = new float[r0]
            r2 = {0, 1065353216} // fill-array
            r0 = r22
            r0.addFloat(r4, r10, r2)
            com.android.systemui.plugins.qs.QSIconView r2 = r4.getIcon()
            r36 = r3
            r22 = r6
            r3 = 2
            float[] r6 = new float[r3]
            r6 = {0, 1065353216} // fill-array
            r0.addFloat(r2, r10, r6)
            android.view.View r2 = r4.getLabelContainer()
            float[] r6 = new float[r3]
            r6 = {0, 1065353216} // fill-array
            r0.addFloat(r2, r10, r6)
            android.view.View r2 = r4.getSecondaryIcon()
            float[] r6 = new float[r3]
            r6 = {0, 1065353216} // fill-array
            r0.addFloat(r2, r10, r6)
            com.android.systemui.plugins.qs.QSIconView r2 = r1.getIcon()
            com.android.systemui.plugins.qs.QSIconView r3 = r4.getIcon()
            r6 = 0
            r49 = r0
            r50 = r21
            r0 = r52
            r21 = r1
            r1 = r2
            r30 = r45
            r2 = r3
            r32 = r36
            r3 = r5
            r39 = r4
            r4 = r6
            r33 = r5
            r5 = r19
            r51 = r22
            r6 = r11
            r22 = r26
            r26 = r7
            r7 = r13
            r9 = r8
            r8 = r12
            r0.translateContent(r1, r2, r3, r4, r5, r6, r7, r8)
            android.view.View r1 = r21.getLabelContainer()
            android.view.View r2 = r39.getLabelContainer()
            r3 = r33
            r4 = r19
            r5 = r11
            r6 = r12
            r0.translateContentForY(r1, r2, r3, r4, r5, r6)
            android.view.View r1 = r21.getSecondaryIcon()
            android.view.View r2 = r39.getSecondaryIcon()
            r4 = 0
            r5 = r19
            r6 = r11
            r0.translateContent(r1, r2, r3, r4, r5, r6, r7, r8)
            r2 = r39
            r0 = 2
            goto L_0x0732
        L_0x0716:
            r50 = r0
            r30 = r2
            r32 = r3
            r39 = r4
            r51 = r6
            r9 = r8
            r49 = r22
            r22 = r26
            r0 = 2
            r26 = r7
            float[] r1 = new float[r0]
            r1 = {0, 1065353216} // fill-array
            r2 = r39
            r9.addFloat(r2, r10, r1)
        L_0x0732:
            float[] r1 = new float[r0]
            r0 = r19
            int r0 = -r0
            float r0 = (float) r0
            r3 = 0
            r1[r3] = r0
            r0 = 0
            r3 = 1
            r1[r3] = r0
            r12.addFloat(r2, r14, r1)
            int r0 = r26 + -1
            r1 = r28
            if (r15 != r0) goto L_0x0771
            r0 = r50
            if (r0 >= r1) goto L_0x0771
            int r0 = r0 + 1
        L_0x074e:
            if (r0 >= r1) goto L_0x0771
            r3 = r51
            com.android.systemui.plugins.qs.QSTileView r4 = r3.getTile(r0)
            if (r4 == 0) goto L_0x0768
            r5 = 0
            r4.setAlpha(r5)
            r5 = 2
            float[] r6 = new float[r5]
            r6 = {1065353216, 0} // fill-array
            r5 = r42
            r5.addFloat(r4, r10, r6)
            goto L_0x076a
        L_0x0768:
            r5 = r42
        L_0x076a:
            int r0 = r0 + 1
            r51 = r3
            r42 = r5
            goto L_0x074e
        L_0x0771:
            r5 = r42
            goto L_0x0795
        L_0x0774:
            r30 = r2
            r32 = r3
            r2 = r4
            r9 = r8
            r49 = r22
            r22 = r26
            r1 = r28
            r5 = r42
            r26 = r7
            goto L_0x0795
        L_0x0785:
            r30 = r2
            r32 = r3
            r2 = r4
            r9 = r8
            r49 = r22
            r22 = r26
            r1 = r28
            r5 = r42
            r26 = r44
        L_0x0795:
            r4 = r2
            r0 = r31
            r2 = r52
        L_0x079a:
            java.util.ArrayList<android.view.View> r3 = r2.mAllViews
            r3.add(r4)
            int r3 = r15 + 1
            r28 = r1
            r1 = r5
            r15 = r11
            r21 = r27
            r4 = r30
            r8 = r32
            r7 = r46
            r5 = r47
            r6 = r48
            r11 = r9
            r27 = r26
            r9 = r2
            r26 = r22
            r2 = r43
            r22 = r49
            goto L_0x013c
        L_0x07bd:
            r43 = r2
            r47 = r5
            r48 = r6
            r46 = r7
            r2 = r9
            r9 = r11
            r27 = r21
            r49 = r22
            r5 = r1
            boolean r0 = r2.mAllowFancy
            if (r0 == 0) goto L_0x0951
            com.android.systemui.qs.QSPanelController r0 = r2.mQsPanelController
            android.view.View r0 = r0.getBrightnessView()
            r1 = 1050253722(0x3e99999a, float:0.3)
            if (r0 == 0) goto L_0x0824
            int r3 = r0.getMeasuredHeight()
            r4 = 2
            float[] r6 = new float[r4]
            int r3 = -r3
            float r3 = (float) r3
            r4 = 0
            r6[r4] = r3
            r3 = 0
            r4 = 1
            r6[r4] = r3
            r9.addFloat(r0, r14, r6)
            boolean r4 = r2.mOnFirstPage
            if (r4 == 0) goto L_0x07f3
            goto L_0x07f4
        L_0x07f3:
            r3 = r1
        L_0x07f4:
            com.android.systemui.qs.TouchAnimator$Builder r4 = new com.android.systemui.qs.TouchAnimator$Builder
            r4.<init>()
            r6 = 2
            float[] r7 = new float[r6]
            r7 = {0, 1065353216} // fill-array
            com.android.systemui.qs.TouchAnimator$Builder r4 = r4.addFloat(r0, r10, r7)
            float[] r7 = new float[r6]
            r7 = {1050253722, 1065353216} // fill-array
            java.lang.String r6 = "sliderScaleY"
            com.android.systemui.qs.TouchAnimator$Builder r4 = r4.addFloat(r0, r6, r7)
            android.view.animation.Interpolator r6 = com.android.p019wm.shell.animation.Interpolators.ALPHA_IN
            com.android.systemui.qs.TouchAnimator$Builder r4 = r4.setInterpolator(r6)
            com.android.systemui.qs.TouchAnimator$Builder r3 = r4.setStartDelay(r3)
            com.android.systemui.qs.TouchAnimator r3 = r3.build()
            r2.mBrightnessAnimator = r3
            java.util.ArrayList<android.view.View> r3 = r2.mAllViews
            r3.add(r0)
            goto L_0x0827
        L_0x0824:
            r0 = 0
            r2.mBrightnessAnimator = r0
        L_0x0827:
            com.android.systemui.qs.TouchAnimator$Builder r0 = r9.setListener(r2)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mFirstPageAnimator = r0
            com.android.systemui.qs.TouchAnimator$Builder r0 = new com.android.systemui.qs.TouchAnimator$Builder
            r0.<init>()
            r3 = 2
            float[] r4 = new float[r3]
            r4 = {0, 1065353216} // fill-array
            r6 = r46
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.addFloat(r6, r10, r4)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mFirstPageDelayedAnimator = r0
            com.android.systemui.qs.TouchAnimator$Builder r0 = new com.android.systemui.qs.TouchAnimator$Builder
            r0.<init>()
            r4 = 1063004406(0x3f5c28f6, float:0.86)
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setStartDelay(r4)
            com.android.systemui.qs.QSSecurityFooter r4 = r2.mSecurityFooter
            android.view.View r4 = r4.getView()
            float[] r7 = new float[r3]
            r7 = {0, 1065353216} // fill-array
            r0.addFloat(r4, r10, r7)
            com.android.systemui.qs.QSPanelController r3 = r2.mQsPanelController
            boolean r3 = r3.shouldUseHorizontalLayout()
            if (r3 == 0) goto L_0x0882
            com.android.systemui.qs.QSPanelController r3 = r2.mQsPanelController
            com.android.systemui.media.MediaHost r3 = r3.mMediaHost
            com.android.systemui.util.animation.UniqueObjectHostView r3 = r3.hostView
            if (r3 == 0) goto L_0x0882
            com.android.systemui.qs.QSPanelController r3 = r2.mQsPanelController
            com.android.systemui.media.MediaHost r3 = r3.mMediaHost
            com.android.systemui.util.animation.UniqueObjectHostView r3 = r3.hostView
            r4 = 2
            float[] r7 = new float[r4]
            r7 = {0, 1065353216} // fill-array
            r0.addFloat(r3, r10, r7)
            goto L_0x088d
        L_0x0882:
            com.android.systemui.qs.QSPanelController r3 = r2.mQsPanelController
            com.android.systemui.media.MediaHost r3 = r3.mMediaHost
            com.android.systemui.util.animation.UniqueObjectHostView r3 = r3.hostView
            r4 = 1065353216(0x3f800000, float:1.0)
            r3.setAlpha(r4)
        L_0x088d:
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mAllPagesDelayedAnimator = r0
            java.util.ArrayList<android.view.View> r0 = r2.mAllViews
            com.android.systemui.qs.QSSecurityFooter r3 = r2.mSecurityFooter
            android.view.View r3 = r3.getView()
            r0.add(r3)
            com.android.systemui.qs.QSExpansionPathInterpolator r0 = r2.mQSExpansionPathInterpolator
            android.view.animation.Interpolator r0 = r0.getYInterpolator()
            r12.setInterpolator(r0)
            com.android.systemui.qs.QSExpansionPathInterpolator r0 = r2.mQSExpansionPathInterpolator
            android.view.animation.Interpolator r0 = r0.getXInterpolator()
            r13.setInterpolator(r0)
            com.android.systemui.qs.TouchAnimator r0 = r12.build()
            r2.mTranslationYAnimator = r0
            com.android.systemui.qs.TouchAnimator r0 = r13.build()
            r2.mTranslationXAnimator = r0
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = r2.mQQSTileHeightAnimator
            if (r0 == 0) goto L_0x08c9
            com.android.systemui.qs.QSExpansionPathInterpolator r3 = r2.mQSExpansionPathInterpolator
            android.view.animation.Interpolator r3 = r3.getYInterpolator()
            r0.setInterpolator(r3)
        L_0x08c9:
            com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator r0 = r2.mOtherTilesExpandAnimator
            if (r0 == 0) goto L_0x08d6
            com.android.systemui.qs.QSExpansionPathInterpolator r3 = r2.mQSExpansionPathInterpolator
            android.view.animation.Interpolator r3 = r3.getYInterpolator()
            r0.setInterpolator(r3)
        L_0x08d6:
            r0 = 1059766403(0x3f2ac083, float:0.667)
            r3 = r48
            com.android.systemui.qs.TouchAnimator$Builder r3 = r3.setEndDelay(r0)
            com.android.systemui.qs.TouchAnimator r3 = r3.build()
            r2.mLabelInOutAnimator = r3
            r3 = 1051361018(0x3eaa7efa, float:0.333)
            r4 = r47
            com.android.systemui.qs.TouchAnimator$Builder r3 = r4.setStartDelay(r3)
            com.android.systemui.qs.TouchAnimator r3 = r3.build()
            r2.mLabelSecondLabelInOutAnimator = r3
            r3 = r27
            com.android.systemui.qs.TouchAnimator$Builder r3 = r3.setStartDelay(r0)
            com.android.systemui.qs.TouchAnimator r3 = r3.build()
            r2.mLabelContainerXAnimator = r3
            r3 = r20
            com.android.systemui.qs.TouchAnimator$Builder r0 = r3.setStartDelay(r0)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mLabelContainerYAnimator = r0
            com.android.systemui.qs.TouchAnimator r0 = r16.build()
            r2.mIconXAnimator = r0
            com.android.systemui.qs.TouchAnimator r0 = r17.build()
            r2.mIconYAnimator = r0
            r0 = r43
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setStartDelay(r1)
            r3 = 1053609165(0x3ecccccd, float:0.4)
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setEndDelay(r3)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mIconInOutNonFirstAnimator = r0
            r0 = 1060320051(0x3f333333, float:0.7)
            com.android.systemui.qs.TouchAnimator$Builder r0 = r5.setEndDelay(r0)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mQQSNonFirstAnimator = r0
            r0 = r23
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setStartDelay(r1)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mIconXNonFirstAnimator = r0
            r0 = r49
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setStartDelay(r1)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mNtNonFirstPageAnimator = r0
            goto L_0x0953
        L_0x0951:
            r6 = r46
        L_0x0953:
            com.android.systemui.qs.TouchAnimator$Builder r0 = new com.android.systemui.qs.TouchAnimator$Builder
            r0.<init>()
            com.android.systemui.qs.QuickQSPanel r1 = r2.mQuickQsPanel
            r3 = 2
            float[] r4 = new float[r3]
            r4 = {1065353216, 0} // fill-array
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.addFloat(r1, r10, r4)
            com.android.systemui.qs.TouchAnimator$Listener r1 = r2.mNonFirstPageListener
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setListener(r1)
            r1 = 1056964608(0x3f000000, float:0.5)
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setEndDelay(r1)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mNonfirstPageAnimator = r0
            com.android.systemui.qs.TouchAnimator$Builder r0 = new com.android.systemui.qs.TouchAnimator$Builder
            r0.<init>()
            r1 = 1041194025(0x3e0f5c29, float:0.14)
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setStartDelay(r1)
            r1 = 2
            float[] r1 = new float[r1]
            r1 = {0, 1065353216} // fill-array
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.addFloat(r6, r10, r1)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.mNonfirstPageDelayedAnimator = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.p024qs.NTQSAnimator.updateAnimators():void");
    }

    private boolean isIconInAnimatedRow(int i) {
        PagedTileLayout pagedTileLayout = this.mPagedLayout;
        if (pagedTileLayout == null) {
            return false;
        }
        int columnCount = pagedTileLayout.getColumnCount();
        if (i < (((this.mNumQuickTiles + columnCount) - 1) / columnCount) * columnCount) {
            return true;
        }
        return false;
    }

    private void getRelativePosition(int[] iArr, View view, View view2) {
        iArr[0] = (view.getWidth() / 2) + 0;
        iArr[1] = 0;
        getRelativePositionInt(iArr, view, view2);
    }

    private void getRelativePositionInt(int[] iArr, View view, View view2) {
        if (view != view2 && view != null) {
            if (!isAPage(view)) {
                iArr[0] = iArr[0] + view.getLeft();
                iArr[1] = iArr[1] + view.getTop();
            }
            if (!(view instanceof PagedTileLayout)) {
                iArr[0] = iArr[0] - view.getScrollX();
                iArr[1] = iArr[1] - view.getScrollY();
            }
            getRelativePositionInt(iArr, (View) view.getParent(), view2);
        }
    }

    private boolean isAPage(View view) {
        return view.getClass().equals(SideLabelTileLayout.class);
    }

    public void setPosition(float f) {
        if (this.mNeedsAnimatorUpdate || f == 0.0f) {
            updateAnimators();
        }
        if (this.mFirstPageAnimator != null) {
            if (this.mOnKeyguard) {
                f = this.mShowCollapsedOnKeyguard ? 0.0f : 1.0f;
            }
            this.mLastPosition = f;
            if (f == 0.0f) {
                this.mToExpand = true;
            } else if (((double) f) == 1.0d) {
                this.mToExpand = false;
            }
            PagedTileLayout pagedTileLayout = this.mPagedLayout;
            if (pagedTileLayout != null) {
                pagedTileLayout.setQsExpansion(f);
            }
            this.mQuickQsPanel.setAlpha(1.0f);
            this.mFirstPageAnimator.setPosition(f);
            if (this.mOnFirstPage) {
                this.mFirstPageDelayedAnimator.setPosition(f);
            } else {
                TouchAnimator touchAnimator = this.mNtNonFirstPageAnimator;
                if (touchAnimator != null) {
                    touchAnimator.setPosition(f);
                }
            }
            this.mTranslationYAnimator.setPosition(f);
            this.mTranslationXAnimator.setPosition(f);
            HeightExpansionAnimator heightExpansionAnimator = this.mQQSTileHeightAnimator;
            if (heightExpansionAnimator != null) {
                heightExpansionAnimator.setPosition(f);
            }
            HeightExpansionAnimator heightExpansionAnimator2 = this.mOtherTilesExpandAnimator;
            if (heightExpansionAnimator2 != null) {
                heightExpansionAnimator2.setPosition(f);
            }
            for (int i = 0; i < this.mCircleLabel.size(); i++) {
                this.mCircleLabel.get(i).setPosition(f, true);
            }
            for (int i2 = 0; i2 < this.mCircleLabelNonFirst.size(); i2++) {
                this.mCircleLabelNonFirst.get(i2).setPosition(f, false);
            }
            HeightExpansionAnimator heightExpansionAnimator3 = this.mQQSCircleTileHeightAnimator;
            if (heightExpansionAnimator3 != null) {
                heightExpansionAnimator3.setPosition(f);
            }
            HeightExpansionAnimator heightExpansionAnimator4 = this.mNonFirstCircleTileHeightAnimator;
            if (heightExpansionAnimator4 != null) {
                heightExpansionAnimator4.setPosition(f);
            }
            TouchAnimator touchAnimator2 = this.mLabelSecondLabelInOutAnimator;
            if (touchAnimator2 != null) {
                if (f >= 0.333f) {
                    touchAnimator2.setPosition(f);
                } else {
                    touchAnimator2.setPosition(0.0f);
                }
            }
            TouchAnimator touchAnimator3 = this.mLabelInOutAnimator;
            if (touchAnimator3 != null && f <= 0.333f) {
                touchAnimator3.setPosition(f);
            }
            TouchAnimator touchAnimator4 = this.mLabelContainerXAnimator;
            if (touchAnimator4 != null && f >= 0.667f) {
                touchAnimator4.setPosition(f);
            }
            TouchAnimator touchAnimator5 = this.mLabelContainerYAnimator;
            if (touchAnimator5 != null) {
                if (f > 0.667f) {
                    touchAnimator5.setPosition(f);
                } else if (f <= 0.5f) {
                    for (int i3 = 0; i3 < this.mCircleLabelNonFirst.size(); i3++) {
                        IgnorableChildLinearLayout ignorableChildLinearLayout = this.mCircleLabelNonFirst.get(i3);
                        ignorableChildLinearLayout.setTranslationX(this.mNonFirstCircleTileTranslationX);
                        ignorableChildLinearLayout.setTranslationY(this.mNonFirstCircleTileTranslationY);
                    }
                }
            }
            TouchAnimator touchAnimator6 = this.mIconInOutNonFirstAnimator;
            if (touchAnimator6 != null && f < 0.6f) {
                touchAnimator6.setPosition(f);
            }
            TouchAnimator touchAnimator7 = this.mQQSNonFirstAnimator;
            if (touchAnimator7 != null && ((double) f) < 0.3d) {
                touchAnimator7.setPosition(f);
            }
            TouchAnimator touchAnimator8 = this.mIconXNonFirstAnimator;
            if (touchAnimator8 != null) {
                touchAnimator8.setPosition(f);
            }
            TouchAnimator touchAnimator9 = this.mIconXAnimator;
            if (touchAnimator9 != null) {
                touchAnimator9.setPosition(f);
            }
            TouchAnimator touchAnimator10 = this.mIconYAnimator;
            if (touchAnimator10 != null) {
                touchAnimator10.setPosition(f);
            }
            if (this.mAllowFancy) {
                this.mAllPagesDelayedAnimator.setPosition(f);
                TouchAnimator touchAnimator11 = this.mBrightnessAnimator;
                if (touchAnimator11 != null) {
                    touchAnimator11.setPosition(f);
                }
            }
        }
    }

    public void onAnimationAtStart() {
        this.mQuickQsPanel.setVisibility(0);
    }

    public void onAnimationAtEnd() {
        this.mQuickQsPanel.setVisibility(4);
        int size = this.mQuickQsViews.size();
        for (int i = 0; i < size; i++) {
            this.mQuickQsViews.get(i).setVisibility(0);
        }
        int size2 = this.mNonFirstQsOrQqsViews.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mNonFirstQsOrQqsViews.get(i2).setAlpha(1.0f);
        }
        HeightExpansionAnimator heightExpansionAnimator = this.mNonFirstCircleTileHeightAnimator;
        if (heightExpansionAnimator != null) {
            heightExpansionAnimator.resetViewsHeights();
        }
    }

    public void onAnimationStarted() {
        updateQQSVisibility();
        if (this.mOnFirstPage) {
            int size = this.mQuickQsViews.size();
            for (int i = 0; i < size; i++) {
                this.mQuickQsViews.get(i).setVisibility(4);
            }
        }
        int size2 = this.mNonFirstQsOrQqsViews.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mNonFirstQsOrQqsViews.get(i2).setAlpha(0.0f);
        }
    }

    private void clearAnimationState() {
        int size = this.mAllViews.size();
        this.mQuickQsPanel.setAlpha(0.0f);
        for (int i = 0; i < size; i++) {
            View view = this.mAllViews.get(i);
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleY(1.0f);
            if (view instanceof SideLabelTileLayout) {
                SideLabelTileLayout sideLabelTileLayout = (SideLabelTileLayout) view;
                sideLabelTileLayout.setClipChildren(false);
                sideLabelTileLayout.setClipToPadding(false);
            }
        }
        HeightExpansionAnimator heightExpansionAnimator = this.mQQSTileHeightAnimator;
        if (heightExpansionAnimator != null) {
            heightExpansionAnimator.resetViewsHeights();
        }
        HeightExpansionAnimator heightExpansionAnimator2 = this.mOtherTilesExpandAnimator;
        if (heightExpansionAnimator2 != null) {
            heightExpansionAnimator2.resetViewsHeights();
        }
        HeightExpansionAnimator heightExpansionAnimator3 = this.mQQSCircleTileHeightAnimator;
        if (heightExpansionAnimator3 != null) {
            heightExpansionAnimator3.resetViewsHeights();
        }
        HeightExpansionAnimator heightExpansionAnimator4 = this.mNonFirstCircleTileHeightAnimator;
        if (heightExpansionAnimator4 != null) {
            heightExpansionAnimator4.resetViewsHeights();
        }
        for (int i2 = 0; i2 < this.mNonFirstQsOrQqsViews.size(); i2++) {
            this.mNonFirstQsOrQqsViews.get(i2).setAlpha(1.0f);
        }
        int size2 = this.mQuickQsViews.size();
        for (int i3 = 0; i3 < size2; i3++) {
            this.mQuickQsViews.get(i3).setVisibility(0);
        }
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        executeUpdateAnimation();
    }

    public void onTilesChanged() {
        executeUpdateAnimation();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-nothing-systemui-qs-NTQSAnimator  reason: not valid java name */
    public /* synthetic */ void m3507lambda$new$0$comnothingsystemuiqsNTQSAnimator() {
        updateAnimators();
        setCurrentPosition();
    }

    public void setTranslateWhileExpanding(boolean z) {
        this.mTranslateWhileExpanding = z;
    }

    /* renamed from: com.nothing.systemui.qs.NTQSAnimator$HeightExpansionAnimator */
    static class HeightExpansionAnimator {
        private final ValueAnimator mAnimator;
        /* access modifiers changed from: private */
        public final TouchAnimator.Listener mListener;
        private final ValueAnimator.AnimatorUpdateListener mUpdateListener;
        /* access modifiers changed from: private */
        public final List<View> mViews = new ArrayList();

        HeightExpansionAnimator(TouchAnimator.Listener listener, int i, int i2) {
            C41851 r0 = new ValueAnimator.AnimatorUpdateListener() {
                float mLastT = -1.0f;

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    int size = HeightExpansionAnimator.this.mViews.size();
                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    for (int i = 0; i < size; i++) {
                        View view = (View) HeightExpansionAnimator.this.mViews.get(i);
                        view.setBottom(view.getTop() + intValue);
                        if (view instanceof HeightOverrideable) {
                            ((HeightOverrideable) view).setHeightOverride(intValue);
                        }
                    }
                    if (animatedFraction == 0.0f) {
                        HeightExpansionAnimator.this.mListener.onAnimationAtStart();
                    } else if (animatedFraction == 1.0f) {
                        HeightExpansionAnimator.this.mListener.onAnimationAtEnd();
                    } else {
                        float f = this.mLastT;
                        if (f <= 0.0f || f == 1.0f) {
                            HeightExpansionAnimator.this.mListener.onAnimationStarted();
                        }
                    }
                    this.mLastT = animatedFraction;
                }
            };
            this.mUpdateListener = r0;
            this.mListener = listener;
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
            this.mAnimator = ofInt;
            ofInt.setRepeatCount(-1);
            ofInt.setRepeatMode(2);
            ofInt.addUpdateListener(r0);
        }

        /* access modifiers changed from: package-private */
        public void addView(View view) {
            this.mViews.add(view);
        }

        /* access modifiers changed from: package-private */
        public void setInterpolator(TimeInterpolator timeInterpolator) {
            this.mAnimator.setInterpolator(timeInterpolator);
        }

        /* access modifiers changed from: package-private */
        public void setPosition(float f) {
            this.mAnimator.setCurrentFraction(f);
        }

        /* access modifiers changed from: package-private */
        public void resetViewsHeights() {
            int size = this.mViews.size();
            for (int i = 0; i < size; i++) {
                View view = this.mViews.get(i);
                view.setBottom(view.getTop() + view.getMeasuredHeight());
                if (view instanceof HeightOverrideable) {
                    ((HeightOverrideable) view).resetOverride();
                }
            }
        }
    }

    public void onSignalPageChanged(int i) {
        if (i != this.mSignalIndex) {
            this.mSignalIndex = i;
            updateAnimators();
        }
    }

    public void onBtPageChanged(int i) {
        if (i != this.mBtIndex) {
            this.mBtIndex = i;
            updateAnimators();
            setPosition(this.mLastPosition);
        }
    }

    private void translateLabelContainer(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        int i7 = (i5 - i3) - i;
        float f = (float) i7;
        builder.addFloat(view, Key.TRANSLATION_X, f, f);
        builder.addFloat(view2, Key.TRANSLATION_X, (float) (-i7), 0.0f);
        float f2 = (float) ((i6 - i4) - i2);
        builder2.addFloat(view, Key.TRANSLATION_Y, 0.667f * f2, f2);
        builder2.addFloat(view2, Key.TRANSLATION_Y, f2 * -0.667f, 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }

    private void translateCircleLabelContainerNonFirst(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = (iArr[0] - i3) - i;
        int i6 = (iArr[1] - i4) - i2;
        builder2.addFloat(view2, Key.TRANSLATION_Y, ((float) i6) * -0.667f, 0.0f);
        this.mAllViews.add(view2);
        this.mNonFirstCircleTileTranslationX = (float) (-i5);
        this.mNonFirstCircleTileTranslationY = (float) (-i6);
    }

    private void translateCircleContentNonFirst(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        builder.addFloat(view2, Key.TRANSLATION_X, (float) (-((i5 - i3) - i)), 0.0f);
        builder2.addFloat(view2, Key.TRANSLATION_Y, (float) (-((i6 - i4) - i2)), 0.0f);
        this.mAllViews.add(view2);
    }

    private void translateContentForY(View view, View view2, View view3, int i, int[] iArr, TouchAnimator.Builder builder) {
        getRelativePosition(iArr, view, view3);
        int i2 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i3 = (iArr[1] - i2) - i;
        builder.addFloat(view, Key.TRANSLATION_Y, 0.0f, (float) i3);
        builder.addFloat(view2, Key.TRANSLATION_Y, (float) (-i3), 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }

    public boolean isNonFirstCircleTileAnimating() {
        return this.mNonFirstCircleTileAnimating;
    }

    private void executeUpdateAnimation() {
        this.mMainHandler.removeCallbacks(this.mUpdateAnimators);
        this.mMainHandler.post(this.mUpdateAnimators);
    }
}
