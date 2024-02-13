package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.ModelClasses.OrderModel;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.spark.swarajyabiz.ui.FragmentHome;
import com.stripe.model.tax.Registration;
import com.tsurkis.timdicator.Timdicator;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.annotations.NonNull;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ItemDetails extends AppCompatActivity implements ItemImagesAdapter.ImageClickListener, ScratchCardView.RevealListener{

    private RecyclerView recyclerViewImages;
    private List<String> imageUrls;
    int size;
    private RecyclerView imageRecyclerView;
    private ItemImagesAdapter imageAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, usersRef, userRef, databaseRef, orderHistoryRef, chatRef;
    String contactNumber, itemContactNumber, Contactnumber, userId, formattedTime,formattedDate, currentuserName; // all same contact but location different
    private List<ItemList> itemList;
    ImageView back, shopimage, percentimg, couponImg;
    Button btnmessage;
    String itemkey, status, ordCartkey, scratchTime, prodKey, retrieveExtraAmt, extraamount, couponextraAmt="0";
    String itemName, numericPart, orderKey;
    String itemPrice, firstImageUrl, shopName, district, address, shopImage, extraAmt;
    TextView itemNameTextView, itemDescriptionTextView, offertextview, pricetextview,sellTextView, shopname, shopaddress, shopdistrict, shoptaluka,
              minqty, enterqty, totalamt, whsaleprice, discount, discounttext, coupontxt, coupontext1, totalAmt, saveAmt;
    private boolean isFirstOrder = true;
    Intent intents;
    private Timdicator timdicator;
    private static final String USER_ID_KEY = "userID";
    private LinearLayout dotsLayout;
    private int currentPosition = 0;
    CardView shopcard, wholesalelay, totalCard, couponCard;
    RecyclerView recyclerViewShops;
    ShopAdapter shopAdapter;
    List<Shop> shopList;
    private List<Shop> filteredList;
    boolean toggle = false;
    RadioButton rdWholesale;
    LinearLayout  callayout;

    private static final long DOUBLE_CLICK_TIME_INTERVAL = 300; // Define the time interval for a double click (in milliseconds)
    private long lastClickTime = 0; // Initialize the last click time
    long clickTime;
    private Dialog dialog;
    int totalamts=0;
    EasyCountDownTextview easyCountDownTextview;
    private boolean shouldDisplayCountdown = false;
    boolean isScratchCardRevealed = false;
    TextView applyCoupon,cpnDiscAmt;
    CardView cpnBefore,cpnAfter;
    String couponStatus="before";
    double couponAmount=0,finalDiscount=0,finalTotalAmt=0,discountWithoutCoupon=0;
    String itemSellPrice="0";
    String wholesale="0";
    String Minqty="0";
    String itemDescription;
    String taluka;
    String itemoffer;
    CardView shareProduct;
    RelativeLayout btnlayout;
    LinearLayout discriptionlayout;
    String prodNameXXX,proDescXXX,DLink;
    ScrollView itemscroll;

    String currentCpnId="cpn1234";

    public static String couponfront, couponback;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        itemscroll = findViewById(R.id.itemSell);
        itemDescriptionTextView = findViewById(R.id.descriptiontext);
        itemDescriptionTextView.setVisibility(View.GONE);
        btnmessage = findViewById(R.id.btnmessage);
        back = findViewById(R.id.back);
        dotsLayout = findViewById(R.id.dotsLayout);
        offertextview = findViewById(R.id.offertextview);
        pricetextview = findViewById(R.id.pricetextview);
        sellTextView = findViewById(R.id.sellprice);
        rdWholesale = findViewById(R.id.rdWholesale);
        wholesalelay = findViewById(R.id.whsalelay);
        minqty = findViewById(R.id.minqty);
        enterqty = findViewById(R.id.enterqty);
        whsaleprice = findViewById(R.id.whsaleprice);
        totalamt = findViewById(R.id.totalamtx);
        discount = findViewById(R.id.discount);
        callayout = findViewById(R.id.callayout);
        percentimg = findViewById(R.id.percentimg);
        discounttext = findViewById(R.id.discounttext);
        easyCountDownTextview = (EasyCountDownTextview)findViewById(R.id.easyCountDownTextview);
        couponImg = findViewById(R.id.couponImg);
        coupontxt = findViewById(R.id.couponTxt);
        coupontext1 = findViewById(R.id.couponTxt2);
        totalAmt = findViewById(R.id.totalAmt);
        saveAmt = findViewById(R.id.saveamt);
        totalCard= findViewById(R.id.totalcard);
        couponCard = findViewById(R.id.couponcard);
        btnlayout = findViewById(R.id.btnLayout);
        discriptionlayout = findViewById(R.id.discriptionlayout);
        coupontext1.setVisibility(View.GONE);
        totalCard.setVisibility(View.GONE);

        applyCoupon = findViewById(R.id.applycoupon);
        cpnBefore = findViewById(R.id.ff);
        cpnAfter = findViewById(R.id.ff2);
        cpnDiscAmt = findViewById(R.id.cpnDiscAmt);

        shareProduct = findViewById(R.id.sharePords);



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


        // Initialize RecyclerView
        imageRecyclerView = findViewById(R.id.horizantalrecyclerview);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        // imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));

        // Create an instance of the ImageAdapter
        imageAdapter = new ItemImagesAdapter(this, imageUrls, this);
        imageRecyclerView.setAdapter(imageAdapter);

        imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateDots();
            }
        });

        itemContactNumber = getIntent().getStringExtra("contactNumber");
        System.out.println("sdfvd " +itemContactNumber);
        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            Contactnumber = sharedIntent.getStringExtra("contactNumber");
            System.out.println("dfh " +Contactnumber);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Shop");
        databaseRef = firebaseDatabase.getReference().child("Shop");
        userRef = firebaseDatabase.getReference("Users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
         userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ItemDetails.this,BottomNavigation.class);
                startActivity(intent);
                ItemDetails.this.finish();
            }
        });


        // Retrieve data from Intent
        Intent intent = getIntent();
        itemName = intent.getStringExtra("itemName");
        itemPrice = intent.getStringExtra("itemPrice");
        itemDescription = intent.getStringExtra("itemDescription");
        firstImageUrl = intent.getStringExtra("firstImageUrl");
        shopName = intent.getStringExtra("shopName");
        district = intent.getStringExtra("district");
        shopImage = intent.getStringExtra("shopimage");
        taluka = intent.getStringExtra("taluka");
        address = intent.getStringExtra("address");
        itemoffer = intent.getStringExtra("itemOffer");
        itemSellPrice = intent.getStringExtra("itemSellPrice");
        //wholesale = intent.getStringExtra("itemWholesale");
        String mqty = intent.getStringExtra("itemMinqty");

        String whsale = intent.getStringExtra("itemWholesale");

       // currentCpnId="cpn1234";

        if(mqty.equals(null) || mqty.equals("")){
            Minqty="0";
        }else {
            Minqty=mqty;
        }

        if(whsale.equals(null) || whsale.equals("")){
            wholesale="0";
        }else {
            wholesale=whsale;
        }


        System.out.println("sedvs s " +wholesale);
        whsaleprice.setText(wholesale);
        minqty.setText(Minqty);
        sellTextView.setText(itemSellPrice);




        // need inspection of this method {
        DatabaseReference ordersRef = databaseRef.child(itemContactNumber).child("orders").child(userId);
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Retrieve the stored time from Firebase
                        String orderkays = dataSnapshot.getKey();
                        DatabaseReference timeRef = ordersRef.child(orderkays);
                        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    status = snapshot.child("status").getValue(String.class);
                                    String productkey = snapshot.child("productkey").getValue(String.class);
                                    String orderkey = snapshot.child("orderkey").getValue(String.class);
                                   // retrieveExtraAmt = snapshot.child("extraAmt").getValue(String.class);

                                    if (productkey!=null && productkey.equals(itemkey) && !orderkey.equals("chats") && !orderkey.equals("buttonchats")) {
                                        if (status != null && (status.equals("cart"))) {
                                            ordCartkey = orderkey;
                                            prodKey = productkey;
                                           // extraamount = retrieveExtraAmt;
                                           // cpnDiscAmt.setText("₹ "+retrieveExtraAmt+ " discount on checkout");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled
                            }
                        });

                        // If the countdown should be displayed, break out of the loop

                    }
                } else {
                    // Handle the case when contactNumber is not present
                    // This could include hiding the countdown or setting a default behavior
                    // For example, easyCountDownTextview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
        // need inspection of this method }



        enterqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(enterqty.getText().toString().trim().isEmpty()){
                    callayout.setVisibility(View.GONE);
                    totalCard.setVisibility(View.GONE);
                }else {
                    finalDiscount=0;
                    finalTotalAmt=0;
                    discountWithoutCoupon=0;;
                    double wholesaleAmt=Double.parseDouble(wholesale);
                    double retailAmt=Double.parseDouble(itemSellPrice);
                    double mrpAmt=Double.parseDouble(itemPrice);
                    int wpqty=Integer.parseInt(Minqty);
                    int qty=Integer.parseInt(enterqty.getText().toString().trim());
                    double mrpTotal=mrpAmt*qty;
                    if(qty>=wpqty && !(wpqty == 0)){ // wholesale price applied
                        double total=wholesaleAmt*qty;
                        totalamt.setText(String.valueOf(wholesaleAmt)+ " x "+String.valueOf(qty)+" = "+String.valueOf(total));
                        discount.setText(""+(mrpTotal-total));
                        total=total+couponAmount;
                        saveAmt.setText("₹ " + (mrpTotal-total));
                        totalAmt.setText("Total : ₹ " + total);
                        finalTotalAmt=total;
                        callayout.setVisibility(View.VISIBLE);
                        totalCard.setVisibility(View.VISIBLE);
                    }else { //Retail price applied
                        double total=retailAmt*qty;
                        totalamt.setText(String.valueOf(retailAmt)+ " x "+String.valueOf(qty)+" = "+String.valueOf(total));
                       // totalamt.setText(String.valueOf(total));
                        discount.setText(""+(mrpTotal-total));
                        total=total+couponAmount;
                        saveAmt.setText("₹ " + (mrpTotal-total));
                        totalAmt.setText("Total : ₹ " + total);
                        finalTotalAmt=total;
                        callayout.setVisibility(View.VISIBLE);
                        totalCard.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        offertextview.setText("( "+itemoffer+" off )");
        pricetextview.setText("₹ "+itemPrice+".00");
        pricetextview.setPaintFlags(pricetextview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        boolean flag = intent.getBooleanExtra("flag", false);
        System.out.println("asjfbjfb " +flag);

        // Initialize RecyclerView
        recyclerViewShops = findViewById(R.id.viewdetails);
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(this));

        shopList = new ArrayList<>();
        filteredList = new ArrayList<>();
        shopAdapter = new ShopAdapter(filteredList, this, this:: onClick);
        recyclerViewShops.setAdapter(shopAdapter);
        readDataFromFirebase();
        RelativeLayout shoplayout = findViewById(R.id.shoplayout);
        // Show or hide the RecyclerView based on whether you are on the Home fragment
        if (flag) {
            shoplayout.setVisibility(View.VISIBLE);
            readDataFromFirebase();  // Load data when on the Home fragment
        } else {
            shoplayout.setVisibility(View.GONE);
        }


        if (itemDescription!=null && !itemDescription.isEmpty())
        {
            itemDescriptionTextView.setVisibility(View.VISIBLE);
            String Description =  intent.getStringExtra("itemDescription");
            itemDescriptionTextView.setText(Html.fromHtml(Description));
            //itemDescriptionTextView.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            discriptionlayout.setVisibility(View.GONE);
            itemDescriptionTextView.setVisibility(View.GONE);
        }

        itemkey = intent.getStringExtra("itemKey");
        Log.d("itemKey ",""+itemkey);
        Log.d("itemName ",""+itemName);
        ArrayList<String> itemImageUrls = intent.getStringArrayListExtra("itemImageUrls");


        // Use the retrieved data to populate your layout elements
        itemNameTextView = findViewById(R.id.Name);

       // TextView itemDescriptionTextView = findViewById(R.id.description);

        itemNameTextView.setText(itemName);

       // itemDescriptionTextView.setText(itemDescription);

        List<String> itemImages = intent.getStringArrayListExtra("itemImages");
        imageAdapter.setImagesUrls(itemImages);
        imageAdapter.notifyDataSetChanged();

        createDots(itemImages.size());
        updateDots();

        notification(itemContactNumber);
        getData();
      //getfirstimage();


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    DatabaseReference shopRef = databaseReference.child(contactNumber).child("items");
                    System.out.println("items " + contactNumber);

                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    currentuserName = dataSnapshot.child("name").getValue(String.class);

                    orderHistoryRef = databaseRef.child(itemContactNumber).child("orders")
                            .child(contactNumber);// Adjust the reference to your Firebase structure

//                    orderHistoryRef = databaseRef.child(shopContactNumber).child("orders")
//                            .child(contactNumber);// Adjust the reference to your Firebase structure
                    System.out.println("sgrgr " +currentuserName);
                    chatRef = databaseRef.child(itemContactNumber).child("orders").child(contactNumber)
                            .child("chats"); // Adjust the reference to your Firebase structure
                    // Fetch order history data
                    System.out.println("wewfrg" +contactNumber);

                    //fetchOrderHistoryAndChatMessages(orderHistoryRef, chatRef);
                    // Fetch chat message data
                    //fetchChatMessages(chatRef);
                    System.out.println("sdfsfs " +currentuserName);

                    if (contactNumber != null && itemContactNumber != null && contactNumber.equals(itemContactNumber)) {
                        btnlayout.setVisibility(View.GONE);
                    } else if (contactNumber.equals(Contactnumber)){

                    } else {

                    }

                    shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                String itemName = itemSnapshot.child("itemname").getValue(String.class);
                                String itemPrice = itemSnapshot.child("price").getValue(String.class);
                                //String itemPrice = itemPriceTextView.getText().toString();
                                DatabaseReference requestsRef = databaseReference.child(contactNumber).child("requests");
                                //DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId).child("requests");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error gracefully
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
            }
        });

        btnmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterqty.getText().toString().trim().isEmpty()) {
                    enterqty.setError("Please enter quantity.");
                } else {
                    handleOrderPlacement(ordCartkey, prodKey, scratchTime);
                }
            }
        });

        shareProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ItemDetails.this, "Ok", Toast.LENGTH_SHORT).show();
                shareProducts(firstImageUrl,itemName,itemDescription);
            }
        });

        getProductLinkData(itemContactNumber,itemkey);

        //getCouponExistence();

        getCurrentCpn();

    }


    // Helper method to convert long to int, handling overflow
    private int convertLongToInt(long value) {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            // Handle overflow by returning a default value or throwing an exception
            return 0; // Default value, replace with your logic
        }
        return (int) value;
    }

    public void qtyCheck(){
        if(enterqty.getText().toString().trim().isEmpty()){
            callayout.setVisibility(View.GONE);
            totalCard.setVisibility(View.GONE);
        }else {
            finalDiscount=0;
            finalTotalAmt=0;
            discountWithoutCoupon=0;;
            double wholesaleAmt=Double.parseDouble(wholesale);
            double retailAmt=Double.parseDouble(itemSellPrice);
            double mrpAmt=Double.parseDouble(itemPrice);
            int wpqty=Integer.parseInt(Minqty);
            int qty=Integer.parseInt(enterqty.getText().toString().trim());
            double mrpTotal=mrpAmt*qty;
            if(qty>=wpqty){ // wholesale price applied
                double total=wholesaleAmt*qty;
                totalamt.setText(String.valueOf(wholesaleAmt)+ " x "+String.valueOf(qty)+" = "+String.valueOf(total));
                discount.setText(""+(mrpTotal-total));
                total=total+couponAmount;
                saveAmt.setText("₹ " + (mrpTotal-total));
                totalAmt.setText("Total : ₹ " + total);
                finalTotalAmt=total;
                callayout.setVisibility(View.VISIBLE);
                totalCard.setVisibility(View.VISIBLE);
            }else { //Retail price applied
                double total=retailAmt*qty;
                totalamt.setText(String.valueOf(retailAmt)+ " x "+String.valueOf(qty)+" = "+String.valueOf(total));
                // totalamt.setText(String.valueOf(total));
                discount.setText(""+(mrpTotal-total));
                total=total+couponAmount;
                saveAmt.setText("₹ " + (mrpTotal-total));
                totalAmt.setText("Total : ₹ " + total);
                finalTotalAmt=total;
                callayout.setVisibility(View.VISIBLE);
                totalCard.setVisibility(View.VISIBLE);
            }
        }
    }


    private void placeorderbtn() {
        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Products").child(itemContactNumber).child(itemkey).child("coupons");
        couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // The "coupons" node is present, show the bottom sheet dialog
                    DatabaseReference ordersRef = databaseRef.child(itemContactNumber).child("orders").child(userId);
                    ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (DataSnapshot orderKeySnapshot : dataSnapshot.getChildren()) {
                                    String orderKey = orderKeySnapshot.getKey();
                                    if (orderKey.contains("RX")){
                                        ordersRef.child(orderKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String status = snapshot.child("status").getValue(String.class);
                                                    System.out.println("resdfvc " + status);
                                                    String quantity = enterqty.getText().toString().trim();
                                                    if (quantity.isEmpty()) {
                                                        enterqty.setError("Please enter quantity.");
                                                    } else {
                                                        if (status != null || status.equals("cart") || status.equals("Expired") || status.equals("Placed") || status.equals("Delivered") || status.equals("Rejected")) {
                                                            handleOrderPlacement(ordCartkey, prodKey, scratchTime);
                                                        } else {
                                                          //  Toast.makeText(ItemDetails.this, "" + status, Toast.LENGTH_SHORT).show();
                                                            //bottomSheetDialog();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }
                            } else{
                                if (enterqty.getText().toString().trim().isEmpty()){
                                    enterqty.setError("Please enter quantity.");
                                }else {
                                  //  bottomSheetDialog();
                                   // Toast.makeText(ItemDetails.this, "No orders found.", Toast.LENGTH_SHORT).show();
                                    // Handle the case when there are no orders or 'contactNumber' does not exist
                                    System.out.println("No orders found.");
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled event
                            System.out.println("DatabaseError: " + databaseError.getMessage());
                        }
                    });

                } else {
//                    couponCard.setVisibility(View.GONE);
                    // The "coupons" node is not present, handle accordingly
                    String ordCartkey = orderKey = generateShortRandomId(itemkey);
                    String scratchTime ="-";
                    String prodKey ="-";
                    handleOrderPlacement(ordCartkey,prodKey,scratchTime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void handleOrderPlacement(String ordCartkey, String scratchTime, String prodKey) {
        String quantity = enterqty.getText().toString().trim();
        if (quantity.isEmpty()) {
            enterqty.setError("Please enter quantity.");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetails.this);
            builder.setTitle("Place Order");
            builder.setMessage("Are you sure you want to place the order?");
            builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Place the order logic goes here
                     clickTime = System.currentTimeMillis();

                    if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_INTERVAL) {
                        // Double click detected, send a message
                        sendMessage();
                    } else {
                        if (ordCartkey != null){
                            placeOrder(clickTime, ordCartkey, prodKey, scratchTime);
                        }else{
                            // Single click detected and quantity is not empty, place an order
                            String ordCartkey = orderKey = generateShortRandomId(itemkey);
                            String scratchTime = "-";
                            String prodKey = "-";
                            placeOrder(clickTime, ordCartkey, scratchTime, prodKey);
                        }

                    }

                    userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot currentUsersnapshot) {
                            if (currentUsersnapshot.exists()) {
                                String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
                                String currentUserName = currentUsersnapshot.child("name").getValue(String.class);

                                DatabaseReference notificationRef = databaseRef.child(itemContactNumber).child("notification");
                                String notificationKey = notificationRef.push().getKey();

                                String message = currentUserName + " ordered " + itemName + " product.";
                                String order = itemName;

                                Map<String, Object> notificationData = new HashMap<>();
                                notificationData.put("message", message);
                                notificationData.put("order", order);
                                notificationData.put("orderkey", orderKey);
                                notificationData.put("contactNumber", userId);

                                if (!TextUtils.isEmpty(orderKey)) {
                                    notificationRef.child(orderKey).setValue(notificationData);
                                }

                                DatabaseReference shopNotificationCountRef = databaseRef.child(itemContactNumber).child("notificationcount");
                                DatabaseReference NotificationCountRef = databaseRef.child(itemContactNumber).child("count").child("notificationcount");

                                shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                                        int newCount = currentCount + 1;
                                        shopNotificationCountRef.setValue(newCount);
                                        NotificationCountRef.setValue(newCount);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle onCancelled event
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
                    dialogInterface.dismiss();
                    placeOrderDialog();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    private void sendMessage() {
        // Send a message in the Firebase Realtime Database with status "sent"
        DatabaseReference messagesRef = databaseRef.child(itemContactNumber).child("messages");
        String messageId = messagesRef.push().getKey();
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("text", "Your message text");
        messageData.put("senderId", userId);
        messageData.put("status", "sent"); // Status is initially set to "sent"
        messagesRef.child(messageId).setValue(messageData);
    }


    private void placeOrder(long clickTime, String ordCartkey, String scratchTime, String prodKey) {
        // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
        SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        formattedTime = TimeFormat.format(new Date(clickTime));

        // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
        formattedDate = dateFormat.format(new Date(clickTime));
        if (ordCartkey!= null){
            // Place the order in the Firebase Realtime Database with status "pending"
            DatabaseReference ordersRef = databaseRef.child(itemContactNumber).child("orders").child(contactNumber);
            orderKey = ordCartkey;
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("itemName", itemName);
            orderData.put("firstImageUrl", firstImageUrl);
            orderData.put("buyerContactNumber", contactNumber);
            orderData.put("orderkey", orderKey);
            orderData.put("status", "Placed");
            orderData.put("buyerName", currentuserName);
            orderData.put("quantity", enterqty.getText().toString());
            orderData.put("timestamp", formattedTime);
            orderData.put("datetamp", formattedDate);
            orderData.put("time", scratchTime);
            orderData.put("productkey", prodKey);
            orderData.put("shopOwnerContactNumber", itemContactNumber);
            orderData.put("shopImage", shopImage);
            orderData.put("shopName", shopName);
            orderData.put("senderID", userId);
            orderData.put("receiverID", itemContactNumber);
            String amt = String.valueOf(totalamts);
            orderData.put("totalAmt", amt);
            String successMessage = "Order is placed successfully of " +itemName+ " with quantity: " + enterqty.getText().toString();
            DatabaseReference userMessageref = databaseRef.child(itemContactNumber).child("orders").child(contactNumber)
                    .child("chats").child(orderKey);
            String messageKey = userMessageref.push().getKey();
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", successMessage);
            messageData.put("sender", userId); // Set sender as current user ID
            messageData.put("receiver", itemContactNumber); // Set receiver as shop contact number
            messageData.put("timestamp", formattedTime);
            messageData.put("datetamp", formattedDate);
            messageData.put("chatkey", messageKey);
            System.out.println("messageData: " + messageData);
            ordersRef.child(orderKey).setValue(orderData);
            userMessageref.child(messageKey).setValue(messageData);

            DatabaseReference orderedRef = userRef.child(userId).child("ordered").child(itemContactNumber);
            DatabaseReference userMessageRef = userRef.child(userId).child("ordered").child(itemContactNumber)
                    .child("chats").child(orderKey);
            orderedRef.child(orderKey).setValue(orderData);
            userMessageRef.child(messageKey).setValue(messageData);
        }
        else{
            // Place the order in the Firebase Realtime Database with status "pending"
            DatabaseReference ordersRef = databaseRef.child(itemContactNumber).child("orders").child(contactNumber);
            orderKey = ordCartkey;
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("itemName", itemName);
            orderData.put("firstImageUrl", firstImageUrl);
            orderData.put("buyerContactNumber", contactNumber);
            orderData.put("orderkey", orderKey);
            orderData.put("status", "Placed");
            System.out.println("shgbhb" + currentuserName);
            orderData.put("buyerName", currentuserName);
            orderData.put("quantity", enterqty.getText().toString());
            orderData.put("timestamp", formattedTime);
            orderData.put("time", scratchTime);
            orderData.put("productkey", prodKey);
            orderData.put("datetamp", formattedDate);
            orderData.put("shopOwnerContactNumber", itemContactNumber);
            orderData.put("shopImage", shopImage);
            orderData.put("shopName", shopName);
            orderData.put("senderID", userId);
            orderData.put("receiverID", itemContactNumber);
            String amt = String.valueOf(totalamts);
            orderData.put("totalAmt", amt);
            String successMessage = "Order is placed successfully of " +itemName+ " with quantity: " + enterqty.getText().toString();
            DatabaseReference userMessageref = databaseRef.child(itemContactNumber).child("orders").child(contactNumber)
                    .child("chats").child(orderKey);
            String messageKey = userMessageref.push().getKey();
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", successMessage);
            messageData.put("sender", userId); // Set sender as current user ID
            messageData.put("receiver", itemContactNumber); // Set receiver as shop contact number
            messageData.put("timestamp", formattedTime);
            messageData.put("datetamp", formattedDate);
            messageData.put("chatkey", messageKey);
            System.out.println("messageData: " + messageData);
            ordersRef.child(orderKey).setValue(orderData);
            userMessageref.child(messageKey).setValue(messageData);

            DatabaseReference orderedRef = userRef.child(userId).child("ordered").child(itemContactNumber);
            DatabaseReference userMessageRef = userRef.child(userId).child("ordered").child(itemContactNumber)
                    .child("chats").child(orderKey);
            orderedRef.child(orderKey).setValue(orderData);
            userMessageRef.child(messageKey).setValue(messageData);
        }


        DatabaseReference shopNodeRef = databaseRef.child(itemContactNumber);
        DatabaseReference orderCountRef = shopNodeRef.child("ordercount");
        DatabaseReference countRef = databaseRef.child(itemContactNumber).child("count").child("ordercount");

        // Use a ValueEventListener to get the current order count and update it
        orderCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long currentOrderCount = 0;
                if (dataSnapshot.exists()) {
                    currentOrderCount = (long) dataSnapshot.getValue();
                }

                // Increment the order count and update it in the database
                currentOrderCount++;
                orderCountRef.setValue(currentOrderCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
        countRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long currentOrderCount = 0;
                if (dataSnapshot.exists()) {
                    currentOrderCount = (long) dataSnapshot.getValue();
                }

                // Increment the order count and update it in the database
                currentOrderCount++;
                countRef.setValue(currentOrderCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
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
            String ss = "RX"+hexString.toString().substring(0, 15);
            // Take the first 10 characters as the shortened random ID
            return ss;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your application's needs
            return null;
        }
    }

    private void updateBackground(RadioButton radioButton) {
        if (toggle) {
            radioButton.setBackgroundColor(getResources().getColor(R.color.mainbrandcolor));
        } else {
            radioButton.setBackground(getResources().getDrawable(R.drawable.checkbox_selector));
            wholesalelay.setVisibility(View.GONE);
        }
    }

    private void onClick(int i) {
    }

    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        Intent intent=new Intent(ItemDetails.this,BottomNavigation.class);
        startActivity(intent);
        ItemDetails.this.finish();
    }

    private void readDataFromFirebase() {
        databaseReference.child(itemContactNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing shop list
                shopList.clear();

                // Process the retrieved data
//                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {

                    Boolean profileverify = dataSnapshot.child("profileverified").getValue(Boolean.class);
                    // long promotedShopCount = shopSnapshot.child("promotedShops").getChildrenCount();
                    System.out.println("sdfdf " + profileverify);
                    // Check if profile is verified (true) before adding to the list
                    if (profileverify != null && profileverify) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        shopName = dataSnapshot.child("shopName").getValue(String.class);
                        //Log.d("FirebaseData", "shopName: " + shopName);
                        String contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                         address = dataSnapshot.child("address").getValue(String.class);
                        String url = dataSnapshot.child("url").getValue(String.class);
                        String service = dataSnapshot.child("service").getValue(String.class);
                        String taluka = dataSnapshot.child("taluka").getValue(String.class);
                        String district = dataSnapshot.child("district").getValue(String.class);
                        String shopcategory = dataSnapshot.child("shopcategory").getValue(String.class);

                        // Retrieve the count of promoted shops
                        int promotedShopCount = dataSnapshot.child("promotionCount").getValue(Integer.class);
                        int ordercount = dataSnapshot.child("promotionCount").getValue(Integer.class);
                        int requestcount = dataSnapshot.child("promotionCount").getValue(Integer.class);
                        Log.d("TAG", "proc " + contactNumber);

                        List<ItemList> itemList = new ArrayList<>();
                        // Retrieve posts for the current shop
                        DataSnapshot postsSnapshot = dataSnapshot.child("items");
                        for (DataSnapshot itemSnapshot : postsSnapshot.getChildren()) {
                            String itemkey = itemSnapshot.getKey();

                            String itemName = itemSnapshot.child("itemname").getValue(String.class);
                            String price = itemSnapshot.child("price").getValue(String.class);
                            String sellprice = itemSnapshot.child("sell").getValue(String.class);
                            String offer = itemSnapshot.child("offer").getValue(String.class);
                            String description = itemSnapshot.child("description").getValue(String.class);
                            String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
                            String wholesale = itemSnapshot.child("wholesale").getValue(String.class);
                            String minqty = itemSnapshot.child("minquantity").getValue(String.class);
                            String servingArea = itemSnapshot.child("servingArea").getValue(String.class);
                            String status = itemSnapshot.child("status").getValue(String.class);
                            String itemCate = itemSnapshot.child("itemCate").getValue(String.class);
                            System.out.println("jfhv " +firstimage);

                            if (TextUtils.isEmpty(firstimage)) {
                                // Set a default image URL here
                                firstimage = String.valueOf(R.drawable.ic_outline_shopping_bag_24);
                            }

                            List<String> imageUrls = new ArrayList<>();
                            DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                            for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                String imageUrl = imageUrlSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                }
                            }

//                            String couponStatus = itemSnapshot.child("couponStatus").getValue(String.class);
//                            databaseReference.child(itemContactNumber).child(itemkey).child("coupons").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                                    if (snapshot.exists()){
//                                        couponfront = snapshot.child("front").getValue(String.class);
//                                        couponback = snapshot.child("back").getValue(String.class);
//                                        couponextraAmt = snapshot.child("extraAmt").getValue(String.class);
//                                        System.out.println("ergfx " +couponfront);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                                }
//                            });


                            ItemList item = new ItemList();
                            item.setShopName(shopName);
                            item.setShopimage(url);
                            item.setShopcontactNumber(contactNumber);
                            item.setAddress(address);
                            item.setDistrict(district);
                            item.setTaluka(taluka);
                            item.setName(itemName);
                            item.setPrice(price);
                            item.setSellPrice(sellprice);
                            item.setDescription(description);
                            item.setFirstImageUrl(firstimage);
                            item.setItemkey(itemkey);
                            item.setImagesUrls(imageUrls);
                            item.setOffer(offer);
                            item.setWholesaleprice(wholesale);
                            item.setMinqty(minqty);
                            item.setServingArea(servingArea);
                            item.setStatus(status);
                            item.setItemCate(itemCate);
                            item.setCouponfront(couponfront);
                            item.setCouponback(couponback);
                            item.setExtraAmt(couponextraAmt);
                            item.setCouponStatus(couponStatus);

//                                    ItemList item = new ItemList(shopname,shopimage,shopcontactNumber, itemName, price, sellprice, description,
//                                            firstImageUrl, itemkey, imageUrls, destrict, taluka,address, offer, wholesale, minqty, servingArea, status,
//                                            itemCate);

                            itemList.add(item);

                        }



                        // Create a Shop object and add it to the shop list
                        Shop shop = new Shop(name, shopName, contactNumber, address, url, service, district,
                                taluka, promotedShopCount, itemList, ordercount, requestcount,shopcategory);
                        shopList.add(shop);
                    }


                // Update the filtered list with the original list
                filteredList.clear();
                filteredList.addAll(shopList);
                // Notify the adapter about the data change
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e("FirebaseError", "Failed to read value: " + databaseError.getMessage());
            }
        });
    }


    private void storeItemNameOnly(DatabaseReference ordersRef, String itemName) {
        // Fetch the current list of item names
        ordersRef.child("itemNames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> itemNames = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String existingItemName = itemSnapshot.getValue(String.class);
                        itemNames.add(existingItemName);
                    }
                }

                // Add the new item name to the list
                itemNames.add(itemName);

                // Update the item names in the Firebase node
                ordersRef.child("itemNames").setValue(itemNames)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Order successfully stored in the database
                                // You can show a confirmation message if needed
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure to store the order
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void getfirstimage() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    Log.d("fgsdgfsdgsdf", "" + contactNumber);
//                    String ContactNumber = getIntent().getStringExtra("contactNumber");
//                    String itemkey = getIntent().getStringExtra("itemkey");
                    DatabaseReference ref = database.getReference("Shop/" + itemContactNumber + "/items/" + itemkey + "/firstImageUrl/" );
                    System.out.println("fsdfsdf" +ref);
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String firstImageUrl = dataSnapshot.getValue(String.class);
                                System.out.println("dfsd " + firstImageUrl);
                             //   intent = new Intent(getApplicationContext(), PlaceOrder.class);
                                // Create an Intent to pass the value to the next activity
                                intents.putExtra("firstImageUrl", firstImageUrl);
                                intents.putExtra("itemName", itemName);
                                intents.putExtra("itemPrice", itemPrice);

                                // Start the next activity
                                startActivity(intents);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Log.w(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w(TAG, "onCancelled", databaseError.toException());
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
                    DatabaseReference ref = database.getReference("Products/" + itemContactNumber + "/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    System.out.println("wedsvxc " +ref);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                 imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("fgsdgfsdgsdfzx", "" + img);
                                    imageUrls.add(img);
                                     size = imageUrls.size();
                                    System.out.println("regfr " +size);
//                                    if (x++ == dataSnapshot.getChildrenCount() - 1) {
//                                        // Log.d("ovov",""+uname);
//                                        ItemList shop = new ItemList();
//                                        // Update the image URLs in the Shop object
//                                         shop.setImagesUrls(imageUrls);
//                                        // Notify the adapter about the data change
//                                        imageAdapter.setImagesUrls(imageUrls);
//                                        imageAdapter.notifyDataSetChanged();
//                                    }

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

                    DatabaseReference imageref = database.getReference("Products/" + contactNumber + "/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    imageref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                 imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("ImageURL", "Index: " + i + " - URL: " + img);
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

    private void createDots(int numberOfDots) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < numberOfDots; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_unselected));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsLayout.addView(dot);
        }
    }

    private void updateDots() {
        int totalDots = dotsLayout.getChildCount();
        int visiblePosition = ((LinearLayoutManager) imageRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (imageUrls != null && !imageUrls.isEmpty() && visiblePosition != RecyclerView.NO_POSITION && visiblePosition < imageUrls.size()) {
            if (currentPosition >= 0 && currentPosition < totalDots) {
                ImageView previousDot = (ImageView) dotsLayout.getChildAt(currentPosition);
                if (previousDot != null) {
                    previousDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_unselected));
                }
            }
            currentPosition = visiblePosition;

            if (currentPosition >= 0 && currentPosition < totalDots) {
                ImageView selectedDot = (ImageView) dotsLayout.getChildAt(currentPosition);
                if (selectedDot != null) {
                    selectedDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_selected));
                }
            }
        } else {
            // Debug logging to help identify the issue
            Log.d("DotsDebug", "imageUrls: " + imageUrls);
            Log.d("DotsDebug", "visiblePosition: " + visiblePosition);
            Log.d("DotsDebug", "currentPosition: " + currentPosition);
            Log.d("DotsDebug", "totalDots: " + totalDots);

            if (totalDots > 0) {
                currentPosition = 0; // Set the current position to the first dot
                ImageView selectedDot = (ImageView) dotsLayout.getChildAt(currentPosition);
                if (selectedDot != null) {
                    selectedDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_selected));
                }
            }
        }
    }


    @Override
    public void onGalleryClick(int position) {

    }

    private void placeOrderDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.place_order_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button chatbtn = dialog.findViewById(R.id.chatButton);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent orderDetailsIntent = new Intent(getApplicationContext(), UserOrderdetails.class);
//                orderDetailsIntent.putExtra("buyerContactNumber", userId);
//                orderDetailsIntent.putExtra("orderkey", orderKey);
//                orderDetailsIntent.putExtra("ownercontactNumber" , itemContactNumber);
//                boolean isBottonCardVisible = false; // Set this to true if you want it initially visible
//                orderDetailsIntent.putExtra("isBottonCardVisible", isBottonCardVisible);
//                startActivity(orderDetailsIntent);
                finish();
            }
        });


    }

    private void bottomSheetDialog(){
        final Dialog bottomSheetView = new Dialog(this);
        bottomSheetView.setContentView(R.layout.placeorder_bottom_sheet);
        bottomSheetView.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView back = bottomSheetView.findViewById(R.id.back);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ScratchCardView scratchCardView = bottomSheetView.findViewById(R.id.scrachCard);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        KonfettiView konfettiView = bottomSheetView.findViewById(R.id.konfettiView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView couponBackImg = bottomSheetView.findViewById(R.id.couponback);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView coupontext = bottomSheetView.findViewById(R.id.coupontext);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CardView placedCard = bottomSheetView.findViewById(R.id.placeorderCard);

        DatabaseReference productRefs = FirebaseDatabase.getInstance().getReference("CpnData/" + currentCpnId + "/");
        productRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot productSnapshot) {
                if (productSnapshot.exists()) {
                    couponback = productSnapshot.child("bkImg").getValue(String.class);
                    couponfront = productSnapshot.child("ftImg").getValue(String.class);
                    extraAmt = productSnapshot.child("amt").getValue(String.class);
                    couponAmount = Double.parseDouble(extraAmt);
                    Glide.with(ItemDetails.this).load(couponback)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(couponBackImg);
                    scratchCardView.setScratchImageUrl(couponfront);
                    coupontext.setText("₹ "+extraAmt);
                    coupontext.setVisibility(View.VISIBLE);
                }else {
                    couponAmount = 0;
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });


        placedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the scratch card has been revealed
                if (isScratchCardRevealed) {
                    // Scratch card has been revealed, proceed with the action
                    // Add your action logic here
                    placeOrder(clickTime, ordCartkey, prodKey, scratchTime);
                } else {
                    // Scratch card has not been revealed, show a message or handle accordingly
                    // For example, display a toast message
                    Toast.makeText(getApplicationContext(), "Please reveal the scratch card first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetView.dismiss();
                // Reload the activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });



        scratchCardView.setRevealListener(new ScratchCardView.RevealListener() {
            @Override
            public void onRevealed() {
                couponAmount= Double.parseDouble(extraAmt);
                //getCouponExistence();
                qtyCheck();
                isScratchCardRevealed = true;
                // Handle reveal completion
                // Toast.makeText(Scratch_Coupon.this, "ol", Toast.LENGTH_SHORT).show();
                scratchCardView.setVisibility(View.GONE);
                coupontext1.setVisibility(View.VISIBLE);
                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(1000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12,5))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        //.setPosition(50f, konfettiView.getWidth()+10f, -50f, -50f)
                        .streamFor(300, 1000L);

                // Store the current time in Firebase
                SimpleDateFormat TimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                formattedTime = TimeFormat.format(new Date());

// Parse the formatted time to a Date object
                try {
                    Date currentTime = TimeFormat.parse(formattedTime);
                    // Add 12 hours to the current time
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentTime);
                    calendar.add(Calendar.HOUR, 12);

// Format the new time with added 12 hours
                    Date newTime = calendar.getTime();
                    String formattedTimeWith12Hours = TimeFormat.format(newTime);

// Store the formatted time with added 12 hours in Firebase

                    String ssd="Applied&&"+formattedTimeWith12Hours;
                    DatabaseReference cpnRefs = FirebaseDatabase.getInstance().getReference("CpnData/" + currentCpnId + "/Members/");
                    cpnRefs.child(userId).setValue(ssd);
                   // ssss

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetView.dismiss();

                        }
                    }, 5000); // 5 seconds delay



                }catch (Exception e){

                }

            }
        });

      //  bottomSheetView.setCanceledOnTouchOutside(false);
        //bottomSheetView.setCancelable(false);
        bottomSheetView.show();

    }


    @Override
    public void onRevealed() {
        // Handle the reveal event here
        //Toast.makeText(this, "Card Revealed!", Toast.LENGTH_SHORT).show();
    }

    public void getCouponExistence(){ // check coupon available or not
        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Products").child(itemContactNumber).child(itemkey);
        couponRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String ss=snapshot.child("couponStatus").getValue(String.class);
                    extraAmt = snapshot.child("extraAmt").getValue(String.class);
                    Log.d("adfgadgfasf",""+ss);
                    System.out.println("gfsgsdsdsfg"+ss);
                    if(ss.equals("Enable")){
                       // Toast.makeText(ItemDetails.this, "enable", Toast.LENGTH_SHORT).show();
                        getCouponStatus(itemkey,"Cpn12345");
                    }else {
                       // Toast.makeText(ItemDetails.this, "disable", Toast.LENGTH_SHORT).show();
                        cpnBefore.setVisibility(View.GONE);
                        cpnAfter.setVisibility(View.GONE);
                    }
                }else {
                    cpnBefore.setVisibility(View.GONE);
                    cpnAfter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

    public void getCouponStatus(String prodKey,String cpnId){ // Applied or not
        DatabaseReference ordersRef = databaseRef.child(itemContactNumber).child("orders").child(userId);
        ordersRef.orderByChild("productkey").equalTo(prodKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot orderKeySnapshot : dataSnapshot.getChildren()) {
                        String orderKey = orderKeySnapshot.getKey();
                        String status=orderKeySnapshot.child("status").getValue(String.class);
                        String cpnAmt=orderKeySnapshot.child("extraAmt").getValue(String.class);
                        String couponTimer=orderKeySnapshot.child("time").getValue(String.class);
                        String cpn=orderKeySnapshot.child("cpnId").getValue(String.class);


                        if(status.equals("cart")){ // coupon scracthed show timer
                            if(cpnId.equals(cpn)){
                                cpnBefore.setVisibility(View.GONE);
                                if(cpnAmt!=null){
                                    couponAmount= Double.parseDouble(cpnAmt);
                                    // setTimer(couponTimer);
                                    setTimer(couponTimer);
                                }else {
                                    couponAmount= 0;
                                }
                            }

                        }else if(status.equals("Expired") || status.equals("Placed") || status.equals("Approved")){
                            cpnBefore.setVisibility(View.GONE);
                            cpnAfter.setVisibility(View.GONE);
                            couponAmount= 0;
                        }else if(status.equals("Delivered") || status.equals("Rejected")){
                            if(cpnId.equals(cpn)){
                                System.out.println("fdsfsdfdafasdf "+orderKey);
                                cpnBefore.setVisibility(View.VISIBLE);
                                cpnAfter.setVisibility(View.GONE);
                                couponAmount= 0;
                            }else {
                                System.out.println("dddddddfdfdf "+cpn);
                                cpnBefore.setVisibility(View.GONE);
                                cpnAfter.setVisibility(View.GONE);
                                couponAmount= 0;
                            }

                        }
                    }
                } else{
                    cpnBefore.setVisibility(View.VISIBLE);
                    cpnAfter.setVisibility(View.GONE);
                    couponAmount= 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
                System.out.println("DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    public void setTimer(String endTimeString){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentTimeString = format.format(new Date());
       // String endTimeString = "31/01/2024 13:00:00";

        // Calculate time difference and return as integers
        int[] timeDifference = getTimeDifference(currentTimeString, endTimeString);

        // Print the time difference
        System.out.println("Time difference: " + timeDifference[0] + " hours, " + timeDifference[1] + " minutes, " + timeDifference[2] + " seconds");

        if(timeDifference[2]<0 || timeDifference[1]<0){ //time passed
            cpnBefore.setVisibility(View.GONE);
            cpnAfter.setVisibility(View.GONE);
            easyCountDownTextview.stopTimer();
        }else {
            cpnBefore.setVisibility(View.GONE);
            cpnAfter.setVisibility(View.VISIBLE);
            // Toast.makeText(this, "Ok1", Toast.LENGTH_SHORT).show();
            easyCountDownTextview.setTime(0, timeDifference[0], timeDifference[1], timeDifference[2]);
            easyCountDownTextview.startTimer();
        }
        easyCountDownTextview.setOnTick(new CountDownInterface() {
                @Override
                public void onTick(long time) {

                }
                @Override
                public void onFinish() {
                    new CountDownTimer(2000, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }
                        public void onFinish() {
                            // Countdown has finished, hide the countdown text view and set status to "expired"
                           // easyCountDownTextview.setVisibility(View.GONE);
                            // Set status to "expired" in the database
//                            DatabaseReference ordersRef = databaseRef.child(itemContactNumber).child("orders").child(contactNumber).child(ordCartkey);
//                            ordersRef.child("status").setValue("Expired");
                        }
                    }.start();
                    onStart();
                }
        });
    }

    public static int[] getTimeDifference(String startTimeString, String endTimeString) {
        int[] timeDifference = new int[3]; // Array to hold hours, minutes, seconds

        // Parse start and end times
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date startTime = format.parse(startTimeString);
            Date endTime = format.parse(endTimeString);

            // Calculate time difference in milliseconds
            long timeDifferenceMillis = endTime.getTime() - startTime.getTime();

            // Convert milliseconds to hours, minutes, and seconds
            timeDifference[0] = (int) TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);
            timeDifference[1] = (int) (TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis) % 60);
            timeDifference[2] = (int) (TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis) % 60);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeDifference;

    }

    private void notification(String commAdmin) {
        if (!userId.equals(commAdmin)) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");

            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                       String currentUserName = snapshot.child("name").getValue(String.class);
                        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(commAdmin)
                                .child("notification"); // Ensure unique child key for each notification

                        String message = currentUserName + " view your " +itemName+" product.";

                        // Create a map to store the message
                        Map<String, Object> notificationData = new HashMap<>();
                        notificationData.put("message", message);
                        notificationData.put("key", userId);

                        String key = notificationRef.push().getKey();
                        // Store the message under the currentusercontactNum
                        if (key!=null) {
                            notificationRef.child(key).setValue(notificationData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@io.reactivex.rxjava3.annotations.NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                        .child(commAdmin)
                                                        .child("notificationcount");

                                                DatabaseReference NotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                        .child(commAdmin)
                                                        .child("count")
                                                        .child("notificationcount");

                                                shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot snapshot) {
                                                        int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                                                        int newCount = currentCount + 1;

                                                        // Update the notification count
                                                        shopNotificationCountRef.setValue(newCount);
                                                        NotificationCountRef.setValue(newCount);
                                                    }

                                                    @Override
                                                    public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                                                        // Handle onCancelled event

                                                    }
                                                });
                                            } else {
                                                // Handle failure to store notification
                                            }
                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });
        }
    }


    public void shareProducts(String prodImgXX,String prodNmXX,String prodesXX){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.shareproduct, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Disable scrolling for the BottomSheetDialog
        // BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        //   behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button myLoc = bottomSheetView.findViewById(R.id.shareProdssXXX);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button custshare = bottomSheetView.findViewById(R.id.customshare);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView prodImg = bottomSheetView.findViewById(R.id.prodImgXXX);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView prodName = bottomSheetView.findViewById(R.id.prodNameXXX);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView prodesc = bottomSheetView.findViewById(R.id.prodescXXX);

        Glide.with(ItemDetails.this).load(prodImgXX)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(prodImg);

        prodesc.setText(prodesXX);
        prodName.setText(prodNmXX);

        prodNameXXX=prodNmXX;
        proDescXXX=prodesXX;

        myLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check and request location permissions if not granted
                new DownloadImageTask().execute(prodImgXX);
            }
        });

        custshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ItemDetails.this,SpacialProductHare.class);
                intent1.putExtra("Url",firstImageUrl);
                intent1.putExtra("Dlink",DLink);
                intent1.putExtra("Name",itemName);
                intent1.putExtra("Desc",itemDescription);
                startActivity(intent1);
            }
        });

        bottomSheetDialog.show();
    }

    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ItemDetails.this);
            progressDialog.setMessage("Loading apps for sharing product...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

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
                    return BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            progressDialog.dismiss();
            if (result != null) {
                // Share the downloaded image
                shareImageAndText(result);
            } else {
                Toast.makeText(ItemDetails.this, "Failed to download image", Toast.LENGTH_SHORT).show();
            }
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
            intent.putExtra(Intent.EXTRA_TEXT, "Hi! I found useful product on Kamdhanda App.\n\n" +prodNameXXX+"\n\n"+proDescXXX+"\n\nCheck this out!\n"+DLink);

            // Grant temporary read permission to the content URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Use Intent.createChooser to show a dialog with app options
            Intent chooser = Intent.createChooser(intent, "Share with");
            // Verify the intent will resolve to at least one activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                Toast.makeText(ItemDetails.this, "No app can handle this request", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ItemDetails.this, "Failed to save image", Toast.LENGTH_SHORT).show();
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

    public void getProductLinkData(String mobno, String prodkey) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products/" + mobno + "/" + prodkey + "/");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot productSnapshot) {
                if (productSnapshot.exists()) {
                    DLink = productSnapshot.child("dynamicLink").getValue(String.class);
                }else {
                    DLink="-";
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getCurrentCpn(){
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products/" + itemContactNumber + "/" + itemkey + "/");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot productSnapshot) {
                if (productSnapshot.exists()) {
                    if (!(productSnapshot.child("CurrentCpnId").getValue(String.class) == null)){
                        currentCpnId = productSnapshot.child("CurrentCpnId").getValue(String.class);
                      //  Log.d("fdfdsffad","ok  "+currentCpnId);
                        if (!currentCpnId.equals("No")){
                            getCPNStatus();
                        }else {
                            couponAmount = 0;
                        }

                    }else {
                        currentCpnId="No";
                        couponAmount = 0;
                        Log.d("fdfdsffad","dd  "+currentCpnId);
                        //hide coupon
                    }
                }else {
                    currentCpnId="No";
                    couponAmount = 0;
                    //hide coupon
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getCPNStatus(){
        DatabaseReference productRefs = FirebaseDatabase.getInstance().getReference("CpnData/" + currentCpnId + "/");
        productRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot productSnapshot) {
                if (productSnapshot.exists()) {
                    couponback = productSnapshot.child("bkImg").getValue(String.class);
                    couponfront = productSnapshot.child("ftImg").getValue(String.class);
                    extraAmt = productSnapshot.child("amt").getValue(String.class);
                    couponAmount = extraAmt != null ? Double.parseDouble(extraAmt) : 0;
                    cpnDiscAmt.setText("₹ "+extraAmt+ " discount on checkout");
                    productRefs.child("Members"+"/"+userId+"/").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot productSnapshot) {
                            if (productSnapshot.exists()) {
                                if(!((productSnapshot.getValue(String.class)) == null)){
                                    String status=productSnapshot.getValue(String.class);
                                    if(status.equals("Expired")){
                                        //Hide Coupon
                                        cpnBefore.setVisibility(View.GONE);
                                        cpnAfter.setVisibility(View.GONE);
                                        couponAmount=0;
                                    }else {
                                        //Show coupon with timer
                                        cpnBefore.setVisibility(View.GONE);
                                        cpnAfter.setVisibility(View.VISIBLE);
                                        String[] parts = status.split("&&");
                                        String endTime=parts[1];
                                        setTimer(endTime);
                                    }
                                }else {
                                    //Show full coupon
                                    cpnBefore.setVisibility(View.VISIBLE);
                                    cpnAfter.setVisibility(View.GONE);
                                    couponAmount = Double.parseDouble(couponextraAmt);
                                }
                            }else {
                                //Toast.makeText(ItemDetails.this, "No ", Toast.LENGTH_SHORT).show();
                                cpnBefore.setVisibility(View.VISIBLE);
                                cpnAfter.setVisibility(View.GONE);
                                couponAmount = Double.parseDouble(couponextraAmt);
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });

                }else {
                    couponAmount = 0;
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }

        });
    }

}

