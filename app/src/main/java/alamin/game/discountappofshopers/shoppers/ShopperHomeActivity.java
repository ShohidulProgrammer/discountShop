package alamin.game.discountappofshopers.shoppers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.activity.SplashActivity;
import alamin.game.discountappofshopers.model.LocationModel;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.fragment.HelpAndRprtFrgmntShopper;
import alamin.game.discountappofshopers.shoppers.fragment.HistoryFragmentShopper;
import alamin.game.discountappofshopers.shoppers.fragment.HomeFragmentShopper;
import alamin.game.discountappofshopers.shoppers.fragment.ProfileFragmentShopper;
import alamin.game.discountappofshopers.shoppers.fragment.TrmConditnFrgmntShopper;

public class ShopperHomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth firebaseAuth;
    private PreferenceData preferenceData;
    private Double myLocationLat, myLocationLong;
    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    private RegistrationModelShopper registrationModelShopper;

    private DatabaseReference databaseReference, shopRef;
    String name;
    String location;
    String pic_url;
    private String user_uid;
    private TextView tv_user_name_nav_header, tv_user_phone__nav_header;
    private ImageView nav_profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_home);
        this.preferenceData = new PreferenceData(ShopperHomeActivity.this);
        shopRef = FirebaseDatabase.getInstance().getReference().child("Users").child("shopper");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user_uid = bundle.getString("uid");
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        if (firebaseAuth.getCurrentUser() != null) {
            user_uid = firebaseAuth.getCurrentUser().getUid();
            preferenceData.setValue("loginStatusShopper", user_uid);
        }

        preferenceData.setValue("CurrentUser_Uid", user_uid);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tv_user_name_nav_header = headerView.findViewById(R.id.tv_user_name_nav_header);
        tv_user_phone__nav_header = headerView.findViewById(R.id.tv_user_phone__nav_header);
        nav_profile_picture = headerView.findViewById(R.id.nav_profile_picture);


        shopRef.child(user_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    tv_user_name_nav_header.setText(dataSnapshot.child("shop_name").getValue().toString());
                    tv_user_phone__nav_header.setText(dataSnapshot.child("shopper_phone").getValue().toString());
                    Picasso.with(ShopperHomeActivity.this).load(dataSnapshot.child("shop_pic_url").getValue().toString()).placeholder(R.drawable.defaultpic).into(nav_profile_picture);

                } catch (Exception e) {
                    Log.d("shopperhome", "onDataChange: error:  " + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        displaySelectedScreen(R.id.nav_history_shopper);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ShopperHomeActivity.this);
        getLastLocation();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_offer, R.id.nav_profile,
//                R.id.rate_up, R.id.nav_share, R.id.nav_help_and_report)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        Bundle bundle = null;
        switch (id) {
            case R.id.nav_home_shopper:
                fragment = new HomeFragmentShopper();
                break;
            case R.id.nav_history_shopper:
                //Intent baby = new Intent(ShopperHomeActivity.this,MapsShopperActivity.class);
                //startActivity(baby);
                fragment = new HistoryFragmentShopper();
                break;
            case R.id.nav_profile_shopper:
                fragment = new ProfileFragmentShopper();
                break;

            case R.id.nav_terms_and_conditions_shopper:
                fragment = new TrmConditnFrgmntShopper();
                break;
            case R.id.rate_up_shopper:
                toast("rate us");
                break;
            case R.id.nav_share_shopper:
                toast("shire");
                break;
            case R.id.nav_help_and_report_shopper:
                fragment = new HelpAndRprtFrgmntShopper();
                break;
            case R.id.nav_logout_shopper:
                firebaseAuth.signOut();
                Intent intent = new Intent(ShopperHomeActivity.this, SplashActivity.class);
                preferenceData.setValue("CurrentUser_Uid", "");
                startActivity(intent);
                finish();
                break;
        }
        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame_shopper, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void toast(String toastMsg) {
        Toast.makeText(this, "" + toastMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopper_home, menu);
        return true;
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
                                    google_maps_data_save(myLocationLat, myLocationLong);
                                    // Toast.makeText(ShopperHomeActivity.this, "latitude : "+myLocationLat+"\n"+"longitude : "+myLocationLong, Toast.LENGTH_SHORT).show();
                                    //todo::we was use this textview to show latitude and longitude it call from this fragment layout bt now i removed it if we need it we will use again to declar textview in layout

//                                    latTextView.setText(location.getLatitude()+"");
//                                    lonTextView.setText(location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
                OpenDialog();
                Toast.makeText(ShopperHomeActivity.this, "Turn on location", Toast.LENGTH_LONG).show();

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ShopperHomeActivity.this);
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

            Toast.makeText(ShopperHomeActivity.this, "mLocationCallback latitude : " + myLocationLat + "\n" + "mLocationCallback longitude : " + myLocationLong, Toast.LENGTH_LONG).show();

            //todo::we was use this textview to show latitude and longitude it call from this fragment layout bt now i removed it if we need it we will use again to declar textview in layout
//            latTextView.setText(mLastLocation.getLatitude()+"");
//            lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(ShopperHomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ShopperHomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                ShopperHomeActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) ShopperHomeActivity.this.getSystemService(Context.LOCATION_SERVICE);
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
        final Dialog dialog = new Dialog(ShopperHomeActivity.this);
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

    private void google_maps_data_save(final double latitude, final double longitude) {
        final boolean status = true;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(user_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    RegistrationModelShopper registrationModelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                    name = registrationModelShopper.getShop_name();
                    location = registrationModelShopper.getShop_location_manually();
                    pic_url = registrationModelShopper.getShop_pic_url();
                    databaseReference = FirebaseDatabase.getInstance().getReference("location details");
                    LocationModel locationModel = new LocationModel(latitude, longitude, name, location, pic_url, user_uid, status);
                    if (!isLocationFound()) {
                        databaseReference.child(user_uid).setValue(locationModel);
                    } else if (isLocationFound()) {
                        Toast.makeText(ShopperHomeActivity.this, "Location is already taken", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ShopperHomeActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.d("TAG", "onDataChange: google_maps_data_save"+e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private Boolean isLocationFound() {
        final String uid = user_uid;
        final List<String> locationModelList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("location details");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    LocationModel locationModel = dataSnapshot1.getValue(LocationModel.class);
                    if (locationModel != null) {
                        if (uid.equals(locationModel.getUser_uid())) {

                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }
}
