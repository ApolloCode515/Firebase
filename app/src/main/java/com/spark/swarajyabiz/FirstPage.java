package com.spark.swarajyabiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class FirstPage extends AppCompatActivity {

    Button Login, Registration;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        Login = findViewById(R.id.Login);
        Registration = findViewById(R.id.ShopOwnerLogin);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            // User is already logged in, redirect to Business class
            startActivity(new Intent(FirstPage.this, Business.class));
            finish();
        }

        if (firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),Business.class));
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActvity.class));
            }
        });

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Create_Profile.class));
            }

        });
    }
}