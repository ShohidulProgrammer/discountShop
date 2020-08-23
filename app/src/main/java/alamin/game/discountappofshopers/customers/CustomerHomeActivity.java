package alamin.game.discountappofshopers.customers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.activity.SplashActivity;
import alamin.game.discountappofshopers.customers.fragment.HelpAndRprtFragmntCustmr;
import alamin.game.discountappofshopers.customers.fragment.HistoryFragmentCustomer;
import alamin.game.discountappofshopers.customers.fragment.ProfileFragmentCustomer;
import alamin.game.discountappofshopers.customers.fragment.ShopListFragment;
import alamin.game.discountappofshopers.customers.fragment.TrmsCondFrgmntCustmr;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;

public class CustomerHomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    FirebaseAuth firebaseAuth;
    private PreferenceData preferenceData;

    TextView tv_user_name_nav_header,tv_user_phone__nav_header;
    ImageView nav_profile_picture;
    String user_uid;
    Handler handler = new Handler();
    Runnable refresh;
    private double myLocationLat,myLocationLong;
    Bundle bundle;
    private DatabaseReference customerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("customer");
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null){
            myLocationLat = bundle.getDouble("myLocationLat");
            myLocationLong = bundle.getDouble("myLocationLong");

            bundle.putDouble("myLocationLat",myLocationLat);
            bundle.putDouble("myLocationLong",myLocationLong);

        }
        firebaseAuth = FirebaseAuth.getInstance();
        refresh = new Runnable() {
            public void run() {
                // Do something
                handler.postDelayed(refresh, 5000);
                Toast.makeText(CustomerHomeActivity.this, "auto loading called", Toast.LENGTH_SHORT).show();
            }
        };
        this.preferenceData = new PreferenceData(CustomerHomeActivity.this);
        preferenceData.getStringValue("CurrentUser_Uid");
        Toolbar toolbar = findViewById(R.id.toolbar_customer);
        setSupportActionBar(toolbar);
        preferenceData.setValue("loginStatusCustomer",firebaseAuth.getCurrentUser().getUid());

        DrawerLayout drawer = findViewById(R.id.drawer_layout_customer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_button);

        NavigationView navigationView = findViewById(R.id.nav_view_customer);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tv_user_name_nav_header = headerView.findViewById(R.id.tv_user_name_nav_header);
        tv_user_phone__nav_header = headerView.findViewById(R.id.tv_user_phone__nav_header);
        nav_profile_picture = headerView.findViewById(R.id.nav_profile_picture);
        displaySelectedScreen(R.id.nav_home_customer);

        customerRef.child(preferenceData.getStringValue("CurrentUser_Uid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tv_user_name_nav_header.setText(dataSnapshot.child("name").getValue().toString());
                tv_user_phone__nav_header.setText(dataSnapshot.child("phone").getValue().toString());
                Picasso.with(CustomerHomeActivity.this).load(dataSnapshot.child("profile_pic_url").getValue().toString()).placeholder(R.drawable.defaultpic).into(nav_profile_picture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //todo:: after solved this problem it will be work properly
        //retrieveUserInfo(firebaseAuth.getCurrentUser().getUid());
    }
    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id){
            case R.id.nav_home_customer:
                fragment = new ShopListFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.nav_history_customer:
                fragment = new HistoryFragmentCustomer();
                break;
            case R.id.nav_profile_customer:
                fragment = new ProfileFragmentCustomer();
                break;

            case R.id.nav_terms_and_conditions_customer:
                fragment = new TrmsCondFrgmntCustmr();
                break;
            case R.id.rate_up_customer:
                toast("rate us customer");
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
                break;
            case R.id.nav_share_customer:
                toast("shire customer");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.nav_help_and_report_customer:
                fragment = new HelpAndRprtFragmntCustmr();
                break;
            case R.id.nav_logout_customer:
               firebaseAuth.signOut();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("Are you sure you want to Logout ?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent intent = new Intent(CustomerHomeActivity.this, UserSelectActivity.class);
//                                //startActivity(intent);
//                                preferenceData.setValue("CurrentUser_Uid","");
//                               finish();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
                preferenceData.setValue("CurrentUser_Uid","");
                Intent intent = new Intent(CustomerHomeActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        if (fragment != null){

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame_customer,fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_customer);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void toast(String toastMsg) {
        Toast.makeText(this, ""+toastMsg, Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_home, menu);
        return true;
    }
    @Override
    public void onClick(View view) {

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        displaySelectedScreen(id);
        return true;
    }
    //todo:: in here i can't cast withg nav_header_main after solve it i need to fixed it
    public void retrieveUserInfo(String uid){



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
//        TextView nav_user = (TextView)headerView.findViewById(R.id.tv_user_name_nav_header);
//        nav_user.setText("alamin");






//        tv_user_name_nav_header = findViewById(R.id.tv_user_name_nav_header);
//        tv_user_phone__nav_header = findViewById(R.id.tv_user_phone__nav_header);
//        nav_profile_picture = findViewById(R.id.nav_profile_picture);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer customer = dataSnapshot.getValue(RegistrationModelCustomer.class);

                if (customer != null) {
                    //tv_user_name_nav_header.setText(customer.getName());
                    //tv_user_phone__nav_header.setText(customer.getPhone());
                    //Picasso.with(CustomerHomeActivity.this).load(customer.getProfile_pic_url()).placeholder(R.drawable.defaultpic).into(nav_profile_picture);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
