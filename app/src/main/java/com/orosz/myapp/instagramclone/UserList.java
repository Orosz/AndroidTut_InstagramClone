package com.orosz.myapp.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orosz.myapp.instagramclone.Model.User;

import java.util.ArrayList;

import static com.orosz.myapp.instagramclone.Common.Common.currentUser;

public class UserList extends AppCompatActivity {

    ListView userListView;
    ArrayList<String> usersArray = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userListView = (ListView) findViewById(R.id.userList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usersArray);

        //Init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        //Get all users and add them to the arrayList -> Without the current user
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersArray.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);


                    if (!user.getName().equals(currentUser.getName())) {
                        usersArray.add(user.getName());
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
}
