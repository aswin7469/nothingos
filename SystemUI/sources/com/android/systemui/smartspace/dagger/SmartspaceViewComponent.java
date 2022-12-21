package com.android.systemui.smartspace.dagger;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001:\u0002\u0004\u0005J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/smartspace/dagger/SmartspaceViewComponent;", "", "getView", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceView;", "Factory", "SmartspaceViewModule", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Subcomponent(modules = {SmartspaceViewModule.class})
/* compiled from: SmartspaceViewComponent.kt */
public interface SmartspaceViewComponent {

    @Subcomponent.Factory
    @Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J&\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00072\b\b\u0001\u0010\b\u001a\u00020\tH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/smartspace/dagger/SmartspaceViewComponent$Factory;", "", "create", "Lcom/android/systemui/smartspace/dagger/SmartspaceViewComponent;", "parent", "Landroid/view/ViewGroup;", "plugin", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin;", "onAttachListener", "Landroid/view/View$OnAttachStateChangeListener;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SmartspaceViewComponent.kt */
    public interface Factory {
        SmartspaceViewComponent create(@BindsInstance ViewGroup viewGroup, @BindsInstance @Named("plugin") BcSmartspaceDataPlugin bcSmartspaceDataPlugin, @BindsInstance View.OnAttachStateChangeListener onAttachStateChangeListener);
    }

    BcSmartspaceDataPlugin.SmartspaceView getView();

    @Module
    @Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J2\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0001\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/smartspace/dagger/SmartspaceViewComponent$SmartspaceViewModule;", "", "()V", "PLUGIN", "", "providesSmartspaceView", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceView;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "parent", "Landroid/view/ViewGroup;", "plugin", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin;", "onAttachListener", "Landroid/view/View$OnAttachStateChangeListener;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SmartspaceViewComponent.kt */
    public static final class SmartspaceViewModule {
        public static final SmartspaceViewModule INSTANCE = new SmartspaceViewModule();
        public static final String PLUGIN = "plugin";

        private SmartspaceViewModule() {
        }

        @Provides
        public final BcSmartspaceDataPlugin.SmartspaceView providesSmartspaceView(ActivityStarter activityStarter, FalsingManager falsingManager, ViewGroup viewGroup, @Named("plugin") BcSmartspaceDataPlugin bcSmartspaceDataPlugin, View.OnAttachStateChangeListener onAttachStateChangeListener) {
            Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
            Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
            Intrinsics.checkNotNullParameter(viewGroup, "parent");
            Intrinsics.checkNotNullParameter(bcSmartspaceDataPlugin, PLUGIN);
            Intrinsics.checkNotNullParameter(onAttachStateChangeListener, "onAttachListener");
            BcSmartspaceDataPlugin.SmartspaceView view = bcSmartspaceDataPlugin.getView(viewGroup);
            view.registerDataProvider(bcSmartspaceDataPlugin);
            view.setIntentStarter(new C2528xeb0a01ce(activityStarter));
            if (view != null) {
                ((View) view).addOnAttachStateChangeListener(onAttachStateChangeListener);
                view.setFalsingManager(falsingManager);
                return view;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.view.View");
        }
    }
}
