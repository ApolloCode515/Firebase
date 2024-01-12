package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPostNew extends AppCompatActivity {
    DatabaseReference usersRef, shopRef, newpostRef, postsRef;
    StorageReference storageRef;
    String userId,postType;
    CardView tempCard,mediaCard,postCard;
    EditText postDesc,postKeys, postCaption,writecationedittext, itemServeArea;
    ImageView back, postImg,removeimg, busipostimg,addServeAreaImageView, img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12;
    int count =1;
    TextView txt1, txt2, txt3, txt4, txt5, txt6;
    ImagePicker imagePicker;

    private static final int PROFILE_IMAGE_REQ_CODE = 101;
    private static final int GALLERY_IMAGE_REQ_CODE = 102;
    private static final int CAMERA_IMAGE_REQ_CODE = 103;
    Uri filePath=null;
    FrameLayout imgFrame;
    String pid, imageUrl, checkstring="Global", servedAreasFirebaseFormat;
    GridLayout gridLayout;
    LinearLayout medialayout, locallayout;
    RelativeLayout templateLayout, imagelayout;
    AlertDialog dialog;
    RadioGroup radioGroup;
    RadioButton rdglobal, rdlocal;
    private List<String> servedAreasList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_new);

        tempCard=findViewById(R.id.tempBtn);
        mediaCard=findViewById(R.id.mediaBtn);
        postCard=findViewById(R.id.postBtn);
        postDesc=findViewById(R.id.postDescr);
        postImg=findViewById(R.id.postImgId);
        removeimg=findViewById(R.id.removImg);
        imgFrame=findViewById(R.id.imgFrame);
        postKeys=findViewById(R.id.bizkeyword);
        back = findViewById(R.id.back);
        imagelayout = findViewById(R.id.busiimagelayout);
        writecationedittext = findViewById(R.id.caption);
        //postKeys=findViewById(R.id.bizkeyword);

        templateLayout = findViewById(R.id.colorPostBackgroundlayout);
        medialayout = findViewById(R.id.medialayout);
        gridLayout = findViewById(R.id.txtgridLayout);
        busipostimg = findViewById(R.id.busipostimg);
        locallayout = findViewById(R.id.locallayout);
        radioGroup = findViewById(R.id.rdgrpx);
        addServeAreaImageView = findViewById(R.id.addServeAreaImageView);
        itemServeArea = findViewById(R.id.itemserveArea);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);
        img9 = findViewById(R.id.img9);
        img10 = findViewById(R.id.img10);
        img11 = findViewById(R.id.img11);
        img12 = findViewById(R.id.img12);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        txt5 = findViewById(R.id.txt5);
        txt6 = findViewById(R.id.txt6);
        pid="1";

        // Set click listeners for each CardView

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


        String imageUri = getIntent().getStringExtra("imageUri");
        System.out.println("aedsx " +imageUri);
        if (imageUri != null) {
            filePath = Uri.parse(imageUri);
            Glide.with(this)
                    .load(imageUri)
                    .into(postImg);
            imgFrame.setVisibility(View.VISIBLE);
        }

        setTextColor(R.color.white);
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.white);
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.black);
            }
        });

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.close_red);
            }
        });

        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.yellow);
            }
        });

        txt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.blue);
            }
        });

        txt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.pink);
            }
        });


        setGradientImage(R.drawable.gradient01);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient01);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient02);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient18);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient05);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage( R.drawable.gradient06);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient07);
            }
        });
        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient11);
            }
        });
        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient12);
            }
        });
        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient13);
            }
        });
        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient16);
            }
        });
        img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient17);
            }
        });

        img12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient19);
            }
        });

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

        mediaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pid="1";
                showFileChooser();
                templateLayout.setVisibility(View.GONE);
                medialayout.setVisibility(View.VISIBLE);
            }
        });

        tempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pid="2";
                templateLayout.setVisibility(View.VISIBLE);
                medialayout.setVisibility(View.GONE);
            }
        });

        removeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePath=null;
                imgFrame.setVisibility(View.GONE);
            }
        });

        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pid=="1"){
                    if(postDesc.getText().toString().isEmpty()&&filePath==null && writecationedittext.getText().toString().trim().isEmpty()){
                        Toast.makeText(AddPostNew.this, "Blank", Toast.LENGTH_SHORT).show();
                    }else {

                        if(filePath!=null && !postDesc.getText().toString().isEmpty()){
                            saveImageToStorage(filePath,"1"); // save both
                            showImageSelectiondialog();
                        }else if(filePath!=null && postDesc.getText().toString().isEmpty()){
                            saveImageToStorage(filePath,"2"); //only image
                            showImageSelectiondialog();
                        }else if(filePath==null && !postDesc.getText().toString().isEmpty()){
                            saveFb();
                            showImageSelectiondialog();
                        }


                    }
                } else {
                    if (!writecationedittext.getText().toString().trim().isEmpty()){
                        shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    captureAndSaveImage();
                                    showImageSelectiondialog();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Handle onCancelled
                            }
                        });

                    } else {
                        Toast.makeText(AddPostNew.this, "Blank Text", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        writecationedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Adjust text size based on the length of the text
                adjustTextSize(editable.length());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        addServeAreaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serveAreaText = itemServeArea.getText().toString().trim();

                if (!serveAreaText.isEmpty() && count<=5) {
                    addServeArea(serveAreaText);
                    itemServeArea.setText(""); // Clear the EditText
                    updateServedAreasUI();
                    count++;
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
                           // global = "Global";
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

    private void captureAndSaveImage() {
        // Get the background image as a bitmap
        Bitmap backgroundBitmap = getBitmapFromView(imagelayout);

        // Create a new bitmap with the same dimensions as the background
        Bitmap mergedBitmap = Bitmap.createBitmap(
                backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        // Create a canvas to draw the merged bitmap
        Canvas canvas = new Canvas(mergedBitmap);

        // Draw the background image onto the canvas
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);

        // Save the merged image and store information in the Firebase Realtime Database
        saveImageToStorage(mergedBitmap);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    private void saveImageToStorage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique filename for the image
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("businessposts/" +userId+ "/" + fileName);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                imageUrl = downloadUri.toString();
                                System.out.println("edsrdbf " +imageUrl);
                                // Move the storeBusiImageInfo call here to ensure imageUrl is set
                                storeBusiImageInfo(imageUrl, writecationedittext.getText().toString().trim());
                            } else {

                                // Handle failure to get download URL
                            }
                        }
                    });
                } else {
                    // Handle failure to upload image
                }
            }
        });
    }

    // Add this method to store information in the Firebase Realtime Database
    private void storeBusiImageInfo(String imageUrl, String captionText) {
        // Assuming you have a reference to your Firebase Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Generate a new key for the business post
        String postKey = databaseRef.child("BusinessPosts").child(userId).push().getKey();

        // Create a map to store the data
        Map<String, Object> postData = new HashMap<>();
        postData.put("postImg", imageUrl);
        postData.put("postType","Image");
        postData.put("postKeys",postKeys.getText().toString().trim());
        postData.put("postCate","-");

        if (checkstring.equals("Global")){
            postData.put("servingArea", "Global");
        }else {
            postData.put("servingArea", servedAreasFirebaseFormat);
        }

        // Set the data under the generated post key
        databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data successfully stored in the database
                            System.out.println("Image URL and Caption stored successfully");
                        } else {
                            // Handle the failure to store data
                            System.out.println("Failed to store data: " + task.getException().getMessage());
                        }
                    }
                });
    }



    public void setGradientImage(int drawable){
        Glide.with(this)
                .load(drawable)
                .into(busipostimg);
    }

    public void setTextColor(int color){
        writecationedittext.setTextColor(ContextCompat.getColor(this, color));
    }

    private void adjustTextSize(int textLength) {
        // Define your desired text size range and corresponding steps
        final float minTextSize = 18; // Minimum text size
        final float maxTextSize = 30; // Maximum text size
        final float textSizeStep = 0.1f; // Text size step

        // Calculate the new text size based on the length of the text
        float newSize = Math.max(minTextSize, maxTextSize - textLength * textSizeStep);

        // Apply the new text size to the EditText
        writecationedittext.setTextSize(newSize);
    }


    private void showFileChooser() {
        new ImagePicker.Builder(this)
                //Crop image(Optional), Check Customization for more option
                // .compress(1024)			//Final image size will be less than 1 MB(Optional)
                // .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();

        Log.d("ss","camera");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d("ss",""+resultCode);

        if (resultCode == RESULT_OK && data != null) {
            // Uri object will not be null for RESULT_OK
            filePath = data.getData();
            //ImageUpload();
            Glide.with(this)
                    .load(filePath)
                    .into(postImg);
            imgFrame.setVisibility(View.VISIBLE);
        }else {
            Log.d("xcxc",""+data);
        }
    }

    private void saveImageToStorage(Uri imageUri,String action) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique filename for the image
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("businessposts/" + userId + "/" + fileName);

        try {
            InputStream stream = getContentResolver().openInputStream(imageUri);
            // Convert the InputStream to a byte array
            byte[] data = getBytes(stream);

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String imageUrl = downloadUri.toString();
                                    System.out.println("Download URL: " + imageUrl);
                                    // Move the storeBusiImageInfo call here to ensure imageUrl is set

                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                                    // Generate a new key for the business post
                                    String postKey = databaseRef.child("BusinessPosts").child(userId).push().getKey();

                                    // Create a map to store the data
                                    Map<String, Object> postData = new HashMap<>();

                                    if(action.equals("2")){
                                        postData.put("postImg", imageUrl);
                                        postData.put("postType","Image");
                                        postData.put("postKeys",postKeys.getText().toString().trim());
                                        postData.put("postCate","-");
                                        if (checkstring.equals("Global")){
                                            postData.put("servingArea", "Global");
                                        }else {
                                            postData.put("servingArea", servedAreasFirebaseFormat);
                                        }
                                    }else {
                                        postData.put("postImg", imageUrl);
                                        postData.put("postDesc",postDesc.getText().toString().trim());
                                        postData.put("postType","Both");
                                        postData.put("postKeys",postKeys.getText().toString().trim());
                                        postData.put("postCate","-");
                                        if (checkstring.equals("Global")){
                                            postData.put("servingArea", "Global");
                                        }else {
                                            postData.put("servingArea", servedAreasFirebaseFormat);
                                        }
                                    }

                                    // Set the data under the generated post key
                                    databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Data successfully stored in the database
                                                      //  Toast.makeText(AddPostNew.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                        System.out.println("Image URL and Caption stored successfully");
                                                    } else {
                                                        // Handle the failure to store data
                                                        Toast.makeText(AddPostNew.this, "Failed", Toast.LENGTH_SHORT).show();
                                                        System.out.println("Failed to store data: " + task.getException().getMessage());
                                                    }
                                                }
                                            });

                                } else {
                                    // Handle failure to get download URL
                                }
                            }
                        });
                    } else {
                        // Handle failure to upload image
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle file not found exception
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void saveFb(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Generate a new key for the business post
        String postKey = databaseRef.child("BusinessPosts").child(userId).push().getKey();

        // Create a map to store the data
        Map<String, Object> postData = new HashMap<>();
        postData.put("postImg","-");
        postData.put("postDesc",postDesc.getText().toString().trim());
        postData.put("postType","Text");
        postData.put("postKeys",postKeys.getText().toString().trim());
        postData.put("postCate","-");
        if (checkstring.equals("Global")){
            postData.put("servingArea", "Global");
        }else {
            postData.put("servingArea", servedAreasFirebaseFormat);
        }
        // Set the data under the generated post key
        databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data successfully stored in the database
                          //  Toast.makeText(AddPostNew.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Image URL and Caption stored successfully");
                        } else {
                            // Handle the failure to store data
                            Toast.makeText(AddPostNew.this, "Failed", Toast.LENGTH_SHORT).show();
                            System.out.println("Failed to store data: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void showImageSelectiondialog() {
        Dialog dialog1 = new Dialog(this);
        // Inflate the custom layout
        dialog1.setContentView(R.layout.progress_dialog);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancelButton = dialog1.findViewById(R.id.closeButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
    }


}