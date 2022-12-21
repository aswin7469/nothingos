package com.android.systemui.statusbar.phone.dagger;

import com.android.keyguard.LockIconViewController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutListContainerModule;
import com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks;
import com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterModule;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenterModule;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import dagger.Subcomponent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;
import javax.inject.Named;
import javax.inject.Scope;

@CentralSurfacesScope
@Subcomponent(modules = {CentralSurfacesStartableModule.class, NotificationStackScrollLayoutListContainerModule.class, StatusBarViewModule.class, StatusBarNotificationActivityStarterModule.class, StatusBarNotificationPresenterModule.class})
public interface CentralSurfacesComponent {

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CentralSurfacesScope {
    }

    @Subcomponent.Factory
    public interface Factory {
        CentralSurfacesComponent create();
    }

    public interface Startable {
        void start();

        void stop();
    }

    @Named("status_bar_fragment")
    CollapsedStatusBarFragment createCollapsedStatusBarFragment();

    AuthRippleController getAuthRippleController();

    NotificationRowBinderImpl.BindRowCallback getBindRowCallback();

    CentralSurfacesCommandQueueCallbacks getCentralSurfacesCommandQueueCallbacks();

    LargeScreenShadeHeaderController getLargeScreenShadeHeaderController();

    LockIconViewController getLockIconViewController();

    NotificationActivityStarter getNotificationActivityStarter();

    NotificationListContainer getNotificationListContainer();

    NotificationPanelViewController getNotificationPanelViewController();

    NotificationPresenter getNotificationPresenter();

    NotificationShadeWindowView getNotificationShadeWindowView();

    NotificationShadeWindowViewController getNotificationShadeWindowViewController();

    NotificationShelfController getNotificationShelfController();

    NotificationStackScrollLayoutController getNotificationStackScrollLayoutController();

    Set<Startable> getStartables();

    StatusBarHeadsUpChangeListener getStatusBarHeadsUpChangeListener();

    StatusBarInitializer getStatusBarInitializer();
}
