package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Extra.NetworkUtils;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;

import java.sql.Ref;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;


public class SplashScreen extends AppCompatActivity implements NetworkUtils.PingTask.PingCallback {

    DatabaseReference userRef;
    String userId,currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FacebookSdk.fullyInitialize();

        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
            // Perform ping test to check internet connectivity
            new NetworkUtils.PingTask(getApplicationContext(), "www.google.com", 80, 5000, this).execute();
        } else {
            // No internet connection, show error message or take appropriate action
            //Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            NoInternet();
        }

        SharedPreferences sharedPreference = getSharedPreferences(LoginMain.PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);





        // Handle deep link
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // Handle deep link when a new intent is received
        handleDeepLink(intent);
    }

    private void handleDeepLink(Intent intent) {
        Uri deepLinkUri = intent.getData();
        if (deepLinkUri != null) {
            // Check if the user is logged in
            if (!isUserLoggedIn()) {
                // Redirect the user to the login page
                String path = deepLinkUri.getPath();
                if("/user".equals(path)){
                    String ReferalUser = deepLinkUri.getQueryParameter("userId");
                    Log.d("feferfeed",""+ ReferalUser);
                    Intent loginIntent = new Intent(this, LoginMain.class);
                    loginIntent.putExtra("ReferalUser",ReferalUser);
                    startActivity(loginIntent);
                    finish();
                }else {
                    Intent loginIntent = new Intent(this, LoginMain.class);
                    loginIntent.putExtra("ReferalUser","Clean");
                    startActivity(loginIntent);
                    finish();
                }
            }else {
                String path = deepLinkUri.getPath();

                // Toast.makeText(this, " "+productId, Toast.LENGTH_SHORT).show();
                // Check if the deep link matches the expected paths and necessary parameters are not null
                if ("/community".equals(path)) {
                    String communityId = deepLinkUri.getQueryParameter("communityId");
                    if (communityId != null) {
                        // Handle the deep link for the "Community" path
                        joinCommunity(communityId);
                    }
                } else if ("/product".equals(path)) {
                    String productId = deepLinkUri.getQueryParameter("productId");
                    if (productId != null) {
                        // Handle the deep link for the "Community" path
                        //  Toast.makeText(this, " "+productId, Toast.LENGTH_SHORT).show();

                        String[] parts = productId.split("XX");
                        String prodkey = parts[0]; // "123456"
                        String mobno = parts[1]; // "7083980082"

                        getProductLinkData(mobno,prodkey);
                    }
                    // Handle the deep link for the "Product" path

                }else if("/user".equals(path)){
                    String ReferalUser = deepLinkUri.getQueryParameter("userId");
                    Log.d("gdfgfgsfdgg","step1 "+ReferalUser);
                    if (ReferalUser!=null || userId != null){
                        DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referral/"+ReferalUser+"/"+userId+"/");
                        referralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    SnackBarHelper.showSnackbar(SplashScreen.this,"User already referred by another user.");
                                    startActivity(new Intent(SplashScreen.this, BottomNavigation.class));
                                    finish();
                                    Log.d("gdfgfgsfdgg","step4 "+referralRef);
                                }else {
                                    referralRef.setValue("App Installed");
                                    startActivity(new Intent(SplashScreen.this, BottomNavigation.class));
                                    finish();
                                    Log.d("gdfgfgsfdgg","step2 "+referralRef);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else {
                        Log.d("gdfgfgsfdgg","step3 "+userId);
                        Toast.makeText(this, "dd", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }else {
            SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0);
            boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

            if (hasLoggedIn) {
            // User is already logged in, continue with the app
                 startActivity(new Intent(SplashScreen.this, BottomNavigation.class));
            } else {
            // User is not logged in, redirect to the login activity
                 startActivity(new Intent(SplashScreen.this, LoginMain.class));
            }
        }
    }

    private boolean isUserLoggedIn() {
        // Implement the logic to check if the user is logged in
        // This can be based on SharedPreferences or any other method you use for user authentication
        SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0);
        return settings.getBoolean("hasLoggedIn", false);
    }

    private void redirectToLoginPage() {
        // Redirect the user to the login page
        Intent loginIntent = new Intent(this, LoginMain.class);
        startActivity(loginIntent);
        finish(); // Optional: finish the current activity to prevent going back to it
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
                            SnackBarHelper.showSnackbar(SplashScreen.this, "You already joined this community.");
                            if (x++ == snapshotx.getChildrenCount() - 1){
                                startActivity(new Intent(SplashScreen.this, BottomNavigation.class));
                                finish();
                            }
                        }else{
                            if (x++ == snapshotx.getChildrenCount() - 1) {
                                if(userId.equals(commAdmin)){
                                    // Toast.makeText(BottomNavigation.this, "You are the community admin.", Toast.LENGTH_SHORT).show();
                                    SnackBarHelper.showSnackbar(SplashScreen.this, "You are the community admin.");
                                    startActivity(new Intent(SplashScreen.this, BottomNavigation.class));
                                    finish();
                                }else {
                                    View bottomSheetView = LayoutInflater.from(SplashScreen.this).inflate(R.layout.joincomm, null);
                                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SplashScreen.this);
                                    bottomSheetDialog.setContentView(bottomSheetView);

                                    // Set the height of the BottomSheetDialog based on your layout
                                    int dialogHeight = getResources().getDisplayMetrics().heightPixels / 2; // Adjust as needed
                                    bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight);

                                    CardView create = bottomSheetView.findViewById(R.id.joincommunity);
                                    ImageView commImg1 = bottomSheetView.findViewById(R.id.jcomImg);
                                    TextView commName1 = bottomSheetView.findViewById(R.id.jcomName);
                                    TextView commDesc1 = bottomSheetView.findViewById(R.id.jcomDesc);
                                    TextView comMber1 = bottomSheetView.findViewById(R.id.jcommMbr);


                                    Glide.with(SplashScreen.this)
                                            .load(commImg)
                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                                            .transition(DrawableTransitionOptions.withCrossFade())
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
                                                    SnackBarHelper.showSnackbar(SplashScreen.this, "You joined "+commName1.getText().toString()+" community.");
                                                    startActivity(new Intent(SplashScreen.this, BottomNavigation.class));
                                                    finish();
                                                }
                                            });

                                            notification(commAdmin);

                                            bottomSheetDialog.dismiss();

                                        }
                                    });

                                    bottomSheetDialog.show();

                                    bottomSheetDialog.setCancelable(false);

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

    private void notification(String commAdmin) {
        if (!userId.equals(commAdmin)) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");

            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        currentUserName = snapshot.child("name").getValue(String.class);
                        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(commAdmin)
                                .child("notification"); // Ensure unique child key for each notification

                        String message = currentUserName + " join your community.";

                        // Create a map to store the message
                        Map<String, Object> notificationData = new HashMap<>();
                        notificationData.put("message", message);
                        notificationData.put("contactNumber", userId);
                        notificationData.put("key", userId);
                        notificationData.put("comm", "comm");
                        String key = notificationRef.push().getKey();
                        // Store the message under the currentusercontactNum
                        if (key!=null) {
                            notificationRef.child(key).setValue(notificationData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@io.reactivex.rxjava3.annotations.NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                        .child(commAdmin)
                                                        .child("notificationcount");

                                                DatabaseReference NotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                        .child(commAdmin)
                                                        .child("count")
                                                        .child("notificationcount");

                                                shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot snapshot) {
                                                        int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                                                        int newCount = currentCount + 1;

                                                        // Update the notification count
                                                        shopNotificationCountRef.setValue(newCount);
                                                        NotificationCountRef.setValue(newCount);
                                                    }

                                                    @Override
                                                    public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                                                        // Handle onCancelled event

                                                    }
                                                });
                                            } else {
                                                // Handle failure to store notification
                                            }
                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });
        }
    }

    public void getProductLinkData(String mobno, String prodkey) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Product...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products/" + mobno + "/" + prodkey + "/");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot productSnapshot) {
                progressDialog.dismiss();
                if (productSnapshot.exists()) {
                    String productId = productSnapshot.getKey();
                    String status = productSnapshot.child("status").getValue(String.class);
                    if ("Posted".equals(status)) {
                        String itemName = productSnapshot.child("itemname").getValue(String.class);
                        String itemPrice = productSnapshot.child("price").getValue(String.class);
                        String itemDescription = productSnapshot.child("description").getValue(String.class);
                        String itemKey = productSnapshot.child("itemkey").getValue(String.class);
                        String itemOffer = productSnapshot.child("offer").getValue(String.class);
                        String firstImageUrl = productSnapshot.child("firstImageUrl").getValue(String.class);
                        String itemSellPrice = productSnapshot.child("sell").getValue(String.class);
                        String shopContactNumber = productSnapshot.child("shopContactNumber").getValue(String.class);
                        String wholesale = productSnapshot.child("wholesale").getValue(String.class);
                        String minqty = productSnapshot.child("minquantity").getValue(String.class);
                        String servArea = productSnapshot.child("servingArea").getValue(String.class);

                        String proCate = productSnapshot.child("itemCate").getValue(String.class);
                        String proKeys = productSnapshot.child("itemKeys").getValue(String.class);

                        List<String> imageUrls = new ArrayList<>();
                        DataSnapshot imageUrlsSnapshot = productSnapshot.child("imageUrls");
                        for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                            String imageUrl = imageUrlSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                            }
                        }

                        if (shopContactNumber != null) {
                            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Shop/" + shopContactNumber + "/");
                            shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    progressDialog.dismiss();
                                    if (snapshot.exists()) {
                                        String shopName = snapshot.child("shopName").getValue(String.class);
                                        String shopImg = snapshot.child("url").getValue(String.class);
                                        String address = snapshot.child("address").getValue(String.class);
                                        String district = snapshot.child("district").getValue(String.class);
                                        String taluka = snapshot.child("taluka").getValue(String.class);

                                        Intent intent = new Intent(SplashScreen.this, ItemDetails.class);
                                        intent.putExtra("itemName", itemName);
                                        intent.putExtra("firstImageUrl", firstImageUrl);
                                        intent.putExtra("itemDescription", itemDescription);
                                        intent.putExtra("itemPrice", itemPrice);
                                        intent.putExtra("itemKey", itemKey);
                                        intent.putExtra("contactNumber", shopContactNumber);
                                        intent.putExtra("itemOffer", itemOffer);
                                        intent.putExtra("itemSellPrice", itemSellPrice);
                                        intent.putExtra("itemWholesale", wholesale);
                                        intent.putExtra("itemMinqty", minqty);
                                        intent.putExtra("shopName", shopName);
                                        intent.putExtra("district", district);
                                        intent.putExtra("shopimage", shopImg);
                                        intent.putExtra("taluka", taluka);
                                        intent.putExtra("address", address);
                                        intent.putExtra("flag", true);

                                        // Pass the list of item images
                                        intent.putStringArrayListExtra("itemImages", new ArrayList<>(imageUrls));
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                    // Handle onCancelled
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                progressDialog.dismiss();
                // Handle onCancelled
            }
        });
    }


    @Override
    public void onPingResult(boolean success) {
        if (success){
            Timer timer=new Timer();
            timer.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        userRef = FirebaseDatabase.getInstance().getReference("Users");
                                        if (userId != null) {
                                            // userId = mAuth.getCurrentUser().getUid();
                                            System.out.println("dffvf  " +userId);
                                            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        String name = snapshot.child("name").getValue(String.class);
                                                        String activecount = snapshot.child("activeCount").getValue(String.class);

                                                        if (activecount == null) {
                                                            activecount = "0";
                                                        }

                                                        int count = Integer.parseInt(activecount) + 1;
                                                        System.out.println("wdevc " + count);

                                                        // Update the activeCount in the database
                                                        userRef.child(userId).child("activeCount").setValue(String.valueOf(count));

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                    // Handle onCancelled
                                                }
                                            });

                                        } else {
                                            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
                                        }
                                    }
                                    catch (Exception e){

                                    }


                                    handleDeepLink(getIntent());
                                }
                            });
                        }
                    },
                    3000 // Set delay for 3 seconds (3000 milliseconds)
            );
        }else {
            NoInternet();
        }
    }

    public void NoInternet(){
        final Dialog dialog = new Dialog(SplashScreen.this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.setCancelable(false);

        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        Button closeButton = dialog.findViewById(R.id.closeButton);

        // Set Lottie animation file
        lottieAnimationView.setAnimation("nointernet1.json");
        lottieAnimationView.playAnimation();

        // Handle close button click
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}