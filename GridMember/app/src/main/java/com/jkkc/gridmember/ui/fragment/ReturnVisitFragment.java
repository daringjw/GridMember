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
import com.jkkc.gridmember.ui.HomeActivity1;

/**
 * Created by Guan on 2018/1/19.
 */

public class ReturnVisitFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_return_visit, null);

        Button btnReturnVisit = view.findViewById(R.id.btnReturnVisit);
        btnReturnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), HomeActivity1.class));


            }
        });


        return view;

    }
}
