package com.jkkc.gridmember.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jkkc.gridmember.R;
import com.jkkc.gridmember.ui.MainActivity;


/**
 * Created by Guan on 2018/1/22.
 */

public class RefuseDialog extends Dialog {

    private Context mContext;
    private EditText mEtReason;
    private Button mBtnConfirm;

    public RefuseDialog(@NonNull Context context) {
        super(context);

        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_refuse);

        mEtReason = (EditText) findViewById(R.id.etReason);
        mBtnConfirm = (Button) findViewById(R.id.btnConfirm);

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
                mContext.startActivity(new Intent(mContext,
                        MainActivity.class));


            }
        });

    }
}
