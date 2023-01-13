package com.android.p019wm.shell.bubbles;

import android.widget.TextView;
import com.android.p019wm.shell.C3353R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.StackEducationView$titleTextView$2 */
/* compiled from: StackEducationView.kt */
final class StackEducationView$titleTextView$2 extends Lambda implements Function0<TextView> {
    final /* synthetic */ StackEducationView this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    StackEducationView$titleTextView$2(StackEducationView stackEducationView) {
        super(0);
        this.this$0 = stackEducationView;
    }

    public final TextView invoke() {
        return (TextView) this.this$0.findViewById(C3353R.C3356id.stack_education_title);
    }
}
