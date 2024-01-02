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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class ItemInformation extends AppCompatActivity implements ItemImagesAdapter.ImageClickListener{



    private HorizontalScrollView horizontalScrollView;
    private LinearLayout imageContainer;
    private int imageViewCount = 0;
    private final int MAX_IMAGES = 4;
    ImageView back, deleteitem;
    EditText itemname, itemprice, itemdiscription, itemsellingprice;
    TextView catlogtextview, textview, introtextview;
    Button save;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference newItemRef, usersRef;
    String contactNumber,ContactNumber, itemkey, userId;
    //private HashMap<Integer, String> imageUrls = new HashMap<>();
    List<String> imagesUrls = new ArrayList<>();
    // private List<String> imageUrls = new ArrayList<>();
    // Counter for generating image keys
    private int imageCounter = 1;
    boolean isFirstItemAdded = false;
    // Declare a global variable to store the selected image URI
    private Uri selectedImageUri;
    ItemImagesAdapter imageAdapter;
    private List<String> imageUrls;
    private static final String USER_ID_KEY = "userID";
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private int currentImagePosition = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);

        //horizontalScrollView = findViewById(R.id.horizontalScrollView);
        imageContainer = findViewById(R.id.imageContainer);
        // Align the images at the beginning of the LinearLayout
        //imageContainer.setGravity(Gravity.START);

        back = findViewById(R.id.back);
        deleteitem = findViewById(R.id.deleteitem);
        itemname = findViewById(R.id.itemname);
        itemprice = findViewById(R.id.itemprice);
        itemdiscription = findViewById(R.id.itemdiscription);
        save = findViewById(R.id.save);
        catlogtextview = findViewById(R.id.catlogtextview);
        textview = findViewById(R.id.textsview);
        itemsellingprice = findViewById(R.id.itemsellingprice);
//        itemname.setEnabled(false);
//        itemprice.setEnabled(false);
//        itemdiscription.setEnabled(false);
        introtextview = findViewById(R.id.introtextview);
    //    introtextview.setVisibility(View.VISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseApp.initializeApp(this);
        databaseReference = firebaseDatabase.getReference("Shop");
        storageReference = storage.getReference("item_images");

        // imageButton = findViewById(R.id.uploadimage);
        // Initialize RecyclerView
        RecyclerView imageRecyclerView = findViewById(R.id.horizantalrecyclerview);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        // imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));

        // Create an instance of the ImageAdapter
        imageAdapter = new ItemImagesAdapter(this, imageUrls, this);
        imageRecyclerView.setAdapter(imageAdapter);


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

        deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ItemInformation.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.item_delete, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_Delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ItemInformation.this);
                                builder.setTitle("Delete Item");
                                builder.setMessage("Are you sure you want to delete your Item?");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Handle the delete action here
                                        // You can put your Firebase database delete code here

                                        // Example code to delete a node in Firebase Realtime Database
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Shop").child(contactNumber).child("items").child(itemkey).removeValue();
                                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference();
                                        productRef.child("Products").child(contactNumber).child(itemkey).removeValue();

                                        Log.d("TAG", "onClick: " + itemkey);
                                        DatabaseReference promoteitemRef = databaseReference.child(contactNumber).child("promotedShops")
                                                .child(contactNumber).child("items");
                                        DatabaseReference hepromoteitemRef = databaseReference.child(contactNumber).child("hePromoteYou")
                                                .child(contactNumber).child("items");
                                        hepromoteitemRef.child(itemkey).removeValue();
                                        System.out.println("weffg " +promoteitemRef);
                                        promoteitemRef.child(itemkey).removeValue();
                                        // Close the dialog
                                        dialogInterface.dismiss();
                                        // Redirect to the business page
                                        Intent resultIntent = new Intent();
                                        setResult(RESULT_OK, resultIntent); // Use RESULT_OK to indicate success

                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User clicked on Cancel, so just close the dialog
                                        dialogInterface.dismiss();
                                    }
                                });

                                // Create and show the AlertDialog
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newItemName = itemname.getText().toString().trim();
                String itemPrice = itemprice.getText().toString().trim();
                // Parse the price as a double
//                double Price = Double.parseDouble(itemPrice);
                String itemDesc = itemdiscription.getText().toString().trim();
                String itemSell = itemsellingprice.getText().toString().trim();


//                if (TextUtils.isEmpty(newItemName)) {
//                    // Show an error message for missing item name
//                    itemname.setError("Item name is required");
//                    return;
//                }
                // If a new image URL is available in the adapter for the current item, update it
                if (currentImagePosition != -1) {
                    String newImageURL = imageAdapter.getImageURLAtPosition(currentImagePosition);
                    storeImageUrls(newImageURL,currentImagePosition);
                }

                // Show progress dialog while updating data
                ProgressDialog progressDialog = new ProgressDialog(ItemInformation.this);
                progressDialog.setMessage("Updating...");
                progressDialog.setCancelable(true);
                progressDialog.show();

                DatabaseReference itemRef = databaseReference.child(contactNumber).child("items").child(itemkey);
                DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products")
                                               .child(contactNumber).child(itemkey);

              //  String formattedPrice = formatPrice(Price);
                Map<String, Object> updates = new HashMap<>();
                updates.put("itemname", newItemName); // Update the item name
               // updates.put("price", formattedPrice);
                updates.put("description", itemDesc);
                // Check if itemPrice is not empty and contains the currency symbol "₹"
                if (!TextUtils.isEmpty(itemPrice)) {
                    if (itemPrice.startsWith("₹")) {
                        updates.put("price", itemPrice); // Update the price as-is
                    } else {
                        // Format the price before updating
                        double Price = Double.parseDouble(itemPrice);
                        String formattedPrice = formatPrice(Price);
                        updates.put("price", formattedPrice);
                    }
                } else {
                    // Handle the case where itemPrice is empty (set it to null or a default value if needed)
                    updates.put("price", null); // Set it to null in this example
                }

                if (!TextUtils.isEmpty(itemSell)) {
                    if (itemSell.startsWith("₹")) {
                        updates.put("sell", itemSell); // Update the price as-is
                    } else {
                        // Format the price before updating
                        double Sell = Double.parseDouble(itemSell);
                        String formattedPrice = formatPrice(Sell);
                        updates.put("sell", formattedPrice);
                    }
                } else {
                    // Handle the case where itemPrice is empty (set it to null or a default value if needed)
                    updates.put("selling", null); // Set it to null in this example
                }


                // Use itemRef.updateChildren to update only the specified fields
                itemRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Dismiss the progress dialog
                            progressDialog.dismiss();

                            // Redirect to the create catalog page or perform any other action
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            // Handle any errors if the update fails
                        }
                    }
                });

                productRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Dismiss the progress dialog
                            progressDialog.dismiss();

                            // Redirect to the create catalog page or perform any other action
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            // Handle any errors if the update fails
                        }
                    }
                });

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemInformation.this.finish();
            }
        });

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        String itemPrice = intent.getStringExtra("itemPrice");
        String itemDescription = intent.getStringExtra("itemDescription");
        String itemSellPrice = intent.getStringExtra("itemSell");

        itemname.setText(itemName);
        itemprice.setText(itemPrice);
        itemdiscription.setText(itemDescription);
        itemsellingprice.setText(itemSellPrice);

        itemkey = intent.getStringExtra("itemKey");
        Log.d("itemKey ",""+itemkey);
        Log.d("itemName ",""+itemName);

        getData();

    }

    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##,##0.00", symbols);
        return "₹ " + decimalFormat.format(price);
    }
    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        ItemInformation.this.finish();
    }

    public void onImageViewClick(View view) {
        if (imageViewCount < MAX_IMAGES) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, imageViewCount);
        }
    }

    public void onCameraClick(View view){
        if (imageViewCount < MAX_IMAGES) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, imageViewCount);
        }
    }

    @Override
    public void onGalleryClick(int position) {
        currentImagePosition = position;

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    private void startImageCropper(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image"));

        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Crop Image");
        options.setCompressionQuality(90);

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Set the aspect ratio (square in this case)
                .withMaxResultSize(400, 400) // Set the max result size
                .start(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {

            if (data != null) {
                Uri selectedImageUri = data.getData();

                // Start the UCrop activity for image cropping
                startImageCropper(selectedImageUri);
            }
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                // Handle the cropped image result from UCrop
            if (data != null) {
                Uri croppedImageUri = UCrop.getOutput(data);

                if (croppedImageUri != null) {
                    // Update the RecyclerView to display the cropped image
                    imageAdapter.updateImageAtPosition(currentImagePosition, croppedImageUri.toString());

                    // Upload the cropped image to Firebase Storage and store the URL
                    uploadImageToFirebaseStorage(croppedImageUri, currentImagePosition);
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    // Handle any errors that occurred during cropping
                    Throwable error = UCrop.getError(data);
                    Toast.makeText(this, "Error cropping image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri, int position) {
        // Create a unique filename for the image (e.g., using a timestamp)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String imageName = "item_images" + timestamp + ".jpg";

        // Create a StorageReference to the image in Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(imageName);

        // Upload the image to the specified StorageReference
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, now get the download URL
                    storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Handle the obtained download URL
                                String imageUrl = uri.toString();

                                // Update the selected image in the adapter
                                imageAdapter.updateImageAtPosition(position, imageUrl);

                                // Store the image URL in the Realtime Database at the specified position
                               //storeImageUrls(imageUrl, position);
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors while getting the download URL
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors during the image upload
                });
    }

    private void storeImageUrls(String imageUrl, int position) {
        DatabaseReference itemRef = databaseReference.child(contactNumber).child("items").child(itemkey);

        // Create a map to store the image URLs
        Map<String, Object> imageUrlsMap = new HashMap<>();

        // First, get the existing image URLs from the database (if any)
        itemRef.child("imageUrls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> existingImageUrls = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot imageUrlSnapshot : dataSnapshot.getChildren()) {
                        existingImageUrls.add(imageUrlSnapshot.getValue(String.class));
                    }
                }

                // Update the specified position in the list with the new image URL
                existingImageUrls.set(position, imageUrl);

                // Update the "imageUrls" node in the database
                imageUrlsMap.put("imageUrls", existingImageUrls);

                // Update the database with the entire map
                itemRef.updateChildren(imageUrlsMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Dismiss the progress dialog
                          //  progressDialog.dismiss();

                            // Redirect to the create catalog page or perform any other action

                            // Optional, depending on your navigation flow
                        } else {
                            // Handle any errors if the update fails
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }

    public void getData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    Log.d("fgsdgfsdgsdf", "" + contactNumber);
                    DatabaseReference ref = database.getReference("Shop/" + ContactNumber + "/items/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                List<String> imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("fgsdgfsdgsdf", "" + img);
                                    imageUrls.add(img);

                                }
                                imageAdapter.setImagesUrls(imageUrls);
                                imageAdapter.notifyDataSetChanged();
                            } else {
                                // Log.d("sasff","no "+expdate);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Log.w(TAG, "onCancelled", databaseError.toException());
                        }
                    });

                    DatabaseReference imageref = database.getReference("Shop/" + contactNumber + "/items/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    imageref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                List<String> imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("fgsdgfsdgsdf", "" + img);
                                    imageUrls.add(img);
                                }
                                imageAdapter.setImagesUrls(imageUrls);
                                imageAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled (DatabaseError error){

            }
        });
    }




//
//    private RecyclerView recyclerViewImages;
//    private List<String> imageUrls;
//
//    private RecyclerView recyclerView;
//    private ItemImagesAdapter imageAdapter;
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference, usersRef;
//    String contactNumber, ContactNumber;
//    private List<ItemList> itemList;
//    ImageView back;
//    Button btnmessage;
//    String itemkey;
//    String itemName;
//    String itemPrice;
//    TextView itemNameTextView, itemDescriptionTextView;
//    private boolean isFirstOrder = true;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_item_information);
//
//        itemDescriptionTextView = findViewById(R.id.descriptiontext);
//        itemDescriptionTextView.setVisibility(View.GONE);
//        btnmessage = findViewById(R.id.btnmessage);
//        back = findViewById(R.id.back);
//
//        // Initialize RecyclerView
//        RecyclerView imageRecyclerView = findViewById(R.id.horizantalrecyclerview);
//        imageRecyclerView.setHasFixedSize(true);
//        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        //int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
//        // imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));
//
//        // Create an instance of the ImageAdapter
//        imageAdapter = new ItemImagesAdapter(this, imageUrls);
//        imageRecyclerView.setAdapter(imageAdapter);
//
//        Intent sharedIntent = IntentDataHolder.getSharedIntent();
//        if (sharedIntent != null) {
//            ContactNumber = sharedIntent.getStringExtra("contactNumber");
//
//            //Log.d("contactNumber", "" + contactNumber);
////            // Convert the image URI string to a URI object
////            Uri imageUri = Uri.parse(image);
////            Log.d("imageUri", "" + imageUri);
////            catalogshopname.setText(shopName);
////            // Load image using Glide
////            RequestOptions requestOptions = new RequestOptions()
////                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Optional: Set caching strategy
////
////            Glide.with(this)
////                    .load(image).centerCrop()
////                    .apply(requestOptions)
////                    .into(catalogshopimage);
//
//        }
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        FirebaseApp.initializeApp(this);
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("Shop");
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        String userId = sharedPreference.getString("mobilenumber", null);
//        if (userId != null) {
//            // userId = mAuth.getCurrentUser().getUid();
//            System.out.println("dffvf  " +userId);
//        }
//        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
//                    DatabaseReference shopRef = databaseReference.child(contactNumber).child("items");
//                    System.out.println("items " + shopRef);
//
//                    shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
////                                String itemName = itemSnapshot.child("itemname").getValue(String.class);
////                                String itemPrice = itemSnapshot.child("price").getValue(String.class);
//
//
//                                //String itemPrice = itemPriceTextView.getText().toString();
//
//                                DatabaseReference requestsRef = databaseReference.child(contactNumber).child("requests");
//                                //DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId).child("requests");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle the error gracefully
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error gracefully
//            }
//        });
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//
//        // Retrieve data from Intent
//        Intent intent = getIntent();
//        itemName = intent.getStringExtra("itemName");
//        itemPrice = intent.getStringExtra("itemPrice");
//        String itemDescription = intent.getStringExtra("itemDescription");
//        if (itemDescription!=null && !itemDescription.isEmpty())
//        {
//            itemDescriptionTextView.setVisibility(View.VISIBLE);
//        }
//        else{
//            itemDescriptionTextView.setVisibility(View.GONE);
//        }
//        itemkey = intent.getStringExtra("itemKey");
//        Log.d("itemKey ",""+itemkey);
//        Log.d("itemName ",""+itemName);
//        ArrayList<String> itemImageUrls = intent.getStringArrayListExtra("itemImageUrls");
//
//
//        // Use the retrieved data to populate your layout elements
//        itemNameTextView = findViewById(R.id.Name);
//        TextView itemPriceTextView = findViewById(R.id.price);
//        TextView itemDescriptionTextView = findViewById(R.id.description);
//
//
//        itemNameTextView.setText(itemName);
//        itemPriceTextView.setText(itemPrice);
//        itemDescriptionTextView.setText(itemDescription);
//
//
//        getData();
//
//        // Load the image using a library like Picasso or Glide
//        //Picasso.get().load(itemImage).into(itemImageView);
//    }
//    @Override
//    public void onBackPressed() {
//        // Navigate to the previous page when the back button is pressed
//        super.onBackPressed();
//        finish();
//    }
//
//    private void storeItemNameOnly(DatabaseReference ordersRef, String itemName) {
//        // Fetch the current list of item names
//        ordersRef.child("itemNames").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<String> itemNames = new ArrayList<>();
//
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                        String existingItemName = itemSnapshot.getValue(String.class);
//                        itemNames.add(existingItemName);
//                    }
//                }
//
//                // Add the new item name to the list
//                itemNames.add(itemName);
//
//                // Update the item names in the Firebase node
//                ordersRef.child("itemNames").setValue(itemNames)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Order successfully stored in the database
//                                // You can show a confirmation message if needed
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Handle failure to store the order
//                            }
//                        });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error
//            }
//        });
//    }
//
//
//    public void getData(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
////                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
//                    Log.d("fgsdgfsdgsdf", "" + contactNumber);
//                    DatabaseReference ref = database.getReference("Shop/" + ContactNumber + "/items/" + itemkey + "/imageUrls/");
//                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
//                    ref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                int x = 0;
//                                List<String> imageUrls = new ArrayList<>();
//                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
//                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
//                                    Log.d("fgsdgfsdgsdf", "" + img);
//                                    imageUrls.add(img);
//
////                                    if (x++ == dataSnapshot.getChildrenCount() - 1) {
////                                        // Log.d("ovov",""+uname);
////                                        ItemList shop = new ItemList();
////                                        // Update the image URLs in the Shop object
////                                         shop.setImagesUrls(imageUrls);
////                                        // Notify the adapter about the data change
////                                        imageAdapter.setImagesUrls(imageUrls);
////                                        imageAdapter.notifyDataSetChanged();
////                                    }
//
//                                }
//                                imageAdapter.setImagesUrls(imageUrls);
//                                imageAdapter.notifyDataSetChanged();
//                            } else {
//                                // Log.d("sasff","no "+expdate);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Log.w(TAG, "onCancelled", databaseError.toException());
//                        }
//                    });
//
//                    DatabaseReference imageref = database.getReference("Shop/" + contactNumber + "/items/" + itemkey + "/imageUrls/");
//                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
//                    imageref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                int x = 0;
//                                List<String> imageUrls = new ArrayList<>();
//                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
//                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
//                                    Log.d("fgsdgfsdgsdf", "" + img);
//                                    imageUrls.add(img);
//                                }
//                                imageAdapter.setImagesUrls(imageUrls);
//                                imageAdapter.notifyDataSetChanged();
//                            } else {
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled (DatabaseError error){
//
//            }
//        });
//    }
}



