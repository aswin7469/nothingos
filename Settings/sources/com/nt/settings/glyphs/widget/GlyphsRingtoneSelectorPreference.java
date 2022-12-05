package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.settings.R;
import com.android.settings.R$styleable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsRingtoneSelectorPreference extends Preference {
    private final String KEY_GLOBAL_TEST_RINGTONE_LED_DELAY_TIME;
    private int i;
    private AudioManager mAudioManager;
    private Context mContext;
    private int mCurrentIndex;
    private android.media.Ringtone mCurrentRingtone;
    private Handler mHandler;
    private PreferenceViewHolder mHolder;
    private boolean mIsLoop;
    private boolean mIsPlaying;
    private int mNewPosition;
    private int mOldPosition;
    private PlayLedRunnable mPlayLedRunnable;
    private Thread mRingtoneThread;
    private int mRingtoneType;
    private List<Ringtone> mRingtones;
    private boolean mSoundOnly;
    private Runnable mStopAudioRunnable;
    private TextView mTvRingtoneIndex;
    private List<GlyphsItemView> mViewHolders;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class PlayLedRunnable implements Runnable {
        private Context mContext;
        private String mTitle;
        private Uri mUri;

        private PlayLedRunnable() {
        }

        public void init(Context context, Uri uri, String str) {
            this.mContext = context;
            this.mUri = uri;
            this.mTitle = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d("RingtoneSelectorP", "START REAR LED ");
            GlyphsRingtoneSelectorPreference.this.playRingtoneLed(this.mContext, this.mUri, this.mTitle);
            Log.d("RingtoneSelectorP", "START FRONT LED ");
            GlyphsRingtoneSelectorPreference.this.loopStartCurrentItemAnim();
        }
    }

    public GlyphsRingtoneSelectorPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mRingtones = null;
        this.mCurrentIndex = 0;
        this.mNewPosition = 0;
        this.mOldPosition = -1;
        this.mSoundOnly = true;
        this.mIsPlaying = false;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mViewHolders = new ArrayList();
        this.KEY_GLOBAL_TEST_RINGTONE_LED_DELAY_TIME = "key_global_test_ringtone_led_delay_time";
        this.mPlayLedRunnable = new PlayLedRunnable();
        this.mStopAudioRunnable = new Runnable() { // from class: com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference.1
            @Override // java.lang.Runnable
            public void run() {
                GlyphsRingtoneSelectorPreference.this.audioLedStop();
            }
        };
        this.i = 3;
        init(context, attributeSet, i, i2);
    }

    public GlyphsRingtoneSelectorPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public GlyphsRingtoneSelectorPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public GlyphsRingtoneSelectorPreference(Context context) {
        this(context, null, 0, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        this.mContext = context;
        setLayoutResource(R.layout.nt_glyphs_ringtone_preference_view_pager);
        setSelectable(false);
        this.mIsLoop = context.obtainStyledAttributes(attributeSet, R$styleable.ImagePreference, i, i2).getBoolean(R$styleable.NtRingtoneSelectorPreference_isLoop, false);
        initHolder();
    }

    private void initHolder() {
        for (int i = 0; i < 3; i++) {
            this.mViewHolders.add(createView());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GlyphsItemView getDilHolder() {
        GlyphsItemView glyphsItemView;
        Iterator<GlyphsItemView> it = this.mViewHolders.iterator();
        while (true) {
            if (!it.hasNext()) {
                glyphsItemView = null;
                break;
            }
            glyphsItemView = it.next();
            if (glyphsItemView.getParent() == null) {
                break;
            }
        }
        if (glyphsItemView == null) {
            GlyphsItemView createView = createView();
            this.mViewHolders.add(createView);
            return createView;
        }
        return glyphsItemView;
    }

    private GlyphsItemView createView() {
        GlyphsItemView glyphsItemView = new GlyphsItemView(this.mContext);
        glyphsItemView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (GlyphsRingtoneSelectorPreference.this.mCurrentRingtone == null || GlyphsRingtoneSelectorPreference.this.mCurrentRingtone.isPlaying() || view.getTag() == null) {
                    return;
                }
                String obj = view.getTag().toString();
                if (!TextUtils.equals(obj, "position_" + GlyphsRingtoneSelectorPreference.this.mCurrentIndex)) {
                    return;
                }
                GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = GlyphsRingtoneSelectorPreference.this;
                glyphsRingtoneSelectorPreference.playRingTone(glyphsRingtoneSelectorPreference.getContext(), GlyphsRingtoneSelectorPreference.this.mCurrentRingtone.getUri(), GlyphsRingtoneSelectorPreference.this.mCurrentRingtone.getTitle(GlyphsRingtoneSelectorPreference.this.getContext()));
            }
        });
        return glyphsItemView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAll() {
        for (GlyphsItemView glyphsItemView : this.mViewHolders) {
            glyphsItemView.release();
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mHolder = preferenceViewHolder;
        release();
    }

    public void onPause() {
        this.mIsPlaying = false;
        try {
            stopAll();
            Thread thread = this.mRingtoneThread;
            if (thread != null && thread.isAlive()) {
                this.mRingtoneThread.stop();
                this.mRingtoneThread = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        release();
    }

    private int dp2Px(float f) {
        return (int) TypedValue.applyDimension(1, f, this.mContext.getResources().getDisplayMetrics());
    }

    private void refresh() {
        List<Ringtone> list = this.mRingtones;
        if (list == null || list.size() == 0) {
            return;
        }
        if (this.mHolder == null) {
            notifyChanged();
            return;
        }
        int width = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getWidth();
        final ViewPager viewPager = (ViewPager) this.mHolder.findViewById(R.id.vp_content);
        final TextView textView = (TextView) this.mHolder.findViewById(R.id.tv_ringtone_name);
        this.mTvRingtoneIndex = (TextView) this.mHolder.findViewById(R.id.tv_ringtone_index);
        viewPager.setPageMargin((width - dp2Px(257.0f)) / 2);
        viewPager.setPadding((width - dp2Px(177.0f)) / 2, 0, (width - dp2Px(177.0f)) / 2, 0);
        viewPager.setAdapter(new RingtoneAdapter(getContext(), this.mRingtones));
        if (this.mRingtones.size() > 2) {
            viewPager.setOffscreenPageLimit(3);
        }
        if (this.mRingtones.size() > 0) {
            textView.setText(this.mRingtones.get(this.mCurrentIndex).getTitle());
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference.3
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                GlyphsRingtoneSelectorPreference.this.mHandler.removeCallbacksAndMessages(null);
                GlyphsRingtoneSelectorPreference.this.stopAll();
                GlyphsRingtoneSelectorPreference.this.mNewPosition = i;
                GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = GlyphsRingtoneSelectorPreference.this;
                glyphsRingtoneSelectorPreference.mOldPosition = glyphsRingtoneSelectorPreference.mCurrentIndex;
                GlyphsRingtoneSelectorPreference.this.mCurrentIndex = i;
                textView.setText(((Ringtone) GlyphsRingtoneSelectorPreference.this.mRingtones.get(i)).getTitle());
                GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference2 = GlyphsRingtoneSelectorPreference.this;
                glyphsRingtoneSelectorPreference2.setRingtoneIndex(glyphsRingtoneSelectorPreference2.mCurrentIndex, GlyphsRingtoneSelectorPreference.this.mRingtones.size());
                GlyphsRingtoneSelectorPreference.this.playRingTone(viewPager.getContext(), Uri.parse(((Ringtone) GlyphsRingtoneSelectorPreference.this.mRingtones.get(i)).getUri()), ((Ringtone) GlyphsRingtoneSelectorPreference.this.mRingtones.get(i)).getTitle());
            }
        });
        viewPager.setCurrentItem(this.mCurrentIndex);
        setRingtoneIndex(this.mCurrentIndex, this.mRingtones.size());
        this.mHandler.postDelayed(new Runnable() { // from class: com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference.4
            @Override // java.lang.Runnable
            public void run() {
                if (GlyphsRingtoneSelectorPreference.this.mRingtones == null || GlyphsRingtoneSelectorPreference.this.mRingtones.size() <= 0) {
                    return;
                }
                GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = GlyphsRingtoneSelectorPreference.this;
                glyphsRingtoneSelectorPreference.mNewPosition = glyphsRingtoneSelectorPreference.mCurrentIndex;
                GlyphsRingtoneSelectorPreference.this.mOldPosition = -1;
                GlyphsRingtoneSelectorPreference.this.playRingTone(viewPager.getContext(), Uri.parse(((Ringtone) GlyphsRingtoneSelectorPreference.this.mRingtones.get(GlyphsRingtoneSelectorPreference.this.mCurrentIndex)).getUri()), ((Ringtone) GlyphsRingtoneSelectorPreference.this.mRingtones.get(GlyphsRingtoneSelectorPreference.this.mCurrentIndex)).getTitle());
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRingtoneIndex(int i, int i2) {
        TextView textView = this.mTvRingtoneIndex;
        if (textView != null) {
            textView.setText(String.valueOf(i + 1) + "/" + i2);
        }
    }

    public void setSoundOnly(boolean z) {
        this.mSoundOnly = z;
        if (z) {
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                audioManager.setParameters(getLedStopParams());
            }
            stopCurrentAnim();
            return;
        }
        List<Ringtone> list = this.mRingtones;
        if (list == null || list.size() <= 0 || this.mHolder == null) {
            return;
        }
        playRingTone(getContext(), Uri.parse(this.mRingtones.get(this.mCurrentIndex).getUri()), this.mRingtones.get(this.mCurrentIndex).getTitle());
    }

    private void stopCurrentAnim() {
        PreferenceViewHolder preferenceViewHolder = this.mHolder;
        if (preferenceViewHolder == null || this.mCurrentIndex <= -1) {
            return;
        }
        GlyphsItemView glyphsItemView = (GlyphsItemView) ((ViewPager) preferenceViewHolder.findViewById(R.id.vp_content)).findViewWithTag("position_" + this.mCurrentIndex);
        if (glyphsItemView == null) {
            return;
        }
        glyphsItemView.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playRingTone(final Context context, final Uri uri, final String str) {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) context.getSystemService("audio");
        }
        getRingtoneThread(new Runnable() { // from class: com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference.5
            @Override // java.lang.Runnable
            public void run() {
                GlyphsRingtoneSelectorPreference.this.ringtoneStop();
                GlyphsRingtoneSelectorPreference.this.mIsPlaying = true;
                try {
                    GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = GlyphsRingtoneSelectorPreference.this;
                    glyphsRingtoneSelectorPreference.threadPlayRingTone(context, uri, glyphsRingtoneSelectorPreference.mIsLoop, str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startCurrentItemAnim(ViewPager viewPager, int i, int i2) {
        if (!this.mSoundOnly && viewPager != null) {
            GlyphsItemView glyphsItemView = (GlyphsItemView) viewPager.findViewWithTag("position_" + i);
            GlyphsItemView glyphsItemView2 = (GlyphsItemView) viewPager.findViewWithTag("position_" + this.mCurrentIndex);
            if (glyphsItemView2 != null) {
                glyphsItemView2.release();
            }
            if (glyphsItemView == null) {
                return;
            }
            List<Ringtone> list = this.mRingtones;
            if (list != null && list.size() > this.mCurrentIndex) {
                glyphsItemView.startAnim("glyphs/" + this.mRingtones.get(this.mCurrentIndex).getTitle().replaceAll(" ", "_") + ".csv");
                return;
            }
            new Throwable("mCurrentIndex is bigger than mRingtones.sze() or mRingtones is null");
        }
    }

    public Ringtone getCurrentRingtone() {
        List<Ringtone> list = this.mRingtones;
        if (list == null) {
            return null;
        }
        return list.get(this.mCurrentIndex);
    }

    public void setRingtoneType(int i) {
        this.mRingtoneType = i;
    }

    public void setIsLoop(boolean z) {
        this.mIsLoop = z;
    }

    public void release() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        ringtoneStop();
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            audioManager.setParameters(getLedStopParams());
        }
    }

    public void setData(List<Ringtone> list, int i) {
        this.mCurrentIndex = i;
        if (list == null || list.size() == 0) {
            return;
        }
        this.mRingtones = list;
        refresh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class RingtoneAdapter extends PagerAdapter {
        private List<Ringtone> mRingtones;

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return obj == view;
        }

        public RingtoneAdapter(Context context, List<Ringtone> list) {
            this.mRingtones = null;
            this.mRingtones = list;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            List<Ringtone> list = this.mRingtones;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            GlyphsItemView dilHolder = GlyphsRingtoneSelectorPreference.this.getDilHolder();
            dilHolder.setTag("position_" + i);
            viewGroup.addView(dilHolder);
            return dilHolder;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            GlyphsItemView glyphsItemView = (GlyphsItemView) obj;
            glyphsItemView.release();
            GlyphsRingtoneSelectorPreference.this.mViewHolders.add(glyphsItemView);
            viewGroup.removeView(glyphsItemView);
        }
    }

    /* loaded from: classes2.dex */
    public static class Ringtone {
        private String id;
        private String ledUri;
        private String path;
        private String title;
        private String uri;

        public Ringtone(String str, String str2) {
            this.uri = str;
            this.title = str2;
        }

        public Ringtone(String str, String str2, String str3, String str4, String str5) {
            this.uri = str;
            this.title = str2;
            this.ledUri = str3;
            this.path = str4;
            this.id = str5;
        }

        public String getId() {
            return this.id;
        }

        public String getTitle() {
            return this.title;
        }

        public String getUri() {
            return this.uri;
        }

        public String getPath() {
            return this.path;
        }
    }

    private void initRingtoneVolume(Context context, android.media.Ringtone ringtone) {
        if (ringtone == null) {
            return;
        }
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) context.getSystemService("audio");
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null || audioManager.getRingerMode() != 1) {
            return;
        }
        ringtone.setVolume(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playRingtoneLed(Context context, Uri uri, String str) {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) context.getSystemService("audio");
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null) {
            return;
        }
        if (this.mSoundOnly) {
            audioManager.setParameters(getLedSoundOnlyParams(uri, str));
            return;
        }
        audioManager.setParameters(getLedStopParams());
        Log.d("RingtoneSelectorP", "LED START");
        this.mAudioManager.setParameters(this.mRingtoneType == 1 ? getContactLedStartParams(uri, str) : getNotificationLedStartParams(uri, str));
    }

    private String getNotificationLedStartParams(Uri uri, String str) {
        if (uri == null) {
            return null;
        }
        return "TYPE=NOTIFICATION,NT_LED_URI=content://media" + uri.getPath() + "?title=" + str;
    }

    private String getContactLedStartParams(Uri uri, String str) {
        if (uri == null) {
            return null;
        }
        return "NT_LED_URI=content://media" + uri.getPath() + "?title=" + str;
    }

    private String getLedStopParams() {
        Log.d("RingtoneSelectorP", "getLedStopParams " + this.mCurrentRingtone);
        StringBuilder sb = new StringBuilder();
        sb.append("NT_LED_ACTION=STOP,TYPE=");
        sb.append(this.mRingtoneType == 1 ? "" : "NOTIFICATION");
        return sb.toString();
    }

    private String getLedSoundOnlyParams(Uri uri, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("LED=OFF,TYPE=");
        sb.append(this.mRingtoneType == 1 ? "" : "NOTIFICATION");
        sb.append(",NT_LED_URI=content://media");
        sb.append(uri.getPath());
        sb.append("?title=");
        sb.append(str);
        return sb.toString();
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        super.onDetached();
        stopAll();
        List<GlyphsItemView> list = this.mViewHolders;
        if (list != null) {
            list.clear();
        }
        release();
    }

    private synchronized Thread getRingtoneThread(Runnable runnable) {
        Thread thread;
        Thread thread2 = this.mRingtoneThread;
        if (thread2 != null) {
            thread2.interrupt();
        }
        thread = new Thread(runnable);
        this.mRingtoneThread = thread;
        return thread;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0031, code lost:
        if (android.provider.Settings.System.getInt(r5.mContext.getContentResolver(), "notification_vibration_intensity", 0) == 0) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void threadPlayRingTone(Context context, Uri uri, boolean z, String str) throws InterruptedException {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        try {
            this.mCurrentRingtone = RingtoneManager.getRingtone(context, uri);
            boolean z2 = false;
            if (this.mRingtoneType == 1) {
                if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 0) == 0) {
                    z2 = true;
                }
                AudioAttributes build = new AudioAttributes.Builder().setInternalLegacyStreamType(this.mRingtoneType == 1 ? 2 : 5).setHapticChannelsMuted(z2).build();
                initRingtoneVolume(this.mContext, this.mCurrentRingtone);
                this.mCurrentRingtone.setAudioAttributes(build);
                this.mCurrentRingtone.setOnMediaPlayerListener(new Ringtone.OnMediaPlayerListener() { // from class: com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference.6
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        GlyphsRingtoneSelectorPreference.this.mHandler.removeCallbacksAndMessages(null);
                        GlyphsRingtoneSelectorPreference.this.audioLedStop();
                        GlyphsRingtoneSelectorPreference.this.ringtoneStop();
                    }
                });
                ringtonePlay();
                this.mHandler.removeCallbacks(this.mStopAudioRunnable);
                this.mHandler.removeCallbacks(this.mPlayLedRunnable);
                int ledDelayTime = getLedDelayTime();
                this.mHandler.postDelayed(this.mStopAudioRunnable, this.mCurrentRingtone.getDuration() + ledDelayTime);
                Log.d("RingtoneSelectorP", "START LED delayTime " + ledDelayTime);
                this.mPlayLedRunnable.init(context, uri, str);
                this.mHandler.postDelayed(this.mPlayLedRunnable, (long) ledDelayTime);
                return;
            }
        } catch (Exception e) {
            Log.e("RingtoneSelectorP", "Failed to open ringtone " + uri + ": " + e);
            ringtoneStop();
        }
        Log.e("RingtoneSelectorP", "Failed to open ringtone " + uri + ": " + e);
        ringtoneStop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ringtoneStop() {
        android.media.Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null) {
            try {
                ringtone.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void audioLedStop() {
        if (this.mAudioManager != null) {
            Log.d("RingtoneSelectorP", "LED STOP");
            this.mAudioManager.setParameters(getLedStopParams());
        }
    }

    private void ringtonePlay() {
        if (this.mCurrentRingtone != null) {
            Log.d("RingtoneSelectorP", "ringtonePlay");
            this.mCurrentRingtone.play();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loopStartCurrentItemAnim() {
        PreferenceViewHolder preferenceViewHolder = this.mHolder;
        if (preferenceViewHolder != null) {
            int i = R.id.vp_content;
            if (preferenceViewHolder.findViewById(i) != null) {
                startCurrentItemAnim((ViewPager) this.mHolder.findViewById(i), this.mNewPosition, this.mOldPosition);
                return;
            }
        }
        Log.d("RingtoneSelectorP", "START FRONT LED mHolder " + this.mHolder);
    }

    private int getLedDelayTime() {
        if (this.mRingtoneType != 1) {
            return 0;
        }
        return Settings.Global.getInt(this.mContext.getContentResolver(), "key_global_test_ringtone_led_delay_time", 90);
    }
}
