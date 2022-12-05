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
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.glwallpaper.EglHelper;
import com.android.systemui.glwallpaper.ImageWallpaperRenderer;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ImageWallpaper extends WallpaperService {
    private Bitmap mMiniBitmap;
    private HandlerThread mWorker;
    private static final String TAG = ImageWallpaper.class.getSimpleName();
    private static final RectF LOCAL_COLOR_BOUNDS = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    private final ArrayList<RectF> mLocalColorsToAdd = new ArrayList<>();
    private final ArraySet<RectF> mColorAreas = new ArraySet<>();
    private volatile int mPages = 1;

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mWorker = handlerThread;
        handlerThread.start();
    }

    @Override // android.service.wallpaper.WallpaperService
    public WallpaperService.Engine onCreateEngine() {
        return new GLEngine();
    }

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mWorker.quitSafely();
        this.mWorker = null;
        this.mMiniBitmap = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class GLEngine extends WallpaperService.Engine implements DisplayManager.DisplayListener {
        @VisibleForTesting
        static final int MIN_SURFACE_HEIGHT = 128;
        @VisibleForTesting
        static final int MIN_SURFACE_WIDTH = 128;
        private EglHelper mEglHelper;
        private ImageWallpaperRenderer mRenderer;
        private final Runnable mFinishRenderingTask = new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ImageWallpaper.GLEngine.this.finishRendering();
            }
        };
        private boolean mDisplaySizeValid = false;
        private int mDisplayWidth = 1;
        private int mDisplayHeight = 1;
        private int mImgWidth = 1;
        private int mImgHeight = 1;

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
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

        @VisibleForTesting
        GLEngine(Handler handler) {
            super(ImageWallpaper.this, ImageWallpaper$GLEngine$$ExternalSyntheticLambda9.INSTANCE, handler);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onCreate(SurfaceHolder surfaceHolder) {
            Trace.beginSection("ImageWallpaper.Engine#onCreate");
            this.mEglHelper = getEglHelperInstance();
            this.mRenderer = getRendererInstance();
            setFixedSizeAllowed(true);
            updateSurfaceSize();
            this.mRenderer.setOnBitmapChanged(new Consumer() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ImageWallpaper.GLEngine.this.updateMiniBitmap((Bitmap) obj);
                }
            });
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).registerDisplayListener(this, ImageWallpaper.this.mWorker.getThreadHandler());
            Trace.endSection();
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            if (i == getDisplayContext().getDisplayId()) {
                this.mDisplaySizeValid = false;
            }
        }

        EglHelper getEglHelperInstance() {
            return new EglHelper();
        }

        ImageWallpaperRenderer getRendererInstance() {
            return new ImageWallpaperRenderer(getDisplayContext());
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            int i3 = 1;
            if (f3 > 0.0f && f3 <= 1.0f) {
                i3 = 1 + Math.round(1.0f / f3);
            }
            if (i3 == ImageWallpaper.this.mPages) {
                return;
            }
            ImageWallpaper.this.mPages = i3;
            if (ImageWallpaper.this.mMiniBitmap == null || ImageWallpaper.this.mMiniBitmap.isRecycled()) {
                return;
            }
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageWallpaper.GLEngine.this.lambda$onOffsetsChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onOffsetsChanged$0() {
            computeAndNotifyLocalColors(new ArrayList(ImageWallpaper.this.mColorAreas), ImageWallpaper.this.mMiniBitmap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateMiniBitmap(Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
            float f = min > 128 ? 128.0f / min : 1.0f;
            this.mImgHeight = bitmap.getHeight();
            this.mImgWidth = bitmap.getWidth();
            ImageWallpaper.this.mMiniBitmap = Bitmap.createScaledBitmap(bitmap, (int) Math.max(bitmap.getWidth() * f, 1.0f), (int) Math.max(f * bitmap.getHeight(), 1.0f), false);
            computeAndNotifyLocalColors(ImageWallpaper.this.mLocalColorsToAdd, ImageWallpaper.this.mMiniBitmap);
            ImageWallpaper.this.mLocalColorsToAdd.clear();
        }

        private void updateSurfaceSize() {
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Size reportSurfaceSize = this.mRenderer.reportSurfaceSize();
            surfaceHolder.setFixedSize(Math.max(128, reportSurfaceSize.getWidth()), Math.max(128, reportSurfaceSize.getHeight()));
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onDestroy() {
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).unregisterDisplayListener(this);
            ImageWallpaper.this.mMiniBitmap = null;
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageWallpaper.GLEngine.this.lambda$onDestroy$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDestroy$1() {
            Trace.beginSection("ImageWallpaper.Engine#onDestroy");
            this.mRenderer.finish();
            this.mRenderer = null;
            this.mEglHelper.finish();
            this.mEglHelper = null;
            Trace.endSection();
        }

        public void addLocalColorsAreas(final List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ImageWallpaper.GLEngine.this.lambda$addLocalColorsAreas$2(list);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addLocalColorsAreas$2(List list) {
            if (ImageWallpaper.this.mColorAreas.size() + ImageWallpaper.this.mLocalColorsToAdd.size() == 0) {
                setOffsetNotificationsEnabled(true);
            }
            Bitmap bitmap = ImageWallpaper.this.mMiniBitmap;
            if (bitmap == null) {
                ImageWallpaper.this.mLocalColorsToAdd.addAll(list);
            } else {
                computeAndNotifyLocalColors(list, bitmap);
            }
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

        public void removeLocalColorsAreas(final List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ImageWallpaper.GLEngine.this.lambda$removeLocalColorsAreas$3(list);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeLocalColorsAreas$3(List list) {
            ImageWallpaper.this.mColorAreas.removeAll(list);
            ImageWallpaper.this.mLocalColorsToAdd.removeAll(list);
            if (ImageWallpaper.this.mColorAreas.size() + ImageWallpaper.this.mLocalColorsToAdd.size() == 0) {
                setOffsetNotificationsEnabled(false);
            }
        }

        private RectF pageToImgRect(RectF rectF) {
            int i;
            int i2;
            if (!this.mDisplaySizeValid) {
                Rect bounds = ((WindowManager) getDisplayContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
                this.mDisplayWidth = bounds.width();
                this.mDisplayHeight = bounds.height();
                this.mDisplaySizeValid = true;
            }
            float f = 1.0f / ImageWallpaper.this.mPages;
            float f2 = (rectF.left % f) / f;
            float f3 = (rectF.right % f) / f;
            int floor = (int) Math.floor(rectF.centerX() / f);
            RectF rectF2 = new RectF();
            if (this.mImgWidth != 0 && (i = this.mImgHeight) != 0 && this.mDisplayWidth > 0 && (i2 = this.mDisplayHeight) > 0) {
                rectF2.bottom = rectF.bottom;
                rectF2.top = rectF.top;
                float min = this.mDisplayWidth * Math.min(i / i2, 1.0f);
                int i3 = this.mImgWidth;
                float min2 = Math.min(1.0f, i3 > 0 ? min / i3 : 1.0f);
                float f4 = floor * ((1.0f - min2) / (ImageWallpaper.this.mPages - 1));
                rectF2.left = MathUtils.constrain((f2 * min2) + f4, 0.0f, 1.0f);
                float constrain = MathUtils.constrain((f3 * min2) + f4, 0.0f, 1.0f);
                rectF2.right = constrain;
                if (rectF2.left > constrain) {
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
                    Rect rect = new Rect((int) Math.floor(pageToImgRect.left * bitmap.getWidth()), (int) Math.floor(pageToImgRect.top * bitmap.getHeight()), (int) Math.ceil(pageToImgRect.right * bitmap.getWidth()), (int) Math.ceil(pageToImgRect.bottom * bitmap.getHeight()));
                    if (rect.isEmpty()) {
                        arrayList.add(null);
                    } else {
                        arrayList.add(WallpaperColors.fromBitmap(Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())));
                    }
                }
            }
            return arrayList;
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceCreated(final SurfaceHolder surfaceHolder) {
            if (ImageWallpaper.this.mWorker == null) {
                return;
            }
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ImageWallpaper.GLEngine.this.lambda$onSurfaceCreated$4(surfaceHolder);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceCreated$4(SurfaceHolder surfaceHolder) {
            Trace.beginSection("ImageWallpaper#onSurfaceCreated");
            this.mEglHelper.init(surfaceHolder, needSupportWideColorGamut());
            this.mRenderer.onSurfaceCreated();
            Trace.endSection();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, final int i2, final int i3) {
            if (ImageWallpaper.this.mWorker == null) {
                return;
            }
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ImageWallpaper.GLEngine.this.lambda$onSurfaceChanged$5(i2, i3);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceChanged$5(int i, int i2) {
            this.mRenderer.onSurfaceChanged(i, i2);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
            if (ImageWallpaper.this.mWorker == null) {
                return;
            }
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageWallpaper.GLEngine.this.drawFrame();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
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
                    if (this.mEglHelper.hasEglContext() || !this.mEglHelper.hasEglSurface() || !z) {
                        return;
                    }
                    this.mRenderer.onSurfaceCreated();
                    this.mRenderer.onSurfaceChanged(surfaceFrame.width(), surfaceFrame.height());
                    return;
                }
            }
            z = false;
            if (this.mEglHelper.hasEglContext()) {
                Log.w(ImageWallpaper.TAG, "recreate egl surface failed!");
            }
            if (this.mEglHelper.hasEglContext()) {
            }
        }

        public void requestRender() {
            Trace.beginSection("ImageWallpaper#requestRender");
            requestRenderInternal();
            Trace.endSection();
        }

        private void requestRenderInternal() {
            Rect surfaceFrame = getSurfaceHolder().getSurfaceFrame();
            if (!(this.mEglHelper.hasEglContext() && this.mEglHelper.hasEglSurface() && surfaceFrame.width() > 0 && surfaceFrame.height() > 0)) {
                String str = ImageWallpaper.TAG;
                Log.e(str, "requestRender: not ready, has context=" + this.mEglHelper.hasEglContext() + ", has surface=" + this.mEglHelper.hasEglSurface() + ", frame=" + surfaceFrame);
                return;
            }
            this.mRenderer.onDrawFrame();
            if (this.mEglHelper.swapBuffer()) {
                return;
            }
            Log.e(ImageWallpaper.TAG, "drawFrame failed!");
        }

        public void postRender() {
            Trace.beginSection("ImageWallpaper#postRender");
            scheduleFinishRendering();
            reportEngineShown(false);
            Trace.endSection();
        }

        private void cancelFinishRenderingTask() {
            if (ImageWallpaper.this.mWorker == null) {
                return;
            }
            ImageWallpaper.this.mWorker.getThreadHandler().removeCallbacks(this.mFinishRenderingTask);
        }

        private void scheduleFinishRendering() {
            if (ImageWallpaper.this.mWorker == null) {
                return;
            }
            cancelFinishRenderingTask();
            ImageWallpaper.this.mWorker.getThreadHandler().postDelayed(this.mFinishRenderingTask, 1000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        @Override // android.service.wallpaper.WallpaperService.Engine
        protected void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            super.dump(str, fileDescriptor, printWriter, strArr);
            printWriter.print(str);
            printWriter.print("Engine=");
            printWriter.println(this);
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
