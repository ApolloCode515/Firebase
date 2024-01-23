package com.spark.swarajyabiz.ui;

import static android.app.Activity.RESULT_OK;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rd.PageIndicatorView;
import com.spark.swarajyabiz.Adapters.CommAdapter;
import com.spark.swarajyabiz.Adapters.CommunityPagerAdapter;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.Adapters.ImageAdapter;
import com.spark.swarajyabiz.Adapters.MyPagerAdapter;
import com.spark.swarajyabiz.AddPostNew;
import com.spark.swarajyabiz.BottomNavigation;
import com.spark.swarajyabiz.EmployeeAdapter;
import com.spark.swarajyabiz.JobPostAdapter;
import com.spark.swarajyabiz.ModelClasses.Banner;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.MyFragments.PostsFragment;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.spark.swarajyabiz.R;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.annotations.NonNull;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class CommunityFragment extends Fragment implements CommAdapter.OnItemClickListener {
    ExtendedFloatingActionButton newCommunity;
    Uri filePath=null;
    ImageView commImg;
    String userId;
    ArrayList<CommModel> commModels=new ArrayList<>();
    CommAdapter commAdapter;
    RecyclerView commView;
    private ViewPager viewPager;
    private ImageAdapter imageAdapter;
    private PageIndicatorView pageIndicatorView;
    private List<Banner> bannerList;
    RadioGroup rdGrp;
    RadioButton rdLocal;

    TabLayout tabLayout;

    ViewPager2 viewPager2;
    public static CommunityFragment newInstance(String commId) {
        //dd=commId;
        return new CommunityFragment();
    }
    public CommunityFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_community, container, false);

        FirebaseApp.initializeApp(requireContext());

        newCommunity=view.findViewById(R.id.fabx25);
        commView=view.findViewById(R.id.comrecy);

        viewPager=view.findViewById(R.id.viewPagerX);
        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        rdGrp=view.findViewById(R.id.rdgrpxddd);
        rdLocal=view.findViewById(R.id.rdmycomm);

        tabLayout=view.findViewById(R.id.tabLayout1);
        viewPager2 = view.findViewById(R.id.viewPager1);

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);

       //  Toast.makeText(getContext(), ""+userId, Toast.LENGTH_SHORT).show();

        newCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCommunity();
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            String communityId = args.getString("communityId");
            // Now you can use the communityId in your fragment
            // Update UI or perform actions based on the communityId
        }

        Data();

//        rdLocal.setChecked(true);
//        ClearAllEmployee();
//        getMyCommunityData();
//        rdGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
//                if (null != rb) {
//                    // checkedId is the RadioButton selected
//                    switch (i) {
//                        case R.id.rdmycomm:
//                            // Do Something
//                            ClearAllEmployee();
//                            getMyCommunityData();
//                            break;
//
//                        case R.id.rdglobalcomm:
//                            // Do Something'
//                            ClearAllEmployee();
//                            getAllCommunityData();
//                            break;
//                    }
//                }
//            }
//        });


        CommunityPagerAdapter adapter = new CommunityPagerAdapter(getActivity());
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            View customTabView = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
            TextView tabTextView = customTabView.findViewById(R.id.tabTextView);
            // Customize tab labels using a switch statement
            switch (position) {
                case 0:
                    tabTextView.setText("My Community");
                    tab.setCustomView(customTabView);
                    break;
                case 1:
                    tabTextView.setText("Global Community");
                    tab.setCustomView(customTabView);
                    break;
                // Add more cases as needed
            }
        }).attach();

        return view;

    }

    private void Data() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("CommBanners");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Banner> bannerList = new ArrayList<>();

                for (DataSnapshot bannerSnapshot : dataSnapshot.getChildren()) {
                    Banner banner = bannerSnapshot.getValue(Banner.class);
                    if (banner != null) {
                        bannerList.add(banner);
                    }
                }

                // Set up the ViewPager with the ImageAdapter
                imageAdapter = new ImageAdapter(getContext(), bannerList);
                viewPager.setAdapter(imageAdapter);

                // Set up the PageIndicatorView with the count
                pageIndicatorView.setCount(bannerList.size());
                pageIndicatorView.setSelection(0); // Optional: Set the initial selected position

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        // Not needed for this example
                    }

                    @Override
                    public void onPageSelected(int position) {
                        // Set the selected position in the PageIndicatorView
                        pageIndicatorView.setSelection(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        // Not needed for this example
                    }
                });

                // Auto swipe every 3 seconds
                final int delay = 3000; // milliseconds
                final int period = 3000; // milliseconds
                final Handler handler = new Handler();
                final Runnable update = new Runnable() {
                    public void run() {
                        if (imageAdapter != null) {
                            int currentPage = viewPager.getCurrentItem();
                            int nextPage = currentPage + 1;

                            if (nextPage >= imageAdapter.getCount()) {
                                nextPage = 0;
                            }

                            viewPager.setCurrentItem(nextPage, true);
                        }
                    }
                };

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        handler.post(update);
                    }
                }, delay, period);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }



    private void ClearAllEmployee(){
        if(commModels != null){
            commModels.clear();

            if(commAdapter!=null){
                commAdapter.notifyDataSetChanged();
            }
        }
        commModels=new ArrayList<>();
    }

    public void getMyCommunityData(){
        ClearAllEmployee();
        //lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community");
        databaseReference.orderByChild("commAdmin").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    int x=0;
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String commId = keySnapshot.getKey();
                        String commName = keySnapshot.child("commName").getValue(String.class);
                        String commDesc = keySnapshot.child("commDesc").getValue(String.class);
                        String commImg = keySnapshot.child("commImg").getValue(String.class);
                        String commStatus = keySnapshot.child("commStatus").getValue(String.class);
                        String commAdmin = keySnapshot.child("servingArea").getValue(String.class);
                        String commLink = keySnapshot.child("dynamicLink").getValue(String.class);

                        int comCnt= (int) keySnapshot.child("commMembers").getChildrenCount();

                        CommModel commModel=new CommModel();
                        commModel.setCommId(commId);
                        commModel.setCommName(commName);
                        commModel.setCommDesc(commDesc);
                        commModel.setCommAdmin(commAdmin);
                        commModel.setCommImg(commImg);
                        commModel.setCommLink(commLink);
                        commModel.setChecked(true);
                        commModel.setMbrCount(String.valueOf(comCnt));

                        commModels.add(commModel);
                       // lottieAnimationView.setVisibility(View.GONE);
                        if(x++==snapshotx.getChildrenCount()-1){
                           // getProductData(homeItemList);
                            Log.d("fsfsfdsdn","Ok 1");
                        }

                    }

                    commView.setLayoutManager(new LinearLayoutManager(getContext()));
                    commAdapter = new CommAdapter(getActivity(),commModels,CommunityFragment.this);
                    commView.setAdapter(commAdapter);

                    commAdapter.notifyDataSetChanged();
                  //  lottieAnimationView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getAllCommunityData(){
        ClearAllEmployee();
        //lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Community");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    int x=0;
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String commId = keySnapshot.getKey();
                        String commName = keySnapshot.child("commName").getValue(String.class);
                        String commDesc = keySnapshot.child("commDesc").getValue(String.class);
                        String commImg = keySnapshot.child("commImg").getValue(String.class);
                        String commStatus = keySnapshot.child("commStatus").getValue(String.class);
                        String commAdmin = keySnapshot.child("commAdmin").getValue(String.class);
                        String commLink = keySnapshot.child("dynamicLink").getValue(String.class);

                        int comCnt= (int) keySnapshot.child("commMembers").getChildrenCount();

                        if(!commAdmin.equals(userId)){
                            CommModel commModel=new CommModel();
                            commModel.setCommId(commId);
                            commModel.setCommName(commName);
                            commModel.setCommDesc(commDesc);
                            commModel.setCommAdmin(commAdmin);
                            commModel.setCommImg(commImg);
                            commModel.setCommLink(commLink);
                            commModel.setChecked(false);
                            commModel.setMbrCount(String.valueOf(comCnt));

                            commModels.add(commModel);
                            // lottieAnimationView.setVisibility(View.GONE);
                        }

                    }

                    commView.setLayoutManager(new LinearLayoutManager(getContext()));
                    commAdapter = new CommAdapter(getActivity(),commModels,CommunityFragment.this);
                    commView.setAdapter(commAdapter);

                    commAdapter.notifyDataSetChanged();

                    //  lottieAnimationView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void newCommunity(){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.commdialog, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetView);

        // Disable scrolling for the BottomSheetDialog
         BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
         behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView create = bottomSheetView.findViewById(R.id.addComm);
        commImg = bottomSheetView.findViewById(R.id.commImg);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText commName = bottomSheetView.findViewById(R.id.commName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText commDesc = bottomSheetView.findViewById(R.id.commDesc);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView close = bottomSheetView.findViewById(R.id.closeCom);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commName.getText().toString().trim().isEmpty()){
                    commName.setError("Community Name should not blank");
                }else if (commDesc.getText().toString().trim().isEmpty()){
                    commDesc.setError("Community Desc should not blank");
                }else if(filePath == null){
                    Toast.makeText(getContext(), "Please choose any image.", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("New Community")
                            .setMessage("Are you sure you want to create a community?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    String randomId = generateShortRandomId(userId);
                                    saveImageToStorage(filePath,randomId,commName.getText().toString().trim(),commDesc.getText().toString().trim());
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

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Creating Community...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            InputStream stream = getActivity().getContentResolver().openInputStream(imageUri);
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

//    private void updateCommunityInfo(String commId, String commName, String commDesc, String imageUrl) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
//        String formattedTimestamp = dateFormat.format(new Date());
//
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community");
//        DatabaseReference communityRef = databaseRef.child(commId);
//
//        communityRef.child("commName").setValue(commName.trim());
//        communityRef.child("commDesc").setValue(commDesc.trim());
//        communityRef.child("commAdmin").setValue(userId.trim());
//        communityRef.child("commStatus").setValue("Visible");
//        communityRef.child("commImg").setValue(imageUrl);
//        communityRef.child("commDate").setValue(formattedTimestamp).addOnSuccessListener(unused ->
//                        showToast("Community Created Successfully"))
//                .addOnFailureListener(e -> showToast("Failed to update community information"));
//    }





    private void updateCommunityInfo(String commId, String commName, String commDesc, String imageUrl) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String formattedTimestamp = dateFormat.format(new Date());

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community");
        DatabaseReference communityRef = databaseRef.child(commId);

        communityRef.child("commName").setValue(commName.trim());
        communityRef.child("commDesc").setValue(commDesc.trim());
        communityRef.child("commAdmin").setValue(userId.trim());
        communityRef.child("commStatus").setValue("Visible");
        communityRef.child("commImg").setValue(imageUrl);
        communityRef.child("monit").setValue("disable");
        communityRef.child("commDate").setValue(formattedTimestamp)
                .addOnSuccessListener(unused -> {
                   // showToast("Community Created Successfully");
                    //SnackBarHelper.showSnackbar(getActivity(), "Community Created Successfully");
                    // Generate and share the Dynamic Link
                    try {
                        createCommunityDynamicLink(commId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to update community information");
                    Log.e("TAG", "Error updating community information", e);
                });
    }

    private void createCommunityDynamicLink(String communityId) {
        // Build the Dynamic Link
       // String encodedCommunityId = URLEncoder.encode(communityId, "UTF-8");

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://kaamdhanda.page.link/community?communityId=" + communityId))
                .setDomainUriPrefix("https://kaamdhanda.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                //.setNavigationInfoParameters()
                // Set the fallback URL to the Play Store URL
                // .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=your_package_name"))
                // .setIosParameters(new DynamicLink.IosParameters.Builder("your_ios_bundle_id").build())'
                .buildDynamicLink();

        // Shorten the Dynamic Link
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnSuccessListener(shortDynamicLink -> {
                    // Handle the short link (e.g., store it or share it with users)
                    Uri shortLink = shortDynamicLink.getShortLink();
                    //showToast("Dynamic Link created: " + shortLink.toString());

                    // Save the Dynamic Link in the Realtime Database
                    saveDynamicLinkToDatabase(communityId, shortLink.toString());

                    // Now you can share the short link with users
                   // shareDynamicLink(shortLink.toString());
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to create Dynamic Link");
                    Log.e("TAG", "Error creating Dynamic Link", e);
                });
    }

    private void saveDynamicLinkToDatabase(String communityId, String dynamicLink) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Community");
        DatabaseReference communityRef = databaseRef.child(communityId);
        communityRef.child("dynamicLink").setValue(dynamicLink)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        SnackBarHelper.showSnackbar(getActivity(), "Community Created Successfully");
                        getMyCommunityData();
                    }
                });
    }

// The rest of your methods (shareDynamicLink, showToast) remain unchanged.
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(int position) {

    }
}