package com.jkkc.gridmember.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import static com.jkkc.gridmember.R.id.spinner;

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
    @BindView(spinner)
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

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String data = data_list.get(i);//从spinner中获取被选择的数据
                Toast.makeText(getApplicationContext(), "选中" + data, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


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

                Toast.makeText(getApplicationContext(), "开始搜索",
                        Toast.LENGTH_SHORT).show();

            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
//创建并设置Adapter
        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("条目1");
        itemList.add("条目2");
        itemList.add("条目3");
        itemList.add("条目4");
        itemList.add("条目5");
        itemList.add("条目6");

        mAdapter = new MyAdapter(itemList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

                Toast.makeText(getApplicationContext(), data,
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements
            View.OnClickListener {

        public List<String> datas = null;

        public MyAdapter(List<String> datas) {
            this.datas = datas;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);

            //将创建的View注册点击事件
            view.setOnClickListener(this);

            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.mTextView.setText(datas.get(position));

            //将数据保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(datas.get(position));

        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }


        @Override
        public void onClick(View view) {

            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(view, (String) view.getTag());
            }

        }


        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mTextView = view.findViewById(R.id.tvCity);
            }
        }


        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {

            this.mOnItemClickListener = listener;

        }

    }

    public interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, String data);

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

