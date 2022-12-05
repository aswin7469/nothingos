package com.android.nothingos.gamemode;
/* loaded from: classes4.dex */
public interface IGameModeController {
    void addGameModeListener(GameModeListener gameModeListener);

    String getGamingPackage();

    boolean isGaming();

    void onCreate();

    void onDestroy();

    void removeGameModeListener(GameModeListener gameModeListener);

    /* loaded from: classes4.dex */
    public interface GameModeListener {
        default void onEnterGameMode(String gamePkg) {
        }

        default void onGamingPackageChanged(String gamePkg) {
        }

        default void onExitGameMode(String gamePkg) {
        }
    }
}
