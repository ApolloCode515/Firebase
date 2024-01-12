package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.ui.FragmentHome;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.annotations.NonNull;

public class AddItems extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout imageContainer;
    private int imageViewCount = 0;
    private final int MAX_IMAGES = 4;
    private int imageCount = 0;
    ImageView back, addServeAreaImageView ;
    RelativeLayout relativeLayout, wholesalerelativelay;
    EditText itemname, itemprice, itemdiscription, itemsellingprice, wholesaleprice, minquantity, itemServeArea ;
    TextView catlogtextview, textview, introtextview;
    Button save;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference newItemRef,newproductRef, usersRef, productRef ,shopkeyRef;
    String contactNumber,ContactNumber, itemkey, userId, shopkey;
    //private HashMap<Integer, String> imageUrls = new HashMap<>();
    List<String> imagesUrls = new ArrayList<>();
   // private List<String> imageUrls = new ArrayList<>();
    // Counter for generating image keys
    private int imageCounter = 1;
    boolean isFirstItemAdded = false;
    // Declare a global variable to store the selected image URI
    private Uri selectedImageUri, croppedImageUri;
    private static final String USER_ID_KEY = "userID";
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private AlertDialog dialog;
    private boolean isDialogShowing = false;
    private boolean inUCropFlow = false;
    private static final int REQUEST_CODE_IMAGE = 1;
    private static final int REQUEST_CODE_CROP = 2;
    private int count = 0;
    Boolean premium;
    RadioGroup radioGroup;
    RadioButton rdretail, rdwholesale;
    LinearLayout radiolayout, locallayout;
    private List<String> servedAreasList = new ArrayList<>();
    String servedAreasFirebaseFormat, global,checkstring="Global";;
    int counts =1;
    RadioButton allAreabtn, localAreabtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        //horizontalScrollView = findViewById(R.id.horizontalScrollView);
        imageContainer = findViewById(R.id.imageContainer);
        // Align the images at the beginning of the LinearLayout
        //imageContainer.setGravity(Gravity.START);

        back = findViewById(R.id.back);
        itemname = findViewById(R.id.itemname);
        itemprice = findViewById(R.id.itemprice);
        itemdiscription = findViewById(R.id.itemdiscription);
        itemsellingprice = findViewById(R.id.itemsellingprice);
        save = findViewById(R.id.save);
        catlogtextview = findViewById(R.id.catlogtextview);
        textview = findViewById(R.id.textsview);
//        itemname.setEnabled(false);
//        itemprice.setEnabled(false);
//        itemdiscription.setEnabled(false);
        introtextview = findViewById(R.id.introtextview);
      //  introtextview.setVisibility(View.VISIBLE);
        relativeLayout = findViewById(R.id.relativelay);
        radioGroup = findViewById(R.id.rdgrpx);
        radiolayout = findViewById(R.id.radiolay);
        wholesaleprice = findViewById(R.id.wholesaleitemprice);
        minquantity = findViewById(R.id.wholesalequantity);
         itemServeArea = findViewById(R.id.itemserveArea);
         addServeAreaImageView = findViewById(R.id.addServeAreaImageView);
        locallayout = findViewById(R.id.locallayout);
       // wholesalerelativelay = findViewById(R.id.wholesalerelativelay);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseApp.initializeApp(this);
        databaseReference = firebaseDatabase.getReference("Shop");
        storageReference = storage.getReference("item_images");

        // imageButton = findViewById(R.id.uploadimage);
//        RecyclerView imageRecyclerView = findViewById(R.id.horizantalRecyclerView);
//        ItemImagesAdapter imageAdapter = new ItemImagesAdapter(imagesUrls);
//        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        imageRecyclerView.setAdapter(imageAdapter);
//        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
//        imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));


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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
         usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("contactnumber " +contactNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
            }
        });

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   premium = snapshot.child("premium").getValue(Boolean.class);
                    System.out.println("sdzcvf " +premium);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });


        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            ContactNumber = sharedIntent.getStringExtra("contactNumber");

            //Log.d("contactNumber", "" + contactNumber);
//            // Convert the image URI string to a URI object
//            Uri imageUri = Uri.parse(image);
//            Log.d("imageUri", "" + imageUri);
//            catalogshopname.setText(shopName);
//            // Load image using Glide
//            RequestOptions requestOptions = new RequestOptions()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Optional: Set caching strategy
//
//            Glide.with(this)
//                    .load(image).centerCrop()
//                    .apply(requestOptions)
//                    .into(catalogshopimage);

        }

//        String itemsPrice = itemprice.getText().toString().trim();
//        String itemSell = itemsellingprice.getText().toString().trim();
//        double originalPrice = Double.parseDouble(itemsPrice);
//        double sellingPrice = Double.parseDouble(itemSell);
//        double discountPercentage = ((originalPrice - sellingPrice) / originalPrice) * 100;
//        System.out.println("Discount Percentage: " + discountPercentage);

        checkstring="Global";

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = itemname.getText().toString().trim();
                String itemPrice = itemprice.getText().toString().trim();
                String itemSell = itemsellingprice.getText().toString().trim();
                String itemWholeSale = wholesaleprice.getText().toString().trim();
                String itemquantity = minquantity.getText().toString().trim();

                // Check if required fields are entered
                if (TextUtils.isEmpty(itemName)) {
                    // Show an error message for missing item name
                    itemname.setError("Item name is required");
                    return;
                }

                if (TextUtils.isEmpty(itemPrice)) {
                    // Show an error message for missing item price
                    itemprice.setError("Item price is required");
                    return;
                }

                if (TextUtils.isEmpty(itemSell)) {
                    // Show an error message for missing item price
                    itemsellingprice.setError("Item price is required");
                    return;
                }

                // Convert itemPrice and itemSell to numeric values
                double price = Double.parseDouble(itemPrice);
                double sellPrice = Double.parseDouble(itemSell);

                if (sellPrice > price) {
                    // Show an error message for selling price greater than or equal to item price
                    itemsellingprice.setError("Selling price cannot be greater than to item price");
                    return;
                }

                // Show progress dialog while creating profile
                ProgressDialog progressDialog = new ProgressDialog(AddItems.this);
                progressDialog.setMessage("Saving...");
                progressDialog.setCancelable(true);
                progressDialog.show();

                // Parse the prices as doubles
                double originalPrice = Double.parseDouble(itemPrice);
                double sellingPrice = Double.parseDouble(itemSell);

                // Calculate the discount percentage
                double discountPercentage = ((originalPrice - sellingPrice) / originalPrice) * 100;
                int roundedDiscountPercentage = (int) Math.round(discountPercentage);
                String formattedDiscountPercentage = String.valueOf(roundedDiscountPercentage) + "%";

                // Assuming userId is your user identifier
                productRef = FirebaseDatabase.getInstance().getReference("Products").child(userId);

                // Check if productRef exists
                if (productRef != null) {
                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            long count = snapshot.getChildrenCount();
                            System.out.println("sdfv " + count);

                            // Now you have the count, you can perform any logic based on it
                            if (count < 5 || premium.equals(true)) {
                                System.out.println("edfaa " + premium);
                                String itemDesc = itemdiscription.getText().toString().trim();

                                DatabaseReference itemRef = databaseReference.child(contactNumber).child("items");
                                DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                                        .child("Products").child(contactNumber);

                                // Get the current timestamp as a unique key
                                String itemKey = String.valueOf(System.currentTimeMillis());

                                // Create a new item reference using the timestamp key
                                newItemRef = itemRef.child(itemKey);
                                newproductRef = productRef.child(itemKey);

                                // Format the price before saving
                                String formattedPrice = formatPrice(originalPrice);
                                String formattedSell = formatPrice(sellingPrice);

                                // Save the item information
                                newproductRef.child("itemname").setValue(itemName);
                                newproductRef.child("price").setValue(itemPrice);
                                newproductRef.child("sell").setValue(itemSell);
                                newproductRef.child("description").setValue(itemDesc);
                                newproductRef.child("itemkey").setValue(itemKey);
                                newproductRef.child("offer").setValue(formattedDiscountPercentage);
                                newproductRef.child("shopContactNumber").setValue(contactNumber);
                                newproductRef.child("wholesale").setValue(itemWholeSale);
                                newproductRef.child("minquantity").setValue(itemquantity);

                               if (checkstring.equals("Global")){
                                    newproductRef.child("servingArea").setValue("Global");
                                } else if (checkstring.equals("Local")){
                                    newproductRef.child("servingArea").setValue(servedAreasFirebaseFormat);
                                }

                                newItemRef.child("itemname").setValue(itemName);
                                newItemRef.child("price").setValue(itemPrice);
                                newItemRef.child("sell").setValue(itemSell);
                                newItemRef.child("description").setValue(itemDesc);
                                newItemRef.child("itemkey").setValue(itemKey);
                                newItemRef.child("offer").setValue(formattedDiscountPercentage);
                                newItemRef.child("shopContactNumber").setValue(contactNumber);
                                newItemRef.child("wholesale").setValue(itemWholeSale);
                                newItemRef.child("minquantity").setValue(itemquantity);

                                if (checkstring.equals("Global")){
                                    newItemRef.child("servingArea").setValue("Global");
                                }  else if (checkstring.equals("Local")){
                                    newItemRef.child("servingArea").setValue(servedAreasFirebaseFormat);
                                }

                                // Store image URLs in the database
                                storeImageUrls(newItemRef, newproductRef);

                                // Dismiss the progress dialog
                                progressDialog.dismiss();

                                Intent resultIntent = new Intent();
                                setResult(RESULT_OK, resultIntent); // Use RESULT_OK to indicate success
                                AddItems.this.finish(); // Optional, depending on your navigation flow
                            } else {
                                // Redirect to the PremiumMembership activity
                                showImageSelectiondialog();
//                                Intent intent = new Intent(getApplicationContext(), PremiumMembership.class);
//                                startActivity(intent);
//                                finish();
                                progressDialog.dismiss();
                            }

                            // Toast.makeText(AddItems.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                            // Redirect to the createcatalog page
                            // In createcatlogpage when an item is saved

                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                            // Handle onCancelled
                            progressDialog.dismiss(); // Dismiss the progress dialog in case of an error
                        }
                    });
                } else {
                    // productRef is null, create and store the data
                    // ... (Your logic for creating and storing the data)
                    progressDialog.dismiss();
                }
            }
        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String itemName = itemname.getText().toString().trim();
//                String itemPrice = itemprice.getText().toString().trim();
//                String itemSell = itemsellingprice.getText().toString().trim();
//                // Check if required fields are entered
//
//                if (croppedImageUri != null) {
//
//
//                    if (TextUtils.isEmpty(itemName)) {
//                        // Show an error message for missing item name
//                        itemname.setError("Item name is required");
//                        return;
//                    }
//
//                    if (TextUtils.isEmpty(itemPrice)) {
//                        // Show an error message for missing item price
//                        itemprice.setError("Item price is required");
//                        return;
//                    }
//
//                    if (TextUtils.isEmpty(itemSell)) {
//                        // Show an error message for missing item price
//                        itemsellingprice.setError("Item price is required");
//                        return;
//                    }
//
//
//
//                    // Show progress dialog while creating profile
//                    ProgressDialog progressDialog = new ProgressDialog(AddItems.this);
//                    progressDialog.setMessage("Saving...");
//                    progressDialog.setCancelable(true);
//                    progressDialog.show();
//
//                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                long count = snapshot.getChildrenCount();
//                                System.out.println("sdfv " +count);
//                                // Now you have the count, you can perform any logic based on it
//                                if (count < 5 || premium.equals(true)) {
//                                    System.out.println("edfaa " +premium);
//                                    String itemName = itemname.getText().toString().trim();
//                                    String itemPrice = itemprice.getText().toString().trim();
//                                    String itemSell = itemsellingprice.getText().toString().trim();
//                                    // Parse the price as a double
//                                    double Price = Double.parseDouble(itemPrice);
//                                    double Sell = Double.parseDouble(itemSell);
//                                    String itemDesc = itemdiscription.getText().toString().trim();
//
//                                    double originalPrice = Double.parseDouble(itemPrice);
//                                    double sellingPrice = Double.parseDouble(itemSell);
//                                    double discountPercentage = ((originalPrice - sellingPrice) / originalPrice) * 100;
//                                    System.out.println("Discount Percentage: " + discountPercentage);
//                                    // Round the discount percentage to the nearest integer
//                                    int roundedDiscountPercentage = (int) Math.round(discountPercentage);
//
//                                    DatabaseReference itemRef = databaseReference.child(contactNumber).child("items");
//                                    DatabaseReference productRef =  FirebaseDatabase.getInstance().getReference()
//                                            .child("Products").child(contactNumber);
//
//                                    // Get the current timestamp as a unique key
//                                    String itemKey = String.valueOf(System.currentTimeMillis());
//
//                                    // Create a new item reference using the timestamp key
//                                    newItemRef = itemRef.child(itemKey);
//                                    newproductRef = productRef.child(itemKey);
//
//                                    // Format the price before saving
//                                    String formattedPrice = formatPrice(Price);
//                                    String formattedSell = formatPrice(Sell);
//
//                                    // Save the item information
////                                                newItemRef.child("itemname").setValue(itemName);
////                                                newItemRef.child("price").setValue(formattedPrice);
////                                                newItemRef.child("sell").setValue(formattedSell);
////                                                newItemRef.child("description").setValue(itemDesc);
////                                                newItemRef.child("itemkey").setValue(itemKey);
////                                                newItemRef.child("offer").setValue(roundedDiscountPercentage);
//
//                                    newproductRef.child("itemname").setValue(itemName);
//                                    newproductRef.child("price").setValue(formattedPrice);
//                                    newproductRef.child("sell").setValue(formattedSell);
//                                    newproductRef.child("description").setValue(itemDesc);
//                                    newproductRef.child("itemkey").setValue(itemKey);
//                                    newproductRef.child("offer").setValue(roundedDiscountPercentage);
//
//
//// Store image URLs in the database
////                            for (String imageUrl : imageUrls) {
////                                storeImageUrl(itemRef, imageUrl);
////                            }
////                            for (int i = 0; i < imageViewCount; i++) {
////                                storeImageUrl(newItemRef, imageUrls.get(i).toString());
////                            }
//
//
//                                    storeImageUrls(newItemRef);
//
//
//                                    // Store image URLs in the database
//                                    //toreImageUrls(newItemRef);
////                            imageAdapter.setImagesUrls(imagesUrls);
////                            imageAdapter.notifyDataSetChanged();
//
//                                    // Dismiss the progress dialog
//                                    progressDialog.dismiss();
//                                } else {
//                                    // Redirect to the PremiumMembership activity
//                                    Intent intent = new Intent(getApplicationContext(), PremiumMembership.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//
//                            // Toast.makeText(AddItems.this, "Item added successfully", Toast.LENGTH_SHORT).show();
//                            // Redirect to the createcatalog page
//                            // In createcatlogpage when an item is saved
//                            Intent resultIntent = new Intent();
//                            setResult(RESULT_OK, resultIntent); // Use RESULT_OK to indicate success
//
//                            AddItems.this.finish(); // Optional, depending on your navigation flow
//                        }
//
//                        @Override
//                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                    databaseReference.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//
//
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//
//                        }
//                    });
//                }else{
//                    Toast.makeText(AddItems.this, "Add at least one image", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
//                if (null != rb) {
//                    // checkedId is the RadioButton selected
//                    switch (i) {
//                        case R.id.rdRetail:
//                            // Do Something
//                            relativeLayout.setVisibility(View.VISIBLE);
//                            wholesalerelativelay.setVisibility(View.GONE);
//                            break;
//
//                        case R.id.rdWholesale:
//                            // Do Something
//                            relativeLayout.setVisibility(View.GONE);
//                            wholesalerelativelay.setVisibility(View.VISIBLE);
//                            break;
//
//                    }
//                }
//            }
//        });

        showImageSelectionDialog();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItems.this.finish();
            }
        });

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        String itemPrice = intent.getStringExtra("itemPrice");
        String itemDescription = intent.getStringExtra("itemDescription");

        itemname.setText(itemName);
        itemprice.setText(itemPrice);
        itemdiscription.setText(itemDescription);

         itemkey = intent.getStringExtra("itemKey");
        Log.d("itemKey ",""+itemkey);
        Log.d("itemName ",""+itemName);

      //  getData();

        addServeAreaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serveAreaText = itemServeArea.getText().toString().trim();
                if (!serveAreaText.isEmpty() && counts<=5) {
                    addServeArea(serveAreaText);
                    itemServeArea.setText(""); // Clear the EditText
                    updateServedAreasUI();
                    counts++;
                }else {
                    itemServeArea.setError("You have added the maximum number of locations");
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (null != rb) {
                    // checkedId is the RadioButton selected
                    switch (i) {
                        case R.id.rdglobal:
                            // Do Something
                            checkstring = "Global";
                            global = "Global";
                            locallayout.setVisibility(View.GONE);
                            break;

                        case R.id.rdlocal:
                            // Do Something
                            checkstring = "Local";
                            locallayout.setVisibility(View.VISIBLE);
                            break;

                    }
                }
            }
        });

    }

    // Method to add a served area to the list
    private void addServeArea(String serveArea) {
        servedAreasList.add(serveArea);
    }

    // Method to remove a served area from the list
    private void removeServeArea(int position) {
        servedAreasList.remove(position);
        updateServedAreasUI();
    }

    // Method to update the UI with the current list of served areas
// Method to update the UI with the current list of served areas
    private void updateServedAreasUI() {
        ChipGroup servedAreasLayout = findViewById(R.id.servedAreasLayout);
        servedAreasLayout.removeAllViews(); // Clear the existing views

        StringBuilder servedAreasString = new StringBuilder(); // StringBuilder to concatenate served areas

        for (int i = 0; i < servedAreasList.size(); i++) {
            View servedAreaView = LayoutInflater.from(this).inflate(R.layout.served_area_item, null);

            TextView servedAreaTextView = servedAreaView.findViewById(R.id.servedAreaTextView);
            servedAreaTextView.setText(servedAreasList.get(i));

            ImageView cancelServeAreaImageView = servedAreaView.findViewById(R.id.cancelServeAreaImageView);
            final int position = i;

            cancelServeAreaImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeServeArea(position);
                }
            });

            servedAreasLayout.setVisibility(View.VISIBLE);
            servedAreasLayout.addView(servedAreaView);

            // Append the served area to the StringBuilder with the delimiter
            servedAreasString.append(servedAreasList.get(i));
            if (i < servedAreasList.size() - 1) {
                servedAreasString.append("&&");
            }
        }

        // Now, you can use servedAreasString.toString() to store in Firebase
         servedAreasFirebaseFormat = servedAreasString.toString();

        // Store servedAreasFirebaseFormat in Firebase
    }

    private void displayImagesInFormat(List<String> imageUrls) {
         imageContainer = findViewById(R.id.imageContainer); // Change to your actual image container ID

        for (String imageUrl : imageUrls) {
           // addImageView(Uri.parse(imageUrl), imageContainer);
        }
    }
    
    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##,##0.00", symbols);
        return "₹ " + decimalFormat.format(price);
    }

    public void onImageViewClick(View view) {
        if (imageCount < 4) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1); // Use a unique requestCode to differentiate from the initial image selection
        } else {
            // Handle maximum image limit reached
            Toast.makeText(this, "Maximum image limit reached (4 images)", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCameraClick(View view){
        if (imageViewCount < MAX_IMAGES) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, imageViewCount);
        }
    }

    private void startImageCropper(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image"));

        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Crop Image");
        options.setCompressionQuality(100);

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Set the aspect ratio (square in this case)
                .withMaxResultSize(400, 400) // Set the max result size
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_CANCELED) {
            if (inUCropFlow) {
                // Handle the situation when UCrop was not involved
                // For example, you can dismiss the dialog here
                showImageSelectionDialog();
                relativeLayout.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                imageContainer.setVisibility(View.GONE);
            }
            inUCropFlow = false; // Reset the flag

        } else if (requestCode == 1 && data != null && resultCode == RESULT_OK && imageCount < 4) {
            Uri imageUri = data.getData();

            if (imageUri != null) {
                croppedImageUri = imageUri;
                startImageCropper(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && imageCount < 4) {
            if (data != null) { // Check if data is not null
                croppedImageUri = UCrop.getOutput(data);

                if (croppedImageUri != null) {
                    // Rest of your code for processing the cropped image
                    relativeLayout.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    imageContainer.setVisibility(View.VISIBLE);
                    itemdiscription.setEnabled(true);
                    itemname.setEnabled(true);
                    itemprice.setEnabled(true);
                    addImageView(croppedImageUri);

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    String imageName = UUID.randomUUID().toString(); // Generate a unique image name
                    StorageReference imageRef = storageRef.child("item_images/"+ userId+ "/" + imageName);

                    UploadTask uploadTask = imageRef.putFile(croppedImageUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            // Store the image URL in the Realtime Database
                            imagesUrls.add(downloadUri.toString());
                        });
                    });
                    imageCount++;
                } else {
                    // Handle errors (e.g., user canceled cropping)
                    Toast.makeText(this, "Image cropping canceled", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (imageCount >= 4) {
            // Handle maximum image limit
            Toast.makeText(this, "Maximum image limit reached (4 images)", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImageToFirebaseStorage(Uri imageUri) {

    }


    // Add this method to show the AlertDialog for image selection
    private void showImageSelectionDialog() {
        isDialogShowing = true;
        inUCropFlow = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItems.this);
        builder.setTitle("Select Image");
        builder.setMessage("Choose item image from gallery.");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                inUCropFlow = true;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked on Cancel, so just close the dialog
                Intent intent = new Intent(AddItems.this, CreateCatalogList.class); // Replace "PreviousActivity" with the appropriate activity class
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });



        dialog = builder.create();

        // Set the dialog to not be canceled on touch outside
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // Show the dialog
        dialog.show();

       }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void storeImageUrls(DatabaseReference itemRef, DatabaseReference newproductRef) {
        // Store image URLs under "imageUrls" node for the current item
        for (int i = 0; i < imageCount; i++) {
//            String firstimage = imagesUrls.get(0);
//            itemRef.child("firstimage").child(String.valueOf(0)).setValue(firstimage);
            String imageUrl = imagesUrls.get(i);
            itemRef.child("imageUrls").child(String.valueOf(i)).setValue(imageUrl);
            newproductRef.child("imageUrls").child(String.valueOf(i)).setValue(imageUrl);

            Intent imageIntent = new Intent(getApplicationContext(), ItemAdapter.class);
            imageIntent.putExtra("image0", imagesUrls.get(0));

            // Store the first image URL directly under the item
            if (i == 0) {
                itemRef.child("firstImageUrl").setValue(imageUrl);
                newproductRef.child("firstImageUrl").setValue(imageUrl);
            }
        }

    }

    private void addImageView(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        // Add some debugging log statements
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.image_width),
                getResources().getDimensionPixelSize(R.dimen.images_height)
        ));
       imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(imageUri);
        imageView.setTag(imageViewCount + 1);

        int insertIndex = imageContainer.indexOfChild(findViewById(R.id.imageView1));

        // Insert the new image view at the calculated index
        imageContainer.addView(imageView, insertIndex);
       // imageContainer.addView(imageView,0);
        imageViewCount++;

        if (imageViewCount == MAX_IMAGES) {
            // Hide the "Add Image" view
            findViewById(R.id.imageView1).setVisibility(View.GONE);
        }
    }

    private void showImageSelectiondialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItems.this);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setView(customLayout);

        // Find views in the custom layout
        ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
        TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
        TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
        Button positiveButton = customLayout.findViewById(R.id.positiveButton);

        // Customize the views as needed
        Glide.with(this).asGif().load(R.drawable.gif2).into(alertImageView); // Replace with your image resource
        alertTitle.setText("प्रीमियम");
        alertMessage.setText("आपल फ्री कॅटलॉग लिमिट संपले आहे. \n" +
                "अधिक प्रॉडक्ट ॲड करण्यासाठी प्रिमि यम प्लॅन निवडा.");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItems.this, PremiumMembership.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });

        // Create and show the dialog
        dialog = builder.create();
        dialog.show();
    }

}