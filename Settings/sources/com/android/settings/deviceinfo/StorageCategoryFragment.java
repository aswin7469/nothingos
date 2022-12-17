package com.android.settings.deviceinfo;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.util.SparseArray;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.android.settings.R$id;
import com.android.settings.R$xml;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.deviceinfo.storage.ManageStoragePreferenceController;
import com.android.settings.deviceinfo.storage.SecondaryUserController;
import com.android.settings.deviceinfo.storage.StorageAsyncLoader;
import com.android.settings.deviceinfo.storage.StorageCacheHelper;
import com.android.settings.deviceinfo.storage.StorageEntry;
import com.android.settings.deviceinfo.storage.StorageItemPreferenceController;
import com.android.settings.deviceinfo.storage.UserIconLoader;
import com.android.settings.deviceinfo.storage.VolumeSizesLoader;
import com.android.settingslib.applications.StorageStatsSource;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.deviceinfo.PrivateStorageInfo;
import com.android.settingslib.deviceinfo.StorageManagerVolumeProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StorageCategoryFragment extends DashboardFragment implements LoaderManager.LoaderCallbacks<SparseArray<StorageAsyncLoader.StorageResult>> {
    private SparseArray<StorageAsyncLoader.StorageResult> mAppsResult;
    private boolean mIsLoadedFromCache;
    private boolean mIsWorkProfile;
    private StorageItemPreferenceController mPreferenceController;
    /* access modifiers changed from: private */
    public List<AbstractPreferenceController> mSecondaryUsers;
    /* access modifiers changed from: private */
    public StorageEntry mSelectedStorageEntry;
    private StorageCacheHelper mStorageCacheHelper;
    /* access modifiers changed from: private */
    public PrivateStorageInfo mStorageInfo;
    /* access modifiers changed from: private */
    public StorageManager mStorageManager;
    private int mUserId;
    private UserManager mUserManager;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "StorageCategoryFrag";
    }

    public int getMetricsCategory() {
        return 745;
    }

    public void onLoaderReset(Loader<SparseArray<StorageAsyncLoader.StorageResult>> loader) {
    }

    public void refreshUi(StorageEntry storageEntry) {
        this.mSelectedStorageEntry = storageEntry;
        if (this.mPreferenceController != null) {
            setSecondaryUsersVisible(false);
            if (!this.mSelectedStorageEntry.isMounted()) {
                this.mPreferenceController.setVolume((VolumeInfo) null);
                return;
            }
            if (this.mStorageCacheHelper.hasCachedSizeInfo() && this.mSelectedStorageEntry.isPrivate()) {
                StorageCacheHelper.StorageCache retrieveCachedSize = this.mStorageCacheHelper.retrieveCachedSize();
                this.mPreferenceController.setVolume(this.mSelectedStorageEntry.getVolumeInfo());
                this.mPreferenceController.setUsedSize(retrieveCachedSize.totalUsedSize);
                this.mPreferenceController.setTotalSize(retrieveCachedSize.totalSize);
            }
            if (this.mSelectedStorageEntry.isPrivate()) {
                this.mStorageInfo = null;
                this.mAppsResult = null;
                if (this.mStorageCacheHelper.hasCachedSizeInfo()) {
                    this.mPreferenceController.onLoadFinished(this.mAppsResult, this.mUserId);
                } else {
                    maybeSetLoading(isQuotaSupported());
                    this.mPreferenceController.setVolume((VolumeInfo) null);
                }
                LoaderManager loaderManager = getLoaderManager();
                Bundle bundle = Bundle.EMPTY;
                loaderManager.restartLoader(0, bundle, this);
                getLoaderManager().restartLoader(2, bundle, new VolumeSizeCallbacks());
                getLoaderManager().restartLoader(1, bundle, new IconLoaderCallbacks());
                return;
            }
            this.mPreferenceController.setVolume(this.mSelectedStorageEntry.getVolumeInfo());
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mStorageManager = (StorageManager) getActivity().getSystemService(StorageManager.class);
        if (bundle != null) {
            this.mSelectedStorageEntry = (StorageEntry) bundle.getParcelable("selected_storage_entry_key");
        }
        if (this.mStorageCacheHelper.hasCachedSizeInfo()) {
            this.mIsLoadedFromCache = true;
            StorageEntry storageEntry = this.mSelectedStorageEntry;
            if (storageEntry != null) {
                refreshUi(storageEntry);
            }
            updateSecondaryUserControllers(this.mSecondaryUsers, this.mAppsResult);
            setSecondaryUsersVisible(true);
        }
    }

    public void onAttach(Context context) {
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        boolean z = getArguments().getInt("profile") == 2;
        this.mIsWorkProfile = z;
        this.mUserId = Utils.getCurrentUserId(this.mUserManager, z);
        this.mStorageCacheHelper = new StorageCacheHelper(getContext(), this.mUserId);
        super.onAttach(context);
        ((ManageStoragePreferenceController) use(ManageStoragePreferenceController.class)).setUserId(this.mUserId);
    }

    public void onResume() {
        super.onResume();
        if (this.mIsLoadedFromCache) {
            this.mIsLoadedFromCache = false;
            return;
        }
        StorageEntry storageEntry = this.mSelectedStorageEntry;
        if (storageEntry != null) {
            refreshUi(storageEntry);
        }
    }

    public void onPause() {
        super.onPause();
        getLoaderManager().destroyLoader(0);
        getLoaderManager().destroyLoader(1);
        getLoaderManager().destroyLoader(2);
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable("selected_storage_entry_key", this.mSelectedStorageEntry);
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: private */
    public void onReceivedSizes() {
        if (this.mStorageInfo != null && this.mAppsResult != null) {
            if (getView().findViewById(R$id.loading_container).getVisibility() == 0) {
                setLoading(false, true);
            }
            PrivateStorageInfo privateStorageInfo = this.mStorageInfo;
            long j = privateStorageInfo.totalBytes - privateStorageInfo.freeBytes;
            this.mPreferenceController.setVolume(this.mSelectedStorageEntry.getVolumeInfo());
            this.mPreferenceController.setUsedSize(j);
            this.mPreferenceController.setTotalSize(this.mStorageInfo.totalBytes);
            this.mStorageCacheHelper.cacheTotalSizeAndTotalUsedSize(this.mStorageInfo.totalBytes, j);
            int size = this.mSecondaryUsers.size();
            for (int i = 0; i < size; i++) {
                AbstractPreferenceController abstractPreferenceController = this.mSecondaryUsers.get(i);
                if (abstractPreferenceController instanceof SecondaryUserController) {
                    ((SecondaryUserController) abstractPreferenceController).setTotalSize(this.mStorageInfo.totalBytes);
                }
            }
            this.mPreferenceController.onLoadFinished(this.mAppsResult, this.mUserId);
            updateSecondaryUserControllers(this.mSecondaryUsers, this.mAppsResult);
            setSecondaryUsersVisible(true);
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.storage_category_fragment;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        StorageItemPreferenceController storageItemPreferenceController = new StorageItemPreferenceController(context, this, (VolumeInfo) null, new StorageManagerVolumeProvider((StorageManager) context.getSystemService(StorageManager.class)), this.mIsWorkProfile);
        this.mPreferenceController = storageItemPreferenceController;
        arrayList.add(storageItemPreferenceController);
        List<AbstractPreferenceController> secondaryUserControllers = SecondaryUserController.getSecondaryUserControllers(context, this.mUserManager, this.mIsWorkProfile);
        this.mSecondaryUsers = secondaryUserControllers;
        arrayList.addAll(secondaryUserControllers);
        return arrayList;
    }

    private void updateSecondaryUserControllers(List<AbstractPreferenceController> list, SparseArray<StorageAsyncLoader.StorageResult> sparseArray) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            AbstractPreferenceController abstractPreferenceController = list.get(i);
            if (abstractPreferenceController instanceof StorageAsyncLoader.ResultHandler) {
                ((StorageAsyncLoader.ResultHandler) abstractPreferenceController).handleResult(sparseArray);
            }
        }
    }

    public Loader<SparseArray<StorageAsyncLoader.StorageResult>> onCreateLoader(int i, Bundle bundle) {
        Context context = getContext();
        return new StorageAsyncLoader(context, this.mUserManager, this.mSelectedStorageEntry.getFsUuid(), new StorageStatsSource(context), context.getPackageManager());
    }

    public void onLoadFinished(Loader<SparseArray<StorageAsyncLoader.StorageResult>> loader, SparseArray<StorageAsyncLoader.StorageResult> sparseArray) {
        this.mAppsResult = sparseArray;
        onReceivedSizes();
    }

    public PrivateStorageInfo getPrivateStorageInfo() {
        return this.mStorageInfo;
    }

    public void setPrivateStorageInfo(PrivateStorageInfo privateStorageInfo) {
        this.mStorageInfo = privateStorageInfo;
    }

    public SparseArray<StorageAsyncLoader.StorageResult> getStorageResult() {
        return this.mAppsResult;
    }

    public void setStorageResult(SparseArray<StorageAsyncLoader.StorageResult> sparseArray) {
        this.mAppsResult = sparseArray;
    }

    public void maybeSetLoading(boolean z) {
        if ((z && (this.mStorageInfo == null || this.mAppsResult == null)) || (!z && this.mStorageInfo == null)) {
            setLoading(true, false);
        }
    }

    private boolean isQuotaSupported() {
        return this.mSelectedStorageEntry.isMounted() && ((StorageStatsManager) getActivity().getSystemService(StorageStatsManager.class)).isQuotaSupported(this.mSelectedStorageEntry.getFsUuid());
    }

    private void setSecondaryUsersVisible(boolean z) {
        Optional findAny = this.mSecondaryUsers.stream().filter(new StorageCategoryFragment$$ExternalSyntheticLambda0()).map(new StorageCategoryFragment$$ExternalSyntheticLambda1()).findAny();
        if (findAny.isPresent()) {
            ((SecondaryUserController) findAny.get()).setPreferenceGroupVisible(z);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$setSecondaryUsersVisible$0(AbstractPreferenceController abstractPreferenceController) {
        return abstractPreferenceController instanceof SecondaryUserController;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ SecondaryUserController lambda$setSecondaryUsersVisible$1(AbstractPreferenceController abstractPreferenceController) {
        return (SecondaryUserController) abstractPreferenceController;
    }

    public final class IconLoaderCallbacks implements LoaderManager.LoaderCallbacks<SparseArray<Drawable>> {
        public void onLoaderReset(Loader<SparseArray<Drawable>> loader) {
        }

        public IconLoaderCallbacks() {
        }

        public Loader<SparseArray<Drawable>> onCreateLoader(int i, Bundle bundle) {
            return new UserIconLoader(StorageCategoryFragment.this.getContext(), new C0883xfb8aaf6(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ SparseArray lambda$onCreateLoader$0() {
            return UserIconLoader.loadUserIconsWithContext(StorageCategoryFragment.this.getContext());
        }

        public void onLoadFinished(Loader<SparseArray<Drawable>> loader, SparseArray<Drawable> sparseArray) {
            StorageCategoryFragment.this.mSecondaryUsers.stream().filter(new C0884xfb8aaf7()).forEach(new C0885xfb8aaf8(sparseArray));
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onLoadFinished$1(AbstractPreferenceController abstractPreferenceController) {
            return abstractPreferenceController instanceof UserIconLoader.UserIconHandler;
        }
    }

    public final class VolumeSizeCallbacks implements LoaderManager.LoaderCallbacks<PrivateStorageInfo> {
        public void onLoaderReset(Loader<PrivateStorageInfo> loader) {
        }

        public VolumeSizeCallbacks() {
        }

        public Loader<PrivateStorageInfo> onCreateLoader(int i, Bundle bundle) {
            Context context = StorageCategoryFragment.this.getContext();
            return new VolumeSizesLoader(context, new StorageManagerVolumeProvider(StorageCategoryFragment.this.mStorageManager), (StorageStatsManager) context.getSystemService(StorageStatsManager.class), StorageCategoryFragment.this.mSelectedStorageEntry.getVolumeInfo());
        }

        public void onLoadFinished(Loader<PrivateStorageInfo> loader, PrivateStorageInfo privateStorageInfo) {
            if (privateStorageInfo == null) {
                StorageCategoryFragment.this.getActivity().finish();
                return;
            }
            StorageCategoryFragment.this.mStorageInfo = privateStorageInfo;
            StorageCategoryFragment.this.onReceivedSizes();
        }
    }
}
