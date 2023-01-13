package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import java.p026io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\b&\u0018\u00002\u00020\u00012\u00020\u0002B%\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0010J#\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\u000e\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u001a¢\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u0014J\u000e\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u0004J\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u00042\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\u0004J\u000e\u0010#\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u0004J\u0016\u0010$\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00102\u0006\u0010%\u001a\u00020&J\u0010\u0010'\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0010H\u0016J\u0010\u0010(\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0010H\u0016J\u0010\u0010)\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u0010H&J\u000e\u0010*\u001a\u00020\u00142\u0006\u0010+\u001a\u00020\fJ\b\u0010,\u001a\u00020\u0014H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R \u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00100\u000fX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006-"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/notifcollection/SelfTrackingLifetimeExtender;", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender;", "Lcom/android/systemui/Dumpable;", "tag", "", "name", "debug", "", "mainHandler", "Landroid/os/Handler;", "(Ljava/lang/String;Ljava/lang/String;ZLandroid/os/Handler;)V", "mCallback", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender$OnEndLifetimeExtensionCallback;", "mEnding", "mEntriesExtended", "Landroid/util/ArrayMap;", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getMEntriesExtended", "()Landroid/util/ArrayMap;", "cancelLifetimeExtension", "", "entry", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "endAllLifetimeExtensions", "endLifetimeExtension", "key", "endLifetimeExtensionAfterDelay", "delayMillis", "", "getName", "isExtending", "maybeExtendLifetime", "reason", "", "onCanceledLifetimeExtension", "onStartedLifetimeExtension", "queryShouldExtendLifetime", "setCallback", "callback", "warnIfEnding", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SelfTrackingLifetimeExtender.kt */
public abstract class SelfTrackingLifetimeExtender implements NotifLifetimeExtender, Dumpable {
    private final boolean debug;
    private NotifLifetimeExtender.OnEndLifetimeExtensionCallback mCallback;
    private boolean mEnding;
    private final ArrayMap<String, NotificationEntry> mEntriesExtended = new ArrayMap<>();
    private final Handler mainHandler;
    private final String name;
    private final String tag;

    public void onCanceledLifetimeExtension(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
    }

    public void onStartedLifetimeExtension(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
    }

    public abstract boolean queryShouldExtendLifetime(NotificationEntry notificationEntry);

    public SelfTrackingLifetimeExtender(String str, String str2, boolean z, Handler handler) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(str2, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        this.tag = str;
        this.name = str2;
        this.debug = z;
        this.mainHandler = handler;
    }

    /* access modifiers changed from: protected */
    public final ArrayMap<String, NotificationEntry> getMEntriesExtended() {
        return this.mEntriesExtended;
    }

    private final void warnIfEnding() {
        if (this.debug && this.mEnding) {
            Log.w(this.tag, "reentrant code while ending a lifetime extension");
        }
    }

    public final void endAllLifetimeExtensions() {
        Collection<NotificationEntry> values = this.mEntriesExtended.values();
        Intrinsics.checkNotNullExpressionValue(values, "mEntriesExtended.values");
        List<NotificationEntry> list = CollectionsKt.toList(values);
        if (this.debug) {
            Log.d(this.tag, this.name + ".endAllLifetimeExtensions() entries=" + list);
        }
        this.mEntriesExtended.clear();
        warnIfEnding();
        this.mEnding = true;
        for (NotificationEntry notificationEntry : list) {
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = this.mCallback;
            if (onEndLifetimeExtensionCallback == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCallback");
                onEndLifetimeExtensionCallback = null;
            }
            onEndLifetimeExtensionCallback.onEndLifetimeExtension(this, notificationEntry);
        }
        this.mEnding = false;
    }

    public final void endLifetimeExtensionAfterDelay(String str, long j) {
        Intrinsics.checkNotNullParameter(str, "key");
        if (this.debug) {
            Log.d(this.tag, this.name + ".endLifetimeExtensionAfterDelay(key=" + str + ", delayMillis=" + j + ") isExtending=" + isExtending(str));
        }
        if (isExtending(str)) {
            this.mainHandler.postDelayed(new SelfTrackingLifetimeExtender$$ExternalSyntheticLambda0(this, str), j);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: endLifetimeExtensionAfterDelay$lambda-1  reason: not valid java name */
    public static final void m3121endLifetimeExtensionAfterDelay$lambda1(SelfTrackingLifetimeExtender selfTrackingLifetimeExtender, String str) {
        Intrinsics.checkNotNullParameter(selfTrackingLifetimeExtender, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        selfTrackingLifetimeExtender.endLifetimeExtension(str);
    }

    public final void endLifetimeExtension(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        if (this.debug) {
            Log.d(this.tag, this.name + ".endLifetimeExtension(key=" + str + ") isExtending=" + isExtending(str));
        }
        warnIfEnding();
        this.mEnding = true;
        NotificationEntry remove = this.mEntriesExtended.remove(str);
        if (remove != null) {
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = this.mCallback;
            if (onEndLifetimeExtensionCallback == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCallback");
                onEndLifetimeExtensionCallback = null;
            }
            onEndLifetimeExtensionCallback.onEndLifetimeExtension(this, remove);
        }
        this.mEnding = false;
    }

    public final boolean isExtending(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        return this.mEntriesExtended.containsKey(str);
    }

    public final String getName() {
        return this.name;
    }

    public final boolean maybeExtendLifetime(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        boolean queryShouldExtendLifetime = queryShouldExtendLifetime(notificationEntry);
        if (this.debug) {
            String str = this.tag;
            StringBuilder append = new StringBuilder().append(this.name).append(".shouldExtendLifetime(key=").append(notificationEntry.getKey()).append(", reason=").append(i).append(") isExtending=");
            String key = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            Log.d(str, append.append(isExtending(key)).append(" shouldExtend=").append(queryShouldExtendLifetime).toString());
        }
        warnIfEnding();
        if (queryShouldExtendLifetime && this.mEntriesExtended.put(notificationEntry.getKey(), notificationEntry) == null) {
            onStartedLifetimeExtension(notificationEntry);
        }
        return queryShouldExtendLifetime;
    }

    public final void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (this.debug) {
            String str = this.tag;
            StringBuilder append = new StringBuilder().append(this.name).append(".cancelLifetimeExtension(key=").append(notificationEntry.getKey()).append(") isExtending=");
            String key = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            Log.d(str, append.append(isExtending(key)).toString());
        }
        warnIfEnding();
        this.mEntriesExtended.remove(notificationEntry.getKey());
        onCanceledLifetimeExtension(notificationEntry);
    }

    public final void setCallback(NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback) {
        Intrinsics.checkNotNullParameter(onEndLifetimeExtensionCallback, "callback");
        this.mCallback = onEndLifetimeExtensionCallback;
    }

    public final void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("LifetimeExtender: " + this.name + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        printWriter.println("  mEntriesExtended: " + this.mEntriesExtended.size());
        for (Map.Entry key : this.mEntriesExtended.entrySet()) {
            printWriter.println("  * " + ((String) key.getKey()));
        }
    }
}
