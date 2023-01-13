package com.android.systemui;

import android.app.WallpaperColors;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.service.wallpaper.WallpaperService;
import android.util.ArraySet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import com.android.systemui.glwallpaper.EglHelper;
import com.android.systemui.glwallpaper.ImageWallpaperRenderer;
import java.p026io.FileDescriptor;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ImageWallpaper extends WallpaperService {
    private static final boolean DEBUG = false;
    private static final int DELAY_FINISH_RENDERING = 1000;
    /* access modifiers changed from: private */
    public static final RectF LOCAL_COLOR_BOUNDS = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    /* access modifiers changed from: private */
    public static final String TAG = "ImageWallpaper";
    /* access modifiers changed from: private */
    public final ArraySet<RectF> mColorAreas = new ArraySet<>();
    /* access modifiers changed from: private */
    public final ArrayList<RectF> mLocalColorsToAdd = new ArrayList<>();
    /* access modifiers changed from: private */
    public Bitmap mMiniBitmap;
    /* access modifiers changed from: private */
    public volatile int mPages = 1;
    /* access modifiers changed from: private */
    public HandlerThread mWorker;

    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mWorker = handlerThread;
        handlerThread.start();
    }

    public WallpaperService.Engine onCreateEngine() {
        return new GLEngine();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mWorker.quitSafely();
        this.mWorker = null;
        this.mMiniBitmap = null;
    }

    class GLEngine extends WallpaperService.Engine implements DisplayManager.DisplayListener {
        static final int MIN_SURFACE_HEIGHT = 128;
        static final int MIN_SURFACE_WIDTH = 128;
        private int mDisplayHeight = 1;
        private boolean mDisplaySizeValid = false;
        private int mDisplayWidth = 1;
        private EglHelper mEglHelper;
        private final Runnable mFinishRenderingTask = new ImageWallpaper$GLEngine$$ExternalSyntheticLambda7(this);
        private int mImgHeight = 1;
        private int mImgWidth = 1;
        private boolean mNeedRedraw;
        private ImageWallpaperRenderer mRenderer;

        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public boolean shouldWaitForEngineShown() {
            return true;
        }

        public boolean shouldZoomOutWallpaper() {
            return true;
        }

        public boolean supportsLocalColorExtraction() {
            return true;
        }

        GLEngine() {
            super(ImageWallpaper.this);
        }

        GLEngine(Handler handler) {
            super(ImageWallpaper.this, new ImageWallpaper$GLEngine$$ExternalSyntheticLambda10(), handler);
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            Trace.beginSection("ImageWallpaper.Engine#onCreate");
            this.mEglHelper = getEglHelperInstance();
            this.mRenderer = getRendererInstance();
            setFixedSizeAllowed(true);
            updateSurfaceSize();
            setShowForAllUsers(true);
            this.mRenderer.setOnBitmapChanged(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda6(this));
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).registerDisplayListener(this, ImageWallpaper.this.mWorker.getThreadHandler());
            Trace.endSection();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onCreate$0$com-android-systemui-ImageWallpaper$GLEngine  reason: not valid java name */
        public /* synthetic */ void m2525lambda$onCreate$0$comandroidsystemuiImageWallpaper$GLEngine(Bitmap bitmap) {
            ImageWallpaper.this.mLocalColorsToAdd.addAll(ImageWallpaper.this.mColorAreas);
            if (ImageWallpaper.this.mLocalColorsToAdd.size() > 0) {
                updateMiniBitmapAndNotify(bitmap);
            }
        }

        public void onDisplayChanged(int i) {
            if (i == getDisplayContext().getDisplayId()) {
                this.mDisplaySizeValid = false;
            }
        }

        /* access modifiers changed from: package-private */
        public EglHelper getEglHelperInstance() {
            return new EglHelper();
        }

        /* access modifiers changed from: package-private */
        public ImageWallpaperRenderer getRendererInstance() {
            return new ImageWallpaperRenderer(getDisplayContext());
        }

        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            int i3 = 1;
            if (f3 > 0.0f && f3 <= 1.0f) {
                i3 = 1 + Math.round(1.0f / f3);
            }
            if (i3 != ImageWallpaper.this.mPages) {
                int unused = ImageWallpaper.this.mPages = i3;
                if (ImageWallpaper.this.mMiniBitmap != null && !ImageWallpaper.this.mMiniBitmap.isRecycled()) {
                    ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda4(this));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onOffsetsChanged$1$com-android-systemui-ImageWallpaper$GLEngine */
        public /* synthetic */ void mo29700x2aa9b98b() {
            computeAndNotifyLocalColors(new ArrayList(ImageWallpaper.this.mColorAreas), ImageWallpaper.this.mMiniBitmap);
        }

        /* access modifiers changed from: private */
        public void updateMiniBitmapAndNotify(Bitmap bitmap) {
            if (bitmap != null) {
                int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
                float f = min > 128 ? 128.0f / ((float) min) : 1.0f;
                this.mImgHeight = bitmap.getHeight();
                this.mImgWidth = bitmap.getWidth();
                Bitmap unused = ImageWallpaper.this.mMiniBitmap = Bitmap.createScaledBitmap(bitmap, (int) Math.max(((float) bitmap.getWidth()) * f, 1.0f), (int) Math.max(f * ((float) bitmap.getHeight()), 1.0f), false);
                computeAndNotifyLocalColors(ImageWallpaper.this.mLocalColorsToAdd, ImageWallpaper.this.mMiniBitmap);
                ImageWallpaper.this.mLocalColorsToAdd.clear();
            }
        }

        private void updateSurfaceSize() {
            Trace.beginSection("ImageWallpaper#updateSurfaceSize");
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Size reportSurfaceSize = this.mRenderer.reportSurfaceSize();
            surfaceHolder.setFixedSize(Math.max(128, reportSurfaceSize.getWidth()), Math.max(128, reportSurfaceSize.getHeight()));
            Trace.endSection();
        }

        public void onDestroy() {
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).unregisterDisplayListener(this);
            Bitmap unused = ImageWallpaper.this.mMiniBitmap = null;
            ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDestroy$2$com-android-systemui-ImageWallpaper$GLEngine  reason: not valid java name */
        public /* synthetic */ void m2526lambda$onDestroy$2$comandroidsystemuiImageWallpaper$GLEngine() {
            this.mRenderer.finish();
            this.mRenderer = null;
            this.mEglHelper.finish();
            this.mEglHelper = null;
        }

        public void addLocalColorsAreas(List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, list));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$addLocalColorsAreas$3$com-android-systemui-ImageWallpaper$GLEngine */
        public /* synthetic */ void mo29697x2b06aa4a(List list) {
            if (ImageWallpaper.this.mColorAreas.size() + ImageWallpaper.this.mLocalColorsToAdd.size() == 0) {
                setOffsetNotificationsEnabled(true);
            }
            Bitmap access$200 = ImageWallpaper.this.mMiniBitmap;
            if (access$200 == null) {
                ImageWallpaper.this.mLocalColorsToAdd.addAll(list);
                ImageWallpaperRenderer imageWallpaperRenderer = this.mRenderer;
                if (imageWallpaperRenderer != null) {
                    imageWallpaperRenderer.use(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda5(this));
                    return;
                }
                return;
            }
            computeAndNotifyLocalColors(list, access$200);
        }

        private void computeAndNotifyLocalColors(List<RectF> list, Bitmap bitmap) {
            List<WallpaperColors> localWallpaperColors = getLocalWallpaperColors(list, bitmap);
            ImageWallpaper.this.mColorAreas.addAll(list);
            try {
                notifyLocalColorsChanged(list, localWallpaperColors);
            } catch (RuntimeException e) {
                Log.e(ImageWallpaper.TAG, e.getMessage(), e);
            }
        }

        public void removeLocalColorsAreas(List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda8(this, list));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$removeLocalColorsAreas$4$com-android-systemui-ImageWallpaper$GLEngine */
        public /* synthetic */ void mo29703x42de68c(List list) {
            ImageWallpaper.this.mColorAreas.removeAll(list);
            ImageWallpaper.this.mLocalColorsToAdd.removeAll(list);
            if (ImageWallpaper.this.mColorAreas.size() + ImageWallpaper.this.mLocalColorsToAdd.size() == 0) {
                setOffsetNotificationsEnabled(false);
            }
        }

        private RectF pageToImgRect(RectF rectF) {
            if (!this.mDisplaySizeValid) {
                Rect bounds = ((WindowManager) getDisplayContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
                this.mDisplayWidth = bounds.width();
                this.mDisplayHeight = bounds.height();
                this.mDisplaySizeValid = true;
            }
            float access$100 = 1.0f / ((float) ImageWallpaper.this.mPages);
            float f = (rectF.left % access$100) / access$100;
            float f2 = (rectF.right % access$100) / access$100;
            int floor = (int) Math.floor((double) (rectF.centerX() / access$100));
            RectF rectF2 = new RectF();
            if (this.mImgWidth != 0 && this.mImgHeight != 0 && this.mDisplayWidth > 0 && this.mDisplayHeight > 0) {
                rectF2.bottom = rectF.bottom;
                rectF2.top = rectF.top;
                float min = ((float) this.mDisplayWidth) * Math.min(((float) this.mImgHeight) / ((float) this.mDisplayHeight), 1.0f);
                int i = this.mImgWidth;
                float min2 = Math.min(1.0f, i > 0 ? min / ((float) i) : 1.0f);
                float access$1002 = ((float) floor) * ((1.0f - min2) / ((float) (ImageWallpaper.this.mPages - 1)));
                rectF2.left = MathUtils.constrain((f * min2) + access$1002, 0.0f, 1.0f);
                rectF2.right = MathUtils.constrain((f2 * min2) + access$1002, 0.0f, 1.0f);
                if (rectF2.left > rectF2.right) {
                    rectF2.left = 0.0f;
                    rectF2.right = 1.0f;
                }
            }
            return rectF2;
        }

        private List<WallpaperColors> getLocalWallpaperColors(List<RectF> list, Bitmap bitmap) {
            ArrayList arrayList = new ArrayList(list.size());
            for (int i = 0; i < list.size(); i++) {
                RectF pageToImgRect = pageToImgRect(list.get(i));
                if (pageToImgRect == null || !ImageWallpaper.LOCAL_COLOR_BOUNDS.contains(pageToImgRect)) {
                    arrayList.add(null);
                } else {
                    Rect rect = new Rect((int) Math.floor((double) (pageToImgRect.left * ((float) bitmap.getWidth()))), (int) Math.floor((double) (pageToImgRect.top * ((float) bitmap.getHeight()))), (int) Math.ceil((double) (pageToImgRect.right * ((float) bitmap.getWidth()))), (int) Math.ceil((double) (pageToImgRect.bottom * ((float) bitmap.getHeight()))));
                    if (rect.isEmpty()) {
                        arrayList.add(null);
                    } else {
                        arrayList.add(WallpaperColors.fromBitmap(Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())));
                    }
                }
            }
            return arrayList;
        }

        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda9(this, surfaceHolder));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSurfaceCreated$5$com-android-systemui-ImageWallpaper$GLEngine */
        public /* synthetic */ void mo29702x1ac4c000(SurfaceHolder surfaceHolder) {
            Trace.beginSection("ImageWallpaper#onSurfaceCreated");
            this.mEglHelper.init(surfaceHolder, needSupportWideColorGamut());
            this.mRenderer.onSurfaceCreated();
            Trace.endSection();
        }

        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda3(this, i2, i3));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSurfaceChanged$6$com-android-systemui-ImageWallpaper$GLEngine */
        public /* synthetic */ void mo29701xc1020853(int i, int i2) {
            this.mRenderer.onSurfaceChanged(i, i2);
        }

        public void onSurfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda2(this));
            }
        }

        /* access modifiers changed from: private */
        public void drawFrame() {
            Trace.beginSection("ImageWallpaper#drawFrame");
            preRender();
            requestRender();
            postRender();
            Trace.endSection();
        }

        public void preRender() {
            Trace.beginSection("ImageWallpaper#preRender");
            preRenderInternal();
            Trace.endSection();
        }

        private void preRenderInternal() {
            boolean z;
            Rect surfaceFrame = getSurfaceHolder().getSurfaceFrame();
            cancelFinishRenderingTask();
            if (!this.mEglHelper.hasEglContext()) {
                this.mEglHelper.destroyEglSurface();
                if (!this.mEglHelper.createEglContext()) {
                    Log.w(ImageWallpaper.TAG, "recreate egl context failed!");
                } else {
                    z = true;
                    if (this.mEglHelper.hasEglContext() && !this.mEglHelper.hasEglSurface() && !this.mEglHelper.createEglSurface(getSurfaceHolder(), needSupportWideColorGamut())) {
                        Log.w(ImageWallpaper.TAG, "recreate egl surface failed!");
                    }
                    if (!this.mEglHelper.hasEglContext() && !this.mEglHelper.hasEglSurface() && z) {
                        this.mRenderer.onSurfaceCreated();
                        this.mRenderer.onSurfaceChanged(surfaceFrame.width(), surfaceFrame.height());
                        return;
                    }
                }
            }
            z = false;
            Log.w(ImageWallpaper.TAG, "recreate egl surface failed!");
            if (!this.mEglHelper.hasEglContext() && !this.mEglHelper.hasEglSurface()) {
            }
        }

        public void requestRender() {
            Trace.beginSection("ImageWallpaper#requestRender");
            requestRenderInternal();
            Trace.endSection();
        }

        private void requestRenderInternal() {
            Rect surfaceFrame = getSurfaceHolder().getSurfaceFrame();
            if (this.mEglHelper.hasEglContext() && this.mEglHelper.hasEglSurface() && surfaceFrame.width() > 0 && surfaceFrame.height() > 0) {
                this.mRenderer.onDrawFrame();
                if (!this.mEglHelper.swapBuffer()) {
                    Log.e(ImageWallpaper.TAG, "drawFrame failed!");
                    return;
                }
                return;
            }
            Log.e(ImageWallpaper.TAG, "requestRender: not ready, has context=" + this.mEglHelper.hasEglContext() + ", has surface=" + this.mEglHelper.hasEglSurface() + ", frame=" + surfaceFrame);
        }

        public void postRender() {
            scheduleFinishRendering();
            reportEngineShown(false);
        }

        private void cancelFinishRenderingTask() {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().removeCallbacks(this.mFinishRenderingTask);
            }
        }

        private void scheduleFinishRendering() {
            if (ImageWallpaper.this.mWorker != null) {
                cancelFinishRenderingTask();
                ImageWallpaper.this.mWorker.getThreadHandler().postDelayed(this.mFinishRenderingTask, 1000);
            }
        }

        /* access modifiers changed from: private */
        public void finishRendering() {
            Trace.beginSection("ImageWallpaper#finishRendering");
            EglHelper eglHelper = this.mEglHelper;
            if (eglHelper != null) {
                eglHelper.destroyEglSurface();
                this.mEglHelper.destroyEglContext();
            }
            Trace.endSection();
        }

        private boolean needSupportWideColorGamut() {
            return this.mRenderer.isWcgContent();
        }

        /* access modifiers changed from: protected */
        public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            super.dump(str, fileDescriptor, printWriter, strArr);
            printWriter.print(str);
            printWriter.print("Engine=");
            printWriter.println((Object) this);
            printWriter.print(str);
            printWriter.print("valid surface=");
            Object obj = "null";
            printWriter.println((getSurfaceHolder() == null || getSurfaceHolder().getSurface() == null) ? obj : Boolean.valueOf(getSurfaceHolder().getSurface().isValid()));
            printWriter.print(str);
            printWriter.print("surface frame=");
            if (getSurfaceHolder() != null) {
                obj = getSurfaceHolder().getSurfaceFrame();
            }
            printWriter.println(obj);
            this.mEglHelper.dump(str, fileDescriptor, printWriter, strArr);
            this.mRenderer.dump(str, fileDescriptor, printWriter, strArr);
        }
    }
}
