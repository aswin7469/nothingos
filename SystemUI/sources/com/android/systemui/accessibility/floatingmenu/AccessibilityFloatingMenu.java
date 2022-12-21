package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.accessibility.dialog.AccessibilityTargetHelper;
import com.android.systemui.Prefs;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import com.android.systemui.shared.system.SysUiStatsLog;
import java.util.List;

public class AccessibilityFloatingMenu implements IAccessibilityFloatingMenu {
    private static final int DEFAULT_FADE_EFFECT_IS_ENABLED = 1;
    private static final int DEFAULT_MIGRATION_TOOLTIP_PROMPT_IS_DISABLED = 0;
    private static final float DEFAULT_OPACITY_VALUE = 0.55f;
    private static final float DEFAULT_POSITION_X_PERCENT = 1.0f;
    private static final float DEFAULT_POSITION_Y_PERCENT = 0.9f;
    private final ContentObserver mContentObserver;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final DockTooltipView mDockTooltipView;
    private final ContentObserver mEnabledA11yServicesContentObserver;
    private final ContentObserver mFadeOutContentObserver;
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public final AccessibilityFloatingMenuView mMenuView;
    private final MigrationTooltipView mMigrationTooltipView;
    private final ContentObserver mSizeContentObserver;

    public AccessibilityFloatingMenu(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(AccessibilityFloatingMenu.this.mContext, 0));
            }
        };
        this.mSizeContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.setSizeType(AccessibilityFloatingMenu.getSizeType(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mFadeOutContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.updateOpacityWith(AccessibilityFloatingMenu.isFadeEffectEnabled(AccessibilityFloatingMenu.this.mContext), AccessibilityFloatingMenu.getOpacityValue(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mEnabledA11yServicesContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onEnabledFeaturesChanged();
            }
        };
        this.mContext = context;
        AccessibilityFloatingMenuView accessibilityFloatingMenuView = new AccessibilityFloatingMenuView(context, getPosition(context));
        this.mMenuView = accessibilityFloatingMenuView;
        this.mMigrationTooltipView = new MigrationTooltipView(context, accessibilityFloatingMenuView);
        this.mDockTooltipView = new DockTooltipView(context, accessibilityFloatingMenuView);
    }

    AccessibilityFloatingMenu(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(AccessibilityFloatingMenu.this.mContext, 0));
            }
        };
        this.mSizeContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.setSizeType(AccessibilityFloatingMenu.getSizeType(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mFadeOutContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.updateOpacityWith(AccessibilityFloatingMenu.isFadeEffectEnabled(AccessibilityFloatingMenu.this.mContext), AccessibilityFloatingMenu.getOpacityValue(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mEnabledA11yServicesContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onEnabledFeaturesChanged();
            }
        };
        this.mContext = context;
        this.mMenuView = accessibilityFloatingMenuView;
        this.mMigrationTooltipView = new MigrationTooltipView(context, accessibilityFloatingMenuView);
        this.mDockTooltipView = new DockTooltipView(context, accessibilityFloatingMenuView);
    }

    public boolean isShowing() {
        return this.mMenuView.isShowing();
    }

    public void show() {
        if (!isShowing()) {
            List targets = AccessibilityTargetHelper.getTargets(this.mContext, 0);
            if (!targets.isEmpty()) {
                this.mMenuView.show();
                this.mMenuView.onTargetsChanged(targets);
                this.mMenuView.updateOpacityWith(isFadeEffectEnabled(this.mContext), getOpacityValue(this.mContext));
                this.mMenuView.setSizeType(getSizeType(this.mContext));
                this.mMenuView.setShapeType(getShapeType(this.mContext));
                this.mMenuView.setOnDragEndListener(new AccessibilityFloatingMenu$$ExternalSyntheticLambda0(this));
                showMigrationTooltipIfNecessary();
                registerContentObservers();
            }
        }
    }

    public void hide() {
        if (isShowing()) {
            this.mMenuView.hide();
            this.mMenuView.setOnDragEndListener((AccessibilityFloatingMenuView.OnDragEndListener) null);
            this.mMigrationTooltipView.hide();
            this.mDockTooltipView.hide();
            unregisterContentObservers();
        }
    }

    private Position getPosition(Context context) {
        String string = Prefs.getString(context, Prefs.Key.ACCESSIBILITY_FLOATING_MENU_POSITION, (String) null);
        if (TextUtils.isEmpty(string)) {
            return new Position(1.0f, 0.9f);
        }
        return Position.fromString(string);
    }

    private void showMigrationTooltipIfNecessary() {
        if (isMigrationTooltipPromptEnabled(this.mContext)) {
            this.mMigrationTooltipView.show();
            Settings.Secure.putInt(this.mContext.getContentResolver(), "accessibility_floating_menu_migration_tooltip_prompt", 0);
        }
    }

    private static boolean isMigrationTooltipPromptEnabled(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_migration_tooltip_prompt", 0) == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void onDragEnd(Position position) {
        SysUiStatsLog.write((int) SysUiStatsLog.ACCESSIBILITY_FLOATING_MENU_UI_CHANGED, position.getPercentageX(), position.getPercentageY(), this.mContext.getResources().getConfiguration().orientation);
        savePosition(this.mContext, position);
        showDockTooltipIfNecessary(this.mContext);
    }

    private void savePosition(Context context, Position position) {
        Prefs.putString(context, Prefs.Key.ACCESSIBILITY_FLOATING_MENU_POSITION, position.toString());
    }

    private void showDockTooltipIfNecessary(Context context) {
        if (!Prefs.get(context).getBoolean(Prefs.Key.HAS_SEEN_ACCESSIBILITY_FLOATING_MENU_DOCK_TOOLTIP, false)) {
            if (this.mMenuView.isOvalShape()) {
                this.mDockTooltipView.show();
            }
            Prefs.putBoolean(context, Prefs.Key.HAS_SEEN_ACCESSIBILITY_FLOATING_MENU_DOCK_TOOLTIP, true);
        }
    }

    /* access modifiers changed from: private */
    public static boolean isFadeEffectEnabled(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_fade_enabled", 1) == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static float getOpacityValue(Context context) {
        return Settings.Secure.getFloat(context.getContentResolver(), "accessibility_floating_menu_opacity", DEFAULT_OPACITY_VALUE);
    }

    /* access modifiers changed from: private */
    public static int getSizeType(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_size", 0);
    }

    private static int getShapeType(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_icon_type", 0);
    }

    private void registerContentObservers() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_button_targets"), false, this.mContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_size"), false, this.mSizeContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_fade_enabled"), false, this.mFadeOutContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_opacity"), false, this.mFadeOutContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("enabled_accessibility_services"), false, this.mEnabledA11yServicesContentObserver, -2);
    }

    private void unregisterContentObservers() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mSizeContentObserver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mFadeOutContentObserver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mEnabledA11yServicesContentObserver);
    }
}
