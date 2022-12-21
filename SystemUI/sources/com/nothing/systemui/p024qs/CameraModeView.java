package com.nothing.systemui.p024qs;

import android.content.Context;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.android.systemui.C1893R;

/* renamed from: com.nothing.systemui.qs.CameraModeView */
public class CameraModeView extends LinearLayout {
    private static final String CAMERA_MODE = "persist.vendor.camera.singlebokeh.enable";
    private Switch mSwitch;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public CameraModeView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CameraModeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public CameraModeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void setMode(boolean z) {
        this.mSwitch.setChecked(z);
    }

    private void init() {
        this.mSwitch = (Switch) findViewById(C1893R.C1897id.camera_mode_check);
        this.mSwitch.setChecked(SystemProperties.getBoolean(CAMERA_MODE, false));
        this.mSwitch.setOnCheckedChangeListener(new CameraModeView$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ void lambda$init$0(CompoundButton compoundButton, boolean z) {
        SystemProperties.set(CAMERA_MODE, z ? "true" : "false");
    }
}
