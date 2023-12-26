package com.spark.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class ImageCropper extends AppCompatActivity {

    private ImageView profileImageView;
    private Button selectImageButton;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        profileImageView = findViewById(R.id.profileImageView);
        selectImageButton = findViewById(R.id.selectImageButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the image picker to select an image for cropping
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                startCropActivity(selectedImageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedImageUri = UCrop.getOutput(data);

            if (croppedImageUri != null) {
                // Display the cropped image
                profileImageView.setImageURI(croppedImageUri);

                // You can save the cropped image URI for the user's profile picture
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle any errors that occurred during cropping
            Throwable error = UCrop.getError(data);
        }
    }

    private void startCropActivity(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image"));

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Set the aspect ratio (square in this case)
                .withMaxResultSize(300, 300) // Set the max result size
                .start(this);
    }
}