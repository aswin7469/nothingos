package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import com.android.internal.util.EmergencyAffordanceManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;

public class EmergencyButton extends Button {
    private int mDownX;
    private int mDownY;
    private final EmergencyAffordanceManager mEmergencyAffordanceManager;
    private final boolean mEnableEmergencyCallWhileSimLocked;
    private LockPatternUtils mLockPatternUtils;
    private boolean mLongPressWasDragged;

    public EmergencyButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public EmergencyButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEnableEmergencyCallWhileSimLocked = this.mContext.getResources().getBoolean(17891654);
        this.mEmergencyAffordanceManager = new EmergencyAffordanceManager(context);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLockPatternUtils = new LockPatternUtils(this.mContext);
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            setOnLongClickListener(new EmergencyButton$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$0$com-android-keyguard-EmergencyButton  reason: not valid java name */
    public /* synthetic */ boolean m2281lambda$onFinishInflate$0$comandroidkeyguardEmergencyButton(View view) {
        if (this.mLongPressWasDragged || !this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            return false;
        }
        this.mEmergencyAffordanceManager.performEmergencyCall();
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (motionEvent.getActionMasked() == 0) {
            this.mDownX = x;
            this.mDownY = y;
            this.mLongPressWasDragged = false;
        } else {
            int abs = Math.abs(x - this.mDownX);
            int abs2 = Math.abs(y - this.mDownY);
            int scaledTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
            if (Math.abs(abs2) > scaledTouchSlop || Math.abs(abs) > scaledTouchSlop) {
                this.mLongPressWasDragged = true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void reloadColors() {
        setTextColor(Utils.getColorAttrDefaultColor(getContext(), 17957103));
        setBackground(getContext().getDrawable(C1893R.C1895drawable.kg_emergency_button_background));
    }

    public boolean performLongClick() {
        return super.performLongClick();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003c, code lost:
        if (r7 != false) goto L_0x0042;
     */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateEmergencyCallButton(boolean r4, boolean r5, boolean r6, boolean r7) {
        /*
            r3 = this;
            r0 = 0
            if (r5 == 0) goto L_0x0041
            r5 = 1
            if (r4 == 0) goto L_0x0007
            goto L_0x0042
        L_0x0007:
            if (r6 == 0) goto L_0x000c
            boolean r6 = r3.mEnableEmergencyCallWhileSimLocked
            goto L_0x002b
        L_0x000c:
            com.android.internal.widget.LockPatternUtils r6 = r3.mLockPatternUtils
            int r1 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            boolean r6 = r6.isSecure(r1)
            if (r6 != 0) goto L_0x002a
            android.content.Context r6 = r3.mContext
            android.content.res.Resources r6 = r6.getResources()
            r1 = 2131034164(0x7f050034, float:1.7678838E38)
            boolean r6 = r6.getBoolean(r1)
            if (r6 == 0) goto L_0x0028
            goto L_0x002a
        L_0x0028:
            r6 = r0
            goto L_0x002b
        L_0x002a:
            r6 = r5
        L_0x002b:
            android.content.Context r1 = r3.mContext
            android.content.res.Resources r1 = r1.getResources()
            r2 = 2131034207(0x7f05005f, float:1.7678925E38)
            boolean r1 = r1.getBoolean(r2)
            if (r1 == 0) goto L_0x003f
            if (r6 == 0) goto L_0x0041
            if (r7 == 0) goto L_0x0041
            goto L_0x0042
        L_0x003f:
            r5 = r6
            goto L_0x0042
        L_0x0041:
            r5 = r0
        L_0x0042:
            if (r5 == 0) goto L_0x0054
            r3.setVisibility(r0)
            if (r4 == 0) goto L_0x004d
            r4 = 17040633(0x10404f9, float:2.424814E-38)
            goto L_0x0050
        L_0x004d:
            r4 = 17040606(0x10404de, float:2.4248063E-38)
        L_0x0050:
            r3.setText(r4)
            goto L_0x0059
        L_0x0054:
            r4 = 8
            r3.setVisibility(r4)
        L_0x0059:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.EmergencyButton.updateEmergencyCallButton(boolean, boolean, boolean, boolean):void");
    }
}
