package com.jkkc.gridmember;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.etPwd)
    EditText mEtPwd;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }


    @OnClick(R.id.btnLogin)
    public void onViewClicked(View view) {

        Snackbar.make(view, "忠义二字", Snackbar.LENGTH_SHORT).show();

    }
}
