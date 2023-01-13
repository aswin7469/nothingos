package com.android.systemui.statusbar.phone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class SystemUIDialog extends AlertDialog implements ViewRootImpl.ConfigChangedCallback {
    private static final boolean DEFAULT_DISMISS_ON_DEVICE_LOCK = true;
    private static final int DEFAULT_THEME = 2132018213;
    private static final String FLAG_TABLET_DIALOG_WIDTH = "persist.systemui.flag_tablet_dialog_width";
    private final Context mContext;
    private final SystemUIDialogManager mDialogManager;
    private final DismissReceiver mDismissReceiver;
    private final Handler mHandler;
    private int mLastConfigurationHeightDp;
    private int mLastConfigurationWidthDp;
    private int mLastHeight;
    private int mLastWidth;
    private List<Runnable> mOnCreateRunnables;
    private final SysUiState mSysUiState;

    private static int getDefaultDialogHeight() {
        return -2;
    }

    public SystemUIDialog(Context context) {
        this(context, 2132018213, true);
    }

    public SystemUIDialog(Context context, int i) {
        this(context, i, true);
    }

    public SystemUIDialog(Context context, boolean z) {
        this(context, 2132018213, z);
    }

    public SystemUIDialog(Context context, int i, boolean z) {
        super(context, i);
        this.mHandler = new Handler();
        this.mLastWidth = Integer.MIN_VALUE;
        this.mLastHeight = Integer.MIN_VALUE;
        this.mLastConfigurationWidthDp = -1;
        this.mLastConfigurationHeightDp = -1;
        this.mOnCreateRunnables = new ArrayList();
        this.mContext = context;
        applyFlags(this);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.setTitle(getClass().getSimpleName());
        getWindow().setAttributes(attributes);
        this.mDismissReceiver = z ? new DismissReceiver(this) : null;
        this.mDialogManager = (SystemUIDialogManager) Dependency.get(SystemUIDialogManager.class);
        this.mSysUiState = (SysUiState) Dependency.get(SysUiState.class);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Configuration configuration = getContext().getResources().getConfiguration();
        this.mLastConfigurationWidthDp = configuration.screenWidthDp;
        this.mLastConfigurationHeightDp = configuration.screenHeightDp;
        updateWindowSize();
        setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                if (dialogInterface instanceof AlertDialog) {
                    SystemUIDialog.this.getButton(-1).setTextColor(Utils.getColorAttr(SystemUIDialog.this.getContext(), 17957103));
                }
            }
        });
        for (int i = 0; i < this.mOnCreateRunnables.size(); i++) {
            this.mOnCreateRunnables.get(i).run();
        }
    }

    /* access modifiers changed from: private */
    public void updateWindowSize() {
        if (Looper.myLooper() != this.mHandler.getLooper()) {
            this.mHandler.post(new SystemUIDialog$$ExternalSyntheticLambda1(this));
            return;
        }
        int width = getWidth();
        int height = getHeight();
        if (width != this.mLastWidth || height != this.mLastHeight) {
            this.mLastWidth = width;
            this.mLastHeight = height;
            getWindow().setLayout(width, height);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mLastConfigurationWidthDp != configuration.screenWidthDp || this.mLastConfigurationHeightDp != configuration.screenHeightDp) {
            this.mLastConfigurationWidthDp = configuration.screenWidthDp;
            this.mLastConfigurationHeightDp = configuration.compatScreenWidthDp;
            updateWindowSize();
        }
    }

    /* access modifiers changed from: protected */
    public int getWidth() {
        return getDefaultDialogWidth(this);
    }

    /* access modifiers changed from: protected */
    public int getHeight() {
        return getDefaultDialogHeight();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        DismissReceiver dismissReceiver = this.mDismissReceiver;
        if (dismissReceiver != null) {
            dismissReceiver.register();
        }
        ViewRootImpl.addConfigCallback(this);
        this.mDialogManager.setShowing(this, true);
        this.mSysUiState.setFlag(32768, true).commitUpdate(this.mContext.getDisplayId());
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        DismissReceiver dismissReceiver = this.mDismissReceiver;
        if (dismissReceiver != null) {
            dismissReceiver.unregister();
        }
        ViewRootImpl.removeConfigCallback(this);
        this.mDialogManager.setShowing(this, false);
        this.mSysUiState.setFlag(32768, false).commitUpdate(this.mContext.getDisplayId());
    }

    public void setShowForAllUsers(boolean z) {
        setShowForAllUsers(this, z);
    }

    public void setMessage(int i) {
        setMessage(this.mContext.getString(i));
    }

    public void setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
        setPositiveButton(i, onClickListener, true);
    }

    public void setPositiveButton(int i, DialogInterface.OnClickListener onClickListener, boolean z) {
        setButton(-1, i, onClickListener, z);
    }

    public void setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
        setNegativeButton(i, onClickListener, true);
    }

    public void setNegativeButton(int i, DialogInterface.OnClickListener onClickListener, boolean z) {
        setButton(-2, i, onClickListener, z);
    }

    public void setNeutralButton(int i, DialogInterface.OnClickListener onClickListener) {
        setNeutralButton(i, onClickListener, true);
    }

    public void setNeutralButton(int i, DialogInterface.OnClickListener onClickListener, boolean z) {
        setButton(-3, i, onClickListener, z);
    }

    private void setButton(int i, int i2, DialogInterface.OnClickListener onClickListener, boolean z) {
        if (z) {
            setButton(i, this.mContext.getString(i2), onClickListener);
            return;
        }
        DialogInterface.OnClickListener onClickListener2 = null;
        setButton(i, this.mContext.getString(i2), (DialogInterface.OnClickListener) null);
        this.mOnCreateRunnables.add(new SystemUIDialog$$ExternalSyntheticLambda4(this, i, onClickListener));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setButton$1$com-android-systemui-statusbar-phone-SystemUIDialog */
    public /* synthetic */ void mo45384x5d25f216(int i, DialogInterface.OnClickListener onClickListener) {
        getButton(i).setOnClickListener(new SystemUIDialog$$ExternalSyntheticLambda0(this, onClickListener, i));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setButton$0$com-android-systemui-statusbar-phone-SystemUIDialog */
    public /* synthetic */ void mo45383xcfeb4095(DialogInterface.OnClickListener onClickListener, int i, View view) {
        onClickListener.onClick(this, i);
    }

    public static void setShowForAllUsers(Dialog dialog, boolean z) {
        if (z) {
            dialog.getWindow().getAttributes().privateFlags |= 16;
            return;
        }
        dialog.getWindow().getAttributes().privateFlags &= -17;
    }

    public static void setWindowOnTop(Dialog dialog, boolean z) {
        Window window = dialog.getWindow();
        window.setType(2017);
        if (z) {
            window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
        }
    }

    public static AlertDialog applyFlags(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        window.setType(2017);
        window.addFlags(655360);
        window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
        return alertDialog;
    }

    public static void registerDismissListener(Dialog dialog) {
        registerDismissListener(dialog, (Runnable) null);
    }

    public static void registerDismissListener(Dialog dialog, Runnable runnable) {
        DismissReceiver dismissReceiver = new DismissReceiver(dialog);
        dialog.setOnDismissListener(new SystemUIDialog$$ExternalSyntheticLambda3(dismissReceiver, runnable));
        dismissReceiver.register();
    }

    static /* synthetic */ void lambda$registerDismissListener$2(DismissReceiver dismissReceiver, Runnable runnable, DialogInterface dialogInterface) {
        dismissReceiver.unregister();
        if (runnable != null) {
            runnable.run();
        }
    }

    public static void setDialogSize(Dialog dialog) {
        dialog.create();
        dialog.getWindow().setLayout(getDefaultDialogWidth(dialog), getDefaultDialogHeight());
    }

    private static int getDefaultDialogWidth(Dialog dialog) {
        Context context = dialog.getContext();
        int i = SystemProperties.getInt(FLAG_TABLET_DIALOG_WIDTH, 0);
        if (i == -1) {
            return calculateDialogWidthWithInsets(dialog, 624);
        }
        if (i == -2) {
            return calculateDialogWidthWithInsets(dialog, 348);
        }
        if (i > 0) {
            return calculateDialogWidthWithInsets(dialog, i);
        }
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1894R.dimen.large_dialog_width);
        return dimensionPixelSize > 0 ? dimensionPixelSize + getHorizontalInsets(dialog) : dimensionPixelSize;
    }

    private static int calculateDialogWidthWithInsets(Dialog dialog, int i) {
        return Math.round(TypedValue.applyDimension(1, (float) i, dialog.getContext().getResources().getDisplayMetrics()) + ((float) getHorizontalInsets(dialog)));
    }

    private static int getHorizontalInsets(Dialog dialog) {
        Drawable drawable;
        View decorView = dialog.getWindow().getDecorView();
        if (decorView == null) {
            return 0;
        }
        View findViewByPredicate = decorView.findViewByPredicate(new SystemUIDialog$$ExternalSyntheticLambda2());
        if (findViewByPredicate != null) {
            drawable = findViewByPredicate.getBackground();
        } else {
            drawable = decorView.getBackground();
        }
        Insets opticalInsets = drawable != null ? drawable.getOpticalInsets() : Insets.NONE;
        return opticalInsets.left + opticalInsets.right;
    }

    static /* synthetic */ boolean lambda$getHorizontalInsets$3(View view) {
        return view.getTag(C1894R.C1898id.tag_dialog_background) != null;
    }

    private static class DismissReceiver extends BroadcastReceiver {
        private static final IntentFilter INTENT_FILTER;
        private final BroadcastDispatcher mBroadcastDispatcher = ((BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class));
        private final Dialog mDialog;
        private final DialogLaunchAnimator mDialogLaunchAnimator = ((DialogLaunchAnimator) Dependency.get(DialogLaunchAnimator.class));
        private boolean mRegistered;

        static {
            IntentFilter intentFilter = new IntentFilter();
            INTENT_FILTER = intentFilter;
            intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
        }

        DismissReceiver(Dialog dialog) {
            this.mDialog = dialog;
        }

        /* access modifiers changed from: package-private */
        public void register() {
            this.mBroadcastDispatcher.registerReceiver(this, INTENT_FILTER, (Executor) null, UserHandle.CURRENT);
            this.mRegistered = true;
        }

        /* access modifiers changed from: package-private */
        public void unregister() {
            if (this.mRegistered) {
                this.mBroadcastDispatcher.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }

        public void onReceive(Context context, Intent intent) {
            this.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
            this.mDialog.dismiss();
        }
    }
}
