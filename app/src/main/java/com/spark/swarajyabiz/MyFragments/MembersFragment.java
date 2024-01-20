package com.spark.swarajyabiz.MyFragments;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.Adapters.MemberAdapter;
import com.spark.swarajyabiz.ModelClasses.MemberModel;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class MembersFragment extends Fragment implements MemberAdapter.OnItemClickListener {

    int x=0;
    DatabaseReference userRef, shopRef;
    String userId;
    static String dd;
    RecyclerView recyclerView;
    MemberAdapter memberAdapter;

    public static MembersFragment newInstance(String commId) {
        dd=commId;
        return new MembersFragment();
    }

    public MembersFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_members, container, false);

        recyclerView = view.findViewById(R.id.memberView);

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        // Initialize with -1 to start from the first image
        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);

        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            //userRef.child(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        getMembersData(dd);

        return view;

    }

    public void getMembersData(String targetCommunityId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community/" + targetCommunityId + "/commMembers/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    ArrayList<MemberModel> memberList = new ArrayList<>();

                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();
                        // Inside onDataChange for userRef

                        if (contactNumber != null) {
                            userRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    if (userSnapshot.exists()) {
                                        String userName = userSnapshot.child("name").getValue(String.class);
                                        String userdistrict = userSnapshot.child("district").getValue(String.class);

                                        shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot shopSnapshot) {
                                                if (shopSnapshot.exists()) {
                                                    String shopName = shopSnapshot.child("shopName").getValue(String.class);
                                                    String shopImage = shopSnapshot.child("url").getValue(String.class);
                                                    String shopcategory = shopSnapshot.child("shopcategory").getValue(String.class);
                                                    String shopAdd = shopSnapshot.child("address").getValue(String.class);

                                                    MemberModel shopModel = new MemberModel(contactNumber,userName,userdistrict,shopImage,shopName,shopcategory,shopAdd,"-");
                                                    memberList.add(shopModel);
                                                    // Update RecyclerView after adding data
                                                    updateRecyclerView(memberList);

                                                }else {
                                                    MemberModel shopModel = new MemberModel(contactNumber,userName,userdistrict,"https://firebasestorage.googleapis.com/v0/b/fir-39c66.appspot.com/o/bklogo1.jpeg?alt=media&token=92dd83b5-43ce-4ab7-bcfe-c3aebb98cb9c","No Shop","-","-","-");
                                                    memberList.add(shopModel);
                                                    // Update RecyclerView after adding data
                                                    updateRecyclerView(memberList);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle onCancelled
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });

                        }
                        // Inside onDataChange for shopRef

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void updateRecyclerView(ArrayList<MemberModel> memberList) {
        // Assuming you have a RecyclerView with the ID "recyclerView" in your layou
        // Create and set the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create the adapter and set it to the RecyclerView
        MemberAdapter memberAdapter = new MemberAdapter(getContext(),memberList,MembersFragment.this);
        recyclerView.setAdapter(memberAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }
}