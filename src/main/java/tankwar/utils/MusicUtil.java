package tankwar.utils;

import tankwar.config.ThreadPoolFactory;
import tankwar.constant.AudioFiles;

public final class MusicUtil {
    public static void playAddMusic(){
        ThreadPoolFactory.getExecutor().submit(new AudioPlay(AudioFiles.ADD).new AudioThread());
    }
    public static void playBlastMusic(){
        ThreadPoolFactory.getExecutor().submit(new AudioPlay(AudioFiles.BLAST).new AudioThread());
    }
    public static void playFireMusic(){
        ThreadPoolFactory.getExecutor().submit(new AudioPlay(AudioFiles.FIRE).new AudioThread());
    }
    public static void playGameOverMusic(){
        ThreadPoolFactory.getExecutor().submit(new AudioPlay(AudioFiles.GAMEOVER).new AudioThread());
    }
    public static void playHitMusic(){
        ThreadPoolFactory.getExecutor().submit(new AudioPlay(AudioFiles.HIT).new AudioThread());
    }
    public static void playStartMusic(){
        ThreadPoolFactory.getExecutor().submit(new AudioPlay(AudioFiles.START).new AudioThread());
    }
}
