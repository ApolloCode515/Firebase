package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Date;
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

    ImageView commImgx,joinImg;

    TextView name,desc,mbrcnt,jointext;

    CardView invite,share;

    String commLinks;

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

        postView=findViewById(R.id.globalpostview);

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
       // Toast.makeText(this, ""+commId, Toast.LENGTH_SHORT).show();

        checkExistence(commId,userId);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //checkExistence(commId,commAdmin);
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

        Method2(commId);

   }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
                    return Bitmap.createBitmap(BitmapFactory.decodeStream(input));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                //shareToWhatsApp(result);
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
                                            postView.setLayoutManager(new LinearLayoutManager(CommInfoGlobal.this));
                                            homeMultiAdapter = new HomeMultiAdapter(homeItemList, CommInfoGlobal.this);
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

                                                        Log.d("fsfsfdsdn", "" + shopname);

                                                        postModel.setPostUser(shopname);
                                                        postModel.setUserImg(shopimagex);
                                                        postModel.setUserAdd(shopaddress);

                                                        // Add the post to the list
                                                        homeItemList.add(postModel);

                                                        System.out.println("fsdfdfdfdfad "+homeItemList.size());

                                                        postView.setLayoutManager(new LinearLayoutManager(CommInfoGlobal.this));
                                                        homeMultiAdapter = new HomeMultiAdapter(homeItemList, CommInfoGlobal.this);
                                                        postView.setAdapter(homeMultiAdapter);
                                                        homeMultiAdapter.notifyDataSetChanged();

//                                                        if (x++ == snapshotx.getChildrenCount() - 1) {
//                                                            // Set up the adapter and update the UI
//                                                            Log.i("fgdwgsdgsdgzd",""+homeItemList.size());
//                                                            //System.out.println("Data loaded successfully. Count:   " + homeItemList.size());
//
//                                                        }

                                                    }
                                                }
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
}