package com.jkkc.gridmember.Record;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Guan on 2018/1/27.
 */

public class AudioRecorder {


    private final String mDir;
    private String fileName;
    private MediaRecorder mMediaRecorder;


    public AudioRecorder() {

        mDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/myRecorder/";

    }

    public void start() throws IOException {

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {

            throw new IOException("没有可用的存储空间");

        }

        File myDir = new File(mDir);
        if (!myDir.exists()) {
            myDir.mkdir();
        }

        fileName = mDir + System.currentTimeMillis() + ".3gp";
        mMediaRecorder=new MediaRecorder();

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setAudioSamplingRate(8000);
        mMediaRecorder.setOutputFile(fileName);

        mMediaRecorder.prepare();
        mMediaRecorder.start();

    }

    public void stop() {

        if (mMediaRecorder != null) {

            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public String getPath(){

        return  fileName;

    }

}
