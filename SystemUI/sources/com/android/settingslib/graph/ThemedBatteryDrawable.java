package com.android.settingslib.graph;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.PathParser;
import com.android.settingslib.C1757R;
import com.android.settingslib.Utils;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0015\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\f\b\u0016\u0018\u0000 \\2\u00020\u0001:\u0001\\B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010=\u001a\u00020\u00052\u0006\u0010>\u001a\u00020\u0005H\u0002J\u0010\u0010?\u001a\u00020)2\u0006\u0010@\u001a\u00020AH\u0016J\u0006\u0010B\u001a\u00020\u0005J\u0010\u0010C\u001a\u00020\u00052\u0006\u0010>\u001a\u00020\u0005H\u0002J\b\u0010D\u001a\u00020\u0005H\u0016J\b\u0010E\u001a\u00020\u0005H\u0016J\b\u0010F\u001a\u00020\u0005H\u0016J\b\u0010G\u001a\u00020)H\u0002J\u0012\u0010H\u001a\u00020)2\b\u0010I\u001a\u0004\u0018\u00010/H\u0014J\b\u0010J\u001a\u00020)H\u0002J\u0010\u0010K\u001a\u00020)2\u0006\u0010L\u001a\u00020\u0005H\u0016J\u0010\u0010M\u001a\u00020)2\u0006\u0010N\u001a\u00020\u0005H\u0016J\u0012\u0010O\u001a\u00020)2\b\u0010P\u001a\u0004\u0018\u00010QH\u0016J\u001e\u0010R\u001a\u00020)2\u0006\u0010S\u001a\u00020\u00052\u0006\u0010T\u001a\u00020\u00052\u0006\u0010U\u001a\u00020\u0005J&\u0010V\u001a\u00020)2\u0006\u0010W\u001a\u00020\u00052\u0006\u0010X\u001a\u00020\u00052\u0006\u0010Y\u001a\u00020\u00052\u0006\u0010Z\u001a\u00020\u0005J\b\u0010[\u001a\u00020)H\u0002R\u000e\u0010\u0007\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R$\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010'\u001a\b\u0012\u0004\u0012\u00020)0(X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020$X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020/X\u0004¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R$\u00102\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u000f\"\u0004\b4\u0010\u0011R\u000e\u00105\u001a\u000206X\u0004¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006]"}, mo65043d2 = {"Lcom/android/settingslib/graph/ThemedBatteryDrawable;", "Landroid/graphics/drawable/Drawable;", "context", "Landroid/content/Context;", "frameColor", "", "(Landroid/content/Context;I)V", "backgroundColor", "batteryLevel", "boltPath", "Landroid/graphics/Path;", "value", "", "charging", "getCharging", "()Z", "setCharging", "(Z)V", "colorLevels", "", "criticalLevel", "getCriticalLevel", "()I", "setCriticalLevel", "(I)V", "dualTone", "dualToneBackgroundFill", "Landroid/graphics/Paint;", "errorPaint", "errorPerimeterPath", "fillColor", "fillColorStrokePaint", "fillColorStrokeProtection", "fillMask", "fillPaint", "fillRect", "Landroid/graphics/RectF;", "intrinsicHeight", "intrinsicWidth", "invalidateRunnable", "Lkotlin/Function0;", "", "invertFillIcon", "levelColor", "levelPath", "levelRect", "padding", "Landroid/graphics/Rect;", "perimeterPath", "plusPath", "powerSaveEnabled", "getPowerSaveEnabled", "setPowerSaveEnabled", "scaleMatrix", "Landroid/graphics/Matrix;", "scaledBolt", "scaledErrorPerimeter", "scaledFill", "scaledPerimeter", "scaledPlus", "unifiedPath", "batteryColorForLevel", "level", "draw", "c", "Landroid/graphics/Canvas;", "getBatteryLevel", "getColorForLevel", "getIntrinsicHeight", "getIntrinsicWidth", "getOpacity", "loadPaths", "onBoundsChange", "bounds", "postInvalidate", "setAlpha", "alpha", "setBatteryLevel", "l", "setColorFilter", "colorFilter", "Landroid/graphics/ColorFilter;", "setColors", "fgColor", "bgColor", "singleToneColor", "setPadding", "left", "top", "right", "bottom", "updateSize", "Companion", "SettingsLib_release"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
/* compiled from: ThemedBatteryDrawable.kt */
public class ThemedBatteryDrawable extends Drawable {
    private static final int CRITICAL_LEVEL = 15;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final float HEIGHT = 20.0f;
    private static final float PROTECTION_MIN_STROKE_WIDTH = 6.0f;
    private static final float PROTECTION_STROKE_WIDTH = 3.0f;
    private static final String TAG = "ThemedBatteryDrawable";
    private static final float WIDTH = 12.0f;
    private int backgroundColor = -65281;
    private int batteryLevel;
    private final Path boltPath = new Path();
    private boolean charging;
    private int[] colorLevels;
    private final Context context;
    private int criticalLevel;
    private boolean dualTone;
    private final Paint dualToneBackgroundFill;
    private final Paint errorPaint;
    private final Path errorPerimeterPath = new Path();
    private int fillColor = -65281;
    private final Paint fillColorStrokePaint;
    private final Paint fillColorStrokeProtection;
    private final Path fillMask = new Path();
    private final Paint fillPaint;
    private final RectF fillRect = new RectF();
    private int intrinsicHeight;
    private int intrinsicWidth;
    private final Function0<Unit> invalidateRunnable = new ThemedBatteryDrawable$invalidateRunnable$1(this);
    private boolean invertFillIcon;
    private int levelColor = -65281;
    private final Path levelPath = new Path();
    private final RectF levelRect = new RectF();
    private final Rect padding = new Rect();
    private final Path perimeterPath = new Path();
    private final Path plusPath = new Path();
    private boolean powerSaveEnabled;
    private final Matrix scaleMatrix = new Matrix();
    private final Path scaledBolt = new Path();
    private final Path scaledErrorPerimeter = new Path();
    private final Path scaledFill = new Path();
    private final Path scaledPerimeter = new Path();
    private final Path scaledPlus = new Path();
    private final Path unifiedPath = new Path();

    public int getOpacity() {
        return -1;
    }

    public void setAlpha(int i) {
    }

    public ThemedBatteryDrawable(Context context2, int i) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        this.criticalLevel = context2.getResources().getInteger(17694772);
        Paint paint = new Paint(1);
        paint.setColor(i);
        paint.setAlpha(255);
        paint.setDither(true);
        paint.setStrokeWidth(5.0f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setBlendMode(BlendMode.SRC);
        paint.setStrokeMiter(5.0f);
        paint.setStrokeJoin(Paint.Join.ROUND);
        this.fillColorStrokePaint = paint;
        Paint paint2 = new Paint(1);
        paint2.setDither(true);
        paint2.setStrokeWidth(5.0f);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setBlendMode(BlendMode.CLEAR);
        paint2.setStrokeMiter(5.0f);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        this.fillColorStrokeProtection = paint2;
        Paint paint3 = new Paint(1);
        paint3.setColor(i);
        paint3.setAlpha(255);
        paint3.setDither(true);
        paint3.setStrokeWidth(0.0f);
        paint3.setStyle(Paint.Style.FILL_AND_STROKE);
        this.fillPaint = paint3;
        Paint paint4 = new Paint(1);
        paint4.setColor(Utils.getColorStateListDefaultColor(context2, C1757R.C1758color.batterymeter_plus_color));
        paint4.setAlpha(255);
        paint4.setDither(true);
        paint4.setStrokeWidth(0.0f);
        paint4.setStyle(Paint.Style.FILL_AND_STROKE);
        paint4.setBlendMode(BlendMode.SRC);
        this.errorPaint = paint4;
        Paint paint5 = new Paint(1);
        paint5.setColor(i);
        paint5.setAlpha(85);
        paint5.setDither(true);
        paint5.setStrokeWidth(0.0f);
        paint5.setStyle(Paint.Style.FILL_AND_STROKE);
        this.dualToneBackgroundFill = paint5;
        float f = context2.getResources().getDisplayMetrics().density;
        this.intrinsicHeight = (int) (HEIGHT * f);
        this.intrinsicWidth = (int) (f * WIDTH);
        Resources resources = context2.getResources();
        TypedArray obtainTypedArray = resources.obtainTypedArray(C1757R.array.batterymeter_color_levels);
        TypedArray obtainTypedArray2 = resources.obtainTypedArray(C1757R.array.batterymeter_color_values);
        int length = obtainTypedArray.length();
        this.colorLevels = new int[(length * 2)];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 * 2;
            this.colorLevels[i3] = obtainTypedArray.getInt(i2, 0);
            if (obtainTypedArray2.getType(i2) == 2) {
                this.colorLevels[i3 + 1] = Utils.getColorAttrDefaultColor(this.context, obtainTypedArray2.getThemeAttributeId(i2, 0));
            } else {
                this.colorLevels[i3 + 1] = obtainTypedArray2.getColor(i2, 0);
            }
        }
        obtainTypedArray.recycle();
        obtainTypedArray2.recycle();
        loadPaths();
    }

    public int getCriticalLevel() {
        return this.criticalLevel;
    }

    public void setCriticalLevel(int i) {
        this.criticalLevel = i;
    }

    public final boolean getCharging() {
        return this.charging;
    }

    public final void setCharging(boolean z) {
        this.charging = z;
        postInvalidate();
    }

    public final boolean getPowerSaveEnabled() {
        return this.powerSaveEnabled;
    }

    public final void setPowerSaveEnabled(boolean z) {
        this.powerSaveEnabled = z;
        postInvalidate();
    }

    public void draw(Canvas canvas) {
        float f;
        Intrinsics.checkNotNullParameter(canvas, "c");
        canvas.saveLayer((RectF) null, (Paint) null);
        this.unifiedPath.reset();
        this.levelPath.reset();
        this.levelRect.set(this.fillRect);
        int i = this.batteryLevel;
        float f2 = ((float) i) / 100.0f;
        if (i >= 95) {
            f = this.fillRect.top;
        } else {
            f = this.fillRect.top + (this.fillRect.height() * (((float) 1) - f2));
        }
        this.levelRect.top = (float) Math.floor((double) f);
        this.levelPath.addRect(this.levelRect, Path.Direction.CCW);
        this.unifiedPath.addPath(this.scaledPerimeter);
        if (!this.dualTone) {
            this.unifiedPath.op(this.levelPath, Path.Op.UNION);
        }
        this.fillPaint.setColor(this.levelColor);
        if (this.charging) {
            this.unifiedPath.op(this.scaledBolt, Path.Op.DIFFERENCE);
            if (!this.invertFillIcon) {
                canvas.drawPath(this.scaledBolt, this.fillPaint);
            }
        }
        if (this.dualTone) {
            canvas.drawPath(this.unifiedPath, this.dualToneBackgroundFill);
            canvas.save();
            canvas.clipRect(0.0f, ((float) getBounds().bottom) - (((float) getBounds().height()) * f2), (float) getBounds().right, (float) getBounds().bottom);
            canvas.drawPath(this.unifiedPath, this.fillPaint);
            canvas.restore();
        } else {
            this.fillPaint.setColor(this.fillColor);
            canvas.drawPath(this.unifiedPath, this.fillPaint);
            this.fillPaint.setColor(this.levelColor);
            if (this.batteryLevel <= 15 && !this.charging) {
                canvas.save();
                canvas.clipPath(this.scaledFill);
                canvas.drawPath(this.levelPath, this.fillPaint);
                canvas.restore();
            }
        }
        if (this.charging) {
            canvas.clipOutPath(this.scaledBolt);
            if (this.invertFillIcon) {
                canvas.drawPath(this.scaledBolt, this.fillColorStrokePaint);
            } else {
                canvas.drawPath(this.scaledBolt, this.fillColorStrokeProtection);
            }
        } else if (this.powerSaveEnabled) {
            canvas.drawPath(this.scaledErrorPerimeter, this.errorPaint);
            canvas.drawPath(this.scaledPlus, this.errorPaint);
        }
        canvas.restore();
    }

    private final int batteryColorForLevel(int i) {
        if (this.charging || this.powerSaveEnabled) {
            return this.fillColor;
        }
        return getColorForLevel(i);
    }

    private final int getColorForLevel(int i) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int[] iArr = this.colorLevels;
            if (i2 >= iArr.length) {
                return i3;
            }
            int i4 = iArr[i2];
            int i5 = iArr[i2 + 1];
            if (i <= i4) {
                return i2 == iArr.length + -2 ? this.fillColor : i5;
            }
            i2 += 2;
            i3 = i5;
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.fillPaint.setColorFilter(colorFilter);
        this.fillColorStrokePaint.setColorFilter(colorFilter);
        this.dualToneBackgroundFill.setColorFilter(colorFilter);
    }

    public int getIntrinsicHeight() {
        return this.intrinsicHeight;
    }

    public int getIntrinsicWidth() {
        return this.intrinsicWidth;
    }

    public void setBatteryLevel(int i) {
        this.invertFillIcon = i >= 67 ? true : i <= 33 ? false : this.invertFillIcon;
        this.batteryLevel = i;
        this.levelColor = batteryColorForLevel(i);
        invalidateSelf();
    }

    public final int getBatteryLevel() {
        return this.batteryLevel;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        updateSize();
    }

    public final void setPadding(int i, int i2, int i3, int i4) {
        this.padding.left = i;
        this.padding.top = i2;
        this.padding.right = i3;
        this.padding.bottom = i4;
        updateSize();
    }

    public final void setColors(int i, int i2, int i3) {
        if (!this.dualTone) {
            i = i3;
        }
        this.fillColor = i;
        this.fillPaint.setColor(i);
        this.fillColorStrokePaint.setColor(this.fillColor);
        this.backgroundColor = i2;
        this.dualToneBackgroundFill.setColor(i2);
        this.levelColor = batteryColorForLevel(this.batteryLevel);
        invalidateSelf();
    }

    private final void postInvalidate() {
        unscheduleSelf(new ThemedBatteryDrawable$$ExternalSyntheticLambda0(this.invalidateRunnable));
        scheduleSelf(new ThemedBatteryDrawable$$ExternalSyntheticLambda1(this.invalidateRunnable), 0);
    }

    /* access modifiers changed from: private */
    /* renamed from: postInvalidate$lambda-5  reason: not valid java name */
    public static final void m2509postInvalidate$lambda5(Function0 function0) {
        Intrinsics.checkNotNullParameter(function0, "$tmp0");
        function0.invoke();
    }

    /* access modifiers changed from: private */
    /* renamed from: postInvalidate$lambda-6  reason: not valid java name */
    public static final void m2510postInvalidate$lambda6(Function0 function0) {
        Intrinsics.checkNotNullParameter(function0, "$tmp0");
        function0.invoke();
    }

    private final void updateSize() {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            this.scaleMatrix.setScale(1.0f, 1.0f);
        } else {
            this.scaleMatrix.setScale(((float) bounds.right) / WIDTH, ((float) bounds.bottom) / HEIGHT);
        }
        this.perimeterPath.transform(this.scaleMatrix, this.scaledPerimeter);
        this.errorPerimeterPath.transform(this.scaleMatrix, this.scaledErrorPerimeter);
        this.fillMask.transform(this.scaleMatrix, this.scaledFill);
        this.scaledFill.computeBounds(this.fillRect, true);
        this.boltPath.transform(this.scaleMatrix, this.scaledBolt);
        this.plusPath.transform(this.scaleMatrix, this.scaledPlus);
        float max = Math.max((((float) bounds.right) / WIDTH) * 3.0f, (float) PROTECTION_MIN_STROKE_WIDTH);
        this.fillColorStrokePaint.setStrokeWidth(max);
        this.fillColorStrokeProtection.setStrokeWidth(max);
    }

    private final void loadPaths() {
        this.perimeterPath.set(PathParser.createPathFromPathData(this.context.getResources().getString(17039891)));
        this.perimeterPath.computeBounds(new RectF(), true);
        this.errorPerimeterPath.set(PathParser.createPathFromPathData(this.context.getResources().getString(17039889)));
        this.errorPerimeterPath.computeBounds(new RectF(), true);
        this.fillMask.set(PathParser.createPathFromPathData(this.context.getResources().getString(17039890)));
        this.fillMask.computeBounds(this.fillRect, true);
        this.boltPath.set(PathParser.createPathFromPathData(this.context.getResources().getString(17039888)));
        this.plusPath.set(PathParser.createPathFromPathData(this.context.getResources().getString(17039892)));
        this.dualTone = this.context.getResources().getBoolean(17891385);
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"Lcom/android/settingslib/graph/ThemedBatteryDrawable$Companion;", "", "()V", "CRITICAL_LEVEL", "", "HEIGHT", "", "PROTECTION_MIN_STROKE_WIDTH", "PROTECTION_STROKE_WIDTH", "TAG", "", "WIDTH", "SettingsLib_release"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
    /* compiled from: ThemedBatteryDrawable.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
