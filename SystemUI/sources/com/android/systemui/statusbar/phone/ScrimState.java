package com.android.systemui.statusbar.phone;

import android.graphics.Color;
import android.os.Trace;
import androidx.core.view.ViewCompat;
import com.android.systemui.dock.DockManager;
import com.android.systemui.scrim.ScrimView;
import com.nothing.systemui.util.NTColorUtil;

public enum ScrimState {
    UNINITIALIZED,
    OFF {
        public boolean isLowPowerState() {
            return true;
        }

        public void prepare(ScrimState scrimState) {
            this.mFrontTint = ViewCompat.MEASURED_STATE_MASK;
            this.mBehindTint = ViewCompat.MEASURED_STATE_MASK;
            this.mFrontAlpha = 1.0f;
            this.mBehindAlpha = 1.0f;
            this.mAnimationDuration = 1000;
        }
    },
    KEYGUARD {
        public void prepare(ScrimState scrimState) {
            int i = 0;
            this.mBlankScreen = false;
            if (scrimState == ScrimState.AOD) {
                this.mAnimationDuration = 667;
                if (this.mDisplayRequiresBlanking) {
                    this.mBlankScreen = true;
                }
            } else if (scrimState == ScrimState.KEYGUARD) {
                this.mAnimationDuration = 667;
            } else {
                this.mAnimationDuration = 220;
            }
            this.mFrontTint = ViewCompat.MEASURED_STATE_MASK;
            this.mBehindTint = ViewCompat.MEASURED_STATE_MASK;
            if (this.mClipQsScrim) {
                i = -16777216;
            }
            this.mNotifTint = i;
            float f = 0.0f;
            this.mFrontAlpha = 0.0f;
            this.mBehindAlpha = this.mClipQsScrim ? 1.0f : this.mScrimBehindAlphaKeyguard;
            if (this.mClipQsScrim) {
                f = this.mScrimBehindAlphaKeyguard;
            }
            this.mNotifAlpha = f;
            if (this.mClipQsScrim) {
                updateScrimColor(this.mScrimBehind, 1.0f, NTColorUtil.getScrimBehindTintColor(this.mScrimBehind.getContext()));
            }
        }
    },
    AUTH_SCRIMMED_SHADE {
        public void prepare(ScrimState scrimState) {
            this.mFrontTint = ViewCompat.MEASURED_STATE_MASK;
            this.mFrontAlpha = 0.66f;
        }
    },
    AUTH_SCRIMMED {
        public void prepare(ScrimState scrimState) {
            this.mNotifTint = scrimState.mNotifTint;
            this.mNotifAlpha = scrimState.mNotifAlpha;
            this.mBehindTint = scrimState.mBehindTint;
            this.mBehindAlpha = scrimState.mBehindAlpha;
            this.mFrontTint = ViewCompat.MEASURED_STATE_MASK;
            this.mFrontAlpha = 0.66f;
        }
    },
    BOUNCER {
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = this.mClipQsScrim ? 1.0f : this.mDefaultScrimAlpha;
            this.mBehindTint = this.mClipQsScrim ? ViewCompat.MEASURED_STATE_MASK : 0;
            this.mNotifAlpha = this.mClipQsScrim ? this.mDefaultScrimAlpha : 0.0f;
            this.mNotifTint = 0;
            this.mFrontAlpha = 0.0f;
        }
    },
    BOUNCER_SCRIMMED {
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = 0.0f;
            this.mFrontAlpha = this.mDefaultScrimAlpha;
        }
    },
    SHADE_LOCKED {
        public int getBehindTint() {
            return ViewCompat.MEASURED_STATE_MASK;
        }

        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = this.mClipQsScrim ? 1.0f : this.mDefaultScrimAlpha;
            this.mNotifAlpha = 1.0f;
            this.mFrontAlpha = 0.0f;
            this.mBehindTint = ViewCompat.MEASURED_STATE_MASK;
            if (this.mClipQsScrim) {
                updateScrimColor(this.mScrimBehind, 1.0f, NTColorUtil.getScrimBehindTintColor(this.mScrimBehind.getContext()));
            }
        }
    },
    BRIGHTNESS_MIRROR {
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = 0.0f;
            this.mFrontAlpha = 0.0f;
        }
    },
    AOD {
        public boolean isLowPowerState() {
            return true;
        }

        public boolean shouldBlendWithMainColor() {
            return false;
        }

        public void prepare(ScrimState scrimState) {
            float f;
            boolean alwaysOn = this.mDozeParameters.getAlwaysOn();
            boolean isQuickPickupEnabled = this.mDozeParameters.isQuickPickupEnabled();
            boolean isDocked = this.mDockManager.isDocked();
            this.mBlankScreen = this.mDisplayRequiresBlanking;
            this.mFrontTint = ViewCompat.MEASURED_STATE_MASK;
            if (alwaysOn || isDocked || isQuickPickupEnabled) {
                f = this.mAodFrontScrimAlpha;
            } else {
                f = 1.0f;
            }
            this.mFrontAlpha = f;
            this.mBehindTint = ViewCompat.MEASURED_STATE_MASK;
            this.mBehindAlpha = 0.0f;
            this.mAnimationDuration = 1000;
            this.mAnimateChange = this.mDozeParameters.shouldControlScreenOff() && !this.mDozeParameters.shouldShowLightRevealScrim();
        }

        public float getMaxLightRevealScrimAlpha() {
            return (!this.mWallpaperSupportsAmbientMode || this.mHasBackdrop) ? 1.0f : 0.0f;
        }
    },
    PULSING {
        public void prepare(ScrimState scrimState) {
            this.mFrontAlpha = this.mAodFrontScrimAlpha;
            this.mBehindTint = ViewCompat.MEASURED_STATE_MASK;
            this.mFrontTint = ViewCompat.MEASURED_STATE_MASK;
            this.mBlankScreen = this.mDisplayRequiresBlanking;
            this.mAnimationDuration = this.mWakeLockScreenSensorActive ? 1000 : 220;
        }

        public float getMaxLightRevealScrimAlpha() {
            if (this.mWakeLockScreenSensorActive) {
                return 0.6f;
            }
            return AOD.getMaxLightRevealScrimAlpha();
        }
    },
    UNLOCKED {
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = this.mClipQsScrim ? 1.0f : 0.0f;
            this.mNotifAlpha = 0.0f;
            this.mFrontAlpha = 0.0f;
            this.mAnimationDuration = this.mKeyguardFadingAway ? this.mKeyguardFadingAwayDuration : 300;
            this.mAnimateChange = !this.mLaunchingAffordanceWithPreview && !(scrimState == AOD || scrimState == PULSING);
            this.mFrontTint = 0;
            this.mBehindTint = NTColorUtil.getScrimBehindTintColor(this.mScrimBehind.getContext());
            this.mBlankScreen = false;
            if (scrimState == ScrimState.AOD) {
                updateScrimColor(this.mScrimInFront, 1.0f, ViewCompat.MEASURED_STATE_MASK);
                updateScrimColor(this.mScrimBehind, 1.0f, ViewCompat.MEASURED_STATE_MASK);
                this.mFrontTint = ViewCompat.MEASURED_STATE_MASK;
                this.mBehindTint = ViewCompat.MEASURED_STATE_MASK;
                this.mBlankScreen = true;
            }
            if (this.mClipQsScrim) {
                updateScrimColor(this.mScrimBehind, 1.0f, NTColorUtil.getScrimBehindTintColor(this.mScrimBehind.getContext()));
            }
        }
    },
    DREAMING {
        public void prepare(ScrimState scrimState) {
            this.mFrontTint = 0;
            int i = ViewCompat.MEASURED_STATE_MASK;
            this.mBehindTint = ViewCompat.MEASURED_STATE_MASK;
            if (!this.mClipQsScrim) {
                i = 0;
            }
            this.mNotifTint = i;
            this.mFrontAlpha = 0.0f;
            this.mBehindAlpha = this.mClipQsScrim ? 1.0f : 0.0f;
            this.mNotifAlpha = 0.0f;
            this.mBlankScreen = false;
            if (this.mClipQsScrim) {
                updateScrimColor(this.mScrimBehind, 1.0f, NTColorUtil.getScrimBehindTintColor(this.mScrimBehind.getContext()));
            }
        }
    };
    
    boolean mAnimateChange;
    long mAnimationDuration;
    float mAodFrontScrimAlpha;
    float mBehindAlpha;
    int mBehindTint;
    boolean mBlankScreen;
    boolean mClipQsScrim;
    float mDefaultScrimAlpha;
    boolean mDisplayRequiresBlanking;
    DockManager mDockManager;
    DozeParameters mDozeParameters;
    float mFrontAlpha;
    int mFrontTint;
    boolean mHasBackdrop;
    boolean mKeyguardFadingAway;
    long mKeyguardFadingAwayDuration;
    boolean mLaunchingAffordanceWithPreview;
    float mNotifAlpha;
    int mNotifTint;
    ScrimView mScrimBehind;
    float mScrimBehindAlphaKeyguard;
    ScrimView mScrimInFront;
    boolean mWakeLockScreenSensorActive;
    boolean mWallpaperSupportsAmbientMode;

    public float getMaxLightRevealScrimAlpha() {
        return 1.0f;
    }

    public boolean isLowPowerState() {
        return false;
    }

    public void prepare(ScrimState scrimState) {
    }

    public boolean shouldBlendWithMainColor() {
        return true;
    }

    public void init(ScrimView scrimView, ScrimView scrimView2, DozeParameters dozeParameters, DockManager dockManager) {
        this.mScrimInFront = scrimView;
        this.mScrimBehind = scrimView2;
        this.mDozeParameters = dozeParameters;
        this.mDockManager = dockManager;
        this.mDisplayRequiresBlanking = dozeParameters.getDisplayNeedsBlanking();
    }

    public float getFrontAlpha() {
        return this.mFrontAlpha;
    }

    public float getBehindAlpha() {
        return this.mBehindAlpha;
    }

    public float getNotifAlpha() {
        return this.mNotifAlpha;
    }

    public int getFrontTint() {
        return this.mFrontTint;
    }

    public int getBehindTint() {
        return this.mBehindTint;
    }

    public int getNotifTint() {
        return this.mNotifTint;
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public boolean getBlanksScreen() {
        return this.mBlankScreen;
    }

    public void updateScrimColor(ScrimView scrimView, float f, int i) {
        Trace.traceCounter(4096, scrimView == this.mScrimInFront ? "front_scrim_alpha" : "back_scrim_alpha", (int) (255.0f * f));
        Trace.traceCounter(4096, scrimView == this.mScrimInFront ? "front_scrim_tint" : "back_scrim_tint", Color.alpha(i));
        scrimView.setTint(i);
        scrimView.setViewAlpha(f);
    }

    public boolean getAnimateChange() {
        return this.mAnimateChange;
    }

    public void setAodFrontScrimAlpha(float f) {
        this.mAodFrontScrimAlpha = f;
    }

    public void setScrimBehindAlphaKeyguard(float f) {
        this.mScrimBehindAlphaKeyguard = f;
    }

    public void setDefaultScrimAlpha(float f) {
        this.mDefaultScrimAlpha = f;
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
    }

    public void setLaunchingAffordanceWithPreview(boolean z) {
        this.mLaunchingAffordanceWithPreview = z;
    }

    public void setHasBackdrop(boolean z) {
        this.mHasBackdrop = z;
    }

    public void setWakeLockScreenSensorActive(boolean z) {
        this.mWakeLockScreenSensorActive = z;
    }

    public void setKeyguardFadingAway(boolean z, long j) {
        this.mKeyguardFadingAway = z;
        this.mKeyguardFadingAwayDuration = j;
    }

    public void setClipQsScrim(boolean z) {
        this.mClipQsScrim = z;
    }
}
