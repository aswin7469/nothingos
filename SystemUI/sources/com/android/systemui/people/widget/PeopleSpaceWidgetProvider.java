package com.android.systemui.people.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Bundle;
import javax.inject.Inject;

public class PeopleSpaceWidgetProvider extends AppWidgetProvider {
    private static final boolean DEBUG = false;
    public static final String EXTRA_NOTIFICATION_KEY = "extra_notification_key";
    public static final String EXTRA_PACKAGE_NAME = "extra_package_name";
    public static final String EXTRA_TILE_ID = "extra_tile_id";
    public static final String EXTRA_USER_HANDLE = "extra_user_handle";
    private static final String TAG = "PeopleSpaceWidgetPvd";
    public PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;

    @Inject
    PeopleSpaceWidgetProvider(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        super.onUpdate(context, appWidgetManager, iArr);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.updateWidgets(iArr);
    }

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int i, Bundle bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, i, bundle);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.onAppWidgetOptionsChanged(i, bundle);
    }

    public void onDeleted(Context context, int[] iArr) {
        super.onDeleted(context, iArr);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.deleteWidgets(iArr);
    }

    public void onRestored(Context context, int[] iArr, int[] iArr2) {
        super.onRestored(context, iArr, iArr2);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.remapWidgets(iArr, iArr2);
    }

    private void ensurePeopleSpaceWidgetManagerInitialized() {
        this.mPeopleSpaceWidgetManager.init();
    }

    public void setPeopleSpaceWidgetManager(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }
}
