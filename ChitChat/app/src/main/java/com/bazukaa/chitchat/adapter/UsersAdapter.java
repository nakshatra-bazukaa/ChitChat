package com.bazukaa.chitchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.listeners.UsersListener;
import com.bazukaa.chitchat.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;
    private UsersListener usersListener;
    private List<User> selectedUsers;

    public UsersAdapter(List<User> users, UsersListener usersListener) {
        this.users = users;
        this.usersListener = usersListener;
        selectedUsers = new ArrayList<>();
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvFirstChar, tvUserName, tvEmail;
        ImageView imgAudioMeet, imgVideoMeet;
        ConstraintLayout userContainer;
        ImageView imgSelected;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFirstChar = itemView.findViewById(R.id.user_tv_first_char);
            tvUserName = itemView.findViewById(R.id.user_tv_user_name);
            tvEmail = itemView.findViewById(R.id.user_tv_email);
            imgAudioMeet = itemView.findViewById(R.id.user_img_audio_meet);
            imgVideoMeet = itemView.findViewById(R.id.user_img_video_meet);
            userContainer = itemView.findViewById(R.id.user_container);
            imgSelected = itemView.findViewById(R.id.user_img_selected);
        }

        void setUserData(User user){
            tvFirstChar.setText(user.firstName.substring(0, 1));
            tvUserName.setText(String.format("%s %s", user.firstName, user.lastName));
            tvEmail.setText(user.email);
            // Initiate audio call
            imgAudioMeet.setOnClickListener(v -> { usersListener.initiateAudioMeeting(user); });
            // Initiate video call
            imgVideoMeet.setOnClickListener(v -> usersListener.initiateVideoMeeting(user));
            userContainer.setOnLongClickListener(v -> {
                if(imgSelected.getVisibility() != View.VISIBLE){
                    selectedUsers.add(user);
                    imgSelected.setVisibility(View.VISIBLE);
                    imgVideoMeet.setVisibility(View.GONE);
                    imgAudioMeet.setVisibility(View.GONE);
                    usersListener.onMultipleUsersAction(true);
                }
                return true;
            });
            userContainer.setOnClickListener(v -> {
                if(imgSelected.getVisibility() == View.VISIBLE){
                    selectedUsers.remove(user);
                    imgSelected.setVisibility(View.GONE);
                    imgVideoMeet.setVisibility(View.VISIBLE);
                    imgAudioMeet.setVisibility(View.VISIBLE);
                    if(selectedUsers.size() == 0)
                        usersListener.onMultipleUsersAction(false);
                }else{
                    if(selectedUsers.size() > 0){
                        selectedUsers.add(user);
                        imgSelected.setVisibility(View.VISIBLE);
                        imgVideoMeet.setVisibility(View.GONE);
                        imgAudioMeet.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
