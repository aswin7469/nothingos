package com.android.systemui;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Set;

public final class Prefs {

    @Retention(RetentionPolicy.SOURCE)
    public @interface Key {
        public static final String ACCESSIBILITY_FLOATING_MENU_POSITION = "AccessibilityFloatingMenuPosition";
        public static final String COLOR_INVERSION_TILE_LAST_USED = "ColorInversionTileLastUsed";
        public static final String CONTROLS_STRUCTURE_SWIPE_TOOLTIP_COUNT = "ControlsStructureSwipeTooltipCount";
        public static final String DEBUG_MODE_ENABLED = "debugModeEnabled";
        public static final String DND_CONFIRMED_ALARM_INTRODUCTION = "DndConfirmedAlarmIntroduction";
        public static final String DND_CONFIRMED_PRIORITY_INTRODUCTION = "DndConfirmedPriorityIntroduction";
        public static final String DND_CONFIRMED_SILENCE_INTRODUCTION = "DndConfirmedSilenceIntroduction";
        public static final String DND_FAVORITE_BUCKET_INDEX = "DndCountdownMinuteIndex";
        public static final String DND_FAVORITE_ZEN = "DndFavoriteZen";
        public static final String DND_NONE_SELECTED = "DndNoneSelected";
        public static final String DND_TILE_COMBINED_ICON = "DndTileCombinedIcon";
        public static final String DND_TILE_VISIBLE = "DndTileVisible";
        public static final String HAS_CLICKED_NUDGE_TO_SETUP_DREAM = "HasClickedNudgeToSetupDream";
        public static final String HAS_DISMISSED_NUDGE_TO_SETUP_DREAM = "HasDismissedNudgeToSetupDream";
        public static final String HAS_SEEN_ACCESSIBILITY_FLOATING_MENU_DOCK_TOOLTIP = "HasSeenAccessibilityFloatingMenuDockTooltip";
        public static final String HAS_SEEN_ODI_CAPTIONS_TOOLTIP = "HasSeenODICaptionsTooltip";
        public static final String HAS_SEEN_REVERSE_BOTTOM_SHEET = "HasSeenReverseBottomSheet";
        public static final String HOTSPOT_TILE_LAST_USED = "HotspotTileLastUsed";
        @Deprecated
        public static final String OVERVIEW_LAST_STACK_TASK_ACTIVE_TIME = "OverviewLastStackTaskActiveTime";
        @Deprecated
        public static final String QS_DATA_SAVER_ADDED = "QsDataSaverAdded";
        public static final String QS_DATA_SAVER_DIALOG_SHOWN = "QsDataSaverDialogShown";
        public static final String QS_HAS_TURNED_OFF_MOBILE_DATA = "QsHasTurnedOffMobileData";
        @Deprecated
        public static final String QS_HOTSPOT_ADDED = "QsHotspotAdded";
        @Deprecated
        public static final String QS_INVERT_COLORS_ADDED = "QsInvertColorsAdded";
        public static final String QS_LONG_PRESS_TOOLTIP_SHOWN_COUNT = "QsLongPressTooltipShownCount";
        @Deprecated
        public static final String QS_NIGHTDISPLAY_ADDED = "QsNightDisplayAdded";
        public static final String QS_TILE_SPECS_REVEALED = "QsTileSpecsRevealed";
        @Deprecated
        public static final String QS_WORK_ADDED = "QsWorkAdded";
        public static final String SEEN_RINGER_GUIDANCE_COUNT = "RingerGuidanceCount";
        public static final String TOUCHED_RINGER_TOGGLE = "TouchedRingerToggle";
    }

    private Prefs() {
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return get(context).getBoolean(str, z);
    }

    public static void putBoolean(Context context, String str, boolean z) {
        get(context).edit().putBoolean(str, z).apply();
    }

    public static int getInt(Context context, String str, int i) {
        return get(context).getInt(str, i);
    }

    public static void putInt(Context context, String str, int i) {
        get(context).edit().putInt(str, i).apply();
    }

    public static long getLong(Context context, String str, long j) {
        return get(context).getLong(str, j);
    }

    public static void putLong(Context context, String str, long j) {
        get(context).edit().putLong(str, j).apply();
    }

    public static String getString(Context context, String str, String str2) {
        return get(context).getString(str, str2);
    }

    public static void putString(Context context, String str, String str2) {
        get(context).edit().putString(str, str2).apply();
    }

    public static void putStringSet(Context context, String str, Set<String> set) {
        get(context).edit().putStringSet(str, set).apply();
    }

    public static Set<String> getStringSet(Context context, String str, Set<String> set) {
        return get(context).getStringSet(str, set);
    }

    public static Map<String, ?> getAll(Context context) {
        return get(context).getAll();
    }

    public static void remove(Context context, String str) {
        get(context).edit().remove(str).apply();
    }

    public static void registerListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        get(context).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public static void unregisterListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        get(context).unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0);
    }
}
