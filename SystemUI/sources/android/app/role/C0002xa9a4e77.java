package android.app.role;

import android.app.role.RoleManager;

/* renamed from: android.app.role.RoleManager$OnRoleHoldersChangedListenerDelegate$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0002xa9a4e77 implements Runnable {
    public final /* synthetic */ RoleManager.OnRoleHoldersChangedListenerDelegate f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ C0002xa9a4e77(RoleManager.OnRoleHoldersChangedListenerDelegate onRoleHoldersChangedListenerDelegate, String str, int i) {
        this.f$0 = onRoleHoldersChangedListenerDelegate;
        this.f$1 = str;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.mo119x7c07b97c(this.f$1, this.f$2);
    }
}
