package com.android.systemui.statusbar.commandline;

import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001b\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u000f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0006HÆ\u0003J#\u0010\u000e\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, mo64987d2 = {"Lcom/android/systemui/statusbar/commandline/CommandWrapper;", "", "commandFactory", "Lkotlin/Function0;", "Lcom/android/systemui/statusbar/commandline/Command;", "executor", "Ljava/util/concurrent/Executor;", "(Lkotlin/jvm/functions/Function0;Ljava/util/concurrent/Executor;)V", "getCommandFactory", "()Lkotlin/jvm/functions/Function0;", "getExecutor", "()Ljava/util/concurrent/Executor;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: CommandRegistry.kt */
final class CommandWrapper {
    private final Function0<Command> commandFactory;
    private final Executor executor;

    public static /* synthetic */ CommandWrapper copy$default(CommandWrapper commandWrapper, Function0<Command> function0, Executor executor2, int i, Object obj) {
        if ((i & 1) != 0) {
            function0 = commandWrapper.commandFactory;
        }
        if ((i & 2) != 0) {
            executor2 = commandWrapper.executor;
        }
        return commandWrapper.copy(function0, executor2);
    }

    public final Function0<Command> component1() {
        return this.commandFactory;
    }

    public final Executor component2() {
        return this.executor;
    }

    public final CommandWrapper copy(Function0<? extends Command> function0, Executor executor2) {
        Intrinsics.checkNotNullParameter(function0, "commandFactory");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        return new CommandWrapper(function0, executor2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CommandWrapper)) {
            return false;
        }
        CommandWrapper commandWrapper = (CommandWrapper) obj;
        return Intrinsics.areEqual((Object) this.commandFactory, (Object) commandWrapper.commandFactory) && Intrinsics.areEqual((Object) this.executor, (Object) commandWrapper.executor);
    }

    public int hashCode() {
        return (this.commandFactory.hashCode() * 31) + this.executor.hashCode();
    }

    public String toString() {
        return "CommandWrapper(commandFactory=" + this.commandFactory + ", executor=" + this.executor + ')';
    }

    public CommandWrapper(Function0<? extends Command> function0, Executor executor2) {
        Intrinsics.checkNotNullParameter(function0, "commandFactory");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        this.commandFactory = function0;
        this.executor = executor2;
    }

    public final Function0<Command> getCommandFactory() {
        return this.commandFactory;
    }

    public final Executor getExecutor() {
        return this.executor;
    }
}
