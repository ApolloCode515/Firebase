package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.ui.Fragment_Business_post;

import java.util.ArrayList;
import java.util.List;

public class BusinessPosts extends AppCompatActivity implements BusinessPostAdapter.OnClickListener{

    RelativeLayout addpost;
    String userId, shopcontactNumber, shopName, shopimage, shopaddress, name;
    RecyclerView postrecyclerview;
    List<BusinessPost> businessPostList;
    BusinessPostAdapter businessPostAdapter;
    DatabaseReference postRef;
    private static final int REQUEST_ADD_POST = 1;
    RadioGroup radioGroup;
    ImageView back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_posts);

        addpost = findViewById(R.id.addpost);
        postrecyclerview = findViewById(R.id.postviews);

        radioGroup = findViewById(R.id.rdgrpx);
        back = findViewById(R.id.back);


        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

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

        shopcontactNumber = getIntent().getStringExtra("contactNumber");
        shopName = getIntent().getStringExtra("shopName");
        shopimage = getIntent().getStringExtra("shopimage");
        name = getIntent().getStringExtra("ownerName");
        shopaddress = getIntent().getStringExtra("shopaddress");

        postRef = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        businessPostList = new ArrayList<>();

        businessPostAdapter = new BusinessPostAdapter(businessPostList, this);
        postrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        postrecyclerview.setAdapter(businessPostAdapter);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), AddPost.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopimage", shopimage);
                intent.putExtra("ownerName", name);
                intent.putExtra("shopaddress", shopaddress);
                startActivityForResult(intent, REQUEST_ADD_POST);*/

                Intent intent = new Intent(getApplicationContext(), AddPostNew.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopimage", shopimage);
                intent.putExtra("ownerName", name);
                intent.putExtra("shopaddress", shopaddress);
                startActivity(intent);

            }
        });

        retrievepost();


        setDefaultSelection();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (null != rb) {
                    // checkedId is the RadioButton selected
                    Bundle bundle = new Bundle();

                    switch (i) {
                        case R.id.rdinvestors:
                            String btnname1 = "investors";
                            bundle.putString("selectedRadioButtonId", btnname1);
                            break;

                        case R.id.rdMarket:
                            String btnname2 = "Market";
                            bundle.putString("selectedRadioButtonId", btnname2);
                            break;

                        case R.id.rdtalent:
                            String btnname3 = "talent";
                            bundle.putString("selectedRadioButtonId", btnname3);
                            break;

                        case R.id.rdbranding:
                            String btnname4 = "branding";
                            bundle.putString("selectedRadioButtonId", btnname4);
                            break;

                        case R.id.rdbusihelp:
                            String btnname5 = "businesshelp";
                            bundle.putString("selectedRadioButtonId", btnname5);
                            break;
                    }

                    // Pass the bundle to the fragment
                    Fragment_Business_post fragment = new Fragment_Business_post();
                    bundle.putString("contactNumber", shopcontactNumber);
                    bundle.putString("shopName", shopName);
                    bundle.putString("shopimage", shopimage);
                    bundle.putString("ownerName", name);
                    bundle.putString("shopaddress", shopaddress);
                    fragment.setArguments(bundle);

                    // Load the fragment
                    loadFragment(fragment);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setDefaultSelection() {
        // Set default radio button and fragment
        RadioButton defaultRadioButton = findViewById(R.id.rdinvestors);
        defaultRadioButton.setChecked(true);

        // Set default fragment
        Bundle bundle = new Bundle();
        bundle.putString("selectedRadioButtonId", "rdinvestors");
        Fragment_Business_post defaultFragment = new Fragment_Business_post();
        defaultFragment.setArguments(bundle);
        loadFragment(defaultFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void retrievepost() {
        postRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    businessPostList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String postkey = dataSnapshot.getKey();
                        DatabaseReference postref = postRef.child(userId).child(postkey);
                        postref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String postDesc = snapshot.child("postDesc").getValue(String.class);
                                    String postImg = snapshot.child("postImg").getValue(String.class);
                                    String postKeys = snapshot.child("postKeys").getValue(String.class);
                                    String postType = snapshot.child("postType").getValue(String.class);
                                    String postCate = snapshot.child("postCate").getValue(String.class);

                                    BusinessPost businessPost = new BusinessPost(postkey, postType,  postImg,  shopName, shopimage , shopaddress,  postDesc,  postCate);
                                    businessPostList.add(businessPost);
                                }
                                businessPostAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    // Notify the adapter that the data set has changed
                    businessPostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_POST && resultCode == RESULT_OK) {
            // Reload the adapter or fetch the updated data
            retrievepost();
        }
    }

    @Override
    public void onClick(int position, View view, String postkey) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.delete_menu); // Create a menu resource file (delete_menu.xml) with delete option

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.post_Delete) {
                    // Add your Firebase delete logic here
                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("BusinessPosts").child(userId).child(postkey);
                    DatabaseReference postref = FirebaseDatabase.getInstance().getReference("Shop").child(userId).child("BusinessPosts").child(postkey);

                    Task<Void> removePostTask = postRef.removeValue();
                    Task<Void> removeShopPostTask = postref.removeValue();

                    Tasks.whenAllComplete(removePostTask, removeShopPostTask).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Task<?>>> task) {
                            if (task.isSuccessful()) {
                                // The post is successfully deleted from Firebase
                                businessPostList.remove(position);
                                businessPostAdapter.notifyItemRemoved(position);
                                Toast.makeText(BusinessPosts.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle the error
                                Toast.makeText(BusinessPosts.this, "Failed to delete post", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish(); // Finish the current activity
    }
}