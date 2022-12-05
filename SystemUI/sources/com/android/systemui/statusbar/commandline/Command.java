package com.android.systemui.statusbar.commandline;

import java.io.PrintWriter;
import java.util.List;
import org.jetbrains.annotations.NotNull;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public interface Command {
    void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list);
}
