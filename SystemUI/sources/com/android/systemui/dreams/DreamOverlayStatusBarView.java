package com.android.systemui.dreams;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOverlay;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.C1893R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DreamOverlayStatusBarView extends ConstraintLayout {
    public static final int STATUS_ICON_ALARM_SET = 2;
    public static final int STATUS_ICON_MIC_CAMERA_DISABLED = 3;
    public static final int STATUS_ICON_NOTIFICATIONS = 0;
    public static final int STATUS_ICON_PRIORITY_MODE_ON = 4;
    public static final int STATUS_ICON_WIFI_UNAVAILABLE = 1;
    private final Map<Integer, View> mStatusIcons;

    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusIconType {
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public DreamOverlayStatusBarView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mStatusIcons = new HashMap();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mStatusIcons.put(1, fetchStatusIconForResId(C1893R.C1897id.dream_overlay_wifi_status));
        this.mStatusIcons.put(2, fetchStatusIconForResId(C1893R.C1897id.dream_overlay_alarm_set));
        this.mStatusIcons.put(3, fetchStatusIconForResId(C1893R.C1897id.dream_overlay_camera_mic_off));
        this.mStatusIcons.put(0, fetchStatusIconForResId(C1893R.C1897id.dream_overlay_notification_indicator));
        this.mStatusIcons.put(4, fetchStatusIconForResId(C1893R.C1897id.dream_overlay_priority_mode));
    }

    /* access modifiers changed from: package-private */
    public void showIcon(int i, boolean z, String str) {
        View view = this.mStatusIcons.get(Integer.valueOf(i));
        if (view != null) {
            if (z && str != null) {
                view.setContentDescription(str);
            }
            view.setVisibility(z ? 0 : 8);
        }
    }

    private View fetchStatusIconForResId(int i) {
        return (View) Objects.requireNonNull(findViewById(i));
    }
}
