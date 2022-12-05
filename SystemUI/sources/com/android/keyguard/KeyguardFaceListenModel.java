package com.android.keyguard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: KeyguardListenModel.kt */
/* loaded from: classes.dex */
public final class KeyguardFaceListenModel extends KeyguardListenModel {
    private final boolean authInterruptActive;
    private final boolean becauseCannotSkipBouncer;
    private final boolean biometricSettingEnabledForUser;
    private final boolean bouncer;
    private final boolean faceAuthenticated;
    private final boolean faceDisabled;
    private final boolean keyguardAwake;
    private final boolean keyguardGoingAway;
    private final boolean listening;
    private final boolean listeningForFaceAssistant;
    private final boolean lockIconPressed;
    private final int modality = 2;
    private final boolean occludingAppRequestingFaceAuth;
    private final boolean primaryUser;
    private final boolean scanningAllowedByStrongAuth;
    private final boolean secureCameraLaunched;
    private final boolean switchingUser;
    private final long timeMillis;
    private final int userId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyguardFaceListenModel)) {
            return false;
        }
        KeyguardFaceListenModel keyguardFaceListenModel = (KeyguardFaceListenModel) obj;
        return getTimeMillis() == keyguardFaceListenModel.getTimeMillis() && getUserId() == keyguardFaceListenModel.getUserId() && getListening() == keyguardFaceListenModel.getListening() && this.authInterruptActive == keyguardFaceListenModel.authInterruptActive && this.becauseCannotSkipBouncer == keyguardFaceListenModel.becauseCannotSkipBouncer && this.biometricSettingEnabledForUser == keyguardFaceListenModel.biometricSettingEnabledForUser && this.bouncer == keyguardFaceListenModel.bouncer && this.faceAuthenticated == keyguardFaceListenModel.faceAuthenticated && this.faceDisabled == keyguardFaceListenModel.faceDisabled && this.keyguardAwake == keyguardFaceListenModel.keyguardAwake && this.keyguardGoingAway == keyguardFaceListenModel.keyguardGoingAway && this.listeningForFaceAssistant == keyguardFaceListenModel.listeningForFaceAssistant && this.lockIconPressed == keyguardFaceListenModel.lockIconPressed && this.occludingAppRequestingFaceAuth == keyguardFaceListenModel.occludingAppRequestingFaceAuth && this.primaryUser == keyguardFaceListenModel.primaryUser && this.scanningAllowedByStrongAuth == keyguardFaceListenModel.scanningAllowedByStrongAuth && this.secureCameraLaunched == keyguardFaceListenModel.secureCameraLaunched && this.switchingUser == keyguardFaceListenModel.switchingUser;
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
        boolean z = this.authInterruptActive;
        if (z) {
            z = true;
        }
        int i5 = z ? 1 : 0;
        int i6 = z ? 1 : 0;
        int i7 = (i4 + i5) * 31;
        boolean z2 = this.becauseCannotSkipBouncer;
        if (z2) {
            z2 = true;
        }
        int i8 = z2 ? 1 : 0;
        int i9 = z2 ? 1 : 0;
        int i10 = (i7 + i8) * 31;
        boolean z3 = this.biometricSettingEnabledForUser;
        if (z3) {
            z3 = true;
        }
        int i11 = z3 ? 1 : 0;
        int i12 = z3 ? 1 : 0;
        int i13 = (i10 + i11) * 31;
        boolean z4 = this.bouncer;
        if (z4) {
            z4 = true;
        }
        int i14 = z4 ? 1 : 0;
        int i15 = z4 ? 1 : 0;
        int i16 = (i13 + i14) * 31;
        boolean z5 = this.faceAuthenticated;
        if (z5) {
            z5 = true;
        }
        int i17 = z5 ? 1 : 0;
        int i18 = z5 ? 1 : 0;
        int i19 = (i16 + i17) * 31;
        boolean z6 = this.faceDisabled;
        if (z6) {
            z6 = true;
        }
        int i20 = z6 ? 1 : 0;
        int i21 = z6 ? 1 : 0;
        int i22 = (i19 + i20) * 31;
        boolean z7 = this.keyguardAwake;
        if (z7) {
            z7 = true;
        }
        int i23 = z7 ? 1 : 0;
        int i24 = z7 ? 1 : 0;
        int i25 = (i22 + i23) * 31;
        boolean z8 = this.keyguardGoingAway;
        if (z8) {
            z8 = true;
        }
        int i26 = z8 ? 1 : 0;
        int i27 = z8 ? 1 : 0;
        int i28 = (i25 + i26) * 31;
        boolean z9 = this.listeningForFaceAssistant;
        if (z9) {
            z9 = true;
        }
        int i29 = z9 ? 1 : 0;
        int i30 = z9 ? 1 : 0;
        int i31 = (i28 + i29) * 31;
        boolean z10 = this.lockIconPressed;
        if (z10) {
            z10 = true;
        }
        int i32 = z10 ? 1 : 0;
        int i33 = z10 ? 1 : 0;
        int i34 = (i31 + i32) * 31;
        boolean z11 = this.occludingAppRequestingFaceAuth;
        if (z11) {
            z11 = true;
        }
        int i35 = z11 ? 1 : 0;
        int i36 = z11 ? 1 : 0;
        int i37 = (i34 + i35) * 31;
        boolean z12 = this.primaryUser;
        if (z12) {
            z12 = true;
        }
        int i38 = z12 ? 1 : 0;
        int i39 = z12 ? 1 : 0;
        int i40 = (i37 + i38) * 31;
        boolean z13 = this.scanningAllowedByStrongAuth;
        if (z13) {
            z13 = true;
        }
        int i41 = z13 ? 1 : 0;
        int i42 = z13 ? 1 : 0;
        int i43 = (i40 + i41) * 31;
        boolean z14 = this.secureCameraLaunched;
        if (z14) {
            z14 = true;
        }
        int i44 = z14 ? 1 : 0;
        int i45 = z14 ? 1 : 0;
        int i46 = (i43 + i44) * 31;
        boolean z15 = this.switchingUser;
        if (!z15) {
            i = z15 ? 1 : 0;
        }
        return i46 + i;
    }

    @NotNull
    public String toString() {
        return "KeyguardFaceListenModel(timeMillis=" + getTimeMillis() + ", userId=" + getUserId() + ", listening=" + getListening() + ", authInterruptActive=" + this.authInterruptActive + ", becauseCannotSkipBouncer=" + this.becauseCannotSkipBouncer + ", biometricSettingEnabledForUser=" + this.biometricSettingEnabledForUser + ", bouncer=" + this.bouncer + ", faceAuthenticated=" + this.faceAuthenticated + ", faceDisabled=" + this.faceDisabled + ", keyguardAwake=" + this.keyguardAwake + ", keyguardGoingAway=" + this.keyguardGoingAway + ", listeningForFaceAssistant=" + this.listeningForFaceAssistant + ", lockIconPressed=" + this.lockIconPressed + ", occludingAppRequestingFaceAuth=" + this.occludingAppRequestingFaceAuth + ", primaryUser=" + this.primaryUser + ", scanningAllowedByStrongAuth=" + this.scanningAllowedByStrongAuth + ", secureCameraLaunched=" + this.secureCameraLaunched + ", switchingUser=" + this.switchingUser + ')';
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

    public KeyguardFaceListenModel(long j, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16) {
        super(null);
        this.timeMillis = j;
        this.userId = i;
        this.listening = z;
        this.authInterruptActive = z2;
        this.becauseCannotSkipBouncer = z3;
        this.biometricSettingEnabledForUser = z4;
        this.bouncer = z5;
        this.faceAuthenticated = z6;
        this.faceDisabled = z7;
        this.keyguardAwake = z8;
        this.keyguardGoingAway = z9;
        this.listeningForFaceAssistant = z10;
        this.lockIconPressed = z11;
        this.occludingAppRequestingFaceAuth = z12;
        this.primaryUser = z13;
        this.scanningAllowedByStrongAuth = z14;
        this.secureCameraLaunched = z15;
        this.switchingUser = z16;
    }
}
