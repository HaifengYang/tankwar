package tankwar.utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音效工具类
 */

@SuppressWarnings("deprecation")
public class AudioUtil{
	public static final String BASE_PATH = "/";
	/**
	 * 坦克诞生音效
	 */
	public static final String ADD=BASE_PATH + "audio/add.wav";
	/**
	 * 爆炸音效
	 */
	public static final String BLAST=BASE_PATH + "audio/blast.wav";
	/**
	 * 发射子弹音效
	 */
	public static final String FIRE=BASE_PATH + "audio/fire.wav";
	/**
	 * 游戏结束音效
	 */
	public static final String GAMEOVER=BASE_PATH + "audio/gameover.wav";
	/**
	 * 子弹撞击音效
	 */
	public static final String HIT=BASE_PATH + "audio/hit.wav";
	/**
	 * 游戏开始音效
	 */
	public static final String START=BASE_PATH + "audio/start.wav";
}