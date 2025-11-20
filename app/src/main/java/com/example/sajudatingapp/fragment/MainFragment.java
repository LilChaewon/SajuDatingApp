package com.example.sajudatingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.adapter.MatchCardAdapter;
import com.example.sajudatingapp.model.User;
import com.example.sajudatingapp.util.StubDataUtil;

import java.util.List;

public class MainFragment extends Fragment {

    private ImageView leftPanel, rightPanel;
    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.match_card_view_pager);
        leftPanel = view.findViewById(R.id.left_panel);
        rightPanel = view.findViewById(R.id.right_panel);

        List<User> userList = StubDataUtil.getTodayMatchCandidates();
        MatchCardAdapter adapter = new MatchCardAdapter(userList);
        viewPager.setAdapter(adapter);

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
