package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
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
import alamin.game.discountappofshopers.adapter.CustomerListAdapter;
import alamin.game.discountappofshopers.adapter.MarketingPeopleAdapter;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.model.MarketingPeopleModel;

public class MerchantCreateActivity extends AppCompatActivity {

    int referralNumber = 1001;
    FloatingActionButton add_new_one;
    RecyclerView merchant_list;
    DatabaseReference databaseReference,markectorList,marketorContainer;
    MarketingPeopleAdapter adapter;
    List<MarketingPeopleModel> list = new ArrayList<>();
    Toolbar toolbar;
    String marketerContainerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_create);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Marketer List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        add_new_one = findViewById(R.id.add_new_one);
        merchant_list = findViewById(R.id.merchant_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("marketingPeopleList");
        marketorContainer = FirebaseDatabase.getInstance().getReference().child("Admin");

        marketorContainer.child("marketerContainer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                marketerContainerValue = dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_new_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        markectorList = FirebaseDatabase.getInstance().getReference("marketingPeopleList");
        markectorList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MarketingPeopleModel model = dataSnapshot1.getValue(MarketingPeopleModel.class);
                    list.add(model);
                }
                if (list.size()>0){
                    adapter = new MarketingPeopleAdapter(list,MerchantCreateActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MerchantCreateActivity.this);
                    merchant_list.setLayoutManager(mLayoutManager);
                    merchant_list.setItemAnimator(new DefaultItemAnimator());
                    merchant_list.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
        markectorList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MarketingPeopleModel model = dataSnapshot1.getValue(MarketingPeopleModel.class);
                    list.add(model);
                }
                if (list.size()>0){
                    adapter = new MarketingPeopleAdapter(list,MerchantCreateActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MerchantCreateActivity.this);
                    merchant_list.setLayoutManager(mLayoutManager);
                    merchant_list.setItemAnimator(new DefaultItemAnimator());
                    merchant_list.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }

    private void showCustomDialog() {

        final Dialog dialog = new Dialog(MerchantCreateActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.marchant_list_add);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final String referral_cont = String.valueOf(Integer.valueOf(marketerContainerValue)+1);
        
        final ImageView close = dialog.findViewById(R.id.close);

        final TextView referral = dialog.findViewById(R.id.referral_number);
        referral.setText(String.valueOf(referral_cont));
        final TextView name = dialog.findViewById(R.id.name);
        final TextView phone = dialog.findViewById(R.id.phone);

        Button submit = dialog.findViewById(R.id.button);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nm = name.getText().toString().toString().trim();
                String phn = phone.getText().toString().toString().trim();

                if (TextUtils.isEmpty(nm) && TextUtils.isEmpty(phn)){
                    Toast.makeText(MerchantCreateActivity.this, "field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }else if (phn.length()<10){
                    Toast.makeText(MerchantCreateActivity.this, "Phone Number Incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Map<String,Object> marketingPepLst = new HashMap<>();
                    marketingPepLst.put("name",nm);
                    marketingPepLst.put("phone",phn);
                    marketingPepLst.put("referral",referral_cont);

                    databaseReference.push().setValue(marketingPepLst);
                    marketorContainer.child("marketerContainer").setValue(referral_cont);
                    Toast.makeText(MerchantCreateActivity.this, "Submit", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
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
