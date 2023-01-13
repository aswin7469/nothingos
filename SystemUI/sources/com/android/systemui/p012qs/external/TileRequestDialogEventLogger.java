package com.android.systemui.p012qs.external;

import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u0017\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u001e\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u0012\u001a\u00020\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileRequestDialogEventLogger;", "", "()V", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "instanceIdSequence", "Lcom/android/internal/logging/InstanceIdSequence;", "(Lcom/android/internal/logging/UiEventLogger;Lcom/android/internal/logging/InstanceIdSequence;)V", "logDialogShown", "", "packageName", "", "instanceId", "Lcom/android/internal/logging/InstanceId;", "logTileAlreadyAdded", "logUserResponse", "response", "", "newInstanceId", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.external.TileRequestDialogEventLogger */
/* compiled from: TileRequestDialogEventLogger.kt */
public final class TileRequestDialogEventLogger {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int MAX_INSTANCE_ID = 1048576;
    private final InstanceIdSequence instanceIdSequence;
    private final UiEventLogger uiEventLogger;

    public TileRequestDialogEventLogger(UiEventLogger uiEventLogger2, InstanceIdSequence instanceIdSequence2) {
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        Intrinsics.checkNotNullParameter(instanceIdSequence2, "instanceIdSequence");
        this.uiEventLogger = uiEventLogger2;
        this.instanceIdSequence = instanceIdSequence2;
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileRequestDialogEventLogger$Companion;", "", "()V", "MAX_INSTANCE_ID", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.external.TileRequestDialogEventLogger$Companion */
    /* compiled from: TileRequestDialogEventLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public TileRequestDialogEventLogger() {
        this(new UiEventLoggerImpl(), new InstanceIdSequence(1048576));
    }

    public final InstanceId newInstanceId() {
        InstanceId newInstanceId = this.instanceIdSequence.newInstanceId();
        Intrinsics.checkNotNullExpressionValue(newInstanceId, "instanceIdSequence.newInstanceId()");
        return newInstanceId;
    }

    public final void logDialogShown(String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.uiEventLogger.logWithInstanceId(TileRequestDialogEvent.TILE_REQUEST_DIALOG_SHOWN, 0, str, instanceId);
    }

    public final void logUserResponse(int i, String str, InstanceId instanceId) {
        TileRequestDialogEvent tileRequestDialogEvent;
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        if (i == 0) {
            tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_NOT_ADDED;
        } else if (i == 2) {
            tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_ADDED;
        } else if (i == 3) {
            tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_DISMISSED;
        } else {
            throw new IllegalArgumentException("User response not valid: " + i);
        }
        this.uiEventLogger.logWithInstanceId((UiEventLogger.UiEventEnum) tileRequestDialogEvent, 0, str, instanceId);
    }

    public final void logTileAlreadyAdded(String str, InstanceId instanceId) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId, "instanceId");
        this.uiEventLogger.logWithInstanceId(TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_ALREADY_ADDED, 0, str, instanceId);
    }
}
