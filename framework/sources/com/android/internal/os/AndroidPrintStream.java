package com.android.internal.os;

import android.os.SystemProperties;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class AndroidPrintStream extends LoggingPrintStream {
    private static final boolean PERF_SYSTEM_OUT_ENABLED = SystemProperties.getBoolean("persist.perf.print.cmdline", false);
    private String mCmdLine;
    private final int priority;
    private final String tag;

    public AndroidPrintStream(int priority, String tag) {
        if (tag == null) {
            throw new NullPointerException("tag");
        }
        this.priority = priority;
        this.tag = tag;
        if (PERF_SYSTEM_OUT_ENABLED) {
            readCmdLineFile();
        }
    }

    @Override // com.android.internal.os.LoggingPrintStream
    protected void log(String line) {
        if (PERF_SYSTEM_OUT_ENABLED) {
            int i = this.priority;
            String str = this.tag;
            Log.println(i, str, line + this.mCmdLine);
            return;
        }
        Log.println(this.priority, this.tag, line);
    }

    private void readCmdLineFile() {
        byte[] buffer = new byte[1024];
        try {
            InputStream is = Files.newInputStream(Paths.get("/proc/self/cmdline", new String[0]), new OpenOption[0]);
            int len = is.read(buffer);
            is.close();
            if (len > 0) {
                int i = 0;
                while (i < len && buffer[i] != 0) {
                    i++;
                }
                this.mCmdLine = new String(buffer, 0, i);
                this.mCmdLine = " ### " + this.mCmdLine;
            }
            if (is != null) {
                is.close();
            }
            if (this.mCmdLine != null) {
                return;
            }
        } catch (NoSuchFileException e) {
            if (this.mCmdLine != null) {
                return;
            }
        } catch (IOException e2) {
            if (this.mCmdLine != null) {
                return;
            }
        } catch (Throwable th) {
            if (this.mCmdLine == null) {
                this.mCmdLine = " ### null";
            }
            throw th;
        }
        this.mCmdLine = " ### null";
    }
}
