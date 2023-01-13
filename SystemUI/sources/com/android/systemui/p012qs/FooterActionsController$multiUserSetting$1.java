package com.android.systemui.p012qs;

import android.os.Handler;
import com.android.systemui.util.settings.GlobalSettings;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0014¨\u0006\b"}, mo65043d2 = {"com/android/systemui/qs/FooterActionsController$multiUserSetting$1", "Lcom/android/systemui/qs/SettingObserver;", "handleValueChanged", "", "value", "", "observedChange", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.FooterActionsController$multiUserSetting$1 */
/* compiled from: FooterActionsController.kt */
public final class FooterActionsController$multiUserSetting$1 extends SettingObserver {
    final /* synthetic */ FooterActionsController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FooterActionsController$multiUserSetting$1(FooterActionsController footerActionsController, GlobalSettings globalSettings, Handler handler, int i) {
        super(globalSettings, handler, "user_switcher_enabled", i);
        this.this$0 = footerActionsController;
    }

    /* access modifiers changed from: protected */
    public void handleValueChanged(int i, boolean z) {
        if (z) {
            this.this$0.updateView();
        }
    }
}
