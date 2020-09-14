package com.bazukaa.chitchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.model.User;
import com.bazukaa.chitchat.network.ApiClient;
import com.bazukaa.chitchat.network.ApiService;
import com.bazukaa.chitchat.util.Constants;
import com.bazukaa.chitchat.util.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingInvitationActivity extends AppCompatActivity {

    @BindView(R.id.act_meet_invite_outgoing_img_type)
    ImageView imgMeetingType;
    @BindView(R.id.act_meet_invite_outgoing_tv_first_char)
    TextView tvFirstChar;
    @BindView(R.id.act_meet_invite_outgoing_tv_username)
    TextView tvUsername;
    @BindView(R.id.act_meet_invite_outgoing_tv_email)
    TextView tvEmail;
    @BindView(R.id.act_meet_invite_outgoing_img_stop)
    ImageView imgStopInvitation;

    private PreferenceManager preferenceManager;
    private String inviterToken;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);
        ButterKnife.bind(this);

        preferenceManager = new PreferenceManager(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null)
                inviterToken = task.getResult().getToken();
        });

        // Checking and setting meet type
        String meetingType = getIntent().getStringExtra("type");
        if(meetingType != null && meetingType.equals("video"))
            imgMeetingType.setImageResource(R.drawable.ic_video);

        // Extracting and putting user details
        user = (User) getIntent().getSerializableExtra("user");
        if(user != null){
            tvFirstChar.setText(user.firstName.substring(0, 1));
            tvUsername.setText(user.firstName + " " + user.lastName);
            tvEmail.setText(user.email);
        }

        if(meetingType != null && user != null)
            initiateMeeting(meetingType, user.token);
    }
    @OnClick(R.id.act_meet_invite_outgoing_img_stop)
    public void stopInvitationClicked(){
        if(user != null)
            cancelInvitation(user.token);
    }

    private void initiateMeeting(String meetingType, String receiverToken){
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MSG_MEETING_TYPE, meetingType);
            data.put(Constants.KEY_FIRST_NAME, preferenceManager.getString(Constants.KEY_FIRST_NAME));
            data.put(Constants.KEY_LAST_NAME, preferenceManager.getString(Constants.KEY_LAST_NAME));
            data.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void sendRemoteMessage(String remoteMessageBody, String type){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeader(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful())
                    if(type.equals(Constants.REMOTE_MSG_INVITATION))
                        Toast.makeText(OutgoingInvitationActivity.this, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
                    else if(type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)) {
                        Toast.makeText(OutgoingInvitationActivity.this, "Invitation cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                else{
                    Toast.makeText(OutgoingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(OutgoingInvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void cancelInvitation(String receiverToken){
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, Constants.REMOTE_MSG_INVITATION_CANCELLED);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION_RESPONSE);

        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}