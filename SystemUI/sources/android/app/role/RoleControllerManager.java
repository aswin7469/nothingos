package android.app.role;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteCallback;
import android.util.Log;
import android.util.SparseArray;
import com.android.permission.jarjar.com.android.internal.infra.AndroidFuture;
import com.android.permission.jarjar.com.android.internal.infra.ServiceConnector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RoleControllerManager {
    private static final String LOG_TAG = "RoleControllerManager";
    private static final long REQUEST_TIMEOUT_MILLIS = 15000;
    private static volatile ComponentName sRemoteServiceComponentName;
    private static final SparseArray<ServiceConnector<IRoleController>> sRemoteServices = new SparseArray<>();
    private static final Object sRemoteServicesLock = new Object();
    private final ServiceConnector<IRoleController> mRemoteService;

    public static void initializeRemoteServiceComponentName(Context context) {
        sRemoteServiceComponentName = getRemoteServiceComponentName(context);
    }

    public static RoleControllerManager createWithInitializedRemoteServiceComponentName(Handler handler, Context context) {
        return new RoleControllerManager(sRemoteServiceComponentName, handler, context);
    }

    private RoleControllerManager(ComponentName componentName, Handler handler, Context context) {
        synchronized (sRemoteServicesLock) {
            int identifier = context.getUser().getIdentifier();
            SparseArray<ServiceConnector<IRoleController>> sparseArray = sRemoteServices;
            ServiceConnector<IRoleController> serviceConnector = sparseArray.get(identifier);
            if (serviceConnector == null) {
                final Handler handler2 = handler;
                C00001 r2 = new ServiceConnector.Impl<IRoleController>(context.getApplicationContext(), new Intent(RoleControllerService.SERVICE_INTERFACE).setComponent(componentName), 0, identifier, new RoleControllerManager$$ExternalSyntheticLambda6()) {
                    /* access modifiers changed from: protected */
                    public Handler getJobHandler() {
                        return handler2;
                    }
                };
                sparseArray.put(identifier, r2);
                serviceConnector = r2;
            }
            this.mRemoteService = serviceConnector;
        }
    }

    public RoleControllerManager(Context context) {
        this(getRemoteServiceComponentName(context), new Handler(Looper.getMainLooper()), context);
    }

    private static ComponentName getRemoteServiceComponentName(Context context) {
        Intent intent = new Intent(RoleControllerService.SERVICE_INTERFACE);
        PackageManager packageManager = context.getPackageManager();
        intent.setPackage(packageManager.getPermissionControllerPackageName());
        ServiceInfo serviceInfo = packageManager.resolveService(intent, 0).serviceInfo;
        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    public void grantDefaultRoles(Executor executor, Consumer<Boolean> consumer) {
        propagateCallback(this.mRemoteService.postAsync(new RoleControllerManager$$ExternalSyntheticLambda9()), "grantDefaultRoles", executor, consumer);
    }

    static /* synthetic */ CompletableFuture lambda$grantDefaultRoles$0(IRoleController iRoleController) throws Exception {
        AndroidFuture androidFuture = new AndroidFuture();
        iRoleController.grantDefaultRoles(new RemoteCallback(new RoleControllerManager$$ExternalSyntheticLambda2(androidFuture)));
        return androidFuture;
    }

    public void onAddRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) {
        propagateCallback(this.mRemoteService.postAsync(new RoleControllerManager$$ExternalSyntheticLambda3(str, str2, i)), "onAddRoleHolder", remoteCallback);
    }

    static /* synthetic */ CompletableFuture lambda$onAddRoleHolder$1(String str, String str2, int i, IRoleController iRoleController) throws Exception {
        AndroidFuture androidFuture = new AndroidFuture();
        iRoleController.onAddRoleHolder(str, str2, i, new RemoteCallback(new RoleControllerManager$$ExternalSyntheticLambda2(androidFuture)));
        return androidFuture;
    }

    public void onRemoveRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) {
        propagateCallback(this.mRemoteService.postAsync(new RoleControllerManager$$ExternalSyntheticLambda0(str, str2, i)), "onRemoveRoleHolder", remoteCallback);
    }

    static /* synthetic */ CompletableFuture lambda$onRemoveRoleHolder$2(String str, String str2, int i, IRoleController iRoleController) throws Exception {
        AndroidFuture androidFuture = new AndroidFuture();
        iRoleController.onRemoveRoleHolder(str, str2, i, new RemoteCallback(new RoleControllerManager$$ExternalSyntheticLambda2(androidFuture)));
        return androidFuture;
    }

    public void onClearRoleHolders(String str, int i, RemoteCallback remoteCallback) {
        propagateCallback(this.mRemoteService.postAsync(new RoleControllerManager$$ExternalSyntheticLambda5(str, i)), "onClearRoleHolders", remoteCallback);
    }

    static /* synthetic */ CompletableFuture lambda$onClearRoleHolders$3(String str, int i, IRoleController iRoleController) throws Exception {
        AndroidFuture androidFuture = new AndroidFuture();
        iRoleController.onClearRoleHolders(str, i, new RemoteCallback(new RoleControllerManager$$ExternalSyntheticLambda2(androidFuture)));
        return androidFuture;
    }

    public void isApplicationVisibleForRole(String str, String str2, Executor executor, Consumer<Boolean> consumer) {
        propagateCallback(this.mRemoteService.postAsync(new RoleControllerManager$$ExternalSyntheticLambda8(str, str2)), "isApplicationVisibleForRole", executor, consumer);
    }

    static /* synthetic */ CompletableFuture lambda$isApplicationVisibleForRole$4(String str, String str2, IRoleController iRoleController) throws Exception {
        AndroidFuture androidFuture = new AndroidFuture();
        iRoleController.isApplicationVisibleForRole(str, str2, new RemoteCallback(new RoleControllerManager$$ExternalSyntheticLambda2(androidFuture)));
        return androidFuture;
    }

    public void isRoleVisible(String str, Executor executor, Consumer<Boolean> consumer) {
        propagateCallback(this.mRemoteService.postAsync(new RoleControllerManager$$ExternalSyntheticLambda10(str)), "isRoleVisible", executor, consumer);
    }

    static /* synthetic */ CompletableFuture lambda$isRoleVisible$5(String str, IRoleController iRoleController) throws Exception {
        AndroidFuture androidFuture = new AndroidFuture();
        iRoleController.isRoleVisible(str, new RemoteCallback(new RoleControllerManager$$ExternalSyntheticLambda2(androidFuture)));
        return androidFuture;
    }

    private void propagateCallback(AndroidFuture<Bundle> androidFuture, String str, Executor executor, Consumer<Boolean> consumer) {
        androidFuture.orTimeout((long) REQUEST_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).whenComplete(new RoleControllerManager$$ExternalSyntheticLambda1(executor, str, consumer));
    }

    static /* synthetic */ void lambda$propagateCallback$6(Throwable th, String str, Consumer consumer, Bundle bundle) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        boolean z = false;
        if (th != null) {
            try {
                String str2 = LOG_TAG;
                Log.e(str2, "Error calling " + str + "()", th);
                consumer.accept(false);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } else {
            if (bundle != null) {
                z = true;
            }
            consumer.accept(Boolean.valueOf(z));
        }
    }

    private void propagateCallback(AndroidFuture<Bundle> androidFuture, String str, RemoteCallback remoteCallback) {
        androidFuture.orTimeout((long) REQUEST_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).whenComplete(new RoleControllerManager$$ExternalSyntheticLambda4(str, remoteCallback));
    }

    static /* synthetic */ void lambda$propagateCallback$8(String str, RemoteCallback remoteCallback, Bundle bundle, Throwable th) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        if (th != null) {
            try {
                String str2 = LOG_TAG;
                Log.e(str2, "Error calling " + str + "()", th);
                remoteCallback.sendResult((Bundle) null);
            } catch (Throwable th2) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th2;
            }
        } else {
            remoteCallback.sendResult(bundle);
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }
}
