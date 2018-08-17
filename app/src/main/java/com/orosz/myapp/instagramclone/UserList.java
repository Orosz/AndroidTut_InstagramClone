package com.orosz.myapp.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orosz.myapp.instagramclone.Model.ImageLink;
import com.orosz.myapp.instagramclone.Model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.orosz.myapp.instagramclone.Common.Common.currentUser;

public class UserList extends AppCompatActivity {

    ListView userListView;
    ArrayList<User> usersArray = new ArrayList<>();
    ArrayAdapter<User> arrayAdapter;

    FirebaseDatabase database;
    DatabaseReference table_user;
    DatabaseReference table_urls;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userListView = (ListView) findViewById(R.id.userList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usersArray);

        setTitle(currentUser.getName() +" - User List");

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getApplicationContext(), UserFeed.class);
                intent.putExtra("username", usersArray.get(position).toString());
                intent.putExtra("userPhone", usersArray.get(position).getPhone());
                startActivity(intent);

            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Init firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");
        table_urls = database.getReference("Urls");

        //Get all users and add them to the arrayList -> Without the current user
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersArray.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);


                    if (!user.getName().equals(currentUser.getName())) {
                        usersArray.add(user);
                    }


                    userListView.setAdapter(arrayAdapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Cannot reach data", Toast.LENGTH_LONG).show();

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

    protected String currentDate() {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

       // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            final ProgressDialog mDialog = new ProgressDialog(UserList.this);
            mDialog.setMessage("Please wait..");
            mDialog.show();

            Uri selectedImage = data.getData();
            StorageReference picReference = mStorageRef.child(String.valueOf("images/"
                    + currentUser.getPhone()+ "/" + selectedImage.getLastPathSegment()));

            final String picID = currentUser.getPhone() + "_" +selectedImage.getLastPathSegment();

            picReference.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            //save url to database for retrieving images
                            ImageLink imageLink = new ImageLink(currentUser.getPhone(), downloadUrl.toString(), currentDate().trim());
                            table_urls.child(picID).setValue(imageLink);

                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), downloadUrl.toString(), Toast.LENGTH_LONG).show();
                            //Log.i("AppInfoUrlsImg", downloadUrl.toString());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload failed - Please try again!", Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }
}
