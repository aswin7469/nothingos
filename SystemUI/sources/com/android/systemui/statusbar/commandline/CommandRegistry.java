package com.android.systemui.statusbar.commandline;

import android.content.Context;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class CommandRegistry {
    @NotNull
    private final Map<String, CommandWrapper> commandMap = new LinkedHashMap();
    @NotNull
    private final Context context;
    private boolean initialized;
    @NotNull
    private final Executor mainExecutor;

    public CommandRegistry(@NotNull Context context, @NotNull Executor mainExecutor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mainExecutor, "mainExecutor");
        this.context = context;
        this.mainExecutor = mainExecutor;
    }

    @NotNull
    public final Context getContext() {
        return this.context;
    }

    public final synchronized void registerCommand(@NotNull String name, @NotNull Function0<? extends Command> commandFactory, @NotNull Executor executor) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(commandFactory, "commandFactory");
        Intrinsics.checkNotNullParameter(executor, "executor");
        if (this.commandMap.get(name) != null) {
            throw new IllegalStateException("A command is already registered for (" + name + ')');
        }
        this.commandMap.put(name, new CommandWrapper(commandFactory, executor));
    }

    public final synchronized void registerCommand(@NotNull String name, @NotNull Function0<? extends Command> commandFactory) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(commandFactory, "commandFactory");
        registerCommand(name, commandFactory, this.mainExecutor);
    }

    public final synchronized void unregisterCommand(@NotNull String command) {
        Intrinsics.checkNotNullParameter(command, "command");
        this.commandMap.remove(command);
    }

    private final void initializeCommands() {
        this.initialized = true;
        registerCommand("prefs", new CommandRegistry$initializeCommands$1(this));
    }

    public final void onShellCommand(@NotNull final PrintWriter pw, @NotNull final String[] args) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        if (!this.initialized) {
            initializeCommands();
        }
        if (args.length == 0) {
            help(pw);
            return;
        }
        CommandWrapper commandWrapper = this.commandMap.get(args[0]);
        if (commandWrapper == null) {
            help(pw);
            return;
        }
        final Command mo1951invoke = commandWrapper.getCommandFactory().mo1951invoke();
        final FutureTask futureTask = new FutureTask(new Callable<Unit>() { // from class: com.android.systemui.statusbar.commandline.CommandRegistry$onShellCommand$task$1
            @Override // java.util.concurrent.Callable
            public /* bridge */ /* synthetic */ Unit call() {
                call2();
                return Unit.INSTANCE;
            }

            @Override // java.util.concurrent.Callable
            /* renamed from: call  reason: avoid collision after fix types in other method */
            public final void call2() {
                List<String> drop;
                Command command = Command.this;
                PrintWriter printWriter = pw;
                drop = ArraysKt___ArraysKt.drop(args, 1);
                command.execute(printWriter, drop);
            }
        });
        commandWrapper.getExecutor().execute(new Runnable() { // from class: com.android.systemui.statusbar.commandline.CommandRegistry$onShellCommand$1
            @Override // java.lang.Runnable
            public final void run() {
                futureTask.run();
            }
        });
        futureTask.get();
    }

    private final void help(PrintWriter printWriter) {
        printWriter.println("Usage: adb shell cmd statusbar <command>");
        printWriter.println("  known commands:");
        for (String str : this.commandMap.keySet()) {
            printWriter.println(Intrinsics.stringPlus("   ", str));
        }
    }
}
