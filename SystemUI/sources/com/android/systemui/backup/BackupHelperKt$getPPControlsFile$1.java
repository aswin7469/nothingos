package com.android.systemui.backup;

import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Environment;
import com.android.systemui.controls.controller.AuxiliaryPersistenceWrapper;
import java.p026io.File;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.p028io.FilesKt;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: BackupHelper.kt */
final class BackupHelperKt$getPPControlsFile$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Context $context;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    BackupHelperKt$getPPControlsFile$1(Context context) {
        super(0);
        this.$context = context;
    }

    public final void invoke() {
        File filesDir = this.$context.getFilesDir();
        File buildPath = Environment.buildPath(filesDir, new String[]{"controls_favorites.xml"});
        if (buildPath.exists()) {
            File buildPath2 = Environment.buildPath(filesDir, new String[]{AuxiliaryPersistenceWrapper.AUXILIARY_FILE_NAME});
            Intrinsics.checkNotNullExpressionValue(buildPath, "file");
            Intrinsics.checkNotNullExpressionValue(buildPath2, "dest");
            FilesKt.copyTo$default(buildPath, buildPath2, false, 0, 6, (Object) null);
            JobScheduler jobScheduler = (JobScheduler) this.$context.getSystemService(JobScheduler.class);
            if (jobScheduler != null) {
                jobScheduler.schedule(AuxiliaryPersistenceWrapper.DeletionJobService.Companion.getJobForContext(this.$context));
            }
        }
    }
}
