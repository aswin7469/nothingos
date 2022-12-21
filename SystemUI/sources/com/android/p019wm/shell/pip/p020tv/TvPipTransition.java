package com.android.p019wm.shell.pip.p020tv;

import android.app.TaskInfo;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipMenuController;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.pip.tv.TvPipTransition */
public class TvPipTransition extends PipTransitionController {
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        return null;
    }

    public void onFinishResize(TaskInfo taskInfo, Rect rect, int i, SurfaceControl.Transaction transaction) {
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        return false;
    }

    public TvPipTransition(PipBoundsState pipBoundsState, PipMenuController pipMenuController, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, PipAnimationController pipAnimationController, Transitions transitions, ShellTaskOrganizer shellTaskOrganizer) {
        super(pipBoundsState, pipMenuController, tvPipBoundsAlgorithm, pipAnimationController, transitions, shellTaskOrganizer);
    }
}
