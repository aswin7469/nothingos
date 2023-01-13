package com.android.systemui.log;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\bE\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\b\u0018\u0000 b2\u00020\u0001:\u0001bB\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0017\u0010\b\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\t¢\u0006\u0002\b\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u000f\u0012\u0006\u0010\u0011\u001a\u00020\u0007\u0012\u0006\u0010\u0012\u001a\u00020\u0007\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0016\u0012\u0006\u0010\u0018\u001a\u00020\u0016\u0012\u0006\u0010\u0019\u001a\u00020\u0016¢\u0006\u0002\u0010\u001aJ\t\u0010I\u001a\u00020\u0003HÆ\u0003J\t\u0010J\u001a\u00020\u0007HÆ\u0003J\t\u0010K\u001a\u00020\u0007HÆ\u0003J\t\u0010L\u001a\u00020\u0014HÆ\u0003J\t\u0010M\u001a\u00020\u0016HÆ\u0003J\t\u0010N\u001a\u00020\u0016HÆ\u0003J\t\u0010O\u001a\u00020\u0016HÆ\u0003J\t\u0010P\u001a\u00020\u0016HÆ\u0003J\t\u0010Q\u001a\u00020\u0005HÆ\u0003J\t\u0010R\u001a\u00020\u0007HÆ\u0003J\u001a\u0010S\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\t¢\u0006\u0002\b\nHÆ\u0003J\u000b\u0010T\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010U\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010V\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010W\u001a\u00020\u000fHÆ\u0003J\t\u0010X\u001a\u00020\u000fHÆ\u0003JÀ\u0001\u0010Y\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0019\b\u0002\u0010\b\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\t¢\u0006\u0002\b\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u00072\b\b\u0002\u0010\u0012\u001a\u00020\u00072\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00162\b\b\u0002\u0010\u0018\u001a\u00020\u00162\b\b\u0002\u0010\u0019\u001a\u00020\u0016HÆ\u0001J\u0013\u0010Z\u001a\u00020\u00162\b\u0010[\u001a\u0004\u0018\u00010\\HÖ\u0003J\t\u0010]\u001a\u00020\u000fHÖ\u0001J7\u0010^\u001a\u00020_2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0017\u0010`\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\t¢\u0006\u0002\b\nJ\t\u0010a\u001a\u00020\u0005HÖ\u0001R\u001a\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u0017\u001a\u00020\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001c\"\u0004\b \u0010\u001eR\u001a\u0010\u0018\u001a\u00020\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u001c\"\u0004\b\"\u0010\u001eR\u001a\u0010\u0019\u001a\u00020\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001c\"\u0004\b$\u0010\u001eR\u001a\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u001a\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u001a\u0010\u0010\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010*\"\u0004\b.\u0010,R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u001a\u0010\u0011\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001a\u0010\u0012\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u00104\"\u0004\b8\u00106R+\u0010\b\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\t¢\u0006\u0002\b\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b9\u0010:\"\u0004\b;\u0010<R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bA\u0010>\"\u0004\bB\u0010@R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010>\"\u0004\bD\u0010@R\u001a\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bE\u0010>\"\u0004\bF\u0010@R\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bG\u00104\"\u0004\bH\u00106¨\u0006c"}, mo65043d2 = {"Lcom/android/systemui/log/LogMessageImpl;", "Lcom/android/systemui/log/LogMessage;", "level", "Lcom/android/systemui/log/LogLevel;", "tag", "", "timestamp", "", "printer", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "str1", "str2", "str3", "int1", "", "int2", "long1", "long2", "double1", "", "bool1", "", "bool2", "bool3", "bool4", "(Lcom/android/systemui/log/LogLevel;Ljava/lang/String;JLkotlin/jvm/functions/Function1;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IIJJDZZZZ)V", "getBool1", "()Z", "setBool1", "(Z)V", "getBool2", "setBool2", "getBool3", "setBool3", "getBool4", "setBool4", "getDouble1", "()D", "setDouble1", "(D)V", "getInt1", "()I", "setInt1", "(I)V", "getInt2", "setInt2", "getLevel", "()Lcom/android/systemui/log/LogLevel;", "setLevel", "(Lcom/android/systemui/log/LogLevel;)V", "getLong1", "()J", "setLong1", "(J)V", "getLong2", "setLong2", "getPrinter", "()Lkotlin/jvm/functions/Function1;", "setPrinter", "(Lkotlin/jvm/functions/Function1;)V", "getStr1", "()Ljava/lang/String;", "setStr1", "(Ljava/lang/String;)V", "getStr2", "setStr2", "getStr3", "setStr3", "getTag", "setTag", "getTimestamp", "setTimestamp", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "", "hashCode", "reset", "", "renderer", "toString", "Factory", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LogMessageImpl.kt */
public final class LogMessageImpl implements LogMessage {
    public static final Factory Factory = new Factory((DefaultConstructorMarker) null);
    private boolean bool1;
    private boolean bool2;
    private boolean bool3;
    private boolean bool4;
    private double double1;
    private int int1;
    private int int2;
    private LogLevel level;
    private long long1;
    private long long2;
    private Function1<? super LogMessage, String> printer;
    private String str1;
    private String str2;
    private String str3;
    private String tag;
    private long timestamp;

    public static /* synthetic */ LogMessageImpl copy$default(LogMessageImpl logMessageImpl, LogLevel logLevel, String str, long j, Function1 function1, String str4, String str5, String str6, int i, int i2, long j2, long j3, double d, boolean z, boolean z2, boolean z3, boolean z4, int i3, Object obj) {
        int i4 = i3;
        return logMessageImpl.copy((i4 & 1) != 0 ? logMessageImpl.getLevel() : logLevel, (i4 & 2) != 0 ? logMessageImpl.getTag() : str, (i4 & 4) != 0 ? logMessageImpl.getTimestamp() : j, (i4 & 8) != 0 ? logMessageImpl.getPrinter() : function1, (i4 & 16) != 0 ? logMessageImpl.getStr1() : str4, (i4 & 32) != 0 ? logMessageImpl.getStr2() : str5, (i4 & 64) != 0 ? logMessageImpl.getStr3() : str6, (i4 & 128) != 0 ? logMessageImpl.getInt1() : i, (i4 & 256) != 0 ? logMessageImpl.getInt2() : i2, (i4 & 512) != 0 ? logMessageImpl.getLong1() : j2, (i4 & 1024) != 0 ? logMessageImpl.getLong2() : j3, (i4 & 2048) != 0 ? logMessageImpl.getDouble1() : d, (i4 & 4096) != 0 ? logMessageImpl.getBool1() : z, (i4 & 8192) != 0 ? logMessageImpl.getBool2() : z2, (i4 & 16384) != 0 ? logMessageImpl.getBool3() : z3, (i4 & 32768) != 0 ? logMessageImpl.getBool4() : z4);
    }

    public final LogLevel component1() {
        return getLevel();
    }

    public final long component10() {
        return getLong1();
    }

    public final long component11() {
        return getLong2();
    }

    public final double component12() {
        return getDouble1();
    }

    public final boolean component13() {
        return getBool1();
    }

    public final boolean component14() {
        return getBool2();
    }

    public final boolean component15() {
        return getBool3();
    }

    public final boolean component16() {
        return getBool4();
    }

    public final String component2() {
        return getTag();
    }

    public final long component3() {
        return getTimestamp();
    }

    public final Function1<LogMessage, String> component4() {
        return getPrinter();
    }

    public final String component5() {
        return getStr1();
    }

    public final String component6() {
        return getStr2();
    }

    public final String component7() {
        return getStr3();
    }

    public final int component8() {
        return getInt1();
    }

    public final int component9() {
        return getInt2();
    }

    public final LogMessageImpl copy(LogLevel logLevel, String str, long j, Function1<? super LogMessage, String> function1, String str4, String str5, String str6, int i, int i2, long j2, long j3, double d, boolean z, boolean z2, boolean z3, boolean z4) {
        LogLevel logLevel2 = logLevel;
        Intrinsics.checkNotNullParameter(logLevel2, "level");
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(function1, "printer");
        return new LogMessageImpl(logLevel2, str, j, function1, str4, str5, str6, i, i2, j2, j3, d, z, z2, z3, z4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LogMessageImpl)) {
            return false;
        }
        LogMessageImpl logMessageImpl = (LogMessageImpl) obj;
        return getLevel() == logMessageImpl.getLevel() && Intrinsics.areEqual((Object) getTag(), (Object) logMessageImpl.getTag()) && getTimestamp() == logMessageImpl.getTimestamp() && Intrinsics.areEqual((Object) getPrinter(), (Object) logMessageImpl.getPrinter()) && Intrinsics.areEqual((Object) getStr1(), (Object) logMessageImpl.getStr1()) && Intrinsics.areEqual((Object) getStr2(), (Object) logMessageImpl.getStr2()) && Intrinsics.areEqual((Object) getStr3(), (Object) logMessageImpl.getStr3()) && getInt1() == logMessageImpl.getInt1() && getInt2() == logMessageImpl.getInt2() && getLong1() == logMessageImpl.getLong1() && getLong2() == logMessageImpl.getLong2() && Intrinsics.areEqual((Object) Double.valueOf(getDouble1()), (Object) Double.valueOf(logMessageImpl.getDouble1())) && getBool1() == logMessageImpl.getBool1() && getBool2() == logMessageImpl.getBool2() && getBool3() == logMessageImpl.getBool3() && getBool4() == logMessageImpl.getBool4();
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((((((getLevel().hashCode() * 31) + getTag().hashCode()) * 31) + Long.hashCode(getTimestamp())) * 31) + getPrinter().hashCode()) * 31) + (getStr1() == null ? 0 : getStr1().hashCode())) * 31) + (getStr2() == null ? 0 : getStr2().hashCode())) * 31;
        if (getStr3() != null) {
            i = getStr3().hashCode();
        }
        int hashCode2 = (((((((((((hashCode + i) * 31) + Integer.hashCode(getInt1())) * 31) + Integer.hashCode(getInt2())) * 31) + Long.hashCode(getLong1())) * 31) + Long.hashCode(getLong2())) * 31) + Double.hashCode(getDouble1())) * 31;
        boolean bool12 = getBool1();
        boolean z = true;
        if (bool12) {
            bool12 = true;
        }
        int i2 = (hashCode2 + (bool12 ? 1 : 0)) * 31;
        boolean bool22 = getBool2();
        if (bool22) {
            bool22 = true;
        }
        int i3 = (i2 + (bool22 ? 1 : 0)) * 31;
        boolean bool32 = getBool3();
        if (bool32) {
            bool32 = true;
        }
        int i4 = (i3 + (bool32 ? 1 : 0)) * 31;
        boolean bool42 = getBool4();
        if (!bool42) {
            z = bool42;
        }
        return i4 + (z ? 1 : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("LogMessageImpl(level=");
        sb.append((Object) getLevel()).append(", tag=").append(getTag()).append(", timestamp=").append(getTimestamp()).append(", printer=").append((Object) getPrinter()).append(", str1=").append(getStr1()).append(", str2=").append(getStr2()).append(", str3=").append(getStr3()).append(", int1=").append(getInt1()).append(", int2=").append(getInt2()).append(", long1=").append(getLong1()).append(", long2=").append(getLong2()).append(", double1=");
        sb.append(getDouble1()).append(", bool1=").append(getBool1()).append(", bool2=").append(getBool2()).append(", bool3=").append(getBool3()).append(", bool4=").append(getBool4()).append(')');
        return sb.toString();
    }

    public LogMessageImpl(LogLevel logLevel, String str, long j, Function1<? super LogMessage, String> function1, String str4, String str5, String str6, int i, int i2, long j2, long j3, double d, boolean z, boolean z2, boolean z3, boolean z4) {
        Intrinsics.checkNotNullParameter(logLevel, "level");
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(function1, "printer");
        this.level = logLevel;
        this.tag = str;
        this.timestamp = j;
        this.printer = function1;
        this.str1 = str4;
        this.str2 = str5;
        this.str3 = str6;
        this.int1 = i;
        this.int2 = i2;
        this.long1 = j2;
        this.long2 = j3;
        this.double1 = d;
        this.bool1 = z;
        this.bool2 = z2;
        this.bool3 = z3;
        this.bool4 = z4;
    }

    public LogLevel getLevel() {
        return this.level;
    }

    public void setLevel(LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(logLevel, "<set-?>");
        this.level = logLevel;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.tag = str;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    public Function1<LogMessage, String> getPrinter() {
        return this.printer;
    }

    public void setPrinter(Function1<? super LogMessage, String> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.printer = function1;
    }

    public String getStr1() {
        return this.str1;
    }

    public void setStr1(String str) {
        this.str1 = str;
    }

    public String getStr2() {
        return this.str2;
    }

    public void setStr2(String str) {
        this.str2 = str;
    }

    public String getStr3() {
        return this.str3;
    }

    public void setStr3(String str) {
        this.str3 = str;
    }

    public int getInt1() {
        return this.int1;
    }

    public void setInt1(int i) {
        this.int1 = i;
    }

    public int getInt2() {
        return this.int2;
    }

    public void setInt2(int i) {
        this.int2 = i;
    }

    public long getLong1() {
        return this.long1;
    }

    public void setLong1(long j) {
        this.long1 = j;
    }

    public long getLong2() {
        return this.long2;
    }

    public void setLong2(long j) {
        this.long2 = j;
    }

    public double getDouble1() {
        return this.double1;
    }

    public void setDouble1(double d) {
        this.double1 = d;
    }

    public boolean getBool1() {
        return this.bool1;
    }

    public void setBool1(boolean z) {
        this.bool1 = z;
    }

    public boolean getBool2() {
        return this.bool2;
    }

    public void setBool2(boolean z) {
        this.bool2 = z;
    }

    public boolean getBool3() {
        return this.bool3;
    }

    public void setBool3(boolean z) {
        this.bool3 = z;
    }

    public boolean getBool4() {
        return this.bool4;
    }

    public void setBool4(boolean z) {
        this.bool4 = z;
    }

    public final void reset(String str, LogLevel logLevel, long j, Function1<? super LogMessage, String> function1) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        Intrinsics.checkNotNullParameter(function1, "renderer");
        setLevel(logLevel);
        setTag(str);
        setTimestamp(j);
        setPrinter(function1);
        setStr1((String) null);
        setStr2((String) null);
        setStr3((String) null);
        setInt1(0);
        setInt2(0);
        setLong1(0);
        setLong2(0);
        setDouble1(0.0d);
        setBool1(false);
        setBool2(false);
        setBool3(false);
        setBool4(false);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/log/LogMessageImpl$Factory;", "", "()V", "create", "Lcom/android/systemui/log/LogMessageImpl;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: LogMessageImpl.kt */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Factory() {
        }

        public final LogMessageImpl create() {
            return new LogMessageImpl(LogLevel.DEBUG, "UnknownTag", 0, LogMessageImplKt.DEFAULT_RENDERER, (String) null, (String) null, (String) null, 0, 0, 0, 0, 0.0d, false, false, false, false);
        }
    }
}
