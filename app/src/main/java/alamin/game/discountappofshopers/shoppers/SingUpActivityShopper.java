package alamin.game.discountappofshopers.shoppers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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


public class SingUpActivityShopper extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseReference;
    private String user_uid;
    private EditText et_user_password_shopper;
    private TextView phone_number;
    private String chooser_value;
    private ImageButton signUpbtn_shopper;
    private FirebaseAuth firebaseAuth ;
    private  String pass;
    private PreferenceData preferenceData;
    private String phoneNumber;
    private String Current_user_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_shopper);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        this.preferenceData = new PreferenceData(SingUpActivityShopper.this);

        et_user_password_shopper = findViewById(R.id.et_user_password_shopper);
        phone_number = findViewById(R.id.phone_number);
        
//        databaseReference.child("shopper").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Current_user_uid = dataSnapshot.child("shopper_password").getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
       



        signUpbtn_shopper = findViewById(R.id.signUpbtn_shopper);
        signUpbtn_shopper.setOnClickListener(this);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            chooser_value = bundle.getString("selectFromOTPActivity");
            user_uid = bundle.getString("uid");
            preferenceData.setValue("CurrentUser_Uid",user_uid);
            phoneNumber = bundle.getString("phone_number");

            phone_number.setText(phoneNumber);
        }

       
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUpbtn_shopper:
                setPassword("shopper", ShopperHomeActivity.class);
                break;

        }
    }

    private void setPassword(final String parentName, final Class selectedClass){
        firebaseAuth = FirebaseAuth.getInstance();
        String password = et_user_password_shopper.getText().toString().trim();
        final String retype_password = password ;
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(retype_password)){
            toastMaker("file Empty");
            return;
        }else if (!password.matches(retype_password)){
            toastMaker("password doesn't match");
            return;
        }else {
            login(phoneNumber,retype_password);

//            databaseReference.child(parentName).child(user_uid).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    pass = String.valueOf(dataSnapshot.child("shopper_password").getValue());
//                    if (pass.equals(null)){
//                        databaseReference.child(parentName).child(user_uid).child("shopper_password").setValue(retype_password);
//                        movedNextActivity(selectedClass);
//                    }else if (retype_password.equals(pass)){
//                        movedNextActivity(selectedClass);
//                    }else {
//                        toastMaker("Password Doesn't Mass");
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            databaseReference.child(parentName).child(user_uid).child("shopper_password").setValue(retype_password);
//            movedNextActivity(selectedClass);

        }
    }
    private void movedNextActivity(Class selectedActivity) {
        Intent intent = new Intent(this,selectedActivity);
        intent.putExtra("uid",user_uid);
        startActivity(intent);
    }
    private void toastMaker(String toastText){
        Toast.makeText(this, ""+toastText, Toast.LENGTH_SHORT).show();
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(R.drawable.custom_button_background);
    }
    
    private void login(String phone,String pass){
        String email = phone+"@gmail.com";
         firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()){
                     movedNextActivity(ShopperHomeActivity.class);
                     Toast.makeText(SingUpActivityShopper.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                 }
             }
         })
         .addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(SingUpActivityShopper.this, "Error Occurred"+e.getMessage(), Toast.LENGTH_SHORT).show();
             }
         })    ;    
    }
}
