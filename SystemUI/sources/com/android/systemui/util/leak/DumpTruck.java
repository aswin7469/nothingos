package com.android.systemui.util.leak;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Process;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.android.systemui.util.leak.GarbageMonitor;
import java.p026io.BufferedInputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DumpTruck {
    private static final int BUFSIZ = 1048576;
    private static final String FILEPROVIDER_AUTHORITY = "com.android.systemui.fileprovider";
    private static final String FILEPROVIDER_PATH = "leak";
    private static final String TAG = "DumpTruck";
    final StringBuilder body = new StringBuilder();
    private final Context context;
    private Uri hprofUri;
    private final GarbageMonitor mGarbageMonitor;
    private long rss;

    public DumpTruck(Context context2, GarbageMonitor garbageMonitor) {
        this.context = context2;
        this.mGarbageMonitor = garbageMonitor;
    }

    public DumpTruck captureHeaps(List<Long> list) {
        File file = new File(this.context.getCacheDir(), FILEPROVIDER_PATH);
        file.mkdirs();
        this.hprofUri = null;
        this.body.setLength(0);
        this.body.append("Build: ").append(Build.DISPLAY).append("\n\nProcesses:\n");
        ArrayList arrayList = new ArrayList();
        int myPid = Process.myPid();
        for (Long intValue : list) {
            int intValue2 = intValue.intValue();
            this.body.append("  pid ").append(intValue2);
            GarbageMonitor.ProcessMemInfo memInfo = this.mGarbageMonitor.getMemInfo(intValue2);
            if (memInfo != null) {
                this.body.append(":").append(" up=").append(memInfo.getUptime()).append(" rss=").append(memInfo.currentRss);
                this.rss = memInfo.currentRss;
            }
            if (intValue2 == myPid) {
                String path = new File(file, String.format("heap-%d.ahprof", Integer.valueOf(intValue2))).getPath();
                Log.v(TAG, "Dumping memory info for process " + intValue2 + " to " + path);
                try {
                    Debug.dumpHprofData(path);
                    arrayList.add(path);
                    this.body.append(" (hprof attached)");
                } catch (IOException e) {
                    Log.e(TAG, "error dumping memory:", e);
                    this.body.append("\n** Could not dump heap: \n").append(e.toString()).append("\n");
                }
            }
            this.body.append("\n");
        }
        try {
            String canonicalPath = new File(file, String.format("hprof-%d.zip", Long.valueOf(System.currentTimeMillis()))).getCanonicalPath();
            if (zipUp(canonicalPath, arrayList)) {
                this.hprofUri = FileProvider.getUriForFile(this.context, "com.android.systemui.fileprovider", new File(canonicalPath));
                Log.v(TAG, "Heap dump accessible at URI: " + this.hprofUri);
            }
        } catch (IOException e2) {
            Log.e(TAG, "unable to zip up heapdumps", e2);
            this.body.append("\n** Could not zip up files: \n").append(e2.toString()).append("\n");
        }
        return this;
    }

    public Uri getDumpUri() {
        return this.hprofUri;
    }

    public Intent createShareIntent() {
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.addFlags(268435456);
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.SUBJECT", String.format("SystemUI memory dump (rss=%dM)", Long.valueOf(this.rss / 1024)));
        intent.putExtra("android.intent.extra.TEXT", this.body.toString());
        if (this.hprofUri != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.hprofUri);
            intent.setType("application/zip");
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
            intent.setClipData(new ClipData(new ClipDescription("content", new String[]{"text/plain"}), new ClipData.Item(this.hprofUri)));
            intent.addFlags(1);
        }
        return intent;
    }

    private static boolean zipUp(String str, ArrayList<String> arrayList) {
        BufferedInputStream bufferedInputStream;
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(str));
            try {
                byte[] bArr = new byte[1048576];
                Iterator<String> it = arrayList.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(next));
                    zipOutputStream.putNextEntry(new ZipEntry(next));
                    while (true) {
                        int read = bufferedInputStream.read(bArr, 0, 1048576);
                        if (read <= 0) {
                            break;
                        }
                        zipOutputStream.write(bArr, 0, read);
                    }
                    zipOutputStream.closeEntry();
                    bufferedInputStream.close();
                }
                zipOutputStream.close();
                return true;
            } catch (Throwable th) {
                zipOutputStream.close();
                throw th;
            }
            throw th;
        } catch (IOException e) {
            Log.e(TAG, "error zipping up profile data", e);
            return false;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
    }
}
