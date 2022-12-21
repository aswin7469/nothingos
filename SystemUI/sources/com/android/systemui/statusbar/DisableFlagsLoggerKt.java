package com.android.systemui.statusbar;

import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.DisableFlagsLogger;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0004"}, mo64987d2 = {"defaultDisable1FlagsList", "", "Lcom/android/systemui/statusbar/DisableFlagsLogger$DisableFlag;", "defaultDisable2FlagsList", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DisableFlagsLogger.kt */
public final class DisableFlagsLoggerKt {
    /* access modifiers changed from: private */
    public static final List<DisableFlagsLogger.DisableFlag> defaultDisable1FlagsList = CollectionsKt.listOf(new DisableFlagsLogger.DisableFlag(65536, 'E', 'e'), new DisableFlagsLogger.DisableFlag(131072, 'N', 'n'), new DisableFlagsLogger.DisableFlag(262144, 'A', 'a'), new DisableFlagsLogger.DisableFlag(1048576, 'I', 'i'), new DisableFlagsLogger.DisableFlag(2097152, 'H', 'h'), new DisableFlagsLogger.DisableFlag(4194304, 'B', 'b'), new DisableFlagsLogger.DisableFlag(8388608, 'C', 'c'), new DisableFlagsLogger.DisableFlag(16777216, 'R', 'r'), new DisableFlagsLogger.DisableFlag(QuickStepContract.SYSUI_STATE_VOICE_INTERACTION_WINDOW_SHOWING, 'S', 's'), new DisableFlagsLogger.DisableFlag(67108864, 'O', 'o'));
    /* access modifiers changed from: private */
    public static final List<DisableFlagsLogger.DisableFlag> defaultDisable2FlagsList = CollectionsKt.listOf(new DisableFlagsLogger.DisableFlag(1, 'Q', 'q'), new DisableFlagsLogger.DisableFlag(2, 'I', 'i'), new DisableFlagsLogger.DisableFlag(4, 'N', 'n'), new DisableFlagsLogger.DisableFlag(8, 'G', 'g'), new DisableFlagsLogger.DisableFlag(16, 'R', 'r'));
}
