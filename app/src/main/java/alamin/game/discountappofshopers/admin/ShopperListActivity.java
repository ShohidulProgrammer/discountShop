package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.ShopListAdapter;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class ShopperListActivity extends AppCompatActivity {
    private Toolbar toolbar_shop_list;
    private RecyclerView rv_shopperLit;
    private ArrayList<RegistrationModelShopper> shopperArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ShopListAdapter shopListAdapter;
    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_list);
        rv_shopperLit = findViewById(R.id.rv_shopperLit);
        toolbar_shop_list = findViewById(R.id.toolbar_shop_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        retrieveDataFromShopperList();
        toolbar_shop_list.setTitle("Shopper List");
        setSupportActionBar(toolbar_shop_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //initComponent();
    }
    /*
    private void initComponent() {
        shopListAdapter = new ShopListAdapter(this, shopperArrayList);
        shopListAdapter.setOnItemClickListener(new ShopListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RegistrationModelShopper obj, int position) {
                showCustomDialog(obj);
            }
        });
        showCustomDialog(shopperArrayList.get(0));
    }
    private void showCustomDialog(RegistrationModelShopper p) {

//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.set    ContentView(R.layout.review_submit_from_customer_in_shopper_profile_item_layout);
//        dialog.setCancelable(true);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        ((TextView) dialog.findViewById(R.id.title)).setText(p.getShop_name());
//        ((CircularImageView) dialog.findViewById(R.id.image)).setImageResource(R.drawable.defaultpic);
//
//        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//        dialog.getWindow().setAttributes(lp);

    }  */

    public void retrieveDataFromShopperList() {
        databaseReference.child("shopper").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopperArrayList.clear();
                int itemCount = 0;
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        itemCount++;
                        Log.d("firebase", "\n\n\n\nsnapshot Number: " + itemCount + " Firebase dataSnapshot1: " + dataSnapshot1 + "\n\n\n\n");
                        RegistrationModelShopper registrationModelShopper = dataSnapshot1.getValue(RegistrationModelShopper.class);
                        shopperArrayList.add(registrationModelShopper);
                    }

                } catch (Exception e) {
                    Log.e("firebase error", "\n\n\n\nsnapshot No: " + itemCount + " Firebase error: " + e.getMessage());
                }

                if (shopperArrayList.size() > 0) {
                    shopListAdapter = new ShopListAdapter(ShopperListActivity.this, shopperArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShopperListActivity.this);
                    rv_shopperLit.setLayoutManager(mLayoutManager);
                    rv_shopperLit.setItemAnimator(new DefaultItemAnimator());
                    rv_shopperLit.setAdapter(shopListAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                shopListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                shopListAdapter.getFilter().filter(query);
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
