package com.android.systemui.biometrics;

import android.util.Log;
import android.util.Slog;
import android.view.Surface;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: classes.dex */
public class NtHBMProviderImpl implements UdfpsHbmProvider {
    private boolean mHbmEnabled;

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void enableHbm(int i, Surface surface, Runnable runnable) {
        if (this.mHbmEnabled) {
            return;
        }
        runnable.run();
        writeHbmNode(true);
        this.mHbmEnabled = true;
        Log.i("NtHBMProviderImpl", "------HBM ENABLED-1---------");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void disableHbm(Runnable runnable) {
        if (!this.mHbmEnabled) {
            return;
        }
        if (runnable != null) {
            runnable.run();
        }
        writeHbmNode(false);
        this.mHbmEnabled = false;
        Log.i("NtHBMProviderImpl", "------HBM DISABLED---------");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void disableHbm() {
        writeHbmNode(false);
        Log.i("NtHBMProviderImpl", "------HBM DISABLED-------disableHbm--");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void enableHbm() {
        if (this.mHbmEnabled) {
            return;
        }
        writeHbmNode(true);
        this.mHbmEnabled = true;
        Log.i("NtHBMProviderImpl", "------HBM ENABLED----------");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public boolean isHbmEnabled() {
        return this.mHbmEnabled;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0082 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeHbmNode(boolean z) {
        FileOutputStream fileOutputStream;
        IOException e;
        boolean z2;
        String str = z ? "1" : "0";
        FileOutputStream fileOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream("/sys/class/drm/sde-conn-1-DSI-1/hbm_mode");
            try {
                try {
                    Slog.d("NtHBMProviderImpl", "start write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:" + str);
                    fileOutputStream.write(str.getBytes("US-ASCII"));
                    z2 = true;
                    try {
                        fileOutputStream.close();
                    } catch (IOException unused) {
                    }
                } catch (IOException e2) {
                    e = e2;
                    Slog.e("NtHBMProviderImpl", "Unable to write /sys/class/drm/sde-conn-1-DSI-1/hbm_mode" + e.getMessage());
                    e.printStackTrace();
                    writeOldHbmNode(z);
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException unused2) {
                        }
                    }
                    z2 = false;
                    Slog.d("NtHBMProviderImpl", "end write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:" + str + ",  result: " + z2);
                }
            } catch (Throwable th) {
                th = th;
                fileOutputStream2 = fileOutputStream;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            fileOutputStream = null;
            e = e3;
        } catch (Throwable th2) {
            th = th2;
            if (fileOutputStream2 != null) {
            }
            throw th;
        }
        Slog.d("NtHBMProviderImpl", "end write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:" + str + ",  result: " + z2);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x007f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeOldHbmNode(boolean z) {
        FileOutputStream fileOutputStream;
        IOException e;
        boolean z2;
        String str = z ? "1" : "0";
        FileOutputStream fileOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream("/sys/class/backlight/panel0-backlight/hbm_mode");
            try {
                try {
                    Slog.d("NtHBMProviderImpl", "start write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:" + str);
                    fileOutputStream.write(str.getBytes("US-ASCII"));
                    z2 = true;
                    try {
                        fileOutputStream.close();
                    } catch (IOException unused) {
                    }
                } catch (IOException e2) {
                    e = e2;
                    Slog.e("NtHBMProviderImpl", "Unable to write /sys/class/backlight/panel0-backlight/hbm_mode" + e.getMessage());
                    e.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException unused2) {
                        }
                    }
                    z2 = false;
                    Slog.d("NtHBMProviderImpl", "end write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:" + str + ",  result: " + z2);
                }
            } catch (Throwable th) {
                th = th;
                fileOutputStream2 = fileOutputStream;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            fileOutputStream = null;
            e = e3;
        } catch (Throwable th2) {
            th = th2;
            if (fileOutputStream2 != null) {
            }
            throw th;
        }
        Slog.d("NtHBMProviderImpl", "end write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:" + str + ",  result: " + z2);
    }
}
