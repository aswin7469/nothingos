package com.google.android.setupcompat.portal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.portal.IPortalRegisterResultListener;
import com.google.android.setupcompat.portal.ISetupNotificationService;
import com.google.android.setupcompat.portal.PortalConstants;
import com.google.android.setupcompat.util.Logger;

public class PortalHelper {
    public static final String ACTION_BIND_SETUP_NOTIFICATION_SERVICE = "com.google.android.setupcompat.portal.SetupNotificationService.BIND";
    public static final String EXTRA_KEY_IS_SETUP_WIZARD = "isSetupWizard";
    /* access modifiers changed from: private */
    public static final Logger LOG = new Logger("PortalHelper");
    public static final Intent NOTIFICATION_SERVICE_INTENT = new Intent(ACTION_BIND_SETUP_NOTIFICATION_SERVICE).setPackage(PartnerConfigHelper.SUW_PACKAGE_NAME);
    public static final String RESULT_BUNDLE_KEY_ERROR = "Error";
    public static final String RESULT_BUNDLE_KEY_PORTAL_NOTIFICATION_AVAILABLE = "PortalNotificationAvailable";
    public static final String RESULT_BUNDLE_KEY_RESULT = "Result";

    public interface PortalAvailableResultListener {
        void onResult(boolean z);
    }

    public interface ProgressServiceAliveResultListener {
        void onResult(boolean z);
    }

    public interface RegisterCallback {
        void onFailure(Throwable th);

        void onSuccess(boolean z);
    }

    public interface RegisterNotificationCallback {
        void onFailure(Throwable th);

        void onSuccess();
    }

    public static boolean bindSetupNotificationService(Context context, ServiceConnection serviceConnection) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection cannot be null");
        try {
            return context.bindService(NOTIFICATION_SERVICE_INTENT, serviceConnection, 1);
        } catch (SecurityException e) {
            LOG.mo55160e("Exception occurred while binding SetupNotificationService", e);
            return false;
        }
    }

    public static void registerProgressService(final Context context, final ProgressServiceComponent progressServiceComponent, final RegisterCallback registerCallback) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        Preconditions.checkNotNull(progressServiceComponent, "ProgressServiceComponent cannot be null");
        Preconditions.checkNotNull(registerCallback, "RegisterCallback cannot be null");
        if (!bindSetupNotificationService(context, new ServiceConnection() {
            public void onServiceDisconnected(ComponentName componentName) {
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder != null) {
                    try {
                        ISetupNotificationService.Stub.asInterface(iBinder).registerProgressService(ProgressServiceComponent.this, PortalHelper.getCurrentUserHandle(), new IPortalRegisterResultListener.Stub() {
                            public void onResult(Bundle bundle) {
                                if (bundle.getBoolean("Result", false)) {
                                    registerCallback.onSuccess(bundle.getBoolean(PortalHelper.RESULT_BUNDLE_KEY_PORTAL_NOTIFICATION_AVAILABLE, false));
                                } else {
                                    registerCallback.onFailure(new IllegalStateException(bundle.getString("Error", "Unknown error")));
                                }
                                context.unbindService(this);
                            }
                        });
                    } catch (RemoteException | NullPointerException e) {
                        registerCallback.onFailure(e);
                        context.unbindService(this);
                    }
                } else {
                    registerCallback.onFailure(new IllegalStateException("SetupNotification should not return null binder"));
                    context.unbindService(this);
                }
            }
        })) {
            LOG.mo55159e("Failed to bind SetupNotificationService.");
            registerCallback.onFailure(new SecurityException("Failed to bind SetupNotificationService."));
        }
    }

    public static void isPortalAvailable(final Context context, final PortalAvailableResultListener portalAvailableResultListener) {
        if (!bindSetupNotificationService(context, new ServiceConnection() {
            public void onServiceDisconnected(ComponentName componentName) {
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder != null) {
                    try {
                        PortalAvailableResultListener.this.onResult(ISetupNotificationService.Stub.asInterface(iBinder).isPortalAvailable());
                    } catch (RemoteException unused) {
                        PortalHelper.LOG.mo55159e("Failed to invoke SetupNotificationService#isPortalAvailable");
                        PortalAvailableResultListener.this.onResult(false);
                    }
                }
                context.unbindService(this);
            }
        })) {
            LOG.mo55159e("Failed to bind SetupNotificationService. Do you have permission \"com.google.android.setupwizard.SETUP_PROGRESS_SERVICE\"");
            portalAvailableResultListener.onResult(false);
        }
    }

    public static void isProgressServiceAlive(final Context context, final ProgressServiceComponent progressServiceComponent, final ProgressServiceAliveResultListener progressServiceAliveResultListener) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        Preconditions.checkNotNull(progressServiceComponent, "ProgressServiceComponent cannot be null");
        Preconditions.checkNotNull(progressServiceAliveResultListener, "ProgressServiceAliveResultCallback cannot be null");
        if (!bindSetupNotificationService(context, new ServiceConnection() {
            public void onServiceDisconnected(ComponentName componentName) {
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder != null) {
                    try {
                        ProgressServiceAliveResultListener.this.onResult(ISetupNotificationService.Stub.asInterface(iBinder).isProgressServiceAlive(progressServiceComponent, PortalHelper.getCurrentUserHandle()));
                    } catch (RemoteException unused) {
                        PortalHelper.LOG.mo55164w("Failed to invoke SetupNotificationService#isProgressServiceAlive");
                        ProgressServiceAliveResultListener.this.onResult(false);
                    }
                }
                context.unbindService(this);
            }
        })) {
            LOG.mo55159e("Failed to bind SetupNotificationService. Do you have permission \"com.google.android.setupwizard.SETUP_PROGRESS_SERVICE\"");
            progressServiceAliveResultListener.onResult(false);
        }
    }

    /* access modifiers changed from: private */
    public static UserHandle getCurrentUserHandle() {
        return UserHandle.getUserHandleForUid(Process.myUid());
    }

    public static Bundle createResultBundle(boolean z, String str, boolean z2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("Result", z);
        if (!z) {
            bundle.putString("Error", str);
        }
        bundle.putBoolean(RESULT_BUNDLE_KEY_PORTAL_NOTIFICATION_AVAILABLE, z2);
        return bundle;
    }

    public static boolean isFromSUW(Intent intent) {
        return intent != null && intent.getBooleanExtra(EXTRA_KEY_IS_SETUP_WIZARD, false);
    }

    public static class RemainingValueBuilder {
        private final Bundle bundle = new Bundle();

        public static RemainingValueBuilder createBuilder() {
            return new RemainingValueBuilder();
        }

        public RemainingValueBuilder setRemainingSizeInKB(int i) {
            Preconditions.checkArgument(i >= 0, "The remainingSize should be positive integer or zero.");
            this.bundle.putInt(PortalConstants.RemainingValues.REMAINING_SIZE_TO_BE_DOWNLOAD_IN_KB, i);
            return this;
        }

        public Bundle build() {
            return this.bundle;
        }

        private RemainingValueBuilder() {
        }
    }

    private PortalHelper() {
    }
}
