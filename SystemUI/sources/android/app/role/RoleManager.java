package android.app.role;

import android.annotation.SystemApi;
import android.app.role.IOnRoleHoldersChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.permission.jarjar.com.android.internal.util.Preconditions;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public final class RoleManager {
    public static final String ACTION_REQUEST_ROLE = "android.app.role.action.REQUEST_ROLE";
    @SystemApi
    public static final int MANAGE_HOLDERS_FLAG_DONT_KILL_APP = 1;
    public static final String PERMISSION_MANAGE_ROLES_FROM_CONTROLLER = "com.android.permissioncontroller.permission.MANAGE_ROLES_FROM_CONTROLLER";
    public static final String ROLE_ASSISTANT = "android.app.role.ASSISTANT";
    public static final String ROLE_BROWSER = "android.app.role.BROWSER";
    public static final String ROLE_CALL_REDIRECTION = "android.app.role.CALL_REDIRECTION";
    public static final String ROLE_CALL_SCREENING = "android.app.role.CALL_SCREENING";
    @SystemApi
    public static final String ROLE_DEVICE_POLICY_MANAGEMENT = "android.app.role.DEVICE_POLICY_MANAGEMENT";
    public static final String ROLE_DIALER = "android.app.role.DIALER";
    public static final String ROLE_EMERGENCY = "android.app.role.EMERGENCY";
    public static final String ROLE_HOME = "android.app.role.HOME";
    public static final String ROLE_SMS = "android.app.role.SMS";
    @SystemApi
    public static final String ROLE_SYSTEM_ACTIVITY_RECOGNIZER = "android.app.role.SYSTEM_ACTIVITY_RECOGNIZER";
    @SystemApi
    public static final String ROLE_SYSTEM_SUPERVISION = "android.app.role.SYSTEM_SUPERVISION";
    @SystemApi
    public static final String ROLE_SYSTEM_WELLBEING = "android.app.role.SYSTEM_WELLBEING";
    private final Context mContext;
    private final SparseArray<ArrayMap<OnRoleHoldersChangedListener, OnRoleHoldersChangedListenerDelegate>> mListeners = new SparseArray<>();
    private final Object mListenersLock = new Object();
    private RoleControllerManager mRoleControllerManager;
    private final Object mRoleControllerManagerLock = new Object();
    private final IRoleManager mService;

    public @interface ManageHoldersFlags {
    }

    public RoleManager(Context context, IRoleManager iRoleManager) {
        this.mContext = context;
        this.mService = iRoleManager;
    }

    public Intent createRequestRoleIntent(String str) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        Intent intent = new Intent(ACTION_REQUEST_ROLE);
        intent.setPackage(this.mContext.getPackageManager().getPermissionControllerPackageName());
        intent.putExtra("android.intent.extra.ROLE_NAME", str);
        return intent;
    }

    public boolean isRoleAvailable(String str) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        try {
            return this.mService.isRoleAvailable(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isRoleHeld(String str) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        try {
            return this.mService.isRoleHeld(str, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<String> getRoleHolders(String str) {
        return getRoleHoldersAsUser(str, Process.myUserHandle());
    }

    @SystemApi
    public List<String> getRoleHoldersAsUser(String str, UserHandle userHandle) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        Objects.requireNonNull(userHandle, "user cannot be null");
        try {
            return this.mService.getRoleHoldersAsUser(str, userHandle.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void addRoleHolderAsUser(String str, String str2, int i, UserHandle userHandle, Executor executor, Consumer<Boolean> consumer) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
        Objects.requireNonNull(userHandle, "user cannot be null");
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(consumer, "callback cannot be null");
        try {
            this.mService.addRoleHolderAsUser(str, str2, i, userHandle.getIdentifier(), createRemoteCallback(executor, consumer));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void removeRoleHolderAsUser(String str, String str2, int i, UserHandle userHandle, Executor executor, Consumer<Boolean> consumer) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
        Objects.requireNonNull(userHandle, "user cannot be null");
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(consumer, "callback cannot be null");
        try {
            this.mService.removeRoleHolderAsUser(str, str2, i, userHandle.getIdentifier(), createRemoteCallback(executor, consumer));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void clearRoleHoldersAsUser(String str, int i, UserHandle userHandle, Executor executor, Consumer<Boolean> consumer) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        Objects.requireNonNull(userHandle, "user cannot be null");
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(consumer, "callback cannot be null");
        try {
            this.mService.clearRoleHoldersAsUser(str, i, userHandle.getIdentifier(), createRemoteCallback(executor, consumer));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static RemoteCallback createRemoteCallback(Executor executor, Consumer<Boolean> consumer) {
        return new RemoteCallback(new RoleManager$$ExternalSyntheticLambda1(executor, consumer));
    }

    static /* synthetic */ void lambda$createRemoteCallback$0(Bundle bundle, Consumer consumer) {
        boolean z = bundle != null;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            consumer.accept(Boolean.valueOf(z));
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @SystemApi
    public void addOnRoleHoldersChangedListenerAsUser(Executor executor, OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle) {
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(onRoleHoldersChangedListener, "listener cannot be null");
        Objects.requireNonNull(userHandle, "user cannot be null");
        int identifier = userHandle.getIdentifier();
        synchronized (this.mListenersLock) {
            ArrayMap arrayMap = this.mListeners.get(identifier);
            if (arrayMap == null) {
                arrayMap = new ArrayMap();
                this.mListeners.put(identifier, arrayMap);
            } else if (arrayMap.containsKey(onRoleHoldersChangedListener)) {
                return;
            }
            OnRoleHoldersChangedListenerDelegate onRoleHoldersChangedListenerDelegate = new OnRoleHoldersChangedListenerDelegate(executor, onRoleHoldersChangedListener);
            try {
                this.mService.addOnRoleHoldersChangedListenerAsUser(onRoleHoldersChangedListenerDelegate, identifier);
                arrayMap.put(onRoleHoldersChangedListener, onRoleHoldersChangedListenerDelegate);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0040, code lost:
        return;
     */
    @android.annotation.SystemApi
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeOnRoleHoldersChangedListenerAsUser(android.app.role.OnRoleHoldersChangedListener r6, android.os.UserHandle r7) {
        /*
            r5 = this;
            java.lang.String r0 = "listener cannot be null"
            java.util.Objects.requireNonNull(r6, (java.lang.String) r0)
            java.lang.String r0 = "user cannot be null"
            java.util.Objects.requireNonNull(r7, (java.lang.String) r0)
            int r0 = r7.getIdentifier()
            java.lang.Object r1 = r5.mListenersLock
            monitor-enter(r1)
            android.util.SparseArray<android.util.ArrayMap<android.app.role.OnRoleHoldersChangedListener, android.app.role.RoleManager$OnRoleHoldersChangedListenerDelegate>> r2 = r5.mListeners     // Catch:{ all -> 0x0047 }
            java.lang.Object r2 = r2.get(r0)     // Catch:{ all -> 0x0047 }
            android.util.ArrayMap r2 = (android.util.ArrayMap) r2     // Catch:{ all -> 0x0047 }
            if (r2 != 0) goto L_0x001e
            monitor-exit(r1)     // Catch:{ all -> 0x0047 }
            return
        L_0x001e:
            java.lang.Object r3 = r2.get(r6)     // Catch:{ all -> 0x0047 }
            android.app.role.RoleManager$OnRoleHoldersChangedListenerDelegate r3 = (android.app.role.RoleManager.OnRoleHoldersChangedListenerDelegate) r3     // Catch:{ all -> 0x0047 }
            if (r3 != 0) goto L_0x0028
            monitor-exit(r1)     // Catch:{ all -> 0x0047 }
            return
        L_0x0028:
            android.app.role.IRoleManager r4 = r5.mService     // Catch:{ RemoteException -> 0x0041 }
            int r7 = r7.getIdentifier()     // Catch:{ RemoteException -> 0x0041 }
            r4.removeOnRoleHoldersChangedListenerAsUser(r3, r7)     // Catch:{ RemoteException -> 0x0041 }
            r2.remove(r6)     // Catch:{ all -> 0x0047 }
            boolean r6 = r2.isEmpty()     // Catch:{ all -> 0x0047 }
            if (r6 == 0) goto L_0x003f
            android.util.SparseArray<android.util.ArrayMap<android.app.role.OnRoleHoldersChangedListener, android.app.role.RoleManager$OnRoleHoldersChangedListenerDelegate>> r5 = r5.mListeners     // Catch:{ all -> 0x0047 }
            r5.remove(r0)     // Catch:{ all -> 0x0047 }
        L_0x003f:
            monitor-exit(r1)     // Catch:{ all -> 0x0047 }
            return
        L_0x0041:
            r5 = move-exception
            java.lang.RuntimeException r5 = r5.rethrowFromSystemServer()     // Catch:{ all -> 0x0047 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r5 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0047 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.role.RoleManager.removeOnRoleHoldersChangedListenerAsUser(android.app.role.OnRoleHoldersChangedListener, android.os.UserHandle):void");
    }

    @SystemApi
    public boolean isBypassingRoleQualification() {
        try {
            return this.mService.isBypassingRoleQualification();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setBypassingRoleQualification(boolean z) {
        try {
            this.mService.setBypassingRoleQualification(z);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @Deprecated
    public void setRoleNamesFromController(List<String> list) {
        Objects.requireNonNull(list, "roleNames cannot be null");
        try {
            this.mService.setRoleNamesFromController(list);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @Deprecated
    public boolean addRoleHolderFromController(String str, String str2) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
        try {
            return this.mService.addRoleHolderFromController(str, str2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @Deprecated
    public boolean removeRoleHolderFromController(String str, String str2) {
        Preconditions.checkStringNotEmpty(str, "roleName cannot be null or empty");
        Preconditions.checkStringNotEmpty(str2, "packageName cannot be null or empty");
        try {
            return this.mService.removeRoleHolderFromController(str, str2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @Deprecated
    public List<String> getHeldRolesFromController(String str) {
        Preconditions.checkStringNotEmpty(str, "packageName cannot be null or empty");
        try {
            return this.mService.getHeldRolesFromController(str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String getBrowserRoleHolder(int i) {
        try {
            return this.mService.getBrowserRoleHolder(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean setBrowserRoleHolder(String str, int i) {
        try {
            return this.mService.setBrowserRoleHolder(str, i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String getSmsRoleHolder(int i) {
        try {
            return this.mService.getSmsRoleHolder(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void isRoleVisible(String str, Executor executor, Consumer<Boolean> consumer) {
        getRoleControllerManager().isRoleVisible(str, executor, consumer);
    }

    @SystemApi
    public void isApplicationVisibleForRole(String str, String str2, Executor executor, Consumer<Boolean> consumer) {
        getRoleControllerManager().isApplicationVisibleForRole(str, str2, executor, consumer);
    }

    private RoleControllerManager getRoleControllerManager() {
        RoleControllerManager roleControllerManager;
        synchronized (this.mRoleControllerManagerLock) {
            if (this.mRoleControllerManager == null) {
                this.mRoleControllerManager = new RoleControllerManager(this.mContext);
            }
            roleControllerManager = this.mRoleControllerManager;
        }
        return roleControllerManager;
    }

    private static class OnRoleHoldersChangedListenerDelegate extends IOnRoleHoldersChangedListener.Stub {
        private final Executor mExecutor;
        private final OnRoleHoldersChangedListener mListener;

        OnRoleHoldersChangedListenerDelegate(Executor executor, OnRoleHoldersChangedListener onRoleHoldersChangedListener) {
            this.mExecutor = executor;
            this.mListener = onRoleHoldersChangedListener;
        }

        public void onRoleHoldersChanged(String str, int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new C0002xa9a4e77(this, str, i));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onRoleHoldersChanged$0$android-app-role-RoleManager$OnRoleHoldersChangedListenerDelegate */
        public /* synthetic */ void mo119x7c07b97c(String str, int i) {
            this.mListener.onRoleHoldersChanged(str, UserHandle.of(i));
        }
    }
}
