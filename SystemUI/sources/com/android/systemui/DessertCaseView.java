package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.StatsManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import java.util.HashSet;
import java.util.Set;

public class DessertCaseView extends FrameLayout {
    private static final float[] ALPHA_MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private static final boolean DEBUG = false;
    static final int DELAY = 2000;
    static final int DURATION = 500;
    private static final float[] MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private static final int NUM_PASTRIES;
    private static final int[] PASTRIES;
    private static final float PROB_2X = 0.33f;
    private static final float PROB_3X = 0.1f;
    private static final float PROB_4X = 0.01f;
    private static final int[] RARE_PASTRIES;
    public static final float SCALE = 0.25f;
    static final int START_DELAY = 5000;
    private static final String TAG = "DessertCaseView";
    private static final int TAG_POS = 33554433;
    private static final int TAG_SPAN = 33554434;
    private static final float[] WHITE_MASK = {0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, -1.0f, 0.0f, 0.0f, 0.0f, 255.0f};
    private static final int[] XRARE_PASTRIES;
    private static final int[] XXRARE_PASTRIES;
    float[] hsv;
    private int mCellSize;
    private View[] mCells;
    private int mColumns;
    private SparseArray<Drawable> mDrawables;
    private final Set<Point> mFreeList;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private int mHeight;
    /* access modifiers changed from: private */
    public final Runnable mJuggle;
    private int mRows;
    /* access modifiers changed from: private */
    public boolean mStarted;
    private int mWidth;
    private final HashSet<View> tmpSet;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    static {
        int[] iArr = {C1894R.C1896drawable.dessert_kitkat, C1894R.C1896drawable.dessert_android};
        PASTRIES = iArr;
        int[] iArr2 = {C1894R.C1896drawable.dessert_cupcake, C1894R.C1896drawable.dessert_donut, C1894R.C1896drawable.dessert_eclair, C1894R.C1896drawable.dessert_froyo, C1894R.C1896drawable.dessert_gingerbread, C1894R.C1896drawable.dessert_honeycomb, C1894R.C1896drawable.dessert_ics, C1894R.C1896drawable.dessert_jellybean};
        RARE_PASTRIES = iArr2;
        int[] iArr3 = {C1894R.C1896drawable.dessert_petitfour, C1894R.C1896drawable.dessert_donutburger, C1894R.C1896drawable.dessert_flan, C1894R.C1896drawable.dessert_keylimepie};
        XRARE_PASTRIES = iArr3;
        int[] iArr4 = {C1894R.C1896drawable.dessert_zombiegingerbread, C1894R.C1896drawable.dessert_dandroid, C1894R.C1896drawable.dessert_jandycane};
        XXRARE_PASTRIES = iArr4;
        NUM_PASTRIES = iArr.length + iArr2.length + iArr3.length + iArr4.length;
    }

    public DessertCaseView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DessertCaseView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DessertCaseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDrawables = new SparseArray<>(NUM_PASTRIES);
        this.mFreeList = new HashSet();
        this.mHandler = new Handler();
        this.mJuggle = new Runnable() {
            public void run() {
                DessertCaseView.this.place(DessertCaseView.this.getChildAt((int) (Math.random() * ((double) DessertCaseView.this.getChildCount()))), true);
                DessertCaseView.this.fillFreeList();
                if (DessertCaseView.this.mStarted) {
                    DessertCaseView.this.mHandler.postDelayed(DessertCaseView.this.mJuggle, StatsManager.DEFAULT_TIMEOUT_MILLIS);
                }
            }
        };
        this.hsv = new float[]{0.0f, 1.0f, 0.85f};
        this.tmpSet = new HashSet<>();
        Resources resources = getResources();
        this.mStarted = false;
        this.mCellSize = resources.getDimensionPixelSize(C1894R.dimen.dessert_case_cell_size);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (this.mCellSize < 512) {
            options.inSampleSize = 2;
        }
        options.inMutable = true;
        int[][] iArr = {PASTRIES, RARE_PASTRIES, XRARE_PASTRIES, XXRARE_PASTRIES};
        Bitmap bitmap = null;
        for (int i2 = 0; i2 < 4; i2++) {
            for (int i3 : iArr[i2]) {
                options.inBitmap = bitmap;
                bitmap = BitmapFactory.decodeResource(resources, i3, options);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, convertToAlphaMask(bitmap));
                bitmapDrawable.setColorFilter(new ColorMatrixColorFilter(ALPHA_MASK));
                int i4 = this.mCellSize;
                bitmapDrawable.setBounds(0, 0, i4, i4);
                this.mDrawables.append(i3, bitmapDrawable);
            }
        }
    }

    private static Bitmap convertToAlphaMask(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(MASK));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public void start() {
        if (!this.mStarted) {
            this.mStarted = true;
            fillFreeList(2000);
        }
        this.mHandler.postDelayed(this.mJuggle, 5000);
    }

    public void stop() {
        this.mStarted = false;
        this.mHandler.removeCallbacks(this.mJuggle);
    }

    /* access modifiers changed from: package-private */
    public int pick(int[] iArr) {
        return iArr[(int) (Math.random() * ((double) iArr.length))];
    }

    /* access modifiers changed from: package-private */
    public <T> T pick(T[] tArr) {
        return tArr[(int) (Math.random() * ((double) tArr.length))];
    }

    /* access modifiers changed from: package-private */
    public <T> T pick(SparseArray<T> sparseArray) {
        return sparseArray.valueAt((int) (Math.random() * ((double) sparseArray.size())));
    }

    /* access modifiers changed from: package-private */
    public int random_color() {
        this.hsv[0] = ((float) irand(0, 12)) * 30.0f;
        return Color.HSVToColor(this.hsv);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onSizeChanged(int r3, int r4, int r5, int r6) {
        /*
            r2 = this;
            monitor-enter(r2)
            super.onSizeChanged(r3, r4, r5, r6)     // Catch:{ all -> 0x007d }
            int r5 = r2.mWidth     // Catch:{ all -> 0x007d }
            if (r5 != r3) goto L_0x000e
            int r5 = r2.mHeight     // Catch:{ all -> 0x007d }
            if (r5 != r4) goto L_0x000e
            monitor-exit(r2)
            return
        L_0x000e:
            boolean r5 = r2.mStarted     // Catch:{ all -> 0x007d }
            if (r5 == 0) goto L_0x0015
            r2.stop()     // Catch:{ all -> 0x007d }
        L_0x0015:
            r2.mWidth = r3     // Catch:{ all -> 0x007d }
            r2.mHeight = r4     // Catch:{ all -> 0x007d }
            r3 = 0
            r2.mCells = r3     // Catch:{ all -> 0x007d }
            r2.removeAllViewsInLayout()     // Catch:{ all -> 0x007d }
            java.util.Set<android.graphics.Point> r3 = r2.mFreeList     // Catch:{ all -> 0x007d }
            r3.clear()     // Catch:{ all -> 0x007d }
            int r3 = r2.mHeight     // Catch:{ all -> 0x007d }
            int r4 = r2.mCellSize     // Catch:{ all -> 0x007d }
            int r3 = r3 / r4
            r2.mRows = r3     // Catch:{ all -> 0x007d }
            int r6 = r2.mWidth     // Catch:{ all -> 0x007d }
            int r6 = r6 / r4
            r2.mColumns = r6     // Catch:{ all -> 0x007d }
            int r3 = r3 * r6
            android.view.View[] r3 = new android.view.View[r3]     // Catch:{ all -> 0x007d }
            r2.mCells = r3     // Catch:{ all -> 0x007d }
            r3 = 1048576000(0x3e800000, float:0.25)
            r2.setScaleX(r3)     // Catch:{ all -> 0x007d }
            r2.setScaleY(r3)     // Catch:{ all -> 0x007d }
            int r4 = r2.mWidth     // Catch:{ all -> 0x007d }
            int r6 = r2.mCellSize     // Catch:{ all -> 0x007d }
            int r0 = r2.mColumns     // Catch:{ all -> 0x007d }
            int r6 = r6 * r0
            int r4 = r4 - r6
            float r4 = (float) r4     // Catch:{ all -> 0x007d }
            r6 = 1056964608(0x3f000000, float:0.5)
            float r4 = r4 * r6
            float r4 = r4 * r3
            r2.setTranslationX(r4)     // Catch:{ all -> 0x007d }
            int r4 = r2.mHeight     // Catch:{ all -> 0x007d }
            int r0 = r2.mCellSize     // Catch:{ all -> 0x007d }
            int r1 = r2.mRows     // Catch:{ all -> 0x007d }
            int r0 = r0 * r1
            int r4 = r4 - r0
            float r4 = (float) r4     // Catch:{ all -> 0x007d }
            float r4 = r4 * r6
            float r4 = r4 * r3
            r2.setTranslationY(r4)     // Catch:{ all -> 0x007d }
            r3 = 0
            r4 = r3
        L_0x005d:
            int r6 = r2.mRows     // Catch:{ all -> 0x007d }
            if (r4 >= r6) goto L_0x0076
            r6 = r3
        L_0x0062:
            int r0 = r2.mColumns     // Catch:{ all -> 0x007d }
            if (r6 >= r0) goto L_0x0073
            java.util.Set<android.graphics.Point> r0 = r2.mFreeList     // Catch:{ all -> 0x007d }
            android.graphics.Point r1 = new android.graphics.Point     // Catch:{ all -> 0x007d }
            r1.<init>(r6, r4)     // Catch:{ all -> 0x007d }
            r0.add(r1)     // Catch:{ all -> 0x007d }
            int r6 = r6 + 1
            goto L_0x0062
        L_0x0073:
            int r4 = r4 + 1
            goto L_0x005d
        L_0x0076:
            if (r5 == 0) goto L_0x007b
            r2.start()     // Catch:{ all -> 0x007d }
        L_0x007b:
            monitor-exit(r2)
            return
        L_0x007d:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.DessertCaseView.onSizeChanged(int, int, int, int):void");
    }

    public void fillFreeList() {
        fillFreeList(500);
    }

    public synchronized void fillFreeList(int i) {
        Drawable drawable;
        Context context = getContext();
        int i2 = this.mCellSize;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i2, i2);
        while (!this.mFreeList.isEmpty()) {
            Point next = this.mFreeList.iterator().next();
            this.mFreeList.remove(next);
            if (this.mCells[(next.y * this.mColumns) + next.x] == null) {
                final ImageView imageView = new ImageView(context);
                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DessertCaseView.this.place(imageView, true);
                        DessertCaseView.this.postDelayed(new Runnable() {
                            public void run() {
                                DessertCaseView.this.fillFreeList();
                            }
                        }, 250);
                    }
                });
                imageView.setBackgroundColor(random_color());
                float frand = frand();
                if (frand < 5.0E-4f) {
                    drawable = this.mDrawables.get(pick(XXRARE_PASTRIES));
                } else if (frand < 0.005f) {
                    drawable = this.mDrawables.get(pick(XRARE_PASTRIES));
                } else if (frand < 0.5f) {
                    drawable = this.mDrawables.get(pick(RARE_PASTRIES));
                } else {
                    drawable = frand < 0.7f ? this.mDrawables.get(pick(PASTRIES)) : null;
                }
                if (drawable != null) {
                    imageView.getOverlay().add(drawable);
                }
                int i3 = this.mCellSize;
                layoutParams.height = i3;
                layoutParams.width = i3;
                addView(imageView, layoutParams);
                place(imageView, next, false);
                if (i > 0) {
                    float intValue = (float) ((Integer) imageView.getTag(TAG_SPAN)).intValue();
                    float f = 0.5f * intValue;
                    imageView.setScaleX(f);
                    imageView.setScaleY(f);
                    imageView.setAlpha(0.0f);
                    imageView.animate().withLayer().scaleX(intValue).scaleY(intValue).alpha(1.0f).setDuration((long) i);
                }
            }
        }
    }

    public void place(View view, boolean z) {
        place(view, new Point(irand(0, this.mColumns), irand(0, this.mRows)), z);
    }

    private final Animator.AnimatorListener makeHardwareLayerListener(final View view) {
        return new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                view.setLayerType(2, (Paint) null);
                view.buildLayer();
            }

            public void onAnimationEnd(Animator animator) {
                view.setLayerType(0, (Paint) null);
            }
        };
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0125 A[LOOP:4: B:49:0x0123->B:50:0x0125, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0147  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01dc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void place(android.view.View r17, android.graphics.Point r18, boolean r19) {
        /*
            r16 = this;
            r1 = r16
            r0 = r17
            r2 = r18
            monitor-enter(r16)
            int r3 = r2.x     // Catch:{ all -> 0x01ff }
            int r4 = r2.y     // Catch:{ all -> 0x01ff }
            float r5 = frand()     // Catch:{ all -> 0x01ff }
            r6 = 33554433(0x2000001, float:9.403956E-38)
            java.lang.Object r7 = r0.getTag(r6)     // Catch:{ all -> 0x01ff }
            r8 = 0
            if (r7 == 0) goto L_0x0037
            android.graphics.Point[] r7 = r16.getOccupied(r17)     // Catch:{ all -> 0x01ff }
            int r10 = r7.length     // Catch:{ all -> 0x01ff }
            r11 = 0
        L_0x001f:
            if (r11 >= r10) goto L_0x0037
            r12 = r7[r11]     // Catch:{ all -> 0x01ff }
            java.util.Set<android.graphics.Point> r13 = r1.mFreeList     // Catch:{ all -> 0x01ff }
            r13.add(r12)     // Catch:{ all -> 0x01ff }
            android.view.View[] r13 = r1.mCells     // Catch:{ all -> 0x01ff }
            int r14 = r12.y     // Catch:{ all -> 0x01ff }
            int r15 = r1.mColumns     // Catch:{ all -> 0x01ff }
            int r14 = r14 * r15
            int r12 = r12.x     // Catch:{ all -> 0x01ff }
            int r14 = r14 + r12
            r13[r14] = r8     // Catch:{ all -> 0x01ff }
            int r11 = r11 + 1
            goto L_0x001f
        L_0x0037:
            r7 = 1008981770(0x3c23d70a, float:0.01)
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            r11 = 3
            r12 = 2
            r13 = 1
            if (r7 >= 0) goto L_0x004d
            int r5 = r1.mColumns     // Catch:{ all -> 0x01ff }
            int r5 = r5 - r11
            if (r3 >= r5) goto L_0x0073
            int r5 = r1.mRows     // Catch:{ all -> 0x01ff }
            int r5 = r5 - r11
            if (r4 >= r5) goto L_0x0073
            r5 = 4
            goto L_0x0074
        L_0x004d:
            r7 = 1036831949(0x3dcccccd, float:0.1)
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r7 >= 0) goto L_0x0060
            int r5 = r1.mColumns     // Catch:{ all -> 0x01ff }
            int r5 = r5 - r12
            if (r3 >= r5) goto L_0x0073
            int r5 = r1.mRows     // Catch:{ all -> 0x01ff }
            int r5 = r5 - r12
            if (r4 >= r5) goto L_0x0073
            r5 = r11
            goto L_0x0074
        L_0x0060:
            r7 = 1051260355(0x3ea8f5c3, float:0.33)
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 >= 0) goto L_0x0073
            int r5 = r1.mColumns     // Catch:{ all -> 0x01ff }
            int r5 = r5 - r13
            if (r3 == r5) goto L_0x0073
            int r5 = r1.mRows     // Catch:{ all -> 0x01ff }
            int r5 = r5 - r13
            if (r4 == r5) goto L_0x0073
            r5 = r12
            goto L_0x0074
        L_0x0073:
            r5 = r13
        L_0x0074:
            r0.setTag(r6, r2)     // Catch:{ all -> 0x01ff }
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r5)     // Catch:{ all -> 0x01ff }
            r7 = 33554434(0x2000002, float:9.403957E-38)
            r0.setTag(r7, r2)     // Catch:{ all -> 0x01ff }
            java.util.HashSet<android.view.View> r2 = r1.tmpSet     // Catch:{ all -> 0x01ff }
            r2.clear()     // Catch:{ all -> 0x01ff }
            android.graphics.Point[] r2 = r16.getOccupied(r17)     // Catch:{ all -> 0x01ff }
            int r7 = r2.length     // Catch:{ all -> 0x01ff }
            r14 = 0
        L_0x008c:
            if (r14 >= r7) goto L_0x00a9
            r15 = r2[r14]     // Catch:{ all -> 0x01ff }
            android.view.View[] r11 = r1.mCells     // Catch:{ all -> 0x01ff }
            int r13 = r15.y     // Catch:{ all -> 0x01ff }
            int r12 = r1.mColumns     // Catch:{ all -> 0x01ff }
            int r13 = r13 * r12
            int r12 = r15.x     // Catch:{ all -> 0x01ff }
            int r13 = r13 + r12
            r11 = r11[r13]     // Catch:{ all -> 0x01ff }
            if (r11 == 0) goto L_0x00a3
            java.util.HashSet<android.view.View> r12 = r1.tmpSet     // Catch:{ all -> 0x01ff }
            r12.add(r11)     // Catch:{ all -> 0x01ff }
        L_0x00a3:
            int r14 = r14 + 1
            r11 = 3
            r12 = 2
            r13 = 1
            goto L_0x008c
        L_0x00a9:
            java.util.HashSet<android.view.View> r7 = r1.tmpSet     // Catch:{ all -> 0x01ff }
            java.util.Iterator r7 = r7.iterator()     // Catch:{ all -> 0x01ff }
        L_0x00af:
            boolean r11 = r7.hasNext()     // Catch:{ all -> 0x01ff }
            if (r11 == 0) goto L_0x0121
            java.lang.Object r11 = r7.next()     // Catch:{ all -> 0x01ff }
            android.view.View r11 = (android.view.View) r11     // Catch:{ all -> 0x01ff }
            android.graphics.Point[] r14 = r1.getOccupied(r11)     // Catch:{ all -> 0x01ff }
            int r15 = r14.length     // Catch:{ all -> 0x01ff }
            r9 = 0
        L_0x00c1:
            if (r9 >= r15) goto L_0x00dc
            r10 = r14[r9]     // Catch:{ all -> 0x01ff }
            java.util.Set<android.graphics.Point> r12 = r1.mFreeList     // Catch:{ all -> 0x01ff }
            r12.add(r10)     // Catch:{ all -> 0x01ff }
            android.view.View[] r12 = r1.mCells     // Catch:{ all -> 0x01ff }
            int r13 = r10.y     // Catch:{ all -> 0x01ff }
            int r6 = r1.mColumns     // Catch:{ all -> 0x01ff }
            int r13 = r13 * r6
            int r6 = r10.x     // Catch:{ all -> 0x01ff }
            int r13 = r13 + r6
            r12[r13] = r8     // Catch:{ all -> 0x01ff }
            int r9 = r9 + 1
            r6 = 33554433(0x2000001, float:9.403956E-38)
            goto L_0x00c1
        L_0x00dc:
            if (r11 == r0) goto L_0x011d
            r6 = 33554433(0x2000001, float:9.403956E-38)
            r11.setTag(r6, r8)     // Catch:{ all -> 0x01ff }
            if (r19 == 0) goto L_0x0119
            android.view.ViewPropertyAnimator r9 = r11.animate()     // Catch:{ all -> 0x01ff }
            android.view.ViewPropertyAnimator r9 = r9.withLayer()     // Catch:{ all -> 0x01ff }
            r10 = 1056964608(0x3f000000, float:0.5)
            android.view.ViewPropertyAnimator r9 = r9.scaleX(r10)     // Catch:{ all -> 0x01ff }
            android.view.ViewPropertyAnimator r9 = r9.scaleY(r10)     // Catch:{ all -> 0x01ff }
            r10 = 0
            android.view.ViewPropertyAnimator r9 = r9.alpha(r10)     // Catch:{ all -> 0x01ff }
            r12 = 500(0x1f4, double:2.47E-321)
            android.view.ViewPropertyAnimator r9 = r9.setDuration(r12)     // Catch:{ all -> 0x01ff }
            android.view.animation.AccelerateInterpolator r10 = new android.view.animation.AccelerateInterpolator     // Catch:{ all -> 0x01ff }
            r10.<init>()     // Catch:{ all -> 0x01ff }
            android.view.ViewPropertyAnimator r9 = r9.setInterpolator(r10)     // Catch:{ all -> 0x01ff }
            com.android.systemui.DessertCaseView$4 r10 = new com.android.systemui.DessertCaseView$4     // Catch:{ all -> 0x01ff }
            r10.<init>(r11)     // Catch:{ all -> 0x01ff }
            android.view.ViewPropertyAnimator r9 = r9.setListener(r10)     // Catch:{ all -> 0x01ff }
            r9.start()     // Catch:{ all -> 0x01ff }
            goto L_0x00af
        L_0x0119:
            r1.removeView(r11)     // Catch:{ all -> 0x01ff }
            goto L_0x00af
        L_0x011d:
            r6 = 33554433(0x2000001, float:9.403956E-38)
            goto L_0x00af
        L_0x0121:
            int r6 = r2.length     // Catch:{ all -> 0x01ff }
            r7 = 0
        L_0x0123:
            if (r7 >= r6) goto L_0x013b
            r8 = r2[r7]     // Catch:{ all -> 0x01ff }
            android.view.View[] r9 = r1.mCells     // Catch:{ all -> 0x01ff }
            int r10 = r8.y     // Catch:{ all -> 0x01ff }
            int r11 = r1.mColumns     // Catch:{ all -> 0x01ff }
            int r10 = r10 * r11
            int r11 = r8.x     // Catch:{ all -> 0x01ff }
            int r10 = r10 + r11
            r9[r10] = r0     // Catch:{ all -> 0x01ff }
            java.util.Set<android.graphics.Point> r9 = r1.mFreeList     // Catch:{ all -> 0x01ff }
            r9.remove(r8)     // Catch:{ all -> 0x01ff }
            int r7 = r7 + 1
            goto L_0x0123
        L_0x013b:
            r2 = 4
            r6 = 0
            int r2 = irand(r6, r2)     // Catch:{ all -> 0x01ff }
            float r2 = (float) r2     // Catch:{ all -> 0x01ff }
            r6 = 1119092736(0x42b40000, float:90.0)
            float r2 = r2 * r6
            if (r19 == 0) goto L_0x01dc
            r17.bringToFront()     // Catch:{ all -> 0x01ff }
            android.animation.AnimatorSet r6 = new android.animation.AnimatorSet     // Catch:{ all -> 0x01ff }
            r6.<init>()     // Catch:{ all -> 0x01ff }
            r7 = 2
            android.animation.Animator[] r8 = new android.animation.Animator[r7]     // Catch:{ all -> 0x01ff }
            android.util.Property r7 = android.view.View.SCALE_X     // Catch:{ all -> 0x01ff }
            r9 = 1
            float[] r10 = new float[r9]     // Catch:{ all -> 0x01ff }
            float r11 = (float) r5     // Catch:{ all -> 0x01ff }
            r12 = 0
            r10[r12] = r11     // Catch:{ all -> 0x01ff }
            android.animation.ObjectAnimator r7 = android.animation.ObjectAnimator.ofFloat(r0, r7, r10)     // Catch:{ all -> 0x01ff }
            r8[r12] = r7     // Catch:{ all -> 0x01ff }
            android.util.Property r7 = android.view.View.SCALE_Y     // Catch:{ all -> 0x01ff }
            float[] r10 = new float[r9]     // Catch:{ all -> 0x01ff }
            r10[r12] = r11     // Catch:{ all -> 0x01ff }
            android.animation.ObjectAnimator r7 = android.animation.ObjectAnimator.ofFloat(r0, r7, r10)     // Catch:{ all -> 0x01ff }
            r8[r9] = r7     // Catch:{ all -> 0x01ff }
            r6.playTogether(r8)     // Catch:{ all -> 0x01ff }
            android.view.animation.AnticipateOvershootInterpolator r7 = new android.view.animation.AnticipateOvershootInterpolator     // Catch:{ all -> 0x01ff }
            r7.<init>()     // Catch:{ all -> 0x01ff }
            r6.setInterpolator(r7)     // Catch:{ all -> 0x01ff }
            r7 = 500(0x1f4, double:2.47E-321)
            r6.setDuration(r7)     // Catch:{ all -> 0x01ff }
            android.animation.AnimatorSet r7 = new android.animation.AnimatorSet     // Catch:{ all -> 0x01ff }
            r7.<init>()     // Catch:{ all -> 0x01ff }
            r8 = 3
            android.animation.Animator[] r8 = new android.animation.Animator[r8]     // Catch:{ all -> 0x01ff }
            android.util.Property r9 = android.view.View.ROTATION     // Catch:{ all -> 0x01ff }
            r10 = 1
            float[] r11 = new float[r10]     // Catch:{ all -> 0x01ff }
            r12 = 0
            r11[r12] = r2     // Catch:{ all -> 0x01ff }
            android.animation.ObjectAnimator r2 = android.animation.ObjectAnimator.ofFloat(r0, r9, r11)     // Catch:{ all -> 0x01ff }
            r8[r12] = r2     // Catch:{ all -> 0x01ff }
            android.util.Property r2 = android.view.View.X     // Catch:{ all -> 0x01ff }
            float[] r9 = new float[r10]     // Catch:{ all -> 0x01ff }
            int r11 = r1.mCellSize     // Catch:{ all -> 0x01ff }
            int r3 = r3 * r11
            int r5 = r5 - r10
            int r11 = r11 * r5
            r12 = 2
            int r11 = r11 / r12
            int r3 = r3 + r11
            float r3 = (float) r3     // Catch:{ all -> 0x01ff }
            r11 = 0
            r9[r11] = r3     // Catch:{ all -> 0x01ff }
            android.animation.ObjectAnimator r2 = android.animation.ObjectAnimator.ofFloat(r0, r2, r9)     // Catch:{ all -> 0x01ff }
            r8[r10] = r2     // Catch:{ all -> 0x01ff }
            android.util.Property r2 = android.view.View.Y     // Catch:{ all -> 0x01ff }
            float[] r3 = new float[r10]     // Catch:{ all -> 0x01ff }
            int r9 = r1.mCellSize     // Catch:{ all -> 0x01ff }
            int r4 = r4 * r9
            int r5 = r5 * r9
            r9 = 2
            int r5 = r5 / r9
            int r4 = r4 + r5
            float r4 = (float) r4     // Catch:{ all -> 0x01ff }
            r5 = 0
            r3[r5] = r4     // Catch:{ all -> 0x01ff }
            android.animation.ObjectAnimator r2 = android.animation.ObjectAnimator.ofFloat(r0, r2, r3)     // Catch:{ all -> 0x01ff }
            r8[r9] = r2     // Catch:{ all -> 0x01ff }
            r7.playTogether(r8)     // Catch:{ all -> 0x01ff }
            android.view.animation.DecelerateInterpolator r2 = new android.view.animation.DecelerateInterpolator     // Catch:{ all -> 0x01ff }
            r2.<init>()     // Catch:{ all -> 0x01ff }
            r7.setInterpolator(r2)     // Catch:{ all -> 0x01ff }
            r2 = 500(0x1f4, double:2.47E-321)
            r7.setDuration(r2)     // Catch:{ all -> 0x01ff }
            android.animation.Animator$AnimatorListener r0 = r16.makeHardwareLayerListener(r17)     // Catch:{ all -> 0x01ff }
            r6.addListener(r0)     // Catch:{ all -> 0x01ff }
            r6.start()     // Catch:{ all -> 0x01ff }
            r7.start()     // Catch:{ all -> 0x01ff }
            goto L_0x01fd
        L_0x01dc:
            int r6 = r1.mCellSize     // Catch:{ all -> 0x01ff }
            int r3 = r3 * r6
            int r7 = r5 + -1
            int r6 = r6 * r7
            r8 = 2
            int r6 = r6 / r8
            int r3 = r3 + r6
            float r3 = (float) r3     // Catch:{ all -> 0x01ff }
            r0.setX(r3)     // Catch:{ all -> 0x01ff }
            int r3 = r1.mCellSize     // Catch:{ all -> 0x01ff }
            int r4 = r4 * r3
            int r7 = r7 * r3
            int r7 = r7 / r8
            int r4 = r4 + r7
            float r3 = (float) r4     // Catch:{ all -> 0x01ff }
            r0.setY(r3)     // Catch:{ all -> 0x01ff }
            float r3 = (float) r5     // Catch:{ all -> 0x01ff }
            r0.setScaleX(r3)     // Catch:{ all -> 0x01ff }
            r0.setScaleY(r3)     // Catch:{ all -> 0x01ff }
            r0.setRotation(r2)     // Catch:{ all -> 0x01ff }
        L_0x01fd:
            monitor-exit(r16)
            return
        L_0x01ff:
            r0 = move-exception
            monitor-exit(r16)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.DessertCaseView.place(android.view.View, android.graphics.Point, boolean):void");
    }

    private Point[] getOccupied(View view) {
        int intValue = ((Integer) view.getTag(TAG_SPAN)).intValue();
        Point point = (Point) view.getTag(33554433);
        if (point == null || intValue == 0) {
            return new Point[0];
        }
        Point[] pointArr = new Point[(intValue * intValue)];
        int i = 0;
        for (int i2 = 0; i2 < intValue; i2++) {
            int i3 = 0;
            while (i3 < intValue) {
                pointArr[i] = new Point(point.x + i2, point.y + i3);
                i3++;
                i++;
            }
        }
        return pointArr;
    }

    static float frand() {
        return (float) Math.random();
    }

    static float frand(float f, float f2) {
        return (frand() * (f2 - f)) + f;
    }

    static int irand(int i, int i2) {
        return (int) frand((float) i, (float) i2);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public static class RescalingContainer extends FrameLayout {
        private float mDarkness;
        private DessertCaseView mView;

        /* access modifiers changed from: protected */
        public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
            return super.generateDefaultLayoutParams();
        }

        public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
            return super.generateLayoutParams(attributeSet);
        }

        public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
            return super.getOverlay();
        }

        public RescalingContainer(Context context) {
            super(context);
            setSystemUiVisibility(5638);
        }

        public void setView(DessertCaseView dessertCaseView) {
            addView(dessertCaseView);
            this.mView = dessertCaseView;
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            float f = (float) (i3 - i);
            float f2 = (float) (i4 - i2);
            int i5 = (int) ((f / 0.25f) / 2.0f);
            int i6 = (int) ((f2 / 0.25f) / 2.0f);
            int i7 = (int) (((float) i) + (f * 0.5f));
            int i8 = (int) (((float) i2) + (f2 * 0.5f));
            this.mView.layout(i7 - i5, i8 - i6, i7 + i5, i8 + i6);
        }

        public void setDarkness(float f) {
            this.mDarkness = f;
            getDarkness();
            setBackgroundColor((((int) (f * 255.0f)) << 24) & ViewCompat.MEASURED_STATE_MASK);
        }

        public float getDarkness() {
            return this.mDarkness;
        }
    }
}
