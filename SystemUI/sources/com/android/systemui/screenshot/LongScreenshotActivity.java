package com.android.systemui.screenshot;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.HardwareRenderer;
import android.graphics.Matrix;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ScrollCaptureResponse;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.screenshot.CropView;
import com.android.systemui.screenshot.ImageExporter;
import com.android.systemui.screenshot.ImageLoader;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.google.common.util.concurrent.ListenableFuture;
import java.p026io.File;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class LongScreenshotActivity extends Activity {
    public static final String EXTRA_CAPTURE_RESPONSE = "capture-response";
    private static final String KEY_SAVED_IMAGE_PATH = "saved-image-path";
    private static final String TAG = LogConfig.logTag(LongScreenshotActivity.class);
    private final Executor mBackgroundExecutor;
    private ListenableFuture<ImageLoader.Result> mCacheLoadFuture;
    private ListenableFuture<File> mCacheSaveFuture;
    private View mCancel;
    /* access modifiers changed from: private */
    public CropView mCropView;
    private View mEdit;
    /* access modifiers changed from: private */
    public ImageView mEnterTransitionView;
    private final ImageExporter mImageExporter;
    private ScrollCaptureController.LongScreenshot mLongScreenshot;
    /* access modifiers changed from: private */
    public final LongScreenshotData mLongScreenshotHolder;
    private MagnifierView mMagnifierView;
    private Bitmap mOutputBitmap;
    /* access modifiers changed from: private */
    public ImageView mPreview;
    private View mSave;
    private File mSavedImagePath;
    private ScrollCaptureResponse mScrollCaptureResponse;
    private View mShare;
    private boolean mTransitionStarted;
    private ImageView mTransitionView;
    private final UiEventLogger mUiEventLogger;
    private final Executor mUiExecutor;

    private enum PendingAction {
        SHARE,
        EDIT,
        SAVE
    }

    @Inject
    public LongScreenshotActivity(UiEventLogger uiEventLogger, ImageExporter imageExporter, @Main Executor executor, @Background Executor executor2, LongScreenshotData longScreenshotData) {
        this.mUiEventLogger = uiEventLogger;
        this.mUiExecutor = executor;
        this.mBackgroundExecutor = executor2;
        this.mImageExporter = imageExporter;
        this.mLongScreenshotHolder = longScreenshotData;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C1893R.layout.long_screenshot);
        this.mPreview = (ImageView) requireViewById(C1893R.C1897id.preview);
        this.mSave = requireViewById(C1893R.C1897id.save);
        this.mEdit = requireViewById(C1893R.C1897id.edit);
        this.mShare = requireViewById(C1893R.C1897id.share);
        this.mCancel = requireViewById(C1893R.C1897id.cancel);
        this.mCropView = (CropView) requireViewById(C1893R.C1897id.crop_view);
        MagnifierView magnifierView = (MagnifierView) requireViewById(C1893R.C1897id.magnifier);
        this.mMagnifierView = magnifierView;
        this.mCropView.setCropInteractionListener(magnifierView);
        this.mTransitionView = (ImageView) requireViewById(C1893R.C1897id.transition);
        this.mEnterTransitionView = (ImageView) requireViewById(C1893R.C1897id.enter_transition);
        this.mSave.setOnClickListener(new LongScreenshotActivity$$ExternalSyntheticLambda3(this));
        this.mCancel.setOnClickListener(new LongScreenshotActivity$$ExternalSyntheticLambda3(this));
        this.mEdit.setOnClickListener(new LongScreenshotActivity$$ExternalSyntheticLambda3(this));
        this.mShare.setOnClickListener(new LongScreenshotActivity$$ExternalSyntheticLambda3(this));
        this.mPreview.addOnLayoutChangeListener(new LongScreenshotActivity$$ExternalSyntheticLambda4(this));
        this.mScrollCaptureResponse = getIntent().getParcelableExtra(EXTRA_CAPTURE_RESPONSE);
        if (bundle != null) {
            String string = bundle.getString(KEY_SAVED_IMAGE_PATH);
            if (string == null) {
                Log.e(TAG, "Missing saved state entry with key 'saved-image-path'!");
                finishAndRemoveTask();
                return;
            }
            this.mSavedImagePath = new File(string);
            this.mCacheLoadFuture = new ImageLoader(getContentResolver()).load(this.mSavedImagePath);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-android-systemui-screenshot-LongScreenshotActivity */
    public /* synthetic */ void mo37372x85381858(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateImageDimensions();
    }

    public void onStart() {
        super.onStart();
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_STARTED);
        if (this.mPreview.getDrawable() == null) {
            if (this.mCacheLoadFuture != null) {
                Log.d(TAG, "mCacheLoadFuture != null");
                ListenableFuture<ImageLoader.Result> listenableFuture = this.mCacheLoadFuture;
                listenableFuture.addListener(new LongScreenshotActivity$$ExternalSyntheticLambda0(this, listenableFuture), this.mUiExecutor);
                this.mCacheLoadFuture = null;
                return;
            }
            ScrollCaptureController.LongScreenshot takeLongScreenshot = this.mLongScreenshotHolder.takeLongScreenshot();
            if (takeLongScreenshot != null) {
                onLongScreenshotReceived(takeLongScreenshot);
                return;
            }
            Log.e(TAG, "No long screenshot available!");
            finishAndRemoveTask();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStart$1$com-android-systemui-screenshot-LongScreenshotActivity */
    public /* synthetic */ void mo37374x67b51509(ListenableFuture listenableFuture) {
        Log.d(TAG, "cached bitmap load complete");
        try {
            onCachedImageLoaded((ImageLoader.Result) listenableFuture.get());
        } catch (InterruptedException | CancellationException | ExecutionException e) {
            Log.e(TAG, "Failed to load cached image", e);
            File file = this.mSavedImagePath;
            if (file != null) {
                file.delete();
                this.mSavedImagePath = null;
            }
            finishAndRemoveTask();
        }
    }

    private void onLongScreenshotReceived(ScrollCaptureController.LongScreenshot longScreenshot) {
        Log.i(TAG, "Completed: " + longScreenshot);
        this.mLongScreenshot = longScreenshot;
        Drawable drawable = longScreenshot.getDrawable();
        this.mPreview.setImageDrawable(drawable);
        this.mMagnifierView.setDrawable(this.mLongScreenshot.getDrawable(), this.mLongScreenshot.getWidth(), this.mLongScreenshot.getHeight());
        final float max = Math.max(0.0f, ((float) (-this.mLongScreenshot.getTop())) / ((float) this.mLongScreenshot.getHeight()));
        final float min = Math.min(1.0f, 1.0f - (((float) (this.mLongScreenshot.getBottom() - this.mLongScreenshot.getPageHeight())) / ((float) this.mLongScreenshot.getHeight())));
        this.mEnterTransitionView.setImageDrawable(drawable);
        this.mEnterTransitionView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                LongScreenshotActivity.this.mEnterTransitionView.getViewTreeObserver().removeOnPreDrawListener(this);
                LongScreenshotActivity.this.updateImageDimensions();
                LongScreenshotActivity.this.mEnterTransitionView.post(new LongScreenshotActivity$1$$ExternalSyntheticLambda0(this, max, min));
                return true;
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onPreDraw$1$com-android-systemui-screenshot-LongScreenshotActivity$1 */
            public /* synthetic */ void mo37381x100e0b11(float f, float f2) {
                Rect rect = new Rect();
                LongScreenshotActivity.this.mEnterTransitionView.getBoundsOnScreen(rect);
                LongScreenshotActivity.this.mLongScreenshotHolder.takeTransitionDestinationCallback().setTransitionDestination(rect, new LongScreenshotActivity$1$$ExternalSyntheticLambda1(this, f, f2));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onPreDraw$0$com-android-systemui-screenshot-LongScreenshotActivity$1 */
            public /* synthetic */ void mo37380x8320f3f2(float f, float f2) {
                LongScreenshotActivity.this.mPreview.animate().alpha(1.0f);
                LongScreenshotActivity.this.mCropView.setBoundaryPosition(CropView.CropBoundary.TOP, f);
                LongScreenshotActivity.this.mCropView.setBoundaryPosition(CropView.CropBoundary.BOTTOM, f2);
                LongScreenshotActivity.this.mCropView.animateEntrance();
                LongScreenshotActivity.this.mCropView.setVisibility(0);
                LongScreenshotActivity.this.setButtonsEnabled(true);
            }
        });
        ListenableFuture<File> exportToRawFile = this.mImageExporter.exportToRawFile(this.mBackgroundExecutor, this.mLongScreenshot.toBitmap(), new File(getCacheDir(), "long_screenshot_cache.png"));
        this.mCacheSaveFuture = exportToRawFile;
        exportToRawFile.addListener(new LongScreenshotActivity$$ExternalSyntheticLambda1(this), this.mUiExecutor);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLongScreenshotReceived$2$com-android-systemui-screenshot-LongScreenshotActivity */
    public /* synthetic */ void mo37373xc1ca0fef() {
        try {
            this.mSavedImagePath = this.mCacheSaveFuture.get();
        } catch (InterruptedException | CancellationException | ExecutionException e) {
            Log.e(TAG, "Error saving temp image file", e);
            finishAndRemoveTask();
        }
    }

    private void onCachedImageLoaded(ImageLoader.Result result) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_CACHED_IMAGE_LOADED);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), result.bitmap);
        this.mPreview.setImageDrawable(bitmapDrawable);
        this.mPreview.setAlpha(1.0f);
        this.mMagnifierView.setDrawable(bitmapDrawable, result.bitmap.getWidth(), result.bitmap.getHeight());
        this.mCropView.setVisibility(0);
        this.mSavedImagePath = result.fileName;
        setButtonsEnabled(true);
    }

    private static Bitmap renderBitmap(Drawable drawable, Rect rect) {
        RenderNode renderNode = new RenderNode("Bitmap Export");
        renderNode.setPosition(0, 0, rect.width(), rect.height());
        RecordingCanvas beginRecording = renderNode.beginRecording();
        beginRecording.translate((float) (-rect.left), (float) (-rect.top));
        beginRecording.clipRect(rect);
        drawable.draw(beginRecording);
        renderNode.endRecording();
        return HardwareRenderer.createHardwareBitmap(renderNode, rect.width(), rect.height());
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        File file = this.mSavedImagePath;
        if (file != null) {
            bundle.putString(KEY_SAVED_IMAGE_PATH, file.getPath());
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (this.mTransitionStarted) {
            finish();
        }
        if (isFinishing()) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_FINISHED);
            ScrollCaptureResponse scrollCaptureResponse = this.mScrollCaptureResponse;
            if (scrollCaptureResponse != null) {
                scrollCaptureResponse.close();
            }
            cleanupCache();
            ScrollCaptureController.LongScreenshot longScreenshot = this.mLongScreenshot;
            if (longScreenshot != null) {
                longScreenshot.release();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void cleanupCache() {
        ListenableFuture<File> listenableFuture = this.mCacheSaveFuture;
        if (listenableFuture != null) {
            listenableFuture.cancel(true);
        }
        File file = this.mSavedImagePath;
        if (file != null) {
            file.delete();
            this.mSavedImagePath = null;
        }
    }

    /* access modifiers changed from: private */
    public void setButtonsEnabled(boolean z) {
        this.mSave.setEnabled(z);
        this.mEdit.setEnabled(z);
        this.mShare.setEnabled(z);
    }

    private void doEdit(Uri uri) {
        String string = getString(C1893R.string.nt_config_screenshotEditor);
        Intent intent = new Intent("android.intent.action.EDIT");
        if (!TextUtils.isEmpty(string)) {
            intent.setComponent(ComponentName.unflattenFromString(string));
        }
        intent.setDataAndType(uri, "image/png");
        intent.addFlags(3);
        this.mTransitionView.setImageBitmap(this.mOutputBitmap);
        this.mTransitionView.setVisibility(0);
        this.mTransitionView.setTransitionName("screenshot_preview_image");
        this.mTransitionStarted = true;
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, this.mTransitionView, "screenshot_preview_image").toBundle());
    }

    private void doShare(Uri uri) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/png");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.addFlags(268468225);
        startActivityAsUser(Intent.createChooser(intent, (CharSequence) null).addFlags(1), UserHandle.CURRENT);
    }

    /* access modifiers changed from: private */
    public void onClicked(View view) {
        int id = view.getId();
        view.setPressed(true);
        setButtonsEnabled(false);
        if (id == C1893R.C1897id.save) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_SAVED);
            startExport(PendingAction.SAVE);
        } else if (id == C1893R.C1897id.edit) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_EDIT);
            startExport(PendingAction.EDIT);
        } else if (id == C1893R.C1897id.share) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_SHARE);
            startExport(PendingAction.SHARE);
        } else if (id == C1893R.C1897id.cancel) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_EXIT);
            finishAndRemoveTask();
        }
    }

    private void startExport(PendingAction pendingAction) {
        Drawable drawable = this.mPreview.getDrawable();
        if (drawable == null) {
            Log.e(TAG, "No drawable, skipping export!");
            return;
        }
        Rect cropBoundaries = this.mCropView.getCropBoundaries(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (cropBoundaries.isEmpty()) {
            Log.w(TAG, "Crop bounds empty, skipping export.");
            return;
        }
        updateImageDimensions();
        this.mOutputBitmap = renderBitmap(drawable, cropBoundaries);
        ListenableFuture<ImageExporter.Result> export = this.mImageExporter.export(this.mBackgroundExecutor, UUID.randomUUID(), this.mOutputBitmap, ZonedDateTime.now());
        export.addListener(new LongScreenshotActivity$$ExternalSyntheticLambda2(this, pendingAction, export), this.mUiExecutor);
    }

    /* access modifiers changed from: private */
    /* renamed from: onExportCompleted */
    public void mo37375x107eeeb4(PendingAction pendingAction, ListenableFuture<ImageExporter.Result> listenableFuture) {
        setButtonsEnabled(true);
        try {
            ImageExporter.Result result = listenableFuture.get();
            int i = C24402.f338x6ba3e28f[pendingAction.ordinal()];
            if (i == 1) {
                doEdit(result.uri);
            } else if (i == 2) {
                doShare(result.uri);
            } else if (i == 3) {
                finishAndRemoveTask();
            }
        } catch (InterruptedException | CancellationException | ExecutionException e) {
            Log.e(TAG, "failed to export", e);
        }
    }

    /* renamed from: com.android.systemui.screenshot.LongScreenshotActivity$2 */
    static /* synthetic */ class C24402 {

        /* renamed from: $SwitchMap$com$android$systemui$screenshot$LongScreenshotActivity$PendingAction */
        static final /* synthetic */ int[] f338x6ba3e28f;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.android.systemui.screenshot.LongScreenshotActivity$PendingAction[] r0 = com.android.systemui.screenshot.LongScreenshotActivity.PendingAction.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f338x6ba3e28f = r0
                com.android.systemui.screenshot.LongScreenshotActivity$PendingAction r1 = com.android.systemui.screenshot.LongScreenshotActivity.PendingAction.EDIT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f338x6ba3e28f     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.screenshot.LongScreenshotActivity$PendingAction r1 = com.android.systemui.screenshot.LongScreenshotActivity.PendingAction.SHARE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f338x6ba3e28f     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.screenshot.LongScreenshotActivity$PendingAction r1 = com.android.systemui.screenshot.LongScreenshotActivity.PendingAction.SAVE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.screenshot.LongScreenshotActivity.C24402.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public void updateImageDimensions() {
        float f;
        int i;
        Drawable drawable = this.mPreview.getDrawable();
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            float width = ((float) bounds.width()) / ((float) bounds.height());
            int width2 = (this.mPreview.getWidth() - this.mPreview.getPaddingLeft()) - this.mPreview.getPaddingRight();
            int height = (this.mPreview.getHeight() - this.mPreview.getPaddingTop()) - this.mPreview.getPaddingBottom();
            float f2 = (float) width2;
            float f3 = (float) height;
            float f4 = f2 / f3;
            int paddingLeft = this.mPreview.getPaddingLeft();
            int paddingTop = this.mPreview.getPaddingTop();
            if (width > f4) {
                int i2 = (int) ((f4 * f3) / width);
                int i3 = (height - i2) / 2;
                this.mCropView.setExtraPadding(this.mPreview.getPaddingTop() + i3, this.mPreview.getPaddingBottom() + i3);
                paddingTop += i3;
                this.mCropView.setExtraPadding(i3, i3);
                this.mCropView.setImageWidth(width2);
                f = f2 / ((float) this.mPreview.getDrawable().getIntrinsicWidth());
                int i4 = i3;
                height = i2;
                i = i4;
            } else {
                int i5 = (int) ((f2 * width) / f4);
                paddingLeft += (width2 - i5) / 2;
                this.mCropView.setExtraPadding(this.mPreview.getPaddingTop(), this.mPreview.getPaddingBottom());
                this.mCropView.setImageWidth((int) (width * f3));
                i = 0;
                int i6 = i5;
                f = f3 / ((float) this.mPreview.getDrawable().getIntrinsicHeight());
                width2 = i6;
            }
            Rect cropBoundaries = this.mCropView.getCropBoundaries(width2, height);
            this.mTransitionView.setTranslationX((float) (paddingLeft + cropBoundaries.left));
            this.mTransitionView.setTranslationY((float) (paddingTop + cropBoundaries.top));
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mTransitionView.getLayoutParams();
            layoutParams.width = cropBoundaries.width();
            layoutParams.height = cropBoundaries.height();
            this.mTransitionView.setLayoutParams(layoutParams);
            if (this.mLongScreenshot != null) {
                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.mEnterTransitionView.getLayoutParams();
                float max = Math.max(0.0f, ((float) (-this.mLongScreenshot.getTop())) / ((float) this.mLongScreenshot.getHeight()));
                layoutParams2.width = (int) (((float) drawable.getIntrinsicWidth()) * f);
                layoutParams2.height = (int) (((float) this.mLongScreenshot.getPageHeight()) * f);
                this.mEnterTransitionView.setLayoutParams(layoutParams2);
                Matrix matrix = new Matrix();
                matrix.setScale(f, f);
                matrix.postTranslate(0.0f, (-f) * ((float) drawable.getIntrinsicHeight()) * max);
                this.mEnterTransitionView.setImageMatrix(matrix);
                this.mEnterTransitionView.setTranslationY((max * f3) + ((float) this.mPreview.getPaddingTop()) + ((float) i));
            }
        }
    }
}
