package com.android.p019wm.shell.compatui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.compatui.CompatUILayout */
class CompatUILayout extends LinearLayout {
    private CompatUIWindowManager mWindowManager;

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

    public CompatUILayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: package-private */
    public void inject(CompatUIWindowManager compatUIWindowManager) {
        this.mWindowManager = compatUIWindowManager;
    }

    /* access modifiers changed from: package-private */
    public void updateCameraTreatmentButton(int i) {
        int i2;
        int i3;
        if (i == 1) {
            i2 = C3353R.C3355drawable.camera_compat_treatment_suggested_ripple;
        } else {
            i2 = C3353R.C3355drawable.camera_compat_treatment_applied_ripple;
        }
        if (i == 1) {
            i3 = C3353R.string.camera_compat_treatment_suggested_button_description;
        } else {
            i3 = C3353R.string.camera_compat_treatment_applied_button_description;
        }
        ImageButton imageButton = (ImageButton) findViewById(C3353R.C3356id.camera_compat_treatment_button);
        imageButton.setImageResource(i2);
        imageButton.setContentDescription(getResources().getString(i3));
        ((TextView) ((LinearLayout) findViewById(C3353R.C3356id.camera_compat_hint)).findViewById(C3353R.C3356id.compat_mode_hint_text)).setText(i3);
    }

    /* access modifiers changed from: package-private */
    public void setSizeCompatHintVisibility(boolean z) {
        setViewVisibility(C3353R.C3356id.size_compat_hint, z);
    }

    /* access modifiers changed from: package-private */
    public void setCameraCompatHintVisibility(boolean z) {
        setViewVisibility(C3353R.C3356id.camera_compat_hint, z);
    }

    /* access modifiers changed from: package-private */
    public void setRestartButtonVisibility(boolean z) {
        setViewVisibility(C3353R.C3356id.size_compat_restart_button, z);
        if (!z) {
            setSizeCompatHintVisibility(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void setCameraControlVisibility(boolean z) {
        setViewVisibility(C3353R.C3356id.camera_compat_control, z);
        if (!z) {
            setCameraCompatHintVisibility(false);
        }
    }

    private void setViewVisibility(int i, boolean z) {
        View findViewById = findViewById(i);
        int i2 = z ? 0 : 8;
        if (findViewById.getVisibility() != i2) {
            findViewById.setVisibility(i2);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mWindowManager.relayout();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ImageButton imageButton = (ImageButton) findViewById(C3353R.C3356id.size_compat_restart_button);
        imageButton.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda0(this));
        imageButton.setOnLongClickListener(new CompatUILayout$$ExternalSyntheticLambda1(this));
        LinearLayout linearLayout = (LinearLayout) findViewById(C3353R.C3356id.size_compat_hint);
        ((TextView) linearLayout.findViewById(C3353R.C3356id.compat_mode_hint_text)).setText(C3353R.string.restart_button_description);
        linearLayout.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda2(this));
        ImageButton imageButton2 = (ImageButton) findViewById(C3353R.C3356id.camera_compat_treatment_button);
        imageButton2.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda3(this));
        imageButton2.setOnLongClickListener(new CompatUILayout$$ExternalSyntheticLambda4(this));
        ImageButton imageButton3 = (ImageButton) findViewById(C3353R.C3356id.camera_compat_dismiss_button);
        imageButton3.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda5(this));
        imageButton3.setOnLongClickListener(new CompatUILayout$$ExternalSyntheticLambda6(this));
        ((LinearLayout) findViewById(C3353R.C3356id.camera_compat_hint)).setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$0$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ void mo49383x59037d4d(View view) {
        this.mWindowManager.onRestartButtonClicked();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$1$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ boolean mo49384x127b0aec(View view) {
        this.mWindowManager.onRestartButtonLongClicked();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$2$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ void mo49385xcbf2988b(View view) {
        setSizeCompatHintVisibility(false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$3$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ void mo49386x856a262a(View view) {
        this.mWindowManager.onCameraTreatmentButtonClicked();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$4$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ boolean mo49387x3ee1b3c9(View view) {
        this.mWindowManager.onCameraButtonLongClicked();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$5$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ void mo49388xf8594168(View view) {
        this.mWindowManager.onCameraDismissButtonClicked();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$6$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ boolean mo49389xb1d0cf07(View view) {
        this.mWindowManager.onCameraButtonLongClicked();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$7$com-android-wm-shell-compatui-CompatUILayout */
    public /* synthetic */ void mo49390x6b485ca6(View view) {
        setCameraCompatHintVisibility(false);
    }
}
