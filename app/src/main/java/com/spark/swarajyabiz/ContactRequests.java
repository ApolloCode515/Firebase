package com.spark.swarajyabiz;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ContactRequests extends AppCompatActivity implements RequestAdapter.RequestListener {

    TextView nameTextView,shopname, emailTextView, phoneNumberTextView, requests;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersRef, shopRef;
    ImageView back, profileimage, dialogprofileimage;
    Button cancelButton;
    CardView profiledetails, contactrequests;

    private RecyclerView requestRecyclerView;
    private RequestAdapter requestAdapter;
    private List<Request> requestList;
    // Firebase Storage reference
    private StorageReference storageReference;
    // Constants for image upload
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog progressDialog;
    private static final int CROP_IMAGE_REQUEST = 2;
    private Uri selectedImageUri;
    private AlertDialog profileImageDialog; // Declare the AlertDialog variable at the class level
    TextView textViewRequestCount;
    private static final int REQUEST_CODE_BUSINESS = 1; // Define a request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_requests);

        nameTextView = findViewById(R.id.text_name);
        emailTextView = findViewById(R.id.text_email);
        phoneNumberTextView = findViewById(R.id.text_contact);
        shopname = findViewById(R.id.text_shopname);
        back = findViewById(R.id.back);
        textViewRequestCount = findViewById(R.id.textViewRequestCount);
       // requests = findViewById(R.id.requests);
        profileimage = findViewById(R.id.profileimage);
//        profiledetails = findViewById(R.id.profiledetails);
//        contactrequests= findViewById(R.id.contactrequests);

        // Initialize Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");

        // Initialize RecyclerView for requests
        requestRecyclerView = findViewById(R.id.requestdetails);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();
        //requestAdapter = new RequestAdapter(requestList, this);
        requestRecyclerView.setAdapter(requestAdapter);
        requestList.clear();


        // Query the Users node for the user with the matching contact number
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String contactNumber = user.getContactNumber();
                        String name = dataSnapshot.child("Name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("contactNumber").getValue(String.class);

                        nameTextView.setText(name);
                        emailTextView.setText(email);
                        phoneNumberTextView.setText("+91 - " +phoneNumber);

                        String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class);
                        if (profileImageUrl != null) {
                            // Use Glide or Picasso to load the image into the ImageView
                            Glide.with(ContactRequests.this).load(profileImageUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(profileimage);
                            // Picasso.get().load(profileImageUrl).into(profileImageView); // Use this line if using Picasso
                        } else {
                            // If there's no profile image URL, you can set a default image
                            Glide.with(ContactRequests.this)
                                    .load(R.drawable.ic_outline_person_2)
                                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(profileimage);
                            // Picasso.get().load(R.drawable.default_profile_image).into(profileImageView); // Use this line if using Picasso
                        }

                        Query shopQuery = shopRef.orderByChild("contactNumber").equalTo(contactNumber);
                        shopQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    DataSnapshot shopSnapshot = dataSnapshot.getChildren().iterator().next();
                                    String shopId = shopSnapshot.getKey();

                                    // Retrieve the shop name
                                    String shopName = shopSnapshot.child("contactNumber").getValue(String.class);
                                    shopname.setText(shopName); // Set the shop name to the shopname TextView
                                    final int[] requestCount = {0};
                                    // Query the Requests node under the shop
                                    DatabaseReference requestsRef = shopRef.child(shopId).child("requests");
                                    requestsRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            requestList.clear();
                                            requestCount[0] = 0; // Reset the request count before counting again

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String requestId = snapshot.getKey();
                                                String userId = snapshot.getValue(String.class);

                                                if (userId != null && userId.equals("blank")) {
                                                    // Create a Request object and add it to the list
                                                    Request request = new Request(requestId, userId);
                                                    requestList.add(request);
                                                    requestCount[0]++; // Increment the request count
                                                   //requests.setVisibility(View.VISIBLE);
                                                }
                                            }
                                            // Save the request count to the user's node in the database
                                            //shopRef.child(contactNumber).child("requestcount").setValue(requestCount[0]);

                                            // Create the RequestAdapter instance with the shopName parameter
                                            requestAdapter = new RequestAdapter(requestList, ContactRequests.this, contactNumber); // Assuming Profile implements RequestAdapter.RequestListener
                                            requestRecyclerView.setAdapter(requestAdapter);

                                            // Notify the adapter about the data change
                                            requestAdapter.notifyDataSetChanged();

                                            // Reload the requests node with non-blank user IDs
                                            DatabaseReference requestsRef = shopRef.child(shopId).child("requests");
                                            Query blankRequestsQuery = requestsRef.orderByValue().equalTo("blank");

                                            blankRequestsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String requestId = snapshot.getKey();
                                                            String userId = snapshot.getValue(String.class);

                                                            if (userId != null && userId.equals("blank")) {
                                                                // Update the user ID to a non-blank value
                                                                //DatabaseReference requestRef = requestsRef.child(requestId);
                                                                //requestRef.setValue("non-blank value");
                                                            }
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle database error
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database error
                            }
                        });

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
                finish();
            }
        });

//        profiledetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ProfileDetails.class));
//            }
//        });
//
//        contactrequests.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ContactRequest.class));
//            }
//        });
    }
//    // Method to show the profile image dialog with options (Add, Update, Delete)
//    private void showProfileImageDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_profile_image_options, null);
//        builder.setView(dialogView);
//
//        // Buttons in the dialog
//        cancelButton = dialogView.findViewById(R.id.btn_cancel);
//        Button saveButton = dialogView.findViewById(R.id.btn_save);
//        dialogprofileimage = dialogView.findViewById(R.id.profileimage);
//
//        dialogprofileimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openImageChooser();
//            }
//        });
//        // Set click listeners for the buttons
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                profileImageDialog.dismiss();
//            }
//        });
//
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (selectedImageUri != null) {
//                    uploadImageToFirebase(selectedImageUri);
//                }
//                profileImageDialog.dismiss();
//            }
//        });
//
//        // Show the dialog
//        profileImageDialog = builder.create();
//        profileImageDialog.show();
//    }
//
//    // Helper method to open the image chooser when the profile image is clicked
////    private void openImageChooser() {
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
////    }
//
//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    // Handle the image selection result
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            selectedImageUri = data.getData();
//            dialogprofileimage.setImageURI(selectedImageUri); // Set the selected image to the ImageView
//        }
//    }
//
//    // Upload the selected image to Firebase Storage with reduced quality
//    private void uploadImageToFirebase(Uri imageUri) {
//        if (imageUri != null) {
//            try {
//                // Reduce image quality using bitmap compression
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos); // Adjust the compression quality here (0-100)
//                byte[] data = baos.toByteArray();
//
//                final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
//
//                fileReference.putBytes(data)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri downloadUri) {
//                                        // Get the download URL of the uploaded image and store it in the Realtime Database
//                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                                        if (firebaseUser != null) {
//                                            String userId = firebaseUser.getUid();
//                                            usersRef.child(userId).child("profileImage").setValue(downloadUri.toString())
//                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
//                                                            // Profile image URL successfully saved in the Realtime Database
//                                                            // You can also update the ImageView here to display the new profile image
//                                                            updateProfileImage(downloadUri.toString()); // Update the ImageView with the new image
//                                                            Log.d("Profile", "Profile image URL saved to Realtime Database: " + downloadUri.toString());
//                                                        }
//                                                    })
//                                                    .addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            Log.e("Profile", "Error saving profile image URL to Realtime Database: " + e.getMessage());
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                });
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("Profile", "Error uploading profile image to Firebase Storage: " + e.getMessage());
//                            }
//                        });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    // Helper method to get the file extension from the URI
//    private String getFileExtension(Uri uri) {
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
//    // Method to update the profile image in the ImageView after adding or updating it
//    private void updateProfileImage(String imageUrl) {
//        Glide.with(this).load(imageUrl).into(profileimage);
//        // Picasso.get().load(imageUrl).into(profileImageView); // Use this line if using Picasso
//    }

    @Override
    public void onYesButtonClick(Request request) {
        // Handle the request accepted action

    }

    // Implement the onNoButtonClick method from RequestAdapter.RequestListener
    @Override
    public void onNoButtonClick(Request request) {
        // Handle the request canceled action
    }

    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
    }


}
