package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;

public class CustomerProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String uid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null){
            uid = b.getString("uid");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer registrationModelCustomer = dataSnapshot.getValue(RegistrationModelCustomer.class);
                if (registrationModelCustomer != null) {
                    ((TextView) findViewById(R.id.tv_customer_name_in_admin)).setText(registrationModelCustomer.getName());
                    ((TextView) findViewById(R.id.tv_customer_phone_in_admin)).setText(registrationModelCustomer.getPhone());
                    ((TextView) findViewById(R.id.tv_customer_email_in_admin)).setText(registrationModelCustomer.getEmail());
                    ((TextView) findViewById(R.id.tv_customer_gender_in_admin)).setText(registrationModelCustomer.getGender());
                    ((TextView) findViewById(R.id.tv_customer_dateOfBirth_in_admin)).setText(registrationModelCustomer.getDate_of_birth());
                    Picasso.with(CustomerProfileActivity.this).load(registrationModelCustomer.getProfile_pic_url()).placeholder(R.drawable.defaultpic).into(((ImageView) findViewById(R.id.iv_customer_profile_pic_for_admin_panel)));
                    initToolbar(registrationModelCustomer.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initToolbar(String name) {
        Toolbar toolbar =  findViewById(R.id.toolbarCustomerProfileAdminPanel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
