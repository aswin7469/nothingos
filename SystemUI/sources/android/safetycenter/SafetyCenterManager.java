package android.safetycenter;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.safetycenter.IOnSafetyCenterDataChangedListener;
import android.safetycenter.config.SafetyCenterConfig;
import android.util.ArrayMap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

@SystemApi
public final class SafetyCenterManager {
    public static final String ACTION_REFRESH_SAFETY_SOURCES = "android.safetycenter.action.REFRESH_SAFETY_SOURCES";
    public static final String ACTION_SAFETY_CENTER_ENABLED_CHANGED = "android.safetycenter.action.SAFETY_CENTER_ENABLED_CHANGED";
    public static final int EXTRA_REFRESH_REQUEST_TYPE_FETCH_FRESH_DATA = 0;
    public static final int EXTRA_REFRESH_REQUEST_TYPE_GET_DATA = 1;
    public static final String EXTRA_REFRESH_SAFETY_SOURCES_BROADCAST_ID = "android.safetycenter.extra.REFRESH_SAFETY_SOURCES_BROADCAST_ID";
    public static final String EXTRA_REFRESH_SAFETY_SOURCES_REQUEST_TYPE = "android.safetycenter.extra.REFRESH_SAFETY_SOURCES_REQUEST_TYPE";
    public static final String EXTRA_REFRESH_SAFETY_SOURCE_IDS = "android.safetycenter.extra.REFRESH_SAFETY_SOURCE_IDS";
    public static final String EXTRA_SAFETY_SOURCE_ID = "android.safetycenter.extra.SAFETY_SOURCE_ID";
    public static final String EXTRA_SAFETY_SOURCE_ISSUE_ID = "android.safetycenter.extra.SAFETY_SOURCE_ISSUE_ID";
    public static final String EXTRA_SAFETY_SOURCE_USER_HANDLE = "android.safetycenter.extra.SAFETY_SOURCE_USER_HANDLE";
    public static final int REFRESH_REASON_DEVICE_LOCALE_CHANGE = 400;
    public static final int REFRESH_REASON_DEVICE_REBOOT = 300;
    public static final int REFRESH_REASON_OTHER = 600;
    public static final int REFRESH_REASON_PAGE_OPEN = 100;
    public static final int REFRESH_REASON_RESCAN_BUTTON_CLICK = 200;
    public static final int REFRESH_REASON_SAFETY_CENTER_ENABLED = 500;
    private final Context mContext;
    private final Object mListenersLock = new Object();
    private final Map<OnSafetyCenterDataChangedListener, ListenerDelegate> mListenersToDelegates = new ArrayMap();
    private final ISafetyCenterManager mService;

    public interface OnSafetyCenterDataChangedListener {
        void onError(SafetyCenterErrorDetails safetyCenterErrorDetails) {
        }

        void onSafetyCenterDataChanged(SafetyCenterData safetyCenterData);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshReason {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshRequestType {
    }

    public SafetyCenterManager(Context context, ISafetyCenterManager iSafetyCenterManager) {
        this.mContext = context;
        this.mService = iSafetyCenterManager;
    }

    public boolean isSafetyCenterEnabled() {
        try {
            return this.mService.isSafetyCenterEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSafetySourceData(String str, SafetySourceData safetySourceData, SafetyEvent safetyEvent) {
        Objects.requireNonNull(str, "safetySourceId cannot be null");
        Objects.requireNonNull(safetyEvent, "safetyEvent cannot be null");
        try {
            this.mService.setSafetySourceData(str, safetySourceData, safetyEvent, this.mContext.getPackageName(), this.mContext.getUser().getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public SafetySourceData getSafetySourceData(String str) {
        Objects.requireNonNull(str, "safetySourceId cannot be null");
        try {
            return this.mService.getSafetySourceData(str, this.mContext.getPackageName(), this.mContext.getUser().getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void reportSafetySourceError(String str, SafetySourceErrorDetails safetySourceErrorDetails) {
        Objects.requireNonNull(str, "safetySourceId cannot be null");
        Objects.requireNonNull(safetySourceErrorDetails, "safetySourceErrorDetails cannot be null");
        try {
            this.mService.reportSafetySourceError(str, safetySourceErrorDetails, this.mContext.getPackageName(), this.mContext.getUser().getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void refreshSafetySources(int i) {
        try {
            this.mService.refreshSafetySources(i, this.mContext.getUser().getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public SafetyCenterConfig getSafetyCenterConfig() {
        try {
            return this.mService.getSafetyCenterConfig();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public SafetyCenterData getSafetyCenterData() {
        try {
            return this.mService.getSafetyCenterData(this.mContext.getUser().getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addOnSafetyCenterDataChangedListener(Executor executor, OnSafetyCenterDataChangedListener onSafetyCenterDataChangedListener) {
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(onSafetyCenterDataChangedListener, "listener cannot be null");
        synchronized (this.mListenersLock) {
            if (!this.mListenersToDelegates.containsKey(onSafetyCenterDataChangedListener)) {
                ListenerDelegate listenerDelegate = new ListenerDelegate(executor, onSafetyCenterDataChangedListener);
                try {
                    this.mService.addOnSafetyCenterDataChangedListener(listenerDelegate, this.mContext.getUser().getIdentifier());
                    this.mListenersToDelegates.put(onSafetyCenterDataChangedListener, listenerDelegate);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    public void removeOnSafetyCenterDataChangedListener(OnSafetyCenterDataChangedListener onSafetyCenterDataChangedListener) {
        Objects.requireNonNull(onSafetyCenterDataChangedListener, "listener cannot be null");
        synchronized (this.mListenersLock) {
            ListenerDelegate listenerDelegate = this.mListenersToDelegates.get(onSafetyCenterDataChangedListener);
            if (listenerDelegate != null) {
                try {
                    this.mService.removeOnSafetyCenterDataChangedListener(listenerDelegate, this.mContext.getUser().getIdentifier());
                    listenerDelegate.markAsRemoved();
                    this.mListenersToDelegates.remove(onSafetyCenterDataChangedListener);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    public void dismissSafetyCenterIssue(String str) {
        Objects.requireNonNull(str, "safetyCenterIssueId cannot be null");
        try {
            this.mService.dismissSafetyCenterIssue(str, this.mContext.getUser().getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void executeSafetyCenterIssueAction(String str, String str2) {
        Objects.requireNonNull(str, "safetyCenterIssueId cannot be null");
        Objects.requireNonNull(str2, "safetyCenterIssueActionId cannot be null");
        try {
            this.mService.executeSafetyCenterIssueAction(str, str2, this.mContext.getUser().getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearAllSafetySourceDataForTests() {
        try {
            this.mService.clearAllSafetySourceDataForTests();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSafetyCenterConfigForTests(SafetyCenterConfig safetyCenterConfig) {
        Objects.requireNonNull(safetyCenterConfig, "safetyCenterConfig cannot be null");
        try {
            this.mService.setSafetyCenterConfigForTests(safetyCenterConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearSafetyCenterConfigForTests() {
        try {
            this.mService.clearSafetyCenterConfigForTests();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static final class ListenerDelegate extends IOnSafetyCenterDataChangedListener.Stub {
        private final Executor mExecutor;
        private final OnSafetyCenterDataChangedListener mOriginalListener;
        private volatile boolean mRemoved;

        private ListenerDelegate(Executor executor, OnSafetyCenterDataChangedListener onSafetyCenterDataChangedListener) {
            this.mRemoved = false;
            this.mExecutor = executor;
            this.mOriginalListener = onSafetyCenterDataChangedListener;
        }

        public void onSafetyCenterDataChanged(SafetyCenterData safetyCenterData) {
            Objects.requireNonNull(safetyCenterData, "safetyCenterData cannot be null");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new SafetyCenterManager$ListenerDelegate$$ExternalSyntheticLambda1(this, safetyCenterData));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSafetyCenterDataChanged$0$android-safetycenter-SafetyCenterManager$ListenerDelegate */
        public /* synthetic */ void mo6545xfd3b6d6e(SafetyCenterData safetyCenterData) {
            if (!this.mRemoved) {
                this.mOriginalListener.onSafetyCenterDataChanged(safetyCenterData);
            }
        }

        public void onError(SafetyCenterErrorDetails safetyCenterErrorDetails) {
            Objects.requireNonNull(safetyCenterErrorDetails, "safetyCenterErrorDetails cannot be null");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new SafetyCenterManager$ListenerDelegate$$ExternalSyntheticLambda0(this, safetyCenterErrorDetails));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onError$1$android-safetycenter-SafetyCenterManager$ListenerDelegate */
        public /* synthetic */ void mo6544xc6b93c54(SafetyCenterErrorDetails safetyCenterErrorDetails) {
            if (!this.mRemoved) {
                this.mOriginalListener.onError(safetyCenterErrorDetails);
            }
        }

        public void markAsRemoved() {
            this.mRemoved = true;
        }
    }
}
