package com.android.systemui.statusbar.commandline;

import android.content.Context;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.demomode.DemoMode;
import java.p026io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0012H\u0002J!\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\t0\u0018¢\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\t2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dJ$\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\t2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d2\u0006\u0010\u001f\u001a\u00020\u0005J\u000e\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\tR\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "", "context", "Landroid/content/Context;", "mainExecutor", "Ljava/util/concurrent/Executor;", "(Landroid/content/Context;Ljava/util/concurrent/Executor;)V", "commandMap", "", "", "Lcom/android/systemui/statusbar/commandline/CommandWrapper;", "getContext", "()Landroid/content/Context;", "initialized", "", "getMainExecutor", "()Ljava/util/concurrent/Executor;", "help", "", "pw", "Ljava/io/PrintWriter;", "initializeCommands", "onShellCommand", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "registerCommand", "name", "commandFactory", "Lkotlin/Function0;", "Lcom/android/systemui/statusbar/commandline/Command;", "executor", "unregisterCommand", "command", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: CommandRegistry.kt */
public final class CommandRegistry {
    private final Map<String, CommandWrapper> commandMap = new LinkedHashMap();
    private final Context context;
    private boolean initialized;
    private final Executor mainExecutor;

    @Inject
    public CommandRegistry(Context context2, @Main Executor executor) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.context = context2;
        this.mainExecutor = executor;
    }

    public final Context getContext() {
        return this.context;
    }

    public final Executor getMainExecutor() {
        return this.mainExecutor;
    }

    public final synchronized void registerCommand(String str, Function0<? extends Command> function0, Executor executor) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(function0, "commandFactory");
        Intrinsics.checkNotNullParameter(executor, "executor");
        if (this.commandMap.get(str) == null) {
            this.commandMap.put(str, new CommandWrapper(function0, executor));
        } else {
            throw new IllegalStateException("A command is already registered for (" + str + ')');
        }
    }

    public final synchronized void registerCommand(String str, Function0<? extends Command> function0) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(function0, "commandFactory");
        registerCommand(str, function0, this.mainExecutor);
    }

    public final synchronized void unregisterCommand(String str) {
        Intrinsics.checkNotNullParameter(str, DemoMode.EXTRA_COMMAND);
        this.commandMap.remove(str);
    }

    private final void initializeCommands() {
        this.initialized = true;
        registerCommand("prefs", new CommandRegistry$initializeCommands$1(this));
    }

    public final void onShellCommand(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        if (!this.initialized) {
            initializeCommands();
        }
        if (strArr.length == 0) {
            help(printWriter);
            return;
        }
        CommandWrapper commandWrapper = this.commandMap.get(strArr[0]);
        if (commandWrapper == null) {
            help(printWriter);
            return;
        }
        FutureTask futureTask = new FutureTask(new CommandRegistry$$ExternalSyntheticLambda0(commandWrapper.getCommandFactory().invoke(), printWriter, strArr));
        commandWrapper.getExecutor().execute(new CommandRegistry$$ExternalSyntheticLambda1(futureTask));
        futureTask.get();
    }

    /* access modifiers changed from: private */
    /* renamed from: onShellCommand$lambda-0  reason: not valid java name */
    public static final Unit m3057onShellCommand$lambda0(Command command, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(command, "$command");
        Intrinsics.checkNotNullParameter(printWriter, "$pw");
        Intrinsics.checkNotNullParameter(strArr, "$args");
        command.execute(printWriter, ArraysKt.drop((T[]) strArr, 1));
        return Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    /* renamed from: onShellCommand$lambda-1  reason: not valid java name */
    public static final void m3058onShellCommand$lambda1(FutureTask futureTask) {
        Intrinsics.checkNotNullParameter(futureTask, "$task");
        futureTask.run();
    }

    private final void help(PrintWriter printWriter) {
        printWriter.println("Usage: adb shell cmd statusbar <command>");
        printWriter.println("  known commands:");
        for (String str : this.commandMap.keySet()) {
            printWriter.println("   " + str);
        }
    }
}
