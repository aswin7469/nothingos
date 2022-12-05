package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes.dex */
public final class FlingAnimation extends DynamicAnimation<FlingAnimation> {
    private final DragForce mFlingForce;

    public <K> FlingAnimation(K object, FloatPropertyCompat<K> property) {
        super(object, property);
        DragForce dragForce = new DragForce();
        this.mFlingForce = dragForce;
        dragForce.setValueThreshold(getValueThreshold());
    }

    public FlingAnimation setFriction(float friction) {
        if (friction <= 0.0f) {
            throw new IllegalArgumentException("Friction must be positive");
        }
        this.mFlingForce.setFrictionScalar(friction);
        return this;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setMinValue(float minValue) {
        super.setMinValue(minValue);
        return this;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setMaxValue(float maxValue) {
        super.setMaxValue(maxValue);
        return this;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setStartVelocity(float startVelocity) {
        super.setStartVelocity(startVelocity);
        return this;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    boolean updateValueAndVelocity(long deltaT) {
        DynamicAnimation.MassState updateValueAndVelocity = this.mFlingForce.updateValueAndVelocity(this.mValue, this.mVelocity, deltaT);
        float f = updateValueAndVelocity.mValue;
        this.mValue = f;
        float f2 = updateValueAndVelocity.mVelocity;
        this.mVelocity = f2;
        float f3 = this.mMinValue;
        if (f < f3) {
            this.mValue = f3;
            return true;
        }
        float f4 = this.mMaxValue;
        if (f <= f4) {
            return isAtEquilibrium(f, f2);
        }
        this.mValue = f4;
        return true;
    }

    boolean isAtEquilibrium(float value, float velocity) {
        return value >= this.mMaxValue || value <= this.mMinValue || this.mFlingForce.isAtEquilibrium(value, velocity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class DragForce {
        private float mFriction = -4.2f;
        private final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
        private float mVelocityThreshold;

        DragForce() {
        }

        void setFrictionScalar(float frictionScalar) {
            this.mFriction = frictionScalar * (-4.2f);
        }

        DynamicAnimation.MassState updateValueAndVelocity(float value, float velocity, long deltaT) {
            this.mMassState.mVelocity = (float) (velocity * Math.exp((((float) deltaT) / 1000.0f) * this.mFriction));
            DynamicAnimation.MassState massState = this.mMassState;
            float f = massState.mVelocity;
            float f2 = value + ((f - velocity) / this.mFriction);
            massState.mValue = f2;
            if (isAtEquilibrium(f2, f)) {
                this.mMassState.mVelocity = 0.0f;
            }
            return this.mMassState;
        }

        public boolean isAtEquilibrium(float value, float velocity) {
            return Math.abs(velocity) < this.mVelocityThreshold;
        }

        void setValueThreshold(float threshold) {
            this.mVelocityThreshold = threshold * 62.5f;
        }
    }
}
