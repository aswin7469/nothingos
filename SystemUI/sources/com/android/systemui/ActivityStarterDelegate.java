package com.android.systemui;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ActivityStarterDelegate implements ActivityStarter {
    private Optional<Lazy<StatusBar>> mActualStarter;

    public ActivityStarterDelegate(Optional<Lazy<StatusBar>> optional) {
        this.mActualStarter = optional;
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startPendingIntentDismissingKeyguard$0(pendingIntent, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$0(PendingIntent pendingIntent, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent, final Runnable runnable) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startPendingIntentDismissingKeyguard$1(pendingIntent, runnable, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$1(PendingIntent pendingIntent, Runnable runnable, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent, runnable);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent, final Runnable runnable, final View view) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startPendingIntentDismissingKeyguard$2(pendingIntent, runnable, view, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$2(PendingIntent pendingIntent, Runnable runnable, View view, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent, runnable, view);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(final PendingIntent pendingIntent, final Runnable runnable, final ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startPendingIntentDismissingKeyguard$3(pendingIntent, runnable, controller, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$3(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent, runnable, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(final Intent intent, final boolean z, final boolean z2, final int i) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startActivity$4(intent, z, z2, i, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startActivity$4(Intent intent, boolean z, boolean z2, int i, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, z2, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startActivity$5(Intent intent, boolean z, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(final Intent intent, final boolean z) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startActivity$5(intent, z, (Lazy) obj);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(final Intent intent, final boolean z, final ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startActivity$6(intent, z, controller, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startActivity$6(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(final Intent intent, final boolean z, final boolean z2) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startActivity$7(intent, z, z2, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startActivity$7(Intent intent, boolean z, boolean z2, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, z2);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(final Intent intent, final boolean z, final ActivityStarter.Callback callback) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startActivity$8(intent, z, callback, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startActivity$8(Intent intent, boolean z, ActivityStarter.Callback callback, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, callback);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(final Intent intent, final int i) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$postStartActivityDismissingKeyguard$9(intent, i, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$9(Intent intent, int i, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(intent, i);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(final Intent intent, final int i, final ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$postStartActivityDismissingKeyguard$10(intent, i, controller, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$10(Intent intent, int i, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(intent, i, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(final PendingIntent pendingIntent) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$postStartActivityDismissingKeyguard$11(pendingIntent, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$11(PendingIntent pendingIntent, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(pendingIntent);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(final PendingIntent pendingIntent, final ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$postStartActivityDismissingKeyguard$12(pendingIntent, controller, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$12(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(pendingIntent, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postQSRunnableDismissingKeyguard(final Runnable runnable) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$postQSRunnableDismissingKeyguard$13(runnable, (Lazy) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postQSRunnableDismissingKeyguard$13(Runnable runnable, Lazy lazy) {
        ((StatusBar) lazy.get()).postQSRunnableDismissingKeyguard(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dismissKeyguardThenExecute$14(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, Lazy lazy) {
        ((StatusBar) lazy.get()).dismissKeyguardThenExecute(onDismissAction, runnable, z);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void dismissKeyguardThenExecute(final ActivityStarter.OnDismissAction onDismissAction, final Runnable runnable, final boolean z) {
        this.mActualStarter.ifPresent(new Consumer() { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$dismissKeyguardThenExecute$14(ActivityStarter.OnDismissAction.this, runnable, z, (Lazy) obj);
            }
        });
    }
}
