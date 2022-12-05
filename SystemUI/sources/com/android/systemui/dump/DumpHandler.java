package com.android.systemui.dump;

import android.content.Context;
import android.os.SystemClock;
import android.os.Trace;
import com.android.systemui.R$array;
import com.android.systemui.R$string;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class DumpHandler {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Context context;
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final LogBufferEulogizer logBufferEulogizer;

    public DumpHandler(@NotNull Context context, @NotNull DumpManager dumpManager, @NotNull LogBufferEulogizer logBufferEulogizer) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(logBufferEulogizer, "logBufferEulogizer");
        this.context = context;
        this.dumpManager = dumpManager;
        this.logBufferEulogizer = logBufferEulogizer;
    }

    public final void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        Trace.beginSection("DumpManager#dump()");
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            ParsedArgs parseArgs = parseArgs(args);
            String dumpPriority = parseArgs.getDumpPriority();
            if (Intrinsics.areEqual(dumpPriority, "CRITICAL")) {
                dumpCritical(fd, pw, parseArgs);
            } else if (Intrinsics.areEqual(dumpPriority, "NORMAL")) {
                dumpNormal(pw, parseArgs);
            } else {
                dumpParameterized(fd, pw, parseArgs);
            }
            pw.println();
            pw.println("Dump took " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms");
            Trace.endSection();
        } catch (ArgParseException e) {
            pw.println(e.getMessage());
        }
    }

    private final void dumpParameterized(FileDescriptor fileDescriptor, PrintWriter printWriter, ParsedArgs parsedArgs) {
        String command = parsedArgs.getCommand();
        if (command != null) {
            switch (command.hashCode()) {
                case -1354792126:
                    if (command.equals("config")) {
                        dumpConfig(printWriter);
                        return;
                    }
                    break;
                case -1353714459:
                    if (command.equals("dumpables")) {
                        dumpDumpables(fileDescriptor, printWriter, parsedArgs);
                        return;
                    }
                    break;
                case -1045369428:
                    if (command.equals("bugreport-normal")) {
                        dumpNormal(printWriter, parsedArgs);
                        return;
                    }
                    break;
                case 3198785:
                    if (command.equals("help")) {
                        dumpHelp(printWriter);
                        return;
                    }
                    break;
                case 227996723:
                    if (command.equals("buffers")) {
                        dumpBuffers(printWriter, parsedArgs);
                        return;
                    }
                    break;
                case 842828580:
                    if (command.equals("bugreport-critical")) {
                        dumpCritical(fileDescriptor, printWriter, parsedArgs);
                        return;
                    }
                    break;
            }
        }
        dumpTargets(parsedArgs.getNonFlagArgs(), fileDescriptor, printWriter, parsedArgs);
    }

    private final void dumpCritical(FileDescriptor fileDescriptor, PrintWriter printWriter, ParsedArgs parsedArgs) {
        this.dumpManager.dumpDumpables(fileDescriptor, printWriter, parsedArgs.getRawArgs());
        dumpConfig(printWriter);
    }

    private final void dumpNormal(PrintWriter printWriter, ParsedArgs parsedArgs) {
        this.dumpManager.dumpBuffers(printWriter, parsedArgs.getTailLength());
        this.logBufferEulogizer.readEulogyIfPresent(printWriter);
    }

    private final void dumpDumpables(FileDescriptor fileDescriptor, PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (parsedArgs.getListOnly()) {
            this.dumpManager.listDumpables(printWriter);
        } else {
            this.dumpManager.dumpDumpables(fileDescriptor, printWriter, parsedArgs.getRawArgs());
        }
    }

    private final void dumpBuffers(PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (parsedArgs.getListOnly()) {
            this.dumpManager.listBuffers(printWriter);
        } else {
            this.dumpManager.dumpBuffers(printWriter, parsedArgs.getTailLength());
        }
    }

    private final void dumpTargets(List<String> list, FileDescriptor fileDescriptor, PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (!list.isEmpty()) {
            for (String str : list) {
                this.dumpManager.dumpTarget(str, fileDescriptor, printWriter, parsedArgs.getRawArgs(), parsedArgs.getTailLength());
            }
        } else if (parsedArgs.getListOnly()) {
            printWriter.println("Dumpables:");
            this.dumpManager.listDumpables(printWriter);
            printWriter.println();
            printWriter.println("Buffers:");
            this.dumpManager.listBuffers(printWriter);
        } else {
            printWriter.println("Nothing to dump :(");
        }
    }

    private final void dumpConfig(PrintWriter printWriter) {
        printWriter.println("SystemUiServiceComponents configuration:");
        printWriter.print("vendor component: ");
        printWriter.println(this.context.getResources().getString(R$string.config_systemUIVendorServiceComponent));
        dumpServiceList(printWriter, "global", R$array.config_systemUIServiceComponents);
        dumpServiceList(printWriter, "per-user", R$array.config_systemUIServiceComponentsPerUser);
    }

    private final void dumpServiceList(PrintWriter printWriter, String str, int i) {
        String[] stringArray = this.context.getResources().getStringArray(i);
        printWriter.print(str);
        printWriter.print(": ");
        if (stringArray == null) {
            printWriter.println("N/A");
            return;
        }
        printWriter.print(stringArray.length);
        printWriter.println(" services");
        int i2 = 0;
        int length = stringArray.length - 1;
        if (length < 0) {
            return;
        }
        while (true) {
            int i3 = i2 + 1;
            printWriter.print("  ");
            printWriter.print(i2);
            printWriter.print(": ");
            printWriter.println(stringArray[i2]);
            if (i3 > length) {
                return;
            }
            i2 = i3;
        }
    }

    private final void dumpHelp(PrintWriter printWriter) {
        printWriter.println("Let <invocation> be:");
        printWriter.println("$ adb shell dumpsys activity service com.android.systemui/.SystemUIService");
        printWriter.println();
        printWriter.println("Most common usage:");
        printWriter.println("$ <invocation> <targets>");
        printWriter.println("$ <invocation> NotifLog");
        printWriter.println("$ <invocation> StatusBar FalsingManager BootCompleteCacheImpl");
        printWriter.println("etc.");
        printWriter.println();
        printWriter.println("Special commands:");
        printWriter.println("$ <invocation> dumpables");
        printWriter.println("$ <invocation> buffers");
        printWriter.println("$ <invocation> bugreport-critical");
        printWriter.println("$ <invocation> bugreport-normal");
        printWriter.println();
        printWriter.println("Targets can be listed:");
        printWriter.println("$ <invocation> --list");
        printWriter.println("$ <invocation> dumpables --list");
        printWriter.println("$ <invocation> buffers --list");
        printWriter.println();
        printWriter.println("Show only the most recent N lines of buffers");
        printWriter.println("$ <invocation> NotifLog --tail 30");
    }

    private final ParsedArgs parseArgs(String[] strArr) {
        String[] strArr2;
        List mutableList = ArraysKt.toMutableList(strArr);
        ParsedArgs parsedArgs = new ParsedArgs(strArr, mutableList);
        Iterator<String> it = mutableList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (StringsKt.startsWith$default(next, "-", false, 2, null)) {
                it.remove();
                switch (next.hashCode()) {
                    case 1499:
                        if (!next.equals("-h")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                        }
                        parsedArgs.setCommand("help");
                        break;
                    case 1503:
                        if (!next.equals("-l")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                        }
                        parsedArgs.setListOnly(true);
                        break;
                    case 1511:
                        if (!next.equals("-t")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                        }
                        parsedArgs.setTailLength(((Number) readArgument(it, next, DumpHandler$parseArgs$2.INSTANCE)).intValue());
                        break;
                    case 1056887741:
                        if (!next.equals("--dump-priority")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                        }
                        parsedArgs.setDumpPriority((String) readArgument(it, "--dump-priority", DumpHandler$parseArgs$1.INSTANCE));
                        break;
                    case 1333069025:
                        if (!next.equals("--help")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                        }
                        parsedArgs.setCommand("help");
                        break;
                    case 1333192254:
                        if (!next.equals("--list")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                        }
                        parsedArgs.setListOnly(true);
                        break;
                    case 1333422576:
                        if (!next.equals("--tail")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                        }
                        parsedArgs.setTailLength(((Number) readArgument(it, next, DumpHandler$parseArgs$2.INSTANCE)).intValue());
                        break;
                    default:
                        throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", next));
                }
            }
        }
        if (parsedArgs.getCommand() == null && (!mutableList.isEmpty())) {
            strArr2 = DumpHandlerKt.COMMANDS;
            if (ArraysKt.contains(strArr2, mutableList.get(0))) {
                parsedArgs.setCommand((String) mutableList.remove(0));
            }
        }
        return parsedArgs;
    }

    private final <T> T readArgument(Iterator<String> it, String str, Function1<? super String, ? extends T> function1) {
        if (!it.hasNext()) {
            throw new ArgParseException(Intrinsics.stringPlus("Missing argument for ", str));
        }
        String next = it.next();
        try {
            T mo1949invoke = function1.mo1949invoke(next);
            it.remove();
            return mo1949invoke;
        } catch (Exception unused) {
            throw new ArgParseException("Invalid argument '" + next + "' for flag " + str);
        }
    }

    /* compiled from: DumpHandler.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
