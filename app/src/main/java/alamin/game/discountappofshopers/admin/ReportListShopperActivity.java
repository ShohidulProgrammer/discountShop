package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.AcReportListShopperAdapter;
import alamin.game.discountappofshopers.adapter.ShopListAdapter;
import alamin.game.discountappofshopers.adapter.ShopListAdapter1;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.ReceiptModelShopper;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

import static alamin.game.discountappofshopers.admin.DashboardActivity.isShopFound;

public class ReportListShopperActivity extends AppCompatActivity {
    private RecyclerView rv_reportList_shopper_in_admin_panel;
    private DatabaseReference databaseReference;
    private ArrayList<ReceiptModelCustomer> receiptModelCustomerArrayList = new ArrayList<>();
    private ArrayList<ReceiptModelCustomer> receiptModelCustomerArrayListFilter = new ArrayList<>();
    LinkedHashSet<String> listToSet;
    private ArrayList<String> receiptModelCustomerArrayListWithoutDuplicate;
    private AcReportListShopperAdapter reportListAdapter;
    private ShopListAdapter1 shopListAdapter1;
    private SearchView searchView;
    private ArrayList<RegistrationModelShopper> shopperArrayList = new ArrayList<>();

    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list_shopper);
        initToolbar("Shopper Report List");
        rv_reportList_shopper_in_admin_panel = findViewById(R.id.rv_reportList_shopper_in_admin_panel);

        retrieveDataFromShopperList();

        /*
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiptModelCustomerArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReceiptModelCustomer receiptModelCustomer = dataSnapshot1.getValue(ReceiptModelCustomer.class);

                    list.add(receiptModelCustomer.getShop_uid());

                    if (isShopFound(receiptModelCustomer.getShop_uid())){
                        receiptModelCustomerArrayList.add(receiptModelCustomer);
                    }
                    listToSet = new LinkedHashSet<String>(list);
                    receiptModelCustomerArrayListWithoutDuplicate = new ArrayList<>(listToSet);
                    for(int i =0; i < receiptModelCustomerArrayListWithoutDuplicate.size(); i++) {
                        if (receiptModelCustomerArrayListWithoutDuplicate.get(i).equals(receiptModelCustomer.getShop_uid())){

                       }
                    }
                }

                if (receiptModelCustomerArrayList.size()>0){
                    reportListAdapter = new AcReportListShopperAdapter(ReportListShopperActivity.this, receiptModelCustomerArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReportListShopperActivity.this);
                    rv_reportList_shopper_in_admin_panel.setLayoutManager(mLayoutManager);
                    rv_reportList_shopper_in_admin_panel.setItemAnimator(new DefaultItemAnimator());
                    rv_reportList_shopper_in_admin_panel.setAdapter(reportListAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */

    }

    private void initToolbar(String name) {
        Toolbar toolbar = findViewById(R.id.toolbar_report_list_shopper);
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

        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                reportListAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                reportListAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    public void retrieveDataFromShopperList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopperArrayList.clear();
                int i = 0;

                Map<String, Object> objectMap = (HashMap<String, Object>)
                        dataSnapshot.getValue();
                Log.d("firebase", "\n\n\n\nFirebase objectMap: " + objectMap.values());

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    i++;
                    try {
                        Log.d("firebase", "\n\n\n\nsnapshot No: " + i + " Firebase dataSnapshot1: " + dataSnapshot1 + "\n\n\n\n");
                        RegistrationModelShopper registrationModelShopper = dataSnapshot1.getValue(RegistrationModelShopper.class);
                        shopperArrayList.add(registrationModelShopper);
                    } catch (Exception e) {
                        Log.e("firebase error", "\n\n\n\nsnapshot No: "+i+" Firebase error: " + e.getMessage());
                    }
                }
                Toast.makeText(ReportListShopperActivity.this, "total " + shopperArrayList.size(), Toast.LENGTH_SHORT).show();
                if (shopperArrayList.size() > 0) {
                    shopListAdapter1 = new ShopListAdapter1(ReportListShopperActivity.this, shopperArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReportListShopperActivity.this);
                    rv_reportList_shopper_in_admin_panel.setLayoutManager(mLayoutManager);
                    rv_reportList_shopper_in_admin_panel.setItemAnimator(new DefaultItemAnimator());
                    rv_reportList_shopper_in_admin_panel.setAdapter(shopListAdapter1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
    }

    /*

    private Boolean isShopFound(String shop){
        for (int i =0; i < list.size(); i++){
            if (list.get(i).equals(shop)){
                return false;
            }else
                return true;
        }
        return false;
    }

     */
}
