package com.spark.swarajyabiz;


import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;


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

        SharedPreferences sharedPreference = getSharedPreferences(LoginMain.PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }


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

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseApp.initializeApp(this);
        databaseReference = firebaseDatabase.getReference("Shop");
        storageReference = storage.getReference("images");
        storagereference = storage.getReference("image");


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


        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
                                           // User user = dataSnapshot.getValue(User.class);
                                            //String userid = dataSnapshot.child("UserID").getValue(String.class);
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


        btnregister.setOnClickListener(new View.OnClickListener() { // created by IK
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty()){
                    name.setError("Name is required");
                }else if (shopname.getText().toString().isEmpty()){
                    shopname.setError("Shop name is required");
                }else if (address.getText().toString().isEmpty()){
                    address.setError("Shop address is required");
                }else if (contactnumber.getText().toString().isEmpty()){
                    contactnumber.setError("Mobile number is required");
                }else if (phonenumber.getText().toString().isEmpty()){
                    phonenumber.setError("Enter another number");
                }else if(!isTalukaSelected){
                    SnackBarHelper.showSnackbar(Create_Profile.this,"Choose Taluka");
                }else {
                    String names = name.getText().toString();
                    String shopnames = shopname.getText().toString();
                    String addresses = address.getText().toString();
                    String contactNumber = contactnumber.getText().toString();
                    String phoneNumber = phonenumber.getText().toString();

                    if (croppedImageUri == null) {
                        SnackBarHelper.showSnackbar(Create_Profile.this,"Choose Shop Profile Photo");
                    }else {
                        ProgressDialog progressDialog = new ProgressDialog(Create_Profile.this);
                        progressDialog.setMessage("Creating profile...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();

                        DatabaseReference shopReference = databaseReference.child(contactNumber);
                        shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    SnackBarHelper.showSnackbar(Create_Profile.this,"Shop Already Exists.");
                                }else {
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

                                                    // Save the new shop information to Realtime Database
                                                    shopReference.child("name").setValue(names);
                                                    shopReference.child("shopName").setValue(shopnames);
                                                    shopReference.child("address").setValue(addresses);
                                                    shopReference.child("contactNumber").setValue(contactNumber);
                                                    shopReference.child("phoneNumber").setValue(phoneNumber);
                                                    shopReference.child("email").setValue("-");
                                                    shopReference.child("profileverified").setValue(false);
                                                    shopReference.child("premium").setValue(false);
                                                    shopReference.child("district").setValue(selecteddistrict);
                                                    shopReference.child("taluka").setValue(selectedtaluka);
                                                    shopReference.child("category").setValue(selectedCategory);
                                                    shopReference.child("url").setValue(imageUrl);
                                                    shopReference.child("userId").setValue(contactNumber);

                                                    // Shop information added successfully
                                                    shopReference.child("userId").setValue(userId);
                                                    shopReference.child("requestcount").setValue(0);
                                                    shopReference.child("promotionCount").setValue(0);
                                                    shopReference.child("ordercount").setValue(0);

                                                    DatabaseReference countRef = shopReference.child("count");

                                                    countRef.child("ordercount").setValue(0);
                                                    countRef.child("promotionCount").setValue(0);
                                                    countRef.child("notificationcount").setValue(0);

                                                    SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("userType", "business");
                                                    editor.commit();


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
                                                    finish();

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
                                        // Save the new shop information to Realtime Database
                                        shopReference.child("name").setValue(names);
                                        shopReference.child("shopName").setValue(shopnames);
                                        shopReference.child("address").setValue(addresses);
                                        shopReference.child("contactNumber").setValue(contactNumber);
                                        shopReference.child("phoneNumber").setValue(phoneNumber);
                                        shopReference.child("email").setValue("-");
                                        shopReference.child("profileverified").setValue(false);
                                        shopReference.child("premium").setValue(false);
                                        shopReference.child("district").setValue(selecteddistrict);
                                        shopReference.child("taluka").setValue(selectedtaluka);
                                        shopReference.child("category").setValue(selectedCategory);
                                        shopReference.child("url").setValue(imageUrls);
                                        shopReference.child("userId").setValue(contactNumber);

                                        // Shop information added successfully
                                        shopReference.child("userId").setValue(userId);
                                        shopReference.child("requestcount").setValue(0);
                                        shopReference.child("promotionCount").setValue(0);
                                        shopReference.child("ordercount").setValue(0);

                                        DatabaseReference countRef = shopReference.child("count");

                                        countRef.child("ordercount").setValue(0);
                                        countRef.child("promotionCount").setValue(0);
                                        countRef.child("notificationcount").setValue(0);

                                        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("userType", "business");
                                        editor.commit();


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

                                        finish();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
          //  getCurrentLocation();
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


    @Override
    protected void onResume() {
        super.onResume();
      //  startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  stopLocationUpdates();
    }

    @Override
    public void onRemoveClick(int position) {

    }

    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
    }

}




