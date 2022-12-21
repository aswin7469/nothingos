package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.ExtensionControllerImpl;

/* renamed from: com.android.systemui.statusbar.policy.ExtensionControllerImpl$ExtensionImpl$UiModeItem$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3152xc839f287 implements Runnable {
    public final /* synthetic */ ExtensionControllerImpl.ExtensionImpl f$0;

    public /* synthetic */ C3152xc839f287(ExtensionControllerImpl.ExtensionImpl extensionImpl) {
        this.f$0 = extensionImpl;
    }

    public final void run() {
        this.f$0.notifyChanged();
    }
}
