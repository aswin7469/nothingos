package com.android.systemui.controls.controller;

import android.content.Context;
import android.os.Environment;
import android.os.UserHandle;
import java.io.File;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class UserStructure {
    private final File auxiliaryFile;
    private final File file;
    private final Context userContext;

    public UserStructure(@NotNull Context context, @NotNull UserHandle user) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(user, "user");
        Context createContextAsUser = context.createContextAsUser(user, 0);
        this.userContext = createContextAsUser;
        this.file = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{"controls_favorites.xml"});
        this.auxiliaryFile = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{"aux_controls_favorites.xml"});
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
