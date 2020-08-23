package alamin.game.discountappofshopers.customers.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.customers.CustomerMapsActivity;
import alamin.game.discountappofshopers.adapter.ShopListAdapter;
import alamin.game.discountappofshopers.adapter.ShopListWithDiscountAdapter;
import alamin.game.discountappofshopers.customers.ProfileActivityCustomerEditable;
import alamin.game.discountappofshopers.model.LocationModel;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class HomeFragmentCustomer extends Fragment {
    public RecyclerView rv_shop_list_home_customer;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference databaseReference, databaseReferencelocation;
    private FirebaseAuth firebaseAuth;
    private ArrayList<RegistrationModelShopper> shopperArrayList;
    private ShopListWithDiscountAdapter discountAdapter;
    private ImageView iv_grs_icon_customer_home;
    private PreferenceData preferenceData;
    private SearchView searchView;
    private ShopListAdapter shopListAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText search;
    private double myLocationLat, myLocationLong;
    private double onekmIncrementLat = 0.0089936145;
    private double onekmIncrementLong = 0.0097703957;
    private double shopLocationLat, shopLocationLong;
    private String uid;
    private ProgressDialog progressDialog;

    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    private RegistrationModelShopper registrationModelShopper;
    private TextView warning_msg;

    public HomeFragmentCustomer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_customer, container, false);
        warning_msg = view.findViewById(R.id.warning_msg);
        this.preferenceData = new PreferenceData(getActivity());
        //todo:: hay programmer this two method we use just for find current location latitude and longitude from  user it working backend user didn't know about it
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();

        search = view.findViewById(R.id.search);
//        autoCompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
//        autoCompleteTextView.requestFocus();
//        //InputMethodManager mgr = (InputMethodManager) getSystemService(getActivity().INPUT_METHOD_SERVICE);
//        InputMethodManager mgr = (InputMethodManager) getSystem
//        mgr.showSoftInput(autoCompleteTextView, InputMethodManager.SHOW_FORCED);
        shopperArrayList = new ArrayList<>();
        shopListAdapter = new ShopListAdapter(getActivity(), shopperArrayList);

        preferenceData.setValue("aea", "");
        rv_shop_list_home_customer = view.findViewById(R.id.rv_shop_list_home_customer);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_shop_list_home_customer.setLayoutManager(linearLayoutManager);
        rv_shop_list_home_customer.setHasFixedSize(true);
        iv_grs_icon_customer_home = view.findViewById(R.id.iv_grs_icon_customer_home);
        iv_grs_icon_customer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomerMapsActivity.class);
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencelocation = FirebaseDatabase.getInstance().getReference("location details");
        firebaseAuth = FirebaseAuth.getInstance();
//       retrieveDataFromShopperList();
        /*
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait....");
        progressDialog.show();



        databaseReference.child("shopper").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    shopperArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        registrationModelShopper = dataSnapshot1.getValue(RegistrationModelShopper.class);
                        dataSnapshot.getChildrenCount();
                        //shopperArrayList.add(registrationModelShopper);
                        uid = registrationModelShopper.getFb_id();
                        databaseReferencelocation.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                LocationModel locationModel = dataSnapshot.getValue(LocationModel.class);
                                shopLocationLat = locationModel.getLatitude();
                                shopLocationLong = locationModel.getLongitude();

                                if (((myLocationLat - onekmIncrementLat) <= shopLocationLat && shopLocationLat <= (myLocationLat + onekmIncrementLat)) &&
                                        ((myLocationLong - onekmIncrementLong) <= shopLocationLong && shopLocationLong <= (myLocationLong + onekmIncrementLong))) {

                                    shopperArrayList.add(registrationModelShopper);
                                    progressDialog.dismiss();
                                }
                                if (shopperArrayList.size() > 0) {
                                    discountAdapter = new ShopListWithDiscountAdapter(getActivity(), shopperArrayList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    rv_shop_list_home_customer.setLayoutManager(mLayoutManager);
                                    rv_shop_list_home_customer.setItemAnimator(new DefaultItemAnimator());
                                    rv_shop_list_home_customer.setAdapter(discountAdapter);
                                    progressDialog.dismiss();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Data Not found", Toast.LENGTH_SHORT).show();
                    warning_msg.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
                //todo:: i use this statemtn above for collect location (lat,long) from location table automatically
//                if (shopperArrayList.size()>0){
//
//                    discountAdapter = new ShopListWithDiscountAdapter(getActivity(),shopperArrayList);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                    rv_shop_list_home_customer.setLayoutManager(mLayoutManager);
//                    rv_shop_list_home_customer.setItemAnimator(new DefaultItemAnimator());
//                    rv_shop_list_home_customer.setAdapter(discountAdapter);
//                    progressDialog.dismiss();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 */

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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencelocation = FirebaseDatabase.getInstance().getReference("location details");
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<RegistrationModelShopper>()
                        .setQuery(databaseReference, RegistrationModelShopper.class)
                        .build();

        FirebaseRecyclerAdapter<RegistrationModelShopper, ViewHolder> shop_list_adapter
                = new FirebaseRecyclerAdapter<RegistrationModelShopper, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull RegistrationModelShopper registrationModelShopper) {
                final String userIDs = getRef(i).getKey();

                databaseReference.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot_shopper) {
                        databaseReferencelocation.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot_location) {
                                if (dataSnapshot_location.hasChild("latitude") && dataSnapshot_location.hasChild("longitude")){
                                    Toast.makeText(getActivity(), "name : "+dataSnapshot_shopper.child("food_item").getValue().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.shopper_list_item_in_customer_home_layout, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }
        };
        rv_shop_list_home_customer.setAdapter(shop_list_adapter);
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

    private void retrieveDataFromShopperList() {
        databaseReference.child("shopper").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopperArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    RegistrationModelShopper registrationModelShopper = dataSnapshot1.getValue(RegistrationModelShopper.class);
                    shopperArrayList.add(registrationModelShopper);
                }
                if (shopperArrayList.size() > 0) {
                    discountAdapter = new ShopListWithDiscountAdapter(getActivity(), shopperArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rv_shop_list_home_customer.setLayoutManager(mLayoutManager);
                    rv_shop_list_home_customer.setItemAnimator(new DefaultItemAnimator());
                    rv_shop_list_home_customer.setAdapter(discountAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    myLocationLat = location.getLatitude();
                                    myLocationLong = location.getLongitude();

                                    Toast.makeText(getActivity(), "latitude : " + myLocationLat + "\n" + "longitude : " + myLocationLong, Toast.LENGTH_SHORT).show();
                                    //todo::we was use this textview to show latitude and longitude it call from this fragment layout bt now i removed it if we need it we will use again to declar textview in layout

//                                    latTextView.setText(location.getLatitude()+"");
//                                    lonTextView.setText(location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
                OpenDialog();
                Toast.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();

            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            myLocationLat = mLastLocation.getLatitude();
            myLocationLong = mLastLocation.getLongitude();
            Toast.makeText(getActivity(), "mLocationCallback latitude : " + myLocationLat + "\n" + "mLocationCallback longitude : " + myLocationLong, Toast.LENGTH_LONG).show();
            //todo::we was use this textview to show latitude and longitude it call from this fragment layout bt now i removed it if we need it we will use again to declar textview in layout
//            latTextView.setText(mLastLocation.getLatitude()+"");
//            lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    private void OpenDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoglayout);
        dialog.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }










    /*/todo:: to get a good list we use here firebaseAdapter class in here

        private void fetch() {
            FirebaseRecyclerOptions options =
                    new FirebaseRecyclerOptions.Builder<RegistrationModelShopper>()
                    .setQuery(databaseReference, RegistrationModelShopper.class)
                    .build();
        }
        FirebaseRecyclerAdapter<RegistrationModelShopper, ViewHolder> shop_list_adapter
                = new FirebaseRecyclerAdapter<RegistrationModelShopper, ViewHolder>(
                new FirebaseRecyclerOptions.Builder<RegistrationModelShopper>()
                        .setQuery(databaseReference, RegistrationModelShopper.class)
                        .build()
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull RegistrationModelShopper registrationModelShopper) {
                String userIDs = getRef(i).getKey();

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(getContext()).inflate(R.layout.shopper_list_item_in_customer_home_layout,parent,false);
               ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }
        };


     */

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_shopper_name_Customer_home;
        TextView tv_shopper_address_Customer_home;
        TextView tv_shopper_discount_Customer_home;
        ImageView iv_shopper_select_Customer_home;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_shopper_name_Customer_home = itemView.findViewById(R.id.tv_shopper_name_Customer_home);
            tv_shopper_address_Customer_home = itemView.findViewById(R.id.tv_shopper_address_Customer_home);
            tv_shopper_discount_Customer_home = itemView.findViewById(R.id.tv_shopper_discount_Customer_home);
            iv_shopper_select_Customer_home = itemView.findViewById(R.id.iv_shopper_select_Customer_home);
        }
    }
}



