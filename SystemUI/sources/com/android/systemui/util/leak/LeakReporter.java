package com.android.systemui.util.leak;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.android.systemui.dagger.SysUISingleton;
import com.google.android.collect.Lists;
import java.p026io.File;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;

@SysUISingleton
public class LeakReporter {
    public static final String FILEPROVIDER_AUTHORITY = "com.android.systemui.fileprovider";
    static final String LEAK_DIR = "leak";
    static final String LEAK_DUMP = "leak.dump";
    static final String LEAK_HPROF = "leak.hprof";
    static final String TAG = "LeakReporter";
    private final Context mContext;
    private final LeakDetector mLeakDetector;
    private final String mLeakReportEmail;

    @Inject
    public LeakReporter(Context context, LeakDetector leakDetector, @Named("leak_report_email") String str) {
        this.mContext = context;
        this.mLeakDetector = leakDetector;
        this.mLeakReportEmail = str;
    }

    public void dumpLeak(int i) {
        FileOutputStream fileOutputStream;
        try {
            File file = new File(this.mContext.getCacheDir(), LEAK_DIR);
            file.mkdir();
            File file2 = new File(file, LEAK_HPROF);
            Debug.dumpHprofData(file2.getAbsolutePath());
            File file3 = new File(file, LEAK_DUMP);
            fileOutputStream = new FileOutputStream(file3);
            PrintWriter printWriter = new PrintWriter((OutputStream) fileOutputStream);
            printWriter.print("Build: ");
            printWriter.println(SystemProperties.get("ro.build.description"));
            printWriter.println();
            printWriter.flush();
            this.mLeakDetector.dump(printWriter, new String[0]);
            printWriter.close();
            fileOutputStream.close();
            NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
            NotificationChannel notificationChannel = new NotificationChannel(LEAK_DIR, "Leak Alerts", 4);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(TAG, 0, new Notification.Builder(this.mContext, notificationChannel.getId()).setAutoCancel(true).setShowWhen(true).setContentTitle("Memory Leak Detected").setContentText(String.format("SystemUI has detected %d leaked objects. Tap to send", Integer.valueOf(i))).setSmallIcon(17303599).setContentIntent(PendingIntent.getActivityAsUser(this.mContext, 0, getIntent(file2, file3), 201326592, (Bundle) null, UserHandle.CURRENT)).build());
            return;
        } catch (IOException e) {
            Log.e(TAG, "Couldn't dump heap for leak", e);
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    private Intent getIntent(File file, File file2) {
        Uri uriForFile = FileProvider.getUriForFile(this.mContext, FILEPROVIDER_AUTHORITY, file2);
        Uri uriForFile2 = FileProvider.getUriForFile(this.mContext, FILEPROVIDER_AUTHORITY, file);
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.addFlags(1);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.leakreport");
        intent.putExtra("android.intent.extra.SUBJECT", "SystemUI leak report");
        intent.putExtra("android.intent.extra.TEXT", "Build info: " + SystemProperties.get("ro.build.description"));
        ClipData clipData = new ClipData((CharSequence) null, new String[]{"application/vnd.android.leakreport"}, new ClipData.Item((CharSequence) null, (String) null, (Intent) null, uriForFile));
        ArrayList newArrayList = Lists.newArrayList(new Uri[]{uriForFile});
        clipData.addItem(new ClipData.Item((CharSequence) null, (String) null, (Intent) null, uriForFile2));
        newArrayList.add(uriForFile2);
        intent.setClipData(clipData);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", newArrayList);
        String str = this.mLeakReportEmail;
        if (str != null) {
            intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
        }
        return intent;
    }
}
