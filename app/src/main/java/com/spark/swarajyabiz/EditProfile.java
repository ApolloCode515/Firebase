package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.spark.swarajyabiz.ui.FragmentHome;
import com.spark.swarajyabiz.ui.FragmentProfile;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class EditProfile extends AppCompatActivity implements ImageAdapter.ImageClickListener{

    EditText name, shopname, email, address, contactnumber, service, phonenumber;
    Spinner districtspinner, talukaspinner;
    ImageView back, deleteprofile;
    ImageView circleImageView;
    private Uri croppedImageUri = null , imageUris;
    private final int PICK_SINGLE_IMAGE_REQUEST = 1;
    private final int PICK_MULTIPLE_IMAGES_REQUEST = 2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MAX_ALLOWED_IMAGES = 5;

    Button btnregister, selectimage;
    ImageView imageButton;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    DatabaseReference databaseReference, usersRef, shopkeyRef;
    StorageReference storageReference, storagereference;
    Shop shopInfo;
    List<String> imageUrls = new ArrayList<>();
    RecyclerView imageRecyclerView;

    ImageAdapter imageAdapter;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Button getCurrentLocationButton;
    private Button saveLocationButton;
    private SharedPreferences sharedPreferences;
    String contactNumber;
    LinearLayout addimage;
    // Firebase

    private LocationCallback locationCallback;
    private ArrayAdapter<String> talukaAdapter;
    String selecteddistrict, selectedtaluka, shopkey;
    private boolean isTalukaSelected = false;
    private static final String USER_ID_KEY = "userID";
    private static final int CROP_IMAGE_REQUEST_CODE = 123; // Use any unique integer value
    // Initialize hasLoggedIn as false by default


    double latitude;
    double longitude;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.editname);
        shopname = findViewById(R.id.editshopname);
        email = findViewById(R.id.editemail);
        address = findViewById(R.id.editaddress);
        contactnumber = findViewById(R.id.editwhatsappno);
        phonenumber = findViewById(R.id.editcontactno);
        phonenumber.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
        //service = findViewById(R.id.editservice);
        btnregister = findViewById(R.id.btnregister);
        back = findViewById(R.id.back);
        selectimage = findViewById(R.id.selectImagesButton);
        circleImageView = findViewById(R.id.profileimage);
        districtspinner = findViewById(R.id.editdistrictSpinner);
        talukaspinner = findViewById(R.id.edittalukaSpinner);
        deleteprofile = findViewById(R.id.deleteProfile);
        addimage = findViewById(R.id.addimage);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseApp.initializeApp(this);
        databaseReference = firebaseDatabase.getReference("Shop");
        storageReference = storage.getReference("images");
        storagereference = storage.getReference("image");

        // Check if the profile has been created
        /*sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean profileCreated = sharedPreferences.getBoolean("profileCreated", false);
        if (profileCreated) {
            // Navigate to the business page
            Intent intent = new Intent(Create_Profile.this, Business.class);
            startActivity(intent);
            finish(); // Prevent going back to the Create_Profile activity when pressing the back button
        }*/

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

        // imageButton = findViewById(R.id.uploadimage);
        imageRecyclerView = findViewById(R.id.imageRecyclerView);
        imageRecyclerView.setVisibility(View.GONE);
        imageAdapter = new ImageAdapter(this, imageUrls, this, imageRecyclerView);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageRecyclerView.setAdapter(imageAdapter);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));

        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);


        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple image selection
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_MULTIPLE_IMAGES_REQUEST);
                // Create an alert dialog with options to Remove Images and Update Images
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                builder.setTitle("Image Options");
                builder.setItems(new CharSequence[]{"Remove Images", "Update Images"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Remove Images option selected
                                removeImages();
                                break;
                            case 1:
                                // Update Images option selected
                                selectImages();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_SINGLE_IMAGE_REQUEST);
                showImageOptionsDialog();
            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageOptionsDialog();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.districts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtspinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtspinner.setAdapter(adapter);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Firebase initialization
        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //  databaseReference = firebaseDatabase.getReference("locations");

        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestLocationPermission();
                //showLocationSettingsDialog();
            }
        });
        // Request the necessary permissions
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
       String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
         usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);



        districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected district from the first spinner
                selecteddistrict = adapterView.getItemAtPosition(position).toString();

                // Load sub-division data based on the selected district
                int subDivisionArrayId = getResources().getIdentifier(
                        "Taluka_array_" + selecteddistrict.toLowerCase(),
                        "array",
                        getPackageName()
                );

                if (subDivisionArrayId != 0) {
                    String[] subDivisions = getResources().getStringArray(subDivisionArrayId);
                    talukaAdapter = new ArrayAdapter<>(EditProfile.this,
                            android.R.layout.simple_spinner_item, subDivisions);
                    talukaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    talukaspinner.setAdapter(talukaAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        talukaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected taluka from the second spinner
                selectedtaluka = adapterView.getItemAtPosition(position).toString();
                isTalukaSelected = !selectedtaluka.equals("Select Taluka");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
                isTalukaSelected = false;
            }
        });



        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress dialog while updating profile
                ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
                progressDialog.setMessage("Updating profile...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Get the user ID
                SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                String userId = sharedPreference.getString("mobilenumber", null);
                if (userId != null) {
                    // userId = mAuth.getCurrentUser().getUid();
                    System.out.println("dffvf  " +userId);
                }
                String names = name.getText().toString();
                String shopnames = shopname.getText().toString();
                String addresses = address.getText().toString();
                String contactNumber = contactnumber.getText().toString();
                String phoneNumber = phonenumber.getText().toString();
                String emailId = email.getText().toString();
               // String serviceType = service.getText().toString();

                DatabaseReference shopReference = databaseReference.child(contactNumber);



                shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Shop existingShopInfo = dataSnapshot.getValue(Shop.class);
                        if (existingShopInfo == null) {
                            // Shop information doesn't exist, show error message
                            Toast.makeText(EditProfile.this, "Profile doesn't exist. Cannot update.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }

                        // Update the shop information with modified fields
                        existingShopInfo.setName(names);
                        existingShopInfo.setShopName(shopnames);
                        existingShopInfo.setAddress(addresses);
                        existingShopInfo.setPhoneNumber(phoneNumber);
                        existingShopInfo.setEmail(emailId);
                        //existingShopInfo.setService(serviceType);
                        existingShopInfo.setDistrict(selecteddistrict);
                        existingShopInfo.setTaluka(selectedtaluka);

                        if (!isTalukaSelected) {
                            progressDialog.dismiss();
                            // Show an error message if taluka is not selecte
                            Toast.makeText(EditProfile.this, "Please select taluka", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(RegisterActivity.this, "Please select a taluka.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Update the shop information in Realtime Database
                        updateShopInfo(shopReference, userId, existingShopInfo, true); // shop contactnumber
                       // updateShopInfo(editRef, userId, existingShopInfo, true); // shop - contactnumber - promoteshops - contactnumber
                       // updateShopInfo(shopkeyRef, userId, existingShopInfo, true); //  shop - shopkey(all contactnumbers) - promoteshops - contactnumber
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle onCancelled
                        progressDialog.dismiss();
                    }
                });
            }
        });

        deleteprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(EditProfile.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_delete, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_Delete:

                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                                builder.setTitle("Delete Profile");
                                builder.setMessage("Are you sure you want to delete your profile?");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Handle the delete action here
                                        // You can put your Firebase database delete code here

                                        // Example code to delete a node in Firebase Realtime Database
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference promoteRef = databaseReference.child("Shop").child(contactNumber).child("promotedShops");
                                        DatabaseReference removeRef = databaseReference.child("Shop").child(contactNumber).child("hePromoteYou");

                                        promoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                        String promotekey = dataSnapshot.getKey();
                                                        System.out.println("sfujv " +promotekey);

                                                        databaseReference.child("Shop").child(promotekey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()){
                                                                    int currentPromotionCount = snapshot.child("promotionCount").getValue(Integer.class);
                                                                    System.out.println("fvgff " +currentPromotionCount);

                                                                    // Decrease promotion count by one
                                                                    if (currentPromotionCount > 0) {
                                                                        databaseReference.child("Shop").child(promotekey).child("promotionCount").setValue(currentPromotionCount - 1);
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                            }
                                        });

                                        removeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                        String promotekey = dataSnapshot.getKey();
                                                        System.out.println("sfujv " +promotekey);
                                                        DatabaseReference shopRef = databaseReference.child("Shop").child(promotekey).child("promotedShops");
                                                        shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()){

                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                        String promotekey = dataSnapshot.getKey();
                                                                        System.out.println("sfujv " +promotekey);
                                                                        if (contactNumber.equals(promotekey)) {
                                                                            shopRef.child(promotekey).removeValue();
                                                                        }
                                                                    }


                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                            }
                                        });


                                        databaseReference.child("Shop").child(contactNumber).removeValue();
                                        Log.d("TAG", "onClick: " +contactNumber);
                                        // Close the dialog
                                        dialogInterface.dismiss();
                                        // Redirect to the business page
//                                        Intent intent = new Intent(EditProfile.this, FragmentHome.class); // Replace "PreviousActivity" with the appropriate activity class
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                        startActivity(intent);
                                        finish(); // Finish the current activity
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Create a method to show the alert dialog with options
    private void showImageOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image Options");
        builder.setMessage("To update your profile, first remove your existing profile, and then click on 'Update'.");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openGallery();
            }
        });
        builder.setNegativeButton("Remove Image", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              removeImage();
            }
        });
        builder.show();
    }

    // Method to remove the image from Firebase Realtime Database
    private void removeImage() {
        // Assuming you have a DatabaseReference for the shop
        DatabaseReference shopReference = databaseReference.child(contactNumber);
        Log.d("TAG", "removeImage: " +shopReference);
        shopReference.child("url").removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Image removed successfully
                        circleImageView.setImageResource(R.drawable.ic_outline_person_24);
                        addimage.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to remove the image
                        Toast.makeText(EditProfile.this, "Failed to remove image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Remove the image URL from the "promotedshops" node
        DatabaseReference promotedShopsReference = databaseReference.child(contactNumber).child("promotedShops");
        promotedShopsReference.child(contactNumber).child("url").removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Image removed successfully from the "promotedshops" node
                        circleImageView.setImageResource(R.drawable.ic_outline_person_24);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to remove the image from the "promotedshops" node
                        Toast.makeText(EditProfile.this, "Failed to remove image from promotedshops: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to open the gallery and set the selected image
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_SINGLE_IMAGE_REQUEST);
    }

    private void removeImages() {
        DatabaseReference shopReference = databaseReference.child(contactNumber);
        Log.d("TAG", "removeImage: " +shopReference);
        shopReference.child("imageUrls").removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        imageUrls.clear();
                        recreate();
                        Toast.makeText(EditProfile.this, "Remove Successfully", Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(EditProfile.this, Profile.class); // Replace "PreviousActivity" with the appropriate activity class
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(intent);
//                        finish(); // Finish the current activity
                        imageRecyclerView.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to remove the image
                        Toast.makeText(EditProfile.this, "Failed to remove image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Remove the image URL from the "promotedshops" node
        DatabaseReference promotedShopsReference = databaseReference.child(contactNumber).child("promotedShops");
        promotedShopsReference.child(contactNumber).child("imageUrls").removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Image removed successfully from the "promotedshops" node
                        circleImageView.setImageResource(R.drawable.ic_outline_person_24);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to remove the image from the "promotedshops" node
                        Toast.makeText(EditProfile.this, "Failed to remove image from promotedshops: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to select multiple images
    private void selectImages() {
        // Create an intent for image selection
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Start the image selection activity
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_MULTIPLE_IMAGES_REQUEST);
    }


    private void updateShopInfo(DatabaseReference shopReference, String userId, Shop shopInfo, boolean isContactModified) {
        DatabaseReference requestsReference = shopReference.child("requests");


        // Retrieve the existing requests node
        requestsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Store the existing requests node value
                Object requestsNodeValue = dataSnapshot.getValue();

                // Save the shop information to Realtime Database
                shopReference.updateChildren(shopInfo.toMap())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Save the shop information to Realtime Database with the user ID
                                shopReference.child("userId").setValue(userId);

                                // Check if image is modified
                                if (croppedImageUri != null) {
                                    // Create a reference to the image file in Firebase Storage
                                    StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

                                    try {
                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Adjust the compression quality as needed

                                        // Convert the compressed bitmap back to Uri
                                        byte[] byteArray = baos.toByteArray();
                                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                                        fileReference.putStream(byteArrayInputStream).addOnSuccessListener(taskSnapshot -> {
                                            // Get the download URL of the uploaded image
                                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {

                                                        String imageUrl = uri.toString();

                                                        // Update the image URL in shopInfo
                                                        shopInfo.setUrl(imageUrl);

                                                        // Save the updated image URL to Realtime Database
                                                        shopReference.child("url").setValue(imageUrl);
                                                        DatabaseReference editRef = databaseReference.child(contactNumber).child("promotedShops").child(contactNumber);
                                                        System.out.println("dhbfjhb " +editRef.toString());
                                                        editRef.child("url").setValue(imageUrl);
                                                        croppedImageUri = null;
                                                        // Save the shop information to Realtime Database
                                                        updateShopInfoComplete(shopReference, userId, shopInfo, requestsNodeValue);
                                                        //updateShopInfoComplete(shopkeyRef, userId, shopInfo, requestsNodeValue);
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(EditProfile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            //progressDialog.dismiss();
                                                        }
                                                    });
                                        }).addOnFailureListener(e -> {
                                            // Handle failure if uploading image fails
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    // No image modification, save the shop information to Realtime Database
                                    updateShopInfoComplete(shopReference, userId, shopInfo, requestsNodeValue);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


    private void updateShopInfoComplete(DatabaseReference shopReference, String userId, Shop shopInfo, Object requestsNodeValue) {
        // Save the shop information to Realtime Database
        shopReference.child("userId").setValue(userId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Restore the existing requests node value
                        shopReference.child("requests").setValue(requestsNodeValue);

                        // Check if images are modified
                        if (!imageUrls.isEmpty()) {
                            // Update the imageUrls list in shopInfo
                            shopInfo.setImageUrls(imageUrls);
                            shopReference.child("imageUrls").setValue(imageUrls)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Save the shop information to Realtime Database
                                            saveShopInfo(shopReference, shopInfo);

                                            // Update the same data in the "promotedshops" node
                                            DatabaseReference promotedShopsReference = shopReference.child("promotedShops");
                                            Log.d("TAG", "onSuccess: " +promotedShopsReference);
                                            promotedShopsReference.child(shopInfo.getContactNumber()).setValue(shopInfo);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditProfile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Save the shop information to Realtime Database
                            saveShopInfo(shopReference, shopInfo);

                            // Update the same data in the "promotedshops" node
                            DatabaseReference promotedShopsReference = shopReference.child("promotedShops");
                            promotedShopsReference.child(shopInfo.getContactNumber()).setValue(shopInfo);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveShopInfo(DatabaseReference shopReference, Shop shopInfo) {

        // Show progress dialog while creating profile
        ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setMessage("Updating profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();



        // Clear the input fields and image selection
        name.setText("");
        shopname.setText("");
        address.setText("");
        phonenumber.setText("");
        email.setText("");
       // service.setText("");
        circleImageView.setImageResource(R.drawable.ic_outline_person_24);
        addimage.setVisibility(View.VISIBLE);
        imageUrls.clear();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot keySnapshot : snapshot.getChildren()){
                        shopkey = keySnapshot.getKey();
                        System.out.println("dfvter " +shopkey);
                        shopkeyRef = databaseReference.child(shopkey).child("promotedShops").child(contactNumber);
                        System.out.println("ggjng " +shopkeyRef);
                    }

                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

        // Save the shop information to Realtime Database at shopReference
        shopReference.updateChildren(shopInfo.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // After successfully updating shopReference, update shopkeyRef
                        shopkeyRef.updateChildren(shopInfo.toMap())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditProfile.this, "Update Successfully", Toast.LENGTH_SHORT).show();

                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfile.this, "Failed to add data to shopkeyRef: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Failed to add data to shopReference: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void startImageCropper(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image"));

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Set the aspect ratio (square in this case)
                .withMaxResultSize(300, 300) // Set the max result size
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
           Uri imageUri = data.getData();
            circleImageView.setImageDrawable(null);
            Log.d("Debug", "dfgdfg " + imageUri.toString());
            if (imageUri != null) {
                // Set the selected image from the gallery to the CircleImageView

               // circleImageView.setImageURI(imageUri);
                System.out.println("fdgvfdv " +imageUri.toString());

                // Save the selected image URI for later use if needed
                croppedImageUri = imageUri;
                addimage.setVisibility(View.GONE);
                startImageCropper(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            croppedImageUri = UCrop.getOutput(data);

            if (croppedImageUri != null) {
                Picasso.get().invalidate(croppedImageUri);
                circleImageView.setImageDrawable(null);

                addimage.setVisibility(View.GONE);
                System.out.println("cdcvd " +croppedImageUri.toString());


               // uploadCroppedImageToFirebase(croppedImageUri);
                // You can save the cropped image URI for the user's profile picture
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle any errors that occurred during cropping
            Throwable error = UCrop.getError(data);
        }

//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == PICK_SINGLE_IMAGE_REQUEST) {
//                if (data != null && data.getData() != null) {
//                   Uri imageUri = data.getData();
//                    startImageCropper(imageUri);
//                    addimage.setVisibility(View.GONE);
//                } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
//                    Uri croppedImageUri = UCrop.getOutput(data);
//                    System.out.println("efrwfwr " + croppedImageUri.toString());
//                    if (croppedImageUri != null) {
//                        // Display the cropped image
//                        circleImageView.setImageURI(croppedImageUri);
//                        // You can save the cropped image URI for the user's profile picture
//                    }
//                } else if (resultCode == UCrop.RESULT_ERROR) {
//                    // Handle any errors that occurred during cropping
//                    Throwable error = UCrop.getError(data);
//                }
//            } else if (requestCode == PICK_MULTIPLE_IMAGES_REQUEST) {
//                if (data != null && data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount();
//                    if (count <= 5) {
//                        imageRecyclerView.setVisibility(View.VISIBLE);
//                        imageUrls.clear();
//                        imageAdapter.notifyDataSetChanged();
//                        final ProgressDialog progressDialog = new ProgressDialog(this);
//                        progressDialog.setTitle("Loading Images");
//                        progressDialog.setMessage("Please wait...");
//                        progressDialog.setIndeterminate(true);
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
//                        for (int i = 0; i < count; i++) {
//                            Uri imageUris = data.getClipData().getItemAt(i).getUri();
//                            compressAndUploadImage(imageUris, progressDialog, count);
//                        }
//                    }
//                } else if (data != null && data.getData() != null) {
//                    Uri imageUris = data.getData();
//                    final ProgressDialog progressDialog = new ProgressDialog(this);
//                    progressDialog.setTitle("Loading Image");
//                    progressDialog.setMessage("Please wait...");
//                    progressDialog.setIndeterminate(true);
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                    compressAndUploadImage(imageUris, progressDialog, 1);
//                }
//            }
//        }
    }

//    private void uploadCroppedImageToFirebase(Uri croppedImageUri) {
//        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
//
//        fileReference.putFile(croppedImageUri)
//                .addOnSuccessListener(taskSnapshot -> {
//                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                        String imageUrl = uri.toString();
//                        String contactNumber = contactnumber.getText().toString();
//                        saveImageUrlToDatabase(contactNumber, imageUrl); // Pass the contact number and cropped image URL
//                    });
//                })
//                .addOnFailureListener(e -> {
//                    // Handle the error
//                });
//    }

//    private void saveImageUrlToDatabase(String contactNumber, String imageUrl) {
//        // Retrieve the existing value for the "Shop" node based on the contactNumber
//        databaseReference.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // If the "Shop" node with the contactNumber already exists, update the URL
//                    databaseReference.child(contactNumber).child("url").setValue(imageUrl);
//                } else {
//                    // If the "Shop" node doesn't exist, create a new node with the contactNumber and URL
//                    //databaseReference.child(contactNumber).setValue(new Shop(imageUrl));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error
//            }
//        });
//    }


    private void compressAndUploadImage(Uri imageUris, ProgressDialog progressDialog, int count) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUris);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); // Adjust the compression quality as needed
            byte[] imageData = baos.toByteArray();

            // Upload compressed image to Firebase Storage
            StorageReference fileReference = storagereference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String storageUrl = uri.toString();

                            // Add the storage URL to the imageUrls list
                            imageUrls.add(storageUrl);
                            imageAdapter.setImageUrls(imageUrls);
                            imageAdapter.notifyDataSetChanged();

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            // Check if all images are loaded
                            if (imageUrls.size() == count) {
                              //  progressDialog.dismiss(); // Dismiss the progress dialog
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_SINGLE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            circleImageView.setImageURI(imageUri);
//        }
//
//        if (requestCode == PICK_MULTIPLE_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
//            if (data.getClipData() != null) {
//                // Multiple images selected
//
//                int count = data.getClipData().getItemCount();
//
//                if (count <= 5) {
//                    imageUrls.clear();
//                    imageAdapter.notifyDataSetChanged();
//                    final ProgressDialog progressDialog = new ProgressDialog(this);
//                    progressDialog.setTitle("Loading Images");
//                    progressDialog.setMessage("Please wait...");
//                    progressDialog.setIndeterminate(true);
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//
//                    for (int i = 0; i < count; i++) {
//                        imageUris = data.getClipData().getItemAt(i).getUri();
//
//                        // Compress and upload image to Firebase Storage
//                        compressAndUploadImage(imageUris, progressDialog, count);
//                    }
//                } else {
//                    Toast.makeText(this, "Please select a maximum of 5 images", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }


//    private void compressAndUploadImages(Uri imageUris, ProgressDialog progressDialog, int count) {
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUris);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); // Adjust the compression quality as needed
//            byte[] imageData = baos.toByteArray();
//
//            // Upload compressed image to Firebase Storage
//            StorageReference fileReference = storagereference.child(System.currentTimeMillis() + ".jpg");
//
//            fileReference.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String storageUrl = uri.toString();
//
//                            // Add the storage URL to the imageUrls list
//                            imageUrls.add(storageUrl);
//                            imageAdapter.setImageUrls(imageUrls);
//                            imageAdapter.notifyDataSetChanged();
//
//                            if (progressDialog != null) {
//                                progressDialog.dismiss();
//                            }
//                            // Check if all images are loaded
//                            if (imageUrls.size() == count) {
//                              //  progressDialog.dismiss(); // Dismiss the progress dialog
//                            }
//                        }
//                    });
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//




    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            checkLocationEnabled();
        }
    }

    private void checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocationSettingsDialog();
        } else {
            getCurrentLocation();
        }
    }

    private void showLocationSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Required");
        builder.setMessage("Please enable location to proceed");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

// ...

    private void getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                String addresses = getAddressFromLocation(latitude, longitude);
                                address.setText(addresses);
                            } else {
                                // Toast.makeText(Create_Profile.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, "Error getting location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address currentAddress = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();

                // Check if the first address line (usually the street address) matches the locality or administrative area
                if (currentAddress.getAddressLine(0) != null &&
                        (currentAddress.getAddressLine(0).contains(currentAddress.getLocality()) ||
                                currentAddress.getAddressLine(0).contains(currentAddress.getSubAdminArea()))) {
                    stringBuilder.append(currentAddress.getAddressLine(0)).append(", ");
                } else {
                    // Add address components one by one, including district
                    if (currentAddress.getSubLocality() != null) {
                        stringBuilder.append(currentAddress.getSubLocality()).append(", ");
                    }
                    if (currentAddress.getLocality() != null) {
                        stringBuilder.append(currentAddress.getLocality()).append(", ");
                    }
//                    if (currentAddress.getSubAdminArea() != null) {
//                        stringBuilder.append(currentAddress.getSubAdminArea()).append(", ");
//                    }
                    if (currentAddress.getAdminArea() != null) {
                        stringBuilder.append(currentAddress.getAdminArea()).append(", ");
                    }
                    if (currentAddress.getPostalCode() != null) {
                        stringBuilder.append(currentAddress.getPostalCode()).append(", ");
                    }
//                    if (currentAddress.getCountryName() != null) {
//                        stringBuilder.append(currentAddress.getCountryName());
//                    }
                }

                address = stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
        // Reload the shop data when the activity resumes
        loadShopData();
    }

    private void loadShopData() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    String Name = dataSnapshot.child("Name").getValue(String.class);
                    String Email = dataSnapshot.child("email").getValue(String.class);

                    //name.setText(Name);
                    // email.setText(Email);
                    contactnumber.setText(contactNumber);


                    DatabaseReference shopReference = databaseReference.child(contactNumber);
                    shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Shop shopInfo = dataSnapshot.getValue(Shop.class);
                                if (shopInfo != null) {
                                    name.setText(shopInfo.getName());
                                    email.setText(shopInfo.getEmail());
                                    shopname.setText(shopInfo.getShopName());
                                    address.setText(shopInfo.getAddress());
                                    phonenumber.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                                    phonenumber.setText(shopInfo.getPhoneNumber());
                                    //service.setText(shopInfo.getService());
                                    String district = dataSnapshot.child("district").getValue(String.class);
                                    String taluka = dataSnapshot.child("taluka").getValue(String.class);

                                    // Load profile image
                                    String imageUrl = shopInfo.getUrl();
                                    if (croppedImageUri != null) {
                                        // If a cropped image URI is available, load and display it
                                        Picasso.get().load(croppedImageUri).into(circleImageView);
                                    } else if (imageUrl != null && !imageUrl.isEmpty()) {
                                        // If no cropped image is available, load and display the retrieved image
                                        Picasso.get().load(imageUrl).into(circleImageView);
                                    } else {
                                        // If neither cropped nor retrieved image is available, set a default image
                                        circleImageView.setImageResource(R.drawable.ic_outline_person_24);
                                    }

                                    // Clear Picasso's cache for the image when 'croppedImageUri' is updated
                                    if (croppedImageUri != null) {
                                        Picasso.get().invalidate(croppedImageUri);
                                    }

                                    // Load image URLs into the RecyclerView
                                    List<String> imageUrls = shopInfo.getImageUrls();
                                    if (imageUrls != null && !imageUrls.isEmpty()) {
                                        imageRecyclerView.setVisibility(View.VISIBLE);
                                        imageAdapter.setImageUrls(imageUrls);
                                        imageAdapter.notifyDataSetChanged();
                                    }

                                    // Create an ArrayAdapter with a string array containing all the districts
                                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditProfile.this, R.array.districts_array, android.R.layout.simple_spinner_item);
                                    districtspinner.setAdapter(adapter);

                                    // Create an ArrayAdapter with a string array containing all the talukas
                                    talukaAdapter = new ArrayAdapter<>(EditProfile.this, android.R.layout.simple_spinner_item);
                                    talukaspinner.setAdapter(talukaAdapter);

                                    // Set the selected district and taluka
                                    int districtPosition = adapter.getPosition(district);
                                    districtspinner.setSelection(districtPosition);

                                    districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            // Get the selected district from the first spinner
                                            selecteddistrict = adapterView.getItemAtPosition(position).toString();

                                            // Load sub-division data based on the selected district
                                            int subDivisionArrayId = getResources().getIdentifier(
                                                    "Taluka_array_" + selecteddistrict.toLowerCase(),
                                                    "array",
                                                    getPackageName()
                                            );

                                            if (subDivisionArrayId != 0) {
                                                String[] subDivisions = getResources().getStringArray(subDivisionArrayId);
                                                talukaAdapter = new ArrayAdapter<>(EditProfile.this,
                                                        android.R.layout.simple_spinner_item, subDivisions);
                                                talukaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                talukaspinner.setAdapter(talukaAdapter);
                                                // Assuming 'retrievedTaluka' contains the value of the taluka retrieved from Firebase
                                                // Check if the retrieved taluka matches any taluka in the filtered list and set the selection
                                                shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            User user = dataSnapshot.getValue(User.class);
                                                            String userid = dataSnapshot.child("UserID").getValue(String.class);
                                                            if (user != null) {
                                                                String taluka = dataSnapshot.child("taluka").getValue(String.class);
                                                                String district = dataSnapshot.child("district").getValue(String.class);
                                                                System.out.println(district);
                                                                System.out.println(taluka);

                                                                int talukaPosition = talukaAdapter.getPosition(taluka);
                                                                System.out.println(talukaPosition);
                                                                // Check if the taluka exists in the list
                                                                if (talukaPosition != -1) {
                                                                    // Set the selection in the subDivisionSpinner
                                                                    talukaspinner.setAdapter(talukaAdapter);
                                                                    talukaspinner.setSelection(talukaPosition);
                                                                }


                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError error) {

                                                    }
                                                });

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                            // Do nothing
                                        }
                                    });

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error gracefully
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100000)  // Update location every 10 seconds
                .setFastestInterval(5000);  // Fastest update interval is 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String addresses = getAddressFromLocation(latitude, longitude);
                        address.setText(addresses);
                        //  Toast.makeText(Create_Profile.this, "Current location retrieved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfile.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onRemoveClick(int position) {
        DatabaseReference imageRef = databaseReference.child(contactNumber).child("imageUrls");

        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> keys = new ArrayList<>();
                    List<String> values = new ArrayList<>();

                    // Collect the keys and values
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        String value = snapshot.getValue(String.class);

                        if (key != null && value != null) {
                            keys.add(key);
                            values.add(value);
                        }
                    }

                    // Remove the item
                    keys.remove(position);
                    values.remove(position);
                    if (imageAdapter != null) {
                        imageAdapter.removeItem(position);
                    }

                    // Update the keys in Firebase
                    imageRef.setValue(null); // Clear the existing data

                    for (int i = 0; i < keys.size(); i++) {
                        imageRef.child(String.valueOf(i)).setValue(values.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }


    class LocationData {
        private double latitude;
        private double longitude;

        public LocationData() {
            // Default constructor required for Firebase
        }

        public LocationData(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

    }
    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        finish();
    }

}

