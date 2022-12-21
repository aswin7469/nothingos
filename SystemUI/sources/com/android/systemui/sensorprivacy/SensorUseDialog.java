package com.android.systemui.sensorprivacy;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/sensorprivacy/SensorUseDialog;", "Lcom/android/systemui/statusbar/phone/SystemUIDialog;", "context", "Landroid/content/Context;", "sensor", "", "clickListener", "Landroid/content/DialogInterface$OnClickListener;", "dismissListener", "Landroid/content/DialogInterface$OnDismissListener;", "(Landroid/content/Context;ILandroid/content/DialogInterface$OnClickListener;Landroid/content/DialogInterface$OnDismissListener;)V", "getClickListener", "()Landroid/content/DialogInterface$OnClickListener;", "getDismissListener", "()Landroid/content/DialogInterface$OnDismissListener;", "getSensor", "()I", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SensorUseDialog.kt */
public final class SensorUseDialog extends SystemUIDialog {
    private final DialogInterface.OnClickListener clickListener;
    private final DialogInterface.OnDismissListener dismissListener;
    private final int sensor;

    public final int getSensor() {
        return this.sensor;
    }

    public final DialogInterface.OnClickListener getClickListener() {
        return this.clickListener;
    }

    public final DialogInterface.OnDismissListener getDismissListener() {
        return this.dismissListener;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SensorUseDialog(Context context, int i, DialogInterface.OnClickListener onClickListener, DialogInterface.OnDismissListener onDismissListener) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(onClickListener, "clickListener");
        Intrinsics.checkNotNullParameter(onDismissListener, "dismissListener");
        this.sensor = i;
        this.clickListener = onClickListener;
        this.dismissListener = onDismissListener;
        Window window = getWindow();
        Intrinsics.checkNotNull(window);
        window.addFlags(524288);
        Window window2 = getWindow();
        Intrinsics.checkNotNull(window2);
        window2.addSystemFlags(524288);
        View inflate = LayoutInflater.from(context).inflate(C1893R.layout.sensor_use_started_title, (ViewGroup) null);
        inflate.requireViewById(C1893R.C1897id.sensor_use_started_title_message).setText(i != 1 ? i != 2 ? i != Integer.MAX_VALUE ? 0 : C1893R.string.sensor_privacy_start_use_mic_camera_dialog_title : C1893R.string.sensor_privacy_start_use_camera_dialog_title : C1893R.string.sensor_privacy_start_use_mic_dialog_title);
        int i2 = 8;
        ((ImageView) inflate.requireViewById(C1893R.C1897id.sensor_use_microphone_icon)).setVisibility((i == 1 || i == Integer.MAX_VALUE) ? 0 : 8);
        ((ImageView) inflate.requireViewById(C1893R.C1897id.sensor_use_camera_icon)).setVisibility((i == 2 || i == Integer.MAX_VALUE) ? 0 : i2);
        setCustomTitle(inflate);
        setMessage(Html.fromHtml(context.getString(i != 1 ? i != 2 ? i != Integer.MAX_VALUE ? 0 : C1893R.string.sensor_privacy_start_use_mic_camera_dialog_content : C1893R.string.sensor_privacy_start_use_camera_dialog_content : C1893R.string.sensor_privacy_start_use_mic_dialog_content), 0));
        setButton(-1, context.getString(17041475), onClickListener);
        setButton(-2, context.getString(17039360), onClickListener);
        setOnDismissListener(onDismissListener);
        setCancelable(false);
    }
}
