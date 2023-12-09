package com.spark.admin;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import io.reactivex.rxjava3.annotations.NonNull;

public class LoginActvity extends AppCompatActivity {

    EditText editemail, editpassword;
    Button login;
    TextView textregister, errortextview;
    FirebaseAuth firebaseAuth;

    TextView forgetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editemail = findViewById(R.id.editEmail);
        editpassword = findViewById(R.id.editPassword);
        login = findViewById(R.id.btnlogin);
        textregister = findViewById(R.id.textregister);
        errortextview = findViewById(R.id.errortextview);
        forgetpass = findViewById(R.id.forgetpass);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            // User is already logged in, redirect to Business class
            startActivity(new Intent(LoginActvity.this, Business.class));
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail, pass;

                mail = editemail.getText().toString().trim();
                pass = editpassword.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    editemail.setError("Email Required");
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

                firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActvity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActvity.class));
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            if (user != null && user.isEmailVerified()) {
//                                // User is logged in and email is verified
//                                Toast.makeText(LoginActvity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), Business.class));
//                            } else {
//                                // Email is not verified
//                                Toast.makeText(LoginActvity.this, "Email is not verified. Please check your inbox.", Toast.LENGTH_SHORT).show();
//                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                // If the email is already registered, show a dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActvity.this);
                                builder.setTitle("Login Failed");
                                builder.setMessage("You have not registered.");
                                builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Cancel", null);
                                builder.show();
                            } else {
                                Toast.makeText(LoginActvity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }

        });


        textregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editemail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }


                firebaseAuth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActvity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActvity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

}