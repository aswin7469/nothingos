package java.util.zip;

import android.app.StatsManager;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class ZipUtils {
    private static final long WINDOWS_EPOCH_IN_MICROSECONDS = -11644473600000000L;

    ZipUtils() {
    }

    public static final FileTime winTimeToFileTime(long j) {
        return FileTime.from((j / 10) + WINDOWS_EPOCH_IN_MICROSECONDS, TimeUnit.MICROSECONDS);
    }

    public static final long fileTimeToWinTime(FileTime fileTime) {
        return (fileTime.mo61354to(TimeUnit.MICROSECONDS) - WINDOWS_EPOCH_IN_MICROSECONDS) * 10;
    }

    public static final FileTime unixTimeToFileTime(long j) {
        return FileTime.from(j, TimeUnit.SECONDS);
    }

    public static final long fileTimeToUnixTime(FileTime fileTime) {
        return fileTime.mo61354to(TimeUnit.SECONDS);
    }

    private static long dosToJavaTime(long j) {
        return new Date((int) (((j >> 25) & 127) + 80), (int) (((j >> 21) & 15) - 1), (int) ((j >> 16) & 31), (int) ((j >> 11) & 31), (int) ((j >> 5) & 63), (int) ((j << 1) & 62)).getTime();
    }

    public static long extendedDosToJavaTime(long j) {
        return dosToJavaTime(j) + (j >> 32);
    }

    private static long javaToDosTime(long j) {
        Date date = new Date(j);
        int year = date.getYear() + 1900;
        if (year < 1980) {
            return 2162688;
        }
        return ((long) (((year - 1980) << 25) | ((date.getMonth() + 1) << 21) | (date.getDate() << 16) | (date.getHours() << 11) | (date.getMinutes() << 5) | (date.getSeconds() >> 1))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }

    public static long javaToExtendedDosTime(long j) {
        if (j < 0) {
            return 2162688;
        }
        long javaToDosTime = javaToDosTime(j);
        if (javaToDosTime != 2162688) {
            return javaToDosTime + ((j % StatsManager.DEFAULT_TIMEOUT_MILLIS) << 32);
        }
        return 2162688;
    }

    public static final int get16(byte[] bArr, int i) {
        return (Byte.toUnsignedInt(bArr[i + 1]) << 8) | Byte.toUnsignedInt(bArr[i]);
    }

    public static final long get32(byte[] bArr, int i) {
        return ((((long) get16(bArr, i + 2)) << 16) | ((long) get16(bArr, i))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }

    public static final long get64(byte[] bArr, int i) {
        return (get32(bArr, i + 4) << 32) | get32(bArr, i);
    }
}
