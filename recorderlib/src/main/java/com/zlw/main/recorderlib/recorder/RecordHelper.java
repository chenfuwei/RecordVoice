package com.zlw.main.recorderlib.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import com.zlw.main.recorderlib.recorder.mp3.Mp3Encoder;
import com.zlw.main.recorderlib.utils.FileUtils;
import com.zlw.main.recorderlib.utils.ZMTimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author zhaolewei on 2018/7/10.
 */
public class RecordHelper {

    private volatile static RecordHelper instance;
    public AudioRecord audioRecord;
    // 设置正在录制的状态
    private boolean isRecord = false;
    //AudioName裸音频数据文件
    private String audioPath;
    private File file;
    private FileOutputStream fos = null;

    private long startTime = 0;

    private RecordHelper() {
        initRecord();
    }

    public static RecordHelper getInstance() {
        if (instance == null) {
            synchronized (RecordHelper.class) {
                if (instance == null) {
                    instance = new RecordHelper();
                }
            }
        }
        return instance;
    }

    private void initRecord() {
        audioPath = getTempFilePath();
        // 初始化 AudioRecord
        int minBuffSize = AudioRecord.getMinBufferSize(RecordConfig.AUDIO_SAMPLE_RATE,
                RecordConfig.channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioRecord = new AudioRecord.Builder()
                    .setAudioSource(RecordConfig.audioSource)
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(RecordConfig.audioFormat)
                            .setSampleRate(RecordConfig.AUDIO_SAMPLE_RATE)
                            .setChannelMask(RecordConfig.channelConfig)
                            .build())
                    .setBufferSizeInBytes(minBuffSize)
                    .build();
        } else {
            audioRecord = new AudioRecord(RecordConfig.audioSource,
                    RecordConfig.AUDIO_SAMPLE_RATE, RecordConfig.channelConfig, RecordConfig.audioFormat,
                     minBuffSize);
        }

        Mp3Encoder.init(RecordConfig.AUDIO_SAMPLE_RATE, 1, RecordConfig.AUDIO_SAMPLE_RATE,128);
    }

    public void startRecord() {
        if (!isRecord) {
            // 让录制状态为true
            isRecord = true;
            //开始录制
            audioRecord.startRecording();
            // 开启音频文件写入线程
            new Thread(new AudioRecordThread()).start();
        }
    }

    public void stopRecord() {
        if (audioRecord != null) {
            if (isRecord) {
                //停止文件写入
                isRecord = false;
                //停止录制
                audioRecord.stop();
            }
        }
    }

    private class AudioRecordThread implements Runnable {
        @Override
        public void run() {
            //往文件中写入裸数据
            writeDateToFile();
        }
    }

    private void writeDateToFile() {
        int capacity = audioRecord.getSampleRate() * audioRecord.getChannelCount();

        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
        short[] audioData = new short[capacity];
        byte[] lameBuffer = new byte[capacity];
        int readSize;

        try {
            if (file == null) {
                file = new File(audioPath);
                file.createNewFile();
                fos = new FileOutputStream(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRecord) {
            readSize = audioRecord.read(audioData, 0, capacity);
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                int lameSample = Mp3Encoder.encode(audioData, audioData, readSize, lameBuffer);
                if (lameSample > 0) {
                    try {
                        fos.write(lameBuffer, 0, lameSample);
                    } catch (IOException e) {
                        e.printStackTrace();
                        audioRecord.stop();
                    }
                }


                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startTime = startTime + 1000;
                onRecordListener.recordTimeChange(startTime);
            }
        }

        int flushSample = Mp3Encoder.flush(lameBuffer);
        if (flushSample > 0) {
            try {
                fos.write(lameBuffer, 0, flushSample);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public OnRecordListener onRecordListener;

    public void setOnRecordListener(OnRecordListener onRecordListener){
        this.onRecordListener = onRecordListener;
    }

    public interface OnRecordListener {
        void recordTimeChange(long time);
    }

    /**
     * 释放资源
     */
    public void release() {
        audioRecord.release();
        try {
            fos.close();
            fos = null;
            file = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        startTime = 0;
    }

    /**
     * 根据当前的时间生成相应的文件名
     * 实例 record_20160101_13_15_12
     */
    private String getTempFilePath() {
        String fileDir = String.format(Locale.getDefault(), "%s/Record/", Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!FileUtils.createOrExistsDir(fileDir)) {
            //创建文佳佳失败
        }
        String fileName = String.format(Locale.getDefault(), "record_tmp_%s", FileUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.SIMPLIFIED_CHINESE)));
        return String.format(Locale.getDefault(), "%s%s.mp3", fileDir, fileName);
    }
}
