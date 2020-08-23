package alamin.game.discountappofshopers.customers.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.activity.FourActivity;
import alamin.game.discountappofshopers.activity.OneActivity;
import alamin.game.discountappofshopers.activity.ThreeActivity;
import alamin.game.discountappofshopers.activity.TwoActivity;
import alamin.game.discountappofshopers.adapter.ShopListAdapter;
import alamin.game.discountappofshopers.adapter.ShopListWithDiscountAdapter;
import alamin.game.discountappofshopers.customers.CustomerMapsActivity;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class ShopListFragment extends Fragment  implements View.OnClickListener {

    private ShopListAdapter shopListAdapter;
    private DatabaseReference shoplistRef, databaseReferencelocation;

    private ArrayList<RegistrationModelShopper> shopperArrayList;
    private ShopListWithDiscountAdapter discountAdapter;

    private PreferenceData preferenceData;
    private ImageView iv_grs_icon_customer_home;
    private SearchView searchView;
    private RecyclerView rv_shop_list_home_customer;
    private LinearLayoutManager linearLayoutManager;
    private EditText search;
    private TextView warning_msg;
    private AutoCompleteTextView autoCompleteTextView;
    private LinearLayout one, two, three, four;


    private double myLocationLat, myLocationLong;
    private double onekmIncrementLat = 0.0089936145;
    private double onekmIncrementLong = 0.0097703957;
    private double shopLocationLat, shopLocationLong;

    private String uid;
    private ProgressDialog progressDialog;
    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;

    public ShopListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        if (getArguments() != null) {
            myLocationLat = getArguments().getDouble("myLocationLat");
            myLocationLong = getArguments().getDouble("myLocationLong");
        }

        warning_msg = view.findViewById(R.id.warning_msg);
        this.preferenceData = new PreferenceData(getActivity());

        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        shoplistRef = FirebaseDatabase.getInstance().getReference().child("Users").child("shopper");
        databaseReferencelocation = FirebaseDatabase.getInstance().getReference("location details");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        search = view.findViewById(R.id.search);

        shopperArrayList = new ArrayList<>();
        shopListAdapter = new ShopListAdapter(getActivity(), shopperArrayList);

        preferenceData.setValue("aea", "");
        rv_shop_list_home_customer = view.findViewById(R.id.rv_shop_list_home_customer);
        rv_shop_list_home_customer.setLayoutManager(new LinearLayoutManager(getContext()));

        iv_grs_icon_customer_home = view.findViewById(R.id.iv_grs_icon_customer_home);
        iv_grs_icon_customer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "location btn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CustomerMapsActivity.class);
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        shoplistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopperArrayList.clear();

                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        RegistrationModelShopper shopper = dataSnapshot1.getValue(RegistrationModelShopper.class);
                        Log.d("firebase", "firebase data: "+shopper);
                        if (shopper != null){
                            shopLocationLat = shopper.getLatitude();
                            shopLocationLong = shopper.getLongitude();
                            if (((myLocationLat - onekmIncrementLat) <= shopLocationLat && shopLocationLat <= (myLocationLat + onekmIncrementLat)) && ((myLocationLong - onekmIncrementLong) <= shopLocationLong && shopLocationLong <= (myLocationLong + onekmIncrementLong))) {
                                shopperArrayList.add(shopper);

                            }
                        }
                    }
                    if (shopperArrayList.size()>0){
                        discountAdapter = new ShopListWithDiscountAdapter(getActivity(), shopperArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rv_shop_list_home_customer.setLayoutManager(mLayoutManager);
                        rv_shop_list_home_customer.setItemAnimator(new DefaultItemAnimator());
                        rv_shop_list_home_customer.setAdapter(discountAdapter);
                    }
                }catch (Exception e){
                    Log.d("firebase", "firebase error: "+e);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void filter(String text) {
        ArrayList<RegistrationModelShopper> filterList = new ArrayList<>();
        for (RegistrationModelShopper item : shopperArrayList) {
            if (item.getShop_name().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }
        discountAdapter.filteredList(filterList);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                discountAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                discountAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.one:
                nextActivity(OneActivity.class);
                break;
            case R.id.two:
                nextActivity(TwoActivity.class);
                break;
            case R.id.three:
                nextActivity(ThreeActivity.class);
                break;
            case R.id.four:
                nextActivity(FourActivity.class);
                break;
        }
    }

    private void nextActivity(Class selectedClass) {
        Intent intent = new Intent(getActivity(),selectedClass);
        startActivity(intent);
    }
}
