package com.android.systemui.statusbar.phone;

import android.os.Bundle;
import android.view.View;
import com.android.systemui.dagger.qualifiers.DisplayId;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentScope;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

@StatusBarFragmentScope
public class StatusBarDemoMode extends ViewController<View> implements DemoMode {
    private final Clock mClockView;
    private final DemoModeController mDemoModeController;
    private final int mDisplayId;
    private final NavigationBarController mNavigationBarController;
    private final View mOperatorNameView;
    private final PhoneStatusBarTransitions mPhoneStatusBarTransitions;

    @Inject
    StatusBarDemoMode(Clock clock, @Named("operator_name_view") View view, DemoModeController demoModeController, PhoneStatusBarTransitions phoneStatusBarTransitions, NavigationBarController navigationBarController, @DisplayId int i) {
        super(clock);
        this.mClockView = clock;
        this.mOperatorNameView = view;
        this.mDemoModeController = demoModeController;
        this.mPhoneStatusBarTransitions = phoneStatusBarTransitions;
        this.mNavigationBarController = navigationBarController;
        this.mDisplayId = i;
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mDemoModeController.addCallback((DemoMode) this);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mDemoModeController.removeCallback((DemoMode) this);
    }

    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(DemoMode.COMMAND_BARS);
        arrayList.add(DemoMode.COMMAND_CLOCK);
        arrayList.add(DemoMode.COMMAND_OPERATOR);
        return arrayList;
    }

    public void onDemoModeStarted() {
        dispatchDemoModeStartedToView(this.mClockView);
        dispatchDemoModeStartedToView(this.mOperatorNameView);
    }

    public void onDemoModeFinished() {
        dispatchDemoModeFinishedToView(this.mClockView);
        dispatchDemoModeFinishedToView(this.mOperatorNameView);
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
        int i;
        if (str.equals(DemoMode.COMMAND_CLOCK)) {
            dispatchDemoCommandToView(str, bundle, this.mClockView);
        }
        if (str.equals(DemoMode.COMMAND_OPERATOR)) {
            dispatchDemoCommandToView(str, bundle, this.mOperatorNameView);
        }
        if (str.equals(DemoMode.COMMAND_BARS)) {
            String string = bundle.getString("mode");
            if ("opaque".equals(string)) {
                i = 4;
            } else if ("translucent".equals(string)) {
                i = 2;
            } else if ("semi-transparent".equals(string)) {
                i = 1;
            } else if ("transparent".equals(string)) {
                i = 0;
            } else {
                i = "warning".equals(string) ? 5 : -1;
            }
            if (i != -1) {
                this.mPhoneStatusBarTransitions.transitionTo(i, true);
                this.mNavigationBarController.transitionTo(this.mDisplayId, i, true);
            }
        }
    }

    private void dispatchDemoModeStartedToView(View view) {
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).onDemoModeStarted();
        }
    }

    private void dispatchDemoCommandToView(String str, Bundle bundle, View view) {
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).dispatchDemoCommand(str, bundle);
        }
    }

    private void dispatchDemoModeFinishedToView(View view) {
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).onDemoModeFinished();
        }
    }
}
