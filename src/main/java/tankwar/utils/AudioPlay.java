package tankwar.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;


/**
 * 背景音乐播放类
 */
public class AudioPlay {
    private final String fileName;

    public AudioPlay(String fileName) {
        this.fileName = fileName;
    }

    public synchronized void player() {
        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);

            clip.addLineListener(myLineEvent -> {
                if (myLineEvent.getType() == LineEvent.Type.STOP)
                    clip.close();
            });

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 内部类实现播放音乐线程
     */
    public class AudioThread extends Thread {
        @Override
        public void run() {
            player();
        }
    }
}
