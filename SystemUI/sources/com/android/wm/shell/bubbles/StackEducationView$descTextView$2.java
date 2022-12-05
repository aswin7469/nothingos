package com.android.wm.shell.bubbles;

import android.widget.TextView;
import com.android.wm.shell.R;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StackEducationView.kt */
/* loaded from: classes2.dex */
public final class StackEducationView$descTextView$2 extends Lambda implements Function0<TextView> {
    final /* synthetic */ StackEducationView this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StackEducationView$descTextView$2(StackEducationView stackEducationView) {
        super(0);
        this.this$0 = stackEducationView;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final TextView mo1951invoke() {
        return (TextView) this.this$0.findViewById(R.id.stack_education_description);
    }
}
