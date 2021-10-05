package com.example.frdl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button button, button2 ;
    TextInputLayout username, password;


    private final Boolean validateUsername(){
        String val =username.getEditText().getText().toString();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }

        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    private final Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.gobutton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( !validatePassword()  | !validateUsername() ){
                    return;
                }
                else {
                    isUser();
                }

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });



    }

    private void isUser(){

        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)){

                        username.setError(null);
                        username.setErrorEnabled(false);


                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String pinFromDB = dataSnapshot.child(userEnteredUsername).child("pin").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), userprofile.class);

                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("username",usernameFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("phoneNo",phoneNoFromDB);
                        intent.putExtra("pin",pinFromDB);
                        intent.putExtra("password",passwordFromDB);

                        startActivity(intent);

                    }

                    else{
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }

                else{
                    username.setError("No such User exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void openSignUp(){
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
        finish();
    }


}