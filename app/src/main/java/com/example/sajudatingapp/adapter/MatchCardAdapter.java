package com.example.sajudatingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.model.User;
import com.example.sajudatingapp.util.StubDataUtil;

import java.util.List;
import java.util.Locale;

public class MatchCardAdapter extends RecyclerView.Adapter<MatchCardAdapter.MatchCardViewHolder> {

    private List<User> userList;

    public MatchCardAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public MatchCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_card, parent, false);
        return new MatchCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchCardViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class MatchCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePicture;
        private TextView nameAge;
        private TextView bio;
        private TextView compatibilityScore;
        private Button requestChatButton;

        public MatchCardViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.iv_profile_picture);
            nameAge = itemView.findViewById(R.id.tv_name_age);
            bio = itemView.findViewById(R.id.tv_bio);
            compatibilityScore = itemView.findViewById(R.id.tv_compatibility_score);
            requestChatButton = itemView.findViewById(R.id.btn_request_chat);
        }

        void bind(User user) {
            // In a real app, you'd load the image with Glide or Picasso
            // profilePicture.setImageResource(...);

            nameAge.setText(String.format(Locale.getDefault(), "%s, %d세", user.getName(), user.getAge()));
            bio.setText(user.getBio());
            compatibilityScore.setText(String.format(Locale.getDefault(), "궁합: %d점", user.getCompatibilityScore()));

            requestChatButton.setOnClickListener(v -> {
                if (StubDataUtil.requestChat(user)) {
                    Toast.makeText(itemView.getContext(), "대화 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
