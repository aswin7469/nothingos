package com.android.wm.shell;

import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final class ShellCommandHandlerImpl {
    private final Optional<AppPairsController> mAppPairsOptional;
    private final Optional<HideDisplayCutoutController> mHideDisplayCutout;
    private final HandlerImpl mImpl = new HandlerImpl();
    private final Optional<LegacySplitScreenController> mLegacySplitScreenOptional;
    private final ShellExecutor mMainExecutor;
    private final Optional<OneHandedController> mOneHandedOptional;
    private final Optional<Pip> mPipOptional;
    private final ShellTaskOrganizer mShellTaskOrganizer;
    private final Optional<SplitScreenController> mSplitScreenOptional;

    public ShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, ShellExecutor shellExecutor) {
        this.mShellTaskOrganizer = shellTaskOrganizer;
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

    /* JADX INFO: Access modifiers changed from: private */
    public void dump(final PrintWriter printWriter) {
        this.mShellTaskOrganizer.dump(printWriter, "");
        printWriter.println();
        printWriter.println();
        this.mPipOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Pip) obj).dump(printWriter);
            }
        });
        this.mLegacySplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((LegacySplitScreenController) obj).dump(printWriter);
            }
        });
        this.mOneHandedOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((OneHandedController) obj).dump(printWriter);
            }
        });
        this.mHideDisplayCutout.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((HideDisplayCutoutController) obj).dump(printWriter);
            }
        });
        printWriter.println();
        printWriter.println();
        this.mAppPairsOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AppPairsController) obj).dump(printWriter, "");
            }
        });
        printWriter.println();
        printWriter.println();
        this.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SplitScreenController) obj).dump(printWriter, "");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0048, code lost:
        if (r3.equals("moveToSideStage") == false) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleCommand(String[] strArr, PrintWriter printWriter) {
        char c = 2;
        if (strArr.length < 2) {
            return false;
        }
        String str = strArr[1];
        str.hashCode();
        switch (str.hashCode()) {
            case -968877417:
                if (str.equals("setSideStageVisibility")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -840336141:
                if (str.equals("unpair")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -91197669:
                break;
            case 3198785:
                if (str.equals("help")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 3433178:
                if (str.equals("pair")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 295561529:
                if (str.equals("removeFromSideStage")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1522429422:
                if (str.equals("setSideStagePosition")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return runSetSideStageVisibility(strArr, printWriter);
            case 1:
                return runUnpair(strArr, printWriter);
            case 2:
                return runMoveToSideStage(strArr, printWriter);
            case 3:
                return runHelp(printWriter);
            case 4:
                return runPair(strArr, printWriter);
            case 5:
                return runRemoveFromSideStage(strArr, printWriter);
            case 6:
                return runSetSideStagePosition(strArr, printWriter);
            default:
                return false;
        }
    }

    private boolean runPair(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 4) {
            printWriter.println("Error: two task ids should be provided as arguments");
            return false;
        }
        final int intValue = new Integer(strArr[2]).intValue();
        final int intValue2 = new Integer(strArr[3]).intValue();
        this.mAppPairsOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AppPairsController) obj).pair(intValue, intValue2);
            }
        });
        return true;
    }

    private boolean runUnpair(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as an argument");
            return false;
        }
        final int intValue = new Integer(strArr[2]).intValue();
        this.mAppPairsOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AppPairsController) obj).unpair(intValue);
            }
        });
        return true;
    }

    private boolean runMoveToSideStage(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as arguments");
            return false;
        }
        final int intValue = new Integer(strArr[2]).intValue();
        final int intValue2 = strArr.length > 3 ? new Integer(strArr[3]).intValue() : 1;
        this.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SplitScreenController) obj).moveToSideStage(intValue, intValue2);
            }
        });
        return true;
    }

    private boolean runRemoveFromSideStage(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as arguments");
            return false;
        }
        final int intValue = new Integer(strArr[2]).intValue();
        this.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SplitScreenController) obj).removeFromSideStage(intValue);
            }
        });
        return true;
    }

    private boolean runSetSideStagePosition(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: side stage position should be provided as arguments");
            return false;
        }
        final int intValue = new Integer(strArr[2]).intValue();
        this.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SplitScreenController) obj).setSideStagePosition(intValue);
            }
        });
        return true;
    }

    private boolean runSetSideStageVisibility(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: side stage position should be provided as arguments");
            return false;
        }
        final Boolean bool = new Boolean(strArr[2]);
        this.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShellCommandHandlerImpl.lambda$runSetSideStageVisibility$11(bool, (SplitScreenController) obj);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runSetSideStageVisibility$11(Boolean bool, SplitScreenController splitScreenController) {
        splitScreenController.setSideStageVisibility(bool.booleanValue());
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
        printWriter.println("  setSideStagePosition <SideStagePosition>");
        printWriter.println("    Sets the position of the side-stage.");
        printWriter.println("  setSideStageVisibility <true/false>");
        printWriter.println("    Show/hide side-stage.");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class HandlerImpl implements ShellCommandHandler {
        private HandlerImpl() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dump$0(PrintWriter printWriter) {
            ShellCommandHandlerImpl.this.dump(printWriter);
        }

        @Override // com.android.wm.shell.ShellCommandHandler
        public void dump(final PrintWriter printWriter) {
            try {
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking(new Runnable() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShellCommandHandlerImpl.HandlerImpl.this.lambda$dump$0(printWriter);
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to dump the Shell in 2s", e);
            }
        }

        @Override // com.android.wm.shell.ShellCommandHandler
        public boolean handleCommand(final String[] strArr, final PrintWriter printWriter) {
            try {
                final boolean[] zArr = new boolean[1];
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking(new Runnable() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShellCommandHandlerImpl.HandlerImpl.this.lambda$handleCommand$1(zArr, strArr, printWriter);
                    }
                });
                return zArr[0];
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to handle Shell command in 2s", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleCommand$1(boolean[] zArr, String[] strArr, PrintWriter printWriter) {
            zArr[0] = ShellCommandHandlerImpl.this.handleCommand(strArr, printWriter);
        }
    }
}
