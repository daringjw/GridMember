package com.jkkc.gridmember.Record;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Guan on 2018/1/27.
 */

public class AudioPlayer {

    private MediaPlayer mMediaPlayer;
    private String playerPath;

    public AudioPlayer() {

    }

    public void setPlayerPath(String playerPath) {

        this.playerPath = playerPath;

    }

    public void play() {

        if (mMediaPlayer == null) {

            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(playerPath);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                        mMediaPlayer = null;

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    public void stop() {

        if (mMediaPlayer != null) {

            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;

        }


    }


}
