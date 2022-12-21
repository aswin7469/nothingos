package com.android.systemui.log;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/log/LogLevel;", "", "nativeLevel", "", "shortString", "", "(Ljava/lang/String;IILjava/lang/String;)V", "getNativeLevel", "()I", "getShortString", "()Ljava/lang/String;", "VERBOSE", "DEBUG", "INFO", "WARNING", "ERROR", "WTF", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogLevel.kt */
public enum LogLevel {
    VERBOSE(2, ExifInterface.GPS_MEASUREMENT_INTERRUPTED),
    DEBUG(3, "D"),
    INFO(4, "I"),
    WARNING(5, ExifInterface.LONGITUDE_WEST),
    ERROR(6, "E"),
    WTF(7, "WTF");
    
    private final int nativeLevel;
    private final String shortString;

    private LogLevel(int i, String str) {
        this.nativeLevel = i;
        this.shortString = str;
    }

    public final int getNativeLevel() {
        return this.nativeLevel;
    }

    public final String getShortString() {
        return this.shortString;
    }
}
