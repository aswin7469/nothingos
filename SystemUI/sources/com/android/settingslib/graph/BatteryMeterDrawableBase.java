package com.android.settingslib.graph;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import com.android.settingslib.C1757R;
import com.android.settingslib.Utils;

public class BatteryMeterDrawableBase extends Drawable {
    private static final float ASPECT_RATIO = 0.58f;
    private static final float BOLT_LEVEL_THRESHOLD = 0.3f;
    private static final int FULL = 96;
    private static final float RADIUS_RATIO = 0.05882353f;
    private static final boolean SINGLE_DIGIT_PERCENT = false;
    public static final String TAG = "BatteryMeterDrawableBase";
    protected final Paint mBatteryPaint;
    private final RectF mBoltFrame = new RectF();
    protected final Paint mBoltPaint;
    private final Path mBoltPath = new Path();
    private final float[] mBoltPoints;
    private final RectF mButtonFrame = new RectF();
    protected float mButtonHeightFraction;
    private int mChargeColor;
    private boolean mCharging;
    private final int[] mColors;
    protected final Context mContext;
    private final int mCriticalLevel;
    private final RectF mFrame = new RectF();
    protected final Paint mFramePaint;
    private int mHeight;
    private int mIconTint = -1;
    private final int mIntrinsicHeight;
    private final int mIntrinsicWidth;
    private int mLevel = -1;
    private float mOldDarkIntensity = -1.0f;
    private final Path mOutlinePath = new Path();
    private final Rect mPadding = new Rect();
    private final RectF mPlusFrame = new RectF();
    protected final Paint mPlusPaint;
    private final Path mPlusPath = new Path();
    private final float[] mPlusPoints;
    protected boolean mPowerSaveAsColorError = true;
    private boolean mPowerSaveEnabled;
    protected final Paint mPowersavePaint;
    private final Path mShapePath = new Path();
    private boolean mShowPercent;
    private float mSubpixelSmoothingLeft;
    private float mSubpixelSmoothingRight;
    private float mTextHeight;
    protected final Paint mTextPaint;
    private final Path mTextPath = new Path();
    private String mWarningString;
    private float mWarningTextHeight;
    protected final Paint mWarningTextPaint;
    private int mWidth;

    /* access modifiers changed from: protected */
    public float getAspectRatio() {
        return ASPECT_RATIO;
    }

    public int getOpacity() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public float getRadiusRatio() {
        return RADIUS_RATIO;
    }

    public void setAlpha(int i) {
    }

    public BatteryMeterDrawableBase(Context context, int i) {
        this.mContext = context;
        Resources resources = context.getResources();
        TypedArray obtainTypedArray = resources.obtainTypedArray(C1757R.array.batterymeter_color_levels);
        TypedArray obtainTypedArray2 = resources.obtainTypedArray(C1757R.array.batterymeter_color_values);
        int length = obtainTypedArray.length();
        this.mColors = new int[(length * 2)];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 * 2;
            this.mColors[i3] = obtainTypedArray.getInt(i2, 0);
            if (obtainTypedArray2.getType(i2) == 2) {
                this.mColors[i3 + 1] = Utils.getColorAttrDefaultColor(context, obtainTypedArray2.getThemeAttributeId(i2, 0));
            } else {
                this.mColors[i3 + 1] = obtainTypedArray2.getColor(i2, 0);
            }
        }
        obtainTypedArray.recycle();
        obtainTypedArray2.recycle();
        this.mWarningString = context.getString(C1757R.string.battery_meter_very_low_overlay_symbol);
        this.mCriticalLevel = this.mContext.getResources().getInteger(17694772);
        this.mButtonHeightFraction = context.getResources().getFraction(C1757R.fraction.battery_button_height_fraction, 1, 1);
        this.mSubpixelSmoothingLeft = context.getResources().getFraction(C1757R.fraction.battery_subpixel_smoothing_left, 1, 1);
        this.mSubpixelSmoothingRight = context.getResources().getFraction(C1757R.fraction.battery_subpixel_smoothing_right, 1, 1);
        Paint paint = new Paint(1);
        this.mFramePaint = paint;
        paint.setColor(i);
        paint.setDither(true);
        paint.setStrokeWidth(0.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Paint paint2 = new Paint(1);
        this.mBatteryPaint = paint2;
        paint2.setDither(true);
        paint2.setStrokeWidth(0.0f);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        Paint paint3 = new Paint(1);
        this.mTextPaint = paint3;
        paint3.setTypeface(Typeface.create("sans-serif-condensed", 1));
        paint3.setTextAlign(Paint.Align.CENTER);
        Paint paint4 = new Paint(1);
        this.mWarningTextPaint = paint4;
        paint4.setTypeface(Typeface.create("sans-serif", 1));
        paint4.setTextAlign(Paint.Align.CENTER);
        int[] iArr = this.mColors;
        if (iArr.length > 1) {
            paint4.setColor(iArr[1]);
        }
        this.mChargeColor = Utils.getColorStateListDefaultColor(this.mContext, C1757R.C1758color.meter_consumed_color);
        Paint paint5 = new Paint(1);
        this.mBoltPaint = paint5;
        paint5.setColor(Utils.getColorStateListDefaultColor(this.mContext, C1757R.C1758color.batterymeter_bolt_color));
        this.mBoltPoints = loadPoints(resources, C1757R.array.batterymeter_bolt_points);
        Paint paint6 = new Paint(1);
        this.mPlusPaint = paint6;
        paint6.setColor(Utils.getColorStateListDefaultColor(this.mContext, C1757R.C1758color.batterymeter_plus_color));
        this.mPlusPoints = loadPoints(resources, C1757R.array.batterymeter_plus_points);
        Paint paint7 = new Paint(1);
        this.mPowersavePaint = paint7;
        paint7.setColor(paint6.getColor());
        paint7.setStyle(Paint.Style.STROKE);
        paint7.setStrokeWidth((float) context.getResources().getDimensionPixelSize(C1757R.dimen.battery_powersave_outline_thickness));
        this.mIntrinsicWidth = context.getResources().getDimensionPixelSize(C1757R.dimen.battery_width);
        this.mIntrinsicHeight = context.getResources().getDimensionPixelSize(C1757R.dimen.battery_height);
    }

    public int getIntrinsicHeight() {
        return this.mIntrinsicHeight;
    }

    public int getIntrinsicWidth() {
        return this.mIntrinsicWidth;
    }

    public void setShowPercent(boolean z) {
        this.mShowPercent = z;
        postInvalidate();
    }

    public void setCharging(boolean z) {
        this.mCharging = z;
        postInvalidate();
    }

    public boolean getCharging() {
        return this.mCharging;
    }

    public void setBatteryLevel(int i) {
        this.mLevel = i;
        postInvalidate();
    }

    public int getBatteryLevel() {
        return this.mLevel;
    }

    public void setPowerSave(boolean z) {
        this.mPowerSaveEnabled = z;
        postInvalidate();
    }

    public boolean getPowerSave() {
        return this.mPowerSaveEnabled;
    }

    /* access modifiers changed from: protected */
    public void setPowerSaveAsColorError(boolean z) {
        this.mPowerSaveAsColorError = z;
    }

    /* access modifiers changed from: protected */
    public void postInvalidate() {
        unscheduleSelf(new BatteryMeterDrawableBase$$ExternalSyntheticLambda0(this));
        scheduleSelf(new BatteryMeterDrawableBase$$ExternalSyntheticLambda0(this), 0);
    }

    private static float[] loadPoints(Resources resources, int i) {
        int[] intArray = resources.getIntArray(i);
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < intArray.length; i4 += 2) {
            i2 = Math.max(i2, intArray[i4]);
            i3 = Math.max(i3, intArray[i4 + 1]);
        }
        float[] fArr = new float[intArray.length];
        for (int i5 = 0; i5 < intArray.length; i5 += 2) {
            fArr[i5] = ((float) intArray[i5]) / ((float) i2);
            int i6 = i5 + 1;
            fArr[i6] = ((float) intArray[i6]) / ((float) i3);
        }
        return fArr;
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        updateSize();
    }

    private void updateSize() {
        Rect bounds = getBounds();
        this.mHeight = (bounds.bottom - this.mPadding.bottom) - (bounds.top + this.mPadding.top);
        this.mWidth = (bounds.right - this.mPadding.right) - (bounds.left + this.mPadding.left);
        this.mWarningTextPaint.setTextSize(((float) this.mHeight) * 0.75f);
        this.mWarningTextHeight = -this.mWarningTextPaint.getFontMetrics().ascent;
    }

    public boolean getPadding(Rect rect) {
        if (this.mPadding.left == 0 && this.mPadding.top == 0 && this.mPadding.right == 0 && this.mPadding.bottom == 0) {
            return super.getPadding(rect);
        }
        rect.set(this.mPadding);
        return true;
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPadding.left = i;
        this.mPadding.top = i2;
        this.mPadding.right = i3;
        this.mPadding.bottom = i4;
        updateSize();
    }

    private int getColorForLevel(int i) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int[] iArr = this.mColors;
            if (i2 >= iArr.length) {
                return i3;
            }
            int i4 = iArr[i2];
            int i5 = iArr[i2 + 1];
            if (i <= i4) {
                return i2 == iArr.length + -2 ? this.mIconTint : i5;
            }
            i2 += 2;
            i3 = i5;
        }
    }

    public void setColors(int i, int i2) {
        this.mIconTint = i;
        this.mFramePaint.setColor(i2);
        this.mBoltPaint.setColor(i);
        this.mChargeColor = i;
        invalidateSelf();
    }

    /* access modifiers changed from: protected */
    public int batteryColorForLevel(int i) {
        if (this.mCharging || (this.mPowerSaveEnabled && this.mPowerSaveAsColorError)) {
            return this.mChargeColor;
        }
        return getColorForLevel(i);
    }

    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        String str;
        Canvas canvas2 = canvas;
        int i = this.mLevel;
        Rect bounds = getBounds();
        if (i != -1) {
            float f4 = ((float) i) / 100.0f;
            int i2 = this.mHeight;
            int aspectRatio = (int) (getAspectRatio() * ((float) this.mHeight));
            float f5 = (float) i2;
            int round = Math.round(this.mButtonHeightFraction * f5);
            int i3 = this.mPadding.left + bounds.left;
            int i4 = (bounds.bottom - this.mPadding.bottom) - i2;
            float f6 = (float) i3;
            float f7 = (float) i4;
            this.mFrame.set(f6, f7, (float) (i3 + aspectRatio), (float) (i2 + i4));
            this.mFrame.offset((float) ((this.mWidth - aspectRatio) / 2), 0.0f);
            float f8 = ((float) aspectRatio) * 0.28f;
            float f9 = (float) round;
            this.mButtonFrame.set(this.mFrame.left + ((float) Math.round(f8)), this.mFrame.top, this.mFrame.right - ((float) Math.round(f8)), this.mFrame.top + f9);
            this.mFrame.top += f9;
            this.mBatteryPaint.setColor(batteryColorForLevel(i));
            if (i >= 96) {
                f4 = 1.0f;
            } else if (i <= this.mCriticalLevel) {
                f4 = 0.0f;
            }
            if (f4 == 1.0f) {
                f = this.mButtonFrame.top;
            } else {
                f = this.mFrame.top + (this.mFrame.height() * (1.0f - f4));
            }
            this.mShapePath.reset();
            this.mOutlinePath.reset();
            float radiusRatio = getRadiusRatio() * (this.mFrame.height() + f9);
            this.mShapePath.setFillType(Path.FillType.WINDING);
            this.mShapePath.addRoundRect(this.mFrame, radiusRatio, radiusRatio, Path.Direction.CW);
            this.mShapePath.addRect(this.mButtonFrame, Path.Direction.CW);
            this.mOutlinePath.addRoundRect(this.mFrame, radiusRatio, radiusRatio, Path.Direction.CW);
            Path path = new Path();
            path.addRect(this.mButtonFrame, Path.Direction.CW);
            this.mOutlinePath.op(path, Path.Op.XOR);
            boolean z = false;
            if (this.mCharging) {
                float width = this.mFrame.left + (this.mFrame.width() / 4.0f) + 1.0f;
                float height = this.mFrame.top + (this.mFrame.height() / 6.0f);
                float width2 = (this.mFrame.right - (this.mFrame.width() / 4.0f)) + 1.0f;
                float height2 = this.mFrame.bottom - (this.mFrame.height() / 10.0f);
                if (!(this.mBoltFrame.left == width && this.mBoltFrame.top == height && this.mBoltFrame.right == width2 && this.mBoltFrame.bottom == height2)) {
                    this.mBoltFrame.set(width, height, width2, height2);
                    this.mBoltPath.reset();
                    this.mBoltPath.moveTo(this.mBoltFrame.left + (this.mBoltPoints[0] * this.mBoltFrame.width()), this.mBoltFrame.top + (this.mBoltPoints[1] * this.mBoltFrame.height()));
                    for (int i5 = 2; i5 < this.mBoltPoints.length; i5 += 2) {
                        this.mBoltPath.lineTo(this.mBoltFrame.left + (this.mBoltPoints[i5] * this.mBoltFrame.width()), this.mBoltFrame.top + (this.mBoltPoints[i5 + 1] * this.mBoltFrame.height()));
                    }
                    this.mBoltPath.lineTo(this.mBoltFrame.left + (this.mBoltPoints[0] * this.mBoltFrame.width()), this.mBoltFrame.top + (this.mBoltPoints[1] * this.mBoltFrame.height()));
                }
                if (Math.min(Math.max((this.mBoltFrame.bottom - f) / (this.mBoltFrame.bottom - this.mBoltFrame.top), 0.0f), 1.0f) <= 0.3f) {
                    canvas2.drawPath(this.mBoltPath, this.mBoltPaint);
                } else {
                    this.mShapePath.op(this.mBoltPath, Path.Op.DIFFERENCE);
                }
            } else if (this.mPowerSaveEnabled) {
                float width3 = (this.mFrame.width() * 2.0f) / 3.0f;
                float width4 = this.mFrame.left + ((this.mFrame.width() - width3) / 2.0f);
                float height3 = this.mFrame.top + ((this.mFrame.height() - width3) / 2.0f);
                float width5 = this.mFrame.right - ((this.mFrame.width() - width3) / 2.0f);
                float height4 = this.mFrame.bottom - ((this.mFrame.height() - width3) / 2.0f);
                if (!(this.mPlusFrame.left == width4 && this.mPlusFrame.top == height3 && this.mPlusFrame.right == width5 && this.mPlusFrame.bottom == height4)) {
                    this.mPlusFrame.set(width4, height3, width5, height4);
                    this.mPlusPath.reset();
                    this.mPlusPath.moveTo(this.mPlusFrame.left + (this.mPlusPoints[0] * this.mPlusFrame.width()), this.mPlusFrame.top + (this.mPlusPoints[1] * this.mPlusFrame.height()));
                    for (int i6 = 2; i6 < this.mPlusPoints.length; i6 += 2) {
                        this.mPlusPath.lineTo(this.mPlusFrame.left + (this.mPlusPoints[i6] * this.mPlusFrame.width()), this.mPlusFrame.top + (this.mPlusPoints[i6 + 1] * this.mPlusFrame.height()));
                    }
                    this.mPlusPath.lineTo(this.mPlusFrame.left + (this.mPlusPoints[0] * this.mPlusFrame.width()), this.mPlusFrame.top + (this.mPlusPoints[1] * this.mPlusFrame.height()));
                }
                this.mShapePath.op(this.mPlusPath, Path.Op.DIFFERENCE);
                if (this.mPowerSaveAsColorError) {
                    canvas2.drawPath(this.mPlusPath, this.mPlusPaint);
                }
            }
            if (this.mCharging || this.mPowerSaveEnabled || i <= this.mCriticalLevel || !this.mShowPercent) {
                str = null;
                f3 = 0.0f;
                f2 = 0.0f;
            } else {
                this.mTextPaint.setColor(getColorForLevel(i));
                this.mTextPaint.setTextSize(f5 * (this.mLevel == 100 ? 0.38f : 0.5f));
                this.mTextHeight = -this.mTextPaint.getFontMetrics().ascent;
                str = String.valueOf(i);
                f3 = (((float) this.mWidth) * 0.5f) + f6;
                f2 = ((((float) this.mHeight) + this.mTextHeight) * 0.47f) + f7;
                if (f > f2) {
                    z = true;
                }
                if (!z) {
                    this.mTextPath.reset();
                    this.mTextPaint.getTextPath(str, 0, str.length(), f3, f2, this.mTextPath);
                    this.mShapePath.op(this.mTextPath, Path.Op.DIFFERENCE);
                }
            }
            canvas2.drawPath(this.mShapePath, this.mFramePaint);
            this.mFrame.top = f;
            canvas.save();
            canvas2.clipRect(this.mFrame);
            canvas2.drawPath(this.mShapePath, this.mBatteryPaint);
            canvas.restore();
            if (!this.mCharging && !this.mPowerSaveEnabled) {
                if (i <= this.mCriticalLevel) {
                    canvas2.drawText(this.mWarningString, (((float) this.mWidth) * 0.5f) + f6, ((((float) this.mHeight) + this.mWarningTextHeight) * 0.48f) + f7, this.mWarningTextPaint);
                } else if (z) {
                    canvas2.drawText(str, f3, f2, this.mTextPaint);
                }
            }
            if (!this.mCharging && this.mPowerSaveEnabled && this.mPowerSaveAsColorError) {
                canvas2.drawPath(this.mOutlinePath, this.mPowersavePaint);
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mFramePaint.setColorFilter(colorFilter);
        this.mBatteryPaint.setColorFilter(colorFilter);
        this.mWarningTextPaint.setColorFilter(colorFilter);
        this.mBoltPaint.setColorFilter(colorFilter);
        this.mPlusPaint.setColorFilter(colorFilter);
    }

    public int getCriticalLevel() {
        return this.mCriticalLevel;
    }
}
