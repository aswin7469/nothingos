package com.android.systemui.statusbar.notification.row;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.ImageResolver;
import com.android.internal.widget.LocalImageResolver;
import com.android.internal.widget.MessagingMessage;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NotificationInlineImageResolver implements ImageResolver {
    private static final String TAG = NotificationInlineImageResolver.class.getSimpleName();
    private final Context mContext;
    private final ImageCache mImageCache;
    @VisibleForTesting
    protected int mMaxImageHeight;
    @VisibleForTesting
    protected int mMaxImageWidth;
    private Set<Uri> mWantedUriSet;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ImageCache {
        Drawable get(Uri uri);

        boolean hasEntry(Uri uri);

        void preload(Uri uri);

        void purge();

        void setImageResolver(NotificationInlineImageResolver notificationInlineImageResolver);
    }

    public NotificationInlineImageResolver(Context context, ImageCache imageCache) {
        this.mContext = context.getApplicationContext();
        this.mImageCache = imageCache;
        if (imageCache != null) {
            imageCache.setImageResolver(this);
        }
        updateMaxImageSizes();
    }

    public boolean hasCache() {
        return this.mImageCache != null && !isLowRam();
    }

    private boolean isLowRam() {
        return ActivityManager.isLowRamDeviceStatic();
    }

    public void updateMaxImageSizes() {
        this.mMaxImageWidth = getMaxImageWidth();
        this.mMaxImageHeight = getMaxImageHeight();
    }

    @VisibleForTesting
    protected int getMaxImageWidth() {
        return this.mContext.getResources().getDimensionPixelSize(isLowRam() ? 17105385 : 17105384);
    }

    @VisibleForTesting
    protected int getMaxImageHeight() {
        return this.mContext.getResources().getDimensionPixelSize(isLowRam() ? 17105383 : 17105382);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable resolveImage(Uri uri) throws IOException {
        return LocalImageResolver.resolveImage(uri, this.mContext, this.mMaxImageWidth, this.mMaxImageHeight);
    }

    public Drawable loadImage(Uri uri) {
        try {
            if (hasCache()) {
                if (!this.mImageCache.hasEntry(uri)) {
                    this.mImageCache.preload(uri);
                }
                return this.mImageCache.get(uri);
            }
            return resolveImage(uri);
        } catch (IOException | SecurityException e) {
            String str = TAG;
            Log.d(str, "loadImage: Can't load image from " + uri, e);
            return null;
        }
    }

    public void preloadImages(Notification notification) {
        if (!hasCache()) {
            return;
        }
        retrieveWantedUriSet(notification);
        getWantedUriSet().forEach(new Consumer() { // from class: com.android.systemui.statusbar.notification.row.NotificationInlineImageResolver$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationInlineImageResolver.this.lambda$preloadImages$0((Uri) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadImages$0(Uri uri) {
        if (!this.mImageCache.hasEntry(uri)) {
            this.mImageCache.preload(uri);
        }
    }

    public void purgeCache() {
        if (!hasCache()) {
            return;
        }
        this.mImageCache.purge();
    }

    private void retrieveWantedUriSet(Notification notification) {
        HashSet hashSet = new HashSet();
        Bundle bundle = notification.extras;
        if (bundle == null) {
            return;
        }
        Parcelable[] parcelableArray = bundle.getParcelableArray("android.messages");
        List<Notification.MessagingStyle.Message> list = null;
        List<Notification.MessagingStyle.Message> messagesFromBundleArray = parcelableArray == null ? null : Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray);
        if (messagesFromBundleArray != null) {
            for (Notification.MessagingStyle.Message message : messagesFromBundleArray) {
                if (MessagingMessage.hasImage(message)) {
                    hashSet.add(message.getDataUri());
                }
            }
        }
        Parcelable[] parcelableArray2 = bundle.getParcelableArray("android.messages.historic");
        if (parcelableArray2 != null) {
            list = Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray2);
        }
        if (list != null) {
            for (Notification.MessagingStyle.Message message2 : list) {
                if (MessagingMessage.hasImage(message2)) {
                    hashSet.add(message2.getDataUri());
                }
            }
        }
        this.mWantedUriSet = hashSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<Uri> getWantedUriSet() {
        return this.mWantedUriSet;
    }
}
