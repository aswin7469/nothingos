package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;
import com.android.settings.core.InstrumentedFragment;
import com.android.settingslib.R$dimen;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.R$string;
import com.android.settingslib.qrcode.QrCamera;

public class QrCodeScanModeFragment extends InstrumentedFragment implements TextureView.SurfaceTextureListener, QrCamera.ScannerCallback {
    /* access modifiers changed from: private */
    public String mBroadcastMetadata;
    private QrCamera mCamera;
    private Context mContext;
    /* access modifiers changed from: private */
    public QrCodeScanModeController mController;
    /* access modifiers changed from: private */
    public int mCornerRadius;
    /* access modifiers changed from: private */
    public TextView mErrorMessage;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                QrCodeScanModeFragment.this.mErrorMessage.setVisibility(4);
            } else if (i == 2) {
                QrCodeScanModeFragment.this.mErrorMessage.setVisibility(0);
                QrCodeScanModeFragment.this.mErrorMessage.setText((String) message.obj);
                QrCodeScanModeFragment.this.mErrorMessage.sendAccessibilityEvent(32);
                removeMessages(1);
                sendEmptyMessageDelayed(1, 10000);
            } else if (i == 3) {
                QrCodeScanModeFragment.this.mController.addSource(QrCodeScanModeFragment.this.mSink, QrCodeScanModeFragment.this.mBroadcastMetadata, QrCodeScanModeFragment.this.mIsGroupOp);
                QrCodeScanModeFragment.this.updateSummary();
                QrCodeScanModeFragment.this.mSummary.sendAccessibilityEvent(32);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsGroupOp;
    /* access modifiers changed from: private */
    public BluetoothDevice mSink;
    /* access modifiers changed from: private */
    public TextView mSummary;
    private TextureView mTextureView;

    public int getMetricsCategory() {
        return 1926;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public QrCodeScanModeFragment(boolean z, BluetoothDevice bluetoothDevice) {
        this.mIsGroupOp = z;
        this.mSink = bluetoothDevice;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context context = getContext();
        this.mContext = context;
        this.mController = new QrCodeScanModeController(context);
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R$layout.qrcode_scanner_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        this.mTextureView = (TextureView) view.findViewById(R$id.preview_view);
        this.mCornerRadius = this.mContext.getResources().getDimensionPixelSize(R$dimen.qrcode_preview_radius);
        this.mTextureView.setSurfaceTextureListener(this);
        this.mTextureView.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) QrCodeScanModeFragment.this.mCornerRadius);
            }
        });
        this.mTextureView.setClipToOutline(true);
        this.mErrorMessage = (TextView) view.findViewById(R$id.error_message);
    }

    private void initCamera(SurfaceTexture surfaceTexture) {
        if (this.mCamera == null) {
            QrCamera qrCamera = new QrCamera(this.mContext, this);
            this.mCamera = qrCamera;
            qrCamera.start(surfaceTexture);
        }
    }

    private void destroyCamera() {
        QrCamera qrCamera = this.mCamera;
        if (qrCamera != null) {
            qrCamera.stop();
            this.mCamera = null;
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        initCamera(surfaceTexture);
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        destroyCamera();
        return true;
    }

    public void handleSuccessfulResult(String str) {
        Log.d("QrCodeScanModeFragment", "handleSuccessfulResult(), get the qr code string.");
        this.mBroadcastMetadata = str;
        handleBtLeAudioScanner();
    }

    public void handleCameraFailure() {
        destroyCamera();
    }

    public Size getViewSize() {
        return new Size(this.mTextureView.getWidth(), this.mTextureView.getHeight());
    }

    public Rect getFramePosition(Size size, int i) {
        return new Rect(0, 0, size.getHeight(), size.getHeight());
    }

    public void setTransform(Matrix matrix) {
        this.mTextureView.setTransform(matrix);
    }

    public boolean isValid(String str) {
        if (str.startsWith("BT:")) {
            return true;
        }
        showErrorMessage(R$string.bt_le_audio_qr_code_is_not_valid_format);
        return false;
    }

    private void showErrorMessage(int i) {
        this.mHandler.obtainMessage(2, getString(i)).sendToTarget();
    }

    private void handleBtLeAudioScanner() {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3), 1000);
    }

    /* access modifiers changed from: private */
    public void updateSummary() {
        this.mSummary.setText(getString(R$string.bt_le_audio_scan_qr_code_scanner));
    }
}
