package com.android.systemui.p012qs;

import com.android.systemui.Dumpable;
import com.android.systemui.plugins.p011qs.QSTile;
import java.p026io.PrintWriter;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ PrintWriter f$0;
    public final /* synthetic */ String[] f$1;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda3(PrintWriter printWriter, String[] strArr) {
        this.f$0 = printWriter;
        this.f$1 = strArr;
    }

    public final void accept(Object obj) {
        ((Dumpable) ((QSTile) obj)).dump(this.f$0, this.f$1);
    }
}
