package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.MyFragments.PostsFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class CommInfoGlobal extends AppCompatActivity implements HomeMultiAdapter.OnViewDetailsClickListener {

    RecyclerView postView;
    DatabaseReference userRef, shopRef;
    String shopcontactNumber, taluka,address, shopName, shopimage, destrict;

    String userId, jobTitle, companyname, joblocation, jobtype, description, workplacetype, currentdate,
            postcontactNumber, jobid, experience, skills, salary, jobopenings;
    String shopname, premium, postImg, postDesc,postType ,postKeys, postCate, contactkey,shopimagex,shopaddress;
    HomeMultiAdapter homeMultiAdapter;
    List<Object> homeItemList=new ArrayList<>();
    private LottieAnimationView lottieAnimationView;

    int x = 0;
    static String dd;

    ImageView commImgx;

    TextView name,desc,mbrcnt;

    CardView invite,share;

    String commLinks;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_info_global);

        postView=findViewById(R.id.globalpostview);

        commImgx=findViewById(R.id.ImgComm1);
        name=findViewById(R.id.nameComm1);
        desc=findViewById(R.id.descComm1);
        mbrcnt=findViewById(R.id.commMbr1);
        invite=findViewById(R.id.joincomm1);
        share=findViewById(R.id.shareComm1);

        Intent intent=getIntent();
        ArrayList<String> data=intent.getStringArrayListExtra("Data");
        String commId=data.get(0);
        String commName=data.get(1);
        String commAdmin=data.get(2);
        String commImg=data.get(3);
        String commDesc=data.get(4);
        String mbrCnt=data.get(5);
        commLinks=data.get(6);


        Glide.with(this)
                .load(commImg)
                .into(commImgx);

        name.setText(commName);
        desc.setText(commDesc);
        mbrcnt.setText(mbrCnt + " Members");

       // userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        // Initialize with -1 to start from the first image
        SharedPreferences sharedPreference = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
           // userRef.child(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        // Toast.makeText(getContext(), " "+dd, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, ""+commId, Toast.LENGTH_SHORT).show();

        LoadHomeDataNewTestSSS("8c108aa5ff");

       // DataLoad(commId);

   }

    private void ClearAllHome(){
        if(homeItemList != null){
            homeItemList.clear();

            if(homeMultiAdapter!=null){
                homeMultiAdapter.notifyDataSetChanged();
            }
        }
        homeItemList=new ArrayList<>();
    }

    public void LoadHomeDataNewTestSSS(String targetCommunityId) {
        ClearAllHome();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x = 0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();
                        Log.d("fdsfds","step1  "+contactNumber);
                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String status = keySnapshot.child("status").getValue(String.class);
                                        Log.d("fdsfds","step2  "+status);
                                        if (!status.equals("Rejected")) {
                                            String commIds = keySnapshot.child("Comm").getValue(String.class);
                                            if (commIds != null){
                                                String[] commArray = commIds.split("&&");
                                                for (String commId : commArray) {
                                                    if (commId.equals(targetCommunityId)) {
                                                        Log.d("fdsfds","step3  "+commId);
                                                        // Fetch post details based on commId
                                                        System.out.println("Processing post for community ID: " + commId);
                                                        // Your existing code for fetching post details

                                                        shopname = snapshot.child("shopName").getValue(String.class);
                                                        shopimagex = snapshot.child("url").getValue(String.class);
                                                        shopaddress = snapshot.child("address").getValue(String.class);

                                                        String postImg = keySnapshot.child("postImg").getValue(String.class);
                                                        String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                                        String postType = keySnapshot.child("postType").getValue(String.class);
                                                        String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                                        String postCate = keySnapshot.child("postCate").getValue(String.class);
                                                        String servArea = keySnapshot.child("servingArea").getValue(String.class);
                                                        String visibilityCount = keySnapshot.child("visibilityCount").getValue(String.class);
                                                        String clickCount = keySnapshot.child("clickCount").getValue(String.class);

                                                        PostModel postModel = new PostModel();
                                                        postModel.setPostId(key);
                                                        postModel.setPostDesc(postDesc);
                                                        postModel.setPostType(postType);
                                                        postModel.setPostImg(postImg);
                                                        postModel.setPostKeys(postKeys);
                                                        postModel.setPostCate(postCate);
                                                        postModel.setPostcontactKey(contactNumber);
                                                        postModel.setPostStatus(status);
                                                        postModel.setPostvisibilityCount(visibilityCount);
                                                        postModel.setPostclickCount(clickCount);

                                                        Log.d("fsfsfdsdn", "" + shopname);

                                                        postModel.setPostUser(shopname);
                                                        postModel.setUserImg(shopimagex);
                                                        postModel.setUserAdd(shopaddress);

                                                        // Add the post to the list
                                                        homeItemList.add(postModel);

                                                    }else {
                                                        Log.d("sdgsgsdfgs","No Data");
                                                    }
                                                }
                                            }
                                        }

                                        if (x++ == snapshotx.getChildrenCount() - 1) {
                                            // Set up the adapter and update the UI
                                            postView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                            homeMultiAdapter = new HomeMultiAdapter(homeItemList, CommInfoGlobal.this);
                                            postView.setAdapter(homeMultiAdapter);
                                            homeMultiAdapter.notifyDataSetChanged();

                                            Log.d("fsfsfdsdn", "Ok   "+homeItemList.size());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void DataLoad(String targetCommunityId) {
        ClearAllHome();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x = 0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String status = keySnapshot.child("status").getValue(String.class);
                                        if (!status.equals("Rejected")) {
                                            String commIds = keySnapshot.child("Comm").getValue(String.class);
                                            if (commIds != null){
                                                String[] commArray = commIds.split("&&");
                                                for (String commId : commArray) {
                                                    if (commId.equals(targetCommunityId)) {
                                                        // Fetch post details based on commId
                                                        System.out.println("Processing post for community ID: " + commId);
                                                        // Your existing code for fetching post details

                                                        shopname = snapshot.child("shopName").getValue(String.class);
                                                        shopimagex = snapshot.child("url").getValue(String.class);
                                                        shopaddress = snapshot.child("address").getValue(String.class);

                                                        String postImg = keySnapshot.child("postImg").getValue(String.class);
                                                        String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                                        String postType = keySnapshot.child("postType").getValue(String.class);
                                                        String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                                        String postCate = keySnapshot.child("postCate").getValue(String.class);
                                                        String servArea = keySnapshot.child("servingArea").getValue(String.class);
                                                        String visibilityCount = keySnapshot.child("visibilityCount").getValue(String.class);
                                                        String clickCount = keySnapshot.child("clickCount").getValue(String.class);

                                                        PostModel postModel = new PostModel();
                                                        postModel.setPostId(key);
                                                        postModel.setPostDesc(postDesc);
                                                        postModel.setPostType(postType);
                                                        postModel.setPostImg(postImg);
                                                        postModel.setPostKeys(postKeys);
                                                        postModel.setPostCate(postCate);
                                                        postModel.setPostcontactKey(contactNumber);
                                                        postModel.setPostStatus(status);
                                                        postModel.setPostvisibilityCount(visibilityCount);
                                                        postModel.setPostclickCount(clickCount);

                                                        Log.d("fsfsfdsdn", "" + shopname);

                                                        postModel.setPostUser(shopname);
                                                        postModel.setUserImg(shopimagex);
                                                        postModel.setUserAdd(shopaddress);

                                                        // Add the post to the list
                                                        homeItemList.add(postModel);

                                                    }
                                                }
                                            }
                                        }

                                        if (x++ == snapshotx.getChildrenCount() - 1) {
                                            // Set up the adapter and update the UI
                                            postView.setLayoutManager(new LinearLayoutManager(CommInfoGlobal.this));
                                            homeMultiAdapter = new HomeMultiAdapter(homeItemList, CommInfoGlobal.this);
                                            postView.setAdapter(homeMultiAdapter);
                                            homeMultiAdapter.notifyDataSetChanged();

                                            Log.d("fsfsfdsdn", "Ok 1");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


    @Override
    public void onViewDetailsClick(int position) {

    }

    @Override
    public void onPostClick(int position) {
        if (position >= 0 && position < homeItemList.size()) {
            Object selectedItem = homeItemList.get(position);

            if (selectedItem instanceof PostModel) {
                PostModel selectedPost = (PostModel) selectedItem;
                String contactkey = selectedPost.getPostId();
                String postDesc = selectedPost.getPostDesc();
                String postType = selectedPost.getPostType();
                String postImg = selectedPost.getPostImg();
                String postKeys = selectedPost.getPostKeys();
                String postCate = selectedPost.getPostCate();
                String shopname = selectedPost.getPostUser();
                String shopimagex = selectedPost.getUserImg();
                String shopaddress = selectedPost.getUserAdd();
                String shopcontact = selectedPost.getPostcontactKey();
                String viewcount = selectedPost.getPostvisibilityCount();
                String clickcount = selectedPost.getPostclickCount();
                Boolean flag = true;
                System.out.println("rgdfer " +contactkey);

                // Increment the click count in Firebase
                // updateClickCount(contactkey, shopcontact);

                Intent intent = new Intent(this, PostInfo.class);
                intent.putExtra("contactKey", contactkey);
                intent.putExtra("postDesc", postDesc);
                intent.putExtra("postType", postType);
                intent.putExtra("postImg", postImg);
                intent.putExtra("postKeys", postKeys);
                intent.putExtra("postCate", postCate);
                intent.putExtra("shopname", shopname);
                intent.putExtra("shopimagex", shopimagex);
                intent.putExtra("shopaddress", shopaddress);
                intent.putExtra("shopContact", shopcontact);
                intent.putExtra("viewcount", viewcount);
                intent.putExtra("clickcount", clickcount);
                // intent.putExtra("shopName", shopName);
                intent.putExtra("flag", flag);
                // Pass the list of item images
                startActivity(intent);

            } else {
                // Handle the case where the item at the specified position is not of type ItemList
                Log.e("ItemDetails", "Invalid item type at position " + position);
            }
        }
    }
}