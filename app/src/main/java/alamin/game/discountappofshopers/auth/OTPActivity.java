package alamin.game.discountappofshopers.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import alamin.game.discountappofshopers.R;

public class OTPActivity extends AppCompatActivity {
    private static final int PERMISSION_ID = 44;
    private static final String TAG = "Tag";
    private Button btn_otp_code;
    private String userType;
    private String phoneNumber;
    private String name = "";
    private String password = "";

    private String fb_id;
    private FirebaseAuth auth;
    private EditText editTextCode;

    private DatabaseReference databaseReference;
    private String gender = "default";
    private String date_of_birth = "default";
    private String profile_pic_url = "profile_pic";
    private String location = "default";
    private String email = "example@gmail.com";
    private String google_location = "default";
    private String food_item = "blank";
    private String payment_gateway = "blank";
    private String payment_number = "blank";
    private double latitude, longitude;
    private Boolean status = false;
    private int discount = 10;
    private String smsCode;

    public int counter = 30;
    public int counter_loop = 30;
    Button button;
    private OtpView otpView;
    private String Current_Uid;
    FusedLocationProviderClient mFusedLocationClient;

    private String verificationCode;

    private TextView tv_phone_number, tv_time_counter;
    private EditText et_password, et_re_type_password, et_name, et_otp;
    private Button btn_continue;
    private Button btn_otp_verify;
    private FirebaseAuth loginAuth;
    private DatabaseReference shopperRef, customerRef;
    private String bksh = "Bkash";
    private String bksh_number = "";

    private String rocket = "Rocket";
    private String rocket_number = "";

    private String mCash = "mCash";
    private String mCashNumber = "";

    private String myCash = "MyCash";
    private String myCashNumber = "";

    private String uCash = "uCash";
    private String uCashNumber = "";

    private String nogod = "Nogod";
    private String nogod_number = "";

    private String sureCash = "SureCash";
    private String sureCashNumber = "";
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
//        firebaseEmailAuth();
        findViews();
        getIntentData();
    }

    private void findViews() {
        tv_phone_number = findViewById(R.id.tv_phone_number);
        btn_otp_verify = findViewById(R.id.btn_otp_verify);
        et_otp = findViewById(R.id.et_otp_code);

        otpView = findViewById(R.id.otpView);
        tv_time_counter = findViewById(R.id.tv_time_counter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            userType = bundle.getString("SelectFromLoginActivity");
            phoneNumber = bundle.getString("phone_number");
//            Current_Uid = bundle.getString("uid");
            latitude = bundle.getDouble("myLocationLat");
            longitude = bundle.getDouble("myLocationLong");
            Log.d("TAG", "\n\n\ngetIntentData: " + bundle.toString() + "\nPhone Num: " + phoneNumber);

            tv_phone_number.setText(phoneNumber);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseOtpAuth();
    }

    private void firebaseOtpAuth() {
        otpTimer();
        if (phoneNumber != null) {
            Log.d("TAG", "firebaseOtpAuth: Phone No: " + phoneNumber);
            StartFirebaseOtpLogin();
            generateOtpCode(phoneNumber);
        }
        onClickOtpVerify();
    }


    private void otpTimer() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_time_counter.setText(String.valueOf(counter_loop));
                counter_loop--;
            }

            public void onFinish() {
//                tv_time_counter.setText("Please Wait....");
                tv_time_counter.setText("ReEnter Phone Number to Resend Otp");
            }
        }.start();
    }

    private void generateOtpCode(String mobile) {
        Log.d("TAG", "generateOtpCode for phone no: " + mobile);
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+88" + mobile,         // Phone number to verify
//                counter,                        // Timeout duration
                    60,
                    TimeUnit.SECONDS,                // Unit of timeout
                    OTPActivity.this,        // Activity (for callback binding)
//                    TaskExecutors.MAIN_THREAD,
                    mCallback);                      // OnVerificationStateChangedCallbacks
        } catch (Exception e) {
            Log.d(TAG, "generateOtpCode error: " + e.getMessage());
        }
    }

    private void StartFirebaseOtpLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                try {
                    smsCode = phoneAuthCredential.getSmsCode();
                    if (smsCode != null) {
                        Log.i(TAG, "onVerificationCompleted: CODE " + smsCode);
                        Log.i(TAG, "onVerificationCompleted: PROVIDER " + phoneAuthCredential.getProvider());

                        et_otp.setText(smsCode);
                        otpView.setText(smsCode);
                        verifyOtpVerificationCode(smsCode);
                    }
                } catch (Exception e) {
                    Log.i(TAG, "onVerificationCompleted failed to get CODE and error: " + e.getMessage());
                }
                Log.d(TAG, "onVerificationCompleted: ");
                Toast.makeText(OTPActivity.this, "verification completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "\nOTP verification failed for \n" + e.getMessage());
                Toast.makeText(OTPActivity.this, "verification failed for \n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(OTPActivity.this, "OTP Code sent", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCodeSent: verificationCode: " + verificationCode);
            }
        };
    }

    private void verifyOtpVerificationCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
            Log.d(TAG, "verifyOtpVerificationCode: " + code);
            if (credential != null) {
                Log.d(TAG, "verifyOtpVerificationCode credential: " + credential);
                gotoNextActivity(EmailSignUpActivity.class, phoneNumber, userType);
//               signInWithPhoneAuthCredential(credential);
            }
        } catch (Exception e) {
            Log.d(TAG, "verifyOtpVerificationCode error: " + e.getMessage());
        }
    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        auth.signInWithCredential(credential).addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    fb_id = auth.getCurrentUser().getUid();
//
//                    if (userType.equals("customer")) {
//                        RegistrationModelCustomer customer = new RegistrationModelCustomer(fb_id, phoneNumber, email, password, name, gender, date_of_birth, profile_pic_url, latitude, longitude, status, bksh, bksh_number, rocket, rocket_number, mCash, mCashNumber, myCash, myCashNumber, uCash, uCashNumber, nogod, nogod_number, sureCash, sureCashNumber);
//                        databaseReference.child("customer").child(fb_id).setValue(customer);
//                        gotoNextActivity(SingUpActivityCustomer.class, phoneNumber, userType);
//
//                    } else if (userType.equals("shopper")) {
//                        RegistrationModelShopper shopper = new RegistrationModelShopper(fb_id, name, phoneNumber, email, password, date_of_birth, profile_pic_url, location, google_location, discount, food_item, latitude, longitude);
//                        databaseReference.child("shopper").child(fb_id).setValue(shopper);
//                        Boolean status = false;
//                        String name = "";
//                        String location = "";
//                        String picture_url = "";
//                        String user_uid;
//                        databaseReference = FirebaseDatabase.getInstance().getReference("location details");
//                        LocationModel locationModel = new LocationModel(latitude, longitude, name, location, profile_pic_url, fb_id, status);
//                        databaseReference.child(fb_id).setValue(locationModel);
//                        gotoNextActivity(SingUpActivityShopper.class, phoneNumber, userType);
//                    }
//                } else {
//                    String message = "Something is wrong, we will fix it soon...";
//                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                        message = "Invalid code entered...";
//                    }
//                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
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

    public void gotoNextActivity(Class selected_class, String phone, String userType) {
        Intent intent = new Intent(OTPActivity.this, selected_class);
        intent.putExtra("phone_number", phone);
        intent.putExtra("selectFromOTPActivity", userType);
        intent.putExtra("uid", Current_Uid);
        intent.putExtra("myLocationLat", latitude);
        intent.putExtra("myLocationLong", longitude);
//        intent.putExtra("selectFromOTPActivity", "shopper");
//        intent.putExtra("uid", fb_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pageTransectionAnimation();
        startActivity(intent);
        finish();
    }

    private void onClickOtpVerify() {
        btn_otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (otpView.getText().toString().trim().equals(smsCode)) {
                        String otp = otpView.getText().toString().trim();
                        Log.d(TAG, "onClick: otp: " + otp);
                        verifyOtpVerificationCode(otp);
                    } else {
                        Log.d(TAG, "onClick otp Code is invalid: ");
                        Toast.makeText(OTPActivity.this, "Code is invalid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "otp verify onClick error: " + e.getMessage());
                }
            }
        });
    }


    private void pageTransectionAnimation() {
        YoYo.with(Techniques.Hinge)
                .duration(700)
                .playOn(findViewById(R.id.otp_page));
    }


//    public void selectHomeActivity(Class selectedClass) {
//        Intent intent = new Intent(OTPActivity.this, selectedClass);
//        YoYo.with(Techniques.Hinge)
//                .duration(700)
//                .playOn(findViewById(R.id.otp_page));
//        startActivity(intent);
//        finish();
//    }


}


//
////        mAuth = FirebaseAuth.getInstance();
////        Intent intent = getIntent();
////        Bundle bundle = intent.getExtras();
////
//////        editTextCode = findViewById(R.id.et_verification_code);
//////        tv_send_phone_number = findViewById(R.id.tv_send_phone_number);
////
////        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
////        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
////
//////        otpView = findViewById(R.id.otpView);
//////        tv_time_counter = findViewById(R.id.tv_time_counter);
////
////
////        new CountDownTimer(30000, 1000){
////            public void onTick(long millisUntilFinished){
////                tv_time_counter.setText(String.valueOf(counter_loop));
////                counter_loop--;
////            }
////            public  void onFinish(){
////                tv_time_counter.setText("Please Wait....");
////            }
////        }.start();
////
////
////        if(bundle != null){
////             chooser_value = bundle.getString("SelectFromLoginActivity");
////             phoneNumber = bundle.getString("phone_number");
////            Curreemt_Uid = bundle.getString("uid");
////            tv_send_phone_number.setText(phoneNumber);
////            sendVerificationCode(phoneNumber);
////        }
////        btn_otp_code = findViewById(R.id.btn_otp_code);
////        btn_otp_code.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View view) {
//////                if(otpView.getText().toString().equals(code)) {
//////                    Intent intent = new Intent(OTPActivity.this, SingUpActivityCustomer.class);
//////                    intent.putExtra("selectFromOTPActivity",chooser_value);
//////                    startActivity(intent);
//////                }else {
//////                    Toast.makeText(OTPActivity.this, "Code is invalid", Toast.LENGTH_SHORT).show();
//////                    return;
//////                }
//////            }
//////        });
////    }
//    private void sendVerificationCode(String mobile) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+88" + mobile,
//                counter,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                mCallbacks);
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
////                        RegistrationModelCustomer customer = new RegistrationModelCustomer(fb_id, phoneNumber,email,password,shopName, gender, date_of_birth, payment_gateway,payment_number,password, profile_pic_url,latitude,longitude,status);
//                        RegistrationModelCustomer customer = new RegistrationModelCustomer( fb_id,  phoneNumber,  email,  password,  shopName,  gender,  date_of_birth,  profile_pic_url,  latitude,  longitude,  status,  bksh,  bksh_number,  rocket,  rocket_number,  mCash,  mCashNumber,  myCash,  myCashNumber,  uCash,  uCashNumber,  nogod,  nogod_number,  sureCash,  sureCashNumber);
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
//                        RegistrationModelShopper shopper = new RegistrationModelShopper(fb_id,shopName,phoneNumber,email, password,date_of_birth,profile_pic_url,location,google_location,discount,food_item,latitude,longitude);
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
////    @SuppressLint("MissingPermission")
////    private void getLastLocation(){
////        if (checkPermissions()) {
////            if (isLocationEnabled()) {
////                mFusedLocationClient.getLastLocation().addOnCompleteListener(
////                        new OnCompleteListener<Location>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Location> task) {
////                                Location location = task.getResult();
////                                if (location == null) {
////                                    requestNewLocationData();
////                                } else {
////                                    latitude = (location.getLatitude());
////                                    longitude =(location.getLongitude());
////                                }
////                            }
////                        }
////                );
////            } else {
////                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
////                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                startActivity(intent);
////            }
////        } else {
////            requestPermissions();
////        }
////    }
////
////    @SuppressLint("MissingPermission")
////    private void requestNewLocationData(){
////
////        LocationRequest mLocationRequest = new LocationRequest();
////        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////        mLocationRequest.setInterval(0);
////        mLocationRequest.setFastestInterval(0);
////        mLocationRequest.setNumUpdates(1);
////
////        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
////        mFusedLocationClient.requestLocationUpdates(
////                mLocationRequest, mLocationCallback,
////                Looper.myLooper()
////        );
////
////    }
////
////    private LocationCallback mLocationCallback = new LocationCallback() {
////        @Override
////        public void onLocationResult(LocationResult locationResult) {
////            Location mLastLocation = locationResult.getLastLocation();
////            latitude = (mLastLocation.getLatitude());
////            longitude = (mLastLocation.getLongitude());
////        }
////    };
////
////    private boolean checkPermissions() {
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
////                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////            return true;
////        }
////        return false;
////    }
////
////    private void requestPermissions() {
////        ActivityCompat.requestPermissions(
////                this,
////                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
////                PERMISSION_ID
////        );
////    }
////
////    private boolean isLocationEnabled() {
////        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
////                LocationManager.NETWORK_PROVIDER
////        );
////    }
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        if (requestCode == PERMISSION_ID) {
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                getLastLocation();
////            }
////        }
////    }
////
////    @Override
////    public void onResume(){
////        super.onResume();
////        if (checkPermissions()) {
////            getLastLocation();
////        }
//
////    }
//}