package com.jkkc.gridmember.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.bean.ReturnVisitList;
import com.jkkc.gridmember.bean.ReturnVisitListData;
import com.jkkc.gridmember.common.Config;
import com.jkkc.gridmember.ui.ReturnVisitRecordActivity;
import com.jkkc.gridmember.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.header.FunGameHitBlockHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.jkkc.gridmember.R.id.spinner;

/**
 * Created by Guan on 2018/1/19.
 */

public class ReturnVisitFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    private DatePickerDialog mDialog;
    private Spinner mSpinner;
    private EditText mEtName;
    private EditText mEtSelectDate;
    private Button mBtnSearch;
    private LoginInfo mLoginInfo;

    private static final String TAG = "ReturnVisitFragment";
    //回访数据列表
    private List<ReturnVisitListData> mDatas;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = View.inflate(getActivity(), R.layout.fragment_return_visit, null);

        mSpinner = view.findViewById(spinner);
        mEtName = view.findViewById(R.id.etName);
        mEtSelectDate = view.findViewById(R.id.etSelectDate);
        mBtnSearch = view.findViewById(R.id.btnSearch);

        String user_info = PrefUtils.getString(getActivity(), "user_info", "");
        Gson gson = new Gson();
        mLoginInfo = gson.fromJson(user_info, LoginInfo.class);


        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
            }
        });

        FunGameHitBlockHeader funGameHitBlockHeader = new FunGameHitBlockHeader(getActivity());
        //设置 Header 为 Material样式
        refreshLayout.setRefreshHeader(funGameHitBlockHeader);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        //回访列表
        OkGo.<String>post(Config.GRIDMAN_URL + Config.RETURN_VISIT_URL)
                .tag(this)
                .params("token", mLoginInfo.getData().getToken())
                .params("gridMemberId", mLoginInfo.getData().getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String result = response.body().toString();
//                        Log.d(TAG, result);
                        Gson gson1 = new Gson();
                        ReturnVisitList returnVisitList = gson1.fromJson(result, ReturnVisitList.class);
                        String data = returnVisitList.getData();
                        Log.d(TAG, data);

                        Gson gson2 = new Gson();
                        mDatas = gson2.fromJson(data, new TypeToken<List<ReturnVisitListData>>() {
                        }.getType());

                        if (mDatas != null) {

                            int size = mDatas.size();
                            PrefUtils.setString(getActivity(), "size", size + "");

                            mAdapter = new MyAdapter(mDatas);
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, String data) {

//                                    Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }

                });


        //数据
        data_list = new ArrayList<String>();
        data_list.add("北京");
        data_list.add("上海");
        data_list.add("广州");
        data_list.add("深圳");

        //适配器
        arr_adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSpinner.setAdapter(arr_adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String data = data_list.get(i);//从spinner中获取被选择的数据

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        Calendar calendar = Calendar.getInstance();

        mDialog = new DatePickerDialog(getActivity(), this,
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

                Toast.makeText(getActivity(), "开始搜索",
                        Toast.LENGTH_SHORT).show();

            }
        });


        mRecyclerView = view.findViewById(R.id.my_recycler_view);
//创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
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


        return view;

    }

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements
            View.OnClickListener {

        public List<ReturnVisitListData> datas = null;

        public MyAdapter(List<ReturnVisitListData> datas) {
            this.datas = datas;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_return_visit, viewGroup, false);


            ViewHolder vh = new ViewHolder(view);

            //将创建的View注册点击事件
            view.setOnClickListener(this);

            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//            viewHolder.mTextView.setText(datas.get(position));
            viewHolder.tvName.setText(datas.get(position).getName());
            viewHolder.tvHomeAddress.setText(datas.get(position).getAddress());
            String day = datas.get(position).day;
            if (TextUtils.isEmpty(day)) {
                viewHolder.tvDay.setText("还未回访");
            } else {
                viewHolder.tvDay.setText(day);
            }

            //将数据保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(datas.get(position).getName());

            viewHolder.ivfu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d(TAG, "oldId==" + datas.get(position).oldId);
                    Intent intent = new Intent(getActivity(), ReturnVisitRecordActivity.class);
                    intent.putExtra("oldName", datas.get(position).getName());
                    intent.putExtra("oldId", datas.get(position).oldId);
                    startActivity(intent);


                }
            });

            viewHolder.ivGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //驾车导航
                    new SweetAlertDialog(getActivity())
                            .setTitleText("导航?")
                            .setContentText(datas.get(position).getAddress())
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    Intent i1 = new Intent();
                                    // 地址解析
                                    i1.setData(Uri.parse("baidumap://map/geocoder?src=openApiDemo&" +
                                            "address="
                                            + mDatas.get(position).getAddress()));

                                    getActivity().startActivity(i1);


                                }
                            })
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();

                                }
                            })
                            .show();


                }
            });


            viewHolder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //打电话
                    new SweetAlertDialog(getActivity())
                            .setTitleText("拨号?")
                            .setContentText(datas.get(position).getPhone())
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    //调用Android系统API打电话
                                    Uri uri = Uri.parse("tel:" + datas.get(position).getPhone());
                                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                    startActivity(intent);

                                }
                            })
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();

                                }
                            })
                            .show();


                }
            });

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
            //            public TextView mTextView;
            public TextView tvName, tvHomeAddress, tvDay;
            public ImageView ivCall, ivGuide, ivfu;


            public ViewHolder(View view) {
                super(view);
//                mTextView = view.findViewById(R.id.tvCity);
                tvName = view.findViewById(R.id.tvName);
                tvHomeAddress = view.findViewById(R.id.tvHomeAddress);
                tvDay = view.findViewById(R.id.tvDay);
                ivCall = view.findViewById(R.id.ivCall);
                ivGuide = view.findViewById(R.id.ivGuide);
                ivfu = view.findViewById(R.id.ivfu);


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
