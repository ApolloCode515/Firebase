package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nex3z.notificationbadge.NotificationBadge;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class Profile extends AppCompatActivity {

    TextView nameTextView, shopname, emailTextView, phoneNumberTextView, requests, verifytext;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersRef, shopRef;
    ImageView back, profileimage, dialogprofileimage;
    Button cancelButton;
    CardView profiledetails, contactrequests, catlog, promoteshop, orders, boost, myorders, logout, addpost;
    String phoneNumber, userId;
    int ordercount;
    LinearLayout imagelayout;

    private RecyclerView requestRecyclerView;
    private List<Request> requestList;
    // Firebase Storage reference
    private StorageReference storageReference;
    // Constants for image upload
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog progressDialog;
    private static final int CROP_IMAGE_REQUEST = 2;
    private Uri selectedImageUri;
    private AlertDialog profileImageDialog; // Declare the AlertDialog variable at the class level
    public NotificationBadge notificationBadge;
    private static final String USER_ID_KEY = "userID";
    // Initialize hasLoggedIn as false by default
    private boolean hasLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.text_name);
        emailTextView = findViewById(R.id.text_email);
        phoneNumberTextView = findViewById(R.id.text_contact);
        shopname = findViewById(R.id.text_shopname);
        back = findViewById(R.id.back);
        //requests = findViewById(R.id.requests);
        profileimage = findViewById(R.id.profileimage);
        profiledetails = findViewById(R.id.profiledetails);
        contactrequests = findViewById(R.id.contactrequests);
        catlog = findViewById(R.id.catalog);
        promoteshop = findViewById(R.id.Promoteshop);
        orders = findViewById(R.id.orders);
        boost = findViewById(R.id.boost);
        myorders = findViewById(R.id.myorder);
        logout = findViewById(R.id.message);
        notificationBadge = findViewById(R.id.badge_count);
        imagelayout = findViewById(R.id.imagelayout);
        verifytext = findViewById(R.id.verifytext);
        addpost = findViewById(R.id.mypost);
       // message.setVisibility(View.GONE);
        myorders.setVisibility(View.GONE);
        profiledetails.setVisibility(View.GONE);
        catlog.setVisibility(View.GONE);
        promoteshop.setVisibility(View.GONE);
        orders.setVisibility(View.GONE);
        boost.setVisibility(View.GONE);
        addpost.setVisibility(View.GONE);
        imagelayout.setVisibility(View.GONE);

        // Initialize Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");

        // Initialize RecyclerView for requests
//        requestRecyclerView = findViewById(R.id.requestsdetails);
//        requestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        requestList = new ArrayList<>();
//        requestsAdapter = new RequestAdapter(requestList, ContactRequest.this); // Initialize the adapter before using it
//        requestRecyclerView.setAdapter(requestsAdapter);





        // Query the Users node for the user with the matching contact number
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String contactNumber = user.getContactNumber();
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        phoneNumber = dataSnapshot.child("contactNumber").getValue(String.class);

                        nameTextView.setText(name);
                        emailTextView.setText(email);
                        phoneNumberTextView.setText("+91 - " + phoneNumber);


//                        String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class);
//                        if (profileImageUrl != null) {
//                            // Use Glide or Picasso to load the image into the ImageView
//                            Glide.with(Profile.this).load(profileImageUrl).into(profileimage);
//                            // Picasso.get().load(profileImageUrl).into(profileImageView); // Use this line if using Picasso
//                        } else {
//                            // If there's no profile image URL, you can set a default image
//                            Glide.with(Profile.this).load(R.drawable.ic_outline_person_2).into(profileimage);
//                            // Picasso.get().load(R.drawable.default_profile_image).into(profileImageView); // Use this line if using Picasso
//                        }

                        shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot shopSnapshot) {
                                if (shopSnapshot.exists()) {
                                    String name = shopSnapshot.child("name").getValue(String.class);
                                    Boolean verify = shopSnapshot.child("profileverified").getValue(Boolean.class);
                                    System.out.println("ruggj " +verify);

                                    if (verify==true){
                                        verifytext.setVisibility(View.GONE);
                                    }else {
                                        verifytext.setVisibility(View.VISIBLE);
                                    }

                                     imagelayout.setVisibility(View.VISIBLE);
                                    String image = shopSnapshot.child("url").getValue(String.class);
                                    Log.d("TAG", "onDataChange: " +image);
                                    Glide.with(Profile.this).load(image).into(profileimage);

                                    profileimage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // Inflate the popup layout
                                            View popupView = LayoutInflater.from(Profile.this).inflate(R.layout.activity_full_screen, null);

                                            // Find the ImageView in the popup layout
                                            ImageView imageView = popupView.findViewById(R.id.popup_image_view);
                                            ImageView cancelimageview = popupView.findViewById(R.id.close_image_view);

                                            // Load the image into the ImageView using Glide
                                            Glide.with(Profile.this)
                                                    .load(image)
                                                    .into(imageView);

                                            // Calculate the width and height of the popup window
                                            int width = LinearLayout.LayoutParams.MATCH_PARENT;
                                            int height = LinearLayout.LayoutParams.MATCH_PARENT;

                                            // Create the PopupWindow and set its properties
                                            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                                            popupWindow.setOutsideTouchable(true);
                                            popupWindow.setFocusable(true);

                                            // Show the popup window at the center of the anchor view
                                            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                                            cancelimageview.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // Close the popup window when cancelimageview is clicked
                                                    if (popupWindow != null && popupWindow.isShowing()) {
                                                        popupWindow.dismiss();
                                                    }
                                                }
                                            });

                                        }
                                    });



                                    profiledetails.setVisibility(View.VISIBLE);
                                    catlog.setVisibility(View.VISIBLE);
                                    promoteshop.setVisibility(View.VISIBLE);
                                    orders.setVisibility(View.VISIBLE);
                                    boost.setVisibility(View.VISIBLE);
                                    myorders.setVisibility(View.VISIBLE);
                                    addpost.setVisibility(View.VISIBLE);
                                } else {
                                    profiledetails.setVisibility(View.GONE);
                                    catlog.setVisibility(View.GONE);
                                    promoteshop.setVisibility(View.GONE);
                                    orders.setVisibility(View.GONE);
                                    boost.setVisibility(View.GONE);
                                    addpost.setVisibility(View.GONE);
                                    myorders.setVisibility(View.VISIBLE);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
//                        Query shopQuery = shopRef.orderByChild("contactNumber").equalTo(contactNumber);
//                        shopQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    DataSnapshot shopSnapshot = dataSnapshot.getChildren().iterator().next();
//                                    String shopId = shopSnapshot.getKey();
//
//                                    // Retrieve the shop name
//                                    String shopName = shopSnapshot.child("contactNumber").getValue(String.class);
//                                    shopname.setText(shopName); // Set the shop name to the shopname TextView
//
//                                    // Query the Requests node under the shop
//                                    DatabaseReference requestsRef = shopRef.child(shopId).child("requests");
//                                    requestsRef.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            // requestList.clear();
//                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                                String requestId = snapshot.getKey();
//                                                String userId = snapshot.getValue(String.class);
//
//                                                if (userId != null && userId.equals("blank")) {
//                                                    // Create a Request object and add it to the list
//                                                    Request request = new Request(requestId, userId);
//                                                    //requestList.add(request);
//                                                    // requests.setVisibility(View.VISIBLE);
//                                                }
//                                            }
//
//                                            // Create the RequestAdapter instance with the shopName parameter
////                                            requestsAdapter = new RequestAdapter(requestList, ContactRequest.this, contactNumber); // Assuming Profile implements RequestAdapter.RequestListener
////                                            requestRecyclerView.setAdapter(requestsAdapter);
////
////                                            // Notify the adapter about the data change
////                                            requestsAdapter.notifyDataSetChanged();
//
//                                            // Reload the requests node with non-blank user IDs
//                                            DatabaseReference requestsRef = shopRef.child(shopId).child("requests");
//                                            Query blankRequestsQuery = requestsRef.orderByValue().equalTo("blank");
//
//                                            blankRequestsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    if (dataSnapshot.exists()) {
//                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                                            String requestId = snapshot.getKey();
//                                                            String userId = snapshot.getValue(String.class);
//
//                                                            if (userId != null && userId.equals("blank")) {
//                                                                // Update the user ID to a non-blank value
//                                                                //DatabaseReference requestRef = requestsRef.child(requestId);
//                                                                //requestRef.setValue("non-blank value");
//                                                            }
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError error) {
//
//                                                }
//                                            });
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                            // Handle database error
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                // Handle database error
//                            }
//                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });




//        // Initialize Firebase Storage
//        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
//        // Initialize the ProgressDialog
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//
//        // Set a click listener on the profile image view to allow users to change their profile picture
//        profileimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showProfileImageDialog();
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Business.class));
                finish();
            }
        });

        profiledetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // ContactNumber exists, navigate to CreateCatalogList activity
                            startActivity(new Intent(getApplicationContext(), EditProfile.class));
                        } else {
                            // ContactNumber does not exist, show shop profile not created message
                            Toast.makeText(getApplicationContext(), "Shop profile not created.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }
        });

        contactrequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // ContactNumber exists, navigate to CreateCatalogList activity
                            startActivity(new Intent(getApplicationContext(), ContactRequests.class));
                        } else {
                            // ContactNumber does not exist, show shop profile not created message
                            Toast.makeText(getApplicationContext(), "Shop profile not created.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }
        });

        catlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if contactNumber exists in Firebase under "Shop" node
                // DatabaseReference shopRef = firebaseDatabase.getReference("Shop");
                shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // ContactNumber exists, navigate to CreateCatalogList activity
                            startActivity(new Intent(getApplicationContext(), CreateCatalogList.class));
                        } else {
                            // ContactNumber does not exist, show shop profile not created message
                            Toast.makeText(getApplicationContext(), "Shop profile not created.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }
        });

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if contactNumber exists in Firebase under "Shop" node
                // DatabaseReference shopRef = firebaseDatabase.getReference("Shop");
                shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // ContactNumber exists, navigate to CreateCatalogList activity
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        } else {
                            // ContactNumber does not exist, show shop profile not created message
                            Toast.makeText(getApplicationContext(), "Shop profile not created.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }
        });

        promoteshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if contactNumber exists in Firebase under "Shop" node
                // DatabaseReference shopRef = firebaseDatabase.getReference("Shop");
                shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // ContactNumber exists, navigate to CreateCatalogList activity
                            startActivity(new Intent(getApplicationContext(), PomoteShop.class));
                        } else {
                            // ContactNumber does not exist, show shop profile not created message
                            Toast.makeText(getApplicationContext(), "Shop profile not created.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // ContactNumber exists, navigate to CreateCatalogList activity
                            startActivity(new Intent(getApplicationContext(), OrderLists.class));
                        } else {
                            // ContactNumber does not exist, show shop profile not created message
                            Toast.makeText(getApplicationContext(), "Shop profile not created.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }
        });

        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyOrders.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                // Start the FirstPage activity and clear the task stack
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");

                // Logout button
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform logout action here (e.g., clear session, go to login page)
                        // Redirect to the login page
                        hasLoggedIn = false;
                        SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("hasLoggedIn", false);
                        editor.apply();
                        Intent intent = new Intent(Profile.this, LoginMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                // Cancel button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog (do nothing)
                        dialog.dismiss();
                    }
                });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        boost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // ContactNumber exists, navigate to CreateCatalogList activity
                            try {
                                // Create an Intent with the ACTION_SEND action
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);

                                // Set the type of content you want to share
                                sendIntent.setType("text/plain");

                                // Add your app link and message to the Intent
                                String message = "Check out This app: https://play.google.com/store/apps/details?id=com.spark.swarajyabiz&hl=en-IN&pli=1";
                                sendIntent.putExtra(Intent.EXTRA_TEXT, message);

                                // If you want to share an image (logo), you can do it like this:
                                // Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.your_logo);
                                // sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                // sendIntent.setType("image/*");

                                // Set the package name for WhatsApp
                                sendIntent.setPackage("com.whatsapp");

                                // Start the Intent
                                startActivity(sendIntent);
                            } catch (Exception e) {
                                // If WhatsApp is not installed, handle the exception here
                                Toast.makeText(getApplicationContext(), "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // ContactNumber does not exist, show shop profile not created message
                            Toast.makeText(getApplicationContext(), "Shop profile not created.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    shopRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int ordercount = dataSnapshot.child("ordercount").getValue(Integer.class);
                                updateBadgeAndUI(ordercount);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle database error if needed
                        }
                    });

                    shopRef.child(phoneNumber).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int ordercount = dataSnapshot.child("ordercount").getValue(Integer.class);
                                updateBadgeAndUI(ordercount);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle database error if needed
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });

    }

    private void updateBadgeAndUI(int ordercount) {
        // Update your UI elements based on the new ordercount value
        if (ordercount > 0) {
            // Display the badge count for promoted shops
            notificationBadge.setVisibility(View.VISIBLE);
            notificationBadge.setText(String.valueOf(ordercount));

            // Create a custom Drawable with a solid background color
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            int colorResource = R.color.colorAccent;
            int color = ContextCompat.getColor(Profile.this, colorResource);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            drawable.setCornerRadius(10);

            // Set the background color of the badge
            notificationBadge.setBadgeBackgroundDrawable(drawable);

            // Set the text color to white
            notificationBadge.setTextColor(Color.WHITE);
        } else {
            // Hide the badge if the count is zero or negative
            notificationBadge.setVisibility(View.GONE);
        }
    }
    // Method to show the profile image dialog with options (Add, Update, Delete)
    private void showProfileImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_image_options, null);
        builder.setView(dialogView);

        // Buttons in the dialog
        cancelButton = dialogView.findViewById(R.id.btn_cancel);
        Button saveButton = dialogView.findViewById(R.id.btn_save);
        dialogprofileimage = dialogView.findViewById(R.id.profileimage);

        dialogprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
        // Set click listeners for the buttons
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageDialog.dismiss();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImageUri != null) {
                    uploadImageToFirebase(selectedImageUri);
                }
                profileImageDialog.dismiss();
            }
        });

        // Show the dialog
        profileImageDialog = builder.create();
        profileImageDialog.show();
    }

    // Helper method to open the image chooser when the profile image is clicked
//    private void openImageChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            dialogprofileimage.setImageURI(selectedImageUri); // Set the selected image to the ImageView
        }
    }

    // Upload the selected image to Firebase Storage with reduced quality


    private void uploadImageToFirebase(Uri imageUri) {
        // Create and show the progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating profile image...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (imageUri != null) {
            try {
                // Reduce image quality using bitmap compression
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos); // Adjust the compression quality here (0-100)
                byte[] data = baos.toByteArray();

                final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

                fileReference.putBytes(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        // Get the download URL of the uploaded image and store it in the Realtime Database
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        if (firebaseUser != null) {
                                            String userId = firebaseUser.getUid();
                                            usersRef.child(userId).child("profileImage").setValue(downloadUri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Profile image URL successfully saved in the Realtime Database
                                                            // You can also update the ImageView here to display the new profile image
                                                            updateProfileImage(downloadUri.toString()); // Update the ImageView with the new image
                                                            Log.d("Profile", "Profile image URL saved to Realtime Database: " + downloadUri.toString());

                                                            // Dismiss the progress dialog as the profile image is displayed
                                                            progressDialog.dismiss();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e("Profile", "Error saving profile image URL to Realtime Database: " + e.getMessage());
                                                            // Dismiss the progress dialog on failure
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Profile", "Error uploading profile image to Firebase Storage: " + e.getMessage());
                                // Dismiss the progress dialog on failure
                                progressDialog.dismiss();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
                // Dismiss the progress dialog on failure
                progressDialog.dismiss();
            }
        } else {
            // Dismiss the progress dialog if there is no image to upload
            progressDialog.dismiss();
        }
    }



    // Helper method to get the file extension from the URI
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Method to update the profile image in the ImageView after adding or updating it
    private void updateProfileImage(String imageUrl) {
        Glide.with(this).load(imageUrl).into(profileimage);
        // Picasso.get().load(imageUrl).into(profileImageView); // Use this line if using Picasso
    }

    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        finish();
    }

}