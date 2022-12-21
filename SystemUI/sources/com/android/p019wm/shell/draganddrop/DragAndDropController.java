package com.android.p019wm.shell.draganddrop;

import android.content.ClipDescription;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.protolog.common.ProtoLog;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
import java.util.Optional;

/* renamed from: com.android.wm.shell.draganddrop.DragAndDropController */
public class DragAndDropController implements DisplayController.OnDisplaysChangedListener, View.OnDragListener {
    private static final String TAG = "DragAndDropController";
    private final Context mContext;
    private final DisplayController mDisplayController;
    private final SparseArray<PerDisplay> mDisplayDropTargets = new SparseArray<>();
    private final IconProvider mIconProvider;
    private DragAndDropImpl mImpl;
    private ArrayList<DragAndDropListener> mListeners = new ArrayList<>();
    private final DragAndDropEventLogger mLogger;
    /* access modifiers changed from: private */
    public ShellExecutor mMainExecutor;
    private SplitScreenController mSplitScreen;
    private final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropController$DragAndDropListener */
    public interface DragAndDropListener {
        void onDragStarted();
    }

    public DragAndDropController(Context context, DisplayController displayController, UiEventLogger uiEventLogger, IconProvider iconProvider, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mDisplayController = displayController;
        this.mLogger = new DragAndDropEventLogger(uiEventLogger);
        this.mIconProvider = iconProvider;
        this.mMainExecutor = shellExecutor;
        this.mImpl = new DragAndDropImpl();
    }

    public DragAndDrop asDragAndDrop() {
        return this.mImpl;
    }

    public void initialize(Optional<SplitScreenController> optional) {
        this.mSplitScreen = optional.orElse(null);
        this.mDisplayController.addDisplayWindowListener(this);
    }

    public void addListener(DragAndDropListener dragAndDropListener) {
        this.mListeners.add(dragAndDropListener);
    }

    public void removeListener(DragAndDropListener dragAndDropListener) {
        this.mListeners.remove((Object) dragAndDropListener);
    }

    private void notifyListeners() {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onDragStarted();
        }
    }

    public void onDisplayAdded(int i) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Display added: %d", new Object[]{Integer.valueOf(i)});
        if (i == 0) {
            Context createWindowContext = this.mDisplayController.getDisplayContext(i).createWindowContext(2038, (Bundle) null);
            WindowManager windowManager = (WindowManager) createWindowContext.getSystemService(WindowManager.class);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2038, 16777224, -3);
            layoutParams.privateFlags |= -2147483568;
            layoutParams.layoutInDisplayCutoutMode = 3;
            layoutParams.setFitInsetsTypes(0);
            layoutParams.setTitle("ShellDropTarget");
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(createWindowContext).inflate(C3343R.layout.global_drop_target, (ViewGroup) null);
            frameLayout.setOnDragListener(this);
            frameLayout.setVisibility(4);
            DragLayout dragLayout = new DragLayout(createWindowContext, this.mSplitScreen, this.mIconProvider);
            frameLayout.addView(dragLayout, new FrameLayout.LayoutParams(-1, -1));
            try {
                windowManager.addView(frameLayout, layoutParams);
                addDisplayDropTarget(i, createWindowContext, windowManager, frameLayout, dragLayout);
            } catch (WindowManager.InvalidDisplayException unused) {
                Slog.w(TAG, "Unable to add view for display id: " + i);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addDisplayDropTarget(int i, Context context, WindowManager windowManager, FrameLayout frameLayout, DragLayout dragLayout) {
        this.mDisplayDropTargets.put(i, new PerDisplay(i, context, windowManager, frameLayout, dragLayout));
    }

    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Display changed: %d", new Object[]{Integer.valueOf(i)});
        PerDisplay perDisplay = this.mDisplayDropTargets.get(i);
        if (perDisplay != null) {
            perDisplay.rootView.requestApplyInsets();
        }
    }

    public void onDisplayRemoved(int i) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Display removed: %d", new Object[]{Integer.valueOf(i)});
        PerDisplay perDisplay = this.mDisplayDropTargets.get(i);
        if (perDisplay != null) {
            perDisplay.f405wm.removeViewImmediate(perDisplay.rootView);
            this.mDisplayDropTargets.remove(i);
        }
    }

    public boolean onDrag(View view, DragEvent dragEvent) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Drag event: action=%s x=%f y=%f xOffset=%f yOffset=%f", new Object[]{DragEvent.actionToString(dragEvent.getAction()), Float.valueOf(dragEvent.getX()), Float.valueOf(dragEvent.getY()), Float.valueOf(dragEvent.getOffsetX()), Float.valueOf(dragEvent.getOffsetY())});
        int displayId = view.getDisplay().getDisplayId();
        PerDisplay perDisplay = this.mDisplayDropTargets.get(displayId);
        ClipDescription clipDescription = dragEvent.getClipDescription();
        if (perDisplay == null) {
            return false;
        }
        if (dragEvent.getAction() == 1) {
            perDisplay.isHandlingDrag = dragEvent.getClipData().getItemCount() > 0 && (clipDescription.hasMimeType("application/vnd.android.activity") || clipDescription.hasMimeType("application/vnd.android.shortcut") || clipDescription.hasMimeType("application/vnd.android.task"));
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Clip description: handlingDrag=%b itemCount=%d mimeTypes=%s", new Object[]{Boolean.valueOf(perDisplay.isHandlingDrag), Integer.valueOf(dragEvent.getClipData().getItemCount()), getMimeTypes(clipDescription)});
        }
        if (!perDisplay.isHandlingDrag) {
            return false;
        }
        switch (dragEvent.getAction()) {
            case 1:
                if (perDisplay.activeDragCount == 0) {
                    InstanceId logStart = this.mLogger.logStart(dragEvent);
                    perDisplay.activeDragCount++;
                    perDisplay.dragLayout.prepare(this.mDisplayController.getDisplayLayout(displayId), dragEvent.getClipData(), logStart);
                    setDropTargetWindowVisibility(perDisplay, 0);
                    notifyListeners();
                    break;
                } else {
                    Slog.w(TAG, "Unexpected drag start during an active drag");
                    return false;
                }
            case 2:
                perDisplay.dragLayout.update(dragEvent);
                break;
            case 3:
                return handleDrop(dragEvent, perDisplay);
            case 4:
                if (perDisplay.dragLayout.hasDropped()) {
                    this.mLogger.logDrop();
                } else {
                    perDisplay.activeDragCount--;
                    perDisplay.dragLayout.hide(dragEvent, new DragAndDropController$$ExternalSyntheticLambda0(this, perDisplay));
                }
                this.mLogger.logEnd();
                break;
            case 5:
                perDisplay.dragLayout.show();
                perDisplay.dragLayout.update(dragEvent);
                break;
            case 6:
                perDisplay.dragLayout.hide(dragEvent, (Runnable) null);
                break;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDrag$0$com-android-wm-shell-draganddrop-DragAndDropController */
    public /* synthetic */ void mo49481x2f313c31(PerDisplay perDisplay) {
        if (perDisplay.activeDragCount == 0) {
            setDropTargetWindowVisibility(perDisplay, 4);
        }
    }

    private boolean handleDrop(DragEvent dragEvent, PerDisplay perDisplay) {
        perDisplay.activeDragCount--;
        return perDisplay.dragLayout.drop(dragEvent, dragEvent.getDragSurface(), new DragAndDropController$$ExternalSyntheticLambda1(this, perDisplay));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleDrop$1$com-android-wm-shell-draganddrop-DragAndDropController */
    public /* synthetic */ void mo49480x1217bc8c(PerDisplay perDisplay) {
        if (perDisplay.activeDragCount == 0) {
            setDropTargetWindowVisibility(perDisplay, 4);
        }
    }

    private void setDropTargetWindowVisibility(PerDisplay perDisplay, int i) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Set drop target window visibility: displayId=%d visibility=%d", new Object[]{Integer.valueOf(perDisplay.displayId), Integer.valueOf(i)});
        perDisplay.rootView.setVisibility(i);
        if (i == 0) {
            perDisplay.rootView.requestApplyInsets();
        }
    }

    private String getMimeTypes(ClipDescription clipDescription) {
        String str = "";
        for (int i = 0; i < clipDescription.getMimeTypeCount(); i++) {
            if (i > 0) {
                str = str + ", ";
            }
            str = str + clipDescription.getMimeType(i);
        }
        return str;
    }

    /* access modifiers changed from: private */
    public void onThemeChange() {
        for (int i = 0; i < this.mDisplayDropTargets.size(); i++) {
            this.mDisplayDropTargets.get(i).dragLayout.onThemeChange();
        }
    }

    /* access modifiers changed from: private */
    public void onConfigChanged(Configuration configuration) {
        for (int i = 0; i < this.mDisplayDropTargets.size(); i++) {
            this.mDisplayDropTargets.get(i).dragLayout.onConfigChanged(configuration);
        }
    }

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropController$PerDisplay */
    private static class PerDisplay {
        int activeDragCount;
        final Context context;
        final int displayId;
        final DragLayout dragLayout;
        boolean isHandlingDrag;
        final FrameLayout rootView;

        /* renamed from: wm */
        final WindowManager f405wm;

        PerDisplay(int i, Context context2, WindowManager windowManager, FrameLayout frameLayout, DragLayout dragLayout2) {
            this.displayId = i;
            this.context = context2;
            this.f405wm = windowManager;
            this.rootView = frameLayout;
            this.dragLayout = dragLayout2;
        }
    }

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropController$DragAndDropImpl */
    private class DragAndDropImpl implements DragAndDrop {
        private DragAndDropImpl() {
        }

        public void onThemeChanged() {
            DragAndDropController.this.mMainExecutor.execute(new DragAndDropController$DragAndDropImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onThemeChanged$0$com-android-wm-shell-draganddrop-DragAndDropController$DragAndDropImpl */
        public /* synthetic */ void mo49485x5a5d1aa8() {
            DragAndDropController.this.onThemeChange();
        }

        public void onConfigChanged(Configuration configuration) {
            DragAndDropController.this.mMainExecutor.execute(new DragAndDropController$DragAndDropImpl$$ExternalSyntheticLambda1(this, configuration));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConfigChanged$1$com-android-wm-shell-draganddrop-DragAndDropController$DragAndDropImpl */
        public /* synthetic */ void mo49484x8bb88078(Configuration configuration) {
            DragAndDropController.this.onConfigChanged(configuration);
        }
    }
}
