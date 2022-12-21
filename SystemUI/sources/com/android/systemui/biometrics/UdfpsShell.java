package com.android.systemui.biometrics;

import android.hardware.fingerprint.IUdfpsOverlayController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.p026io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0011H\u0002J\u0010\u0010\u0015\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u0016\u001a\u00020\fH\u0002J\u0010\u0010\u0017\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u0013H\u0002R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/biometrics/UdfpsShell;", "Lcom/android/systemui/statusbar/commandline/Command;", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "(Lcom/android/systemui/statusbar/commandline/CommandRegistry;)V", "udfpsOverlayController", "Landroid/hardware/fingerprint/IUdfpsOverlayController;", "getUdfpsOverlayController", "()Landroid/hardware/fingerprint/IUdfpsOverlayController;", "setUdfpsOverlayController", "(Landroid/hardware/fingerprint/IUdfpsOverlayController;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "getEnrollmentReason", "", "reason", "help", "hideOverlay", "invalidCommand", "showOverlay", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UdfpsShell.kt */
public final class UdfpsShell implements Command {
    private IUdfpsOverlayController udfpsOverlayController;

    @Inject
    public UdfpsShell(CommandRegistry commandRegistry) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        commandRegistry.registerCommand("udfps", new Function0<Command>(this) {
            final /* synthetic */ UdfpsShell this$0;

            {
                this.this$0 = r1;
            }

            public final Command invoke() {
                return this.this$0;
            }
        });
    }

    public final IUdfpsOverlayController getUdfpsOverlayController() {
        return this.udfpsOverlayController;
    }

    public final void setUdfpsOverlayController(IUdfpsOverlayController iUdfpsOverlayController) {
        this.udfpsOverlayController = iUdfpsOverlayController;
    }

    public void execute(PrintWriter printWriter, List<String> list) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(list, "args");
        if (list.size() == 1 && Intrinsics.areEqual((Object) list.get(0), (Object) "hide")) {
            hideOverlay();
        } else if (list.size() != 2 || !Intrinsics.areEqual((Object) list.get(0), (Object) "show")) {
            invalidCommand(printWriter);
        } else {
            showOverlay(getEnrollmentReason(list.get(1)));
        }
    }

    public void help(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printWriter.println("Usage: adb shell cmd statusbar udfps <cmd>");
        printWriter.println("Supported commands:");
        printWriter.println("  - show <reason>");
        printWriter.println("    -> supported reasons: [enroll-find-sensor, enroll-enrolling, auth-bp, auth-keyguard, auth-other, auth-settings]");
        printWriter.println("    -> reason otherwise defaults to unknown");
        printWriter.println("  - hide");
    }

    private final void invalidCommand(PrintWriter printWriter) {
        printWriter.println("invalid command");
        help(printWriter);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x004a A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int getEnrollmentReason(java.lang.String r1) {
        /*
            r0 = this;
            int r0 = r1.hashCode()
            switch(r0) {
                case -945543637: goto L_0x003f;
                case -943067225: goto L_0x0034;
                case -646572397: goto L_0x0029;
                case -19448152: goto L_0x001e;
                case 244570389: goto L_0x0013;
                case 902271659: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x004a
        L_0x0008:
            java.lang.String r0 = "auth-other"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0011
            goto L_0x004a
        L_0x0011:
            r0 = 5
            goto L_0x004b
        L_0x0013:
            java.lang.String r0 = "enroll-enrolling"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x001c
            goto L_0x004a
        L_0x001c:
            r0 = 2
            goto L_0x004b
        L_0x001e:
            java.lang.String r0 = "auth-settings"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0027
            goto L_0x004a
        L_0x0027:
            r0 = 6
            goto L_0x004b
        L_0x0029:
            java.lang.String r0 = "auth-bp"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0032
            goto L_0x004a
        L_0x0032:
            r0 = 3
            goto L_0x004b
        L_0x0034:
            java.lang.String r0 = "enroll-find-sensor"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x003d
            goto L_0x004a
        L_0x003d:
            r0 = 1
            goto L_0x004b
        L_0x003f:
            java.lang.String r0 = "auth-keyguard"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0048
            goto L_0x004a
        L_0x0048:
            r0 = 4
            goto L_0x004b
        L_0x004a:
            r0 = 0
        L_0x004b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsShell.getEnrollmentReason(java.lang.String):int");
    }

    private final void showOverlay(int i) {
        IUdfpsOverlayController iUdfpsOverlayController = this.udfpsOverlayController;
        if (iUdfpsOverlayController != null) {
            iUdfpsOverlayController.showUdfpsOverlay(2, 0, i, new UdfpsShell$showOverlay$1());
        }
    }

    private final void hideOverlay() {
        IUdfpsOverlayController iUdfpsOverlayController = this.udfpsOverlayController;
        if (iUdfpsOverlayController != null) {
            iUdfpsOverlayController.hideUdfpsOverlay(0);
        }
    }
}
