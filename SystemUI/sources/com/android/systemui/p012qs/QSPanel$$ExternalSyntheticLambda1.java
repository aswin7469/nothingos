package com.android.systemui.p012qs;

import android.content.res.Configuration;
import com.android.systemui.p012qs.QSPanel;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.qs.QSPanel$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSPanel$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ Configuration f$0;

    public /* synthetic */ QSPanel$$ExternalSyntheticLambda1(Configuration configuration) {
        this.f$0 = configuration;
    }

    public final void accept(Object obj) {
        ((QSPanel.OnConfigurationChangedListener) obj).onConfigurationChange(this.f$0);
    }
}
