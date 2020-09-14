package com.bazukaa.chitchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.adapter.UsersAdapter;
import com.bazukaa.chitchat.model.User;
import com.bazukaa.chitchat.util.Constants;
import com.bazukaa.chitchat.util.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.act_main_tv_title)
    TextView tvTitle;
    @BindView(R.id.act_main_rv_users)
    RecyclerView usersRV;
    @BindView(R.id.act_main_tv_error_message)
    TextView tvErrorMessage;
    @BindView(R.id.act_main_progress_bar_users)
    ProgressBar usersProgressBar;

    private PreferenceManager preferenceManager;
    private List<User> users;
    private UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // To set user name on the header
        preferenceManager = new PreferenceManager(this);
        tvTitle.setText(String.format(
                "%s %s",
                preferenceManager.getString(Constants.KEY_FIRST_NAME),
                preferenceManager.getString(Constants.KEY_LAST_NAME)
        ));

        // To update fcm token in db
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null)
                sendFCMTokenToDatabase(task.getResult().getToken());
        });

        // Users Rv setup
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users);
        usersRV.setAdapter(usersAdapter);
        getUsers();

    }
    // Get users list from firestore
    private void getUsers(){
        usersProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    usersProgressBar.setVisibility(View.GONE);
                    String myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            if(myUserId.equals(documentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);
                        }
                        if(users.size() > 0){
                            usersAdapter.notifyDataSetChanged();
                        }else{
                            tvErrorMessage.setText("No Users Available");
                            tvErrorMessage.setVisibility(View.VISIBLE);
                        }
                    }else{
                        tvErrorMessage.setText("No Users Available");
                        tvErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }
    // TO sign out
    @OnClick(R.id.act_main_tv_signout)
    public void onSignOutClicked(){
        signOut();
    }
    private void sendFCMTokenToDatabase(String token){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to send token" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void signOut(){
        Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }
}
