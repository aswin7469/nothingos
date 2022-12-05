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
import java.util.Objects;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: BackupHelper.kt */
/* loaded from: classes.dex */
public class BackupHelper extends BackupAgentHelper {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final Object controlsDataLock = new Object();

    /* compiled from: BackupHelper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final Object getControlsDataLock() {
            return BackupHelper.controlsDataLock;
        }
    }

    public void onCreate(@NotNull UserHandle userHandle, int i) {
        Function0 pPControlsFile;
        Map mapOf;
        Intrinsics.checkNotNullParameter(userHandle, "userHandle");
        super.onCreate();
        pPControlsFile = BackupHelperKt.getPPControlsFile(this);
        mapOf = MapsKt__MapsJVMKt.mapOf(TuplesKt.to("controls_favorites.xml", pPControlsFile));
        addHelper("systemui.files_no_overwrite", new NoOverwriteFileBackupHelper(Companion.getControlsDataLock(), this, mapOf));
        if (!userHandle.isSystem()) {
            return;
        }
        List<String> keys = PeopleBackupHelper.getFilesToBackup();
        Intrinsics.checkNotNullExpressionValue(keys, "keys");
        Object[] array = keys.toArray(new String[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
        addHelper("systemui.people.shared_preferences", new PeopleBackupHelper(this, userHandle, (String[]) array));
    }

    @Override // android.app.backup.BackupAgent
    public void onRestoreFinished() {
        super.onRestoreFinished();
        Intent intent = new Intent("com.android.systemui.backup.RESTORE_FINISHED");
        intent.setPackage(getPackageName());
        intent.putExtra("android.intent.extra.USER_ID", getUserId());
        intent.setFlags(1073741824);
        sendBroadcastAsUser(intent, UserHandle.SYSTEM, "com.android.systemui.permission.SELF");
    }

    /* compiled from: BackupHelper.kt */
    /* loaded from: classes.dex */
    private static final class NoOverwriteFileBackupHelper extends FileBackupHelper {
        @NotNull
        private final Context context;
        @NotNull
        private final Map<String, Function0<Unit>> fileNamesAndPostProcess;
        @NotNull
        private final Object lock;

        @NotNull
        public final Map<String, Function0<Unit>> getFileNamesAndPostProcess() {
            return this.fileNamesAndPostProcess;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /* JADX WARN: Multi-variable type inference failed */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public NoOverwriteFileBackupHelper(@NotNull Object lock, @NotNull Context context, @NotNull Map<String, ? extends Function0<Unit>> fileNamesAndPostProcess) {
            super(context, r2);
            Intrinsics.checkNotNullParameter(lock, "lock");
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(fileNamesAndPostProcess, "fileNamesAndPostProcess");
            Object[] array = fileNamesAndPostProcess.keySet().toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
            String[] strArr = (String[]) array;
            String[] strArr2 = new String[strArr.length];
            System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
            this.lock = lock;
            this.context = context;
            this.fileNamesAndPostProcess = fileNamesAndPostProcess;
        }

        @Override // android.app.backup.FileBackupHelper, android.app.backup.BackupHelper
        public void restoreEntity(@NotNull BackupDataInputStream data) {
            Intrinsics.checkNotNullParameter(data, "data");
            if (Environment.buildPath(this.context.getFilesDir(), new String[]{data.getKey()}).exists()) {
                Log.w("BackupHelper", "File " + ((Object) data.getKey()) + " already exists. Skipping restore.");
                return;
            }
            synchronized (this.lock) {
                super.restoreEntity(data);
                Function0<Unit> function0 = getFileNamesAndPostProcess().get(data.getKey());
                if (function0 != null) {
                    function0.mo1951invoke();
                    Unit unit = Unit.INSTANCE;
                }
            }
        }

        @Override // android.app.backup.FileBackupHelper, android.app.backup.BackupHelper
        public void performBackup(@Nullable ParcelFileDescriptor parcelFileDescriptor, @Nullable BackupDataOutput backupDataOutput, @Nullable ParcelFileDescriptor parcelFileDescriptor2) {
            synchronized (this.lock) {
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
