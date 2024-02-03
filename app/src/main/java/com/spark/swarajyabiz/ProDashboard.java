package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.ShimmerTextView;
import com.spark.swarajyabiz.Adapters.CommAdapter;
import com.spark.swarajyabiz.Adapters.CommInsightAdapter;
import com.spark.swarajyabiz.ModelClasses.CommInsightModel;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.ui.CommunityFragment;

import java.util.ArrayList;

public class ProDashboard extends AppCompatActivity implements CommInsightAdapter.OnItemClickListener {
    TextView amt100,amt300,amt500,egbal,unamex,earnbal;
    EditText tpedit,usermb,tpedit1;
    ImageView clear,clear1,img1,img2,img3,img4,img5,img6;
    Button addmoney,check,acto,withdraw;
    LinearLayout rcharge,activate,history,rclay,actlay,histlay,silver,gold,platinum,nohist,setpro,incentive;
    String userId,uname,email="dd",wallbal,plan,vldty,pamt,payId,Tid;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mref;
    ArrayList<Integer> Idlist1=new ArrayList<Integer>();
    TextView translog,expdts,uplan,totalComm;
    LinearLayout sharekk;

    double earn=0;
    String adBalance,wallBalance;

    CommInsightAdapter commInsightAdapter;
    ArrayList<CommInsightModel> commInsightModels;

    RecyclerView insightView;
    int x=0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_dashboard);

        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference();

        amt100=findViewById(R.id.amt100);
        amt300=findViewById(R.id.amt300);
        amt500=findViewById(R.id.amt500);
        tpedit=findViewById(R.id.tpamt);
        clear=findViewById(R.id.clear);
        egbal=findViewById(R.id.egbalance);
        unamex=findViewById(R.id.unamev);
        uplan=findViewById(R.id.uplan);

        translog=findViewById(R.id.translogxx);


        rcharge=findViewById(R.id.recharge11);
        activate=findViewById(R.id.activate11);
        history=findViewById(R.id.history11);
        rclay=findViewById(R.id.rclay);
        actlay=findViewById(R.id.actlay);
        histlay=findViewById(R.id.histlay);
        addmoney=findViewById(R.id.addmoney);

        withdraw=findViewById(R.id.withdrawmoney);

        tpedit1=findViewById(R.id.tpamt1);
        clear1=findViewById(R.id.clear1);


        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        img3=findViewById(R.id.img3);

        earnbal=findViewById(R.id.earnball);

        insightView=findViewById(R.id.insightView);

        totalComm=findViewById(R.id.commCntss);

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

        SharedPreferences sharedPreference = ProDashboard.this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            //contacttext.setText(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }
        rcharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rclay.setVisibility(View.VISIBLE);
                actlay.setVisibility(View.GONE);
                histlay.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.ic_baseline_commit_2422);
                img2.setImageResource(R.drawable.ic_baseline_commit_24);
                img3.setImageResource(R.drawable.ic_baseline_commit_24);
            }
        });

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rclay.setVisibility(View.GONE);
                actlay.setVisibility(View.VISIBLE);
                histlay.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.ic_baseline_commit_24);
                img2.setImageResource(R.drawable.ic_baseline_commit_2422);
                img3.setImageResource(R.drawable.ic_baseline_commit_24);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rclay.setVisibility(View.GONE);
                histlay.setVisibility(View.VISIBLE);
                actlay.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.ic_baseline_commit_24);
                img2.setImageResource(R.drawable.ic_baseline_commit_24);
                img3.setImageResource(R.drawable.ic_baseline_commit_2422);
                //ClearAll();
                //loadDataHist();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpedit.getText().toString().trim().isEmpty()){

                }else {
                    tpedit.setText("");
                }
            }
        });

        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpedit1.getText().toString().trim().isEmpty()){

                }else {
                    tpedit1.setText("");
                }
            }
        });

        tpedit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!tpedit1.getText().toString().isEmpty()) {
                    double amt = Double.parseDouble(tpedit1.getText().toString().trim());
                    if (amt >= 100 && !(amt > earn)) {
                        withdraw.setEnabled(true);
                        withdraw.setBackgroundResource(R.drawable.button); // Use setBackgroundResource
                    } else {
                        withdraw.setEnabled(false); // Disable the button
                        withdraw.setBackgroundResource(R.drawable.buttondisable); // Use setBackgroundResource
                    }
                } else {
                    withdraw.setEnabled(false); // Disable the button if the edit text is empty
                    withdraw.setBackgroundResource(R.drawable.buttondisable); // Use setBackgroundResource
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        amt100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tpedit.getText().toString().trim().isEmpty()){
                    tpedit.setText("100");
                }else {
                    long amt= Long.parseLong(tpedit.getText().toString().trim());
                    long tt=amt+100;
                    tpedit.setText(String.valueOf(tt));
                }
            }
        });

        amt300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tpedit.getText().toString().trim().isEmpty()){
                    tpedit.setText("300");
                }else {
                    long amt= Long.parseLong(tpedit.getText().toString().trim());
                    long tt=amt+300;
                    tpedit.setText(String.valueOf(tt));
                }
            }
        });

        amt500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tpedit.getText().toString().trim().isEmpty()){
                    tpedit.setText("500");
                }else {
                    long amt= Long.parseLong(tpedit.getText().toString().trim());
                    long tt=amt+500;
                    tpedit.setText(String.valueOf(tt));
                }
            }
        });

        egbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadBalance();
                Toast.makeText(ProDashboard.this, "Balance Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpedit.getText().toString().trim().isEmpty()){

                }else {
                    Intent intent=new Intent(ProDashboard.this,WalletPayment.class);
                    intent.putExtra("NewBalance",tpedit.getText().toString().trim());
                    intent.putExtra("OldBalance", adBalance.trim());
                    startActivity(intent);
                    //Toast.makeText(ProDashboard.this, "Step 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tpedit1.getText().toString().trim().isEmpty()){

                }else {

                }
            }
        });

        getWallBal();

        getInsightData2();

        getCommCount();

    }

    public void getWallBal() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String wallbal = snapshot.child("WallBal").getValue(String.class);
                    String adBal = snapshot.child("AdBalance").getValue(String.class);
                    String uname = snapshot.child("name").getValue(String.class);
                    String plan = snapshot.child("Plan").getValue(String.class);

                    unamex.setText(uname);
                    uplan.setText(plan);

                    adBalance=adBal;

                    earn= Double.parseDouble(wallbal);

                    try {
                        float addBall = Float.parseFloat(adBal);
                        animateTextView(0, addBall, egbal,adBal);

                        float EarnBal = Float.parseFloat(wallbal);
                        animateTextView2(0, EarnBal, earnbal,wallbal);
                        if(EarnBal>=100){
                            tpedit1.setText(wallbal);
                        }else {
                            tpedit1.setText("");
                        }

                    } catch (NumberFormatException e) {
                        // Handle the case where wallbal is not a valid float string
                        // Log the error or set a default value for finalWallBal
                    }
                } else {
                    egbal.setText("₹ 0.0");
                    earnbal.setText("₹ 0.0");
                    //  rupeesx.setText("0.0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }


    public void animateTextView(int initialValue, float finalValue, final TextView textview,String mainval) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                int intValue = Math.round(animatedValue); // Round to the nearest integer
                textview.setText("₹ " + intValue);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation finished
                // You can perform any actions here when the animation finishes
                // For example, you can show a toast message or execute additional logic
                textview.setText("₹ " + mainval);
            }
        });

        valueAnimator.start();
    }

    public void animateTextView2(int initialValue, float finalValue, final TextView textview,String mainval) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                int intValue = Math.round(animatedValue); // Round to the nearest integer
                textview.setText("₹ " + intValue);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation finished
                // You can perform any actions here when the animation finishes
                // For example, you can show a toast message or execute additional logic
                textview.setText("₹ " + mainval);
            }
        });

        valueAnimator.start();
    }

    private void ClearAllEmployee(){
        if(commInsightModels != null){
            commInsightModels.clear();

            if(commInsightAdapter!=null){
                commInsightAdapter.notifyDataSetChanged();
            }
        }
        commInsightModels=new ArrayList<>();
    }

    public void getInsightData(){
        ClearAllEmployee();
        //lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community");
        databaseReference.orderByChild("commAdmin").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x=0;
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String commId = keySnapshot.getKey();
                        String commName = keySnapshot.child("commName").getValue(String.class);
                        String commDesc = keySnapshot.child("commDesc").getValue(String.class);
                        String commImg = keySnapshot.child("commImg").getValue(String.class);
                        String commStatus = keySnapshot.child("commStatus").getValue(String.class);
                        String commAdmin = keySnapshot.child("servingArea").getValue(String.class);
                        String commLink = keySnapshot.child("dynamicLink").getValue(String.class);

                        int comCnt= (int) keySnapshot.child("commMembers").getChildrenCount();

                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/CommView/");
                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                                if(snapshotx.exists()){
                                    double amt=0;
                                    int views=0;
                                    for (DataSnapshot keySnap : snapshotx.getChildren()) {
                                        String postId = keySnap.getKey();
                                        String viewCnt=keySnap.child("views").getValue(String.class);
                                        String earnCnt=keySnap.child("wallamt").getValue(String.class);
                                        int v1= Integer.parseInt(viewCnt);
                                        double amt1= Double.parseDouble(earnCnt);
                                        views=views+v1;
                                        amt=amt+amt1;
                                    }

                                    if (x == snapshotx.getChildrenCount() - 1) {
                                        commInsightAdapter.notifyDataSetChanged();
                                    }
                                    x++;

                                }else {

                                }
                            }
                            @Override
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                                // Handle onCancelled
                            }
                        });

                    }

                    insightView.setLayoutManager(new LinearLayoutManager(ProDashboard.this));
                    commInsightAdapter = new CommInsightAdapter(ProDashboard.this,commInsightModels, ProDashboard.this);
                    insightView.setAdapter(commInsightAdapter);

                    commInsightAdapter.notifyDataSetChanged();
                    //  lottieAnimationView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    public void getInsightData2() {
        ClearAllEmployee();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community");
        databaseReference.orderByChild("commAdmin").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x = 0;
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String commId = keySnapshot.getKey();
                        String commName = keySnapshot.child("commName").getValue(String.class);
                        String commDesc = keySnapshot.child("commDesc").getValue(String.class);
                        String commImg = keySnapshot.child("commImg").getValue(String.class);
                        String commStatus = keySnapshot.child("commStatus").getValue(String.class);
                        String commAdmin = keySnapshot.child("servingArea").getValue(String.class);
                        String commLink = keySnapshot.child("dynamicLink").getValue(String.class);
                        String monit = keySnapshot.child("monit").getValue(String.class);
                        int comCnt = (int) keySnapshot.child("commMembers").getChildrenCount();

                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/CommView/");
                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                                if(snapshotx.exists()){
                                    double amt = 0;
                                    int views = 0;
                                    for (DataSnapshot keySnap : snapshotx.getChildren()) {
                                        String postId = keySnap.getKey();
                                        String viewCnt = keySnap.child("views").getValue(String.class);
                                        String earnCnt = keySnap.child("wallamt").getValue(String.class);
                                        int v1 = Integer.parseInt(viewCnt);
                                        double amt1 = Double.parseDouble(earnCnt);
                                        views = views + v1;
                                        amt = amt + amt1;
                                    }
                                    // Add data to your CommInsightModels list here...

                                    CommInsightModel commInsightModel=new CommInsightModel();
                                    commInsightModel.setCommId(commId);
                                    commInsightModel.setCommName(commName);
                                    commInsightModel.setCommMbrCnt(String.valueOf(comCnt));
                                    commInsightModel.setCommEarns(String.valueOf(amt));
                                    commInsightModel.setCommViews(String.valueOf(views));
                                    if(monit.equals("enable")){
                                        commInsightModel.setCommMonit("Enabled");
                                    }else {
                                        commInsightModel.setCommMonit("Disabled");
                                    }
                                    commInsightModel.setCommProf(commImg);
                                    commInsightModel.setCommStatus(commStatus);
                                    commInsightModels.add(commInsightModel);

                                    insightView.setLayoutManager(new LinearLayoutManager(ProDashboard.this));
                                    commInsightAdapter = new CommInsightAdapter(ProDashboard.this,commInsightModels, ProDashboard.this);
                                    insightView.setAdapter(commInsightAdapter);
                                    commInsightAdapter.notifyDataSetChanged();

                                    // Notify adapter after all data is retrieved for all communities
                                    if (++x == snapshotx.getChildrenCount()) {
                                        Toast.makeText(ProDashboard.this, "Ok", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                                // Handle onCancelled
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getCommCount() {
        ClearAllEmployee();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community");
        databaseReference.orderByChild("commAdmin").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    String ss= String.valueOf(snapshotx.getChildrenCount());
                    totalComm.setText(ss);
                }else {
                    totalComm.setText("0");
                }
            }

            @Override
            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

}