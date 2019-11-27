package com.zlw.main.recorderlib.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Locale;

/**
 * @author zhaolewei on 2018/7/11.
 */
public class RecordConfig implements Serializable {

    // 音频获取源
    public static int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static final int AUDIO_SAMPLE_RATE = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    /**
     * 录制总时长 --30分钟
     */
    public static int RECORD_TOTAL_TIME = 30*60*1000;

}
