package com.news.star.recordvoice;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.utils.ZMTimeUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author 郑刚耀
 * @date 2019/9/16.
 * GitHub：
 * email：1397452815@qq.com
 * description：
 */
public class RecordActivity extends AppCompatActivity implements View.OnClickListener, RecordHelper.OnRecordListener {

    private static final String TAG = RecordActivity.class.getSimpleName();
    private TextView tvRecord, tvTime;
    private ProgressBar progressBar;
    private RecordHelper recordHelper;
    // 设置正在录制的状态
    private boolean recording = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRecord = this.findViewById(R.id.tv_record);
        tvRecord.setOnClickListener(this);
        tvTime = this.findViewById(R.id.tv_time);
        progressBar = this.findViewById(R.id.progress_bar);

        recordHelper = RecordHelper.getInstance();
        recordHelper.setOnRecordListener(this);
    }

    @Override
    public void onClick(View v) {
        if (recording) {
            recordHelper.stopRecord();
        } else {
            recordHelper.startRecord();
        }
        tvRecord.setText(recording ? "录制已暂停" : "正在录制");
        recording = !recording;
    }

    @Override
    public void recordTimeChange(final long time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String recordTime = ZMTimeUtils.getSecondTimes(time);
                String totalTime = ZMTimeUtils.getSecondTimes(RecordConfig.RECORD_TOTAL_TIME);
                tvTime.setText(recordTime + "/" + totalTime);

                progressBar.setProgress((int) time);
                progressBar.setMax(RecordConfig.RECORD_TOTAL_TIME);
            }
        });

    }
}
