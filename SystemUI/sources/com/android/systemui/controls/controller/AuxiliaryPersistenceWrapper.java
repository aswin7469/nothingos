package com.android.systemui.controls.controller;

import android.app.backup.BackupManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.backup.BackupHelper;
import java.p026io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00172\u00020\u0001:\u0002\u0017\u0018B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B\u000f\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u0003J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0012R*\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/AuxiliaryPersistenceWrapper;", "", "file", "Ljava/io/File;", "executor", "Ljava/util/concurrent/Executor;", "(Ljava/io/File;Ljava/util/concurrent/Executor;)V", "wrapper", "Lcom/android/systemui/controls/controller/ControlsFavoritePersistenceWrapper;", "(Lcom/android/systemui/controls/controller/ControlsFavoritePersistenceWrapper;)V", "<set-?>", "", "Lcom/android/systemui/controls/controller/StructureInfo;", "favorites", "getFavorites", "()Ljava/util/List;", "persistenceWrapper", "changeFile", "", "getCachedFavoritesAndRemoveFor", "componentName", "Landroid/content/ComponentName;", "initialize", "Companion", "DeletionJobService", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuxiliaryPersistenceWrapper.kt */
public final class AuxiliaryPersistenceWrapper {
    public static final String AUXILIARY_FILE_NAME = "aux_controls_favorites.xml";
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private List<StructureInfo> favorites;
    private ControlsFavoritePersistenceWrapper persistenceWrapper;

    public AuxiliaryPersistenceWrapper(ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper) {
        Intrinsics.checkNotNullParameter(controlsFavoritePersistenceWrapper, "wrapper");
        this.persistenceWrapper = controlsFavoritePersistenceWrapper;
        this.favorites = CollectionsKt.emptyList();
        initialize();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AuxiliaryPersistenceWrapper(File file, Executor executor) {
        this(new ControlsFavoritePersistenceWrapper(file, executor, (BackupManager) null, 4, (DefaultConstructorMarker) null));
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(executor, "executor");
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/AuxiliaryPersistenceWrapper$Companion;", "", "()V", "AUXILIARY_FILE_NAME", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AuxiliaryPersistenceWrapper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final List<StructureInfo> getFavorites() {
        return this.favorites;
    }

    public final void changeFile(File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        this.persistenceWrapper.changeFileAndBackupManager(file, (BackupManager) null);
        initialize();
    }

    public final void initialize() {
        List<StructureInfo> list;
        if (this.persistenceWrapper.getFileExists()) {
            list = this.persistenceWrapper.readFavorites();
        } else {
            list = CollectionsKt.emptyList();
        }
        this.favorites = list;
    }

    public final List<StructureInfo> getCachedFavoritesAndRemoveFor(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        if (!this.persistenceWrapper.getFileExists()) {
            return CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object next : this.favorites) {
            if (Intrinsics.areEqual((Object) ((StructureInfo) next).getComponentName(), (Object) componentName)) {
                arrayList.add(next);
            } else {
                arrayList2.add(next);
            }
        }
        Pair pair = new Pair(arrayList, arrayList2);
        List<StructureInfo> list = (List) pair.component1();
        List<StructureInfo> list2 = (List) pair.component2();
        this.favorites = list2;
        if (!list2.isEmpty()) {
            this.persistenceWrapper.storeFavorites(list2);
        } else {
            this.persistenceWrapper.deleteFile();
        }
        return list;
    }

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0012\u0010\u000b\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/AuxiliaryPersistenceWrapper$DeletionJobService;", "Landroid/app/job/JobService;", "()V", "attachContext", "", "context", "Landroid/content/Context;", "onStartJob", "", "params", "Landroid/app/job/JobParameters;", "onStopJob", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AuxiliaryPersistenceWrapper.kt */
    public static final class DeletionJobService extends JobService {
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        /* access modifiers changed from: private */
        public static final int DELETE_FILE_JOB_ID = 1000;
        /* access modifiers changed from: private */
        public static final long WEEK_IN_MILLIS = TimeUnit.DAYS.toMillis(7);

        public boolean onStopJob(JobParameters jobParameters) {
            return true;
        }

        @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rR\u001c\u0010\u0003\u001a\u00020\u00048\u0000XD¢\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/AuxiliaryPersistenceWrapper$DeletionJobService$Companion;", "", "()V", "DELETE_FILE_JOB_ID", "", "getDELETE_FILE_JOB_ID$SystemUI_nothingRelease$annotations", "getDELETE_FILE_JOB_ID$SystemUI_nothingRelease", "()I", "WEEK_IN_MILLIS", "", "getJobForContext", "Landroid/app/job/JobInfo;", "context", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
        /* compiled from: AuxiliaryPersistenceWrapper.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public static /* synthetic */ void getDELETE_FILE_JOB_ID$SystemUI_nothingRelease$annotations() {
            }

            private Companion() {
            }

            public final int getDELETE_FILE_JOB_ID$SystemUI_nothingRelease() {
                return DeletionJobService.DELETE_FILE_JOB_ID;
            }

            public final JobInfo getJobForContext(Context context) {
                Intrinsics.checkNotNullParameter(context, "context");
                JobInfo build = new JobInfo.Builder(getDELETE_FILE_JOB_ID$SystemUI_nothingRelease() + context.getUserId(), new ComponentName(context, DeletionJobService.class)).setMinimumLatency(DeletionJobService.WEEK_IN_MILLIS).setPersisted(true).build();
                Intrinsics.checkNotNullExpressionValue(build, "Builder(jobId, component…                 .build()");
                return build;
            }
        }

        public final void attachContext(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            attachBaseContext(context);
        }

        public boolean onStartJob(JobParameters jobParameters) {
            Intrinsics.checkNotNullParameter(jobParameters, "params");
            synchronized (BackupHelper.Companion.getControlsDataLock()) {
                getBaseContext().deleteFile(AuxiliaryPersistenceWrapper.AUXILIARY_FILE_NAME);
            }
            return false;
        }
    }
}
