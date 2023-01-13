package com.android.systemui.assist;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dagger.SysUISingleton;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0017\u0018\u0000 )2\u00020\u0001:\u0001)B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010\u0013\u001a\u00020\u0014H\u0004J\b\u0010\u0015\u001a\u00020\u0016H\u0004J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0016H\u0004J\b\u0010\u001a\u001a\u00020\u000eH\u0004J+\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001d2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u0018¢\u0006\u0002\u0010\u001fJ3\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00182\u0006\u0010\"\u001a\u00020#2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u0018¢\u0006\u0002\u0010%J\b\u0010&\u001a\u00020\u0014H\u0014J\u000e\u0010'\u001a\u00020\u00142\u0006\u0010(\u001a\u00020\u001dR\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006*"}, mo65043d2 = {"Lcom/android/systemui/assist/AssistLogger;", "", "context", "Landroid/content/Context;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "assistUtils", "Lcom/android/internal/app/AssistUtils;", "phoneStateMonitor", "Lcom/android/systemui/assist/PhoneStateMonitor;", "(Landroid/content/Context;Lcom/android/internal/logging/UiEventLogger;Lcom/android/internal/app/AssistUtils;Lcom/android/systemui/assist/PhoneStateMonitor;)V", "getContext", "()Landroid/content/Context;", "currentInstanceId", "Lcom/android/internal/logging/InstanceId;", "instanceIdSequence", "Lcom/android/internal/logging/InstanceIdSequence;", "getUiEventLogger", "()Lcom/android/internal/logging/UiEventLogger;", "clearInstanceId", "", "getAssistantComponentForCurrentUser", "Landroid/content/ComponentName;", "getAssistantUid", "", "assistantComponent", "getOrCreateInstanceId", "reportAssistantInvocationEvent", "invocationEvent", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "deviceState", "(Lcom/android/internal/logging/UiEventLogger$UiEventEnum;Landroid/content/ComponentName;Ljava/lang/Integer;)V", "reportAssistantInvocationEventFromLegacy", "legacyInvocationType", "isInvocationComplete", "", "legacyDeviceState", "(IZLandroid/content/ComponentName;Ljava/lang/Integer;)V", "reportAssistantInvocationExtraData", "reportAssistantSessionEvent", "sessionEvent", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AssistLogger.kt */
public class AssistLogger {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int INSTANCE_ID_MAX = 1048576;
    private static final Set<AssistantSessionEvent> SESSION_END_EVENTS = SetsKt.setOf(AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED, AssistantSessionEvent.ASSISTANT_SESSION_CLOSE);
    protected static final String TAG = "AssistLogger";
    private final AssistUtils assistUtils;
    private final Context context;
    private InstanceId currentInstanceId;
    private final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(1048576);
    private final PhoneStateMonitor phoneStateMonitor;
    private final UiEventLogger uiEventLogger;

    /* access modifiers changed from: protected */
    public void reportAssistantInvocationExtraData() {
    }

    @Inject
    public AssistLogger(Context context2, UiEventLogger uiEventLogger2, AssistUtils assistUtils2, PhoneStateMonitor phoneStateMonitor2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        Intrinsics.checkNotNullParameter(assistUtils2, "assistUtils");
        Intrinsics.checkNotNullParameter(phoneStateMonitor2, "phoneStateMonitor");
        this.context = context2;
        this.uiEventLogger = uiEventLogger2;
        this.assistUtils = assistUtils2;
        this.phoneStateMonitor = phoneStateMonitor2;
    }

    /* access modifiers changed from: protected */
    public final Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: protected */
    public final UiEventLogger getUiEventLogger() {
        return this.uiEventLogger;
    }

    public static /* synthetic */ void reportAssistantInvocationEventFromLegacy$default(AssistLogger assistLogger, int i, boolean z, ComponentName componentName, Integer num, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 4) != 0) {
                componentName = null;
            }
            if ((i2 & 8) != 0) {
                num = null;
            }
            assistLogger.reportAssistantInvocationEventFromLegacy(i, z, componentName, num);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: reportAssistantInvocationEventFromLegacy");
    }

    public final void reportAssistantInvocationEventFromLegacy(int i, boolean z, ComponentName componentName, Integer num) {
        Integer num2;
        if (num == null) {
            num2 = null;
            Integer num3 = null;
        } else {
            num2 = Integer.valueOf(AssistantInvocationEvent.Companion.deviceStateFromLegacyDeviceState(num.intValue()));
        }
        reportAssistantInvocationEvent(AssistantInvocationEvent.Companion.eventFromLegacyInvocationType(i, z), componentName, num2);
    }

    public static /* synthetic */ void reportAssistantInvocationEvent$default(AssistLogger assistLogger, UiEventLogger.UiEventEnum uiEventEnum, ComponentName componentName, Integer num, int i, Object obj) {
        if (obj == null) {
            if ((i & 2) != 0) {
                componentName = null;
            }
            if ((i & 4) != 0) {
                num = null;
            }
            assistLogger.reportAssistantInvocationEvent(uiEventEnum, componentName, num);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: reportAssistantInvocationEvent");
    }

    public final void reportAssistantInvocationEvent(UiEventLogger.UiEventEnum uiEventEnum, ComponentName componentName, Integer num) {
        int i;
        Intrinsics.checkNotNullParameter(uiEventEnum, "invocationEvent");
        if (componentName == null) {
            componentName = getAssistantComponentForCurrentUser();
        }
        int assistantUid = getAssistantUid(componentName);
        if (num != null) {
            i = num.intValue();
        } else {
            i = AssistantInvocationEvent.Companion.deviceStateFromLegacyDeviceState(this.phoneStateMonitor.getPhoneState());
        }
        FrameworkStatsLog.write(281, uiEventEnum.getId(), assistantUid, componentName.flattenToString(), getOrCreateInstanceId().getId(), i, false);
        reportAssistantInvocationExtraData();
    }

    public final void reportAssistantSessionEvent(UiEventLogger.UiEventEnum uiEventEnum) {
        Intrinsics.checkNotNullParameter(uiEventEnum, "sessionEvent");
        ComponentName assistantComponentForCurrentUser = getAssistantComponentForCurrentUser();
        this.uiEventLogger.logWithInstanceId(uiEventEnum, getAssistantUid(assistantComponentForCurrentUser), assistantComponentForCurrentUser.flattenToString(), getOrCreateInstanceId());
        if (CollectionsKt.contains(SESSION_END_EVENTS, uiEventEnum)) {
            clearInstanceId();
        }
    }

    /* access modifiers changed from: protected */
    public final InstanceId getOrCreateInstanceId() {
        InstanceId instanceId = this.currentInstanceId;
        if (instanceId == null) {
            instanceId = this.instanceIdSequence.newInstanceId();
        }
        this.currentInstanceId = instanceId;
        Intrinsics.checkNotNullExpressionValue(instanceId, "instanceId");
        return instanceId;
    }

    /* access modifiers changed from: protected */
    public final void clearInstanceId() {
        this.currentInstanceId = null;
    }

    /* access modifiers changed from: protected */
    public final ComponentName getAssistantComponentForCurrentUser() {
        ComponentName assistComponentForUser = this.assistUtils.getAssistComponentForUser(KeyguardUpdateMonitor.getCurrentUser());
        Intrinsics.checkNotNullExpressionValue(assistComponentForUser, "assistUtils.getAssistCom…Monitor.getCurrentUser())");
        return assistComponentForUser;
    }

    /* access modifiers changed from: protected */
    public final int getAssistantUid(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "assistantComponent");
        try {
            return this.context.getPackageManager().getApplicationInfo(componentName.getPackageName(), 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to find Assistant UID", e);
            return 0;
        }
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/assist/AssistLogger$Companion;", "", "()V", "INSTANCE_ID_MAX", "", "SESSION_END_EVENTS", "", "Lcom/android/systemui/assist/AssistantSessionEvent;", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AssistLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
