package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R$styleable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public class KeyTimeCycle extends Key {
    private String mTransitionEasing;
    private int mCurveFit = -1;
    private float mAlpha = Float.NaN;
    private float mElevation = Float.NaN;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mTransitionPathRotate = Float.NaN;
    private float mScaleX = Float.NaN;
    private float mScaleY = Float.NaN;
    private float mTranslationX = Float.NaN;
    private float mTranslationY = Float.NaN;
    private float mTranslationZ = Float.NaN;
    private float mProgress = Float.NaN;
    private int mWaveShape = 0;
    private float mWavePeriod = Float.NaN;
    private float mWaveOffset = 0.0f;

    public KeyTimeCycle() {
        this.mType = 3;
        this.mCustomConstraints = new HashMap<>();
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void load(Context context, AttributeSet attributeSet) {
        Loader.read(this, context.obtainStyledAttributes(attributeSet, R$styleable.KeyTimeCycle));
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void getAttributeNames(HashSet<String> hashSet) {
        if (!Float.isNaN(this.mAlpha)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.mElevation)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.mRotation)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.mRotationX)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.mRotationY)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.mTranslationX)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.mTranslationY)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            hashSet.add("translationZ");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.mProgress)) {
            hashSet.add("progress");
        }
        if (this.mCustomConstraints.size() > 0) {
            Iterator<String> it = this.mCustomConstraints.keySet().iterator();
            while (it.hasNext()) {
                hashSet.add("CUSTOM," + it.next());
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void setInterpolation(HashMap<String, Integer> hashMap) {
        if (this.mCurveFit == -1) {
            return;
        }
        if (!Float.isNaN(this.mAlpha)) {
            hashMap.put("alpha", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mElevation)) {
            hashMap.put("elevation", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mRotation)) {
            hashMap.put("rotation", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mRotationX)) {
            hashMap.put("rotationX", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mRotationY)) {
            hashMap.put("rotationY", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mTranslationX)) {
            hashMap.put("translationX", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mTranslationY)) {
            hashMap.put("translationY", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            hashMap.put("translationZ", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashMap.put("transitionPathRotate", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashMap.put("scaleX", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashMap.put("scaleY", Integer.valueOf(this.mCurveFit));
        }
        if (!Float.isNaN(this.mProgress)) {
            hashMap.put("progress", Integer.valueOf(this.mCurveFit));
        }
        if (this.mCustomConstraints.size() <= 0) {
            return;
        }
        Iterator<String> it = this.mCustomConstraints.keySet().iterator();
        while (it.hasNext()) {
            hashMap.put("CUSTOM," + it.next(), Integer.valueOf(this.mCurveFit));
        }
    }

    @Override // androidx.constraintlayout.motion.widget.Key
    public void addValues(HashMap<String, SplineSet> hashMap) {
        throw new IllegalArgumentException(" KeyTimeCycles do not support SplineSet");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0087, code lost:
        if (r1.equals("scaleY") == false) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void addTimeValues(HashMap<String, TimeCycleSplineSet> hashMap) {
        Iterator<String> it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            TimeCycleSplineSet timeCycleSplineSet = hashMap.get(next);
            char c = 7;
            if (next.startsWith("CUSTOM")) {
                ConstraintAttribute constraintAttribute = this.mCustomConstraints.get(next.substring(7));
                if (constraintAttribute != null) {
                    ((TimeCycleSplineSet.CustomSet) timeCycleSplineSet).setPoint(this.mFramePosition, constraintAttribute, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                }
            } else {
                switch (next.hashCode()) {
                    case -1249320806:
                        if (next.equals("rotationX")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1249320805:
                        if (next.equals("rotationY")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1225497657:
                        if (next.equals("translationX")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1225497656:
                        if (next.equals("translationY")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1225497655:
                        if (next.equals("translationZ")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1001078227:
                        if (next.equals("progress")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case -908189618:
                        if (next.equals("scaleX")) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case -908189617:
                        break;
                    case -40300674:
                        if (next.equals("rotation")) {
                            c = '\b';
                            break;
                        }
                        c = 65535;
                        break;
                    case -4379043:
                        if (next.equals("elevation")) {
                            c = '\t';
                            break;
                        }
                        c = 65535;
                        break;
                    case 37232917:
                        if (next.equals("transitionPathRotate")) {
                            c = '\n';
                            break;
                        }
                        c = 65535;
                        break;
                    case 92909918:
                        if (next.equals("alpha")) {
                            c = 11;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        if (!Float.isNaN(this.mRotationX)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mRotationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 1:
                        if (!Float.isNaN(this.mRotationY)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mRotationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 2:
                        if (!Float.isNaN(this.mTranslationX)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTranslationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 3:
                        if (!Float.isNaN(this.mTranslationY)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTranslationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 4:
                        if (!Float.isNaN(this.mTranslationZ)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTranslationZ, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 5:
                        if (!Float.isNaN(this.mProgress)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mProgress, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 6:
                        if (!Float.isNaN(this.mScaleX)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mScaleX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 7:
                        if (!Float.isNaN(this.mScaleY)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mScaleY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case '\b':
                        if (!Float.isNaN(this.mRotation)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mRotation, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case '\t':
                        if (!Float.isNaN(this.mElevation)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mElevation, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case '\n':
                        if (!Float.isNaN(this.mTransitionPathRotate)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    case 11:
                        if (!Float.isNaN(this.mAlpha)) {
                            timeCycleSplineSet.setPoint(this.mFramePosition, this.mAlpha, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                            break;
                        } else {
                            continue;
                        }
                    default:
                        Log.e("KeyTimeCycles", "UNKNOWN addValues \"" + next + "\"");
                        continue;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static class Loader {
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(R$styleable.KeyTimeCycle_android_alpha, 1);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_elevation, 2);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_rotation, 4);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_rotationX, 5);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_rotationY, 6);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_scaleX, 7);
            mAttrMap.append(R$styleable.KeyTimeCycle_transitionPathRotate, 8);
            mAttrMap.append(R$styleable.KeyTimeCycle_transitionEasing, 9);
            mAttrMap.append(R$styleable.KeyTimeCycle_motionTarget, 10);
            mAttrMap.append(R$styleable.KeyTimeCycle_framePosition, 12);
            mAttrMap.append(R$styleable.KeyTimeCycle_curveFit, 13);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_scaleY, 14);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_translationX, 15);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_translationY, 16);
            mAttrMap.append(R$styleable.KeyTimeCycle_android_translationZ, 17);
            mAttrMap.append(R$styleable.KeyTimeCycle_motionProgress, 18);
            mAttrMap.append(R$styleable.KeyTimeCycle_wavePeriod, 20);
            mAttrMap.append(R$styleable.KeyTimeCycle_waveOffset, 21);
            mAttrMap.append(R$styleable.KeyTimeCycle_waveShape, 19);
        }

        public static void read(KeyTimeCycle keyTimeCycle, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = typedArray.getIndex(i);
                switch (mAttrMap.get(index)) {
                    case 1:
                        keyTimeCycle.mAlpha = typedArray.getFloat(index, keyTimeCycle.mAlpha);
                        break;
                    case 2:
                        keyTimeCycle.mElevation = typedArray.getDimension(index, keyTimeCycle.mElevation);
                        break;
                    case 3:
                    case 11:
                    default:
                        Log.e("KeyTimeCycle", "unused attribute 0x" + Integer.toHexString(index) + "   " + mAttrMap.get(index));
                        break;
                    case 4:
                        keyTimeCycle.mRotation = typedArray.getFloat(index, keyTimeCycle.mRotation);
                        break;
                    case 5:
                        keyTimeCycle.mRotationX = typedArray.getFloat(index, keyTimeCycle.mRotationX);
                        break;
                    case 6:
                        keyTimeCycle.mRotationY = typedArray.getFloat(index, keyTimeCycle.mRotationY);
                        break;
                    case 7:
                        keyTimeCycle.mScaleX = typedArray.getFloat(index, keyTimeCycle.mScaleX);
                        break;
                    case 8:
                        keyTimeCycle.mTransitionPathRotate = typedArray.getFloat(index, keyTimeCycle.mTransitionPathRotate);
                        break;
                    case 9:
                        keyTimeCycle.mTransitionEasing = typedArray.getString(index);
                        break;
                    case 10:
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            int resourceId = typedArray.getResourceId(index, keyTimeCycle.mTargetId);
                            keyTimeCycle.mTargetId = resourceId;
                            if (resourceId == -1) {
                                keyTimeCycle.mTargetString = typedArray.getString(index);
                                break;
                            } else {
                                break;
                            }
                        } else if (typedArray.peekValue(index).type == 3) {
                            keyTimeCycle.mTargetString = typedArray.getString(index);
                            break;
                        } else {
                            keyTimeCycle.mTargetId = typedArray.getResourceId(index, keyTimeCycle.mTargetId);
                            break;
                        }
                    case 12:
                        keyTimeCycle.mFramePosition = typedArray.getInt(index, keyTimeCycle.mFramePosition);
                        break;
                    case 13:
                        keyTimeCycle.mCurveFit = typedArray.getInteger(index, keyTimeCycle.mCurveFit);
                        break;
                    case 14:
                        keyTimeCycle.mScaleY = typedArray.getFloat(index, keyTimeCycle.mScaleY);
                        break;
                    case 15:
                        keyTimeCycle.mTranslationX = typedArray.getDimension(index, keyTimeCycle.mTranslationX);
                        break;
                    case 16:
                        keyTimeCycle.mTranslationY = typedArray.getDimension(index, keyTimeCycle.mTranslationY);
                        break;
                    case 17:
                        if (Build.VERSION.SDK_INT >= 21) {
                            keyTimeCycle.mTranslationZ = typedArray.getDimension(index, keyTimeCycle.mTranslationZ);
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        keyTimeCycle.mProgress = typedArray.getFloat(index, keyTimeCycle.mProgress);
                        break;
                    case 19:
                        keyTimeCycle.mWaveShape = typedArray.getInt(index, keyTimeCycle.mWaveShape);
                        break;
                    case 20:
                        keyTimeCycle.mWavePeriod = typedArray.getFloat(index, keyTimeCycle.mWavePeriod);
                        break;
                    case 21:
                        if (typedArray.peekValue(index).type == 5) {
                            keyTimeCycle.mWaveOffset = typedArray.getDimension(index, keyTimeCycle.mWaveOffset);
                            break;
                        } else {
                            keyTimeCycle.mWaveOffset = typedArray.getFloat(index, keyTimeCycle.mWaveOffset);
                            break;
                        }
                }
            }
        }
    }
}
