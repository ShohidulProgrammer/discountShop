package alamin.game.discountappofshopers.customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.auth.LoginActivity;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.shoppers.ShopperHomeActivity;

public class SingUpActivityCustomer extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout password_page;
    private TextView et_user_phone_number;
    private ImageButton signUpbtn;
    private String chooser_value;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String user_uid;
    private EditText et_user_password;
    private String pass;
    private String phoneNumber;
    PreferenceData preferenceData;
    private double myLocationLat, myLocationLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        this.preferenceData = new PreferenceData(SingUpActivityCustomer.this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        password_page = findViewById(R.id.password_page);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            chooser_value = bundle.getString("selectFromOTPActivity");
            phoneNumber = bundle.getString("phone_number");
            user_uid = bundle.getString("uid");
            preferenceData.setValue("CurrentUser_Uid",user_uid);
            myLocationLat = bundle.getDouble("myLocationLat");
            myLocationLong = bundle.getDouble("myLocationLong");
        }
        if (bundle == null) {
            databaseReference.child("customer").child(user_uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    et_user_phone_number.setText(String.valueOf(dataSnapshot.child("phone").getValue()));
                    pass = String.valueOf(dataSnapshot.child("password").getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        et_user_password = findViewById(R.id.et_user_password);
        et_user_phone_number = findViewById(R.id.et_user_phone_number);
        et_user_phone_number.setText(phoneNumber);

        signUpbtn = findViewById(R.id.signUpbtn);
        signUpbtn.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpbtn:
                //setPassword("customer", CustomerHomeActivity.class);
                String pass = et_user_password.getText().toString().trim();
                if (TextUtils.isEmpty(pass)){
                    YoYo.with(Techniques.Shake)
                            .duration(1000)
                            .playOn(et_user_password);
                    toastMaker( "password is null");
                    return;
                }else if (pass.length()<6){
                    YoYo.with(Techniques.Shake)
                            .duration(1000)
                            .playOn(et_user_password);
                    toastMaker( "password must be at least 6 characters");
                    return;
                }else {
                    String number = phoneNumber+"@gmail.com";
                    login_method(number,pass);
                }
            break;
        }
    }
    private void setPassword(final String parentName, final Class selectedClass){
        String password = et_user_password.getText().toString().trim();
        final String retype_password = et_user_password.getText().toString().trim();
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(retype_password)){
            toastMaker("file Empty");
            return;
        }else if (!password.matches(retype_password)){
            toastMaker("password and confirm password does not match");
            return;
        }else {

            databaseReference.child(parentName).child(user_uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    et_user_phone_number.setText(String.valueOf(dataSnapshot.child("phone").getValue()));
                    pass = String.valueOf(dataSnapshot.child("password").getValue());
                    if (pass.equals("password//password//password")){
                        databaseReference.child(parentName).child(user_uid).child("password").setValue(retype_password);
                        movedNextActivity(selectedClass);
                    }else if (retype_password.equals(pass)){
                        movedNextActivity(selectedClass);
                    }else {
                        toastMaker("Password Doesn't Mass");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //databaseReference.child(parentName).child(user_uid).child("password").setValue(retype_password);
            //movedNextActivity(selectedClass);
        }
    }
    private void movedNextActivity(Class selectedActivity) {
        Intent intent = new Intent(this,selectedActivity);
        intent.putExtra("myLocationLat",myLocationLat);
        intent.putExtra("myLocationLong",myLocationLong);
        startActivity(intent);
    }
    private void toastMaker(String toastText){
        Toast.makeText(this, ""+toastText, Toast.LENGTH_SHORT).show();
    }
    private void login_method(String phone, String password){
        firebaseAuth.signInWithEmailAndPassword(phone,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    movedNextActivity(CustomerHomeActivity.class);
                    toastMaker("Login Success");
                    YoYo.with(Techniques.Hinge)
                            .duration(1000)
                            .playOn(password_page);
                    finish();
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMaker("Error Occurred : "+e.getMessage());
            }
        });
    }
}
