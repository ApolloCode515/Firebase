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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spark.swarajyabiz.ui.FragmentProfile;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddPost extends AppCompatActivity {

    ImageView back, imageview;
    Button postbtn;
    private LinearLayout imageContainer;
    DatabaseReference usersRef, shopRef, newpostRef, postsRef;
    StorageReference storageRef;
    String userId, imageUrl;
    EditText writecationedittext;
    private Uri selectedImageUri, croppedImageUri;
    private int imageViewCount = 0;
    private final int MAX_IMAGES = 4;
    List<String> imagesUrls = new ArrayList<>();
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private AlertDialog dialog;
    private boolean isDialogShowing = false;
    private boolean inUCropFlow = false;
    private int imageCount = 0;
    RelativeLayout relativeLayout;
    ImageView busipostimg;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        back = findViewById(R.id.back);
        postbtn = findViewById(R.id.postbtn);
        writecationedittext = findViewById(R.id.writecaption);
        busipostimg = findViewById(R.id.busipostimg);

//        imageContainer = findViewById(R.id.imageContainer);
//        relativeLayout = findViewById(R.id.relativelay);

        // Initialize Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        storageRef = FirebaseStorage.getInstance().getReference();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

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

            imageUrl = getIntent().getStringExtra("IMAGE_URL");
            System.out.println("sxdoui " +imageUrl);
            Glide.with(this).load(imageUrl).into(busipostimg);

        postbtn();
      //  showImageSelectionDialog();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Business.class));
                finish();
            }
        });

    }

    public void postbtn() {
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = writecationedittext.getText().toString().trim();

                if (imageUrl != null) {
                    ProgressDialog progressDialog = new ProgressDialog(AddPost.this);
                    progressDialog.setMessage("Posting...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String shopname = dataSnapshot.child("shopName").getValue(String.class);
                                String shopimage = dataSnapshot.child("url").getValue(String.class);
                                String caption = writecationedittext.getText().toString().trim();
                                DatabaseReference itemRef = shopRef.child(userId).child("BusinessPosts");
                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference()
                                                            .child("BusinessPosts").child(userId);

                                String itemKey = itemRef.push().getKey();
                                newpostRef = itemRef.child(itemKey);
                                postsRef = postRef.child(itemKey);


                                // Save the item information
                                newpostRef.child("caption").setValue(caption);
                                newpostRef.child("postkey").setValue(itemKey);
                                newpostRef.child("shopName").setValue(shopname);
                                newpostRef.child("imageURL").setValue(imageUrl);
                               // newpostRef.child("shopImage").setValue(shopimage);

                                postsRef.child("caption").setValue(caption);
                                postsRef.child("postkey").setValue(itemKey);
                                postsRef.child("shopName").setValue(shopname);
                                postsRef.child("imageURL").setValue(imageUrl);
                              //  postsRef.child("shopImage").setValue(shopimage);

                                // Upload the cropped image to Firebase Storage
                              //  uploadImageToStorage(croppedImageUri, itemKey);

                                progressDialog.dismiss();
                                setResult(RESULT_OK); // Set the result to indicate success
                                AddPost.this.finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
                } else {
                    Toast.makeText(AddPost.this, "Add at least one image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImageToStorage(Uri imageUri, String itemKey) {
        // Create a unique image name
        String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("post_images/" + userId + "/" + imageName);

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putFile(Uri.parse(imageUrl));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully, get the download URL
            imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                // Store the image URL in the Realtime Database
                newpostRef.child("imageURL").setValue(downloadUri.toString());
                postsRef.child("imageURL").setValue(downloadUri.toString());
            });
        }).addOnFailureListener(e -> {
            // Handle image upload failure
            Toast.makeText(AddPost.this, "Image upload failed", Toast.LENGTH_SHORT).show();
        });
    }
    // ... your existing code


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
                //imageContainer.setVisibility(View.GONE);
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
                 //   imageContainer.setVisibility(View.VISIBLE);
//                    relativeLayout.setVisibility(View.VISIBLE);
                    postbtn.setVisibility(View.VISIBLE);
                 //   addImageView(croppedImageUri);

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    String imageName = UUID.randomUUID().toString(); // Generate a unique image name
                    StorageReference imageRef = storageRef.child("item_images" + imageName);

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
                Intent intent = new Intent(AddPost.this, FragmentProfile.class); // Replace "PreviousActivity" with the appropriate activity class
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
         imageContainer.addView(imageView,0);
        imageViewCount++;

        if (imageViewCount == MAX_IMAGES) {
            // Hide the "Add Image" view
            findViewById(R.id.imageView1).setVisibility(View.GONE);
        }
    }

}