package com.android.keyguard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: KeyguardListenModel.kt */
/* loaded from: classes.dex */
public final class KeyguardFingerprintListenModel extends KeyguardListenModel {
    private final boolean biometricEnabledForUser;
    private final boolean bouncer;
    private final boolean canSkipBouncer;
    private final boolean credentialAttempted;
    private final boolean deviceInteractive;
    private final boolean dreaming;
    private final boolean encryptedOrLockdown;
    private final boolean fingerprintDisabled;
    private final boolean fingerprintLockedOut;
    private final boolean goingToSleep;
    private final boolean keyguardGoingAway;
    private final boolean keyguardIsVisible;
    private final boolean keyguardOccluded;
    private final boolean listening;
    private final int modality = 8;
    private final boolean occludingAppRequestingFp;
    private final boolean primaryUser;
    private final boolean shouldListenForFingerprintAssistant;
    private final boolean switchingUser;
    private final long timeMillis;
    private final boolean udfps;
    private final boolean userDoesNotHaveTrust;
    private final int userId;
    private final boolean userNeedsStrongAuth;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyguardFingerprintListenModel)) {
            return false;
        }
        KeyguardFingerprintListenModel keyguardFingerprintListenModel = (KeyguardFingerprintListenModel) obj;
        return getTimeMillis() == keyguardFingerprintListenModel.getTimeMillis() && getUserId() == keyguardFingerprintListenModel.getUserId() && getListening() == keyguardFingerprintListenModel.getListening() && this.biometricEnabledForUser == keyguardFingerprintListenModel.biometricEnabledForUser && this.bouncer == keyguardFingerprintListenModel.bouncer && this.canSkipBouncer == keyguardFingerprintListenModel.canSkipBouncer && this.credentialAttempted == keyguardFingerprintListenModel.credentialAttempted && this.deviceInteractive == keyguardFingerprintListenModel.deviceInteractive && this.dreaming == keyguardFingerprintListenModel.dreaming && this.encryptedOrLockdown == keyguardFingerprintListenModel.encryptedOrLockdown && this.fingerprintDisabled == keyguardFingerprintListenModel.fingerprintDisabled && this.fingerprintLockedOut == keyguardFingerprintListenModel.fingerprintLockedOut && this.goingToSleep == keyguardFingerprintListenModel.goingToSleep && this.keyguardGoingAway == keyguardFingerprintListenModel.keyguardGoingAway && this.keyguardIsVisible == keyguardFingerprintListenModel.keyguardIsVisible && this.keyguardOccluded == keyguardFingerprintListenModel.keyguardOccluded && this.occludingAppRequestingFp == keyguardFingerprintListenModel.occludingAppRequestingFp && this.primaryUser == keyguardFingerprintListenModel.primaryUser && this.shouldListenForFingerprintAssistant == keyguardFingerprintListenModel.shouldListenForFingerprintAssistant && this.switchingUser == keyguardFingerprintListenModel.switchingUser && this.udfps == keyguardFingerprintListenModel.udfps && this.userDoesNotHaveTrust == keyguardFingerprintListenModel.userDoesNotHaveTrust && this.userNeedsStrongAuth == keyguardFingerprintListenModel.userNeedsStrongAuth;
    }

    public int hashCode() {
        int hashCode = ((Long.hashCode(getTimeMillis()) * 31) + Integer.hashCode(getUserId())) * 31;
        boolean listening = getListening();
        int i = 1;
        if (listening) {
            listening = true;
        }
        int i2 = listening ? 1 : 0;
        int i3 = listening ? 1 : 0;
        int i4 = (hashCode + i2) * 31;
        boolean z = this.biometricEnabledForUser;
        if (z) {
            z = true;
        }
        int i5 = z ? 1 : 0;
        int i6 = z ? 1 : 0;
        int i7 = (i4 + i5) * 31;
        boolean z2 = this.bouncer;
        if (z2) {
            z2 = true;
        }
        int i8 = z2 ? 1 : 0;
        int i9 = z2 ? 1 : 0;
        int i10 = (i7 + i8) * 31;
        boolean z3 = this.canSkipBouncer;
        if (z3) {
            z3 = true;
        }
        int i11 = z3 ? 1 : 0;
        int i12 = z3 ? 1 : 0;
        int i13 = (i10 + i11) * 31;
        boolean z4 = this.credentialAttempted;
        if (z4) {
            z4 = true;
        }
        int i14 = z4 ? 1 : 0;
        int i15 = z4 ? 1 : 0;
        int i16 = (i13 + i14) * 31;
        boolean z5 = this.deviceInteractive;
        if (z5) {
            z5 = true;
        }
        int i17 = z5 ? 1 : 0;
        int i18 = z5 ? 1 : 0;
        int i19 = (i16 + i17) * 31;
        boolean z6 = this.dreaming;
        if (z6) {
            z6 = true;
        }
        int i20 = z6 ? 1 : 0;
        int i21 = z6 ? 1 : 0;
        int i22 = (i19 + i20) * 31;
        boolean z7 = this.encryptedOrLockdown;
        if (z7) {
            z7 = true;
        }
        int i23 = z7 ? 1 : 0;
        int i24 = z7 ? 1 : 0;
        int i25 = (i22 + i23) * 31;
        boolean z8 = this.fingerprintDisabled;
        if (z8) {
            z8 = true;
        }
        int i26 = z8 ? 1 : 0;
        int i27 = z8 ? 1 : 0;
        int i28 = (i25 + i26) * 31;
        boolean z9 = this.fingerprintLockedOut;
        if (z9) {
            z9 = true;
        }
        int i29 = z9 ? 1 : 0;
        int i30 = z9 ? 1 : 0;
        int i31 = (i28 + i29) * 31;
        boolean z10 = this.goingToSleep;
        if (z10) {
            z10 = true;
        }
        int i32 = z10 ? 1 : 0;
        int i33 = z10 ? 1 : 0;
        int i34 = (i31 + i32) * 31;
        boolean z11 = this.keyguardGoingAway;
        if (z11) {
            z11 = true;
        }
        int i35 = z11 ? 1 : 0;
        int i36 = z11 ? 1 : 0;
        int i37 = (i34 + i35) * 31;
        boolean z12 = this.keyguardIsVisible;
        if (z12) {
            z12 = true;
        }
        int i38 = z12 ? 1 : 0;
        int i39 = z12 ? 1 : 0;
        int i40 = (i37 + i38) * 31;
        boolean z13 = this.keyguardOccluded;
        if (z13) {
            z13 = true;
        }
        int i41 = z13 ? 1 : 0;
        int i42 = z13 ? 1 : 0;
        int i43 = (i40 + i41) * 31;
        boolean z14 = this.occludingAppRequestingFp;
        if (z14) {
            z14 = true;
        }
        int i44 = z14 ? 1 : 0;
        int i45 = z14 ? 1 : 0;
        int i46 = (i43 + i44) * 31;
        boolean z15 = this.primaryUser;
        if (z15) {
            z15 = true;
        }
        int i47 = z15 ? 1 : 0;
        int i48 = z15 ? 1 : 0;
        int i49 = (i46 + i47) * 31;
        boolean z16 = this.shouldListenForFingerprintAssistant;
        if (z16) {
            z16 = true;
        }
        int i50 = z16 ? 1 : 0;
        int i51 = z16 ? 1 : 0;
        int i52 = (i49 + i50) * 31;
        boolean z17 = this.switchingUser;
        if (z17) {
            z17 = true;
        }
        int i53 = z17 ? 1 : 0;
        int i54 = z17 ? 1 : 0;
        int i55 = (i52 + i53) * 31;
        boolean z18 = this.udfps;
        if (z18) {
            z18 = true;
        }
        int i56 = z18 ? 1 : 0;
        int i57 = z18 ? 1 : 0;
        int i58 = (i55 + i56) * 31;
        boolean z19 = this.userDoesNotHaveTrust;
        if (z19) {
            z19 = true;
        }
        int i59 = z19 ? 1 : 0;
        int i60 = z19 ? 1 : 0;
        int i61 = (i58 + i59) * 31;
        boolean z20 = this.userNeedsStrongAuth;
        if (!z20) {
            i = z20 ? 1 : 0;
        }
        return i61 + i;
    }

    @NotNull
    public String toString() {
        return "KeyguardFingerprintListenModel(timeMillis=" + getTimeMillis() + ", userId=" + getUserId() + ", listening=" + getListening() + ", biometricEnabledForUser=" + this.biometricEnabledForUser + ", bouncer=" + this.bouncer + ", canSkipBouncer=" + this.canSkipBouncer + ", credentialAttempted=" + this.credentialAttempted + ", deviceInteractive=" + this.deviceInteractive + ", dreaming=" + this.dreaming + ", encryptedOrLockdown=" + this.encryptedOrLockdown + ", fingerprintDisabled=" + this.fingerprintDisabled + ", fingerprintLockedOut=" + this.fingerprintLockedOut + ", goingToSleep=" + this.goingToSleep + ", keyguardGoingAway=" + this.keyguardGoingAway + ", keyguardIsVisible=" + this.keyguardIsVisible + ", keyguardOccluded=" + this.keyguardOccluded + ", occludingAppRequestingFp=" + this.occludingAppRequestingFp + ", primaryUser=" + this.primaryUser + ", shouldListenForFingerprintAssistant=" + this.shouldListenForFingerprintAssistant + ", switchingUser=" + this.switchingUser + ", udfps=" + this.udfps + ", userDoesNotHaveTrust=" + this.userDoesNotHaveTrust + ", userNeedsStrongAuth=" + this.userNeedsStrongAuth + ')';
    }

    @Override // com.android.keyguard.KeyguardListenModel
    public long getTimeMillis() {
        return this.timeMillis;
    }

    public int getUserId() {
        return this.userId;
    }

    @Override // com.android.keyguard.KeyguardListenModel
    public boolean getListening() {
        return this.listening;
    }

    public KeyguardFingerprintListenModel(long j, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16, boolean z17, boolean z18, boolean z19, boolean z20, boolean z21) {
        super(null);
        this.timeMillis = j;
        this.userId = i;
        this.listening = z;
        this.biometricEnabledForUser = z2;
        this.bouncer = z3;
        this.canSkipBouncer = z4;
        this.credentialAttempted = z5;
        this.deviceInteractive = z6;
        this.dreaming = z7;
        this.encryptedOrLockdown = z8;
        this.fingerprintDisabled = z9;
        this.fingerprintLockedOut = z10;
        this.goingToSleep = z11;
        this.keyguardGoingAway = z12;
        this.keyguardIsVisible = z13;
        this.keyguardOccluded = z14;
        this.occludingAppRequestingFp = z15;
        this.primaryUser = z16;
        this.shouldListenForFingerprintAssistant = z17;
        this.switchingUser = z18;
        this.udfps = z19;
        this.userDoesNotHaveTrust = z20;
        this.userNeedsStrongAuth = z21;
    }
}
