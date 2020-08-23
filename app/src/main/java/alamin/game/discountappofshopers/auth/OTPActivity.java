package alamin.game.discountappofshopers.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukesh.OtpView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.activity.OneActivity;
import alamin.game.discountappofshopers.activity.SplashActivity;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.customers.SingUpActivityCustomer;
import alamin.game.discountappofshopers.model.LocationModel;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.ShopperHomeActivity;
import alamin.game.discountappofshopers.shoppers.SingUpActivityShopper;

public class OTPActivity extends AppCompatActivity {
    private static final int PERMISSION_ID = 44;
    private Button btn_otp_code;
    private String chooser_value;
    private String phoneNumber;
    private String fb_id;
    private FirebaseAuth mAuth;
    private EditText editTextCode;
    private String mVerificationId;
    private DatabaseReference databaseReference;
    private String gender = "default";
    private String date_of_birth = "default";
    private String profile_pic_url = "profiel_pic";
    private String location = "default";
    private String email = "example@gmail.com";
    private String google_location = "default";
    private String food_item = "blank";
    private String payment_gateway = "blank";
    private String payment_number = "blank";
    private double latitude, longitude;
    private Boolean status = false;
    private int discount = 10;
    private String code;

    public int counter = 30;
    public int counter_loop = 30;
    Button button;
    TextView tv_time_counter, tv_send_phone_number;
    private OtpView otpView;
    private String Curreemt_Uid;
    FusedLocationProviderClient mFusedLocationClient;
    private FirebaseAuth firebaseAuth;


    private TextView tv_phone_number;
    private EditText et_password, et_re_type_password, et_name;
    private Button btn_continue;
    private FirebaseAuth loginAuth;
    private DatabaseReference shopperRef, customerRef;
    private String bksh="Bkash";
    private String bksh_number="";

    private String rocket="Rocket";
    private String rocket_number="";

    private String mCash="mCash";
    private String mCashNumber="";

    private String myCash="MyCash";
    private String myCashNumber="";

    private String uCash="uCash";
    private String uCashNumber="";

    private String nogod="Nogod";
    private String nogod_number="";

    private String sureCash="SureCash";
    private String sureCashNumber="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        et_name = findViewById(R.id.et_name);
        tv_phone_number = findViewById(R.id.phone_number);
        et_password = findViewById(R.id.password);
        et_re_type_password = findViewById(R.id.re_type_password);
        btn_continue = findViewById(R.id.btn_continue);

        shopperRef = FirebaseDatabase.getInstance().getReference().child("Users").child("shopper");
        customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("customer");
        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .playOn(findViewById(R.id.otp_page));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        firebaseAuth = FirebaseAuth.getInstance();

        if (bundle != null) {
            chooser_value = bundle.getString("SelectFromLoginActivity");
            phoneNumber = bundle.getString("phone_number");
            Curreemt_Uid = bundle.getString("uid");
            tv_phone_number.setText(phoneNumber);


            latitude = bundle.getDouble("myLocationLat");
            longitude = bundle.getDouble("myLocationLong");
        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = tv_phone_number.getText().toString().trim();
                final String name = et_name.getText().toString().trim();
                String pass = et_password.getText().toString().trim();
                final String confirmPass = et_re_type_password.getText().toString().trim();

                if (TextUtils.isEmpty(pass) && TextUtils.isEmpty(confirmPass)) {
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.pass_container));
                    return;

                }else if(TextUtils.isEmpty(name)){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.et_name));
                    return;

                }else if (!pass.matches(confirmPass)) {

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.pass_container));
                    return;
                }else {
                    final String phonemail = String.valueOf(phoneNumber+"@gmail.com");
                    firebaseAuth.createUserWithEmailAndPassword(phonemail, confirmPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String fb_id = firebaseAuth.getCurrentUser().getUid();
                                if (chooser_value.equals("shopper")) {
                                    RegistrationModelShopper shopper = new RegistrationModelShopper(fb_id, name, phoneNumber, phonemail, confirmPass, date_of_birth, profile_pic_url, location, google_location, discount, food_item, latitude, longitude);
                                    shopperRef.child(fb_id).setValue(shopper);
                                    selectHomeActivity(ShopperHomeActivity.class);
                                    et_name.setText("");
                                    et_password.setText("");
                                    et_re_type_password.setText("");
                                    Toast.makeText(OTPActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();

                                } else if (chooser_value.equals("customer")) {
                                    RegistrationModelCustomer customer = new RegistrationModelCustomer(fb_id,phoneNumber,email,confirmPass,name,date_of_birth,profile_pic_url,profile_pic_url,latitude,longitude,status,bksh,bksh_number,rocket,rocket_number,mCash,mCashNumber,myCash,myCashNumber,uCash,uCashNumber,nogod,nogod_number,sureCash,sureCashNumber);
                                    customerRef.child(fb_id).setValue(customer);
                                    selectHomeActivity(CustomerHomeActivity.class);
                                    et_name.setText("");
                                    et_password.setText("");
                                    et_re_type_password.setText("");
                                    Toast.makeText(OTPActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                Toast.makeText(OTPActivity.this, "Error Occurred : "+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OTPActivity.this, "Error Occurred : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    public void selectHomeActivity(Class selectedClass){
        Intent intent = new Intent(OTPActivity.this,selectedClass);
        YoYo.with(Techniques.Hinge)
                .duration(700)
                .playOn(findViewById(R.id.otp_page));
        startActivity(intent);
        finish();
    }
}






//        mAuth = FirebaseAuth.getInstance();
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//
////        editTextCode = findViewById(R.id.et_verification_code);
////        tv_send_phone_number = findViewById(R.id.tv_send_phone_number);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
////        otpView = findViewById(R.id.otpView);
////        tv_time_counter = findViewById(R.id.tv_time_counter);
//
//
//        new CountDownTimer(30000, 1000){
//            public void onTick(long millisUntilFinished){
//                tv_time_counter.setText(String.valueOf(counter_loop));
//                counter_loop--;
//            }
//            public  void onFinish(){
//                tv_time_counter.setText("Please Wait....");
//            }
//        }.start();
//
//
//        if(bundle != null){
//             chooser_value = bundle.getString("SelectFromLoginActivity");
//             phoneNumber = bundle.getString("phone_number");
//            Curreemt_Uid = bundle.getString("uid");
//            tv_send_phone_number.setText(phoneNumber);
//            sendVerificationCode(phoneNumber);
//        }
////        btn_otp_code = findViewById(R.id.btn_otp_code);
//        btn_otp_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(otpView.getText().toString().equals(code)) {
//                    Intent intent = new Intent(OTPActivity.this, SingUpActivityCustomer.class);
//                    intent.putExtra("selectFromOTPActivity",chooser_value);
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(OTPActivity.this, "Code is invalid", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        });
//    }
//    private void sendVerificationCode(String mobile) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+88" + mobile,
//                counter,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                mCallbacks);
//
//    }
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            code = phoneAuthCredential.getSmsCode();
//            if (code != null) {
//                //editTextCode.setText(code);
//                otpView.setText(code);
//                verifyVerificationCode(code);
//            }
//        }
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            mVerificationId = s;
//        }
//    };
//    private void verifyVerificationCode(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
//        signInWithPhoneAuthCredential(credential);
//    }
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential).addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    fb_id = mAuth.getCurrentUser().getUid();
//
//                    if (chooser_value.equals("customer")){
//                        RegistrationModelCustomer customer = new RegistrationModelCustomer(fb_id, name,phoneNumber,payment_gateway,payment_number,email,password, gender, date_of_birth, profile_pic_url,latitude,longitude,status);
//                        databaseReference.child("customer").child(fb_id).setValue(customer);
//                        gotoNextActivity(SingUpActivityCustomer.class,phoneNumber);
////                        final List<String> stringList = new ArrayList<>();
////                        databaseReference.child("customer").addValueEventListener(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
////                                    stringList.add(dataSnapshot1.getChildren().toString());
////                                }
////                               int a = stringList.size();
////                            }
////                            @Override
////                            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                            }
////                        });
////                        for (int i =1; i<=stringList.size();i++) {
////                            String val = stringList.get(i);
////                            if (val.equals(fb_id)) {
////                                gotoNextActivity(SingUpActivityCustomer.class,phoneNumber);
////                                fileList();
////                            }else {
////                                RegistrationModelCustomer customer = new RegistrationModelCustomer(fb_id, name,payment_gateway,payment_number, phoneNumber,email,password, gender, date_of_birth, profile_pic_url,latitude,longitude,status);
////                                databaseReference.child("customer").child(fb_id).setValue(customer);
////                                gotoNextActivity(SingUpActivityCustomer.class,phoneNumber);
////                            }
////                        }
//
//                    }else if (chooser_value.equals("shopper")){
//                        RegistrationModelShopper shopper = new RegistrationModelShopper(fb_id,name,phoneNumber,email,password,date_of_birth,profile_pic_url,location,google_location,discount,food_item,latitude,longitude);
//                        databaseReference.child("shopper").child(fb_id).setValue(shopper);
//                        Boolean status=false;
//                        String name="";
//                        String location="";
//                        String picture_url="";
//                        String user_uid;
//                        databaseReference = FirebaseDatabase.getInstance().getReference("location details");
//                        LocationModel locationModel = new LocationModel(latitude,longitude,name,location,profile_pic_url,fb_id,status);
//                        databaseReference.child(fb_id).setValue(locationModel);
//                        gotoNextActivity(SingUpActivityShopper.class,phoneNumber);
//                        finish();
//
//                       /*
//                        final List<String> stringList = new ArrayList<>();
//                        databaseReference.child("shopper").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                                    stringList.add(dataSnapshot1.getChildren().toString());
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                        for (int i =1; i<=stringList.size();i++){
//                           String val = stringList.get(i);
//                           if (val.equals(fb_id)){
//                               gotoNextActivity(SingUpActivityShopper.class,phoneNumber);
//                               fileList();
//                           }else {
//                               RegistrationModelShopper shopper = new RegistrationModelShopper(fb_id,name,phoneNumber,email,password,date_of_birth,profile_pic_url,location,google_location,discount,food_item);
//                               databaseReference.child("shopper").child(fb_id).setValue(shopper);
//                               Boolean status=false;
//                               double latitude = 0.0;
//                               double longitude = 0.0;
//                               String name="";
//                               String location="";
//                               String picture_url="";
//                               String user_uid;
//                               databaseReference = FirebaseDatabase.getInstance().getReference("location details");
//                               LocationModel locationModel = new LocationModel(latitude,longitude,name,location,profile_pic_url,fb_id,status);
//                               databaseReference.child(fb_id).setValue(locationModel);
//                               gotoNextActivity(SingUpActivityShopper.class,phoneNumber);
//                               finish();
//                           }
//                        }*/
//
//                    }
//                } else {
//                    String message = "Something is wrong, we will fix it soon...";
////                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
////                        message = "Invalid code entered...";
////                    }
//                    Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                    snackbar.setAction("Dismiss", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                    snackbar.show();
//                }
//            }
//        });
//    }
//    public void gotoNextActivity(Class selected_class,String phone){
//        Intent intent = new Intent(OTPActivity.this, selected_class);
//        intent.putExtra("selectFromOTPActivity","shopper");
//        intent.putExtra("phone_number",phone);
//        intent.putExtra("uid",fb_id);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }
//
//    @SuppressLint("MissingPermission")
//    private void getLastLocation(){
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.getLastLocation().addOnCompleteListener(
//                        new OnCompleteListener<Location>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Location> task) {
//                                Location location = task.getResult();
//                                if (location == null) {
//                                    requestNewLocationData();
//                                } else {
//                                    latitude = (location.getLatitude());
//                                    longitude =(location.getLongitude());
//                                }
//                            }
//                        }
//                );
//            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            requestPermissions();
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void requestNewLocationData(){
//
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        );
//
//    }
//
//    private LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Location mLastLocation = locationResult.getLastLocation();
//            latitude = (mLastLocation.getLatitude());
//            longitude = (mLastLocation.getLongitude());
//        }
//    };
//
//    private boolean checkPermissions() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                PERMISSION_ID
//        );
//    }
//
//    private boolean isLocationEnabled() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//        );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_ID) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            }
//        }
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        if (checkPermissions()) {
//            getLastLocation();
//        }

//    }
//}