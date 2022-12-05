package com.android.systemui.log;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.constraintlayout.widget.R$styleable;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: LogcatEchoTrackerDebug.kt */
/* loaded from: classes.dex */
public final class LogcatEchoTrackerDebug implements LogcatEchoTracker {
    @NotNull
    public static final Factory Factory = new Factory(null);
    @NotNull
    private final Map<String, LogLevel> cachedBufferLevels;
    @NotNull
    private final Map<String, LogLevel> cachedTagLevels;
    @NotNull
    private final ContentResolver contentResolver;

    public /* synthetic */ LogcatEchoTrackerDebug(ContentResolver contentResolver, DefaultConstructorMarker defaultConstructorMarker) {
        this(contentResolver);
    }

    @NotNull
    public static final LogcatEchoTrackerDebug create(@NotNull ContentResolver contentResolver, @NotNull Looper looper) {
        return Factory.create(contentResolver, looper);
    }

    private LogcatEchoTrackerDebug(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.cachedBufferLevels = new LinkedHashMap();
        this.cachedTagLevels = new LinkedHashMap();
    }

    /* compiled from: LogcatEchoTrackerDebug.kt */
    /* loaded from: classes.dex */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Factory() {
        }

        @NotNull
        public final LogcatEchoTrackerDebug create(@NotNull ContentResolver contentResolver, @NotNull Looper mainLooper) {
            Intrinsics.checkNotNullParameter(contentResolver, "contentResolver");
            Intrinsics.checkNotNullParameter(mainLooper, "mainLooper");
            LogcatEchoTrackerDebug logcatEchoTrackerDebug = new LogcatEchoTrackerDebug(contentResolver, null);
            logcatEchoTrackerDebug.attach(mainLooper);
            return logcatEchoTrackerDebug;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void attach(Looper looper) {
        ContentResolver contentResolver = this.contentResolver;
        Uri uriFor = Settings.Global.getUriFor("systemui/buffer");
        final Handler handler = new Handler(looper);
        contentResolver.registerContentObserver(uriFor, true, new ContentObserver(handler) { // from class: com.android.systemui.log.LogcatEchoTrackerDebug$attach$1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, @NotNull Uri uri) {
                Map map;
                Intrinsics.checkNotNullParameter(uri, "uri");
                super.onChange(z, uri);
                map = LogcatEchoTrackerDebug.this.cachedBufferLevels;
                map.clear();
            }
        });
        ContentResolver contentResolver2 = this.contentResolver;
        Uri uriFor2 = Settings.Global.getUriFor("systemui/tag");
        final Handler handler2 = new Handler(looper);
        contentResolver2.registerContentObserver(uriFor2, true, new ContentObserver(handler2) { // from class: com.android.systemui.log.LogcatEchoTrackerDebug$attach$2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, @NotNull Uri uri) {
                Map map;
                Intrinsics.checkNotNullParameter(uri, "uri");
                super.onChange(z, uri);
                map = LogcatEchoTrackerDebug.this.cachedTagLevels;
                map.clear();
            }
        });
    }

    @Override // com.android.systemui.log.LogcatEchoTracker
    public synchronized boolean isBufferLoggable(@NotNull String bufferName, @NotNull LogLevel level) {
        Intrinsics.checkNotNullParameter(bufferName, "bufferName");
        Intrinsics.checkNotNullParameter(level, "level");
        return level.ordinal() >= getLogLevel(bufferName, "systemui/buffer", this.cachedBufferLevels).ordinal();
    }

    @Override // com.android.systemui.log.LogcatEchoTracker
    public synchronized boolean isTagLoggable(@NotNull String tagName, @NotNull LogLevel level) {
        Intrinsics.checkNotNullParameter(tagName, "tagName");
        Intrinsics.checkNotNullParameter(level, "level");
        return level.compareTo(getLogLevel(tagName, "systemui/tag", this.cachedTagLevels)) >= 0;
    }

    private final LogLevel getLogLevel(String str, String str2, Map<String, LogLevel> map) {
        LogLevel logLevel = map.get(str);
        if (logLevel == null) {
            LogLevel readSetting = readSetting(str2 + '/' + str);
            map.put(str, readSetting);
            return readSetting;
        }
        return logLevel;
    }

    private final LogLevel readSetting(String str) {
        LogLevel logLevel;
        try {
            return parseProp(Settings.Global.getString(this.contentResolver, str));
        } catch (Settings.SettingNotFoundException unused) {
            logLevel = LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
            return logLevel;
        }
    }

    private final LogLevel parseProp(String str) {
        String lowerCase;
        LogLevel logLevel;
        if (str == null) {
            lowerCase = null;
        } else {
            lowerCase = str.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase()");
        }
        if (lowerCase != null) {
            switch (lowerCase.hashCode()) {
                case -1408208058:
                    if (lowerCase.equals("assert")) {
                        return LogLevel.WTF;
                    }
                    break;
                case R$styleable.Constraint_layout_goneMarginLeft /* 100 */:
                    if (lowerCase.equals("d")) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case R$styleable.Constraint_layout_goneMarginRight /* 101 */:
                    if (lowerCase.equals("e")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case R$styleable.Constraint_pathMotionArc /* 105 */:
                    if (lowerCase.equals("i")) {
                        return LogLevel.INFO;
                    }
                    break;
                case androidx.appcompat.R$styleable.AppCompatTheme_windowActionBarOverlay /* 118 */:
                    if (lowerCase.equals("v")) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case androidx.appcompat.R$styleable.AppCompatTheme_windowActionModeOverlay /* 119 */:
                    if (lowerCase.equals("w")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 118057:
                    if (lowerCase.equals("wtf")) {
                        return LogLevel.WTF;
                    }
                    break;
                case 3237038:
                    if (lowerCase.equals("info")) {
                        return LogLevel.INFO;
                    }
                    break;
                case 3641990:
                    if (lowerCase.equals("warn")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 95458899:
                    if (lowerCase.equals("debug")) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case 96784904:
                    if (lowerCase.equals("error")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case 351107458:
                    if (lowerCase.equals("verbose")) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case 1124446108:
                    if (lowerCase.equals("warning")) {
                        return LogLevel.WARNING;
                    }
                    break;
            }
        }
        logLevel = LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
        return logLevel;
    }
}
