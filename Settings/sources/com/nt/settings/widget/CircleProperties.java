package com.nt.settings.widget;

import android.view.ViewGroup;
/* loaded from: classes2.dex */
public class CircleProperties {
    private int mBottom;
    private float mCx;
    private float mCy;
    private int mLeft;
    private ViewGroup.MarginLayoutParams mParams;
    private float mRadius;
    private int mRight;
    private int mTop;

    public CircleProperties(int i, int i2, int i3, int i4, float f, float f2, float f3, ViewGroup.MarginLayoutParams marginLayoutParams) {
        this.mLeft = i;
        this.mTop = i2;
        this.mRight = i3;
        this.mBottom = i4;
        this.mRadius = f;
        this.mCx = f2;
        this.mCy = f3;
        this.mParams = marginLayoutParams;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getTop() {
        return this.mTop;
    }

    public int getRight() {
        return this.mRight;
    }

    public int getBottom() {
        return this.mBottom;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public float getCx() {
        return this.mCx;
    }

    public float getCy() {
        return this.mCy;
    }

    public ViewGroup.MarginLayoutParams getMarginLayoutParams() {
        return this.mParams;
    }
}
