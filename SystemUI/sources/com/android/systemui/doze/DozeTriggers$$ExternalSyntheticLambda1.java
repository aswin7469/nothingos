package com.android.systemui.doze;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.doze.DozeTriggers;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ UiEventLogger f$0;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda1(UiEventLogger uiEventLogger) {
        this.f$0 = uiEventLogger;
    }

    public final void accept(Object obj) {
        this.f$0.log((DozeTriggers.DozingUpdateUiEvent) obj);
    }
}
