package com.example.sajudatingapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Import Toolbar
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.sajudatingapp.fragment.FortuneDialogFragment;
import com.example.sajudatingapp.util.NotificationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FortuneDialogFragment.OnFortuneDialogDismissedListener {

    private LinearLayout foldingScreenLayout;
    private ImageView leftPanel;
    private ImageView rightPanel;
    private Toolbar mainToolbar; // Declare Toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        NotificationHelper.createNotificationChannel(this);

        mainToolbar = findViewById(R.id.main_toolbar); // Initialize Toolbar
        setSupportActionBar(mainToolbar); // Set Toolbar as ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("사주 데이팅 앱"); // Set title for the main activity
        }

        foldingScreenLayout = findViewById(R.id.folding_screen_layout);
        leftPanel = findViewById(R.id.left_panel);
        rightPanel = findViewById(R.id.right_panel);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // UI 확인용: folding screen과 dialog 임시로 완전히 숨김
        foldingScreenLayout.setVisibility(View.GONE);
        
        // FortuneDialog는 나중에 활성화
        // if (savedInstanceState == null) {
        //     // Keep the screen visible initially
        //     foldingScreenLayout.setVisibility(View.VISIBLE);
        //     FortuneDialogFragment dialog = new FortuneDialogFragment();
        //     dialog.show(getSupportFragmentManager(), "FortuneDialog");
        // } else {
        //     // If restored, hide the animation screen immediately
        //     foldingScreenLayout.setVisibility(View.GONE);
        // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Inflate the main menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_show_fortune) {
            FortuneDialogFragment fortuneDialog = new FortuneDialogFragment();
            fortuneDialog.show(getSupportFragmentManager(), "fortune_dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
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