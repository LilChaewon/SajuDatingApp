package com.example.sajudatingapp;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.sajudatingapp.fragment.FortuneDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FortuneDialogFragment.OnFortuneDialogDismissedListener {

    private LinearLayout foldingScreenLayout;
    private ImageView leftPanel;
    private ImageView rightPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foldingScreenLayout = findViewById(R.id.folding_screen_layout);
        leftPanel = findViewById(R.id.left_panel);
        rightPanel = findViewById(R.id.right_panel);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if (savedInstanceState == null) {
            // Keep the screen visible initially
            foldingScreenLayout.setVisibility(View.VISIBLE);
            FortuneDialogFragment dialog = new FortuneDialogFragment();
            dialog.show(getSupportFragmentManager(), "FortuneDialog");
        } else {
            // If restored, hide the animation screen immediately
            foldingScreenLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFortuneDialogDismissed() {
        Animation slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        slideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                foldingScreenLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        leftPanel.startAnimation(slideOutLeft);
        rightPanel.startAnimation(slideOutRight);
    }
}