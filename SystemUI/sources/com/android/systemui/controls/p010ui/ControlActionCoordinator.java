package com.android.systemui.controls.p010ui;

import android.content.Context;
import android.service.controls.Control;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH&J\u0010\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000fH&J\u0010\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0012H&J\u0010\u0010\u0013\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000fH&J \u0010\u0014\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u0017H&J \u0010\u0018\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\fH&J \u0010\u001a\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u001cH&R\u0018\u0010\u0002\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u001dÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ControlActionCoordinator;", "", "activityContext", "Landroid/content/Context;", "getActivityContext", "()Landroid/content/Context;", "setActivityContext", "(Landroid/content/Context;)V", "closeDialogs", "", "drag", "isEdge", "", "enableActionOnTouch", "controlId", "", "longPress", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "runPendingAction", "setValue", "templateId", "newValue", "", "toggle", "isChecked", "touch", "control", "Landroid/service/controls/Control;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinator */
/* compiled from: ControlActionCoordinator.kt */
public interface ControlActionCoordinator {
    void closeDialogs();

    void drag(boolean z);

    void enableActionOnTouch(String str);

    Context getActivityContext();

    void longPress(ControlViewHolder controlViewHolder);

    void runPendingAction(String str);

    void setActivityContext(Context context);

    void setValue(ControlViewHolder controlViewHolder, String str, float f);

    void toggle(ControlViewHolder controlViewHolder, String str, boolean z);

    void touch(ControlViewHolder controlViewHolder, String str, Control control);
}
