package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.ModelClasses.OrderModel;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.spark.swarajyabiz.databinding.ActivityBottomNavigationBinding;
import com.spark.swarajyabiz.ui.CommunityFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomNavigation extends AppCompatActivity {

    private ActivityBottomNavigationBinding binding;
    String userId, currentUserName;
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

//        Intent intent = getIntent();
//        Uri data = intent.getData();
//        if (data != null) {
//            // Handle the deep link data
//            handleDeepLink(data);
//        } else {
//            // Handle regular activity launch
//        }

        //handleDeepLink(getIntent());

    }

//    private void handleDeepLink(Intent intent) {
//        Uri deepLinkUri = intent.getData();
//        if (deepLinkUri != null) {
//            // Extract data from the deep link
//            String path = deepLinkUri.getPath();
//            String communityId = deepLinkUri.getQueryParameter("communityId");
//
//            //Toast.makeText(this, "Toast " + communityId, Toast.LENGTH_SHORT).show();
//
//            // Check if the deep link matches the expected path
//            if ("/community".equals(path) && communityId != null) {
//                // Now you can handle the communityId as needed
//                // For example, navigate to a fragment or perform some action
//                // navigateToFragment(communityId);
//                joinCommunity(communityId);
//            }
//
//        }
//    }

//    private void handleDeepLink(Uri uri) {
//        String host = uri.getHost();
//        String path = uri.getPath();
//
//        // Check the host and path to determine the action to take
//        if ("kaamdhanda.page.link".equals(host)) {
//            if ("/community".equals(path)) {
//                // Handle the deep link for the /community path
//            } else if ("/product".equals(path)) {
//                // Handle the deep link for the /product path
//            } else if ("/shop".equals(path)) {
//                // Handle the deep link for the /shop path
//            }
//        }
//    }

    private void handleDeepLink(Intent intent) {
        Uri deepLinkUri = intent.getData();
        if (deepLinkUri != null) {
            // Extract data from the deep link
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

            } else if ("/shop".equals(path)) {
                // Handle the deep link for the "Shop" path
                String shopId = deepLinkUri.getQueryParameter("shopId");
            }else if("/user".equals(path)){

                String ReferalUser = deepLinkUri.getQueryParameter("userId");
                Log.d("gdfgfgsfdgg","step1 "+ReferalUser);
                if (ReferalUser!=null || userId!=null){
                    DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referral/"+ReferalUser+"/"+userId+"/");
                    referralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                SnackBarHelper.showSnackbar(BottomNavigation.this,"User already referred by another user.");
                                Log.d("gdfgfgsfdgg","step4 "+referralRef);
                            }else {
                                referralRef.setValue("App Installed");
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
                                                    SnackBarHelper.showSnackbar(BottomNavigation.this, "You joined "+commName1.getText().toString()+" community.");
                                                }
                                            });

                                            notification(commAdmin);

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

                                        Intent intent = new Intent(BottomNavigation.this, ItemDetails.class);
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


}