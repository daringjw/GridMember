package com.jkkc.gridmember.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        Button btnHelp = view.findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), TimingActivity.class));

            }
        });


        Button btnLocation = view.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),
                        BaiduMapActivity.class));


            }
        });


        Button btnYourLocation = view.findViewById(R.id.btnYourLocation);
        btnYourLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),YourLocationActivity.class));

            }
        });


        Button btnReturnRecordList = view.findViewById(R.id.btnReturnRecordList);
        btnReturnRecordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ReturnRecordListActivity.class));
            }
        });

        return view;

    }


}
