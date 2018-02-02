package com.jkkc.gridmember.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkkc.gridmember.R;
import com.jkkc.gridmember.bean.LoginInfo;
import com.jkkc.gridmember.bean.ReturnRecordInfo;
import com.jkkc.gridmember.common.Config;
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

import java.util.List;

/**
 * Created by Guan on 2018/1/31.
 */

public class ReturnRecordListActivity extends AppCompatActivity {

    private static final String TAG = ReturnRecordListActivity.class.getSimpleName();

    private LoginInfo mLoginInfo;
    private List<ReturnRecordInfo.DataBean> mReturnDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_return_record_list);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        String user_info = PrefUtils.getString(getApplicationContext(), "user_info", null);
        final Gson gson = new Gson();
        mLoginInfo = gson.fromJson(user_info, LoginInfo.class);
        Log.d(TAG, mLoginInfo.getData().getToken() + "\n" + mLoginInfo.getData().getId());


        //回弹

        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
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

        FunGameHitBlockHeader funGameHitBlockHeader = new FunGameHitBlockHeader(this);
        //设置 Header 为 Material样式
        refreshLayout.setRefreshHeader(funGameHitBlockHeader);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this)
                .setSpinnerStyle(SpinnerStyle.Scale));



        OkGo.<String>post(Config.GRIDMAN_URL + Config.FIND_RETURN_VISIT_LIST)
                .tag(this)
                .params("token", mLoginInfo.getData().getToken())
                .params("gridMemberId", mLoginInfo.getData().getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String result = response.body().toString();
                        Log.d(TAG, result);
                        PrefUtils.setString(getApplicationContext(), "return_record_list", result);
                        Gson gson1 = new Gson();
                        ReturnRecordInfo returnRecordInfo = gson1.fromJson(result, ReturnRecordInfo.class);
                        mReturnDatas = returnRecordInfo.getData();

                        if (mReturnDatas != null) {
                            int size = mReturnDatas.size();
                            PrefUtils.setString(getApplicationContext(), "customer_visits", size + "");
                            mAdapter = new MyAdapter(mReturnDatas);
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int data) {

                                    /*Toast.makeText(getApplicationContext(),
                                            "" + data, Toast.LENGTH_SHORT).show();*/
                                    Intent intent = new Intent(getApplicationContext(),
                                            ReturnRecordDetailActivity.class);
                                    intent.putExtra("detail_position", data);

                                    startActivity(intent);


                                }
                            });


                        }


                    }

                });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
//创建并设置Adapter


    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
        public List<ReturnRecordInfo.DataBean> datas = null;

        public MyAdapter(List<ReturnRecordInfo.DataBean> datas) {
            this.datas = datas;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_old_address, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);

            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
//            viewHolder.mTextView.setText(datas[position]);
            viewHolder.mDateTime.setText(datas.get(position).getReturnVisitDate());
            viewHolder.mTvOldName.setText(datas.get(position).getName());
            viewHolder.mTvOldAddress.setText(datas.get(position).getAddress());

            //将数据保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(position);


        }


        //获取数据的数量
        @Override
        public int getItemCount() {

            return datas.size();

        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mDateTime;
            public TextView mTvOldName;
            public TextView mTvOldAddress;

            public ViewHolder(View view) {
                super(view);

                mDateTime = view.findViewById(R.id.tvDateTime);
                mTvOldName = view.findViewById(R.id.tvOldName);
                mTvOldAddress = view.findViewById(R.id.tvOldAddress);


            }
        }

        private OnRecyclerViewItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {

            this.mOnItemClickListener = listener;

        }

        @Override
        public void onClick(View view) {

            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(view, (int) view.getTag());
            }

        }


    }

    public interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, int data);

    }


    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private MyAdapter mAdapter;


}
