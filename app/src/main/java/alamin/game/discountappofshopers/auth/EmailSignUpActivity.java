package alamin.game.discountappofshopers.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukesh.OtpView;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.animation.Techniques;
import alamin.game.discountappofshopers.animation.YoYo;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.ShopperHomeActivity;

public class EmailSignUpActivity extends AppCompatActivity {
    private static final int PERMISSION_ID = 44;
    private static final String TAG = "Tag";
    private Button btn_otp_code;
    private String userType;
    private String phoneNumber;
    private String name = "";
    private String password;

    private String fb_id;
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


    private TextView tv_phone_number, tv_time_counter;
    private EditText et_password, et_re_type_password, et_name;
    private Button btn_continue;
    private Button btn_otp_verify;
    private FirebaseAuth firebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);
        findViews();
        getIntentData();
    }

    private void findViews() {
        et_name = findViewById(R.id.et_name);
        tv_phone_number = findViewById(R.id.phone_number);
        et_password = findViewById(R.id.password);
        et_re_type_password = findViewById(R.id.re_type_password);
        btn_continue = findViewById(R.id.btn_continue);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            userType = bundle.getString("selectFromOTPActivity");
            phoneNumber = bundle.getString("phone_number");
            latitude = bundle.getDouble("myLocationLat");
            longitude = bundle.getDouble("myLocationLong");
            Log.d("TAG", "\n\n\ngetIntentData: " + bundle.toString() + "\nPhone Num: " + phoneNumber);

            tv_phone_number.setText(phoneNumber);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        firebaseEmailAuth();
    }

    private void firebaseEmailAuth() {

        shopperRef = FirebaseDatabase.getInstance().getReference().child("Users").child("shopper");
        customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("customer");
//        YoYo.with(Techniques.FadeIn)
//                .duration(2000)
//                .playOn(findViewById(R.id.otp_page));

        firebaseAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "firebaseEmailAuth: " + firebaseAuth.toString());


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phoneNumber = tv_phone_number.getText().toString().trim();
                final String name = et_name.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                final String confirmPass = et_re_type_password.getText().toString().trim();
                Log.d(TAG, "onClick: Phone: " + phoneNumber + " Name: " + name + " password: " + password + " Confirm Pass: " + confirmPass);

                if (TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPass)) {
                    try {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.pass_container));
                    } catch (Exception e) {
                        Log.d(TAG, "isEmpty(password): ");
                    }
                    return;

                } else if (TextUtils.isEmpty(name)) {
                    try {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.et_name));
                    } catch (Exception e) {
                        Log.d(TAG, "isEmpty(name)");
                    }
                    return;

                } else if (!password.matches(confirmPass)) {

                    try {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(findViewById(R.id.pass_container));
                    } catch (Exception e) {
                        Log.d(TAG, "!password.matches(confirmPass: ");
                    }
                    return;
                } else {
                    final String email = String.valueOf(phoneNumber + "@gmail.com");
                    Log.d(TAG, "onClick: email " + email);
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           try {
                               if (task.isSuccessful()) {
                                   String fb_id = firebaseAuth.getCurrentUser().getUid();
                                   if (userType.equals("shopper")) {
                                       RegistrationModelShopper shopper = new RegistrationModelShopper(fb_id, name, phoneNumber, email, password, date_of_birth, profile_pic_url, location, google_location, discount, food_item, latitude, longitude);
                                       shopperRef.child(fb_id).setValue(shopper);
                                       selectHomeActivity(ShopperHomeActivity.class);
                                       et_name.setText("");
                                       et_password.setText("");
                                       et_re_type_password.setText("");
                                       Toast.makeText(EmailSignUpActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();

                                   } else if (userType.equals("customer")) {
                                       RegistrationModelCustomer customer = new RegistrationModelCustomer(fb_id, phoneNumber, email, password, name, date_of_birth, profile_pic_url, profile_pic_url, latitude, longitude, status, bksh, bksh_number, rocket, rocket_number, mCash, mCashNumber, myCash, myCashNumber, uCash, uCashNumber, nogod, nogod_number, sureCash, sureCashNumber);
                                       customerRef.child(fb_id).setValue(customer);
                                       selectHomeActivity(CustomerHomeActivity.class);
                                       et_name.setText("");
                                       et_password.setText("");
                                       et_re_type_password.setText("");
                                       Toast.makeText(EmailSignUpActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                   }
                                   Log.d("TAG", "\nRegistration Successfully to ID: " + fb_id);
                               } else {
                                   Toast.makeText(EmailSignUpActivity.this, "Error Occurred : " + task.getException(), Toast.LENGTH_SHORT).show();
                               }
                           }catch (Exception e){
                               Log.d(TAG, "onComplete: Registration user id: "+fb_id);
                           }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EmailSignUpActivity.this, "Error Occurred : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void selectHomeActivity(Class selectedClass) {
        Log.d(TAG, "selectHomeActivity: " + selectedClass.toString());
        Intent intent = new Intent(EmailSignUpActivity.this, selectedClass);
         try {
             YoYo.with(Techniques.Hinge)
                     .duration(700)
                     .playOn(findViewById(R.id.email_sign_up_page));
         }catch (Exception e){
             Log.d(TAG, "selectHomeActivity: yoyo exception:  "+e);
         }

        startActivity(intent);
        finish();
    }

}