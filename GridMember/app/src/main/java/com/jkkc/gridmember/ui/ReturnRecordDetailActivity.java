package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.Record.AudioPlayer;
import com.jkkc.gridmember.bean.ReturnRecordInfo;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guan on 2018/1/31.
 */

public class ReturnRecordDetailActivity extends AppCompatActivity {

    private static final String TAG = ReturnRecordDetailActivity.class.getSimpleName();
    private Intent mIntent;
    private List<ReturnRecordInfo.DataBean> mReturnDatas;
    private AudioPlayer mAudioPlayer;
    private ArrayList<String> mImgList;
    private ArrayList<String> mPicPathList;
    private SimpleDraweeView mDraweeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_return_record_detail);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        mIntent = getIntent();
        int detail_position = mIntent.getIntExtra("detail_position", 0);
        Log.d(TAG, detail_position + "");

        String result = PrefUtils.getString(getApplicationContext(), "return_record_list", null);
        if (!TextUtils.isEmpty(result)) {
            Gson gson1 = new Gson();
            ReturnRecordInfo returnRecordInfo = gson1.fromJson(result, ReturnRecordInfo.class);
            mReturnDatas = returnRecordInfo.getData();

            String imgPath = mReturnDatas.get(detail_position).getImgPath();
            Log.d(TAG, imgPath);
            String[] split = imgPath.split(",");
            mImgList = new ArrayList<>();

            mPicPathList = new ArrayList<>();

            for (int j = 0; j < split.length; j++) {
                if (!"".equals(split[j])) {
                    String ss = split[j];
                    String imgUrl = Config.GRIDMAN_URL + ss;
                    Log.d(TAG, imgUrl);

                    mImgList.add(imgUrl);


                }
            }


            Log.d(TAG, mImgList.size() + "张");


        }

        /**
         {
         "address": "吉林省长春市二道区鲁辉国际城六区10栋2单元504",
         "filePath": "/files/gridMemberApp/1c2593260cb9f71e8deb7b24cfc72f4c.3gp",
         "gridMemberId": 2,
         "gridMemberName": "陈双飞",
         "imgPath": "/files/gridMemberApp/7e47eec5fd562f484a8163d87afa0706.jpg",
         "name": "张桂霞",
         "phone": "13596448920",
         "returnVisitDate": "2018-01-31 17:17:16"
         }
         */
        final ReturnRecordInfo.DataBean dataBean = mReturnDatas.get(detail_position);
        TextView tvOldAddress = (TextView) findViewById(R.id.tvOldAddress);
        TextView tvOldName = (TextView) findViewById(R.id.tvOldName);
        TextView tvOldPhoneNumber = (TextView) findViewById(R.id.tvOldPhoneNumber);
        TextView tvReturnDateTime = (TextView) findViewById(R.id.tvReturnDateTime);
        tvOldAddress.setText(dataBean.getAddress());
        tvOldName.setText(dataBean.getName());
        tvOldPhoneNumber.setText(dataBean.getPhone());
        tvReturnDateTime.setText(dataBean.getReturnVisitDate());

        //下载录音文件 到本地，打开录音文件
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        Button btnStopPlay = (Button) findViewById(R.id.btnStopPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(dataBean.getFilePath()) &&
                        !dataBean.getFilePath().equals("null")) {
                    //播放
                    OkGo.<File>get(Config.GRIDMAN_URL + dataBean.getFilePath())
                            .tag(this)
                            .execute(new FileCallback() {

                                @Override
                                public void onSuccess(Response<File> response) {

                                    File file = response.body().getAbsoluteFile();
                                    String path = file.getAbsolutePath();
                                    if (mAudioPlayer == null) {
                                        mAudioPlayer = new AudioPlayer();
                                    }
                                    mAudioPlayer.setPlayerPath(path);
                                    mAudioPlayer.play();

                                }
                            });

                } else {

                    Toast.makeText(getApplicationContext(), "服务器没有录音文件",
                            Toast.LENGTH_SHORT).show();

                }


            }
        });

        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //停止播放
                if (mAudioPlayer != null) {
                    mAudioPlayer.stop();
                }


            }
        });


        Uri uri = Uri.parse(mImgList.get(0));
        mDraweeView = (SimpleDraweeView) findViewById(R.id.dvPic);
        mDraweeView.setImageURI(uri);

        mDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),
                        PicDetailActivity.class);
                intent.putStringArrayListExtra("imgList", mImgList);
                startActivity(intent);


            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //停止播放
        if (mAudioPlayer != null) {
            mAudioPlayer.stop();
        }


    }


}
