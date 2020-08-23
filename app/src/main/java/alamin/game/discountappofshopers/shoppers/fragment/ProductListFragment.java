package alamin.game.discountappofshopers.shoppers.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.ShopProfileCustomerViewActivity;

public class ProductListFragment extends Fragment {
    private TextView product_list ;
    private PreferenceData preferenceData;
    private DatabaseReference databaseReference;
    public ProductListFragment() {
        // Required empty public constructor
    }
    ShopProfileCustomerViewActivity shopProfileCustomerViewActivity = new ShopProfileCustomerViewActivity();



    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        this.preferenceData = new PreferenceData(getActivity());
        product_list = view.findViewById(R.id.product_list);



        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(preferenceData.getStringValue("aea")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelShopper shopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                if (shopper != null) {
                    if (shopper.getFood_item().equals("")){
                        product_list.setText("No Product Available");
                    }else {
                        product_list.setText(shopper.getFood_item());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
