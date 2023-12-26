package com.spark.swarajyabiz;


import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.annotations.NonNull;


public class ShopDetails extends AppCompatActivity implements ImageAdapter.ImageClickListener{
    private static final int PICK_IMAGE_MULTIPLE = 20;

    private TextView nameTextView, shopNameTextView, addressTextView, contacttext, districttextview, talukatextview,
            seealltextview, seeallshop, call, verifytextview, premiumtextview;
    ImageView imageView, whatsapp, back, callimageview;
    CardView buttons, request, cancel, services, linkedprofile, photos, profileverify, premiumcard;
    RatingBar rating_bar,Avgrating_bar;
    TextView average_rating_text_view,total_rating_text_view;
    TextView Timetextview, addimage, phonetextview, contactview, emailtextview, servicetextview;
    private Uri filePath;
    FirebaseStorage storage;
    private List<Uri> filePathList;
    StorageReference storageReference;
    private DatabaseReference shopRef; // DatabaseReference for the shop node
    private DatabaseReference ratingRef; // DatabaseReference for the rating node

    private List<Integer> ratings = new ArrayList<>();
    private int[] ratingCounts = new int[5];
    private int totalRating = 0;
    TextView giverating;
    private int userCount = 0;

    private TextView totalRatingTextView;
    private TextView averageRatingTextView;

    private DatabaseReference databaseReference;

    private static final int PICK_IMAGES_REQUEST_CODE = 1;
    private static final String TAG = "FirebaseUpload";

    private List<String> imageUrls;
    private ImageAdapter imageAdapter;
    private static final Pattern TIME_RANGE_PATTERN = Pattern.compile("^(\\d{1,2})(?::(\\d{2}))?\\s*(AM|PM)\\s*-\\s*(\\d{1,2})(?::(\\d{2}))?\\s*(AM|PM)$");
    String userId, shopId;
    private ImageButton getDirectionButton;
    private DatabaseReference userRatingsRef, userRef;
    private FirebaseUser currentUser;
    private Button  cancelbutton;
    CardView requestbutton;
    private boolean isRequestAccepted = false;
    RecyclerView productrecyclerView, recyclerViewShops;
    PromoteAdapter shopAdapter;
    private List<PromoteShop> shopList;
    private List<ItemList> itemList;
    FloatingActionButton callfloatingButton;
    String imageUrl;
    Shop shopdata;
    private static final String USER_ID_KEY = "userID";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetails);

        nameTextView = findViewById(R.id.name);
        shopNameTextView = findViewById(R.id.ShopName);
        addressTextView = findViewById(R.id.address);
        imageView = findViewById(R.id.profileimage);
        emailtextview = findViewById(R.id.emailtextview);
        //servicetextview = findViewById(R.id.servicetextview);
        contacttext = findViewById(R.id.contacttext);
        districttextview = findViewById(R.id.districttext);
        talukatextview = findViewById(R.id.talukatext);
        //addimage = findViewById(R.id.addimage);
        //statusTextView= findViewById(R.id.statusTextView);
        contactview = findViewById(R.id.contacttextview);
        phonetextview = findViewById(R.id.phonetextview);
        call = findViewById(R.id.call);
        whatsapp = findViewById(R.id.whatsapp);
        //  back = findViewById(R.id.back);
        //getDirectionButton = findViewById(R.id.getDirectionButton);
        requestbutton = findViewById(R.id.requestbutton);
        cancelbutton = findViewById(R.id.cancelbutton);
        giverating = findViewById(R.id.giverating);
        productrecyclerView = findViewById(R.id.viewitems);
        buttons = findViewById(R.id.contactbuttons);
        //request = findViewById(R.id.request);
        cancel = findViewById(R.id.cancel);
        seealltextview = findViewById(R.id.seeall);
        seeallshop = findViewById(R.id.seeallshop);
        services = findViewById(R.id.services);
        linkedprofile = findViewById(R.id.linked_profiles);
        photos = findViewById(R.id.Photos);
        callfloatingButton = findViewById(R.id.callfloatbutton);
        profileverify = findViewById(R.id.profileverify);
        premiumcard = findViewById(R.id.premium);
        verifytextview = findViewById(R.id.verifytextview);
        premiumtextview = findViewById(R.id.premiumtextview);

        seealltextview.setVisibility(View.GONE);
        seeallshop.setVisibility(View.GONE);
        services.setVisibility(View.GONE);
        linkedprofile.setVisibility(View.GONE);
        photos.setVisibility(View.GONE);
        requestbutton.setVisibility(View.GONE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRatingsRef = database.getReference().child("UserRatings");
        userRef = database.getReference().child("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

      /*  if (currentUser != null) {
            String userIds = currentUser.getUid();
            // Use the userId as needed
        } else {
            // Handle the case when the current user is null
        }
*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Initialize RecyclerView
        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));

        // Create an instance of the ImageAdapter
        ImageAdapter imageAdapter = new ImageAdapter(this, imageUrls, this, imageRecyclerView);
        imageRecyclerView.setAdapter(imageAdapter);


       recyclerViewShops = findViewById(R.id.RecyclerView);
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(this));
        // Initialize shopList
         shopList = new ArrayList<>();
        Set<Integer> promotedShopPositions = new HashSet<>();
        PromoteAdapter shopAdapter = new PromoteAdapter(this, shopList,promotedShopPositions, sharedPreference);
        // shopAdapter = new PromoteAdapter(this,filteredList);
        recyclerViewShops.setAdapter(shopAdapter);



        seeallshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PromotedShops.class));
            }
        });



         shopId = getIntent().getStringExtra("contactNumber");
         System.out.println("sdfjf " + shopId);
         Intent intent = new Intent(ShopDetails.this, EditProfile.class);
         intent.putExtra("shopcontactnumber" , shopId);



        // Retrieve the shop details from Firebase Realtime Database
        shopRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId);

        shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the shop exists in the database
                if (dataSnapshot.exists()) {
                    // Loop through the result (should have only one shop
                    for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve the shop details
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String shopName = dataSnapshot.child("shopName").getValue(String.class);
                        String address = dataSnapshot.child("address").getValue(String.class);
                        String contact = dataSnapshot.child("contactNumber").getValue(String.class);
                        String phone = dataSnapshot.child("phoneNumber").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String shopimage = dataSnapshot.child("url").getValue(String.class);
                        String service = dataSnapshot.child("service").getValue(String.class);
                        String taluka = dataSnapshot.child("taluka").getValue(String.class);
                        String district = dataSnapshot.child("district").getValue(String.class);
                        Boolean profileverification = dataSnapshot.child("profileverified").getValue(Boolean.class);
                        Boolean premium = dataSnapshot.child("premium").getValue(Boolean.class);
                        int promotedShopCount = dataSnapshot.child("promotionCount").getValue(Integer.class);

                        if (profileverification==true){
                            verifytextview.setText("Verified");
                        }else if (profileverification == false){
                            verifytextview.setText("Profile Verification");
                        }

                        if (premium==true){
                            premiumtextview.setText("Premium(Y)");
                        }else if (premium == false){
                            premiumtextview.setText("Premium(N)");
                        }


                        // Retrieve the image URLs from Firebase
                        DataSnapshot imageUrlsSnapshot = dataSnapshot.child("imageUrls");
                        List<String> imageUrls = new ArrayList<>();
                        for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                            String imageUrl = imageUrlSnapshot.getValue(String.class);
                            imageUrls.add(imageUrl);
                            Log.d("ImageURL", imageUrl); // Print the URL for debugging
                        }

                        if (imageUrls.isEmpty()) {
                            photos.setVisibility(View.GONE);
                        } else {
                         //   photos.setVisibility(View.VISIBLE);
                        }

                        Shop shop = new Shop();
                        // Update the image URLs in the Shop object
                        shop.setImageUrls(imageUrls);

                        // Notify the adapter about the data change
                        imageAdapter.setImageUrls(imageUrls);
                        imageAdapter.notifyDataSetChanged();

                        GridView gridView = findViewById(R.id.viewproduct);
                        List<ItemList> itemList = new ArrayList<>();  // Initialize your itemList
                        ProductAdapter adapter = new ProductAdapter(ShopDetails.this, itemList);
                        gridView.setAdapter(adapter);

                        callfloatingButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Get the phone number from the tag
                             //   String phoneNumber = (String) callImageView.getTag();

                                // Create an Intent to initiate a phone call
                                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));

                                // Start the phone call activity
                                startActivity(callIntent);
                            }
                        });

                        seealltextview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String contactNumber= getIntent().getStringExtra("contactNumber");
                                Intent intent = new Intent(getApplicationContext(), ShowAllItemsList.class);
                                intent.putExtra("contactNumber", contactNumber);
                                intent.putExtra("shopImage", imageUrl);
                                intent.putExtra("shopName", shopName);
                                startActivity(intent);
                            }
                        });


                        profileverify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Boolean profileverification = dataSnapshot.child("profileverified").getValue(Boolean.class);
                                System.out.println("fgrgvf " +profileverification);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference profileVerifiedRef = shopRef.child("profileverified");

                                if (profileverification!=null && profileverification==false){
                                    profileVerifiedRef.setValue(true);
                                    verifytextview.setText("Verified");
                                }else if (profileverification == true){
                                    profileVerifiedRef.setValue(false);
                                    verifytextview.setText("Profile Verification");
                                }


                            }
                        });

                        premiumcard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Boolean premium = dataSnapshot.child("premium").getValue(Boolean.class);
                                System.out.println("fgrgvf " +premium);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference premiumRef = shopRef.child("premium");

                                if (premium!=null && premium==false){
                                    premiumRef.setValue(true);
                                    premiumtextview.setText("Premium(Y)");
                                }else if (premium == true){
                                    premiumRef.setValue(false);
                                    premiumtextview.setText("Premium(N)");
                                }


                            }
                        });



                        shopRef.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                itemList.clear();  // Clear the list before populating with new data
                                int itemCounter = 0;  // Counter for limiting to 6 items
                                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                    // Limit the number of items to 6
                                    if (itemCounter >= 3) {
                                        break;
                                    }

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
                                    itemCounter++;
                                    // Retrieve the first image URL from imageUrls
                                    // String firstImageUrl = null;
//                                    DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
//                                    for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
//                                        firstImageUrl = imageUrlSnapshot.getValue(String.class);
//                                        break;  // Retrieve only the first image URL
//                                    }

                                    List<String> imageUrls = new ArrayList<>();
                                    DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                                    for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                        String imageUrl = imageUrlSnapshot.getValue(String.class);
                                        if (imageUrl != null) {
                                            imageUrls.add(imageUrl);
                                        }
                                    }


                                    System.out.println("ertgr " +shopId);
                                    ItemList item = new ItemList(shopName,shopimage, shopId, itemName, price, description, firstimage,
                                            itemkey, imageUrls, district, taluka,address);
                                    itemList.add(item);

                                    if (itemList.isEmpty()) {
                                        seealltextview.setVisibility(View.GONE);
                                        services.setVisibility(View.GONE);
                                    } else {
                                        seealltextview.setVisibility(View.VISIBLE);
                                        services.setVisibility(View.VISIBLE);
                                    }
                                }

                                adapter.notifyDataSetChanged();  // Notify the adapter about the changes
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error gracefully
                            }
                        });
                        // Set the number of columns to 3
                        //gridView.setNumColumns(3);

                        RecyclerView recyclerView = findViewById(R.id.viewshops);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ShopDetails.this, LinearLayoutManager.VERTICAL, false));
                        List<PromoteShop> shopList = new ArrayList<>();
                        PromotedShopAdapter promoteAdapter = new PromotedShopAdapter(ShopDetails.this, shopList);
                        recyclerView.setAdapter(promoteAdapter);

                        shopRef.child("promotedShops").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                shopList.clear();  // Clear the list before populating with new data
                                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {

                                    String name = shopSnapshot.child("name").getValue(String.class);
                                    String shopName = shopSnapshot.child("shopName").getValue(String.class);
                                    Log.d("FirebaseData", "shopName: " + shopName);
                                    String contactNumber = shopSnapshot.child("contactNumber").getValue(String.class);
                                    String address = shopSnapshot.child("address").getValue(String.class);
                                    String url = shopSnapshot.child("url").getValue(String.class);
                                    String service = shopSnapshot.child("service").getValue(String.class);
                                    String taluka = shopSnapshot.child("taluka").getValue(String.class);
                                    String district = shopSnapshot.child("district").getValue(String.class);
                                    System.out.println("retrive " + taluka);
                                   // boolean isShopPromoted = shopSnapshot.child(shopName).exists();

                                    // Create a Shop object and add it to the shop list
                                    PromoteShop shop = new PromoteShop(name, shopName, contactNumber, address, url, service, district, taluka
                                            , promotedShopCount);
                                    shopList.add(shop);
                                }

                                if (shopList.isEmpty()) {
                                    linkedprofile.setVisibility(View.GONE);
                                    seeallshop.setVisibility(View.GONE);
                                } else {
                                  //  linkedprofile.setVisibility(View.VISIBLE);
                                  //  seeallshop.setVisibility(View.VISIBLE);
                                }

//                                if (shopList.size() > 3) {
//                                    promoteAdapter.setItems(shopList.subList(0, 3)); // Initially show the first 3 items
//                                    // seeallshop.setVisibility(View.VISIBLE); // Show "See All" button
//                                } else {
//                                    promoteAdapter.setItems(shopList); // Show all items if there are 3 or fewer
//                                    // seeallshop.setVisibility(View.GONE); // Hide "See All" button
//                                }
                                promoteAdapter.notifyDataSetChanged();  // Notify the adapter about the changes

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error gracefully
                            }
                        });


                        // Assuming you have a Firebase Realtime Database reference
                        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId).child("requests");

                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String requestValue = dataSnapshot.getValue(String.class);
                                            if (requestValue != null && requestValue.equals("yes")) {
                                                // Create an intent to initiate a phone call
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(Uri.parse("tel:" + contact));

                                                // Start the phone app activity
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(ShopDetails.this, "Your request has not been accepted. Please wait.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(ShopDetails.this, "Please send a request first.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });
                            }
                        });

                        //DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId).child("requests");
                        whatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String requestValue = dataSnapshot.getValue(String.class);

                                            if (requestValue != null && requestValue.equals("yes")) {
                                                //String phoneNumber = contactview.getText().toString(); // Get the phone number from the contact view

                                                // Remove any non-digit characters from the phone number
                                                contact.replaceAll("\\D+", "");

                                                // Open the WhatsApp app with the specified phone number
                                                String url = "https://api.whatsapp.com/send?phone=" + contact;
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(url));

                                                // Verify that there is an activity available to handle the intent
                                                PackageManager packageManager = getPackageManager();
                                                if (intent.resolveActivity(packageManager) != null) {
                                                    startActivity(intent);
                                                } else {
                                                    // WhatsApp is not installed, show an error or redirect to Play Store for installation
                                                    Toast.makeText(ShopDetails.this, "WhatsApp is not installed on your device", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(ShopDetails.this, "Your request has not been accepted. Please wait.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(ShopDetails.this, "Please send a request first.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle any database error
                                    }
                                });
                            }
                        });


                        // Set the retrieved details to the TextViews
                        nameTextView.setText(name);
                        shopNameTextView.setText(shopName);
                        addressTextView.setText(address);
                        emailtextview.setText(email);
                        // servicetextview.setText(service);
                        districttextview.setText(district);
                        talukatextview.setText(taluka);

                        contactview.setText(contact);
                        if (phone != null && !phone.isEmpty()) {
                            contacttext.setVisibility(View.VISIBLE);
                            contacttext.setTextColor(Color.WHITE);
                            phonetextview.setVisibility(View.VISIBLE);
                            phonetextview.setText(phone);
                        } else {
                            contacttext.setVisibility(View.GONE);
                            phonetextview.setVisibility(View.GONE);
                        }

//                        if (contact.length() == 10) {
//                            String firstTwoChars = contact.substring(0, 2);
//                            String lastTwoChars = contact.substring(contact.length() - 2);
//                            String xChars = new String(new char[contact.length() - 4]).replace('\0', 'X');
//                            contactview.setText(firstTwoChars + xChars + lastTwoChars);
//                        }
//                        if (phone != null && !phone.isEmpty()) {
//                            // The phone number exists and is not empty
//                            contacttext.setVisibility(View.VISIBLE);
//                            phonetextview.setText(phone);
//
//                            if (phone.length() == 10) {
//                                // Format the phone number if it has 10 digits
//                                String firstTwoChars = phone.substring(0, 2);
//                                String lastTwoChars = phone.substring(phone.length() - 2);
//                                String xChars = new String(new char[phone.length() - 4]).replace('\0', 'X');
//                                phonetextview.setText(firstTwoChars + xChars + lastTwoChars);
//                            }
//                        } else {
//                            // The phone number is empty or null, hide the view or display a placeholder
//                            phonetextview.setVisibility(View.GONE); // Hide the view
//                            // OR
//                            // phonetextview.setText("N/A"); // Display a placeholder value
//                        }


//  ******************************** Contact Request code ***********************************************//


//                        request.setVisibility(View.VISIBLE);
//                        cancel.setVisibility(View.GONE);
//                        DatabaseReference userRequestRef = shopRef.child("requests").child(userId);
//                        requestbutton.setOnClickListener(new View.OnClickListener() {
//                                                             @Override
//                                                             public void onClick(View v) {
//                                                                 // Get the user ID of the currently logged-in user
//                                                                 // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                                                                 // String userId = firebaseUser.getUid();
//
//                                                                 // Check if the user has already sent a request
//
//                                                                 userRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                     @Override
//                                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                         if (dataSnapshot.exists()) {
//                                                                             String requestStatus = dataSnapshot.getValue(String.class);
//                                                                             if (requestStatus != null) {
//                                                                                 if (requestStatus.equals("no")) {
//                                                                                     // User can send a request again
//                                                                                     userRequestRef.setValue("blank")
//                                                                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                                                 @Override
//                                                                                                 public void onComplete(@NonNull Task<Void> task) {
//                                                                                                     if (task.isSuccessful()) {
//                                                                                                         Toast.makeText(ShopDetails.this, "Request sent again", Toast.LENGTH_SHORT).show();
//                                                                                                         isRequestAccepted = false; // Set request status to not accepted initially
//
//                                                                                                         // Change button text to "Requested"
//                                                                                                         requestbutton.setText("Requested");
//                                                                                                         requestbutton.setEnabled(false); // Optional: Disable the button after clicking
//                                                                                                         cancel.setVisibility(View.VISIBLE);
//                                                                                                     } else {
//                                                                                                         Toast.makeText(ShopDetails.this, "Failed to send request again", Toast.LENGTH_SHORT).show();
//                                                                                                     }
//                                                                                                 }
//                                                                                             });
//                                                                                 } else {
//                                                                                     // User has already sent a request
//                                                                                     Toast.makeText(ShopDetails.this, "You have already sent a request", Toast.LENGTH_SHORT).show();
//                                                                                 }
//                                                                             }
//                                                                         } else {
//                                                                             // User hasn't sent a request, create a new one
//                                                                             userRequestRef.setValue("blank")
//                                                                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                                         @Override
//                                                                                         public void onComplete(@NonNull Task<Void> task) {
//                                                                                             if (task.isSuccessful()) {
//                                                                                                 Toast.makeText(ShopDetails.this, "Request sent", Toast.LENGTH_SHORT).show();
//                                                                                                 isRequestAccepted = false; // Set request status to not accepted initially
//
//                                                                                                 // Change button text to "Requested"
//                                                                                                 requestbutton.setText("Requested");
//                                                                                                 requestbutton.setEnabled(false); // Optional: Disable the button after clicking
//                                                                                                 cancel.setVisibility(View.VISIBLE);
//                                                                                             } else {
//                                                                                                 Toast.makeText(ShopDetails.this, "Failed to send request", Toast.LENGTH_SHORT).show();
//                                                                                             }
//                                                                                         }
//                                                                                     });
//                                                                         }
//                                                                     }
//
//                                                                     @Override
//                                                                     public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                                         // Handle database error
//                                                                         Toast.makeText(ShopDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                     }
//                                                                 });
//
//                                                                 // Update the count for the contact number
//                                                                 DatabaseReference contactCountRef = shopRef.child("requestcount");
//                                                                 contactCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                     @Override
//                                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                         int count = 0;
//                                                                         if (dataSnapshot.exists()) {
//                                                                             // If the count already exists, get the current value
//                                                                             count = dataSnapshot.getValue(Integer.class);
//                                                                         }
//                                                                         count++; // Increment the count for the new request
//                                                                         // Store the updated count under the contact number
//                                                                         contactCountRef.setValue(count);
//                                                                     }
//
//                                                                     @Override
//                                                                     public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                                         // Handle database error
//                                                                         Toast.makeText(ShopDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                     }
//                                                                 });
//                                                             }
//                                                         });
//
//                        // Listen for changes in the request status
//                        userRequestRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    String requestStatus = dataSnapshot.getValue(String.class);
//                                    if (requestStatus != null) {
//                                        if (requestStatus.equals("yes")) {
//                                            // Request is accepted, change button text to "Accepted"
//                                            requestbutton.setText("Accepted");
//                                            // Request is accepted, show the full contact number
//                                            contactview.setText(contact);
//                                            phonetextview.setText(phone);
//                                            requestbutton.setEnabled(false); // Optional: Disable the button after request is accepted
//                                            call.setVisibility(View.VISIBLE);
//                                            request.setVisibility(View.GONE);
//                                        } else if (requestStatus.equals("no")) {
//                                            if (contact.length() == 10) {
//                                                String firstTwoChars = contact.substring(0, 2);
//                                                String lastTwoChars = contact.substring(contact.length() - 2);
//                                                String xChars = new String(new char[contact.length() - 4]).replace('\0', 'X');
//                                                contactview.setText(firstTwoChars + xChars + lastTwoChars);
//                                            }
//                                            if (phone.length() == 10) {
//                                                String firstTwoChars = phone.substring(0, 2);
//                                                String lastTwoChars = phone.substring(phone.length() - 2);
//                                                String xChars = new String(new char[phone.length() - 4]).replace('\0', 'X');
//                                                phonetextview.setText(firstTwoChars + xChars + lastTwoChars);
//                                            }
//                                            // Request is not accepted, keep button text as "Requested"
//                                            requestbutton.setText("Send Contact Request");
//                                            requestbutton.setEnabled(true); // Optional: Disable the button until request is accepted
//                                            request.setVisibility(View.VISIBLE);
//                                        } else if (requestStatus.equals("blank")) {
//                                            if (contact.length() == 10) {
//                                                String firstTwoChars = contact.substring(0, 2);
//                                                String lastTwoChars = contact.substring(contact.length() - 2);
//                                                String xChars = new String(new char[contact.length() - 4]).replace('\0', 'X');
//                                                contactview.setText(firstTwoChars + xChars + lastTwoChars);
//                                            }
//                                            if (phone.length() == 10) {
//                                                String firstTwoChars = phone.substring(0, 2);
//                                                String lastTwoChars = phone.substring(phone.length() - 2);
//                                                String xChars = new String(new char[phone.length() - 4]).replace('\0', 'X');
//                                                phonetextview.setText(firstTwoChars + xChars + lastTwoChars);
//                                            }
//                                            // Request is not accepted, keep button text as "Requested"
//                                            requestbutton.setText("Requested");
//                                            requestbutton.setEnabled(false); // Optional: Disable the button until request is accepted
//                                            cancel.setVisibility(View.VISIBLE);
//                                        }
//
//                                        // If the previous value was "blank" and the new value is either "yes" or "no", decrement the request count
//                                        if (requestStatus.equals("yes") || requestStatus.equals("no")) {
//                                            // Get the current count
//                                            DatabaseReference contactNumberRef = shopRef.child("requestcount");
//                                            contactNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    Integer count = dataSnapshot.getValue(Integer.class);
//                                                    if (count != null && count > 0) {
//                                                        // Decrease the count and update the "contactNumber" node
//                                                        count--;
//                                                        contactNumberRef.setValue(count);
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                    // Handle database error
//                                                    Toast.makeText(ShopDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                // Handle database error
//                                Toast.makeText(ShopDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        cancelbutton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // Delete the request from Firebase Realtime Database
//                                userRequestRef.removeValue()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(ShopDetails.this, "Request canceled", Toast.LENGTH_SHORT).show();
//                                                    request.setVisibility(View.VISIBLE);
//                                                    cancel.setVisibility(View.GONE);
//                                                    // Reset the UI elements to their initial state
//                                                    requestbutton.setText("Send Contact Request");
//                                                    requestbutton.setEnabled(true);
//                                                    if (contact.length() == 10) {
//                                                        String firstTwoChars = contact.substring(0, 2);
//                                                        String lastTwoChars = contact.substring(contact.length() - 2);
//                                                        String xChars = new String(new char[contact.length() - 4]).replace('\0', 'X');
//                                                        contactview.setText(firstTwoChars + xChars + lastTwoChars);
//                                                    } // Clear the contact information
//                                                    if (phone.length() == 10) {
//                                                        String firstTwoChars = phone.substring(0, 2);
//                                                        String lastTwoChars = phone.substring(phone.length() - 2);
//                                                        String xChars = new String(new char[phone.length() - 4]).replace('\0', 'X');
//                                                        phonetextview.setText(firstTwoChars + xChars + lastTwoChars);
//                                                    }
//                                                } else {
//                                                    Toast.makeText(ShopDetails.this, "Failed to cancel request", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//
//                                // Update the count for the contact number when a request is canceled
//                                DatabaseReference contactCountRef = shopRef.child("requestcount");
//                                contactCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        int count = 0;
//                                        if (dataSnapshot.exists()) {
//                                            // If the count already exists, get the current value
//                                            count = dataSnapshot.getValue(Integer.class);
//                                        }
//                                        if (count > 0) {
//                                            count--; // Decrement the count for the canceled request
//                                        }
//                                        // Store the updated count under the contact number
//                                        contactCountRef.setValue(count);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        // Handle database error
//                                        Toast.makeText(ShopDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(ShopDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Assuming you have a Firebase Realtime Database referenc
         imageUrl = getIntent().getStringExtra("image");
        Glide.with(this).load(imageUrl).into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Inflate the popup layout
                View popupView = LayoutInflater.from(ShopDetails.this).inflate(R.layout.activity_full_screen, null);

                // Find the ImageView in the popup layout
                ImageView imageView = popupView.findViewById(R.id.popup_image_view);
                ImageView cancelimageview = popupView.findViewById(R.id.close_image_view);

                // Load the image into the ImageView using Glide
                Glide.with(ShopDetails.this)
                        .load(imageUrl)
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

        // Initialize the progress bars
        ProgressBar progressBar1 = findViewById(R.id.progress_bar_1);
        ProgressBar progressBar2 = findViewById(R.id.progress_bar_2);
        ProgressBar progressBar3 = findViewById(R.id.progress_bar_3);
        ProgressBar progressBar4 = findViewById(R.id.progress_bar_4);
        ProgressBar progressBar5 = findViewById(R.id.progress_bar_5);
        giverating = findViewById(R.id.giverating);
        RatingBar ratingBar = findViewById(R.id.rating_bar);

        ratingRef = shopRef.child("Rating");
        // Retrieve ratings from Firebase Realtime Database
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear previous ratings
                ratings.clear();
                for (int i = 0; i < ratingCounts.length; i++) {
                    ratingCounts[i] = 0;
                }
                totalRating = 0;
                userCount = 0;

                // Retrieve new ratings
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ratingKey = snapshot.getKey();
                    int ratingCount = snapshot.getValue(Integer.class);
                    int ratingValue = Integer.parseInt(ratingKey.substring(ratingKey.lastIndexOf("_") + 1));
                    ratingCounts[ratingValue - 1] = ratingCount;
                    totalRating += ratingCount * ratingValue;
                    userCount += ratingCount;
                    for (int i = 0; i < ratingCount; i++) {
                        ratings.add(ratingValue);
                    }
                }

                // Update the UI with retrieved ratings
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShopDetails.this, "Failed to retrieve ratings", Toast.LENGTH_SHORT).show();
            }
        });


        giverating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user has already rated this shop
                userRatingsRef.child(userId).child(shopId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User has already rated this shop
                            int previousRating = dataSnapshot.getValue(Integer.class);
                            showRatingDialog(previousRating);
                        } else {
                            // User has not rated this shop yet, open rating dialog
                            showRatingDialog(0); // Pass 0 as the previous rating since it doesn't exist
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        Toast.makeText(ShopDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        // Set the OnRatingBarChangeListener for the rating bar
        // RatingBar ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int roundedRating = Math.round(rating);

                // Disable the RatingBar to prevent further rating changes
                ratingBar.setIsIndicator(true);


                // Add the rating to the list
                ratings.add(roundedRating);

                // Update the rating counts
                ratingCounts[roundedRating - 1]++;

                // Update the total rating
                totalRating += roundedRating;

                // Update the user count
                userCount++;

                // Update the progress bars
                int maxProgress = userCount;
                progressBar1.setMax(maxProgress);
                progressBar2.setMax(maxProgress);
                progressBar3.setMax(maxProgress);
                progressBar4.setMax(maxProgress);
                progressBar5.setMax(maxProgress);
                progressBar1.setProgress(ratingCounts[0]);
                progressBar2.setProgress(ratingCounts[1]);
                progressBar3.setProgress(ratingCounts[2]);
                progressBar4.setProgress(ratingCounts[3]);
                progressBar5.setProgress(ratingCounts[4]);

                // Update the total rating TextView
                TextView totalRatingTextView = findViewById(R.id.total_rating_text_view);
                totalRatingTextView.setText("" + userCount);

                // Calculate and update the average rating
                float averageRating = calculateAverageRating();
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                String averageRatingFormatted = decimalFormat.format(averageRating);
                TextView averageRatingTextView = findViewById(R.id.average_rating_text_view);
                RatingBar Avgrating_bar = findViewById(R.id.Avgrating_bar);
                averageRatingTextView.setText(averageRatingFormatted);
                Avgrating_bar.setRating(averageRating);

                // Store the ratings in Firebase Realtime Database
                storeRatingsInFirebase();

                // Store the user's rating for this shop in Firebase Realtime Database
                userRatingsRef.child(userId).child(shopId).setValue(roundedRating)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ShopDetails.this, "Rating saved.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ShopDetails.this, "Failed to save rating.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        setCurrentUser();


//        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
//                if (currentUsersnapshot.exists()){
//                    String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
//                    System.out.println("sfsgr " +currentusercontactNum);
//                    String currentUserName = currentUsersnapshot.child("name").getValue(String.class);
//
//                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Shop");
//                    databaseRef.child(currentusercontactNum).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                shopdata = snapshot.getValue(Shop.class);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                    DatabaseReference shopsRef  = shopRef.child("notification");
//                    // Get a reference to the "notification" node under "shopRef"
//                    DatabaseReference notificationRef = shopRef.child("notification");
//
//                    // Generate a random key for the notification
//                    String notificationKey = notificationRef.push().getKey();
//
//                    // Create a message
//                    String message = currentUserName + " visited your profile.";
//
//                    // Create a map to store the message
//                    Map<String, Object> notificationData = new HashMap<>();
//                    notificationData.put("message", message);
//
//                    // Store the message under the generated key
//                    notificationRef.child(notificationKey).setValue(notificationData);
//
//                    // Increment the notification count for the shop
//                    DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
//                            .child(shopId)
//                            .child("notificationcount");
//
//                    // Read the current count and increment it by 1
//                    shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
//                            int newCount = currentCount + 1;
//
//                            // Update the notification count
//                            shopNotificationCountRef.setValue(newCount);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            // Handle onCancelled event
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//            }
//        });

//        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
//                if (currentUsersnapshot.exists()){
//                    String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
//                    System.out.println("sfsgr " +currentusercontactNum);
//                    String currentUserName = currentUsersnapshot.child("name").getValue(String.class);
//
//                    for (DataSnapshot snapshot : currentUsersnapshot.getChildren()){
//
//                    }
//                    String currentUserId = currentUsersnapshot.getKey();
//
//                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Shop");
//                    databaseRef.child(currentusercontactNum).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                shopdata = snapshot.getValue(Shop.class);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                    if (!currentUserId.equals(shopId)) {
//                        // Check if the user is not viewing their own profile
//                        DatabaseReference notificationRef = shopRef.child("notification");
//
//                        // Generate a random key for the notification
//                        String notificationKey = notificationRef.push().getKey();
//
//                        // Create a message
//                        String message = currentUserName + " visited your profile.";
//
//                        // Create a map to store the message
//                        Map<String, Object> notificationData = new HashMap<>();
//                        notificationData.put("message", message);
//
//                        // Store the message under the generated key
//                        notificationRef.child(notificationKey).setValue(notificationData);
//
//                        // Increment the notification count for the shop
//                        DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
//                                .child(shopId)
//                                .child("notificationcount");
//
//                        // Read the current count and increment it by 1
//                        shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
//                                int newCount = currentCount + 1;
//
//                                // Update the notification count
//                                shopNotificationCountRef.setValue(newCount);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                // Handle onCancelled event
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//            }
//        });

        // Assuming you have already defined SharedPreferences in your activity
   //     SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

//        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
//                if (currentUsersnapshot.exists()) {
//                    String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
//                    System.out.println("sfsgr " + currentusercontactNum);
//                    String currentUserName = currentUsersnapshot.child("name").getValue(String.class);
//                    String currentUserId = currentUsersnapshot.getKey();
//
//                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Shop");
//                    databaseRef.child(currentusercontactNum).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                shopdata = snapshot.getValue(Shop.class);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//                            // Handle onCancelled event
//                        }
//                    });
//
//                    if (!currentUserId.equals(shopId)) {
//                        // Check if the user is not viewing their own profile
//
//                        // Check if the user has already visited the shop's profile
//                        boolean hasVisitedShopProfile = sharedPreferences.getBoolean("visited_shop_profile_" + shopId, false);
//
//                        if (!hasVisitedShopProfile) {
//                            // Generate a random key for the notification
//                            DatabaseReference notificationRef = shopRef.child("notification");
//                            String notificationKey = notificationRef.push().getKey();
//
//                            // Create a message
//                            String message = currentUserName + " visited your profile.";
//
//                            // Create a map to store the message
//                            Map<String, Object> notificationData = new HashMap<>();
//                            notificationData.put("message", message);
//                            notificationData.put("contactNumber", currentUserId);
//
//                            // Store the message under the generated key
//                            notificationRef.child(notificationKey).setValue(notificationData);
//
//                            // Increment the notification count for the shop
//                            DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
//                                    .child(shopId)
//                                    .child("notificationcount");
//
//                            // Read the current count and increment it by 1
//                            shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
//                                    int newCount = currentCount + 1;
//
//                                    // Update the notification count
//                                    shopNotificationCountRef.setValue(newCount);
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    // Handle onCancelled event
//                                }
//                            });
//
//                            // Mark that the user has visited the shop's profile
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("visited_shop_profile_" + shopId, true);
//                            editor.apply();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//                // Handle onCancelled event
//            }
//        });


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
                if (currentUsersnapshot.exists()) {
                    String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("sfsgr " + currentusercontactNum);
                    String currentUserName = currentUsersnapshot.child("name").getValue(String.class);
                    String currentUserId = currentUsersnapshot.getKey();

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Shop");
                    databaseRef.child(currentusercontactNum).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                shopdata = snapshot.getValue(Shop.class);
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });

                    if (!currentUserId.equals(shopId)) {
                        // Check if the user is not viewing their own profile

                        // Check if the user has already visited the shop's profile
                        boolean hasVisitedShopProfile = sharedPreferences.getBoolean("visited_shop_profile_" + shopId, false);

                        // Remove the "visited" status when visiting the profile
                        sharedPreferences.edit().remove("visited_shop_profile_" + shopId).apply();

                        // Check if the notification already exists for the currentusercontactNum
                        DatabaseReference notificationRef = shopRef.child("notification");

                        notificationRef.child(currentusercontactNum).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    // The notification doesn't exist, so create it
                                    String message = currentUserName + " visited your profile.";

                                    // Create a map to store the message
                                    Map<String, Object> notificationData = new HashMap<>();
                                    notificationData.put("message", message);
                                    notificationData.put("contactNumber", currentUserId);
                                    notificationData.put("key" , currentusercontactNum);

                                    // Store the message under the currentusercontactNum
                                    notificationRef.child(currentusercontactNum).setValue(notificationData);

                                    // Increment the notification count for the shop
                                    DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(shopId)
                                            .child("notificationcount");

                                    DatabaseReference NotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(shopId).child("count")
                                            .child("notificationcount");


                                    // Read the current count and increment it by 1
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
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled event
                            }
                        });

                        // Mark that the user has visited the shop's profile
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("visited_shop_profile_" + shopId, true);
                        editor.apply();
                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });

    }

    private void setCurrentUser(){

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot usersnapshot) {
                if (usersnapshot.exists()){
                    String currentusercontactNum = usersnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("dfdfg " +currentusercontactNum);

                    // Get a reference to the "Shop" node
                    DatabaseReference shopNodeRef = FirebaseDatabase.getInstance().getReference("Shop");

                    // Check if currentusercontactNum is present in the "Shop" node
                    shopNodeRef.child(currentusercontactNum).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // currentusercontactNum is present in the "Shop" node, show the button
                                requestbutton.setVisibility(View.VISIBLE);
                            } else {
                                // currentusercontactNum is not present in the "Shop" node, hide the button
                                requestbutton.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });

                    DatabaseReference promotedShopsRef = FirebaseDatabase.getInstance().getReference("Shop")
                            .child(currentusercontactNum) // Use the current user's contactNumber
                            .child("promotedShops");
                    promotedShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot shopSnapshot : snapshot.getChildren()){
                                    String key = shopSnapshot.getKey();
                                    System.out.println("sdfv  " + shopId);
                                    System.out.println("bfbdf  " + key);
                                    if (shopId.equals(shopSnapshot.getKey())){
                                        requestbutton.setVisibility(View.GONE);
                                    } else {
//                                        request.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });

                    requestbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference promotedShopsRef = FirebaseDatabase.getInstance().getReference("Shop")
                                    .child(currentusercontactNum) // Use the current user's contactNumber
                                    .child("promotedShops");// Create a node "promotedShops" if it doesn't exist
                            System.out.println("sdffsv " +promotedShopsRef);
                            // Assuming you have a "selectedShop" object containing the shop information

                            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
                                    if (currentUsersnapshot.exists()){
                                        String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
                                        System.out.println("sfsgr " +currentusercontactNum);
                                        String currentUserName = currentUsersnapshot.child("name").getValue(String.class);

                                        // Get a reference to the "notification" node under "shopRef"
                                        DatabaseReference notificationRef = shopRef.child("notification");

                                        // Generate a random key for the notification
                                        String notificationKey = notificationRef.push().getKey();

                                        // Create a message
                                        String message = currentUserName + " promoted your shop.";


                                        // Create a map to store the message
                                        Map<String, Object> notificationData = new HashMap<>();
                                        notificationData.put("message", message);
                                        notificationData.put("shopNumber",currentusercontactNum);

                                        // Store the message under the generated key
                                        notificationRef.child(notificationKey).setValue(notificationData);

                                        DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                .child(shopId)
                                                .child("notificationcount");

                                        DatabaseReference NotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                .child(shopId).child("count")
                                                .child("notificationcount");

                                        // Read the current count and increment it by 1
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


                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                }
                            });


                            shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    Shop shop = snapshot.getValue(Shop.class);
                                    DatabaseReference shopRef = promotedShopsRef.child(shopId);

                                    // Update the shop details with the values from the "shop" object
                                    shopRef.setValue(shop);
                                    int newCount = shop.getpromotionCount() + 1; // Increment by 1

                                    DatabaseReference promotionRef =  FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(shopId)
                                            .child("hePromoteYou");
                                    System.out.println("ggfvd " +promotionRef.toString());

                                    promotionRef.child(currentusercontactNum).setValue(shopdata);

                                    // Update the promoted shop count in the "promotionCount" key for the selected shop
                                    DatabaseReference promotionCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(shopId)
                                            .child("promotionCount");
                                    promotionCountRef.setValue(newCount);

                                    DatabaseReference promoteCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(shopId).child("count")
                                            .child("promotionCount");
                                    promoteCountRef.setValue(newCount);



                                    requestbutton.setVisibility(View.GONE);

                                }


                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });



    }


    private void showRatingDialog(int previousRating) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetails.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating_dialog, null);
        builder.setView(dialogView);

        RatingBar ratingBar = dialogView.findViewById(R.id.dialog_rating_bar);
        ratingBar.setRating(previousRating); // Set the previous rating on the RatingBar

        // Initialize the progress bars
        ProgressBar progressBar1 = findViewById(R.id.progress_bar_1);
        ProgressBar progressBar2 = findViewById(R.id.progress_bar_2);
        ProgressBar progressBar3 = findViewById(R.id.progress_bar_3);
        ProgressBar progressBar4 = findViewById(R.id.progress_bar_4);
        ProgressBar progressBar5 = findViewById(R.id.progress_bar_5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRatingsRef = database.getReference().child("UserRatings");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }


        builder.setPositiveButton("Submit", null); // Set the button initially as null

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();

                if (rating == 0) {
                    // Display a message asking the user to provide a rating
                    Toast.makeText(ShopDetails.this, "Please give a rating.", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform any necessary operations with the rating and comment
                    int roundedRating = Math.round(rating);

                    // Disable the RatingBar to prevent further rating changes
                    ratingBar.setIsIndicator(true);

                    // Remove the previous rating from the calculations if it exists
                    if (previousRating > 0) {
                        // Update the rating counts
                        ratingCounts[previousRating - 1]--;

                        // Update the total rating
                        totalRating -= previousRating;

                        // Update the user count
                        userCount--;
                    }

                    // Add the new rating to the calculations
                    // Update the rating counts
                    ratingCounts[roundedRating - 1]++;

                    // Update the total rating
                    totalRating += roundedRating;

                    // Update the user count
                    userCount++;

                    // Update the progress bars
                    int maxProgress = userCount;
                    progressBar1.setMax(maxProgress);
                    progressBar2.setMax(maxProgress);
                    progressBar3.setMax(maxProgress);
                    progressBar4.setMax(maxProgress);
                    progressBar5.setMax(maxProgress);
                    progressBar1.setProgress(ratingCounts[0]);
                    progressBar2.setProgress(ratingCounts[1]);
                    progressBar3.setProgress(ratingCounts[2]);
                    progressBar4.setProgress(ratingCounts[3]);
                    progressBar5.setProgress(ratingCounts[4]);

                    // Update the total rating TextView
                    TextView totalRatingTextView = findViewById(R.id.total_rating_text_view);
                    totalRatingTextView.setText(String.valueOf(userCount));
                    String shopId = getIntent().getStringExtra("contactNumber");
                    // Retrieve the shop details from Firebase Realtime Database
                    DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId);

                    // Calculate and update the average rating
                    float averageRating = calculateAverageRating();
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    String averageRatingFormatted = decimalFormat.format(averageRating);
                    TextView averageRatingTextView = findViewById(R.id.average_rating_text_view);
                    RatingBar avgRatingBar = findViewById(R.id.Avgrating_bar);
                    averageRatingTextView.setText(averageRatingFormatted);
                    avgRatingBar.setRating(averageRating);

                    // Store the ratings in Firebase Realtime Database
                    storeRatingsInFirebase();

                    // Store the user's rating for this shop in Firebase Realtime Database
                    userRatingsRef.child(userId).child(shopId).setValue(roundedRating)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ShopDetails.this, "Rating saved.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ShopDetails.this, "Failed to save rating.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
                            if (currentUsersnapshot.exists()){
                                String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
                                System.out.println("sfsgr " +currentusercontactNum);
                                String currentUserName = currentUsersnapshot.child("name").getValue(String.class);

                                // Get a reference to the "notification" node under "shopRef"
                                DatabaseReference notificationRef = shopRef.child("notification");

                                // Generate a random key for the notification
                                String notificationKey = notificationRef.push().getKey();

                                // Create a message
                                String message = currentUserName + " gives you " +roundedRating + " rating.";

                                // Create a map to store the message
                                Map<String, Object> notificationData = new HashMap<>();
                                notificationData.put("message", message);

                                // Store the message under the generated key
                                notificationRef.child(notificationKey).setValue(notificationData);

                                DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                        .child(shopId)
                                        .child("notificationcount");

                                DatabaseReference NotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                        .child(shopId).child("count")
                                        .child("notificationcount");

                                // Read the current count and increment it by 1
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


                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });

                }
                dialog.dismiss();
            }
        });
    }

    /*private void openGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + addressTextView.getText().toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps app not installed", Toast.LENGTH_SHORT).show();
        }
    }*/





    private float calculateAverageRating() {
        if (ratings.size() == 0) {
            return 0.0F;
        }

        int sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }

        return (float) sum / ratings.size();
    }

    private void storeRatingsInFirebase() {
        Map<String, Integer> ratingMap = new HashMap<>();
        for (int i = 0; i < ratingCounts.length; i++) {
            ratingMap.put("rating_" + (i + 1), ratingCounts[i]);
        }

        // Store the ratings in the "ratings" node in Firebase Realtime Database
        ratingRef.setValue(ratingMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                   // Toast.makeText(ShopDetails.this, "Ratings stored successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShopDetails.this, "Failed to store ratings", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUI() {
        // Update the progress bars
        ProgressBar progressBar1 = findViewById(R.id.progress_bar_1);
        ProgressBar progressBar2 = findViewById(R.id.progress_bar_2);
        ProgressBar progressBar3 = findViewById(R.id.progress_bar_3);
        ProgressBar progressBar4 = findViewById(R.id.progress_bar_4);
        ProgressBar progressBar5 = findViewById(R.id.progress_bar_5);
        int maxProgress = userCount;
        progressBar1.setMax(maxProgress);
        progressBar2.setMax(maxProgress);
        progressBar3.setMax(maxProgress);
        progressBar4.setMax(maxProgress);
        progressBar5.setMax(maxProgress);
        progressBar1.setProgress(ratingCounts[0]);
        progressBar2.setProgress(ratingCounts[1]);
        progressBar3.setProgress(ratingCounts[2]);
        progressBar4.setProgress(ratingCounts[3]);
        progressBar5.setProgress(ratingCounts[4]);

        TextView totalRatingTextView = findViewById(R.id.total_rating_text_view);
        totalRatingTextView.setText("" + userCount);

        // Calculate and update the average rating
        float averageRating = calculateAverageRating();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String averageRatingFormatted = decimalFormat.format(averageRating);
        TextView averageRatingTextView = findViewById(R.id.average_rating_text_view);
        RatingBar Avgrating_bar = findViewById(R.id.Avgrating_bar);
        averageRatingTextView.setText(averageRatingFormatted);
        Avgrating_bar.setRating(averageRating);
    }

    @Override
    public void onRemoveClick(int position) {

    }
}

