package com.android.systemui.accessibility.floatingmenu;

import android.view.View;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AnnotationLinkSpan$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ View f$0;

    public /* synthetic */ AnnotationLinkSpan$$ExternalSyntheticLambda3(View view) {
        this.f$0 = view;
    }

    public final void accept(Object obj) {
        ((View.OnClickListener) obj).onClick(this.f$0);
    }
}
