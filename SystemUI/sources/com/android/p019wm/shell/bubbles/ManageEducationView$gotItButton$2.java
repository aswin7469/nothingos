package com.android.p019wm.shell.bubbles;

import android.widget.Button;
import com.android.p019wm.shell.C3343R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n¢\u0006\u0002\b\u0003"}, mo64987d2 = {"<anonymous>", "Landroid/widget/Button;", "kotlin.jvm.PlatformType", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.bubbles.ManageEducationView$gotItButton$2 */
/* compiled from: ManageEducationView.kt */
final class ManageEducationView$gotItButton$2 extends Lambda implements Function0<Button> {
    final /* synthetic */ ManageEducationView this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ManageEducationView$gotItButton$2(ManageEducationView manageEducationView) {
        super(0);
        this.this$0 = manageEducationView;
    }

    public final Button invoke() {
        return (Button) this.this$0.findViewById(C3343R.C3346id.got_it);
    }
}
