package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.spark.swarajyabiz.Adapters.MyPagerAdapter;
import com.spark.swarajyabiz.MyFragments.MembersFragment;
import com.spark.swarajyabiz.MyFragments.PostsFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommInfo extends AppCompatActivity {
    private ViewPager viewPager;
    ImageView commImgx;

    TextView name,desc,mbrcnt;

    CardView invite,share;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_info);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout=findViewById(R.id.tabLayout);

        commImgx=findViewById(R.id.ImgComm);
        name=findViewById(R.id.nameComm);
        desc=findViewById(R.id.descComm);
        mbrcnt=findViewById(R.id.commMbr);
        invite=findViewById(R.id.inviteComm);
        share=findViewById(R.id.shareComm);

        Intent intent=getIntent();
        ArrayList<String> data=intent.getStringArrayListExtra("Data");
        String commId=data.get(0);
        String commName=data.get(1);
        String commAdmin=data.get(2);
        String commImg=data.get(3);
        String commDesc=data.get(4);
        String mbrCnt=data.get(5);


        Glide.with(this)
                .load(commImg)
                .into(commImgx);

        name.setText(commName);
        desc.setText(commDesc);
        mbrcnt.setText(mbrCnt + " Members");

        //Toast.makeText(this, ""+commId, Toast.LENGTH_SHORT).show();

        System.out.println("rsgfbdfb " +commId);
        Bundle bundle = new Bundle();
        bundle.putString("CommID", commId);
     //   Toast.makeText(this, ""+commId, Toast.LENGTH_SHORT).show();

        // Create the fragment and set arguments
        PostsFragment fragment1 = new PostsFragment();
        fragment1.setArguments(bundle);
        MembersFragment fragment2 = new MembersFragment();
        fragment2.setArguments(bundle);


        MyPagerAdapter adapter = new MyPagerAdapter(this,commId);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTabView = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
            TextView tabTextView = customTabView.findViewById(R.id.tabTextView);
            // Customize tab labels using a switch statement
            switch (position) {
                case 0:
                    tabTextView.setText("Posts");
                    tab.setCustomView(customTabView);
                    break;
                case 1:
                    tabTextView.setText("Members");
                    tab.setCustomView(customTabView);
                    break;
                // Add more cases as needed
            }
        }).attach();

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}