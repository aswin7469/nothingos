package com.android.keyguard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.dagger.KeyguardBouncerScope;
import com.android.systemui.C1893R;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

@KeyguardBouncerScope
public class KeyguardSecurityViewFlipperController extends ViewController<KeyguardSecurityViewFlipper> {
    private static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "KeyguardSecurityView";
    private final List<KeyguardInputViewController<KeyguardInputView>> mChildren = new ArrayList();
    private final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
    private final KeyguardInputViewController.Factory mKeyguardSecurityViewControllerFactory;
    private final LayoutInflater mLayoutInflater;

    /* access modifiers changed from: protected */
    public void onViewAttached() {
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
    }

    @Inject
    protected KeyguardSecurityViewFlipperController(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, LayoutInflater layoutInflater, KeyguardInputViewController.Factory factory, EmergencyButtonController.Factory factory2) {
        super(keyguardSecurityViewFlipper);
        this.mKeyguardSecurityViewControllerFactory = factory;
        this.mLayoutInflater = layoutInflater;
        this.mEmergencyButtonControllerFactory = factory2;
    }

    public void reset() {
        for (KeyguardInputViewController<KeyguardInputView> reset : this.mChildren) {
            reset.reset();
        }
    }

    public void reloadColors() {
        for (KeyguardInputViewController<KeyguardInputView> reloadColors : this.mChildren) {
            reloadColors.reloadColors();
        }
    }

    /* access modifiers changed from: package-private */
    public KeyguardInputViewController<KeyguardInputView> getSecurityView(KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback) {
        KeyguardInputViewController<KeyguardInputView> keyguardInputViewController;
        int layoutIdFor;
        Iterator<KeyguardInputViewController<KeyguardInputView>> it = this.mChildren.iterator();
        while (true) {
            if (!it.hasNext()) {
                keyguardInputViewController = null;
                break;
            }
            keyguardInputViewController = it.next();
            if (keyguardInputViewController.getSecurityMode() == securityMode) {
                break;
            }
        }
        if (!(keyguardInputViewController != null || securityMode == KeyguardSecurityModel.SecurityMode.None || securityMode == KeyguardSecurityModel.SecurityMode.Invalid || (layoutIdFor = getLayoutIdFor(securityMode)) == 0)) {
            if (DEBUG) {
                Log.v(TAG, "inflating id = " + layoutIdFor);
            }
            KeyguardInputView keyguardInputView = (KeyguardInputView) this.mLayoutInflater.inflate(layoutIdFor, (ViewGroup) this.mView, false);
            ((KeyguardSecurityViewFlipper) this.mView).addView(keyguardInputView);
            keyguardInputViewController = this.mKeyguardSecurityViewControllerFactory.create(keyguardInputView, securityMode, keyguardSecurityCallback);
            keyguardInputViewController.init();
            this.mChildren.add(keyguardInputViewController);
        }
        return keyguardInputViewController == null ? new NullKeyguardInputViewController(securityMode, keyguardSecurityCallback, this.mEmergencyButtonControllerFactory.create((EmergencyButton) null)) : keyguardInputViewController;
    }

    /* renamed from: com.android.keyguard.KeyguardSecurityViewFlipperController$1 */
    static /* synthetic */ class C16321 {

        /* renamed from: $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode */
        static final /* synthetic */ int[] f233xdc0e830a;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.keyguard.KeyguardSecurityModel$SecurityMode[] r0 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f233xdc0e830a = r0
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Pattern     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f233xdc0e830a     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.PIN     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f233xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Password     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f233xdc0e830a     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPin     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f233xdc0e830a     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPuk     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityViewFlipperController.C16321.<clinit>():void");
        }
    }

    private int getLayoutIdFor(KeyguardSecurityModel.SecurityMode securityMode) {
        int i = C16321.f233xdc0e830a[securityMode.ordinal()];
        if (i == 1) {
            return C1893R.layout.keyguard_pattern_view;
        }
        if (i == 2) {
            return C1893R.layout.keyguard_pin_view;
        }
        if (i == 3) {
            return C1893R.layout.keyguard_password_view;
        }
        if (i == 4) {
            return C1893R.layout.keyguard_sim_pin_view;
        }
        if (i != 5) {
            return 0;
        }
        return C1893R.layout.keyguard_sim_puk_view;
    }

    public void show(KeyguardInputViewController<KeyguardInputView> keyguardInputViewController) {
        int indexIn = keyguardInputViewController.getIndexIn((KeyguardSecurityViewFlipper) this.mView);
        if (indexIn != -1) {
            ((KeyguardSecurityViewFlipper) this.mView).setDisplayedChild(indexIn);
        }
    }

    private static class NullKeyguardInputViewController extends KeyguardInputViewController<KeyguardInputView> {
        public boolean needsInput() {
            return false;
        }

        public void onStartingToHide() {
        }

        protected NullKeyguardInputViewController(KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback, EmergencyButtonController emergencyButtonController) {
            super(null, securityMode, keyguardSecurityCallback, emergencyButtonController);
        }
    }
}
