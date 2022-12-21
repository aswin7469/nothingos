package com.android.systemui.controls.controller;

import android.content.Context;
import android.os.Environment;
import android.os.UserHandle;
import java.p026io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0019\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\b¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0019\u0010\f\u001a\n \t*\u0004\u0018\u00010\b0\b¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0019\u0010\u000e\u001a\n \t*\u0004\u0018\u00010\u00030\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/UserStructure;", "", "context", "Landroid/content/Context;", "user", "Landroid/os/UserHandle;", "(Landroid/content/Context;Landroid/os/UserHandle;)V", "auxiliaryFile", "Ljava/io/File;", "kotlin.jvm.PlatformType", "getAuxiliaryFile", "()Ljava/io/File;", "file", "getFile", "userContext", "getUserContext", "()Landroid/content/Context;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsControllerImpl.kt */
public final class UserStructure {
    private final File auxiliaryFile;
    private final File file;
    private final Context userContext;

    public UserStructure(Context context, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        Context createContextAsUser = context.createContextAsUser(userHandle, 0);
        this.userContext = createContextAsUser;
        this.file = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{"controls_favorites.xml"});
        this.auxiliaryFile = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{AuxiliaryPersistenceWrapper.AUXILIARY_FILE_NAME});
    }

    public final Context getUserContext() {
        return this.userContext;
    }

    public final File getFile() {
        return this.file;
    }

    public final File getAuxiliaryFile() {
        return this.auxiliaryFile;
    }
}
