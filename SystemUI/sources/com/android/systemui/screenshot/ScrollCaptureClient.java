package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Rect;
import android.media.Image;
import android.media.ImageReader;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.util.Log;
import android.view.IScrollCaptureCallbacks;
import android.view.IScrollCaptureConnection;
import android.view.IScrollCaptureResponseListener;
import android.view.IWindowManager;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import sun.util.locale.LanguageTag;

public class ScrollCaptureClient {
    static final int MATCH_ANY_TASK = -1;
    private static final int MAX_TILES = 30;
    /* access modifiers changed from: private */
    public static final String TAG = LogConfig.logTag(ScrollCaptureClient.class);
    private static final int TILES_PER_PAGE = 2;
    private static final int TILE_SIZE_PX_MAX = 4194304;
    private final Executor mBgExecutor;
    private IBinder mHostWindowToken;
    private final IWindowManager mWindowManagerService;

    interface Session {
        ListenableFuture<Void> end();

        int getMaxTiles();

        int getPageHeight();

        int getPageWidth();

        int getTargetHeight();

        int getTileHeight();

        Rect getWindowBounds();

        void release();

        ListenableFuture<CaptureResult> requestTile(int i);
    }

    static class CaptureResult {
        public final Rect captured;
        public final Image image;
        public final Rect requested;

        CaptureResult(Image image2, Rect rect, Rect rect2) {
            this.image = image2;
            this.requested = rect;
            this.captured = rect2;
        }

        public String toString() {
            return "CaptureResult{requested=" + this.requested + " (" + this.requested.width() + LanguageTag.PRIVATEUSE + this.requested.height() + "), captured=" + this.captured + " (" + this.captured.width() + LanguageTag.PRIVATEUSE + this.captured.height() + "), image=" + this.image + '}';
        }
    }

    @Inject
    public ScrollCaptureClient(IWindowManager iWindowManager, @Background Executor executor, Context context) {
        Objects.requireNonNull(context.getDisplay(), "context must be associated with a Display!");
        this.mBgExecutor = executor;
        this.mWindowManagerService = iWindowManager;
    }

    public void setHostWindowToken(IBinder iBinder) {
        this.mHostWindowToken = iBinder;
    }

    public ListenableFuture<ScrollCaptureResponse> request(int i) {
        return request(i, -1);
    }

    public ListenableFuture<ScrollCaptureResponse> request(int i, int i2) {
        return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$$ExternalSyntheticLambda0(this, i, i2));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$request$0$com-android-systemui-screenshot-ScrollCaptureClient */
    public /* synthetic */ Object mo37564xf116bb57(int i, int i2, final CallbackToFutureAdapter.Completer completer) throws Exception {
        try {
            this.mWindowManagerService.requestScrollCapture(i, this.mHostWindowToken, i2, new IScrollCaptureResponseListener.Stub() {
                public void onScrollCaptureResponse(ScrollCaptureResponse scrollCaptureResponse) {
                    completer.set(scrollCaptureResponse);
                }
            });
        } catch (RemoteException e) {
            completer.setException(e);
        }
        return "ScrollCaptureClient#request(displayId=" + i + ", taskId=" + i2 + NavigationBarInflaterView.KEY_CODE_END;
    }

    public ListenableFuture<Session> start(ScrollCaptureResponse scrollCaptureResponse, float f) {
        Log.d(TAG, "start maxPages" + f);
        return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$$ExternalSyntheticLambda1(this, scrollCaptureResponse.getConnection(), scrollCaptureResponse, f));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$start$1$com-android-systemui-screenshot-ScrollCaptureClient */
    public /* synthetic */ Object mo37565xc725f2b(IScrollCaptureConnection iScrollCaptureConnection, ScrollCaptureResponse scrollCaptureResponse, float f, CallbackToFutureAdapter.Completer completer) throws Exception {
        if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
            completer.setException(new DeadObjectException("No active connection!"));
            return "";
        }
        new SessionWrapper(iScrollCaptureConnection, scrollCaptureResponse.getWindowBounds(), scrollCaptureResponse.getBoundsInWindow(), f, this.mBgExecutor).start(completer);
        return "IScrollCaptureCallbacks#onCaptureStarted";
    }

    private static class SessionWrapper extends IScrollCaptureCallbacks.Stub implements Session, IBinder.DeathRecipient, ImageReader.OnImageAvailableListener {
        private final Executor mBgExecutor;
        private final Rect mBoundsInWindow;
        private ICancellationSignal mCancellationSignal;
        private Rect mCapturedArea;
        private Image mCapturedImage;
        private IScrollCaptureConnection mConnection;
        private CallbackToFutureAdapter.Completer<Void> mEndCompleter;
        private final Object mLock;
        private ImageReader mReader;
        private Rect mRequestRect;
        private CallbackToFutureAdapter.Completer<Session> mStartCompleter;
        private boolean mStarted;
        private final int mTargetHeight;
        private final int mTileHeight;
        private CallbackToFutureAdapter.Completer<CaptureResult> mTileRequestCompleter;
        private final int mTileWidth;
        private final Rect mWindowBounds;

        public int getMaxTiles() {
            return 30;
        }

        private SessionWrapper(IScrollCaptureConnection iScrollCaptureConnection, Rect rect, Rect rect2, float f, Executor executor) throws RemoteException {
            this.mLock = new Object();
            IScrollCaptureConnection iScrollCaptureConnection2 = (IScrollCaptureConnection) Objects.requireNonNull(iScrollCaptureConnection);
            this.mConnection = iScrollCaptureConnection2;
            iScrollCaptureConnection2.asBinder().linkToDeath(this, 0);
            this.mWindowBounds = (Rect) Objects.requireNonNull(rect);
            Rect rect3 = (Rect) Objects.requireNonNull(rect2);
            this.mBoundsInWindow = rect3;
            int min = Math.min(4194304, (rect3.width() * rect3.height()) / 2);
            this.mTileWidth = rect3.width();
            this.mTileHeight = min / rect3.width();
            this.mTargetHeight = (int) (((float) rect3.height()) * f);
            this.mBgExecutor = executor;
        }

        public void binderDied() {
            Log.d(ScrollCaptureClient.TAG, "binderDied! The target process just crashed :-(");
            this.mConnection = null;
            CallbackToFutureAdapter.Completer<Session> completer = this.mStartCompleter;
            if (completer != null) {
                completer.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<CaptureResult> completer2 = this.mTileRequestCompleter;
            if (completer2 != null) {
                completer2.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<Void> completer3 = this.mEndCompleter;
            if (completer3 != null) {
                completer3.setException(new DeadObjectException("The remote process died"));
            }
        }

        /* access modifiers changed from: private */
        public void start(CallbackToFutureAdapter.Completer<Session> completer) {
            ImageReader newInstance = ImageReader.newInstance(this.mTileWidth, this.mTileHeight, 1, 30, 256);
            this.mReader = newInstance;
            this.mStartCompleter = completer;
            newInstance.setOnImageAvailableListenerWithExecutor(this, this.mBgExecutor);
            try {
                this.mCancellationSignal = this.mConnection.startCapture(this.mReader.getSurface(), this);
                Log.d(ScrollCaptureClient.TAG, "startCapture");
                completer.addCancellationListener(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda3(this), new SaveImageInBackgroundTask$$ExternalSyntheticLambda1());
                this.mStarted = true;
            } catch (RemoteException e) {
                Log.d(ScrollCaptureClient.TAG, "start reader.close()");
                this.mReader.close();
                completer.setException(e);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$start$0$com-android-systemui-screenshot-ScrollCaptureClient$SessionWrapper */
        public /* synthetic */ void mo37585xd9936b77() {
            try {
                this.mCancellationSignal.cancel();
            } catch (RemoteException unused) {
            }
        }

        public void onCaptureStarted() {
            Log.d(ScrollCaptureClient.TAG, "onCaptureStarted");
            this.mStartCompleter.set(this);
        }

        public ListenableFuture<CaptureResult> requestTile(int i) {
            this.mRequestRect = new Rect(0, i, this.mTileWidth, this.mTileHeight + i);
            return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$requestTile$2$com-android-systemui-screenshot-ScrollCaptureClient$SessionWrapper */
        public /* synthetic */ Object mo37584x975269da(CallbackToFutureAdapter.Completer completer) throws Exception {
            IScrollCaptureConnection iScrollCaptureConnection = this.mConnection;
            if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
                completer.setException(new DeadObjectException("Connection is closed!"));
                return "";
            }
            try {
                this.mTileRequestCompleter = completer;
                this.mCancellationSignal = this.mConnection.requestImage(this.mRequestRect);
                completer.addCancellationListener(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda2(this), new SaveImageInBackgroundTask$$ExternalSyntheticLambda1());
                return "IScrollCaptureCallbacks#onImageRequestCompleted";
            } catch (RemoteException e) {
                completer.setException(e);
                return "IScrollCaptureCallbacks#onImageRequestCompleted";
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$requestTile$1$com-android-systemui-screenshot-ScrollCaptureClient$SessionWrapper */
        public /* synthetic */ void mo37583xfae46d7b() {
            try {
                this.mCancellationSignal.cancel();
            } catch (RemoteException unused) {
            }
        }

        public void onImageRequestCompleted(int i, Rect rect) {
            Log.d(ScrollCaptureClient.TAG, "onImageRequestCompleted");
            synchronized (this.mLock) {
                this.mCapturedArea = rect;
                if (this.mCapturedImage != null || rect == null || rect.isEmpty()) {
                    completeCaptureRequest();
                }
            }
        }

        public void onImageAvailable(ImageReader imageReader) {
            synchronized (this.mLock) {
                this.mCapturedImage = this.mReader.acquireLatestImage();
                if (this.mCapturedArea != null) {
                    completeCaptureRequest();
                }
            }
        }

        private void completeCaptureRequest() {
            Log.d(ScrollCaptureClient.TAG, "completeCaptureRequest");
            CaptureResult captureResult = new CaptureResult(this.mCapturedImage, this.mRequestRect, this.mCapturedArea);
            this.mCapturedImage = null;
            this.mRequestRect = null;
            this.mCapturedArea = null;
            this.mTileRequestCompleter.set(captureResult);
        }

        public ListenableFuture<Void> end() {
            Log.d(ScrollCaptureClient.TAG, "end()");
            return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$end$3$com-android-systemui-screenshot-ScrollCaptureClient$SessionWrapper */
        public /* synthetic */ Object mo37582xd5f9e2fb(CallbackToFutureAdapter.Completer completer) throws Exception {
            if (!this.mStarted) {
                try {
                    this.mConnection.asBinder().unlinkToDeath(this, 0);
                    this.mConnection.close();
                } catch (RemoteException unused) {
                }
                this.mConnection = null;
                completer.set(null);
                return "";
            }
            this.mEndCompleter = completer;
            try {
                this.mConnection.endCapture();
                return "IScrollCaptureCallbacks#onCaptureEnded";
            } catch (RemoteException e) {
                completer.setException(e);
                return "IScrollCaptureCallbacks#onCaptureEnded";
            }
        }

        public void release() {
            Log.d(ScrollCaptureClient.TAG, "mReader.close()");
            this.mReader.close();
        }

        public void onCaptureEnded() {
            Log.d(ScrollCaptureClient.TAG, "onCaptureEnded ");
            try {
                this.mConnection.close();
            } catch (RemoteException unused) {
            }
            this.mConnection = null;
            this.mEndCompleter.set(null);
        }

        public int getPageHeight() {
            return this.mBoundsInWindow.height();
        }

        public int getPageWidth() {
            return this.mBoundsInWindow.width();
        }

        public int getTileHeight() {
            return this.mTileHeight;
        }

        public Rect getWindowBounds() {
            return new Rect(this.mWindowBounds);
        }

        public Rect getBoundsInWindow() {
            return new Rect(this.mBoundsInWindow);
        }

        public int getTargetHeight() {
            return this.mTargetHeight;
        }
    }
}
