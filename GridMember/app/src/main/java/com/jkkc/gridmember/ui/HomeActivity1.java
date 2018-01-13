package com.jkkc.gridmember.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.jkkc.gridmember.R;
import com.tencent.bugly.Bugly;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guan on 2018/1/12.
 */

public class HomeActivity1 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = HomeActivity1.class.getSimpleName();

    @BindView(R.id.tvVersionCode)
    TextView mTvVersionCode;
    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.etSelectDate)
    EditText mEtSelectDate;
    @BindView(R.id.spinner)
    Spinner mSpinner;
    @BindView(R.id.dpDatePicker)
    DatePicker mDpDatePicker;
    @BindView(R.id.btnSearch)
    Button mBtnSearch;

    private DatePickerDialog mDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home1);
        ButterKnife.bind(this);

        String isDebugStr = AppUtils.isAppDebug() ? "内测版本" : "正式版本";

        mTvVersionCode.setText("当前版本：" + isDebugStr +
                AppUtils.getAppVersionName() + "." +
                AppUtils.getAppVersionCode());


        if (AppUtils.isAppDebug()) {

            //内测版本
            Bugly.init(getApplicationContext(), "8711747843", false);

        } else {
            //正式版本
            Bugly.init(getApplicationContext(), "001e1b77fe", false);


        }


        //数据
        data_list = new ArrayList<String>();
        data_list.add("北京");
        data_list.add("上海");
        data_list.add("广州");
        data_list.add("深圳");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSpinner.setAdapter(arr_adapter);

        Calendar calendar = Calendar.getInstance();

        mDialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        mEtSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.show();

            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"开始搜索",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;


    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

        String desc = String.format("%d年%d月%d日",
                year, monthOfYear + 1, dayOfMonth);
        //您选择的日期是
        mEtSelectDate.setText(desc);

    }
}

