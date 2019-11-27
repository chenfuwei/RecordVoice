package com.zlw.main.recorderlib.player;

import android.media.MediaPlayer;

/**
 * @author 郑刚耀
 * @date 2019/9/21.
 * GitHub：
 * email：1397452815@qq.com
 * description：
 */
public class VoicePlayer extends MediaPlayer {

    private static VoicePlayer voicePlayer;
    private MediaPlayer mediaPlayer;

    private VoicePlayer(){
        init();
    }

    public static VoicePlayer getInstance(){
        if (voicePlayer == null){
            synchronized (VoicePlayer.class){
                if (voicePlayer == null){
                    voicePlayer = new VoicePlayer();
                }
            }
        }
        return voicePlayer;
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(0.5f,0.5f);

    }
}
