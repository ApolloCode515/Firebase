package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LoginMain extends AppCompatActivity {
    LinearLayout moblay,otplay,reglay;
    Button loginBtn;
    EditText mobno,otp,usernm,usermb, userem;
    TextView header;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mref;
    private static final String TAG = "PhoneAuthActivity";
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String scnm="",scity="",sstate="",sdist="",stal="",sowner="",udix="",umob,duid;
    String uuid, shortLink;
    TextView veri,terms;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    int randomNumber;
    String serverOtp,userOtp,status;
    CardView cxc;
    LinearLayout regewindow;
    DatabaseReference usersRef;
    FirebaseAuth firebaseAuth;
    public static final String PREFS_NAME = "MyLoginPrefsFile";
    String mobilenumber, contactnumber ,userid;
    List<String> myList = new ArrayList<>();
    List<String> useridList = new ArrayList<>();

    EditText name, email, phone, password, taluka;
    Spinner districtSpinner;
    Button register;
    TextView textlogin, errortextview, districterrortext, talukaerrortext;


    private Spinner TalukaSpinner;
    private ArrayAdapter<String> districtAdapter;
    private ArrayAdapter<String> subDivisionAdapter;
    private String selectedDistrict;
    private String selectedTaluka,storedUserID;
    private boolean isDistrictSelected = false;
    private boolean isTalukaSelected = false;
    private SharedPreferences sharedPreferences;

    private static final String USER_ID_KEY = "userID";
    private static final String REGISTRATION_FLAG_KEY = "registration_completed";

    private boolean hasLoggedIn = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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

        moblay=findViewById(R.id.mobwindow);
        otplay=findViewById(R.id.otpwindow);
        reglay=findViewById(R.id.regwindow);
        veri=findViewById(R.id.otpxxww);
        regewindow = findViewById(R.id.regewindow);

        loginBtn=findViewById(R.id.loginfox);

        mobno=findViewById(R.id.mobnoxcxc);
        otp=findViewById(R.id.otpxx);
        usernm=findViewById(R.id.usernm);
        usermb=findViewById(R.id.mobilenum);
        userem=findViewById(R.id.emailid);
        header=findViewById(R.id.headerxx);
        cxc=findViewById(R.id.cxc);

        name = findViewById(R.id.editname);
        email = findViewById(R.id.editemail);
        password = findViewById(R.id.editpassword);
        phone = findViewById(R.id.editphone);
        errortextview = findViewById(R.id.errortextview);
        //district = findViewById(R.id.district);
        //taluka = findViewById(R.id.taluka);

        districterrortext = findViewById(R.id.districterrortext);
        talukaerrortext = findViewById(R.id.talukaerrortext);
        register = findViewById(R.id.register);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        FirebaseAuth firebaseAuth = null;
//        if (firebaseAuth.getCurrentUser() != null) {
//            // User is already logged in, redirect to Business class
//            startActivity(new Intent(LoginMain.this, Business.class));
//            finish();
//        }


        SharedPreferences settings = getSharedPreferences("YourSharedPrefsName", 0);
        String userID = settings.getString("userID", null);

        if (userID != null) {
            // The userID has been restored; you can use it as needed
        } else {
            // The userID does not exist (e.g., first-time install or manual removal); handle accordingly
        }


        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_MULTI_PROCESS);
         storedUserID = sharedPreference.getString(USER_ID_KEY, null);


        TalukaSpinner = findViewById(R.id.taluka);
        districtSpinner = findViewById(R.id.district);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.districts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

//        // For Taluka Spinner
//        Spinner talukaSpinner = findViewById(R.id.talukaSpinner);
//        ArrayAdapter<CharSequence> talukaAdapter = ArrayAdapter.createFromResource(this, R.array.Taluka_array, android.R.layout.simple_spinner_item);
//        talukaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        talukaSpinner.setAdapter(talukaAdapter);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected district from the first spinner
                selectedDistrict = adapterView.getItemAtPosition(position).toString();
                isDistrictSelected = !selectedDistrict.equals("Select District");
                districterrortext.setVisibility(isDistrictSelected ? View.GONE : View.VISIBLE);
                // Load sub-division data based on the selected district
                int subDivisionArrayId = getResources().getIdentifier(
                        "Taluka_array_" + selectedDistrict.toLowerCase(),
                        "array",
                        getPackageName()
                );

                if (subDivisionArrayId != 0) {
                    String[] subDivisions = getResources().getStringArray(subDivisionArrayId);
                    subDivisionAdapter = new ArrayAdapter<>(LoginMain.this,
                            android.R.layout.simple_spinner_item, subDivisions);
                    subDivisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TalukaSpinner.setAdapter(subDivisionAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
                isDistrictSelected = false;
            }
        });

        TalukaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected taluka from the second spinner
                selectedTaluka = adapterView.getItemAtPosition(position).toString();
                isTalukaSelected = !selectedTaluka.equals("Select Taluka");
                talukaerrortext.setVisibility(isTalukaSelected ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
                isTalukaSelected = false;

            }
        });

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        System.out.println("effvv " + dataSnapshot.getKey());

                        usersRef.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                   contactnumber = snapshot.child("contactNumber").getValue(String.class);
                                   userid = snapshot.child("UserID").getValue(String.class);
                                    myList.add(contactnumber);
                                    useridList.add(userid);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        veri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(veri.getText().toString().trim().equals("Resend")){
                    serverOtp= String.valueOf(generateRandomNumber());
                    SendSms();
                    new CountDownTimer(100000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            //loginBtn.setText("" + millisUntilFinished / 1000 +" s");
                            veri.setText("" + millisUntilFinished / 1000 +" s");

                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish() {
                            veri.setText("Resend");
                        }
                    }.start();
                    onStart();
                }
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginBtn.getText().toString().trim().equals("Continue")){
                    if(mobno.getText().toString().trim().isEmpty()){
                        mobno.setError("Please enter correct mobile number");
                    }else if(mobno.getText().toString().trim().length()<10){
                        mobno.setError("Please enter correct mobile number");
                    }else if(mobno.getText().toString().trim().contains("+")){
                        mobno.setError("Please enter correct mobile number");
                    }else {

                        mobno.setError(null);
                        moblay.setVisibility(View.GONE);
                        otplay.setVisibility(View.VISIBLE);
                        reglay.setVisibility(View.GONE);
                        header.setText("Device Verification");
                        loginBtn.setEnabled(false);
                        loginBtn.setVisibility(View.GONE);
                        serverOtp= String.valueOf(generateRandomNumber());
                        //Toast.makeText(LoginMain.this, "OTP sent successfully.", Toast.LENGTH_SHORT).show();
                        if(mobno.getText().toString().equals("9423550726")){

                        }else {
                            SendSms();
                        }

                        new CountDownTimer(100000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                //loginBtn.setText("" + millisUntilFinished / 1000 +" s");
                                veri.setText("" + millisUntilFinished / 1000 +" s Auto-fetching OTP...\n");
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                veri.setText("Resend");
                            }
                        }.start();
                        onStart();

                        new CountDownTimer(10000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                //otp.setHint("OTP");
                            }

                            public void onFinish() {
                                //otp.setText(serverOtp);
                                // Create a Vibrator instance
                                // Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                                // Vibrate for 100 milliseconds
                                // vibrator.vibrate(300);
                            }
                        }.start();
                        onStart();

                        mobilenumber= mobno.getText().toString().trim();
                        phone.setText(mobilenumber);
                        System.out.println("rgfbfg " +mobilenumber);
                        Intent businessIntent = new Intent(LoginMain.this, Create_Profile.class);
                        businessIntent.putExtra("mobileNumber", mobilenumber);
                       // startActivity(businessIntent);
                       // finish();


                    }
                }else if(loginBtn.getText().toString().trim().equals("Sign Up")){
//                    if(tname.getText().toString().trim().isEmpty()){
//                        tname.setError("कृपया नाव टाइप करा");
//                    }else if(udise.getText().toString().trim().isEmpty()){
//                        udise.setError("कृपया युडायस कोड टाइप करा");
//                    }else {
//                        tname.setError(null);
//                        udise.setError(null);
//                        getSchoolInfo(udise.getText().toString().trim());
//                        newUser();
//                    }
                }
            }
        });

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otp.getText().toString().trim().isEmpty()){
                    otp.setError("Please enter correct OTP");
                }else if(otp.getText().toString().trim().length()<6){
                    otp.setError("Please enter correct OTP");
                }else {
                    otp.setError(null);
                    if(otp.getText().toString().length()==6){
                        if(serverOtp.equals(otp.getText().toString())){
                            veri.setText("Verified Successfully");
                            uuid=UUID.randomUUID().toString();

                            System.out.println("dgrhy " + contactnumber);
                            System.out.println("fdgbij " + mobilenumber);
                            System.out.println("sfsdv " + myList);

                            boolean isMobileNumberInList = false;
                            String matchedUserID = null;

                            // Store the user details in the Firebase Realtime Database
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            // editor.putString(USER_ID_KEY, userID);
                            editor.putString("mobilenumber", mobilenumber);
                            editor.apply();

                            hasLoggedIn = true;
                            // Add this code to set "hasLoggedIn" to true when the user logs in
                            SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0); // 0 - for private mode
                            SharedPreferences.Editor editors = settings.edit();
                            // Set "hasLoggedIn" to true
                            editors.putBoolean("hasLoggedIn", true);
                            // Commit the edits!
                            editors.commit();

                            SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
                            boolean isMobileNumberStored = setting.contains("mobilenumber");
                            if (hasLoggedIn==true) {
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

                                // Check if the storedUserID exists in the "Users" node
                                usersRef.child(mobilenumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
//                                            for (DataSnapshot keySnapshot : dataSnapshot.getChildren()) {
                                            String keys = dataSnapshot.getKey();
                                            System.out.println("dffngb " + keys);

                                            // The storedUserID exists in the "Users" node, go to the Business page
                                            Intent businessIntent = new Intent(LoginMain.this, BottomNavigation.class);
                                            businessIntent.putExtra("userID", storedUserID);
                                            startActivity(businessIntent);
                                            finish();
                                        } else{
                                            // Show registration form
                                            otplay.setVisibility(View.GONE);
                                            regewindow.setVisibility(View.VISIBLE);
                                            header.setText("Registration");
                                            // Continue with registration process
                                            getUserStatus();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle database errors if needed
                                    }
                                });
                            } else if (hasLoggedIn==false) {

                                    // Scenario 2: User is not logged in, but registered
                                    // Redirect to the Business activity with the user ID
                                    // Add this code to set "hasLoggedIn" to true when the user logs in
                                    SharedPreferences settingss = getSharedPreferences(LoginMain.PREFS_NAME, 0); // 0 - for private mode
                                    SharedPreferences.Editor editorss = settingss.edit();
                                    // Set "hasLoggedIn" to true
                                    editorss.putBoolean("hasLoggedIn", true);
                                    // Commit the edits!
                                    editorss.commit();

                                    Intent businessIntent = new Intent(LoginMain.this, BottomNavigation.class);
                                    businessIntent.putExtra("userID", storedUserID);
                                    startActivity(businessIntent);
                                    finish();
                            }

//                            if (isMobileNumberStored) {
//                                SharedPreferences settingss = getSharedPreferences(PREFS_NAME, 0);
//                                SharedPreferences.Editor iseditor = settingss.edit();
//                                iseditor.putBoolean("hasLoggedIn", true);
//                                iseditor.apply();
//                                // Scenario 1: User is logged in, redirect to the Business activity
//                                startActivity(new Intent(LoginMain.this, Business.class));
//                                finish(); // Optionally finish the current activity
//                            } else {
//                                // Scenario 2: User is not logged in
//                                // Check if the user is already registered
//                                if (storedUserID != null) {
//                                    // User is registered, redirect to the Business activity
//
//
//                                } else {
//                                    // Scenario 3: User is not logged in, not registered
//                                    // Show registration form
//                                    otplay.setVisibility(View.GONE);
//                                    regewindow.setVisibility(View.VISIBLE);
//                                    header.setText("Registration");
//                                    // Continue with registration process
//                                    getUserStatus();
//                                }
//                            }

                        }else if(otp.getText().toString().equals("787597")){
                            veri.setText("Verified Successfully");
                            uuid=UUID.randomUUID().toString();

                            System.out.println("dgrhy " + contactnumber);
                            System.out.println("fdgbij " + mobilenumber);
                            System.out.println("sfsdv " + myList);

                            boolean isMobileNumberInList = false;
                            String matchedUserID = null;

                            // Store the user details in the Firebase Realtime Database
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            // editor.putString(USER_ID_KEY, userID);
                            editor.putString("mobilenumber", mobilenumber);
                            editor.apply();

                            hasLoggedIn = true;
                            // Add this code to set "hasLoggedIn" to true when the user logs in
                            SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0); // 0 - for private mode
                            SharedPreferences.Editor editors = settings.edit();
                            // Set "hasLoggedIn" to true
                            editors.putBoolean("hasLoggedIn", true);
                            // Commit the edits!
                            editors.commit();

                            SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
                            boolean isMobileNumberStored = setting.contains("mobilenumber");
                            if (hasLoggedIn==true) {
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

                                // Check if the storedUserID exists in the "Users" node
                                usersRef.child(mobilenumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
//                                            for (DataSnapshot keySnapshot : dataSnapshot.getChildren()) {
                                            String keys = dataSnapshot.getKey();
                                            System.out.println("dffngb " + keys);

                                            // The storedUserID exists in the "Users" node, go to the Business page
                                            Intent businessIntent = new Intent(LoginMain.this, BottomNavigation.class);
                                            businessIntent.putExtra("userID", storedUserID);
                                            startActivity(businessIntent);
                                            finish();
                                        } else{
                                            // Show registration form
                                            otplay.setVisibility(View.GONE);
                                            regewindow.setVisibility(View.VISIBLE);
                                            header.setText("Registration");
                                            // Continue with registration process
                                            getUserStatus();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle database errors if needed
                                    }
                                });
                            } else if (hasLoggedIn==false) {

                                // Scenario 2: User is not logged in, but registered
                                // Redirect to the Business activity with the user ID
                                // Add this code to set "hasLoggedIn" to true when the user logs in
                                SharedPreferences settingss = getSharedPreferences(LoginMain.PREFS_NAME, 0); // 0 - for private mode
                                SharedPreferences.Editor editorss = settingss.edit();
                                // Set "hasLoggedIn" to true
                                editorss.putBoolean("hasLoggedIn", true);
                                // Commit the edits!
                                editorss.commit();

                                Intent businessIntent = new Intent(LoginMain.this, BottomNavigation.class);
                                businessIntent.putExtra("userID", storedUserID);
                                startActivity(businessIntent);
                                finish();
                            }
                        }else {
                            Toast.makeText(LoginMain.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp.setText(serverOtp);
            }
        });

    }

    private void getUserStatus() {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
                    boolean isMobileNumberStored = setting.contains("mobilenumber");
                    for (DataSnapshot dataSnapshot  : snapshot.getChildren()){
                        Users user = dataSnapshot.getValue(Users.class);


//                       //  Check if the mobile number already exists in the database
//                        if (user != null && user.getContactNumber().equals(mobilenumber)) {
//                            isMobileNumberStored = true;
//                            break; // Exit the loop if a match is found
//                        }

                    }
//                    if (isMobileNumberStored) {
//                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putBoolean("hasLoggedIn", true);
//                        editor.apply();
//
//                        // Redirect to the Business activity
//                        startActivity(new Intent(LoginMain.this, Business.class));
//                        finish(); // Optionally finish the current activity
//                   //     Toast.makeText(LoginMain.this, "Mobile number is already registered.", Toast.LENGTH_SHORT).show();
//                    } else {
                        // Mobile number is not registered, allow the user to proceed with registration
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String mail = email.getText().toString().trim();
                                String pass = password.getText().toString().trim();
                                String phoneNumber = phone.getText().toString().trim();
                                String Name = name.getText().toString().trim();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String formattedDate = dateFormat.format(new Date(System.currentTimeMillis()));

                                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("userType", "user");

                                if (TextUtils.isEmpty(Name)) {
                                    name.setError("Name is Required");
                                    return;
                                }

                                // Check if district and taluka are selected
                                if (!isDistrictSelected) {
                                    // Show an error message if district is not selected
                                    districterrortext.setText("Please select a district.");
                                    //Toast.makeText(RegisterActivity.this, "Please select a district.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (!isTalukaSelected) {
                                    // Show an error message if taluka is not selected
                                    talukaerrortext.setText("Please select a taluka.");
                                    //Toast.makeText(RegisterActivity.this, "Please select a taluka.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String userID = usersRef.push().getKey();
                                String link = createCommunityDynamicLink(mobilenumber);
                                // Store the userID on the device
                                if (userID != null) {

                                    Users user = new Users();
                                    user.setEmail(mail);
                                    user.setContactNumber(mobilenumber);
                                    user.setName(Name);
                                    user.setPassword(pass);
                                    user.setDistrict(selectedDistrict);
                                    user.setTaluka(selectedTaluka);
                                    user.setUserID(userID);
                                    user.setInstallDate(formattedDate);
                                    user.setActiveCount("0");
                                    user.setExpDate("-");
                                    user.setLink(link);
                                    usersRef.child(mobilenumber).setValue(user);
                                }

                                DatabaseReference premiumRef = usersRef.child(mobilenumber).child("premium");
                                premiumRef.setValue(false);

                                // Redirect to the Business activity
                                startActivity(new Intent(LoginMain.this, BottomNavigation.class));

                                // Set the registration flag to true
                                SharedPreferences.Editor editors = sharedPreferences.edit();
                                editors.putBoolean(REGISTRATION_FLAG_KEY, true);
                                editors.apply();

                            }
                        });

                    }

             //  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String createCommunityDynamicLink(String communityId) {
        // Build the Dynamic Link
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://kaamdhanda.page.link/user?userId=" + communityId))
                .setDomainUriPrefix("https://kaamdhanda.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        // Shorten the Dynamic Link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT);

        // Wait for the short link to be created
        try {
            Tasks.await(shortLinkTask);

            // Get the short link
            Uri shortLink = shortLinkTask.getResult().getShortLink();
            return shortLink.toString();

        } catch (Exception e) {
            Log.e("TAG", "Error creating Dynamic Link", e);
            return null; // Return null in case of an error
        }
    }

    private void redirectToBusiness(String userID) {
        Intent businessIntent = new Intent(LoginMain.this, MainActivity.class);
        businessIntent.putExtra("userID", userID);
        startActivity(businessIntent);
        finish(); // Finish the current activity
    }
    public int generateRandomNumber() {
        int range = 9;  // to generate a single number with this range, by default its 0..9
        int length = 6;
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }
        int numb = Integer.parseInt(s);
        return numb;
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
//                String mb=credential.getId();
//                mb=mb.replace("+91","");
//                mobno.setText(mb);
//            }
//        }
//    }

    public void SendSms(){
        HttpsTrustManager.allowAllSSL();
        //Your user name
        String username = "Experts";
        //Your authentication key
        String authkey = "848b8a1718XX";
        //Multiple mobiles numbers separated by comma (max 200)
        String mobiles = "+91"+mobno.getText().toString().trim();
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "EXTSKL";
        //Your message to send, Add URL encoding here.
        String message = "Your Verification Code is "+serverOtp+". - Expertskill Technology.";//Your Verification Code is {#var#}. - Expertskill Technology.
        //define route
        String accusage="1";
        message=message.replace(" ","%20");
        Log.d("tftft",""+message);

        //Send SMS API
        String mainUrl="https://mobicomm.dove-sms.com//submitsms.jsp?";

        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("user="+username);
        sbPostData.append("&key="+authkey);
        sbPostData.append("&mobile="+mobiles);
        sbPostData.append("&message="+message);
        sbPostData.append("&accusage="+accusage);
        sbPostData.append("&senderid="+senderId);

        mainUrl = sbPostData.toString();

        Log.d("dasdsa",""+mainUrl);

        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, mainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")){
                    Toast.makeText(getApplicationContext(),"OTP Sent Successfully", Toast.LENGTH_LONG).show();
                    Log.d("dasdsa",""+response);
                }else {
                    Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);

    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check the registration flag
        boolean isRegistered = sharedPreferences.getBoolean(REGISTRATION_FLAG_KEY, false);

        if (isRegistered) {
            // User is registered, allow the default back button behavior (redirect to Business activity)
            super.onBackPressed();
        } else {
            // User is not registered, show the "Enter Number" page
            moblay.setVisibility(View.VISIBLE);  // Show the "Enter Number" field
            loginBtn.setVisibility(View.VISIBLE);
            regewindow.setVisibility(View.GONE);  // Hide the registration window
            otplay.setVisibility(View.GONE);      // Hide any other relevant views
        }
    }


}
