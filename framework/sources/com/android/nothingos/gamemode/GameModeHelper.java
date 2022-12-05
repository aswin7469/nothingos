package com.android.nothingos.gamemode;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import com.android.internal.R;
import com.android.internal.os.BackgroundThread;
import com.android.nothingos.gamemode.IGameModeController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
/* loaded from: classes4.dex */
public class GameModeHelper {
    public static final int FLAGS_LESS_DISTRACT_NOTIFICATION = 8192;
    public static final String KEY_STARRED_CONTACT = "starred_contact";
    private static final float LANDSCAPE_SWIPE_FROM_TOP_WIDTH_FRACTION = 0.33333334f;
    private static final int MISTOUCH_PREVENTION_GESTURE_OFF = 0;
    private static final int MISTOUCH_PREVENTION_GESTURE_ON = 1;
    public static final int NOTIFICATION_DISPLAY_BLOCK = 2;
    public static final int NOTIFICATION_DISPLAY_FLOAT = 0;
    public static final int NOTIFICATION_DISPLAY_LESS_DISTRACT = 1;
    public static final String STARRED_CONTACT = "starred";
    private static final String TAG = "GameModeHelper";
    private static GameModeHelper sInstance;
    private boolean mAllowStarredContacts;
    private boolean mBlockIncomingCalls;
    private final ContentObserver mContentObserver;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final IGameModeController mGameModeController;
    private final ArrayList<String> mLessDistractNotificationBlockList;
    private boolean mMistouchPreventionOn;
    private int mNotificationDisplayMode;
    private final WindowManager mWindowManager;
    private static final ArrayList<String> sBlockThirdInComingCalls = new ArrayList<>();
    private static final ArrayList<String> sDefalutBlockThirdInComingCalls = new ArrayList<>();
    private static final String GAME_MODE_NOTIFICATION_DISPALYE_MODE = "nt_game_mode_notification_display_mode";
    private static final Uri URI_GAME_MODE_NOTIFICATION_DISPALYE_MODE = Settings.Secure.getUriFor(GAME_MODE_NOTIFICATION_DISPALYE_MODE);
    private static final String GAME_MODE_BLOCK_INCOMING_CALLS = "nt_game_mode_block_incoming_calls";
    private static final Uri URI_GAME_MODE_BLOCK_INCOMING_CALLS = Settings.Secure.getUriFor(GAME_MODE_BLOCK_INCOMING_CALLS);
    private static final String GAME_MODE_ALLOW_STARRED_CONTACTS = "nt_game_mode_allow_starred_contacts";
    private static final Uri URI_GAME_MODE_ALLOW_STARRED_CONTACTS = Settings.Secure.getUriFor(GAME_MODE_ALLOW_STARRED_CONTACTS);
    private static final String SETTING_GAME_MODE_MISTOUCH_PREVENTION = "nt_game_mode_mistouch_prevention";
    private static final Uri URI_GAME_MODE_MISTOUCH_PREVENTION = Settings.Secure.getUriFor(SETTING_GAME_MODE_MISTOUCH_PREVENTION);
    String KEY_GAME_MODE_BLOCK_INCOMING_CALL_BLACKLIST = "nt_game_mode_block_incoming_call_package_list";
    private OnPropertiesChangedListener mProperListener = new OnPropertiesChangedListener();
    private final Object mLock = new Object();

    public static GameModeHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GameModeHelper.class) {
                if (sInstance == null) {
                    GameModeHelper gameModeHelper = new GameModeHelper(context);
                    sInstance = gameModeHelper;
                    gameModeHelper.register();
                }
            }
        }
        return sInstance;
    }

    private GameModeHelper(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        this.mLessDistractNotificationBlockList = arrayList;
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        ContentResolver contentResolver = context.getContentResolver();
        this.mContentResolver = contentResolver;
        this.mGameModeController = GameModeControllerImpl.createDefault(applicationContext);
        this.mContentObserver = new ContentObserver(applicationContext.getMainThreadHandler()) { // from class: com.android.nothingos.gamemode.GameModeHelper.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                if (GameModeHelper.URI_GAME_MODE_MISTOUCH_PREVENTION.equals(uri)) {
                    GameModeHelper.this.updateMistouchPrevention();
                    return;
                }
                boolean z = false;
                if (!GameModeHelper.URI_GAME_MODE_NOTIFICATION_DISPALYE_MODE.equals(uri)) {
                    if (!GameModeHelper.URI_GAME_MODE_BLOCK_INCOMING_CALLS.equals(uri)) {
                        if (GameModeHelper.URI_GAME_MODE_ALLOW_STARRED_CONTACTS.equals(uri)) {
                            GameModeHelper gameModeHelper = GameModeHelper.this;
                            if (Settings.Secure.getInt(gameModeHelper.mContentResolver, GameModeHelper.GAME_MODE_ALLOW_STARRED_CONTACTS, 0) == 1) {
                                z = true;
                            }
                            gameModeHelper.mAllowStarredContacts = z;
                            return;
                        }
                        return;
                    }
                    GameModeHelper gameModeHelper2 = GameModeHelper.this;
                    if (Settings.Secure.getInt(gameModeHelper2.mContentResolver, GameModeHelper.GAME_MODE_BLOCK_INCOMING_CALLS, 0) == 1) {
                        z = true;
                    }
                    gameModeHelper2.mBlockIncomingCalls = z;
                    return;
                }
                GameModeHelper gameModeHelper3 = GameModeHelper.this;
                gameModeHelper3.mNotificationDisplayMode = Settings.Secure.getInt(gameModeHelper3.mContentResolver, GameModeHelper.GAME_MODE_NOTIFICATION_DISPALYE_MODE, 0);
            }
        };
        Collections.addAll(arrayList, context.getResources().getStringArray(R.array.config_game_mode_less_distract_notification_block_list));
        Collections.addAll(sDefalutBlockThirdInComingCalls, context.getResources().getStringArray(R.array.config_game_mode_block_incoming_call_package_list));
        boolean z = false;
        this.mNotificationDisplayMode = Settings.Secure.getInt(contentResolver, GAME_MODE_NOTIFICATION_DISPALYE_MODE, 0);
        this.mBlockIncomingCalls = Settings.Secure.getInt(contentResolver, GAME_MODE_BLOCK_INCOMING_CALLS, 0) == 1;
        this.mAllowStarredContacts = Settings.Secure.getInt(contentResolver, GAME_MODE_ALLOW_STARRED_CONTACTS, 0) == 1 ? true : z;
    }

    private void register() {
        this.mGameModeController.onCreate();
        updateMistouchPrevention();
        this.mContentResolver.registerContentObserver(URI_GAME_MODE_MISTOUCH_PREVENTION, false, this.mContentObserver);
        this.mContentResolver.registerContentObserver(URI_GAME_MODE_NOTIFICATION_DISPALYE_MODE, false, this.mContentObserver);
        this.mContentResolver.registerContentObserver(URI_GAME_MODE_BLOCK_INCOMING_CALLS, false, this.mContentObserver);
        this.mContentResolver.registerContentObserver(URI_GAME_MODE_ALLOW_STARRED_CONTACTS, false, this.mContentObserver);
        DeviceConfig.addOnPropertiesChangedListener(DeviceConfig.NAMESPACE_SYSTEMUI, BackgroundThread.getExecutor(), this.mProperListener);
        String property = DeviceConfig.getProperty(DeviceConfig.NAMESPACE_SYSTEMUI, this.KEY_GAME_MODE_BLOCK_INCOMING_CALL_BLACKLIST);
        updateBlockImcominglist(property);
    }

    private void unregister() {
        this.mGameModeController.onDestroy();
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
        DeviceConfig.removeOnPropertiesChangedListener(this.mProperListener);
        sBlockThirdInComingCalls.clear();
    }

    public boolean isGaming() {
        return this.mGameModeController.isGaming();
    }

    public boolean isMistouchPreventionOn() {
        return isGaming() && this.mMistouchPreventionOn;
    }

    public boolean isInAllowedSwipeFromTopRegion(float x) {
        int rotation = this.mWindowManager.getDefaultDisplay().getRotation();
        boolean landscape = rotation == 1 || rotation == 3;
        if (landscape) {
            Rect bounds = this.mWindowManager.getMaximumWindowMetrics().getBounds();
            int width = bounds.width();
            float left = (width * 0.6666666f) / 2.0f;
            float right = (width * 1.3333334f) / 2.0f;
            return left <= x && x <= right;
        }
        return true;
    }

    public void addGameModeListener(IGameModeController.GameModeListener listener) {
        this.mGameModeController.addGameModeListener(listener);
    }

    public void removeGameModeListener(IGameModeController.GameModeListener listener) {
        this.mGameModeController.removeGameModeListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMistouchPrevention() {
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.mContentResolver, SETTING_GAME_MODE_MISTOUCH_PREVENTION, 0, -2) == 1) {
            z = true;
        }
        this.mMistouchPreventionOn = z;
    }

    public int getNotificationDisplayMode() {
        if (!isGaming()) {
            return 0;
        }
        return this.mNotificationDisplayMode;
    }

    public boolean isBlockInComingCalls() {
        if (!isGaming()) {
            return false;
        }
        return this.mBlockIncomingCalls;
    }

    public boolean isBlockThirdInComingCalls(String packageName) {
        if (!isGaming()) {
            return false;
        }
        return sBlockThirdInComingCalls.contains(packageName);
    }

    public boolean isAllowStarredContacts() {
        if (!isGaming()) {
            return false;
        }
        return this.mAllowStarredContacts;
    }

    public boolean isAllowLessDistractNotification(String pkg) {
        return isGaming() && 1 == getNotificationDisplayMode() && !this.mLessDistractNotificationBlockList.contains(pkg);
    }

    /* loaded from: classes4.dex */
    public static abstract class GameModeSettingChangeListener {
        Uri uri;

        public abstract void onChanged(Uri uri);

        public GameModeSettingChangeListener(Uri uri) {
            this.uri = uri;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class OnPropertiesChangedListener implements DeviceConfig.OnPropertiesChangedListener {
        private OnPropertiesChangedListener() {
        }

        @Override // android.provider.DeviceConfig.OnPropertiesChangedListener
        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains(GameModeHelper.this.KEY_GAME_MODE_BLOCK_INCOMING_CALL_BLACKLIST)) {
                GameModeHelper gameModeHelper = GameModeHelper.this;
                gameModeHelper.updateBlockImcominglist(properties.getString(gameModeHelper.KEY_GAME_MODE_BLOCK_INCOMING_CALL_BLACKLIST, null));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBlockImcominglist(String property) {
        synchronized (this.mLock) {
            sBlockThirdInComingCalls.clear();
            if (property != null) {
                String[] packages = property.split(",");
                for (String pkg : packages) {
                    String pkgName = pkg.trim();
                    if (!pkgName.isEmpty()) {
                        sBlockThirdInComingCalls.add(pkgName);
                    }
                }
            } else {
                Iterator<String> it = sDefalutBlockThirdInComingCalls.iterator();
                while (it.hasNext()) {
                    String pkg2 = it.next();
                    sBlockThirdInComingCalls.add(pkg2);
                }
            }
            Log.i(TAG, "updateBlockImcominglist === " + sBlockThirdInComingCalls);
        }
    }
}
