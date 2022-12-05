package androidx.slice;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import androidx.collection.ArrayMap;
import androidx.slice.widget.SliceLiveData;
import java.util.Collection;
import java.util.Set;
/* loaded from: classes.dex */
class SliceViewManagerWrapper extends SliceViewManagerBase {
    private final ArrayMap<String, String> mCachedAuthorities;
    private final ArrayMap<String, Boolean> mCachedSuspendFlags;
    private final android.app.slice.SliceManager mManager;
    private final Set<android.app.slice.SliceSpec> mSpecs;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SliceViewManagerWrapper(Context context) {
        this(context, (android.app.slice.SliceManager) context.getSystemService(android.app.slice.SliceManager.class));
    }

    SliceViewManagerWrapper(Context context, android.app.slice.SliceManager manager) {
        super(context);
        this.mCachedSuspendFlags = new ArrayMap<>();
        this.mCachedAuthorities = new ArrayMap<>();
        this.mManager = manager;
        this.mSpecs = SliceConvert.unwrap(SliceLiveData.SUPPORTED_SPECS);
    }

    @Override // androidx.slice.SliceViewManager
    public void pinSlice(Uri uri) {
        try {
            this.mManager.pinSlice(uri, this.mSpecs);
        } catch (RuntimeException e) {
            ContentProviderClient acquireContentProviderClient = this.mContext.getContentResolver().acquireContentProviderClient(uri);
            if (acquireContentProviderClient == null) {
                throw new IllegalArgumentException("No provider found for " + uri);
            }
            acquireContentProviderClient.release();
            throw e;
        }
    }

    @Override // androidx.slice.SliceViewManager
    public void unpinSlice(Uri uri) {
        try {
            this.mManager.unpinSlice(uri);
        } catch (IllegalStateException unused) {
        }
    }

    @Override // androidx.slice.SliceViewManager
    public Slice bindSlice(Uri uri) {
        if (isAuthoritySuspended(uri.getAuthority())) {
            return null;
        }
        return SliceConvert.wrap(this.mManager.bindSlice(uri, this.mSpecs), this.mContext);
    }

    @Override // androidx.slice.SliceViewManager
    public Slice bindSlice(Intent intent) {
        if (isPackageSuspended(intent)) {
            return null;
        }
        return SliceConvert.wrap(this.mManager.bindSlice(intent, this.mSpecs), this.mContext);
    }

    private boolean isPackageSuspended(Intent intent) {
        if (intent.getComponent() != null) {
            return isPackageSuspended(intent.getComponent().getPackageName());
        }
        if (intent.getPackage() != null) {
            return isPackageSuspended(intent.getPackage());
        }
        if (intent.getData() == null) {
            return false;
        }
        return isAuthoritySuspended(intent.getData().getAuthority());
    }

    private boolean isAuthoritySuspended(String authority) {
        String str = this.mCachedAuthorities.get(authority);
        if (str == null) {
            ProviderInfo resolveContentProvider = this.mContext.getPackageManager().resolveContentProvider(authority, 0);
            if (resolveContentProvider == null) {
                return false;
            }
            str = resolveContentProvider.packageName;
            this.mCachedAuthorities.put(authority, str);
        }
        return isPackageSuspended(str);
    }

    private boolean isPackageSuspended(String pkg) {
        Boolean bool = this.mCachedSuspendFlags.get(pkg);
        if (bool == null) {
            try {
                Boolean valueOf = Boolean.valueOf((this.mContext.getPackageManager().getApplicationInfo(pkg, 0).flags & 1073741824) != 0);
                this.mCachedSuspendFlags.put(pkg, valueOf);
                bool = valueOf;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }
        return bool.booleanValue();
    }

    @Override // androidx.slice.SliceViewManager
    @SuppressLint({"WrongThread"})
    public Collection<Uri> getSliceDescendants(Uri uri) {
        try {
            return this.mManager.getSliceDescendants(uri);
        } catch (RuntimeException e) {
            ContentProviderClient acquireContentProviderClient = this.mContext.getContentResolver().acquireContentProviderClient(uri);
            if (acquireContentProviderClient == null) {
                throw new IllegalArgumentException("No provider found for " + uri);
            }
            acquireContentProviderClient.release();
            throw e;
        }
    }
}
