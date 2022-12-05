package com.android.wm.shell.bubbles;

import android.widget.TextView;
import com.android.wm.shell.R;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ManageEducationView.kt */
/* loaded from: classes2.dex */
public final class ManageEducationView$titleTextView$2 extends Lambda implements Function0<TextView> {
    final /* synthetic */ ManageEducationView this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ManageEducationView$titleTextView$2(ManageEducationView manageEducationView) {
        super(0);
        this.this$0 = manageEducationView;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final TextView mo1951invoke() {
        return (TextView) this.this$0.findViewById(R.id.user_education_title);
    }
}
