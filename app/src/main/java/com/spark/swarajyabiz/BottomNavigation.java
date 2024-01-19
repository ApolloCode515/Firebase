package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.spark.swarajyabiz.databinding.ActivityBottomNavigationBinding;
import com.spark.swarajyabiz.ui.CommunityFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomNavigation extends AppCompatActivity {

    private ActivityBottomNavigationBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        SharedPreferences sharedPreference = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);

        handleDeepLink(getIntent());

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       // Toast.makeText(this, "Toast ", Toast.LENGTH_SHORT).show();
        // Handle new intents, such as when the app is already running, and a dynamic link is clicked
      //  handleDeepLink(intent);
    }

    private void handleDeepLink(Intent intent) {
        Uri deepLinkUri = intent.getData();
        if (deepLinkUri != null) {
            // Extract data from the deep link
            String path = deepLinkUri.getPath();
            String communityId = deepLinkUri.getQueryParameter("communityId");

            //Toast.makeText(this, "Toast " + communityId, Toast.LENGTH_SHORT).show();

            // Check if the deep link matches the expected path
            if ("/community".equals(path) && communityId != null) {
                // Now you can handle the communityId as needed
                // For example, navigate to a fragment or perform some action
                // navigateToFragment(communityId);
                joinCommunity(communityId);
            }

        }
    }

    private void navigateToFragment(String communityId) {
        // Implement your fragment navigation logic here
        // For example, replace the existing fragment with the community fragment
        CommunityFragment fragment = CommunityFragment.newInstance(communityId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_bottom_navigation, fragment)
                .commit();
    }

    public void joinCommunity(String comId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community/"+comId+"/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    int x = 0;
                    for (int i = 0; i < snapshotx.getChildrenCount(); i++) {
                        String commName = snapshotx.child("commName").getValue(String.class);
                        String commDesc = snapshotx.child("commDesc").getValue(String.class);
                        String commImg = snapshotx.child("commImg").getValue(String.class);
                        String commAdmin = snapshotx.child("commAdmin").getValue(String.class);
                        int comCnt = (int) snapshotx.child("commMembers").getChildrenCount();

                        if(snapshotx.child("commMembers").hasChild(userId)){
                            SnackBarHelper.showSnackbar(BottomNavigation.this, "You already joined this community.");
                        }else{
                            if (x++ == snapshotx.getChildrenCount() - 1) {
                                if(userId.equals(commAdmin)){
                                    // Toast.makeText(BottomNavigation.this, "You are the community admin.", Toast.LENGTH_SHORT).show();
                                    SnackBarHelper.showSnackbar(BottomNavigation.this, "You are the community admin.");
                                }else {
                                    View bottomSheetView = LayoutInflater.from(BottomNavigation.this).inflate(R.layout.joincomm, null);
                                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BottomNavigation.this);
                                    bottomSheetDialog.setContentView(bottomSheetView);

                                    // Set the height of the BottomSheetDialog based on your layout
                                    int dialogHeight = getResources().getDisplayMetrics().heightPixels / 2; // Adjust as needed
                                    bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight);

                                    CardView create = bottomSheetView.findViewById(R.id.joincommunity);
                                    ImageView commImg1 = bottomSheetView.findViewById(R.id.jcomImg);
                                    TextView commName1 = bottomSheetView.findViewById(R.id.jcomName);
                                    TextView commDesc1 = bottomSheetView.findViewById(R.id.jcomDesc);
                                    TextView comMber1 = bottomSheetView.findViewById(R.id.jcommMbr);


                                    Glide.with(BottomNavigation.this)
                                            .load(commImg)
                                            .into(commImg1);

                                    commName1.setText(commName);
                                    commDesc1.setText(commDesc);
                                    comMber1.setText(String.valueOf(comCnt) + " Members");

                                    create.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community/"+comId+"/commMembers/");
                                            DatabaseReference communityRef = databaseRef.child(userId);

                                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                                            Date date = new Date();
                                            String trandate=(dateFormat.format(date));

                                            communityRef.child("Jdate").setValue(trandate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    SnackBarHelper.showSnackbar(BottomNavigation.this, "You joined "+commName1.getText().toString()+" community.");
                                                }
                                            });

                                            bottomSheetDialog.dismiss();

                                        }
                                    });

                                    bottomSheetDialog.show();
                                }

                            }
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