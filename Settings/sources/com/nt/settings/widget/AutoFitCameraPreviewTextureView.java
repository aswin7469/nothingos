package com.nt.settings.widget;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
/* loaded from: classes2.dex */
public class AutoFitCameraPreviewTextureView extends TextureView {
    private int mBottom;
    private float mCx;
    private float mCy;
    private int mLeft;
    private ViewGroup.MarginLayoutParams mParams;
    private int mPreviewHeight;
    private float mPreviewScaleX;
    private float mPreviewScaleY;
    private int mPreviewWidth;
    private ViewOutlineProvider mProvider;
    private float mRadius;
    private int mRatioHeight;
    private int mRatioWidth;
    private int mRight;
    private OnSurfaceTextureAvailableListener mSurfaceTextureAvailableListener;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener;
    private int mTop;

    /* loaded from: classes2.dex */
    public interface OnSurfaceTextureAvailableListener {
        void onSurfaceTextureAvailableCallback(SurfaceTexture surfaceTexture, int i, int i2);
    }

    public AutoFitCameraPreviewTextureView(Context context) {
        this(context, null);
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
        this.mProvider = new ViewOutlineProvider() { // from class: com.nt.settings.widget.AutoFitCameraPreviewTextureView.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                Log.d("CameraPreviewTexture", " getOutline mLeft:" + AutoFitCameraPreviewTextureView.this.mLeft + ",mTop:" + AutoFitCameraPreviewTextureView.this.mTop + ",mRight:" + AutoFitCameraPreviewTextureView.this.mRight + ",mBottom:" + AutoFitCameraPreviewTextureView.this.mBottom);
                outline.setOval(AutoFitCameraPreviewTextureView.this.mLeft, AutoFitCameraPreviewTextureView.this.mTop, AutoFitCameraPreviewTextureView.this.mRight, AutoFitCameraPreviewTextureView.this.mBottom);
            }
        };
        TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() { // from class: com.nt.settings.widget.AutoFitCameraPreviewTextureView.2
            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i2, int i3) {
                Log.d("CameraPreviewTexture", "onSurfaceTextureAvailable width:" + i2 + ",height:" + i3 + "");
                if (surfaceTexture != null) {
                    AutoFitCameraPreviewTextureView.this.setSurfaceTextureDefaultBufferSize();
                    if (AutoFitCameraPreviewTextureView.this.mSurfaceTextureAvailableListener != null) {
                        AutoFitCameraPreviewTextureView.this.mSurfaceTextureAvailableListener.onSurfaceTextureAvailableCallback(surfaceTexture, i2, i3);
                    }
                } else {
                    Log.e("CameraPreviewTexture", "onSurfaceTextureAvailable texture == null");
                }
                if (i2 > i3) {
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView.mPreviewScaleX = i2 / autoFitCameraPreviewTextureView.mPreviewWidth;
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView2 = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView2.mPreviewScaleY = i3 / autoFitCameraPreviewTextureView2.mPreviewHeight;
                    return;
                }
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView3 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView3.mPreviewScaleX = i2 / autoFitCameraPreviewTextureView3.mPreviewHeight;
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView4 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView4.mPreviewScaleY = i3 / autoFitCameraPreviewTextureView4.mPreviewWidth;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i2, int i3) {
                Log.d("CameraPreviewTexture", "onSurfaceTextureSizeChanged width:" + i2 + ",height:" + i3);
                if (surfaceTexture != null) {
                    surfaceTexture.setDefaultBufferSize(AutoFitCameraPreviewTextureView.this.mPreviewHeight, AutoFitCameraPreviewTextureView.this.mPreviewWidth);
                }
                if (i2 > i3) {
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView.mPreviewScaleX = i2 / autoFitCameraPreviewTextureView.mPreviewWidth;
                    AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView2 = AutoFitCameraPreviewTextureView.this;
                    autoFitCameraPreviewTextureView2.mPreviewScaleY = i3 / autoFitCameraPreviewTextureView2.mPreviewHeight;
                    return;
                }
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView3 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView3.mPreviewScaleX = i2 / autoFitCameraPreviewTextureView3.mPreviewHeight;
                AutoFitCameraPreviewTextureView autoFitCameraPreviewTextureView4 = AutoFitCameraPreviewTextureView.this;
                autoFitCameraPreviewTextureView4.mPreviewScaleY = i3 / autoFitCameraPreviewTextureView4.mPreviewWidth;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                Log.d("CameraPreviewTexture", "onSurfaceTextureDestroyed");
                return true;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                Log.d("CameraPreviewTexture", "onSurfaceTextureUpdated");
            }
        };
        this.mSurfaceTextureListener = surfaceTextureListener;
        ((TextureView) this).mContext = context;
        setSurfaceTextureListener(surfaceTextureListener);
    }

    public void setAspectRatio() {
        requestLayout();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int i4 = this.mRatioWidth;
        if (i4 == 0 || (i3 = this.mRatioHeight) == 0) {
            setMeasuredDimension(size, size2);
        } else if (size < (size2 * i4) / i3) {
            setMeasuredDimension(size, (i3 * size) / i4);
        } else {
            setMeasuredDimension((i4 * size2) / i3, size2);
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Log.d("CameraPreviewTexture", "left:" + i + ",top:" + i2 + ",right:" + i3 + ",bottom:" + i4 + ",changed:" + z);
        if (!z || i == i3 || i2 == i4) {
            return;
        }
        initMaskArea();
    }

    private void initMaskArea() {
        this.mRadius = getWidth() / 2;
        this.mCx = getWidth() / 2.0f;
        float height = getHeight() / 2.0f;
        this.mCy = height;
        float f = this.mCx;
        float f2 = this.mRadius;
        this.mLeft = (int) (f - f2);
        this.mTop = (int) (height - f2);
        this.mRight = (int) (f + f2);
        this.mBottom = (int) (height + f2);
        this.mParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        Log.d("CameraPreviewTexture", "mLeft:" + this.mLeft + ",mTop:" + this.mTop + ",mRight:" + this.mRight + ",mBottom:" + this.mBottom + ",mRadius:" + this.mRadius);
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
        sb.append(" SurfaceTexture is : ");
        sb.append(getSurfaceTexture() != null);
        Log.d("CameraPreviewTexture", sb.toString());
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
