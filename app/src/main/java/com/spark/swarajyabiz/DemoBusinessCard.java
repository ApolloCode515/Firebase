package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DemoBusinessCard extends AppCompatActivity {

    Button demo1, demo2, demo3, demo4;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_business_card);

        demo1 = findViewById(R.id.demo1);
        demo2 = findViewById(R.id.demo2);
        demo3 = findViewById(R.id.demo3);
        demo4 = findViewById(R.id.demo4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        demo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Replace "https://www.example.com" with your desired URL
                String url = "https://www.kamdhanda.online/PARSHAV";

                Intent intent = new Intent(DemoBusinessCard.this, WebsiteView.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        demo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Replace "https://www.example.com" with your desired URL
                String url = "https://kamdhanda.online/Vaishu-Info";

                Intent intent = new Intent(DemoBusinessCard.this, WebsiteView.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        demo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Replace "https://www.example.com" with your desired URL
                String url = "https://kamdhanda.online/KAVERI-JWELLERS";

                Intent intent = new Intent(DemoBusinessCard.this, WebsiteView.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        demo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Replace "https://www.example.com" with your desired URL
                String url = "https://kamdhanda.online/Kavya-2";

                Intent intent = new Intent(DemoBusinessCard.this, WebsiteView.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }
}