package android.nearby;

import android.accounts.Account;
import android.content.Intent;
import android.nearby.aidl.ByteArrayParcel;
import android.nearby.aidl.FastPairAccountDevicesMetadataRequestParcel;
import android.nearby.aidl.FastPairAccountKeyDeviceMetadataParcel;
import android.nearby.aidl.FastPairAntispoofKeyDeviceMetadataRequestParcel;
import android.nearby.aidl.FastPairEligibleAccountParcel;
import android.nearby.aidl.FastPairEligibleAccountsRequestParcel;
import android.nearby.aidl.FastPairManageAccountDeviceRequestParcel;
import android.nearby.aidl.FastPairManageAccountRequestParcel;
import android.nearby.aidl.IFastPairAccountDevicesMetadataCallback;
import android.nearby.aidl.IFastPairAntispoofKeyDeviceMetadataCallback;
import android.nearby.aidl.IFastPairDataProvider;
import android.nearby.aidl.IFastPairEligibleAccountsCallback;
import android.nearby.aidl.IFastPairManageAccountCallback;
import android.nearby.aidl.IFastPairManageAccountDeviceCallback;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;

public abstract class FastPairDataProviderService extends android.app.Service {
    public static final String ACTION_FAST_PAIR_DATA_PROVIDER = "android.nearby.action.FAST_PAIR_DATA_PROVIDER";
    public static final int ERROR_CODE_BAD_REQUEST = 0;
    public static final int ERROR_CODE_INTERNAL_ERROR = 1;
    public static final int MANAGE_REQUEST_ADD = 0;
    public static final int MANAGE_REQUEST_REMOVE = 1;
    private final IBinder mBinder = new Service();
    /* access modifiers changed from: private */
    public final String mTag;

    @Retention(RetentionPolicy.SOURCE)
    @interface ErrorCode {
    }

    public interface FastPairAccountDevicesMetadataCallback {
        void onError(int i, String str);

        void onFastPairAccountDevicesMetadataReceived(Collection<FastPairAccountKeyDeviceMetadata> collection);
    }

    public interface FastPairAntispoofKeyDeviceMetadataCallback {
        void onError(int i, String str);

        void onFastPairAntispoofKeyDeviceMetadataReceived(FastPairAntispoofKeyDeviceMetadata fastPairAntispoofKeyDeviceMetadata);
    }

    public interface FastPairEligibleAccountsCallback {
        void onError(int i, String str);

        void onFastPairEligibleAccountsReceived(Collection<FastPairEligibleAccount> collection);
    }

    public interface FastPairManageActionCallback {
        void onError(int i, String str);

        void onSuccess();
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface ManageRequestType {
    }

    public abstract void onLoadFastPairAccountDevicesMetadata(FastPairAccountDevicesMetadataRequest fastPairAccountDevicesMetadataRequest, FastPairAccountDevicesMetadataCallback fastPairAccountDevicesMetadataCallback);

    public abstract void onLoadFastPairAntispoofKeyDeviceMetadata(FastPairAntispoofKeyDeviceMetadataRequest fastPairAntispoofKeyDeviceMetadataRequest, FastPairAntispoofKeyDeviceMetadataCallback fastPairAntispoofKeyDeviceMetadataCallback);

    public abstract void onLoadFastPairEligibleAccounts(FastPairEligibleAccountsRequest fastPairEligibleAccountsRequest, FastPairEligibleAccountsCallback fastPairEligibleAccountsCallback);

    public abstract void onManageFastPairAccount(FastPairManageAccountRequest fastPairManageAccountRequest, FastPairManageActionCallback fastPairManageActionCallback);

    public abstract void onManageFastPairAccountDevice(FastPairManageAccountDeviceRequest fastPairManageAccountDeviceRequest, FastPairManageActionCallback fastPairManageActionCallback);

    public FastPairDataProviderService(String str) {
        this.mTag = str;
    }

    public final IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public static class FastPairAntispoofKeyDeviceMetadataRequest {
        private final FastPairAntispoofKeyDeviceMetadataRequestParcel mMetadataRequestParcel;

        private FastPairAntispoofKeyDeviceMetadataRequest(FastPairAntispoofKeyDeviceMetadataRequestParcel fastPairAntispoofKeyDeviceMetadataRequestParcel) {
            this.mMetadataRequestParcel = fastPairAntispoofKeyDeviceMetadataRequestParcel;
        }

        public byte[] getModelId() {
            return this.mMetadataRequestParcel.modelId;
        }
    }

    public static class FastPairAccountDevicesMetadataRequest {
        private final FastPairAccountDevicesMetadataRequestParcel mMetadataRequestParcel;

        private FastPairAccountDevicesMetadataRequest(FastPairAccountDevicesMetadataRequestParcel fastPairAccountDevicesMetadataRequestParcel) {
            this.mMetadataRequestParcel = fastPairAccountDevicesMetadataRequestParcel;
        }

        public Account getAccount() {
            return this.mMetadataRequestParcel.account;
        }

        public Collection<byte[]> getDeviceAccountKeys() {
            if (this.mMetadataRequestParcel.deviceAccountKeys == null) {
                return new ArrayList(0);
            }
            ArrayList arrayList = new ArrayList(this.mMetadataRequestParcel.deviceAccountKeys.length);
            for (ByteArrayParcel byteArrayParcel : this.mMetadataRequestParcel.deviceAccountKeys) {
                arrayList.add(byteArrayParcel.byteArray);
            }
            return arrayList;
        }
    }

    public static class FastPairEligibleAccountsRequest {
        private final FastPairEligibleAccountsRequestParcel mAccountsRequestParcel;

        private FastPairEligibleAccountsRequest(FastPairEligibleAccountsRequestParcel fastPairEligibleAccountsRequestParcel) {
            this.mAccountsRequestParcel = fastPairEligibleAccountsRequestParcel;
        }
    }

    public static class FastPairManageAccountRequest {
        private final FastPairManageAccountRequestParcel mAccountRequestParcel;

        private FastPairManageAccountRequest(FastPairManageAccountRequestParcel fastPairManageAccountRequestParcel) {
            this.mAccountRequestParcel = fastPairManageAccountRequestParcel;
        }

        public int getRequestType() {
            return this.mAccountRequestParcel.requestType;
        }

        public Account getAccount() {
            return this.mAccountRequestParcel.account;
        }
    }

    public static class FastPairManageAccountDeviceRequest {
        private final FastPairManageAccountDeviceRequestParcel mRequestParcel;

        private FastPairManageAccountDeviceRequest(FastPairManageAccountDeviceRequestParcel fastPairManageAccountDeviceRequestParcel) {
            this.mRequestParcel = fastPairManageAccountDeviceRequestParcel;
        }

        public int getRequestType() {
            return this.mRequestParcel.requestType;
        }

        public Account getAccount() {
            return this.mRequestParcel.account;
        }

        public FastPairAccountKeyDeviceMetadata getAccountKeyDeviceMetadata() {
            return new FastPairAccountKeyDeviceMetadata(this.mRequestParcel.accountKeyDeviceMetadata);
        }
    }

    private final class WrapperFastPairAntispoofKeyDeviceMetadataCallback implements FastPairAntispoofKeyDeviceMetadataCallback {
        private IFastPairAntispoofKeyDeviceMetadataCallback mCallback;

        private WrapperFastPairAntispoofKeyDeviceMetadataCallback(IFastPairAntispoofKeyDeviceMetadataCallback iFastPairAntispoofKeyDeviceMetadataCallback) {
            this.mCallback = iFastPairAntispoofKeyDeviceMetadataCallback;
        }

        public void onFastPairAntispoofKeyDeviceMetadataReceived(FastPairAntispoofKeyDeviceMetadata fastPairAntispoofKeyDeviceMetadata) {
            try {
                this.mCallback.onFastPairAntispoofKeyDeviceMetadataReceived(fastPairAntispoofKeyDeviceMetadata.mMetadataParcel);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }

        public void onError(int i, String str) {
            try {
                this.mCallback.onError(i, str);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }
    }

    private final class WrapperFastPairAccountDevicesMetadataCallback implements FastPairAccountDevicesMetadataCallback {
        private IFastPairAccountDevicesMetadataCallback mCallback;

        private WrapperFastPairAccountDevicesMetadataCallback(IFastPairAccountDevicesMetadataCallback iFastPairAccountDevicesMetadataCallback) {
            this.mCallback = iFastPairAccountDevicesMetadataCallback;
        }

        public void onFastPairAccountDevicesMetadataReceived(Collection<FastPairAccountKeyDeviceMetadata> collection) {
            FastPairAccountKeyDeviceMetadataParcel[] fastPairAccountKeyDeviceMetadataParcelArr = new FastPairAccountKeyDeviceMetadataParcel[collection.size()];
            int i = 0;
            for (FastPairAccountKeyDeviceMetadata fastPairAccountKeyDeviceMetadata : collection) {
                fastPairAccountKeyDeviceMetadataParcelArr[i] = fastPairAccountKeyDeviceMetadata.mMetadataParcel;
                i++;
            }
            try {
                this.mCallback.onFastPairAccountDevicesMetadataReceived(fastPairAccountKeyDeviceMetadataParcelArr);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }

        public void onError(int i, String str) {
            try {
                this.mCallback.onError(i, str);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }
    }

    private final class WrapperFastPairEligibleAccountsCallback implements FastPairEligibleAccountsCallback {
        private IFastPairEligibleAccountsCallback mCallback;

        private WrapperFastPairEligibleAccountsCallback(IFastPairEligibleAccountsCallback iFastPairEligibleAccountsCallback) {
            this.mCallback = iFastPairEligibleAccountsCallback;
        }

        public void onFastPairEligibleAccountsReceived(Collection<FastPairEligibleAccount> collection) {
            FastPairEligibleAccountParcel[] fastPairEligibleAccountParcelArr = new FastPairEligibleAccountParcel[collection.size()];
            int i = 0;
            for (FastPairEligibleAccount fastPairEligibleAccount : collection) {
                fastPairEligibleAccountParcelArr[i] = fastPairEligibleAccount.mAccountParcel;
                i++;
            }
            try {
                this.mCallback.onFastPairEligibleAccountsReceived(fastPairEligibleAccountParcelArr);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }

        public void onError(int i, String str) {
            try {
                this.mCallback.onError(i, str);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }
    }

    private final class WrapperFastPairManageAccountCallback implements FastPairManageActionCallback {
        private IFastPairManageAccountCallback mCallback;

        private WrapperFastPairManageAccountCallback(IFastPairManageAccountCallback iFastPairManageAccountCallback) {
            this.mCallback = iFastPairManageAccountCallback;
        }

        public void onSuccess() {
            try {
                this.mCallback.onSuccess();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }

        public void onError(int i, String str) {
            try {
                this.mCallback.onError(i, str);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }
    }

    private final class WrapperFastPairManageAccountDeviceCallback implements FastPairManageActionCallback {
        private IFastPairManageAccountDeviceCallback mCallback;

        private WrapperFastPairManageAccountDeviceCallback(IFastPairManageAccountDeviceCallback iFastPairManageAccountDeviceCallback) {
            this.mCallback = iFastPairManageAccountDeviceCallback;
        }

        public void onSuccess() {
            try {
                this.mCallback.onSuccess();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }

        public void onError(int i, String str) {
            try {
                this.mCallback.onError(i, str);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (RuntimeException e2) {
                Log.w(FastPairDataProviderService.this.mTag, e2);
            }
        }
    }

    private final class Service extends IFastPairDataProvider.Stub {
        Service() {
        }

        public void loadFastPairAntispoofKeyDeviceMetadata(FastPairAntispoofKeyDeviceMetadataRequestParcel fastPairAntispoofKeyDeviceMetadataRequestParcel, IFastPairAntispoofKeyDeviceMetadataCallback iFastPairAntispoofKeyDeviceMetadataCallback) {
            FastPairDataProviderService.this.onLoadFastPairAntispoofKeyDeviceMetadata(new FastPairAntispoofKeyDeviceMetadataRequest(fastPairAntispoofKeyDeviceMetadataRequestParcel), new WrapperFastPairAntispoofKeyDeviceMetadataCallback(iFastPairAntispoofKeyDeviceMetadataCallback));
        }

        public void loadFastPairAccountDevicesMetadata(FastPairAccountDevicesMetadataRequestParcel fastPairAccountDevicesMetadataRequestParcel, IFastPairAccountDevicesMetadataCallback iFastPairAccountDevicesMetadataCallback) {
            FastPairDataProviderService.this.onLoadFastPairAccountDevicesMetadata(new FastPairAccountDevicesMetadataRequest(fastPairAccountDevicesMetadataRequestParcel), new WrapperFastPairAccountDevicesMetadataCallback(iFastPairAccountDevicesMetadataCallback));
        }

        public void loadFastPairEligibleAccounts(FastPairEligibleAccountsRequestParcel fastPairEligibleAccountsRequestParcel, IFastPairEligibleAccountsCallback iFastPairEligibleAccountsCallback) {
            FastPairDataProviderService.this.onLoadFastPairEligibleAccounts(new FastPairEligibleAccountsRequest(fastPairEligibleAccountsRequestParcel), new WrapperFastPairEligibleAccountsCallback(iFastPairEligibleAccountsCallback));
        }

        public void manageFastPairAccount(FastPairManageAccountRequestParcel fastPairManageAccountRequestParcel, IFastPairManageAccountCallback iFastPairManageAccountCallback) {
            FastPairDataProviderService.this.onManageFastPairAccount(new FastPairManageAccountRequest(fastPairManageAccountRequestParcel), new WrapperFastPairManageAccountCallback(iFastPairManageAccountCallback));
        }

        public void manageFastPairAccountDevice(FastPairManageAccountDeviceRequestParcel fastPairManageAccountDeviceRequestParcel, IFastPairManageAccountDeviceCallback iFastPairManageAccountDeviceCallback) {
            FastPairDataProviderService.this.onManageFastPairAccountDevice(new FastPairManageAccountDeviceRequest(fastPairManageAccountDeviceRequestParcel), new WrapperFastPairManageAccountDeviceCallback(iFastPairManageAccountDeviceCallback));
        }
    }
}
