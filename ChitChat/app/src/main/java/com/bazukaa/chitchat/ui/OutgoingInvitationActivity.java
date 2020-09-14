package com.bazukaa.chitchat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);
        ButterKnife.bind(this);

        // Checking and setting meet type
        String meetingType = getIntent().getStringExtra("type");
        if(meetingType != null && meetingType.equals("video"))
            imgMeetingType.setImageResource(R.drawable.ic_video);

        // Extracting and putting user details
        User user = (User) getIntent().getSerializableExtra("user");
        if(user != null){
            tvFirstChar.setText(user.firstName.substring(0, 1));
            tvUsername.setText(user.firstName + " " + user.lastName);
            tvEmail.setText(user.email);
        }
    }
    @OnClick(R.id.act_meet_invite_outgoing_img_stop)
    public void stopInvitationClicked(){ onBackPressed(); }
}
