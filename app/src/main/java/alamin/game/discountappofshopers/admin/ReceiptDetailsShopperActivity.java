package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.ReceiptModelShopper;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class ReceiptDetailsShopperActivity extends AppCompatActivity {
    private String uid;
    private String uid_shopper;
    private DatabaseReference databaseReference;

    private String receipt_no;
    private String receipt_amount;
    private String receipt_discount;
    private String customer_return_value;
    private String receipt_pic_url;
    private String receipt_customer_uid;
    private String collectDate;
    private String receipt_date;
    private String random_key;
    private Boolean paid_status = false;
    private Boolean moneyStatus;
    private TextView tv_paid_btn,tv_reject_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details_shopper);
        tv_paid_btn = findViewById(R.id.tv_paid_btn);
        tv_reject_btn = findViewById(R.id.tv_reject_btn);

        Bundle build = new Bundle();
        build = getIntent().getExtras();
        if (build != null) {
            uid = build.getString("uid");
            retrievedReceiptInfo(uid);
            uid_shopper = build.getString("uid_shopper");
            receipt_customer_uid = build.getString("uid_customer");
            retrievedCustomerInfo(uid_shopper);
        }
        tv_reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptRejectStatus(uid);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    //todo:: we don't need this method so why we don't use this . Bt i don't delete it for think if in future if we need to use it then we will use it;
    private void CollectedMoneyFromShopper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("CollectedReceiptMoneyFromCustomer");
        //pic up current date from my user device
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        collectDate = currentDate.format(calForDate.getTime());

        // pic up current time from my user device
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calForTime.getTime());
        random_key = uid_shopper+receipt_customer_uid+receipt_date+saveCurrentTime;
        ReceiptModelShopper receiptModelShopper = new ReceiptModelShopper(receipt_no,receipt_amount,receipt_discount,customer_return_value,receipt_pic_url,uid_shopper,receipt_customer_uid,collectDate,receipt_date,random_key,paid_status);
        databaseReference.child(random_key).setValue(receiptModelShopper);
    }

    private void retrievedCustomerInfo(String uid_shopper){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(uid_shopper).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelShopper registrationModelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                if (registrationModelShopper != null) {
                    ((TextView) findViewById(R.id.tv_in_shop_name_adminList)).setText(registrationModelShopper.getShop_name());
                    ((TextView) findViewById(R.id.tv_in_shop_location_adminList)).setText(registrationModelShopper.getShop_location_manually());
                    ((TextView) findViewById(R.id.tv_in_shop_phone_adminList)).setText(registrationModelShopper.getShopper_phone());


                    initToolbar(String.valueOf(registrationModelShopper.getShop_name()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    private void retrievedReceiptInfo(String uid){
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReceiptModelCustomer receiptModelCustomer = dataSnapshot.getValue(ReceiptModelCustomer.class);
                if (receiptModelCustomer != null) {
                    moneyStatus = receiptModelCustomer.getCollect_money_from_shop_status();
                    checkStatus(moneyStatus);
                    receipt_no = receiptModelCustomer.getInvoiceNumber();
                    ((TextView) findViewById(R.id.tv_in_voice_number_adminList)).setText(receipt_no);
                    receipt_amount = receiptModelCustomer.getReceipt_amount();
                    ((TextView) findViewById(R.id.tv_total_amount_adminList)).setText(receipt_amount+"/=");
                    receipt_discount = receiptModelCustomer.getDiscount_percentage();
                    ((TextView) findViewById(R.id.tv_discount_adminList)).setText(receipt_discount+"%");
                    customer_return_value = receiptModelCustomer.getDiscount_money();
                    ((TextView) findViewById(R.id.tv_back_amount_adminList)).setText(customer_return_value+"/=");
                    receipt_date = receiptModelCustomer.getReceipt_submit_data();
                    ((TextView) findViewById(R.id.tv_date_adminList)).setText(receipt_date);
                    receipt_pic_url = receiptModelCustomer.getReceipt_pic_url();
                    Picasso.with(ReceiptDetailsShopperActivity.this).load(receipt_pic_url).placeholder(R.drawable.defaultpic).into((ImageView) findViewById(R.id.tv_receiptPic_adminList));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initToolbar(String name) {
        Toolbar toolbar = findViewById(R.id.toolbar_report_list_shopperDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
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
    private void collectMoneyFromShopStatus(String uid){
        Boolean status = true;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        String todayDate = currentDate.format(calForDate.getTime());
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.child(uid).child("collect_money_from_shop_status").setValue(status);
        databaseReference.child(uid).child("collect_money_from_shop_date").setValue(todayDate);
    }

    private void receiptRejectStatus(String uid){

        Boolean status = true;

        //todo:: hey programmer in here String variable rejectedData is use for collect rejected date but we don't use this variable in our project. in future if we need rejectedDate then we will use it
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        String rejectedDate = currentDate.format(calForDate.getTime());


        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.child(uid).child("rejected_status").setValue(status);
        Toast.makeText(ReceiptDetailsShopperActivity.this, "Rejected", Toast.LENGTH_SHORT).show();

    }

    private void checkStatus(Boolean status){
        if (!status){
            tv_paid_btn.setText("Need Collect");
            tv_paid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    collectMoneyFromShopStatus(uid);
                    //CollectedMoneyFromShopper();
                    Toast.makeText(ReceiptDetailsShopperActivity.this, "Collected", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            tv_paid_btn.setText("Collected");
            tv_paid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ReceiptDetailsShopperActivity.this, "already Collected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
