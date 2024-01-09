package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PostInfo extends AppCompatActivity {

    String shopname, shopadd, shopImg, postDesc, postImg, contactkey, postType, postCate, postKeys;
    TextView postDec, bizName, bizAdd, postkeys;
    ImageView bizImg, postimg;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);

        bizName = findViewById(R.id.bizname);
        bizAdd = findViewById(R.id.bizadd);
        bizImg = findViewById(R.id.userImg);
        postDec = findViewById(R.id.postde);
        postimg = findViewById(R.id.postImg);
        postkeys = findViewById(R.id.postkays);


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

         contactkey = getIntent().getStringExtra("contactKey");
         postDesc = getIntent().getStringExtra("postDesc");
         postType = getIntent().getStringExtra("postType");
         postImg = getIntent().getStringExtra("postImg");
         postKeys = getIntent().getStringExtra("postKeys");
         postCate = getIntent().getStringExtra("postCate");
         shopname = getIntent().getStringExtra("shopname");
         shopImg = getIntent().getStringExtra("shopimagex");
         shopadd = getIntent().getStringExtra("shopaddress");


        Glide.with(this).load(shopImg).into(bizImg);
        Glide.with(this).load(postImg).into(postimg);
        bizName.setText(shopname);
        bizAdd.setText(shopadd);
        postDec.setText(postDesc);
        postkeys.setText(postKeys);

    }
}