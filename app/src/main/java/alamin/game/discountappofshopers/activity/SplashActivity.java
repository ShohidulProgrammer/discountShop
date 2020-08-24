package alamin.game.discountappofshopers.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.DashboardActivity;
import alamin.game.discountappofshopers.appUtils.AppUtils;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.customers.SingUpActivityCustomer;
import alamin.game.discountappofshopers.auth.UserSelectActivity;
import alamin.game.discountappofshopers.shoppers.ShopperHomeActivity;
import alamin.game.discountappofshopers.shoppers.SingUpActivityShopper;

public class SplashActivity extends AppCompatActivity {
    private static final int PERMISSION_ID = 44;
    private Context context;
    private Animation logoRotate;
    private final long SPLASH_DISPLAY_LENGTH = 3000;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private TextView tvVersionCode;
    private FirebaseAuth firebaseAuth;
    private PreferenceData preferenceData ;
    public static final String DEVICEVERSION="6";
    private TextView tv_update_msg,tv_update_btn;
    MediaPlayer music;
    String uid;
    private double myLocationLat, myLocationLong;
    private FusedLocationProviderClient mFusedLocationClient;
    private Animation anim;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;

        // animation code start from here
        imageView=(ImageView)findViewById(R.id.imgv_logo); // Declare an imageView to show the animation.
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up); // Create the animation.

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                startActivity(new Intent(this,HomeActivity.class));
                // HomeActivity.class is the activity to go after showing the splash screen.
                Log.d("Splash screen", "Splash Screen End");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(anim);
        // end animation code


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SplashActivity.this);
        tv_update_msg = findViewById(R.id.tv_update_msg);
        tv_update_btn = findViewById(R.id.tv_update_btn);

        tv_update_msg.setVisibility(View.INVISIBLE);
        tv_update_btn.setVisibility(View.INVISIBLE);

        this.preferenceData = new PreferenceData(SplashActivity.this);
        preferenceData.setValue("admin","000000");
        //uid = preferenceData.getStringValue("CurrentUser_Uid");
        firebaseAuth = FirebaseAuth.getInstance();
        initFunctionality();

        if (firebaseAuth.getCurrentUser() != null){
            uid = firebaseAuth.getCurrentUser().getUid();
        }

        /*
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                   // nextAcvitity(CustomerHomeActivity.class);
//                Todo:: This code we wil use after complete admin task
//                      to save time we are using this process
                    FirebaseUser current_user = firebaseAuth.getCurrentUser();
//                    if (1==1){
//                        nextAcvitity(DashboardActivity.class);
//                    }
                     if (current_user != null){

                        if ((preferenceData.getStringValue("loginStatusCustomer")).equals(firebaseAuth.getCurrentUser().getUid())){
                            nextAcvitity(CustomerHomeActivity.class);
                        }else if((preferenceData.getStringValue("loginStatusShopper")).equals(firebaseAuth.getCurrentUser().getUid())){
                            nextAcvitity(ShopperHomeActivity.class);
                        }else {
                            nextAcvitity(UserSelectActivity.class);
                            //Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                     }else {
                         Toast.makeText(context, "This is splash else statement", Toast.LENGTH_SHORT).show();
                     }
                }
            }
        };
        thread.start();

         */

    }
    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    private void initFunctionality() {
        if (AppUtils.isNetworkAvailable(SplashActivity.this)) {
            findViewById(R.id.imgv_logo).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //FirebaseUser current_user = uid;
//                    music= MediaPlayer.create(SplashActivity.this, R.raw.music);
                   // music.start();
                    if (uid != null && uid.length()>0){
                        if ((preferenceData.getStringValue("loginStatusCustomer")).equals(uid)){

                            nextAcvitity(CustomerHomeActivity.class);
                        }else if((preferenceData.getStringValue("loginStatusShopper")).equals(uid)){

                            nextAcvitity(ShopperHomeActivity.class);
                        }
                        else {
                            nextAcvitity(UserSelectActivity.class);
                        }
                    }
                    else{
                        nextAcvitity(UserSelectActivity.class);
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            AppUtils.noInternetWarning(findViewById(R.id.imgv_logo), SplashActivity.this);
        }
    }
    public void nextAcvitity(final Class selectedClass){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (DEVICEVERSION.equals(dataSnapshot.child("version").getValue().toString())){
                            Intent intent = new Intent(SplashActivity.this,selectedClass);
                            intent.putExtra("myLocationLat",myLocationLat);
                            intent.putExtra("myLocationLong",myLocationLong);
                            startActivity(intent);

                        }else {
                            tv_update_msg.setVisibility(View.VISIBLE);
                            tv_update_btn.setVisibility(View.VISIBLE);
                            tv_update_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(context, "Update Your Project Please", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
    private void getLastLocation(){
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
                                    myLocationLat = (location.getLatitude());
                                    myLocationLong = (location.getLongitude());
                                    Toast.makeText(context, "location 1 : "+myLocationLat+"\n"+myLocationLong, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            myLocationLat = (mLastLocation.getLatitude());
            myLocationLong = (mLastLocation.getLongitude());
            Toast.makeText(context, "location 2 : "+myLocationLat+"\n"+myLocationLong, Toast.LENGTH_SHORT).show();
        }
    };
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                SplashActivity.this,
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

}