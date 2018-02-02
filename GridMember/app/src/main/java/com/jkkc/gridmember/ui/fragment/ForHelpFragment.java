package com.jkkc.gridmember.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jkkc.gridmember.R;
import com.jkkc.gridmember.ui.BaiduMapActivity;
import com.jkkc.gridmember.ui.ReturnRecordListActivity;
import com.jkkc.gridmember.ui.TimingActivity;
import com.jkkc.gridmember.ui.YourLocationActivity;

/**
 * Created by Guan on 2018/1/19.
 */

public class ForHelpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_for_help, null);


        ImageView ivForHelp = view.findViewById(R.id.ivForHelp);
        ivForHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TimingActivity.class));
            }
        });


        ImageView ivOldPeoplePosition = view.findViewById(R.id.ivOldPeoplePosition);
        ivOldPeoplePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),
                        BaiduMapActivity.class));
            }
        });


        ImageView ivMyPosition = view.findViewById(R.id.ivMyPosition);
        ivMyPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),
                        YourLocationActivity.class));

            }
        });


        ImageView ivReturnVisitRecord = view.findViewById(R.id.ivReturnVisitRecord);
        ivReturnVisitRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),
                        ReturnRecordListActivity.class));

            }
        });

        return view;

    }


}
