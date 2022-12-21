package com.android.p019wm.shell.pip.p020tv;

import android.app.PictureInPictureParams;
import android.content.Context;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipBoundsAlgorithm;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipMenuController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.PipTransitionState;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.PipUtils;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.util.Objects;
import java.util.Optional;

/* renamed from: com.android.wm.shell.pip.tv.TvPipTaskOrganizer */
public class TvPipTaskOrganizer extends PipTaskOrganizer {
    public TvPipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipTransitionState pipTransitionState, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipMenuController pipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        super(context, syncTransactionQueue, pipTransitionState, pipBoundsState, pipBoundsAlgorithm, pipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, pipParamsChangedForwarder, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor);
    }

    /* access modifiers changed from: protected */
    public void applyNewPictureInPictureParams(PictureInPictureParams pictureInPictureParams) {
        super.applyNewPictureInPictureParams(pictureInPictureParams);
        if (PipUtils.aspectRatioChanged(pictureInPictureParams.getExpandedAspectRatioFloat(), this.mPictureInPictureParams.getExpandedAspectRatioFloat())) {
            this.mPipParamsChangedForwarder.notifyExpandedAspectRatioChanged(pictureInPictureParams.getExpandedAspectRatioFloat());
        }
        if (!Objects.equals(pictureInPictureParams.getTitle(), this.mPictureInPictureParams.getTitle())) {
            this.mPipParamsChangedForwarder.notifyTitleChanged(pictureInPictureParams.getTitle());
        }
        if (!Objects.equals(pictureInPictureParams.getSubtitle(), this.mPictureInPictureParams.getSubtitle())) {
            this.mPipParamsChangedForwarder.notifySubtitleChanged(pictureInPictureParams.getSubtitle());
        }
    }
}
