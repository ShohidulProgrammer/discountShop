package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.AcReportListShopperAdapter;
import alamin.game.discountappofshopers.adapter.SelectedShopReportListShoperAdapter;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;

public class SelectedShopReceiptListActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView selected_shop_receipt_list;
    private ArrayList<ReceiptModelCustomer> receiptModelCustomerArrayList = new ArrayList<>();
    private SelectedShopReportListShoperAdapter adapter;
    public static Button btn_submit_selected_value;
    static TextView tv_total_amount;
    private Toolbar selected_shop_toolbar;
    private Map<String,Boolean> statuschange = new HashMap<>();
    TextView tv_update_msg,tv_update_btn;
    private String selected_shop_uid;
    public static String abc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_shop_receipt_list);

        Bundle bundle=getIntent().getExtras();
        selected_shop_uid =getIntent().getStringExtra("uid");

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        selected_shop_receipt_list = findViewById(R.id.selected_shop_receipt_list);
        btn_submit_selected_value = findViewById(R.id.btn_submit_selected_value);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_total_amount.setText(abc);

        selected_shop_toolbar = findViewById(R.id.selected_shop_toolbar);
        setSupportActionBar(selected_shop_toolbar);
        selected_shop_toolbar.setTitle("Shop name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiptModelCustomerArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReceiptModelCustomer receiptModelCustomer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                    if (!receiptModelCustomer.getRejected_status() && !receiptModelCustomer.getBack_money_to_customer_status() && !receiptModelCustomer.getCollect_money_from_shop_status()){
                        //receiptModelCustomerArrayList.add(receiptModelCustomer);
                        if (receiptModelCustomer.getShop_uid().equals(selected_shop_uid)){
                            receiptModelCustomerArrayList.add(receiptModelCustomer);
                        }
                    }
                }
                if (receiptModelCustomerArrayList.size()>0){
                    adapter = new SelectedShopReportListShoperAdapter(SelectedShopReceiptListActivity.this, receiptModelCustomerArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SelectedShopReceiptListActivity.this);
                    selected_shop_receipt_list.setLayoutManager(mLayoutManager);
                    selected_shop_receipt_list.setItemAnimator(new DefaultItemAnimator());
                    selected_shop_receipt_list.setAdapter(adapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*

        btn_submit_selected_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("CalculateCheck");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       // dataSnapshot.getRef().removeValue();
//                        databaseReference.child("value").setValue(null);
//                        databaseReference.child("id").setValue(null);
//                        databaseReference.child("status").setValue(null);
                        Toast.makeText(SelectedShopReceiptListActivity.this, "total submit : "+statuschange.size(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("CalculateCheck");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tv_total_amount.setText("Total : " + dataSnapshot.child("value").getValue().toString() + "/=");
//                if (dataSnapshot != null) {
//                    statuschange.put(dataSnapshot.child("CalculateCheck").child("value").getValue().toString(), Boolean.valueOf((Boolean) dataSnapshot.child("status").getValue()));
//                    tv_total_amount.setText("Total : " + dataSnapshot.child("CalculateCheck").child("value").getValue().toString() + "/=");
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        */
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public static void total_amount(double amount){
        if (amount>0){
            tv_total_amount.setText(String.valueOf(amount));
        }else {
            tv_total_amount.setText("0.0");
        }
    }
}
