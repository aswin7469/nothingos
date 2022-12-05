package com.android.settingslib.users;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import com.android.settingslib.R$dimen;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.R$string;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.drawable.CircleFramedDrawable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import libcore.io.Streams;
/* loaded from: classes.dex */
public class EditUserPhotoController {
    private final Activity mActivity;
    private final ActivityStarter mActivityStarter;
    private final Uri mCropPictureUri;
    private final String mFileAuthority;
    private final ImageView mImageView;
    private final File mImagesDir;
    private Bitmap mNewUserPhotoBitmap;
    private Drawable mNewUserPhotoDrawable;
    private final int mPhotoSize;
    private final Uri mPreCropPictureUri;
    private final Uri mTakePictureUri;

    public EditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView, Bitmap bitmap, boolean z, String str) {
        this.mActivity = activity;
        this.mActivityStarter = activityStarter;
        this.mImageView = imageView;
        this.mFileAuthority = str;
        File file = new File(activity.getCacheDir(), "multi_user");
        this.mImagesDir = file;
        file.mkdir();
        this.mPreCropPictureUri = createTempImageUri(activity, "PreCropEditUserPhoto.jpg", !z);
        this.mCropPictureUri = createTempImageUri(activity, "CropEditUserPhoto.jpg", !z);
        this.mTakePictureUri = createTempImageUri(activity, "TakeEditUserPhoto.jpg", !z);
        this.mPhotoSize = getPhotoSize(activity);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.settingslib.users.EditUserPhotoController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                EditUserPhotoController.this.lambda$new$0(view);
            }
        });
        this.mNewUserPhotoBitmap = bitmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        showUpdatePhotoPopup();
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            return false;
        }
        Uri data = (intent == null || intent.getData() == null) ? this.mTakePictureUri : intent.getData();
        if (!"content".equals(data.getScheme())) {
            Log.e("EditUserPhotoController", "Invalid pictureUri scheme: " + data.getScheme());
            EventLog.writeEvent(1397638484, "172939189", -1, data.getPath());
            return false;
        }
        switch (i) {
            case 1001:
            case 1002:
                if (this.mTakePictureUri.equals(data)) {
                    if (PhotoCapabilityUtils.canCropPhoto(this.mActivity)) {
                        cropPhoto(data);
                    } else {
                        onPhotoNotCropped(data);
                    }
                } else {
                    copyAndCropPhoto(data);
                }
                return true;
            case 1003:
                onPhotoCropped(data);
                return true;
            default:
                return false;
        }
    }

    public Drawable getNewUserPhotoDrawable() {
        return this.mNewUserPhotoDrawable;
    }

    private void showUpdatePhotoPopup() {
        Context context = this.mImageView.getContext();
        boolean canTakePhoto = PhotoCapabilityUtils.canTakePhoto(context);
        boolean canChoosePhoto = PhotoCapabilityUtils.canChoosePhoto(context);
        if (canTakePhoto || canChoosePhoto) {
            ArrayList arrayList = new ArrayList();
            if (canTakePhoto) {
                arrayList.add(new RestrictedMenuItem(context, context.getString(R$string.user_image_take_photo), "no_set_user_icon", new Runnable() { // from class: com.android.settingslib.users.EditUserPhotoController$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        EditUserPhotoController.this.takePhoto();
                    }
                }));
            }
            if (canChoosePhoto) {
                arrayList.add(new RestrictedMenuItem(context, context.getString(R$string.user_image_choose_photo), "no_set_user_icon", new Runnable() { // from class: com.android.settingslib.users.EditUserPhotoController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        EditUserPhotoController.this.choosePhoto();
                    }
                }));
            }
            final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
            listPopupWindow.setAnchorView(this.mImageView);
            listPopupWindow.setModal(true);
            listPopupWindow.setInputMethodMode(2);
            listPopupWindow.setAdapter(new RestrictedPopupMenuAdapter(context, arrayList));
            listPopupWindow.setWidth(Math.max(this.mImageView.getWidth(), context.getResources().getDimensionPixelSize(R$dimen.update_user_photo_popup_min_width)));
            listPopupWindow.setDropDownGravity(8388611);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.android.settingslib.users.EditUserPhotoController$$ExternalSyntheticLambda1
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    EditUserPhotoController.lambda$showUpdatePhotoPopup$1(listPopupWindow, adapterView, view, i, j);
                }
            });
            listPopupWindow.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showUpdatePhotoPopup$1(ListPopupWindow listPopupWindow, AdapterView adapterView, View view, int i, long j) {
        listPopupWindow.dismiss();
        ((RestrictedMenuItem) adapterView.getAdapter().getItem(i)).doAction();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE_SECURE");
        appendOutputExtra(intent, this.mTakePictureUri);
        this.mActivityStarter.startActivityForResult(intent, 1002);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void choosePhoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT", (Uri) null);
        intent.setType("image/*");
        appendOutputExtra(intent, this.mTakePictureUri);
        this.mActivityStarter.startActivityForResult(intent, 1001);
    }

    private void copyAndCropPhoto(final Uri uri) {
        new AsyncTask<Void, Void, Void>() { // from class: com.android.settingslib.users.EditUserPhotoController.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... voidArr) {
                ContentResolver contentResolver = EditUserPhotoController.this.mActivity.getContentResolver();
                try {
                    InputStream openInputStream = contentResolver.openInputStream(uri);
                    OutputStream openOutputStream = contentResolver.openOutputStream(EditUserPhotoController.this.mPreCropPictureUri);
                    Streams.copy(openInputStream, openOutputStream);
                    if (openOutputStream != null) {
                        openOutputStream.close();
                    }
                    if (openInputStream == null) {
                        return null;
                    }
                    openInputStream.close();
                    return null;
                } catch (IOException e) {
                    Log.w("EditUserPhotoController", "Failed to copy photo", e);
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Void r1) {
                if (EditUserPhotoController.this.mActivity.isFinishing() || EditUserPhotoController.this.mActivity.isDestroyed()) {
                    return;
                }
                EditUserPhotoController editUserPhotoController = EditUserPhotoController.this;
                editUserPhotoController.cropPhoto(editUserPhotoController.mPreCropPictureUri);
            }
        }.execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        appendOutputExtra(intent, this.mCropPictureUri);
        appendCropExtras(intent);
        try {
            StrictMode.disableDeathOnFileUriExposure();
            if (startSystemActivityForResult(intent, 1003)) {
                return;
            }
            StrictMode.enableDeathOnFileUriExposure();
            onPhotoNotCropped(this.mTakePictureUri);
        } finally {
            StrictMode.enableDeathOnFileUriExposure();
        }
    }

    private boolean startSystemActivityForResult(Intent intent, int i) {
        ActivityInfo resolveActivityInfo = intent.resolveActivityInfo(this.mActivity.getPackageManager(), 1048576);
        if (resolveActivityInfo == null) {
            Log.w("EditUserPhotoController", "No system package activity could be found for code " + i);
            return false;
        }
        intent.setPackage(resolveActivityInfo.packageName);
        this.mActivityStarter.startActivityForResult(intent, i);
        return true;
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

    private void onPhotoCropped(final Uri uri) {
        new AsyncTask<Void, Void, Bitmap>() { // from class: com.android.settingslib.users.EditUserPhotoController.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Removed duplicated region for block: B:28:0x0040 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // android.os.AsyncTask
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public Bitmap doInBackground(Void... voidArr) {
                Throwable th;
                InputStream inputStream;
                try {
                    inputStream = EditUserPhotoController.this.mActivity.getContentResolver().openInputStream(uri);
                    try {
                        try {
                            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream);
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    Log.w("EditUserPhotoController", "Cannot close image stream", e);
                                }
                            }
                            return decodeStream;
                        } catch (FileNotFoundException e2) {
                            e = e2;
                            Log.w("EditUserPhotoController", "Cannot find image file", e);
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e3) {
                                    Log.w("EditUserPhotoController", "Cannot close image stream", e3);
                                }
                            }
                            return null;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e4) {
                                Log.w("EditUserPhotoController", "Cannot close image stream", e4);
                            }
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e5) {
                    e = e5;
                    inputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                    inputStream = null;
                    if (inputStream != null) {
                    }
                    throw th;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Bitmap bitmap) {
                EditUserPhotoController.this.onPhotoProcessed(bitmap);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    private void onPhotoNotCropped(final Uri uri) {
        new AsyncTask<Void, Void, Bitmap>() { // from class: com.android.settingslib.users.EditUserPhotoController.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Bitmap doInBackground(Void... voidArr) {
                Bitmap createBitmap = Bitmap.createBitmap(EditUserPhotoController.this.mPhotoSize, EditUserPhotoController.this.mPhotoSize, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                try {
                    Bitmap decodeStream = BitmapFactory.decodeStream(EditUserPhotoController.this.mActivity.getContentResolver().openInputStream(uri));
                    if (decodeStream != null) {
                        EditUserPhotoController editUserPhotoController = EditUserPhotoController.this;
                        int rotation = editUserPhotoController.getRotation(editUserPhotoController.mActivity, uri);
                        int min = Math.min(decodeStream.getWidth(), decodeStream.getHeight());
                        int width = (decodeStream.getWidth() - min) / 2;
                        int height = (decodeStream.getHeight() - min) / 2;
                        Matrix matrix = new Matrix();
                        matrix.setRectToRect(new RectF(width, height, width + min, height + min), new RectF(0.0f, 0.0f, EditUserPhotoController.this.mPhotoSize, EditUserPhotoController.this.mPhotoSize), Matrix.ScaleToFit.CENTER);
                        matrix.postRotate(rotation, EditUserPhotoController.this.mPhotoSize / 2.0f, EditUserPhotoController.this.mPhotoSize / 2.0f);
                        canvas.drawBitmap(decodeStream, matrix, new Paint());
                        return createBitmap;
                    }
                } catch (FileNotFoundException unused) {
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Bitmap bitmap) {
                EditUserPhotoController.this.onPhotoProcessed(bitmap);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRotation(Context context, Uri uri) {
        int i = -1;
        try {
            i = new ExifInterface(context.getContentResolver().openInputStream(uri)).getAttributeInt("Orientation", -1);
        } catch (IOException e) {
            Log.e("EditUserPhotoController", "Error while getting rotation", e);
        }
        if (i != 3) {
            if (i == 6) {
                return 90;
            }
            return i != 8 ? 0 : 270;
        }
        return 180;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPhotoProcessed(Bitmap bitmap) {
        if (bitmap != null) {
            this.mNewUserPhotoBitmap = bitmap;
            CircleFramedDrawable circleFramedDrawable = CircleFramedDrawable.getInstance(this.mImageView.getContext(), this.mNewUserPhotoBitmap);
            this.mNewUserPhotoDrawable = circleFramedDrawable;
            this.mImageView.setImageDrawable(circleFramedDrawable);
        }
        new File(this.mImagesDir, "TakeEditUserPhoto.jpg").delete();
        new File(this.mImagesDir, "CropEditUserPhoto.jpg").delete();
    }

    private static int getPhotoSize(Context context) {
        Cursor query = context.getContentResolver().query(ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI, new String[]{"display_max_dim"}, null, null, null);
        if (query == null) {
            if (query != null) {
                query.close();
            }
            return 500;
        }
        try {
            query.moveToFirst();
            int i = query.getInt(0);
            query.close();
            return i;
        } catch (Throwable th) {
            try {
                query.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private Uri createTempImageUri(Context context, String str, boolean z) {
        File file = new File(this.mImagesDir, str);
        if (z) {
            file.delete();
        }
        return FileProvider.getUriForFile(context, this.mFileAuthority, file);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public File saveNewUserPhotoBitmap() {
        if (this.mNewUserPhotoBitmap == null) {
            return null;
        }
        try {
            File file = new File(this.mImagesDir, "NewUserPhoto.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            this.mNewUserPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (IOException e) {
            Log.e("EditUserPhotoController", "Cannot create temp file", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bitmap loadNewUserPhotoBitmap(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeNewUserPhotoBitmapFile() {
        new File(this.mImagesDir, "NewUserPhoto.png").delete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class RestrictedMenuItem {
        private final Runnable mAction;
        private final RestrictedLockUtils.EnforcedAdmin mAdmin;
        private final Context mContext;
        private final boolean mIsRestrictedByBase;
        private final String mTitle;

        RestrictedMenuItem(Context context, String str, String str2, Runnable runnable) {
            this.mContext = context;
            this.mTitle = str;
            this.mAction = runnable;
            int myUserId = UserHandle.myUserId();
            this.mAdmin = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, str2, myUserId);
            this.mIsRestrictedByBase = RestrictedLockUtilsInternal.hasBaseUserRestriction(context, str2, myUserId);
        }

        public String toString() {
            return this.mTitle;
        }

        void doAction() {
            if (isRestrictedByBase()) {
                return;
            }
            if (isRestrictedByAdmin()) {
                RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.mContext, this.mAdmin);
            } else {
                this.mAction.run();
            }
        }

        boolean isRestrictedByAdmin() {
            return this.mAdmin != null;
        }

        boolean isRestrictedByBase() {
            return this.mIsRestrictedByBase;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class RestrictedPopupMenuAdapter extends ArrayAdapter<RestrictedMenuItem> {
        RestrictedPopupMenuAdapter(Context context, List<RestrictedMenuItem> list) {
            super(context, R$layout.restricted_popup_menu_item, R$id.text, list);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i, view, viewGroup);
            RestrictedMenuItem item = getItem(i);
            TextView textView = (TextView) view2.findViewById(R$id.text);
            ImageView imageView = (ImageView) view2.findViewById(R$id.restricted_icon);
            int i2 = 0;
            textView.setEnabled(!item.isRestrictedByAdmin() && !item.isRestrictedByBase());
            if (!item.isRestrictedByAdmin() || item.isRestrictedByBase()) {
                i2 = 8;
            }
            imageView.setVisibility(i2);
            return view2;
        }
    }
}
