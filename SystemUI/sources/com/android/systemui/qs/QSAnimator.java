package com.android.systemui.qs;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.qs.tileimpl.HeightOverrideable;
import com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.android.wm.shell.animation.Interpolators;
import com.nothingos.systemui.qs.CirclePagedTileLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class QSAnimator implements QSHost.Callback, PagedTileLayout.PageListener, TouchAnimator.Listener, View.OnLayoutChangeListener, View.OnAttachStateChangeListener, TunerService.Tunable {
    static final boolean DEBUG = Log.isLoggable("QSAnimator", 3);
    private TouchAnimator mAllPagesDelayedAnimator;
    private boolean mAllowFancy;
    private TouchAnimator mBrightnessAnimator;
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
    private boolean mNonFirstCircleTileAnimating;
    private HeightExpansionAnimator mNonFirstCircleTileHeightAnimator;
    private float mNonFirstCircleTileTranslationX;
    private float mNonFirstCircleTileTranslationY;
    private TouchAnimator mNonfirstPageAnimator;
    private TouchAnimator mNonfirstPageDelayedAnimator;
    private TouchAnimator mNtNonFirstPageAnimator;
    private int mNumQuickTiles;
    private boolean mOnKeyguard;
    private HeightExpansionAnimator mOtherTilesExpandAnimator;
    private PagedTileLayout mPagedLayout;
    private HeightExpansionAnimator mQQSCircleTileHeightAnimator;
    private TouchAnimator mQQSNonFirstAnimator;
    private HeightExpansionAnimator mQQSTileHeightAnimator;
    private QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    private final QS mQs;
    private final QSPanelController mQsPanelController;
    private final QuickQSPanelController mQuickQSPanelController;
    private final QuickQSPanel mQuickQsPanel;
    private final QuickStatusBarHeader mQuickStatusBarHeader;
    private final QSSecurityFooter mSecurityFooter;
    private boolean mShowCollapsedOnKeyguard;
    private boolean mToShowing;
    private boolean mTranslateWhileExpanding;
    private TouchAnimator mTranslationXAnimator;
    private TouchAnimator mTranslationYAnimator;
    private final TunerService mTunerService;
    private final ArrayList<View> mAllViews = new ArrayList<>();
    private final ArrayList<View> mQuickQsViews = new ArrayList<>();
    private boolean mOnFirstPage = true;
    private boolean mNeedsAnimatorUpdate = false;
    private final TouchAnimator.Listener mNonFirstPageListener = new TouchAnimator.ListenerAdapter() { // from class: com.android.systemui.qs.QSAnimator.2
        @Override // com.android.systemui.qs.TouchAnimator.ListenerAdapter, com.android.systemui.qs.TouchAnimator.Listener
        public void onAnimationAtEnd() {
            QSAnimator.this.mQuickQsPanel.setVisibility(4);
        }

        @Override // com.android.systemui.qs.TouchAnimator.Listener
        public void onAnimationStarted() {
            QSAnimator.this.mQuickQsPanel.setVisibility(0);
        }
    };
    private final Runnable mUpdateAnimators = new Runnable() { // from class: com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            QSAnimator.this.lambda$new$0();
        }
    };
    private QSExpansionPathInterpolator mCircleQSExpansionPathInterpolator = new QSExpansionPathInterpolator();
    private boolean mToExpand = true;
    private ArrayList<IgnorableChildLinearLayout> mCircleLabel = new ArrayList<>();
    private ArrayList<IgnorableChildLinearLayout> mCircleLabelNonFirst = new ArrayList<>();
    private ArrayList<View> mNonFirstQsOrQqsViews = new ArrayList<>();
    private int mSignalIndex = 0;
    private int mBtIndex = 0;

    public QSAnimator(QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, QSSecurityFooter qSSecurityFooter, Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator) {
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
            onViewAttachedToWindow(null);
        }
        QSPanel.QSTileLayout tileLayout = qSPanelController.getTileLayout();
        if (tileLayout instanceof PagedTileLayout) {
            this.mPagedLayout = (PagedTileLayout) tileLayout;
        } else {
            Log.w("QSAnimator", "QS Not using page layout");
        }
        qSPanelController.setPageListener(this);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startAlphaAnimation(boolean z) {
        if (z == this.mToShowing) {
            return;
        }
        this.mToShowing = z;
        if (z) {
            CrossFadeHelper.fadeIn(this.mQs.getView(), 200L, 0);
        } else {
            CrossFadeHelper.fadeOut(this.mQs.getView(), 50L, 0, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        this.mTunerService.addTunable(this, "sysui_qs_fancy_anim", "sysui_qs_move_whole_rows");
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        this.mHost.removeCallback(this);
        this.mTunerService.removeTunable(this);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("sysui_qs_fancy_anim".equals(str)) {
            boolean parseIntegerSwitch = TunerService.parseIntegerSwitch(str2, true);
            this.mAllowFancy = parseIntegerSwitch;
            if (!parseIntegerSwitch) {
                clearAnimationState();
            }
        } else if ("sysui_qs_move_whole_rows".equals(str)) {
            this.mFullRows = TunerService.parseIntegerSwitch(str2, true);
        }
        updateAnimators();
    }

    @Override // com.android.systemui.qs.PagedTileLayout.PageListener
    public void onPageChanged(boolean z) {
        if (DEBUG) {
            Log.d("QSAnimator", "onPageChanged: mOnFirstPage " + this.mOnFirstPage + " isFirst " + z);
        }
        if (this.mOnFirstPage == z) {
            return;
        }
        if (!z) {
            clearAnimationState();
        }
        this.mOnFirstPage = z;
        updateAnimators();
    }

    private void translateContent(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        int i7 = (i5 - i3) - i;
        builder.addFloat(view, "translationX", 0.0f, i7);
        builder.addFloat(view2, "translationX", -i7, 0.0f);
        int i8 = (i6 - i4) - i2;
        builder2.addFloat(view, "translationY", 0.0f, i8);
        builder2.addFloat(view2, "translationY", -i8, 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }

    /* JADX WARN: Removed duplicated region for block: B:73:0x031e  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0345  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0450  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateAnimators() {
        TouchAnimator.Builder builder;
        int i;
        QSPanel.QSTileLayout qSTileLayout;
        int i2;
        int i3;
        int i4;
        int i5;
        TouchAnimator.Builder builder2;
        QSPanel.QSTileLayout qSTileLayout2;
        UniqueObjectHostView uniqueObjectHostView;
        int i6;
        int i7;
        String str;
        int i8;
        TouchAnimator.Builder builder3;
        TouchAnimator.Builder builder4;
        TouchAnimator.Builder builder5;
        TouchAnimator.Builder builder6;
        QSPanel.QSTileLayout qSTileLayout3;
        int i9;
        int i10;
        int[] iArr;
        TouchAnimator.Builder builder7;
        String str2;
        TouchAnimator.Builder builder8;
        TouchAnimator.Builder builder9;
        int i11;
        int[] iArr2;
        TouchAnimator.Builder builder10;
        String str3;
        String str4;
        String str5;
        int i12;
        QSTileView qSTileView;
        int i13;
        int[] iArr3;
        int[] iArr4;
        TouchAnimator.Builder builder11;
        int i14;
        int i15;
        int i16;
        int phantomTopPosition;
        int i17;
        SideLabelTileLayout sideLabelTileLayout;
        TouchAnimator.Builder builder12;
        int i18;
        int i19;
        TouchAnimator.Builder builder13;
        TouchAnimator.Builder builder14;
        QSTileView qSTileView2;
        QSAnimator qSAnimator;
        boolean z;
        int i20;
        int i21;
        QSTileView qSTileView3;
        int i22;
        View view;
        int[] iArr5;
        TouchAnimator.Builder builder15;
        int i23;
        QSAnimator qSAnimator2 = this;
        boolean z2 = DEBUG;
        String str6 = "QSAnimator";
        if (z2) {
            Log.d(str6, "updateAnimators: mOnFirstPage " + qSAnimator2.mOnFirstPage + " update from " + Debug.getCallers(2));
        }
        qSAnimator2.mNeedsAnimatorUpdate = false;
        TouchAnimator.Builder builder16 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder17 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder18 = new TouchAnimator.Builder();
        Collection<QSTile> tiles = qSAnimator2.mHost.getTiles();
        int[] iArr6 = new int[2];
        int[] iArr7 = new int[2];
        clearAnimationState();
        qSAnimator2.mAllViews.clear();
        qSAnimator2.mQuickQsViews.clear();
        qSAnimator2.mQQSTileHeightAnimator = null;
        qSAnimator2.mOtherTilesExpandAnimator = null;
        qSAnimator2.mQQSCircleTileHeightAnimator = null;
        qSAnimator2.mLabelInOutAnimator = null;
        qSAnimator2.mLabelSecondLabelInOutAnimator = null;
        qSAnimator2.mLabelContainerXAnimator = null;
        qSAnimator2.mLabelContainerYAnimator = null;
        qSAnimator2.mIconXAnimator = null;
        qSAnimator2.mIconYAnimator = null;
        TouchAnimator.Builder builder19 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder20 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder21 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder22 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder23 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder24 = new TouchAnimator.Builder();
        qSAnimator2.mIconInOutNonFirstAnimator = null;
        qSAnimator2.mQQSNonFirstAnimator = null;
        qSAnimator2.mIconXNonFirstAnimator = null;
        qSAnimator2.mNonFirstCircleTileHeightAnimator = null;
        qSAnimator2.mNonFirstCircleTileAnimating = false;
        TouchAnimator.Builder builder25 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder26 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder27 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder28 = new TouchAnimator.Builder();
        qSAnimator2.mCircleLabel.clear();
        qSAnimator2.mCircleLabelNonFirst.clear();
        qSAnimator2.mNonFirstQsOrQqsViews.clear();
        qSAnimator2.mNumQuickTiles = qSAnimator2.mQuickQsPanel.getNumQuickTiles();
        QSPanel.QSTileLayout tileLayout = qSAnimator2.mQsPanelController.getTileLayout();
        TouchAnimator.Builder builder29 = builder21;
        TouchAnimator.Builder builder30 = builder19;
        qSAnimator2.mAllViews.add((View) tileLayout);
        int measuredHeight = ((qSAnimator2.mQs.getView() != null ? qSAnimator2.mQs.getView().getMeasuredHeight() : 0) - qSAnimator2.mQs.getHeader().getBottom()) + qSAnimator2.mQs.getHeader().getPaddingBottom();
        if (!qSAnimator2.mTranslateWhileExpanding) {
            measuredHeight = (int) (measuredHeight * 0.1f);
        }
        int i24 = measuredHeight;
        TouchAnimator.Builder builder31 = builder28;
        String str7 = "translationY";
        TouchAnimator.Builder builder32 = builder20;
        if (qSAnimator2.mOnFirstPage) {
            builder = builder26;
            i = 0;
            builder16.addFloat(tileLayout, str7, i24, 0.0f);
        } else {
            builder = builder26;
            i = 0;
            ViewGroup viewGroup = (ViewGroup) tileLayout;
            viewGroup.setClipChildren(false);
            viewGroup.setClipToPadding(false);
        }
        if (tileLayout instanceof PagedTileLayout) {
            PagedTileLayout pagedTileLayout = (PagedTileLayout) tileLayout;
            int currentPageNumber = pagedTileLayout.getCurrentPageNumber();
            int pageTilesNum = pagedTileLayout.getPageTilesNum(i);
            if (currentPageNumber != 0) {
                int i25 = (currentPageNumber * pageTilesNum) + 2;
                i3 = pagedTileLayout.getNumVisibleTiles() + i25;
                qSTileLayout = tileLayout;
                i2 = currentPageNumber;
                i4 = i25;
                i5 = pageTilesNum;
            } else {
                i5 = pageTilesNum;
                i3 = 0;
                qSTileLayout = tileLayout;
                i2 = currentPageNumber;
                i4 = 0;
            }
        } else {
            qSTileLayout = tileLayout;
            i2 = 0;
            i3 = 0;
            i4 = 0;
            i5 = 0;
        }
        if (z2) {
            StringBuilder sb = new StringBuilder();
            builder2 = builder16;
            sb.append("updateAnimators: curQSPage ");
            sb.append(i2);
            sb.append(" firstQSPageTilesNum ");
            sb.append(i5);
            sb.append(" nonFirstQSTilesStart ");
            sb.append(i4);
            sb.append(" nonFirstQSTilesEnd ");
            sb.append(i3);
            sb.append(" heightDiff ");
            sb.append(i24);
            Log.d(str6, sb.toString());
        } else {
            builder2 = builder16;
        }
        String str8 = "alpha";
        if (qSAnimator2.mQsPanelController.areThereTiles()) {
            int i26 = 0;
            int i27 = 0;
            for (QSTile qSTile : tiles) {
                int i28 = i26;
                QSTileView tileView = qSAnimator2.mQsPanelController.getTileView(qSTile);
                if (tileView == null) {
                    Log.e(str6, "tileView is null " + qSTile.getTileSpec());
                    i6 = i4;
                    i7 = i24;
                    str = str7;
                    i8 = i5;
                    builder3 = builder30;
                    builder4 = builder31;
                    builder5 = builder32;
                    builder6 = builder;
                    qSTileLayout3 = qSTileLayout;
                    i9 = i28;
                    i10 = i3;
                    iArr = iArr6;
                    builder7 = builder25;
                    str2 = str8;
                    builder8 = builder27;
                    builder9 = builder29;
                    i11 = i27;
                } else {
                    int i29 = i3;
                    View iconView = tileView.getIcon().getIconView();
                    int i30 = i4;
                    View view2 = qSAnimator2.mQs.getView();
                    int i31 = i24;
                    if (i27 < qSAnimator2.mQuickQSPanelController.getTileLayout().getNumVisibleTiles() && qSAnimator2.mAllowFancy) {
                        final QSTileView tileView2 = qSAnimator2.mQuickQSPanelController.getTileView(qSTile);
                        String tileSpec = qSTile.getTileSpec();
                        boolean isCellularTile = QSTileHost.isCellularTile(tileSpec);
                        boolean isWifiTile = QSTileHost.isWifiTile(tileSpec);
                        boolean isHotspot = QSTileHost.isHotspot(tileSpec);
                        boolean isBluetoothTile = QSTileHost.isBluetoothTile(tileSpec);
                        boolean z3 = isCellularTile || isBluetoothTile;
                        int i32 = i27;
                        int btTileIndex = isBluetoothTile ? qSAnimator2.mQsPanelController.getBtTileIndex(tileView) : -1;
                        i8 = i5;
                        if (isBluetoothTile && (i23 = qSAnimator2.mBtIndex) != 0 && btTileIndex != i23 && (tileView = qSAnimator2.mQsPanelController.getBtTile(i23)) == null) {
                            Log.d(str6, "updateAnimators: tileView is null skip ");
                        } else {
                            QSTileView qSTileView4 = tileView;
                            if (isCellularTile && qSAnimator2.mSignalIndex != QSTileHost.getCellExpectedIndexInVP()) {
                                Log.d(str6, "updateAnimators: skip non first page cell");
                            } else {
                                if (isWifiTile) {
                                    z = isCellularTile;
                                    if (qSAnimator2.mSignalIndex == QSTileHost.getWifiExpectedIndexInVP()) {
                                        qSAnimator2.mNonFirstCircleTileAnimating = true;
                                        if (!isWifiTile || isHotspot) {
                                            QuickQSPanelController quickQSPanelController = qSAnimator2.mQuickQSPanelController;
                                            tileView2 = quickQSPanelController.getTileView(quickQSPanelController.getTile(QSTileHost.getCellularSpec()));
                                        }
                                        qSAnimator2.getRelativePosition(iArr6, tileView2, view2);
                                        qSAnimator2.getRelativePosition(iArr7, qSTileView4, view2);
                                        int i33 = iArr7[1] - iArr6[1];
                                        int i34 = iArr7[0] - iArr6[0];
                                        int offsetTranslation = i33 - qSAnimator2.mQuickStatusBarHeader.getOffsetTranslation();
                                        int[] iArr8 = iArr7;
                                        int[] iArr9 = iArr6;
                                        builder17.addFloat(qSTileView4, str7, -offsetTranslation, 0.0f);
                                        builder18.addFloat(qSTileView4, "translationX", -i34, 0.0f);
                                        if (DEBUG) {
                                            Log.d(str6, "updateAnimators: xOffset " + i34 + " yOffset " + i33 + " offsetWithQSBHTranslation " + offsetTranslation);
                                        }
                                        if (tileView2 == null) {
                                            final IgnorableChildLinearLayout ignorableChildLinearLayout = (IgnorableChildLinearLayout) tileView2.mo812getLabelContainer();
                                            if (qSAnimator2.mNonFirstCircleTileHeightAnimator == null) {
                                                qSAnimator2.mNonFirstCircleTileHeightAnimator = new HeightExpansionAnimator(qSAnimator2, tileView2.getHeight(), qSTileView4.getHeight());
                                            }
                                            qSAnimator2.mNonFirstCircleTileHeightAnimator.addView(qSTileView4);
                                            builder25.addFloat(qSTileView4.getIcon(), str8, 0.0f, 1.0f);
                                            TouchAnimator.Builder builder33 = builder;
                                            builder33.addFloat(tileView2, str8, 1.0f, 0.0f).setListener(new TouchAnimator.Listener() { // from class: com.android.systemui.qs.QSAnimator.1
                                                @Override // com.android.systemui.qs.TouchAnimator.Listener
                                                public void onAnimationAtEnd() {
                                                }

                                                @Override // com.android.systemui.qs.TouchAnimator.Listener
                                                public void onAnimationAtStart() {
                                                }

                                                @Override // com.android.systemui.qs.TouchAnimator.Listener
                                                public void onAnimationStarted() {
                                                    tileView2.mo813getSecondaryLabel().setAlpha(0.0f);
                                                    if (ignorableChildLinearLayout.isSingleMode()) {
                                                        ignorableChildLinearLayout.setLabelSingleMode(false);
                                                    }
                                                }
                                            });
                                            TouchAnimator.Builder builder34 = builder32;
                                            builder34.addFloat(qSTileView4.mo811getLabel(), str8, 0.0f, 1.0f);
                                            builder34.addFloat(qSTileView4.mo813getSecondaryLabel(), str8, 0.0f, 1.0f);
                                            if (qSTileView4.getParent() != null && (qSTileView4.getParent() instanceof CirclePagedTileLayout)) {
                                                builder34.addFloat(((CirclePagedTileLayout) qSTileView4.getParent()).getPageIndicator(), str8, 0.0f, 1.0f);
                                            }
                                            builder7 = builder25;
                                            i9 = i28;
                                            qSTileLayout3 = qSTileLayout;
                                            TouchAnimator.Builder builder35 = builder29;
                                            i10 = i29;
                                            str2 = str8;
                                            builder5 = builder34;
                                            i6 = i30;
                                            builder3 = builder30;
                                            i7 = i31;
                                            QSTileView qSTileView5 = tileView2;
                                            str = str7;
                                            builder8 = builder27;
                                            builder4 = builder31;
                                            builder9 = builder35;
                                            iArr2 = iArr8;
                                            builder6 = builder33;
                                            iArr = iArr9;
                                            translateCircleLabelContainerNonFirst(tileView2.mo812getLabelContainer(), qSTileView4.mo812getLabelContainer(), view2, i34, i33, iArr9, builder35, builder22);
                                            translateCircleContentNonFirst(qSTileView5.getIcon(), qSTileView4.getIcon(), view2, i34, i33, iArr, builder8, builder24);
                                            qSAnimator2.mAllViews.add(qSTileView4.mo811getLabel());
                                            qSAnimator2.mAllViews.add(qSTileView4.mo813getSecondaryLabel());
                                            qSAnimator2.mAllViews.add(qSTileView4);
                                            qSAnimator2.mCircleLabelNonFirst.add((IgnorableChildLinearLayout) qSTileView4.mo812getLabelContainer());
                                            if (!qSAnimator2.mNonFirstQsOrQqsViews.contains(qSTileView5)) {
                                                qSAnimator2.mNonFirstQsOrQqsViews.add(qSTileView5);
                                            }
                                            i27 = i32 + 1;
                                            iArr7 = iArr2;
                                            builder29 = builder9;
                                            iArr6 = iArr;
                                            builder25 = builder7;
                                            str8 = str2;
                                            builder27 = builder8;
                                            i5 = i8;
                                            i26 = i9;
                                            qSTileLayout = qSTileLayout3;
                                            i3 = i10;
                                            builder32 = builder5;
                                            i4 = i6;
                                            builder30 = builder3;
                                            i24 = i7;
                                            builder31 = builder4;
                                            str7 = str;
                                            builder = builder6;
                                        } else {
                                            str = str7;
                                            builder3 = builder30;
                                            builder4 = builder31;
                                            builder5 = builder32;
                                            builder6 = builder;
                                            qSTileLayout3 = qSTileLayout;
                                            i9 = i28;
                                            i10 = i29;
                                            i6 = i30;
                                            i7 = i31;
                                            iArr = iArr9;
                                            builder7 = builder25;
                                            str2 = str8;
                                            builder8 = builder27;
                                            builder9 = builder29;
                                            iArr2 = iArr8;
                                            i11 = i32;
                                            i27 = i11;
                                            iArr7 = iArr2;
                                            builder29 = builder9;
                                            iArr6 = iArr;
                                            builder25 = builder7;
                                            str8 = str2;
                                            builder27 = builder8;
                                            i5 = i8;
                                            i26 = i9;
                                            qSTileLayout = qSTileLayout3;
                                            i3 = i10;
                                            builder32 = builder5;
                                            i4 = i6;
                                            builder30 = builder3;
                                            i24 = i7;
                                            builder31 = builder4;
                                            str7 = str;
                                            builder = builder6;
                                        }
                                    }
                                } else {
                                    z = isCellularTile;
                                }
                                if ((!isHotspot || qSAnimator2.mSignalIndex != QSTileHost.getHotspotExpectedIndexInVP()) && (!isBluetoothTile || qSAnimator2.mBtIndex == 0)) {
                                    str = str7;
                                    builder3 = builder30;
                                    builder4 = builder31;
                                    builder5 = builder32;
                                    builder6 = builder;
                                    qSTileLayout3 = qSTileLayout;
                                    i9 = i28;
                                    i10 = i29;
                                    i6 = i30;
                                    i7 = i31;
                                    iArr = iArr6;
                                    builder7 = builder25;
                                    str2 = str8;
                                    builder8 = builder27;
                                    builder9 = builder29;
                                    i11 = i32;
                                    iArr2 = iArr7;
                                    if (tileView2 == null) {
                                        Log.d(str6, "updateAnimators: update normally");
                                        i27 = i11;
                                        iArr7 = iArr2;
                                        builder29 = builder9;
                                        iArr6 = iArr;
                                        builder25 = builder7;
                                        str8 = str2;
                                        builder27 = builder8;
                                        i5 = i8;
                                        i26 = i9;
                                        qSTileLayout = qSTileLayout3;
                                        i3 = i10;
                                        builder32 = builder5;
                                        i4 = i6;
                                        builder30 = builder3;
                                        i24 = i7;
                                        builder31 = builder4;
                                        str7 = str;
                                        builder = builder6;
                                    } else {
                                        if (!qSAnimator2.mOnFirstPage && (z || isBluetoothTile)) {
                                            qSAnimator2.mNonFirstQsOrQqsViews.add(qSTileView4);
                                        }
                                        qSAnimator2.getRelativePosition(iArr, tileView2, view2);
                                        qSAnimator2.getRelativePosition(iArr2, qSTileView4, view2);
                                        int i35 = iArr2[1] - iArr[1];
                                        int i36 = iArr2[0] - iArr[0];
                                        int offsetTranslation2 = i35 - qSAnimator2.mQuickStatusBarHeader.getOffsetTranslation();
                                        builder17.addFloat(tileView2, str, 0.0f, offsetTranslation2);
                                        builder17.addFloat(qSTileView4, str, -offsetTranslation2, 0.0f);
                                        builder18.addFloat(tileView2, "translationX", 0.0f, i36);
                                        builder18.addFloat(qSTileView4, "translationX", -i36, 0.0f);
                                        if (DEBUG) {
                                            Log.d(str6, "updateAnimators: xOffset-1 " + i36 + " yOffset-1 " + i35 + " offsetWithQSBHTranslation-1 " + offsetTranslation2 + " count " + i11);
                                        }
                                        if (z3) {
                                            if (qSAnimator2.mQQSCircleTileHeightAnimator == null) {
                                                qSAnimator2.mQQSCircleTileHeightAnimator = new HeightExpansionAnimator(qSAnimator2, tileView2.getHeight(), qSTileView4.getHeight());
                                            }
                                            qSAnimator2.mQQSCircleTileHeightAnimator.addView(tileView2);
                                        } else {
                                            if (qSAnimator2.mQQSTileHeightAnimator == null) {
                                                qSAnimator2.mQQSTileHeightAnimator = new HeightExpansionAnimator(qSAnimator2, tileView2.getHeight(), qSTileView4.getHeight());
                                                i20 = tileView2.getHeight();
                                            } else {
                                                i20 = i9;
                                            }
                                            qSAnimator2.mQQSTileHeightAnimator.addView(tileView2);
                                            i9 = i20;
                                        }
                                        if (z3) {
                                            builder3.addFloat(tileView2.mo811getLabel(), str2, 1.0f, 0.0f);
                                            builder5.addFloat(tileView2.mo811getLabel(), str2, 0.0f, 1.0f);
                                            i21 = i36;
                                            builder5.addFloat(tileView2.mo813getSecondaryLabel(), str2, 0.0f, 1.0f);
                                            if (qSTileView4.getParent() != null && (qSTileView4.getParent() instanceof CirclePagedTileLayout)) {
                                                builder5.addFloat(((CirclePagedTileLayout) qSTileView4.getParent()).getPageIndicator(), str2, 0.0f, 1.0f);
                                            }
                                            str3 = str6;
                                            str4 = str2;
                                            builder3 = builder3;
                                            str5 = str;
                                            builder10 = builder5;
                                            qSTileView3 = tileView2;
                                            i22 = i35;
                                            view = view2;
                                            iArr5 = iArr;
                                            translateLabelContainer(tileView2.mo812getLabelContainer(), qSTileView4.mo812getLabelContainer(), view2, i21, i35, iArr, builder9, builder22);
                                            translateContent(qSTileView3.getIcon(), qSTileView4.getIcon(), view, i21, i22, iArr5, builder23, builder24);
                                            qSAnimator2.mAllViews.add(qSTileView3.mo811getLabel());
                                            qSAnimator2.mCircleLabel.add((IgnorableChildLinearLayout) qSTileView3.mo812getLabelContainer());
                                        } else {
                                            str5 = str;
                                            i21 = i36;
                                            qSTileView3 = tileView2;
                                            i22 = i35;
                                            view = view2;
                                            iArr5 = iArr;
                                            str3 = str6;
                                            str4 = str2;
                                            builder10 = builder5;
                                            translateContent(qSTileView3.getIcon(), qSTileView4.getIcon(), view, i36, i22, iArr5, builder18, builder17);
                                            translateContent(qSTileView3.mo812getLabelContainer(), qSTileView4.mo812getLabelContainer(), view, i36, i22, iArr5, builder18, builder17);
                                        }
                                        translateContent(qSTileView3.getSecondaryIcon(), qSTileView4.getSecondaryIcon(), view, i21, i22, iArr5, builder18, builder17);
                                        if (!z3) {
                                            builder15 = builder2;
                                            builder15.addFloat(qSTileView3.mo813getSecondaryLabel(), str4, 0.0f, 1.0f);
                                        } else {
                                            builder15 = builder2;
                                        }
                                        qSAnimator2.mQuickQsViews.add(qSTileView4);
                                        QSTileView qSTileView6 = qSTileView3;
                                        qSAnimator2.mAllViews.add(qSTileView6);
                                        qSAnimator2.mAllViews.add(qSTileView6.mo813getSecondaryLabel());
                                        qSAnimator = qSAnimator2;
                                        qSTileView2 = qSTileView4;
                                        iArr3 = iArr2;
                                        iArr4 = iArr5;
                                        i14 = i8;
                                        i15 = i10;
                                        i13 = i6;
                                        builder12 = builder4;
                                        builder14 = builder6;
                                        builder11 = builder15;
                                    }
                                }
                                qSAnimator2.mNonFirstCircleTileAnimating = true;
                                if (!isWifiTile) {
                                }
                                QuickQSPanelController quickQSPanelController2 = qSAnimator2.mQuickQSPanelController;
                                tileView2 = quickQSPanelController2.getTileView(quickQSPanelController2.getTile(QSTileHost.getCellularSpec()));
                                qSAnimator2.getRelativePosition(iArr6, tileView2, view2);
                                qSAnimator2.getRelativePosition(iArr7, qSTileView4, view2);
                                int i332 = iArr7[1] - iArr6[1];
                                int i342 = iArr7[0] - iArr6[0];
                                int offsetTranslation3 = i332 - qSAnimator2.mQuickStatusBarHeader.getOffsetTranslation();
                                int[] iArr82 = iArr7;
                                int[] iArr92 = iArr6;
                                builder17.addFloat(qSTileView4, str7, -offsetTranslation3, 0.0f);
                                builder18.addFloat(qSTileView4, "translationX", -i342, 0.0f);
                                if (DEBUG) {
                                }
                                if (tileView2 == null) {
                                }
                            }
                        }
                        str = str7;
                        builder3 = builder30;
                        builder4 = builder31;
                        builder5 = builder32;
                        builder6 = builder;
                        qSTileLayout3 = qSTileLayout;
                        i9 = i28;
                        i10 = i29;
                        i6 = i30;
                        i7 = i31;
                        iArr = iArr6;
                        builder7 = builder25;
                        str2 = str8;
                        builder8 = builder27;
                        builder9 = builder29;
                        i11 = i32;
                    } else {
                        int[] iArr10 = iArr7;
                        int[] iArr11 = iArr6;
                        int i37 = i5;
                        builder3 = builder30;
                        TouchAnimator.Builder builder36 = builder31;
                        builder10 = builder32;
                        TouchAnimator.Builder builder37 = builder;
                        qSTileLayout3 = qSTileLayout;
                        TouchAnimator.Builder builder38 = builder2;
                        i9 = i28;
                        i7 = i31;
                        str3 = str6;
                        builder7 = builder25;
                        str4 = str8;
                        builder8 = builder27;
                        builder9 = builder29;
                        i11 = i27;
                        str5 = str7;
                        if (qSAnimator2.mFullRows && qSAnimator2.isIconInAnimatedRow(i11)) {
                            builder38.addFloat(tileView, str5, -i7, 0.0f);
                            qSAnimator2.mAllViews.add(iconView);
                            qSTileView = tileView;
                            builder11 = builder38;
                            iArr3 = iArr10;
                            iArr4 = iArr11;
                            i14 = i37;
                            i15 = i29;
                            i13 = i30;
                        } else {
                            SideLabelTileLayout sideLabelTileLayout2 = (SideLabelTileLayout) qSAnimator2.mQuickQsPanel.getTileLayout();
                            qSAnimator2.getRelativePosition(iArr11, sideLabelTileLayout2, view2);
                            qSAnimator2.getRelativePosition(iArr10, tileView, view2);
                            int phantomTopPosition2 = iArr10[1] - (iArr11[1] + sideLabelTileLayout2.getPhantomTopPosition(i11));
                            i7 = i7;
                            if (qSAnimator2.mOnFirstPage || i11 < i30) {
                                builder17.addFloat(tileView, str5, -phantomTopPosition2, 0.0f);
                            }
                            if (qSAnimator2.mOtherTilesExpandAnimator == null) {
                                i12 = i9;
                                qSAnimator2.mOtherTilesExpandAnimator = new HeightExpansionAnimator(qSAnimator2, i12, tileView.getHeight());
                            } else {
                                i12 = i9;
                            }
                            qSAnimator2.mOtherTilesExpandAnimator.addView(tileView);
                            tileView.setClipChildren(true);
                            tileView.setClipToPadding(true);
                            i9 = i12;
                            builder38.addFloat(tileView.mo813getSecondaryLabel(), str4, 0.0f, 1.0f);
                            if (qSAnimator2.mOnFirstPage || i11 < i30) {
                                qSTileView = tileView;
                                i13 = i30;
                                iArr3 = iArr10;
                                iArr4 = iArr11;
                                builder11 = builder38;
                                i14 = i37;
                                i15 = i29;
                            } else {
                                i15 = i29;
                                if (i11 < i15) {
                                    int i38 = ((i11 - 2) % i37) + 2;
                                    QSTileView tile = sideLabelTileLayout2.getTile(i38);
                                    if (tile != null) {
                                        qSAnimator2.getRelativePosition(iArr11, tile, view2);
                                        i16 = iArr10[1];
                                        phantomTopPosition = iArr11[1];
                                    } else {
                                        i16 = iArr10[1];
                                        phantomTopPosition = iArr11[1] + sideLabelTileLayout2.getPhantomTopPosition(i38);
                                    }
                                    int i39 = i16 - phantomTopPosition;
                                    if (tile != null) {
                                        qSAnimator2.mNonFirstQsOrQqsViews.add(tile);
                                        builder37.addFloat(tile, str4, 1.0f, 0.0f);
                                        builder36.addFloat(tileView.getIcon(), str4, 0.0f, 1.0f);
                                        builder36.addFloat(tileView.mo812getLabelContainer(), str4, 0.0f, 1.0f);
                                        builder36.addFloat(tileView.getSecondaryIcon(), str4, 0.0f, 1.0f);
                                        builder12 = builder36;
                                        i17 = i38;
                                        i13 = i30;
                                        iArr3 = iArr10;
                                        iArr4 = iArr11;
                                        sideLabelTileLayout = sideLabelTileLayout2;
                                        builder11 = builder38;
                                        translateContent(tile.getIcon(), tileView.getIcon(), view2, 0, i39, iArr4, builder18, builder17);
                                        translateContentForY(tile.mo812getLabelContainer(), tileView.mo812getLabelContainer(), view2, i39, iArr4, builder17);
                                        translateContent(tile.getSecondaryIcon(), tileView.getSecondaryIcon(), view2, 0, i39, iArr4, builder18, builder17);
                                        qSTileView = tileView;
                                        i18 = 2;
                                    } else {
                                        i17 = i38;
                                        i13 = i30;
                                        iArr3 = iArr10;
                                        iArr4 = iArr11;
                                        sideLabelTileLayout = sideLabelTileLayout2;
                                        builder11 = builder38;
                                        builder12 = builder36;
                                        i18 = 2;
                                        qSTileView = tileView;
                                        builder11.addFloat(qSTileView, str4, 0.0f, 1.0f);
                                    }
                                    float[] fArr = new float[i18];
                                    fArr[0] = -i39;
                                    fArr[1] = 0.0f;
                                    builder17.addFloat(qSTileView, str5, fArr);
                                    i14 = i37;
                                    if (i11 == i15 - 1 && (i19 = i17) < i14) {
                                        int i40 = i19 + 1;
                                        while (i40 < i14) {
                                            SideLabelTileLayout sideLabelTileLayout3 = sideLabelTileLayout;
                                            QSTileView tile2 = sideLabelTileLayout3.getTile(i40);
                                            if (tile2 != null) {
                                                tile2.setAlpha(0.0f);
                                                builder13 = builder37;
                                                builder13.addFloat(tile2, str4, 1.0f, 0.0f);
                                            } else {
                                                builder13 = builder37;
                                            }
                                            i40++;
                                            sideLabelTileLayout = sideLabelTileLayout3;
                                            builder37 = builder13;
                                        }
                                    }
                                    builder14 = builder37;
                                    qSTileView2 = qSTileView;
                                    qSAnimator = this;
                                } else {
                                    qSTileView = tileView;
                                    i13 = i30;
                                    iArr3 = iArr10;
                                    iArr4 = iArr11;
                                    builder11 = builder38;
                                    i14 = i37;
                                }
                            }
                        }
                        builder12 = builder36;
                        builder14 = builder37;
                        qSTileView2 = qSTileView;
                        qSAnimator = this;
                    }
                    qSAnimator.mAllViews.add(qSTileView2);
                    int i41 = i11 + 1;
                    builder2 = builder11;
                    i3 = i15;
                    str7 = str5;
                    builder29 = builder9;
                    i4 = i13;
                    builder25 = builder7;
                    iArr7 = iArr3;
                    builder27 = builder8;
                    iArr6 = iArr4;
                    qSTileLayout = qSTileLayout3;
                    builder32 = builder10;
                    builder31 = builder12;
                    i5 = i14;
                    qSAnimator2 = qSAnimator;
                    builder = builder14;
                    str8 = str4;
                    str6 = str3;
                    builder30 = builder3;
                    i24 = i7;
                    i27 = i41;
                    i26 = i9;
                }
                iArr2 = iArr7;
                i27 = i11;
                iArr7 = iArr2;
                builder29 = builder9;
                iArr6 = iArr;
                builder25 = builder7;
                str8 = str2;
                builder27 = builder8;
                i5 = i8;
                i26 = i9;
                qSTileLayout = qSTileLayout3;
                i3 = i10;
                builder32 = builder5;
                i4 = i6;
                builder30 = builder3;
                i24 = i7;
                builder31 = builder4;
                str7 = str;
                builder = builder6;
            }
        }
        QSAnimator qSAnimator3 = qSAnimator2;
        String str9 = str8;
        TouchAnimator.Builder builder39 = builder27;
        TouchAnimator.Builder builder40 = builder29;
        TouchAnimator.Builder builder41 = builder30;
        TouchAnimator.Builder builder42 = builder31;
        TouchAnimator.Builder builder43 = builder32;
        TouchAnimator.Builder builder44 = builder;
        QSPanel.QSTileLayout qSTileLayout4 = qSTileLayout;
        TouchAnimator.Builder builder45 = builder2;
        String str10 = str7;
        TouchAnimator.Builder builder46 = builder25;
        if (qSAnimator3.mAllowFancy) {
            View brightnessView = qSAnimator3.mQsPanelController.getBrightnessView();
            if (brightnessView != null) {
                float f = 0.0f;
                builder45.addFloat(brightnessView, str10, -brightnessView.getMeasuredHeight(), 0.0f);
                if (!qSAnimator3.mOnFirstPage) {
                    f = 0.3f;
                }
                qSAnimator3.mBrightnessAnimator = new TouchAnimator.Builder().addFloat(brightnessView, str9, 0.0f, 1.0f).addFloat(brightnessView, "sliderScaleY", 0.3f, 1.0f).setInterpolator(Interpolators.ALPHA_IN).setStartDelay(f).build();
                qSAnimator3.mAllViews.add(brightnessView);
            } else {
                qSAnimator3.mBrightnessAnimator = null;
            }
            qSAnimator3.mFirstPageAnimator = builder45.setListener(qSAnimator3).build();
            qSTileLayout2 = qSTileLayout4;
            qSAnimator3.mFirstPageDelayedAnimator = new TouchAnimator.Builder().addFloat(qSTileLayout2, str9, 0.0f, 1.0f).build();
            TouchAnimator.Builder startDelay = new TouchAnimator.Builder().setStartDelay(0.86f);
            startDelay.addFloat(qSAnimator3.mSecurityFooter.getView(), str9, 0.0f, 1.0f);
            if (!qSAnimator3.mQsPanelController.shouldUseHorizontalLayout() || (uniqueObjectHostView = qSAnimator3.mQsPanelController.mMediaHost.hostView) == null) {
                qSAnimator3.mQsPanelController.mMediaHost.hostView.setAlpha(1.0f);
            } else {
                startDelay.addFloat(uniqueObjectHostView, str9, 0.0f, 1.0f);
            }
            qSAnimator3.mAllPagesDelayedAnimator = startDelay.build();
            qSAnimator3.mAllViews.add(qSAnimator3.mSecurityFooter.getView());
            builder17.setInterpolator(qSAnimator3.mQSExpansionPathInterpolator.getYInterpolator());
            builder18.setInterpolator(qSAnimator3.mQSExpansionPathInterpolator.getXInterpolator());
            qSAnimator3.mTranslationYAnimator = builder17.build();
            qSAnimator3.mTranslationXAnimator = builder18.build();
            HeightExpansionAnimator heightExpansionAnimator = qSAnimator3.mQQSTileHeightAnimator;
            if (heightExpansionAnimator != null) {
                heightExpansionAnimator.setInterpolator(qSAnimator3.mQSExpansionPathInterpolator.getYInterpolator());
            }
            HeightExpansionAnimator heightExpansionAnimator2 = qSAnimator3.mOtherTilesExpandAnimator;
            if (heightExpansionAnimator2 != null) {
                heightExpansionAnimator2.setInterpolator(qSAnimator3.mQSExpansionPathInterpolator.getYInterpolator());
            }
            qSAnimator3.mLabelInOutAnimator = builder41.setEndDelay(0.667f).build();
            qSAnimator3.mLabelSecondLabelInOutAnimator = builder43.setStartDelay(0.667f).build();
            qSAnimator3.mLabelContainerXAnimator = builder40.setStartDelay(0.667f).build();
            qSAnimator3.mLabelContainerYAnimator = builder22.setStartDelay(0.667f).build();
            qSAnimator3.mIconXAnimator = builder23.build();
            qSAnimator3.mIconYAnimator = builder24.build();
            qSAnimator3.mIconInOutNonFirstAnimator = builder46.setStartDelay(0.3f).setEndDelay(0.4f).build();
            qSAnimator3.mQQSNonFirstAnimator = builder44.setEndDelay(0.7f).build();
            qSAnimator3.mIconXNonFirstAnimator = builder39.setStartDelay(0.3f).build();
            qSAnimator3.mNtNonFirstPageAnimator = builder42.setStartDelay(0.3f).build();
        } else {
            qSTileLayout2 = qSTileLayout4;
        }
        qSAnimator3.mNonfirstPageAnimator = new TouchAnimator.Builder().addFloat(qSAnimator3.mQuickQsPanel, str9, 1.0f, 0.0f).setListener(qSAnimator3.mNonFirstPageListener).setEndDelay(0.5f).build();
        qSAnimator3.mNonfirstPageDelayedAnimator = new TouchAnimator.Builder().setStartDelay(0.14f).addFloat(qSTileLayout2, str9, 0.0f, 1.0f).build();
    }

    private boolean isIconInAnimatedRow(int i) {
        PagedTileLayout pagedTileLayout = this.mPagedLayout;
        if (pagedTileLayout == null) {
            return false;
        }
        int columnCount = pagedTileLayout.getColumnCount();
        return i < (((this.mNumQuickTiles + columnCount) - 1) / columnCount) * columnCount;
    }

    private void getRelativePosition(int[] iArr, View view, View view2) {
        iArr[0] = (view.getWidth() / 2) + 0;
        iArr[1] = 0;
        getRelativePositionInt(iArr, view, view2);
    }

    private void getRelativePositionInt(int[] iArr, View view, View view2) {
        if (view == view2 || view == null) {
            return;
        }
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

    private boolean isAPage(View view) {
        return view.getClass().equals(SideLabelTileLayout.class);
    }

    public void setPosition(float f) {
        if (this.mNeedsAnimatorUpdate) {
            updateAnimators();
        }
        if (this.mFirstPageAnimator == null) {
            return;
        }
        if (this.mOnKeyguard) {
            f = this.mShowCollapsedOnKeyguard ? 0.0f : 1.0f;
        }
        this.mLastPosition = f;
        if (f == 0.0f) {
            this.mToExpand = true;
        } else if (f == 1.0d) {
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
            if (f >= 0.667f) {
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
        if (touchAnimator7 != null && f < 0.3d) {
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
        if (!this.mAllowFancy) {
            return;
        }
        this.mAllPagesDelayedAnimator.setPosition(f);
        TouchAnimator touchAnimator11 = this.mBrightnessAnimator;
        if (touchAnimator11 == null) {
            return;
        }
        touchAnimator11.setPosition(f);
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
    public void onAnimationAtStart() {
        this.mQuickQsPanel.setVisibility(0);
        if (DEBUG) {
            Log.d("QSAnimator", "onAnimationAtStart: ");
        }
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
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
        if (DEBUG) {
            Log.d("QSAnimator", "onAnimationAtEnd: mNonFirstQsOrQqsViews " + this.mNonFirstQsOrQqsViews.size());
        }
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
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
        if (DEBUG) {
            Log.d("QSAnimator", "onAnimationStarted: mNonFirstQsOrQqsViews " + this.mNonFirstQsOrQqsViews.size());
        }
    }

    private void clearAnimationState() {
        if (DEBUG) {
            Log.d("QSAnimator", "clearAnimationState: ");
        }
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

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.mExecutor.execute(this.mUpdateAnimators);
    }

    @Override // com.android.systemui.qs.QSHost.Callback
    public void onTilesChanged() {
        this.mExecutor.execute(this.mUpdateAnimators);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateAnimators();
        setCurrentPosition();
    }

    public void setTranslateWhileExpanding(boolean z) {
        this.mTranslateWhileExpanding = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class HeightExpansionAnimator {
        private final ValueAnimator mAnimator;
        private final TouchAnimator.Listener mListener;
        private final ValueAnimator.AnimatorUpdateListener mUpdateListener;
        private final List<View> mViews = new ArrayList();

        HeightExpansionAnimator(TouchAnimator.Listener listener, int i, int i2) {
            ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.qs.QSAnimator.HeightExpansionAnimator.1
                float mLastT = -1.0f;

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    int size = HeightExpansionAnimator.this.mViews.size();
                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    for (int i3 = 0; i3 < size; i3++) {
                        View view = (View) HeightExpansionAnimator.this.mViews.get(i3);
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
            this.mUpdateListener = animatorUpdateListener;
            this.mListener = listener;
            ValueAnimator ofInt = ValueAnimator.ofInt(i, i2);
            this.mAnimator = ofInt;
            ofInt.setRepeatCount(-1);
            ofInt.setRepeatMode(2);
            ofInt.addUpdateListener(animatorUpdateListener);
        }

        void addView(View view) {
            this.mViews.add(view);
        }

        void setInterpolator(TimeInterpolator timeInterpolator) {
            this.mAnimator.setInterpolator(timeInterpolator);
        }

        void setPosition(float f) {
            this.mAnimator.setCurrentFraction(f);
        }

        void resetViewsHeights() {
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
        int i3;
        getRelativePosition(iArr, view, view3);
        int i4 = iArr[0];
        int i5 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i6 = iArr[0];
        int i7 = iArr[1];
        float f = (i6 - i4) - i;
        builder.addFloat(view, "translationX", f, f);
        builder.addFloat(view2, "translationX", -i3, 0.0f);
        float f2 = (i7 - i5) - i2;
        builder2.addFloat(view, "translationY", 0.667f * f2, f2);
        builder2.addFloat(view2, "translationY", f2 * (-0.667f), 0.0f);
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
        builder2.addFloat(view2, "translationY", i6 * (-0.667f), 0.0f);
        this.mAllViews.add(view2);
        this.mNonFirstCircleTileTranslationX = -i5;
        this.mNonFirstCircleTileTranslationY = -i6;
    }

    private void translateCircleContentNonFirst(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        builder.addFloat(view2, "translationX", -((i5 - i3) - i), 0.0f);
        builder2.addFloat(view2, "translationY", -((i6 - i4) - i2), 0.0f);
        this.mAllViews.add(view2);
    }

    private void translateContentForY(View view, View view2, View view3, int i, int[] iArr, TouchAnimator.Builder builder) {
        getRelativePosition(iArr, view, view3);
        int i2 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i3 = (iArr[1] - i2) - i;
        builder.addFloat(view, "translationY", 0.0f, i3);
        builder.addFloat(view2, "translationY", -i3, 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }
}
