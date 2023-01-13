package com.android.systemui.flags;

import android.net.connectivity.android.net.mdns.aidl.IMDnsEventListener;
import android.safetycenter.SafetyCenterStatus;
import com.android.systemui.C1894R;
import com.nothing.p023os.device.DeviceConstant;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flags {
    public static final ResourceBooleanFlag BOUNCER_USER_SWITCHER = new ResourceBooleanFlag(204, C1894R.bool.config_enableBouncerUserSwitcher);
    public static final ResourceBooleanFlag CHARGING_RIPPLE = new ResourceBooleanFlag(203, C1894R.bool.flag_charging_ripple);
    public static final BooleanFlag COMBINED_QS_HEADERS = new BooleanFlag(501, false);
    public static final BooleanFlag COMBINED_STATUS_BAR_SIGNAL_ICONS = new BooleanFlag((int) DeviceConstant.ORDER_SOUND, false);
    public static final ResourceBooleanFlag FULL_SCREEN_USER_SWITCHER = new ResourceBooleanFlag(506, C1894R.bool.config_enableFullscreenUserSwitcher);
    public static final BooleanFlag LOCKSCREEN_ANIMATIONS = new BooleanFlag(201, true);
    public static final BooleanFlag MEDIA_MUTE_AWAIT = new BooleanFlag(904, true);
    public static final BooleanFlag MEDIA_NEARBY_DEVICES = new BooleanFlag(903, true);
    public static final BooleanFlag MEDIA_SESSION_ACTIONS = new BooleanFlag(901, false);
    public static final BooleanFlag MEDIA_TAP_TO_TRANSFER = new BooleanFlag(900, false);
    public static final ResourceBooleanFlag MONET = new ResourceBooleanFlag(800, C1894R.bool.flag_monet);
    @Deprecated
    public static final BooleanFlag NEW_FOOTER = new BooleanFlag((int) HttpURLConnection.HTTP_GATEWAY_TIMEOUT, true);
    public static final BooleanFlag NEW_HEADER = new BooleanFlag((int) HttpURLConnection.HTTP_VERSION, false);
    public static final BooleanFlag NEW_NOTIFICATION_PIPELINE_RENDERING = new BooleanFlag(101, true);
    public static final BooleanFlag NEW_PIPELINE_CRASH_ON_CALL_TO_OLD_PIPELINE = new BooleanFlag(107, false);
    public static final BooleanFlag NEW_UNLOCK_SWIPE_ANIMATION = new BooleanFlag(202, true);
    @Deprecated
    public static final BooleanFlag NEW_USER_SWITCHER = new BooleanFlag(500, true);
    public static final ResourceBooleanFlag NOTIFICATION_DRAG_TO_CONTENTS = new ResourceBooleanFlag(108, C1894R.bool.config_notificationToContents);
    public static final BooleanFlag NOTIFICATION_PIPELINE_DEVELOPER_LOGGING = new BooleanFlag(103, false);
    public static final BooleanFlag NSSL_DEBUG_LINES = new BooleanFlag(105, false);
    public static final BooleanFlag NSSL_DEBUG_REMOVE_ANIMATION = new BooleanFlag(106, false);
    public static final BooleanFlag ONGOING_CALL_IN_IMMERSIVE = new BooleanFlag((int) DeviceConstant.ORDER_ADVANCED_FEATURES, true);
    public static final BooleanFlag ONGOING_CALL_IN_IMMERSIVE_CHIP_TAP = new BooleanFlag(702, true);
    public static final BooleanFlag ONGOING_CALL_STATUS_BAR_CHIP = new BooleanFlag(700, true);
    public static final ResourceBooleanFlag PEOPLE_TILE = new ResourceBooleanFlag(HttpURLConnection.HTTP_BAD_GATEWAY, C1894R.bool.flag_conversations);
    public static final BooleanFlag POWER_MENU_LITE = new BooleanFlag(300, true);
    public static final ResourceBooleanFlag QS_USER_DETAIL_SHORTCUT = new ResourceBooleanFlag(HttpURLConnection.HTTP_UNAVAILABLE, C1894R.bool.flag_lockscreen_qs_user_detail_shortcut);
    public static final BooleanFlag SIMULATE_DOCK_THROUGH_CHARGING = new BooleanFlag(1000, true);
    public static final ResourceBooleanFlag SMARTSPACE = new ResourceBooleanFlag(402, C1894R.bool.flag_smartspace);
    public static final BooleanFlag SMARTSPACE_DEDUPING = new BooleanFlag(400, true);
    public static final BooleanFlag SMARTSPACE_SHARED_ELEMENT_TRANSITION_ENABLED = new BooleanFlag(401, true);
    public static final ResourceBooleanFlag STATUS_BAR_USER_SWITCHER = new ResourceBooleanFlag(IMDnsEventListener.SERVICE_DISCOVERY_FAILED, C1894R.bool.flag_user_switcher_chip);
    public static final BooleanFlag TEAMFOOD = new BooleanFlag(1, false);
    public static final SysPropBooleanFlag WM_ALWAYS_ENFORCE_PREDICTIVE_BACK = new SysPropBooleanFlag(1202, "persist.wm.debug.predictive_back_always_enforce", false);
    public static final SysPropBooleanFlag WM_ENABLE_PREDICTIVE_BACK = new SysPropBooleanFlag(1200, "persist.wm.debug.predictive_back", true);
    public static final SysPropBooleanFlag WM_ENABLE_PREDICTIVE_BACK_ANIM = new SysPropBooleanFlag(1201, "persist.wm.debug.predictive_back_anim", false);
    public static final SysPropBooleanFlag WM_ENABLE_SHELL_TRANSITIONS = new SysPropBooleanFlag(SafetyCenterStatus.OVERALL_SEVERITY_LEVEL_OK, "persist.wm.debug.shell_transit", false);
    private static Map<Integer, Flag<?>> sFlagMap;

    static Map<Integer, Flag<?>> collectFlags() {
        Map<Integer, Flag<?>> map = sFlagMap;
        if (map != null) {
            return map;
        }
        HashMap hashMap = new HashMap();
        for (Field field : getFlagFields()) {
            try {
                Flag flag = (Flag) field.get((Object) null);
                hashMap.put(Integer.valueOf(flag.getId()), flag);
            } catch (IllegalAccessException unused) {
            }
        }
        sFlagMap = hashMap;
        return hashMap;
    }

    static List<Field> getFlagFields() {
        Field[] fields = Flags.class.getFields();
        ArrayList arrayList = new ArrayList();
        for (Field field : fields) {
            if (Flag.class.isAssignableFrom(field.getType())) {
                arrayList.add(field);
            }
        }
        return arrayList;
    }
}
