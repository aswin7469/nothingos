package com.android.p019wm.shell;

import com.android.p019wm.shell.apppairs.AppPairsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.p019wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.p019wm.shell.onehanded.OneHandedController;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.p026io.PrintWriter;
import java.util.Optional;

/* renamed from: com.android.wm.shell.ShellCommandHandlerImpl */
public final class ShellCommandHandlerImpl {
    private static final String TAG = "ShellCommandHandlerImpl";
    private final Optional<AppPairsController> mAppPairsOptional;
    private final Optional<HideDisplayCutoutController> mHideDisplayCutout;
    private final HandlerImpl mImpl = new HandlerImpl();
    private final KidsModeTaskOrganizer mKidsModeTaskOrganizer;
    private final Optional<LegacySplitScreenController> mLegacySplitScreenOptional;
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final Optional<OneHandedController> mOneHandedOptional;
    private final Optional<Pip> mPipOptional;
    private final Optional<RecentTasksController> mRecentTasks;
    private final ShellTaskOrganizer mShellTaskOrganizer;
    private final Optional<SplitScreenController> mSplitScreenOptional;

    public ShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, Optional<RecentTasksController> optional7, ShellExecutor shellExecutor) {
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mKidsModeTaskOrganizer = kidsModeTaskOrganizer;
        this.mRecentTasks = optional7;
        this.mLegacySplitScreenOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mPipOptional = optional3;
        this.mOneHandedOptional = optional4;
        this.mHideDisplayCutout = optional5;
        this.mAppPairsOptional = optional6;
        this.mMainExecutor = shellExecutor;
    }

    public ShellCommandHandler asShellCommandHandler() {
        return this.mImpl;
    }

    /* access modifiers changed from: private */
    public void dump(PrintWriter printWriter) {
        this.mShellTaskOrganizer.dump(printWriter, "");
        printWriter.println();
        printWriter.println();
        this.mPipOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda7(printWriter));
        this.mLegacySplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda8(printWriter));
        this.mOneHandedOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda9(printWriter));
        this.mHideDisplayCutout.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda10(printWriter));
        printWriter.println();
        printWriter.println();
        this.mAppPairsOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda11(printWriter));
        printWriter.println();
        printWriter.println();
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda1(printWriter));
        printWriter.println();
        printWriter.println();
        this.mRecentTasks.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda2(printWriter));
        printWriter.println();
        printWriter.println();
        this.mKidsModeTaskOrganizer.dump(printWriter, "");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003d, code lost:
        if (r3.equals("help") == false) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handleCommand(java.lang.String[] r7, java.p026io.PrintWriter r8) {
        /*
            r6 = this;
            int r0 = r7.length
            r1 = 0
            r2 = 2
            if (r0 >= r2) goto L_0x0006
            return r1
        L_0x0006:
            r0 = 1
            r3 = r7[r0]
            r3.hashCode()
            int r4 = r3.hashCode()
            r5 = -1
            switch(r4) {
                case -840336141: goto L_0x004b;
                case -91197669: goto L_0x0040;
                case 3198785: goto L_0x0037;
                case 3433178: goto L_0x002c;
                case 295561529: goto L_0x0021;
                case 1522429422: goto L_0x0016;
                default: goto L_0x0014;
            }
        L_0x0014:
            r2 = r5
            goto L_0x0056
        L_0x0016:
            java.lang.String r0 = "setSideStagePosition"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x001f
            goto L_0x0014
        L_0x001f:
            r2 = 5
            goto L_0x0056
        L_0x0021:
            java.lang.String r0 = "removeFromSideStage"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x002a
            goto L_0x0014
        L_0x002a:
            r2 = 4
            goto L_0x0056
        L_0x002c:
            java.lang.String r0 = "pair"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x0035
            goto L_0x0014
        L_0x0035:
            r2 = 3
            goto L_0x0056
        L_0x0037:
            java.lang.String r0 = "help"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x0056
            goto L_0x0014
        L_0x0040:
            java.lang.String r2 = "moveToSideStage"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L_0x0049
            goto L_0x0014
        L_0x0049:
            r2 = r0
            goto L_0x0056
        L_0x004b:
            java.lang.String r0 = "unpair"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x0055
            goto L_0x0014
        L_0x0055:
            r2 = r1
        L_0x0056:
            switch(r2) {
                case 0: goto L_0x0073;
                case 1: goto L_0x006e;
                case 2: goto L_0x0069;
                case 3: goto L_0x0064;
                case 4: goto L_0x005f;
                case 5: goto L_0x005a;
                default: goto L_0x0059;
            }
        L_0x0059:
            return r1
        L_0x005a:
            boolean r6 = r6.runSetSideStagePosition(r7, r8)
            return r6
        L_0x005f:
            boolean r6 = r6.runRemoveFromSideStage(r7, r8)
            return r6
        L_0x0064:
            boolean r6 = r6.runPair(r7, r8)
            return r6
        L_0x0069:
            boolean r6 = r6.runHelp(r8)
            return r6
        L_0x006e:
            boolean r6 = r6.runMoveToSideStage(r7, r8)
            return r6
        L_0x0073:
            boolean r6 = r6.runUnpair(r7, r8)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.ShellCommandHandlerImpl.handleCommand(java.lang.String[], java.io.PrintWriter):boolean");
    }

    private boolean runPair(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 4) {
            printWriter.println("Error: two task ids should be provided as arguments");
            return false;
        }
        this.mAppPairsOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda4(new Integer(strArr[2]).intValue(), new Integer(strArr[3]).intValue()));
        return true;
    }

    private boolean runUnpair(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as an argument");
            return false;
        }
        this.mAppPairsOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda3(new Integer(strArr[2]).intValue()));
        return true;
    }

    private boolean runMoveToSideStage(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as arguments");
            return false;
        }
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda6(new Integer(strArr[2]).intValue(), strArr.length > 3 ? new Integer(strArr[3]).intValue() : 1));
        return true;
    }

    private boolean runRemoveFromSideStage(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as arguments");
            return false;
        }
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda5(new Integer(strArr[2]).intValue()));
        return true;
    }

    private boolean runSetSideStagePosition(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: side stage position should be provided as arguments");
            return false;
        }
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda0(new Integer(strArr[2]).intValue()));
        return true;
    }

    private boolean runHelp(PrintWriter printWriter) {
        printWriter.println("Window Manager Shell commands:");
        printWriter.println("  help");
        printWriter.println("      Print this help text.");
        printWriter.println("  <no arguments provided>");
        printWriter.println("    Dump Window Manager Shell internal state");
        printWriter.println("  pair <taskId1> <taskId2>");
        printWriter.println("  unpair <taskId>");
        printWriter.println("    Pairs/unpairs tasks with given ids.");
        printWriter.println("  moveToSideStage <taskId> <SideStagePosition>");
        printWriter.println("    Move a task with given id in split-screen mode.");
        printWriter.println("  removeFromSideStage <taskId>");
        printWriter.println("    Remove a task with given id in split-screen mode.");
        printWriter.println("  setSideStageOutline <true/false>");
        printWriter.println("    Enable/Disable outline on the side-stage.");
        printWriter.println("  setSideStagePosition <SideStagePosition>");
        printWriter.println("    Sets the position of the side-stage.");
        return true;
    }

    /* renamed from: com.android.wm.shell.ShellCommandHandlerImpl$HandlerImpl */
    private class HandlerImpl implements ShellCommandHandler {
        private HandlerImpl() {
        }

        public void dump(PrintWriter printWriter) {
            try {
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking(new ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1(this, printWriter));
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to dump the Shell in 2s", e);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$dump$0$com-android-wm-shell-ShellCommandHandlerImpl$HandlerImpl */
        public /* synthetic */ void mo47947xb0608392(PrintWriter printWriter) {
            ShellCommandHandlerImpl.this.dump(printWriter);
        }

        public boolean handleCommand(String[] strArr, PrintWriter printWriter) {
            try {
                boolean[] zArr = new boolean[1];
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking(new ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda0(this, zArr, strArr, printWriter));
                return zArr[0];
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to handle Shell command in 2s", e);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$handleCommand$1$com-android-wm-shell-ShellCommandHandlerImpl$HandlerImpl */
        public /* synthetic */ void mo47948xd9453c5c(boolean[] zArr, String[] strArr, PrintWriter printWriter) {
            zArr[0] = ShellCommandHandlerImpl.this.handleCommand(strArr, printWriter);
        }
    }
}
