package android.util;

import android.annotation.SystemApi;
import android.os.Process;
import android.util.proto.ProtoOutputStream;
import com.android.internal.statsd.StatsdStatsLog;

public final class StatsLog {
    @SystemApi
    public static final byte ANNOTATION_ID_DEFAULT_STATE = 6;
    @SystemApi
    public static final byte ANNOTATION_ID_EXCLUSIVE_STATE = 4;
    @SystemApi
    public static final byte ANNOTATION_ID_IS_UID = 1;
    @SystemApi
    public static final byte ANNOTATION_ID_PRIMARY_FIELD = 3;
    @SystemApi
    public static final byte ANNOTATION_ID_PRIMARY_FIELD_FIRST_UID = 5;
    @SystemApi
    public static final byte ANNOTATION_ID_STATE_NESTED = 8;
    @SystemApi
    public static final byte ANNOTATION_ID_TRIGGER_STATE_RESET = 7;
    @SystemApi
    public static final byte ANNOTATION_ID_TRUNCATE_TIMESTAMP = 2;
    private static final boolean DEBUG = false;
    private static final int EXPERIMENT_IDS_FIELD_ID = 1;
    private static final String TAG = "StatsLog";

    private static native void writeImpl(byte[] bArr, int i, int i2);

    static {
        System.loadLibrary("stats_jni");
    }

    private StatsLog() {
    }

    public static boolean logStart(int i) {
        StatsdStatsLog.write(47, Process.myUid(), i, 3);
        return true;
    }

    public static boolean logStop(int i) {
        StatsdStatsLog.write(47, Process.myUid(), i, 2);
        return true;
    }

    public static boolean logEvent(int i) {
        StatsdStatsLog.write(47, Process.myUid(), i, 1);
        return true;
    }

    public static boolean logBinaryPushStateChanged(String str, long j, int i, int i2, long[] jArr) {
        ProtoOutputStream protoOutputStream = new ProtoOutputStream();
        for (long write : jArr) {
            protoOutputStream.write(2211908157441L, write);
        }
        StatsdStatsLog.write(102, str, j, (i & 1) > 0, (i & 2) > 0, (i & 4) > 0, i2, protoOutputStream.getBytes(), 0, 0, false);
        return true;
    }

    @SystemApi
    @Deprecated
    public static void writeRaw(byte[] bArr, int i) {
        writeImpl(bArr, i, 0);
    }

    @SystemApi
    public static void write(StatsEvent statsEvent) {
        writeImpl(statsEvent.getBytes(), statsEvent.getNumBytes(), statsEvent.getAtomId());
        statsEvent.release();
    }
}
