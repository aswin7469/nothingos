package com.android.systemui.media.taptotransfer;

import android.app.StatusBarManager;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.media.taptotransfer.receiver.ChipStateReceiver;
import com.android.systemui.media.taptotransfer.sender.ChipStateSender;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.p026io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0002\t\nB!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/media/taptotransfer/MediaTttCommandLineHelper;", "", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "context", "Landroid/content/Context;", "mainExecutor", "Ljava/util/concurrent/Executor;", "(Lcom/android/systemui/statusbar/commandline/CommandRegistry;Landroid/content/Context;Ljava/util/concurrent/Executor;)V", "ReceiverCommand", "SenderCommand", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTttCommandLineHelper.kt */
public final class MediaTttCommandLineHelper {
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public final Executor mainExecutor;

    @Inject
    public MediaTttCommandLineHelper(CommandRegistry commandRegistry, Context context2, @Main Executor executor) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.context = context2;
        this.mainExecutor = executor;
        commandRegistry.registerCommand(MediaTttCommandLineHelperKt.SENDER_COMMAND, new Function0<Command>(this) {
            final /* synthetic */ MediaTttCommandLineHelper this$0;

            {
                this.this$0 = r1;
            }

            public final Command invoke() {
                return new SenderCommand();
            }
        });
        commandRegistry.registerCommand(MediaTttCommandLineHelperKt.RECEIVER_COMMAND, new Function0<Command>(this) {
            final /* synthetic */ MediaTttCommandLineHelper this$0;

            {
                this.this$0 = r1;
            }

            public final Command invoke() {
                return new ReceiverCommand();
            }
        });
    }

    @Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0012\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\f\u001a\u00020\rH\u0002¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/media/taptotransfer/MediaTttCommandLineHelper$SenderCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "(Lcom/android/systemui/media/taptotransfer/MediaTttCommandLineHelper;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "getUndoCallback", "Ljava/lang/Runnable;", "displayState", "", "getUndoExecutor", "Ljava/util/concurrent/Executor;", "help", "isSucceededState", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaTttCommandLineHelper.kt */
    public final class SenderCommand implements Command {
        private final boolean isSucceededState(int i) {
            return i == 4 || i == 5;
        }

        public SenderCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            boolean z = true;
            String str = list.get(1);
            try {
                Integer valueOf = Integer.valueOf(ChipStateSender.Companion.getSenderStateIdFromName(str));
                Object systemService = MediaTttCommandLineHelper.this.context.getSystemService("statusbar");
                if (systemService != null) {
                    StatusBarManager statusBarManager = (StatusBarManager) systemService;
                    MediaRoute2Info.Builder addFeature = new MediaRoute2Info.Builder("id", list.get(0)).addFeature("feature");
                    if (list.size() >= 3 && Intrinsics.areEqual((Object) list.get(2), (Object) "useAppIcon=false")) {
                        z = false;
                    }
                    if (z) {
                        addFeature.setPackageName("com.android.systemui");
                    }
                    statusBarManager.updateMediaTapToTransferSenderDisplay(valueOf.intValue(), addFeature.build(), getUndoExecutor(valueOf.intValue()), getUndoCallback(valueOf.intValue()));
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.app.StatusBarManager");
            } catch (IllegalArgumentException unused) {
                printWriter.println("Invalid command name " + str);
            }
        }

        private final Executor getUndoExecutor(int i) {
            if (isSucceededState(i)) {
                return MediaTttCommandLineHelper.this.mainExecutor;
            }
            Executor executor = null;
            return null;
        }

        private final Runnable getUndoCallback(int i) {
            if (isSucceededState(i)) {
                return new C2238x368af39e(i);
            }
            Runnable runnable = null;
            return null;
        }

        /* access modifiers changed from: private */
        /* renamed from: getUndoCallback$lambda-0  reason: not valid java name */
        public static final void m2842getUndoCallback$lambda0(int i) {
            Log.i("MediaTransferCli", "Undo triggered for " + i);
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar media-ttt-chip-sender <deviceName> <chipState> useAppIcon=[true|false]");
        }
    }

    @Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/media/taptotransfer/MediaTttCommandLineHelper$ReceiverCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "(Lcom/android/systemui/media/taptotransfer/MediaTttCommandLineHelper;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaTttCommandLineHelper.kt */
    public final class ReceiverCommand implements Command {
        public ReceiverCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            boolean z = false;
            String str = list.get(0);
            try {
                Integer valueOf = Integer.valueOf(ChipStateReceiver.Companion.getReceiverStateIdFromName(str));
                Object systemService = MediaTttCommandLineHelper.this.context.getSystemService("statusbar");
                if (systemService != null) {
                    StatusBarManager statusBarManager = (StatusBarManager) systemService;
                    MediaRoute2Info.Builder addFeature = new MediaRoute2Info.Builder("id", "Test Name").addFeature("feature");
                    if (list.size() < 2 || !Intrinsics.areEqual((Object) list.get(1), (Object) "useAppIcon=false")) {
                        z = true;
                    }
                    if (z) {
                        addFeature.setPackageName("com.android.systemui");
                    }
                    statusBarManager.updateMediaTapToTransferReceiverDisplay(valueOf.intValue(), addFeature.build(), (Icon) null, (CharSequence) null);
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.app.StatusBarManager");
            } catch (IllegalArgumentException unused) {
                printWriter.println("Invalid command name " + str);
            }
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar media-ttt-chip-receiver <chipState> useAppIcon=[true|false]");
        }
    }
}
