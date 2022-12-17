package com.nothing.settings.display.colors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.android.settings.R$dimen;
import com.android.settings.Utils;

public class ColorTemperatureSeekBar extends SeekBar {
    static final float SNAP_TO_PERCENTAGE = 0.03f;
    /* access modifiers changed from: private */
    public int mCenter;
    private final Paint mCenterMarkerPaint;
    private final Rect mCenterMarkerRect;
    private final Context mContext;
    /* access modifiers changed from: private */
    public int mLastProgress;
    /* access modifiers changed from: private */
    public final Object mListenerLock;
    /* access modifiers changed from: private */
    public SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
    private final SeekBar.OnSeekBarChangeListener mProxySeekBarListener;
    /* access modifiers changed from: private */
    public float mSnapThreshold;

    public ColorTemperatureSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842875);
    }

    public ColorTemperatureSeekBar(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ColorTemperatureSeekBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mListenerLock = new Object();
        int i3 = -1;
        this.mLastProgress = -1;
        C19441 r6 = new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                synchronized (ColorTemperatureSeekBar.this.mListenerLock) {
                    if (ColorTemperatureSeekBar.this.mOnSeekBarChangeListener != null) {
                        ColorTemperatureSeekBar.this.mOnSeekBarChangeListener.onStopTrackingTouch(seekBar);
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                synchronized (ColorTemperatureSeekBar.this.mListenerLock) {
                    if (ColorTemperatureSeekBar.this.mOnSeekBarChangeListener != null) {
                        ColorTemperatureSeekBar.this.mOnSeekBarChangeListener.onStartTrackingTouch(seekBar);
                    }
                }
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    if (i != ColorTemperatureSeekBar.this.mCenter) {
                        float f = (float) i;
                        if (f > ((float) ColorTemperatureSeekBar.this.mCenter) - ColorTemperatureSeekBar.this.mSnapThreshold && f < ((float) ColorTemperatureSeekBar.this.mCenter) + ColorTemperatureSeekBar.this.mSnapThreshold) {
                            i = ColorTemperatureSeekBar.this.mCenter;
                            seekBar.setProgress(i);
                        }
                    }
                    if (i != ColorTemperatureSeekBar.this.mLastProgress) {
                        if (i == ColorTemperatureSeekBar.this.mCenter || i == ColorTemperatureSeekBar.this.getMin() || i == ColorTemperatureSeekBar.this.getMax()) {
                            seekBar.performHapticFeedback(4);
                        }
                        ColorTemperatureSeekBar.this.mLastProgress = i;
                    }
                }
                synchronized (ColorTemperatureSeekBar.this.mListenerLock) {
                    if (ColorTemperatureSeekBar.this.mOnSeekBarChangeListener != null) {
                        ColorTemperatureSeekBar.this.mOnSeekBarChangeListener.onProgressChanged(seekBar, i, z);
                    }
                }
            }
        };
        this.mProxySeekBarListener = r6;
        this.mContext = context;
        Resources resources = getResources();
        this.mCenterMarkerRect = new Rect(0, 0, resources.getDimensionPixelSize(R$dimen.balance_seekbar_center_marker_width), resources.getDimensionPixelSize(R$dimen.balance_seekbar_center_marker_height));
        Paint paint = new Paint();
        this.mCenterMarkerPaint = paint;
        paint.setColor(!Utils.isNightMode(context) ? -16777216 : i3);
        paint.setStyle(Paint.Style.FILL);
        setProgressTintList(ColorStateList.valueOf(0));
        super.setOnSeekBarChangeListener(r6);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        synchronized (this.mListenerLock) {
            this.mOnSeekBarChangeListener = onSeekBarChangeListener;
        }
    }

    public synchronized void setMax(int i) {
        super.setMax(i);
        this.mCenter = i / 2;
        this.mSnapThreshold = ((float) i) * SNAP_TO_PERCENTAGE;
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        canvas.save();
        int width = canvas.getWidth();
        Rect rect = this.mCenterMarkerRect;
        canvas.translate((float) ((width - rect.right) / 2), (float) (((canvas.getHeight() - getPaddingBottom()) / 2) - (rect.bottom / 2)));
        canvas.drawRect(this.mCenterMarkerRect, this.mCenterMarkerPaint);
        canvas.restore();
        super.onDraw(canvas);
    }
}
