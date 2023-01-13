package com.android.systemui.dump;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u0001B!\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006¢\u0006\u0002\u0010\u0007R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001c\u0010\r\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\n\"\u0004\b\u000f\u0010\fR\u001a\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0019\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\n\n\u0002\u0010\u001a\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 ¨\u0006!"}, mo65043d2 = {"Lcom/android/systemui/dump/ParsedArgs;", "", "rawArgs", "", "", "nonFlagArgs", "", "([Ljava/lang/String;Ljava/util/List;)V", "command", "getCommand", "()Ljava/lang/String;", "setCommand", "(Ljava/lang/String;)V", "dumpPriority", "getDumpPriority", "setDumpPriority", "listOnly", "", "getListOnly", "()Z", "setListOnly", "(Z)V", "getNonFlagArgs", "()Ljava/util/List;", "getRawArgs", "()[Ljava/lang/String;", "[Ljava/lang/String;", "tailLength", "", "getTailLength", "()I", "setTailLength", "(I)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpHandler.kt */
final class ParsedArgs {
    private String command;
    private String dumpPriority;
    private boolean listOnly;
    private final List<String> nonFlagArgs;
    private final String[] rawArgs;
    private int tailLength;

    public ParsedArgs(String[] strArr, List<String> list) {
        Intrinsics.checkNotNullParameter(strArr, "rawArgs");
        Intrinsics.checkNotNullParameter(list, "nonFlagArgs");
        this.rawArgs = strArr;
        this.nonFlagArgs = list;
    }

    public final String[] getRawArgs() {
        return this.rawArgs;
    }

    public final List<String> getNonFlagArgs() {
        return this.nonFlagArgs;
    }

    public final String getDumpPriority() {
        return this.dumpPriority;
    }

    public final void setDumpPriority(String str) {
        this.dumpPriority = str;
    }

    public final int getTailLength() {
        return this.tailLength;
    }

    public final void setTailLength(int i) {
        this.tailLength = i;
    }

    public final String getCommand() {
        return this.command;
    }

    public final void setCommand(String str) {
        this.command = str;
    }

    public final boolean getListOnly() {
        return this.listOnly;
    }

    public final void setListOnly(boolean z) {
        this.listOnly = z;
    }
}
