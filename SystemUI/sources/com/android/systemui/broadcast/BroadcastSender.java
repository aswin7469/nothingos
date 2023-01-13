package com.android.systemui.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.util.wakelock.WakeLock;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u0018\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\nJ\u0016\u0010\u0012\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014J \u0010\u0012\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0011\u001a\u0004\u0018\u00010\nJ*\u0010\u0012\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0011\u001a\u0004\u0018\u00010\n2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016J(\u0010\u0012\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0011\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0017\u001a\u00020\u0018J\u0016\u0010\u0019\u001a\u00020\r2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\r0\u001bH\u0002R\u000e\u0010\t\u001a\u00020\nXD¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nXD¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo65043d2 = {"Lcom/android/systemui/broadcast/BroadcastSender;", "", "context", "Landroid/content/Context;", "wakeLockBuilder", "Lcom/android/systemui/util/wakelock/WakeLock$Builder;", "bgExecutor", "Ljava/util/concurrent/Executor;", "(Landroid/content/Context;Lcom/android/systemui/util/wakelock/WakeLock$Builder;Ljava/util/concurrent/Executor;)V", "WAKE_LOCK_SEND_REASON", "", "WAKE_LOCK_TAG", "closeSystemDialogs", "", "sendBroadcast", "intent", "Landroid/content/Intent;", "receiverPermission", "sendBroadcastAsUser", "userHandle", "Landroid/os/UserHandle;", "options", "Landroid/os/Bundle;", "appOp", "", "sendInBackground", "callable", "Lkotlin/Function0;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: BroadcastSender.kt */
public final class BroadcastSender {
    private final String WAKE_LOCK_SEND_REASON = "sendInBackground";
    private final String WAKE_LOCK_TAG = "SysUI:BroadcastSender";
    private final Executor bgExecutor;
    /* access modifiers changed from: private */
    public final Context context;
    private final WakeLock.Builder wakeLockBuilder;

    @Inject
    public BroadcastSender(Context context2, WakeLock.Builder builder, @Background Executor executor) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(builder, "wakeLockBuilder");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        this.context = context2;
        this.wakeLockBuilder = builder;
        this.bgExecutor = executor;
    }

    public final void sendBroadcast(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        sendInBackground(new BroadcastSender$sendBroadcast$1(this, intent));
    }

    public final void sendBroadcast(Intent intent, String str) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        sendInBackground(new BroadcastSender$sendBroadcast$2(this, intent, str));
    }

    public final void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(userHandle, "userHandle");
        sendInBackground(new BroadcastSender$sendBroadcastAsUser$1(this, intent, userHandle));
    }

    public final void sendBroadcastAsUser(Intent intent, UserHandle userHandle, String str) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(userHandle, "userHandle");
        sendInBackground(new BroadcastSender$sendBroadcastAsUser$2(this, intent, userHandle, str));
    }

    public final void sendBroadcastAsUser(Intent intent, UserHandle userHandle, String str, Bundle bundle) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(userHandle, "userHandle");
        sendInBackground(new BroadcastSender$sendBroadcastAsUser$3(this, intent, userHandle, str, bundle));
    }

    public final void sendBroadcastAsUser(Intent intent, UserHandle userHandle, String str, int i) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(userHandle, "userHandle");
        sendInBackground(new BroadcastSender$sendBroadcastAsUser$4(this, intent, userHandle, str, i));
    }

    public final void closeSystemDialogs() {
        sendInBackground(new BroadcastSender$closeSystemDialogs$1(this));
    }

    private final void sendInBackground(Function0<Unit> function0) {
        WakeLock build = this.wakeLockBuilder.setTag(this.WAKE_LOCK_TAG).setMaxTimeout(5000).build();
        build.acquire(this.WAKE_LOCK_SEND_REASON);
        this.bgExecutor.execute(new BroadcastSender$$ExternalSyntheticLambda0(function0, build, this));
    }

    /* access modifiers changed from: private */
    /* renamed from: sendInBackground$lambda-0  reason: not valid java name */
    public static final void m2600sendInBackground$lambda0(Function0 function0, WakeLock wakeLock, BroadcastSender broadcastSender) {
        Intrinsics.checkNotNullParameter(function0, "$callable");
        Intrinsics.checkNotNullParameter(broadcastSender, "this$0");
        try {
            function0.invoke();
        } finally {
            wakeLock.release(broadcastSender.WAKE_LOCK_SEND_REASON);
        }
    }
}
