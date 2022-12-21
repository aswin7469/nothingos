package com.android.p019wm.shell.pip.phone;

/* renamed from: com.android.wm.shell.pip.phone.PipTouchGesture */
public abstract class PipTouchGesture {
    public void onDown(PipTouchState pipTouchState) {
    }

    public boolean onMove(PipTouchState pipTouchState) {
        return false;
    }

    public boolean onUp(PipTouchState pipTouchState) {
        return false;
    }
}
