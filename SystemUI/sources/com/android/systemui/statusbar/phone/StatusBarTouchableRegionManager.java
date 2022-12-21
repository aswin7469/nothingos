package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public final class StatusBarTouchableRegionManager implements Dumpable {
    private static final String TAG = "TouchableRegionManager";
    /* access modifiers changed from: private */
    public CentralSurfaces mCentralSurfaces;
    private final Context mContext;
    private int mDisplayCutoutTouchableRegionSize;
    /* access modifiers changed from: private */
    public boolean mForceCollapsedUntilLayout = false;
    private final HeadsUpManagerPhone mHeadsUpManager;
    /* access modifiers changed from: private */
    public boolean mIsStatusBarExpanded = false;
    /* access modifiers changed from: private */
    public View mNotificationPanelView;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private View mNotificationShadeWindowView;
    private final ViewTreeObserver.OnComputeInternalInsetsListener mOnComputeInternalInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() {
        public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            if (!StatusBarTouchableRegionManager.this.mIsStatusBarExpanded && !StatusBarTouchableRegionManager.this.mCentralSurfaces.isBouncerShowing()) {
                internalInsetsInfo.setTouchableInsets(3);
                internalInsetsInfo.touchableRegion.set(StatusBarTouchableRegionManager.this.calculateTouchableRegion());
            }
        }
    };
    private boolean mShouldAdjustInsets = false;
    private int mStatusBarHeight;
    private Region mTouchableRegion = new Region();

    @Inject
    public StatusBarTouchableRegionManager(Context context, NotificationShadeWindowController notificationShadeWindowController, ConfigurationController configurationController, HeadsUpManagerPhone headsUpManagerPhone) {
        this.mContext = context;
        initResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onDensityOrFontScaleChanged() {
                StatusBarTouchableRegionManager.this.initResources();
            }

            public void onThemeChanged() {
                StatusBarTouchableRegionManager.this.initResources();
            }
        });
        this.mHeadsUpManager = headsUpManagerPhone;
        headsUpManagerPhone.addListener(new OnHeadsUpChangedListener() {
            public void onHeadsUpPinnedModeChanged(boolean z) {
                if (Log.isLoggable(StatusBarTouchableRegionManager.TAG, 5)) {
                    Log.w(StatusBarTouchableRegionManager.TAG, "onHeadsUpPinnedModeChanged");
                }
                StatusBarTouchableRegionManager.this.updateTouchableRegion();
            }
        });
        headsUpManagerPhone.addHeadsUpPhoneListener(new HeadsUpManagerPhone.OnHeadsUpPhoneListenerChange() {
            public void onHeadsUpGoingAwayStateChanged(boolean z) {
                if (!z) {
                    StatusBarTouchableRegionManager.this.updateTouchableRegionAfterLayout();
                } else {
                    StatusBarTouchableRegionManager.this.updateTouchableRegion();
                }
            }
        });
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        notificationShadeWindowController.setForcePluginOpenListener(new StatusBarTouchableRegionManager$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-StatusBarTouchableRegionManager */
    public /* synthetic */ void mo45350x3ee4734b(boolean z) {
        updateTouchableRegion();
    }

    /* access modifiers changed from: protected */
    public void setup(CentralSurfaces centralSurfaces, View view) {
        this.mCentralSurfaces = centralSurfaces;
        this.mNotificationShadeWindowView = view;
        this.mNotificationPanelView = view.findViewById(C1893R.C1897id.notification_panel);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("StatusBarTouchableRegionManager state:");
        printWriter.print("  mTouchableRegion=");
        printWriter.println((Object) this.mTouchableRegion);
    }

    /* access modifiers changed from: package-private */
    public void setPanelExpanded(boolean z) {
        if (z != this.mIsStatusBarExpanded) {
            this.mIsStatusBarExpanded = z;
            if (z) {
                this.mForceCollapsedUntilLayout = false;
            }
            updateTouchableRegion();
        }
    }

    /* access modifiers changed from: package-private */
    public Region calculateTouchableRegion() {
        Region touchableRegion = this.mHeadsUpManager.getTouchableRegion();
        if (touchableRegion != null) {
            this.mTouchableRegion.set(touchableRegion);
        } else {
            this.mTouchableRegion.set(0, 0, this.mNotificationShadeWindowView.getWidth(), this.mStatusBarHeight);
            updateRegionForNotch(this.mTouchableRegion);
        }
        return this.mTouchableRegion;
    }

    /* access modifiers changed from: private */
    public void initResources() {
        this.mDisplayCutoutTouchableRegionSize = this.mContext.getResources().getDimensionPixelSize(17105199);
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
    }

    /* access modifiers changed from: private */
    public void updateTouchableRegion() {
        View view = this.mNotificationShadeWindowView;
        boolean z = true;
        boolean z2 = (view == null || view.getRootWindowInsets() == null || this.mNotificationShadeWindowView.getRootWindowInsets().getDisplayCutout() == null) ? false : true;
        if (!this.mHeadsUpManager.hasPinnedHeadsUp() && !this.mHeadsUpManager.isHeadsUpGoingAway() && !this.mForceCollapsedUntilLayout && !z2 && !this.mNotificationShadeWindowController.getForcePluginOpen()) {
            z = false;
        }
        if (z != this.mShouldAdjustInsets) {
            if (z) {
                this.mNotificationShadeWindowView.getViewTreeObserver().addOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
                this.mNotificationShadeWindowView.requestLayout();
            } else {
                this.mNotificationShadeWindowView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
            }
            this.mShouldAdjustInsets = z;
        }
    }

    /* access modifiers changed from: private */
    public void updateTouchableRegionAfterLayout() {
        View view = this.mNotificationPanelView;
        if (view != null) {
            this.mForceCollapsedUntilLayout = true;
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    if (!StatusBarTouchableRegionManager.this.mNotificationPanelView.isVisibleToUser()) {
                        StatusBarTouchableRegionManager.this.mNotificationPanelView.removeOnLayoutChangeListener(this);
                        boolean unused = StatusBarTouchableRegionManager.this.mForceCollapsedUntilLayout = false;
                        StatusBarTouchableRegionManager.this.updateTouchableRegion();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void updateRegionForNotch(Region region) {
        WindowInsets rootWindowInsets = this.mNotificationShadeWindowView.getRootWindowInsets();
        if (rootWindowInsets == null) {
            Log.w(TAG, "StatusBarWindowView is not attached.");
            return;
        }
        DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
        if (displayCutout != null) {
            Rect rect = new Rect();
            ScreenDecorations.DisplayCutoutView.boundsFromDirection(displayCutout, 48, rect);
            rect.offset(0, this.mDisplayCutoutTouchableRegionSize);
            region.union(rect);
        }
    }
}
