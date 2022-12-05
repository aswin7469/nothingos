package android.media;
/* loaded from: classes2.dex */
public class AudioDevicePortConfig extends AudioPortConfig {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDevicePortConfig(AudioDevicePort devicePort, int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        super(devicePort, samplingRate, channelMask, format, gain);
    }

    AudioDevicePortConfig(AudioDevicePortConfig config) {
        this(config.mo1521port(), config.samplingRate(), config.channelMask(), config.format(), config.gain());
    }

    @Override // android.media.AudioPortConfig
    /* renamed from: port */
    public AudioDevicePort mo1521port() {
        return (AudioDevicePort) this.mPort;
    }
}
