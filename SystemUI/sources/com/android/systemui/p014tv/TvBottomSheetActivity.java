package com.android.systemui.p014tv;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.android.systemui.C1893R;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.tv.TvBottomSheetActivity */
public abstract class TvBottomSheetActivity extends Activity {
    private static final String TAG = "TvBottomSheetActivity";
    private Drawable mBackgroundWithBlur;
    private Drawable mBackgroundWithoutBlur;
    private final Consumer<Boolean> mBlurConsumer = new TvBottomSheetActivity$$ExternalSyntheticLambda1(this);

    /* access modifiers changed from: private */
    public void onBlurChanged(boolean z) {
        Log.v(TAG, "blur enabled: " + z);
        getWindow().setBackgroundDrawable(z ? this.mBackgroundWithBlur : this.mBackgroundWithoutBlur);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C1893R.layout.tv_bottom_sheet);
        overridePendingTransition(C1893R.anim.tv_bottom_sheet_enter, 0);
        this.mBackgroundWithBlur = getResources().getDrawable(C1893R.C1895drawable.bottom_sheet_background_with_blur);
        this.mBackgroundWithoutBlur = getResources().getDrawable(C1893R.C1895drawable.bottom_sheet_background);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.bottom_sheet_margin);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = i - (dimensionPixelSize * 2);
        attributes.height = -2;
        attributes.gravity = 81;
        attributes.horizontalMargin = 0.0f;
        attributes.verticalMargin = ((float) dimensionPixelSize) / ((float) i2);
        attributes.format = -2;
        attributes.type = 2008;
        attributes.flags |= 128;
        attributes.flags |= 16777216;
        getWindow().setAttributes(attributes);
        getWindow().setElevation(getWindow().getElevation() + 5.0f);
        getWindow().setBackgroundBlurRadius(getResources().getDimensionPixelSize(C1893R.dimen.bottom_sheet_background_blur_radius));
        View findViewById = findViewById(C1893R.C1897id.bottom_sheet);
        findViewById.addOnLayoutChangeListener(new TvBottomSheetActivity$$ExternalSyntheticLambda0(findViewById));
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindowManager().addCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    public void onDetachedFromWindow() {
        getWindowManager().removeCrossWindowBlurEnabledListener(this.mBlurConsumer);
        super.onDetachedFromWindow();
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, C1893R.anim.tv_bottom_sheet_exit);
    }
}
