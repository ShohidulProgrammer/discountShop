package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.AcReportListAdapter;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.ReceiptModelShopper;

public class ReportListCustomerActivity extends AppCompatActivity {
    private RecyclerView rv_reportList_in_admin_panel;
    private AcReportListAdapter reportListAdapter;
    private ArrayList<ReceiptModelCustomer> receiptModelCustomerArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        initToolbar("Report List");
        rv_reportList_in_admin_panel = findViewById(R.id.rv_reportList_in_admin_panel);
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiptModelCustomerArrayList.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ReceiptModelCustomer receiptModelCustomer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                        if (receiptModelCustomer != null) {
                            if (receiptModelCustomer.getCollect_money_from_shop_status() == true) {
                                receiptModelCustomerArrayList.add(receiptModelCustomer);
                            }
                        }
                    }
                } catch (Exception e) {

                }

                if (receiptModelCustomerArrayList.size() > 0) {
                    reportListAdapter = new AcReportListAdapter(ReportListCustomerActivity.this, receiptModelCustomerArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReportListCustomerActivity.this);
                    rv_reportList_in_admin_panel.setLayoutManager(mLayoutManager);
                    rv_reportList_in_admin_panel.setItemAnimator(new DefaultItemAnimator());
                    rv_reportList_in_admin_panel.setAdapter(reportListAdapter);
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                reportListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                reportListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }

        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
