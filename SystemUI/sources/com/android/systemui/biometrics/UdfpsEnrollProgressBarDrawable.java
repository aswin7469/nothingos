package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class UdfpsEnrollProgressBarDrawable extends Drawable {
    private final Context mContext;
    private UdfpsEnrollHelper mEnrollHelper;
    private List<UdfpsEnrollProgressBarSegment> mSegments = new ArrayList();
    private int mTotalSteps = 1;
    private int mProgressSteps = 0;
    private boolean mIsShowingHelp = false;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public UdfpsEnrollProgressBarDrawable(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mEnrollHelper = udfpsEnrollHelper;
        if (udfpsEnrollHelper != null) {
            int stageCount = udfpsEnrollHelper.getStageCount();
            this.mSegments = new ArrayList(stageCount);
            float f = 6.0f;
            float f2 = (360.0f / stageCount) - 12.0f;
            Runnable runnable = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsEnrollProgressBarDrawable.this.invalidateSelf();
                }
            };
            for (int i = 0; i < stageCount; i++) {
                this.mSegments.add(new UdfpsEnrollProgressBarSegment(this.mContext, getBounds(), f, f2, 12.0f, runnable));
                f += f2 + 12.0f;
            }
            invalidateSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEnrollmentProgress(int i, int i2) {
        this.mTotalSteps = i2;
        updateState(getProgressSteps(i, i2), false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEnrollmentHelp(int i, int i2) {
        updateState(getProgressSteps(i, i2), true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLastStepAcquired() {
        updateState(this.mTotalSteps, false);
    }

    private static int getProgressSteps(int i, int i2) {
        return Math.max(1, i2 - i);
    }

    private void updateState(int i, boolean z) {
        updateProgress(i);
        updateFillColor(z);
    }

    private void updateProgress(int i) {
        if (this.mProgressSteps == i) {
            return;
        }
        this.mProgressSteps = i;
        if (this.mEnrollHelper == null) {
            Log.e("UdfpsProgressBar", "updateState: UDFPS enroll helper was null");
            return;
        }
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= this.mSegments.size()) {
                break;
            }
            UdfpsEnrollProgressBarSegment udfpsEnrollProgressBarSegment = this.mSegments.get(i2);
            int stageThresholdSteps = this.mEnrollHelper.getStageThresholdSteps(this.mTotalSteps, i2);
            if (i >= stageThresholdSteps && udfpsEnrollProgressBarSegment.getProgress() < 1.0f) {
                udfpsEnrollProgressBarSegment.updateProgress(1.0f);
                break;
            } else if (i >= i3 && i < stageThresholdSteps) {
                udfpsEnrollProgressBarSegment.updateProgress((i - i3) / (stageThresholdSteps - i3));
                break;
            } else {
                i2++;
                i3 = stageThresholdSteps;
            }
        }
        if (i >= this.mTotalSteps) {
            for (UdfpsEnrollProgressBarSegment udfpsEnrollProgressBarSegment2 : this.mSegments) {
                udfpsEnrollProgressBarSegment2.startCompletionAnimation();
            }
            return;
        }
        for (UdfpsEnrollProgressBarSegment udfpsEnrollProgressBarSegment3 : this.mSegments) {
            udfpsEnrollProgressBarSegment3.cancelCompletionAnimation();
        }
    }

    private void updateFillColor(boolean z) {
        if (this.mIsShowingHelp == z) {
            return;
        }
        this.mIsShowingHelp = z;
        for (UdfpsEnrollProgressBarSegment udfpsEnrollProgressBarSegment : this.mSegments) {
            udfpsEnrollProgressBarSegment.updateFillColor(z);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Log.d("UdfpsProgressBar", "setEnrollmentProgress: draw");
        canvas.save();
        canvas.rotate(-90.0f, getBounds().centerX(), getBounds().centerY());
        for (UdfpsEnrollProgressBarSegment udfpsEnrollProgressBarSegment : this.mSegments) {
            udfpsEnrollProgressBarSegment.draw(canvas);
        }
        canvas.restore();
    }
}
