package com.example.loginsignup;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    private FirebaseAuth auth;
    public static final String TAG = "MyTag";
    //..............................................recycleview................................................
    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    SearchView searchView;
    FirebaseAuth fAuth;
    String userID;
    FirebaseDatabase database;
    //..........................................recycleview.........................................
//@BindViews()
  //  FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener authListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = fAuth.getCurrentUser().getUid();
        Log.i("userID", String.valueOf(userID));
        //....................................................FETCHFCM.................................................
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                Log.i("fcm", String.valueOf(token));
            }
        });
        //....................................................FETCHFCM.................................................


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                    finish();
                }
            }
        };
//        auth = FirebaseAuth.getInstance();
//        userID = fAuth.getCurrentUser().getUid();
//        Log.i("userID", String.valueOf(userID));
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged( FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(com.example.loginsignup.MainActivity.this, SignUpActivity.class));
//                    finish();
//                }
//            }
//
//        };

        logout = findViewById(R.id.logout);
      ///  userName = findViewById(R.id.userName);
        //.........................................recycleview.....................................................
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();


        //............................................recycleview.................................................






//.......................................................BELOW MAIN CODE...........................................................
//...................................................................................................................................


//        userID = fAuth.getCurrentUser().getUid();
//        Log.i("userID", String.valueOf(userID));
        // MyAdapter adapter;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);
        //............................................GET UID.......................................................
//        userID = fAuth.getCurrentUser().getUid();
//        Log.i("userID", String.valueOf(userID));
        //  "ZATKA_MACHINE/"+userID
        //.............................................GET UID.......................................................
        //............................................................projectpath...................................................................
        String projectPathString = getResources().getString(R.string.Project_Path);
        databaseReference = FirebaseDatabase.getInstance().getReference(projectPathString+userID);
        //............................................................projectpath...................................................................

//        databaseReference = FirebaseDatabase.getInstance().getReference("ZATKA_MACHINE/"+userID);
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }

        });








//...................................................................................................................................

//.......................................................BELOW MAIN CODE...........................................................




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();
            }
        });


//
    }
    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass : dataList) {
            if (dataClass.getDataDname().toLowerCase().contains(text.toLowerCase())) { //change//3
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    public void signOut() {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
