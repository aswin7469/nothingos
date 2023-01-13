package com.android.systemui.keyguard;

import android.content.res.ColorStateList;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.SystemClock;
import android.text.TextUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KeyguardIndicationRotateTextViewController extends ViewController<KeyguardIndicationTextView> implements Dumpable {
    private static final long DEFAULT_INDICATION_SHOW_LENGTH = 3500;
    public static final long IMPORTANT_MSG_MIN_DURATION = 2600;
    public static final int INDICATION_TYPE_ALIGNMENT = 4;
    public static final int INDICATION_TYPE_BATTERY = 3;
    public static final int INDICATION_TYPE_BIOMETRIC_MESSAGE = 11;
    public static final int INDICATION_TYPE_DISCLOSURE = 1;
    public static final int INDICATION_TYPE_LOGOUT = 2;
    static final int INDICATION_TYPE_NONE = -1;
    public static final int INDICATION_TYPE_OWNER_INFO = 0;
    public static final int INDICATION_TYPE_RESTING = 7;
    public static final int INDICATION_TYPE_REVERSE_CHARGING = 10;
    public static final int INDICATION_TYPE_TRANSIENT = 5;
    public static final int INDICATION_TYPE_TRUST = 6;
    public static final int INDICATION_TYPE_USER_LOCKED = 8;
    public static String TAG = "KgIndicationRotatingCtrl";
    private int mCurrIndicationType = -1;
    private CharSequence mCurrMessage;
    /* access modifiers changed from: private */
    public final DelayableExecutor mExecutor;
    private final Map<Integer, KeyguardIndication> mIndicationMessages = new HashMap();
    /* access modifiers changed from: private */
    public final List<Integer> mIndicationQueue = new LinkedList();
    private final ColorStateList mInitialTextColorState;
    /* access modifiers changed from: private */
    public boolean mIsDozing;
    private long mLastIndicationSwitch;
    /* access modifiers changed from: private */
    public final float mMaxAlpha;
    private ShowNextIndication mShowNextIndicationRunnable;
    private final StatusBarStateController mStatusBarStateController;
    private StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onDozeAmountChanged(float f, float f2) {
            ((KeyguardIndicationTextView) KeyguardIndicationRotateTextViewController.this.mView).setAlpha((1.0f - f) * KeyguardIndicationRotateTextViewController.this.mMaxAlpha);
        }

        public void onDozingChanged(boolean z) {
            if (z != KeyguardIndicationRotateTextViewController.this.mIsDozing) {
                boolean unused = KeyguardIndicationRotateTextViewController.this.mIsDozing = z;
                if (KeyguardIndicationRotateTextViewController.this.mIsDozing) {
                    KeyguardIndicationRotateTextViewController.this.showIndication(-1);
                } else if (KeyguardIndicationRotateTextViewController.this.mIndicationQueue.size() > 0) {
                    KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = KeyguardIndicationRotateTextViewController.this;
                    keyguardIndicationRotateTextViewController.showIndication(((Integer) keyguardIndicationRotateTextViewController.mIndicationQueue.get(0)).intValue());
                }
            }
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicationType {
    }

    public KeyguardIndicationRotateTextViewController(KeyguardIndicationTextView keyguardIndicationTextView, @Main DelayableExecutor delayableExecutor, StatusBarStateController statusBarStateController) {
        super(keyguardIndicationTextView);
        this.mMaxAlpha = keyguardIndicationTextView.getAlpha();
        this.mExecutor = delayableExecutor;
        this.mInitialTextColorState = this.mView != null ? ((KeyguardIndicationTextView) this.mView).getTextColors() : ColorStateList.valueOf(-1);
        this.mStatusBarStateController = statusBarStateController;
        init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        cancelScheduledIndication();
    }

    public void updateIndication(int i, KeyguardIndication keyguardIndication, boolean z) {
        if (i != 10) {
            long minVisibilityMillis = getMinVisibilityMillis(this.mIndicationMessages.get(Integer.valueOf(this.mCurrIndicationType)));
            boolean z2 = true;
            boolean z3 = keyguardIndication != null && !TextUtils.isEmpty(keyguardIndication.getMessage());
            if (!z3) {
                this.mIndicationMessages.remove(Integer.valueOf(i));
                this.mIndicationQueue.removeIf(new C2149x419da85b(i));
            } else {
                if (!this.mIndicationQueue.contains(Integer.valueOf(i))) {
                    this.mIndicationQueue.add(Integer.valueOf(i));
                }
                this.mIndicationMessages.put(Integer.valueOf(i), keyguardIndication);
            }
            if (!this.mIsDozing) {
                long uptimeMillis = SystemClock.uptimeMillis() - this.mLastIndicationSwitch;
                if (uptimeMillis < minVisibilityMillis) {
                    z2 = false;
                }
                if (z3) {
                    int i2 = this.mCurrIndicationType;
                    if (i2 == -1 || i2 == i) {
                        showIndication(i);
                    } else if (z) {
                        if (z2) {
                            showIndication(i);
                            return;
                        }
                        this.mIndicationQueue.removeIf(new C2150x419da85c(i));
                        this.mIndicationQueue.add(0, Integer.valueOf(i));
                        scheduleShowNextIndication(minVisibilityMillis - uptimeMillis);
                    } else if (!isNextIndicationScheduled()) {
                        long max = Math.max(getMinVisibilityMillis(this.mIndicationMessages.get(Integer.valueOf(i))), (long) DEFAULT_INDICATION_SHOW_LENGTH);
                        if (uptimeMillis >= max) {
                            showIndication(i);
                        } else {
                            scheduleShowNextIndication(max - uptimeMillis);
                        }
                    }
                } else if (this.mCurrIndicationType == i && !z3 && z) {
                    if (z2) {
                        ShowNextIndication showNextIndication = this.mShowNextIndicationRunnable;
                        if (showNextIndication != null) {
                            showNextIndication.runImmediately();
                        } else {
                            showIndication(-1);
                        }
                    } else {
                        scheduleShowNextIndication(minVisibilityMillis - uptimeMillis);
                    }
                }
            }
        }
    }

    static /* synthetic */ boolean lambda$updateIndication$0(int i, Integer num) {
        return num.intValue() == i;
    }

    static /* synthetic */ boolean lambda$updateIndication$1(int i, Integer num) {
        return num.intValue() == i;
    }

    public void hideIndication(int i) {
        if (this.mIndicationMessages.containsKey(Integer.valueOf(i)) && !TextUtils.isEmpty(this.mIndicationMessages.get(Integer.valueOf(i)).getMessage())) {
            updateIndication(i, (KeyguardIndication) null, true);
        }
    }

    public void showTransient(CharSequence charSequence) {
        updateIndication(5, new KeyguardIndication.Builder().setMessage(charSequence).setMinVisibilityMillis(Long.valueOf((long) IMPORTANT_MSG_MIN_DURATION)).setTextColor(this.mInitialTextColorState).build(), true);
    }

    public void hideTransient() {
        hideIndication(5);
    }

    public boolean hasIndications() {
        return this.mIndicationMessages.keySet().size() > 0;
    }

    public void clearMessages() {
        this.mCurrIndicationType = -1;
        this.mIndicationQueue.clear();
        this.mIndicationMessages.clear();
        ((KeyguardIndicationTextView) this.mView).clearMessages();
    }

    /* access modifiers changed from: private */
    public void showIndication(int i) {
        cancelScheduledIndication();
        CharSequence charSequence = this.mCurrMessage;
        int i2 = this.mCurrIndicationType;
        this.mCurrIndicationType = i;
        this.mCurrMessage = this.mIndicationMessages.get(Integer.valueOf(i)) != null ? this.mIndicationMessages.get(Integer.valueOf(i)).getMessage() : null;
        this.mIndicationQueue.removeIf(new C2148x419da85a(i));
        if (this.mCurrIndicationType != -1) {
            this.mIndicationQueue.add(Integer.valueOf(i));
        }
        this.mLastIndicationSwitch = SystemClock.uptimeMillis();
        if (!TextUtils.equals(charSequence, this.mCurrMessage) || i2 != this.mCurrIndicationType) {
            ((KeyguardIndicationTextView) this.mView).switchIndication(this.mIndicationMessages.get(Integer.valueOf(i)));
        }
        if (this.mCurrIndicationType != -1 && this.mIndicationQueue.size() > 1) {
            scheduleShowNextIndication(Math.max(getMinVisibilityMillis(this.mIndicationMessages.get(Integer.valueOf(i))), (long) DEFAULT_INDICATION_SHOW_LENGTH));
        }
    }

    static /* synthetic */ boolean lambda$showIndication$2(int i, Integer num) {
        return num.intValue() == i;
    }

    private long getMinVisibilityMillis(KeyguardIndication keyguardIndication) {
        if (keyguardIndication == null || keyguardIndication.getMinVisibilityMillis() == null) {
            return 0;
        }
        return keyguardIndication.getMinVisibilityMillis().longValue();
    }

    /* access modifiers changed from: protected */
    public boolean isNextIndicationScheduled() {
        return this.mShowNextIndicationRunnable != null;
    }

    private void scheduleShowNextIndication(long j) {
        cancelScheduledIndication();
        this.mShowNextIndicationRunnable = new ShowNextIndication(j);
    }

    private void cancelScheduledIndication() {
        ShowNextIndication showNextIndication = this.mShowNextIndicationRunnable;
        if (showNextIndication != null) {
            showNextIndication.cancelDelayedExecution();
            this.mShowNextIndicationRunnable = null;
        }
    }

    class ShowNextIndication {
        private Runnable mCancelDelayedRunnable;
        private final Runnable mShowIndicationRunnable;

        ShowNextIndication(long j) {
            C2151x49118fb4 keyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0 = new C2151x49118fb4(this);
            this.mShowIndicationRunnable = keyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0;
            this.mCancelDelayedRunnable = KeyguardIndicationRotateTextViewController.this.mExecutor.executeDelayed(keyguardIndicationRotateTextViewController$ShowNextIndication$$ExternalSyntheticLambda0, j);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-android-systemui-keyguard-KeyguardIndicationRotateTextViewController$ShowNextIndication */
        public /* synthetic */ void mo33130x7d3b789() {
            int i;
            if (KeyguardIndicationRotateTextViewController.this.mIndicationQueue.size() == 0) {
                i = -1;
            } else {
                i = ((Integer) KeyguardIndicationRotateTextViewController.this.mIndicationQueue.get(0)).intValue();
            }
            KeyguardIndicationRotateTextViewController.this.showIndication(i);
        }

        public void runImmediately() {
            cancelDelayedExecution();
            this.mShowIndicationRunnable.run();
        }

        public void cancelDelayedExecution() {
            Runnable runnable = this.mCancelDelayedRunnable;
            if (runnable != null) {
                runnable.run();
                this.mCancelDelayedRunnable = null;
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardIndicationRotatingTextViewController:");
        printWriter.println("    currentMessage=" + ((KeyguardIndicationTextView) this.mView).getText());
        printWriter.println("    dozing:" + this.mIsDozing);
        printWriter.println("    queue:" + this.mIndicationQueue.toString());
        printWriter.println("    showNextIndicationRunnable:" + this.mShowNextIndicationRunnable);
        if (hasIndications()) {
            printWriter.println("    All messages:");
            for (Integer intValue : this.mIndicationMessages.keySet()) {
                int intValue2 = intValue.intValue();
                printWriter.println("        type=" + intValue2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mIndicationMessages.get(Integer.valueOf(intValue2)));
            }
        }
    }
}
