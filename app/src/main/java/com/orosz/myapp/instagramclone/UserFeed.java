package com.orosz.myapp.instagramclone;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.orosz.myapp.instagramclone.Model.ImageLink;
import com.orosz.myapp.instagramclone.Model.User;

import java.util.ArrayList;

import static com.orosz.myapp.instagramclone.Common.Common.currentUser;

public class UserFeed extends AppCompatActivity {

    private StorageReference storageRef;
    ArrayList<String> imageStorageUrls = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference table_urls;

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String activeUsername = i.getStringExtra("username");
        String activeUserPhoneNum = i.getStringExtra("userPhone");

        //Log.i("AppInfo", activeUsername);
        setTitle(activeUsername + "'s Feed");
        imageStorageUrls.clear();

        //download images...


        //show images
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        //in download for...
        //Init firebase
        database = FirebaseDatabase.getInstance();
        table_urls = database.getReference("Urls/");

        table_urls.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageStorageUrls.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    ImageLink imageLink = postSnapshot.getValue(ImageLink.class);

                    if (!imageLink.getUserID().equals(currentUser.getPhone())) {
                        imageStorageUrls.add(imageLink.getUrl());
                    }
                }

                for (int index = 0; index < imageStorageUrls.size(); index++) {
                    //code for pictures
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_user_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.postPictureItem){

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);

        }

        if (id == R.id.logOut){

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            currentUser.setPhone("");
            currentUser.setName("");
            currentUser.setPassword("");
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
