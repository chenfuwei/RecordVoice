package com.news.star.recordvoice;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRecord;
    private boolean isStart = false;
    private boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        tvRecord = this.findViewById(R.id.tv_record);
        tvRecord.setOnClickListener(this);

        initRecord();

        AndPermission.with(this)
                .runtime()
                .permission(new String[]{Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.RECORD_AUDIO})
                .start();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void initRecord() {
        String recordDir = String.format(Locale.getDefault(), "%s/Record/com.news.star.recordvoice/",
                Environment.getExternalStorageDirectory().getAbsolutePath());


    }

    @Override
    public void onClick(View v) {

    }
}
