package arc.backends.gwt;

import arc.Audio;
import arc.audio.AudioDevice;
import arc.audio.AudioRecorder;
import arc.audio.Music;
import arc.audio.Sound;
import arc.files.FileHandle;
import arc.util.ArcRuntimeException;

public class GwtAudio implements Audio{
    @Override
    public AudioDevice newAudioDevice(int samplingRate, boolean isMono){
        throw new ArcRuntimeException("AudioDevice not supported by GWT backend");
    }

    @Override
    public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono){
        throw new ArcRuntimeException("AudioRecorder not supported by GWT backend");
    }

    @Override
    public Sound newSound(FileHandle fileHandle){
        return new GwtSound(fileHandle);
    }

    @Override
    public Music newMusic(FileHandle file){
        return new GwtMusic(file);
    }
}
