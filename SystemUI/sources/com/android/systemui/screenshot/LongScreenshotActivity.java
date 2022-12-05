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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.screenshot.CropView;
import com.android.systemui.screenshot.ImageExporter;
import com.android.systemui.screenshot.ImageLoader;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class LongScreenshotActivity extends Activity {
    private static final String TAG = LogConfig.logTag(LongScreenshotActivity.class);
    private final Executor mBackgroundExecutor;
    private ListenableFuture<ImageLoader.Result> mCacheLoadFuture;
    private ListenableFuture<File> mCacheSaveFuture;
    private CropView mCropView;
    private View mEdit;
    private ImageView mEnterTransitionView;
    private final ImageExporter mImageExporter;
    private ScrollCaptureController.LongScreenshot mLongScreenshot;
    private final LongScreenshotData mLongScreenshotHolder;
    private MagnifierView mMagnifierView;
    private Bitmap mOutputBitmap;
    private ImageView mPreview;
    private View mSave;
    private File mSavedImagePath;
    private ScrollCaptureResponse mScrollCaptureResponse;
    private View mShare;
    private boolean mTransitionStarted;
    private ImageView mTransitionView;
    private final UiEventLogger mUiEventLogger;
    private final Executor mUiExecutor;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum PendingAction {
        SHARE,
        EDIT,
        SAVE
    }

    public LongScreenshotActivity(UiEventLogger uiEventLogger, ImageExporter imageExporter, Executor executor, Executor executor2, LongScreenshotData longScreenshotData) {
        this.mUiEventLogger = uiEventLogger;
        this.mUiExecutor = executor;
        this.mBackgroundExecutor = executor2;
        this.mImageExporter = imageExporter;
        this.mLongScreenshotHolder = longScreenshotData;
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.long_screenshot);
        this.mPreview = (ImageView) requireViewById(R$id.preview);
        this.mSave = requireViewById(R$id.save);
        this.mEdit = requireViewById(R$id.edit);
        this.mShare = requireViewById(R$id.share);
        this.mCropView = (CropView) requireViewById(R$id.crop_view);
        MagnifierView magnifierView = (MagnifierView) requireViewById(R$id.magnifier);
        this.mMagnifierView = magnifierView;
        this.mCropView.setCropInteractionListener(magnifierView);
        this.mTransitionView = (ImageView) requireViewById(R$id.transition);
        this.mEnterTransitionView = (ImageView) requireViewById(R$id.enter_transition);
        requireViewById(R$id.cancel).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LongScreenshotActivity.this.lambda$onCreate$0(view);
            }
        });
        this.mSave.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LongScreenshotActivity.this.onClicked(view);
            }
        });
        this.mEdit.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LongScreenshotActivity.this.onClicked(view);
            }
        });
        this.mShare.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LongScreenshotActivity.this.onClicked(view);
            }
        });
        this.mPreview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                LongScreenshotActivity.this.lambda$onCreate$1(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        this.mScrollCaptureResponse = getIntent().getParcelableExtra("capture-response");
        if (bundle != null) {
            String string = bundle.getString("saved-image-path");
            if (string == null) {
                Log.e(TAG, "Missing saved state entry with key 'saved-image-path'!");
                finishAndRemoveTask();
                return;
            }
            this.mSavedImagePath = new File(string);
            this.mCacheLoadFuture = new ImageLoader(getContentResolver()).load(this.mSavedImagePath);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finishAndRemoveTask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateImageDimensions();
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_ACTIVITY_STARTED);
        if (this.mPreview.getDrawable() != null) {
            return;
        }
        if (this.mCacheLoadFuture != null) {
            Log.d(TAG, "mCacheLoadFuture != null");
            final ListenableFuture<ImageLoader.Result> listenableFuture = this.mCacheLoadFuture;
            listenableFuture.addListener(new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    LongScreenshotActivity.this.lambda$onStart$2(listenableFuture);
                }
            }, this.mUiExecutor);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$2(ListenableFuture listenableFuture) {
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
        String str = TAG;
        Log.i(str, "Completed: " + longScreenshot);
        this.mLongScreenshot = longScreenshot;
        Drawable drawable = longScreenshot.getDrawable();
        this.mPreview.setImageDrawable(drawable);
        this.mMagnifierView.setDrawable(this.mLongScreenshot.getDrawable(), this.mLongScreenshot.getWidth(), this.mLongScreenshot.getHeight());
        float max = Math.max(0.0f, (-this.mLongScreenshot.getTop()) / this.mLongScreenshot.getHeight());
        float min = Math.min(1.0f, 1.0f - ((this.mLongScreenshot.getBottom() - this.mLongScreenshot.getPageHeight()) / this.mLongScreenshot.getHeight()));
        this.mEnterTransitionView.setImageDrawable(drawable);
        this.mEnterTransitionView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass1(max, min));
        ListenableFuture<File> exportToRawFile = this.mImageExporter.exportToRawFile(this.mBackgroundExecutor, this.mLongScreenshot.toBitmap(), new File(getCacheDir(), "long_screenshot_cache.png"));
        this.mCacheSaveFuture = exportToRawFile;
        exportToRawFile.addListener(new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                LongScreenshotActivity.this.lambda$onLongScreenshotReceived$3();
            }
        }, this.mUiExecutor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.screenshot.LongScreenshotActivity$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ float val$bottomFraction;
        final /* synthetic */ float val$topFraction;

        AnonymousClass1(float f, float f2) {
            this.val$topFraction = f;
            this.val$bottomFraction = f2;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            LongScreenshotActivity.this.mEnterTransitionView.getViewTreeObserver().removeOnPreDrawListener(this);
            LongScreenshotActivity.this.updateImageDimensions();
            ImageView imageView = LongScreenshotActivity.this.mEnterTransitionView;
            final float f = this.val$topFraction;
            final float f2 = this.val$bottomFraction;
            imageView.post(new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LongScreenshotActivity.AnonymousClass1.this.lambda$onPreDraw$1(f, f2);
                }
            });
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPreDraw$1(final float f, final float f2) {
            Rect rect = new Rect();
            LongScreenshotActivity.this.mEnterTransitionView.getBoundsOnScreen(rect);
            LongScreenshotActivity.this.mLongScreenshotHolder.takeTransitionDestinationCallback().setTransitionDestination(rect, new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LongScreenshotActivity.AnonymousClass1.this.lambda$onPreDraw$0(f, f2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPreDraw$0(float f, float f2) {
            LongScreenshotActivity.this.mPreview.animate().alpha(1.0f);
            LongScreenshotActivity.this.mCropView.setBoundaryPosition(CropView.CropBoundary.TOP, f);
            LongScreenshotActivity.this.mCropView.setBoundaryPosition(CropView.CropBoundary.BOTTOM, f2);
            LongScreenshotActivity.this.mCropView.animateEntrance();
            LongScreenshotActivity.this.mCropView.setVisibility(0);
            LongScreenshotActivity.this.setButtonsEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLongScreenshotReceived$3() {
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
        beginRecording.translate(-rect.left, -rect.top);
        beginRecording.clipRect(rect);
        drawable.draw(beginRecording);
        renderNode.endRecording();
        return HardwareRenderer.createHardwareBitmap(renderNode, rect.width(), rect.height());
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        File file = this.mSavedImagePath;
        if (file != null) {
            bundle.putString("saved-image-path", file.getPath());
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
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
            if (longScreenshot == null) {
                return;
            }
            longScreenshot.release();
        }
    }

    void cleanupCache() {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void setButtonsEnabled(boolean z) {
        this.mSave.setEnabled(z);
        this.mEdit.setEnabled(z);
        this.mShare.setEnabled(z);
    }

    private void doEdit(Uri uri) {
        String string = getString(R$string.config_screenshotEditor);
        Intent intent = new Intent("android.intent.action.EDIT");
        if (!TextUtils.isEmpty(string)) {
            intent.setComponent(ComponentName.unflattenFromString(string));
        }
        intent.setDataAndType(uri, "image/png");
        intent.addFlags(3);
        this.mTransitionView.setImageBitmap(this.mOutputBitmap);
        this.mTransitionView.setTransitionName("screenshot_preview_image");
        this.mTransitionStarted = true;
        int[] iArr = new int[2];
        this.mTransitionView.getLocationOnScreen(iArr);
        int[] iArr2 = new int[2];
        this.mTransitionView.getLocationInWindow(iArr2);
        int i = iArr[1] - iArr2[1];
        ImageView imageView = this.mTransitionView;
        imageView.setX(imageView.getX() - (iArr[0] - iArr2[0]));
        ImageView imageView2 = this.mTransitionView;
        imageView2.setY(imageView2.getY() - i);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, this.mTransitionView, "screenshot_preview_image").toBundle());
    }

    private void doShare(Uri uri) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/png");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.addFlags(268468225);
        startActivityAsUser(Intent.createChooser(intent, null).addFlags(1), UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onClicked(View view) {
        int id = view.getId();
        view.setPressed(true);
        setButtonsEnabled(false);
        if (id == R$id.save) {
            startExport(PendingAction.SAVE);
        } else if (id == R$id.edit) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_EDIT);
            startExport(PendingAction.EDIT);
        } else if (id != R$id.share) {
        } else {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_SHARE);
            startExport(PendingAction.SHARE);
        }
    }

    private void startExport(final PendingAction pendingAction) {
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
        final ListenableFuture<ImageExporter.Result> export = this.mImageExporter.export(this.mBackgroundExecutor, UUID.randomUUID(), this.mOutputBitmap, ZonedDateTime.now());
        export.addListener(new Runnable() { // from class: com.android.systemui.screenshot.LongScreenshotActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LongScreenshotActivity.this.lambda$startExport$4(pendingAction, export);
            }
        }, this.mUiExecutor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onExportCompleted */
    public void lambda$startExport$4(PendingAction pendingAction, ListenableFuture<ImageExporter.Result> listenableFuture) {
        setButtonsEnabled(true);
        try {
            ImageExporter.Result result = listenableFuture.get();
            int i = AnonymousClass2.$SwitchMap$com$android$systemui$screenshot$LongScreenshotActivity$PendingAction[pendingAction.ordinal()];
            if (i == 1) {
                doEdit(result.uri);
            } else if (i == 2) {
                doShare(result.uri);
            } else if (i != 3) {
            } else {
                finishAndRemoveTask();
            }
        } catch (InterruptedException | CancellationException | ExecutionException e) {
            Log.e(TAG, "failed to export", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.screenshot.LongScreenshotActivity$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$screenshot$LongScreenshotActivity$PendingAction;

        static {
            int[] iArr = new int[PendingAction.values().length];
            $SwitchMap$com$android$systemui$screenshot$LongScreenshotActivity$PendingAction = iArr;
            try {
                iArr[PendingAction.EDIT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$LongScreenshotActivity$PendingAction[PendingAction.SHARE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$LongScreenshotActivity$PendingAction[PendingAction.SAVE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImageDimensions() {
        float intrinsicHeight;
        Drawable drawable = this.mPreview.getDrawable();
        if (drawable == null) {
            return;
        }
        Rect bounds = drawable.getBounds();
        float width = bounds.width() / bounds.height();
        int width2 = (this.mPreview.getWidth() - this.mPreview.getPaddingLeft()) - this.mPreview.getPaddingRight();
        int height = (this.mPreview.getHeight() - this.mPreview.getPaddingTop()) - this.mPreview.getPaddingBottom();
        float f = width2;
        float f2 = height;
        float f3 = f / f2;
        int paddingLeft = this.mPreview.getPaddingLeft();
        int paddingTop = this.mPreview.getPaddingTop();
        int i = 0;
        if (width > f3) {
            int i2 = (int) ((f3 * f2) / width);
            i = (height - i2) / 2;
            this.mCropView.setExtraPadding(this.mPreview.getPaddingTop() + i, this.mPreview.getPaddingBottom() + i);
            paddingTop += i;
            this.mCropView.setExtraPadding(i, i);
            this.mCropView.setImageWidth(width2);
            intrinsicHeight = f / this.mPreview.getDrawable().getIntrinsicWidth();
            height = i2;
        } else {
            int i3 = (int) ((f * width) / f3);
            paddingLeft += (width2 - i3) / 2;
            this.mCropView.setExtraPadding(this.mPreview.getPaddingTop(), this.mPreview.getPaddingBottom());
            this.mCropView.setImageWidth((int) (width * f2));
            intrinsicHeight = f2 / this.mPreview.getDrawable().getIntrinsicHeight();
            width2 = i3;
        }
        Rect cropBoundaries = this.mCropView.getCropBoundaries(width2, height);
        this.mTransitionView.setTranslationX(paddingLeft + cropBoundaries.left);
        this.mTransitionView.setTranslationY(paddingTop + cropBoundaries.top);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mTransitionView.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).width = cropBoundaries.width();
        ((ViewGroup.MarginLayoutParams) layoutParams).height = cropBoundaries.height();
        this.mTransitionView.setLayoutParams(layoutParams);
        if (this.mLongScreenshot == null) {
            return;
        }
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.mEnterTransitionView.getLayoutParams();
        float max = Math.max(0.0f, (-this.mLongScreenshot.getTop()) / this.mLongScreenshot.getHeight());
        ((ViewGroup.MarginLayoutParams) layoutParams2).width = (int) (drawable.getIntrinsicWidth() * intrinsicHeight);
        ((ViewGroup.MarginLayoutParams) layoutParams2).height = (int) (this.mLongScreenshot.getPageHeight() * intrinsicHeight);
        this.mEnterTransitionView.setLayoutParams(layoutParams2);
        Matrix matrix = new Matrix();
        matrix.setScale(intrinsicHeight, intrinsicHeight);
        matrix.postTranslate(0.0f, (-intrinsicHeight) * drawable.getIntrinsicHeight() * max);
        this.mEnterTransitionView.setImageMatrix(matrix);
        this.mEnterTransitionView.setTranslationY((max * f2) + this.mPreview.getPaddingTop() + i);
    }
}
