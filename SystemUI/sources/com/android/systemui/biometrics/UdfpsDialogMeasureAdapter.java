package com.android.systemui.biometrics;

import android.graphics.Insets;
import android.graphics.Rect;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.FrameLayout;
import com.android.systemui.C1893R;
import com.android.systemui.biometrics.AuthDialog;

public class UdfpsDialogMeasureAdapter {
    private static final boolean DEBUG = (Build.IS_USERDEBUG || Build.IS_ENG);
    private static final String TAG = "UdfpsDialogMeasurementAdapter";
    private int mBottomSpacerHeight;
    private final FingerprintSensorPropertiesInternal mSensorProps;
    private final ViewGroup mView;
    private WindowManager mWindowManager;

    public UdfpsDialogMeasureAdapter(ViewGroup viewGroup, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        this.mView = viewGroup;
        this.mSensorProps = fingerprintSensorPropertiesInternal;
        this.mWindowManager = (WindowManager) viewGroup.getContext().getSystemService(WindowManager.class);
    }

    /* access modifiers changed from: package-private */
    public FingerprintSensorPropertiesInternal getSensorProps() {
        return this.mSensorProps;
    }

    /* access modifiers changed from: package-private */
    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2, AuthDialog.LayoutParams layoutParams) {
        int rotation = this.mView.getDisplay().getRotation();
        if (rotation == 0) {
            return onMeasureInternalPortrait(i, i2);
        }
        if (rotation == 1 || rotation == 3) {
            return onMeasureInternalLandscape(i, i2);
        }
        Log.e(TAG, "Unsupported display rotation: " + rotation);
        return layoutParams;
    }

    /* access modifiers changed from: package-private */
    public int getBottomSpacerHeight() {
        return this.mBottomSpacerHeight;
    }

    private AuthDialog.LayoutParams onMeasureInternalPortrait(int i, int i2) {
        int i3 = i;
        WindowMetrics maximumWindowMetrics = this.mWindowManager.getMaximumWindowMetrics();
        int viewHeightPx = getViewHeightPx(C1893R.C1897id.button_bar);
        int dialogMarginPx = getDialogMarginPx();
        int height = getMaximumWindowBounds(maximumWindowMetrics).height();
        this.mBottomSpacerHeight = calculateBottomSpacerHeightForPortrait(this.mSensorProps, height, 0, viewHeightPx, dialogMarginPx, getNavbarInsets(maximumWindowMetrics).bottom);
        int childCount = this.mView.getChildCount();
        int i4 = this.mSensorProps.getLocation().sensorRadius * 2;
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = this.mView.getChildAt(i6);
            if (childAt.getId() == C1893R.C1897id.biometric_icon_frame) {
                FrameLayout frameLayout = (FrameLayout) childAt;
                View childAt2 = frameLayout.getChildAt(0);
                frameLayout.measure(View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().width, 1073741824), View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
                childAt2.getLayoutParams().width = i4;
                childAt2.getLayoutParams().height = i4;
                childAt2.measure(View.MeasureSpec.makeMeasureSpec(i4, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i4, Integer.MIN_VALUE));
                int dimensionPixelSize = this.mView.getResources().getDimensionPixelSize(C1893R.dimen.nt_biometric_dialog_fp_icon_padding);
                childAt2.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
            } else if (childAt.getId() == C1893R.C1897id.space_above_icon || childAt.getId() == C1893R.C1897id.space_above_title || childAt.getId() == C1893R.C1897id.indicator) {
                int i7 = i2;
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                if (!(childAt.getVisibility() == 8 || childAt.getId() == C1893R.C1897id.indicator)) {
                    i5 += childAt.getMeasuredHeight();
                }
            } else if (childAt.getId() == C1893R.C1897id.button_bar) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == C1893R.C1897id.space_below_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(this.mBottomSpacerHeight, 0), 1073741824));
            } else if (childAt.getId() == C1893R.C1897id.description) {
                int i8 = i2;
            } else {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
                i5 += childAt.getMeasuredHeight();
            }
            int i9 = i2;
            i5 += childAt.getMeasuredHeight();
        }
        View findViewById = this.mView.findViewById(C1893R.C1897id.description);
        if (!(findViewById == null || findViewById.getVisibility() == 8)) {
            i5 += measureDescription(findViewById, height, i3, i5);
        }
        return new AuthDialog.LayoutParams(i3, i5);
    }

    private int measureDescription(View view, int i, int i2, int i3) {
        int i4 = (int) (((double) i) * 0.75d);
        if (view.getMeasuredHeight() + i3 > i4) {
            view.measure(View.MeasureSpec.makeMeasureSpec(i2, 1073741824), View.MeasureSpec.makeMeasureSpec(i4 - i3, 1073741824));
        }
        return view.getMeasuredHeight();
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0116 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.systemui.biometrics.AuthDialog.LayoutParams onMeasureInternalLandscape(int r17, int r18) {
        /*
            r16 = this;
            r0 = r16
            android.view.WindowManager r1 = r0.mWindowManager
            android.view.WindowMetrics r1 = r1.getMaximumWindowMetrics()
            r2 = 2131429048(0x7f0b06b8, float:1.8479758E38)
            int r3 = r0.getViewHeightPx(r2)
            r2 = 2131428931(0x7f0b0643, float:1.847952E38)
            int r4 = r0.getViewHeightPx(r2)
            r2 = 2131427826(0x7f0b01f2, float:1.847728E38)
            int r5 = r0.getViewHeightPx(r2)
            r2 = 2131428877(0x7f0b060d, float:1.847941E38)
            int r6 = r0.getViewHeightPx(r2)
            r7 = 2131428092(0x7f0b02fc, float:1.8477819E38)
            int r7 = r0.getViewHeightPx(r7)
            r10 = 2131427631(0x7f0b012f, float:1.8476884E38)
            int r8 = r0.getViewHeightPx(r10)
            android.graphics.Insets r11 = getNavbarInsets(r1)
            int r9 = r11.bottom
            int r3 = calculateBottomSpacerHeightForLandscape(r3, r4, r5, r6, r7, r8, r9)
            android.graphics.Rect r1 = getMaximumWindowBounds(r1)
            int r1 = r1.width()
            int r4 = r16.getDialogMarginPx()
            int r5 = r11.left
            int r6 = r11.right
            int r5 = r5 + r6
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r6 = r0.mSensorProps
            int r1 = calculateHorizontalSpacerWidthForLandscape(r6, r1, r4, r5)
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r4 = r0.mSensorProps
            android.hardware.biometrics.SensorLocationInternal r4 = r4.getLocation()
            int r4 = r4.sensorRadius
            int r4 = r4 * 2
            int r1 = r1 * 2
            int r1 = r1 + r4
            android.view.ViewGroup r5 = r0.mView
            int r5 = r5.getChildCount()
            r6 = 0
            r7 = r6
            r8 = r7
        L_0x0069:
            if (r7 >= r5) goto L_0x011a
            android.view.ViewGroup r9 = r0.mView
            android.view.View r9 = r9.getChildAt(r7)
            int r11 = r9.getId()
            r12 = 2131427551(0x7f0b00df, float:1.8476721E38)
            r13 = -2147483648(0xffffffff80000000, float:-0.0)
            r14 = 1073741824(0x40000000, float:2.0)
            if (r11 != r12) goto L_0x00ae
            r11 = r9
            android.widget.FrameLayout r11 = (android.widget.FrameLayout) r11
            android.view.View r12 = r11.getChildAt(r6)
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r14)
            int r14 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r14)
            r11.measure(r15, r14)
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r13)
            int r13 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r13)
            r12.measure(r11, r13)
            android.view.ViewGroup r11 = r0.mView
            android.content.res.Resources r11 = r11.getResources()
            r13 = 2131166643(0x7f0705b3, float:1.7947537E38)
            int r11 = r11.getDimensionPixelSize(r13)
            r12.setPadding(r11, r11, r11, r11)
        L_0x00ab:
            r12 = r18
            goto L_0x0109
        L_0x00ae:
            int r11 = r9.getId()
            if (r11 != r2) goto L_0x00cb
            android.view.ViewGroup$LayoutParams r11 = r9.getLayoutParams()
            int r11 = r11.height
            int r12 = java.lang.Math.min((int) r3, (int) r6)
            int r11 = r11 - r12
            int r12 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r14)
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r14)
            r9.measure(r12, r11)
            goto L_0x00ab
        L_0x00cb:
            int r11 = r9.getId()
            if (r11 != r10) goto L_0x00e3
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r14)
            android.view.ViewGroup$LayoutParams r12 = r9.getLayoutParams()
            int r12 = r12.height
            int r12 = android.view.View.MeasureSpec.makeMeasureSpec(r12, r14)
            r9.measure(r11, r12)
            goto L_0x00ab
        L_0x00e3:
            int r11 = r9.getId()
            r12 = 2131428879(0x7f0b060f, float:1.8479415E38)
            if (r11 != r12) goto L_0x00fc
            int r11 = java.lang.Math.max((int) r3, (int) r6)
            int r12 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r14)
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r14)
            r9.measure(r12, r11)
            goto L_0x00ab
        L_0x00fc:
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r14)
            r12 = r18
            int r13 = android.view.View.MeasureSpec.makeMeasureSpec(r12, r13)
            r9.measure(r11, r13)
        L_0x0109:
            int r11 = r9.getVisibility()
            r13 = 8
            if (r11 == r13) goto L_0x0116
            int r9 = r9.getMeasuredHeight()
            int r8 = r8 + r9
        L_0x0116:
            int r7 = r7 + 1
            goto L_0x0069
        L_0x011a:
            com.android.systemui.biometrics.AuthDialog$LayoutParams r0 = new com.android.systemui.biometrics.AuthDialog$LayoutParams
            r0.<init>(r1, r8)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsDialogMeasureAdapter.onMeasureInternalLandscape(int, int):com.android.systemui.biometrics.AuthDialog$LayoutParams");
    }

    private int getViewHeightPx(int i) {
        View findViewById = this.mView.findViewById(i);
        if (findViewById == null || findViewById.getVisibility() == 8) {
            return 0;
        }
        return findViewById.getMeasuredHeight();
    }

    private int getDialogMarginPx() {
        return this.mView.getResources().getDimensionPixelSize(C1893R.dimen.biometric_dialog_border_padding);
    }

    private static Insets getNavbarInsets(WindowMetrics windowMetrics) {
        if (windowMetrics != null) {
            return windowMetrics.getWindowInsets().getInsets(WindowInsets.Type.navigationBars());
        }
        return Insets.NONE;
    }

    private static Rect getMaximumWindowBounds(WindowMetrics windowMetrics) {
        return windowMetrics != null ? windowMetrics.getBounds() : new Rect();
    }

    static int calculateBottomSpacerHeightForPortrait(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, int i2, int i3, int i4, int i5) {
        SensorLocationInternal location = fingerprintSensorPropertiesInternal.getLocation();
        int i6 = (i - location.sensorLocationY) - location.sensorRadius;
        int i7 = (((i6 - i2) - i3) - i4) - i5;
        if (DEBUG) {
            Log.d(TAG, "Display height: " + i + ", Distance from bottom: " + i6 + ", Bottom margin: " + i4 + ", Navbar bottom inset: " + i5 + ", Bottom spacer height (portrait): " + i7);
        }
        return i7;
    }

    static int calculateBottomSpacerHeightForLandscape(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8 = ((((i + i2) + i3) + i4) - (i5 + i6)) - i7;
        if (DEBUG) {
            Log.d(TAG, "Title height: " + i + ", Subtitle height: " + i2 + ", Description height: " + i3 + ", Top spacer height: " + i4 + ", Text indicator height: " + i5 + ", Button bar height: " + i6 + ", Navbar bottom inset: " + i7 + ", Bottom spacer height (landscape): " + i8);
        }
        return i8;
    }

    static int calculateHorizontalSpacerWidthForLandscape(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, int i2, int i3) {
        SensorLocationInternal location = fingerprintSensorPropertiesInternal.getLocation();
        int i4 = (i - location.sensorLocationY) - location.sensorRadius;
        int i5 = (i4 - i2) - i3;
        if (DEBUG) {
            Log.d(TAG, "Display width: " + i + ", Distance from edge: " + i4 + ", Dialog margin: " + i2 + ", Navbar horizontal inset: " + i3 + ", Horizontal spacer width (landscape): " + i5);
        }
        return i5;
    }
}
