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
import java.p026io.File;
import java.p026io.FileNotFoundException;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import libcore.p030io.Streams;

class AvatarPhotoController {
    private static final String CROP_PICTURE_FILE_NAME = "CropEditUserPhoto.jpg";
    private static final long DELAY_BEFORE_CROP_MILLIS = 150;
    private static final String IMAGES_DIR = "multi_user";
    private static final String PRE_CROP_PICTURE_FILE_NAME = "PreCropEditUserPhoto.jpg";
    static final int REQUEST_CODE_CHOOSE_PHOTO = 1001;
    static final int REQUEST_CODE_CROP_PHOTO = 1003;
    static final int REQUEST_CODE_TAKE_PHOTO = 1002;
    private static final String TAG = "AvatarPhotoController";
    private static final String TAKE_PICTURE_FILE_NAME = "TakeEditUserPhoto.jpg";
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
        File file = new File(contextInjector.getCacheDir(), IMAGES_DIR);
        this.mImagesDir = file;
        file.mkdir();
        this.mPreCropPictureUri = contextInjector.createTempImageUri(file, PRE_CROP_PICTURE_FILE_NAME, !z);
        this.mCropPictureUri = contextInjector.createTempImageUri(file, CROP_PICTURE_FILE_NAME, !z);
        this.mTakePictureUri = contextInjector.createTempImageUri(file, TAKE_PICTURE_FILE_NAME, !z);
        this.mPhotoSize = avatarUi.getPhotoSize();
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            return false;
        }
        Uri data = (intent == null || intent.getData() == null) ? this.mTakePictureUri : intent.getData();
        if (!"content".equals(data.getScheme())) {
            Log.e(TAG, "Invalid pictureUri scheme: " + data.getScheme());
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
            ThreadUtils.postOnBackgroundThread((Runnable) new AvatarPhotoController$$ExternalSyntheticLambda1(this, uri, z)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error performing copy-and-crop", e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$copyAndCropPhoto$1$com-android-settingslib-users-AvatarPhotoController */
    public /* synthetic */ void mo29192xdf1d27a1(Uri uri, boolean z) {
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
                AvatarPhotoController$$ExternalSyntheticLambda3 avatarPhotoController$$ExternalSyntheticLambda3 = new AvatarPhotoController$$ExternalSyntheticLambda3(this);
                if (z) {
                    ThreadUtils.postOnMainThreadDelayed(avatarPhotoController$$ExternalSyntheticLambda3, 150);
                    return;
                } else {
                    ThreadUtils.postOnMainThread(avatarPhotoController$$ExternalSyntheticLambda3);
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
            Log.w(TAG, "Failed to copy photo", e);
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$copyAndCropPhoto$0$com-android-settingslib-users-AvatarPhotoController */
    public /* synthetic */ void mo29191x51e27620() {
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
            ThreadUtils.postOnBackgroundThread((Runnable) new AvatarPhotoController$$ExternalSyntheticLambda2(this, uri)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error performing internal crop", e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPhotoNotCropped$3$com-android-settingslib-users-AvatarPhotoController */
    public /* synthetic */ void mo29194xd76a6ea8(Uri uri) {
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
                saveBitmapToFile(createBitmap, new File(this.mImagesDir, CROP_PICTURE_FILE_NAME));
                ThreadUtils.postOnMainThread(new AvatarPhotoController$$ExternalSyntheticLambda0(this));
            }
        } catch (FileNotFoundException unused) {
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPhotoNotCropped$2$com-android-settingslib-users-AvatarPhotoController */
    public /* synthetic */ void mo29193x4a2fbd27() {
        this.mAvatarUi.returnUriResult(this.mCropPictureUri);
    }

    private int getRotation(Uri uri) {
        int i = -1;
        try {
            i = new ExifInterface(this.mContextInjector.getContentResolver().openInputStream(uri)).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, -1);
        } catch (IOException e) {
            Log.e(TAG, "Error while getting rotation", e);
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
            Log.e(TAG, "Cannot create temp file", e);
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
                Log.w(AvatarPhotoController.TAG, "No system package activity could be found for code " + i);
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
