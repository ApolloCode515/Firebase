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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SpacialProductHare extends AppCompatActivity {

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

    public SpacialProductHare() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacial_product_hare);

        frontImage=findViewById(R.id.frontImageView);
        backImage=findViewById(R.id.backImageView);

        // Initialize list to hold frame bitmaps

        Intent intent=getIntent();
        link=intent.getStringExtra("Url");
        DLink=intent.getStringExtra("Dlink");
        prodNameXXX=intent.getStringExtra("Name");
        proDescXXX=intent.getStringExtra("Desc");

        kaka=findViewById(R.id.shareKaka);

        frameLayout=findViewById(R.id.iksmxxxx);

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
                    .into(backImage);
        }catch (Exception dd){

        }

        // Initialize frameUrls list
        frameUrls = new ArrayList<>();

        // Retrieve image download URLs from Firebase Storage

        recyclerView = findViewById(R.id.frameListView);
     //   recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        retrieveImageUrls();

//        width = findViewById(R.id.widthSeekBar);
//        height = findViewById(R.id.heightSeekBar);
//
//        left = findViewById(R.id.leftSeekBar);
//        top = findViewById(R.id.topSeekBar);
//
//        width.setProgress(500);
//        height.setProgress(500);
//        left.setProgress(250);
//        top.setProgress(10);
//        width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                imgHeight=height.getProgress();
//                imgWidth=progress;
//                imgLeft=left.getProgress();
//                imgTop=top.getProgress();
//                setImage();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//
//        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // Adjust desired height based on SeekBar progress
//               // int desiredHeight = progress; // Modify as needed
//                // Load and display the product image with the updated desired dimensions
//                imgHeight=progress;
//                imgWidth=width.getProgress();
//                imgLeft=left.getProgress();
//                imgTop=top.getProgress();
//                setImage();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//
//        left.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // Adjust desired height based on SeekBar progress
//                // int desiredHeight = progress; // Modify as needed
//                // Load and display the product image with the updated desired dimensions
//                imgHeight=height.getProgress();
//                imgWidth=width.getProgress();
//                imgLeft=progress;
//                imgTop=top.getProgress();
//                setImage();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//
//        top.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // Adjust desired height based on SeekBar progress
//                // int desiredHeight = progress; // Modify as needed
//                // Load and display the product image with the updated desired dimensions
//                imgHeight=height.getProgress();
//                imgWidth=width.getProgress();
//                imgLeft=left.getProgress();
//                imgTop=progress;
//                setImage();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });

        kaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///shareImageAndText(raja);
                shareLinearLayoutAsImageAndText(frameLayout);
                //throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

//        backImage.setOnTouchListener(new View.OnTouchListener() {
//            private static final int INVALID_POINTER_ID = -1;
//            private int activePointerId = INVALID_POINTER_ID;
//            private float lastTouchX, lastTouchY;
//            private float offsetX, offsetY;
//            private float scaleFactor = 1f;
//            private ScaleGestureDetector scaleGestureDetector;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (scaleGestureDetector == null) {
//                    scaleGestureDetector = new ScaleGestureDetector(v.getContext(), new ScaleGestureListener());
//                }
//                scaleGestureDetector.onTouchEvent(event);
//
//                final int action = event.getActionMasked();
//
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN: {
//                        final int pointerIndex = event.getActionIndex();
//                        final float x = event.getX(pointerIndex);
//                        final float y = event.getY(pointerIndex);
//
//                        lastTouchX = x;
//                        lastTouchY = y;
//                        activePointerId = event.getPointerId(0);
//                        break;
//                    }
//                    case MotionEvent.ACTION_MOVE: {
//                        if (activePointerId == INVALID_POINTER_ID) {
//                            break;
//                        }
//
//                        final int pointerIndex = event.findPointerIndex(activePointerId);
//                        final float x = event.getX(pointerIndex);
//                        final float y = event.getY(pointerIndex);
//
//                        if (!scaleGestureDetector.isInProgress()) {
//                            float dx = x - lastTouchX;
//                            float dy = y - lastTouchY;
//
//                            offsetX += dx;
//                            offsetY += dy;
//
//                            backImage.setTranslationX(offsetX);
//                            backImage.setTranslationY(offsetY);
//                        }
//
//                        lastTouchX = x;
//                        lastTouchY = y;
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL: {
//                        activePointerId = INVALID_POINTER_ID;
//                        break;
//                    }
//                    case MotionEvent.ACTION_POINTER_DOWN: {
//                        final int pointerIndex = event.getActionIndex();
//                        final float x = event.getX(pointerIndex);
//                        final float y = event.getY(pointerIndex);
//
//                        lastTouchX = x;
//                        lastTouchY = y;
//
//                        // Save the ID of this pointer (for dragging)
//                        activePointerId = event.getPointerId(pointerIndex);
//                        break;
//                    }
//                    case MotionEvent.ACTION_POINTER_UP: {
//                        final int pointerIndex = event.getActionIndex();
//                        final int pointerId = event.getPointerId(pointerIndex);
//
//                        if (pointerId == activePointerId) {
//                            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
//                            lastTouchX = event.getX(newPointerIndex);
//                            lastTouchY = event.getY(newPointerIndex);
//                            activePointerId = event.getPointerId(newPointerIndex);
//                        }
//                        break;
//                    }
//                }
//                return true;
//            }
//
//            class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//                private static final float MIN_SCALE_FACTOR = 0.1f;
//                private static final float MAX_SCALE_FACTOR = 10.0f;
//                private float scaleFactor = 1.0f;
//
//                @Override
//                public boolean onScale(ScaleGestureDetector detector) {
//                    scaleFactor *= detector.getScaleFactor();
//                    scaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(scaleFactor, MAX_SCALE_FACTOR));
//                    backImage.setScaleX(scaleFactor);
//                    backImage.setScaleY(scaleFactor);
//                    return true;
//                }
//
//                @Override
//                public void onScaleEnd(ScaleGestureDetector detector) {
//                    // Reset the scale factor when zoom gesture ends
//                    scaleFactor = 1.0f;
//                }
//            }
//        });


    }

    public void setImage(){
        // Combine the frame bitmap with the resized product bitmap
        if (imgWidth > 0 && imgHeight > 0 && imgTop > 0 && imgLeft > 0) {
            Bitmap combinedBitmap = combineImages(imgHeight,imgWidth,imgTop,imgLeft);
            raja=combinedBitmap;
            // Display the combined bitmap in the ImageView
            ImageView productImageView = findViewById(R.id.productImageView);
            productImageView.setImageBitmap(combinedBitmap);
        }

    }

//    private void shareImageAndText(Bitmap imageBitmap) {
//        File imageFile = saveBitmapToFile(imageBitmap);
//
//        if (imageFile != null) {
//            Uri imageUri = FileProvider.getUriForFile(
//                    this,
//                    getPackageName() + ".provider",
//                    imageFile
//            );
//
//            // Create an intent to share the image and text
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("image/jpeg");
//            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//            intent.putExtra(Intent.EXTRA_TEXT, "Hi! I found useful product on Kamdhanda App.\n\n" +prodNameXXX+"\n\n"+proDescXXX+"\n\nCheck this out!\n"+DLink);
//
//            // Grant temporary read permission to the content URI
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            // Use Intent.createChooser to show a dialog with app options
//            Intent chooser = Intent.createChooser(intent, "Share with");
//            // Verify the intent will resolve to at least one activity
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(chooser);
//            } else {
//                Toast.makeText(SpacialProductHare.this, "No app can handle this request", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(SpacialProductHare.this, "Failed to save image", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private File saveBitmapToFile(Bitmap bitmap) {
//        try {
//            File cacheDir = getCacheDir();
//            File imageFile = File.createTempFile("temp_image", ".jpg", cacheDir);
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            outputStream.close();
//            return imageFile;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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
                intent.putExtra(Intent.EXTRA_TEXT, "Hi! I found useful product on Kamdhanda App.\n\n" + prodNameXXX + "\n\n" + proDescXXX + "\n\nCheck this out!\n" + DLink);

                // Grant temporary read permission to the content URI
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Use Intent.createChooser to show a dialog with app options
                Intent chooser = Intent.createChooser(intent, "Share with");

                // Verify the intent will resolve to at least one activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                } else {
                    Toast.makeText(SpacialProductHare.this, "No app can handle this request", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SpacialProductHare.this, "Failed to save image", Toast.LENGTH_SHORT).show();
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frame, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        StorageReference storageRef = storage.getReference().child("ProductFrames"); // Replace "your_image_folder" with your actual folder name

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