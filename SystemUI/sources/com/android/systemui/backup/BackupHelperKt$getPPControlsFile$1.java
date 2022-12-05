package com.android.systemui.backup;

import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Environment;
import com.android.systemui.controls.controller.AuxiliaryPersistenceWrapper;
import java.io.File;
import kotlin.Unit;
import kotlin.io.FilesKt__UtilsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: BackupHelper.kt */
/* loaded from: classes.dex */
final class BackupHelperKt$getPPControlsFile$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Context $context;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BackupHelperKt$getPPControlsFile$1(Context context) {
        super(0);
        this.$context = context;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        File filesDir = this.$context.getFilesDir();
        File file = Environment.buildPath(filesDir, new String[]{"controls_favorites.xml"});
        if (file.exists()) {
            File dest = Environment.buildPath(filesDir, new String[]{"aux_controls_favorites.xml"});
            Intrinsics.checkNotNullExpressionValue(file, "file");
            Intrinsics.checkNotNullExpressionValue(dest, "dest");
            FilesKt__UtilsKt.copyTo$default(file, dest, false, 0, 6, null);
            JobScheduler jobScheduler = (JobScheduler) this.$context.getSystemService(JobScheduler.class);
            if (jobScheduler == null) {
                return;
            }
            jobScheduler.schedule(AuxiliaryPersistenceWrapper.DeletionJobService.Companion.getJobForContext(this.$context));
        }
    }
}
