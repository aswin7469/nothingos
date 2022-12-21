package com.nothing.systemui.p024qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.QSFragment;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.p012qs.QuickQSPanelController;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 :2\u00020\u0001:\u0001:B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J&\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020 2\u0006\u0010&\u001a\u00020'J(\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u00152\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010+\u001a\u0004\u0018\u00010\u0014J\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010,\u001a\u00020)2\u0006\u0010-\u001a\u00020\u0006J\u000e\u0010.\u001a\u00020)2\u0006\u0010-\u001a\u00020\u0006J\u000e\u0010/\u001a\u00020)2\u0006\u00100\u001a\u000201J\u0018\u00102\u001a\u00020)2\u0006\u00103\u001a\u00020\u00102\b\u00104\u001a\u0004\u0018\u000105J\u0016\u00106\u001a\u00020)2\u0006\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020\u0004J\b\u00109\u001a\u00020)H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R&\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R*\u0010\u0016\u001a\u0004\u0018\u00010\u00152\b\u0010\u0005\u001a\u0004\u0018\u00010\u00158F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u000e¢\u0006\u0002\n\u0000¨\u0006;"}, mo64987d2 = {"Lcom/nothing/systemui/qs/QSFragmentEx;", "", "()V", "bt", "", "value", "", "btPageIndex", "getBtPageIndex", "()I", "setBtPageIndex", "(I)V", "expandFraction", "hostEx", "Lcom/nothing/systemui/qs/QSTileHostEx;", "isSignalPageChanged", "", "qQSPanelController", "Lcom/android/systemui/qs/QuickQSPanelController;", "qSAnimator", "Lcom/nothing/systemui/qs/NTQSAnimator;", "Lcom/android/systemui/qs/QSFragment;", "qSFragment", "getQSFragment", "()Lcom/android/systemui/qs/QSFragment;", "setQSFragment", "(Lcom/android/systemui/qs/QSFragment;)V", "qSPanelController", "Lcom/android/systemui/qs/QSPanelController;", "quickTileRunnable", "Ljava/lang/Runnable;", "createQSInflater", "Landroid/view/LayoutInflater;", "res", "Landroid/content/res/Resources;", "oldDm", "Landroid/util/DisplayMetrics;", "inflater", "context", "Landroid/content/Context;", "init", "", "qS", "animator", "onBtPageChanged", "index", "onSignalPageChanged", "reInflateBrightnessMirror", "brightnessMirrorController", "Lcom/android/systemui/statusbar/policy/BrightnessMirrorController;", "resetOverScrollAmountIfNeeded", "isQSVisible", "qs", "Landroid/view/View;", "setExpansion", "expansion", "proposedTranslation", "setTiles", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.QSFragmentEx */
/* compiled from: QSFragmentEx.kt */
public final class QSFragmentEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int NT_QS_DENSITY_DPI_STANDARD = 420;
    private static final int NT_QS_DENSITY_DPI_THRESHOLD = 430;
    private static final float NT_QS_DENSITY_THRESHOLD = 2.688f;
    private static final float NT_QS_FONT_SCALE_THRESHOLD = 1.15f;
    private static final float NT_QS_SCALED_DENSITY_THRESHOLD = 3.375f;
    private static final String TAG = "QSFragmentEx";

    /* renamed from: bt */
    private float f504bt;
    private int btPageIndex;
    private float expandFraction;
    private final QSTileHostEx hostEx;
    private boolean isSignalPageChanged;
    private QuickQSPanelController qQSPanelController;
    private NTQSAnimator qSAnimator;
    private QSFragment qSFragment;
    private QSPanelController qSPanelController;
    private Runnable quickTileRunnable;

    @Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bXT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo64987d2 = {"Lcom/nothing/systemui/qs/QSFragmentEx$Companion;", "", "()V", "NT_QS_DENSITY_DPI_STANDARD", "", "NT_QS_DENSITY_DPI_THRESHOLD", "NT_QS_DENSITY_THRESHOLD", "", "NT_QS_FONT_SCALE_THRESHOLD", "NT_QS_SCALED_DENSITY_THRESHOLD", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.QSFragmentEx$Companion */
    /* compiled from: QSFragmentEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Inject
    public QSFragmentEx() {
        Object obj = NTDependencyEx.get(QSTileHostEx.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(QSTileHostEx::class.java)");
        this.hostEx = (QSTileHostEx) obj;
    }

    public final int getBtPageIndex() {
        return this.btPageIndex;
    }

    public final void setBtPageIndex(int i) {
        this.btPageIndex = i;
    }

    public final QSFragment getQSFragment() {
        return this.qSFragment;
    }

    public final void setQSFragment(QSFragment qSFragment2) {
        this.qSFragment = qSFragment2;
    }

    public final void init(QSFragment qSFragment2, QuickQSPanelController quickQSPanelController, QSPanelController qSPanelController2, NTQSAnimator nTQSAnimator) {
        Intrinsics.checkNotNullParameter(qSFragment2, "qS");
        Intrinsics.checkNotNullParameter(quickQSPanelController, "qQSPanelController");
        Intrinsics.checkNotNullParameter(qSPanelController2, "qSPanelController");
        setQSFragment(qSFragment2);
        this.qQSPanelController = quickQSPanelController;
        this.qSPanelController = qSPanelController2;
        this.qSAnimator = nTQSAnimator;
    }

    public final boolean isSignalPageChanged() {
        return this.isSignalPageChanged;
    }

    public final void onSignalPageChanged(int i) {
        this.hostEx.onSignalPageChanged(i);
        NTQSAnimator nTQSAnimator = this.qSAnimator;
        if (nTQSAnimator != null) {
            nTQSAnimator.onSignalPageChanged(i);
        }
        if (!this.hostEx.isPanelFullyCollapsed()) {
            if (!(this.expandFraction == 1.0f)) {
                QSFragmentEx$$ExternalSyntheticLambda0 qSFragmentEx$$ExternalSyntheticLambda0 = new QSFragmentEx$$ExternalSyntheticLambda0(this);
                this.quickTileRunnable = qSFragmentEx$$ExternalSyntheticLambda0;
                this.hostEx.addPostFullyCollapseAction(qSFragmentEx$$ExternalSyntheticLambda0);
                return;
            }
        }
        setTiles();
    }

    /* access modifiers changed from: private */
    /* renamed from: onSignalPageChanged$lambda-0  reason: not valid java name */
    public static final void m3508onSignalPageChanged$lambda0(QSFragmentEx qSFragmentEx) {
        Intrinsics.checkNotNullParameter(qSFragmentEx, "this$0");
        qSFragmentEx.setTiles();
    }

    public final void onBtPageChanged(int i) {
        setBtPageIndex(i);
        NTQSAnimator nTQSAnimator = this.qSAnimator;
        if (nTQSAnimator != null) {
            nTQSAnimator.onBtPageChanged(i);
        }
    }

    public final void resetOverScrollAmountIfNeeded(boolean z, View view) {
        if (!z && view != null) {
            if (!(view.getTranslationY() == 0.0f)) {
                NTLogUtil.m1680d(TAG, "resetOverScrollAmount: " + view.getTranslationY());
                QSFragment qSFragment2 = getQSFragment();
                Intrinsics.checkNotNull(qSFragment2);
                qSFragment2.setOverScrollAmount(0);
            }
        }
    }

    public final void setExpansion(float f, float f2) {
        this.expandFraction = f;
        QSPanelController qSPanelController2 = this.qSPanelController;
        Intrinsics.checkNotNull(qSPanelController2);
        qSPanelController2.setExpansion(f, f2);
        Runnable runnable = this.quickTileRunnable;
        if (runnable != null) {
            if (this.expandFraction == 1.0f) {
                Intrinsics.checkNotNull(runnable);
                runnable.run();
                this.quickTileRunnable = null;
            }
        }
    }

    public final LayoutInflater createQSInflater(Resources resources, DisplayMetrics displayMetrics, LayoutInflater layoutInflater, Context context) {
        Intrinsics.checkNotNullParameter(resources, "res");
        Intrinsics.checkNotNullParameter(displayMetrics, "oldDm");
        Intrinsics.checkNotNullParameter(layoutInflater, "inflater");
        Intrinsics.checkNotNullParameter(context, "context");
        if (displayMetrics.densityDpi > NT_QS_DENSITY_DPI_STANDARD || displayMetrics.scaledDensity > NT_QS_SCALED_DENSITY_THRESHOLD) {
            Configuration configuration = resources.getConfiguration();
            Configuration configuration2 = new Configuration();
            if (displayMetrics.densityDpi > NT_QS_DENSITY_DPI_STANDARD) {
                configuration2.smallestScreenWidthDp = (int) (((float) Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels)) / NT_QS_DENSITY_THRESHOLD);
                configuration2.screenWidthDp = (int) (((float) displayMetrics.widthPixels) / NT_QS_DENSITY_THRESHOLD);
                configuration2.screenHeightDp = (int) (((float) displayMetrics.heightPixels) / NT_QS_DENSITY_THRESHOLD);
                configuration2.densityDpi = NT_QS_DENSITY_DPI_THRESHOLD;
                if (!(configuration.fontScale == 1.0f)) {
                    float f = configuration.fontScale;
                    float f2 = NT_QS_FONT_SCALE_THRESHOLD;
                    if (f < NT_QS_FONT_SCALE_THRESHOLD) {
                        f2 = configuration.fontScale;
                    }
                    configuration2.fontScale = f2;
                }
            }
            Context createConfigurationContext = context.createConfigurationContext(configuration2);
            createConfigurationContext.setTheme(C1893R.style.Theme_SystemUI_QuickSettings);
            LayoutInflater cloneInContext = layoutInflater.cloneInContext(createConfigurationContext);
            Intrinsics.checkNotNullExpressionValue(cloneInContext, "inflater.cloneInContext(qsContext)");
            return cloneInContext;
        }
        LayoutInflater cloneInContext2 = layoutInflater.cloneInContext(new ContextThemeWrapper(context, C1893R.style.Theme_SystemUI_QuickSettings));
        Intrinsics.checkNotNullExpressionValue(cloneInContext2, "inflater.cloneInContext(qsContext)");
        return cloneInContext2;
    }

    public final void reInflateBrightnessMirror(BrightnessMirrorController brightnessMirrorController) {
        Intrinsics.checkNotNullParameter(brightnessMirrorController, "brightnessMirrorController");
        brightnessMirrorController.reinflate();
        brightnessMirrorController.updateResources();
    }

    private final void setTiles() {
        this.isSignalPageChanged = true;
        QuickQSPanelController quickQSPanelController = this.qQSPanelController;
        if (quickQSPanelController != null) {
            quickQSPanelController.setTiles();
        }
        this.isSignalPageChanged = false;
    }
}
