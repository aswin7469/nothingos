package com.android.systemui.p012qs.tileimpl;

import androidx.constraintlayout.motion.widget.Key;
import androidx.core.app.NotificationCompat;
import com.android.systemui.C1893R;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.statusbar.phone.AutoTileManager;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b\u0003\bÁ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\u00062\b\u0010\b\u001a\u0004\u0018\u00010\u0005R\u001c\u0010\u0003\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/qs/tileimpl/SubtitleArrayMapping;", "", "()V", "subtitleIdsMap", "", "", "", "getSubtitleId", "spec", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.tileimpl.SubtitleArrayMapping */
/* compiled from: QSTileViewImpl.kt */
public final class SubtitleArrayMapping {
    public static final SubtitleArrayMapping INSTANCE = new SubtitleArrayMapping();
    private static final Map<String, Integer> subtitleIdsMap = MapsKt.mapOf(TuplesKt.m1796to("internet", Integer.valueOf((int) C1893R.array.tile_states_internet)), TuplesKt.m1796to("wifi", Integer.valueOf((int) C1893R.array.tile_states_wifi)), TuplesKt.m1796to("cell", Integer.valueOf((int) C1893R.array.tile_states_cell)), TuplesKt.m1796to(DemoMode.COMMAND_BATTERY, Integer.valueOf((int) C1893R.array.tile_states_battery)), TuplesKt.m1796to("dnd", Integer.valueOf((int) C1893R.array.tile_states_dnd)), TuplesKt.m1796to("flashlight", Integer.valueOf((int) C1893R.array.tile_states_flashlight)), TuplesKt.m1796to(Key.ROTATION, Integer.valueOf((int) C1893R.array.tile_states_rotation)), TuplesKt.m1796to("bt", Integer.valueOf((int) C1893R.array.tile_states_bt)), TuplesKt.m1796to("airplane", Integer.valueOf((int) C1893R.array.tile_states_airplane)), TuplesKt.m1796to("location", Integer.valueOf((int) C1893R.array.tile_states_location)), TuplesKt.m1796to(AutoTileManager.HOTSPOT, Integer.valueOf((int) C1893R.array.tile_states_hotspot)), TuplesKt.m1796to(AutoTileManager.INVERSION, Integer.valueOf((int) C1893R.array.tile_states_inversion)), TuplesKt.m1796to(AutoTileManager.SAVER, Integer.valueOf((int) C1893R.array.tile_states_saver)), TuplesKt.m1796to("dark", Integer.valueOf((int) C1893R.array.tile_states_dark)), TuplesKt.m1796to(AutoTileManager.WORK, Integer.valueOf((int) C1893R.array.tile_states_work)), TuplesKt.m1796to(AutoTileManager.CAST, Integer.valueOf((int) C1893R.array.tile_states_cast)), TuplesKt.m1796to(AutoTileManager.NIGHT, Integer.valueOf((int) C1893R.array.tile_states_night)), TuplesKt.m1796to("screenrecord", Integer.valueOf((int) C1893R.array.tile_states_screenrecord)), TuplesKt.m1796to("reverse", Integer.valueOf((int) C1893R.array.tile_states_reverse)), TuplesKt.m1796to(AutoTileManager.BRIGHTNESS, Integer.valueOf((int) C1893R.array.tile_states_reduce_brightness)), TuplesKt.m1796to("cameratoggle", Integer.valueOf((int) C1893R.array.tile_states_cameratoggle)), TuplesKt.m1796to("mictoggle", Integer.valueOf((int) C1893R.array.tile_states_mictoggle)), TuplesKt.m1796to(AutoTileManager.DEVICE_CONTROLS, Integer.valueOf((int) C1893R.array.tile_states_controls)), TuplesKt.m1796to(AutoTileManager.WALLET, Integer.valueOf((int) C1893R.array.tile_states_wallet)), TuplesKt.m1796to("qr_code_scanner", Integer.valueOf((int) C1893R.array.tile_states_qr_code_scanner)), TuplesKt.m1796to(NotificationCompat.CATEGORY_ALARM, Integer.valueOf((int) C1893R.array.tile_states_alarm)), TuplesKt.m1796to("onehanded", Integer.valueOf((int) C1893R.array.tile_states_onehanded)), TuplesKt.m1796to("color_correction", Integer.valueOf((int) C1893R.array.tile_states_color_correction)));

    private SubtitleArrayMapping() {
    }

    public final int getSubtitleId(String str) {
        return subtitleIdsMap.getOrDefault(str, Integer.valueOf((int) C1893R.array.tile_states_default)).intValue();
    }
}
