package alamin.game.discountappofshopers.shoppers;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.customers.ProfileActivityCustomerEditable;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
public class ProfileActivityShopperEditable extends AppCompatActivity implements View.OnClickListener {
    private static final int PIC_IMAGE_REQUEST = 420;
    private Uri pic_uri;
    private TextView tv_shopper_date_of_birth_edit;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageView iv_profile_picture_shppper;
    private ImageView iv_choose_image_shopper;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private EditText et_shopper_name_edit,et_shopper_email_edit,tv_shopper_location_manually,et_customer_shop_item;
    private Button btn_save_info_shopper;
    private PreferenceData preferenceData;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_shopper_editable);
        this.preferenceData = new PreferenceData(ProfileActivityShopperEditable.this);
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        tv_shopper_location_manually = findViewById(R.id.tv_shopper_location_manually);
        et_customer_shop_item = findViewById(R.id.et_customer_shop_item);

        tv_shopper_date_of_birth_edit = findViewById(R.id.tv_shopper_date_of_birth_edit);
        tv_shopper_date_of_birth_edit.setOnClickListener(this);

        iv_choose_image_shopper = findViewById(R.id.iv_choose_image_shopper);
        iv_choose_image_shopper.setOnClickListener(this);

        iv_profile_picture_shppper = findViewById(R.id.iv_profile_picture_shppper);

        et_shopper_name_edit = findViewById(R.id.et_shopper_name_edit);
        et_shopper_email_edit = findViewById(R.id.et_shopper_email_edit);

        btn_save_info_shopper = findViewById(R.id.btn_save_info_shopper);
        btn_save_info_shopper.setOnClickListener(this);
        retrieveDataFromFirebase ();
        initToolbar();
        initComponent();
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initComponent() {
        final ImageView image = (ImageView) findViewById(R.id.iv_profile_picture_shppper);
        final CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_shopper_date_of_birth_edit:
                dialogDatePickerLight();
                break;
            case R.id.iv_choose_image_shopper:
                chooseimage();
                break;
            case R.id.btn_save_info_shopper:
                save_info_into_database();
                break;
        }
    }
    private void dialogDatePickerLight() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                ProfileActivityShopperEditable.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = month + "/" + day + "/" +year;
                tv_shopper_date_of_birth_edit.setText(date);
            }
        };
    }
    private void chooseimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PIC_IMAGE_REQUEST);
        Toast.makeText(ProfileActivityShopperEditable.this, "plz choose image first", Toast.LENGTH_SHORT).show();
    }
    private void uploadimage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profile_pic");
        firebaseAuth = FirebaseAuth.getInstance();
        final String user_uid = preferenceData.getStringValue("CurrentUser_Uid");

        if (pic_uri != null){
            progressDialog = new ProgressDialog(ProfileActivityShopperEditable.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child(user_uid+".jpg");
            ref.putFile(pic_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivityShopperEditable.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("shopper").child(user_uid).child("shop_pic_url").setValue( uri.toString());
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivityShopperEditable.this, "Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading "+(int)progress+"%");
                        }
                    });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            pic_uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),pic_uri);
                iv_profile_picture_shppper.setImageBitmap(bitmap);
                if (bitmap != null){
                    uploadimage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void retrieveDataFromFirebase (){
        String uid = preferenceData.getStringValue("CurrentUser_Uid");
        databaseReference.child("shopper").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    RegistrationModelShopper registrationModelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                    et_shopper_name_edit.setText(registrationModelShopper.getShop_name());
                    et_shopper_email_edit.setText(registrationModelShopper.getShopper_email());
                    tv_shopper_date_of_birth_edit.setText(registrationModelShopper.getShop_creation_date());
                    tv_shopper_location_manually.setText(registrationModelShopper.getShop_location_manually());
                    et_customer_shop_item.setText(registrationModelShopper.getFood_item());
                    Picasso.with(ProfileActivityShopperEditable.this).load(registrationModelShopper.getShop_pic_url()).placeholder(R.drawable.noimage).into(iv_profile_picture_shppper);

                }
                catch (Exception e){
                    Log.d("TAG", "onDataChange: "+e);
                }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void save_info_into_database(){
        String name = et_shopper_name_edit.getText().toString().trim();
        String email = et_shopper_email_edit.getText().toString().trim();
        String date_of_birth = tv_shopper_date_of_birth_edit.getText().toString().trim();
        String location_manually = tv_shopper_location_manually.getText().toString().trim();
        String food_item = et_customer_shop_item.getText().toString().trim();

        String user_uid = preferenceData.getStringValue("CurrentUser_Uid");
        databaseReference.child("shopper").child(user_uid).child("shop_name").setValue(name);
        databaseReference.child("shopper").child(user_uid).child("shopper_email").setValue(email);
        databaseReference.child("shopper").child(user_uid).child("shop_creation_date").setValue(date_of_birth);
        databaseReference.child("shopper").child(user_uid).child("shop_location_manually").setValue(location_manually);
        databaseReference.child("shopper").child(user_uid).child("food_item").setValue(food_item);
        Intent intent = new Intent(ProfileActivityShopperEditable.this, ShopperHomeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
