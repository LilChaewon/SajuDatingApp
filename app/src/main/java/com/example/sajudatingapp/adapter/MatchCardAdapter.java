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

import com.bumptech.glide.Glide;
import com.example.sajudatingapp.R;
import com.example.sajudatingapp.model.CompatibilityResponse;
import com.example.sajudatingapp.model.User;
import com.example.sajudatingapp.network.ApiClient;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchCardAdapter extends RecyclerView.Adapter<MatchCardAdapter.MatchCardViewHolder> {

    private List<User> userList;

    public MatchCardAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void setUsers(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
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
            Glide.with(itemView.getContext())
                    .load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_profile) // a default placeholder
                    .into(profilePicture);

            nameAge.setText(String.format(Locale.getDefault(), "%s, %d세", user.getName(), user.getAge()));
            bio.setText(user.getBio());
            
            // Fetch compatibility score
            compatibilityScore.setText("궁합: 계산중...");
            ApiClient.getApiService().getCompatibility(user.getId()).enqueue(new Callback<CompatibilityResponse>() {
                @Override
                public void onResponse(Call<CompatibilityResponse> call, Response<CompatibilityResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int score = response.body().getCompatibilityScore();
                        compatibilityScore.setText(String.format(Locale.getDefault(), "궁합: %d점", score));
                    } else {
                        compatibilityScore.setText("궁합: 계산 실패");
                    }
                }

                @Override
                public void onFailure(Call<CompatibilityResponse> call, Throwable t) {
                    compatibilityScore.setText("궁합: 오류");
                }
            });


            requestChatButton.setOnClickListener(v -> {
                ApiClient.getApiService().requestChat(user.getId()).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(itemView.getContext(), "대화 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "대화 요청에 실패했습니다. (코드: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}
