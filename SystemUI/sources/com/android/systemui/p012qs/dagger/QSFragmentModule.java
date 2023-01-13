package com.android.systemui.p012qs.dagger;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.C1894R;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.qualifiers.RootView;
import com.android.systemui.p012qs.FooterActionsView;
import com.android.systemui.p012qs.QSContainerImpl;
import com.android.systemui.p012qs.QSFooter;
import com.android.systemui.p012qs.QSFooterView;
import com.android.systemui.p012qs.QSFooterViewController;
import com.android.systemui.p012qs.QSFragment;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.QuickQSPanel;
import com.android.systemui.p012qs.QuickStatusBarHeader;
import com.android.systemui.p012qs.customize.QSCustomizer;
import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import com.nothing.systemui.p024qs.NTQSStatusBar;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule */
public interface QSFragmentModule {
    public static final String NT_QS_STATUS_BAR_BATTERY_METER_VIEW = "nt_qs_header_battery_meter_view";
    public static final String NT_QS_STATUS_BAR_BATTERY_VIEW_CONTROLLER = "nt_qs_header_battery_controller";
    public static final String QS_FGS_MANAGER_FOOTER_VIEW = "qs_fgs_manager_footer";
    public static final String QS_SECURITY_FOOTER_VIEW = "qs_security_footer";
    public static final String QS_USING_COLLAPSED_LANDSCAPE_MEDIA = "qs_using_collapsed_landscape_media";
    public static final String QS_USING_MEDIA_PLAYER = "qs_using_media_player";

    @Binds
    C2304QS bindQS(QSFragment qSFragment);

    @QSThemedContext
    @Provides
    static Context provideThemedContext(@RootView View view) {
        return view.getContext();
    }

    @QSThemedContext
    @Provides
    static LayoutInflater provideThemedLayoutInflater(@QSThemedContext Context context) {
        return LayoutInflater.from(context);
    }

    @RootView
    @Provides
    static View provideRootView(QSFragment qSFragment) {
        return qSFragment.getView();
    }

    @Provides
    static QSPanel provideQSPanel(@RootView View view) {
        return (QSPanel) view.findViewById(C1894R.C1898id.quick_settings_panel);
    }

    @Provides
    static QSContainerImpl providesQSContainerImpl(@RootView View view) {
        return (QSContainerImpl) view.findViewById(C1894R.C1898id.quick_settings_container);
    }

    @Provides
    static QuickStatusBarHeader providesQuickStatusBarHeader(@RootView View view) {
        return (QuickStatusBarHeader) view.findViewById(C1894R.C1898id.header);
    }

    @Provides
    static QuickQSPanel providesQuickQSPanel(QuickStatusBarHeader quickStatusBarHeader) {
        return (QuickQSPanel) quickStatusBarHeader.findViewById(C1894R.C1898id.quick_qs_panel);
    }

    @Provides
    static BatteryMeterView providesBatteryMeterView(QuickStatusBarHeader quickStatusBarHeader) {
        return (BatteryMeterView) quickStatusBarHeader.findViewById(C1894R.C1898id.batteryRemainingIcon);
    }

    @Provides
    static QSFooterView providesQSFooterView(@RootView View view) {
        return (QSFooterView) view.findViewById(C1894R.C1898id.qs_footer);
    }

    @Provides
    static FooterActionsView providesQSFooterActionsView(@RootView View view) {
        return (FooterActionsView) view.findViewById(C1894R.C1898id.qs_footer_actions);
    }

    @QSScope
    @Provides
    static QSFooter providesQSFooter(QSFooterViewController qSFooterViewController) {
        qSFooterViewController.init();
        return qSFooterViewController;
    }

    @QSScope
    @Provides
    static QSCustomizer providesQSCutomizer(@RootView View view) {
        return (QSCustomizer) view.findViewById(C1894R.C1898id.qs_customize);
    }

    @QSScope
    @Provides
    @Named("qs_security_footer")
    static View providesQSSecurityFooterView(@QSThemedContext LayoutInflater layoutInflater, FooterActionsView footerActionsView) {
        return layoutInflater.inflate(C1894R.layout.quick_settings_security_footer, footerActionsView, false);
    }

    @Provides
    @Named("qs_using_media_player")
    static boolean providesQSUsingMediaPlayer(Context context) {
        return Utils.useQsMediaPlayer(context);
    }

    @Provides
    @Named("qs_using_collapsed_landscape_media")
    static boolean providesQSUsingCollapsedLandscapeMedia(Context context) {
        return Utils.useCollapsedMediaInLandscape(context.getResources());
    }

    @QSScope
    @Provides
    static OngoingPrivacyChip providesPrivacyChip(QuickStatusBarHeader quickStatusBarHeader) {
        return (OngoingPrivacyChip) quickStatusBarHeader.findViewById(C1894R.C1898id.privacy_chip);
    }

    @QSScope
    @Provides
    static StatusIconContainer providesStatusIconContainer(QuickStatusBarHeader quickStatusBarHeader) {
        return (StatusIconContainer) quickStatusBarHeader.findViewById(C1894R.C1898id.statusIcons);
    }

    @QSScope
    @Provides
    @Named("qs_fgs_manager_footer")
    static View providesQSFgsManagerFooterView(@QSThemedContext LayoutInflater layoutInflater, FooterActionsView footerActionsView) {
        return layoutInflater.inflate(C1894R.layout.fgs_footer, footerActionsView, false);
    }

    @Provides
    static NTQSStatusBar providesNTQSStatusBar(@RootView View view) {
        return (NTQSStatusBar) view.findViewById(C1894R.C1898id.qs_status_bar_layout);
    }

    @Provides
    @Named("nt_qs_header_battery_meter_view")
    static BatteryMeterView getBatteryMeterView(QuickStatusBarHeader quickStatusBarHeader) {
        return (BatteryMeterView) quickStatusBarHeader.findViewById(C1894R.C1898id.qs_batteryRemainingIcon);
    }

    @Provides
    @Named("nt_qs_header_battery_controller")
    static BatteryMeterViewController getBatteryMeterViewController(@Named("nt_qs_header_battery_meter_view") BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, @Main Handler handler, ContentResolver contentResolver, BatteryController batteryController) {
        return new BatteryMeterViewController(batteryMeterView, configurationController, tunerService, broadcastDispatcher, handler, contentResolver, batteryController);
    }
}
