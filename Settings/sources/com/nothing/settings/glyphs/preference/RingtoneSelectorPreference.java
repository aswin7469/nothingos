package com.nothing.settings.glyphs.preference;

import android.annotation.SuppressLint;
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
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$styleable;
import com.android.settings.notification.AudioHelper;
import com.nothing.settings.glyphs.utils.FastClickUtils;
import com.nothing.settings.glyphs.widget.GlyphsItemView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RingtoneSelectorPreference extends Preference {
    private final String KEY_GLOBAL_TEST_RINGTONE_LED_DELAY_TIME;
    private AudioManager mAudioManager;
    private Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentIndex;
    /* access modifiers changed from: private */
    public android.media.Ringtone mCurrentRingtone;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private AudioHelper mHelper;
    private PreferenceViewHolder mHolder;
    private boolean mIsLoop;
    /* access modifiers changed from: private */
    public int mNewPosition;
    /* access modifiers changed from: private */
    public int mOldPosition;
    private PlayLedRunnable mPlayLedRunnable;
    /* access modifiers changed from: private */
    public Runnable mRingtonePositionSyncTask;
    private Thread mRingtoneThread;
    private int mRingtoneType;
    /* access modifiers changed from: private */
    public List<Ringtone> mRingtones;
    private boolean mSoundOnly;
    private Runnable mStopAudioRunnable;
    private TextView mTvRingtoneIndex;
    /* access modifiers changed from: private */
    public List<GlyphsItemView> mViewHolders;

    private class PositionSyncTask implements Runnable {
        private PositionSyncTask() {
        }

        public void run() {
            if (RingtoneSelectorPreference.this.mCurrentRingtone.getCurrentPosition() > 0 || RingtoneSelectorPreference.this.isSystemSoundMute()) {
                RingtoneSelectorPreference.this.loopStartCurrentItemAnim();
                RingtoneSelectorPreference.this.mHandler.removeCallbacks(this);
                return;
            }
            RingtoneSelectorPreference.this.mHandler.postDelayed(RingtoneSelectorPreference.this.mRingtonePositionSyncTask, 5);
        }
    }

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

        public void run() {
            Log.d("RingtoneSelectorP", "START REAR LED ");
            RingtoneSelectorPreference.this.playRingtoneLed(this.mContext, this.mUri, this.mTitle);
            RingtoneSelectorPreference.this.ringtonePlay();
            Log.d("RingtoneSelectorP", "START FRONT LED ");
            RingtoneSelectorPreference.this.mHandler.removeCallbacks(RingtoneSelectorPreference.this.mRingtonePositionSyncTask);
            RingtoneSelectorPreference.this.mHandler.post(RingtoneSelectorPreference.this.mRingtonePositionSyncTask);
        }
    }

    public RingtoneSelectorPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mRingtonePositionSyncTask = new PositionSyncTask();
        this.mRingtones = null;
        this.mCurrentIndex = 0;
        this.mNewPosition = 0;
        this.mOldPosition = -1;
        this.mSoundOnly = true;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mViewHolders = new ArrayList();
        this.KEY_GLOBAL_TEST_RINGTONE_LED_DELAY_TIME = "key_global_test_ringtone_led_delay_time";
        this.mPlayLedRunnable = new PlayLedRunnable();
        this.mStopAudioRunnable = new RingtoneSelectorPreference$$ExternalSyntheticLambda0(this);
        init(context, attributeSet, i, i2);
        this.mHelper = new AudioHelper(context);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        audioLedStop();
    }

    public RingtoneSelectorPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RingtoneSelectorPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public RingtoneSelectorPreference(Context context) {
        this(context, (AttributeSet) null, 0, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        this.mContext = context;
        setLayoutResource(R$layout.glyphs_ringtone_preference_view_pager);
        setSelectable(false);
        this.mIsLoop = context.obtainStyledAttributes(attributeSet, R$styleable.ImagePreference, i, i2).getBoolean(R$styleable.RingtoneSelectorPreference_isLoop, false);
        initHolder();
    }

    private void initHolder() {
        for (int i = 0; i < 3; i++) {
            this.mViewHolders.add(createView());
        }
    }

    public GlyphsItemView getDilHolder() {
        GlyphsItemView glyphsItemView;
        Iterator<GlyphsItemView> it = this.mViewHolders.iterator();
        while (true) {
            if (it.hasNext()) {
                GlyphsItemView next = it.next();
                if (next.getParent() == null) {
                    glyphsItemView = next;
                    break;
                }
            } else {
                glyphsItemView = null;
                break;
            }
        }
        if (glyphsItemView != null) {
            return glyphsItemView;
        }
        GlyphsItemView createView = createView();
        this.mViewHolders.add(createView);
        return createView;
    }

    private GlyphsItemView createView() {
        GlyphsItemView glyphsItemView = new GlyphsItemView(this.mContext);
        glyphsItemView.setOnClickListener(new RingtoneSelectorPreference$$ExternalSyntheticLambda3(this));
        return glyphsItemView;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view) {
        if (!FastClickUtils.isFastClick() && playEnable() && view.getTag() != null) {
            String obj = view.getTag().toString();
            if (TextUtils.equals(obj, "position_" + this.mCurrentIndex)) {
                playRingTone(getContext(), this.mCurrentRingtone.getUri(), this.mCurrentRingtone.getTitle(getContext()));
            }
        }
    }

    public boolean playEnable() {
        return this.mCurrentRingtone != null;
    }

    public void stopAll() {
        for (GlyphsItemView release : this.mViewHolders) {
            release.release();
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mHolder = preferenceViewHolder;
        refresh();
    }

    public void onPause() {
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
        if (list != null && list.size() != 0) {
            if (this.mHolder == null) {
                notifyChanged();
                return;
            }
            int width = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getWidth();
            final ViewPager viewPager = (ViewPager) this.mHolder.findViewById(R$id.vp_content);
            final TextView textView = (TextView) this.mHolder.findViewById(R$id.tv_ringtone_name);
            this.mTvRingtoneIndex = (TextView) this.mHolder.findViewById(R$id.tv_ringtone_index);
            viewPager.setPageMargin((width - dp2Px(257.0f)) / 2);
            viewPager.setPadding((width - dp2Px(177.0f)) / 2, 0, (width - dp2Px(177.0f)) / 2, 0);
            viewPager.setAdapter(new RingtoneAdapter(getContext(), this.mRingtones));
            if (this.mRingtones.size() > 2) {
                viewPager.setOffscreenPageLimit(3);
            }
            if (this.mRingtones.size() > 0) {
                textView.setText(this.mRingtones.get(this.mCurrentIndex).getTitle());
            }
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int i) {
                }

                public void onPageScrolled(int i, float f, int i2) {
                }

                public void onPageSelected(int i) {
                    RingtoneSelectorPreference.this.mHandler.removeCallbacksAndMessages((Object) null);
                    RingtoneSelectorPreference.this.stopAll();
                    RingtoneSelectorPreference.this.mNewPosition = i;
                    RingtoneSelectorPreference ringtoneSelectorPreference = RingtoneSelectorPreference.this;
                    ringtoneSelectorPreference.mOldPosition = ringtoneSelectorPreference.mCurrentIndex;
                    RingtoneSelectorPreference.this.mCurrentIndex = i;
                    textView.setText(((Ringtone) RingtoneSelectorPreference.this.mRingtones.get(i)).getTitle());
                    RingtoneSelectorPreference ringtoneSelectorPreference2 = RingtoneSelectorPreference.this;
                    ringtoneSelectorPreference2.setRingtoneIndex(ringtoneSelectorPreference2.mCurrentIndex, RingtoneSelectorPreference.this.mRingtones.size());
                    RingtoneSelectorPreference.this.playRingTone(viewPager.getContext(), Uri.parse(((Ringtone) RingtoneSelectorPreference.this.mRingtones.get(i)).getUri()), ((Ringtone) RingtoneSelectorPreference.this.mRingtones.get(i)).getTitle());
                }
            });
            viewPager.setCurrentItem(this.mCurrentIndex);
            setRingtoneIndex(this.mCurrentIndex, this.mRingtones.size());
            this.mHandler.postDelayed(new RingtoneSelectorPreference$$ExternalSyntheticLambda1(this, viewPager), 500);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$2(ViewPager viewPager) {
        List<Ringtone> list = this.mRingtones;
        if (list != null && list.size() > 0) {
            this.mNewPosition = this.mCurrentIndex;
            this.mOldPosition = -1;
            playRingTone(viewPager.getContext(), Uri.parse(this.mRingtones.get(this.mCurrentIndex).getUri()), this.mRingtones.get(this.mCurrentIndex).getTitle());
        }
    }

    @SuppressLint({"SetTextI18n"})
    public void setRingtoneIndex(int i, int i2) {
        TextView textView = this.mTvRingtoneIndex;
        if (textView != null) {
            textView.setText((i + 1) + "/" + i2);
        }
    }

    public void setSoundOnly(boolean z) {
        this.mSoundOnly = z;
        if (z) {
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                audioManager.setParameters(getLedStopParams(this.mRingtoneType));
            }
            stopCurrentAnim();
            return;
        }
        List<Ringtone> list = this.mRingtones;
        if (list != null && list.size() > 0 && this.mHolder != null) {
            playRingTone(getContext(), Uri.parse(this.mRingtones.get(this.mCurrentIndex).getUri()), this.mRingtones.get(this.mCurrentIndex).getTitle());
        }
    }

    private void stopCurrentAnim() {
        PreferenceViewHolder preferenceViewHolder = this.mHolder;
        if (preferenceViewHolder != null && this.mCurrentIndex > -1) {
            GlyphsItemView glyphsItemView = (GlyphsItemView) ((ViewPager) preferenceViewHolder.findViewById(R$id.vp_content)).findViewWithTag("position_" + this.mCurrentIndex);
            if (glyphsItemView != null) {
                glyphsItemView.release();
            }
        }
    }

    public void playRingTone(Context context, Uri uri, String str) {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) context.getSystemService("audio");
        }
        getRingtoneThread(new RingtoneSelectorPreference$$ExternalSyntheticLambda2(this, context, uri, str)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$playRingTone$3(Context context, Uri uri, String str) {
        ringtoneStop();
        try {
            threadPlayRingTone(context, uri, this.mIsLoop, str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startCurrentItemAnim(ViewPager viewPager, int i, int i2) {
        if (!this.mSoundOnly && viewPager != null) {
            GlyphsItemView glyphsItemView = (GlyphsItemView) viewPager.findViewWithTag("position_" + i);
            GlyphsItemView glyphsItemView2 = (GlyphsItemView) viewPager.findViewWithTag("position_" + this.mCurrentIndex);
            if (glyphsItemView2 != null) {
                glyphsItemView2.release();
            }
            if (glyphsItemView != null) {
                List<Ringtone> list = this.mRingtones;
                if (list == null || list.size() <= this.mCurrentIndex) {
                    new Throwable("mCurrentIndex is bigger than mRingtones.sze() or mRingtones is null");
                    return;
                }
                glyphsItemView.startAnim("glyphs/" + this.mRingtones.get(this.mCurrentIndex).getTitle().replaceAll(" ", "_") + ".csv");
            }
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
            handler.removeCallbacksAndMessages((Object) null);
        }
        ringtoneStop();
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            audioManager.setParameters(getLedStopParams(this.mRingtoneType));
        }
    }

    public void setData(List<Ringtone> list, int i) {
        this.mCurrentIndex = i;
        if (list != null && list.size() != 0) {
            this.mRingtones = list;
            refresh();
        }
    }

    public class RingtoneAdapter extends PagerAdapter {
        private final List<Ringtone> mRingtones;

        public boolean isViewFromObject(View view, Object obj) {
            return obj == view;
        }

        public RingtoneAdapter(Context context, List<Ringtone> list) {
            this.mRingtones = list;
        }

        public int getCount() {
            List<Ringtone> list = this.mRingtones;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            GlyphsItemView dilHolder = RingtoneSelectorPreference.this.getDilHolder();
            dilHolder.setTag("position_" + i);
            viewGroup.addView(dilHolder);
            return dilHolder;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            GlyphsItemView glyphsItemView = (GlyphsItemView) obj;
            glyphsItemView.release();
            RingtoneSelectorPreference.this.mViewHolders.add(glyphsItemView);
            viewGroup.removeView(glyphsItemView);
        }
    }

    public static class Ringtone {

        /* renamed from: id */
        private String f265id;
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
            this.f265id = str5;
        }

        public String getId() {
            return this.f265id;
        }

        public String getTitle() {
            return this.title;
        }

        public String getUri() {
            return this.uri;
        }
    }

    private void initRingtoneVolume(Context context, android.media.Ringtone ringtone) {
        if (ringtone != null) {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) context.getSystemService("audio");
            }
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null && audioManager.getRingerMode() == 1) {
                ringtone.setVolume(0.0f);
            }
        }
    }

    public void playRingtoneLed(Context context, Uri uri, String str) {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) context.getSystemService("audio");
        }
        boolean z = false;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "led_effect_enable", 0) != 0) {
            z = true;
        }
        Log.d("RingtoneSelectorP", "playRingtoneLed ledEnabled:" + z + " mRingtoneType:" + this.mRingtoneType + " mSoundOnly:" + this.mSoundOnly);
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null && z) {
            if (this.mSoundOnly) {
                audioManager.setParameters(getLedSoundOnlyParams(uri, str));
                return;
            }
            audioManager.setParameters(getLedStopParams(this.mRingtoneType));
            Log.d("RingtoneSelectorP", "LED START");
            String contactLedStartParams = this.mRingtoneType == 1 ? getContactLedStartParams(uri, str) : getNotificationLedStartParams(uri, str);
            Log.d("RingtoneSelectorP", "LED START params:" + contactLedStartParams);
            this.mAudioManager.setParameters(contactLedStartParams);
        }
    }

    private String getNotificationLedStartParams(Uri uri, String str) {
        if (uri == null) {
            return null;
        }
        return "nt_lights:light_type=notification;light_uri=content://media" + uri.getPath() + ";light_state=on";
    }

    private String getContactLedStartParams(Uri uri, String str) {
        if (uri == null) {
            return null;
        }
        return "nt_lights:light_type=ringtone;light_uri=content://media" + uri.getPath() + ";light_state=on";
    }

    private String getLedStopParams(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("nt_lights:light_type=");
        sb.append(i == 1 ? "ringtone" : "notification");
        sb.append(";");
        sb.append("light_uri=");
        sb.append(";");
        sb.append("light_state=off");
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

    /* access modifiers changed from: private */
    public boolean isSystemSoundMute() {
        int ringerModeInternal = this.mHelper.getRingerModeInternal();
        Log.d("RingtoneSelectorP", "isSystemSoundMute ringerMode:" + ringerModeInternal);
        return ringerModeInternal == 0;
    }

    private boolean isSoundMute() {
        boolean z = true;
        if (this.mRingtoneType != 1 ? Settings.System.getInt(this.mContext.getContentResolver(), "notification_vibration_intensity", 0) != 0 : Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_when_ringing", 0) != 0) {
            z = false;
        }
        Log.d("RingtoneSelectorP", "isSoundMute:" + z + ", mRingtoneType:" + this.mRingtoneType);
        return z;
    }

    public void threadPlayRingTone(Context context, Uri uri, boolean z, String str) throws InterruptedException {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object) null);
        }
        try {
            this.mCurrentRingtone = RingtoneManager.getRingtone(context, uri);
            AudioAttributes build = new AudioAttributes.Builder().setInternalLegacyStreamType(this.mRingtoneType == 1 ? 2 : 5).setHapticChannelsMuted(isSoundMute()).build();
            initRingtoneVolume(this.mContext, this.mCurrentRingtone);
            this.mCurrentRingtone.setAudioAttributes(build);
            this.mCurrentRingtone.setOnMediaPlayerListener(new Ringtone.OnMediaPlayerListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    RingtoneSelectorPreference.this.mHandler.removeCallbacksAndMessages((Object) null);
                    RingtoneSelectorPreference.this.audioLedStop();
                }
            });
            this.mHandler.removeCallbacks(this.mStopAudioRunnable);
            this.mHandler.removeCallbacks(this.mPlayLedRunnable);
            int ledDelayTime = getLedDelayTime();
            this.mHandler.postDelayed(this.mStopAudioRunnable, (long) (this.mCurrentRingtone.getDuration() + ledDelayTime));
            this.mPlayLedRunnable.init(context, uri, str);
            this.mHandler.postDelayed(this.mPlayLedRunnable, (long) ledDelayTime);
        } catch (Exception unused) {
            ringtoneStop();
        }
    }

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

    public void audioLedStop() {
        if (this.mAudioManager != null) {
            Log.d("RingtoneSelectorP", "LED STOP");
            this.mAudioManager.setParameters(getLedStopParams(this.mRingtoneType));
        }
    }

    /* access modifiers changed from: private */
    public void ringtonePlay() {
        if (this.mCurrentRingtone != null) {
            Log.d("RingtoneSelectorP", "ringtonePlay");
            this.mCurrentRingtone.play();
        }
    }

    public void loopStartCurrentItemAnim() {
        PreferenceViewHolder preferenceViewHolder = this.mHolder;
        if (preferenceViewHolder != null) {
            int i = R$id.vp_content;
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
