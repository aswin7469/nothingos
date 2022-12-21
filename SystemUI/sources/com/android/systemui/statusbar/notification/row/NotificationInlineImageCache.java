package com.android.systemui.statusbar.notification.row;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.android.systemui.statusbar.notification.row.NotificationInlineImageResolver;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class NotificationInlineImageCache implements NotificationInlineImageResolver.ImageCache {
    private static final String TAG = "NotificationInlineImageCache";
    private final ConcurrentHashMap<Uri, PreloadImageTask> mCache = new ConcurrentHashMap<>();
    private NotificationInlineImageResolver mResolver;

    public void setImageResolver(NotificationInlineImageResolver notificationInlineImageResolver) {
        this.mResolver = notificationInlineImageResolver;
    }

    public boolean hasEntry(Uri uri) {
        return this.mCache.containsKey(uri);
    }

    public void preload(Uri uri) {
        PreloadImageTask preloadImageTask = new PreloadImageTask(this.mResolver);
        preloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Uri[]{uri});
        this.mCache.put(uri, preloadImageTask);
    }

    public Drawable get(Uri uri) {
        try {
            return (Drawable) this.mCache.get(uri).get();
        } catch (InterruptedException | ExecutionException unused) {
            Log.d(TAG, "get: Failed get image from " + uri);
            return null;
        }
    }

    public void purge() {
        this.mCache.entrySet().removeIf(new NotificationInlineImageCache$$ExternalSyntheticLambda0(this.mResolver.getWantedUriSet()));
    }

    static /* synthetic */ boolean lambda$purge$0(Set set, Map.Entry entry) {
        return !set.contains(entry.getKey());
    }

    private static class PreloadImageTask extends AsyncTask<Uri, Void, Drawable> {
        private final NotificationInlineImageResolver mResolver;

        PreloadImageTask(NotificationInlineImageResolver notificationInlineImageResolver) {
            this.mResolver = notificationInlineImageResolver;
        }

        /* access modifiers changed from: protected */
        public Drawable doInBackground(Uri... uriArr) {
            return this.mResolver.resolveImage(uriArr[0]);
        }
    }
}
