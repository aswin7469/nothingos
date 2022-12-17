package com.nothing.settings.face;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

public class AutoFitCameraPreviewTextureView extends TextureView {
    /* access modifiers changed from: private */
    public int mBottom;
    private float mCx;
    private float mCy;
    /* access modifiers changed from: private */
    public int mLeft;
    private ViewGroup.MarginLayoutParams mParams;
    /* access modifiers changed from: private */
    public int mPreviewHeight;
    /* access modifiers changed from: private */
    public float mPreviewScaleX;
    /* access modifiers changed from: private */
    public float mPreviewScaleY;
    /* access modifiers changed from: private */
    public int mPreviewWidth;
    private ViewOutlineProvider mProvider;
    private float mRadius;
    private int mRatioHeight;
    private int mRatioWidth;
    /* access modifiers changed from: private */
    public int mRight;
    /* access modifiers changed from: private */
    public OnSurfaceTextureAvailableListener mSurfaceTextureAvailableListener;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener;
    /* access modifiers changed from: private */
    public int mTop;

    public interface OnSurfaceTextureAvailableListener {
        void onSurfaceTextureAvailableCallback(SurfaceTexture surfaceTexture, int i, int i2);
    }

    public AutoFitCameraPreviewTextureView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AutoFitCameraPreviewTextureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AutoFitCameraPreviewTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRatioWidth = 3;
        this.mRatioHeight = 4;
        this.mPreviewWidth = 640;
        this.mPreviewHeight = 480;
        this.mProvider = new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                Log.d("Face", "AutoFitCameraPreviewTextureView getOutline mLeft:" + AutoFitCameraPreviewTextureView.this.mLeft + ",mTop:" + AutoFitCameraPreviewTextureView.this.mTop + ",mRight:" + AutoFitCameraPreviewTextureView.this.mRight + ",mBottom:" + AutoFitCameraPreviewTextureView.this.mBottom);
                outline.setOval(AutoFitCameraPreviewTextureView.this.mLeft, AutoFitCameraPreviewTextureView.this.mTop, AutoFitCameraPreviewTextureView.this.mRight, AutoFitCameraPreviewTextureView.this.mBottom);
            }
        };
        C19552 r2 = new TextureView.SurfaceTextureListener() {
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                Log.d("Face", "AutoFitCameraPreviewTextureView onSurfaceTextureAvailable width:" + i + ",height:" + i2 + "");
                if (surfaceTexture != null) {
                    AutoFitCameraPreviewTextureView.this.setSurfaceTextureDefaultBufferSize();
                    if (AutoFitCameraPreviewTextureView.this.mSurfaceTextureAvailableListener != null) {
                        AutoFitCameraPreviewTextureView.this.mSurfaceTextureAvailableListener.onSurfaceTextureAvailableCallback(surfaceTexture, i, i2);
                    }
                } else {
                    Log.e("Face", "onSurfaceTextureAvailable texture == null");
                }
                if (i > i2) {
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView.mPreviewScaleX = (float) (i / autoFitCameraPreviewTextureView.mPreviewWidth);
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView2 = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView2.mPreviewScaleY = (float) (i2 / autoFitCameraPreviewTextureView2.mPreviewHeight);
                    return;
                }
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView3 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView3.mPreviewScaleX = (float) (i / autoFitCameraPreviewTextureView3.mPreviewHeight);
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView4 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView4.mPreviewScaleY = (float) (i2 / autoFitCameraPreviewTextureView4.mPreviewWidth);
            }

            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
                Log.d("Face", "AutoFitCameraPreviewTextureView onSurfaceTextureSizeChanged width:" + i + ",height:" + i2);
                if (surfaceTexture != null) {
                    surfaceTexture.setDefaultBufferSize(AutoFitCameraPreviewTextureView.this.mPreviewHeight, AutoFitCameraPreviewTextureView.this.mPreviewWidth);
                }
                if (i > i2) {
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView.mPreviewScaleX = (float) (i / autoFitCameraPreviewTextureView.mPreviewWidth);
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView2 = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView2.mPreviewScaleY = (float) (i2 / autoFitCameraPreviewTextureView2.mPreviewHeight);
                    return;
                }
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView3 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView3.mPreviewScaleX = (float) (i / autoFitCameraPreviewTextureView3.mPreviewHeight);
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView4 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView4.mPreviewScaleY = (float) (i2 / autoFitCameraPreviewTextureView4.mPreviewWidth);
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                Log.d("Face", "AutoFitCameraPreviewTextureView onSurfaceTextureDestroyed");
                return true;
            }
        };
        this.mSurfaceTextureListener = r2;
        this.mContext = context;
        setSurfaceTextureListener(r2);
    }

    public void setAspectRatio() {
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int i4 = this.mRatioWidth;
        if (i4 == 0 || (i3 = this.mRatioHeight) == 0) {
            setMeasuredDimension(size, size2);
        } else if (size < size2) {
            setMeasuredDimension(size, (i3 * size) / i4);
        } else {
            setMeasuredDimension((i4 * size2) / i3, size2);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Log.d("Face", "AutoFitCameraPreviewTextureView left:" + i + ",top:" + i2 + ",right:" + i3 + ",bottom:" + i4 + ",changed:" + z);
        if (z && i != i3 && i2 != i4) {
            initMaskArea();
        }
    }

    private void initMaskArea() {
        this.mRadius = (float) (getWidth() / 2);
        this.mCx = ((float) getWidth()) / 2.0f;
        float height = ((float) getHeight()) / 2.0f;
        this.mCy = height;
        float f = this.mCx;
        float f2 = this.mRadius;
        this.mLeft = (int) (f - f2);
        this.mTop = (int) (height - f2);
        this.mRight = (int) (f + f2);
        this.mBottom = (int) (height + f2);
        this.mParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        Log.d("Face", "AutoFitCameraPreviewTextureView initMaskArea mLeft:" + this.mLeft + ",mTop:" + this.mTop + ",mRight:" + this.mRight + ",mBottom:" + this.mBottom + ",mRadius:" + this.mRadius);
    }

    public CircleProperties getCircleProperties() {
        return new CircleProperties(this.mLeft, this.mTop, this.mRight, this.mBottom, this.mRadius, this.mCx, this.mCy, this.mParams);
    }

    public void setCircleOutlineProvider() {
        setOutlineProvider(this.mProvider);
        setClipToOutline(true);
    }

    public void setSurfaceTextureAvailableListener(OnSurfaceTextureAvailableListener onSurfaceTextureAvailableListener) {
        this.mSurfaceTextureAvailableListener = onSurfaceTextureAvailableListener;
    }

    public void setSurfaceTextureDefaultBufferSize() {
        StringBuilder sb = new StringBuilder();
        sb.append("AutoFitCameraPreviewTextureView SurfaceTexture is : ");
        sb.append(getSurfaceTexture() != null);
        Log.d("Face", sb.toString());
        if (getSurfaceTexture() != null) {
            getSurfaceTexture().setDefaultBufferSize(this.mPreviewWidth, this.mPreviewHeight);
        }
    }

    public float getPreviewScaleX() {
        return this.mPreviewScaleX;
    }

    public float getPreviewScaleY() {
        return this.mPreviewScaleY;
    }
}
