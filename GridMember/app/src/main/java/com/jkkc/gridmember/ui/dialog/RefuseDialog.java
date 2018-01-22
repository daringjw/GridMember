package com.jkkc.gridmember.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jkkc.gridmember.R;

import butterknife.BindView;

/**
 * Created by Guan on 2018/1/22.
 */

public class RefuseDialog extends Dialog {


    @BindView(R.id.etReason)
    EditText mEtReason;
    @BindView(R.id.btnConfirm)
    Button mBtnConfirm;


    //定义回调事件，用于dialog的点击事件
    public interface OnRefuseDialogListener{

        public void confirm();

    }

    private OnRefuseDialogListener mOnRefuseDialogListener;

    private View.OnClickListener  mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mOnRefuseDialogListener.confirm();

            RefuseDialog.this.dismiss();

        }
    };



    public RefuseDialog(@NonNull Context context,OnRefuseDialogListener onRefuseDialogListener) {

        super(context);

        this.mOnRefuseDialogListener =onRefuseDialogListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_refuse);

        mBtnConfirm.setOnClickListener(mClickListener);


    }


}
