package com.android.nothingos.gamemode;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.nothingos.gamemode.IGameModeController;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes4.dex */
public class GameModeControllerImpl implements IGameModeController {
    private static final int DEF_GAME_MODE_GAMING = 0;
    public static final String GAME_MODE_SWITCH = "nt_game_mode_switch";
    private static final String TAG = "GameModeControllerImpl";
    private boolean mGaming;
    private String mGamingPkg;
    private final List<IGameModeController.GameModeListener> mListeners = new CopyOnWriteArrayList();
    private final Handler mMainHandler;
    private final ContentResolver mResolver;
    private final ContentObserver mSettingsObserver;
    public static final String GAME_MODE_GAMING = "nt_game_mode_gaming";
    private static final Uri URI_GAME_MODE_GAMING = Settings.Secure.getUriFor(GAME_MODE_GAMING);
    public static final String GAME_MODE_GAMING_PKG = "nt_game_mode_gaming_pkg";
    private static final Uri URI_GAME_MODE_GAMING_PKG = Settings.Secure.getUriFor(GAME_MODE_GAMING_PKG);

    public static GameModeControllerImpl createDefault(Context context) {
        return new GameModeControllerImpl(context);
    }

    public GameModeControllerImpl(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mMainHandler = handler;
        this.mSettingsObserver = new ContentObserver(handler) { // from class: com.android.nothingos.gamemode.GameModeControllerImpl.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                if (GameModeControllerImpl.URI_GAME_MODE_GAMING.equals(uri)) {
                    boolean z = false;
                    if (Settings.Secure.getInt(GameModeControllerImpl.this.mResolver, GameModeControllerImpl.GAME_MODE_GAMING, 0) == 1) {
                        z = true;
                    }
                    boolean gaming = z;
                    String gamingPkg = Settings.Secure.getString(GameModeControllerImpl.this.mResolver, GameModeControllerImpl.GAME_MODE_GAMING_PKG);
                    GameModeControllerImpl.this.handleGamingChanged(gaming, gamingPkg);
                } else if (GameModeControllerImpl.URI_GAME_MODE_GAMING_PKG.equals(uri)) {
                    String pkg = Settings.Secure.getString(GameModeControllerImpl.this.mResolver, GameModeControllerImpl.GAME_MODE_GAMING_PKG);
                    GameModeControllerImpl.this.handleGamingPackageChanged(pkg);
                }
            }
        };
        this.mResolver = context.getContentResolver();
    }

    @Override // com.android.nothingos.gamemode.IGameModeController
    public void onCreate() {
        boolean z = false;
        this.mResolver.registerContentObserver(URI_GAME_MODE_GAMING, false, this.mSettingsObserver);
        this.mResolver.registerContentObserver(URI_GAME_MODE_GAMING_PKG, false, this.mSettingsObserver);
        if (Settings.Secure.getInt(this.mResolver, GAME_MODE_GAMING, 0) == 1) {
            z = true;
        }
        boolean gaming = z;
        String gamingPkg = Settings.Secure.getString(this.mResolver, GAME_MODE_GAMING_PKG);
        handleGamingChanged(gaming, gamingPkg);
    }

    @Override // com.android.nothingos.gamemode.IGameModeController
    public void onDestroy() {
        this.mResolver.unregisterContentObserver(this.mSettingsObserver);
    }

    @Override // com.android.nothingos.gamemode.IGameModeController
    public boolean isGaming() {
        return this.mGaming;
    }

    @Override // com.android.nothingos.gamemode.IGameModeController
    public String getGamingPackage() {
        return this.mGamingPkg;
    }

    @Override // com.android.nothingos.gamemode.IGameModeController
    public void addGameModeListener(IGameModeController.GameModeListener listener) {
        this.mListeners.add(listener);
        sendUpdates(listener);
    }

    @Override // com.android.nothingos.gamemode.IGameModeController
    public void removeGameModeListener(IGameModeController.GameModeListener listener) {
        this.mListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGamingChanged(boolean gaming, String gamingPkg) {
        if (gaming) {
            if (!this.mGaming) {
                enterGameMode(gamingPkg);
            }
            handleGamingPackageChanged(gamingPkg);
        } else if (this.mGaming) {
            exitGameMode();
        }
    }

    private void enterGameMode(String gamingPkg) {
        this.mGaming = true;
        for (IGameModeController.GameModeListener l : this.mListeners) {
            l.onEnterGameMode(gamingPkg);
        }
    }

    private void exitGameMode() {
        String lastGamingPkg = this.mGamingPkg;
        this.mGaming = false;
        this.mGamingPkg = null;
        for (IGameModeController.GameModeListener l : this.mListeners) {
            l.onExitGameMode(lastGamingPkg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGamingPackageChanged(String gamingPkg) {
        if (this.mGaming) {
            if (!TextUtils.equals(this.mGamingPkg, gamingPkg)) {
                this.mGamingPkg = gamingPkg;
                for (IGameModeController.GameModeListener l : this.mListeners) {
                    l.onGamingPackageChanged(gamingPkg);
                }
                return;
            }
            return;
        }
        Log.w(TAG, "Unexpected gaming package changed when not gaming");
    }

    private void sendUpdates(IGameModeController.GameModeListener listener) {
        if (this.mGaming) {
            listener.onEnterGameMode(this.mGamingPkg);
        }
    }
}
