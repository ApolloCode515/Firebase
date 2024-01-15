package com.spark.swarajyabiz.MyFragments;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.R;

import io.reactivex.rxjava3.annotations.NonNull;

public class PostsFragment extends Fragment {

    RecyclerView postView;
    int x=0;
    DatabaseReference userRef, shopRef;
    String shopcontactNumber, taluka,address, shopName, shopimage, destrict;

    String userId, jobTitle, companyname, joblocation, jobtype, description, workplacetype, currentdate,
            postcontactNumber, jobid, experience, skills, salary, jobopenings;
    String shopname, premium, postImg, postDesc,postType ,postKeys, postCate, contactkey,shopimagex,shopaddress;
    public static PostsFragment newInstance(String data) {

        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString("KEY_DATA", data);
        fragment.setArguments(args);
        return fragment;

    }

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_posts, container, false);

        postView=view.findViewById(R.id.postView);

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        // Initialize with -1 to start from the first image
        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            userRef.child(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        return view;
    }

    public void getMyCommunityPosts(){
       // ClearAllHome();
       // lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x=0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        shopname = snapshot.child("shopName").getValue(String.class);
                                        shopimagex = snapshot.child("url").getValue(String.class);
                                        shopaddress= snapshot.child("address").getValue(String.class);

                                        String postImg = keySnapshot.child("postImg").getValue(String.class);
                                        String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                        String postType = keySnapshot.child("postType").getValue(String.class);
                                        String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                        String postCate = keySnapshot.child("postCate").getValue(String.class);
                                        String servArea = keySnapshot.child("servingArea").getValue(String.class);

                                        PostModel postModel=new PostModel();
                                        postModel.setPostId(key);
                                        postModel.setPostDesc(postDesc);
                                        postModel.setPostType(postType);
                                        postModel.setPostImg(postImg);
                                        postModel.setPostKeys(postKeys);
                                        postModel.setPostCate(postCate);

                                        Log.d("fsfsfdsdn",""+shopname);

                                        postModel.setPostUser(shopname);
                                        postModel.setUserImg(shopimagex);
                                        postModel.setUserAdd(shopaddress);

//                                        homeItemList.add(postModel);
//                                        lottieAnimationView.setVisibility(View.GONE);
//                                        if(x++==snapshotx.getChildrenCount()-1){
//                                            getProductData(homeItemList);
//                                            Log.d("fsfsfdsdn","Ok 1");
//                                        }

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
}