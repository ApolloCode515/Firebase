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

import com.google.firebase.database.DatabaseReference;
import com.spark.swarajyabiz.ui.Fragment_Business_Click_Event;
import com.spark.swarajyabiz.ui.Fragment_Festival_Event_Click;
import com.spark.swarajyabiz.ui.Fragment_Thoughts_Click_Event;

public class BannerDetails extends AppCompatActivity {

    TextView titletextview, titletextview1, titletextview3;
    String titletextthoughts, titletextbusiness, titletextfestival,
            shopName, shopcontactNumber, shopownername, shopimage,shopaddress, bannerimage;
    DatabaseReference Thoughtsref;
    ImageView back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_details);

        titletextview = findViewById(R.id.titletextthoughts);
        titletextview1 = findViewById(R.id.titletextbusiness);
        titletextview3 = findViewById(R.id.titletextfestival);

        back = findViewById(R.id.back);
        titletextthoughts = getIntent().getStringExtra("THOUGHTS_NAME");
        titletextfestival = getIntent().getStringExtra("FESTIVAL_NAME");
        bannerimage = getIntent().getStringExtra("IMAGE_URL");
        shopcontactNumber = getIntent().getStringExtra("contactNumber");
        shopName = getIntent().getStringExtra("shopName");
        shopimage = getIntent().getStringExtra("shopimage");
        shopownername = getIntent().getStringExtra("ownerName");
        shopaddress = getIntent().getStringExtra("shopaddress");
        System.out.println("cv z " +shopownername);
        titletextbusiness = getIntent().getStringExtra("BUSINESS_NAME");


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

// Load the appropriate fragment based on the titletext

        if (titletextthoughts != null) {
            Bundle bundle = new Bundle();
            bundle.putString("TITLE_TEXT", titletextthoughts);
            bundle.putString("contactNumber",shopcontactNumber);
            bundle.putString("shopName", shopName);
            bundle.putString("shopimage", shopimage);
            bundle.putString("ownerName", shopownername);
            bundle.putString("shopaddress", shopaddress);
            bundle.putString("BANNER_IMAGE_URL", bannerimage);

            System.out.println("fdsv " +bannerimage);
            System.out.println("sdgvb " +shopcontactNumber);
            System.out.println("fdsv " +shopownername);

            Fragment_Thoughts_Click_Event fragment = new Fragment_Thoughts_Click_Event();
            fragment.setArguments(bundle);

            titletextview.setText(titletextthoughts);
            loadFragment(fragment);
            titletextview1.setVisibility(View.GONE);
            titletextview3.setVisibility(View.GONE);
        } else if (titletextbusiness != null){
            Bundle bundle = new Bundle();
            bundle.putString("TITLE_TEXT", titletextbusiness);
            bundle.putString("contactNumber",shopcontactNumber);
            bundle.putString("shopName", shopName);
            bundle.putString("shopimage", shopimage);
            bundle.putString("ownerName", shopownername);
            bundle.putString("shopaddress", shopaddress);
            bundle.putString("BANNER_IMAGE_URL", bannerimage);
            System.out.println("fdsv " +bannerimage);

            Fragment_Business_Click_Event fragment = new Fragment_Business_Click_Event();
            fragment.setArguments(bundle);

            titletextview1.setText(titletextbusiness);
            loadFragment(fragment);
            titletextview.setVisibility(View.GONE);
            titletextview3.setVisibility(View.GONE);

        }else if (titletextfestival != null) {
            Bundle bundle = new Bundle();
            bundle.putString("TITLE_TEXT", titletextfestival);
            bundle.putString("contactNumber",shopcontactNumber);
            bundle.putString("shopName", shopName);
            bundle.putString("shopimage", shopimage);
            bundle.putString("ownerName", shopownername);
            bundle.putString("shopaddress", shopaddress);
            bundle.putString("BANNER_IMAGE_URL", bannerimage);

            System.out.println("fdsv " +titletextfestival);
            System.out.println("sdgvb " +shopcontactNumber);
            System.out.println("fdsv " +shopownername);

            Fragment_Festival_Event_Click fragment = new Fragment_Festival_Event_Click();
            fragment.setArguments(bundle);

            titletextview3.setText(titletextfestival);
            loadFragment(fragment);
            titletextview.setVisibility(View.GONE);
            titletextview1.setVisibility(View.GONE);
        }

//        } else if ("प्रेरणादायी सुविचार".equalsIgnoreCase(titletext)) {
//            loadFragment(new Fragment_Suvichar());
//        }else if ("महापुरुष विचारधारा".equalsIgnoreCase(titletext)) {
//            loadFragment(new Fragment_MahaPurushVichar());
//        }else if ("शुभ रात्री".equalsIgnoreCase(titletext)) {
//            loadFragment(new Fragment_GoodNight());
//        }else if ("शुभ सकाळ".equalsIgnoreCase(titletext)) {
//            loadFragment(new Fragment_GoodMorning());
//        }

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