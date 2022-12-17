package com.android.settingslib.users;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.StrictMode;
import android.util.EventLog;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import libcore.io.Streams;

class AvatarPhotoController {
    private final AvatarUi mAvatarUi;
    private final ContextInjector mContextInjector;
    private final Uri mCropPictureUri;
    private final File mImagesDir;
    private final int mPhotoSize;
    private final Uri mPreCropPictureUri;
    private final Uri mTakePictureUri;

    interface AvatarUi {
        int getPhotoSize();

        boolean isFinishing();

        void returnUriResult(Uri uri);

        void startActivityForResult(Intent intent, int i);

        boolean startSystemActivityForResult(Intent intent, int i);
    }

    interface ContextInjector {
        Uri createTempImageUri(File file, String str, boolean z);

        File getCacheDir();

        ContentResolver getContentResolver();
    }

    AvatarPhotoController(AvatarUi avatarUi, ContextInjector contextInjector, boolean z) {
        this.mAvatarUi = avatarUi;
        this.mContextInjector = contextInjector;
        File file = new File(contextInjector.getCacheDir(), "multi_user");
        this.mImagesDir = file;
        file.mkdir();
        this.mPreCropPictureUri = contextInjector.createTempImageUri(file, "PreCropEditUserPhoto.jpg", !z);
        this.mCropPictureUri = contextInjector.createTempImageUri(file, "CropEditUserPhoto.jpg", !z);
        this.mTakePictureUri = contextInjector.createTempImageUri(file, "TakeEditUserPhoto.jpg", !z);
        this.mPhotoSize = avatarUi.getPhotoSize();
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            return false;
        }
        Uri data = (intent == null || intent.getData() == null) ? this.mTakePictureUri : intent.getData();
        if (!"content".equals(data.getScheme())) {
            Log.e("AvatarPhotoController", "Invalid pictureUri scheme: " + data.getScheme());
            EventLog.writeEvent(1397638484, new Object[]{"172939189", -1, data.getPath()});
            return false;
        }
        switch (i) {
            case 1001:
                copyAndCropPhoto(data, true);
                return true;
            case 1002:
                if (this.mTakePictureUri.equals(data)) {
                    cropPhoto(data);
                } else {
                    copyAndCropPhoto(data, false);
                }
                return true;
            case 1003:
                this.mAvatarUi.returnUriResult(data);
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE_SECURE");
        appendOutputExtra(intent, this.mTakePictureUri);
        this.mAvatarUi.startActivityForResult(intent, 1002);
    }

    /* access modifiers changed from: package-private */
    public void choosePhoto() {
        Intent intent = new Intent("android.provider.action.PICK_IMAGES", (Uri) null);
        intent.setType("image/*");
        this.mAvatarUi.startActivityForResult(intent, 1001);
    }

    private void copyAndCropPhoto(Uri uri, boolean z) {
        try {
            ThreadUtils.postOnBackgroundThread((Runnable) new AvatarPhotoController$$ExternalSyntheticLambda0(this, uri, z)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("AvatarPhotoController", "Error performing copy-and-crop", e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$copyAndCropPhoto$1(Uri uri, boolean z) {
        OutputStream openOutputStream;
        ContentResolver contentResolver = this.mContextInjector.getContentResolver();
        try {
            InputStream openInputStream = contentResolver.openInputStream(uri);
            try {
                openOutputStream = contentResolver.openOutputStream(this.mPreCropPictureUri);
                Streams.copy(openInputStream, openOutputStream);
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
                if (openInputStream != null) {
                    openInputStream.close();
                }
                AvatarPhotoController$$ExternalSyntheticLambda2 avatarPhotoController$$ExternalSyntheticLambda2 = new AvatarPhotoController$$ExternalSyntheticLambda2(this);
                if (z) {
                    ThreadUtils.postOnMainThreadDelayed(avatarPhotoController$$ExternalSyntheticLambda2, 150);
                    return;
                } else {
                    ThreadUtils.postOnMainThread(avatarPhotoController$$ExternalSyntheticLambda2);
                    return;
                }
            } catch (Throwable th) {
                if (openInputStream != null) {
                    openInputStream.close();
                }
                throw th;
            }
            throw th;
        } catch (IOException e) {
            Log.w("AvatarPhotoController", "Failed to copy photo", e);
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$copyAndCropPhoto$0() {
        if (!this.mAvatarUi.isFinishing()) {
            cropPhoto(this.mPreCropPictureUri);
        }
    }

    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        appendOutputExtra(intent, this.mCropPictureUri);
        appendCropExtras(intent);
        try {
            StrictMode.disableDeathOnFileUriExposure();
            if (!this.mAvatarUi.startSystemActivityForResult(intent, 1003)) {
                StrictMode.enableDeathOnFileUriExposure();
                onPhotoNotCropped(uri);
            }
        } finally {
            StrictMode.enableDeathOnFileUriExposure();
        }
    }

    private void appendOutputExtra(Intent intent, Uri uri) {
        intent.putExtra("output", uri);
        intent.addFlags(3);
        intent.setClipData(ClipData.newRawUri("output", uri));
    }

    private void appendCropExtras(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", this.mPhotoSize);
        intent.putExtra("outputY", this.mPhotoSize);
    }

    private void onPhotoNotCropped(Uri uri) {
        try {
            ThreadUtils.postOnBackgroundThread((Runnable) new AvatarPhotoController$$ExternalSyntheticLambda1(this, uri)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("AvatarPhotoController", "Error performing internal crop", e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPhotoNotCropped$3(Uri uri) {
        int i = this.mPhotoSize;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(this.mContextInjector.getContentResolver().openInputStream(uri));
            if (decodeStream != null) {
                int rotation = getRotation(uri);
                int min = Math.min(decodeStream.getWidth(), decodeStream.getHeight());
                int width = (decodeStream.getWidth() - min) / 2;
                int height = (decodeStream.getHeight() - min) / 2;
                Matrix matrix = new Matrix();
                RectF rectF = new RectF((float) width, (float) height, (float) (width + min), (float) (height + min));
                int i2 = this.mPhotoSize;
                matrix.setRectToRect(rectF, new RectF(0.0f, 0.0f, (float) i2, (float) i2), Matrix.ScaleToFit.CENTER);
                int i3 = this.mPhotoSize;
                matrix.postRotate((float) rotation, ((float) i3) / 2.0f, ((float) i3) / 2.0f);
                canvas.drawBitmap(decodeStream, matrix, new Paint());
                saveBitmapToFile(createBitmap, new File(this.mImagesDir, "CropEditUserPhoto.jpg"));
                ThreadUtils.postOnMainThread(new AvatarPhotoController$$ExternalSyntheticLambda3(this));
            }
        } catch (FileNotFoundException unused) {
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPhotoNotCropped$2() {
        this.mAvatarUi.returnUriResult(this.mCropPictureUri);
    }

    private int getRotation(Uri uri) {
        int i = -1;
        try {
            i = new ExifInterface(this.mContextInjector.getContentResolver().openInputStream(uri)).getAttributeInt("Orientation", -1);
        } catch (IOException e) {
            Log.e("AvatarPhotoController", "Error while getting rotation", e);
        }
        if (i == 3) {
            return 180;
        }
        if (i != 6) {
            return i != 8 ? 0 : 270;
        }
        return 90;
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("AvatarPhotoController", "Cannot create temp file", e);
        }
    }

    static class AvatarUiImpl implements AvatarUi {
        private final AvatarPickerActivity mActivity;

        AvatarUiImpl(AvatarPickerActivity avatarPickerActivity) {
            this.mActivity = avatarPickerActivity;
        }

        public boolean isFinishing() {
            return this.mActivity.isFinishing() || this.mActivity.isDestroyed();
        }

        public void returnUriResult(Uri uri) {
            this.mActivity.returnUriResult(uri);
        }

        public void startActivityForResult(Intent intent, int i) {
            this.mActivity.startActivityForResult(intent, i);
        }

        public boolean startSystemActivityForResult(Intent intent, int i) {
            List<ResolveInfo> queryIntentActivities = this.mActivity.getPackageManager().queryIntentActivities(intent, 1048576);
            if (queryIntentActivities.isEmpty()) {
                Log.w("AvatarPhotoController", "No system package activity could be found for code " + i);
                return false;
            }
            intent.setPackage(queryIntentActivities.get(0).activityInfo.packageName);
            this.mActivity.startActivityForResult(intent, i);
            return true;
        }

        public int getPhotoSize() {
            return this.mActivity.getResources().getDimensionPixelSize(17105619);
        }
    }

    static class ContextInjectorImpl implements ContextInjector {
        private final Context mContext;
        private final String mFileAuthority;

        ContextInjectorImpl(Context context, String str) {
            this.mContext = context;
            this.mFileAuthority = str;
        }

        public File getCacheDir() {
            return this.mContext.getCacheDir();
        }

        public Uri createTempImageUri(File file, String str, boolean z) {
            File file2 = new File(file, str);
            if (z) {
                file2.delete();
            }
            return FileProvider.getUriForFile(this.mContext, this.mFileAuthority, file2);
        }

        public ContentResolver getContentResolver() {
            return this.mContext.getContentResolver();
        }
    }
}
