package com.android.p019wm.shell.displayareahelper;

import android.view.SurfaceControl;
import com.android.p019wm.shell.RootDisplayAreaOrganizer;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.displayareahelper.DisplayAreaHelperController */
public class DisplayAreaHelperController implements DisplayAreaHelper {
    private final Executor mExecutor;
    private final RootDisplayAreaOrganizer mRootDisplayAreaOrganizer;

    public DisplayAreaHelperController(Executor executor, RootDisplayAreaOrganizer rootDisplayAreaOrganizer) {
        this.mExecutor = executor;
        this.mRootDisplayAreaOrganizer = rootDisplayAreaOrganizer;
    }

    public void attachToRootDisplayArea(int i, SurfaceControl.Builder builder, Consumer<SurfaceControl.Builder> consumer) {
        this.mExecutor.execute(new DisplayAreaHelperController$$ExternalSyntheticLambda0(this, i, builder, consumer));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$attachToRootDisplayArea$0$com-android-wm-shell-displayareahelper-DisplayAreaHelperController */
    public /* synthetic */ void mo49482x5024e067(int i, SurfaceControl.Builder builder, Consumer consumer) {
        this.mRootDisplayAreaOrganizer.attachToDisplayArea(i, builder);
        consumer.accept(builder);
    }
}
