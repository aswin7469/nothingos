package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.DreamWeatherComplication;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.List;

/* renamed from: com.android.systemui.dreams.complication.DreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2089xbf1b0f76 implements BcSmartspaceDataPlugin.SmartspaceTargetListener {
    public final /* synthetic */ DreamWeatherComplication.DreamWeatherViewController f$0;

    public /* synthetic */ C2089xbf1b0f76(DreamWeatherComplication.DreamWeatherViewController dreamWeatherViewController) {
        this.f$0 = dreamWeatherViewController;
    }

    public final void onSmartspaceTargetsUpdated(List list) {
        this.f$0.mo32612x5a753507(list);
    }
}
