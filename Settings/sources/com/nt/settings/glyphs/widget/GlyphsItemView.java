package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.settings.R;
import com.nt.settings.glyphs.utils.GlyphsLedDataProvider;
import com.nt.settings.glyphs.utils.LedAnimUtils;
import com.nt.settings.glyphs.widget.bean.GlyphsLedAnimPoint;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsItemView extends FrameLayout implements LedAnimUtils.OnAnimCallback {
    private ImageView imgLed1;
    private ImageView imgLed2;
    private ImageView imgLed3;
    private ImageView imgLed4;
    private ImageView imgLed5;
    private LedAnimUtils ledAnimUtils;
    private List<GlyphsLedAnimPoint> mAnims;
    private String mCurrentPath;
    private GlyphsLedDataProvider mGlyphsLedDataProvider;
    private OnAnimListener mListener;
    long time;

    /* loaded from: classes2.dex */
    public interface OnAnimListener {
        void onFinish(View view);

        void onStart(View view);
    }

    public GlyphsItemView(Context context) {
        this(context, null, 0, 0);
    }

    public GlyphsItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public GlyphsItemView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public GlyphsItemView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCurrentPath = "glyphs/default.csv";
        this.time = 0L;
        init(context, attributeSet, i, i2);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        LayoutInflater.from(context).inflate(R.layout.nt_glyphs_item_led_pager, this);
        this.imgLed1 = (ImageView) findViewById(R.id.img_led1);
        this.imgLed2 = (ImageView) findViewById(R.id.img_led2);
        this.imgLed3 = (ImageView) findViewById(R.id.img_led3);
        this.imgLed4 = (ImageView) findViewById(R.id.img_led4);
        this.imgLed5 = (ImageView) findViewById(R.id.img_led5);
        this.mGlyphsLedDataProvider = new GlyphsLedDataProvider();
        initAnimConfig();
    }

    private void initAnimConfig() {
        if (this.mGlyphsLedDataProvider == null) {
            this.mGlyphsLedDataProvider = new GlyphsLedDataProvider();
        }
        this.ledAnimUtils = new LedAnimUtils(getContext());
    }

    public void startAnim() {
        this.ledAnimUtils.setCallback(this);
        this.ledAnimUtils.start();
    }

    public void startAnim(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mCurrentPath = str;
        }
        this.ledAnimUtils.updateLeds(getContext(), this.mCurrentPath, new LedAnimUtils.OnDataLoadCallback() { // from class: com.nt.settings.glyphs.widget.GlyphsItemView.1
            @Override // com.nt.settings.glyphs.utils.LedAnimUtils.OnDataLoadCallback
            public void onDataLoaded(List<GlyphsLedAnimPoint> list) {
                GlyphsItemView.this.startAnim();
            }
        });
    }

    public void release() {
        this.ledAnimUtils.stop();
        this.ledAnimUtils.setCallback(null);
    }

    @Override // com.nt.settings.glyphs.utils.LedAnimUtils.OnAnimCallback
    public void onAnimStart() {
        this.imgLed1.setImageResource(R.drawable.ic_default_led1);
        this.imgLed2.setImageResource(R.drawable.ic_default_led2);
        this.imgLed3.setImageResource(R.drawable.ic_default_led3);
        this.imgLed4.setImageResource(R.drawable.ic_default_led4);
        this.imgLed5.setImageResource(R.drawable.ic_default_led5);
        this.mAnims = this.ledAnimUtils.getLedAnims();
        OnAnimListener onAnimListener = this.mListener;
        if (onAnimListener != null) {
            onAnimListener.onStart(this);
        }
    }

    @Override // com.nt.settings.glyphs.utils.LedAnimUtils.OnAnimCallback
    public void onAnimRefresh(int i) {
        this.time = System.currentTimeMillis();
        setLedDrawable(this.imgLed1, R.drawable.ic_glyphs_led1, R.drawable.ic_default_led1, this.mAnims.get(i).getValue(0));
        setLedDrawable(this.imgLed2, R.drawable.ic_glyphs_led2, R.drawable.ic_default_led2, this.mAnims.get(i).getValue(1));
        setLedDrawable(this.imgLed3, R.drawable.ic_glyphs_led3, R.drawable.ic_default_led3, this.mAnims.get(i).getValue(2));
        setLedDrawable(this.imgLed4, R.drawable.ic_glyphs_led4, R.drawable.ic_default_led4, this.mAnims.get(i).getValue(3));
        setLedDrawable(this.imgLed5, R.drawable.ic_glyphs_led5, R.drawable.ic_default_led5, this.mAnims.get(i).getValue(4));
    }

    @Override // com.nt.settings.glyphs.utils.LedAnimUtils.OnAnimCallback
    public void onAnimFinish() {
        this.imgLed1.setImageResource(R.drawable.ic_default_led1);
        this.imgLed2.setImageResource(R.drawable.ic_default_led2);
        this.imgLed3.setImageResource(R.drawable.ic_default_led3);
        this.imgLed4.setImageResource(R.drawable.ic_default_led4);
        this.imgLed5.setImageResource(R.drawable.ic_default_led5);
        OnAnimListener onAnimListener = this.mListener;
        if (onAnimListener != null) {
            onAnimListener.onFinish(this);
        }
    }

    private void setLedDrawable(ImageView imageView, int i, int i2, int i3) {
        if (i3 <= 0) {
            imageView.setImageResource(i2);
            return;
        }
        imageView.setImageResource(i);
        imageView.getDrawable().setAlpha(i3);
    }

    public void setOnAnimListener(OnAnimListener onAnimListener) {
        this.mListener = onAnimListener;
    }
}
