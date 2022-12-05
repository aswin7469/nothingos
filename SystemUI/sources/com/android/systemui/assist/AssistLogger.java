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
import java.util.Set;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: AssistLogger.kt */
/* loaded from: classes.dex */
public class AssistLogger {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final Set<AssistantSessionEvent> SESSION_END_EVENTS;
    @NotNull
    private final AssistUtils assistUtils;
    @NotNull
    private final Context context;
    @Nullable
    private InstanceId currentInstanceId;
    @NotNull
    private final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(1048576);
    @NotNull
    private final PhoneStateMonitor phoneStateMonitor;
    @NotNull
    private final UiEventLogger uiEventLogger;

    protected void reportAssistantInvocationExtraData() {
    }

    public AssistLogger(@NotNull Context context, @NotNull UiEventLogger uiEventLogger, @NotNull AssistUtils assistUtils, @NotNull PhoneStateMonitor phoneStateMonitor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        Intrinsics.checkNotNullParameter(assistUtils, "assistUtils");
        Intrinsics.checkNotNullParameter(phoneStateMonitor, "phoneStateMonitor");
        this.context = context;
        this.uiEventLogger = uiEventLogger;
        this.assistUtils = assistUtils;
        this.phoneStateMonitor = phoneStateMonitor;
    }

    public final void reportAssistantInvocationEventFromLegacy(int i, boolean z, @Nullable ComponentName componentName, @Nullable Integer num) {
        reportAssistantInvocationEvent(AssistantInvocationEvent.Companion.eventFromLegacyInvocationType(i, z), componentName, num == null ? null : Integer.valueOf(AssistantInvocationEvent.Companion.deviceStateFromLegacyDeviceState(num.intValue())));
    }

    public final void reportAssistantInvocationEvent(@NotNull UiEventLogger.UiEventEnum invocationEvent, @Nullable ComponentName componentName, @Nullable Integer num) {
        int intValue;
        Intrinsics.checkNotNullParameter(invocationEvent, "invocationEvent");
        if (componentName == null) {
            componentName = getAssistantComponentForCurrentUser();
        }
        int assistantUid = getAssistantUid(componentName);
        if (num != null) {
            intValue = num.intValue();
        } else {
            intValue = AssistantInvocationEvent.Companion.deviceStateFromLegacyDeviceState(this.phoneStateMonitor.getPhoneState());
        }
        FrameworkStatsLog.write(281, invocationEvent.getId(), assistantUid, componentName.flattenToString(), getOrCreateInstanceId().getId(), intValue, false);
        reportAssistantInvocationExtraData();
    }

    public final void reportAssistantSessionEvent(@NotNull UiEventLogger.UiEventEnum sessionEvent) {
        Intrinsics.checkNotNullParameter(sessionEvent, "sessionEvent");
        ComponentName assistantComponentForCurrentUser = getAssistantComponentForCurrentUser();
        this.uiEventLogger.logWithInstanceId(sessionEvent, getAssistantUid(assistantComponentForCurrentUser), assistantComponentForCurrentUser.flattenToString(), getOrCreateInstanceId());
        if (CollectionsKt.contains(SESSION_END_EVENTS, sessionEvent)) {
            clearInstanceId();
        }
    }

    @NotNull
    protected final InstanceId getOrCreateInstanceId() {
        InstanceId instanceId = this.currentInstanceId;
        if (instanceId == null) {
            instanceId = this.instanceIdSequence.newInstanceId();
        }
        this.currentInstanceId = instanceId;
        Intrinsics.checkNotNullExpressionValue(instanceId, "instanceId");
        return instanceId;
    }

    protected final void clearInstanceId() {
        this.currentInstanceId = null;
    }

    @NotNull
    protected final ComponentName getAssistantComponentForCurrentUser() {
        ComponentName assistComponentForUser = this.assistUtils.getAssistComponentForUser(KeyguardUpdateMonitor.getCurrentUser());
        Intrinsics.checkNotNullExpressionValue(assistComponentForUser, "assistUtils.getAssistComponentForUser(KeyguardUpdateMonitor.getCurrentUser())");
        return assistComponentForUser;
    }

    protected final int getAssistantUid(@NotNull ComponentName assistantComponent) {
        Intrinsics.checkNotNullParameter(assistantComponent, "assistantComponent");
        try {
            return this.context.getPackageManager().getApplicationInfo(assistantComponent.getPackageName(), 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AssistLogger", "Unable to find Assistant UID", e);
            return 0;
        }
    }

    /* compiled from: AssistLogger.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        Set<AssistantSessionEvent> of;
        of = SetsKt__SetsKt.setOf((Object[]) new AssistantSessionEvent[]{AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED, AssistantSessionEvent.ASSISTANT_SESSION_CLOSE});
        SESSION_END_EVENTS = of;
    }
}
