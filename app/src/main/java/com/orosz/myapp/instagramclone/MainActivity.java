package com.orosz.myapp.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orosz.myapp.instagramclone.Common.Common;
import com.orosz.myapp.instagramclone.Model.User;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

    Button btnSignIn, btnSignUp;
    EditText editPhone, editName, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = (Button) findViewById(R.id.btnSingUp);
        btnSignIn = (Button) findViewById(R.id.btnSingIn);

        editPhone = (EditText) findViewById(R.id.editPhone);
        editName = (EditText) findViewById(R.id.editName);
        editPassword = (EditText) findViewById(R.id.editPassword);

        //Init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        editPhone.setOnKeyListener(this);
        editPassword.setOnKeyListener(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Please wait..");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check if user does exist in the data base
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {

                            mDialog.dismiss();

                            //Get User information
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);

                            if (user.getPassword().equals(editPassword.getText().toString())) {

                                Toast.makeText(MainActivity.this, "Sign in successfully !", Toast.LENGTH_LONG).show();

                                //Intent homeIntent = new Intent(SignIn.this, Home.class);
                                //Common.currentUser = user;
                                //startActivity(homeIntent);
                                //finish();

                            } else {

                                mDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Wrong Password !", Toast.LENGTH_LONG).show();

                            }

                        } else {

                            Toast.makeText(MainActivity.this, "User not registered !", Toast.LENGTH_LONG).show();

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Please wait..");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if already user exist
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {

                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Phone Number already registered !", Toast.LENGTH_LONG).show();

                        } else {

                            mDialog.dismiss();
                            User user =new User(editName.getText().toString(),
                                    editPassword.getText().toString());
                            table_user.child(editPhone.getText().toString()).setValue(user);
                            Toast.makeText(MainActivity.this, "Sign Up successfully!", Toast.LENGTH_LONG).show();
                            //finish();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {



        }
        return false;
    }
}
