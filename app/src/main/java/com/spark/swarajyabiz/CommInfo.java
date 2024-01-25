package com.spark.swarajyabiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spark.swarajyabiz.Adapters.MyPagerAdapter;
import com.spark.swarajyabiz.MyFragments.MembersFragment;
import com.spark.swarajyabiz.MyFragments.PostsFragment;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.spark.swarajyabiz.data.CustomProgressBar;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommInfo extends AppCompatActivity {
    private ViewPager viewPager;
    ImageView commImgx,editcomm;

    TextView name,desc,mbrcnt;

    CardView invite,share,monit;

    String commLinks;

    public String IMAGE_URL;

    Uri filePath=null;
    ImageView commImg;
    String userId;

    boolean hasval;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_info);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout=findViewById(R.id.tabLayout);

        commImgx=findViewById(R.id.ImgComm);
        name=findViewById(R.id.nameComm);
        desc=findViewById(R.id.descComm);
        mbrcnt=findViewById(R.id.commMbr);
        invite=findViewById(R.id.inviteComm);
        share=findViewById(R.id.shareComm);
        monit=findViewById(R.id.monitbtn);
        editcomm=findViewById(R.id.editcomm);

        Intent intent=getIntent();
        ArrayList<String> data=intent.getStringArrayListExtra("Data");
        String commId=data.get(0);
        String commName=data.get(1);
        String commAdmin=data.get(2);
        String commImg=data.get(3);
        String commDesc=data.get(4);
        String mbrCnt=data.get(5);
        commLinks=data.get(6);


        Glide.with(this)
                .load(commImg)
                .into(commImgx);

        name.setText(commName);
        desc.setText(commDesc);
        mbrcnt.setText(mbrCnt + " Members");

        //Toast.makeText(this, ""+commId, Toast.LENGTH_SHORT).show();

        System.out.println("rsgfbdfb " +commId);
        Bundle bundle = new Bundle();
        bundle.putString("CommID", commId);
     //   Toast.makeText(this, ""+commId, Toast.LENGTH_SHORT).show();

        // Create the fragment and set arguments
        PostsFragment fragment1 = new PostsFragment();
        fragment1.setArguments(bundle);
        MembersFragment fragment2 = new MembersFragment();
        fragment2.setArguments(bundle);

        checkExistence(commId);

        MyPagerAdapter adapter = new MyPagerAdapter(this,commId);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTabView = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
            TextView tabTextView = customTabView.findViewById(R.id.tabTextView);
            // Customize tab labels using a switch statement
            switch (position) {
                case 0:
                    tabTextView.setText("Posts");
                    tab.setCustomView(customTabView);
                    break;
                case 1:
                    tabTextView.setText("Members");
                    tab.setCustomView(customTabView);
                    break;
                // Add more cases as needed
            }
        }).attach();

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyTextToClipboard(commLinks);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadImageTask().execute(commImg);
            }
        });

        monit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inflate the layout for the BottomSheetDialog
                View bottomSheetView = LayoutInflater.from(CommInfo.this).inflate(R.layout.monetization, null);

                // Customize the BottomSheetDialog as needed
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CommInfo.this);
                bottomSheetDialog.setContentView(bottomSheetView);

                // Disable scrolling for the BottomSheetDialog
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

                com.moos.library.HorizontalProgressView progressView=bottomSheetView.findViewById(R.id.progressView_horizontal);

                TextView msgTxt=bottomSheetView.findViewById(R.id.messages);

                TextView btnText=bottomSheetView.findViewById(R.id.btnText);

                TextView mmm=bottomSheetView.findViewById(R.id.sss);

                ImageView btnImg=bottomSheetView.findViewById(R.id.btnImg);

                CardView enableBtn=bottomSheetView.findViewById(R.id.enablemonit);

                LinearLayout btnlay=bottomSheetView.findViewById(R.id.laybtn);

                Toast.makeText(CommInfo.this, ""+hasval, Toast.LENGTH_SHORT).show();

                int ss= Integer.parseInt(mbrCnt);
                progressView.setProgress(ss);
                if(ss<=100){
                    if(hasval){
                        progressView.setVisibility(View.GONE);
                        mmm.setVisibility(View.GONE);
                        msgTxt.setText("Monetization is enabled");
                        btnText.setText("Disable Monetization");
                        Glide.with(CommInfo.this)
                                .load(R.drawable.joinedcheck) // Replace "your_image" with the actual image resource name
                                .into(btnImg);
                        // Change layout background color
                        int color = Color.parseColor("#239328"); // Replace with your desired color
                        btnlay.setBackgroundColor(color);

                    }else {
                        progressView.setVisibility(View.GONE);
                        mmm.setVisibility(View.GONE);
                        msgTxt.setText("You are eligible to monetize.");
                        btnText.setText("Enable Monetization");
                        Glide.with(CommInfo.this)
                                .load(R.drawable.monetwhite) // Replace "your_image" with the actual image resource name
                                .into(btnImg);
                        // Change layout background color
                        int color = Color.parseColor("#771591"); // Replace with your desired color
                        btnlay.setBackgroundColor(color);
                    }
                    // enableBtn
                }else {
                    progressView.setVisibility(View.VISIBLE);
                    mmm.setVisibility(View.VISIBLE);
                    msgTxt.setText("To enable monetization and expand the reach of your community globally, you need a minimum of 100 members for your community.");
                    btnText.setText("Enable Monetization");
                    Glide.with(CommInfo.this)
                            .load(R.drawable.monetwhite) // Replace "your_image" with the actual image resource name
                            .into(btnImg);
                    // Change layout background color
                    int color = Color.parseColor("#6D000000"); // Replace with your desired color
                    btnlay.setBackgroundColor(color);
                }

                enableBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(hasval){
                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community");
                            DatabaseReference communityRef = databaseRef.child(commId);
                            communityRef.child("monit").setValue("disable");

                            progressView.setVisibility(View.GONE);
                            mmm.setVisibility(View.GONE);
                            msgTxt.setText("You are eligible to monetize.");
                            btnText.setText("Enable Monetization");
                            Glide.with(CommInfo.this)
                                    .load(R.drawable.monetwhite) // Replace "your_image" with the actual image resource name
                                    .into(btnImg);
                            // Change layout background color
                            int color = Color.parseColor("#771591"); // Replace with your desired color
                            btnlay.setBackgroundColor(color);
                            checkExistence(commId);

                        }else {
                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community");
                            DatabaseReference communityRef = databaseRef.child(commId);
                            communityRef.child("monit").setValue("enable");

                            progressView.setVisibility(View.GONE);
                            mmm.setVisibility(View.GONE);
                            msgTxt.setText("Monetization is enabled");
                            btnText.setText("Disable Monetization");
                            Glide.with(CommInfo.this)
                                    .load(R.drawable.joinedcheck) // Replace "your_image" with the actual image resource name
                                    .into(btnImg);
                            // Change layout background color
                            int color = Color.parseColor("#239328"); // Replace with your desired color
                            btnlay.setBackgroundColor(color);
                            checkExistence(commId);

                        }
                    }
                });

                bottomSheetDialog.show();
            }
        });

        editcomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCommunity(commName,commImg,commDesc,commId);
            }
        });

    }

    private void copyTextToClipboard(String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("text", textToCopy);
            clipboard.setPrimaryClip(clip);
            SnackBarHelper.showSnackbar(CommInfo.this, "Invite link copied to clipboard");
            //Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    return Bitmap.createBitmap(BitmapFactory.decodeStream(input));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                //shareToWhatsApp(result);
                shareImageAndText(result);
            } else {
                Toast.makeText(CommInfo.this, "Failed to download image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void newCommunity(String comname,String comImg,String comdesk,String comId){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView1 = LayoutInflater.from(CommInfo.this).inflate(R.layout.commdialog, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CommInfo.this);
        bottomSheetDialog.setContentView(bottomSheetView1);

        // Disable scrolling for the BottomSheetDialog
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView1.getParent());
        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView create = bottomSheetView1.findViewById(R.id.addComm);
        commImg = bottomSheetView1.findViewById(R.id.commImg);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText commName = bottomSheetView1.findViewById(R.id.commName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText commDesc = bottomSheetView1.findViewById(R.id.commDesc);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView close = bottomSheetView1.findViewById(R.id.closeCom);

        commName.setText(comname);
        commDesc.setText(comdesk);

        Glide.with(this)
                .load(comImg)
                .into(commImg);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commName.getText().toString().trim().isEmpty()){
                    commName.setError("Community Name should not blank");
                }else if (commDesc.getText().toString().trim().isEmpty()){
                    commDesc.setError("Community Desc should not blank");
                }else if(filePath == null){
                    Toast.makeText(CommInfo.this, "Please choose any image.", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(CommInfo.this)
                            .setTitle("Update Community")
                            .setMessage("Are you sure you want to update community details?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    //String randomId = generateShortRandomId(userId);
                                    saveImageToStorage(filePath,comId,commName.getText().toString().trim(),commDesc.getText().toString().trim());
                                    bottomSheetDialog.dismiss();
                                }
                            }).create().show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        commImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        bottomSheetDialog.show();

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d("ss",""+resultCode);

        if (resultCode == RESULT_OK && data != null) {
            // Uri object will not be null for RESULT_OK
            filePath = data.getData();
            //ImageUpload();
            Glide.with(this)
                    .load(filePath)
                    .into(commImg);
        }else {
            Log.d("xcxc",""+data);
        }
    }

    private void saveImageToStorage(Uri imageUri, String commId, String commName, String commDesc) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique filename for the image
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("Community/" + fileName);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Community...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            InputStream stream = CommInfo.this.getContentResolver().openInputStream(imageUri);
            // Convert the InputStream to a byte array
            byte[] data = getBytes(stream);

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnCompleteListener(downloadUrlTask -> {
                        if (downloadUrlTask.isSuccessful()) {
                            Uri downloadUri = downloadUrlTask.getResult();
                            String imageUrl = downloadUri.toString();
                            updateCommunityInfo(commId, commName, commDesc, imageUrl);
                        } else {
                            // Handle failure to get download URL
                            showToast("Failed to get download URL");
                        }
                    });
                } else {
                    // Handle failure to upload image
                    showToast("Failed to upload image");
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToast("File not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    public static String generateShortRandomId(String mobileNumber) {
        // Get the current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        // Format the timestamp (including milliseconds)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String formattedTimestamp = dateFormat.format(new Date(timestamp));

        // Combine mobile number and timestamp
        String timestampId = mobileNumber + formattedTimestamp;

        // Generate a random ID from the timestampId using SHA-256 hashing
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(timestampId.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Take the first 10 characters as the shortened random ID
            return hexString.toString().substring(0, 10);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your application's needs
            return null;
        }
    }

    private void updateCommunityInfo(String commId, String commName, String commDesc, String imageUrl) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String formattedTimestamp = dateFormat.format(new Date());

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community");
        DatabaseReference communityRef = databaseRef.child(commId);

        communityRef.child("commName").setValue(commName.trim());
        communityRef.child("commDesc").setValue(commDesc.trim());
        //communityRef.child("commAdmin").setValue(userId.trim());
        //communityRef.child("commStatus").setValue("Visible");
        communityRef.child("commImg").setValue(imageUrl)
        //communityRef.child("monit").setValue("disable");
                .addOnSuccessListener(unused -> {
                    // showToast("Community Created Successfully");
                    SnackBarHelper.showSnackbar(this, "Community Updated Successfully");
                    // Generate and share the Dynamic Link
                    try {
                        //createCommunityDynamicLink(commId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to update community information");
                    Log.e("TAG", "Error updating community information", e);
                });
    }

    private void shareToWhatsApp(Bitmap imageBitmap) {
        File imageFile = saveBitmapToFile(imageBitmap);

        if (imageFile != null) {
            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    imageFile
            );

            // Create an intent to share the image and text to WhatsApp
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg"); // Specify the MIME type
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_TEXT, "Join our community!\n" + commLinks);
            intent.setPackage("com.whatsapp");

            // Grant temporary read permission to the content URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start the activity
            startActivity(intent);
        } else {
            Toast.makeText(CommInfo.this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImageAndText(Bitmap imageBitmap) {
        File imageFile = saveBitmapToFile(imageBitmap);

        if (imageFile != null) {
            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    imageFile
            );

            // Create an intent to share the image and text
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_TEXT, "Join our community!\n" + commLinks);

            // Grant temporary read permission to the content URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Use Intent.createChooser to show a dialog with app options
            Intent chooser = Intent.createChooser(intent, "Share with");
            // Verify the intent will resolve to at least one activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                Toast.makeText(CommInfo.this, "No app can handle this request", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CommInfo.this, "Failed to save image", Toast.LENGTH_SHORT).show();
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

    public void checkExistence(String commId){
        hasval=false;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community/"+commId+"/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ss=snapshot.child("monit").getValue(String.class);
                    Toast.makeText(CommInfo.this, ""+ss, Toast.LENGTH_SHORT).show();
                    if(ss.equals("enable")){
                        hasval=true;
                    }else {
                        hasval=false;
                    }
                }else {
                    hasval=false;
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }
}