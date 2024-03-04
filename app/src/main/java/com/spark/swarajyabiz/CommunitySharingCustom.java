package com.spark.swarajyabiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CommunitySharingCustom extends AppCompatActivity {
    private static final int NUM_FRAMES = 3; // Change this to the number of frames you have
    private List<Bitmap> frameBitmaps;
    String link;

    private RecyclerView recyclerView;
    private FrameAdapter adapter;
    private List<String> frameUrls;

    public Bitmap frameBitmap1,productBitmap1,raja;

    SeekBar width,height,left,top;
    int imgWidth=500,imgHeight=500,imgLeft=250,imgTop=10;

    String DLink,prodNameXXX,proDescXXX;

    CardView kaka;

    ImageView frontImage;

    ZoomageView backImage;

    FrameLayout frameLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_sharing_custom);

        frontImage=findViewById(R.id.frontImageView22);
        backImage=findViewById(R.id.backImageView22);

        // Initialize list to hold frame bitmaps

        Intent intent=getIntent();
        link=intent.getStringExtra("Url"); // logo link
        DLink=intent.getStringExtra("Dlink");

        kaka=findViewById(R.id.shareKaka22);

        frameLayout=findViewById(R.id.iksmxxxx22);

//        try {
//            Glide.with(this)
//                    .asBitmap()
//                    .load(link)
//                    .into(new CustomTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap productBitmap, @Nullable Transition<? super Bitmap> transition) {
//                            productBitmap1=productBitmap;
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//                            // Placeholder cleanup
//
//                        }
//                    });
//        }catch (Exception dd){
//
//        }

        try {
            Glide.with(this)
                    .load(link)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(backImage);
        }catch (Exception dd){

        }

        // Initialize frameUrls list
        frameUrls = new ArrayList<>();

        // Retrieve image download URLs from Firebase Storage

        recyclerView = findViewById(R.id.frameListView22);
        //   recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        retrieveImageUrls();


        kaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///shareImageAndText(raja);
                shareLinearLayoutAsImageAndText(frameLayout);
                //throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

    }

    private Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private void shareLinearLayoutAsImageAndText(FrameLayout linearLayout) {
        // Convert LinearLayout to a Bitmap image
        Bitmap imageBitmap = convertViewToBitmap(linearLayout);

        // Check if the bitmap conversion was successful
        if (imageBitmap != null) {
            // Save the Bitmap image to a temporary file
            File imageFile = saveBitmapToFile(imageBitmap);

            // Proceed if the image file was successfully created
            if (imageFile != null) {
                // Get the content URI for the image file
                Uri imageUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".provider",
                        imageFile
                );

                // Create an intent to share the image and text
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
               // intent.putExtra(Intent.EXTRA_TEXT, "Come join our thriving community on Kamdhanda App and be part of the conversation!\n\n Join with this link!\n"+DLink);

                String msg="कामधंदा एप्प च्या माध्यमातून आमच्या कम्युनिटीला जॉईन व्हा. कम्युनिटी जॉईन करण्यासाठी खाली दिलेल्या लिंकवर क्लिक करा.\n"+DLink;

                intent.putExtra(Intent.EXTRA_TEXT, msg);

                // Grant temporary read permission to the content URI
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Use Intent.createChooser to show a dialog with app options
                Intent chooser = Intent.createChooser(intent, "Share with");

                // Verify the intent will resolve to at least one activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                } else {
                    Toast.makeText(CommunitySharingCustom.this, "No app can handle this request", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CommunitySharingCustom.this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            File cacheDir = getCacheDir();
            File imageFile = File.createTempFile("temp_image", ".jpg", cacheDir);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    private Bitmap combineImages(int desiredHeight,int desiredWidth,int top, int left) {

        Bitmap resizedProductBitmap = Bitmap.createScaledBitmap(productBitmap1, desiredWidth, desiredHeight, true);
        // Create a new bitmap with the dimensions of the fram

        Bitmap combinedBitmap = Bitmap.createBitmap(frameBitmap1.getWidth(), frameBitmap1.getHeight(), frameBitmap1.getConfig());

        // Create a canvas with the new bitmap
        android.graphics.Canvas canvas = new android.graphics.Canvas(combinedBitmap);

        // Draw the product image onto the frame image
        canvas.drawBitmap(resizedProductBitmap, left, top, null);
        // Draw the frame onto the canvas
        canvas.drawBitmap(frameBitmap1, 0, 0, null);

        return combinedBitmap;

    }

    private void loadAndDisplayProductImage(final Bitmap frameBitmap) {
        // Load the product image from online URL using Glide
        //frameBitmap1=frameBitmap;
        //  setImage();
        try {
            Glide.with(this)
                    .load(frameBitmap)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(frontImage);
        }catch (Exception dd){

        }
        // setImage();
    }

    private class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {

        Context mContext;
        private List<String> frameUrls;
        String linkss;

        public FrameAdapter(Context mContext, List<String> frameUrls,String linkss) {
            this.mContext = mContext;
            this.frameUrls = frameUrls;
            this.linkss = linkss;

        }

        @NonNull
        @Override
        public FrameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frame, parent, false);
            return new FrameAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FrameAdapter.ViewHolder holder, int position) {
            holder.bind(frameUrls.get(position));
        }

        @Override
        public int getItemCount() {
            return frameUrls.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView frameImageView;
            private Bitmap bb;
            private Context mContext;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                frameImageView = itemView.findViewById(R.id.frameImageView);
                mContext = itemView.getContext(); // Store the context in a member variable
            }

            public void bind(String frameUrl) {
                // Load frame image from online URL using Glide
                if (mContext != null) { // Check if the context is not null
                    Glide.with(mContext)
                            .asBitmap()
                            .load(frameUrl)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap frameBitmap, @Nullable Transition<? super Bitmap> transition) {
                                    frameImageView.setImageBitmap(frameBitmap);
                                    bb = frameBitmap;
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Placeholder cleanup
                                }
                            });
                }

                frameImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mContext != null) { // Check if the context is not null
                            loadAndDisplayProductImage(bb);
                        }
                    }
                });
            }
        }
    }

    private void retrieveImageUrls() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("CommFrames"); // Replace "your_image_folder" with your actual folder name

        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    frameUrls.add(imageUrl);
                    adapter.notifyDataSetChanged(); // Notify adapter when new URL is added
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });
            }
            // Initialize and set adapter after retrieving URLs
            adapter = new FrameAdapter(this,frameUrls,link);
            recyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle any errors
        });
    }
}