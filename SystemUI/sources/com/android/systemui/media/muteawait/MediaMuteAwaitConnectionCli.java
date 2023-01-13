package com.android.systemui.media.muteawait;

import android.content.Context;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.p026io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0007B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionCli;", "", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "context", "Landroid/content/Context;", "(Lcom/android/systemui/statusbar/commandline/CommandRegistry;Landroid/content/Context;)V", "MuteAwaitCommand", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaMuteAwaitConnectionCli.kt */
public final class MediaMuteAwaitConnectionCli {
    /* access modifiers changed from: private */
    public final Context context;

    @Inject
    public MediaMuteAwaitConnectionCli(CommandRegistry commandRegistry, Context context2) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        commandRegistry.registerCommand("media-mute-await", new Function0<Command>(this) {
            final /* synthetic */ MediaMuteAwaitConnectionCli this$0;

            {
                this.this$0 = r1;
            }

            public final Command invoke() {
                return new MuteAwaitCommand();
            }
        });
    }

    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionCli$MuteAwaitCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "(Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionCli;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaMuteAwaitConnectionCli.kt */
    public final class MuteAwaitCommand implements Command {
        public MuteAwaitCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            AudioDeviceAttributes audioDeviceAttributes = new AudioDeviceAttributes(2, Integer.parseInt(list.get(0)), "address", list.get(1), CollectionsKt.emptyList(), CollectionsKt.emptyList());
            String str = list.get(2);
            Object systemService = MediaMuteAwaitConnectionCli.this.context.getSystemService("audio");
            if (systemService != null) {
                AudioManager audioManager = (AudioManager) systemService;
                if (Intrinsics.areEqual((Object) str, (Object) "start")) {
                    audioManager.muteAwaitConnection(new int[]{1}, audioDeviceAttributes, 5, MediaMuteAwaitConnectionCliKt.TIMEOUT_UNITS);
                } else if (Intrinsics.areEqual((Object) str, (Object) "cancel")) {
                    audioManager.cancelMuteAwaitConnection(audioDeviceAttributes);
                } else {
                    printWriter.println("Must specify `start` or `cancel`; was " + str);
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
            }
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar media-mute-await [type] [name] [start|cancel]");
        }
    }
}
