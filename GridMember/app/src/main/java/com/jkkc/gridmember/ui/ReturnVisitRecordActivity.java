package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jkkc.gridmember.R;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_return_visit_record);

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




                draweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /*File file = new File(mPicStr0);
                        //打开指定的一张照片
                        //使用Intent
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "image");
                        startActivity(intent);*/
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
                                    public void onClick(SweetAlertDialog sDialog) {



                                        sDialog
                                                .setTitleText("上传成功!")
                                                .setContentText("以上路径的图片已经全部上传平台!")
                                                .setConfirmText("确定")
                                                .setCancelText("恭喜您")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    }
                                })
                                .show();


                    }
                });


            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }


}
