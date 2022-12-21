package com.android.p019wm.shell;

import com.android.p019wm.shell.common.annotations.ExternalThread;
import java.p026io.PrintWriter;

@ExternalThread
/* renamed from: com.android.wm.shell.ShellCommandHandler */
public interface ShellCommandHandler {
    void dump(PrintWriter printWriter);

    boolean handleCommand(String[] strArr, PrintWriter printWriter);
}
