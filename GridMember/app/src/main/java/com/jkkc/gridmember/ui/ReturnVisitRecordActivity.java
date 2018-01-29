package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.manager.UserInfoManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Guan on 2018/1/26.
 * <p>
 * 回访记录详情
 */

public class ReturnVisitRecordActivity extends AppCompatActivity {

    private static final String TAG = ReturnVisitRecordActivity.class.getSimpleName();
    private Button mBtnTakePic;

    // class variables
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();
    private TextView mTvPicDir;
    private String mPicStr0;
    private Button mBtnUpload;
    private ArrayList<File> mFiles;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_return_visit_record);

        mBtnUpload = (Button) findViewById(R.id.btnUpload);


        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        String oldName = getIntent().getStringExtra("oldName");
        TextView tvOldName = (TextView) findViewById(R.id.tvOldName);
        tvOldName.setText(oldName);

        mBtnTakePic = (Button) findViewById(R.id.btnTakePic);
        mBtnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


// start multiple photos selector
                Intent intent = new Intent(getApplicationContext(), ImagesSelectorActivity.class);
// max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
// min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
                startActivityForResult(intent, REQUEST_CODE);


            }
        });

        mTvPicDir = (TextView) findViewById(R.id.tvPicDir);


        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //上传文件到服务器
                new SweetAlertDialog(ReturnVisitRecordActivity.this)
                        .setTitleText("图片上传到平台?")
                        .setContentText("是否将以上路径的图片上传到平台")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.cancel();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {

                                //上传图片
                                sDialog
                                        .setTitleText("开始上传")
                                        .setContentText("正在上传...")
                                        .setConfirmText("确定")
                                        .setCancelText("请稍后")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

                                LoginInfo.DataBean data = UserInfoManager.getInstance().getLoginInfo().getData();
                                String token = data.getToken();

                                for (int i = 0; i < mResults.size(); i++) {


                                    final int finalI = i;
                                    OkGo.<String>post(Config.GRIDMAN_URL + Config.UPLOADFILE_URL)//
                                            .tag(this)//
                                            //.isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                            //.params("param1", "paramValue1")        // 这里可以上传参数
                                            .params("uploadFile", new File(mResults.get(i).trim()))   // 可以添加文件上传
                                            //.params("file2", new File("filepath2"))     // 支持多文件同时添加上传
                                            .params("token", token)
//                                        .addFileParams(keyName, files)    // 这里支持一个key传多个文件
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {

                                                    Log.d(TAG, "Upload success");
                                                    Log.d(TAG, "第" + finalI + "张图片地址：" +
                                                            Config.GRIDMAN_URL +
                                                            response.body().toString());


                                                }


                                            });

                                }



                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sDialog
                                                .setTitleText("上传成功!")
                                                .setContentText("以上路径的图片已经全部上传平台!")
                                                .setConfirmText("确定")
                                                .setCancelText("恭喜您")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                },3000);


                               /* mFiles = new ArrayList<>();
//                                        File file = new File(mResults.get(0).trim());
//                                        files.add(file);
                                for (int i = 0; i < mResults.size(); i++) {

                                    mFiles.add(new File(mResults.get(i).trim()));

                                }

                                uploadFiles(Config.GRIDMAN_URL + Config.UPLOADFILE_URL,
                                        "uploadFile", mFiles, sDialog);*/


                            }


                        })

                        .show();


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;


                // show results in textview
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("", mResults.size())).append("\n");
                for (String result : mResults) {
                    sb.append(result).append("\n");
                }

                mTvPicDir.setText(sb.toString().trim());

                mPicStr0 = mResults.get(0).trim();
                Log.d(TAG, mPicStr0);
                Uri uri = Uri.parse("file://" + mPicStr0);
                SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
                draweeView.setImageURI(uri);

                mBtnUpload.setVisibility(View.VISIBLE);

                draweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       /* File file = new File(mPicStr0);
                        //打开指定的一张照片
                        //使用Intent
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "image");
                        startActivity(intent);*/


                    }
                });


            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }


    /**
     * 多文件上传
     *
     * @param url
     * @param keyName
     * @param files   文件集合
     */
    private void uploadFiles(String url, String keyName, List<File> files, final SweetAlertDialog sDialog) {

        sDialog
                .setTitleText("开始上传")
                .setContentText("正在上传...")
                .setConfirmText("确定")
                .setCancelText("请稍后")
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

        LoginInfo.DataBean data = UserInfoManager.getInstance().getLoginInfo().getData();
        String token = data.getToken();

        OkGo.<String>post(url)//
                .tag(this)//
                //.isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                //.params("param1", "paramValue1")        // 这里可以上传参数
                //.params("file1", new File("filepath1"))   // 可以添加文件上传
                //.params("file2", new File("filepath2"))     // 支持多文件同时添加上传
                .params("token", token)
                .addFileParams(keyName, files)    // 这里支持一个key传多个文件
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {


                        Log.d(TAG, "Upload success");
                        sDialog
                                .setTitleText("上传成功!")
                                .setContentText("以上路径的图片已经全部上传平台!")
                                .setConfirmText("确定")
                                .setCancelText("恭喜您")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        Log.d(TAG, response.body().toString());


                    }


                });


    }


}
