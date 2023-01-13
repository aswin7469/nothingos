package com.android.systemui.screenshot;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.p026io.BufferedInputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import javax.inject.Inject;

public class ImageLoader {
    private final ContentResolver mResolver;

    static class Result {
        Bitmap bitmap;
        File fileName;
        Uri uri;

        Result() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Result{uri=");
            sb.append((Object) this.uri);
            sb.append(", fileName=").append((Object) this.fileName);
            sb.append(", bitmap=").append((Object) this.bitmap);
            sb.append('}');
            return sb.toString();
        }
    }

    @Inject
    ImageLoader(ContentResolver contentResolver) {
        this.mResolver = contentResolver;
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<Result> load(Uri uri) {
        return CallbackToFutureAdapter.getFuture(new ImageLoader$$ExternalSyntheticLambda0(this, uri));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$load$0$com-android-systemui-screenshot-ImageLoader  reason: not valid java name */
    public /* synthetic */ Object m3001lambda$load$0$comandroidsystemuiscreenshotImageLoader(Uri uri, CallbackToFutureAdapter.Completer completer) throws Exception {
        InputStream openInputStream;
        Result result = new Result();
        try {
            openInputStream = this.mResolver.openInputStream(uri);
            result.uri = uri;
            result.bitmap = BitmapFactory.decodeStream(openInputStream);
            completer.set(result);
            if (openInputStream == null) {
                return "BitmapFactory#decodeStream";
            }
            openInputStream.close();
            return "BitmapFactory#decodeStream";
        } catch (IOException e) {
            completer.setException(e);
            return "BitmapFactory#decodeStream";
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<Result> load(File file) {
        return CallbackToFutureAdapter.getFuture(new ImageLoader$$ExternalSyntheticLambda1(file));
    }

    static /* synthetic */ Object lambda$load$1(File file, CallbackToFutureAdapter.Completer completer) throws Exception {
        BufferedInputStream bufferedInputStream;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            Result result = new Result();
            result.fileName = file;
            result.bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            completer.set(result);
            bufferedInputStream.close();
            return "BitmapFactory#decodeStream";
        } catch (IOException e) {
            completer.setException(e);
            return "BitmapFactory#decodeStream";
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }
}
