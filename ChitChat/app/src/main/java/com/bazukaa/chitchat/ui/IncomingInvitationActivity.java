package com.bazukaa.chitchat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IncomingInvitationActivity extends AppCompatActivity {

    @BindView(R.id.act_meet_invite_incoming_img_type)
    ImageView incomingMeetType;
    @BindView(R.id.act_meet_invite_incoming_tv_first_char)
    TextView tvFirstChar;
    @BindView(R.id.act_meet_invite_incoming_tv_username)
    TextView tvUserName;
    @BindView(R.id.act_meet_invite_incoming_tv_email)
    TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitation);
        ButterKnife.bind(this);

        String meetingType = getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);
        if(meetingType != null && meetingType.equals("video")){
            incomingMeetType.setImageResource(R.drawable.ic_video);
        }

        String firstName = getIntent().getStringExtra(Constants.KEY_FIRST_NAME);
        String lastName = getIntent().getStringExtra(Constants.KEY_LAST_NAME);
        String email = getIntent().getStringExtra(Constants.KEY_EMAIL);

        if(firstName != null)
            tvFirstChar.setText(firstName.substring(0, 1));

        tvUserName.setText(firstName + " " + lastName);

        tvEmail.setText(email);

    }
}
