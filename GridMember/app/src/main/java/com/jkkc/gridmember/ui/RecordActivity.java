package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.Record.AudioPlayer;
import com.jkkc.gridmember.Record.AudioRecorder;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by Guan on 2018/1/29.
 */

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RecordActivity.class.getSimpleName();

    private Button mBtnBack;

    private Button mBtnRecord;
    private Button mBtnStopRecord;
    private Button mBtnPlay;
    private Button mBtnStopPlay;

    private AudioRecorder mAudioRecorder;
    private AudioPlayer mAudioPlayer;
    private String mPath;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_record);

        mBtnBack = (Button) findViewById(R.id.btnBack);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mBtnRecord = (Button) findViewById(R.id.btnRecord);
        mBtnStopRecord = (Button) findViewById(R.id.btnStopRecord);
        mBtnPlay = (Button) findViewById(R.id.btnPlay);
        mBtnStopPlay = (Button) findViewById(R.id.btnStopPlay);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);


        mBtnRecord.setOnClickListener(this);
        mBtnStopRecord.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnStopPlay.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnRecord:

                if (mAudioRecorder == null) {
                    mAudioRecorder = new AudioRecorder();

                }

                try {

                    mAudioRecorder.start();
                    mBtnRecord.setVisibility(View.GONE);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;

            case R.id.btnStopRecord:

                if (mAudioRecorder != null) {

                    mAudioRecorder.stop();

                }

                break;


            case R.id.btnPlay:

                if (mAudioPlayer == null) {

                    mAudioPlayer = new AudioPlayer();
                    mAudioPlayer.setPlayerPath(mAudioRecorder.getPath());


                }

                mAudioPlayer.play();

                break;

            case R.id.btnStopPlay:

                if (mAudioPlayer != null) {
                    mAudioPlayer.stop();
                }
                break;

            case R.id.btnSubmit:

                if (mAudioRecorder != null) {

//                    String token = UserInfoManager.getInstance().getLoginInfo().getData().getToken();
                    String user_info = PrefUtils.getString(getApplicationContext(), "user_info", null);
                    Gson gson = new Gson();
                    LoginInfo loginInfo = gson.fromJson(user_info, LoginInfo.class);
                    String token = loginInfo.getData().getToken();

                    if (!TextUtils.isEmpty(token)) {
                        Log.d(TAG, mAudioRecorder.getPath());
                        OkGo.<String>post(Config.GRIDMAN_URL + Config.UPLOADFILE_URL)//
                                .tag(this)//
                                //.isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                //.params("param1", "paramValue1")        // 这里可以上传参数
                                .params("uploadFile", new File(mAudioRecorder.getPath()))   // 可以添加文件上传
                                //.params("file2", new File("filepath2"))     // 支持多文件同时添加上传
                                .params("token", token)
//                                        .addFileParams(keyName, files)    // 这里支持一个key传多个文件
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {

                                        Log.d(TAG, "Upload success");
                                        Log.d(TAG, "录音地址：" +
                                                Config.GRIDMAN_URL +
                                                response.body().toString());

                                        Intent intent = new Intent(getApplicationContext(),
                                                ReturnVisitRecordActivity.class);
//                                        intent.putExtra("record", Config.GRIDMAN_URL +
//                                                response.body().toString());
                                        PrefUtils.setString(getApplicationContext(), "record"
                                                , response.body().toString());


                                        startActivity(intent);
                                        finish();


                                    }

                                });

                    }

                } else {

                    Toast.makeText(getApplicationContext(), "您还没有录音", Toast.LENGTH_SHORT).show();
                }


                break;


        }

    }


}
