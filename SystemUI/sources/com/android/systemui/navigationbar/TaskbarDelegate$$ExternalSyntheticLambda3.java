package com.android.systemui.navigationbar;

import android.graphics.Rect;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TaskbarDelegate$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ EdgeBackGestureHandler f$0;

    public /* synthetic */ TaskbarDelegate$$ExternalSyntheticLambda3(EdgeBackGestureHandler edgeBackGestureHandler) {
        this.f$0 = edgeBackGestureHandler;
    }

    public final void accept(Object obj) {
        this.f$0.setPipStashExclusionBounds((Rect) obj);
    }
}
