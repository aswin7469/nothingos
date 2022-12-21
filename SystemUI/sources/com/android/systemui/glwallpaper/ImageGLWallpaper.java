package com.android.systemui.glwallpaper;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.p026io.FileDescriptor;
import java.p026io.PrintWriter;

class ImageGLWallpaper {
    private static final String A_POSITION = "aPosition";
    private static final String A_TEXTURE_COORDINATES = "aTextureCoordinates";
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final String TAG = "ImageGLWallpaper";
    private static final float[] TEXTURES = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    private static final String U_TEXTURE = "uTexture";
    private static final float[] VERTICES = {-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f};
    private int mAttrPosition;
    private int mAttrTextureCoordinates;
    private final ImageGLProgram mProgram;
    private final FloatBuffer mTextureBuffer;
    private int mTextureId;
    private int mUniTexture;
    private final FloatBuffer mVertexBuffer;

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    ImageGLWallpaper(ImageGLProgram imageGLProgram) {
        this.mProgram = imageGLProgram;
        float[] fArr = VERTICES;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mVertexBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        float[] fArr2 = TEXTURES;
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureBuffer = asFloatBuffer2;
        asFloatBuffer2.put(fArr2);
        asFloatBuffer2.position(0);
    }

    /* access modifiers changed from: package-private */
    public void setup(Bitmap bitmap) {
        setupAttributes();
        setupUniforms();
        setupTexture(bitmap);
    }

    private void setupAttributes() {
        this.mAttrPosition = this.mProgram.getAttributeHandle(A_POSITION);
        this.mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mAttrPosition, 2, 5126, false, 0, this.mVertexBuffer);
        GLES20.glEnableVertexAttribArray(this.mAttrPosition);
        this.mAttrTextureCoordinates = this.mProgram.getAttributeHandle(A_TEXTURE_COORDINATES);
        this.mTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mAttrTextureCoordinates, 2, 5126, false, 0, this.mTextureBuffer);
        GLES20.glEnableVertexAttribArray(this.mAttrTextureCoordinates);
    }

    private void setupUniforms() {
        this.mUniTexture = this.mProgram.getUniformHandle(U_TEXTURE);
    }

    /* access modifiers changed from: package-private */
    public void draw() {
        GLES20.glDrawArrays(4, 0, VERTICES.length / 2);
    }

    private void setupTexture(Bitmap bitmap) {
        int[] iArr = new int[1];
        if (bitmap == null || bitmap.isRecycled()) {
            Log.w(TAG, "setupTexture: invalid bitmap");
            return;
        }
        GLES20.glGenTextures(1, iArr, 0);
        int i = iArr[0];
        if (i == 0) {
            Log.w(TAG, "setupTexture: glGenTextures() failed");
            return;
        }
        try {
            GLES20.glBindTexture(3553, i);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            this.mTextureId = iArr[0];
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Failed uploading texture: " + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public void useTexture() {
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.mTextureId);
        GLES20.glUniform1i(this.mUniTexture, 0);
    }
}
