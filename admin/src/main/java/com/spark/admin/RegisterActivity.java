package com.spark.admin;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.rxjava3.annotations.NonNull;

// ...


// ...

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, phone, password, taluka;
    Spinner districtSpinner;
    Button register;
    TextView textlogin, errortextview, districterrortext, talukaerrortext;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersRef;

    private Spinner TalukaSpinner;
    private ArrayAdapter<String> districtAdapter;
    private ArrayAdapter<String> subDivisionAdapter;
    private String selectedDistrict;
    private String selectedTaluka;
    private boolean isDistrictSelected = false;
    private boolean isTalukaSelected = false;




    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//        name = findViewById(R.id.usernm);
//        email = findViewById(R.id.useremail);
//        password = findViewById(R.id.userpass);
//        phone = findViewById(R.id.mobilenum);
//        register = findViewById(R.id.loginfox);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        usersRef = FirebaseDatabase.getInstance().getReference("Users");
//
//        if (firebaseAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), Business.class));
//            finish();
//        }
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String Password = password.getText().toString().trim();
//                String mail = email.getText().toString().trim();
//                String phoneNumber = phone.getText().toString().trim();
//                String Name = name.getText().toString().trim();
//
//                if(register.getText().toString().trim().equals("Continue")){
//                    if(phone.getText().toString().trim().isEmpty()){
//                        phone.setError("Enter correct mobile number");
//                    }else if(phone.getText().toString().trim().length()<10){
//                        phone.setError("Enter correct mobile number");
//                    }else if(phone.getText().toString().trim().contains("+")) {
//                        phone.setError("Enter correct mobile number");
//                    }else if (name.getText().toString().trim().isEmpty()){
//                        name.setError("Please enter your name");
//                    }else if(password.getText().toString().trim().isEmpty()){
//                        password.setError("Enter Password");
//                    }else if(password.getText().toString().trim().length()>6) {
//                        password.setError("Please enter max 6 digit character");
//                    }else if (email.getText().toString().trim().isEmpty()) {
//                        email.setError("Please enter your email");
//                    }else {
//                        name.setError(null);
//                        phone.setError(null);
//                        password.setError(null);
//                        email.setError(null);
//                    }
//                }
//
//                firebaseAuth.createUserWithEmailAndPassword(mail, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if (task.isSuccessful()) {
//                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                            DatabaseReference userRef = usersRef.child(uid);
//                            // Store user information in the Realtime Database using contact number as the key
//                            // DatabaseReference userRef = usersRef.child(phoneNumber);
//                            userRef.child("email").setValue(mail);
//                            userRef.child("contactNumber").setValue(phoneNumber);
//                            userRef.child("Name").setValue(Name);
//                            userRef.child("UserID").setValue(uid);
//                            Toast.makeText(RegisterActivity.this, "User Account Created", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), Business.class));
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });

//        textlogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), LoginActvity.class));
//            }
//        });

        name = findViewById(R.id.editname);
        email = findViewById(R.id.editemail);
        password = findViewById(R.id.editpassword);
        phone = findViewById(R.id.editphone);
        //district = findViewById(R.id.district);
        //taluka = findViewById(R.id.taluka);
        errortextview = findViewById(R.id.errortextview);
        districterrortext = findViewById(R.id.districterrortext);
        talukaerrortext = findViewById(R.id.talukaerrortext);
        register = findViewById(R.id.btnregister);
        textlogin = findViewById(R.id.textlogin);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Business.class));
            finish();
        }

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
                    subDivisionAdapter = new ArrayAdapter<>(RegisterActivity.this,
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String phoneNumber = phone.getText().toString().trim();
                String Name = name.getText().toString().trim();


                if (TextUtils.isEmpty(Name)) {
                    name.setError("Name is Required");
                    return;
                }

                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email Required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    phone.setError("Contact Number Required");
                    return;
                }

                if (phoneNumber.length() != 10) {
                    phone.setError("Contact Number must be 10 digits");
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

                if (TextUtils.isEmpty(pass)) {
                    errortextview.setText("Password Required");
                    errortextview.setVisibility(View.VISIBLE);
                    return;
                }
                if (pass.length() < 6) {
                    errortextview.setText("Password must contain 6 characters");
                    errortextview.setVisibility(View.VISIBLE);
                    return;
                }
                // Clear the error message and hide it if no errors occurred
                errortextview.setText("");
                errortextview.setVisibility(View.GONE);

                firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

//                            // Send email verification link
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            if (user != null) {
//                                user.sendEmailVerification()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> verificationTask) {
//                                                if (verificationTask.isSuccessful()) {
//                                                    // Email verification link sent successfully
//                                                    Toast.makeText(RegisterActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    // Failed to send verification email
//                                                    Toast.makeText(RegisterActivity.this, "Failed to send verification email: " + verificationTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            }


                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = usersRef.child(uid);
                            // Store user information in the Realtime Database using contact number as the key
                            // DatabaseReference userRef = usersRef.child(phoneNumber);
                            userRef.child("email").setValue(mail);
                            userRef.child("contactNumber").setValue(phoneNumber);
                            userRef.child("Name").setValue(Name);
                            userRef.child("password").setValue(pass);
                            userRef.child("UserID").setValue(uid);
                            // Save selected district and taluka
                            userRef.child("district").setValue(selectedDistrict);
                            userRef.child("taluka").setValue(selectedTaluka);

//                            // Add this code to set "hasLoggedIn" to true when the user logs in
//                            SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0); // 0 - for private mode
//                            SharedPreferences.Editor editor = settings.edit();
//                            // Set "hasLoggedIn" to true
//                            editor.putBoolean("hasLoggedIn", true);
//                            // Commit the edits!
//                            editor.commit();


                            Toast.makeText(RegisterActivity.this, "User Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActvity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // If the email is already registered, show a dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("Registration Failed");
                                builder.setMessage("You have already registered.");
                                builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(getApplicationContext(), LoginActvity.class));
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Cancel", null);
                                builder.show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        textlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActvity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", false);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), LoginMain.class));
        finish();
    }

}

/*
String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = usersRef.child(phoneNumber);
                            // Store user information in the Realtime Database using contact number as the key
                           // DatabaseReference userRef = usersRef.child(phoneNumber);
                            userRef.child("email").setValue(mail);
                            userRef.child("contactNumber").setValue(phoneNumber);
                            userRef.child("Name").setValue(Name);
                            //userRef.child("Password").setValue(password);
                            userRef.child("UserID").setValue(uid);

                            Toast.makeText(RegisterActivity.this, "User Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Business.class));
 */