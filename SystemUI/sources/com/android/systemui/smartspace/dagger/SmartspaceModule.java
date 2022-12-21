package com.android.systemui.smartspace.dagger;

import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.smartspace.SmartspacePrecondition;
import com.android.systemui.smartspace.SmartspaceTargetFilter;
import com.android.systemui.smartspace.filters.LockscreenTargetFilter;
import com.android.systemui.smartspace.preconditions.LockscreenPrecondition;
import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.Module;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0005¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H'J\n\u0010\u0007\u001a\u0004\u0018\u00010\bH'J\n\u0010\t\u001a\u0004\u0018\u00010\nH'J\u0014\u0010\u000b\u001a\u0004\u0018\u00010\b2\b\u0010\f\u001a\u0004\u0018\u00010\rH'¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/smartspace/dagger/SmartspaceModule;", "", "()V", "bindSmartspacePrecondition", "Lcom/android/systemui/smartspace/SmartspacePrecondition;", "lockscreenPrecondition", "Lcom/android/systemui/smartspace/preconditions/LockscreenPrecondition;", "optionalDreamSmartspaceTargetFilter", "Lcom/android/systemui/smartspace/SmartspaceTargetFilter;", "optionalDreamsBcSmartspaceDataPlugin", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin;", "provideLockscreenSmartspaceTargetFilter", "filter", "Lcom/android/systemui/smartspace/filters/LockscreenTargetFilter;", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module(subcomponents = {SmartspaceViewComponent.class})
/* compiled from: SmartspaceModule.kt */
public abstract class SmartspaceModule {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String DREAM_SMARTSPACE_DATA_PLUGIN = "dreams_smartspace_data_plugin";
    public static final String DREAM_SMARTSPACE_PRECONDITION = "dream_smartspace_precondition";
    public static final String DREAM_SMARTSPACE_TARGET_FILTER = "dream_smartspace_target_filter";
    public static final String LOCKSCREEN_SMARTSPACE_TARGET_FILTER = "lockscreen_smartspace_target_filter";

    @Binds
    @Named("dream_smartspace_precondition")
    public abstract SmartspacePrecondition bindSmartspacePrecondition(LockscreenPrecondition lockscreenPrecondition);

    @BindsOptionalOf
    @Named("dream_smartspace_target_filter")
    public abstract SmartspaceTargetFilter optionalDreamSmartspaceTargetFilter();

    @BindsOptionalOf
    @Named("dreams_smartspace_data_plugin")
    public abstract BcSmartspaceDataPlugin optionalDreamsBcSmartspaceDataPlugin();

    @Binds
    @Named("lockscreen_smartspace_target_filter")
    public abstract SmartspaceTargetFilter provideLockscreenSmartspaceTargetFilter(LockscreenTargetFilter lockscreenTargetFilter);

    @Module
    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/smartspace/dagger/SmartspaceModule$Companion;", "", "()V", "DREAM_SMARTSPACE_DATA_PLUGIN", "", "DREAM_SMARTSPACE_PRECONDITION", "DREAM_SMARTSPACE_TARGET_FILTER", "LOCKSCREEN_SMARTSPACE_TARGET_FILTER", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SmartspaceModule.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
