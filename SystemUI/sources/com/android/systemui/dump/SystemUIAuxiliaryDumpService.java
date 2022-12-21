package com.android.systemui.dump;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.p026io.FileDescriptor;
import java.p026io.PrintWriter;
import javax.inject.Inject;

public class SystemUIAuxiliaryDumpService extends Service {
    private final DumpHandler mDumpHandler;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Inject
    public SystemUIAuxiliaryDumpService(DumpHandler dumpHandler) {
        this.mDumpHandler = dumpHandler;
    }

    /* access modifiers changed from: protected */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mDumpHandler.dump(printWriter, new String[]{DumpHandler.PRIORITY_ARG, DumpHandler.PRIORITY_ARG_NORMAL});
    }
}
