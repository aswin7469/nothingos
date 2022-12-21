package androidx.slice.compat;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import androidx.collection.ArraySet;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import androidx.slice.Slice;
import androidx.slice.SliceItemHolder;
import androidx.slice.SliceProvider;
import androidx.slice.SliceSpec;
import androidx.slice.core.SliceHints;
import androidx.versionedparcelable.ParcelUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SliceProviderCompat {
    private static final String ALL_FILES = "slice_data_all_slice_files";
    public static final String ARG_SUPPORTS_VERSIONED_PARCELABLE = "supports_versioned_parcelable";
    private static final String DATA_PREFIX = "slice_data_";
    public static final String EXTRA_BIND_URI = "slice_uri";
    public static final String EXTRA_INTENT = "slice_intent";
    public static final String EXTRA_PID = "pid";
    public static final String EXTRA_PKG = "pkg";
    public static final String EXTRA_PROVIDER_PKG = "provider_pkg";
    public static final String EXTRA_RESULT = "result";
    public static final String EXTRA_SLICE = "slice";
    public static final String EXTRA_SLICE_DESCENDANTS = "slice_descendants";
    public static final String EXTRA_SUPPORTED_SPECS = "specs";
    public static final String EXTRA_SUPPORTED_SPECS_REVS = "revs";
    public static final String EXTRA_UID = "uid";
    public static final String METHOD_CHECK_PERMISSION = "check_perms";
    public static final String METHOD_GET_DESCENDANTS = "get_descendants";
    public static final String METHOD_GET_PINNED_SPECS = "get_specs";
    public static final String METHOD_GRANT_PERMISSION = "grant_perms";
    public static final String METHOD_MAP_INTENT = "map_slice";
    public static final String METHOD_MAP_ONLY_INTENT = "map_only";
    public static final String METHOD_PIN = "pin_slice";
    public static final String METHOD_REVOKE_PERMISSION = "revoke_perms";
    public static final String METHOD_SLICE = "bind_slice";
    public static final String METHOD_UNPIN = "unpin_slice";
    public static final String PERMS_PREFIX = "slice_perms_";
    private static final long SLICE_BIND_ANR = 2000;
    private static final String TAG = "SliceProviderCompat";
    private final Runnable mAnr = new Runnable() {
        public void run() {
            Process.sendSignal(Process.myPid(), 3);
            Log.wtf(SliceProviderCompat.TAG, "Timed out while handling slice callback " + SliceProviderCompat.this.mCallback);
        }
    };
    String mCallback;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private CompatPermissionManager mPermissionManager;
    private CompatPinnedList mPinnedList;
    private final SliceProvider mProvider;

    public SliceProviderCompat(SliceProvider sliceProvider, CompatPermissionManager compatPermissionManager, Context context) {
        this.mProvider = sliceProvider;
        this.mContext = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(ALL_FILES, 0);
        Set<String> stringSet = sharedPreferences.getStringSet(ALL_FILES, Collections.emptySet());
        if (!stringSet.contains("slice_data_androidx.slice.compat.SliceProviderCompat")) {
            ArraySet arraySet = new ArraySet(stringSet);
            arraySet.add("slice_data_androidx.slice.compat.SliceProviderCompat");
            sharedPreferences.edit().putStringSet(ALL_FILES, arraySet).commit();
        }
        this.mPinnedList = new CompatPinnedList(context, "slice_data_androidx.slice.compat.SliceProviderCompat");
        this.mPermissionManager = compatPermissionManager;
    }

    private Context getContext() {
        return this.mContext;
    }

    public String getCallingPackage() {
        return this.mProvider.getCallingPackage();
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        Parcelable parcelable = null;
        if (str.equals(METHOD_SLICE)) {
            Uri uri = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
            this.mProvider.validateIncomingAuthority(uri.getAuthority());
            Slice handleBindSlice = handleBindSlice(uri, getSpecs(bundle), getCallingPackage());
            Bundle bundle2 = new Bundle();
            if (ARG_SUPPORTS_VERSIONED_PARCELABLE.equals(str2)) {
                synchronized (SliceItemHolder.sSerializeLock) {
                    if (handleBindSlice != null) {
                        parcelable = ParcelUtils.toParcelable(handleBindSlice);
                    }
                    bundle2.putParcelable(EXTRA_SLICE, parcelable);
                }
            } else {
                if (handleBindSlice != null) {
                    parcelable = handleBindSlice.toBundle();
                }
                bundle2.putParcelable(EXTRA_SLICE, parcelable);
            }
            return bundle2;
        } else if (str.equals(METHOD_MAP_INTENT)) {
            Uri onMapIntentToUri = this.mProvider.onMapIntentToUri((Intent) bundle.getParcelable(EXTRA_INTENT));
            this.mProvider.validateIncomingAuthority(onMapIntentToUri.getAuthority());
            Bundle bundle3 = new Bundle();
            if (onMapIntentToUri != null) {
                Slice handleBindSlice2 = handleBindSlice(onMapIntentToUri, getSpecs(bundle), getCallingPackage());
                if (ARG_SUPPORTS_VERSIONED_PARCELABLE.equals(str2)) {
                    synchronized (SliceItemHolder.sSerializeLock) {
                        if (handleBindSlice2 != null) {
                            parcelable = ParcelUtils.toParcelable(handleBindSlice2);
                        }
                        bundle3.putParcelable(EXTRA_SLICE, parcelable);
                    }
                } else {
                    if (handleBindSlice2 != null) {
                        parcelable = handleBindSlice2.toBundle();
                    }
                    bundle3.putParcelable(EXTRA_SLICE, parcelable);
                }
            } else {
                bundle3.putParcelable(EXTRA_SLICE, (Parcelable) null);
            }
            return bundle3;
        } else if (str.equals(METHOD_MAP_ONLY_INTENT)) {
            Uri onMapIntentToUri2 = this.mProvider.onMapIntentToUri((Intent) bundle.getParcelable(EXTRA_INTENT));
            this.mProvider.validateIncomingAuthority(onMapIntentToUri2.getAuthority());
            Bundle bundle4 = new Bundle();
            bundle4.putParcelable(EXTRA_SLICE, onMapIntentToUri2);
            return bundle4;
        } else if (str.equals(METHOD_PIN)) {
            Uri uri2 = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
            this.mProvider.validateIncomingAuthority(uri2.getAuthority());
            Set<SliceSpec> specs = getSpecs(bundle);
            if (this.mPinnedList.addPin(uri2, bundle.getString(EXTRA_PKG), specs)) {
                handleSlicePinned(uri2);
            }
            return null;
        } else if (str.equals(METHOD_UNPIN)) {
            Uri uri3 = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
            this.mProvider.validateIncomingAuthority(uri3.getAuthority());
            if (this.mPinnedList.removePin(uri3, bundle.getString(EXTRA_PKG))) {
                handleSliceUnpinned(uri3);
            }
            return null;
        } else if (str.equals(METHOD_GET_PINNED_SPECS)) {
            Uri uri4 = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
            this.mProvider.validateIncomingAuthority(uri4.getAuthority());
            Bundle bundle5 = new Bundle();
            ArraySet<SliceSpec> specs2 = this.mPinnedList.getSpecs(uri4);
            if (specs2.size() != 0) {
                addSpecs(bundle5, specs2);
                return bundle5;
            }
            throw new IllegalStateException(uri4 + " is not pinned");
        } else if (str.equals(METHOD_GET_DESCENDANTS)) {
            Uri uri5 = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
            this.mProvider.validateIncomingAuthority(uri5.getAuthority());
            Bundle bundle6 = new Bundle();
            bundle6.putParcelableArrayList(EXTRA_SLICE_DESCENDANTS, new ArrayList(handleGetDescendants(uri5)));
            return bundle6;
        } else if (str.equals(METHOD_CHECK_PERMISSION)) {
            Uri uri6 = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
            this.mProvider.validateIncomingAuthority(uri6.getAuthority());
            int i = bundle.getInt(EXTRA_PID);
            int i2 = bundle.getInt(EXTRA_UID);
            Bundle bundle7 = new Bundle();
            bundle7.putInt(EXTRA_RESULT, this.mPermissionManager.checkSlicePermission(uri6, i, i2));
            return bundle7;
        } else {
            if (str.equals(METHOD_GRANT_PERMISSION)) {
                Uri uri7 = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
                this.mProvider.validateIncomingAuthority(uri7.getAuthority());
                String string = bundle.getString(EXTRA_PKG);
                if (Binder.getCallingUid() == Process.myUid()) {
                    this.mPermissionManager.grantSlicePermission(uri7, string);
                } else {
                    throw new SecurityException("Only the owning process can manage slice permissions");
                }
            } else if (str.equals(METHOD_REVOKE_PERMISSION)) {
                Uri uri8 = (Uri) bundle.getParcelable(EXTRA_BIND_URI);
                this.mProvider.validateIncomingAuthority(uri8.getAuthority());
                String string2 = bundle.getString(EXTRA_PKG);
                if (Binder.getCallingUid() == Process.myUid()) {
                    this.mPermissionManager.revokeSlicePermission(uri8, string2);
                } else {
                    throw new SecurityException("Only the owning process can manage slice permissions");
                }
            }
            return null;
        }
    }

    private Collection<Uri> handleGetDescendants(Uri uri) {
        this.mCallback = "onGetSliceDescendants";
        return this.mProvider.onGetSliceDescendants(uri);
    }

    private void handleSlicePinned(Uri uri) {
        this.mCallback = "onSlicePinned";
        this.mHandler.postDelayed(this.mAnr, 2000);
        try {
            this.mProvider.onSlicePinned(uri);
            this.mProvider.handleSlicePinned(uri);
        } finally {
            this.mHandler.removeCallbacks(this.mAnr);
        }
    }

    private void handleSliceUnpinned(Uri uri) {
        this.mCallback = "onSliceUnpinned";
        this.mHandler.postDelayed(this.mAnr, 2000);
        try {
            this.mProvider.onSliceUnpinned(uri);
            this.mProvider.handleSliceUnpinned(uri);
        } finally {
            this.mHandler.removeCallbacks(this.mAnr);
        }
    }

    private Slice handleBindSlice(Uri uri, Set<SliceSpec> set, String str) {
        if (str == null) {
            str = getContext().getPackageManager().getNameForUid(Binder.getCallingUid());
        }
        if (this.mPermissionManager.checkSlicePermission(uri, Binder.getCallingPid(), Binder.getCallingUid()) != 0) {
            return this.mProvider.createPermissionSlice(uri, str);
        }
        return onBindSliceStrict(uri, set);
    }

    private Slice onBindSliceStrict(Uri uri, Set<SliceSpec> set) {
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        this.mCallback = "onBindSlice";
        this.mHandler.postDelayed(this.mAnr, 2000);
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().build());
            SliceProvider.setSpecs(set);
            try {
                Slice onBindSlice = this.mProvider.onBindSlice(uri);
                SliceProvider.setSpecs((Set<SliceSpec>) null);
                this.mHandler.removeCallbacks(this.mAnr);
                StrictMode.setThreadPolicy(threadPolicy);
                return onBindSlice;
            } catch (Exception e) {
                Log.wtf(TAG, "Slice with URI " + uri.toString() + " is invalid.", e);
                SliceProvider.setSpecs((Set<SliceSpec>) null);
                this.mHandler.removeCallbacks(this.mAnr);
                StrictMode.setThreadPolicy(threadPolicy);
                return null;
            }
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(threadPolicy);
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public static Slice bindSlice(Context context, Uri uri, Set<SliceSpec> set) {
        ProviderHolder acquireClient = acquireClient(context.getContentResolver(), uri);
        if (acquireClient.mProvider != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_BIND_URI, uri);
                addSpecs(bundle, set);
                Slice parseSlice = parseSlice(context, acquireClient.mProvider.call(METHOD_SLICE, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle));
                acquireClient.close();
                return parseSlice;
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to bind slice", e);
                acquireClient.close();
                return null;
            } catch (Throwable th) {
                acquireClient.close();
                throw th;
            }
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public static void addSpecs(Bundle bundle, Set<SliceSpec> set) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (SliceSpec next : set) {
            arrayList.add(next.getType());
            arrayList2.add(Integer.valueOf(next.getRevision()));
        }
        bundle.putStringArrayList(EXTRA_SUPPORTED_SPECS, arrayList);
        bundle.putIntegerArrayList(EXTRA_SUPPORTED_SPECS_REVS, arrayList2);
    }

    public static Set<SliceSpec> getSpecs(Bundle bundle) {
        ArraySet arraySet = new ArraySet();
        ArrayList<String> stringArrayList = bundle.getStringArrayList(EXTRA_SUPPORTED_SPECS);
        ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList(EXTRA_SUPPORTED_SPECS_REVS);
        if (!(stringArrayList == null || integerArrayList == null)) {
            for (int i = 0; i < stringArrayList.size(); i++) {
                arraySet.add(new SliceSpec(stringArrayList.get(i), integerArrayList.get(i).intValue()));
            }
        }
        return arraySet;
    }

    public static Slice bindSlice(Context context, Intent intent, Set<SliceSpec> set) {
        Preconditions.checkNotNull(intent, "intent");
        Preconditions.checkArgument((intent.getComponent() == null && intent.getPackage() == null && intent.getData() == null) ? false : true, String.format("Slice intent must be explicit %s", intent));
        ContentResolver contentResolver = context.getContentResolver();
        Uri data = intent.getData();
        if (data != null && "vnd.android.slice".equals(contentResolver.getType(data))) {
            return bindSlice(context, data, set);
        }
        Intent intent2 = new Intent(intent);
        if (!intent2.hasCategory("android.app.slice.category.SLICE")) {
            intent2.addCategory("android.app.slice.category.SLICE");
        }
        List<ResolveInfo> queryIntentContentProviders = context.getPackageManager().queryIntentContentProviders(intent2, 0);
        if (queryIntentContentProviders == null || queryIntentContentProviders.isEmpty()) {
            ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 128);
            if (resolveActivity == null || resolveActivity.activityInfo == null || resolveActivity.activityInfo.metaData == null || !resolveActivity.activityInfo.metaData.containsKey(SliceHints.SLICE_METADATA_KEY)) {
                return null;
            }
            return bindSlice(context, Uri.parse(resolveActivity.activityInfo.metaData.getString(SliceHints.SLICE_METADATA_KEY)), set);
        }
        Uri build = new Uri.Builder().scheme("content").authority(queryIntentContentProviders.get(0).providerInfo.authority).build();
        ProviderHolder acquireClient = acquireClient(contentResolver, build);
        if (acquireClient.mProvider != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_INTENT, intent);
                addSpecs(bundle, set);
                return parseSlice(context, acquireClient.mProvider.call(METHOD_MAP_INTENT, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle));
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to bind slice", e);
                return null;
            } finally {
                acquireClient.close();
            }
        } else {
            throw new IllegalArgumentException("Unknown URI " + build);
        }
    }

    private static Slice parseSlice(final Context context, Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        synchronized (SliceItemHolder.sSerializeLock) {
            try {
                SliceItemHolder.sHandler = new SliceItemHolder.HolderHandler() {
                    public void handle(SliceItemHolder sliceItemHolder, String str) {
                        if (sliceItemHolder.mVersionedParcelable instanceof IconCompat) {
                            IconCompat iconCompat = (IconCompat) sliceItemHolder.mVersionedParcelable;
                            iconCompat.checkResource(context);
                            if (iconCompat.getType() == 2 && iconCompat.getResId() == 0) {
                                sliceItemHolder.mVersionedParcelable = null;
                            }
                        }
                    }
                };
                bundle.setClassLoader(SliceProviderCompat.class.getClassLoader());
                Parcelable parcelable = bundle.getParcelable(EXTRA_SLICE);
                if (parcelable == null) {
                    SliceItemHolder.sHandler = null;
                    return null;
                } else if (parcelable instanceof Bundle) {
                    Slice slice = new Slice((Bundle) parcelable);
                    SliceItemHolder.sHandler = null;
                    return slice;
                } else {
                    Slice slice2 = (Slice) ParcelUtils.fromParcelable(parcelable);
                    SliceItemHolder.sHandler = null;
                    return slice2;
                }
            } catch (Throwable th) {
                SliceItemHolder.sHandler = null;
                throw th;
            }
        }
    }

    public static void pinSlice(Context context, Uri uri, Set<SliceSpec> set) {
        ProviderHolder acquireClient = acquireClient(context.getContentResolver(), uri);
        if (acquireClient.mProvider != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_BIND_URI, uri);
                bundle.putString(EXTRA_PKG, context.getPackageName());
                addSpecs(bundle, set);
                acquireClient.mProvider.call(METHOD_PIN, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to pin slice", e);
            } catch (Throwable th) {
                acquireClient.close();
                throw th;
            }
            acquireClient.close();
            return;
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    public static void unpinSlice(Context context, Uri uri, Set<SliceSpec> set) {
        ProviderHolder acquireClient = acquireClient(context.getContentResolver(), uri);
        if (acquireClient.mProvider != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_BIND_URI, uri);
                bundle.putString(EXTRA_PKG, context.getPackageName());
                addSpecs(bundle, set);
                acquireClient.mProvider.call(METHOD_UNPIN, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to unpin slice", e);
            } catch (Throwable th) {
                acquireClient.close();
                throw th;
            }
            acquireClient.close();
            return;
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    public static Set<SliceSpec> getPinnedSpecs(Context context, Uri uri) {
        ProviderHolder acquireClient = acquireClient(context.getContentResolver(), uri);
        if (acquireClient.mProvider != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_BIND_URI, uri);
                Bundle call = acquireClient.mProvider.call(METHOD_GET_PINNED_SPECS, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
                if (call != null) {
                    Set<SliceSpec> specs = getSpecs(call);
                    acquireClient.close();
                    return specs;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to get pinned specs", e);
            } catch (Throwable th) {
                acquireClient.close();
                throw th;
            }
            acquireClient.close();
            return null;
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    public static Uri mapIntentToUri(Context context, Intent intent) {
        ProviderHolder acquireClient;
        Preconditions.checkNotNull(intent, "intent");
        Preconditions.checkArgument((intent.getComponent() == null && intent.getPackage() == null && intent.getData() == null) ? false : true, String.format("Slice intent must be explicit %s", intent));
        ContentResolver contentResolver = context.getContentResolver();
        Uri data = intent.getData();
        if (data != null && "vnd.android.slice".equals(contentResolver.getType(data))) {
            return data;
        }
        Intent intent2 = new Intent(intent);
        if (!intent2.hasCategory("android.app.slice.category.SLICE")) {
            intent2.addCategory("android.app.slice.category.SLICE");
        }
        List<ResolveInfo> queryIntentContentProviders = context.getPackageManager().queryIntentContentProviders(intent2, 0);
        if (queryIntentContentProviders == null || queryIntentContentProviders.isEmpty()) {
            ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 128);
            if (resolveActivity == null || resolveActivity.activityInfo == null || resolveActivity.activityInfo.metaData == null || !resolveActivity.activityInfo.metaData.containsKey(SliceHints.SLICE_METADATA_KEY)) {
                return null;
            }
            return Uri.parse(resolveActivity.activityInfo.metaData.getString(SliceHints.SLICE_METADATA_KEY));
        }
        Uri build = new Uri.Builder().scheme("content").authority(queryIntentContentProviders.get(0).providerInfo.authority).build();
        try {
            acquireClient = acquireClient(contentResolver, build);
            if (acquireClient.mProvider != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_INTENT, intent);
                Bundle call = acquireClient.mProvider.call(METHOD_MAP_ONLY_INTENT, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
                if (call != null) {
                    Uri uri = (Uri) call.getParcelable(EXTRA_SLICE);
                    if (acquireClient != null) {
                        acquireClient.close();
                    }
                    return uri;
                }
                if (acquireClient != null) {
                    acquireClient.close();
                }
                return null;
            }
            throw new IllegalArgumentException("Unknown URI " + build);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to map slice", e);
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static Collection<Uri> getSliceDescendants(Context context, Uri uri) {
        ProviderHolder acquireClient;
        try {
            acquireClient = acquireClient(context.getContentResolver(), uri);
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_BIND_URI, uri);
            Bundle call = acquireClient.mProvider.call(METHOD_GET_DESCENDANTS, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
            if (call != null) {
                ArrayList parcelableArrayList = call.getParcelableArrayList(EXTRA_SLICE_DESCENDANTS);
                if (acquireClient != null) {
                    acquireClient.close();
                }
                return parcelableArrayList;
            }
            if (acquireClient != null) {
                acquireClient.close();
            }
            return Collections.emptyList();
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to get slice descendants", e);
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static int checkSlicePermission(Context context, String str, Uri uri, int i, int i2) {
        ProviderHolder acquireClient;
        try {
            acquireClient = acquireClient(context.getContentResolver(), uri);
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_BIND_URI, uri);
            bundle.putString(EXTRA_PKG, str);
            bundle.putInt(EXTRA_PID, i);
            bundle.putInt(EXTRA_UID, i2);
            Bundle call = acquireClient.mProvider.call(METHOD_CHECK_PERMISSION, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
            if (call != null) {
                int i3 = call.getInt(EXTRA_RESULT);
                if (acquireClient != null) {
                    acquireClient.close();
                }
                return i3;
            } else if (acquireClient == null) {
                return -1;
            } else {
                acquireClient.close();
                return -1;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to check slice permission", e);
            return -1;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static void grantSlicePermission(Context context, String str, String str2, Uri uri) {
        ProviderHolder acquireClient;
        try {
            acquireClient = acquireClient(context.getContentResolver(), uri);
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_BIND_URI, uri);
            bundle.putString(EXTRA_PROVIDER_PKG, str);
            bundle.putString(EXTRA_PKG, str2);
            acquireClient.mProvider.call(METHOD_GRANT_PERMISSION, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
            if (acquireClient != null) {
                acquireClient.close();
                return;
            }
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to get slice descendants", e);
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static void revokeSlicePermission(Context context, String str, String str2, Uri uri) {
        ProviderHolder acquireClient;
        try {
            acquireClient = acquireClient(context.getContentResolver(), uri);
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_BIND_URI, uri);
            bundle.putString(EXTRA_PROVIDER_PKG, str);
            bundle.putString(EXTRA_PKG, str2);
            acquireClient.mProvider.call(METHOD_REVOKE_PERMISSION, ARG_SUPPORTS_VERSIONED_PARCELABLE, bundle);
            if (acquireClient != null) {
                acquireClient.close();
                return;
            }
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to get slice descendants", e);
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static List<Uri> getPinnedSlices(Context context) {
        ArrayList arrayList = new ArrayList();
        for (String compatPinnedList : context.getSharedPreferences(ALL_FILES, 0).getStringSet(ALL_FILES, Collections.emptySet())) {
            arrayList.addAll(new CompatPinnedList(context, compatPinnedList).getPinnedSlices());
        }
        return arrayList;
    }

    private static ProviderHolder acquireClient(ContentResolver contentResolver, Uri uri) {
        ContentProviderClient acquireUnstableContentProviderClient = contentResolver.acquireUnstableContentProviderClient(uri);
        if (acquireUnstableContentProviderClient != null) {
            return new ProviderHolder(acquireUnstableContentProviderClient);
        }
        throw new IllegalArgumentException("No provider found for " + uri);
    }

    private static class ProviderHolder implements AutoCloseable {
        final ContentProviderClient mProvider;

        ProviderHolder(ContentProviderClient contentProviderClient) {
            this.mProvider = contentProviderClient;
        }

        public void close() {
            if (this.mProvider != null) {
                this.mProvider.close();
            }
        }
    }
}
