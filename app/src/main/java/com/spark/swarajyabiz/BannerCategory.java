package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.spark.swarajyabiz.ui.FragmentBusiness;
import com.spark.swarajyabiz.ui.FragmentFestivals;
import com.spark.swarajyabiz.ui.FragmentGreeting;
import com.spark.swarajyabiz.ui.FragmentThoughts;

public class BannerCategory extends AppCompatActivity {

    TextView titletextview;
    String titletext;
    ImageView back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_category);

        titletextview = findViewById(R.id.titletext);
        back = findViewById(R.id.back);

        titletext = getIntent().getStringExtra("titletext");
        titletextview.setText(titletext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

// Load the appropriate fragment based on the titletext
        if ("Festival".equalsIgnoreCase(titletext)) {
            loadFragment(new FragmentFestivals());
        } else if ("greeting".equalsIgnoreCase(titletext)) {
            loadFragment(new FragmentGreeting());
        }else if ("Thoughts".equalsIgnoreCase(titletext)) {
            loadFragment(new FragmentThoughts());
        }else if ("Business".equalsIgnoreCase(titletext)) {
            loadFragment(new FragmentBusiness());
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish(); // Finish the current activity
    }

}