package com.example.frdl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class pin extends AppCompatActivity {

    Button pinbtn;
    TextInputLayout inputpin;
    DatabaseReference  data;

    private final Boolean validatePin(){
        String val =inputpin.getEditText().getText().toString();

        if(val.isEmpty()){
            inputpin.setError("Field cannot be empty");

            inputpin.requestFocus();
            return false;
        }

        else {

            inputpin.setError(null);
            return true;
        }

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin);
        pinbtn = findViewById(R.id.pintextbtn);
        inputpin = findViewById(R.id.pintext);

        data = FirebaseDatabase.getInstance().getReference();

        pinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !validatePin()){
                    return;
                }
                else {
                    po();
                     data.child("Value").setValue("true");

                }

            }
        });

    }
    private void po(){

        final String userEnteredpin = inputpin.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUser = reference.orderByChild("pin").equalTo(userEnteredpin);






        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    inputpin.setError(null);
                    inputpin.setErrorEnabled(false);

                    String pinFromDB = dataSnapshot.child(userEnteredpin).child("pin").getValue(String.class);

                    if (pinFromDB.equals(userEnteredpin)){


                        //door unlock code from raspberry pie
                        Toast.makeText(pin.this,"Door Unlocked",Toast.LENGTH_SHORT).show();

                    }

                }

                else{
                    inputpin.setError("Wrong Pin");
                    inputpin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}