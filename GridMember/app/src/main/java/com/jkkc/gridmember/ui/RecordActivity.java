package com.jkkc.gridmember.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jkkc.gridmember.R;
import com.jkkc.gridmember.Record.AudioPlayer;
import com.jkkc.gridmember.Record.AudioRecorder;

import java.io.IOException;

/**
 * Created by Guan on 2018/1/29.
 */

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnBack;

    private Button mBtnRecord;
    private Button mBtnStopRecord;
    private Button mBtnPlay;
    private Button mBtnStopPlay;

    private AudioRecorder mAudioRecorder;
    private AudioPlayer mAudioPlayer;
    private String mPath;

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

        mBtnRecord.setOnClickListener(this);
        mBtnStopRecord.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnStopPlay.setOnClickListener(this);





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


        }

    }


}
