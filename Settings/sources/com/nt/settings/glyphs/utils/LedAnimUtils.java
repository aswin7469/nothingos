package com.nt.settings.glyphs.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.nt.settings.glyphs.widget.bean.GlyphsLedAnimPoint;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class LedAnimUtils {
    private OnAnimCallback mCallback;
    private Runnable mCountDownRunnable;
    private GlyphsLedDataProvider mGlyphsLedDataProvider;
    private int mIndex;
    private String mLedPath;
    private List<GlyphsLedAnimPoint> mLeds;
    private Handler mMainHandler;

    /* loaded from: classes2.dex */
    public interface OnAnimCallback {
        void onAnimFinish();

        void onAnimRefresh(int i);

        void onAnimStart();
    }

    /* loaded from: classes2.dex */
    public interface OnDataLoadCallback {
        void onDataLoaded(List<GlyphsLedAnimPoint> list);
    }

    static /* synthetic */ int access$008(LedAnimUtils ledAnimUtils) {
        int i = ledAnimUtils.mIndex;
        ledAnimUtils.mIndex = i + 1;
        return i;
    }

    public List<GlyphsLedAnimPoint> getLedAnims() {
        return this.mLeds;
    }

    private LedAnimUtils() {
        this.mLeds = new ArrayList();
        this.mLedPath = "glyphs/default.csv";
        this.mIndex = 0;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mCountDownRunnable = new Runnable() { // from class: com.nt.settings.glyphs.utils.LedAnimUtils.1
            @Override // java.lang.Runnable
            public void run() {
                if (LedAnimUtils.this.mIndex >= LedAnimUtils.this.mLeds.size()) {
                    LedAnimUtils.this.mIndex = 0;
                    if (LedAnimUtils.this.mCallback == null) {
                        return;
                    }
                    LedAnimUtils.this.mCallback.onAnimFinish();
                    return;
                }
                LedAnimUtils.this.mMainHandler.postDelayed(this, 16L);
                if (LedAnimUtils.this.mCallback != null) {
                    LedAnimUtils.this.mCallback.onAnimRefresh(LedAnimUtils.this.mIndex);
                }
                LedAnimUtils.access$008(LedAnimUtils.this);
            }
        };
    }

    public LedAnimUtils(Context context) {
        this(context, null, null);
    }

    public LedAnimUtils(Context context, String str, OnDataLoadCallback onDataLoadCallback) {
        this();
        if (this.mGlyphsLedDataProvider == null) {
            this.mGlyphsLedDataProvider = new GlyphsLedDataProvider();
        }
        updateLeds(context, str, onDataLoadCallback);
    }

    public void updateLeds(final Context context, String str, final OnDataLoadCallback onDataLoadCallback) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mLedPath = str;
        com.android.settingslib.utils.ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.utils.LedAnimUtils.2
            @Override // java.lang.Runnable
            public void run() {
                LedAnimUtils ledAnimUtils = LedAnimUtils.this;
                ledAnimUtils.mLeds = ledAnimUtils.mGlyphsLedDataProvider.getLedAnim(context, LedAnimUtils.this.mLedPath);
                com.android.settingslib.utils.ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.utils.LedAnimUtils.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        OnDataLoadCallback onDataLoadCallback2 = onDataLoadCallback;
                        if (onDataLoadCallback2 != null) {
                            onDataLoadCallback2.onDataLoaded(LedAnimUtils.this.mLeds);
                        }
                    }
                });
            }
        });
    }

    public void stop() {
        this.mIndex = 0;
        this.mMainHandler.removeCallbacksAndMessages(null);
        OnAnimCallback onAnimCallback = this.mCallback;
        if (onAnimCallback != null) {
            onAnimCallback.onAnimFinish();
            this.mCallback = null;
        }
    }

    public void start() {
        List<GlyphsLedAnimPoint> list = this.mLeds;
        if (list == null || list.size() == 0) {
            new Exception();
        }
        OnAnimCallback onAnimCallback = this.mCallback;
        if (onAnimCallback != null) {
            onAnimCallback.onAnimStart();
        }
        this.mIndex = 0;
        this.mMainHandler.removeCallbacksAndMessages(null);
        this.mMainHandler.post(this.mCountDownRunnable);
        Log.d("LedAnim", "LED ANIM start");
    }

    public void setCallback(OnAnimCallback onAnimCallback) {
        if (onAnimCallback == null) {
            this.mMainHandler.removeCallbacks(this.mCountDownRunnable);
            this.mCallback = null;
            return;
        }
        this.mCallback = onAnimCallback;
    }
}
