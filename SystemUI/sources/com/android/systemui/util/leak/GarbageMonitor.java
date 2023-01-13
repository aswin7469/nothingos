package com.android.systemui.util.leak;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import androidx.exifinterface.media.ExifInterface;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSIconViewImpl;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.MessageRouter;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import kotlin.text.Typography;

@SysUISingleton
public class GarbageMonitor implements Dumpable {
    public static final boolean ADD_MEMORY_TILE_TO_DEFAULT_ON_DEBUGGABLE_BUILDS = true;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int DO_GARBAGE_INSPECTION = 1000;
    private static final int DO_HEAP_TRACK = 3000;
    private static final boolean ENABLE_AM_HEAP_LIMIT;
    public static final String FORCE_ENABLE_LEAK_REPORTING = "sysui_force_enable_leak_reporting";
    static final int GARBAGE_ALLOWANCE = 5;
    private static final long GARBAGE_INSPECTION_INTERVAL = 900000;
    public static final boolean HEAP_TRACKING_ENABLED = Build.IS_DEBUGGABLE;
    private static final int HEAP_TRACK_HISTORY_LEN = 720;
    private static final long HEAP_TRACK_INTERVAL = 60000;
    public static final boolean LEAK_REPORTING_ENABLED = (Build.IS_DEBUGGABLE && SystemProperties.getBoolean("debug.enable_leak_reporting", false));
    private static final String SETTINGS_KEY_AM_HEAP_LIMIT = "systemui_am_heap_limit";
    private static final String TAG = "GarbageMonitor";
    private final Context mContext;
    private final LongSparseArray<ProcessMemInfo> mData = new LongSparseArray<>();
    private final DelayableExecutor mDelayableExecutor;
    private final DumpTruck mDumpTruck;
    /* access modifiers changed from: private */
    public long mHeapLimit;
    private final LeakReporter mLeakReporter;
    private final MessageRouter mMessageRouter;
    private final ArrayList<Long> mPids = new ArrayList<>();
    private MemoryTile mQSTile;
    private final TrackedGarbage mTrackedGarbage;

    static {
        boolean z = true;
        if (!Build.IS_DEBUGGABLE || !SystemProperties.getBoolean("debug.enable_sysui_heap_limit", false)) {
            z = false;
        }
        ENABLE_AM_HEAP_LIMIT = z;
    }

    @Inject
    public GarbageMonitor(Context context, @Background DelayableExecutor delayableExecutor, @Background MessageRouter messageRouter, LeakDetector leakDetector, LeakReporter leakReporter, DumpManager dumpManager) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mDelayableExecutor = delayableExecutor;
        this.mMessageRouter = messageRouter;
        messageRouter.subscribeTo(1000, (MessageRouter.SimpleMessageListener) new GarbageMonitor$$ExternalSyntheticLambda1(this));
        messageRouter.subscribeTo(3000, (MessageRouter.SimpleMessageListener) new GarbageMonitor$$ExternalSyntheticLambda2(this));
        this.mTrackedGarbage = leakDetector.getTrackedGarbage();
        this.mLeakReporter = leakReporter;
        this.mDumpTruck = new DumpTruck(applicationContext, this);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        if (ENABLE_AM_HEAP_LIMIT) {
            this.mHeapLimit = (long) Settings.Global.getInt(context.getContentResolver(), SETTINGS_KEY_AM_HEAP_LIMIT, applicationContext.getResources().getInteger(C1894R.integer.watch_heap_limit));
        }
    }

    public void startLeakMonitor() {
        if (this.mTrackedGarbage != null) {
            this.mMessageRouter.sendMessage(1000);
        }
    }

    public void startHeapTracking() {
        startTrackingProcess((long) Process.myPid(), this.mContext.getPackageName(), System.currentTimeMillis());
        this.mMessageRouter.sendMessage(3000);
    }

    private boolean gcAndCheckGarbage() {
        if (this.mTrackedGarbage.countOldGarbage() <= 5) {
            return false;
        }
        Runtime.getRuntime().mo59687gc();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void reinspectGarbageAfterGc() {
        int countOldGarbage = this.mTrackedGarbage.countOldGarbage();
        if (countOldGarbage > 5) {
            this.mLeakReporter.dumpLeak(countOldGarbage);
        }
    }

    public ProcessMemInfo getMemInfo(int i) {
        return this.mData.get((long) i);
    }

    public List<Long> getTrackedProcesses() {
        return this.mPids;
    }

    public void startTrackingProcess(long j, String str, long j2) {
        synchronized (this.mPids) {
            if (!this.mPids.contains(Long.valueOf(j))) {
                this.mPids.add(Long.valueOf(j));
                logPids();
                this.mData.put(j, new ProcessMemInfo(j, str, j2));
            }
        }
    }

    private void logPids() {
        if (DEBUG) {
            StringBuffer stringBuffer = new StringBuffer("Now tracking processes: ");
            for (int i = 0; i < this.mPids.size(); i++) {
                this.mPids.get(i).intValue();
                stringBuffer.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            Log.v(TAG, stringBuffer.toString());
        }
    }

    private void update() {
        synchronized (this.mPids) {
            int i = 0;
            while (true) {
                if (i >= this.mPids.size()) {
                    break;
                }
                int intValue = this.mPids.get(i).intValue();
                long[] rss = Process.getRss(intValue);
                if (rss != null || rss.length != 0) {
                    long j = rss[0];
                    long j2 = (long) intValue;
                    ProcessMemInfo processMemInfo = this.mData.get(j2);
                    long[] jArr = processMemInfo.rss;
                    int i2 = processMemInfo.head;
                    processMemInfo.currentRss = j;
                    jArr[i2] = j;
                    processMemInfo.head = (processMemInfo.head + 1) % processMemInfo.rss.length;
                    if (processMemInfo.currentRss > processMemInfo.max) {
                        processMemInfo.max = processMemInfo.currentRss;
                    }
                    if (processMemInfo.currentRss == 0) {
                        if (DEBUG) {
                            Log.v(TAG, "update: pid " + intValue + " has rss=0, it probably died");
                        }
                        this.mData.remove(j2);
                    }
                    i++;
                } else if (DEBUG) {
                    Log.e(TAG, "update: Process.getRss() didn't provide any values.");
                }
            }
            for (int size = this.mPids.size() - 1; size >= 0; size--) {
                if (this.mData.get((long) this.mPids.get(size).intValue()) == null) {
                    this.mPids.remove(size);
                    logPids();
                }
            }
        }
        MemoryTile memoryTile = this.mQSTile;
        if (memoryTile != null) {
            memoryTile.update();
        }
    }

    /* access modifiers changed from: private */
    public void setTile(MemoryTile memoryTile) {
        this.mQSTile = memoryTile;
        if (memoryTile != null) {
            memoryTile.update();
        }
    }

    /* access modifiers changed from: private */
    public static String formatBytes(long j) {
        String[] strArr = {"B", "K", "M", "G", ExifInterface.GPS_DIRECTION_TRUE};
        int i = 0;
        while (i < 5 && j >= 1024) {
            j /= 1024;
            i++;
        }
        return j + strArr[i];
    }

    /* access modifiers changed from: private */
    public Intent dumpHprofAndGetShareIntent() {
        return this.mDumpTruck.captureHeaps(getTrackedProcesses()).createShareIntent();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("GarbageMonitor params:");
        printWriter.println(String.format("   mHeapLimit=%d KB", Long.valueOf(this.mHeapLimit)));
        printWriter.println(String.format("   GARBAGE_INSPECTION_INTERVAL=%d (%.1f mins)", Long.valueOf((long) GARBAGE_INSPECTION_INTERVAL), Float.valueOf(15.0f)));
        printWriter.println(String.format("   HEAP_TRACK_INTERVAL=%d (%.1f mins)", Long.valueOf((long) HEAP_TRACK_INTERVAL), Float.valueOf(1.0f)));
        printWriter.println(String.format("   HEAP_TRACK_HISTORY_LEN=%d (%.1f hr total)", 720, Float.valueOf(12.0f)));
        printWriter.println("GarbageMonitor tracked processes:");
        Iterator<Long> it = this.mPids.iterator();
        while (it.hasNext()) {
            ProcessMemInfo processMemInfo = this.mData.get(it.next().longValue());
            if (processMemInfo != null) {
                processMemInfo.dump(printWriter, strArr);
            }
        }
    }

    private static class MemoryIconDrawable extends Drawable {
        final Drawable baseIcon;

        /* renamed from: dp */
        final float f399dp;
        long limit;
        final Paint paint;
        long rss;

        public int getOpacity() {
            return -3;
        }

        MemoryIconDrawable(Context context) {
            Paint paint2 = new Paint();
            this.paint = paint2;
            this.baseIcon = context.getDrawable(C1894R.C1896drawable.ic_memory).mutate();
            this.f399dp = context.getResources().getDisplayMetrics().density;
            paint2.setColor(QSIconViewImpl.getIconColorForState(context, 2));
        }

        public void setRss(long j) {
            if (j != this.rss) {
                this.rss = j;
                invalidateSelf();
            }
        }

        public void setLimit(long j) {
            if (j != this.limit) {
                this.limit = j;
                invalidateSelf();
            }
        }

        public void draw(Canvas canvas) {
            this.baseIcon.draw(canvas);
            long j = this.limit;
            if (j > 0) {
                long j2 = this.rss;
                if (j2 > 0) {
                    float min = Math.min(1.0f, ((float) j2) / ((float) j));
                    Rect bounds = getBounds();
                    canvas.translate(((float) bounds.left) + (this.f399dp * 8.0f), ((float) bounds.top) + (this.f399dp * 5.0f));
                    float f = this.f399dp;
                    canvas.drawRect(0.0f, f * 14.0f * (1.0f - min), (8.0f * f) + 1.0f, (f * 14.0f) + 1.0f, this.paint);
                }
            }
        }

        public void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4);
            this.baseIcon.setBounds(i, i2, i3, i4);
        }

        public int getIntrinsicHeight() {
            return this.baseIcon.getIntrinsicHeight();
        }

        public int getIntrinsicWidth() {
            return this.baseIcon.getIntrinsicWidth();
        }

        public void setAlpha(int i) {
            this.baseIcon.setAlpha(i);
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.baseIcon.setColorFilter(colorFilter);
            this.paint.setColorFilter(colorFilter);
        }

        public void setTint(int i) {
            super.setTint(i);
            this.baseIcon.setTint(i);
        }

        public void setTintList(ColorStateList colorStateList) {
            super.setTintList(colorStateList);
            this.baseIcon.setTintList(colorStateList);
        }

        public void setTintMode(PorterDuff.Mode mode) {
            super.setTintMode(mode);
            this.baseIcon.setTintMode(mode);
        }
    }

    private static class MemoryGraphIcon extends QSTile.Icon {
        long limit;
        long rss;

        private MemoryGraphIcon() {
        }

        public void setRss(long j) {
            this.rss = j;
        }

        public void setHeapLimit(long j) {
            this.limit = j;
        }

        public Drawable getDrawable(Context context) {
            MemoryIconDrawable memoryIconDrawable = new MemoryIconDrawable(context);
            memoryIconDrawable.setRss(this.rss);
            memoryIconDrawable.setLimit(this.limit);
            return memoryIconDrawable;
        }
    }

    public static class MemoryTile extends QSTileImpl<QSTile.State> {
        public static final String TILE_SPEC = "dbg:mem";
        /* access modifiers changed from: private */
        public boolean dumpInProgress;
        /* access modifiers changed from: private */

        /* renamed from: gm */
        public final GarbageMonitor f400gm;
        private ProcessMemInfo pmi;

        public int getMetricsCategory() {
            return 0;
        }

        @Inject
        public MemoryTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, GarbageMonitor garbageMonitor) {
            super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
            this.f400gm = garbageMonitor;
        }

        public QSTile.State newTileState() {
            return new QSTile.State();
        }

        public Intent getLongClickIntent() {
            return new Intent();
        }

        /* access modifiers changed from: protected */
        public void handleClick(View view) {
            if (!this.dumpInProgress) {
                this.dumpInProgress = true;
                refreshState();
                new Thread("HeapDumpThread") {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException unused) {
                        }
                        MemoryTile.this.mHandler.post(new GarbageMonitor$MemoryTile$1$$ExternalSyntheticLambda0(this, MemoryTile.this.f400gm.dumpHprofAndGetShareIntent()));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$run$0$com-android-systemui-util-leak-GarbageMonitor$MemoryTile$1 */
                    public /* synthetic */ void mo47024x51b69d46(Intent intent) {
                        boolean unused = MemoryTile.this.dumpInProgress = false;
                        MemoryTile.this.refreshState();
                        MemoryTile.this.getHost().collapsePanels();
                        MemoryTile.this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
                    }
                }.start();
            }
        }

        public void handleSetListening(boolean z) {
            super.handleSetListening(z);
            GarbageMonitor garbageMonitor = this.f400gm;
            if (garbageMonitor != null) {
                garbageMonitor.setTile(z ? this : null);
            }
            ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService(ActivityManager.class);
            if (!z || this.f400gm.mHeapLimit <= 0) {
                activityManager.clearWatchHeapLimit();
            } else {
                activityManager.setWatchHeapLimit(this.f400gm.mHeapLimit * 1024);
            }
        }

        public CharSequence getTileLabel() {
            return getState().label;
        }

        /* access modifiers changed from: protected */
        public void handleUpdateState(QSTile.State state, Object obj) {
            String str;
            this.pmi = this.f400gm.getMemInfo(Process.myPid());
            MemoryGraphIcon memoryGraphIcon = new MemoryGraphIcon();
            memoryGraphIcon.setHeapLimit(this.f400gm.mHeapLimit);
            state.state = this.dumpInProgress ? 0 : 2;
            if (this.dumpInProgress) {
                str = "Dumping...";
            } else {
                str = this.mContext.getString(C1894R.string.heap_dump_tile_name);
            }
            state.label = str;
            ProcessMemInfo processMemInfo = this.pmi;
            if (processMemInfo != null) {
                memoryGraphIcon.setRss(processMemInfo.currentRss);
                state.secondaryLabel = String.format("rss: %s / %s", GarbageMonitor.formatBytes(this.pmi.currentRss * 1024), GarbageMonitor.formatBytes(this.f400gm.mHeapLimit * 1024));
            } else {
                memoryGraphIcon.setRss(0);
                state.secondaryLabel = null;
            }
            state.icon = memoryGraphIcon;
        }

        public void update() {
            refreshState();
        }

        public long getRss() {
            ProcessMemInfo processMemInfo = this.pmi;
            if (processMemInfo != null) {
                return processMemInfo.currentRss;
            }
            return 0;
        }

        public long getHeapLimit() {
            GarbageMonitor garbageMonitor = this.f400gm;
            if (garbageMonitor != null) {
                return garbageMonitor.mHeapLimit;
            }
            return 0;
        }
    }

    public static class ProcessMemInfo implements Dumpable {
        public long currentRss;
        public int head = 0;
        public long max = 1;
        public String name;
        public long pid;
        public long[] rss = new long[720];
        public long startTime;

        public ProcessMemInfo(long j, String str, long j2) {
            this.pid = j;
            this.name = str;
            this.startTime = j2;
        }

        public long getUptime() {
            return System.currentTimeMillis() - this.startTime;
        }

        public void dump(PrintWriter printWriter, String[] strArr) {
            printWriter.print("{ \"pid\": ");
            printWriter.print(this.pid);
            printWriter.print(", \"name\": \"");
            printWriter.print(this.name.replace((char) Typography.quote, '-'));
            printWriter.print("\", \"start\": ");
            printWriter.print(this.startTime);
            printWriter.print(", \"rss\": [");
            for (int i = 0; i < this.rss.length; i++) {
                if (i > 0) {
                    printWriter.print(NavigationBarInflaterView.BUTTON_SEPARATOR);
                }
                long[] jArr = this.rss;
                printWriter.print(jArr[(this.head + i) % jArr.length]);
            }
            printWriter.println("] }");
        }
    }

    @SysUISingleton
    public static class Service extends CoreStartable implements Dumpable {
        private final GarbageMonitor mGarbageMonitor;

        @Inject
        public Service(Context context, GarbageMonitor garbageMonitor) {
            super(context);
            this.mGarbageMonitor = garbageMonitor;
        }

        public void start() {
            boolean z = false;
            if (Settings.Secure.getInt(this.mContext.getContentResolver(), GarbageMonitor.FORCE_ENABLE_LEAK_REPORTING, 0) != 0) {
                z = true;
            }
            if (GarbageMonitor.LEAK_REPORTING_ENABLED || z) {
                this.mGarbageMonitor.startLeakMonitor();
            }
            if (GarbageMonitor.HEAP_TRACKING_ENABLED || z) {
                this.mGarbageMonitor.startHeapTracking();
            }
        }

        public void dump(PrintWriter printWriter, String[] strArr) {
            GarbageMonitor garbageMonitor = this.mGarbageMonitor;
            if (garbageMonitor != null) {
                garbageMonitor.dump(printWriter, strArr);
            }
        }
    }

    /* access modifiers changed from: private */
    public void doGarbageInspection(int i) {
        if (gcAndCheckGarbage()) {
            this.mDelayableExecutor.executeDelayed(new GarbageMonitor$$ExternalSyntheticLambda0(this), 100);
        }
        this.mMessageRouter.cancelMessages(1000);
        this.mMessageRouter.sendMessageDelayed(1000, (long) GARBAGE_INSPECTION_INTERVAL);
    }

    /* access modifiers changed from: private */
    public void doHeapTrack(int i) {
        update();
        this.mMessageRouter.cancelMessages(3000);
        this.mMessageRouter.sendMessageDelayed(3000, (long) HEAP_TRACK_INTERVAL);
    }
}
