package com.android.settingslib.users;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.settingslib.utils.ThreadUtils;
import java.p026io.File;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.util.concurrent.ExecutionException;

public class EditUserPhotoController {
    private static final String IMAGES_DIR = "multi_user";
    private static final String NEW_USER_PHOTO_FILE_NAME = "NewUserPhoto.png";
    private static final int REQUEST_CODE_PICK_AVATAR = 1004;
    private static final String TAG = "EditUserPhotoController";
    private final Activity mActivity;
    private final ActivityStarter mActivityStarter;
    private final String mFileAuthority;
    private final ImageView mImageView;
    private final File mImagesDir;
    private Bitmap mNewUserPhotoBitmap;
    private Drawable mNewUserPhotoDrawable;

    public EditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView, Bitmap bitmap, Drawable drawable, String str) {
        this.mActivity = activity;
        this.mActivityStarter = activityStarter;
        this.mFileAuthority = str;
        File file = new File(activity.getCacheDir(), IMAGES_DIR);
        this.mImagesDir = file;
        file.mkdir();
        this.mImageView = imageView;
        imageView.setOnClickListener(new EditUserPhotoController$$ExternalSyntheticLambda2(this));
        this.mNewUserPhotoBitmap = bitmap;
        this.mNewUserPhotoDrawable = drawable;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-settingslib-users-EditUserPhotoController */
    public /* synthetic */ void mo29242x883584ca(View view) {
        showAvatarPicker();
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 1004) {
            if (intent.hasExtra("default_icon_tint_color")) {
                onDefaultIconSelected(intent.getIntExtra("default_icon_tint_color", -1));
                return true;
            } else if (intent.getData() != null) {
                onPhotoCropped(intent.getData());
                return true;
            }
        }
        return false;
    }

    public Drawable getNewUserPhotoDrawable() {
        return this.mNewUserPhotoDrawable;
    }

    private void showAvatarPicker() {
        Intent intent = new Intent(this.mImageView.getContext(), AvatarPickerActivity.class);
        intent.putExtra("file_authority", this.mFileAuthority);
        this.mActivityStarter.startActivityForResult(intent, 1004);
    }

    private void onDefaultIconSelected(int i) {
        try {
            ThreadUtils.postOnBackgroundThread((Runnable) new EditUserPhotoController$$ExternalSyntheticLambda1(this, i)).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error processing default icon", e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDefaultIconSelected$2$com-android-settingslib-users-EditUserPhotoController */
    public /* synthetic */ void mo29244x9c87bba2(int i) {
        Resources resources = this.mActivity.getResources();
        ThreadUtils.postOnMainThread(new EditUserPhotoController$$ExternalSyntheticLambda3(this, UserIcons.convertToBitmapAtUserIconSize(resources, UserIcons.getDefaultUserIconInColor(resources, i))));
    }

    private void onPhotoCropped(Uri uri) {
        ThreadUtils.postOnBackgroundThread((Runnable) new EditUserPhotoController$$ExternalSyntheticLambda0(this, uri));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: android.graphics.Bitmap} */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002b A[SYNTHETIC, Splitter:B:18:0x002b] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x003d A[SYNTHETIC, Splitter:B:25:0x003d] */
    /* JADX WARNING: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* renamed from: lambda$onPhotoCropped$4$com-android-settingslib-users-EditUserPhotoController */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void mo29246x34ca5c74(android.net.Uri r6) {
        /*
            r5 = this;
            java.lang.String r0 = "Cannot close image stream"
            java.lang.String r1 = "EditUserPhotoController"
            r2 = 0
            android.app.Activity r3 = r5.mActivity     // Catch:{ FileNotFoundException -> 0x0022, all -> 0x0020 }
            android.content.ContentResolver r3 = r3.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0022, all -> 0x0020 }
            java.io.InputStream r6 = r3.openInputStream(r6)     // Catch:{ FileNotFoundException -> 0x0022, all -> 0x0020 }
            android.graphics.Bitmap r2 = android.graphics.BitmapFactory.decodeStream(r6)     // Catch:{ FileNotFoundException -> 0x001e }
            if (r6 == 0) goto L_0x002e
            r6.close()     // Catch:{ IOException -> 0x0019 }
            goto L_0x002e
        L_0x0019:
            r6 = move-exception
            android.util.Log.w(r1, r0, r6)
            goto L_0x002e
        L_0x001e:
            r3 = move-exception
            goto L_0x0024
        L_0x0020:
            r5 = move-exception
            goto L_0x003b
        L_0x0022:
            r3 = move-exception
            r6 = r2
        L_0x0024:
            java.lang.String r4 = "Cannot find image file"
            android.util.Log.w(r1, r4, r3)     // Catch:{ all -> 0x0039 }
            if (r6 == 0) goto L_0x002e
            r6.close()     // Catch:{ IOException -> 0x0019 }
        L_0x002e:
            if (r2 == 0) goto L_0x0038
            com.android.settingslib.users.EditUserPhotoController$$ExternalSyntheticLambda4 r6 = new com.android.settingslib.users.EditUserPhotoController$$ExternalSyntheticLambda4
            r6.<init>(r5, r2)
            com.android.settingslib.utils.ThreadUtils.postOnMainThread(r6)
        L_0x0038:
            return
        L_0x0039:
            r5 = move-exception
            r2 = r6
        L_0x003b:
            if (r2 == 0) goto L_0x0045
            r2.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0045
        L_0x0041:
            r6 = move-exception
            android.util.Log.w(r1, r0, r6)
        L_0x0045:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.EditUserPhotoController.mo29246x34ca5c74(android.net.Uri):void");
    }

    /* access modifiers changed from: private */
    /* renamed from: onPhotoProcessed */
    public void mo29245xb760733(Bitmap bitmap) {
        if (bitmap != null) {
            this.mNewUserPhotoBitmap = bitmap;
            CircleFramedDrawable instance = CircleFramedDrawable.getInstance(this.mImageView.getContext(), this.mNewUserPhotoBitmap);
            this.mNewUserPhotoDrawable = instance;
            this.mImageView.setImageDrawable(instance);
        }
    }

    /* access modifiers changed from: package-private */
    public File saveNewUserPhotoBitmap() {
        if (this.mNewUserPhotoBitmap == null) {
            return null;
        }
        try {
            File file = new File(this.mImagesDir, NEW_USER_PHOTO_FILE_NAME);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            this.mNewUserPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (IOException e) {
            Log.e(TAG, "Cannot create temp file", e);
            return null;
        }
    }

    static Bitmap loadNewUserPhotoBitmap(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /* access modifiers changed from: package-private */
    public void removeNewUserPhotoBitmapFile() {
        new File(this.mImagesDir, NEW_USER_PHOTO_FILE_NAME).delete();
    }
}
