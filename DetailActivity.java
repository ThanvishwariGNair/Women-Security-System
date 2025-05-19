package com.example.loginsignup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.ekn.gruzer.gaugelibrary.contract.ValueFormatter;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ntt.customgaugeview.library.GaugeView;

import de.nitri.gauge.Gauge;

public class DetailActivity extends AppCompatActivity {


    TextView detailDid, detailTitle,detailDeviceID, detailLang;      //change2
    private Button MapRedirectButton;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton ,WIFICONFIG;
    EditText SPEEDLIMIT;
    String key = "";
    String imageUrl = "";
    String ttl= "";
    String IMAGE= "";
    String LANGUAGE= "";
    String title= "";
    String content= "";

    private Switch sw1;
    private FirebaseAuth auth;
    //    final Gauge gauge1ACCELERATION = findViewById(R.id.ACCELERATION );
    FirebaseDatabase database;
    FirebaseAuth fAuth;
    String userID;
    DatabaseReference databaseReference1,databasefeedback1,TEMPdetection,DataSPEEDLIMIT,DataSPEED,DataTILT,DataVIBRATION,DataAccidentAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = fAuth.getCurrentUser().getUid();
        Log.i("userID", String.valueOf(userID));

        //....................................................FETCH_PATH................................................
        String projectPathString = getResources().getString(R.string.Project_Path);
        String projectPathHardware = getResources().getString(R.string.Project_Path_Hardware);
        //....................................................FETCH_PATH................................................

        //....................................................FETCHFCM.................................................
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                databaseReference1=FirebaseDatabase.getInstance().getReference(projectPathHardware+ttl+"/fcmToken");
                databaseReference1.setValue(token);
                Log.i("fcm", String.valueOf(token));
            }
        });
        //....................................................FETCHFCM.................................................

        sw1 = (Switch)findViewById(R.id.switch1);
        MapRedirectButton = findViewById(R.id.location_button);
        SPEEDLIMIT = findViewById(R.id.speedlimit);    //change2
        /// detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDid = findViewById(R.id.detailDeviceID);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        WIFICONFIG = findViewById(R.id.WIFICONFIG);

        /// detailLang = findViewById(R.id.DetailLang);
        final HalfGauge gaugeTemp = findViewById(R.id.gaugetemp );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDid.setText(bundle.getString("Description"));  //Change2
            detailTitle.setText(bundle.getString("Title"));
            // detailDeviceID.setText(bundle.getString("Description"));  //change1
            // detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            ttl = bundle.getString("Description");
            imageUrl = bundle.getString("Image");
            LANGUAGE= bundle.getString("Language");
            // IMAGE   =bundle.getString("Image");
            //  Glide.with(this).load(bundle.getString("Image")).into(detailImage);

        }




        //................................................................................................................
        databasefeedback1=FirebaseDatabase.getInstance().getReference();
        databasefeedback1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                int val=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/SWITCH").getValue().toString());
                //  Log.i("val", String.valueOf(val));

                if(val==0){
                    sw1.setChecked(false);
                }else{
                    sw1.setChecked(true);
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (sw1.isChecked()){
                    //  Log.i("switch 1", String.valueOf(b));

                    databaseReference1=FirebaseDatabase.getInstance().getReference(projectPathHardware+ttl+"/SWITCH");
                    databaseReference1.setValue(1);
                }else{
                    databaseReference1=FirebaseDatabase.getInstance().getReference(projectPathHardware+ttl+"/SWITCH");
                    databaseReference1.setValue(0);
                }
            }
        });
//.........................................................TempGaugeMeter................................................................................
        gaugeTemp.setValueColor(Color.BLACK);
        /// arcGauge.setValueColor(Color.BLUE);
        Range range4 = new Range();
        range4.setColor(Color.parseColor("#00b20b"));
        range4.setFrom(0.0);
        range4.setTo(30.6);

        Range range5 = new Range();
        range5.setColor(Color.parseColor("#E3E500"));
        range5.setFrom(30.6);
        range5.setTo(60.3);

        Range range6 = new Range();
        range6.setColor(Color.parseColor("#ce0000"));
        range6.setFrom(60.3);
        range6.setTo(100);

// add color ranges to gauge
        gaugeTemp.addRange(range4);
        gaugeTemp.addRange(range5);
        gaugeTemp.addRange(range6);

// set min max and current value
        gaugeTemp.setMinValue(0.0);
        gaugeTemp.setMaxValue(100);
        // halfGauge.setValue(80);

        gaugeTemp.setNeedleColor(Color.DKGRAY);
        gaugeTemp.setValueColor(Color.BLACK);
        gaugeTemp.setMinValueTextColor(Color.GREEN);
        gaugeTemp.setMaxValueTextColor(Color.RED);
//.........................................................TempGaugeMeter................................................................................


//.........................................................RoundSpeedoMeter................................................................................
        final Gauge gauge1Tilt = findViewById(R.id.ACCELERATION );
        gauge1Tilt.moveToValue(100);
//        final Gauge gauge1 = findViewById(R.id.gauge );
//        gauge1.moveToValue(10);

        DataTILT=FirebaseDatabase.getInstance().getReference();
        DataTILT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                float valTILT=Float.parseFloat(dataSnapshot.child(projectPathHardware+ttl+"/TILTSENSOR").getValue().toString());
                Log.i("valcurrent", String.valueOf(valTILT));

                gauge1Tilt.moveToValue(valTILT);
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });

//.........................................................RoundSpeedoMeter................................................................................

//.........................................................NETSpeedoMeter................................................................................
//        final GaugeView gaugeView = (GaugeView) findViewById(R.id.gauge_view);
//        gaugeView.setShowRangeValues(true);
////        gaugeView.setTargetValue(100);
//        DataVIBRATION=FirebaseDatabase.getInstance().getReference();
//        DataVIBRATION.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot) {
//                int valVIBRATION=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/VIBRATIONSENSOR").getValue().toString());
//                Log.i("valcurrent", String.valueOf(valVIBRATION));
//
//                gaugeView.setTargetValue(valVIBRATION);
//            }
//            @Override
//            public void onCancelled( DatabaseError databaseError) {
//            }
//        });


//.........................................................NETSpeedoMeter................................................................................

//.........................................................greenYellowGreenSpeedoMeter................................................................................
//        SpeedView speedometer = findViewById(R.id.speedView);
//        speedometer.speedTo(50);
//.........................................................greenYellowGreenSpeedoMeter................................................................................
//.........................................................AccidentAlert................................................................................

//        DataAccidentAlert=FirebaseDatabase.getInstance().getReference();
//        DataAccidentAlert.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot) {
//
//                int AccidentAlert=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/Accident_Alert").getValue().toString());
//                Log.i("valcurrent", String.valueOf(AccidentAlert));
//
//                if (AccidentAlert==1){
////                    myLoop(); // Call the loop method
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
//                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
//                    TextView emailBox = dialogView.findViewById(R.id.emailBox);
//                    builder.setView(dialogView);AlertDialog dialog = builder.create();emailBox.setText("VEHICLE ACCIDENT DETECTED");
//                    dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//                        @Override public void onClick(View view) { dialog.dismiss(); }});
//                    if (dialog.getWindow() != null) { dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }dialog.show();
//                }
//                else{
//
//                }
//            }
//            @Override
//            public void onCancelled( DatabaseError databaseError) {
//            }
//        });
//.........................................................AccidentAlert................................................................................

//.........................................................BluePointerSpeedometer................................................................................
        PointerSpeedometer pointerSpeedometer= (PointerSpeedometer) findViewById(R.id.pointerSpeedometer);


        DataSPEED=FirebaseDatabase.getInstance().getReference();
        DataSPEED.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                int valspeed=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/SPEED").getValue().toString());
                Log.i("valcurrent", String.valueOf(valspeed));
                int valspeedltd=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/SPEED_LIMIT").getValue().toString());
                Log.i("valcurrent", String.valueOf(valspeedltd));
                int AccidentAlert=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/Accident_Alert").getValue().toString());
                Log.i("valcurrent", String.valueOf(AccidentAlert));
//                int valVIBRATION=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/VIBRATIONSENSOR").getValue().toString());
//                Log.i("valcurrent", String.valueOf(valVIBRATION));
                pointerSpeedometer.speedTo(valspeed);


                if (valspeed>valspeedltd){
                    title = "HIGH ALERT";
                    content = "OVER SPEED DETECTED";
                    myLoop();
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
                    TextView emailBox = dialogView.findViewById(R.id.emailBox);
                    builder.setView(dialogView);AlertDialog dialog = builder.create();emailBox.setText("OVER SPEED DETECTED");
                    dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) { dialog.dismiss(); }});
                    if (dialog.getWindow() != null) { dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }dialog.show();
                }
                if (AccidentAlert==1){
                    title = "HIGH ALERT";
                    content = "VEHICLE ACCIDENT DETECTED";
                    myLoop();
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
                    TextView emailBox = dialogView.findViewById(R.id.emailBox);
                    builder.setView(dialogView);AlertDialog dialog = builder.create();emailBox.setText("VEHICLE ACCIDENT DETECTED");
                    dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) { dialog.dismiss(); }});
                    if (dialog.getWindow() != null) { dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }dialog.show();
                }
//                if (valVIBRATION > 500){
//                    title = "HIGH ALERT";
//                    content = "VIBRATION DETECTED";
//                    myLoop();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
//                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
//                    TextView emailBox = dialogView.findViewById(R.id.emailBox);
//                    builder.setView(dialogView);AlertDialog dialog = builder.create();emailBox.setText("VIBRATION DETECTED");
//                    dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//                        @Override public void onClick(View view) { dialog.dismiss(); }});
//                    if (dialog.getWindow() != null) { dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); }dialog.show();
//                }
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });
//.........................................................BluePointerSpeedometer................................................................................


        TEMPdetection=FirebaseDatabase.getInstance().getReference();
        TEMPdetection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
//                String val2=String.valueOf(dataSnapshot.child("water_quality_monitoring/phSensor").getValue().toString());
//                Log.i("val1", String.valueOf(val2));
//                // =setText(temperature);
//                amonia_detection.setText(String.valueOf(val2));

                float valph=Float.parseFloat(dataSnapshot.child(projectPathHardware+ttl+"/TEMPERATURE").getValue().toString());
                // int valph=Integer.parseInt(dataSnapshot.child("ZATKA_MACHINE1/BATTERY").getValue().toString());
                Log.i("valph", String.valueOf(valph));
                /// gaugePH.moveToValue(valph);
                gaugeTemp.setValue(valph);
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
        gaugeTemp.setFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(double value) {
                float intValue = Double.valueOf(value).intValue();
                return String.valueOf(intValue);

            }
        });

        DataSPEEDLIMIT=FirebaseDatabase.getInstance().getReference();
        DataSPEEDLIMIT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                int valspeedltd=Integer.parseInt(dataSnapshot.child(projectPathHardware+ttl+"/SPEED_LIMIT").getValue().toString());
                Log.i("valcurrent", String.valueOf(valspeedltd));
                // =setText(temperature);
                SPEEDLIMIT.setText(String.valueOf(valspeedltd));
                /// gaugeTURBIDITY.moveToValue(valturbidity);
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

        final Button Btn_SPEEDLIMIT =(Button)findViewById(R.id.Speedlmt_button);
        Btn_SPEEDLIMIT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String value1 = SPEEDLIMIT.getText().toString();

                // Push creates a unique id in database
                //demoRef.setValue(value);
                databaseReference1= FirebaseDatabase.getInstance().getReference(projectPathHardware+ttl+"/SPEED_LIMIT");
                databaseReference1.setValue(value1);
//
            }
        });

        //android:id="@+id/location_button"

        MapRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MapActivity.class));
            }
        });

        //..................................................................................................................................

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(projectPathString+userID);//"Zatka_Machine_Final/"+
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDid.getText().toString())
                        /// .putExtra("Language", detailLang.getText().toString())
                        .putExtra("Language", LANGUAGE)
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
        WIFICONFIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this, WificonfigActivity.class));
            }
        });


    }
    //.........................................................DIALOGBOX_NOTIFICATION....................................................................................
    private void myLoop() {
        Utils.showNotification(this,title,content);
//        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert, null);
//        TextView emailBox = dialogView.findViewById(R.id.emailBox);
//        builder.setView(dialogView);
//        AlertDialog dialog = builder.create();
//        emailBox.setText("Over Speed Detected");
//        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        dialog.show();
    }
//.........................................................DIALOGBOX_NOTIFICATION....................................................................................


}
//public class DetailActivity extends AppCompatActivity {
//
//
//    TextView detailDid, detailTitle,detailDeviceID, detailLang;      //change2
//    private Button MapRedirectButton;
//    ImageView detailImage;
//    FloatingActionButton deleteButton, editButton ,WIFICONFIG;
//    TextView CURRENT;
//    String key = "";
//    String imageUrl = "";
//    String ttl= "";
//    String IMAGE= "";
//    String LANGUAGE= "";
//    private Switch sw1;
//    private FirebaseAuth auth;
//    FirebaseDatabase database;
//            FirebaseAuth fAuth;
//        String userID;
//    DatabaseReference databaseReference1,databasefeedback1,VOLTAGEdetection,CURRENTdata;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//        auth = FirebaseAuth.getInstance();
//        database=FirebaseDatabase.getInstance();
//        fAuth = FirebaseAuth.getInstance();
//        //get current user
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        userID = fAuth.getCurrentUser().getUid();
//        Log.i("userID", String.valueOf(userID));
//
//        //....................................................FETCHFCM.................................................
//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
//            @Override
//            public void onSuccess(String token) {
//                databaseReference1=FirebaseDatabase.getInstance().getReference("Zatka_Machine_Final/"+ttl+"/fcmToken");
//                databaseReference1.setValue(token);
//                Log.i("fcm", String.valueOf(token));
//            }
//        });
//        //....................................................FETCHFCM.................................................
//
//        sw1 = (Switch)findViewById(R.id.switch1);
//        MapRedirectButton = findViewById(R.id.location_button);
//        CURRENT = findViewById(R.id.current);    //change2
//       /// detailImage = findViewById(R.id.detailImage);
//        detailTitle = findViewById(R.id.detailTitle);
//        detailDid = findViewById(R.id.detailDeviceID);
//        deleteButton = findViewById(R.id.deleteButton);
//        editButton = findViewById(R.id.editButton);
//        WIFICONFIG = findViewById(R.id.WIFICONFIG);
//
//       /// detailLang = findViewById(R.id.DetailLang);
//        final HalfGauge gaugeBattery = findViewById(R.id.gaugeBattery );
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null){
//            detailDid.setText(bundle.getString("Description"));  //Change2
//            detailTitle.setText(bundle.getString("Title"));
//           // detailDeviceID.setText(bundle.getString("Description"));  //change1
//           // detailLang.setText(bundle.getString("Language"));
//            key = bundle.getString("Key");
//            ttl = bundle.getString("Description");
//            imageUrl = bundle.getString("Image");
//            LANGUAGE= bundle.getString("Language");
//           // IMAGE   =bundle.getString("Image");
//          //  Glide.with(this).load(bundle.getString("Image")).into(detailImage);
//
//        }
//
//
//
//
//        //................................................................................................................
//      databasefeedback1=FirebaseDatabase.getInstance().getReference();
//        databasefeedback1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot) {
//                int val=Integer.parseInt(dataSnapshot.child("Zatka_Machine_Final/"+ttl+"/SWITCH").getValue().toString());
//                //  Log.i("val", String.valueOf(val));
//
//                if(val==0){
//                    sw1.setChecked(false);
//                }else{
//                    sw1.setChecked(true);
//                }
//            }
//
//            @Override
//            public void onCancelled( DatabaseError databaseError) {
//
//            }
//        });
//        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if (sw1.isChecked()){
//                    //  Log.i("switch 1", String.valueOf(b));
//
//                    databaseReference1=FirebaseDatabase.getInstance().getReference("Zatka_Machine_Final/"+ttl+"/SWITCH");
//                    databaseReference1.setValue(1);
//                }else{
//                    databaseReference1=FirebaseDatabase.getInstance().getReference("Zatka_Machine_Final/"+ttl+"/SWITCH");
//                    databaseReference1.setValue(0);
//                }
//            }
//        });
//
//        gaugeBattery.setValueColor(Color.BLACK);
//        /// arcGauge.setValueColor(Color.BLUE);
//        Range range4 = new Range();
//        range4.setColor(Color.parseColor("#ce0000"));
//        range4.setFrom(0.0);
//        range4.setTo(5.6);
//
//        Range range5 = new Range();
//        range5.setColor(Color.parseColor("#E3E500"));
//        range5.setFrom(5.6);
//        range5.setTo(10.3);
//
//        Range range6 = new Range();
//        range6.setColor(Color.parseColor("#00b20b"));
//        range6.setFrom(10.3);
//        range6.setTo(15);
//
//// add color ranges to gauge
//        gaugeBattery.addRange(range4);
//        gaugeBattery.addRange(range5);
//        gaugeBattery.addRange(range6);
//
//// set min max and current value
//        gaugeBattery.setMinValue(0.0);
//        gaugeBattery.setMaxValue(15);
//        // halfGauge.setValue(80);
//
//        gaugeBattery.setNeedleColor(Color.DKGRAY);
//        gaugeBattery.setValueColor(Color.BLACK);
//        gaugeBattery.setMinValueTextColor(Color.RED);
//        gaugeBattery.setMaxValueTextColor(Color.GREEN);
//
//
//        VOLTAGEdetection=FirebaseDatabase.getInstance().getReference();
//        VOLTAGEdetection.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot) {
////                String val2=String.valueOf(dataSnapshot.child("water_quality_monitoring/phSensor").getValue().toString());
////                Log.i("val1", String.valueOf(val2));
////                // =setText(temperature);
////                amonia_detection.setText(String.valueOf(val2));
//
//                float valph=Float.parseFloat(dataSnapshot.child("Zatka_Machine_Final/"+ttl+"/BATTERY").getValue().toString());
//               // int valph=Integer.parseInt(dataSnapshot.child("ZATKA_MACHINE1/BATTERY").getValue().toString());
//                Log.i("valph", String.valueOf(valph));
//                /// gaugePH.moveToValue(valph);
//                gaugeBattery.setValue(valph);
//            }
//
//            @Override
//            public void onCancelled( DatabaseError databaseError) {
//
//            }
//        });
//        gaugeBattery.setFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(double value) {
//                float intValue = Double.valueOf(value).intValue();
//                return String.valueOf(intValue);
//
//            }
//        });
//
//        CURRENTdata=FirebaseDatabase.getInstance().getReference();
//        CURRENTdata.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot) {
//                int valcurrent=Integer.parseInt(dataSnapshot.child("Zatka_Machine_Final/"+ttl+"/CURRENT").getValue().toString());
//                Log.i("valcurrent", String.valueOf(valcurrent));
//                // =setText(temperature);
//                CURRENT.setText(String.valueOf(valcurrent));
//               /// gaugeTURBIDITY.moveToValue(valturbidity);
//            }
//
//            @Override
//            public void onCancelled( DatabaseError databaseError) {
//
//            }
//        });
//
//        //android:id="@+id/location_button"
//
//        MapRedirectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DetailActivity.this, MapActivity.class));
//            }
//        });
//
//        //..................................................................................................................................
//
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ZATKA_MACHINE/"+userID);//"Zatka_Machine_Final/"+
//                FirebaseStorage storage = FirebaseStorage.getInstance();
//                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
//                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        reference.child(key).removeValue();
//                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        finish();
//                    }
//                });
//            }
//        });
//
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
//                        .putExtra("Title", detailTitle.getText().toString())
//                        .putExtra("Description", detailDid.getText().toString())
//                       /// .putExtra("Language", detailLang.getText().toString())
//                        .putExtra("Language", LANGUAGE)
//                        .putExtra("Image", imageUrl)
//                        .putExtra("Key", key);
//                startActivity(intent);
//            }
//        });
//        WIFICONFIG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetailActivity.this, WificonfigActivity.class));
//            }
//        });
//
//
//    }
//}
