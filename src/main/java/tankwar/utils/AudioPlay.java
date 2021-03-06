package tankwar.utils;

import sun.audio.*;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 背景音乐播放类
 */
public class AudioPlay {
    private final String fileName;

    public AudioPlay(String fileName) {
        this.fileName = fileName;
    }

//    public synchronized void player() {
//        SourceDataLine line = null;
//        try {
//           java.net.URL soundURL = AudioPlay.class.getResource(fileName);
//           AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( soundURL );
//           AudioFormat format = audioInputStream.getFormat();
//           DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
//           line = (SourceDataLine) AudioSystem.getLine(info);
//           line.open(format);//或者line.open();format参数可有可无
//           line.start();
//           int nBytesRead = 0;
//           byte[] buffer = new byte[512];
//           while (true) {
//              nBytesRead = audioInputStream.read(buffer, 0, buffer.length);
//              if (nBytesRead <= 0) return;
//              line.write(buffer, 0, nBytesRead);
//           }
//
//        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
//            e.printStackTrace();
//        }finally {
//            if (null != line)
//            {
//                line.drain();
//                line.close();
//            }
//        }
//    }

    public void player() {
        try {
            InputStream in = new FileInputStream(new File(this.getClass().getClassLoader().getResource(fileName).getFile()));
            AudioStream audio = new AudioStream(in);
            AudioPlayer.player.start(audio);
        } catch (IOException e) {
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
