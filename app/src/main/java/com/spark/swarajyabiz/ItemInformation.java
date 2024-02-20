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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spark.swarajyabiz.Extra.CustomSpinnerAdapter;
import com.spark.swarajyabiz.ModelClasses.CouponModel;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class ItemInformation extends AppCompatActivity implements ItemImagesAdapter.ImageClickListener{



    private HorizontalScrollView horizontalScrollView;
    private LinearLayout imageContainer;
    private int imageViewCount = 0;
    private final int MAX_IMAGES = 4;

    ImageView back, deleteitem, addServeAreaImageView, couponImg, couponcancelImg;
    EditText itemname, itemprice, itemdiscription, itemsellingprice, wholesaleprice, minquantity, itemServeArea;;
    TextView catlogtextview, textview, introtextview, errortext, coupontext;
    Button save;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, productRef, itemRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference newItemRef, usersRef;
    String contactNumber,ContactNumber, itemkey, userId, couponstatus, couponfront, couponback, extraAmt;
    //private HashMap<Integer, String> imageUrls = new HashMap<>();
    List<String> imagesUrls = new ArrayList<>();
    // private List<String> imageUrls = new ArrayList<>();
    // Counter for generating image keys
    private int imageCounter = 1;
    boolean isFirstItemAdded = false;
    // Declare a global variable to store the selected image URI
    private Uri selectedImageUri;
    ItemImagesAdapter imageAdapter;
    private List<String> imageUrls;
    private static final String USER_ID_KEY = "userID";
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private int currentImagePosition = -1;
    private List<String> servedAreasList = new ArrayList<>();
    String servedAreasFirebaseFormat, checkstring, itemCate, frontcoupon, backcoupon, extraamt;
    LinearLayout locallayout;
    RadioGroup radioGroup;
    RadioButton globalbtn, localbtn;
    ChipGroup servedAreasLayout;
    int count=1;
    Spinner spinner, subspinner;
    CardView couponcard;
    private static final int REQUEST_CODE_ADD_ITEMS = 1;

    SwitchCompat switchCompat;

    String cpstatus;

    LinearLayout nocpn;

    List<CouponModel> items = new ArrayList<>();

    String cpnAmount,cpnFront,cpnBack,cpnIdx="0",oldCpnAmount,oldCpnFront,oldCpnBack,oldCpnIdx="0";

    Spinner cpnSpinner;

    TextView newCpn;

    boolean swt=false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);

        //horizontalScrollView = findViewById(R.id.horizontalScrollView);
        imageContainer = findViewById(R.id.imageContainer);
        // Align the images at the beginning of the LinearLayout
        //imageContainer.setGravity(Gravity.START);

        back = findViewById(R.id.back);
        deleteitem = findViewById(R.id.deleteitem);
        itemname = findViewById(R.id.itemname);
        itemprice = findViewById(R.id.itemprice);
        itemdiscription = findViewById(R.id.itemdiscription);
        save = findViewById(R.id.save);
        catlogtextview = findViewById(R.id.catlogtextview);
        textview = findViewById(R.id.textsview);
        itemsellingprice = findViewById(R.id.itemsellingprice);
        wholesaleprice = findViewById(R.id.wholesaleitemprice);
        minquantity = findViewById(R.id.wholesalequantity);
        itemServeArea = findViewById(R.id.itemserveArea);
        addServeAreaImageView = findViewById(R.id.addServeAreaImageView);
        locallayout = findViewById(R.id.locallayout);
        radioGroup = findViewById(R.id.rdgrpx);
         servedAreasLayout = findViewById(R.id.servedAreasLayout);
        spinner = findViewById(R.id.postctyspinner);
        errortext = findViewById(R.id.errortext);
        couponcard = findViewById(R.id.couponcard);
        coupontext = findViewById(R.id.coupontext);
        couponImg = findViewById(R.id.couponImg);
        couponcancelImg=findViewById(R.id.couponcancelImg);
        newCpn=findViewById(R.id.createnewcpn1);


        cpnSpinner=findViewById(R.id.cpnSpinner1);
        switchCompat=findViewById(R.id.swtBtn1);
        nocpn=findViewById(R.id.nocoupons1);


//        itemname.setEnabled(false);
//        itemprice.setEnabled(false);
//        itemdiscription.setEnabled(false);
        introtextview = findViewById(R.id.introtextview);
    //    introtextview.setVisibility(View.VISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseApp.initializeApp(this);
        databaseReference = firebaseDatabase.getReference("Shop");
        storageReference = storage.getReference("item_images");

        // imageButton = findViewById(R.id.uploadimage);
        // Initialize RecyclerView
        RecyclerView imageRecyclerView = findViewById(R.id.horizantalrecyclerview);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        // imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));

        // Create an instance of the ImageAdapter
        imageAdapter = new ItemImagesAdapter(this, imageUrls, this);
        imageRecyclerView.setAdapter(imageAdapter);


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


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("contactnumber " + contactNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
            }
        });

        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            ContactNumber = sharedIntent.getStringExtra("contactNumber");

            //Log.d("contactNumber", "" + contactNumber);
//            // Convert the image URI string to a URI object
//            Uri imageUri = Uri.parse(image);
//            Log.d("imageUri", "" + imageUri);
//            catalogshopname.setText(shopName);
//            // Load image using Glide
//            RequestOptions requestOptions = new RequestOptions()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Optional: Set caching strategy
//
//            Glide.with(this)
//                    .load(image).centerCrop()
//                    .apply(requestOptions)
//                    .into(catalogshopimage);

        }
        retrievePostCategory();
        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        String itemPrice = intent.getStringExtra("itemPrice");
        String itemDescription = intent.getStringExtra("itemDescription");
        String itemSellPrice = intent.getStringExtra("itemSell");
        String itemwholesale = intent.getStringExtra("wholesale");
        String itemminquantity = intent.getStringExtra("minquantity");
        String itemservingArea = intent.getStringExtra("servingArea");

        couponfront = intent.getStringExtra("couponfront");
        couponback = intent.getStringExtra("couponback");
        extraAmt = intent.getStringExtra("extraAmt");
        couponstatus = intent.getStringExtra("CurrentCpnId");
        itemCate = intent.getStringExtra("itemCate");



        deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ItemInformation.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.item_delete, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_Delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ItemInformation.this);
                                builder.setTitle("Delete Item");
                                builder.setMessage("Are you sure you want to delete your Item?");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Handle the delete action here
                                        // You can put your Firebase database delete code here

                                        // Example code to delete a node in Firebase Realtime Database
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Shop").child(contactNumber).child("items").child(itemkey).removeValue();
                                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference();
                                        productRef.child("Products").child(contactNumber).child(itemkey).removeValue();

                                        Log.d("TAG", "onClick: " + itemkey);
                                        DatabaseReference promoteitemRef = databaseReference.child(contactNumber).child("promotedShops")
                                                .child(contactNumber).child("items");
                                        DatabaseReference hepromoteitemRef = databaseReference.child(contactNumber).child("hePromoteYou")
                                                .child(contactNumber).child("items");
                                        hepromoteitemRef.child(itemkey).removeValue();
                                        System.out.println("weffg " +promoteitemRef);
                                        promoteitemRef.child(itemkey).removeValue();
                                        // Close the dialog
                                        dialogInterface.dismiss();
                                        // Redirect to the business page
                                        Intent resultIntent = new Intent();
                                        setResult(RESULT_OK, resultIntent); // Use RESULT_OK to indicate success

                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User clicked on Cancel, so just close the dialog
                                        dialogInterface.dismiss();
                                    }
                                });

                                // Create and show the AlertDialog
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newItemName = itemname.getText().toString().trim();
                String itemPrice = itemprice.getText().toString().trim();
                String itemWholeSale = wholesaleprice.getText().toString().trim();
                String itemquantity = minquantity.getText().toString().trim();
                // Parse the price as a double
//                double Price = Double.parseDouble(itemPrice);
                String itemDesc = itemdiscription.getText().toString().trim();
                String itemSell = itemsellingprice.getText().toString().trim();

                // If a new image URL is available in the adapter for the current item, update it
                if (currentImagePosition != -1) {
                    String newImageURL = imageAdapter.getImageURLAtPosition(currentImagePosition);
                    storeImageUrls(newImageURL,currentImagePosition);
                }

                // Assuming orderModel.getProsell() returns a string like "₹ 76.00"
                String numericPart1 = itemPrice
                        .replaceAll("[^0-9.]+", "");  // Remove non-numeric characters except the dot
                numericPart1 = numericPart1.replaceAll("\\.0*$", "");
                if (numericPart1.endsWith(".")) {
                    numericPart1 = numericPart1.substring(0, numericPart1.length() - 1);
                }

                String numericPart2 = itemSell
                        .replaceAll("[^0-9.]+", "");  // Remove non-numeric characters except the dot
                numericPart2 = numericPart2.replaceAll("\\.0*$", "");
                if (numericPart2.endsWith(".")) {
                    numericPart2 = numericPart2.substring(0, numericPart2.length() - 1);
                }
                // Convert itemPrice and itemSell to numeric values
                double price = Double.parseDouble(numericPart1);
                double sellPrice = Double.parseDouble(numericPart2);

                if (sellPrice > price) {
                    // Show an error message for selling price greater than or equal to item price
                    itemsellingprice.setError("Selling price cannot be greater than to item price");
                    return;
                }

                // Show progress dialog while updating data
                ProgressDialog progressDialog = new ProgressDialog(ItemInformation.this);
                progressDialog.setMessage("Updating...");
                progressDialog.setCancelable(true);
                progressDialog.show();

                if (swt){
                    if (cpstatus.equals("Yes")){
                        if(oldCpnIdx.equals(cpnIdx)){ //do not generate currentcpnid use old
                            DatabaseReference couponsRef = FirebaseDatabase.getInstance().getReference().child("CpnData").child(couponstatus);
                            if (cpnAmount!=null && cpnFront!=null && cpnBack!=null){
                                couponsRef.child("amt").setValue(cpnAmount);
                                couponsRef.child("ftImg").setValue(cpnFront);
                                couponsRef.child("bkImg").setValue(cpnBack);
                                couponsRef.child("cpnId").setValue(cpnIdx);
                               // FirebaseDatabase.getInstance().getReference("Products").child(contactNumber).child(itemkey).child("CurrentCpnId").setValue(couponId);
                              //  databaseReference.child(contactNumber).child("items").child(itemkey).child("CurrentCpnId").setValue(couponId);
                            }
                        }else { // generate new cpn id
                            String couponId = generateShortRandomId(contactNumber);
                            DatabaseReference couponsRef = FirebaseDatabase.getInstance().getReference().child("CpnData").child(couponId);
                            if (cpnAmount!=null && cpnFront!=null && cpnBack!=null){
                                couponsRef.child("amt").setValue(cpnAmount);
                                couponsRef.child("ftImg").setValue(cpnFront);
                                couponsRef.child("bkImg").setValue(cpnBack);
                                couponsRef.child("cpnId").setValue(cpnIdx);
                                couponsRef.child("Members").child("1234").setValue("dd");
                                FirebaseDatabase.getInstance().getReference("Products").child(contactNumber).child(itemkey).child("CurrentCpnId").setValue(couponId);
                                databaseReference.child(contactNumber).child("items").child(itemkey).child("CurrentCpnId").setValue(couponId);
                            }
                        }
                    }else {
                        FirebaseDatabase.getInstance().getReference("Products").child(contactNumber).child(itemkey).child("CurrentCpnId").setValue("No");
                        databaseReference.child(contactNumber).child("items").child(itemkey).child("CurrentCpnId").setValue("No");
                    }
                }else {
                    FirebaseDatabase.getInstance().getReference("Products").child(contactNumber).child(itemkey).child("CurrentCpnId").setValue("No");
                    databaseReference.child(contactNumber).child("items").child(itemkey).child("CurrentCpnId").setValue("No");
                }


                DatabaseReference itemsDetailsRef= databaseReference.child(contactNumber).child("items").child(itemkey);
                DatabaseReference productDetailsRef= databaseReference.child(contactNumber).child("items").child(itemkey);
                //  String formattedPrice = formatPrice(Price);
                Map<String, Object> updates = new HashMap<>();
                updates.put("itemname", newItemName); // Update the item name
                // updates.put("price", formattedPrice);
                updates.put("description", itemDesc);
                updates.put("wholesale",itemWholeSale);
                updates.put("minquantity",itemquantity);
                updates.put("price", itemPrice);
                updates.put("sell", itemSell);
                updates.put("itemCate", spinner.getSelectedItem().toString().trim());

                if (("Global").equals(checkstring)){
                    updates.put("servingArea", "Global");
                } else  {
                    updates.put("servingArea",servedAreasFirebaseFormat);
                }

                // Calculate the discount percentage
                double discountPercentage = ((price - sellPrice) / price) * 100;
                int roundedDiscountPercentage = (int) Math.round(discountPercentage);
                String formattedDiscountPercentage = String.valueOf(roundedDiscountPercentage) + "%";
                updates.put("offer", formattedDiscountPercentage);

                // Use itemRef.updateChildren to update only the specified fields
                itemsDetailsRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Dismiss the progress dialog
                            progressDialog.dismiss();

                            // Redirect to the create catalog page or perform any other action
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            // Handle any errors if the update fails
                        }
                    }
                });

                productDetailsRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Dismiss the progress dialog
                            progressDialog.dismiss();

                            // Redirect to the create catalog page or perform any other action
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            // Handle any errors if the update fails
                        }
                    }
                });

            }
        });

        newCpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ItemInformation.this,CouponMaker.class);
                startActivity(intent1);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemInformation.this.finish();
            }
        });


// Check if itemservingArea is "Global" and update checkstring accordingly
        if ("Global".equals(itemservingArea)) {
            checkstring = "Global";
            radioGroup.check(R.id.rdglobal);
        }else {
            // Set the state of rdlocal to checked
            radioGroup.check(R.id.rdlocal);
            // updateServedAreasUI();
        }

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
                            locallayout.setVisibility(View.GONE);
                            servedAreasLayout.setVisibility(View.GONE);
                            break;

                        case R.id.rdlocal:
                            // Do Something
                            checkstring = "Local";
                            locallayout.setVisibility(View.VISIBLE);

                            updateServedAreasUI();
                            break;
                    }
                }
            }
        });

// Now you can use the updated value of checkstring



        itemname.setText(itemName);
        itemprice.setText(itemPrice);
        itemdiscription.setText(itemDescription);
        itemsellingprice.setText(itemSellPrice);
        wholesaleprice.setText(itemwholesale);
        minquantity.setText(itemminquantity);

        itemkey = intent.getStringExtra("itemKey");
        Log.d("itemKey ",""+itemkey);
        Log.d("itemName ",""+itemName);

        addServeAreaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serveAreaText = itemServeArea.getText().toString().trim();
                if (!serveAreaText.isEmpty() && count<=5) {
                    addServeArea(serveAreaText);
                    itemServeArea.setText(""); // Clear the EditText
                    updateServedAreasUI();
                    count++;
                } else {
                    itemServeArea.setError("You have added the maximum number of locations");
                }
            }
        });

        // Split the retrieved string using the delimiter "&&"
        // Split the retrieved string using the delimiter "&&"
        String[] servedAreasArray = itemservingArea.split("&&");

        // Iterate through the array and add chips to the ChipGroup
        for (String servedArea : servedAreasArray) {
            if ("Global".equals(servedArea)) {

            }else {
                addServeArea(servedArea);
            }
        }

        // Update the UI to reflect the retrieved served areas

        getData();
        updateServedAreasUI();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    swt=true;
                    if (cpstatus.equals("Yes")){
                        cpnSpinner.setVisibility(View.VISIBLE);
                        nocpn.setVisibility(View.GONE);
                    }else {
                        cpnSpinner.setVisibility(View.GONE);
                        nocpn.setVisibility(View.VISIBLE);
                    }
                }else {
                    cpnSpinner.setVisibility(View.GONE);
                    nocpn.setVisibility(View.GONE);
                    swt=false;
                }
            }
        });

        cpnSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CouponModel selectedItem = (CouponModel) adapterView.getItemAtPosition(i);
                // Access the data from the selected item
                cpnIdx = selectedItem.getCpnId();
                cpnAmount = selectedItem.getCpnAmt();
                cpnFront = selectedItem.getCpnFront();
                cpnBack = selectedItem.getCpnBack();
                // Do something with the selected data
                // Toast.makeText(AddItems.this, ""+title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getCoupons();

        if (("No").equals(couponstatus)){
            cpstatus="No";
            switchCompat.setChecked(false);
            swt=false;
        }else {
            DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("CpnData").child(couponstatus);
            couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        oldCpnAmount = snapshot.child("amt").getValue(String.class);
                        oldCpnFront= snapshot.child("ftImg").getValue(String.class);
                        oldCpnBack=snapshot.child("bkImg").getValue(String.class);
                        oldCpnIdx=snapshot.child("cpnId").getValue(String.class);

                        cpstatus="Yes";
                        switchCompat.setChecked(true);
                        swt=true;

                        int selectedIndex = -1;
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getCpnId().equals(oldCpnIdx)) {
                                selectedIndex = i;
                                break;
                            }
                        }

                        if (selectedIndex != -1) {
                            cpnSpinner.setSelection(selectedIndex);
                        }
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }
            });
        }


    }

    // Method to add a served area to the list
    private void addServeArea(String serveArea) {
        servedAreasList.add(serveArea);
    }

    // Method to remove a served area from the list
    private void removeServeArea(int position) {
        servedAreasList.remove(position);
        updateServedAreasUI();
    }

    // Method to update the UI with the current list of served areas
    private void updateServedAreasUI() {

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

            servedAreasString.append(servedAreasList.get(i));
            if (i < servedAreasList.size() - 1) {
                servedAreasString.append("&&");
            }
        }
        servedAreasFirebaseFormat = servedAreasString.toString();

    }

    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##,##0.00", symbols);
        return "₹ " + decimalFormat.format(price);
    }
    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        ItemInformation.this.finish();
    }

    public void onImageViewClick(View view) {
        if (imageViewCount < MAX_IMAGES) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, imageViewCount);
        }
    }

    public void onCameraClick(View view){
        if (imageViewCount < MAX_IMAGES) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, imageViewCount);
        }
    }

    @Override
    public void onGalleryClick(int position) {
        currentImagePosition = position;

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    private void startImageCropper(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image"));

        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Crop Image");
        options.setCompressionQuality(90);

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Set the aspect ratio (square in this case)
                .withMaxResultSize(400, 400) // Set the max result size
                .start(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_ITEMS && resultCode == RESULT_OK) {
            // Handle the result from AddItems activity if needed
            // For example, you can retrieve any data returned from AddItems activity
            String front = data.getStringExtra("frontcoupon");
            String back = data.getStringExtra("backcoupon");
            String extra = data.getStringExtra("extraamt");
           // String status = data.getStringExtra("couponstatus");

           // System.out.println("rfvwsv "+status);
            couponfront = front;
            couponback = back;
            extraAmt = extra;
//            if (status==null){
//                couponstatus="Disable";
//            }else {
//                couponstatus = status;
//            }

            if (extraAmt!=null) {
                coupontext.setText("Coupon Attached");
                couponcancelImg.setBackgroundResource(R.drawable.ic_outline_cancel_24);
                couponcancelImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        couponfront=null;
                        couponback=null;
                        extraAmt=null;

                        databaseReference.child(contactNumber).child("items").child(itemkey).child("CurrentCpnId").setValue("No");
                        FirebaseDatabase.getInstance().getReference("Products").child(contactNumber).child(itemkey).child("CurrentCpnId").setValue("No");

                        FirebaseDatabase.getInstance().getReference()
                                .child("CpnData").child(couponstatus).removeValue();

                        couponcancelImg.setVisibility(View.VISIBLE);
                        couponcancelImg.setBackgroundResource(R.drawable.ic_baseline_add_box_24);
                        couponImg.setVisibility(View.VISIBLE);

                        coupontext.setText("Attach coupon facility for discount");
                    }
                });
            }
        }


        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {

            if (data != null) {
                Uri selectedImageUri = data.getData();

                // Start the UCrop activity for image cropping
                startImageCropper(selectedImageUri);
            }
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                // Handle the cropped image result from UCrop
            if (data != null) {
                Uri croppedImageUri = UCrop.getOutput(data);

                if (croppedImageUri != null) {
                    // Update the RecyclerView to display the cropped image
                    imageAdapter.updateImageAtPosition(currentImagePosition, croppedImageUri.toString());

                    // Upload the cropped image to Firebase Storage and store the URL
                    uploadImageToFirebaseStorage(croppedImageUri, currentImagePosition);
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    // Handle any errors that occurred during cropping
                    Throwable error = UCrop.getError(data);
                    Toast.makeText(this, "Error cropping image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri, int position) {
        // Create a unique filename for the image (e.g., using a timestamp)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String imageName = "item_images" + timestamp + ".jpg";

        // Create a StorageReference to the image in Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(imageName);

        // Upload the image to the specified StorageReference
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, now get the download URL
                    storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Handle the obtained download URL
                                String imageUrl = uri.toString();

                                // Update the selected image in the adapter
                                imageAdapter.updateImageAtPosition(position, imageUrl);

                                // Store the image URL in the Realtime Database at the specified position
                               //storeImageUrls(imageUrl, position);
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors while getting the download URL
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors during the image upload
                });
    }

    private void storeImageUrls(String imageUrl, int position) {
        DatabaseReference itemRef = databaseReference.child(contactNumber).child("items").child(itemkey);

        // Create a map to store the image URLs
        Map<String, Object> imageUrlsMap = new HashMap<>();

        // First, get the existing image URLs from the database (if any)
        itemRef.child("imageUrls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> existingImageUrls = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot imageUrlSnapshot : dataSnapshot.getChildren()) {
                        existingImageUrls.add(imageUrlSnapshot.getValue(String.class));
                    }
                }

                // Update the specified position in the list with the new image URL
                existingImageUrls.set(position, imageUrl);

                // Update the "imageUrls" node in the database
                imageUrlsMap.put("imageUrls", existingImageUrls);

                // Update the database with the entire map
                itemRef.updateChildren(imageUrlsMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Dismiss the progress dialog
                          //  progressDialog.dismiss();

                            // Redirect to the create catalog page or perform any other action

                            // Optional, depending on your navigation flow
                        } else {
                            // Handle any errors if the update fails
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }

    public void getData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    Log.d("fgsdgfsdgsdf", "" + contactNumber);
                    DatabaseReference ref = database.getReference("Shop/" + ContactNumber + "/items/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                List<String> imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("fgsdgfsdgsdf", "" + img);
                                    imageUrls.add(img);
                                }
                                imageAdapter.setImagesUrls(imageUrls);
                                imageAdapter.notifyDataSetChanged();
                            } else {
                                // Log.d("sasff","no "+expdate);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Log.w(TAG, "onCancelled", databaseError.toException());
                        }
                    });

                    DatabaseReference imageref = database.getReference("Shop/" + contactNumber + "/items/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    imageref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                List<String> imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("fgsdgfsdgsdf", "" + img);
                                    imageUrls.add(img);
                                }
                                imageAdapter.setImagesUrls(imageUrls);
                                imageAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled (DatabaseError error){

            }
        });
    }

    public void retrievePostCategory() {
        DatabaseReference postcatRef = FirebaseDatabase.getInstance().getReference("Categories");

        postcatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                List<String> categoryKeys = new ArrayList<>();

                // Iterate through the categories and get their keys
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryKey = categorySnapshot.getKey();
                    if (categoryKey != null) {
                        categoryKeys.add(categoryKey);
                    }
                }

                // Get the selected category from your Intent or wherever you have it
               // String selectedCategory = intent.getStringExtra("itemCate");

                // Now you can use the categoryKeys list to set the main category spinner data
                setCategorySpinnerData(categoryKeys, itemCate);
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void setCategorySpinnerData(List<String> categoryKeys, String selectedCategory) {
        // Add "Select category" as the first item
        categoryKeys.add(0, "Select");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryKeys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Find the position of the selected category in the list
        int position = categoryKeys.indexOf(selectedCategory);

        // Set the selected category as the default selection
        if (position >= 0) {
            spinner.setSelection(position);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    String selectedCategory = categoryKeys.get(position);
                    String selectedCat = spinner.getSelectedItem().toString().trim();
                    retrieveKeywords(selectedCategory);
                } else {
                    // Handle "Select category" selection if needed
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle nothing selected
            }
        });
    }

    private void retrieveKeywords(String selectedCategory) {
        DatabaseReference keywordsRef = FirebaseDatabase.getInstance().getReference("Categories")
                .child(selectedCategory).child("Keywords");

        keywordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String keywords = snapshot.getValue(String.class);
                    // Update the EditText with the retrieved keywords;
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
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
            String ss = "CPN-"+hexString.toString().substring(0, 8);
            // Take the first 10 characters as the shortened random ID
            return ss;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your application's needs
            return null;
        }
    }


//
//    private RecyclerView recyclerViewImages;
//    private List<String> imageUrls;
//
//    private RecyclerView recyclerView;
//    private ItemImagesAdapter imageAdapter;
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference, usersRef;
//    String contactNumber, ContactNumber;
//    private List<ItemList> itemList;
//    ImageView back;
//    Button btnmessage;
//    String itemkey;
//    String itemName;
//    String itemPrice;
//    TextView itemNameTextView, itemDescriptionTextView;
//    private boolean isFirstOrder = true;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_item_information);
//
//        itemDescriptionTextView = findViewById(R.id.descriptiontext);
//        itemDescriptionTextView.setVisibility(View.GONE);
//        btnmessage = findViewById(R.id.btnmessage);
//        back = findViewById(R.id.back);
//
//        // Initialize RecyclerView
//        RecyclerView imageRecyclerView = findViewById(R.id.horizantalrecyclerview);
//        imageRecyclerView.setHasFixedSize(true);
//        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        //int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
//        // imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));
//
//        // Create an instance of the ImageAdapter
//        imageAdapter = new ItemImagesAdapter(this, imageUrls);
//        imageRecyclerView.setAdapter(imageAdapter);
//
//        Intent sharedIntent = IntentDataHolder.getSharedIntent();
//        if (sharedIntent != null) {
//            ContactNumber = sharedIntent.getStringExtra("contactNumber");
//
//            //Log.d("contactNumber", "" + contactNumber);
////            // Convert the image URI string to a URI object
////            Uri imageUri = Uri.parse(image);
////            Log.d("imageUri", "" + imageUri);
////            catalogshopname.setText(shopName);
////            // Load image using Glide
////            RequestOptions requestOptions = new RequestOptions()
////                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Optional: Set caching strategy
////
////            Glide.with(this)
////                    .load(image).centerCrop()
////                    .apply(requestOptions)
////                    .into(catalogshopimage);
//
//        }
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        FirebaseApp.initializeApp(this);
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("Shop");
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        String userId = sharedPreference.getString("mobilenumber", null);
//        if (userId != null) {
//            // userId = mAuth.getCurrentUser().getUid();
//            System.out.println("dffvf  " +userId);
//        }
//        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
//                    DatabaseReference shopRef = databaseReference.child(contactNumber).child("items");
//                    System.out.println("items " + shopRef);
//
//                    shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
////                                String itemName = itemSnapshot.child("itemname").getValue(String.class);
////                                String itemPrice = itemSnapshot.child("price").getValue(String.class);
//
//
//                                //String itemPrice = itemPriceTextView.getText().toString();
//
//                                DatabaseReference requestsRef = databaseReference.child(contactNumber).child("requests");
//                                //DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId).child("requests");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle the error gracefully
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error gracefully
//            }
//        });
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//
//        // Retrieve data from Intent
//        Intent intent = getIntent();
//        itemName = intent.getStringExtra("itemName");
//        itemPrice = intent.getStringExtra("itemPrice");
//        String itemDescription = intent.getStringExtra("itemDescription");
//        if (itemDescription!=null && !itemDescription.isEmpty())
//        {
//            itemDescriptionTextView.setVisibility(View.VISIBLE);
//        }
//        else{
//            itemDescriptionTextView.setVisibility(View.GONE);
//        }
//        itemkey = intent.getStringExtra("itemKey");
//        Log.d("itemKey ",""+itemkey);
//        Log.d("itemName ",""+itemName);
//        ArrayList<String> itemImageUrls = intent.getStringArrayListExtra("itemImageUrls");
//
//
//        // Use the retrieved data to populate your layout elements
//        itemNameTextView = findViewById(R.id.Name);
//        TextView itemPriceTextView = findViewById(R.id.price);
//        TextView itemDescriptionTextView = findViewById(R.id.description);
//
//
//        itemNameTextView.setText(itemName);
//        itemPriceTextView.setText(itemPrice);
//        itemDescriptionTextView.setText(itemDescription);
//
//
//        getData();
//
//        // Load the image using a library like Picasso or Glide
//        //Picasso.get().load(itemImage).into(itemImageView);
//    }
//    @Override
//    public void onBackPressed() {
//        // Navigate to the previous page when the back button is pressed
//        super.onBackPressed();
//        finish();
//    }
//
//    private void storeItemNameOnly(DatabaseReference ordersRef, String itemName) {
//        // Fetch the current list of item names
//        ordersRef.child("itemNames").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<String> itemNames = new ArrayList<>();
//
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                        String existingItemName = itemSnapshot.getValue(String.class);
//                        itemNames.add(existingItemName);
//                    }
//                }
//
//                // Add the new item name to the list
//                itemNames.add(itemName);
//
//                // Update the item names in the Firebase node
//                ordersRef.child("itemNames").setValue(itemNames)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Order successfully stored in the database
//                                // You can show a confirmation message if needed
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Handle failure to store the order
//                            }
//                        });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error
//            }
//        });
//    }
//
//
//    public void getData(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
////                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
//                    Log.d("fgsdgfsdgsdf", "" + contactNumber);
//                    DatabaseReference ref = database.getReference("Shop/" + ContactNumber + "/items/" + itemkey + "/imageUrls/");
//                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
//                    ref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                int x = 0;
//                                List<String> imageUrls = new ArrayList<>();
//                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
//                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
//                                    Log.d("fgsdgfsdgsdf", "" + img);
//                                    imageUrls.add(img);
//
////                                    if (x++ == dataSnapshot.getChildrenCount() - 1) {
////                                        // Log.d("ovov",""+uname);
////                                        ItemList shop = new ItemList();
////                                        // Update the image URLs in the Shop object
////                                         shop.setImagesUrls(imageUrls);
////                                        // Notify the adapter about the data change
////                                        imageAdapter.setImagesUrls(imageUrls);
////                                        imageAdapter.notifyDataSetChanged();
////                                    }
//
//                                }
//                                imageAdapter.setImagesUrls(imageUrls);
//                                imageAdapter.notifyDataSetChanged();
//                            } else {
//                                // Log.d("sasff","no "+expdate);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Log.w(TAG, "onCancelled", databaseError.toException());
//                        }
//                    });
//
//                    DatabaseReference imageref = database.getReference("Shop/" + contactNumber + "/items/" + itemkey + "/imageUrls/");
//                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
//                    imageref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                int x = 0;
//                                List<String> imageUrls = new ArrayList<>();
//                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
//                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
//                                    Log.d("fgsdgfsdgsdf", "" + img);
//                                    imageUrls.add(img);
//                                }
//                                imageAdapter.setImagesUrls(imageUrls);
//                                imageAdapter.notifyDataSetChanged();
//                            } else {
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled (DatabaseError error){
//
//            }
//        });
//    }

    public void getCoupons(){
        //lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/AvCoupons/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                items=new ArrayList<>();
                if (snapshotx.exists()) {
                    int maxCount = 0; // Initialize maxCount
                    cpstatus="Yes";
                    int ss=0;
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String cpnId = keySnapshot.getKey();
                        // Assuming cpnId is in the format "couponX", where X is a number

                        String cpnamt = keySnapshot.child("cpnAmt").getValue(String.class);
                        String cpnfront = keySnapshot.child("cpnFront").getValue(String.class);
                        String cpnback = keySnapshot.child("cpnBack").getValue(String.class);

                        CouponModel commModel=new CouponModel();
                        commModel.setCpnId(cpnId);
                        commModel.setCpnAmt(cpnamt);
                        commModel.setCpnFront(cpnfront);
                        commModel.setCpnBack(cpnback);
                        items.add(commModel);

                        // Create a custom adapter using the list of items
                        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(ItemInformation.this, items);

// Apply the adapter to the spinner
                        cpnSpinner.setAdapter(adapter);

                        if(ss++==snapshotx.getChildrenCount()-1){
                            int selectedIndex = -1;
                            for (int i = 0; i < items.size(); i++) {
                                if (items.get(i).getCpnId().equals(cpnIdx)) {
                                    selectedIndex = i;
                                    break;
                                }
                            }

                            if (selectedIndex != -1) {
                                cpnSpinner.setSelection(selectedIndex);
                            }
                        }
                    }
                }else {
                    cpstatus="No";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

    }
}



