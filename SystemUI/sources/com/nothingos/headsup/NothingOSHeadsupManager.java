package com.nothingos.headsup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.nothingos.systemui.statusbar.policy.PopNotificationView;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class NothingOSHeadsupManager {
    private static ArrayList<String> mDisturbPKgForPop;
    private int mAutoDismissNotificationDecay;
    private Context mContext;
    private HeadsUpManager mHeadsUpManager;
    private WindowManager.LayoutParams mLpChanged;
    private PopNotificationView mPopNotificationView;
    private WindowManager mWindowManager;
    private boolean DEBUG = Log.isLoggable("NothingOSHeadsupManager", 3);
    private boolean mShowPopNotification = true;
    private boolean mIsForceQuickReply = true;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mDissmissPopView = new Runnable() { // from class: com.nothingos.headsup.NothingOSHeadsupManager.1
        @Override // java.lang.Runnable
        public void run() {
            NothingOSHeadsupManager.this.hidePopNotificationView();
        }
    };

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        mDisturbPKgForPop = arrayList;
        arrayList.add("com.android.incallui");
    }

    public NothingOSHeadsupManager(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mAutoDismissNotificationDecay = this.mContext.getResources().getInteger(R$integer.heads_up_notification_decay);
        PopNotificationView popNotificationView = new PopNotificationView(this.mContext);
        this.mPopNotificationView = popNotificationView;
        popNotificationView.setVisibility(8);
        initContentResolver();
    }

    public boolean isDisturbForPop(String str) {
        return mDisturbPKgForPop.contains(str);
    }

    public boolean isShowPopNotification() {
        return this.mShowPopNotification;
    }

    public boolean isForceQuickReply() {
        return this.mIsForceQuickReply;
    }

    private void initContentResolver() {
        this.mShowPopNotification = true;
        this.mIsForceQuickReply = true;
    }

    public void addToWindow() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2038, 8389416, -3);
        this.mLpChanged = layoutParams;
        layoutParams.flags |= 16777216;
        layoutParams.gravity = 49;
        layoutParams.softInputMode = 16;
        layoutParams.setTitle("Heads Up");
        this.mLpChanged.packageName = this.mContext.getPackageName();
        WindowManager.LayoutParams layoutParams2 = this.mLpChanged;
        layoutParams2.width = -2;
        layoutParams2.height = -2;
        layoutParams2.y = this.mContext.getResources().getDimensionPixelSize(R$dimen.pop_notification_offset_y);
        this.mWindowManager.addView(this.mPopNotificationView, this.mLpChanged);
    }

    public void removeFromWindow() {
        PopNotificationView popNotificationView = this.mPopNotificationView;
        if (popNotificationView == null || !popNotificationView.isAttachedToWindow()) {
            return;
        }
        this.mWindowManager.removeView(this.mPopNotificationView);
    }

    public void showPopNotificationView(ExpandableNotificationRow expandableNotificationRow) {
        expandableNotificationRow.getEntry().getSbn().getOpPkg();
        WindowManager.LayoutParams layoutParams = this.mLpChanged;
        layoutParams.windowAnimations = 0;
        layoutParams.gravity = 49;
        if (this.mPopNotificationView.isAttachedToWindow()) {
            this.mWindowManager.updateViewLayout(this.mPopNotificationView, this.mLpChanged);
        } else {
            this.mWindowManager.addView(this.mPopNotificationView, this.mLpChanged);
        }
        this.mLpChanged.y = this.mContext.getResources().getDimensionPixelSize(R$dimen.pop_notification_offset_y);
        this.mPopNotificationView.updateNotificationRow(expandableNotificationRow);
        setPopViewVisibilityWithAnimation(true);
        this.mHandler.removeCallbacks(this.mDissmissPopView);
        this.mHandler.postDelayed(this.mDissmissPopView, this.mAutoDismissNotificationDecay);
    }

    public void hidePopNotificationView() {
        setPopViewVisibilityWithAnimation(false);
    }

    private void setPopViewVisibilityWithAnimation(boolean z) {
        if (z) {
            this.mPopNotificationView.setVisibility(0);
            setPopWithAnimation(false, null);
            setPopWithAnimation(true, null);
            return;
        }
        Log.d("NothingOSHeadsupManager", "setHeadsUpViewVisibilityWithAnimation: setVisible = false");
        setPopWithAnimation(false, new AnimatorListenerAdapter() { // from class: com.nothingos.headsup.NothingOSHeadsupManager.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                NothingOSHeadsupManager.this.mPopNotificationView.setVisibility(8);
            }
        });
    }

    public void setPopWithAnimation(boolean z, Animator.AnimatorListener animatorListener) {
        this.mPopNotificationView.releasePopWithAnimation(z, animatorListener);
    }

    public void snooze(String str) {
        if (this.mHeadsUpManager == null || isDisturbForPop(str)) {
            return;
        }
        this.mHeadsUpManager.snoozePackage(str);
    }

    public void onConfigurationChanged(Configuration configuration) {
        removeFromWindow();
        PopNotificationView popNotificationView = new PopNotificationView(this.mContext);
        this.mPopNotificationView = popNotificationView;
        popNotificationView.setVisibility(8);
        addToWindow();
    }
}
