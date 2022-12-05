package com.android.keyguard;

import android.content.res.Resources;
import android.telephony.SubscriptionManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.DejankUtils;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardSecurityModel {
    private final boolean mIsPukScreenAvailable;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LockPatternUtils mLockPatternUtils;

    /* loaded from: classes.dex */
    public enum SecurityMode {
        Invalid,
        None,
        Pattern,
        Password,
        PIN,
        SimPin,
        SimPuk
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyguardSecurityModel(Resources resources, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mIsPukScreenAvailable = resources.getBoolean(17891554);
        this.mLockPatternUtils = lockPatternUtils;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    public SecurityMode getSecurityMode(final int i) {
        if (this.mIsPukScreenAvailable && SubscriptionManager.isValidSubscriptionId(this.mKeyguardUpdateMonitor.getNextSubIdForState(3))) {
            return SecurityMode.SimPuk;
        }
        if (SubscriptionManager.isValidSubscriptionId(this.mKeyguardUpdateMonitor.getUnlockedSubIdForState(2))) {
            return SecurityMode.SimPin;
        }
        int intValue = ((Integer) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardSecurityModel$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                Integer lambda$getSecurityMode$0;
                lambda$getSecurityMode$0 = KeyguardSecurityModel.this.lambda$getSecurityMode$0(i);
                return lambda$getSecurityMode$0;
            }
        })).intValue();
        if (intValue == 0) {
            return SecurityMode.None;
        }
        if (intValue == 65536) {
            return SecurityMode.Pattern;
        }
        if (intValue == 131072 || intValue == 196608) {
            return SecurityMode.PIN;
        }
        if (intValue == 262144 || intValue == 327680 || intValue == 393216 || intValue == 524288) {
            return SecurityMode.Password;
        }
        throw new IllegalStateException("Unknown security quality:" + intValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getSecurityMode$0(int i) {
        return Integer.valueOf(this.mLockPatternUtils.getActivePasswordQuality(i));
    }

    public static boolean isSecurityViewOneHanded(SecurityMode securityMode) {
        return securityMode == SecurityMode.Pattern || securityMode == SecurityMode.PIN;
    }
}
