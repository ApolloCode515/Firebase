package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.CommAdapter;
import com.spark.swarajyabiz.Adapters.RefsAdapter;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.ModelClasses.RefModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class RefsData extends AppCompatActivity implements RefsAdapter.OnItemClickListener {
    TextView rewards,claim;
    RecyclerView referview;

    String userId;

    RefsAdapter refsAdapter;
    ArrayList<RefModel> refModels;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refs_data);

        SharedPreferences sharedPreference = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            // userRef.child(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        referview=findViewById(R.id.refslist);
        refModels=new ArrayList<>();
        rewards=findViewById(R.id.rewardamt);
        claim=findViewById(R.id.claimreward);

        getReferData();

        getReward();

    }

    private void ClearAllEmployee(){
        if(refModels != null){
            refModels.clear();

            if(refsAdapter!=null){
                refsAdapter.notifyDataSetChanged();
            }
        }
        refModels=new ArrayList<>();
    }

    public void getReferData(){
        ClearAllEmployee();
        //lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ReferSection");
        databaseReference.orderByChild("RefBy").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    int x=0;
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String refsnumbers = keySnapshot.getKey();
                        String cplan = keySnapshot.child("Cplan").getValue(String.class);
                        String rfdate = keySnapshot.child("RefDate").getValue(String.class);

                        RefModel refModel=new RefModel();
                        refModel.setCplan(cplan);
                        refModel.setDate(rfdate);
                        refModel.setMobno(refsnumbers);
                        refModels.add(refModel);
                        // lottieAnimationView.setVisibility(View.GONE);
                        if(x++==snapshotx.getChildrenCount()-1){
                            // getProductData(homeItemList);
                        }

                    }

                    referview.setLayoutManager(new LinearLayoutManager(RefsData.this));
                    refsAdapter = new RefsAdapter(RefsData.this,refModels, RefsData.this);
                    referview.setAdapter(refsAdapter);

                    refsAdapter.notifyDataSetChanged();
                    //  lottieAnimationView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getReward(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/Reward/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    String reward = snapshotx.getValue(String.class);
                    rewards.setText(reward);
                }else {
                    rewards.setText("0.0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                rewards.setText("0.0");
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}