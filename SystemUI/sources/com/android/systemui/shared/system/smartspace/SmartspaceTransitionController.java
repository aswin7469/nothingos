package com.android.systemui.shared.system.smartspace;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SmartspaceTransitionController.kt */
/* loaded from: classes.dex */
public final class SmartspaceTransitionController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private ISmartspaceCallback launcherSmartspace;
    @Nullable
    private View lockscreenSmartspace;
    @Nullable
    private SmartspaceState mLauncherSmartspaceState;
    @NotNull
    private final SmartspaceTransitionController$ISmartspaceTransitionController$1 ISmartspaceTransitionController = new ISmartspaceTransitionController.Stub() { // from class: com.android.systemui.shared.system.smartspace.SmartspaceTransitionController$ISmartspaceTransitionController$1
        @Override // com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController
        public void setSmartspace(@Nullable ISmartspaceCallback iSmartspaceCallback) {
            SmartspaceTransitionController.this.setLauncherSmartspace(iSmartspaceCallback);
            SmartspaceTransitionController.this.updateLauncherSmartSpaceState();
        }
    };
    @NotNull
    private final Rect smartspaceOriginBounds = new Rect();
    @NotNull
    private final Rect smartspaceDestinationBounds = new Rect();

    @Nullable
    public final ISmartspaceCallback getLauncherSmartspace() {
        return this.launcherSmartspace;
    }

    public final void setLauncherSmartspace(@Nullable ISmartspaceCallback iSmartspaceCallback) {
        this.launcherSmartspace = iSmartspaceCallback;
    }

    @Nullable
    public final View getLockscreenSmartspace() {
        return this.lockscreenSmartspace;
    }

    public final void setLockscreenSmartspace(@Nullable View view) {
        this.lockscreenSmartspace = view;
    }

    public final void setMLauncherSmartspaceState(@Nullable SmartspaceState smartspaceState) {
        this.mLauncherSmartspaceState = smartspaceState;
    }

    @NotNull
    public final ISmartspaceTransitionController createExternalInterface() {
        return this.ISmartspaceTransitionController;
    }

    @Nullable
    public final SmartspaceState updateLauncherSmartSpaceState() {
        ISmartspaceCallback iSmartspaceCallback = this.launcherSmartspace;
        SmartspaceState smartspaceState = iSmartspaceCallback == null ? null : iSmartspaceCallback.getSmartspaceState();
        setMLauncherSmartspaceState(smartspaceState);
        return smartspaceState;
    }

    public final void prepareForUnlockTransition() {
        SmartspaceState updateLauncherSmartSpaceState = updateLauncherSmartSpaceState();
        if ((updateLauncherSmartSpaceState == null ? null : updateLauncherSmartSpaceState.getBoundsOnScreen()) == null || getLockscreenSmartspace() == null) {
            return;
        }
        View lockscreenSmartspace = getLockscreenSmartspace();
        Intrinsics.checkNotNull(lockscreenSmartspace);
        lockscreenSmartspace.getBoundsOnScreen(this.smartspaceOriginBounds);
        Rect rect = this.smartspaceDestinationBounds;
        rect.set(updateLauncherSmartSpaceState.getBoundsOnScreen());
        View lockscreenSmartspace2 = getLockscreenSmartspace();
        Intrinsics.checkNotNull(lockscreenSmartspace2);
        View lockscreenSmartspace3 = getLockscreenSmartspace();
        Intrinsics.checkNotNull(lockscreenSmartspace3);
        rect.offset(-lockscreenSmartspace2.getPaddingLeft(), -lockscreenSmartspace3.getPaddingTop());
    }

    public final void setProgressToDestinationBounds(float f) {
        if (!isSmartspaceTransitionPossible()) {
            return;
        }
        float min = Math.min(1.0f, f);
        Rect rect = this.smartspaceDestinationBounds;
        int i = rect.left;
        Rect rect2 = this.smartspaceOriginBounds;
        float f2 = (i - rect2.left) * min;
        float f3 = (rect.top - rect2.top) * min;
        Rect rect3 = new Rect();
        View lockscreenSmartspace = getLockscreenSmartspace();
        Intrinsics.checkNotNull(lockscreenSmartspace);
        lockscreenSmartspace.getBoundsOnScreen(rect3);
        Rect rect4 = this.smartspaceOriginBounds;
        float f4 = (rect4.left + f2) - rect3.left;
        float f5 = (rect4.top + f3) - rect3.top;
        View view = this.lockscreenSmartspace;
        Intrinsics.checkNotNull(view);
        view.setTranslationX(view.getTranslationX() + f4);
        view.setTranslationY(view.getTranslationY() + f5);
    }

    public final boolean isSmartspaceTransitionPossible() {
        SmartspaceState smartspaceState = this.mLauncherSmartspaceState;
        Rect boundsOnScreen = smartspaceState == null ? null : smartspaceState.getBoundsOnScreen();
        return Companion.isLauncherUnderneath() && !(boundsOnScreen == null ? true : boundsOnScreen.isEmpty());
    }

    /* compiled from: SmartspaceTransitionController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isLauncherUnderneath() {
            ComponentName componentName;
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
            String str = null;
            if (runningTask != null && (componentName = runningTask.topActivity) != null) {
                str = componentName.getClassName();
            }
            if (str == null) {
                return false;
            }
            return str.equals("com.google.android.apps.nexuslauncher.NexusLauncherActivity");
        }
    }
}
