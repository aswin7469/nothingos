package com.android.systemui.dump;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.SystemClock;
import android.os.Trace;
import com.android.systemui.C1894R;
import com.android.systemui.CoreStartable;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Provider;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010)\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 72\u00020\u0001:\u00017BE\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u001c\u0010\b\u001a\u0018\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\t\u0012\u0006\u0010\r\u001a\u00020\u000e¢\u0006\u0002\u0010\u000fJ!\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015¢\u0006\u0002\u0010\u0017J\u0018\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0002J\u0010\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0002J\u0018\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0002J\u0010\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0018\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0002J\u0018\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0002J-\u0010 \u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010!\u001a\u00020\u00162\u000e\u0010\"\u001a\n\u0012\u0004\u0012\u00020\u0016\u0018\u00010\u0015H\u0002¢\u0006\u0002\u0010#J \u0010 \u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010!\u001a\u00020\u00162\u0006\u0010$\u001a\u00020%H\u0002J&\u0010&\u001a\u00020\u00112\f\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00160(2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0002J\u0006\u0010)\u001a\u00020\u0011J\u001b\u0010*\u001a\u00020\u00192\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u0002¢\u0006\u0002\u0010+JL\u0010,\u001a\u0002H-\"\u0004\b\u0000\u0010-2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00160/2\u0006\u00100\u001a\u00020\u00162!\u00101\u001a\u001d\u0012\u0013\u0012\u00110\u0016¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(5\u0012\u0004\u0012\u0002H-02H\u0002¢\u0006\u0002\u00106R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R$\u0010\b\u001a\u0018\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000¨\u00068"}, mo65043d2 = {"Lcom/android/systemui/dump/DumpHandler;", "", "context", "Landroid/content/Context;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "logBufferEulogizer", "Lcom/android/systemui/dump/LogBufferEulogizer;", "startables", "", "Ljava/lang/Class;", "Ljavax/inject/Provider;", "Lcom/android/systemui/CoreStartable;", "uncaughtExceptionPreHandlerManager", "Lcom/android/systemui/shared/system/UncaughtExceptionPreHandlerManager;", "(Landroid/content/Context;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/dump/LogBufferEulogizer;Ljava/util/Map;Lcom/android/systemui/shared/system/UncaughtExceptionPreHandlerManager;)V", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "dumpBuffers", "Lcom/android/systemui/dump/ParsedArgs;", "dumpConfig", "dumpCritical", "dumpDumpables", "dumpHelp", "dumpNormal", "dumpParameterized", "dumpServiceList", "type", "services", "(Ljava/io/PrintWriter;Ljava/lang/String;[Ljava/lang/String;)V", "resId", "", "dumpTargets", "targets", "", "init", "parseArgs", "([Ljava/lang/String;)Lcom/android/systemui/dump/ParsedArgs;", "readArgument", "T", "iterator", "", "flag", "parser", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "arg", "(Ljava/util/Iterator;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpHandler.kt */
public final class DumpHandler {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String PRIORITY_ARG = "--dump-priority";
    public static final String PRIORITY_ARG_CRITICAL = "CRITICAL";
    public static final String PRIORITY_ARG_HIGH = "HIGH";
    public static final String PRIORITY_ARG_NORMAL = "NORMAL";
    private final Context context;
    private final DumpManager dumpManager;
    private final LogBufferEulogizer logBufferEulogizer;
    private final Map<Class<?>, Provider<CoreStartable>> startables;
    private final UncaughtExceptionPreHandlerManager uncaughtExceptionPreHandlerManager;

    @Inject
    public DumpHandler(Context context2, DumpManager dumpManager2, LogBufferEulogizer logBufferEulogizer2, Map<Class<?>, Provider<CoreStartable>> map, UncaughtExceptionPreHandlerManager uncaughtExceptionPreHandlerManager2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(logBufferEulogizer2, "logBufferEulogizer");
        Intrinsics.checkNotNullParameter(map, "startables");
        Intrinsics.checkNotNullParameter(uncaughtExceptionPreHandlerManager2, "uncaughtExceptionPreHandlerManager");
        this.context = context2;
        this.dumpManager = dumpManager2;
        this.logBufferEulogizer = logBufferEulogizer2;
        this.startables = map;
        this.uncaughtExceptionPreHandlerManager = uncaughtExceptionPreHandlerManager2;
    }

    public final void init() {
        this.uncaughtExceptionPreHandlerManager.registerHandler(new DumpHandler$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: init$lambda-0  reason: not valid java name */
    public static final void m2752init$lambda0(DumpHandler dumpHandler, Thread thread, Throwable th) {
        Intrinsics.checkNotNullParameter(dumpHandler, "this$0");
        if (th instanceof Exception) {
            dumpHandler.logBufferEulogizer.record((Exception) th);
        }
    }

    public final void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        Trace.beginSection("DumpManager#dump()");
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            ParsedArgs parseArgs = parseArgs(strArr);
            String dumpPriority = parseArgs.getDumpPriority();
            if (Intrinsics.areEqual((Object) dumpPriority, (Object) PRIORITY_ARG_CRITICAL)) {
                dumpCritical(printWriter, parseArgs);
            } else if (Intrinsics.areEqual((Object) dumpPriority, (Object) PRIORITY_ARG_NORMAL)) {
                dumpNormal(printWriter, parseArgs);
            } else {
                dumpParameterized(printWriter, parseArgs);
            }
            printWriter.println();
            printWriter.println("Dump took " + (SystemClock.uptimeMillis() - uptimeMillis) + DateFormat.MINUTE_SECOND);
            Trace.endSection();
        } catch (ArgParseException e) {
            printWriter.println(e.getMessage());
        }
    }

    private final void dumpParameterized(PrintWriter printWriter, ParsedArgs parsedArgs) {
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
                        dumpDumpables(printWriter, parsedArgs);
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
                        dumpCritical(printWriter, parsedArgs);
                        return;
                    }
                    break;
            }
        }
        dumpTargets(parsedArgs.getNonFlagArgs(), printWriter, parsedArgs);
    }

    private final void dumpCritical(PrintWriter printWriter, ParsedArgs parsedArgs) {
        this.dumpManager.dumpDumpables(printWriter, parsedArgs.getRawArgs());
        dumpConfig(printWriter);
    }

    private final void dumpNormal(PrintWriter printWriter, ParsedArgs parsedArgs) {
        this.dumpManager.dumpBuffers(printWriter, parsedArgs.getTailLength());
        this.logBufferEulogizer.readEulogyIfPresent(printWriter);
    }

    private final void dumpDumpables(PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (parsedArgs.getListOnly()) {
            this.dumpManager.listDumpables(printWriter);
        } else {
            this.dumpManager.dumpDumpables(printWriter, parsedArgs.getRawArgs());
        }
    }

    private final void dumpBuffers(PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (parsedArgs.getListOnly()) {
            this.dumpManager.listBuffers(printWriter);
        } else {
            this.dumpManager.dumpBuffers(printWriter, parsedArgs.getTailLength());
        }
    }

    private final void dumpTargets(List<String> list, PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (!list.isEmpty()) {
            for (String dumpTarget : list) {
                this.dumpManager.dumpTarget(dumpTarget, printWriter, parsedArgs.getRawArgs(), parsedArgs.getTailLength());
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
        printWriter.println(this.context.getResources().getString(C1894R.string.config_systemUIVendorServiceComponent));
        Iterable<Class> keySet = this.startables.keySet();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(keySet, 10));
        for (Class simpleName : keySet) {
            arrayList.add(simpleName.getSimpleName());
        }
        List mutableList = CollectionsKt.toMutableList((List) arrayList);
        String string = this.context.getResources().getString(C1894R.string.config_systemUIVendorServiceComponent);
        Intrinsics.checkNotNullExpressionValue(string, "context.resources.getStr…UIVendorServiceComponent)");
        mutableList.add(string);
        Object[] array = mutableList.toArray((T[]) new String[0]);
        Intrinsics.checkNotNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        dumpServiceList(printWriter, Logger.GLOBAL_LOGGER_NAME, (String[]) array);
        dumpServiceList(printWriter, "per-user", (int) C1894R.array.config_systemUIServiceComponentsPerUser);
    }

    private final void dumpServiceList(PrintWriter printWriter, String str, int i) {
        String[] stringArray = this.context.getResources().getStringArray(i);
        Intrinsics.checkNotNullExpressionValue(stringArray, "context.resources.getStringArray(resId)");
        dumpServiceList(printWriter, str, stringArray);
    }

    private final void dumpServiceList(PrintWriter printWriter, String str, String[] strArr) {
        printWriter.print(str);
        printWriter.print(": ");
        if (strArr == null) {
            printWriter.println("N/A");
            return;
        }
        printWriter.print(strArr.length);
        printWriter.println(" services");
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            printWriter.print("  ");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.println(strArr[i]);
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

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0069, code lost:
        r1.setTailLength(((java.lang.Number) readArgument(r9, r2, com.android.systemui.dump.DumpHandler$parseArgs$2.INSTANCE)).intValue());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0083, code lost:
        r1.setListOnly(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x008f, code lost:
        r1.setCommand("help");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00aa, code lost:
        throw new com.android.systemui.dump.ArgParseException("Unknown flag: " + r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.android.systemui.dump.ParsedArgs parseArgs(java.lang.String[] r9) {
        /*
            r8 = this;
            java.util.List r0 = kotlin.collections.ArraysKt.toMutableList((T[]) r9)
            com.android.systemui.dump.ParsedArgs r1 = new com.android.systemui.dump.ParsedArgs
            r1.<init>(r9, r0)
            java.util.Iterator r9 = r0.iterator()
        L_0x000d:
            boolean r2 = r9.hasNext()
            r3 = 1
            r4 = 0
            if (r2 == 0) goto L_0x00ab
            java.lang.Object r2 = r9.next()
            java.lang.String r2 = (java.lang.String) r2
            r5 = 2
            r6 = 0
            java.lang.String r7 = "-"
            boolean r4 = kotlin.text.StringsKt.startsWith$default(r2, r7, r4, r5, r6)
            if (r4 == 0) goto L_0x000d
            r9.remove()
            int r4 = r2.hashCode()
            switch(r4) {
                case 1499: goto L_0x0087;
                case 1503: goto L_0x007b;
                case 1511: goto L_0x0061;
                case 1056887741: goto L_0x004b;
                case 1333069025: goto L_0x0042;
                case 1333192254: goto L_0x0039;
                case 1333422576: goto L_0x0030;
                default: goto L_0x002f;
            }
        L_0x002f:
            goto L_0x0096
        L_0x0030:
            java.lang.String r3 = "--tail"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0096
            goto L_0x0069
        L_0x0039:
            java.lang.String r4 = "--list"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x0096
            goto L_0x0083
        L_0x0042:
            java.lang.String r3 = "--help"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0096
            goto L_0x008f
        L_0x004b:
            java.lang.String r3 = "--dump-priority"
            boolean r4 = r2.equals(r3)
            if (r4 == 0) goto L_0x0096
            com.android.systemui.dump.DumpHandler$parseArgs$1 r2 = com.android.systemui.dump.DumpHandler$parseArgs$1.INSTANCE
            kotlin.jvm.functions.Function1 r2 = (kotlin.jvm.functions.Function1) r2
            java.lang.Object r2 = r8.readArgument(r9, r3, r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setDumpPriority(r2)
            goto L_0x000d
        L_0x0061:
            java.lang.String r3 = "-t"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0096
        L_0x0069:
            com.android.systemui.dump.DumpHandler$parseArgs$2 r3 = com.android.systemui.dump.DumpHandler$parseArgs$2.INSTANCE
            kotlin.jvm.functions.Function1 r3 = (kotlin.jvm.functions.Function1) r3
            java.lang.Object r2 = r8.readArgument(r9, r2, r3)
            java.lang.Number r2 = (java.lang.Number) r2
            int r2 = r2.intValue()
            r1.setTailLength(r2)
            goto L_0x000d
        L_0x007b:
            java.lang.String r4 = "-l"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x0096
        L_0x0083:
            r1.setListOnly(r3)
            goto L_0x000d
        L_0x0087:
            java.lang.String r3 = "-h"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0096
        L_0x008f:
            java.lang.String r2 = "help"
            r1.setCommand(r2)
            goto L_0x000d
        L_0x0096:
            com.android.systemui.dump.ArgParseException r8 = new com.android.systemui.dump.ArgParseException
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r0 = "Unknown flag: "
            r9.<init>((java.lang.String) r0)
            java.lang.StringBuilder r9 = r9.append((java.lang.String) r2)
            java.lang.String r9 = r9.toString()
            r8.<init>(r9)
            throw r8
        L_0x00ab:
            java.lang.String r8 = r1.getCommand()
            if (r8 != 0) goto L_0x00d2
            r8 = r0
            java.util.Collection r8 = (java.util.Collection) r8
            boolean r8 = r8.isEmpty()
            r8 = r8 ^ r3
            if (r8 == 0) goto L_0x00d2
            java.lang.String[] r8 = com.android.systemui.dump.DumpHandlerKt.COMMANDS
            java.lang.Object r9 = r0.get(r4)
            boolean r8 = kotlin.collections.ArraysKt.contains((T[]) r8, r9)
            if (r8 == 0) goto L_0x00d2
            java.lang.Object r8 = r0.remove((int) r4)
            java.lang.String r8 = (java.lang.String) r8
            r1.setCommand(r8)
        L_0x00d2:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.DumpHandler.parseArgs(java.lang.String[]):com.android.systemui.dump.ParsedArgs");
    }

    private final <T> T readArgument(Iterator<String> it, String str, Function1<? super String, ? extends T> function1) {
        if (it.hasNext()) {
            String next = it.next();
            try {
                T invoke = function1.invoke(next);
                it.remove();
                return invoke;
            } catch (Exception unused) {
                throw new ArgParseException("Invalid argument '" + next + "' for flag " + str);
            }
        } else {
            throw new ArgParseException("Missing argument for " + str);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/dump/DumpHandler$Companion;", "", "()V", "PRIORITY_ARG", "", "PRIORITY_ARG_CRITICAL", "PRIORITY_ARG_HIGH", "PRIORITY_ARG_NORMAL", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DumpHandler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
