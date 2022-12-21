package com.android.systemui.log;

import android.content.ContentResolver;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J,\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006H\u0002J\u0018\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\bH\u0016J\u0018\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\bH\u0016J\u0012\u0010\u001b\u001a\u00020\b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0007H\u0002J\u0010\u0010\u001d\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0007H\u0002R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u001f"}, mo64987d2 = {"Lcom/android/systemui/log/LogcatEchoTrackerDebug;", "Lcom/android/systemui/log/LogcatEchoTracker;", "contentResolver", "Landroid/content/ContentResolver;", "(Landroid/content/ContentResolver;)V", "cachedBufferLevels", "", "", "Lcom/android/systemui/log/LogLevel;", "cachedTagLevels", "logInBackgroundThread", "", "getLogInBackgroundThread", "()Z", "attach", "", "mainLooper", "Landroid/os/Looper;", "getLogLevel", "name", "path", "cache", "isBufferLoggable", "bufferName", "level", "isTagLoggable", "tagName", "parseProp", "propValue", "readSetting", "Factory", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogcatEchoTrackerDebug.kt */
public final class LogcatEchoTrackerDebug implements LogcatEchoTracker {
    public static final Factory Factory = new Factory((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public final Map<String, LogLevel> cachedBufferLevels;
    /* access modifiers changed from: private */
    public final Map<String, LogLevel> cachedTagLevels;
    private final ContentResolver contentResolver;
    private final boolean logInBackgroundThread;

    public /* synthetic */ LogcatEchoTrackerDebug(ContentResolver contentResolver2, DefaultConstructorMarker defaultConstructorMarker) {
        this(contentResolver2);
    }

    @JvmStatic
    public static final LogcatEchoTrackerDebug create(ContentResolver contentResolver2, Looper looper) {
        return Factory.create(contentResolver2, looper);
    }

    private LogcatEchoTrackerDebug(ContentResolver contentResolver2) {
        this.contentResolver = contentResolver2;
        this.cachedBufferLevels = new LinkedHashMap();
        this.cachedTagLevels = new LinkedHashMap();
        this.logInBackgroundThread = true;
    }

    public boolean getLogInBackgroundThread() {
        return this.logInBackgroundThread;
    }

    @Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/log/LogcatEchoTrackerDebug$Factory;", "", "()V", "create", "Lcom/android/systemui/log/LogcatEchoTrackerDebug;", "contentResolver", "Landroid/content/ContentResolver;", "mainLooper", "Landroid/os/Looper;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: LogcatEchoTrackerDebug.kt */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Factory() {
        }

        @JvmStatic
        public final LogcatEchoTrackerDebug create(ContentResolver contentResolver, Looper looper) {
            Intrinsics.checkNotNullParameter(contentResolver, "contentResolver");
            Intrinsics.checkNotNullParameter(looper, "mainLooper");
            LogcatEchoTrackerDebug logcatEchoTrackerDebug = new LogcatEchoTrackerDebug(contentResolver, (DefaultConstructorMarker) null);
            logcatEchoTrackerDebug.attach(looper);
            return logcatEchoTrackerDebug;
        }
    }

    /* access modifiers changed from: private */
    public final void attach(Looper looper) {
        this.contentResolver.registerContentObserver(Settings.Global.getUriFor("systemui/buffer"), true, new LogcatEchoTrackerDebug$attach$1(this, new Handler(looper)));
        this.contentResolver.registerContentObserver(Settings.Global.getUriFor("systemui/tag"), true, new LogcatEchoTrackerDebug$attach$2(this, new Handler(looper)));
    }

    public synchronized boolean isBufferLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "bufferName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.ordinal() >= getLogLevel(str, "systemui/buffer", this.cachedBufferLevels).ordinal();
    }

    public synchronized boolean isTagLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "tagName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.compareTo(getLogLevel(str, "systemui/tag", this.cachedTagLevels)) >= 0;
    }

    private final LogLevel getLogLevel(String str, String str2, Map<String, LogLevel> map) {
        LogLevel logLevel = map.get(str);
        if (logLevel != null) {
            return logLevel;
        }
        LogLevel readSetting = readSetting(str2 + '/' + str);
        map.put(str, readSetting);
        return readSetting;
    }

    private final LogLevel readSetting(String str) {
        try {
            return parseProp(Settings.Global.getString(this.contentResolver, str));
        } catch (Settings.SettingNotFoundException unused) {
            return LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
        }
    }

    private final LogLevel parseProp(String str) {
        String str2;
        if (str != null) {
            str2 = str.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(str2, "this as java.lang.String).toLowerCase(Locale.ROOT)");
        } else {
            str2 = null;
        }
        if (str2 != null) {
            switch (str2.hashCode()) {
                case -1408208058:
                    if (str2.equals("assert")) {
                        return LogLevel.WTF;
                    }
                    break;
                case 100:
                    if (str2.equals(DateFormat.DAY)) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case 101:
                    if (str2.equals("e")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case 105:
                    if (str2.equals("i")) {
                        return LogLevel.INFO;
                    }
                    break;
                case 118:
                    if (str2.equals(DateFormat.ABBR_GENERIC_TZ)) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case 119:
                    if (str2.equals("w")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 118057:
                    if (str2.equals("wtf")) {
                        return LogLevel.WTF;
                    }
                    break;
                case 3237038:
                    if (str2.equals("info")) {
                        return LogLevel.INFO;
                    }
                    break;
                case 3641990:
                    if (str2.equals("warn")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 95458899:
                    if (str2.equals("debug")) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case 96784904:
                    if (str2.equals("error")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case 351107458:
                    if (str2.equals("verbose")) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case 1124446108:
                    if (str2.equals("warning")) {
                        return LogLevel.WARNING;
                    }
                    break;
            }
        }
        return LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
    }
}
