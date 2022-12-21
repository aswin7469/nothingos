package android.app.role;

import android.annotation.SystemApi;
import android.app.Service;
import android.app.role.IRoleController;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteCallback;
import com.android.permission.jarjar.com.android.internal.util.Preconditions;
import java.util.Objects;

@SystemApi
@Deprecated
public abstract class RoleControllerService extends Service {
    public static final String SERVICE_INTERFACE = "android.app.role.RoleControllerService";
    /* access modifiers changed from: private */
    public Handler mWorkerHandler;
    private HandlerThread mWorkerThread;

    public abstract boolean onAddRoleHolder(String str, String str2, int i);

    public abstract boolean onClearRoleHolders(String str, int i);

    public abstract boolean onGrantDefaultRoles();

    @Deprecated
    public abstract boolean onIsApplicationQualifiedForRole(String str, String str2);

    public abstract boolean onIsRoleVisible(String str);

    public abstract boolean onRemoveRoleHolder(String str, String str2, int i);

    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("RoleControllerService");
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        this.mWorkerHandler = new Handler(this.mWorkerThread.getLooper());
    }

    public void onDestroy() {
        super.onDestroy();
        this.mWorkerThread.quitSafely();
    }

    public final IBinder onBind(Intent intent) {
        return new IRoleController.Stub() {
            public void grantDefaultRoles(RemoteCallback remoteCallback) {
                enforceCallerSystemUid("grantDefaultRoles");
                Objects.requireNonNull(remoteCallback, "callback cannot be null");
                RoleControllerService.this.mWorkerHandler.post(new RoleControllerService$1$$ExternalSyntheticLambda1(this, remoteCallback));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$grantDefaultRoles$0$android-app-role-RoleControllerService$1 */
            public /* synthetic */ void mo93xf563ea60(RemoteCallback remoteCallback) {
                RoleControllerService.this.grantDefaultRoles(remoteCallback);
            }

            public void onAddRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) {
                enforceCallerSystemUid("onAddRoleHolder");
                Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
                Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
                Objects.requireNonNull(remoteCallback, "callback cannot be null");
                RoleControllerService.this.mWorkerHandler.post(new RoleControllerService$1$$ExternalSyntheticLambda2(this, str, str2, i, remoteCallback));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onAddRoleHolder$1$android-app-role-RoleControllerService$1 */
            public /* synthetic */ void mo94x7bc265d3(String str, String str2, int i, RemoteCallback remoteCallback) {
                RoleControllerService.this.onAddRoleHolder(str, str2, i, remoteCallback);
            }

            public void onRemoveRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) {
                enforceCallerSystemUid("onRemoveRoleHolder");
                Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
                Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
                Objects.requireNonNull(remoteCallback, "callback cannot be null");
                RoleControllerService.this.mWorkerHandler.post(new RoleControllerService$1$$ExternalSyntheticLambda3(this, str, str2, i, remoteCallback));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onRemoveRoleHolder$2$android-app-role-RoleControllerService$1 */
            public /* synthetic */ void mo96xc98177(String str, String str2, int i, RemoteCallback remoteCallback) {
                RoleControllerService.this.onRemoveRoleHolder(str, str2, i, remoteCallback);
            }

            public void onClearRoleHolders(String str, int i, RemoteCallback remoteCallback) {
                enforceCallerSystemUid("onClearRoleHolders");
                Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
                Objects.requireNonNull(remoteCallback, "callback cannot be null");
                RoleControllerService.this.mWorkerHandler.post(new RoleControllerService$1$$ExternalSyntheticLambda0(this, str, i, remoteCallback));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onClearRoleHolders$3$android-app-role-RoleControllerService$1 */
            public /* synthetic */ void mo95x3d78aed8(String str, int i, RemoteCallback remoteCallback) {
                RoleControllerService.this.onClearRoleHolders(str, i, remoteCallback);
            }

            private void enforceCallerSystemUid(String str) {
                if (Binder.getCallingUid() != 1000) {
                    throw new SecurityException("Only the system process can call " + str + "()");
                }
            }

            public void isApplicationQualifiedForRole(String str, String str2, RemoteCallback remoteCallback) {
                Bundle bundle = null;
                RoleControllerService.this.enforceCallingPermission("android.permission.MANAGE_ROLE_HOLDERS", (String) null);
                Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
                Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
                Objects.requireNonNull(remoteCallback, "callback cannot be null");
                if (RoleControllerService.this.onIsApplicationQualifiedForRole(str, str2)) {
                    bundle = Bundle.EMPTY;
                }
                remoteCallback.sendResult(bundle);
            }

            public void isApplicationVisibleForRole(String str, String str2, RemoteCallback remoteCallback) {
                Bundle bundle = null;
                RoleControllerService.this.enforceCallingPermission("android.permission.MANAGE_ROLE_HOLDERS", (String) null);
                Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
                Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
                Objects.requireNonNull(remoteCallback, "callback cannot be null");
                if (RoleControllerService.this.onIsApplicationVisibleForRole(str, str2)) {
                    bundle = Bundle.EMPTY;
                }
                remoteCallback.sendResult(bundle);
            }

            public void isRoleVisible(String str, RemoteCallback remoteCallback) {
                Bundle bundle = null;
                RoleControllerService.this.enforceCallingPermission("android.permission.MANAGE_ROLE_HOLDERS", (String) null);
                Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
                Objects.requireNonNull(remoteCallback, "callback cannot be null");
                if (RoleControllerService.this.onIsRoleVisible(str)) {
                    bundle = Bundle.EMPTY;
                }
                remoteCallback.sendResult(bundle);
            }
        };
    }

    /* access modifiers changed from: private */
    public void grantDefaultRoles(RemoteCallback remoteCallback) {
        remoteCallback.sendResult(onGrantDefaultRoles() ? Bundle.EMPTY : null);
    }

    /* access modifiers changed from: private */
    public void onAddRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) {
        remoteCallback.sendResult(onAddRoleHolder(str, str2, i) ? Bundle.EMPTY : null);
    }

    /* access modifiers changed from: private */
    public void onRemoveRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) {
        remoteCallback.sendResult(onRemoveRoleHolder(str, str2, i) ? Bundle.EMPTY : null);
    }

    /* access modifiers changed from: private */
    public void onClearRoleHolders(String str, int i, RemoteCallback remoteCallback) {
        remoteCallback.sendResult(onClearRoleHolders(str, i) ? Bundle.EMPTY : null);
    }

    public boolean onIsApplicationVisibleForRole(String str, String str2) {
        return onIsApplicationQualifiedForRole(str, str2);
    }
}
