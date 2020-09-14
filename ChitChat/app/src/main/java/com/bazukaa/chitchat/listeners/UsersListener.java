package com.bazukaa.chitchat.listeners;

import com.bazukaa.chitchat.model.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);
    void initiateAudioMeeting(User user);
    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}
