package com.android.systemui.statusbar.phone;

import com.android.keyguard.KeyguardViewController;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;

@SysUISingleton
public class SystemUIDialogManager implements Dumpable {
    private final Set<SystemUIDialog> mDialogsShowing = new HashSet();
    private final KeyguardViewController mKeyguardViewController;
    private final Set<Listener> mListeners = new HashSet();

    public interface Listener {
        void shouldHideAffordances(boolean z);
    }

    @Inject
    public SystemUIDialogManager(DumpManager dumpManager, KeyguardViewController keyguardViewController) {
        dumpManager.registerDumpable(this);
        this.mKeyguardViewController = keyguardViewController;
    }

    public boolean shouldHideAffordance() {
        return !this.mDialogsShowing.isEmpty();
    }

    public void registerListener(Listener listener) {
        this.mListeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        this.mListeners.remove(listener);
    }

    /* access modifiers changed from: package-private */
    public void setShowing(SystemUIDialog systemUIDialog, boolean z) {
        boolean shouldHideAffordance = shouldHideAffordance();
        if (z) {
            this.mDialogsShowing.add(systemUIDialog);
        } else {
            this.mDialogsShowing.remove(systemUIDialog);
        }
        if (shouldHideAffordance != shouldHideAffordance()) {
            NTLogUtil.m1686d("SystemUIDialogManager", "shouldHideAffordance " + shouldHideAffordance());
            updateDialogListeners();
        }
    }

    private void updateDialogListeners() {
        if (shouldHideAffordance()) {
            this.mKeyguardViewController.resetAlternateAuth(true);
        }
        for (Listener shouldHideAffordances : this.mListeners) {
            shouldHideAffordances.shouldHideAffordances(shouldHideAffordance());
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("listeners:");
        for (Listener listener : this.mListeners) {
            printWriter.println("\t" + listener);
        }
        printWriter.println("dialogs tracked:");
        for (SystemUIDialog systemUIDialog : this.mDialogsShowing) {
            printWriter.println("\t" + systemUIDialog);
        }
    }
}
