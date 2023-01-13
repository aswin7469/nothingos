package com.android.systemui.log;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;

@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0012\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\t\u0010\u0005\"\u0004\b\n\u0010\u0007R\u0018\u0010\u000b\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\f\u0010\u0005\"\u0004\b\r\u0010\u0007R\u0018\u0010\u000e\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u000f\u0010\u0005\"\u0004\b\u0010\u0010\u0007R\u0018\u0010\u0011\u001a\u00020\u0012X¦\u000e¢\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0018\u0010\u0017\u001a\u00020\u0018X¦\u000e¢\u0006\f\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0018\u0010\u001d\u001a\u00020\u0018X¦\u000e¢\u0006\f\u001a\u0004\b\u001e\u0010\u001a\"\u0004\b\u001f\u0010\u001cR\u0012\u0010 \u001a\u00020!X¦\u0004¢\u0006\u0006\u001a\u0004\b\"\u0010#R\u0018\u0010$\u001a\u00020%X¦\u000e¢\u0006\f\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\u0018\u0010*\u001a\u00020%X¦\u000e¢\u0006\f\u001a\u0004\b+\u0010'\"\u0004\b,\u0010)R#\u0010-\u001a\u0013\u0012\u0004\u0012\u00020\u0000\u0012\u0004\u0012\u00020/0.¢\u0006\u0002\b0X¦\u0004¢\u0006\u0006\u001a\u0004\b1\u00102R\u001a\u00103\u001a\u0004\u0018\u00010/X¦\u000e¢\u0006\f\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u001a\u00108\u001a\u0004\u0018\u00010/X¦\u000e¢\u0006\f\u001a\u0004\b9\u00105\"\u0004\b:\u00107R\u001a\u0010;\u001a\u0004\u0018\u00010/X¦\u000e¢\u0006\f\u001a\u0004\b<\u00105\"\u0004\b=\u00107R\u0012\u0010>\u001a\u00020/X¦\u0004¢\u0006\u0006\u001a\u0004\b?\u00105R\u0012\u0010@\u001a\u00020%X¦\u0004¢\u0006\u0006\u001a\u0004\bA\u0010'ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006BÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/log/LogMessage;", "", "bool1", "", "getBool1", "()Z", "setBool1", "(Z)V", "bool2", "getBool2", "setBool2", "bool3", "getBool3", "setBool3", "bool4", "getBool4", "setBool4", "double1", "", "getDouble1", "()D", "setDouble1", "(D)V", "int1", "", "getInt1", "()I", "setInt1", "(I)V", "int2", "getInt2", "setInt2", "level", "Lcom/android/systemui/log/LogLevel;", "getLevel", "()Lcom/android/systemui/log/LogLevel;", "long1", "", "getLong1", "()J", "setLong1", "(J)V", "long2", "getLong2", "setLong2", "printer", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "getPrinter", "()Lkotlin/jvm/functions/Function1;", "str1", "getStr1", "()Ljava/lang/String;", "setStr1", "(Ljava/lang/String;)V", "str2", "getStr2", "setStr2", "str3", "getStr3", "setStr3", "tag", "getTag", "timestamp", "getTimestamp", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LogMessage.kt */
public interface LogMessage {
    boolean getBool1();

    boolean getBool2();

    boolean getBool3();

    boolean getBool4();

    double getDouble1();

    int getInt1();

    int getInt2();

    LogLevel getLevel();

    long getLong1();

    long getLong2();

    Function1<LogMessage, String> getPrinter();

    String getStr1();

    String getStr2();

    String getStr3();

    String getTag();

    long getTimestamp();

    void setBool1(boolean z);

    void setBool2(boolean z);

    void setBool3(boolean z);

    void setBool4(boolean z);

    void setDouble1(double d);

    void setInt1(int i);

    void setInt2(int i);

    void setLong1(long j);

    void setLong2(long j);

    void setStr1(String str);

    void setStr2(String str);

    void setStr3(String str);
}
