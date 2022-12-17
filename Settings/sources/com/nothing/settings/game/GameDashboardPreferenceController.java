package com.nothing.settings.game;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.core.BasePreferenceController;

public class GameDashboardPreferenceController extends BasePreferenceController {
    private static final String GAME_ACTION = "com.google.android.gms.gp.gameservice.VIEW_SETTINGS";
    private static final String GAME_ACTION_CLASS = "com.google.android.gms.gp.gameservice.SettingsActivity";
    private static final String GAME_ACTION_PACKAGE = "com.google.android.gms";
    private static final String KEY_GAME_DASHBOARD = "gm_setting_game_dashboard";
    private static final String TAG = "GameDashboardPreferenceController";

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GameDashboardPreferenceController(Context context, String str) {
        super(context, KEY_GAME_DASHBOARD);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0046 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getAvailabilityStatus() {
        /*
            r5 = this;
            r0 = 0
            android.content.Intent r1 = new android.content.Intent     // Catch:{ Exception -> 0x003b }
            r1.<init>()     // Catch:{ Exception -> 0x003b }
            java.lang.String r2 = "com.google.android.gms.gp.gameservice.VIEW_SETTINGS"
            r1.setAction(r2)     // Catch:{ Exception -> 0x003b }
            java.lang.String r2 = "android.intent.category.DEFAULT"
            r1.addCategory(r2)     // Catch:{ Exception -> 0x003b }
            android.content.ComponentName r2 = new android.content.ComponentName     // Catch:{ Exception -> 0x003b }
            java.lang.String r3 = "com.google.android.gms"
            java.lang.String r4 = "com.google.android.gms.gp.gameservice.SettingsActivity"
            r2.<init>(r3, r4)     // Catch:{ Exception -> 0x003b }
            r1.setComponent(r2)     // Catch:{ Exception -> 0x003b }
            android.content.Context r5 = r5.mContext     // Catch:{ Exception -> 0x003b }
            android.content.pm.PackageManager r5 = r5.getPackageManager()     // Catch:{ Exception -> 0x003b }
            r2 = 131072(0x20000, float:1.83671E-40)
            java.util.List r5 = r5.queryIntentActivities(r1, r2)     // Catch:{ Exception -> 0x003b }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ Exception -> 0x003b }
            r1 = r0
        L_0x002d:
            boolean r2 = r5.hasNext()     // Catch:{ Exception -> 0x003c }
            if (r2 == 0) goto L_0x0043
            java.lang.Object r2 = r5.next()     // Catch:{ Exception -> 0x003c }
            android.content.pm.ResolveInfo r2 = (android.content.pm.ResolveInfo) r2     // Catch:{ Exception -> 0x003c }
            r1 = 1
            goto L_0x002d
        L_0x003b:
            r1 = r0
        L_0x003c:
            java.lang.String r5 = "GameDashboardPreferenceController"
            java.lang.String r2 = "Error initial getAvailabilityStatus"
            android.util.Log.w(r5, r2)
        L_0x0043:
            if (r1 == 0) goto L_0x0046
            goto L_0x0047
        L_0x0046:
            r0 = 3
        L_0x0047:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.game.GameDashboardPreferenceController.getAvailabilityStatus():int");
    }
}
