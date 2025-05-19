package com.example.loginsignup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;



public class UploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton;
    EditText uploadDname, uploadDid, uploadLang;
    String imageURL;
    Uri uri;
    FirebaseAuth fAuth;
    String userID;
    Button btn_scanQR; ///////QRSCAN
  //  String lang = "-";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
//        auth = FirebaseAuth.getInstance();
//        database=FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = fAuth.getCurrentUser().getUid();
        Log.i("userID", String.valueOf(userID));


        uploadImage = findViewById(R.id.uploadImage);
        uploadDid = findViewById(R.id.uploadDid);
        uploadDname = findViewById(R.id.uploadDname);
        uploadLang = findViewById(R.id.uploadLang);
        saveButton = findViewById(R.id.saveButton);
        ///......................................................SCANQR...................................................
        btn_scanQR =findViewById(R.id.scanQR);
        btn_scanQR.setOnClickListener(v->
        {
            scanCode();
        });

        ///......................................................SCANQR...................................................

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );




        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
//.................................................scanCode QR.................................................
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setTitle("Device ID");
            builder.setMessage(result.getContents());
           // Did = result.getContents();
            uploadDid.setText(String.valueOf(result.getContents()));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
//.................................................scanCode QR.................................................

    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadData(){
        String Dname = uploadDname.getText().toString();  //changge3
       /// String title1 = uploadDname.getText().toString(); //change
        String Did = uploadDid.getText().toString();   //change2    //String
        String lang = uploadLang.getText().toString(); //String
        DataClass dataClass = new DataClass(Dname, Did, lang, imageURL); // title1,
        //We are changing the child from title to currentDate,
        // because we will be updating title as well and it may affect child value.
        //............................................................projectpath...................................................................
        String projectPathString = getResources().getString(R.string.Project_Path);
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        FirebaseDatabase.getInstance().getReference(projectPathString+userID).child(currentDate)
       // FirebaseDatabase.getInstance().getReference("ZATKA_MACHINE/"+userID).child(currentDate)
       // FirebaseDatabase.getInstance().getReference("FinalYear23_24Project/"+userID).child(currentDate)
       //............................................................projectpath...................................................................
        //ZATKA_MACHINE
                // "@string/esptouch1_package_multicast"
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}