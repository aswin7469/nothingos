package pl.droidsonroids.gif;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import java.io.IOException;
/* loaded from: classes2.dex */
public abstract class InputSource {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract GifInfoHandle open() throws IOException;

    private InputSource() {
    }

    /* loaded from: classes2.dex */
    public static final class AssetSource extends InputSource {
        private final AssetManager mAssetManager;
        private final String mAssetName;

        public AssetSource(@NonNull AssetManager assetManager, @NonNull String str) {
            super();
            this.mAssetManager = assetManager;
            this.mAssetName = str;
        }

        @Override // pl.droidsonroids.gif.InputSource
        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mAssetManager.openFd(this.mAssetName));
        }
    }

    /* loaded from: classes2.dex */
    public static class ResourcesSource extends InputSource {
        private final int mResourceId;
        private final Resources mResources;

        public ResourcesSource(@NonNull Resources resources, @RawRes @DrawableRes int i) {
            super();
            this.mResources = resources;
            this.mResourceId = i;
        }

        @Override // pl.droidsonroids.gif.InputSource
        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mResources.openRawResourceFd(this.mResourceId));
        }
    }
}
