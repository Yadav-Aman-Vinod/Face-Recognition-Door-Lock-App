package com.example.frdl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class userprofile extends AppCompatActivity {


    TextInputLayout fullName,email,phoneNo,pin,password;
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword, regPin;
    TextView fullNameLabel, usernameLabel;
    Button out,pincod;
    String _USERNAME, _NAME, _EMAIL, _PHONE, _PIN, _PASSWORD;
    DatabaseReference reference;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private final Boolean validateName(){
        String val =regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Field cannot be empty");

            regName.requestFocus();

            return false;
        }
        else {

            regName.setError(null);
            return true;
        }

    }

    private final Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            regEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            regEmail.setError("Invalid email address");
            regEmail.requestFocus();

            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }

    }

    private final Boolean validatePhoneNo(){
        String val = regPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            regPhoneNo.requestFocus();

            return false;
        }
        else if (val.length()<10) {
            regPhoneNo.setError("Phone Number is less than 10 digit");
            regPhoneNo.requestFocus();
            return false;
        }
        else if (val.length()>10) {
            regPhoneNo.setError("Phone Number is more than 10 digit");
            regPhoneNo.requestFocus();

            return false;
        }
        else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }

    }

    private final Boolean validatePin(){
        String val =regPin.getEditText().getText().toString();

        if(val.isEmpty()){
            regPin.setError("Field cannot be empty");

            regPin.requestFocus();
            return false;
        }
        else if (val.length()>4) {
            regPin.setError("Pin too long");
            regPin.requestFocus();

            return false;
        }
        else if (val.length()<4) {
            regPin.setError("Pin too short");
            regPin.requestFocus();

            return false;
        }
        else {

            regPin.setError(null);
            return true;
        }

    }

    private final Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            regPassword.requestFocus();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(val).matches()) {
            regPassword.setError("Password is too weak, try special characters");
            regPassword.requestFocus();
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_userprofile);



        reference = FirebaseDatabase.getInstance().getReference("users");


        regName = findViewById(R.id.full_name_profile);
        regEmail = findViewById(R.id.email_profile);
        regPhoneNo = findViewById(R.id.phone_no_profile);
        regPassword = findViewById(R.id.password_profile);
        regPin = findViewById(R.id.pin_profile);

        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_no_profile);
        pin = findViewById(R.id.pin_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.fullname_field);
        usernameLabel = findViewById(R.id.username_field);
        out = findViewById(R.id.logout);
        pincod = findViewById(R.id.pinco);


        pincod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pincoo();
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opening();
            }
        });

        showAllUserData();

    }

    public void opening(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void pincoo(){
        Intent intent = new Intent(this , pin.class);
        startActivity(intent);
    }

    private void showAllUserData() {

        Intent intent = getIntent();
        _USERNAME = intent.getStringExtra("username");
        _NAME = intent.getStringExtra("name");
        _EMAIL = intent.getStringExtra("email");
        _PHONE = intent.getStringExtra("phoneNo");
        _PIN = intent.getStringExtra("pin");
        _PASSWORD = intent.getStringExtra("password");


        fullNameLabel.setText(_NAME);
        usernameLabel.setText(_USERNAME);
        fullName.getEditText().setText(_NAME);
        email.getEditText().setText(_EMAIL);
        phoneNo.getEditText().setText(_PHONE);
        pin.getEditText().setText(_PIN);
        password.getEditText().setText(_PASSWORD);

    }

    public void update(View view){

        if (!validateName() | !validatePassword() | !validatePhoneNo()  | !validatePin() | !validateEmail()){
            Toast.makeText(this, "Error while updating", Toast.LENGTH_SHORT).show();
            return ;
        }

        else if (isDataChanged() ){

            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
        }


        else{
            Toast.makeText(this, "Data is same and cannot be changed", Toast.LENGTH_SHORT).show();

        }
    }


    private boolean isDataChanged(){

        isNameChanged();
        isPinChanged();
        isPhoneChanged();
        isEmailChanged();
        isPasswordChanged();
        return true;
    }

    private boolean isNameChanged(){
        if(!_NAME.equals(fullName.getEditText().getText().toString()))
        {
            reference.child(_USERNAME).child("name").setValue(fullName.getEditText().getText().toString());
            _NAME=fullName.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }

    private boolean isPhoneChanged() {
        if (!_PHONE.equals(phoneNo.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("phoneNo").setValue(phoneNo.getEditText().getText().toString());
            _PHONE = phoneNo.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }


    private boolean isEmailChanged() {
        if (!_EMAIL.equals(email.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("email").setValue(email.getEditText().getText().toString());
            _EMAIL = email.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }



    private boolean isPinChanged() {
        if (!_PIN.equals(pin.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("pin").setValue(pin.getEditText().getText().toString());
            _PIN = pin.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }


    private boolean isPasswordChanged() {
        if(!_PASSWORD.equals(password.getEditText().getText().toString()))
        {
            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD=password.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }















}