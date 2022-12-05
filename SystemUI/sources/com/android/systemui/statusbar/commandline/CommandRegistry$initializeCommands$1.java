package com.android.systemui.statusbar.commandline;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class CommandRegistry$initializeCommands$1 extends Lambda implements Function0<Command> {
    final /* synthetic */ CommandRegistry this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CommandRegistry$initializeCommands$1(CommandRegistry commandRegistry) {
        super(0);
        this.this$0 = commandRegistry;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    @NotNull
    /* renamed from: invoke */
    public final Command mo1951invoke() {
        return new PrefsCommand(this.this$0.getContext());
    }
}
