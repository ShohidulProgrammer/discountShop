package alamin.game.discountappofshopers.shoppers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.fragment.ProductListFragment;
import alamin.game.discountappofshopers.shoppers.fragment.RcptInfoAddByClintFrgmnt;
import alamin.game.discountappofshopers.shoppers.fragment.ReviewInfoByCustmrFrgmnt;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class ShopProfileCustomerViewActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    private PreferenceData preferenceData;
    private ViewPager view_pager;
    private TabLayout tab_layout;
    private String chooser_uid;
    private DatabaseReference databaseReference;
    private TextView tv_shop_profile_name_customer_view,tv_shop_profile_phone_customer_view,tv_shop_profile_location_customer_view;
    private ImageView iv_shop_profile_pic_customer_view;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile_customer_view);
        coordinatorLayout = findViewById(R.id.main_content);
        view = coordinatorLayout;
        this.preferenceData = new PreferenceData(ShopProfileCustomerViewActivity.this);
        initComponent();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            chooser_uid = bundle.getString("selected_shop_uid");
            retrieveShopInfo(chooser_uid);
            preferenceData.setValue("aea",chooser_uid);
        }
        if (!checkPermission()) {
            requestPermission();
        }
    }
    private void initToolbar(String shopName) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(shopName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

    }
    private void retrieveShopInfo(String uid){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        tv_shop_profile_name_customer_view = findViewById(R.id.tv_shop_profile_name_customer_view);
        tv_shop_profile_phone_customer_view = findViewById(R.id.tv_shop_profile_phone_customer_view);
        tv_shop_profile_location_customer_view = findViewById(R.id.tv_shop_profile_location_customer_view);
        iv_shop_profile_pic_customer_view = findViewById(R.id.iv_shop_profile_pic_customer_view);

        databaseReference.child("shopper").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelShopper shopper = dataSnapshot.getValue(RegistrationModelShopper.class);

                tv_shop_profile_name_customer_view.setText(shopper.getShop_name());
                tv_shop_profile_phone_customer_view.setText(shopper.getShopper_phone());
                tv_shop_profile_location_customer_view.setText(shopper.getShop_location_manually());
                Picasso.with(getApplicationContext()).load(shopper.getShop_pic_url()).placeholder(R.drawable.shop).into(iv_shop_profile_pic_customer_view);
                initToolbar(shopper.getShop_name());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(view_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
    }
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProductListFragment.newInstance(), "Product List");
        adapter.addFragment(RcptInfoAddByClintFrgmnt.newInstance(), "Received Add");
        adapter.addFragment(ReviewInfoByCustmrFrgmnt.newInstance(), "Review");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ShopProfileCustomerViewActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
