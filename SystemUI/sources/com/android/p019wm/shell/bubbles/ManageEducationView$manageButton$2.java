package com.android.p019wm.shell.bubbles;

import android.widget.Button;
import com.android.p019wm.shell.C3353R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "Landroid/widget/Button;", "kotlin.jvm.PlatformType", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.ManageEducationView$manageButton$2 */
/* compiled from: ManageEducationView.kt */
final class ManageEducationView$manageButton$2 extends Lambda implements Function0<Button> {
    final /* synthetic */ ManageEducationView this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ManageEducationView$manageButton$2(ManageEducationView manageEducationView) {
        super(0);
        this.this$0 = manageEducationView;
    }

    public final Button invoke() {
        return (Button) this.this$0.findViewById(C3353R.C3356id.manage_button);
    }
}
