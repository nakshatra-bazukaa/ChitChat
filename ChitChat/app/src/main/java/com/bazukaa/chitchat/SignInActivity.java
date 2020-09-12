package com.bazukaa.chitchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.act_signin_tv_signup)
    TextView tvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.act_signin_tv_signup)
    public void onSignUpClicked(){
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }
}
