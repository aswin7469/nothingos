package com.nothing.experience.internalapi;

import android.content.AttributionSource;
import android.content.ContentResolver;
import android.content.IContentProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

public class ContentProviderWrapper {
    private IContentProvider provider;

    public final ContentProviderWrapper acquireUnstableProvider(ContentResolver contentResolver, Uri uri) {
        if (contentResolver == null) {
            return null;
        }
        this.provider = contentResolver.acquireUnstableProvider(uri);
        return new ContentProviderWrapper();
    }

    public Bundle call(AttributionSource attributionSource, String str, String str2, String str3, Bundle bundle) throws RemoteException {
        IContentProvider iContentProvider = this.provider;
        if (iContentProvider != null) {
            return iContentProvider.call(attributionSource, str, str2, str3, bundle);
        }
        return null;
    }
}
