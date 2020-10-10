package alamin.game.discountappofshopers.shoppers.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.ProfileActivityShopperEditable;

public class ProfileFragmentShopper extends Fragment implements View.OnClickListener {

    private FloatingActionButton fab_shopper_profile;
    private TextView tv_shop_profile_name, tv_shopper_profile_email, tv_shopper_profile_phone, tv_shop_profile_create_date, tv_shop_profile_location;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ImageView civ_profile_pic_shopper;
    private PreferenceData preferenceData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_shopper, container, false);
        this.preferenceData = new PreferenceData(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        fab_shopper_profile = root.findViewById(R.id.fab_shopper_profile);
        fab_shopper_profile.setOnClickListener(this);

        tv_shop_profile_name = root.findViewById(R.id.tv_shop_profile_name);
        tv_shopper_profile_email = root.findViewById(R.id.tv_shopper_profile_email);
        tv_shopper_profile_phone = root.findViewById(R.id.tv_shopper_profile_phone);
        tv_shop_profile_create_date = root.findViewById(R.id.tv_shop_profile_create_date);
        civ_profile_pic_shopper = root.findViewById(R.id.civ_profile_pic_shopper);
        tv_shop_profile_location = root.findViewById(R.id.tv_shop_profile_location);
        retrieveDataFromFirebase();
        return root;


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_shopper_profile:
                Intent intent = new Intent(getActivity(), ProfileActivityShopperEditable.class);
                startActivity(intent);
                break;
        }
    }

    public void retrieveDataFromFirebase() {
        //String uid = preferenceData.getStringValue("CurrentUser_Uid");
        String uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("shopper").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    RegistrationModelShopper registrationModelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                    tv_shop_profile_name.setText(registrationModelShopper.getShop_name());
                    tv_shopper_profile_email.setText(registrationModelShopper.getShopper_email());
                    tv_shopper_profile_phone.setText(registrationModelShopper.getShopper_phone());
                    tv_shop_profile_create_date.setText(registrationModelShopper.getShop_creation_date());
                    tv_shop_profile_location.setText(registrationModelShopper.getShop_location_manually());
                    Picasso.with(getActivity()).load(registrationModelShopper.getShop_pic_url()).placeholder(R.drawable.noimage).into(civ_profile_pic_shopper);

                } catch (Exception e) {
                    Log.d("TAG", " onDataChange error:  " + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}