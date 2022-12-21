package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class ScrollCaptureController {
    private static final float IDEAL_PORTION_ABOVE = 0.4f;
    public static final int MAX_HEIGHT = 12000;
    private static final float MAX_PAGES_DEFAULT = 3.0f;
    private static final String SETTING_KEY_MAX_PAGES = "screenshot.scroll_max_pages";
    private static final String TAG = LogConfig.logTag(ScrollCaptureController.class);
    private final Executor mBgExecutor;
    private volatile boolean mCancelled;
    private CallbackToFutureAdapter.Completer<LongScreenshot> mCaptureCompleter;
    private final ScrollCaptureClient mClient;
    private final Context mContext;
    private ListenableFuture<Void> mEndFuture;
    private final UiEventLogger mEventLogger;
    private boolean mFinishOnBoundary;
    private final ImageTileSet mImageTileSet;
    private boolean mScrollingUp = true;
    private ScrollCaptureClient.Session mSession;
    private ListenableFuture<ScrollCaptureClient.Session> mSessionFuture;
    private ListenableFuture<ScrollCaptureClient.CaptureResult> mTileFuture;
    private String mWindowOwner;

    /* access modifiers changed from: package-private */
    public float getTargetTopSizeRatio() {
        return IDEAL_PORTION_ABOVE;
    }

    static class LongScreenshot {
        private final ImageTileSet mImageTileSet;
        private final ScrollCaptureClient.Session mSession;

        LongScreenshot(ScrollCaptureClient.Session session, ImageTileSet imageTileSet) {
            this.mSession = session;
            this.mImageTileSet = imageTileSet;
        }

        public Bitmap toBitmap() {
            return this.mImageTileSet.toBitmap();
        }

        public Bitmap toBitmap(Rect rect) {
            return this.mImageTileSet.toBitmap(rect);
        }

        public void release() {
            this.mImageTileSet.clear();
            this.mSession.release();
        }

        public int getLeft() {
            return this.mImageTileSet.getLeft();
        }

        public int getTop() {
            return this.mImageTileSet.getTop();
        }

        public int getBottom() {
            return this.mImageTileSet.getBottom();
        }

        public int getWidth() {
            return this.mImageTileSet.getWidth();
        }

        public int getHeight() {
            return this.mImageTileSet.getHeight();
        }

        public int getPageHeight() {
            return this.mSession.getPageHeight();
        }

        public String toString() {
            return "LongScreenshot{w=" + this.mImageTileSet.getWidth() + ", h=" + this.mImageTileSet.getHeight() + "}";
        }

        public Drawable getDrawable() {
            return this.mImageTileSet.getDrawable();
        }
    }

    @Inject
    ScrollCaptureController(Context context, @Background Executor executor, ScrollCaptureClient scrollCaptureClient, ImageTileSet imageTileSet, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mBgExecutor = executor;
        this.mClient = scrollCaptureClient;
        this.mImageTileSet = imageTileSet;
        this.mEventLogger = uiEventLogger;
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<LongScreenshot> run(ScrollCaptureResponse scrollCaptureResponse) {
        this.mCancelled = false;
        return CallbackToFutureAdapter.getFuture(new ScrollCaptureController$$ExternalSyntheticLambda4(this, scrollCaptureResponse));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$run$1$com-android-systemui-screenshot-ScrollCaptureController */
    public /* synthetic */ Object mo37593xfbe3cca5(ScrollCaptureResponse scrollCaptureResponse, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mCaptureCompleter = completer;
        this.mWindowOwner = scrollCaptureResponse.getPackageName();
        this.mCaptureCompleter.addCancellationListener(new ScrollCaptureController$$ExternalSyntheticLambda1(this), this.mBgExecutor);
        this.mBgExecutor.execute(new ScrollCaptureController$$ExternalSyntheticLambda2(this, scrollCaptureResponse));
        return "<batch scroll capture>";
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$run$0$com-android-systemui-screenshot-ScrollCaptureController */
    public /* synthetic */ void mo37592xd64fc3a4(ScrollCaptureResponse scrollCaptureResponse) {
        ListenableFuture<ScrollCaptureClient.Session> start = this.mClient.start(scrollCaptureResponse, Settings.Secure.getFloat(this.mContext.getContentResolver(), SETTING_KEY_MAX_PAGES, 3.0f));
        this.mSessionFuture = start;
        start.addListener(new ScrollCaptureController$$ExternalSyntheticLambda3(this), this.mContext.getMainExecutor());
    }

    /* access modifiers changed from: private */
    public void onCancelled() {
        this.mCancelled = true;
        ListenableFuture<ScrollCaptureClient.Session> listenableFuture = this.mSessionFuture;
        if (listenableFuture != null) {
            listenableFuture.cancel(true);
        }
        ListenableFuture<ScrollCaptureClient.CaptureResult> listenableFuture2 = this.mTileFuture;
        if (listenableFuture2 != null) {
            listenableFuture2.cancel(true);
        }
        ScrollCaptureClient.Session session = this.mSession;
        if (session != null) {
            session.end();
        }
        this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, this.mWindowOwner);
    }

    /* access modifiers changed from: private */
    public void onStartComplete() {
        try {
            this.mSession = this.mSessionFuture.get();
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_STARTED, 0, this.mWindowOwner);
            requestNextTile(0);
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "session start failed!");
            this.mCaptureCompleter.setException(e);
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, this.mWindowOwner);
        }
    }

    private void requestNextTile(int i) {
        if (this.mCancelled) {
            Log.d(TAG, "requestNextTile: CANCELLED");
            return;
        }
        ListenableFuture<ScrollCaptureClient.CaptureResult> requestTile = this.mSession.requestTile(i);
        this.mTileFuture = requestTile;
        requestTile.addListener(new ScrollCaptureController$$ExternalSyntheticLambda0(this), this.mBgExecutor);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$requestNextTile$2$com-android-systemui-screenshot-ScrollCaptureController */
    public /* synthetic */ void mo37591x1a1bde4b() {
        try {
            onCaptureResult(this.mTileFuture.get());
        } catch (CancellationException unused) {
            Log.e(TAG, "requestTile cancelled");
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "requestTile failed!", e);
            this.mCaptureCompleter.setException(e);
        }
    }

    private void onCaptureResult(ScrollCaptureClient.CaptureResult captureResult) {
        int i;
        int top;
        int tileHeight;
        boolean z = captureResult.captured.height() == 0;
        if (z) {
            if (this.mFinishOnBoundary) {
                finishCapture();
                return;
            }
            this.mImageTileSet.clear();
            this.mFinishOnBoundary = true;
            this.mScrollingUp = !this.mScrollingUp;
        } else if (this.mImageTileSet.size() + 1 >= this.mSession.getMaxTiles()) {
            finishCapture();
            return;
        } else if (this.mScrollingUp && !this.mFinishOnBoundary && ((float) (this.mImageTileSet.getHeight() + captureResult.captured.height())) >= ((float) this.mSession.getTargetHeight()) * IDEAL_PORTION_ABOVE) {
            this.mImageTileSet.clear();
            this.mScrollingUp = false;
        }
        if (!z) {
            this.mImageTileSet.m2998lambda$addTile$0$comandroidsystemuiscreenshotImageTileSet(new ImageTile(captureResult.image, captureResult.captured));
        }
        Rect gaps = this.mImageTileSet.getGaps();
        if (!gaps.isEmpty()) {
            requestNextTile(gaps.top);
        } else if (this.mImageTileSet.getHeight() >= this.mSession.getTargetHeight()) {
            finishCapture();
        } else {
            if (z) {
                if (this.mScrollingUp) {
                    top = captureResult.requested.top;
                    tileHeight = this.mSession.getTileHeight();
                } else {
                    i = captureResult.requested.bottom;
                    requestNextTile(i);
                }
            } else if (this.mScrollingUp) {
                top = this.mImageTileSet.getTop();
                tileHeight = this.mSession.getTileHeight();
            } else {
                i = this.mImageTileSet.getBottom();
                requestNextTile(i);
            }
            i = top - tileHeight;
            requestNextTile(i);
        }
    }

    private void finishCapture() {
        if (this.mImageTileSet.getHeight() > 0) {
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_COMPLETED, 0, this.mWindowOwner);
        } else {
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, this.mWindowOwner);
        }
        ListenableFuture<Void> end = this.mSession.end();
        this.mEndFuture = end;
        end.addListener(new ScrollCaptureController$$ExternalSyntheticLambda5(this), this.mContext.getMainExecutor());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$finishCapture$3$com-android-systemui-screenshot-ScrollCaptureController */
    public /* synthetic */ void mo37590x6ef2598f() {
        this.mCaptureCompleter.set(new LongScreenshot(this.mSession, this.mImageTileSet));
    }
}
