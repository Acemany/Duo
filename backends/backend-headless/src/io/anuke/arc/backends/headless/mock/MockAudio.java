package arc.backends.headless.mock;

import arc.Audio;
import arc.audio.AudioDevice;
import arc.audio.AudioRecorder;
import arc.audio.Music;
import arc.audio.Sound;
import arc.files.FileHandle;

/**
 * The headless backend does its best to mock elements. This is intended to make code-sharing between
 * server and client as simple as possible.
 */
public class MockAudio implements Audio{

    @Override
    public AudioDevice newAudioDevice(int samplingRate, boolean isMono){
        return new MockAudioDevice();
    }

    @Override
    public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono){
        return new MockAudioRecorder();
    }

    @Override
    public Sound newSound(FileHandle fileHandle){
        return new MockSound();
    }

    @Override
    public Music newMusic(FileHandle file){
        return new MockMusic();
    }
}
