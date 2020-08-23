package alamin.game.discountappofshopers.customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import alamin.game.discountappofshopers.customers.fragment.ProfileFragmentCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.ProfileActivityShopperEditable;

public class ProfileActivityCustomerEditable extends AppCompatActivity implements View.OnClickListener {
    private static final int PIC_IMAGE_REQUEST =420 ;
    private TextView tv_customer_date_of_birth_edit,tv_customer_phone_number,bkash,rocket,surecash,mcash,mycash,ucash;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageView iv_choose_image_customer;
    private CircularImageView iv_profile_picture_customer;
    private Uri pic_uri;
    private RadioGroup rg_choose_gender_customer;
    private RadioButton radioButton_gender,rb_choose_gender_male_customer;
    private Button btn_save_info_customer,btn_cancel_info_customer;
    private EditText et_customer_name_edit,et_customer_email_edit,bkash_number,rocket_number,surecash_number,mcash_number,mycash_number,ucash_number,nogod_number;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private String gender;
    private String CurrentUser_Uid;
    private PreferenceData preferenceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer_editable);
        this.preferenceData = new PreferenceData(ProfileActivityCustomerEditable.this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        CurrentUser_Uid = preferenceData.getStringValue("CurrentUser_Uid");
        tv_customer_date_of_birth_edit = findViewById(R.id.tv_customer_date_of_birth_edit);
        tv_customer_date_of_birth_edit.setOnClickListener(this);

        iv_choose_image_customer = findViewById(R.id.iv_choose_image_customer);
        iv_choose_image_customer.setOnClickListener(this);

        iv_profile_picture_customer = findViewById(R.id.iv_profile_picture_customer);

        btn_save_info_customer = findViewById(R.id.btn_save_info_customer);
        btn_save_info_customer.setOnClickListener(this);

        btn_cancel_info_customer = findViewById(R.id.btn_cancel_info_customer);
        btn_cancel_info_customer.setOnClickListener(this);

        et_customer_name_edit = findViewById(R.id.et_customer_name_edit);
        et_customer_email_edit = findViewById(R.id.et_customer_email_edit);
        tv_customer_phone_number = findViewById(R.id.tv_customer_phone_number);

        bkash_number = findViewById(R.id.bkash_nmbr);
        rocket_number  =  findViewById(R.id.rocket_nmbr);
        surecash_number = findViewById(R.id.surecash_nmbr);
        mcash_number = findViewById(R.id.mcash_nmbr);
        mycash_number = findViewById(R.id.mycash_nmbr);
        ucash_number = findViewById(R.id.ucash_nmbr);
        nogod_number = findViewById(R.id.nogod_nmbr);

        rg_choose_gender_customer = (RadioGroup) findViewById(R.id.rg_choose_gender_customer);
        rb_choose_gender_male_customer = findViewById(R.id.rb_choose_gender_male_customer);

        retrieveShopInfo(CurrentUser_Uid);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_customer_date_of_birth_edit:
                dialogDatePickerLight();
                break;
            case R.id.iv_choose_image_customer:
                chooseimage();
                break;
            case R.id.btn_save_info_customer:
                save_info_into_database();
                break;
        }
    }
    private void save_info_into_database(){
        String name = et_customer_name_edit.getText().toString().trim();
        String email = et_customer_email_edit.getText().toString().trim();
        String date_of_birth = tv_customer_date_of_birth_edit.getText().toString().trim();
        String gender = choose_gender();

        String bkash_nmbr = bkash_number.getText().toString().trim();
        String rocket_nmbr = rocket_number.getText().toString().trim();
        String sure_nmbr = surecash_number.getText().toString().trim();
        String mcash_nmbr = mcash_number.getText().toString().trim();
        String mycash_nmbr = mycash_number.getText().toString().trim();
        String ucash_nmbr = ucash_number.getText().toString().trim();
        String nogod_nmbr = nogod_number.getText().toString().trim();

        String user_uid = CurrentUser_Uid;
        databaseReference.child("customer").child(user_uid).child("name").setValue(name);
        databaseReference.child("customer").child(user_uid).child("email").setValue(email);
        databaseReference.child("customer").child(user_uid).child("date_of_birth").setValue(date_of_birth);
        databaseReference.child("customer").child(user_uid).child("gender").setValue(gender);

        databaseReference.child("customer").child(user_uid).child("biksh_number").setValue(bkash_nmbr);
        databaseReference.child("customer").child(user_uid).child("rocket_number").setValue(rocket_nmbr);
        databaseReference.child("customer").child(user_uid).child("sureCash_number").setValue(sure_nmbr);
        databaseReference.child("customer").child(user_uid).child("mCash_number").setValue(mcash_nmbr);
        databaseReference.child("customer").child(user_uid).child("myCash_number").setValue(mycash_nmbr);
        databaseReference.child("customer").child(user_uid).child("uCash_number").setValue(ucash_nmbr);
        databaseReference.child("customer").child(user_uid).child("nogod_number").setValue(nogod_nmbr);

        Intent intent = new Intent(ProfileActivityCustomerEditable.this, CustomerHomeActivity.class);
        startActivity(intent);
    }
    private void dialogDatePickerLight() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                ProfileActivityCustomerEditable.this,
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
                tv_customer_date_of_birth_edit.setText(date);
            }
        };
    }
    private void chooseimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PIC_IMAGE_REQUEST);
        Toast.makeText(ProfileActivityCustomerEditable.this, "plz choose image first", Toast.LENGTH_SHORT).show();
    }
    private void uploadimage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profile_pic");
        firebaseAuth = FirebaseAuth.getInstance();
        final String user_uid = CurrentUser_Uid;

        if (pic_uri != null){
            progressDialog = new ProgressDialog(ProfileActivityCustomerEditable.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child(user_uid+".jpg");
            ref.putFile(pic_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivityCustomerEditable.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("customer").child(user_uid).child("profile_pic_url").setValue( uri.toString());
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivityCustomerEditable.this, "Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
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
                iv_profile_picture_customer.setImageBitmap(bitmap);
                if (bitmap != null){
                    uploadimage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String choose_gender() {
        rg_choose_gender_customer = (RadioGroup) findViewById(R.id.rg_choose_gender_customer);
        int selectedId = rg_choose_gender_customer.getCheckedRadioButtonId();
        radioButton_gender = (RadioButton) findViewById(selectedId);
        return String.valueOf(radioButton_gender.getText());
    }

    private void retrieveShopInfo(String uid){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer customer = dataSnapshot.getValue(RegistrationModelCustomer.class);
                et_customer_name_edit.setText(customer.getName());
                et_customer_email_edit.setText(customer.getEmail());
                tv_customer_date_of_birth_edit.setText(customer.getDate_of_birth());
                tv_customer_phone_number.setText(customer.getPhone());
                bkash_number.setText(customer.getBiksh_number());
                rocket_number.setText(customer.getRocket_number());
                surecash_number.setText(customer.getSureCash_number());
                mcash_number.setText(customer.getmCash_number());
                mycash_number.setText(customer.getMyCash_number());
                ucash_number.setText(customer.getuCash_number());
                nogod_number.setText(customer.getNogod_number());

                gender = customer.getGender();
                if (gender.equals("Male")) {
                    rg_choose_gender_customer.check(R.id.rb_choose_gender_male_customer);
                }else if(gender.equals("Female")){
                    rg_choose_gender_customer.check(R.id.rg_choose_gender_femal_customer);
                }
                Picasso.with(getApplicationContext()).load(customer.getProfile_pic_url()).placeholder(R.drawable.defaultpic).into(iv_profile_picture_customer);
                initToolbar(customer.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initToolbar(String shopName) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(shopName+"'s Profile Editing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDialogBox(){
        final Dialog dialog = new Dialog(ProfileActivityCustomerEditable.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_and_shoper);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView)dialog.findViewById(R.id.tv_shopper)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ProfileActivityCustomerEditable.this, "shopper", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        ((TextView)dialog.findViewById(R.id.tv_customer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ProfileActivityCustomerEditable.this, "customer", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


}
