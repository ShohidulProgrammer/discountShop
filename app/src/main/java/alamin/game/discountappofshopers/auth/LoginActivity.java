package alamin.game.discountappofshopers.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.DashboardActivity;
import alamin.game.discountappofshopers.customers.SingUpActivityCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.SingUpActivityShopper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_ID = 44;
    private ImageButton ib_loginPage;
    private EditText et_user_phone;
    private RelativeLayout login_layout;
    private String chooser_user = "customer";
    private String phoneNumber;
    private DatabaseReference databaseReference, userRef;
    private PreferenceData preferenceData;
    private CircularImageView add_new_shop;
    private TextView user_panel;
    private ProgressDialog progressDialog;
    FusedLocationProviderClient mFusedLocationClient;
    double latTextView, lonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.preferenceData = new PreferenceData(LoginActivity.this);
        login_layout = findViewById(R.id.login_layout);
        add_new_shop = findViewById(R.id.add_new_shop);
        add_new_shop.setOnClickListener(this);
        ib_loginPage = findViewById(R.id.ib_loginPage);
        ib_loginPage.setOnClickListener(this);
        et_user_phone = findViewById(R.id.et_user_phone);
        user_panel = findViewById(R.id.user_panel);
        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Toast.makeText(this, "selected : " + chooser_user, Toast.LENGTH_SHORT).show();
        getLastLocation();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_loginPage:
                login_checker();
                break;

            case R.id.add_new_shop:
                chooser_user = "shopper";
                login_layout.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shoploginback));
                user_panel.setText("Shop");
                add_new_shop.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }


    private void login_checker() {
        phoneNumber = et_user_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            toast("Enter Phone Number First");
            return;
        } else if (phoneNumber.equals(preferenceData.getStringValue("admin"))) {
            movedNextActivity(DashboardActivity.class, "admin", null);
        } else {
            /*
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                chooser_value = bundle.getString("selectFromUserSelectActivity");

                movedNextActivity(OTPActivity.class,chooser_value);
            }
             */
            registration_check(chooser_user, phoneNumber);
        }
    }

    private void movedNextActivity(Class selectedActivity, String selectItem, String uid) {
        Intent intent = new Intent(LoginActivity.this, selectedActivity);
        intent.putExtra("phone_number", phoneNumber);
        intent.putExtra("SelectFromLoginActivity", selectItem);
        intent.putExtra("uid", uid);
        intent.putExtra("myLocationLat", latTextView);
        intent.putExtra("myLocationLong", lonTextView);
        startActivity(intent);
        finish();
    }

    private void toast(String toast_text) {
        Toast.makeText(this, "" + toast_text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this);
        // set title
        alertDialogBuilder.setTitle("Exit");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Finish all activities in stack and app closes
                        finishAffinity();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void registration_check(final String ref, final String phone) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        userRef.child(ref).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (ref.equals("customer")) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        RegistrationModelCustomer customer = dataSnapshot1.getValue(RegistrationModelCustomer.class);
                        if (phone.equals(customer.getPhone())) {
                            movedNextActivity(SingUpActivityCustomer.class, phone, customer.getFb_id().toString());
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            //   progressDialog.dismiss();
                            return;
                        }
                    }
                    movedNextActivity(OTPActivity.class, chooser_user, null);
                } else if (ref.equals("shopper")) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        RegistrationModelShopper shopper = dataSnapshot1.getValue(RegistrationModelShopper.class);
                        if (phone.equals(shopper.getShopper_phone())) {
                            //progressDialog.dismiss();
                            movedNextActivity(SingUpActivityShopper.class, phone, shopper.getFb_id().toString());
                            return;
                        }
                    }
                    movedNextActivity(OTPActivity.class, chooser_user, null);
                } else {
                    Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                    startActivity(intent);
                    //movedNextActivity(OTPActivity.class,chooser_user);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                                    latTextView = (location.getLatitude());
                                    lonTextView = (location.getLongitude());
                                    Toast.makeText(LoginActivity.this, "manually: " + latTextView + "\n" + lonTextView, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView = (mLastLocation.getLatitude());
            lonTextView = (mLastLocation.getLongitude());
            Toast.makeText(LoginActivity.this, "callback: " + latTextView + "\n" + lonTextView, Toast.LENGTH_SHORT).show();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
}



