package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddPostNew extends AppCompatActivity {
    DatabaseReference usersRef, shopRef, newpostRef, postsRef;
    StorageReference storageRef;
    String userId,postType;
    CardView tempCard,mediaCard,postCard;
    EditText postDesc,postKeys;
    ImageView postImg,removeimg;

    ImagePicker imagePicker;

    private static final int PROFILE_IMAGE_REQ_CODE = 101;
    private static final int GALLERY_IMAGE_REQ_CODE = 102;
    private static final int CAMERA_IMAGE_REQ_CODE = 103;
    Uri filePath=null;
    FrameLayout imgFrame;
    String pid;
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
            }
        });

        tempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pid="2";
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
                if(postDesc.getText().toString().isEmpty()&&filePath==null){
                    Toast.makeText(AddPostNew.this, "Blank", Toast.LENGTH_SHORT).show();
                }else {

                    if(pid=="1"){

                    }else {

                    }
                    //Toast.makeText(AddPostNew.this, "Not Blank", Toast.LENGTH_SHORT).show();
                    if(filePath!=null && !postDesc.getText().toString().isEmpty()){
                        saveImageToStorage(filePath,"1"); // save both
                    }else if(filePath!=null && postDesc.getText().toString().isEmpty()){
                        saveImageToStorage(filePath,"2"); //only image
                    }else if(filePath==null && !postDesc.getText().toString().isEmpty()){
                        saveFb();
                    }

                }
            }
        });

    }

    private void showFileChooser() {
        new ImagePicker.Builder(this)
                .crop()
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
                                    }else {
                                        postData.put("postImg", imageUrl);
                                        postData.put("postDesc",postDesc.getText().toString().trim());
                                        postData.put("postType","Both");
                                        postData.put("postKeys",postKeys.getText().toString().trim());
                                        postData.put("postCate","-");
                                    }

                                    // Set the data under the generated post key
                                    databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Data successfully stored in the database
                                                        Toast.makeText(AddPostNew.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
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

        // Set the data under the generated post key
        databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data successfully stored in the database
                            Toast.makeText(AddPostNew.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Image URL and Caption stored successfully");
                        } else {
                            // Handle the failure to store data
                            Toast.makeText(AddPostNew.this, "Failed", Toast.LENGTH_SHORT).show();
                            System.out.println("Failed to store data: " + task.getException().getMessage());
                        }
                    }
                });
    }


}