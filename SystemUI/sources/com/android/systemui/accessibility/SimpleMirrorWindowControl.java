package com.android.systemui.accessibility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.C1894R;
import com.android.systemui.accessibility.MirrorWindowControl;

class SimpleMirrorWindowControl extends MirrorWindowControl implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private static final int MOVE_FRAME_DURATION_MS = 100;
    private static final String TAG = "SimpleMirrorWindowControl";
    private final Handler mHandler;
    private boolean mIsDragState;
    private PointF mLastDrag = new PointF();
    private final int mMoveFrameAmountLong;
    private final int mMoveFrameAmountShort;
    private MoveWindowTask mMoveWindowTask;
    private boolean mShouldSetTouchStart;

    SimpleMirrorWindowControl(Context context, Handler handler) {
        super(context);
        this.mHandler = handler;
        Resources resources = context.getResources();
        this.mMoveFrameAmountShort = resources.getDimensionPixelSize(C1894R.dimen.magnification_frame_move_short);
        this.mMoveFrameAmountLong = resources.getDimensionPixelSize(C1894R.dimen.magnification_frame_move_long);
    }

    /* access modifiers changed from: package-private */
    public String getWindowTitle() {
        return this.mContext.getString(C1894R.string.magnification_controls_title);
    }

    /* access modifiers changed from: package-private */
    public View onCreateView(LayoutInflater layoutInflater, Point point) {
        View inflate = layoutInflater.inflate(C1894R.layout.magnifier_controllers, (ViewGroup) null);
        View findViewById = inflate.findViewById(C1894R.C1898id.left_control);
        View findViewById2 = inflate.findViewById(C1894R.C1898id.up_control);
        View findViewById3 = inflate.findViewById(C1894R.C1898id.right_control);
        View findViewById4 = inflate.findViewById(C1894R.C1898id.down_control);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        findViewById3.setOnClickListener(this);
        findViewById4.setOnClickListener(this);
        findViewById.setOnLongClickListener(this);
        findViewById2.setOnLongClickListener(this);
        findViewById3.setOnLongClickListener(this);
        findViewById4.setOnLongClickListener(this);
        findViewById.setOnTouchListener(this);
        findViewById2.setOnTouchListener(this);
        findViewById3.setOnTouchListener(this);
        findViewById4.setOnTouchListener(this);
        inflate.setOnTouchListener(this);
        inflate.setOnLongClickListener(new SimpleMirrorWindowControl$$ExternalSyntheticLambda0(this));
        return inflate;
    }

    private Point findOffset(View view, int i) {
        this.mTmpPoint.set(0, 0);
        if (view.getId() == C1894R.C1898id.left_control) {
            this.mTmpPoint.x = -i;
        } else if (view.getId() == C1894R.C1898id.up_control) {
            this.mTmpPoint.y = -i;
        } else if (view.getId() == C1894R.C1898id.right_control) {
            this.mTmpPoint.x = i;
        } else if (view.getId() == C1894R.C1898id.down_control) {
            this.mTmpPoint.y = i;
        } else {
            Log.w(TAG, "findOffset move is zero ");
        }
        return this.mTmpPoint;
    }

    public void onClick(View view) {
        if (this.mMirrorWindowDelegate != null) {
            Point findOffset = findOffset(view, this.mMoveFrameAmountShort);
            this.mMirrorWindowDelegate.move(findOffset.x, findOffset.y);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        MoveWindowTask moveWindowTask;
        if (handleDragState(motionEvent)) {
            return true;
        }
        int action = motionEvent.getAction();
        if ((action != 1 && action != 3) || (moveWindowTask = this.mMoveWindowTask) == null) {
            return false;
        }
        moveWindowTask.cancel();
        this.mMoveWindowTask = null;
        return false;
    }

    public boolean onLongClick(View view) {
        Point findOffset = findOffset(view, this.mMoveFrameAmountLong);
        MoveWindowTask moveWindowTask = new MoveWindowTask(this.mMirrorWindowDelegate, this.mHandler, findOffset.x, findOffset.y, 100);
        this.mMoveWindowTask = moveWindowTask;
        moveWindowTask.schedule();
        return true;
    }

    /* access modifiers changed from: private */
    public boolean onViewRootLongClick(View view) {
        this.mIsDragState = true;
        this.mShouldSetTouchStart = true;
        return true;
    }

    private boolean handleDragState(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 1) {
            if (action != 2) {
                if (action != 3) {
                    return false;
                }
            } else if (!this.mIsDragState) {
                return false;
            } else {
                if (this.mShouldSetTouchStart) {
                    this.mLastDrag.set(motionEvent.getRawX(), motionEvent.getRawY());
                    this.mShouldSetTouchStart = false;
                }
                move((int) (motionEvent.getRawX() - this.mLastDrag.x), (int) (motionEvent.getRawY() - this.mLastDrag.y));
                this.mLastDrag.set(motionEvent.getRawX(), motionEvent.getRawY());
                return true;
            }
        }
        if (!this.mIsDragState) {
            return false;
        }
        this.mIsDragState = false;
        return true;
    }

    static class MoveWindowTask implements Runnable {
        private boolean mCancel;
        private final Handler mHandler;
        private final MirrorWindowControl.MirrorWindowDelegate mMirrorWindowDelegate;
        private long mPeriod;
        private final int mXOffset;
        private final int mYOffset;

        MoveWindowTask(MirrorWindowControl.MirrorWindowDelegate mirrorWindowDelegate, Handler handler, int i, int i2, long j) {
            this.mMirrorWindowDelegate = mirrorWindowDelegate;
            this.mXOffset = i;
            this.mYOffset = i2;
            this.mHandler = handler;
            this.mPeriod = j;
        }

        public void run() {
            if (!this.mCancel) {
                this.mMirrorWindowDelegate.move(this.mXOffset, this.mYOffset);
                schedule();
            }
        }

        /* access modifiers changed from: package-private */
        public void schedule() {
            this.mHandler.postDelayed(this, this.mPeriod);
            this.mCancel = false;
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            this.mHandler.removeCallbacks(this);
            this.mCancel = true;
        }
    }
}
