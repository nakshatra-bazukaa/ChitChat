package com.bazukaa.chitchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bazukaa.chitchat.R;
import com.bazukaa.chitchat.model.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
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

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvFirstChar, tvUserName, tvEmail;
        ImageView imgAudioMeet, imgVideoMeet;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFirstChar = itemView.findViewById(R.id.user_tv_first_char);
            tvUserName = itemView.findViewById(R.id.user_tv_user_name);
            tvEmail = itemView.findViewById(R.id.user_tv_email);
            imgAudioMeet = itemView.findViewById(R.id.user_img_audio_meet);
            imgVideoMeet = itemView.findViewById(R.id.user_img_video_meet);
        }

        void setUserData(User user){
            tvFirstChar.setText(user.firstName.substring(0, 1));
            tvUserName.setText(String.format("%s %s", user.firstName, user.lastName));
            tvEmail.setText(user.email);
        }
    }
}