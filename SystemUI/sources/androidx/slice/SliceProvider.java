package androidx.slice;

import android.app.PendingIntent;
import android.app.slice.Slice;
import android.app.slice.SliceManager;
import android.app.slice.SliceSpec;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Process;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import androidx.core.app.CoreComponentFactory;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceConvert;
import androidx.slice.SliceProvider;
import androidx.slice.compat.CompatPermissionManager;
import androidx.slice.compat.SliceProviderCompat;
import androidx.slice.core.R$drawable;
import androidx.slice.core.R$string;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class SliceProvider extends ContentProvider implements CoreComponentFactory.CompatWrapped {
    private static Clock sClock;
    private static Set<SliceSpec> sSpecs;
    private String[] mAuthorities;
    private String mAuthority;
    private SliceProviderCompat mCompat;
    private List<Uri> mPinnedSliceUris;
    private Context mContext = null;
    private final Object mCompatLock = new Object();
    private final Object mPinnedSliceUrisLock = new Object();
    private final String[] mAutoGrantPermissions = new String[0];

    @Override // android.content.ContentProvider
    public final int bulkInsert(Uri uri, ContentValues[] values) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public final Uri canonicalize(Uri url) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public final Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public abstract Slice onBindSlice(Uri sliceUri);

    public PendingIntent onCreatePermissionRequest(Uri sliceUri, String callingPackage) {
        return null;
    }

    public abstract boolean onCreateSliceProvider();

    public void onSlicePinned(Uri sliceUri) {
    }

    public void onSliceUnpinned(Uri sliceUri) {
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] projection, Bundle queryArgs, CancellationSignal cancellationSignal) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override // androidx.core.app.CoreComponentFactory.CompatWrapped
    public Object getWrapper() {
        if (Build.VERSION.SDK_INT >= 28) {
            final String[] strArr = this.mAutoGrantPermissions;
            return new android.app.slice.SliceProvider(this, strArr) { // from class: androidx.slice.compat.SliceProviderWrapperContainer$SliceProviderWrapper
                private String[] mAutoGrantPermissions;
                private SliceManager mSliceManager;
                private SliceProvider mSliceProvider;

                @Override // android.content.ContentProvider
                public boolean onCreate() {
                    return true;
                }

                {
                    super(strArr);
                    this.mAutoGrantPermissions = (strArr == null || strArr.length == 0) ? null : autoGrantPermissions;
                    this.mSliceProvider = this;
                }

                @Override // android.app.slice.SliceProvider, android.content.ContentProvider
                public void attachInfo(Context context, ProviderInfo info) {
                    this.mSliceProvider.attachInfo(context, info);
                    super.attachInfo(context, info);
                    this.mSliceManager = (SliceManager) context.getSystemService(SliceManager.class);
                }

                @Override // android.app.slice.SliceProvider
                public PendingIntent onCreatePermissionRequest(Uri sliceUri) {
                    if (this.mAutoGrantPermissions != null) {
                        checkPermissions(sliceUri);
                    }
                    PendingIntent onCreatePermissionRequest = this.mSliceProvider.onCreatePermissionRequest(sliceUri, getCallingPackage());
                    return onCreatePermissionRequest != null ? onCreatePermissionRequest : super.onCreatePermissionRequest(sliceUri);
                }

                @Override // android.app.slice.SliceProvider, android.content.ContentProvider
                public Bundle call(String method, String arg, Bundle extras) {
                    Intent intent;
                    if (this.mAutoGrantPermissions != null) {
                        Uri uri = null;
                        if ("bind_slice".equals(method)) {
                            if (extras != null) {
                                uri = (Uri) extras.getParcelable("slice_uri");
                            }
                        } else if ("map_slice".equals(method) && (intent = (Intent) extras.getParcelable("slice_intent")) != null) {
                            uri = onMapIntentToUri(intent);
                        }
                        if (uri != null && this.mSliceManager.checkSlicePermission(uri, Binder.getCallingPid(), Binder.getCallingUid()) != 0) {
                            checkPermissions(uri);
                        }
                    }
                    if ("androidx.remotecallback.method.PROVIDER_CALLBACK".equals(method)) {
                        return this.mSliceProvider.call(method, arg, extras);
                    }
                    return super.call(method, arg, extras);
                }

                private void checkPermissions(Uri uri) {
                    String[] strArr2;
                    if (uri != null) {
                        for (String str : this.mAutoGrantPermissions) {
                            if (getContext().checkCallingPermission(str) == 0) {
                                this.mSliceManager.grantSlicePermission(str, uri);
                                getContext().getContentResolver().notifyChange(uri, null);
                                return;
                            }
                        }
                    }
                }

                @Override // android.app.slice.SliceProvider
                public Slice onBindSlice(Uri sliceUri, Set<SliceSpec> supportedVersions) {
                    SliceProvider.setSpecs(SliceConvert.wrap(supportedVersions));
                    try {
                        return SliceConvert.unwrap(this.mSliceProvider.onBindSlice(sliceUri));
                    } catch (Exception e) {
                        Log.wtf("SliceProviderWrapper", "Slice with URI " + sliceUri.toString() + " is invalid.", e);
                        return null;
                    } finally {
                        SliceProvider.setSpecs(null);
                    }
                }

                @Override // android.app.slice.SliceProvider
                public void onSlicePinned(Uri sliceUri) {
                    this.mSliceProvider.onSlicePinned(sliceUri);
                    this.mSliceProvider.handleSlicePinned(sliceUri);
                }

                @Override // android.app.slice.SliceProvider
                public void onSliceUnpinned(Uri sliceUri) {
                    this.mSliceProvider.onSliceUnpinned(sliceUri);
                    this.mSliceProvider.handleSliceUnpinned(sliceUri);
                }

                @Override // android.app.slice.SliceProvider
                public Collection<Uri> onGetSliceDescendants(Uri uri) {
                    return this.mSliceProvider.onGetSliceDescendants(uri);
                }

                @Override // android.app.slice.SliceProvider
                public Uri onMapIntentToUri(Intent intent) {
                    return this.mSliceProvider.onMapIntentToUri(intent);
                }
            };
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public final boolean onCreate() {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        return onCreateSliceProvider();
    }

    private SliceProviderCompat getSliceProviderCompat() {
        synchronized (this.mCompatLock) {
            if (this.mCompat == null) {
                this.mCompat = new SliceProviderCompat(this, onCreatePermissionManager(this.mAutoGrantPermissions), getContext());
            }
        }
        return this.mCompat;
    }

    protected CompatPermissionManager onCreatePermissionManager(String[] autoGrantPermissions) {
        Context context = getContext();
        return new CompatPermissionManager(context, "slice_perms_" + getClass().getName(), Process.myUid(), autoGrantPermissions);
    }

    @Override // android.content.ContentProvider
    public final String getType(Uri uri) {
        if (Build.VERSION.SDK_INT < 19) {
            return null;
        }
        return "vnd.android.slice";
    }

    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        if (this.mContext == null) {
            this.mContext = context;
            if (info == null) {
                return;
            }
            setAuthorities(info.authority);
        }
    }

    @Override // android.content.ContentProvider
    public Bundle call(String method, String arg, Bundle extras) {
        int i = Build.VERSION.SDK_INT;
        if (i < 19 || i >= 28 || extras == null) {
            return null;
        }
        return getSliceProviderCompat().call(method, arg, extras);
    }

    private void setAuthorities(String authorities) {
        if (authorities != null) {
            if (authorities.indexOf(59) == -1) {
                this.mAuthority = authorities;
                this.mAuthorities = null;
                return;
            }
            this.mAuthority = null;
            this.mAuthorities = authorities.split(";");
        }
    }

    private boolean matchesOurAuthorities(String authority) {
        String str = this.mAuthority;
        if (str != null) {
            return str.equals(authority);
        }
        String[] strArr = this.mAuthorities;
        if (strArr != null) {
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                if (this.mAuthorities[i].equals(authority)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Slice createPermissionSlice(Uri sliceUri, String callingPackage) {
        Context context = getContext();
        PendingIntent onCreatePermissionRequest = onCreatePermissionRequest(sliceUri, callingPackage);
        if (onCreatePermissionRequest == null) {
            onCreatePermissionRequest = createPermissionIntent(context, sliceUri, callingPackage);
        }
        Slice.Builder builder = new Slice.Builder(sliceUri);
        Slice.Builder addAction = new Slice.Builder(builder).addIcon(IconCompat.createWithResource(context, R$drawable.abc_ic_permission), (String) null, new String[0]).addHints(Arrays.asList("title", "shortcut")).addAction(onCreatePermissionRequest, new Slice.Builder(builder).build(), null);
        TypedValue typedValue = new TypedValue();
        new ContextThemeWrapper(context, 16974123).getTheme().resolveAttribute(16843829, typedValue, true);
        builder.addSubSlice(new Slice.Builder(sliceUri.buildUpon().appendPath("permission").build()).addIcon(IconCompat.createWithResource(context, R$drawable.abc_ic_arrow_forward), (String) null, new String[0]).addText(getPermissionString(context, callingPackage), (String) null, new String[0]).addInt(typedValue.data, "color", new String[0]).addSubSlice(addAction.build(), null).build(), null);
        return builder.addHints(Arrays.asList("permission_request")).build();
    }

    private static PendingIntent createPermissionIntent(Context context, Uri sliceUri, String callingPackage) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context.getPackageName(), "androidx.slice.compat.SlicePermissionActivity"));
        intent.putExtra("slice_uri", sliceUri);
        intent.putExtra("pkg", callingPackage);
        intent.putExtra("provider_pkg", context.getPackageName());
        intent.setData(sliceUri.buildUpon().appendQueryParameter("package", callingPackage).build());
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private static CharSequence getPermissionString(Context context, String callingPackage) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return context.getString(R$string.abc_slices_permission_request, packageManager.getApplicationInfo(callingPackage, 0).loadLabel(packageManager), context.getApplicationInfo().loadLabel(packageManager));
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unknown calling app", e);
        }
    }

    public void handleSlicePinned(Uri sliceUri) {
        List<Uri> pinnedSlices = getPinnedSlices();
        if (!pinnedSlices.contains(sliceUri)) {
            pinnedSlices.add(sliceUri);
        }
    }

    public void handleSliceUnpinned(Uri sliceUri) {
        List<Uri> pinnedSlices = getPinnedSlices();
        if (pinnedSlices.contains(sliceUri)) {
            pinnedSlices.remove(sliceUri);
        }
    }

    public Uri onMapIntentToUri(Intent intent) {
        throw new UnsupportedOperationException("This provider has not implemented intent to uri mapping");
    }

    public Collection<Uri> onGetSliceDescendants(Uri uri) {
        return Collections.emptyList();
    }

    public List<Uri> getPinnedSlices() {
        synchronized (this.mPinnedSliceUrisLock) {
            if (this.mPinnedSliceUris == null) {
                this.mPinnedSliceUris = new ArrayList(SliceManager.getInstance(getContext()).getPinnedSlices());
            }
        }
        return this.mPinnedSliceUris;
    }

    public void validateIncomingAuthority(String authority) throws SecurityException {
        String str;
        if (!matchesOurAuthorities(getAuthorityWithoutUserId(authority))) {
            String str2 = "The authority " + authority + " does not match the one of the contentProvider: ";
            if (this.mAuthority != null) {
                str = str2 + this.mAuthority;
            } else {
                str = str2 + Arrays.toString(this.mAuthorities);
            }
            throw new SecurityException(str);
        }
    }

    private static String getAuthorityWithoutUserId(String auth) {
        if (auth == null) {
            return null;
        }
        return auth.substring(auth.lastIndexOf(64) + 1);
    }

    public static void setSpecs(Set<SliceSpec> specs) {
        sSpecs = specs;
    }

    public static Set<SliceSpec> getCurrentSpecs() {
        return sSpecs;
    }

    public static Clock getClock() {
        return sClock;
    }
}
