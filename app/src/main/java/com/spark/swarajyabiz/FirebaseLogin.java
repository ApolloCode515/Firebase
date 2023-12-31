package com.spark.swarajyabiz;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class FirebaseLogin extends AppCompatActivity {

    Button _btnLogin, _btnVerOTP;
    EditText _txtName, _txtPhone, _txtVerOTP;
    int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        _txtName = (EditText) findViewById(R.id.txtName);
        _txtPhone = (EditText) findViewById(R.id.txtPhone);
        _txtVerOTP = (EditText) findViewById(R.id.txtVerOTP);
        _btnLogin = (Button) findViewById(R.id.btnLogin);
        _btnVerOTP = (Button) findViewById(R.id.btnVerOTP);
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
// Construct data
                    String apiKey = "apikey=MzY2ZDRjNzI3NDU4Mzg2ZDYxNGE0OTU3NmU2ODVhMzM=";
                    Random random = new Random();
                    randomNumber = random.nextInt(999999);
                    String message = "&message=Hey " + _txtName.getText().toString() + ", Your OTP IS " + randomNumber;
                    String sender = "&sender=TSN";
                    String numbers = "&numbers=" + _txtPhone.getText().toString();

// Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                    String data = apiKey + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();
                    Toast.makeText(getApplicationContext(), "OTP SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();

//return stringBuffer.toString();
                } catch (Exception e) {
//System.out.println("Error SMS "+e);
///return "Error "+e;
                    Toast.makeText(getApplicationContext(), "ERROR SMS " + e, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "ERROR " + e, Toast.LENGTH_LONG).show();
                }
            }
        });

        _btnVerOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (randomNumber == Integer.valueOf(_txtVerOTP.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "WRONG OTP", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
