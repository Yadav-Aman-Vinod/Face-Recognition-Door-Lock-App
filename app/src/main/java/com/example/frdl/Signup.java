package com.example.frdl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private Button button;
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword, regPin;
    Button regBtn, regToLoginBtn;

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

    private final Boolean validateUsername(){
        String val =regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            regUsername.requestFocus();

            return false;
        }
        else if (val.length()>=15){
            regUsername.setError("Username too long");
            regUsername.requestFocus();

            return false;
        }
        else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("White Spaces are not allowed");
            regUsername.requestFocus();

            return false;
        }
        else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
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




    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        button = (Button) findViewById(R.id.reg_login_btn);
        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regPin = findViewById(R.id.reg_pin);
        regToLoginBtn = findViewById(R.id.reg_login_btn);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });






        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                ref = rootNode.getReference("user");

                if (!validateName() | !validatePassword() | !validatePhoneNo() | !validateUsername() | !validatePin() | !validateEmail() ){
                    return;
                }

                String name =regName.getEditText().getText().toString();
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String pin = regPin.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(name, username ,email,phoneNo,password,pin);
                helpp help = new helpp(name, username ,email,phoneNo,password,pin);





                reference.child(username).setValue(helperClass);
                ref.child(pin).setValue(help);

                openLogin();


            }
        });






    }

    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        Toast.makeText(this, "Registration Successful, Login with Id created", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    public void login(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}