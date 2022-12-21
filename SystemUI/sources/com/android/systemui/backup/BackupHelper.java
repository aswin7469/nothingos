package com.android.systemui.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.people.widget.PeopleBackupHelper;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0016\u0018\u0000 \n2\u00020\u0001:\u0002\n\u000bB\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\u0004H\u0016¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/backup/BackupHelper;", "Landroid/app/backup/BackupAgentHelper;", "()V", "onCreate", "", "userHandle", "Landroid/os/UserHandle;", "operationType", "", "onRestoreFinished", "Companion", "NoOverwriteFileBackupHelper", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BackupHelper.kt */
public class BackupHelper extends BackupAgentHelper {
    public static final String ACTION_RESTORE_FINISHED = "com.android.systemui.backup.RESTORE_FINISHED";
    public static final String CONTROLS = "controls_favorites.xml";
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String NO_OVERWRITE_FILES_BACKUP_KEY = "systemui.files_no_overwrite";
    private static final String PEOPLE_TILES_BACKUP_KEY = "systemui.people.shared_preferences";
    private static final String PERMISSION_SELF = "com.android.systemui.permission.SELF";
    public static final String TAG = "BackupHelper";
    /* access modifiers changed from: private */
    public static final Object controlsDataLock = new Object();

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/backup/BackupHelper$Companion;", "", "()V", "ACTION_RESTORE_FINISHED", "", "CONTROLS", "NO_OVERWRITE_FILES_BACKUP_KEY", "PEOPLE_TILES_BACKUP_KEY", "PERMISSION_SELF", "TAG", "controlsDataLock", "getControlsDataLock", "()Ljava/lang/Object;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: BackupHelper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Object getControlsDataLock() {
            return BackupHelper.controlsDataLock;
        }
    }

    public void onCreate(UserHandle userHandle, int i) {
        Intrinsics.checkNotNullParameter(userHandle, "userHandle");
        super.onCreate();
        Context context = this;
        addHelper(NO_OVERWRITE_FILES_BACKUP_KEY, new NoOverwriteFileBackupHelper(controlsDataLock, context, MapsKt.mapOf(TuplesKt.m1796to("controls_favorites.xml", BackupHelperKt.getPPControlsFile(context)))));
        if (userHandle.isSystem()) {
            List<String> filesToBackup = PeopleBackupHelper.getFilesToBackup();
            Intrinsics.checkNotNullExpressionValue(filesToBackup, "keys");
            Object[] array = filesToBackup.toArray((T[]) new String[0]);
            Intrinsics.checkNotNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            addHelper(PEOPLE_TILES_BACKUP_KEY, new PeopleBackupHelper(context, userHandle, (String[]) array));
        }
    }

    public void onRestoreFinished() {
        super.onRestoreFinished();
        Intent intent = new Intent(ACTION_RESTORE_FINISHED);
        intent.setPackage(getPackageName());
        intent.putExtra("android.intent.extra.USER_ID", getUserId());
        intent.setFlags(1073741824);
        sendBroadcastAsUser(intent, UserHandle.SYSTEM, "com.android.systemui.permission.SELF");
    }

    @Metadata(mo64986d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0018\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0007¢\u0006\u0002\u0010\u000bJ&\u0010\u0012\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0014H\u0016J\u0010\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0019H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR#\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001a"}, mo64987d2 = {"Lcom/android/systemui/backup/BackupHelper$NoOverwriteFileBackupHelper;", "Landroid/app/backup/FileBackupHelper;", "lock", "", "context", "Landroid/content/Context;", "fileNamesAndPostProcess", "", "", "Lkotlin/Function0;", "", "(Ljava/lang/Object;Landroid/content/Context;Ljava/util/Map;)V", "getContext", "()Landroid/content/Context;", "getFileNamesAndPostProcess", "()Ljava/util/Map;", "getLock", "()Ljava/lang/Object;", "performBackup", "oldState", "Landroid/os/ParcelFileDescriptor;", "data", "Landroid/app/backup/BackupDataOutput;", "newState", "restoreEntity", "Landroid/app/backup/BackupDataInputStream;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: BackupHelper.kt */
    private static final class NoOverwriteFileBackupHelper extends FileBackupHelper {
        private final Context context;
        private final Map<String, Function0<Unit>> fileNamesAndPostProcess;
        private final Object lock;

        public final Object getLock() {
            return this.lock;
        }

        public final Context getContext() {
            return this.context;
        }

        public final Map<String, Function0<Unit>> getFileNamesAndPostProcess() {
            return this.fileNamesAndPostProcess;
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public NoOverwriteFileBackupHelper(java.lang.Object r3, android.content.Context r4, java.util.Map<java.lang.String, ? extends kotlin.jvm.functions.Function0<kotlin.Unit>> r5) {
            /*
                r2 = this;
                java.lang.String r0 = "lock"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
                java.lang.String r0 = "context"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
                java.lang.String r0 = "fileNamesAndPostProcess"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                java.util.Set r0 = r5.keySet()
                java.util.Collection r0 = (java.util.Collection) r0
                r1 = 0
                java.lang.String[] r1 = new java.lang.String[r1]
                java.lang.Object[] r0 = r0.toArray((T[]) r1)
                java.lang.String r1 = "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>"
                kotlin.jvm.internal.Intrinsics.checkNotNull(r0, r1)
                java.lang.String[] r0 = (java.lang.String[]) r0
                int r1 = r0.length
                java.lang.Object[] r0 = java.util.Arrays.copyOf((T[]) r0, (int) r1)
                java.lang.String[] r0 = (java.lang.String[]) r0
                r2.<init>(r4, r0)
                r2.lock = r3
                r2.context = r4
                r2.fileNamesAndPostProcess = r5
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.backup.BackupHelper.NoOverwriteFileBackupHelper.<init>(java.lang.Object, android.content.Context, java.util.Map):void");
        }

        public void restoreEntity(BackupDataInputStream backupDataInputStream) {
            Intrinsics.checkNotNullParameter(backupDataInputStream, "data");
            if (Environment.buildPath(this.context.getFilesDir(), new String[]{backupDataInputStream.getKey()}).exists()) {
                Log.w(BackupHelper.TAG, "File " + backupDataInputStream.getKey() + " already exists. Skipping restore.");
                return;
            }
            synchronized (this.lock) {
                super.restoreEntity(backupDataInputStream);
                Function0 function0 = this.fileNamesAndPostProcess.get(backupDataInputStream.getKey());
                if (function0 != null) {
                    function0.invoke();
                    Unit unit = Unit.INSTANCE;
                }
            }
        }

        public void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
            synchronized (this.lock) {
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
