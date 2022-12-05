package com.android.systemui.log;

import org.jetbrains.annotations.NotNull;
/* compiled from: LogLevel.kt */
/* loaded from: classes.dex */
public enum LogLevel {
    VERBOSE(2, "V"),
    DEBUG(3, "D"),
    INFO(4, "I"),
    WARNING(5, "W"),
    ERROR(6, "E"),
    WTF(7, "WTF");
    
    private final int nativeLevel;
    @NotNull
    private final String shortString;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static LogLevel[] valuesCustom() {
        LogLevel[] valuesCustom = values();
        LogLevel[] logLevelArr = new LogLevel[valuesCustom.length];
        System.arraycopy(valuesCustom, 0, logLevelArr, 0, valuesCustom.length);
        return logLevelArr;
    }

    LogLevel(int i, String str) {
        this.nativeLevel = i;
        this.shortString = str;
    }

    @NotNull
    public final String getShortString() {
        return this.shortString;
    }
}
