package com.bazukaa.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazukaa.chitchat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.act_sigup_tv_signin)
    TextView tvSignin;

    @BindView(R.id.act_signup_img_back)
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }
    // To go to sign in activity
    @OnClick(R.id.act_sigup_tv_signin)
    public void onSigninClicked(){
        onBackPressed();
    }
    @OnClick(R.id.act_signup_img_back)
    public void onBackImgClicked(){
        onBackPressed();
    }
}
