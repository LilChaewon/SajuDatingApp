package com.example.sajudatingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.adapter.MatchCardAdapter;
import com.example.sajudatingapp.model.User;
import com.example.sajudatingapp.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private ImageView leftPanel, rightPanel;
    private ViewPager2 viewPager;
    private MatchCardAdapter adapter;
    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.match_card_view_pager);
        leftPanel = view.findViewById(R.id.left_panel);
        rightPanel = view.findViewById(R.id.right_panel);

        adapter = new MatchCardAdapter(new ArrayList<>()); // Initialize with empty list
        viewPager.setAdapter(adapter);

        fetchMatches();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (!isFirstLoad) {
                    playFoldingScreenAnimation();
                }
                isFirstLoad = false;
            }
        });
    }

    private void fetchMatches() {
        ApiClient.getApiService().getMatches().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setUsers(response.body());
                } else {
                    Toast.makeText(getContext(), "추천 상대를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playFoldingScreenAnimation() {
        leftPanel.setVisibility(View.VISIBLE);
        rightPanel.setVisibility(View.VISIBLE);

        Animation slideOutLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        Animation slideOutRight = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);

        slideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                leftPanel.setVisibility(View.GONE);
                rightPanel.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        leftPanel.startAnimation(slideOutLeft);
        rightPanel.startAnimation(slideOutRight);
    }
}
