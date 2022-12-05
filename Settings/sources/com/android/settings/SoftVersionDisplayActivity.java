package com.android.settings;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.BidiFormatter;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
/* loaded from: classes.dex */
public class SoftVersionDisplayActivity extends Activity {
    private TextView mAndroidVersion;
    private String mAndroidVersionText;
    private TextView mBasebandVersion;
    private String mBasebandVersionText;
    private TextView mBuildVersion;
    private String mBuildVersionText;
    private TextView mKernel;
    private String mKernelText;
    private TextView mModel;
    private String mModelText;
    private TextView mPsn;
    private String mPsnText;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_soft_version);
        initView();
        initData();
        initText();
    }

    private void initData() {
        this.mModelText = getModel();
        this.mAndroidVersionText = getAndroidVersion();
        this.mBasebandVersionText = getBasebandVersion();
        this.mKernelText = getKernel();
        this.mBuildVersionText = getBuildVersion();
        this.mPsnText = getPsn();
        Log.w("SoftVersionDisplayActivity", "initData mPsnText = " + this.mPsnText);
        if ("".equals(this.mPsnText)) {
            this.mPsnText = getString(R.string.device_info_default);
        }
    }

    private String getPsn() {
        return ReadPSN();
    }

    private String getBuildVersion() {
        return BidiFormatter.getInstance().unicodeWrap(Build.DISPLAY).toString();
    }

    private String getKernel() {
        return SystemProperties.get("ro.kernel.version");
    }

    private String getBasebandVersion() {
        return getBaseband().toString();
    }

    private String getAndroidVersion() {
        return Build.VERSION.RELEASE_OR_CODENAME.toString();
    }

    private String getModel() {
        return SystemProperties.get("ro.product.model");
    }

    private void initText() {
        this.mModel.setText(this.mModelText);
        this.mAndroidVersion.setText(this.mAndroidVersionText);
        this.mBasebandVersion.setText(this.mBasebandVersionText);
        this.mKernel.setText(this.mKernelText);
        this.mBuildVersion.setText(this.mBuildVersionText);
        this.mPsn.setText(this.mPsnText);
    }

    private void initView() {
        this.mModel = (TextView) findViewById(R.id.tv_model);
        this.mAndroidVersion = (TextView) findViewById(R.id.tv_android_version);
        this.mBasebandVersion = (TextView) findViewById(R.id.tv_baseband_version);
        this.mKernel = (TextView) findViewById(R.id.tv_kernel);
        this.mBuildVersion = (TextView) findViewById(R.id.tv_build_version);
        this.mPsn = (TextView) findViewById(R.id.tv_psn);
    }

    private String ReadPSN() {
        File file = new File("/data/config/PSN.txt");
        String str = "";
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    str = str + readLine + "\n";
                }
                fileInputStream.close();
            } catch (FileNotFoundException unused) {
                Log.d("SoftVersionDisplayActivity", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("SoftVersionDisplayActivity", e.getMessage());
            }
        } else {
            Log.d("SoftVersionDisplayActivity", "The File doesn't not exist.");
        }
        Log.d("SoftVersionDisplayActivity", "content " + str);
        return str;
    }

    public CharSequence getBaseband() {
        String str;
        String[] split;
        if (Utils.isSupportCTPA(getApplicationContext()) && (str = SystemProperties.get("gsm.version.baseband", getString(R.string.device_info_default))) != null && (split = str.split(",")) != null && split.length > 0) {
            return split[0];
        }
        return SystemProperties.get("gsm.version.baseband", getString(R.string.device_info_default));
    }
}
