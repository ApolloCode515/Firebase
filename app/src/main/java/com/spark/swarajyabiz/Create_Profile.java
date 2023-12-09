package com.spark.swarajyabiz;



import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;import android.content.SharedPreferences;


public class Create_Profile extends AppCompatActivity implements ImageAdapter.ImageClickListener {

    EditText name, shopname, email, address, contactnumber, service, phonenumber;
    Spinner districtspinner, talukaspinner, shopcatagoryspinner;
    ImageView back, promote;
    ImageView circleImageView;
    private Uri imageUri, imageUris, croppedImageUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_SINGLE_IMAGE_REQUEST = 1;
    private final int PICK_MULTIPLE_IMAGES_REQUEST = 2;
    Button btnregister, selectimage;
    ImageView imageButton;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    DatabaseReference databaseReference, usersRef;
    StorageReference storageReference, storagereference;
    Shop shopInfo;
    List<String> imageUrls = new ArrayList<>();
    RecyclerView imageRecyclerView;
    LinearLayout addimage;
    ImageAdapter imageAdapter;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Button getCurrentLocationButton;
    private Button saveLocationButton;
    private SharedPreferences sharedPreferences;
    private boolean isProfileCreationProcess = false;
    private static final String USER_ID_KEY = "userID";
    // Firebase

    private LocationCallback locationCallback;

    private ArrayAdapter<String> talukaAdapter;
    String selecteddistrict, selectedtaluka, userId, selectedCategory;
    private boolean isTalukaSelected = false;

    double latitude;
    double longitude;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        name = findViewById(R.id.editname);
        shopname = findViewById(R.id.editshopname);
        email = findViewById(R.id.editemail);
        address = findViewById(R.id.editaddress);
        contactnumber = findViewById(R.id.editwhatsappno);
        phonenumber = findViewById(R.id.editcontactno);
        phonenumber.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
       // service = findViewById(R.id.editservice);
        btnregister = findViewById(R.id.btnregister);
        back = findViewById(R.id.back);
        selectimage = findViewById(R.id.selectImagesButton);
        circleImageView = findViewById(R.id.profileimage);
        districtspinner = findViewById(R.id.adddistrictSpinner);
        talukaspinner = findViewById(R.id.addtalukaSpinner);
        shopcatagoryspinner = findViewById(R.id.catagorySpinner);
        addimage = findViewById(R.id.addimage);
       // promote =findViewById(R.id.promote);


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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple image selection
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_MULTIPLE_IMAGES_REQUEST);
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_SINGLE_IMAGE_REQUEST);
            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_SINGLE_IMAGE_REQUEST);
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.districts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtspinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtspinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> catagoryadapter = ArrayAdapter.createFromResource(this, R.array.shop_catagory, android.R.layout.simple_spinner_item);
        catagoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtspinner.setAdapter(catagoryadapter);

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
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
         userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        shopcatagoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected district from the first spinner
                selectedCategory = adapterView.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    String Name = dataSnapshot.child("Name").getValue(String.class);
                    String Email = dataSnapshot.child("email").getValue(String.class);
                    String district = dataSnapshot.child("district").getValue(String.class);
                    String taluka = dataSnapshot.child("taluka").getValue(String.class);

// In the onCreate method of the Create_Profile activity
                    Intent intent = getIntent();
                    if (intent != null) {
                        String mobileNumber = intent.getStringExtra("mobileNumber");
                        if (mobileNumber != null) {
                            // Assuming you have an EditText with id "editTextMobileNumber" in your layout


                        }
                    }

                    name.setText(Name);
                    email.setText(Email);
                    contactnumber.setText(contactNumber);


                    DatabaseReference shopReference = databaseReference.child(contactNumber);
                    shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Profile already exists, disable the register button
                                btnregister.setEnabled(false);
                                Toast.makeText(Create_Profile.this, "Profile already created", Toast.LENGTH_SHORT).show();
                            } else {
                                // Profile does not exist, enable the register button
                                btnregister.setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error if necessary
                        }
                    });

                    // Create an ArrayAdapter with a string array containing all the districts
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Create_Profile.this, R.array.districts_array, android.R.layout.simple_spinner_item);
                    districtspinner.setAdapter(adapter);

                    // Create an ArrayAdapter with a string array containing all the talukas
                    talukaAdapter = new ArrayAdapter<>(Create_Profile.this, android.R.layout.simple_spinner_item);
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
                                talukaAdapter = new ArrayAdapter<>(Create_Profile.this,
                                        android.R.layout.simple_spinner_item, subDivisions);
                                talukaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                talukaspinner.setAdapter(talukaAdapter);
                                // Assuming 'retrievedTaluka' contains the value of the taluka retrieved from Firebase
                                // Check if the retrieved taluka matches any taluka in the filtered list and set the selection
                                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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




//                    DatabaseReference shopReference = databaseReference.child(contactNumber);
//                    shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                Shop shopInfo = dataSnapshot.getValue(Shop.class);
//                                if (shopInfo != null) {
//                                    name.setText(shopInfo.getName());
//                                    email.setText(shopInfo.getEmail());
//                                    shopname.setText(shopInfo.getShopName());
//                                    address.setText(shopInfo.getAddress());
//                                    phonenumber.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
//                                    phonenumber.setText(shopInfo.getPhoneNumber());
//                                    service.setText(shopInfo.getService());
//
//                                    // Load profile image
//                                    String imageUrl = shopInfo.getUrl();
//                                    if (imageUrl != null && !imageUrl.isEmpty()) {
//                                        Picasso.get().load(imageUrl).into(circleImageView);
//                                    }
//                                    // Load image URLs into the RecyclerView
//                                    List<String> imageUrls = shopInfo.getImageUrls();
//                                    if (imageUrls != null && !imageUrls.isEmpty()) {
//                                        imageAdapter.setImageUrls(imageUrls);
//                                        imageAdapter.notifyDataSetChanged();
//                                    }
//                                }
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle the error gracefully
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
            }
        });

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
                    talukaAdapter = new ArrayAdapter<>(Create_Profile.this,
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

        String contactNumber = getIntent().getStringExtra("contactNumber");



        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress dialog while creating profile
                ProgressDialog progressDialog = new ProgressDialog(Create_Profile.this);
                progressDialog.setMessage("Creating profile...");
                progressDialog.setCancelable(true);
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
                String phoneNumber = phonenumber.getText().toString();
                String emailId = email.getText().toString();
               // String serviceType = service.getText().toString();
              //  final String contactNumber = contactnumber.getText().toString();

                if (TextUtils.isEmpty(shopnames)) {
                    shopname.setError("Shop name is required");
                    progressDialog.dismiss();
                    return;

                }

                // Retrieve the existing shop information
                DatabaseReference shopReference = databaseReference.child(contactNumber);
                System.out.println("dfvffg " +contactNumber);

                shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Shop existingShopInfo = dataSnapshot.getValue(Shop.class);
                        if (existingShopInfo == null) {
                            // Shop information doesn't exist
                            // Add the user's details to Firebase

                            // Create a new Shop object with the user's entered details
                            Shop newShopInfo = new Shop();
                            newShopInfo.setName(names);
                            newShopInfo.setShopName(shopnames);
                            newShopInfo.setAddress(addresses);
                            newShopInfo.setContactNumber(contactNumber);
                            newShopInfo.setPhoneNumber(phoneNumber);
                            newShopInfo.setEmail(emailId);
                            newShopInfo.setProfileverified(false); // Set profileverified to false
                            newShopInfo.setPremium(false);
                           // newShopInfo.setService(serviceType);
                            // Save selected district and taluka
                            newShopInfo.setDistrict(selecteddistrict);
                            newShopInfo.setTaluka(selectedtaluka);
                            newShopInfo.setShopcategory(selectedCategory);

                            if (!isTalukaSelected) {
                                progressDialog.dismiss();
                                // Show an error message if taluka is not selecte
                                Toast.makeText(Create_Profile.this, "Please select taluka", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(RegisterActivity.this, "Please select a taluka.", Toast.LENGTH_SHORT).show();
                                return;
                            }

//                            // Check if image is selected for the profile during the profile creation process
//                            if (imageUri == null) {
//                                // Image not selected, show Toast and dismiss the progress dialog
//                                Toast.makeText(Create_Profile.this, "Please select profile image", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
//                                // Set the flag back to false to indicate the profile creation process is complete
//                                isProfileCreationProcess = false;
//                                return;
//                            }

                            if (croppedImageUri != null) {
                                // Create a reference to the image file in Firebase Storage
                                StorageReference fileReference = storageReference.child(contactNumber + "/" + System.currentTimeMillis() + ".jpg");

                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); // Adjust the compression quality as needed

                                    // Convert the compressed bitmap back to Uri
                                    byte[] byteArray = baos.toByteArray();
                                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

                                    fileReference.putStream(byteArrayInputStream).addOnSuccessListener(taskSnapshot -> {
                                        // Get the download URL of the uploaded image
                                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                            String imageUrl = uri.toString();
                                            newShopInfo.setUrl(imageUrl);
                                            newShopInfo.setImageUrls(imageUrls);

                                            // Save the new shop information to Realtime Database
                                            shopReference.setValue(newShopInfo)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Shop information added successfully
                                                            shopReference.child("userId").setValue(userId);
                                                            shopReference.child("requestcount").setValue(0);
                                                            shopReference.child("promotionCount").setValue(0);
                                                            shopReference.child("ordercount").setValue(0);

                                                            DatabaseReference countRef = shopReference.child("count");

                                                            countRef.child("ordercount").setValue(0);
                                                            countRef.child("promotionCount").setValue(0);
                                                            countRef.child("notificationcount").setValue(0);

                                                            // Clear the input fields and image selection
                                                            name.setText("");
                                                            shopname.setText("");
                                                            address.setText("");
                                                            phonenumber.setText("");
                                                            email.setText("");
                                                           // service.setText("");
                                                            circleImageView.setImageResource(R.drawable.ic_outline_person_24);
                                                            imageUrls.clear();
                                                            imageAdapter.notifyDataSetChanged();

                                                            progressDialog.dismiss();

                                                            // Redirect to the business page
//                                                            Intent businessIntent = new Intent(Create_Profile.this, FragmentHome.class);
//                                                            // Pass any relevant data to the BusinessActivity if needed
//                                                            startActivity(businessIntent);
                                                            finish(); // Optional: Close this activity if you don't need to come back to it.
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                        });
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(Create_Profile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    });

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                }

                            } else {

                                // No image selected, continue with storing shop information only
                                newShopInfo.setImageUrls(imageUrls);
                                // Save the new shop information to Realtime Database
                                shopReference.setValue(newShopInfo)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Shop information added successfully
                                                shopReference.child("userId").setValue(userId);
                                                shopReference.child("requestcount").setValue(0);
                                                shopReference.child("promotionCount").setValue(0);
                                                shopReference.child("ordercount").setValue(0);

                                                DatabaseReference countRef = shopReference.child("count");

                                                countRef.child("ordercount").setValue(0);
                                                countRef.child("promotionCount").setValue(0);
                                                countRef.child("notificationcount").setValue(0);

                                                // Clear the input fields
                                                name.setText("");
                                                shopname.setText("");
                                                address.setText("");
                                                phonenumber.setText("");
                                                email.setText("");
                                                // service.setText("");
                                                circleImageView.setImageResource(R.drawable.ic_outline_person_24);
                                                imageUrls.clear();
                                                imageAdapter.notifyDataSetChanged();
                                                progressDialog.dismiss();

                                                // Redirect to the business page
//                                                Intent businessIntent = new Intent(Create_Profile.this, FragmentHome.class);
//                                                // Pass any relevant data to the BusinessActivity if needed
//                                                startActivity(businessIntent);
                                                finish(); // Optional: Close this activity if you don't need to come back to it.
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                // Image not selected, show Toast and dismiss the progress dialog
//                                Toast.makeText(Create_Profile.this, "Please select profile image", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
                            }
                        } else {
                            // Shop information already exists, handle accordingly or show an error message
                            progressDialog.dismiss();
                            Toast.makeText(Create_Profile.this, "Shop information already exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle onCancelled
                        progressDialog.dismiss();
                    }
                });
            }
        });

                                                // Upload the image file to Firebase Storage
//                                                fileReference.putFile(imageUri)
//                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                            @Override
//                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                                // Get the download URL of the uploaded image
//
//                                                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                    @Override
//                                                                    public void onSuccess(Uri uri) {
//                                                                        String imageUrl = uri.toString();
//
//                                                                        shopInfo = new Shop(names, shopnames, contactNumber, addresses, imageUrl, serviceType);
//                                                                        shopInfo.setImageUrls(imageUrls);
//                                                                        shopInfo.setPhoneNumber(phoneNumber);
//                                                                        shopInfo.setEmail(emailId);
//                                                                        shopInfo.setService(serviceType);
//
//                                                                        DatabaseReference shopReference = databaseReference.child(contactNumber);
//
//
//                                                                        // Save the shop information to Realtime Database
//                                                                        shopReference.setValue(shopInfo)
//                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                                    @Override
//                                                                                    public void onSuccess(Void aVoid) {
//                                                                                        // Shop information saved successfully
//                                                                                        // Save the image URLs under the "imagesURL" child node
//                                                                                        //DatabaseReference imagesUrlReference = shopReference.child("imagesURL");
//
//                                                                                        // Save the shop information to Realtime Database with the user ID
//                                                                                        shopReference.child("userId").setValue(userId);
//                                                                                        // Clear the input fields and image selection
//                                                                                        name.setText("");
//                                                                                        shopname.setText("");
//                                                                                        address.setText("");
//                                                                                        //contactnumber.setText("");
//                                                                                        phonenumber.setText("");
//                                                                                        email.setText("");
//                                                                                        service.setText("");
//                                                                                        circleImageView.setImageResource(R.drawable.ic_outline_person_24);
//                                                                                        imageUrls.clear();
//                                                                                        imageAdapter.notifyDataSetChanged();
//
//                                                                                        progressDialog.dismiss();
//
//                                                                                        // Redirect to the business page
//                                                                                        Intent businessIntent = new Intent(Create_Profile.this, Business.class);
//                                                                                        // Pass any relevant data to the BusinessActivity if needed
//                                                                                        startActivity(businessIntent);
//                                                                                        finish(); // Optional: Close this activity if you don't need to come back to it.
//                                                                                    }
//                                                                                })
//                                                                                .addOnFailureListener(new OnFailureListener() {
//                                                                                    @Override
//                                                                                    public void onFailure(@NonNull Exception e) {
//                                                                                        Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                                        progressDialog.dismiss();
//                                                                                    }
//                                                                                });
//                                                                    }
//                                                                });
//                                                            }
//                                                        });
//                                            }
//
//                                    });
//
//                            return;
//                        }



//                        // Retrieve the modified field values
//                        String names = name.getText().toString();
//                        String shopnames = shopname.getText().toString();
//                        String addresses = address.getText().toString();
//                        String contactNumber = contactnumber.getText().toString();
//                        String phoneNumber = phonenumber.getText().toString();
//                        String emailId = email.getText().toString();
//                        String serviceType = service.getText().toString();
//
//                        // Check if the contact number is modified
//                        boolean isContactModified = !existingShopInfo.getContactNumber().equals(contactNumber);
//
//                        // Update the shop information with modified fields
//                        existingShopInfo.setName(names);
//                        existingShopInfo.setShopName(shopnames);
//                        existingShopInfo.setAddress(addresses);
//                        existingShopInfo.setPhoneNumber(phoneNumber);
//                        existingShopInfo.setEmail(emailId);
//                        existingShopInfo.setService(serviceType);
//
//                        // Update the shop information in Realtime Database
//                        updateShopInfo(shopReference, userId, existingShopInfo, isContactModified);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        // Handle onCancelled
//                    }
//                });
//            }
//        });

//        promote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(Create_Profile.this, view);
//                popupMenu.getMenuInflater().inflate(R.menu.promote_menu, popupMenu.getMenu());
//
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.menu_promote:
//                                // Handle logout action
//                                // Add your logout logic here
//                                FirebaseAuth.getInstance().signOut();
//                                // Start the FirstPage activity and clear the task stack
//                                Intent intent = new Intent(Create_Profile.this, PomoteShop.class);
//                                startActivity(intent);
//                                return true;
//                            default:
//                                return false;
//
////                            case R.id.menu_Profile:
////                                // Start the FirstPage activity and clear the task stack
////                                Intent intents = new Intent(Business.this, Profile.class);
////                                startActivity(intents);
////                                return true;
//
//                        }
//                    }
//                });
//
//                popupMenu.show();
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == PICK_SINGLE_IMAGE_REQUEST) {
//                if (data != null && data.getData() != null) {
//                    Uri imageUri = data.getData();
//                   // uploadImageToFirebase(imageUri);
//                    circleImageView.setImageURI(imageUri);
//                }
//            } else if (requestCode == PICK_MULTIPLE_IMAGES_REQUEST) {
//                if (data != null && data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount();
//                    if (count <= 5) {
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
//                    compressAndUploadImage(imageUris, null, 1);
//                }
//            }
//        }
//    }
//    private void uploadImageToFirebase(Uri imageUri) {
//        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
//
//        fileReference.putFile(imageUri)
//                .addOnSuccessListener(taskSnapshot -> {
//                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                        String imageUrl = uri.toString();
//                        String contactNumber = contactnumber.getText().toString();
//                        saveImageUrlToDatabase(contactNumber, imageUrl); // Pass the contact number and image URL
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


//    private void compressAndUploadImage(Uri imageUris, ProgressDialog progressDialog, int count) {
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
//                                // progressDialog.dismiss(); // Dismiss the progress dialog
//                            }
//                        }
//                    });
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void updateShopInfo(DatabaseReference shopReference, String userId, Shop shopInfo, boolean isContactModified) {
//        DatabaseReference requestsReference = shopReference.child("requests");
//
//        // Retrieve the existing requests node
//        requestsReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Store the existing requests node value
//                Object requestsNodeValue = dataSnapshot.getValue();
//
//                // Save the shop information to Realtime Database
//                shopReference.updateChildren(shopInfo.toMap())
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Save the shop information to Realtime Database with the user ID
//                                shopReference.child("userId").setValue(userId);
//
//                                // Check if image is modified
//                                if (imageUri != null) {
//                                    // Create a reference to the image file in Firebase Storage
//                                    StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
//
//                                    try {
//                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); // Adjust the compression quality as needed
//
//                                        // Convert the compressed bitmap back to Uri
//                                        byte[] byteArray = baos.toByteArray();
//                                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
//                                        fileReference.putStream(byteArrayInputStream).addOnSuccessListener(taskSnapshot -> {
//                                            // Get the download URL of the uploaded image
//                                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//
//                                                        String imageUrl = uri.toString();
//
//                                                        // Update the image URL in shopInfo
//                                                        shopInfo.setUrl(imageUrl);
//
//                                                        // Save the updated image URL to Realtime Database
//                                                        shopReference.child("url").setValue(imageUrl);
//
//                                                        // Save the shop information to Realtime Database
//                                                        updateShopInfoComplete(shopReference, userId, shopInfo, requestsNodeValue);
//                                                    })
//                                                    .addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                            //progressDialog.dismiss();
//                                                        }
//                                                    });
//                                        }).addOnFailureListener(e -> {
//                                            // Handle failure if uploading image fails
//                                        });
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                } else {
//                                    // No image modification, save the shop information to Realtime Database
//                                    updateShopInfoComplete(shopReference, userId, shopInfo, requestsNodeValue);
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });
//    }
//
//
//
//
//    private void updateShopInfoComplete(DatabaseReference shopReference, String userId, Shop shopInfo, Object requestsNodeValue) {
//        // Save the shop information to Realtime Database
//        shopReference.child("userId").setValue(userId)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // Restore the existing requests node value
//                        shopReference.child("requests").setValue(requestsNodeValue);
//
//                        // Check if images are modified
//                        if (!imageUrls.isEmpty()) {
//                            // Update the imageUrls list in shopInfo
//                            shopInfo.setImageUrls(imageUrls);
//                            shopReference.child("imageUrls").setValue(imageUrls)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            // Save the shop information to Realtime Database
//                                            saveShopInfo(shopReference, shopInfo);
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        } else {
//                            // Save the shop information to Realtime Database
//                            saveShopInfo(shopReference, shopInfo);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void saveShopInfo(DatabaseReference shopReference, Shop shopInfo) {
//
//        // Show progress dialog while creating profile
//        ProgressDialog progressDialog = new ProgressDialog(Create_Profile.this);
//        progressDialog.setMessage("Creating profile...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        // Clear the input fields and image selection
//        name.setText("");
//        shopname.setText("");
//        address.setText("");
//        phonenumber.setText("");
//        email.setText("");
//        service.setText("");
//        circleImageView.setImageResource(R.drawable.ic_outline_person_24);
//        imageUrls.clear();
//
//        // Save the shop information to Realtime Database
//        shopReference.updateChildren(shopInfo.toMap())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        progressDialog.dismiss();
//
//                        // Redirect to the business page
//                        Intent businessIntent = new Intent(Create_Profile.this, Business.class);
//                        // Pass any relevant data to the BusinessActivity if needed
//                        startActivity(businessIntent);
//                        finish(); // Optional: Close this activity if you don't need to come back to it.
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Create_Profile.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            circleImageView.setImageURI(imageUri);
//        }
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
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

            if (imageUri != null) {
                startImageCropper(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            croppedImageUri = UCrop.getOutput(data);

            if (croppedImageUri != null) {
                // Display the cropped image
                circleImageView.setImageDrawable(null);
                circleImageView.setImageURI(croppedImageUri);
                addimage.setVisibility(View.GONE);

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
//                    imageUri = data.getData();
//                    circleImageView.setImageURI(imageUri);
//                    addimage.setVisibility(View.GONE);
//                    // Check if the profile creation process is ongoing
//                    if (isProfileCreationProcess) {
//                        uploadImageToFirebase(imageUri);
//                    }
//                }
//            } else if (requestCode == PICK_MULTIPLE_IMAGES_REQUEST) {
//                if (data != null && data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount();
//                    if (count <= 5) {
//                        imageUrls.clear();
//                        imageRecyclerView.setVisibility(View.VISIBLE);
//                        imageAdapter.notifyDataSetChanged();
//                        final ProgressDialog progressDialog = new ProgressDialog(this);
//                        progressDialog.setTitle("Loading Images");
//                        progressDialog.setMessage("Please wait...");
//                        progressDialog.setIndeterminate(true);
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
//
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
    private void uploadImageToFirebase(Uri imageUri) {
        // Create a reference to the image file in Firebase Storage
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful, get the download URL of the uploaded image
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        String contactNumber = contactnumber.getText().toString();
                        // Save the image URL to the Realtime Database
                        saveImageUrlToDatabase(contactNumber, imageUrl);
                    }).addOnFailureListener(e -> {
                        // Handle the error if getting the download URL fails
                        Toast.makeText(Create_Profile.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle the error if image upload fails
                    Toast.makeText(Create_Profile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageUrlToDatabase(String contactNumber, String imageUrl) {
        // Retrieve the existing value for the "Shop" node based on the contactNumber
        databaseReference.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If the "Shop" node with the contactNumber already exists, update the URL
                    databaseReference.child(contactNumber).child("imageUrl").setValue(imageUrl);
                } else {
                    // If the "Shop" node doesn't exist, create a new node with the contactNumber and URL
                    //databaseReference.child(contactNumber).child("imageUrl").setValue(imageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(Create_Profile.this, "Failed to save image URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                            if (imageUrls.size() == 1) {
                               // progressDialog.dismiss(); // Dismiss the progress dialog
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
                            Toast.makeText(Create_Profile.this, "Error getting location", Toast.LENGTH_SHORT).show();
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

//    private String getAddressFromLocation(double latitude, double longitude) {
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        String address = "";
//
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses.size() > 0) {
//                Address currentAddress = addresses.get(0);
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i <= currentAddress.getMaxAddressLineIndex(); i++) {
//                    stringBuilder.append(currentAddress.getAddressLine(i));
//                    if (i < currentAddress.getMaxAddressLineIndex()) {
//                        stringBuilder.append(", ");
//                    }
//                }
//                address = stringBuilder.toString();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return address;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
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
                       // Toast.makeText(Create_Profile.this, "Unable to get location", Toast.LENGTH_SHORT).show();
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
    }

}


/*
    private void addDatatoFirebase(String names, String shopnames, String whatsapp, String addresses) {
        // below 3 lines of code is used to set
        // data in our object class.
        shopInfo.setName(names);
        shopInfo.setShopName(shopnames);
        shopInfo.setWhatsapp(whatsapp);
        shopInfo.setAddress(addresses);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(shopInfo);

                // after adding this data we are showing toast message.
                Toast.makeText(Create_Profile.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(Create_Profile.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }*/



