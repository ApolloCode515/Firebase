package com.spark.admin;

import static com.spark.admin.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddPost extends AppCompatActivity {

    ImageView back;
    Button postbtn;
    private LinearLayout imageContainer;
    DatabaseReference usersRef, shopRef, newpostRef;
    String userId;
    EditText writecationedittext;
    private Uri selectedImageUri;
    private int imageViewCount = 0;
    private final int MAX_IMAGES = 4;
    List<String> imagesUrls = new ArrayList<>();
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private AlertDialog dialog;
    private boolean isDialogShowing = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        back = findViewById(R.id.back);
        postbtn = findViewById(R.id.postbtn);
        writecationedittext = findViewById(R.id.writecaption);
        imageContainer = findViewById(R.id.imageContainer);

        // Initialize Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        postbtn();
        showImageSelectionDialog();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Business.class));
                finish();
            }
        });

    }

    public void postbtn(){

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = writecationedittext.getText().toString().trim();


                if (selectedImageUri != null) {

                    // Show progress dialog while creating profile
                    ProgressDialog progressDialog = new ProgressDialog(AddPost.this);
                    progressDialog.setMessage("Post...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String shopname = dataSnapshot.child("shopName").getValue(String.class);
                                String shopimage = dataSnapshot.child("url").getValue(String.class);
                                String caption = writecationedittext.getText().toString().trim();
                                DatabaseReference itemRef = shopRef.child(userId).child("Posts");

                                // Get the current timestamp as a unique key
                                String itemKey = itemRef.push().getKey();
                                // Create a new item reference using the timestamp key
                                newpostRef = itemRef.child(itemKey);

                                // Save the item information
                                newpostRef.child("caption").setValue(caption);
                                newpostRef.child("postkey").setValue(itemKey);
                                newpostRef.child("shopName").setValue(shopname);
                                newpostRef.child("shopImage").setValue(shopimage);

                                storeImageUrls(newpostRef);

                                progressDialog.dismiss();

                            }

                            Intent intent = new Intent(AddPost.this, Profile.class);
                            startActivity(intent);
                            AddPost.this.finish(); // Optional, depending on your navigation flow
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(AddPost.this, "Add at least one image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && imageViewCount < MAX_IMAGES) {
            // Uri selectedImageUri = data.getData();
            selectedImageUri = data.getData();
            addImageView(selectedImageUri);
            // Upload the image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("post_images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully, get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    // Store the image URL in the list
                    imagesUrls.add(downloadUri.toString());

                });
            });

//            if (requestCode == REQUEST_IMAGE_GALLERY) {
//                if (resultCode == RESULT_OK && data != null) {
//                    Uri selectedImageUri = data.getData();
//                    addImageView(selectedImageUri);
//                }
//            }

        }
    }

    // Add this method to show the AlertDialog for image selection
    private void showImageSelectionDialog() {
        isDialogShowing = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPost.this);
        builder.setTitle("Select Image");
        builder.setMessage("Choose item image from gallery.");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked on Cancel, so just close the dialog
                Intent intent = new Intent(AddPost.this, Profile.class); // Replace "PreviousActivity" with the appropriate activity class
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

    }


    private void storeImageUrls(DatabaseReference itemRef) {
        // Store image URLs under "imageUrls" node for the current item
        for (int i = 0; i < imagesUrls.size(); i++) {
//            String firstimage = imagesUrls.get(0);
//            itemRef.child("firstimage").child(String.valueOf(0)).setValue(firstimage);
            String imageUrl = imagesUrls.get(i);
            itemRef.child("imageUrls").child(String.valueOf(i)).setValue(imageUrl);

            Intent imageIntent = new Intent(getApplicationContext(), ItemAdapter.class);
            imageIntent.putExtra("image0", imagesUrls.get(0));

            // Store the first image URL directly under the item
            if (i == 0) {
                itemRef.child("firstImageUrl").setValue(imageUrl);
            }
        }
    }

    private void addImageView(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.image_width),
                getResources().getDimensionPixelSize(R.dimen.image_height)
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

}