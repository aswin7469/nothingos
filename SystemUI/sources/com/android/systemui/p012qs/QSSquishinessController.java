package com.android.systemui.p012qs;

import com.android.systemui.p012qs.dagger.QSScope;
import com.nothing.systemui.p024qs.NTQSAnimator;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@QSScope
@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u0010\u001a\u00020\u0011H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R$\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/systemui/qs/QSSquishinessController;", "", "qsAnimator", "Lcom/nothing/systemui/qs/NTQSAnimator;", "qsPanelController", "Lcom/android/systemui/qs/QSPanelController;", "quickQSPanelController", "Lcom/android/systemui/qs/QuickQSPanelController;", "(Lcom/nothing/systemui/qs/NTQSAnimator;Lcom/android/systemui/qs/QSPanelController;Lcom/android/systemui/qs/QuickQSPanelController;)V", "value", "", "squishiness", "getSquishiness", "()F", "setSquishiness", "(F)V", "updateSquishiness", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.QSSquishinessController */
/* compiled from: QSSquishinessController.kt */
public final class QSSquishinessController {
    private final NTQSAnimator qsAnimator;
    private final QSPanelController qsPanelController;
    private final QuickQSPanelController quickQSPanelController;
    private float squishiness = 1.0f;

    @Inject
    public QSSquishinessController(NTQSAnimator nTQSAnimator, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController2) {
        Intrinsics.checkNotNullParameter(nTQSAnimator, "qsAnimator");
        Intrinsics.checkNotNullParameter(qSPanelController, "qsPanelController");
        Intrinsics.checkNotNullParameter(quickQSPanelController2, "quickQSPanelController");
        this.qsAnimator = nTQSAnimator;
        this.qsPanelController = qSPanelController;
        this.quickQSPanelController = quickQSPanelController2;
    }

    public final float getSquishiness() {
        return this.squishiness;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
        if ((r6 == 1.0f) == false) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0032, code lost:
        if (r2 != false) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0034, code lost:
        r5.qsAnimator.requestAnimatorUpdate();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setSquishiness(float r6) {
        /*
            r5 = this;
            float r0 = r5.squishiness
            int r1 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x000a
            r1 = r2
            goto L_0x000b
        L_0x000a:
            r1 = r3
        L_0x000b:
            if (r1 == 0) goto L_0x000e
            return
        L_0x000e:
            r1 = 1065353216(0x3f800000, float:1.0)
            int r4 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r4 != 0) goto L_0x0016
            r4 = r2
            goto L_0x0017
        L_0x0016:
            r4 = r3
        L_0x0017:
            if (r4 != 0) goto L_0x0022
            int r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r1 != 0) goto L_0x001f
            r1 = r2
            goto L_0x0020
        L_0x001f:
            r1 = r3
        L_0x0020:
            if (r1 != 0) goto L_0x0034
        L_0x0022:
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L_0x0029
            r0 = r2
            goto L_0x002a
        L_0x0029:
            r0 = r3
        L_0x002a:
            if (r0 != 0) goto L_0x0039
            int r0 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r0 != 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r2 = r3
        L_0x0032:
            if (r2 == 0) goto L_0x0039
        L_0x0034:
            com.nothing.systemui.qs.NTQSAnimator r0 = r5.qsAnimator
            r0.requestAnimatorUpdate()
        L_0x0039:
            r5.squishiness = r6
            r5.updateSquishiness()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.QSSquishinessController.setSquishiness(float):void");
    }

    private final void updateSquishiness() {
        this.qsPanelController.setSquishinessFraction(this.squishiness);
        this.quickQSPanelController.setSquishinessFraction(this.squishiness);
    }
}
