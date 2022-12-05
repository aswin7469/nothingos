package com.android.wm.shell.pip.phone;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Pair;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
/* loaded from: classes2.dex */
public class PipAppOpsListener {
    private AppOpsManager.OnOpChangedListener mAppOpsChangedListener = new AnonymousClass1();
    private AppOpsManager mAppOpsManager;
    private Callback mCallback;
    private Context mContext;
    private ShellExecutor mMainExecutor;

    /* loaded from: classes2.dex */
    public interface Callback {
        void dismissPip();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.wm.shell.pip.phone.PipAppOpsListener$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements AppOpsManager.OnOpChangedListener {
        AnonymousClass1() {
        }

        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(String str, String str2) {
            try {
                Pair<ComponentName, Integer> topPipActivity = PipUtils.getTopPipActivity(PipAppOpsListener.this.mContext);
                if (topPipActivity.first == null) {
                    return;
                }
                ApplicationInfo applicationInfoAsUser = PipAppOpsListener.this.mContext.getPackageManager().getApplicationInfoAsUser(str2, 0, ((Integer) topPipActivity.second).intValue());
                if (!applicationInfoAsUser.packageName.equals(((ComponentName) topPipActivity.first).getPackageName()) || PipAppOpsListener.this.mAppOpsManager.checkOpNoThrow(67, applicationInfoAsUser.uid, str2) == 0) {
                    return;
                }
                PipAppOpsListener.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipAppOpsListener$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PipAppOpsListener.AnonymousClass1.this.lambda$onOpChanged$0();
                    }
                });
            } catch (PackageManager.NameNotFoundException unused) {
                PipAppOpsListener.this.unregisterAppOpsListener();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onOpChanged$0() {
            PipAppOpsListener.this.mCallback.dismissPip();
        }
    }

    public PipAppOpsListener(Context context, Callback callback, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
        this.mCallback = callback;
    }

    public void onActivityPinned(String str) {
        registerAppOpsListener(str);
    }

    public void onActivityUnpinned() {
        unregisterAppOpsListener();
    }

    private void registerAppOpsListener(String str) {
        this.mAppOpsManager.startWatchingMode(67, str, this.mAppOpsChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterAppOpsListener() {
        this.mAppOpsManager.stopWatchingMode(this.mAppOpsChangedListener);
    }
}
