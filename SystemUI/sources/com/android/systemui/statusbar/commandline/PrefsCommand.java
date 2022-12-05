package com.android.systemui.statusbar.commandline;

import android.content.Context;
import com.android.systemui.Prefs;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
final class PrefsCommand implements Command {
    @NotNull
    private final Context context;

    public PrefsCommand(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public void help(@NotNull PrintWriter pw) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        pw.println("usage: prefs <command> [args]");
        pw.println("Available commands:");
        pw.println("  list-prefs");
        pw.println("  set-pref <pref name> <value>");
    }

    @Override // com.android.systemui.statusbar.commandline.Command
    public void execute(@NotNull PrintWriter pw, @NotNull List<String> args) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        if (args.isEmpty()) {
            help(pw);
        } else if (Intrinsics.areEqual(args.get(0), "list-prefs")) {
            listPrefs(pw);
        } else {
            help(pw);
        }
    }

    private final void listPrefs(PrintWriter printWriter) {
        printWriter.println("Available keys:");
        Field[] declaredFields = Prefs.Key.class.getDeclaredFields();
        Intrinsics.checkNotNullExpressionValue(declaredFields, "Prefs.Key::class.java.declaredFields");
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            i++;
            printWriter.print("  ");
            printWriter.println(field.get(Prefs.Key.class));
        }
    }
}
