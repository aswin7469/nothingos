package com.android.systemui.statusbar;

import com.android.systemui.statusbar.SysuiStatusBarStateController;
import java.util.function.ToIntFunction;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBarStateControllerImpl$$ExternalSyntheticLambda1 implements ToIntFunction {
    public static final /* synthetic */ StatusBarStateControllerImpl$$ExternalSyntheticLambda1 INSTANCE = new StatusBarStateControllerImpl$$ExternalSyntheticLambda1();

    private /* synthetic */ StatusBarStateControllerImpl$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int i;
        i = ((SysuiStatusBarStateController.RankedListener) obj).mRank;
        return i;
    }
}
