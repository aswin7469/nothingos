package com.android.p019wm.shell.stagesplit;

import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.util.FrameworkStatsLog;

/* renamed from: com.android.wm.shell.stagesplit.SplitscreenEventLogger */
public class SplitscreenEventLogger {
    private int mDragEnterPosition;
    private InstanceId mDragEnterSessionId;
    private final InstanceIdSequence mIdSequence = new InstanceIdSequence(Integer.MAX_VALUE);
    private int mLastMainStagePosition = -1;
    private int mLastMainStageUid = -1;
    private int mLastSideStagePosition = -1;
    private int mLastSideStageUid = -1;
    private float mLastSplitRatio = -1.0f;
    private InstanceId mLoggerSessionId;

    private int getMainStagePositionFromSplitPosition(int i, boolean z) {
        if (i == -1) {
            return 0;
        }
        return z ? i == 0 ? 1 : 2 : i == 0 ? 3 : 4;
    }

    private int getSideStagePositionFromSplitPosition(int i, boolean z) {
        if (i == -1) {
            return 0;
        }
        return z ? i == 0 ? 1 : 2 : i == 0 ? 3 : 4;
    }

    public int getDragEnterReasonFromSplitPosition(int i, boolean z) {
        return z ? i == 0 ? 2 : 4 : i == 0 ? 3 : 5;
    }

    public boolean hasStartedSession() {
        return this.mLoggerSessionId != null;
    }

    public void enterRequestedByDrag(int i, InstanceId instanceId) {
        this.mDragEnterPosition = i;
        this.mDragEnterSessionId = instanceId;
    }

    public void logEnter(float f, int i, int i2, int i3, int i4, boolean z) {
        boolean z2 = z;
        this.mLoggerSessionId = this.mIdSequence.newInstanceId();
        int i5 = this.mDragEnterPosition;
        int dragEnterReasonFromSplitPosition = i5 != -1 ? getDragEnterReasonFromSplitPosition(i5, z2) : 1;
        updateMainStageState(getMainStagePositionFromSplitPosition(i, z2), i2);
        updateSideStageState(getSideStagePositionFromSplitPosition(i3, z2), i4);
        updateSplitRatioState(f);
        int i6 = this.mLastMainStagePosition;
        int i7 = this.mLastMainStageUid;
        int i8 = this.mLastSideStagePosition;
        int i9 = this.mLastSideStageUid;
        InstanceId instanceId = this.mDragEnterSessionId;
        FrameworkStatsLog.write(388, 1, dragEnterReasonFromSplitPosition, 0, f, i6, i7, i8, i9, instanceId != null ? instanceId.getId() : 0, this.mLoggerSessionId.getId());
    }

    public void logExit(int i, int i2, int i3, int i4, int i5, boolean z) {
        int i6 = i2;
        int i7 = i4;
        boolean z2 = z;
        if (this.mLoggerSessionId != null) {
            if ((i6 == -1 || i7 == -1) && (i3 == 0 || i5 == 0)) {
                FrameworkStatsLog.write(388, 2, 0, i, 0.0f, getMainStagePositionFromSplitPosition(i6, z2), i3, getSideStagePositionFromSplitPosition(i7, z2), i5, 0, this.mLoggerSessionId.getId());
                this.mLoggerSessionId = null;
                this.mDragEnterPosition = -1;
                this.mDragEnterSessionId = null;
                this.mLastMainStagePosition = -1;
                this.mLastMainStageUid = -1;
                this.mLastSideStagePosition = -1;
                this.mLastSideStageUid = -1;
                return;
            }
            throw new IllegalArgumentException("Only main or side stage should be set");
        }
    }

    public void logMainStageAppChange(int i, int i2, boolean z) {
        if (this.mLoggerSessionId != null && updateMainStageState(getMainStagePositionFromSplitPosition(i, z), i2)) {
            FrameworkStatsLog.write(388, 3, 0, 0, 0.0f, this.mLastMainStagePosition, this.mLastMainStageUid, 0, 0, 0, this.mLoggerSessionId.getId());
        }
    }

    public void logSideStageAppChange(int i, int i2, boolean z) {
        if (this.mLoggerSessionId != null && updateSideStageState(getSideStagePositionFromSplitPosition(i, z), i2)) {
            FrameworkStatsLog.write(388, 3, 0, 0, 0.0f, 0, 0, this.mLastSideStagePosition, this.mLastSideStageUid, 0, this.mLoggerSessionId.getId());
        }
    }

    public void logResize(float f) {
        if (this.mLoggerSessionId != null && f > 0.0f && f < 1.0f && updateSplitRatioState(f)) {
            FrameworkStatsLog.write(388, 4, 0, 0, this.mLastSplitRatio, 0, 0, 0, 0, 0, this.mLoggerSessionId.getId());
        }
    }

    public void logSwap(int i, int i2, int i3, int i4, boolean z) {
        boolean z2 = z;
        if (this.mLoggerSessionId != null) {
            int i5 = i;
            int i6 = i2;
            updateMainStageState(getMainStagePositionFromSplitPosition(i, z2), i2);
            updateSideStageState(getSideStagePositionFromSplitPosition(i3, z2), i4);
            FrameworkStatsLog.write(388, 5, 0, 0, 0.0f, this.mLastMainStagePosition, this.mLastMainStageUid, this.mLastSideStagePosition, this.mLastSideStageUid, 0, this.mLoggerSessionId.getId());
        }
    }

    private boolean updateMainStageState(int i, int i2) {
        if (!((this.mLastMainStagePosition == i && this.mLastMainStageUid == i2) ? false : true)) {
            return false;
        }
        this.mLastMainStagePosition = i;
        this.mLastMainStageUid = i2;
        return true;
    }

    private boolean updateSideStageState(int i, int i2) {
        if (!((this.mLastSideStagePosition == i && this.mLastSideStageUid == i2) ? false : true)) {
            return false;
        }
        this.mLastSideStagePosition = i;
        this.mLastSideStageUid = i2;
        return true;
    }

    private boolean updateSplitRatioState(float f) {
        if (!(Float.compare(this.mLastSplitRatio, f) != 0)) {
            return false;
        }
        this.mLastSplitRatio = f;
        return true;
    }
}
