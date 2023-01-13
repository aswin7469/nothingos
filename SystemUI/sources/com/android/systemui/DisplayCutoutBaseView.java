package com.android.systemui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.View;
import androidx.slice.compat.SliceProviderCompat;
import com.android.systemui.RegionInterceptingFrameLayout;
import com.android.systemui.animation.Interpolators;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\n\b\u0016\u0018\u0000 S2\u00020\u00012\u00020\u0002:\u0001SB\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0019\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bB!\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u001c\u00105\u001a\u00020+2\b\u00106\u001a\u0004\u0018\u00010\u001c2\b\u00107\u001a\u0004\u0018\u00010\u001cH\u0002J\u0010\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020;H\u0014J\u0010\u0010<\u001a\u0002092\u0006\u0010:\u001a\u00020;H\u0017J\u0010\u0010=\u001a\u0002092\u0006\u0010>\u001a\u00020+H\u0016J\n\u0010?\u001a\u0004\u0018\u00010@H\u0016J\b\u0010A\u001a\u00020\u000fH\u0017J\b\u0010B\u001a\u000209H\u0014J\u000e\u0010C\u001a\u0002092\u0006\u0010D\u001a\u00020\nJ\u0010\u0010E\u001a\u0002092\u0006\u0010:\u001a\u00020;H\u0017J\b\u0010F\u001a\u000209H\u0016J\u0018\u0010G\u001a\u00020@2\u0010\u0010H\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010J\u0018\u00010IJ\u0018\u0010K\u001a\u0002092\u0006\u0010L\u001a\u00020\u00172\u0006\u0010M\u001a\u00020JH\u0016J\b\u0010N\u001a\u00020+H\u0016J\b\u0010O\u001a\u000209H\u0017J\b\u0010P\u001a\u000209H\u0014J\u0010\u0010Q\u001a\u0002092\u0006\u0010R\u001a\u00020\nH\u0016R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R$\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0010\u0010\u0016\u001a\u00020\u00178\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0018\u001a\u00020\u00198\u0006X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u001a\u0010\u0011R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010$\u001a\u00020%X\u0004¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0010\u0010(\u001a\u00020)8\u0004X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010*\u001a\u00020+8\u0004@\u0004X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010,\u001a\u00020\u00178\u0006X\u0004¢\u0006\b\n\u0000\u0012\u0004\b-\u0010\u0011R\u000e\u0010.\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010/\u001a\u0002008\u0006X\u0004¢\u0006\b\n\u0000\u0012\u0004\b1\u0010\u0011R\u000e\u00102\u001a\u000200X\u0004¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020+X\u000e¢\u0006\u0002\n\u0000R\u0012\u00104\u001a\u00020+8\u0004@\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006T"}, mo65043d2 = {"Lcom/android/systemui/DisplayCutoutBaseView;", "Landroid/view/View;", "Lcom/android/systemui/RegionInterceptingFrameLayout$RegionInterceptableView;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "cameraProtectionAnimator", "Landroid/animation/ValueAnimator;", "cameraProtectionProgress", "", "getCameraProtectionProgress$annotations", "()V", "getCameraProtectionProgress", "()F", "setCameraProtectionProgress", "(F)V", "cutoutPath", "Landroid/graphics/Path;", "displayInfo", "Landroid/view/DisplayInfo;", "getDisplayInfo$annotations", "displayMode", "Landroid/view/Display$Mode;", "displayRotation", "getDisplayRotation", "()I", "setDisplayRotation", "(I)V", "displayUniqueId", "", "location", "", "getLocation", "()[I", "paint", "Landroid/graphics/Paint;", "pendingConfigChange", "", "protectionPath", "getProtectionPath$annotations", "protectionPathOrig", "protectionRect", "Landroid/graphics/RectF;", "getProtectionRect$annotations", "protectionRectOrig", "shouldDrawCutout", "showProtection", "displayModeChanged", "oldMode", "newMode", "drawCutoutProtection", "", "canvas", "Landroid/graphics/Canvas;", "drawCutouts", "enableShowProtection", "show", "getInterceptRegion", "Landroid/graphics/Region;", "getPhysicalPixelDisplaySizeRatio", "onAttachedToWindow", "onDisplayChanged", "displayId", "onDraw", "onUpdate", "rectsToRegion", "rects", "", "Landroid/graphics/Rect;", "setProtection", "path", "pathBounds", "shouldInterceptTouch", "updateCutout", "updateProtectionBoundingPath", "updateRotation", "rotation", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DisplayCutoutBaseView.kt */
public class DisplayCutoutBaseView extends View implements RegionInterceptingFrameLayout.RegionInterceptableView {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final float HIDDEN_CAMERA_PROTECTION_SCALE = 0.5f;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    /* access modifiers changed from: private */
    public ValueAnimator cameraProtectionAnimator;
    private float cameraProtectionProgress;
    protected final Path cutoutPath;
    public final DisplayInfo displayInfo;
    private Display.Mode displayMode;
    private int displayRotation;
    private String displayUniqueId;
    private final int[] location;
    protected final Paint paint;
    protected boolean pendingConfigChange;
    public final Path protectionPath;
    private final Path protectionPathOrig;
    public final RectF protectionRect;
    private final RectF protectionRectOrig;
    private boolean shouldDrawCutout;
    protected boolean showProtection;

    public static /* synthetic */ void getCameraProtectionProgress$annotations() {
    }

    public static /* synthetic */ void getDisplayInfo$annotations() {
    }

    public static /* synthetic */ void getProtectionPath$annotations() {
    }

    public static /* synthetic */ void getProtectionRect$annotations() {
    }

    @JvmStatic
    protected static final void transformPhysicalToLogicalCoordinates(int i, int i2, int i3, Matrix matrix) {
        Companion.transformPhysicalToLogicalCoordinates(i, i2, i3, matrix);
    }

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public void onUpdate() {
    }

    /* access modifiers changed from: protected */
    public final int[] getLocation() {
        return this.location;
    }

    /* access modifiers changed from: protected */
    public final int getDisplayRotation() {
        return this.displayRotation;
    }

    /* access modifiers changed from: protected */
    public final void setDisplayRotation(int i) {
        this.displayRotation = i;
    }

    public final float getCameraProtectionProgress() {
        return this.cameraProtectionProgress;
    }

    public final void setCameraProtectionProgress(float f) {
        this.cameraProtectionProgress = f;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DisplayCutoutBaseView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Resources resources = getContext().getResources();
        Display display = getContext().getDisplay();
        this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(resources, display != null ? display.getUniqueId() : null);
        this.location = new int[2];
        this.displayInfo = new DisplayInfo();
        this.paint = new Paint();
        this.cutoutPath = new Path();
        this.protectionRect = new RectF();
        this.protectionPath = new Path();
        this.protectionRectOrig = new RectF();
        this.protectionPathOrig = new Path();
        this.cameraProtectionProgress = 0.5f;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DisplayCutoutBaseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        Resources resources = getContext().getResources();
        Display display = getContext().getDisplay();
        this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(resources, display != null ? display.getUniqueId() : null);
        this.location = new int[2];
        this.displayInfo = new DisplayInfo();
        this.paint = new Paint();
        this.cutoutPath = new Path();
        this.protectionRect = new RectF();
        this.protectionPath = new Path();
        this.protectionRectOrig = new RectF();
        this.protectionPathOrig = new Path();
        this.cameraProtectionProgress = 0.5f;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DisplayCutoutBaseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        Resources resources = getContext().getResources();
        Display display = getContext().getDisplay();
        this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(resources, display != null ? display.getUniqueId() : null);
        this.location = new int[2];
        this.displayInfo = new DisplayInfo();
        this.paint = new Paint();
        this.cutoutPath = new Path();
        this.protectionRect = new RectF();
        this.protectionPath = new Path();
        this.protectionRectOrig = new RectF();
        this.protectionPathOrig = new Path();
        this.cameraProtectionProgress = 0.5f;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Display display = getContext().getDisplay();
        this.displayUniqueId = display != null ? display.getUniqueId() : null;
        updateCutout();
        updateProtectionBoundingPath();
        onUpdate();
    }

    public final void onDisplayChanged(int i) {
        Display.Mode mode = this.displayMode;
        Display display = getContext().getDisplay();
        DisplayCutout displayCutout = null;
        this.displayMode = display != null ? display.getMode() : null;
        if (!Intrinsics.areEqual((Object) this.displayUniqueId, (Object) display != null ? display.getUniqueId() : null)) {
            this.displayUniqueId = display != null ? display.getUniqueId() : null;
            this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(getContext().getResources(), this.displayUniqueId);
        }
        if (!displayModeChanged(mode, this.displayMode)) {
            if (display != null) {
                displayCutout = display.getCutout();
            }
            if (Intrinsics.areEqual((Object) displayCutout, (Object) this.displayInfo.displayCutout)) {
                return;
            }
        }
        boolean z = false;
        if (display != null && i == display.getDisplayId()) {
            z = true;
        }
        if (z) {
            updateCutout();
            updateProtectionBoundingPath();
            onUpdate();
        }
    }

    public void updateRotation(int i) {
        this.displayRotation = i;
        updateCutout();
        updateProtectionBoundingPath();
        onUpdate();
    }

    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.onDraw(canvas);
        if (this.shouldDrawCutout) {
            canvas.save();
            getLocationOnScreen(this.location);
            int[] iArr = this.location;
            canvas.translate(-((float) iArr[0]), -((float) iArr[1]));
            drawCutouts(canvas);
            drawCutoutProtection(canvas);
            canvas.restore();
        }
    }

    public boolean shouldInterceptTouch() {
        return this.displayInfo.displayCutout != null && getVisibility() == 0 && this.shouldDrawCutout;
    }

    public Region getInterceptRegion() {
        List<Rect> list = null;
        if (this.displayInfo.displayCutout == null) {
            return null;
        }
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout != null) {
            list = displayCutout.getBoundingRects();
        }
        Region rectsToRegion = rectsToRegion(list);
        getRootView().getLocationOnScreen(this.location);
        int[] iArr = this.location;
        rectsToRegion.translate(-iArr[0], -iArr[1]);
        rectsToRegion.op(getRootView().getLeft(), getRootView().getTop(), getRootView().getRight(), getRootView().getBottom(), Region.Op.INTERSECT);
        return rectsToRegion;
    }

    public void updateCutout() {
        Path cutoutPath2;
        if (!this.pendingConfigChange) {
            this.cutoutPath.reset();
            getDisplay().getDisplayInfo(this.displayInfo);
            DisplayCutout displayCutout = this.displayInfo.displayCutout;
            if (!(displayCutout == null || (cutoutPath2 = displayCutout.getCutoutPath()) == null)) {
                this.cutoutPath.set(cutoutPath2);
            }
            invalidate();
        }
    }

    public void drawCutouts(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if ((displayCutout != null ? displayCutout.getCutoutPath() : null) != null) {
            canvas.drawPath(this.cutoutPath, this.paint);
        }
    }

    /* access modifiers changed from: protected */
    public void drawCutoutProtection(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        if (this.cameraProtectionProgress > 0.5f && !this.protectionRect.isEmpty()) {
            float f = this.cameraProtectionProgress;
            canvas.scale(f, f, this.protectionRect.centerX(), this.protectionRect.centerY());
            canvas.drawPath(this.protectionPath, this.paint);
        }
    }

    public final Region rectsToRegion(List<Rect> list) {
        Region obtain = Region.obtain();
        if (list != null) {
            for (Rect next : list) {
                if (next != null && !next.isEmpty()) {
                    obtain.op(next, Region.Op.UNION);
                }
            }
        }
        Intrinsics.checkNotNullExpressionValue(obtain, SliceProviderCompat.EXTRA_RESULT);
        return obtain;
    }

    public void enableShowProtection(boolean z) {
        if (this.showProtection != z) {
            this.showProtection = z;
            updateProtectionBoundingPath();
            if (this.showProtection) {
                requestLayout();
            }
            ValueAnimator valueAnimator = this.cameraProtectionAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.cameraProtectionProgress;
            fArr[1] = this.showProtection ? 1.0f : 0.5f;
            ValueAnimator duration = ValueAnimator.ofFloat(fArr).setDuration(750);
            this.cameraProtectionAnimator = duration;
            if (duration != null) {
                duration.setInterpolator(Interpolators.DECELERATE_QUINT);
            }
            ValueAnimator valueAnimator2 = this.cameraProtectionAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.addUpdateListener(new DisplayCutoutBaseView$$ExternalSyntheticLambda0(this));
            }
            ValueAnimator valueAnimator3 = this.cameraProtectionAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.addListener(new DisplayCutoutBaseView$enableShowProtection$2(this));
            }
            ValueAnimator valueAnimator4 = this.cameraProtectionAnimator;
            if (valueAnimator4 != null) {
                valueAnimator4.start();
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: enableShowProtection$lambda-1  reason: not valid java name */
    public static final void m2520enableShowProtection$lambda1(DisplayCutoutBaseView displayCutoutBaseView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(displayCutoutBaseView, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            displayCutoutBaseView.cameraProtectionProgress = ((Float) animatedValue).floatValue();
            displayCutoutBaseView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public void setProtection(Path path, Rect rect) {
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(rect, "pathBounds");
        this.protectionPathOrig.reset();
        this.protectionPathOrig.set(path);
        this.protectionPath.reset();
        this.protectionRectOrig.setEmpty();
        this.protectionRectOrig.set(rect);
        this.protectionRect.setEmpty();
    }

    /* access modifiers changed from: protected */
    public void updateProtectionBoundingPath() {
        if (!this.pendingConfigChange) {
            Matrix matrix = new Matrix();
            float physicalPixelDisplaySizeRatio = getPhysicalPixelDisplaySizeRatio();
            matrix.postScale(physicalPixelDisplaySizeRatio, physicalPixelDisplaySizeRatio);
            int i = this.displayInfo.logicalWidth;
            int i2 = this.displayInfo.logicalHeight;
            boolean z = true;
            if (!(this.displayInfo.rotation == 1 || this.displayInfo.rotation == 3)) {
                z = false;
            }
            int i3 = z ? i2 : i;
            if (!z) {
                i = i2;
            }
            transformPhysicalToLogicalCoordinates(this.displayInfo.rotation, i3, i, matrix);
            if (!this.protectionPathOrig.isEmpty()) {
                this.protectionPath.set(this.protectionPathOrig);
                this.protectionPath.transform(matrix);
                matrix.mapRect(this.protectionRect, this.protectionRectOrig);
            }
        }
    }

    public float getPhysicalPixelDisplaySizeRatio() {
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout != null) {
            return displayCutout.getCutoutPathParserInfo().getPhysicalPixelDisplaySizeRatio();
        }
        return 1.0f;
    }

    private final boolean displayModeChanged(Display.Mode mode, Display.Mode mode2) {
        if (mode == null) {
            return true;
        }
        Integer num = null;
        if (!Intrinsics.areEqual((Object) Integer.valueOf(mode.getPhysicalHeight()), (Object) mode2 != null ? Integer.valueOf(mode2.getPhysicalHeight()) : null)) {
            return true;
        }
        Integer valueOf = Integer.valueOf(mode.getPhysicalWidth());
        if (mode2 != null) {
            num = Integer.valueOf(mode2.getPhysicalWidth());
        }
        if (!Intrinsics.areEqual((Object) valueOf, (Object) num)) {
            return true;
        }
        return false;
    }

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J(\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0005R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/DisplayCutoutBaseView$Companion;", "", "()V", "HIDDEN_CAMERA_PROTECTION_SCALE", "", "transformPhysicalToLogicalCoordinates", "", "rotation", "", "physicalWidth", "physicalHeight", "out", "Landroid/graphics/Matrix;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DisplayCutoutBaseView.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* access modifiers changed from: protected */
        @JvmStatic
        public final void transformPhysicalToLogicalCoordinates(int i, int i2, int i3, Matrix matrix) {
            Intrinsics.checkNotNullParameter(matrix, "out");
            if (i == 0) {
                return;
            }
            if (i == 1) {
                matrix.postRotate(270.0f);
                matrix.postTranslate(0.0f, (float) i2);
            } else if (i == 2) {
                matrix.postRotate(180.0f);
                matrix.postTranslate((float) i2, (float) i3);
            } else if (i == 3) {
                matrix.postRotate(90.0f);
                matrix.postTranslate((float) i3, 0.0f);
            } else {
                throw new IllegalArgumentException("Unknown rotation: " + i);
            }
        }
    }
}
