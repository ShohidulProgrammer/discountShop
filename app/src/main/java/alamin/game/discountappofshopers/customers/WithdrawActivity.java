package alamin.game.discountappofshopers.customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.widgets.WidgetContainer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.CustomerWithdrawalStatusAdapter;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;

public class WithdrawActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {
    private String[] paymentMethod = { "Select","Bkash", "Rocket", "SureCash", "MCash", "MyCash","UCash","Nogod","Others"};
    private String bkashNumber,rocketNumber,sureCashNumber,mCashNumber,myCashNumber,uCashNumber,Nogod;
    private String payment_mehtod,gateway;
    private TextView payment_number;
    private TextView name;
    private EditText et_payment_number,et_payment_method;
    private CircularImageView profile_image;
    private Bundle bundle;
    private String current_balance;
    private TextView balance_valuel;

    private DatabaseReference databaseReference,collectMoneyRef;
    private FirebaseAuth firebaseAuth;
    private String CurrentUserId;
    private float customer_discound_money =0;

    private List<ReceiptModelCustomer> receiptModelCustomerList = new ArrayList<>();
    private Map<String,Boolean>  withdraw_req_list = new HashMap<>();
    private Button btn_withdraw_request;

    private String current_user_name;
    private String current_user_phone;
    private String current_user_pic_url;
    private String number;
    private String Current_user_password;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Withdraw Request..");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        balance_valuel = findViewById(R.id.tv_balance_value);
        btn_withdraw_request = findViewById(R.id.btn_withdraw_request);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null){
            current_balance = bundle.getString("User_balance");
            //balance_valuel.setText(current_balance);
        }
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        name = findViewById(R.id.name);
        profile_image = findViewById(R.id.profile_pic);
        payment_number = findViewById(R.id.payment_number);
        et_payment_number = findViewById(R.id.et_payment_number);
        et_payment_method = findViewById(R.id.et_payment_method);


        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,paymentMethod);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("customer");
        collectMoneyRef = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            CurrentUserId = firebaseAuth.getCurrentUser().getUid();
            databaseReference.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Current_user_password = dataSnapshot.child("password").getValue().toString();
                    current_user_name = dataSnapshot.child("name").getValue().toString();
                    current_user_phone = dataSnapshot.child("phone").getValue().toString();
                    current_user_pic_url = dataSnapshot.child("profile_pic_url").getValue().toString();
                    Picasso.with(WithdrawActivity.this).load(dataSnapshot.child("profile_pic_url").getValue().toString()).placeholder(R.drawable.defaultpic).into(profile_image);
                    name.setText(dataSnapshot.child("name").getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            collectMoneyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        ReceiptModelCustomer receiptModelCustomer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                        if (receiptModelCustomer.getCustomer_uid().toString().equals(CurrentUserId)){
                            receiptModelCustomerList.add(receiptModelCustomer);
                            if (receiptModelCustomer.getCollect_money_from_shop_status()&& !receiptModelCustomer.getWithdrawal_request_status()){
                                withdraw_req_list.put(receiptModelCustomer.getRandom_key(),receiptModelCustomer.getWithdrawal_request_status());
                                customer_discound_money = customer_discound_money + Float.valueOf(receiptModelCustomer.getDiscount_money());
                            }
                        }
                    }
                    balance_valuel.setText(String.valueOf(customer_discound_money));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        /*
        btn_withdraw_request.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String payment_gateway_with_number;
                String running_date,running_time;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
                running_date = currentDate.format(calForDate.getTime());
                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                running_time = currentTime.format(calForTime.getTime());
                for (Map.Entry<String, Boolean> current_list : withdraw_req_list.entrySet() ){


                    String val = current_list.getKey();
                    Boolean res = withdraw_req_list.get(current_list.getKey());

                    Animator scale = ObjectAnimator.ofPropertyValuesHolder(v,
                            PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1.5f, 1),
                            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 1.5f, 1)
                    );
                    scale.setDuration(1000);
                    scale.start();
                    number = payment_number.getText().toString().trim();
                    payment_gateway_with_number = number+" ("+gateway+")";

                    collectMoneyRef.child(val).child("gateway").setValue(payment_gateway_with_number);
                    collectMoneyRef.child(val).child("withdraw_request_date").setValue(running_date);
                    collectMoneyRef.child(val).child("withdraw_request_time").setValue(running_time);
                    collectMoneyRef.child(val).child("withdrawal_request_status").setValue(true);
                    Toast.makeText(WithdrawActivity.this, "Withdraw Request Send Successfully", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        */

        btn_withdraw_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPasswordDialog();
            }
        });
    }

    private void getPasswordDialog() {

        final Dialog dialog = new Dialog(WithdrawActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.review_submit_from_customer_in_shopper_profile_item_layout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CircularImageView profile = dialog.findViewById(R.id.civ_profile_picture_customer_in_dialog_box);
        TextView name = dialog.findViewById(R.id.tv_customer_name_in_dialog_box);
        TextView phone = dialog.findViewById(R.id.tv_customer_phone_number_in_dialog_box);

        retreivedCustomerInfo(profile,name,phone);

        final EditText et_pass = (EditText) dialog.findViewById(R.id.et_post);
        password_section(et_pass);

        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(WithdrawActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_pass.getText().toString().trim();
                if (password.isEmpty()) {
                    Toast.makeText(WithdrawActivity.this, "Please fill review text", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!Current_user_password.equals(password)){
                    Toast.makeText(WithdrawActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    confirmTransactionDialog();
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    private void password_section(EditText password_filed){
        password_filed.setHint("Enter password");
        password_filed.setMinLines(2);
        password_filed.setPadding(10,20,5,20);
        password_filed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void retreivedCustomerInfo(CircularImageView userprofile, TextView username, TextView userphone) {
        username.setText(current_user_name);
        userphone.setText(current_user_phone);
        Picasso.with(WithdrawActivity.this).load(current_user_pic_url).placeholder(R.drawable.defaultpic).into(userprofile);
    }

    @Override
    public void onItemSelected(final AdapterView<?> arg0, View arg1, final int position, long id) {
        databaseReference.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(WithdrawActivity.this).load(dataSnapshot.child("profile_pic_url").getValue().toString()).placeholder(R.drawable.defaultpic).into(profile_image);
                name.setText(dataSnapshot.child("name").getValue().toString());
                if (paymentMethod[position].equals("Bkash")){
                    payment_mehtod = dataSnapshot.child("biksh_number").getValue().toString();
                    et_payment_number.setVisibility(View.GONE);
                    et_payment_method.setVisibility(View.GONE);
                    payment_number.setText(payment_mehtod);
                }else if(paymentMethod[position].equals("Rocket")){
                    payment_mehtod = dataSnapshot.child("rocket_number").getValue().toString();
                    et_payment_number.setVisibility(View.GONE);
                    et_payment_method.setVisibility(View.GONE);
                    payment_number.setText(payment_mehtod);
                }else if(paymentMethod[position].equals("SureCash")){
                    payment_mehtod = dataSnapshot.child("sureCash_number").getValue().toString();
                    et_payment_number.setVisibility(View.GONE);
                    et_payment_method.setVisibility(View.GONE);
                    payment_number.setText(payment_mehtod);
                }else if(paymentMethod[position].equals("MCash")){
                    payment_mehtod = dataSnapshot.child("mCash_number").getValue().toString();
                    et_payment_number.setVisibility(View.GONE);
                    et_payment_method.setVisibility(View.GONE);
                    payment_number.setText(payment_mehtod);
                }else if(paymentMethod[position].equals("MyCash")){
                    payment_mehtod = dataSnapshot.child("myCash_number").getValue().toString();
                    et_payment_number.setVisibility(View.GONE);
                    et_payment_method.setVisibility(View.GONE);
                    payment_number.setText(payment_mehtod);
                }else if(paymentMethod[position].equals("UCash")){
                    payment_mehtod = dataSnapshot.child("uCash_number").getValue().toString();
                    et_payment_number.setVisibility(View.GONE);
                    et_payment_method.setVisibility(View.GONE);
                    payment_number.setText(payment_mehtod);
                }else if(paymentMethod[position].equals("Nogod")){
                    payment_mehtod = dataSnapshot.child("nogod_number").getValue().toString();
                    et_payment_number.setVisibility(View.GONE);
                    et_payment_method.setVisibility(View.GONE);
                    payment_number.setText(payment_mehtod);
                }else if (paymentMethod[position].equals("Others")){
                    et_payment_number.setVisibility(View.VISIBLE);
                    et_payment_method.setVisibility(View.VISIBLE);
                    payment_number.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gateway = paymentMethod[position].toString().trim();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }



    private void confirmTransactionDialog() {

        final Dialog dialog = new Dialog(WithdrawActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_transaction_layout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CircularImageView customer_profile_pic = dialog.findViewById(R.id.customer_profile_pic);
        TextView customer_name = dialog.findViewById(R.id.customer_name);
        TextView customer_phone_number = dialog.findViewById(R.id.customer_phone_number);
        retreivedCustomerInfo(customer_profile_pic,customer_name,customer_phone_number);

        TextView tv_total_amount = dialog.findViewById(R.id.tv_total_amount);
        tv_total_amount.setText(String.valueOf(customer_discound_money));

        TextView payment_method = dialog.findViewById(R.id.payment_method);
        payment_method.setText(gateway);

        final TextView payment_number = dialog.findViewById(R.id.payment_number);
        payment_number.setText(payment_mehtod);

        RelativeLayout confirm_btn_layout = dialog.findViewById(R.id.confirm_btn_layout);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);
        final ProgressDialog csprogress=new ProgressDialog(WithdrawActivity.this);

        confirm_btn_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Animator scale = ObjectAnimator.ofPropertyValuesHolder(v,
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1.5f, 1),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 1.5f, 1)
                );
                scale.setDuration(1000);
                scale.start();

                String payment_gateway_with_number;
                String running_date,running_time;

                //pic up current date from my user device
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
                running_date = currentDate.format(calForDate.getTime());

                // pic up current time from my user device
                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                running_time = currentTime.format(calForTime.getTime());

                for (Map.Entry<String, Boolean> current_list : withdraw_req_list.entrySet()){
                    String val = current_list.getKey();
                    Boolean res = withdraw_req_list.get(current_list.getKey());

                    number = payment_number.getText().toString().trim();
                    payment_gateway_with_number = number+" ("+gateway+")";

                    collectMoneyRef.child(val).child("gateway").setValue(payment_gateway_with_number);
                    collectMoneyRef.child(val).child("withdraw_request_date").setValue(running_date);
                    collectMoneyRef.child(val).child("withdraw_request_time").setValue(running_time);
                    collectMoneyRef.child(val).child("withdrawal_request_status").setValue(true);
                    Toast.makeText(WithdrawActivity.this, "Withdraw Request Send Successfully", Toast.LENGTH_SHORT).show();
                }
                //this is for loading
                csprogress.setMessage("Loading...");
                csprogress.show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        csprogress.dismiss();
                    }
                }, 1000);
                return false;
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}