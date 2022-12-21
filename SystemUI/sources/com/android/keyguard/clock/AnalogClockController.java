package com.android.keyguard.clock;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.C1893R;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.plugins.ClockPlugin;
import java.util.TimeZone;

public class AnalogClockController implements ClockPlugin {
    private ImageClock mAnalogClock;
    private ClockLayout mBigClockView;
    private final SmallClockPosition mClockPosition;
    private final SysuiColorExtractor mColorExtractor;
    private final LayoutInflater mLayoutInflater;
    private TextClock mLockClock;
    private final ClockPalette mPalette = new ClockPalette();
    private final ViewPreviewer mRenderer = new ViewPreviewer();
    private final Resources mResources;
    private View mView;

    public String getName() {
        return "analog";
    }

    public void setStyle(Paint.Style style) {
    }

    public boolean shouldShowStatusArea() {
        return true;
    }

    public AnalogClockController(Resources resources, LayoutInflater layoutInflater, SysuiColorExtractor sysuiColorExtractor) {
        this.mResources = resources;
        this.mLayoutInflater = layoutInflater;
        this.mColorExtractor = sysuiColorExtractor;
        this.mClockPosition = new SmallClockPosition(layoutInflater.getContext());
    }

    private void createViews() {
        ClockLayout clockLayout = (ClockLayout) this.mLayoutInflater.inflate(C1893R.layout.analog_clock, (ViewGroup) null);
        this.mBigClockView = clockLayout;
        this.mAnalogClock = (ImageClock) clockLayout.findViewById(C1893R.C1897id.analog_clock);
        View inflate = this.mLayoutInflater.inflate(C1893R.layout.digital_clock, (ViewGroup) null);
        this.mView = inflate;
        this.mLockClock = (TextClock) inflate.findViewById(C1893R.C1897id.lock_screen_clock);
    }

    public void onDestroyView() {
        this.mBigClockView = null;
        this.mAnalogClock = null;
        this.mView = null;
        this.mLockClock = null;
    }

    public String getTitle() {
        return this.mResources.getString(C1893R.string.clock_title_analog);
    }

    public Bitmap getThumbnail() {
        return BitmapFactory.decodeResource(this.mResources, C1893R.C1895drawable.analog_thumbnail);
    }

    public Bitmap getPreview(int i, int i2) {
        View bigClockView = getBigClockView();
        setDarkAmount(1.0f);
        setTextColor(-1);
        ColorExtractor.GradientColors colors = this.mColorExtractor.getColors(2);
        setColorPalette(colors.supportsDarkText(), colors.getColorPalette());
        onTimeTick();
        return this.mRenderer.createPreview(bigClockView, i, i2);
    }

    public View getView() {
        if (this.mView == null) {
            createViews();
        }
        return this.mView;
    }

    public View getBigClockView() {
        if (this.mBigClockView == null) {
            createViews();
        }
        return this.mBigClockView;
    }

    public int getPreferredY(int i) {
        return this.mClockPosition.getPreferredY();
    }

    public void setTextColor(int i) {
        updateColor();
    }

    public void setColorPalette(boolean z, int[] iArr) {
        this.mPalette.setColorPalette(z, iArr);
        updateColor();
    }

    private void updateColor() {
        int primaryColor = this.mPalette.getPrimaryColor();
        int secondaryColor = this.mPalette.getSecondaryColor();
        this.mLockClock.setTextColor(secondaryColor);
        this.mAnalogClock.setClockColors(primaryColor, secondaryColor);
    }

    public void onTimeTick() {
        this.mAnalogClock.onTimeChanged();
        this.mBigClockView.onTimeChanged();
        this.mLockClock.refreshTime();
    }

    public void setDarkAmount(float f) {
        this.mPalette.setDarkAmount(f);
        this.mClockPosition.setDarkAmount(f);
        this.mBigClockView.setDarkAmount(f);
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        this.mAnalogClock.onTimeZoneChanged(timeZone);
    }
}
