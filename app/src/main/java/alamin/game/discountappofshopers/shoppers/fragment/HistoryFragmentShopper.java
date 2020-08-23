package alamin.game.discountappofshopers.shoppers.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.CustomerWithdrawalStatusAdapter;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;

public class HistoryFragmentShopper extends Fragment {

    private RecyclerView rv_customer_paresis_history;
    private ImageView nodatafound;
    private FirebaseAuth firebaseAuth;
    private String uid;
    private DatabaseReference databaseReference;
    private List<ReceiptModelCustomer> customerList  = new ArrayList<>();
    private CustomerWithdrawalStatusAdapter statusAdapter;
    private ProgressDialog progressDialog;
    private PreferenceData preferenceData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        View toy = inflater.inflate(R.layout.fragment_history_shopper, container, false);
        this.preferenceData = new PreferenceData(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        uid = preferenceData.getStringValue("CurrentUser_Uid");
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        rv_customer_paresis_history = toy.findViewById(R.id.rv_customer_paresis_history);
        nodatafound = toy.findViewById(R.id.image_no_data_found);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReceiptModelCustomer customer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                    if (customer != null){
                        Log.d(" shop uid",customer.getShop_uid());
                        if (uid.equals(customer.getShop_uid())){
                            customerList.add(customer);
                        }
                    }
                }
                if (customerList.size() > 0){
                    nodatafound.setVisibility(View.GONE);
                    rv_customer_paresis_history.setVisibility(View.VISIBLE);
                    statusAdapter = new CustomerWithdrawalStatusAdapter(getActivity(),customerList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rv_customer_paresis_history.setLayoutManager(layoutManager);
                    rv_customer_paresis_history.setItemAnimator(new DefaultItemAnimator());
                    rv_customer_paresis_history.setAdapter(statusAdapter);
                    progressDialog.dismiss();
                }else {
                    rv_customer_paresis_history.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No History found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return toy;
    }
}