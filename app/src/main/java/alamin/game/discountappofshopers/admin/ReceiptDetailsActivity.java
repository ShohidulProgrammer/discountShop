package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class ReceiptDetailsActivity extends AppCompatActivity {
    private String uid;
    private String uid_customer;
    private DatabaseReference databaseReference;
    private TextView tv_paid_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);
        tv_paid_btn = findViewById(R.id.tv_paid_btn);
        Bundle build = new Bundle();
        build = getIntent().getExtras();
        if (build != null) {
            uid = build.getString("uid");
            retrievedReceiptInfo(uid);
            uid_customer = build.getString("uid_customer");
            retrievedCustomerInfo(uid_customer);
        }

        tv_paid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCustomerStatus(uid);
            }
        });
    }
    private void changeCustomerStatus(String uid){
        Boolean status = true;
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.child(uid).child("back_money_to_customer_status").setValue(status);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        String todayDate = currentDate.format(calForDate.getTime());
        databaseReference.child(uid).child("back_money_to_customer_date").setValue(todayDate);
        Intent intent = new Intent(ReceiptDetailsActivity.this, ReportListCustomerActivity.class);
        startActivity(intent);
    }
    private void retrievedCustomerInfo(String uid_customer){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").child(uid_customer).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer registrationModelCustomer = dataSnapshot.getValue(RegistrationModelCustomer.class);
                if (registrationModelCustomer != null) {
                    ((TextView) findViewById(R.id.tv_in_customer_name_adminList)).setText(registrationModelCustomer.getName());
                    ((TextView) findViewById(R.id.tv_in_customer_phone_adminList)).setText(registrationModelCustomer.getPhone());
                    initToolbar(String.valueOf(registrationModelCustomer.getName()));
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
                    checkStatus(receiptModelCustomer.getBack_money_to_customer_status());
                    shopName(receiptModelCustomer.getShop_uid());
                    ((TextView) findViewById(R.id.tv_in_voice_number_adminList)).setText(receiptModelCustomer.getInvoiceNumber());
                    ((TextView) findViewById(R.id.tv_total_amount_adminList)).setText(receiptModelCustomer.getReceipt_amount()+"/=");
                    ((TextView) findViewById(R.id.tv_discount_adminList)).setText(receiptModelCustomer.getDiscount_percentage());
                    ((TextView) findViewById(R.id.tv_back_amount_adminList)).setText(receiptModelCustomer.getDiscount_money()+"/=");
                    ((TextView) findViewById(R.id.tv_payment_gateway_adminList)).setText(receiptModelCustomer.getPaymentMethod()+"/"+receiptModelCustomer.getGateway());
                    ((TextView) findViewById(R.id.tv_date_adminList)).setText(receiptModelCustomer.getCollect_money_from_shop_date());
                    ((TextView) findViewById(R.id.tv_submit_date_adminList)).setText(receiptModelCustomer.getReceipt_submit_data());
                    Picasso.with(ReceiptDetailsActivity.this).load(receiptModelCustomer.getReceipt_pic_url()).placeholder(R.drawable.defaultpic).into((ImageView) findViewById(R.id.tv_receiptPic_adminList));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void shopName(String shop_uid){
        final String[] shop_name = new String[1];
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(shop_uid).child("shop_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //RegistrationModelShopper modelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                shop_name[0] = dataSnapshot.getValue().toString();
                ((TextView) findViewById(R.id.tv_shop_name_adminList)).setText(shop_name[0]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initToolbar(String name) {
        Toolbar toolbar = findViewById(R.id.toolbar_report_list);
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
    private void checkStatus(Boolean status){
        if (!status){
            tv_paid_btn.setText("UnPaid");
            tv_paid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeCustomerStatus(uid);
                    //CollectedMoneyFromShopper();
                    Toast.makeText(ReceiptDetailsActivity.this, "Paid", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            tv_paid_btn.setText("Paid");
            tv_paid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ReceiptDetailsActivity.this, "already Paid", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
