package android.media;

import android.bluetooth.BluetoothDevice;
import android.media.IAudioFocusDispatcher;
import android.media.IAudioModeDispatcher;
import android.media.IAudioRoutesObserver;
import android.media.IAudioServerStateDispatcher;
import android.media.ICapturePresetDevicesRoleDispatcher;
import android.media.ICommunicationDeviceDispatcher;
import android.media.IPlaybackConfigDispatcher;
import android.media.IRecordingConfigDispatcher;
import android.media.IRingtonePlayer;
import android.media.IStrategyPreferredDevicesDispatcher;
import android.media.IVolumeController;
import android.media.PlayerBase;
import android.media.audiopolicy.AudioPolicyConfig;
import android.media.audiopolicy.AudioProductStrategy;
import android.media.audiopolicy.AudioVolumeGroup;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.media.projection.IMediaProjection;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.KeyEvent;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public interface IAudioService extends IInterface {
    int abandonAudioFocus(IAudioFocusDispatcher iAudioFocusDispatcher, String str, AudioAttributes audioAttributes, String str2) throws RemoteException;

    int abandonAudioFocusForTest(IAudioFocusDispatcher iAudioFocusDispatcher, String str, AudioAttributes audioAttributes, String str2) throws RemoteException;

    int addMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void adjustStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    void adjustStreamVolumeForUid(int i, int i2, int i3, String str, int i4, int i5, UserHandle userHandle, int i6) throws RemoteException;

    void adjustSuggestedStreamVolume(int i, int i2, int i3, String str, String str2) throws RemoteException;

    void adjustSuggestedStreamVolumeForUid(int i, int i2, int i3, String str, int i4, int i5, UserHandle userHandle, int i6) throws RemoteException;

    boolean areNavigationRepeatSoundEffectsEnabled() throws RemoteException;

    void avrcpSupportsAbsoluteVolume(String str, boolean z) throws RemoteException;

    void cacheParameters(String str) throws RemoteException;

    int clearPreferredDevicesForCapturePreset(int i) throws RemoteException;

    void disableRingtoneSync(int i) throws RemoteException;

    void disableSafeMediaVolume(String str) throws RemoteException;

    int dispatchFocusChange(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void forceRemoteSubmixFullVolume(boolean z, IBinder iBinder) throws RemoteException;

    void forceVolumeControlStream(int i, IBinder iBinder) throws RemoteException;

    List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException;

    List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException;

    long getAdditionalOutputDeviceDelay(AudioDeviceAttributes audioDeviceAttributes) throws RemoteException;

    int getAllowedCapturePolicy() throws RemoteException;

    List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException;

    List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException;

    int[] getAvailableCommunicationDeviceIds() throws RemoteException;

    int getCommunicationDevice() throws RemoteException;

    int getCurrentAudioFocus() throws RemoteException;

    int getDeviceVolumeBehavior(AudioDeviceAttributes audioDeviceAttributes) throws RemoteException;

    List<AudioDeviceAttributes> getDevicesForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    int getDevicesForStream(int i) throws RemoteException;

    long getEnableDoVibrateTime(String str) throws RemoteException;

    int getEncodedSurroundMode(int i) throws RemoteException;

    long getFadeOutDurationOnFocusLossMillis(AudioAttributes audioAttributes) throws RemoteException;

    int getFocusRampTimeMs(int i, AudioAttributes audioAttributes) throws RemoteException;

    int getLastAudibleStreamVolume(int i) throws RemoteException;

    long getMaxAdditionalOutputDeviceDelay(AudioDeviceAttributes audioDeviceAttributes) throws RemoteException;

    int getMaxVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    int getMinVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    int getMode() throws RemoteException;

    List<AudioDeviceAttributes> getPreferredDevicesForCapturePreset(int i) throws RemoteException;

    List<AudioDeviceAttributes> getPreferredDevicesForStrategy(int i) throws RemoteException;

    List getReportedSurroundFormats() throws RemoteException;

    int getRingerModeExternal() throws RemoteException;

    int getRingerModeInternal() throws RemoteException;

    IRingtonePlayer getRingtonePlayer() throws RemoteException;

    int getStreamMaxVolume(int i) throws RemoteException;

    int getStreamMinVolume(int i) throws RemoteException;

    int getStreamVolume(int i) throws RemoteException;

    int[] getSupportedSystemUsages() throws RemoteException;

    Map getSurroundFormats() throws RemoteException;

    int getUiSoundsStreamType() throws RemoteException;

    int getVibrateSetting(int i) throws RemoteException;

    int getVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    void handleBluetoothA2dpActiveDeviceChange(BluetoothDevice bluetoothDevice, int i, int i2, boolean z, int i3) throws RemoteException;

    void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice bluetoothDevice) throws RemoteException;

    void handleVolumeKey(KeyEvent keyEvent, boolean z, String str, String str2) throws RemoteException;

    boolean hasHapticChannels(Uri uri) throws RemoteException;

    boolean hasRegisteredDynamicPolicy() throws RemoteException;

    boolean isAudioServerRunning() throws RemoteException;

    boolean isBluetoothA2dpOn() throws RemoteException;

    boolean isBluetoothScoOn() throws RemoteException;

    boolean isCallScreeningModeSupported() throws RemoteException;

    boolean isCameraSoundForced() throws RemoteException;

    boolean isHdmiSystemAudioSupported() throws RemoteException;

    boolean isHomeSoundEffectEnabled() throws RemoteException;

    boolean isMasterMute() throws RemoteException;

    boolean isMicrophoneMuted() throws RemoteException;

    boolean isMusicActive(boolean z) throws RemoteException;

    boolean isSpeakerphoneOn() throws RemoteException;

    boolean isStreamAffectedByMute(int i) throws RemoteException;

    boolean isStreamAffectedByRingerMode(int i) throws RemoteException;

    boolean isStreamMute(int i) throws RemoteException;

    boolean isSurroundFormatEnabled(int i) throws RemoteException;

    boolean isValidRingerMode(int i) throws RemoteException;

    boolean loadSoundEffects() throws RemoteException;

    void notifyVolumeControllerVisible(IVolumeController iVolumeController, boolean z) throws RemoteException;

    void playSoundEffect(int i) throws RemoteException;

    void playSoundEffectVolume(int i, float f) throws RemoteException;

    void playerAttributes(int i, AudioAttributes audioAttributes) throws RemoteException;

    void playerEvent(int i, int i2, int i3) throws RemoteException;

    void playerHasOpPlayAudio(int i, boolean z) throws RemoteException;

    void playerSessionId(int i, int i2) throws RemoteException;

    void recorderEvent(int i, int i2) throws RemoteException;

    String registerAudioPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback, boolean z, boolean z2, boolean z3, boolean z4, IMediaProjection iMediaProjection) throws RemoteException;

    void registerAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void registerCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher iCapturePresetDevicesRoleDispatcher) throws RemoteException;

    void registerCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher iCommunicationDeviceDispatcher) throws RemoteException;

    void registerModeDispatcher(IAudioModeDispatcher iAudioModeDispatcher) throws RemoteException;

    void registerPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    void registerRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;

    void registerStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher iStrategyPreferredDevicesDispatcher) throws RemoteException;

    void releasePlayer(int i) throws RemoteException;

    void releaseRecorder(int i) throws RemoteException;

    void reloadAudioSettings() throws RemoteException;

    int removeMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    int removePreferredDevicesForStrategy(int i) throws RemoteException;

    int removeUidDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i) throws RemoteException;

    int removeUserIdDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i) throws RemoteException;

    int requestAudioFocus(AudioAttributes audioAttributes, int i, IBinder iBinder, IAudioFocusDispatcher iAudioFocusDispatcher, String str, String str2, int i2, IAudioPolicyCallback iAudioPolicyCallback, int i3) throws RemoteException;

    int requestAudioFocusForTest(AudioAttributes audioAttributes, int i, IBinder iBinder, IAudioFocusDispatcher iAudioFocusDispatcher, String str, String str2, int i2, int i3) throws RemoteException;

    boolean setAdditionalOutputDeviceDelay(AudioDeviceAttributes audioDeviceAttributes, long j) throws RemoteException;

    int setAllowedCapturePolicy(int i) throws RemoteException;

    void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice bluetoothDevice, int i, int i2, boolean z, int i3) throws RemoteException;

    void setBluetoothA2dpOn(boolean z) throws RemoteException;

    void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice bluetoothDevice, int i, boolean z, int i2) throws RemoteException;

    void setBluetoothScoOn(boolean z) throws RemoteException;

    boolean setCommunicationDevice(IBinder iBinder, int i) throws RemoteException;

    void setDeviceVolumeBehavior(AudioDeviceAttributes audioDeviceAttributes, int i, String str) throws RemoteException;

    void setEnableDoVibrateTime(String str, long j) throws RemoteException;

    boolean setEncodedSurroundMode(int i) throws RemoteException;

    int setFocusPropertiesForPolicy(int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void setFocusRequestResultFromExtPolicy(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    int setHdmiSystemAudioSupported(boolean z) throws RemoteException;

    void setHomeSoundEffectEnabled(boolean z) throws RemoteException;

    void setMasterMute(boolean z, int i, String str, int i2) throws RemoteException;

    void setMicrophoneMute(boolean z, String str, int i) throws RemoteException;

    void setMicrophoneMuteFromSwitch(boolean z) throws RemoteException;

    void setMode(int i, IBinder iBinder, String str) throws RemoteException;

    void setMultiAudioFocusEnabled(boolean z) throws RemoteException;

    void setNavigationRepeatSoundEffectsEnabled(boolean z) throws RemoteException;

    void setNtParameters(String str, IBinder iBinder) throws RemoteException;

    int setPreferredDevicesForCapturePreset(int i, List<AudioDeviceAttributes> list) throws RemoteException;

    int setPreferredDevicesForStrategy(int i, List<AudioDeviceAttributes> list) throws RemoteException;

    void setRingerModeExternal(int i, String str) throws RemoteException;

    void setRingerModeInternal(int i, String str) throws RemoteException;

    void setRingtonePlayer(IRingtonePlayer iRingtonePlayer) throws RemoteException;

    void setRttEnabled(boolean z) throws RemoteException;

    void setSpeakerphoneOn(IBinder iBinder, boolean z) throws RemoteException;

    void setStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    void setStreamVolumeForUid(int i, int i2, int i3, String str, int i4, int i5, UserHandle userHandle, int i6) throws RemoteException;

    void setSupportedSystemUsages(int[] iArr) throws RemoteException;

    boolean setSurroundFormatEnabled(int i, boolean z) throws RemoteException;

    int setUidDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i, int[] iArr, String[] strArr) throws RemoteException;

    int setUserIdDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i, int[] iArr, String[] strArr) throws RemoteException;

    void setVibrateSetting(int i, int i2) throws RemoteException;

    void setVolumeController(IVolumeController iVolumeController) throws RemoteException;

    void setVolumeIndexForAttributes(AudioAttributes audioAttributes, int i, int i2, String str) throws RemoteException;

    void setVolumePolicy(VolumePolicy volumePolicy) throws RemoteException;

    void setWiredDeviceConnectionState(int i, int i2, String str, String str2, String str3) throws RemoteException;

    boolean shouldVibrate(int i) throws RemoteException;

    void startBluetoothSco(IBinder iBinder, int i) throws RemoteException;

    void startBluetoothScoVirtualCall(IBinder iBinder) throws RemoteException;

    AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver iAudioRoutesObserver) throws RemoteException;

    void stopBluetoothSco(IBinder iBinder) throws RemoteException;

    void trackAudioStart(int i, int i2, int i3, int i4) throws RemoteException;

    void trackAudioStop(int i, int i2, int i3, int i4) throws RemoteException;

    int trackPlayer(PlayerBase.PlayerIdCard playerIdCard) throws RemoteException;

    int trackRecorder(IBinder iBinder) throws RemoteException;

    void unloadSoundEffects() throws RemoteException;

    void unregisterAudioFocusClient(String str) throws RemoteException;

    void unregisterAudioPolicy(IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void unregisterAudioPolicyAsync(IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void unregisterCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher iCapturePresetDevicesRoleDispatcher) throws RemoteException;

    void unregisterCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher iCommunicationDeviceDispatcher) throws RemoteException;

    void unregisterModeDispatcher(IAudioModeDispatcher iAudioModeDispatcher) throws RemoteException;

    void unregisterPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    void unregisterRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;

    void unregisterStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher iStrategyPreferredDevicesDispatcher) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IAudioService {
        @Override // android.media.IAudioService
        public int trackPlayer(PlayerBase.PlayerIdCard pic) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void playerAttributes(int piid, AudioAttributes attr) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void playerEvent(int piid, int event, int deviceId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void releasePlayer(int piid) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int trackRecorder(IBinder recorder) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void recorderEvent(int riid, int event) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void releaseRecorder(int riid) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void playerSessionId(int piid, int sessionId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void trackAudioStart(int pid, int uid, int sessionId, int streamType) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void trackAudioStop(int pid, int uid, int sessionId, int streamType) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, String callingPackage, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void adjustStreamVolume(int streamType, int direction, int flags, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setStreamVolume(int streamType, int index, int flags, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void handleVolumeKey(KeyEvent event, boolean isOnTv, String callingPackage, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isStreamMute(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void forceRemoteSubmixFullVolume(boolean startForcing, IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isMasterMute() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setMasterMute(boolean mute, int flags, String callingPackage, int userId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getStreamVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getStreamMinVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getStreamMaxVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void setVolumeIndexForAttributes(AudioAttributes aa, int index, int flags, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getMaxVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getMinVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getLastAudibleStreamVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setSupportedSystemUsages(int[] systemUsages) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int[] getSupportedSystemUsages() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public boolean isMicrophoneMuted() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setMicrophoneMute(boolean on, String callingPackage, int userId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setMicrophoneMuteFromSwitch(boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setRingerModeExternal(int ringerMode, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setRingerModeInternal(int ringerMode, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getRingerModeExternal() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getRingerModeInternal() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean isValidRingerMode(int ringerMode) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setVibrateSetting(int vibrateType, int vibrateSetting) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getVibrateSetting(int vibrateType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean shouldVibrate(int vibrateType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setMode(int mode, IBinder cb, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getMode() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void playSoundEffect(int effectType) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void playSoundEffectVolume(int effectType, float volume) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean loadSoundEffects() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void unloadSoundEffects() throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void reloadAudioSettings() throws RemoteException {
        }

        @Override // android.media.IAudioService
        public Map getSurroundFormats() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public List getReportedSurroundFormats() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public boolean setSurroundFormatEnabled(int audioFormat, boolean enabled) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isSurroundFormatEnabled(int audioFormat) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean setEncodedSurroundMode(int mode) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int getEncodedSurroundMode(int targetSdkVersion) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void avrcpSupportsAbsoluteVolume(String address, boolean support) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setSpeakerphoneOn(IBinder cb, boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isSpeakerphoneOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setBluetoothScoOn(boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isBluetoothScoOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setBluetoothA2dpOn(boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isBluetoothA2dpOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int requestAudioFocus(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int abandonAudioFocus(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void unregisterAudioFocusClient(String clientId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getCurrentAudioFocus() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void startBluetoothSco(IBinder cb, int targetSdkVersion) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void startBluetoothScoVirtualCall(IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void stopBluetoothSco(IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void forceVolumeControlStream(int streamType, IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setRingtonePlayer(IRingtonePlayer player) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public IRingtonePlayer getRingtonePlayer() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public int getUiSoundsStreamType() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setWiredDeviceConnectionState(int type, int state, String address, String name, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice device) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void handleBluetoothA2dpActiveDeviceChange(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver observer) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public boolean isCameraSoundForced() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setVolumeController(IVolumeController controller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isStreamAffectedByRingerMode(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isStreamAffectedByMute(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void disableSafeMediaVolume(String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int setHdmiSystemAudioSupported(boolean on) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean isHdmiSystemAudioSupported() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public String registerAudioPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, IMediaProjection projection) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void unregisterAudioPolicyAsync(IAudioPolicyCallback pcb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterAudioPolicy(IAudioPolicyCallback pcb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int addMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int removeMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int setFocusPropertiesForPolicy(int duckingBehavior, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setVolumePolicy(VolumePolicy policy) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean hasRegisteredDynamicPolicy() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void registerRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void registerPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void disableRingtoneSync(int userId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getFocusRampTimeMs(int focusGain, AudioAttributes attr) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int dispatchFocusChange(AudioFocusInfo afi, int focusChange, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice device, int state, boolean suppressNoisyIntent, int musicDevice) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setFocusRequestResultFromExtPolicy(AudioFocusInfo afi, int requestResult, IAudioPolicyCallback pcb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void registerAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isAudioServerRunning() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int setUidDeviceAffinity(IAudioPolicyCallback pcb, int uid, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int removeUidDeviceAffinity(IAudioPolicyCallback pcb, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int setUserIdDeviceAffinity(IAudioPolicyCallback pcb, int userId, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int removeUserIdDeviceAffinity(IAudioPolicyCallback pcb, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean hasHapticChannels(Uri uri) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isCallScreeningModeSupported() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int setPreferredDevicesForStrategy(int strategy, List<AudioDeviceAttributes> device) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int removePreferredDevicesForStrategy(int strategy) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public List<AudioDeviceAttributes> getPreferredDevicesForStrategy(int strategy) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public List<AudioDeviceAttributes> getDevicesForAttributes(AudioAttributes attributes) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public int setAllowedCapturePolicy(int capturePolicy) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getAllowedCapturePolicy() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void registerStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setRttEnabled(boolean rttEnabled) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setDeviceVolumeBehavior(AudioDeviceAttributes device, int deviceVolumeBehavior, String pkgName) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getDeviceVolumeBehavior(AudioDeviceAttributes device) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setMultiAudioFocusEnabled(boolean enabled) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void cacheParameters(String keyValuePairs) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int setPreferredDevicesForCapturePreset(int capturePreset, List<AudioDeviceAttributes> devices) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int clearPreferredDevicesForCapturePreset(int capturePreset) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public List<AudioDeviceAttributes> getPreferredDevicesForCapturePreset(int capturePreset) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void registerCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void adjustStreamVolumeForUid(int streamType, int direction, int flags, String packageName, int uid, int pid, UserHandle userHandle, int targetSdkVersion) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void adjustSuggestedStreamVolumeForUid(int streamType, int direction, int flags, String packageName, int uid, int pid, UserHandle userHandle, int targetSdkVersion) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setStreamVolumeForUid(int streamType, int direction, int flags, String packageName, int uid, int pid, UserHandle userHandle, int targetSdkVersion) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isMusicActive(boolean remotely) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int getDevicesForStream(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int[] getAvailableCommunicationDeviceIds() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public boolean setCommunicationDevice(IBinder cb, int portId) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int getCommunicationDevice() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void registerCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean areNavigationRepeatSoundEffectsEnabled() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setNavigationRepeatSoundEffectsEnabled(boolean enabled) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isHomeSoundEffectEnabled() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setHomeSoundEffectEnabled(boolean enabled) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean setAdditionalOutputDeviceDelay(AudioDeviceAttributes device, long delayMillis) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public long getAdditionalOutputDeviceDelay(AudioDeviceAttributes device) throws RemoteException {
            return 0L;
        }

        @Override // android.media.IAudioService
        public long getMaxAdditionalOutputDeviceDelay(AudioDeviceAttributes device) throws RemoteException {
            return 0L;
        }

        @Override // android.media.IAudioService
        public int requestAudioFocusForTest(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int uid, int sdk) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int abandonAudioFocusForTest(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public long getFadeOutDurationOnFocusLossMillis(AudioAttributes aa) throws RemoteException {
            return 0L;
        }

        @Override // android.media.IAudioService
        public void registerModeDispatcher(IAudioModeDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterModeDispatcher(IAudioModeDispatcher dispatcher) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setEnableDoVibrateTime(String pkgName, long time) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public long getEnableDoVibrateTime(String pkgName) throws RemoteException {
            return 0L;
        }

        @Override // android.media.IAudioService
        public void setNtParameters(String keyValuePairs, IBinder cb) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAudioService {
        public static final String DESCRIPTOR = "android.media.IAudioService";
        static final int TRANSACTION_abandonAudioFocus = 63;
        static final int TRANSACTION_abandonAudioFocusForTest = 151;
        static final int TRANSACTION_addMixForPolicy = 88;
        static final int TRANSACTION_adjustStreamVolume = 12;
        static final int TRANSACTION_adjustStreamVolumeForUid = 133;
        static final int TRANSACTION_adjustSuggestedStreamVolume = 11;
        static final int TRANSACTION_adjustSuggestedStreamVolumeForUid = 134;
        static final int TRANSACTION_areNavigationRepeatSoundEffectsEnabled = 143;
        static final int TRANSACTION_avrcpSupportsAbsoluteVolume = 55;
        static final int TRANSACTION_cacheParameters = 127;
        static final int TRANSACTION_clearPreferredDevicesForCapturePreset = 129;
        static final int TRANSACTION_disableRingtoneSync = 99;
        static final int TRANSACTION_disableSafeMediaVolume = 82;
        static final int TRANSACTION_dispatchFocusChange = 101;
        static final int TRANSACTION_forceRemoteSubmixFullVolume = 16;
        static final int TRANSACTION_forceVolumeControlStream = 69;
        static final int TRANSACTION_getActivePlaybackConfigurations = 98;
        static final int TRANSACTION_getActiveRecordingConfigurations = 95;
        static final int TRANSACTION_getAdditionalOutputDeviceDelay = 148;
        static final int TRANSACTION_getAllowedCapturePolicy = 120;
        static final int TRANSACTION_getAudioProductStrategies = 30;
        static final int TRANSACTION_getAudioVolumeGroups = 22;
        static final int TRANSACTION_getAvailableCommunicationDeviceIds = 138;
        static final int TRANSACTION_getCommunicationDevice = 140;
        static final int TRANSACTION_getCurrentAudioFocus = 65;
        static final int TRANSACTION_getDeviceVolumeBehavior = 125;
        static final int TRANSACTION_getDevicesForAttributes = 118;
        static final int TRANSACTION_getDevicesForStream = 137;
        static final int TRANSACTION_getEnableDoVibrateTime = 156;
        static final int TRANSACTION_getEncodedSurroundMode = 54;
        static final int TRANSACTION_getFadeOutDurationOnFocusLossMillis = 152;
        static final int TRANSACTION_getFocusRampTimeMs = 100;
        static final int TRANSACTION_getLastAudibleStreamVolume = 27;
        static final int TRANSACTION_getMaxAdditionalOutputDeviceDelay = 149;
        static final int TRANSACTION_getMaxVolumeIndexForAttributes = 25;
        static final int TRANSACTION_getMinVolumeIndexForAttributes = 26;
        static final int TRANSACTION_getMode = 43;
        static final int TRANSACTION_getPreferredDevicesForCapturePreset = 130;
        static final int TRANSACTION_getPreferredDevicesForStrategy = 117;
        static final int TRANSACTION_getReportedSurroundFormats = 50;
        static final int TRANSACTION_getRingerModeExternal = 36;
        static final int TRANSACTION_getRingerModeInternal = 37;
        static final int TRANSACTION_getRingtonePlayer = 71;
        static final int TRANSACTION_getStreamMaxVolume = 21;
        static final int TRANSACTION_getStreamMinVolume = 20;
        static final int TRANSACTION_getStreamVolume = 19;
        static final int TRANSACTION_getSupportedSystemUsages = 29;
        static final int TRANSACTION_getSurroundFormats = 49;
        static final int TRANSACTION_getUiSoundsStreamType = 72;
        static final int TRANSACTION_getVibrateSetting = 40;
        static final int TRANSACTION_getVolumeIndexForAttributes = 24;
        static final int TRANSACTION_handleBluetoothA2dpActiveDeviceChange = 75;
        static final int TRANSACTION_handleBluetoothA2dpDeviceConfigChange = 74;
        static final int TRANSACTION_handleVolumeKey = 14;
        static final int TRANSACTION_hasHapticChannels = 113;
        static final int TRANSACTION_hasRegisteredDynamicPolicy = 92;
        static final int TRANSACTION_isAudioServerRunning = 108;
        static final int TRANSACTION_isBluetoothA2dpOn = 61;
        static final int TRANSACTION_isBluetoothScoOn = 59;
        static final int TRANSACTION_isCallScreeningModeSupported = 114;
        static final int TRANSACTION_isCameraSoundForced = 77;
        static final int TRANSACTION_isHdmiSystemAudioSupported = 84;
        static final int TRANSACTION_isHomeSoundEffectEnabled = 145;
        static final int TRANSACTION_isMasterMute = 17;
        static final int TRANSACTION_isMicrophoneMuted = 31;
        static final int TRANSACTION_isMusicActive = 136;
        static final int TRANSACTION_isSpeakerphoneOn = 57;
        static final int TRANSACTION_isStreamAffectedByMute = 81;
        static final int TRANSACTION_isStreamAffectedByRingerMode = 80;
        static final int TRANSACTION_isStreamMute = 15;
        static final int TRANSACTION_isSurroundFormatEnabled = 52;
        static final int TRANSACTION_isValidRingerMode = 38;
        static final int TRANSACTION_loadSoundEffects = 46;
        static final int TRANSACTION_notifyVolumeControllerVisible = 79;
        static final int TRANSACTION_playSoundEffect = 44;
        static final int TRANSACTION_playSoundEffectVolume = 45;
        static final int TRANSACTION_playerAttributes = 2;
        static final int TRANSACTION_playerEvent = 3;
        static final int TRANSACTION_playerHasOpPlayAudio = 102;
        static final int TRANSACTION_playerSessionId = 8;
        static final int TRANSACTION_recorderEvent = 6;
        static final int TRANSACTION_registerAudioPolicy = 85;
        static final int TRANSACTION_registerAudioServerStateDispatcher = 106;
        static final int TRANSACTION_registerCapturePresetDevicesRoleDispatcher = 131;
        static final int TRANSACTION_registerCommunicationDeviceDispatcher = 141;
        static final int TRANSACTION_registerModeDispatcher = 153;
        static final int TRANSACTION_registerPlaybackCallback = 96;
        static final int TRANSACTION_registerRecordingCallback = 93;
        static final int TRANSACTION_registerStrategyPreferredDevicesDispatcher = 121;
        static final int TRANSACTION_releasePlayer = 4;
        static final int TRANSACTION_releaseRecorder = 7;
        static final int TRANSACTION_reloadAudioSettings = 48;
        static final int TRANSACTION_removeMixForPolicy = 89;
        static final int TRANSACTION_removePreferredDevicesForStrategy = 116;
        static final int TRANSACTION_removeUidDeviceAffinity = 110;
        static final int TRANSACTION_removeUserIdDeviceAffinity = 112;
        static final int TRANSACTION_requestAudioFocus = 62;
        static final int TRANSACTION_requestAudioFocusForTest = 150;
        static final int TRANSACTION_setAdditionalOutputDeviceDelay = 147;
        static final int TRANSACTION_setAllowedCapturePolicy = 119;
        static final int TRANSACTION_setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent = 104;
        static final int TRANSACTION_setBluetoothA2dpOn = 60;
        static final int TRANSACTION_setBluetoothHearingAidDeviceConnectionState = 103;
        static final int TRANSACTION_setBluetoothScoOn = 58;
        static final int TRANSACTION_setCommunicationDevice = 139;
        static final int TRANSACTION_setDeviceVolumeBehavior = 124;
        static final int TRANSACTION_setEnableDoVibrateTime = 155;
        static final int TRANSACTION_setEncodedSurroundMode = 53;
        static final int TRANSACTION_setFocusPropertiesForPolicy = 90;
        static final int TRANSACTION_setFocusRequestResultFromExtPolicy = 105;
        static final int TRANSACTION_setHdmiSystemAudioSupported = 83;
        static final int TRANSACTION_setHomeSoundEffectEnabled = 146;
        static final int TRANSACTION_setMasterMute = 18;
        static final int TRANSACTION_setMicrophoneMute = 32;
        static final int TRANSACTION_setMicrophoneMuteFromSwitch = 33;
        static final int TRANSACTION_setMode = 42;
        static final int TRANSACTION_setMultiAudioFocusEnabled = 126;
        static final int TRANSACTION_setNavigationRepeatSoundEffectsEnabled = 144;
        static final int TRANSACTION_setNtParameters = 157;
        static final int TRANSACTION_setPreferredDevicesForCapturePreset = 128;
        static final int TRANSACTION_setPreferredDevicesForStrategy = 115;
        static final int TRANSACTION_setRingerModeExternal = 34;
        static final int TRANSACTION_setRingerModeInternal = 35;
        static final int TRANSACTION_setRingtonePlayer = 70;
        static final int TRANSACTION_setRttEnabled = 123;
        static final int TRANSACTION_setSpeakerphoneOn = 56;
        static final int TRANSACTION_setStreamVolume = 13;
        static final int TRANSACTION_setStreamVolumeForUid = 135;
        static final int TRANSACTION_setSupportedSystemUsages = 28;
        static final int TRANSACTION_setSurroundFormatEnabled = 51;
        static final int TRANSACTION_setUidDeviceAffinity = 109;
        static final int TRANSACTION_setUserIdDeviceAffinity = 111;
        static final int TRANSACTION_setVibrateSetting = 39;
        static final int TRANSACTION_setVolumeController = 78;
        static final int TRANSACTION_setVolumeIndexForAttributes = 23;
        static final int TRANSACTION_setVolumePolicy = 91;
        static final int TRANSACTION_setWiredDeviceConnectionState = 73;
        static final int TRANSACTION_shouldVibrate = 41;
        static final int TRANSACTION_startBluetoothSco = 66;
        static final int TRANSACTION_startBluetoothScoVirtualCall = 67;
        static final int TRANSACTION_startWatchingRoutes = 76;
        static final int TRANSACTION_stopBluetoothSco = 68;
        static final int TRANSACTION_trackAudioStart = 9;
        static final int TRANSACTION_trackAudioStop = 10;
        static final int TRANSACTION_trackPlayer = 1;
        static final int TRANSACTION_trackRecorder = 5;
        static final int TRANSACTION_unloadSoundEffects = 47;
        static final int TRANSACTION_unregisterAudioFocusClient = 64;
        static final int TRANSACTION_unregisterAudioPolicy = 87;
        static final int TRANSACTION_unregisterAudioPolicyAsync = 86;
        static final int TRANSACTION_unregisterAudioServerStateDispatcher = 107;
        static final int TRANSACTION_unregisterCapturePresetDevicesRoleDispatcher = 132;
        static final int TRANSACTION_unregisterCommunicationDeviceDispatcher = 142;
        static final int TRANSACTION_unregisterModeDispatcher = 154;
        static final int TRANSACTION_unregisterPlaybackCallback = 97;
        static final int TRANSACTION_unregisterRecordingCallback = 94;
        static final int TRANSACTION_unregisterStrategyPreferredDevicesDispatcher = 122;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAudioService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAudioService)) {
                return (IAudioService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "trackPlayer";
                case 2:
                    return "playerAttributes";
                case 3:
                    return "playerEvent";
                case 4:
                    return "releasePlayer";
                case 5:
                    return "trackRecorder";
                case 6:
                    return "recorderEvent";
                case 7:
                    return "releaseRecorder";
                case 8:
                    return "playerSessionId";
                case 9:
                    return "trackAudioStart";
                case 10:
                    return "trackAudioStop";
                case 11:
                    return "adjustSuggestedStreamVolume";
                case 12:
                    return "adjustStreamVolume";
                case 13:
                    return "setStreamVolume";
                case 14:
                    return "handleVolumeKey";
                case 15:
                    return "isStreamMute";
                case 16:
                    return "forceRemoteSubmixFullVolume";
                case 17:
                    return "isMasterMute";
                case 18:
                    return "setMasterMute";
                case 19:
                    return "getStreamVolume";
                case 20:
                    return "getStreamMinVolume";
                case 21:
                    return "getStreamMaxVolume";
                case 22:
                    return "getAudioVolumeGroups";
                case 23:
                    return "setVolumeIndexForAttributes";
                case 24:
                    return "getVolumeIndexForAttributes";
                case 25:
                    return "getMaxVolumeIndexForAttributes";
                case 26:
                    return "getMinVolumeIndexForAttributes";
                case 27:
                    return "getLastAudibleStreamVolume";
                case 28:
                    return "setSupportedSystemUsages";
                case 29:
                    return "getSupportedSystemUsages";
                case 30:
                    return "getAudioProductStrategies";
                case 31:
                    return "isMicrophoneMuted";
                case 32:
                    return "setMicrophoneMute";
                case 33:
                    return "setMicrophoneMuteFromSwitch";
                case 34:
                    return "setRingerModeExternal";
                case 35:
                    return "setRingerModeInternal";
                case 36:
                    return "getRingerModeExternal";
                case 37:
                    return "getRingerModeInternal";
                case 38:
                    return "isValidRingerMode";
                case 39:
                    return "setVibrateSetting";
                case 40:
                    return "getVibrateSetting";
                case 41:
                    return "shouldVibrate";
                case 42:
                    return "setMode";
                case 43:
                    return "getMode";
                case 44:
                    return "playSoundEffect";
                case 45:
                    return "playSoundEffectVolume";
                case 46:
                    return "loadSoundEffects";
                case 47:
                    return "unloadSoundEffects";
                case 48:
                    return "reloadAudioSettings";
                case 49:
                    return "getSurroundFormats";
                case 50:
                    return "getReportedSurroundFormats";
                case 51:
                    return "setSurroundFormatEnabled";
                case 52:
                    return "isSurroundFormatEnabled";
                case 53:
                    return "setEncodedSurroundMode";
                case 54:
                    return "getEncodedSurroundMode";
                case 55:
                    return "avrcpSupportsAbsoluteVolume";
                case 56:
                    return "setSpeakerphoneOn";
                case 57:
                    return "isSpeakerphoneOn";
                case 58:
                    return "setBluetoothScoOn";
                case 59:
                    return "isBluetoothScoOn";
                case 60:
                    return "setBluetoothA2dpOn";
                case 61:
                    return "isBluetoothA2dpOn";
                case 62:
                    return "requestAudioFocus";
                case 63:
                    return "abandonAudioFocus";
                case 64:
                    return "unregisterAudioFocusClient";
                case 65:
                    return "getCurrentAudioFocus";
                case 66:
                    return "startBluetoothSco";
                case 67:
                    return "startBluetoothScoVirtualCall";
                case 68:
                    return "stopBluetoothSco";
                case 69:
                    return "forceVolumeControlStream";
                case 70:
                    return "setRingtonePlayer";
                case 71:
                    return "getRingtonePlayer";
                case 72:
                    return "getUiSoundsStreamType";
                case 73:
                    return "setWiredDeviceConnectionState";
                case 74:
                    return "handleBluetoothA2dpDeviceConfigChange";
                case 75:
                    return "handleBluetoothA2dpActiveDeviceChange";
                case 76:
                    return "startWatchingRoutes";
                case 77:
                    return "isCameraSoundForced";
                case 78:
                    return "setVolumeController";
                case 79:
                    return "notifyVolumeControllerVisible";
                case 80:
                    return "isStreamAffectedByRingerMode";
                case 81:
                    return "isStreamAffectedByMute";
                case 82:
                    return "disableSafeMediaVolume";
                case 83:
                    return "setHdmiSystemAudioSupported";
                case 84:
                    return "isHdmiSystemAudioSupported";
                case 85:
                    return "registerAudioPolicy";
                case 86:
                    return "unregisterAudioPolicyAsync";
                case 87:
                    return "unregisterAudioPolicy";
                case 88:
                    return "addMixForPolicy";
                case 89:
                    return "removeMixForPolicy";
                case 90:
                    return "setFocusPropertiesForPolicy";
                case 91:
                    return "setVolumePolicy";
                case 92:
                    return "hasRegisteredDynamicPolicy";
                case 93:
                    return "registerRecordingCallback";
                case 94:
                    return "unregisterRecordingCallback";
                case 95:
                    return "getActiveRecordingConfigurations";
                case 96:
                    return "registerPlaybackCallback";
                case 97:
                    return "unregisterPlaybackCallback";
                case 98:
                    return "getActivePlaybackConfigurations";
                case 99:
                    return "disableRingtoneSync";
                case 100:
                    return "getFocusRampTimeMs";
                case 101:
                    return "dispatchFocusChange";
                case 102:
                    return "playerHasOpPlayAudio";
                case 103:
                    return "setBluetoothHearingAidDeviceConnectionState";
                case 104:
                    return "setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent";
                case 105:
                    return "setFocusRequestResultFromExtPolicy";
                case 106:
                    return "registerAudioServerStateDispatcher";
                case 107:
                    return "unregisterAudioServerStateDispatcher";
                case 108:
                    return "isAudioServerRunning";
                case 109:
                    return "setUidDeviceAffinity";
                case 110:
                    return "removeUidDeviceAffinity";
                case 111:
                    return "setUserIdDeviceAffinity";
                case 112:
                    return "removeUserIdDeviceAffinity";
                case 113:
                    return "hasHapticChannels";
                case 114:
                    return "isCallScreeningModeSupported";
                case 115:
                    return "setPreferredDevicesForStrategy";
                case 116:
                    return "removePreferredDevicesForStrategy";
                case 117:
                    return "getPreferredDevicesForStrategy";
                case 118:
                    return "getDevicesForAttributes";
                case 119:
                    return "setAllowedCapturePolicy";
                case 120:
                    return "getAllowedCapturePolicy";
                case 121:
                    return "registerStrategyPreferredDevicesDispatcher";
                case 122:
                    return "unregisterStrategyPreferredDevicesDispatcher";
                case 123:
                    return "setRttEnabled";
                case 124:
                    return "setDeviceVolumeBehavior";
                case 125:
                    return "getDeviceVolumeBehavior";
                case 126:
                    return "setMultiAudioFocusEnabled";
                case 127:
                    return "cacheParameters";
                case 128:
                    return "setPreferredDevicesForCapturePreset";
                case 129:
                    return "clearPreferredDevicesForCapturePreset";
                case 130:
                    return "getPreferredDevicesForCapturePreset";
                case 131:
                    return "registerCapturePresetDevicesRoleDispatcher";
                case 132:
                    return "unregisterCapturePresetDevicesRoleDispatcher";
                case 133:
                    return "adjustStreamVolumeForUid";
                case 134:
                    return "adjustSuggestedStreamVolumeForUid";
                case 135:
                    return "setStreamVolumeForUid";
                case 136:
                    return "isMusicActive";
                case 137:
                    return "getDevicesForStream";
                case 138:
                    return "getAvailableCommunicationDeviceIds";
                case 139:
                    return "setCommunicationDevice";
                case 140:
                    return "getCommunicationDevice";
                case 141:
                    return "registerCommunicationDeviceDispatcher";
                case 142:
                    return "unregisterCommunicationDeviceDispatcher";
                case 143:
                    return "areNavigationRepeatSoundEffectsEnabled";
                case 144:
                    return "setNavigationRepeatSoundEffectsEnabled";
                case 145:
                    return "isHomeSoundEffectEnabled";
                case 146:
                    return "setHomeSoundEffectEnabled";
                case 147:
                    return "setAdditionalOutputDeviceDelay";
                case 148:
                    return "getAdditionalOutputDeviceDelay";
                case 149:
                    return "getMaxAdditionalOutputDeviceDelay";
                case 150:
                    return "requestAudioFocusForTest";
                case 151:
                    return "abandonAudioFocusForTest";
                case 152:
                    return "getFadeOutDurationOnFocusLossMillis";
                case 153:
                    return "registerModeDispatcher";
                case 154:
                    return "unregisterModeDispatcher";
                case 155:
                    return "setEnableDoVibrateTime";
                case 156:
                    return "getEnableDoVibrateTime";
                case 157:
                    return "setNtParameters";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PlayerBase.PlayerIdCard _arg0;
            AudioAttributes _arg1;
            KeyEvent _arg02;
            AudioAttributes _arg03;
            AudioAttributes _arg04;
            AudioAttributes _arg05;
            AudioAttributes _arg06;
            AudioAttributes _arg07;
            AudioAttributes _arg2;
            BluetoothDevice _arg08;
            BluetoothDevice _arg09;
            boolean _arg3;
            AudioPolicyConfig _arg010;
            boolean _arg22;
            boolean _arg32;
            boolean _arg4;
            boolean _arg5;
            AudioPolicyConfig _arg011;
            AudioPolicyConfig _arg012;
            VolumePolicy _arg013;
            AudioAttributes _arg12;
            AudioFocusInfo _arg014;
            BluetoothDevice _arg015;
            BluetoothDevice _arg016;
            boolean _arg33;
            AudioFocusInfo _arg017;
            Uri _arg018;
            AudioAttributes _arg019;
            AudioDeviceAttributes _arg020;
            AudioDeviceAttributes _arg021;
            UserHandle _arg6;
            UserHandle _arg62;
            UserHandle _arg63;
            AudioDeviceAttributes _arg022;
            AudioDeviceAttributes _arg023;
            AudioDeviceAttributes _arg024;
            AudioAttributes _arg025;
            AudioAttributes _arg23;
            AudioAttributes _arg026;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    boolean _arg027 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = PlayerBase.PlayerIdCard.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            int _result = trackPlayer(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg028 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg1 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            playerAttributes(_arg028, _arg1);
                            return true;
                        case 3:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg029 = data.readInt();
                            int _arg13 = data.readInt();
                            int _arg24 = data.readInt();
                            playerEvent(_arg029, _arg13, _arg24);
                            return true;
                        case 4:
                            data.enforceInterface(DESCRIPTOR);
                            releasePlayer(data.readInt());
                            return true;
                        case 5:
                            data.enforceInterface(DESCRIPTOR);
                            int _result2 = trackRecorder(data.readStrongBinder());
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 6:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg030 = data.readInt();
                            int _arg14 = data.readInt();
                            recorderEvent(_arg030, _arg14);
                            return true;
                        case 7:
                            data.enforceInterface(DESCRIPTOR);
                            releaseRecorder(data.readInt());
                            return true;
                        case 8:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg031 = data.readInt();
                            int _arg15 = data.readInt();
                            playerSessionId(_arg031, _arg15);
                            return true;
                        case 9:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg032 = data.readInt();
                            int _arg16 = data.readInt();
                            int _arg25 = data.readInt();
                            int _arg34 = data.readInt();
                            trackAudioStart(_arg032, _arg16, _arg25, _arg34);
                            return true;
                        case 10:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg033 = data.readInt();
                            int _arg17 = data.readInt();
                            int _arg26 = data.readInt();
                            int _arg35 = data.readInt();
                            trackAudioStop(_arg033, _arg17, _arg26, _arg35);
                            return true;
                        case 11:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg034 = data.readInt();
                            int _arg18 = data.readInt();
                            int _arg27 = data.readInt();
                            String _arg36 = data.readString();
                            String _arg42 = data.readString();
                            adjustSuggestedStreamVolume(_arg034, _arg18, _arg27, _arg36, _arg42);
                            return true;
                        case 12:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg035 = data.readInt();
                            int _arg19 = data.readInt();
                            int _arg28 = data.readInt();
                            String _arg37 = data.readString();
                            adjustStreamVolume(_arg035, _arg19, _arg28, _arg37);
                            reply.writeNoException();
                            return true;
                        case 13:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg036 = data.readInt();
                            int _arg110 = data.readInt();
                            int _arg29 = data.readInt();
                            String _arg38 = data.readString();
                            setStreamVolume(_arg036, _arg110, _arg29, _arg38);
                            reply.writeNoException();
                            return true;
                        case 14:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = KeyEvent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            String _arg210 = data.readString();
                            String _arg39 = data.readString();
                            handleVolumeKey(_arg02, _arg027, _arg210, _arg39);
                            return true;
                        case 15:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isStreamMute = isStreamMute(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isStreamMute ? 1 : 0);
                            return true;
                        case 16:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            IBinder _arg111 = data.readStrongBinder();
                            forceRemoteSubmixFullVolume(_arg027, _arg111);
                            reply.writeNoException();
                            return true;
                        case 17:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isMasterMute = isMasterMute();
                            reply.writeNoException();
                            reply.writeInt(isMasterMute ? 1 : 0);
                            return true;
                        case 18:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            int _arg112 = data.readInt();
                            String _arg211 = data.readString();
                            int _arg310 = data.readInt();
                            setMasterMute(_arg027, _arg112, _arg211, _arg310);
                            reply.writeNoException();
                            return true;
                        case 19:
                            data.enforceInterface(DESCRIPTOR);
                            int _result3 = getStreamVolume(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 20:
                            data.enforceInterface(DESCRIPTOR);
                            int _result4 = getStreamMinVolume(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 21:
                            data.enforceInterface(DESCRIPTOR);
                            int _result5 = getStreamMaxVolume(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 22:
                            data.enforceInterface(DESCRIPTOR);
                            List<AudioVolumeGroup> _result6 = getAudioVolumeGroups();
                            reply.writeNoException();
                            reply.writeTypedList(_result6);
                            return true;
                        case 23:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            int _arg113 = data.readInt();
                            int _arg212 = data.readInt();
                            String _arg311 = data.readString();
                            setVolumeIndexForAttributes(_arg03, _arg113, _arg212, _arg311);
                            reply.writeNoException();
                            return true;
                        case 24:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            int _result7 = getVolumeIndexForAttributes(_arg04);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 25:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            int _result8 = getMaxVolumeIndexForAttributes(_arg05);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 26:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            int _result9 = getMinVolumeIndexForAttributes(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 27:
                            data.enforceInterface(DESCRIPTOR);
                            int _result10 = getLastAudibleStreamVolume(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 28:
                            data.enforceInterface(DESCRIPTOR);
                            setSupportedSystemUsages(data.createIntArray());
                            reply.writeNoException();
                            return true;
                        case 29:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _result11 = getSupportedSystemUsages();
                            reply.writeNoException();
                            reply.writeIntArray(_result11);
                            return true;
                        case 30:
                            data.enforceInterface(DESCRIPTOR);
                            List<AudioProductStrategy> _result12 = getAudioProductStrategies();
                            reply.writeNoException();
                            reply.writeTypedList(_result12);
                            return true;
                        case 31:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isMicrophoneMuted = isMicrophoneMuted();
                            reply.writeNoException();
                            reply.writeInt(isMicrophoneMuted ? 1 : 0);
                            return true;
                        case 32:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            String _arg114 = data.readString();
                            int _arg213 = data.readInt();
                            setMicrophoneMute(_arg027, _arg114, _arg213);
                            reply.writeNoException();
                            return true;
                        case 33:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setMicrophoneMuteFromSwitch(_arg027);
                            return true;
                        case 34:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg037 = data.readInt();
                            String _arg115 = data.readString();
                            setRingerModeExternal(_arg037, _arg115);
                            reply.writeNoException();
                            return true;
                        case 35:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg038 = data.readInt();
                            String _arg116 = data.readString();
                            setRingerModeInternal(_arg038, _arg116);
                            reply.writeNoException();
                            return true;
                        case 36:
                            data.enforceInterface(DESCRIPTOR);
                            int _result13 = getRingerModeExternal();
                            reply.writeNoException();
                            reply.writeInt(_result13);
                            return true;
                        case 37:
                            data.enforceInterface(DESCRIPTOR);
                            int _result14 = getRingerModeInternal();
                            reply.writeNoException();
                            reply.writeInt(_result14);
                            return true;
                        case 38:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isValidRingerMode = isValidRingerMode(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isValidRingerMode ? 1 : 0);
                            return true;
                        case 39:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg039 = data.readInt();
                            int _arg117 = data.readInt();
                            setVibrateSetting(_arg039, _arg117);
                            reply.writeNoException();
                            return true;
                        case 40:
                            data.enforceInterface(DESCRIPTOR);
                            int _result15 = getVibrateSetting(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 41:
                            data.enforceInterface(DESCRIPTOR);
                            boolean shouldVibrate = shouldVibrate(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(shouldVibrate ? 1 : 0);
                            return true;
                        case 42:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg040 = data.readInt();
                            IBinder _arg118 = data.readStrongBinder();
                            String _arg214 = data.readString();
                            setMode(_arg040, _arg118, _arg214);
                            reply.writeNoException();
                            return true;
                        case 43:
                            data.enforceInterface(DESCRIPTOR);
                            int _result16 = getMode();
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 44:
                            data.enforceInterface(DESCRIPTOR);
                            playSoundEffect(data.readInt());
                            return true;
                        case 45:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg041 = data.readInt();
                            float _arg119 = data.readFloat();
                            playSoundEffectVolume(_arg041, _arg119);
                            return true;
                        case 46:
                            data.enforceInterface(DESCRIPTOR);
                            boolean loadSoundEffects = loadSoundEffects();
                            reply.writeNoException();
                            reply.writeInt(loadSoundEffects ? 1 : 0);
                            return true;
                        case 47:
                            data.enforceInterface(DESCRIPTOR);
                            unloadSoundEffects();
                            return true;
                        case 48:
                            data.enforceInterface(DESCRIPTOR);
                            reloadAudioSettings();
                            return true;
                        case 49:
                            data.enforceInterface(DESCRIPTOR);
                            Map _result17 = getSurroundFormats();
                            reply.writeNoException();
                            reply.writeMap(_result17);
                            return true;
                        case 50:
                            data.enforceInterface(DESCRIPTOR);
                            List _result18 = getReportedSurroundFormats();
                            reply.writeNoException();
                            reply.writeList(_result18);
                            return true;
                        case 51:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg042 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            boolean surroundFormatEnabled = setSurroundFormatEnabled(_arg042, _arg027);
                            reply.writeNoException();
                            reply.writeInt(surroundFormatEnabled ? 1 : 0);
                            return true;
                        case 52:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isSurroundFormatEnabled = isSurroundFormatEnabled(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isSurroundFormatEnabled ? 1 : 0);
                            return true;
                        case 53:
                            data.enforceInterface(DESCRIPTOR);
                            boolean encodedSurroundMode = setEncodedSurroundMode(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(encodedSurroundMode ? 1 : 0);
                            return true;
                        case 54:
                            data.enforceInterface(DESCRIPTOR);
                            int _result19 = getEncodedSurroundMode(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 55:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg043 = data.readString();
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            avrcpSupportsAbsoluteVolume(_arg043, _arg027);
                            return true;
                        case 56:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg044 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setSpeakerphoneOn(_arg044, _arg027);
                            reply.writeNoException();
                            return true;
                        case 57:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isSpeakerphoneOn = isSpeakerphoneOn();
                            reply.writeNoException();
                            reply.writeInt(isSpeakerphoneOn ? 1 : 0);
                            return true;
                        case 58:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setBluetoothScoOn(_arg027);
                            reply.writeNoException();
                            return true;
                        case 59:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isBluetoothScoOn = isBluetoothScoOn();
                            reply.writeNoException();
                            reply.writeInt(isBluetoothScoOn ? 1 : 0);
                            return true;
                        case 60:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setBluetoothA2dpOn(_arg027);
                            reply.writeNoException();
                            return true;
                        case 61:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isBluetoothA2dpOn = isBluetoothA2dpOn();
                            reply.writeNoException();
                            reply.writeInt(isBluetoothA2dpOn ? 1 : 0);
                            return true;
                        case 62:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg07 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg07 = null;
                            }
                            int _arg120 = data.readInt();
                            IBinder _arg215 = data.readStrongBinder();
                            IAudioFocusDispatcher _arg312 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                            String _arg43 = data.readString();
                            String _arg52 = data.readString();
                            int _arg64 = data.readInt();
                            IAudioPolicyCallback _arg7 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _arg8 = data.readInt();
                            int _result20 = requestAudioFocus(_arg07, _arg120, _arg215, _arg312, _arg43, _arg52, _arg64, _arg7, _arg8);
                            reply.writeNoException();
                            reply.writeInt(_result20);
                            return true;
                        case 63:
                            data.enforceInterface(DESCRIPTOR);
                            IAudioFocusDispatcher _arg045 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                            String _arg121 = data.readString();
                            if (data.readInt() != 0) {
                                _arg2 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            String _arg313 = data.readString();
                            int _result21 = abandonAudioFocus(_arg045, _arg121, _arg2, _arg313);
                            reply.writeNoException();
                            reply.writeInt(_result21);
                            return true;
                        case 64:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterAudioFocusClient(data.readString());
                            reply.writeNoException();
                            return true;
                        case 65:
                            data.enforceInterface(DESCRIPTOR);
                            int _result22 = getCurrentAudioFocus();
                            reply.writeNoException();
                            reply.writeInt(_result22);
                            return true;
                        case 66:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg046 = data.readStrongBinder();
                            int _arg122 = data.readInt();
                            startBluetoothSco(_arg046, _arg122);
                            reply.writeNoException();
                            return true;
                        case 67:
                            data.enforceInterface(DESCRIPTOR);
                            startBluetoothScoVirtualCall(data.readStrongBinder());
                            reply.writeNoException();
                            return true;
                        case 68:
                            data.enforceInterface(DESCRIPTOR);
                            stopBluetoothSco(data.readStrongBinder());
                            reply.writeNoException();
                            return true;
                        case 69:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg047 = data.readInt();
                            IBinder _arg123 = data.readStrongBinder();
                            forceVolumeControlStream(_arg047, _arg123);
                            reply.writeNoException();
                            return true;
                        case 70:
                            data.enforceInterface(DESCRIPTOR);
                            setRingtonePlayer(IRingtonePlayer.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 71:
                            data.enforceInterface(DESCRIPTOR);
                            IRingtonePlayer _result23 = getRingtonePlayer();
                            reply.writeNoException();
                            reply.writeStrongBinder(_result23 != null ? _result23.asBinder() : null);
                            return true;
                        case 72:
                            data.enforceInterface(DESCRIPTOR);
                            int _result24 = getUiSoundsStreamType();
                            reply.writeNoException();
                            reply.writeInt(_result24);
                            return true;
                        case 73:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg048 = data.readInt();
                            int _arg124 = data.readInt();
                            String _arg216 = data.readString();
                            String _arg314 = data.readString();
                            String _arg44 = data.readString();
                            setWiredDeviceConnectionState(_arg048, _arg124, _arg216, _arg314, _arg44);
                            reply.writeNoException();
                            return true;
                        case 74:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg08 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg08 = null;
                            }
                            handleBluetoothA2dpDeviceConfigChange(_arg08);
                            reply.writeNoException();
                            return true;
                        case 75:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg09 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg09 = null;
                            }
                            int _arg125 = data.readInt();
                            int _arg217 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg3 = true;
                            } else {
                                _arg3 = false;
                            }
                            int _arg45 = data.readInt();
                            handleBluetoothA2dpActiveDeviceChange(_arg09, _arg125, _arg217, _arg3, _arg45);
                            reply.writeNoException();
                            return true;
                        case 76:
                            data.enforceInterface(DESCRIPTOR);
                            AudioRoutesInfo _result25 = startWatchingRoutes(IAudioRoutesObserver.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            if (_result25 != null) {
                                reply.writeInt(1);
                                _result25.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 77:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isCameraSoundForced = isCameraSoundForced();
                            reply.writeNoException();
                            reply.writeInt(isCameraSoundForced ? 1 : 0);
                            return true;
                        case 78:
                            data.enforceInterface(DESCRIPTOR);
                            setVolumeController(IVolumeController.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 79:
                            data.enforceInterface(DESCRIPTOR);
                            IVolumeController _arg049 = IVolumeController.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            notifyVolumeControllerVisible(_arg049, _arg027);
                            reply.writeNoException();
                            return true;
                        case 80:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isStreamAffectedByRingerMode = isStreamAffectedByRingerMode(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isStreamAffectedByRingerMode ? 1 : 0);
                            return true;
                        case 81:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isStreamAffectedByMute = isStreamAffectedByMute(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isStreamAffectedByMute ? 1 : 0);
                            return true;
                        case 82:
                            data.enforceInterface(DESCRIPTOR);
                            disableSafeMediaVolume(data.readString());
                            reply.writeNoException();
                            return true;
                        case 83:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            int _result26 = setHdmiSystemAudioSupported(_arg027);
                            reply.writeNoException();
                            reply.writeInt(_result26);
                            return true;
                        case 84:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isHdmiSystemAudioSupported = isHdmiSystemAudioSupported();
                            reply.writeNoException();
                            reply.writeInt(isHdmiSystemAudioSupported ? 1 : 0);
                            return true;
                        case 85:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg010 = AudioPolicyConfig.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg010 = null;
                            }
                            IAudioPolicyCallback _arg126 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            } else {
                                _arg22 = false;
                            }
                            if (data.readInt() != 0) {
                                _arg32 = true;
                            } else {
                                _arg32 = false;
                            }
                            if (data.readInt() != 0) {
                                _arg4 = true;
                            } else {
                                _arg4 = false;
                            }
                            if (data.readInt() != 0) {
                                _arg5 = true;
                            } else {
                                _arg5 = false;
                            }
                            IMediaProjection _arg65 = IMediaProjection.Stub.asInterface(data.readStrongBinder());
                            String _result27 = registerAudioPolicy(_arg010, _arg126, _arg22, _arg32, _arg4, _arg5, _arg65);
                            reply.writeNoException();
                            reply.writeString(_result27);
                            return true;
                        case 86:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterAudioPolicyAsync(IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 87:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterAudioPolicy(IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 88:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg011 = AudioPolicyConfig.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg011 = null;
                            }
                            IAudioPolicyCallback _arg127 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _result28 = addMixForPolicy(_arg011, _arg127);
                            reply.writeNoException();
                            reply.writeInt(_result28);
                            return true;
                        case 89:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg012 = AudioPolicyConfig.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg012 = null;
                            }
                            IAudioPolicyCallback _arg128 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _result29 = removeMixForPolicy(_arg012, _arg128);
                            reply.writeNoException();
                            reply.writeInt(_result29);
                            return true;
                        case 90:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg050 = data.readInt();
                            IAudioPolicyCallback _arg129 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _result30 = setFocusPropertiesForPolicy(_arg050, _arg129);
                            reply.writeNoException();
                            reply.writeInt(_result30);
                            return true;
                        case 91:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg013 = VolumePolicy.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg013 = null;
                            }
                            setVolumePolicy(_arg013);
                            reply.writeNoException();
                            return true;
                        case 92:
                            data.enforceInterface(DESCRIPTOR);
                            boolean hasRegisteredDynamicPolicy = hasRegisteredDynamicPolicy();
                            reply.writeNoException();
                            reply.writeInt(hasRegisteredDynamicPolicy ? 1 : 0);
                            return true;
                        case 93:
                            data.enforceInterface(DESCRIPTOR);
                            registerRecordingCallback(IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 94:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterRecordingCallback(IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 95:
                            data.enforceInterface(DESCRIPTOR);
                            List<AudioRecordingConfiguration> _result31 = getActiveRecordingConfigurations();
                            reply.writeNoException();
                            reply.writeTypedList(_result31);
                            return true;
                        case 96:
                            data.enforceInterface(DESCRIPTOR);
                            registerPlaybackCallback(IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 97:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterPlaybackCallback(IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 98:
                            data.enforceInterface(DESCRIPTOR);
                            List<AudioPlaybackConfiguration> _result32 = getActivePlaybackConfigurations();
                            reply.writeNoException();
                            reply.writeTypedList(_result32);
                            return true;
                        case 99:
                            data.enforceInterface(DESCRIPTOR);
                            disableRingtoneSync(data.readInt());
                            reply.writeNoException();
                            return true;
                        case 100:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg051 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg12 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            int _result33 = getFocusRampTimeMs(_arg051, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result33);
                            return true;
                        case 101:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg014 = AudioFocusInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg014 = null;
                            }
                            int _arg130 = data.readInt();
                            IAudioPolicyCallback _arg218 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _result34 = dispatchFocusChange(_arg014, _arg130, _arg218);
                            reply.writeNoException();
                            reply.writeInt(_result34);
                            return true;
                        case 102:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg052 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            playerHasOpPlayAudio(_arg052, _arg027);
                            return true;
                        case 103:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg015 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg015 = null;
                            }
                            int _arg131 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            int _arg315 = data.readInt();
                            setBluetoothHearingAidDeviceConnectionState(_arg015, _arg131, _arg027, _arg315);
                            reply.writeNoException();
                            return true;
                        case 104:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg016 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg016 = null;
                            }
                            int _arg132 = data.readInt();
                            int _arg219 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg33 = true;
                            } else {
                                _arg33 = false;
                            }
                            int _arg46 = data.readInt();
                            setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(_arg016, _arg132, _arg219, _arg33, _arg46);
                            reply.writeNoException();
                            return true;
                        case 105:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg017 = AudioFocusInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg017 = null;
                            }
                            int _arg133 = data.readInt();
                            IAudioPolicyCallback _arg220 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            setFocusRequestResultFromExtPolicy(_arg017, _arg133, _arg220);
                            return true;
                        case 106:
                            data.enforceInterface(DESCRIPTOR);
                            registerAudioServerStateDispatcher(IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 107:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 108:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isAudioServerRunning = isAudioServerRunning();
                            reply.writeNoException();
                            reply.writeInt(isAudioServerRunning ? 1 : 0);
                            return true;
                        case 109:
                            data.enforceInterface(DESCRIPTOR);
                            IAudioPolicyCallback _arg053 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _arg134 = data.readInt();
                            int[] _arg221 = data.createIntArray();
                            String[] _arg316 = data.createStringArray();
                            int _result35 = setUidDeviceAffinity(_arg053, _arg134, _arg221, _arg316);
                            reply.writeNoException();
                            reply.writeInt(_result35);
                            return true;
                        case 110:
                            data.enforceInterface(DESCRIPTOR);
                            IAudioPolicyCallback _arg054 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _arg135 = data.readInt();
                            int _result36 = removeUidDeviceAffinity(_arg054, _arg135);
                            reply.writeNoException();
                            reply.writeInt(_result36);
                            return true;
                        case 111:
                            data.enforceInterface(DESCRIPTOR);
                            IAudioPolicyCallback _arg055 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _arg136 = data.readInt();
                            int[] _arg222 = data.createIntArray();
                            String[] _arg317 = data.createStringArray();
                            int _result37 = setUserIdDeviceAffinity(_arg055, _arg136, _arg222, _arg317);
                            reply.writeNoException();
                            reply.writeInt(_result37);
                            return true;
                        case 112:
                            data.enforceInterface(DESCRIPTOR);
                            IAudioPolicyCallback _arg056 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                            int _arg137 = data.readInt();
                            int _result38 = removeUserIdDeviceAffinity(_arg056, _arg137);
                            reply.writeNoException();
                            reply.writeInt(_result38);
                            return true;
                        case 113:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg018 = Uri.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg018 = null;
                            }
                            boolean hasHapticChannels = hasHapticChannels(_arg018);
                            reply.writeNoException();
                            reply.writeInt(hasHapticChannels ? 1 : 0);
                            return true;
                        case 114:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isCallScreeningModeSupported = isCallScreeningModeSupported();
                            reply.writeNoException();
                            reply.writeInt(isCallScreeningModeSupported ? 1 : 0);
                            return true;
                        case 115:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg057 = data.readInt();
                            List<AudioDeviceAttributes> _arg138 = data.createTypedArrayList(AudioDeviceAttributes.CREATOR);
                            int _result39 = setPreferredDevicesForStrategy(_arg057, _arg138);
                            reply.writeNoException();
                            reply.writeInt(_result39);
                            return true;
                        case 116:
                            data.enforceInterface(DESCRIPTOR);
                            int _result40 = removePreferredDevicesForStrategy(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result40);
                            return true;
                        case 117:
                            data.enforceInterface(DESCRIPTOR);
                            List<AudioDeviceAttributes> _result41 = getPreferredDevicesForStrategy(data.readInt());
                            reply.writeNoException();
                            reply.writeTypedList(_result41);
                            return true;
                        case 118:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg019 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg019 = null;
                            }
                            List<AudioDeviceAttributes> _result42 = getDevicesForAttributes(_arg019);
                            reply.writeNoException();
                            reply.writeTypedList(_result42);
                            return true;
                        case 119:
                            data.enforceInterface(DESCRIPTOR);
                            int _result43 = setAllowedCapturePolicy(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result43);
                            return true;
                        case 120:
                            data.enforceInterface(DESCRIPTOR);
                            int _result44 = getAllowedCapturePolicy();
                            reply.writeNoException();
                            reply.writeInt(_result44);
                            return true;
                        case 121:
                            data.enforceInterface(DESCRIPTOR);
                            registerStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 122:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 123:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setRttEnabled(_arg027);
                            return true;
                        case 124:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg020 = AudioDeviceAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg020 = null;
                            }
                            int _arg139 = data.readInt();
                            String _arg223 = data.readString();
                            setDeviceVolumeBehavior(_arg020, _arg139, _arg223);
                            reply.writeNoException();
                            return true;
                        case 125:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg021 = AudioDeviceAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg021 = null;
                            }
                            int _result45 = getDeviceVolumeBehavior(_arg021);
                            reply.writeNoException();
                            reply.writeInt(_result45);
                            return true;
                        case 126:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setMultiAudioFocusEnabled(_arg027);
                            return true;
                        case 127:
                            data.enforceInterface(DESCRIPTOR);
                            cacheParameters(data.readString());
                            reply.writeNoException();
                            return true;
                        case 128:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg058 = data.readInt();
                            List<AudioDeviceAttributes> _arg140 = data.createTypedArrayList(AudioDeviceAttributes.CREATOR);
                            int _result46 = setPreferredDevicesForCapturePreset(_arg058, _arg140);
                            reply.writeNoException();
                            reply.writeInt(_result46);
                            return true;
                        case 129:
                            data.enforceInterface(DESCRIPTOR);
                            int _result47 = clearPreferredDevicesForCapturePreset(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result47);
                            return true;
                        case 130:
                            data.enforceInterface(DESCRIPTOR);
                            List<AudioDeviceAttributes> _result48 = getPreferredDevicesForCapturePreset(data.readInt());
                            reply.writeNoException();
                            reply.writeTypedList(_result48);
                            return true;
                        case 131:
                            data.enforceInterface(DESCRIPTOR);
                            registerCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 132:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 133:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg059 = data.readInt();
                            int _arg141 = data.readInt();
                            int _arg224 = data.readInt();
                            String _arg318 = data.readString();
                            int _arg47 = data.readInt();
                            int _arg53 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg6 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg6 = null;
                            }
                            int _arg72 = data.readInt();
                            adjustStreamVolumeForUid(_arg059, _arg141, _arg224, _arg318, _arg47, _arg53, _arg6, _arg72);
                            return true;
                        case 134:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg060 = data.readInt();
                            int _arg142 = data.readInt();
                            int _arg225 = data.readInt();
                            String _arg319 = data.readString();
                            int _arg48 = data.readInt();
                            int _arg54 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg62 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg62 = null;
                            }
                            int _arg73 = data.readInt();
                            adjustSuggestedStreamVolumeForUid(_arg060, _arg142, _arg225, _arg319, _arg48, _arg54, _arg62, _arg73);
                            return true;
                        case 135:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg061 = data.readInt();
                            int _arg143 = data.readInt();
                            int _arg226 = data.readInt();
                            String _arg320 = data.readString();
                            int _arg49 = data.readInt();
                            int _arg55 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg63 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg63 = null;
                            }
                            int _arg74 = data.readInt();
                            setStreamVolumeForUid(_arg061, _arg143, _arg226, _arg320, _arg49, _arg55, _arg63, _arg74);
                            return true;
                        case 136:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            boolean isMusicActive = isMusicActive(_arg027);
                            reply.writeNoException();
                            reply.writeInt(isMusicActive ? 1 : 0);
                            return true;
                        case 137:
                            data.enforceInterface(DESCRIPTOR);
                            int _result49 = getDevicesForStream(data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result49);
                            return true;
                        case 138:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _result50 = getAvailableCommunicationDeviceIds();
                            reply.writeNoException();
                            reply.writeIntArray(_result50);
                            return true;
                        case 139:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg062 = data.readStrongBinder();
                            int _arg144 = data.readInt();
                            boolean communicationDevice = setCommunicationDevice(_arg062, _arg144);
                            reply.writeNoException();
                            reply.writeInt(communicationDevice ? 1 : 0);
                            return true;
                        case 140:
                            data.enforceInterface(DESCRIPTOR);
                            int _result51 = getCommunicationDevice();
                            reply.writeNoException();
                            reply.writeInt(_result51);
                            return true;
                        case 141:
                            data.enforceInterface(DESCRIPTOR);
                            registerCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 142:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 143:
                            data.enforceInterface(DESCRIPTOR);
                            boolean areNavigationRepeatSoundEffectsEnabled = areNavigationRepeatSoundEffectsEnabled();
                            reply.writeNoException();
                            reply.writeInt(areNavigationRepeatSoundEffectsEnabled ? 1 : 0);
                            return true;
                        case 144:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setNavigationRepeatSoundEffectsEnabled(_arg027);
                            return true;
                        case 145:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isHomeSoundEffectEnabled = isHomeSoundEffectEnabled();
                            reply.writeNoException();
                            reply.writeInt(isHomeSoundEffectEnabled ? 1 : 0);
                            return true;
                        case 146:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = true;
                            }
                            setHomeSoundEffectEnabled(_arg027);
                            return true;
                        case 147:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg022 = AudioDeviceAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg022 = null;
                            }
                            long _arg145 = data.readLong();
                            boolean additionalOutputDeviceDelay = setAdditionalOutputDeviceDelay(_arg022, _arg145);
                            reply.writeNoException();
                            reply.writeInt(additionalOutputDeviceDelay ? 1 : 0);
                            return true;
                        case 148:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg023 = AudioDeviceAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg023 = null;
                            }
                            long _result52 = getAdditionalOutputDeviceDelay(_arg023);
                            reply.writeNoException();
                            reply.writeLong(_result52);
                            return true;
                        case 149:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg024 = AudioDeviceAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg024 = null;
                            }
                            long _result53 = getMaxAdditionalOutputDeviceDelay(_arg024);
                            reply.writeNoException();
                            reply.writeLong(_result53);
                            return true;
                        case 150:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg025 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg025 = null;
                            }
                            int _arg146 = data.readInt();
                            IBinder _arg227 = data.readStrongBinder();
                            IAudioFocusDispatcher _arg321 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                            String _arg410 = data.readString();
                            String _arg56 = data.readString();
                            int _arg66 = data.readInt();
                            int _arg75 = data.readInt();
                            int _result54 = requestAudioFocusForTest(_arg025, _arg146, _arg227, _arg321, _arg410, _arg56, _arg66, _arg75);
                            reply.writeNoException();
                            reply.writeInt(_result54);
                            return true;
                        case 151:
                            data.enforceInterface(DESCRIPTOR);
                            IAudioFocusDispatcher _arg063 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                            String _arg147 = data.readString();
                            if (data.readInt() != 0) {
                                _arg23 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg23 = null;
                            }
                            String _arg322 = data.readString();
                            int _result55 = abandonAudioFocusForTest(_arg063, _arg147, _arg23, _arg322);
                            reply.writeNoException();
                            reply.writeInt(_result55);
                            return true;
                        case 152:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg026 = AudioAttributes.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg026 = null;
                            }
                            long _result56 = getFadeOutDurationOnFocusLossMillis(_arg026);
                            reply.writeNoException();
                            reply.writeLong(_result56);
                            return true;
                        case 153:
                            data.enforceInterface(DESCRIPTOR);
                            registerModeDispatcher(IAudioModeDispatcher.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 154:
                            data.enforceInterface(DESCRIPTOR);
                            unregisterModeDispatcher(IAudioModeDispatcher.Stub.asInterface(data.readStrongBinder()));
                            return true;
                        case 155:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg064 = data.readString();
                            long _arg148 = data.readLong();
                            setEnableDoVibrateTime(_arg064, _arg148);
                            reply.writeNoException();
                            return true;
                        case 156:
                            data.enforceInterface(DESCRIPTOR);
                            long _result57 = getEnableDoVibrateTime(data.readString());
                            reply.writeNoException();
                            reply.writeLong(_result57);
                            return true;
                        case 157:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg065 = data.readString();
                            IBinder _arg149 = data.readStrongBinder();
                            setNtParameters(_arg065, _arg149);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IAudioService {
            public static IAudioService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.media.IAudioService
            public int trackPlayer(PlayerBase.PlayerIdCard pic) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pic != null) {
                        _data.writeInt(1);
                        pic.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().trackPlayer(pic);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playerAttributes(int piid, AudioAttributes attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playerAttributes(piid, attr);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playerEvent(int piid, int event, int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(event);
                    _data.writeInt(deviceId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playerEvent(piid, event, deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void releasePlayer(int piid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().releasePlayer(piid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int trackRecorder(IBinder recorder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(recorder);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().trackRecorder(recorder);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void recorderEvent(int riid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(riid);
                    _data.writeInt(event);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().recorderEvent(riid, event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void releaseRecorder(int riid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(riid);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().releaseRecorder(riid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playerSessionId(int piid, int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(sessionId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playerSessionId(piid, sessionId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void trackAudioStart(int pid, int uid, int sessionId, int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(sessionId);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().trackAudioStart(pid, uid, sessionId, streamType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void trackAudioStop(int pid, int uid, int sessionId, int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(sessionId);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().trackAudioStop(pid, uid, sessionId, streamType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, String callingPackage, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(direction);
                    _data.writeInt(suggestedStreamType);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adjustSuggestedStreamVolume(direction, suggestedStreamType, flags, callingPackage, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void adjustStreamVolume(int streamType, int direction, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(direction);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adjustStreamVolume(streamType, direction, flags, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setStreamVolume(int streamType, int index, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(index);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStreamVolume(streamType, index, flags, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void handleVolumeKey(KeyEvent event, boolean isOnTv, String callingPackage, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (isOnTv) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(callingPackage);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleVolumeKey(event, isOnTv, callingPackage, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isStreamMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStreamMute(streamType);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void forceRemoteSubmixFullVolume(boolean startForcing, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startForcing ? 1 : 0);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceRemoteSubmixFullVolume(startForcing, cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isMasterMute() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMasterMute();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMasterMute(boolean mute, int flags, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mute ? 1 : 0);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMasterMute(mute, flags, callingPackage, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStreamVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getStreamMinVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStreamMinVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getStreamMaxVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStreamMaxVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAudioVolumeGroups();
                    }
                    _reply.readException();
                    List<AudioVolumeGroup> _result = _reply.createTypedArrayList(AudioVolumeGroup.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumeIndexForAttributes(AudioAttributes aa, int index, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(index);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumeIndexForAttributes(aa, index, flags, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVolumeIndexForAttributes(aa);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMaxVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaxVolumeIndexForAttributes(aa);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMinVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMinVolumeIndexForAttributes(aa);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getLastAudibleStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastAudibleStreamVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSupportedSystemUsages(int[] systemUsages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(systemUsages);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSupportedSystemUsages(systemUsages);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int[] getSupportedSystemUsages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSupportedSystemUsages();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAudioProductStrategies();
                    }
                    _reply.readException();
                    List<AudioProductStrategy> _result = _reply.createTypedArrayList(AudioProductStrategy.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isMicrophoneMuted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMicrophoneMuted();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMicrophoneMute(boolean on, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMicrophoneMute(on, callingPackage, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMicrophoneMuteFromSwitch(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMicrophoneMuteFromSwitch(on);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRingerModeExternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRingerModeExternal(ringerMode, caller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRingerModeInternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRingerModeInternal(ringerMode, caller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getRingerModeExternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRingerModeExternal();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getRingerModeInternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRingerModeInternal();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isValidRingerMode(int ringerMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isValidRingerMode(ringerMode);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVibrateSetting(int vibrateType, int vibrateSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    _data.writeInt(vibrateSetting);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVibrateSetting(vibrateType, vibrateSetting);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getVibrateSetting(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVibrateSetting(vibrateType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean shouldVibrate(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().shouldVibrate(vibrateType);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMode(int mode, IBinder cb, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStrongBinder(cb);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMode(mode, cb, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMode();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playSoundEffect(int effectType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    boolean _status = this.mRemote.transact(44, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playSoundEffect(effectType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playSoundEffectVolume(int effectType, float volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    _data.writeFloat(volume);
                    boolean _status = this.mRemote.transact(45, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playSoundEffectVolume(effectType, volume);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean loadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().loadSoundEffects();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unloadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unloadSoundEffects();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void reloadAudioSettings() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(48, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reloadAudioSettings();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public Map getSurroundFormats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSurroundFormats();
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List getReportedSurroundFormats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getReportedSurroundFormats();
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean setSurroundFormatEnabled(int audioFormat, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioFormat);
                    boolean _result = true;
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setSurroundFormatEnabled(audioFormat, enabled);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isSurroundFormatEnabled(int audioFormat) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioFormat);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSurroundFormatEnabled(audioFormat);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean setEncodedSurroundMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setEncodedSurroundMode(mode);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getEncodedSurroundMode(int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(targetSdkVersion);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEncodedSurroundMode(targetSdkVersion);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void avrcpSupportsAbsoluteVolume(String address, boolean support) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(support ? 1 : 0);
                    boolean _status = this.mRemote.transact(55, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().avrcpSupportsAbsoluteVolume(address, support);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSpeakerphoneOn(IBinder cb, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSpeakerphoneOn(cb, on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isSpeakerphoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSpeakerphoneOn();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothScoOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothScoOn(on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isBluetoothScoOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBluetoothScoOn();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothA2dpOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothA2dpOn(on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isBluetoothA2dpOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBluetoothA2dpOn();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int requestAudioFocus(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(durationHint);
                        try {
                            _data.writeStrongBinder(cb);
                            IBinder iBinder = null;
                            _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                            _data.writeString(clientId);
                            _data.writeString(callingPackageName);
                            _data.writeInt(flags);
                            if (pcb != null) {
                                iBinder = pcb.asBinder();
                            }
                            _data.writeStrongBinder(iBinder);
                            _data.writeInt(sdk);
                            boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                int requestAudioFocus = Stub.getDefaultImpl().requestAudioFocus(aa, durationHint, cb, fd, clientId, callingPackageName, flags, pcb, sdk);
                                _reply.recycle();
                                _data.recycle();
                                return requestAudioFocus;
                            }
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.media.IAudioService
            public int abandonAudioFocus(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                    _data.writeString(clientId);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackageName);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().abandonAudioFocus(fd, clientId, aa, callingPackageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioFocusClient(String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(clientId);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioFocusClient(clientId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getCurrentAudioFocus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentAudioFocus();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void startBluetoothSco(IBinder cb, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    _data.writeInt(targetSdkVersion);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startBluetoothSco(cb, targetSdkVersion);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void startBluetoothScoVirtualCall(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startBluetoothScoVirtualCall(cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void stopBluetoothSco(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopBluetoothSco(cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void forceVolumeControlStream(int streamType, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceVolumeControlStream(streamType, cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRingtonePlayer(IRingtonePlayer player) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(player != null ? player.asBinder() : null);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRingtonePlayer(player);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public IRingtonePlayer getRingtonePlayer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRingtonePlayer();
                    }
                    _reply.readException();
                    IRingtonePlayer _result = IRingtonePlayer.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getUiSoundsStreamType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUiSoundsStreamType();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setWiredDeviceConnectionState(int type, int state, String address, String name, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(state);
                    _data.writeString(address);
                    _data.writeString(name);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setWiredDeviceConnectionState(type, state, address, name, caller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleBluetoothA2dpDeviceConfigChange(device);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void handleBluetoothA2dpActiveDeviceChange(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(profile);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(a2dpVolume);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleBluetoothA2dpActiveDeviceChange(device, state, profile, suppressNoisyIntent, a2dpVolume);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver observer) throws RemoteException {
                AudioRoutesInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startWatchingRoutes(observer);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AudioRoutesInfo.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isCameraSoundForced() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCameraSoundForced();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumeController(IVolumeController controller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumeController(controller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    _data.writeInt(visible ? 1 : 0);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyVolumeControllerVisible(controller, visible);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isStreamAffectedByRingerMode(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStreamAffectedByRingerMode(streamType);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isStreamAffectedByMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStreamAffectedByMute(streamType);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void disableSafeMediaVolume(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableSafeMediaVolume(callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setHdmiSystemAudioSupported(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setHdmiSystemAudioSupported(on);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isHdmiSystemAudioSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHdmiSystemAudioSupported();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public String registerAudioPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, IMediaProjection projection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IBinder iBinder = null;
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(hasFocusListener ? 1 : 0);
                    _data.writeInt(isFocusPolicy ? 1 : 0);
                    _data.writeInt(isTestFocusPolicy ? 1 : 0);
                    if (!isVolumeController) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (projection != null) {
                        iBinder = projection.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    try {
                        boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            String registerAudioPolicy = Stub.getDefaultImpl().registerAudioPolicy(policyConfig, pcb, hasFocusListener, isFocusPolicy, isTestFocusPolicy, isVolumeController, projection);
                            _reply.recycle();
                            _data.recycle();
                            return registerAudioPolicy;
                        }
                        _reply.readException();
                        String _result = _reply.readString();
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioPolicyAsync(IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(86, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioPolicyAsync(pcb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioPolicy(IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioPolicy(pcb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int addMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addMixForPolicy(policyConfig, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int removeMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeMixForPolicy(policyConfig, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setFocusPropertiesForPolicy(int duckingBehavior, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(duckingBehavior);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setFocusPropertiesForPolicy(duckingBehavior, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumePolicy(VolumePolicy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumePolicy(policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean hasRegisteredDynamicPolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasRegisteredDynamicPolicy();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerRecordingCallback(rcdb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(94, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterRecordingCallback(rcdb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActiveRecordingConfigurations();
                    }
                    _reply.readException();
                    List<AudioRecordingConfiguration> _result = _reply.createTypedArrayList(AudioRecordingConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerPlaybackCallback(pcdb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(97, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterPlaybackCallback(pcdb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivePlaybackConfigurations();
                    }
                    _reply.readException();
                    List<AudioPlaybackConfiguration> _result = _reply.createTypedArrayList(AudioPlaybackConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void disableRingtoneSync(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableRingtoneSync(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getFocusRampTimeMs(int focusGain, AudioAttributes attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(focusGain);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFocusRampTimeMs(focusGain, attr);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int dispatchFocusChange(AudioFocusInfo afi, int focusChange, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(focusChange);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().dispatchFocusChange(afi, focusChange, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(hasOpPlayAudio ? 1 : 0);
                    boolean _status = this.mRemote.transact(102, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playerHasOpPlayAudio(piid, hasOpPlayAudio);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice device, int state, boolean suppressNoisyIntent, int musicDevice) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(musicDevice);
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothHearingAidDeviceConnectionState(device, state, suppressNoisyIntent, musicDevice);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(profile);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(a2dpVolume);
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(device, state, profile, suppressNoisyIntent, a2dpVolume);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setFocusRequestResultFromExtPolicy(AudioFocusInfo afi, int requestResult, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestResult);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(105, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFocusRequestResultFromExtPolicy(afi, requestResult, pcb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerAudioServerStateDispatcher(asd);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    boolean _status = this.mRemote.transact(107, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioServerStateDispatcher(asd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isAudioServerRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAudioServerRunning();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setUidDeviceAffinity(IAudioPolicyCallback pcb, int uid, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(uid);
                    _data.writeIntArray(deviceTypes);
                    _data.writeStringArray(deviceAddresses);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setUidDeviceAffinity(pcb, uid, deviceTypes, deviceAddresses);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int removeUidDeviceAffinity(IAudioPolicyCallback pcb, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeUidDeviceAffinity(pcb, uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setUserIdDeviceAffinity(IAudioPolicyCallback pcb, int userId, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeIntArray(deviceTypes);
                    _data.writeStringArray(deviceAddresses);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setUserIdDeviceAffinity(pcb, userId, deviceTypes, deviceAddresses);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int removeUserIdDeviceAffinity(IAudioPolicyCallback pcb, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeUserIdDeviceAffinity(pcb, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean hasHapticChannels(Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasHapticChannels(uri);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isCallScreeningModeSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCallScreeningModeSupported();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setPreferredDevicesForStrategy(int strategy, List<AudioDeviceAttributes> device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(strategy);
                    _data.writeTypedList(device);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPreferredDevicesForStrategy(strategy, device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int removePreferredDevicesForStrategy(int strategy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(strategy);
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removePreferredDevicesForStrategy(strategy);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioDeviceAttributes> getPreferredDevicesForStrategy(int strategy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(strategy);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPreferredDevicesForStrategy(strategy);
                    }
                    _reply.readException();
                    List<AudioDeviceAttributes> _result = _reply.createTypedArrayList(AudioDeviceAttributes.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioDeviceAttributes> getDevicesForAttributes(AudioAttributes attributes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (attributes != null) {
                        _data.writeInt(1);
                        attributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDevicesForAttributes(attributes);
                    }
                    _reply.readException();
                    List<AudioDeviceAttributes> _result = _reply.createTypedArrayList(AudioDeviceAttributes.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setAllowedCapturePolicy(int capturePolicy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capturePolicy);
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setAllowedCapturePolicy(capturePolicy);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getAllowedCapturePolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllowedCapturePolicy();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerStrategyPreferredDevicesDispatcher(dispatcher);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(122, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterStrategyPreferredDevicesDispatcher(dispatcher);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRttEnabled(boolean rttEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rttEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(123, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRttEnabled(rttEnabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setDeviceVolumeBehavior(AudioDeviceAttributes device, int deviceVolumeBehavior, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(deviceVolumeBehavior);
                    _data.writeString(pkgName);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceVolumeBehavior(device, deviceVolumeBehavior, pkgName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getDeviceVolumeBehavior(AudioDeviceAttributes device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceVolumeBehavior(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMultiAudioFocusEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(126, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMultiAudioFocusEnabled(enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void cacheParameters(String keyValuePairs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keyValuePairs);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cacheParameters(keyValuePairs);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setPreferredDevicesForCapturePreset(int capturePreset, List<AudioDeviceAttributes> devices) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capturePreset);
                    _data.writeTypedList(devices);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPreferredDevicesForCapturePreset(capturePreset, devices);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int clearPreferredDevicesForCapturePreset(int capturePreset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capturePreset);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().clearPreferredDevicesForCapturePreset(capturePreset);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioDeviceAttributes> getPreferredDevicesForCapturePreset(int capturePreset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capturePreset);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPreferredDevicesForCapturePreset(capturePreset);
                    }
                    _reply.readException();
                    List<AudioDeviceAttributes> _result = _reply.createTypedArrayList(AudioDeviceAttributes.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerCapturePresetDevicesRoleDispatcher(dispatcher);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(132, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterCapturePresetDevicesRoleDispatcher(dispatcher);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void adjustStreamVolumeForUid(int streamType, int direction, int flags, String packageName, int uid, int pid, UserHandle userHandle, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(streamType);
                        try {
                            _data.writeInt(direction);
                            try {
                                _data.writeInt(flags);
                            } catch (Throwable th) {
                                th = th;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
                try {
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(targetSdkVersion);
                    boolean _status = this.mRemote.transact(133, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adjustStreamVolumeForUid(streamType, direction, flags, packageName, uid, pid, userHandle, targetSdkVersion);
                        _data.recycle();
                        return;
                    }
                    _data.recycle();
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.media.IAudioService
            public void adjustSuggestedStreamVolumeForUid(int streamType, int direction, int flags, String packageName, int uid, int pid, UserHandle userHandle, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(streamType);
                        try {
                            _data.writeInt(direction);
                            try {
                                _data.writeInt(flags);
                            } catch (Throwable th) {
                                th = th;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
                try {
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(targetSdkVersion);
                    boolean _status = this.mRemote.transact(134, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adjustSuggestedStreamVolumeForUid(streamType, direction, flags, packageName, uid, pid, userHandle, targetSdkVersion);
                        _data.recycle();
                        return;
                    }
                    _data.recycle();
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.media.IAudioService
            public void setStreamVolumeForUid(int streamType, int direction, int flags, String packageName, int uid, int pid, UserHandle userHandle, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(streamType);
                        try {
                            _data.writeInt(direction);
                            try {
                                _data.writeInt(flags);
                            } catch (Throwable th) {
                                th = th;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
                try {
                    _data.writeString(packageName);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(targetSdkVersion);
                    boolean _status = this.mRemote.transact(135, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStreamVolumeForUid(streamType, direction, flags, packageName, uid, pid, userHandle, targetSdkVersion);
                        _data.recycle();
                        return;
                    }
                    _data.recycle();
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.media.IAudioService
            public boolean isMusicActive(boolean remotely) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(remotely ? 1 : 0);
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMusicActive(remotely);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getDevicesForStream(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDevicesForStream(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int[] getAvailableCommunicationDeviceIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAvailableCommunicationDeviceIds();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean setCommunicationDevice(IBinder cb, int portId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    _data.writeInt(portId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setCommunicationDevice(cb, portId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getCommunicationDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCommunicationDevice();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerCommunicationDeviceDispatcher(dispatcher);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(142, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterCommunicationDeviceDispatcher(dispatcher);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean areNavigationRepeatSoundEffectsEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().areNavigationRepeatSoundEffectsEnabled();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setNavigationRepeatSoundEffectsEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(144, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNavigationRepeatSoundEffectsEnabled(enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isHomeSoundEffectEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHomeSoundEffectEnabled();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setHomeSoundEffectEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(146, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setHomeSoundEffectEnabled(enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean setAdditionalOutputDeviceDelay(AudioDeviceAttributes device, long delayMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(delayMillis);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setAdditionalOutputDeviceDelay(device, delayMillis);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public long getAdditionalOutputDeviceDelay(AudioDeviceAttributes device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAdditionalOutputDeviceDelay(device);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public long getMaxAdditionalOutputDeviceDelay(AudioDeviceAttributes device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaxAdditionalOutputDeviceDelay(device);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int requestAudioFocusForTest(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int uid, int sdk) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(durationHint);
                        try {
                            _data.writeStrongBinder(cb);
                            _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                            try {
                                _data.writeString(clientId);
                                _data.writeString(callingPackageName);
                                _data.writeInt(uid);
                                _data.writeInt(sdk);
                                boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    int requestAudioFocusForTest = Stub.getDefaultImpl().requestAudioFocusForTest(aa, durationHint, cb, fd, clientId, callingPackageName, uid, sdk);
                                    _reply.recycle();
                                    _data.recycle();
                                    return requestAudioFocusForTest;
                                }
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }

            @Override // android.media.IAudioService
            public int abandonAudioFocusForTest(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                    _data.writeString(clientId);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackageName);
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().abandonAudioFocusForTest(fd, clientId, aa, callingPackageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public long getFadeOutDurationOnFocusLossMillis(AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFadeOutDurationOnFocusLossMillis(aa);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerModeDispatcher(IAudioModeDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerModeDispatcher(dispatcher);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterModeDispatcher(IAudioModeDispatcher dispatcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dispatcher != null ? dispatcher.asBinder() : null);
                    boolean _status = this.mRemote.transact(154, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterModeDispatcher(dispatcher);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setEnableDoVibrateTime(String pkgName, long time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeLong(time);
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setEnableDoVibrateTime(pkgName, time);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public long getEnableDoVibrateTime(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEnableDoVibrateTime(pkgName);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setNtParameters(String keyValuePairs, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keyValuePairs);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNtParameters(keyValuePairs, cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAudioService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAudioService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
