package com.airbnb.lottie.utils;

public class MeanCalculator {

    /* renamed from: n */
    private int f180n;
    private float sum;

    public void add(float f) {
        float f2 = this.sum + f;
        this.sum = f2;
        int i = this.f180n + 1;
        this.f180n = i;
        if (i == Integer.MAX_VALUE) {
            this.sum = f2 / 2.0f;
            this.f180n = i / 2;
        }
    }

    public float getMean() {
        int i = this.f180n;
        if (i == 0) {
            return 0.0f;
        }
        return this.sum / ((float) i);
    }
}
