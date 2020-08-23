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
import alamin.game.discountappofshopers.model.RegistrationModelShopper;


public class ShopperProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ImageView iV_shop_profile;
    private String uid = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_profile);
        iV_shop_profile = findViewById(R.id.iV_shop_profile);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null){
            uid = b.getString("uid");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelShopper registrationModelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                if (registrationModelShopper != null) {
                    ((TextView) findViewById(R.id.tv_shop_name_by_admin)).setText(registrationModelShopper.getShop_name());
                    ((TextView) findViewById(R.id.tv_shop_location_by_admin)).setText(registrationModelShopper.getShop_location_manually());
                    ((TextView) findViewById(R.id.tv_shop_phone_by_admin)).setText(registrationModelShopper.getShopper_phone());
                    ((TextView) findViewById(R.id.tv_shop_email_by_admin)).setText(registrationModelShopper.getShopper_email());
                    ((TextView) findViewById(R.id.tv_shop_create_date_by_admin)).setText(registrationModelShopper.getShop_creation_date());
                    ((TextView) findViewById(R.id.tv_shop_item_by_admin)).setText(registrationModelShopper.getFood_item());
                    Picasso.with(ShopperProfileActivity.this).load(registrationModelShopper.getShop_pic_url()).placeholder(R.drawable.loadingimg).into(iV_shop_profile);
                    initToolbar(String.valueOf(registrationModelShopper.getShop_name()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initToolbar(String name) {
        Toolbar toolbar =  findViewById(R.id.toolbarShopProfileAdminPanel);
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
