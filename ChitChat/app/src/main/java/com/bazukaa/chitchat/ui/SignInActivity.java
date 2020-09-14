package com.bazukaa.chitchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.util.Constants;
import com.bazukaa.chitchat.util.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.act_signin_tv_signup)
    TextView tvSignUp;
    @BindView(R.id.act_signin_et_input_email)
    EditText etEmail;
    @BindView(R.id.act_signin_et_input_password)
    EditText etPassword;
    @BindView(R.id.act_signin_btn_signin)
    MaterialButton btnSignIn;
    @BindView(R.id.act_signin_progressbar_progress_signin)
    ProgressBar progressSignIn;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        preferenceManager = new PreferenceManager(this);
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.act_signin_tv_signup)
    public void onSignUpClicked(){
        startActivity(new Intent(this, SignUpActivity.class));
    }
    @OnClick(R.id.act_signin_btn_signin)
    public void onSignInBtnClicked(){
        if(etEmail.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        else if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
            Toast.makeText(this, "Enter valid Email", Toast.LENGTH_SHORT).show();
        else  if(etPassword.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        else{
            signIn();
        }
    }

    private void signIn(){
        btnSignIn.setVisibility(View.INVISIBLE);
        progressSignIn.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, etEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, etPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        progressSignIn.setVisibility(View.INVISIBLE);
                        btnSignIn.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Unable to sign in", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
