package alamin.game.discountappofshopers.admin;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.CustomerListAdapter;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;

public class CustomerListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCustomerList;
    private ArrayList<RegistrationModelCustomer> customerArrayList = new ArrayList<RegistrationModelCustomer>();
    private CustomerListAdapter customerListAdapter;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private ProgressBar pr_customer_list_admin_panel;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private TextView textView;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        recyclerViewCustomerList = findViewById(R.id.rv_customer_list_admin_page);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbarCustomerListActivity);
        toolbar.setTitle("Customer List");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        pr_customer_list_admin_panel = findViewById(R.id.pr_customer_list_admin_panel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrievedCustomerData();
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 10;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            pr_customer_list_admin_panel.setProgress(progressStatus);
                            textView.setText(progressStatus + "/" + pr_customer_list_admin_panel.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void retrievedCustomerData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerArrayList.clear();
               try {
                   for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                       RegistrationModelCustomer registrationModelCustomer = dataSnapshot1.getValue(RegistrationModelCustomer.class);
                       customerArrayList.add(registrationModelCustomer);
                   }
               }catch (Exception e){

               }
                if (customerArrayList.size() > 0) {
                    customerListAdapter = new CustomerListAdapter(CustomerListActivity.this, customerArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CustomerListActivity.this);
                    recyclerViewCustomerList.setLayoutManager(mLayoutManager);
                    recyclerViewCustomerList.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewCustomerList.setAdapter(customerListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerListActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.toolbarCustomerListActivity) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customerListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                customerListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}