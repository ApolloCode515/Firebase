package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class JobPostDetails extends AppCompatActivity {

    TextView jobtitle, companyname, joblocation, workplace, jobpostedtime, jobdescription, jobtype;
    Button applybtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_details);

        jobtitle = findViewById(R.id.jobtitletextview);
        companyname = findViewById(R.id.jobcompanynametextview);
        jobtype = findViewById(R.id.jobtypetextview);
        joblocation = findViewById(R.id.locationtextview);
        workplace = findViewById(R.id.workplacetextview);
        jobpostedtime = findViewById(R.id.timetextview);
        jobdescription = findViewById(R.id.descriptiontextview);
        applybtn = findViewById(R.id.applybtn);


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

        jobtitle.setText(getIntent().getStringExtra("jobtitle"));
        companyname.setText(getIntent().getStringExtra("companyname"));
        joblocation.setText(getIntent().getStringExtra("joblocation"));
        jobtype.setText(getIntent().getStringExtra("jobtype"));
        workplace.setText(getIntent().getStringExtra("workplacetype"));
        jobdescription.setText(getIntent().getStringExtra("description"));
        jobpostedtime.setText(getIntent().getStringExtra("currentdate"));

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ApplicationProfile.class);
                startActivity(intent);
            }
        });
    }
}