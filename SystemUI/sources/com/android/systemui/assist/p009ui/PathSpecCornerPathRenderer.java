package com.android.systemui.assist.p009ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.util.PathParser;
import com.android.systemui.C1893R;
import com.android.systemui.assist.p009ui.CornerPathRenderer;

/* renamed from: com.android.systemui.assist.ui.PathSpecCornerPathRenderer */
public final class PathSpecCornerPathRenderer extends CornerPathRenderer {
    private static final String TAG = "PathSpecCornerPathRenderer";
    private final int mBottomCornerRadius;
    private final int mHeight;
    private final Matrix mMatrix = new Matrix();
    private final Path mPath = new Path();
    private final float mPathScale;
    private final Path mRoundedPath;
    private final int mTopCornerRadius;
    private final int mWidth;

    public PathSpecCornerPathRenderer(Context context) {
        this.mWidth = DisplayUtils.getWidth(context);
        this.mHeight = DisplayUtils.getHeight(context);
        this.mBottomCornerRadius = DisplayUtils.getCornerRadiusBottom(context);
        this.mTopCornerRadius = DisplayUtils.getCornerRadiusTop(context);
        Path createPathFromPathData = PathParser.createPathFromPathData(context.getResources().getString(C1893R.string.config_rounded_mask));
        if (createPathFromPathData == null) {
            Log.e(TAG, "No rounded corner path found!");
            this.mRoundedPath = new Path();
        } else {
            this.mRoundedPath = createPathFromPathData;
        }
        RectF rectF = new RectF();
        this.mRoundedPath.computeBounds(rectF, true);
        this.mPathScale = Math.min(Math.abs(rectF.right - rectF.left), Math.abs(rectF.top - rectF.bottom));
    }

    public Path getCornerPath(CornerPathRenderer.Corner corner) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (this.mRoundedPath.isEmpty()) {
            return this.mRoundedPath;
        }
        int i7 = C19541.f295x853e7b3e[corner.ordinal()];
        int i8 = 0;
        if (i7 != 1) {
            if (i7 != 2) {
                if (i7 != 3) {
                    i = this.mBottomCornerRadius;
                    i6 = this.mHeight;
                    i5 = 270;
                } else {
                    i = this.mBottomCornerRadius;
                    i8 = this.mWidth;
                    i6 = this.mHeight;
                    i5 = 180;
                }
                i4 = i5;
                i2 = i6;
                i3 = i8;
            } else {
                i = this.mTopCornerRadius;
                i3 = this.mWidth;
                i4 = 90;
                i2 = 0;
            }
            i8 = i4;
        } else {
            i = this.mTopCornerRadius;
            i3 = 0;
            i2 = 0;
        }
        this.mPath.reset();
        this.mMatrix.reset();
        this.mPath.addPath(this.mRoundedPath);
        Matrix matrix = this.mMatrix;
        float f = (float) i;
        float f2 = this.mPathScale;
        matrix.preScale(f / f2, f / f2);
        this.mMatrix.postRotate((float) i8);
        this.mMatrix.postTranslate((float) i3, (float) i2);
        this.mPath.transform(this.mMatrix);
        return this.mPath;
    }

    /* renamed from: com.android.systemui.assist.ui.PathSpecCornerPathRenderer$1 */
    static /* synthetic */ class C19541 {

        /* renamed from: $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner */
        static final /* synthetic */ int[] f295x853e7b3e;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.android.systemui.assist.ui.CornerPathRenderer$Corner[] r0 = com.android.systemui.assist.p009ui.CornerPathRenderer.Corner.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f295x853e7b3e = r0
                com.android.systemui.assist.ui.CornerPathRenderer$Corner r1 = com.android.systemui.assist.p009ui.CornerPathRenderer.Corner.TOP_LEFT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f295x853e7b3e     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.assist.ui.CornerPathRenderer$Corner r1 = com.android.systemui.assist.p009ui.CornerPathRenderer.Corner.TOP_RIGHT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f295x853e7b3e     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.assist.ui.CornerPathRenderer$Corner r1 = com.android.systemui.assist.p009ui.CornerPathRenderer.Corner.BOTTOM_RIGHT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f295x853e7b3e     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.assist.ui.CornerPathRenderer$Corner r1 = com.android.systemui.assist.p009ui.CornerPathRenderer.Corner.BOTTOM_LEFT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.assist.p009ui.PathSpecCornerPathRenderer.C19541.<clinit>():void");
        }
    }
}
