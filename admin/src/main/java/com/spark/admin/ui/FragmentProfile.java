package com.spark.admin.ui;

import static android.app.Activity.RESULT_OK;
import static com.spark.admin.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nex3z.notificationbadge.NotificationBadge;
import com.spark.admin.CreateCatalogList;
import com.spark.admin.Create_Profile;
import com.spark.admin.EditProfile;
import com.spark.admin.ItemList;
import com.spark.admin.LoginMain;
import com.spark.admin.MyOrders;
import com.spark.admin.NotificationPage;
import com.spark.admin.OrderLists;
import com.spark.admin.PomoteShop;
import com.spark.admin.Post;
import com.spark.admin.PostAdapter;
import com.spark.admin.R;
import com.spark.admin.Shop;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentProfile extends Fragment implements PostAdapter.PostClickListener{

    String shopcontactNumber, userId, shopimage, image, shopName;
    ImageView profileimage, notifiimage, notification;
    TextView username, verifytext, contacttext;
    NotificationBadge notificationcount, notificationBadge;
    RelativeLayout notificationcard;
    Uri croppedImageUri;
    private final int PICK_SINGLE_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private List<Post> postList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> itemList = new ArrayList<>(); // Create a list to store post details

    private PostAdapter postAdapter;
    List<Shop> shopList;
    Integer notificationCount;
    LinearLayout imagelayout;
    DatabaseReference shopRef;

    CardView editcard, catlogcard, promotedcard, orderscard, myorderscrd, logoutcard,
            notificatoncard, createprofilecard;
    private boolean hasLoggedIn = false;


    public FragmentProfile() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileimage = view.findViewById(R.id.profileimage);
        username = view.findViewById(R.id.text_name);
        editcard = view.findViewById(R.id.profiledetails);
        catlogcard = view.findViewById(R.id.catalog);
        promotedcard = view.findViewById(R.id.Promoteshop);
        orderscard = view.findViewById(R.id.orders);
        myorderscrd = view.findViewById(R.id.myorder);
        logoutcard = view.findViewById(R.id.message);
        verifytext = view.findViewById(R.id.verifytext);
        verifytext.setVisibility(View.GONE);
        imagelayout = view.findViewById(R.id.imagelayout);
        createprofilecard = view.findViewById(R.id.createprofilecard);
        contacttext = view.findViewById(R.id.text_contact);
        imagelayout.setVisibility(View.GONE);
        notification = view.findViewById(R.id.notificationimage);     // notification
        notificationcount = view.findViewById(R.id.badgecount); // notification count
        notificationBadge = view.findViewById(R.id.badge_count); // order count
        notificationcard = view.findViewById(R.id.notificationcard);
       // notification.setVisibility(View.GONE);
        notificationcount.setVisibility(View.GONE);
        catlogcard.setVisibility(View.GONE);

//        notificatoncard = view.findViewById(R.id.notificationcard);
//        notifiimage = view.findViewById(R.id.notifiimage);


        shopList = new ArrayList<>();

//        recyclerView = view.findViewById(R.id.itemdetails);
//        postAdapter = new PostAdapter(getContext(), itemList, this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(postAdapter);


//        profileimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_SINGLE_IMAGE_REQUEST);
//            }
//        });

//        notificatoncard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), NotificationPage.class);
//                startActivity(intent);
//
//                int count =0;
//                if (notificationCount>= 0){
//                    notifiimage.setVisibility(View.VISIBLE);
//                    count++;
//                }
//
//                notifiimage.setVisibility(View.GONE);
//            }
//        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationPage.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        notificationcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationPage.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        editcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        catlogcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateCatalogList.class);
                startActivity(intent);
            }
        });

        promotedcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PomoteShop.class);
                startActivity(intent);
            }
        });


        myorderscrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyOrders.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        createprofilecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Create_Profile.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        logoutcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                // Start the FirstPage activity and clear the task stack
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");

                // Logout button
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform logout action here (e.g., clear session, go to login page)
                        // Redirect to the login page
                        hasLoggedIn = false;
                        SharedPreferences settings = getActivity().getSharedPreferences(LoginMain.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("hasLoggedIn", false);
                        editor.apply();
                        Intent intent = new Intent(getContext(), LoginMain.class);
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



        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            contacttext.setText(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }


        orderscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderLists.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                intent.putExtra("shopimage",shopimage);
                intent.putExtra("shopName",shopName);
                System.out.println("jhfvbhj " +shopcontactNumber);
                System.out.println("jhfvbhj " +shopimage);
                startActivity(intent);
            }
        });


        retrievePostDetails();
        retrievecurrentuserItemDetails();
        return view;
    }


    private void retrievePostDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        System.out.println("efbf " +databaseReference);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Name = dataSnapshot.child("name").getValue(String.class);
                    String profileImage = dataSnapshot.child("profileimage").getValue(String.class);
                    System.out.println("dfjgbv " +profileImage);
                    shopcontactNumber = dataSnapshot.child("contactNumber").getValue(String.class);




                    username.setText(Name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Shop").child(userId);

        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  postList.clear(); // Clear the existing list before adding new data
                if (dataSnapshot.exists()) {
                     shopName = dataSnapshot.child("shopName").getValue(String.class);
                    shopimage = dataSnapshot.child("url").getValue(String.class);
                    System.out.println("dgbt " + shopimage);
                    shopcontactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    Boolean verify = dataSnapshot.child("profileverified").getValue(Boolean.class);
                    System.out.println("ruggj " + shopcontactNumber);
                    Integer noticationcount = dataSnapshot.child("count").child("notificationcount").getValue(Integer.class);
                    Integer noticationCount = dataSnapshot.child("notificationcount").getValue(Integer.class);
                    int ordercount = dataSnapshot.child("ordercount").getValue(Integer.class);
                    System.out.println("edgfvd " +ordercount);
//                    int orderCount = dataSnapshot.child("count").child("ordercount").getValue(Integer.class);

 //                   updateBadgeAndUI(orderCount);
                    updateBadgeAndUI(ordercount);

                    if (verify == true) {
                        verifytext.setVisibility(View.GONE);
                        catlogcard.setVisibility(View.VISIBLE);
                    } else {
                        verifytext.setVisibility(View.VISIBLE);
                        catlogcard.setVisibility(View.GONE);

                    }

                    if (noticationcount != null) {
                        if (noticationcount > 0) {
                            // Show the badge count and update it with the request count
                            notification.setVisibility(View.VISIBLE);
                            notificationcount.setVisibility(View.VISIBLE);
                            notificationcount.setText(noticationcount.toString());

                        } else {
                            // Hide the badge count when the request count is zero
                            notification.setVisibility(View.VISIBLE);
                            notificationcount.setVisibility(View.GONE);
                        }
                    }


                    imagelayout.setVisibility(View.VISIBLE);
                    image = dataSnapshot.child("url").getValue(String.class);
                    Log.d("TAG", "onDataChange: " + image);


                    if (isAdded() && getContext() != null) {
                        // Fragment is attached to an activity, and context is not null
                        Glide.with(requireContext()).load(image).into(profileimage);
                    } else {
                        // Handle the case where the fragment is not properly attached
                    }



                    profileimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Inflate the popup layout
                            View popupView = LayoutInflater.from(getContext()).inflate(R.layout.activity_full_screen, null);

                            // Find the ImageView in the popup layout
                            ImageView imageView = popupView.findViewById(R.id.popup_image_view);
                            ImageView cancelimageview = popupView.findViewById(R.id.close_image_view);

                            // Load the image into the ImageView using Glide
                            Glide.with(getContext())
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

                    editcard.setVisibility(View.VISIBLE);
                    //catlogcard.setVisibility(View.VISIBLE);
                    promotedcard.setVisibility(View.VISIBLE);
                    orderscard.setVisibility(View.VISIBLE);
                    myorderscrd.setVisibility(View.VISIBLE);
                    createprofilecard.setVisibility(View.GONE);
                    notificationcard.setVisibility(View.VISIBLE);
                } else {
                    editcard.setVisibility(View.GONE);
                    catlogcard.setVisibility(View.GONE);
                    promotedcard.setVisibility(View.GONE);
                    orderscard.setVisibility(View.GONE);
                    notificationcard.setVisibility(View.GONE);
                    myorderscrd.setVisibility(View.VISIBLE);
                    createprofilecard.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
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
//            int colors = getResources().getColor(R.color.colorAccent);

            if (isAdded() && getContext() != null) {
                // Fragment is attached to an activity, and context is not null
                int color = ContextCompat.getColor(requireContext(), R.color.colorAccent);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }

       //     drawable.setColorFilter(colors, PorterDuff.Mode.SRC_ATOP);

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

    private void retrievecurrentuserItemDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Shop").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear(); // Clear the existing list before adding new data
                if (dataSnapshot.exists()){

                    String shopName = dataSnapshot.child("shopName").getValue(String.class);
                    String shopimage = dataSnapshot.child("url").getValue(String.class);
                    shopcontactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    String destrict = dataSnapshot.child("district").getValue(String.class);
                    notificationCount = dataSnapshot.child("notificationcount").getValue(Integer.class);
                    System.out.println("rgdfg "+notificationCount);
                    System.out.println("rgdfg "+shopName);
                    // postAdapter.setShopName(shopName);

                    DataSnapshot itemsSnapshot = dataSnapshot.child("items");
                    for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                        String itemkey = itemSnapshot.getKey();

                        String itemName = itemSnapshot.child("itemname").getValue(String.class);
                        String price = itemSnapshot.child("price").getValue(String.class);
                        String description = itemSnapshot.child("description").getValue(String.class);
                        String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
                        System.out.println("jfhv " +firstimage);

                        if (TextUtils.isEmpty(firstimage)) {
                            // Set a default image URL here
                            firstimage = String.valueOf(R.drawable.ic_outline_shopping_bag_24);
                        }

                        List<String> imageUrls = new ArrayList<>();
                        DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                        for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                            String imageUrl = imageUrlSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                            }
                        }

                        ItemList item = new ItemList(shopName,shopimage,shopcontactNumber, itemName,
                                price, description, firstimage, itemkey, imageUrls, destrict);
                        itemList.add(item);
                    }

                }
                Collections.shuffle(itemList);
                // Notify the adapter that the data has changed
//                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    }


    private void startImageCropper(Uri sourceUri) {
        File cacheDir = requireActivity().getCacheDir();
        Uri destinationUri = Uri.fromFile(new File(cacheDir, "cropped_image"));

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Set the aspect ratio (square in this case)
                .withMaxResultSize(300, 300) // Set the max result size
               .start(requireContext(),FragmentProfile.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_SINGLE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                croppedImageUri = imageUri;
                startImageCropper(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            croppedImageUri = UCrop.getOutput(data);

            if (croppedImageUri != null) {
                // Display the cropped image
                profileimage.setImageURI(croppedImageUri);

                // Upload the cropped image to Firebase Storage and associate it with the user's contactNumber
                uploadCroppedImageToFirebase(croppedImageUri, shopcontactNumber);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle any errors that occurred during cropping
            Throwable error = UCrop.getError(data);
        }
    }

    private void uploadCroppedImageToFirebase(Uri croppedImageUri, String userContactNumber) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference profileImageRef = storageReference.child("profile_images/" + userContactNumber + ".jpg");

        profileImageRef.putFile(croppedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // You can get the download URL and store it in the user's data in the Firestore database or Realtime Database
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        DatabaseReference imageRef = databaseReference.child("Users").child(shopcontactNumber).child("profileimage");
                        imageRef.setValue(downloadUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors during image upload
                });
    }

    @Override
    public void onClickClick(int position) {

    }

    @Override
    public void onContactClick(int position) {

    }

    @Override
    public void onorderClick(int position) {

    }

    @Override
    public void oncallClick(int position) {

    }

    @Override
    public void onseeallClick(int position) {

    }
}
