package com.nothing.systemui.doze;

public class DozeHostEx {

    public interface CallbackEx {
        void onDozeFingerprintDown() {
        }

        void onDozeFingerprintRunningStateChanged() {
        }

        void onDozeFingerprintUp() {
        }

        void onLiftWake() {
        }

        void onMotion() {
        }

        void onTapWakeUp() {
        }
    }
}
