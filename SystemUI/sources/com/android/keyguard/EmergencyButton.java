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
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
/* loaded from: classes.dex */
public class EmergencyButton extends Button {
    private int mDownX;
    private int mDownY;
    private final EmergencyAffordanceManager mEmergencyAffordanceManager;
    private final boolean mEnableEmergencyCallWhileSimLocked;
    private LockPatternUtils mLockPatternUtils;
    private boolean mLongPressWasDragged;

    public EmergencyButton(Context context) {
        this(context, null);
    }

    public EmergencyButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEnableEmergencyCallWhileSimLocked = ((Button) this).mContext.getResources().getBoolean(17891553);
        this.mEmergencyAffordanceManager = new EmergencyAffordanceManager(context);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mLockPatternUtils = new LockPatternUtils(((Button) this).mContext);
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.keyguard.EmergencyButton$$ExternalSyntheticLambda0
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    boolean lambda$onFinishInflate$0;
                    lambda$onFinishInflate$0 = EmergencyButton.this.lambda$onFinishInflate$0(view);
                    return lambda$onFinishInflate$0;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$0(View view) {
        if (this.mLongPressWasDragged || !this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            return false;
        }
        this.mEmergencyAffordanceManager.performEmergencyCall();
        return true;
    }

    @Override // android.widget.TextView, android.view.View
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
            int scaledTouchSlop = ViewConfiguration.get(((Button) this).mContext).getScaledTouchSlop();
            if (Math.abs(abs2) > scaledTouchSlop || Math.abs(abs) > scaledTouchSlop) {
                this.mLongPressWasDragged = true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void reloadColors() {
        setTextColor(Utils.getColorAttrDefaultColor(getContext(), 16842809));
        setBackground(getContext().getDrawable(R$drawable.kg_emergency_button_background));
    }

    @Override // android.widget.TextView, android.view.View
    public boolean performLongClick() {
        return super.performLongClick();
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x003a, code lost:
        if (r7 != false) goto L17;
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0052  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateEmergencyCallButton(boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5;
        boolean z6 = true;
        if (z2) {
            if (!z) {
                if (z3) {
                    z5 = this.mEnableEmergencyCallWhileSimLocked;
                } else {
                    z5 = this.mLockPatternUtils.isSecure(KeyguardUpdateMonitor.getCurrentUser()) || ((Button) this).mContext.getResources().getBoolean(R$bool.config_showEmergencyButton);
                }
                if (!((Button) this).mContext.getResources().getBoolean(R$bool.kg_hide_emgcy_btn_when_oos)) {
                    z6 = z5;
                } else if (z5) {
                }
            }
            if (!z6) {
                setVisibility(0);
                setText(z ? 17040556 : 17040529);
                return;
            }
            setVisibility(8);
            return;
        }
        z6 = false;
        if (!z6) {
        }
    }
}
