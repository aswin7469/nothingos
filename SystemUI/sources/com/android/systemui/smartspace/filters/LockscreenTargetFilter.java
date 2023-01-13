package com.android.systemui.smartspace.filters;

import android.app.smartspace.SmartspaceTarget;
import android.content.ContentResolver;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.UserHandle;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.smartspace.SmartspaceTargetFilter;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0002\u0015\u001f\u0018\u00002\u00020\u0001B;\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0011H\u0016J\u0010\u0010$\u001a\u00020\u00182\u0006\u0010%\u001a\u00020&H\u0016J\n\u0010'\u001a\u0004\u0018\u00010\u0013H\u0002J\u0010\u0010(\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0011H\u0016J\b\u0010)\u001a\u00020\"H\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0004\n\u0002\u0010\u0016R\u001e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0017\u001a\u00020\u0018@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u001a\u0010\u001bR\u001e\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u0017\u001a\u00020\u0018@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u001d\u0010\u001bR\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u00020\u001fX\u0004¢\u0006\u0004\n\u0002\u0010 ¨\u0006*"}, mo65043d2 = {"Lcom/android/systemui/smartspace/filters/LockscreenTargetFilter;", "Lcom/android/systemui/smartspace/SmartspaceTargetFilter;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "execution", "Lcom/android/systemui/util/concurrency/Execution;", "handler", "Landroid/os/Handler;", "contentResolver", "Landroid/content/ContentResolver;", "uiExecutor", "Ljava/util/concurrent/Executor;", "(Lcom/android/systemui/util/settings/SecureSettings;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/util/concurrency/Execution;Landroid/os/Handler;Landroid/content/ContentResolver;Ljava/util/concurrent/Executor;)V", "listeners", "", "Lcom/android/systemui/smartspace/SmartspaceTargetFilter$Listener;", "managedUserHandle", "Landroid/os/UserHandle;", "settingsObserver", "com/android/systemui/smartspace/filters/LockscreenTargetFilter$settingsObserver$1", "Lcom/android/systemui/smartspace/filters/LockscreenTargetFilter$settingsObserver$1;", "value", "", "showSensitiveContentForCurrentUser", "setShowSensitiveContentForCurrentUser", "(Z)V", "showSensitiveContentForManagedUser", "setShowSensitiveContentForManagedUser", "userTrackerCallback", "com/android/systemui/smartspace/filters/LockscreenTargetFilter$userTrackerCallback$1", "Lcom/android/systemui/smartspace/filters/LockscreenTargetFilter$userTrackerCallback$1;", "addListener", "", "listener", "filterSmartspaceTarget", "t", "Landroid/app/smartspace/SmartspaceTarget;", "getWorkProfileUser", "removeListener", "updateUserContentSettings", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenTargetFilter.kt */
public final class LockscreenTargetFilter implements SmartspaceTargetFilter {
    private final ContentResolver contentResolver;
    /* access modifiers changed from: private */
    public final Execution execution;
    private final Handler handler;
    private Set<SmartspaceTargetFilter.Listener> listeners = new LinkedHashSet();
    private UserHandle managedUserHandle;
    private final SecureSettings secureSettings;
    private final LockscreenTargetFilter$settingsObserver$1 settingsObserver;
    private boolean showSensitiveContentForCurrentUser;
    private boolean showSensitiveContentForManagedUser;
    private final Executor uiExecutor;
    private final UserTracker userTracker;
    private final LockscreenTargetFilter$userTrackerCallback$1 userTrackerCallback;

    @Inject
    public LockscreenTargetFilter(SecureSettings secureSettings2, UserTracker userTracker2, Execution execution2, @Main Handler handler2, ContentResolver contentResolver2, @Main Executor executor) {
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(execution2, "execution");
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(contentResolver2, "contentResolver");
        Intrinsics.checkNotNullParameter(executor, "uiExecutor");
        this.secureSettings = secureSettings2;
        this.userTracker = userTracker2;
        this.execution = execution2;
        this.handler = handler2;
        this.contentResolver = contentResolver2;
        this.uiExecutor = executor;
        this.settingsObserver = new LockscreenTargetFilter$settingsObserver$1(this, handler2);
        this.userTrackerCallback = new LockscreenTargetFilter$userTrackerCallback$1(this);
    }

    private final void setShowSensitiveContentForCurrentUser(boolean z) {
        boolean z2 = this.showSensitiveContentForCurrentUser;
        this.showSensitiveContentForCurrentUser = z;
        if (z2 != z) {
            for (SmartspaceTargetFilter.Listener onCriteriaChanged : this.listeners) {
                onCriteriaChanged.onCriteriaChanged();
            }
        }
    }

    private final void setShowSensitiveContentForManagedUser(boolean z) {
        boolean z2 = this.showSensitiveContentForManagedUser;
        this.showSensitiveContentForManagedUser = z;
        if (z2 != z) {
            for (SmartspaceTargetFilter.Listener onCriteriaChanged : this.listeners) {
                onCriteriaChanged.onCriteriaChanged();
            }
        }
    }

    public void addListener(SmartspaceTargetFilter.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(listener);
        if (this.listeners.size() == 1) {
            this.userTracker.addCallback(this.userTrackerCallback, this.uiExecutor);
            this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_allow_private_notifications"), true, this.settingsObserver, -1);
            updateUserContentSettings();
        }
    }

    public void removeListener(SmartspaceTargetFilter.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.remove(listener);
        if (!(!this.listeners.isEmpty())) {
            this.userTracker.removeCallback(this.userTrackerCallback);
            this.contentResolver.unregisterContentObserver(this.settingsObserver);
        }
    }

    public boolean filterSmartspaceTarget(SmartspaceTarget smartspaceTarget) {
        Intrinsics.checkNotNullParameter(smartspaceTarget, "t");
        UserHandle userHandle = smartspaceTarget.getUserHandle();
        if (Intrinsics.areEqual((Object) userHandle, (Object) this.userTracker.getUserHandle())) {
            if (!smartspaceTarget.isSensitive() || this.showSensitiveContentForCurrentUser) {
                return true;
            }
        } else if (Intrinsics.areEqual((Object) userHandle, (Object) this.managedUserHandle) && this.userTracker.getUserHandle().getIdentifier() == 0 && (!smartspaceTarget.isSensitive() || this.showSensitiveContentForManagedUser)) {
            return true;
        }
        return false;
    }

    private final UserHandle getWorkProfileUser() {
        for (UserInfo next : this.userTracker.getUserProfiles()) {
            if (next.isManagedProfile()) {
                return next.getUserHandle();
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public final void updateUserContentSettings() {
        boolean z = false;
        setShowSensitiveContentForCurrentUser(this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, this.userTracker.getUserId()) == 1);
        UserHandle workProfileUser = getWorkProfileUser();
        this.managedUserHandle = workProfileUser;
        Integer valueOf = workProfileUser != null ? Integer.valueOf(workProfileUser.getIdentifier()) : null;
        if (valueOf != null) {
            if (this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, valueOf.intValue()) == 1) {
                z = true;
            }
            setShowSensitiveContentForManagedUser(z);
        }
        for (SmartspaceTargetFilter.Listener onCriteriaChanged : this.listeners) {
            onCriteriaChanged.onCriteriaChanged();
        }
    }
}
