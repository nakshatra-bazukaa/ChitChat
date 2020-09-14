package com.bazukaa.chitchat.firebase;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.bazukaa.chitchat.ui.IncomingInvitationActivity;
import com.bazukaa.chitchat.util.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);

        if(type != null && type.equals(Constants.REMOTE_MSG_INVITATION)){
            Intent intent = new Intent(getApplicationContext(), IncomingInvitationActivity.class);
            intent.putExtra(
                    Constants.REMOTE_MSG_MEETING_TYPE,
                    remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_TYPE)
            );
            intent.putExtra(
                    Constants.KEY_FIRST_NAME,
                    remoteMessage.getData().get(Constants.KEY_FIRST_NAME)
            );
            intent.putExtra(
                    Constants.KEY_LAST_NAME,
                    remoteMessage.getData().get(Constants.KEY_LAST_NAME)
            );
            intent.putExtra(
                    Constants.KEY_EMAIL,
                    remoteMessage.getData().get(Constants.KEY_EMAIL)
            );
            intent.putExtra(
                    Constants.REMOTE_MSG_INVITER_TOKEN,
                    remoteMessage.getData().get(Constants.REMOTE_MSG_INVITER_TOKEN)
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
        }
    }
}
