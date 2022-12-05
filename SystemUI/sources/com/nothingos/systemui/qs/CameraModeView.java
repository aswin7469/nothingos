package com.nothingos.systemui.qs;

import android.content.Context;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.android.systemui.R$id;
/* loaded from: classes2.dex */
public class CameraModeView extends LinearLayout {
    private Switch mSwitch;

    public CameraModeView(Context context) {
        this(context, null);
    }

    public CameraModeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public CameraModeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        this.mSwitch = (Switch) findViewById(R$id.camera_mode_check);
        this.mSwitch.setChecked(SystemProperties.getBoolean("persist.vendor.camera.singlebokeh.enable", false));
        this.mSwitch.setOnCheckedChangeListener(CameraModeView$$ExternalSyntheticLambda0.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$init$0(CompoundButton compoundButton, boolean z) {
        SystemProperties.set("persist.vendor.camera.singlebokeh.enable", z ? "true" : "false");
    }
}
