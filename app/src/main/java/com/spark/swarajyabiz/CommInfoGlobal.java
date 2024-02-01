package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.MyFragments.PostsFragment;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class CommInfoGlobal extends AppCompatActivity implements HomeMultiAdapter.OnViewDetailsClickListener {

    RecyclerView postView;
    DatabaseReference userRef, shopRef;
    String shopcontactNumber, taluka,address, shopName, shopimage, destrict;

    String userId,currentUserName, jobTitle, companyname, joblocation, jobtype, description, workplacetype, currentdate,
            postcontactNumber, jobid, experience, skills, salary, jobopenings;
    String shopname, premium, postImg, postDesc,postType ,postKeys, postCate, contactkey,shopimagex,shopaddress;
    HomeMultiAdapter homeMultiAdapter;
    List<Object> homeItemList=new ArrayList<>();
    private LottieAnimationView lottieAnimationView;

    int x = 0;
    static String dd;

    ImageView commImgx,joinImg,belowImg;

    TextView name,desc,mbrcnt,jointext,joinnow;

    CardView invite,share;

    String commLinks,commAdmin;

    LinearLayout joinbox,boxlay;

    boolean hasJoin=false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_info_global);

        commImgx=findViewById(R.id.ImgComm1);
        name=findViewById(R.id.nameComm1);
        desc=findViewById(R.id.descComm1);
        mbrcnt=findViewById(R.id.commMbr1);
        invite=findViewById(R.id.joincomm1);
        share=findViewById(R.id.shareComm1);

        joinImg=findViewById(R.id.joinImg);
        jointext=findViewById(R.id.jointext);
        joinbox=findViewById(R.id.joinbox);
        boxlay=findViewById(R.id.boxlay);

        joinnow=findViewById(R.id.joinnow);
        belowImg=findViewById(R.id.belowImg);

        postView=findViewById(R.id.globalpostview);

        Intent intent=getIntent();
        ArrayList<String> data=intent.getStringArrayListExtra("Data");
        String commId=data.get(0);
        String commName=data.get(1);
        commAdmin=data.get(2);
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
       // Toast.makeText(this, ""+commId, Toast.LENGTH_SHORT).show();

        checkExistence(commId,userId);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //checkExistence(commId,commAdmin);
                if(hasJoin){
                    leftCommunity(commId,commName);
                }else {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/commMembers/");
                    DatabaseReference communityRef = databaseRef.child(userId);

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    Date date = new Date();
                    String trandate=(dateFormat.format(date));

                    communityRef.child("Jdate").setValue(trandate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            SnackBarHelper.showSnackbar(CommInfoGlobal.this, "You joined "+commName.toString()+" community.");
                            checkExistence(commId,userId);
                            notification();
                        }
                    });
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadImageTask().execute(commImg);
            }
        });

        joinnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasJoin){
                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(CommInfoGlobal.this)
                            // set message, title, and icon
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to exit the group?")
                            // .setIcon(R.drawable.logodelete)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/commMembers/");
                                    Task<Void> communityRef = databaseRef.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            SnackBarHelper.showSnackbar(CommInfoGlobal.this, "You joined "+commName.toString()+" community.");
                                            checkExistence(commId,userId);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@androidx.annotation.NonNull Exception e) {

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myQuittingDialogBox.setCanceledOnTouchOutside(false);
                    myQuittingDialogBox.setCancelable(false);
                    myQuittingDialogBox.show();
                }else {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/commMembers/");
                    DatabaseReference communityRef = databaseRef.child(userId);

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    Date date = new Date();
                    String trandate=(dateFormat.format(date));

                    communityRef.child("Jdate").setValue(trandate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            SnackBarHelper.showSnackbar(CommInfoGlobal.this, "You joined "+commName.toString()+" community.");
                            checkExistence(commId,userId);
                            notification();
                        }
                    });
                }
            }
        });

        Method2(commId);

   }

    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommInfoGlobal.this);
            progressDialog.setMessage("Loading apps for sharing community...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    return BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            progressDialog.dismiss();
            if (result != null) {
                // Share the downloaded image
                shareImageAndText(result);
            } else {
                Toast.makeText(CommInfoGlobal.this, "Failed to download image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareImageAndText(Bitmap imageBitmap) {
        File imageFile = saveBitmapToFile(imageBitmap);

        if (imageFile != null) {
            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    imageFile
            );

            // Create an intent to share the image and text
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_TEXT, "Join our community!\n" + commLinks);

            // Grant temporary read permission to the content URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Use Intent.createChooser to show a dialog with app options
            Intent chooser = Intent.createChooser(intent, "Share with");
            // Verify the intent will resolve to at least one activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                Toast.makeText(CommInfoGlobal.this, "No app can handle this request", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CommInfoGlobal.this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }


    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            File cacheDir = getCacheDir();
            File imageFile = File.createTempFile("temp_image", ".jpg", cacheDir);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    private void notification() {
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
                                        public void onComplete(@NonNull Task<Void> task) {
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
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                                                        int newCount = currentCount + 1;

                                                        // Update the notification count
                                                        shopNotificationCountRef.setValue(newCount);
                                                        NotificationCountRef.setValue(newCount);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
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

    private void notificationleft() {
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

                        String message = currentUserName + " left your community.";

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
                                        public void onComplete(@NonNull Task<Void> task) {
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
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                                                        int newCount = currentCount + 1;

                                                        // Update the notification count
                                                        shopNotificationCountRef.setValue(newCount);
                                                        NotificationCountRef.setValue(newCount);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
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


    public void checkExistence(String commId,String commAdmin){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/commMembers/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(commAdmin)) {
                    jointext.setText("Joined");
                    Glide.with(CommInfoGlobal.this)
                            .load(R.drawable.joinedcheck) // Replace "your_image" with the actual image resource name
                            .into(joinImg);
                    // Change layout background color
                    int color = Color.parseColor("#239328"); // Replace with your desired color
                    joinbox.setBackgroundColor(color);
                    hasJoin=true;
                    boxlay.setVisibility(View.GONE);
                    postView.setVisibility(View.VISIBLE);
                    belowImg.setVisibility(View.VISIBLE);

                }else {
                    jointext.setText("Join");
                    Glide.with(CommInfoGlobal.this)
                            .load(R.drawable.joincomm) // Replace "your_image" with the actual image resource name
                            .into(joinImg);
                    // Change layout background color
                    int color = Color.parseColor("#771591"); // Replace with your desired color
                    joinbox.setBackgroundColor(color);
                    hasJoin=false;
                    boxlay.setVisibility(View.VISIBLE);
                    postView.setVisibility(View.GONE);
                    belowImg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

    public void Method1(String targetCommunityId) {
        ClearAllHome();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x = 0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();
                        Log.d("Debug", "Step 1: " + contactNumber);

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            // You don't need a separate ValueEventListener for each shopRef, so move it outside the loop
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String status = keySnapshot.child("status").getValue(String.class);
                                        Log.d("Debug", "Step 2: " + status);

                                        if (!"Rejected".equals(status)) { // Avoid potential NPE by checking status for null
                                            String commIds = keySnapshot.child("Comm").getValue(String.class);
                                            if (commIds != null) {
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
                                            postView.setLayoutManager(new LinearLayoutManager(CommInfoGlobal.this));
                                            homeMultiAdapter = new HomeMultiAdapter(homeItemList, CommInfoGlobal.this, CommInfoGlobal.this);
                                            postView.setAdapter(homeMultiAdapter);
                                            homeMultiAdapter.notifyDataSetChanged();

                                            System.out.println("Data loaded successfully. Count:   " + homeItemList.size());

                                            Log.d("Debug", "Data loaded successfully. Count: " + homeItemList.size());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                    // Handle onCancelled
                                    Log.e("Error", "Firebase onCancelled: " + error.getMessage());
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                Log.e("Error", "Firebase onCancelled: " + error.getMessage());
            }
        });
    }


    public void Method2(String targetCommunityId) {
        ClearAllHome();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x = 0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();
                        Log.d("Debug", "Step 1: " + contactNumber);
                        System.out.println("Data loaded successfully. Count:1   " + contactNumber);
                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            // You don't need a separate ValueEventListener for each shopRef, so move it outside the loop
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String status = keySnapshot.child("status").getValue(String.class);
                                        //System.out.println("Data loaded successfully. Count:2   " + status);
                                        if(!"Rejected".equals(status)){
                                            String commIds = keySnapshot.child("Comm").getValue(String.class);
                                            if (commIds != null) {
                                                String[] commArray = commIds.split("&&");
                                                for (String commId : commArray) {
                                                    System.out.println("Data loaded successfully. Count:2   " + commId);
                                                    if (commId.equals(targetCommunityId)) {

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
                                                        String postComm = keySnapshot.child("Comm").getValue(String.class);

                                                        // Concatenate values into a single string
                                                        String allValues = "Shop Name: " + shopname +
                                                                "\nShop Image URL: " + shopimagex +
                                                                "\nShop Address: " + shopaddress +
                                                                "\nPost Image URL: " + postImg +
                                                                "\nPost Description: " + postDesc +
                                                                "\nPost Type: " + postType +
                                                                "\nPost Keys: " + postKeys +
                                                                "\nPost Category: " + postCate +
                                                                "\nServing Area: " + servArea +
                                                                "\nVisibility Count: " + visibilityCount +
                                                                "\nClick Count: " + clickCount;

                                                        System.out.println(allValues);

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
                                                        postModel.setPostComm(postComm);
                                                        postModel.setCommId(targetCommunityId);
                                                        postModel.setCurrConNum(commAdmin);

                                                        Log.d("gswvddv", "" + targetCommunityId);

                                                        Log.d("fsfsfdsdn", "" + shopname);

                                                        postModel.setPostUser(shopname);
                                                        postModel.setUserImg(shopimagex);
                                                        postModel.setUserAdd(shopaddress);

                                                        // Add the post to the list
                                                        homeItemList.add(postModel);

                                                        System.out.println("fsdfdfdfdfad "+homeItemList.size());
                                                        int i=0;
                                                        homeMultiAdapter = new HomeMultiAdapter(false);
                                                        postView.setLayoutManager(new LinearLayoutManager(CommInfoGlobal.this));
                                                        homeMultiAdapter = new HomeMultiAdapter(homeItemList, CommInfoGlobal.this,CommInfoGlobal.this);
                                                        postView.setAdapter(homeMultiAdapter);
                                                        RecyclerView.ViewHolder viewHolder = postView.findViewHolderForAdapterPosition(i);
                                                        if (viewHolder instanceof HomeMultiAdapter.PostItemViewHolder) {
                                                            postView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                                @Override
                                                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                                    super.onScrolled(recyclerView, dx, dy);

                                                                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                                                    if (layoutManager != null) {
                                                                        // Get the first visible item position and last visible item position
                                                                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                                                                        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                                                                        // Iterate through the visible items and update the visibility count on each post
                                                                        for (int i = 0; i < homeItemList.size(); i++) {
                                                                            Object post = homeItemList.get(i);
                                                                            if (post instanceof PostModel) {
                                                                                HomeMultiAdapter.PostItemViewHolder viewHolder = (HomeMultiAdapter.PostItemViewHolder) recyclerView
                                                                                        .findViewHolderForAdapterPosition(i);
                                                                                if (viewHolder != null && i < firstVisibleItemPosition
                                                                                        && i > lastVisibleItemPosition) {
                                                                                    // Reset the visibility count flag for posts that are not visible
                                                                                    viewHolder.resetVisibilityCountFlag();

                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }
                                                        homeMultiAdapter.notifyDataSetChanged();
//                                                        if (x++ == snapshotx.getChildrenCount() - 1) {
//                                                            // Set up the adapter and update the UI
//                                                            Log.i("fgdwgsdgsdgzd",""+homeItemList.size());
//                                                            //System.out.println("Data loaded successfully. Count:   " + homeItemList.size());
//
//                                                        }

                                                    }
                                                }

                                                Collections.shuffle(homeItemList);
                                            }
                                               // if (commId.equals(targetCommunityId)) {
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                    // Handle onCancelled
                                    Log.e("Error", "Firebase onCancelled: " + error.getMessage());
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                Log.e("Error", "Firebase onCancelled: " + error.getMessage());
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

    public void leftCommunity(String commId,String commName){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.leftcomm, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Disable scrolling for the BottomSheetDialog
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout left = bottomSheetView.findViewById(R.id.leavecomm);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(CommInfoGlobal.this)
                        // set message, title, and icon
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to leave the group?")
                        // .setIcon(R.drawable.logodelete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/commMembers/");
                                Task<Void> communityRef = databaseRef.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        SnackBarHelper.showSnackbar(CommInfoGlobal.this, "You left "+commName.toString()+" community.");
                                        checkExistence(commId,userId);
                                        notificationleft();
                                        bottomSheetDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@androidx.annotation.NonNull Exception e) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                myQuittingDialogBox.setCanceledOnTouchOutside(false);
                myQuittingDialogBox.setCancelable(false);
                myQuittingDialogBox.show();
            }
        });

        bottomSheetDialog.show();

    }
}