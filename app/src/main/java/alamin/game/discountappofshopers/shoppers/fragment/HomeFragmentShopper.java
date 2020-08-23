package alamin.game.discountappofshopers.shoppers.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.ACRprtCustmrListInShopHomeAdptr;
import alamin.game.discountappofshopers.model.ReceiptModelShopper;

public class HomeFragmentShopper extends Fragment implements SearchView.OnQueryTextListener{

    private SearchView searchView;
    private RecyclerView rl_customer_list_in_my_shop;
    private DatabaseReference databaseReference;
    private ReceiptModelShopper receiptModelShopper;
    private FirebaseAuth firebaseAuth;
    private ACRprtCustmrListInShopHomeAdptr acRprtCustmrListInShopHomeAdptr;
    private List<ReceiptModelShopper> receiptModelShopperList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
               View root = inflater.inflate(R.layout.fragment_home_shopper, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        firebaseAuth = FirebaseAuth.getInstance();
        System.out.println("==========================="+firebaseAuth.getCurrentUser().getUid()+"---------------------------------------");

        rl_customer_list_in_my_shop =root.findViewById(R.id.rl_customer_list_in_my_shop);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiptModelShopperList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReceiptModelShopper receiptModelShopper1 = dataSnapshot1.getValue(ReceiptModelShopper.class);
                    if (receiptModelShopper1 != null && receiptModelShopper1.getReceipt_shop_uid().equals(firebaseAuth.getCurrentUser().getUid())) {
                        receiptModelShopperList.add(receiptModelShopper1);
                        Toast.makeText(getActivity(), "data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                if (receiptModelShopperList.size()>0){
                    acRprtCustmrListInShopHomeAdptr = new ACRprtCustmrListInShopHomeAdptr(getActivity(), receiptModelShopperList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rl_customer_list_in_my_shop.setLayoutManager(mLayoutManager);
                    rl_customer_list_in_my_shop.setItemAnimator(new DefaultItemAnimator());
                    rl_customer_list_in_my_shop.setAdapter(acRprtCustmrListInShopHomeAdptr);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
    /*
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
                //customerListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //customerListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

//    @Override
//    public void onBackPressed() {
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            return;
//        }
//        super.onBackPressed();
//    }

}