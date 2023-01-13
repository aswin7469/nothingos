package com.android.systemui.statusbar.commandline;

import android.content.Context;
import com.android.systemui.Prefs;
import java.lang.reflect.Field;
import java.p026io.PrintWriter;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0016J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/statusbar/commandline/PrefsCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getContext", "()Landroid/content/Context;", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "listPrefs", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: CommandRegistry.kt */
final class PrefsCommand implements Command {
    private final Context context;

    public PrefsCommand(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
    }

    public final Context getContext() {
        return this.context;
    }

    public void help(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printWriter.println("usage: prefs <command> [args]");
        printWriter.println("Available commands:");
        printWriter.println("  list-prefs");
        printWriter.println("  set-pref <pref name> <value>");
    }

    public void execute(PrintWriter printWriter, List<String> list) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(list, "args");
        if (list.isEmpty()) {
            help(printWriter);
        } else if (Intrinsics.areEqual((Object) list.get(0), (Object) "list-prefs")) {
            listPrefs(printWriter);
        } else {
            help(printWriter);
        }
    }

    private final void listPrefs(PrintWriter printWriter) {
        printWriter.println("Available keys:");
        Field[] declaredFields = Prefs.Key.class.getDeclaredFields();
        Intrinsics.checkNotNullExpressionValue(declaredFields, "Prefs.Key::class.java.declaredFields");
        for (Field field : declaredFields) {
            printWriter.print("  ");
            printWriter.println(field.get(Prefs.Key.class));
        }
    }
}
