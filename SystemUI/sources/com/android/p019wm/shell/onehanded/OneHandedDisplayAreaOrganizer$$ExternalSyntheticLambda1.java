package com.android.p019wm.shell.onehanded;

import android.view.SurfaceControl;
import android.window.WindowContainerToken;
import java.util.function.BiConsumer;

/* renamed from: com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda1 implements BiConsumer {
    public final /* synthetic */ OneHandedDisplayAreaOrganizer f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;

    public /* synthetic */ OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda1(OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer, SurfaceControl.Transaction transaction) {
        this.f$0 = oneHandedDisplayAreaOrganizer;
        this.f$1 = transaction;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.mo49899x4ed14564(this.f$1, (WindowContainerToken) obj, (SurfaceControl) obj2);
    }
}
