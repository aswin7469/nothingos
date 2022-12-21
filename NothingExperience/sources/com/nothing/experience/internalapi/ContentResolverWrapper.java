package com.nothing.experience.internalapi;

import android.content.AttributionSource;
import android.content.ContentResolver;
import android.net.Uri;

public class ContentResolverWrapper {
    private ContentProviderWrapper providerWrapper = new ContentProviderWrapper();

    public final ContentProviderWrapper acquireUnstableProvider(ContentResolver contentResolver, String str) {
        if (contentResolver != null) {
            return this.providerWrapper.acquireUnstableProvider(contentResolver, Uri.parse(str));
        }
        return null;
    }

    public AttributionSource getAttributionSource(ContentResolver contentResolver) {
        if (contentResolver != null) {
            return contentResolver.getAttributionSource();
        }
        return null;
    }
}
