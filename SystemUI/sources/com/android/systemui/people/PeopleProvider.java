package com.android.systemui.people;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.shared.system.PeopleProviderUtils;
import javax.inject.Inject;

public class PeopleProvider extends ContentProvider implements SystemUIAppComponentFactory.ContextInitializer {
    private static final boolean DEBUG = false;
    private static final String EMPTY_STRING = "";
    private static final String TAG = "PeopleProvider";
    private SystemUIAppComponentFactory.ContextAvailableCallback mCallback;
    @Inject
    PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;

    public boolean onCreate() {
        return true;
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        if (!doesCallerHavePermission()) {
            String callingPackage = getCallingPackage();
            Log.w(TAG, "API not accessible to calling package: " + callingPackage);
            throw new SecurityException("API not accessible to calling package: " + callingPackage);
        } else if (!PeopleProviderUtils.GET_PEOPLE_TILE_PREVIEW_METHOD.equals(str)) {
            Log.w(TAG, "Invalid method");
            throw new IllegalArgumentException("Invalid method");
        } else if (bundle != null) {
            String string = bundle.getString("shortcut_id", "");
            String string2 = bundle.getString("package_name", "");
            UserHandle userHandle = (UserHandle) bundle.getParcelable(PeopleProviderUtils.EXTRAS_KEY_USER_HANDLE);
            if (string.isEmpty()) {
                Log.w(TAG, "Invalid shortcut id");
                throw new IllegalArgumentException("Invalid shortcut id");
            } else if (string2.isEmpty()) {
                Log.w(TAG, "Invalid package name");
                throw new IllegalArgumentException("Invalid package name");
            } else if (userHandle != null) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = this.mPeopleSpaceWidgetManager;
                if (peopleSpaceWidgetManager == null) {
                    Log.e(TAG, "Could not initialize people widget manager");
                    return null;
                }
                RemoteViews preview = peopleSpaceWidgetManager.getPreview(string, userHandle, string2, bundle);
                if (preview == null) {
                    return null;
                }
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable(PeopleProviderUtils.RESPONSE_KEY_REMOTE_VIEWS, preview);
                return bundle2;
            } else {
                Log.w(TAG, "Null user handle");
                throw new IllegalArgumentException("Null user handle");
            }
        } else {
            Log.w(TAG, "Extras can't be null");
            throw new IllegalArgumentException("Extras can't be null");
        }
    }

    private boolean doesCallerHavePermission() {
        return getContext().checkPermission(PeopleProviderUtils.GET_PEOPLE_TILE_PREVIEW_PERMISSION, Binder.getCallingPid(), Binder.getCallingUid()) == 0;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        throw new IllegalArgumentException("Invalid method");
    }

    public String getType(Uri uri) {
        throw new IllegalArgumentException("Invalid method");
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new IllegalArgumentException("Invalid method");
    }

    public int delete(Uri uri, String str, String[] strArr) {
        throw new IllegalArgumentException("Invalid method");
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new IllegalArgumentException("Invalid method");
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        this.mCallback.onContextAvailable(context);
        super.attachInfo(context, providerInfo);
    }

    public void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mCallback = contextAvailableCallback;
    }
}
