package com.android.p019wm.shell.pip;

import android.app.ActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.PictureInPictureUiState;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.ArraySet;
import android.util.Size;
import com.android.internal.protolog.common.ProtoLog;
import com.android.internal.util.function.TriConsumer;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.pip.PipBoundsState */
public class PipBoundsState {
    public static final int STASH_TYPE_BOTTOM = 3;
    public static final int STASH_TYPE_LEFT = 1;
    public static final int STASH_TYPE_NONE = 0;
    public static final int STASH_TYPE_RIGHT = 2;
    public static final int STASH_TYPE_TOP = 4;
    private static final String TAG = "PipBoundsState";
    private float mAspectRatio;
    private final Rect mBounds = new Rect();
    private final Context mContext;
    private int mDisplayId = 0;
    private final DisplayLayout mDisplayLayout = new DisplayLayout();
    private final Rect mExpandedBounds = new Rect();
    private final Rect mExpandedMovementBounds = new Rect();
    private boolean mHasUserResizedPip;
    private int mImeHeight;
    private boolean mIsImeShowing;
    private boolean mIsShelfShowing;
    private ComponentName mLastPipComponentName;
    private final Point mMaxSize = new Point();
    private int mMinEdgeSize;
    private final Point mMinSize = new Point();
    private final MotionBoundsState mMotionBoundsState = new MotionBoundsState();
    private final Rect mMovementBounds = new Rect();
    private final Rect mNormalBounds = new Rect();
    private final Rect mNormalMovementBounds = new Rect();
    private Runnable mOnMinimalSizeChangeCallback;
    private List<Consumer<Rect>> mOnPipExclusionBoundsChangeCallbacks = new ArrayList();
    private TriConsumer<Boolean, Integer, Boolean> mOnShelfVisibilityChangeCallback;
    private Size mOverrideMinSize;
    private PipReentryState mPipReentryState;
    private final Set<Rect> mRestrictedKeepClearAreas = new ArraySet();
    private int mShelfHeight;
    private int mStashOffset;
    private int mStashedState = 0;
    private final Set<Rect> mUnrestrictedKeepClearAreas = new ArraySet();

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.android.wm.shell.pip.PipBoundsState$StashType */
    public @interface StashType {
    }

    public PipBoundsState(Context context) {
        this.mContext = context;
        reloadResources();
    }

    public void onConfigurationChanged() {
        reloadResources();
    }

    private void reloadResources() {
        this.mStashOffset = this.mContext.getResources().getDimensionPixelSize(C3343R.dimen.pip_stash_offset);
    }

    public void setBounds(Rect rect) {
        this.mBounds.set(rect);
        for (Consumer<Rect> accept : this.mOnPipExclusionBoundsChangeCallbacks) {
            accept.accept(rect);
        }
    }

    public Rect getBounds() {
        return new Rect(this.mBounds);
    }

    public Rect getMovementBounds() {
        return this.mMovementBounds;
    }

    public void setNormalBounds(Rect rect) {
        this.mNormalBounds.set(rect);
    }

    public Rect getNormalBounds() {
        return this.mNormalBounds;
    }

    public void setExpandedBounds(Rect rect) {
        this.mExpandedBounds.set(rect);
    }

    public Rect getExpandedBounds() {
        return this.mExpandedBounds;
    }

    public void setNormalMovementBounds(Rect rect) {
        this.mNormalMovementBounds.set(rect);
    }

    public Rect getNormalMovementBounds() {
        return this.mNormalMovementBounds;
    }

    public void setExpandedMovementBounds(Rect rect) {
        this.mExpandedMovementBounds.set(rect);
    }

    public void setMaxSize(int i, int i2) {
        this.mMaxSize.set(i, i2);
    }

    public void setMinSize(int i, int i2) {
        this.mMinSize.set(i, i2);
    }

    public Point getMaxSize() {
        return this.mMaxSize;
    }

    public Point getMinSize() {
        return this.mMinSize;
    }

    public Rect getExpandedMovementBounds() {
        return this.mExpandedMovementBounds;
    }

    public void setStashed(int i) {
        if (this.mStashedState != i) {
            this.mStashedState = i;
            try {
                ActivityTaskManager.getService().onPictureInPictureStateChanged(new PictureInPictureUiState(i != 0));
            } catch (RemoteException unused) {
                ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Unable to set alert PiP state change.", new Object[]{TAG});
            }
        }
    }

    public int getStashedState() {
        return this.mStashedState;
    }

    public boolean isStashed() {
        return this.mStashedState != 0;
    }

    public int getStashOffset() {
        return this.mStashOffset;
    }

    public void setAspectRatio(float f) {
        this.mAspectRatio = f;
    }

    public float getAspectRatio() {
        return this.mAspectRatio;
    }

    public void saveReentryState(Size size, float f) {
        this.mPipReentryState = new PipReentryState(size, f);
    }

    public PipReentryState getReentryState() {
        return this.mPipReentryState;
    }

    public void setLastPipComponentName(ComponentName componentName) {
        boolean z = !Objects.equals(this.mLastPipComponentName, componentName);
        this.mLastPipComponentName = componentName;
        if (z) {
            clearReentryState();
            setHasUserResizedPip(false);
        }
    }

    public ComponentName getLastPipComponentName() {
        return this.mLastPipComponentName;
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    public void setDisplayId(int i) {
        this.mDisplayId = i;
    }

    public Rect getDisplayBounds() {
        return new Rect(0, 0, this.mDisplayLayout.width(), this.mDisplayLayout.height());
    }

    public void setDisplayLayout(DisplayLayout displayLayout) {
        this.mDisplayLayout.set(displayLayout);
    }

    public DisplayLayout getDisplayLayout() {
        return this.mDisplayLayout;
    }

    /* access modifiers changed from: package-private */
    public void clearReentryState() {
        this.mPipReentryState = null;
    }

    public void setMinEdgeSize(int i) {
        this.mMinEdgeSize = i;
    }

    public int getMinEdgeSize() {
        return this.mMinEdgeSize;
    }

    public void setOverrideMinSize(Size size) {
        Runnable runnable;
        boolean z = !Objects.equals(size, this.mOverrideMinSize);
        this.mOverrideMinSize = size;
        if (z && (runnable = this.mOnMinimalSizeChangeCallback) != null) {
            runnable.run();
        }
    }

    public Size getOverrideMinSize() {
        return this.mOverrideMinSize;
    }

    public int getOverrideMinEdgeSize() {
        Size size = this.mOverrideMinSize;
        if (size == null) {
            return 0;
        }
        return Math.min(size.getWidth(), this.mOverrideMinSize.getHeight());
    }

    public MotionBoundsState getMotionBoundsState() {
        return this.mMotionBoundsState;
    }

    public void setImeVisibility(boolean z, int i) {
        this.mIsImeShowing = z;
        this.mImeHeight = i;
    }

    public boolean isImeShowing() {
        return this.mIsImeShowing;
    }

    public int getImeHeight() {
        return this.mImeHeight;
    }

    public void setShelfVisibility(boolean z, int i) {
        setShelfVisibility(z, i, true);
    }

    public void setShelfVisibility(boolean z, int i, boolean z2) {
        if ((z && i > 0) != this.mIsShelfShowing || i != this.mShelfHeight) {
            this.mIsShelfShowing = z;
            this.mShelfHeight = i;
            TriConsumer<Boolean, Integer, Boolean> triConsumer = this.mOnShelfVisibilityChangeCallback;
            if (triConsumer != null) {
                triConsumer.accept(Boolean.valueOf(z), Integer.valueOf(this.mShelfHeight), Boolean.valueOf(z2));
            }
        }
    }

    public void setKeepClearAreas(Set<Rect> set, Set<Rect> set2) {
        this.mRestrictedKeepClearAreas.clear();
        this.mRestrictedKeepClearAreas.addAll(set);
        this.mUnrestrictedKeepClearAreas.clear();
        this.mUnrestrictedKeepClearAreas.addAll(set2);
    }

    public Set<Rect> getRestrictedKeepClearAreas() {
        return this.mRestrictedKeepClearAreas;
    }

    public Set<Rect> getUnrestrictedKeepClearAreas() {
        return this.mUnrestrictedKeepClearAreas;
    }

    public void setBoundsStateForEntry(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, PipBoundsAlgorithm pipBoundsAlgorithm) {
        setLastPipComponentName(componentName);
        setAspectRatio(pipBoundsAlgorithm.getAspectRatioOrDefault(pictureInPictureParams));
        setOverrideMinSize(pipBoundsAlgorithm.getMinimalSize(activityInfo));
    }

    public boolean isShelfShowing() {
        return this.mIsShelfShowing;
    }

    public int getShelfHeight() {
        return this.mShelfHeight;
    }

    public boolean hasUserResizedPip() {
        return this.mHasUserResizedPip;
    }

    public void setHasUserResizedPip(boolean z) {
        this.mHasUserResizedPip = z;
    }

    public void setOnMinimalSizeChangeCallback(Runnable runnable) {
        this.mOnMinimalSizeChangeCallback = runnable;
    }

    public void setOnShelfVisibilityChangeCallback(TriConsumer<Boolean, Integer, Boolean> triConsumer) {
        this.mOnShelfVisibilityChangeCallback = triConsumer;
    }

    public void addPipExclusionBoundsChangeCallback(Consumer<Rect> consumer) {
        this.mOnPipExclusionBoundsChangeCallbacks.add(consumer);
        for (Consumer<Rect> accept : this.mOnPipExclusionBoundsChangeCallbacks) {
            accept.accept(getBounds());
        }
    }

    public void removePipExclusionBoundsChangeCallback(Consumer<Rect> consumer) {
        this.mOnPipExclusionBoundsChangeCallbacks.remove((Object) consumer);
    }

    /* renamed from: com.android.wm.shell.pip.PipBoundsState$MotionBoundsState */
    public static class MotionBoundsState {
        private final Rect mAnimatingToBounds = new Rect();
        private final Rect mBoundsInMotion = new Rect();

        public boolean isInMotion() {
            return !this.mBoundsInMotion.isEmpty();
        }

        public void setBoundsInMotion(Rect rect) {
            this.mBoundsInMotion.set(rect);
        }

        public void setAnimatingToBounds(Rect rect) {
            this.mAnimatingToBounds.set(rect);
        }

        public void onAllAnimationsEnded() {
            this.mBoundsInMotion.setEmpty();
        }

        public void onPhysicsAnimationEnded() {
            this.mAnimatingToBounds.setEmpty();
        }

        public Rect getBoundsInMotion() {
            return this.mBoundsInMotion;
        }

        public Rect getAnimatingToBounds() {
            return this.mAnimatingToBounds;
        }

        /* access modifiers changed from: package-private */
        public void dump(PrintWriter printWriter, String str) {
            String str2 = str + "  ";
            printWriter.println(str + MotionBoundsState.class.getSimpleName());
            printWriter.println(str2 + "mBoundsInMotion=" + this.mBoundsInMotion);
            printWriter.println(str2 + "mAnimatingToBounds=" + this.mAnimatingToBounds);
        }
    }

    /* renamed from: com.android.wm.shell.pip.PipBoundsState$PipReentryState */
    static final class PipReentryState {
        private static final String TAG = "PipReentryState";
        private final Size mSize;
        private final float mSnapFraction;

        PipReentryState(Size size, float f) {
            this.mSize = size;
            this.mSnapFraction = f;
        }

        /* access modifiers changed from: package-private */
        public Size getSize() {
            return this.mSize;
        }

        /* access modifiers changed from: package-private */
        public float getSnapFraction() {
            return this.mSnapFraction;
        }

        /* access modifiers changed from: package-private */
        public void dump(PrintWriter printWriter, String str) {
            String str2 = str + "  ";
            printWriter.println(str + TAG);
            printWriter.println(str2 + "mSize=" + this.mSize);
            printWriter.println(str2 + "mSnapFraction=" + this.mSnapFraction);
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        printWriter.println(str2 + "mBounds=" + this.mBounds);
        printWriter.println(str2 + "mNormalBounds=" + this.mNormalBounds);
        printWriter.println(str2 + "mExpandedBounds=" + this.mExpandedBounds);
        printWriter.println(str2 + "mMovementBounds=" + this.mMovementBounds);
        printWriter.println(str2 + "mNormalMovementBounds=" + this.mNormalMovementBounds);
        printWriter.println(str2 + "mExpandedMovementBounds=" + this.mExpandedMovementBounds);
        printWriter.println(str2 + "mLastPipComponentName=" + this.mLastPipComponentName);
        printWriter.println(str2 + "mAspectRatio=" + this.mAspectRatio);
        printWriter.println(str2 + "mDisplayId=" + this.mDisplayId);
        printWriter.println(str2 + "mDisplayLayout=" + this.mDisplayLayout);
        printWriter.println(str2 + "mStashedState=" + this.mStashedState);
        printWriter.println(str2 + "mStashOffset=" + this.mStashOffset);
        printWriter.println(str2 + "mMinEdgeSize=" + this.mMinEdgeSize);
        printWriter.println(str2 + "mOverrideMinSize=" + this.mOverrideMinSize);
        printWriter.println(str2 + "mIsImeShowing=" + this.mIsImeShowing);
        printWriter.println(str2 + "mImeHeight=" + this.mImeHeight);
        printWriter.println(str2 + "mIsShelfShowing=" + this.mIsShelfShowing);
        printWriter.println(str2 + "mShelfHeight=" + this.mShelfHeight);
        PipReentryState pipReentryState = this.mPipReentryState;
        if (pipReentryState == null) {
            printWriter.println(str2 + "mPipReentryState=null");
        } else {
            pipReentryState.dump(printWriter, str2);
        }
        this.mMotionBoundsState.dump(printWriter, str2);
    }
}
