package com.android.systemui.controls.p010ui;

import android.content.ComponentName;
import android.content.Context;
import android.service.controls.Control;
import android.view.ViewGroup;
import com.android.systemui.controls.controller.StructureInfo;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cJ\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0016\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\tH&J\b\u0010\n\u001a\u00020\u0003H&J \u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H&J\u001e\u0010\u0012\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\tH&J \u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u001dÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ControlsUiController;", "", "closeDialogs", "", "immediately", "", "getPreferredStructure", "Lcom/android/systemui/controls/controller/StructureInfo;", "structures", "", "hide", "onActionResponse", "componentName", "Landroid/content/ComponentName;", "controlId", "", "response", "", "onRefreshState", "controls", "Landroid/service/controls/Control;", "show", "parent", "Landroid/view/ViewGroup;", "onDismiss", "Ljava/lang/Runnable;", "activityContext", "Landroid/content/Context;", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiController */
/* compiled from: ControlsUiController.kt */
public interface ControlsUiController {
    public static final Companion Companion = Companion.$$INSTANCE;
    public static final String EXTRA_ANIMATE = "extra_animate";
    public static final String TAG = "ControlsUiController";

    void closeDialogs(boolean z);

    StructureInfo getPreferredStructure(List<StructureInfo> list);

    void hide();

    void onActionResponse(ComponentName componentName, String str, int i);

    void onRefreshState(ComponentName componentName, List<Control> list);

    void show(ViewGroup viewGroup, Runnable runnable, Context context);

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ControlsUiController$Companion;", "", "()V", "EXTRA_ANIMATE", "", "TAG", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.controls.ui.ControlsUiController$Companion */
    /* compiled from: ControlsUiController.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        public static final String EXTRA_ANIMATE = "extra_animate";
        public static final String TAG = "ControlsUiController";

        private Companion() {
        }
    }
}
