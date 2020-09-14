package com.bazukaa.chitchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.util.Constants;
import com.bazukaa.chitchat.util.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.act_sigup_tv_signin)
    TextView tvSignin;
    @BindView(R.id.act_signup_img_back)
    ImageView imgBack;
    @BindView(R.id.act_signup_et_input_first_name)
    EditText etFirstName;
    @BindView(R.id.act_signup_et_input_last_name)
    EditText etLastName;
    @BindView(R.id.act_signup_et_input_email)
    EditText etEmail;
    @BindView(R.id.act_signup_et_input_password)
    EditText etPassword;
    @BindView(R.id.act_signup_et_input_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.act_signup_btn_signup)
    MaterialButton btnSignUp;
    @BindView(R.id.act_signup_progress_bar_signup_progress)
    ProgressBar progressSignup;

    // To handle shared preferences
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        preferenceManager = new PreferenceManager(this);
    }

    @OnClick(R.id.act_sigup_tv_signin)
    public void onSigninClicked(){
        onBackPressed();
    }
    @OnClick(R.id.act_signup_img_back)
    public void onBackImgClicked(){
        onBackPressed();
    }
    @OnClick(R.id.act_signup_btn_signup)
    public void onSignUpBtnClicked(){
        if(etFirstName.getText().toString().isEmpty())
            Toast.makeText(this, "Enter First name", Toast.LENGTH_SHORT).show();
        else if(etLastName.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Last name", Toast.LENGTH_SHORT).show();
        else if(etEmail.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Email address", Toast.LENGTH_SHORT).show();
        else if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
        else if(etPassword.getText().toString().isEmpty())
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        else if(etConfirmPassword.getText().toString().isEmpty())
            Toast.makeText(this, "Confirm your password", Toast.LENGTH_SHORT).show();
        else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
            Toast.makeText(this, "Password & confirm password must be same", Toast.LENGTH_SHORT).show();
        else{
            signUp();
        }
    }
    private void signUp(){
        btnSignUp.setVisibility(View.INVISIBLE);
        progressSignup.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, etFirstName.getText().toString());
        user.put(Constants.KEY_LAST_NAME, etLastName.getText().toString());
        user.put(Constants.KEY_EMAIL, etEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, etPassword.getText().toString());
        db.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_FIRST_NAME, etFirstName.getText().toString());
                    preferenceManager.putString(Constants.KEY_LAST_NAME, etLastName.getText().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL, etEmail.getText().toString());
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    progressSignup.setVisibility(View.INVISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
