package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import com.jkkc.gridmember.BuildConfig;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.manager.UserInfoManager;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Button mBtnRecord;
    private Button mBtnConfirmSubmit;
    private StringBuffer mSb;

    private int pgo;
    private LoginInfo mLoginInfo;
    private String mRecord;
    private String mToken;
    private int mGridMemberId;
    private String mImgPath;
    private String mOldId;


//    Bitmap对象保存味图片文件
    public void saveBitmapFile(Bitmap bitmap){

        File file=new File(mResults.get(pgo).trim());//将要保存图片的路径
        Log.d(TAG, file.length()+"压缩前");

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.d(TAG, file.length()+"压缩后");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_return_visit_record);

        mBtnRecord = (Button) findViewById(R.id.btnRecord);
        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), RecordActivity.class));

            }
        });

        String user_info = PrefUtils.getString(getApplicationContext(), "user_info", null);
        Gson gson = new Gson();
        mLoginInfo = gson.fromJson(user_info, LoginInfo.class);


        mBtnUpload = (Button) findViewById(R.id.btnUpload);

        mBtnConfirmSubmit = (Button) findViewById(R.id.btnConfirmSubmit);


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

        mOldId = getIntent().getStringExtra("oldId");


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

//                                LoginInfo.DataBean data = UserInfoManager.getInstance().getLoginInfo().getData();
//                                String token = data.getToken();

                                String token = mLoginInfo.getData().getToken();

                                mSb = new StringBuffer();

                                for (pgo = 0; pgo < mResults.size(); pgo++) {

                                    //压缩图片
                                    Bitmap bitmap = getimage(mResults.get(pgo).trim());
//                                    Bitmap comp = comp(bitmap);
                                    //将bitmap 保存在 file
                                    saveBitmapFile(bitmap);

                                    OkGo.<String>post(Config.GRIDMAN_URL + Config.UPLOADFILE_URL)//
                                            .tag(this)//
                                            //.isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                            //.params("param1", "paramValue1")        // 这里可以上传参数
                                            .params("uploadFile", new File(mResults.get(pgo).trim()))   // 可以添加文件上传
                                            //.params("file2", new File("filepath2"))     // 支持多文件同时添加上传
                                            .params("token", token)
//                                        .addFileParams(keyName, files)    // 这里支持一个key传多个文件
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {

                                                    Log.d(TAG, "Upload success");

                                                    mSb.append("," +
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

                                        mBtnConfirmSubmit.setVisibility(View.VISIBLE);


                                    }
                                }, 1000 * 3);


                            }


                        })

                        .show();


            }
        });


        mBtnConfirmSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "所有图片=" + mSb.toString());
                mRecord = PrefUtils.getString(getApplicationContext(), "record", null);
//                Log.d(TAG, mRecord);
                mToken = mLoginInfo.getData().getToken();
                mGridMemberId = mLoginInfo.getData().getId();
                //  imgPath    mSb.toString());
                // oldId    1
                // filePath



                if (!TextUtils.isEmpty(mSb.toString())) {
                    mImgPath = mSb.toString().substring(1, mSb.toString().length());
                    Log.d(TAG, mToken + "\n" + mGridMemberId + "\n" + mImgPath
                            + "\n" + 1 + "\n"
                            + (TextUtils.isEmpty(mRecord) ? "null" : mRecord)
                    );
                    new SweetAlertDialog(ReturnVisitRecordActivity.this)
                            .setTitleText("提交?")
                            .setContentText("录音和图片提交")
                            .setConfirmText("确定")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sDialog) {

                                    OkGo.<String>post(Config.GRIDMAN_URL + Config.ADD_RETURN_URL)
                                            .tag(ReturnVisitRecordActivity.class)
                                            .params("token", mToken)
                                            .params("gridMemberId", mGridMemberId)
                                            .params("imgPath", mImgPath)
                                            .params("oldId", mOldId)
                                            .params("filePath", (TextUtils.isEmpty(mRecord) ? "null" : mRecord))
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {

                                                    Log.d(TAG, "全部提交完毕" + response.body().toString());
                                                    Toast.makeText(getApplicationContext(), "全部提交完毕",
                                                            Toast.LENGTH_SHORT).show();

                                                    sDialog
                                                            .setTitleText("上传成功!")
                                                            .setContentText("提交成功")
                                                            .setConfirmText("确定")
                                                            .setCancelText("恭喜")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {


                                                                    startActivity(new Intent(getApplicationContext(),
                                                                            ReturnRecordListActivity.class));
                                                                    sweetAlertDialog.cancel();
                                                                    finish();


                                                                }
                                                            })

                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)


                                                    ;

                                                }


                                            });


                                }
                            })
                            .show();


                }else {


                    Toast.makeText(getApplicationContext(),"抱歉服务器有延时，请稍后",
                            Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
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

                        File file = new File(mPicStr0);
                        //打开指定的一张照片
                        //使用Intent
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                                BuildConfig.APPLICATION_ID + ".fileProvider", file);

//                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        intent.setDataAndType(contentUri, "image");
                        startActivity(intent);


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
