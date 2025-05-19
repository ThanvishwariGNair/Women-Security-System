package com.example.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    String userID;


    DatabaseReference databaseReference1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();


//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//            finish();
//        }

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                if (user.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //........................................................................................................
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                userID = fAuth.getCurrentUser().getUid();
                                Log.i("userID", String.valueOf(userID));
//                                String projectPathString = getResources().getString(R.string.Project_Path);
//                                databaseReference1= FirebaseDatabase.getInstance().getReference(projectPathString+"/A_USERDATA/"+userID+"/"+pass);
//                                //databaseReference1= FirebaseDatabase.getInstance().getReference("ZATKA_MACHINE/A_USERDATA/"+userID+"/"+pass);
//                                databaseReference1.setValue(user);
                                //........................................................................................................

                                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}