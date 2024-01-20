package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.CommAdapter;
import com.spark.swarajyabiz.ModelClasses.CommModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class FragmentGlobalCommunity extends Fragment implements CommAdapter.OnItemClickListener{
    ArrayList<CommModel> commModels=new ArrayList<>();
    CommAdapter commAdapter;
    RecyclerView commView;
    String userId;

    public FragmentGlobalCommunity() {
        // Required empty public constructor
    }


    public static FragmentGlobalCommunity newInstance() {
        FragmentGlobalCommunity fragment = new FragmentGlobalCommunity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_global_community, container, false);
        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        commView=view.findViewById(R.id.globalcommview);

        getAllCommunityData();

        return view;
    }

    private void ClearAllEmployee(){
        if(commModels != null){
            commModels.clear();

            if(commAdapter!=null){
                commAdapter.notifyDataSetChanged();
            }
        }
        commModels=new ArrayList<>();
    }

    public void getAllCommunityData(){
        ClearAllEmployee();
        //lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    int x=0;
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String commId = keySnapshot.getKey();
                        String commName = keySnapshot.child("commName").getValue(String.class);
                        String commDesc = keySnapshot.child("commDesc").getValue(String.class);
                        String commImg = keySnapshot.child("commImg").getValue(String.class);
                        String commStatus = keySnapshot.child("commStatus").getValue(String.class);
                        String commAdmin = keySnapshot.child("servingArea").getValue(String.class);
                        String commLink = keySnapshot.child("dynamicLink").getValue(String.class);

                        int comCnt= (int) keySnapshot.child("commMembers").getChildrenCount();

                        // boolean ss=keySnapshot.child("commMembers").hasChild(userId);

                        if(comCnt<=100){
                            CommModel commModel=new CommModel();
                            commModel.setCommId(commId);
                            commModel.setCommName(commName);
                            commModel.setCommDesc(commDesc);
                            commModel.setCommAdmin(commAdmin);
                            commModel.setCommImg(commImg);
                            commModel.setCommLink(commLink);
                            commModel.setChecked(true);
                            commModel.setMbrCount(String.valueOf(comCnt));
                            commModels.add(commModel);
                            //Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                        }

                        // lottieAnimationView.setVisibility(View.GONE);


                    }

                    commView.setLayoutManager(new LinearLayoutManager(getContext()));
                    commAdapter = new CommAdapter(getActivity(),commModels, FragmentGlobalCommunity.this);
                    commView.setAdapter(commAdapter);

                    commAdapter.notifyDataSetChanged();
                    //  lottieAnimationView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}