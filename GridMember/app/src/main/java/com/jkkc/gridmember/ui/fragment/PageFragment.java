package com.jkkc.gridmember.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jkkc.gridmember.R;

import java.util.ArrayList;

/**
 * Created by Guan on 2018/1/19.
 */

public class PageFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    public static final String TAG = "PageFragment";

    private int mPage;
    private ArrayList<String> mImgList;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        mImgList = getActivity().getIntent().getStringArrayListExtra("imgList");
        Log.d(TAG, mPage + "");
        Uri uri = Uri.parse(mImgList.get(mPage - 1));
        SimpleDraweeView draweeView = view.findViewById(R.id.sdvPic);
        draweeView.setImageURI(uri);


        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, mImgList.get(mPage - 1));

                Intent intent = new Intent();
                //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(mImgList.get(mPage - 1));
                intent.setData(content_url);
                startActivity(intent);


            }
        });


        return view;


    }
}
