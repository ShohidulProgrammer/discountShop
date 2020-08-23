package alamin.game.discountappofshopers.customers.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import alamin.game.discountappofshopers.customers.ProfileActivityCustomerEditable;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class ProfileFragmentCustomer extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab_customer_profile;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView tv_customer_profile_name,tv_customer_profile_email,tv_customer_profile_phone,tv_customer_profile_gender,tv_customer_profile_date,tv_payment_gateway,tv_payment_number;
    private TextView bkash_number,rocket_number,surecash_number,mcash_number,mycash_number,ucash_number,nogod_number;
    private CircularImageView civ_profile_pic_customer;
    private PreferenceData preferenceData;
    private String CurrentUser_Uid;

    public ProfileFragmentCustomer() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profile_fragment_customer, container, false);

        this.preferenceData = new PreferenceData(getActivity());
        fab_customer_profile = view.findViewById(R.id.fab_customer_profile);
        fab_customer_profile.setOnClickListener(this);
        CurrentUser_Uid = preferenceData.getStringValue("CurrentUser_Uid");

        tv_customer_profile_name = view.findViewById(R.id.tv_customer_profile_name);
        tv_customer_profile_email = view.findViewById(R.id.tv_customer_profile_email);
        tv_customer_profile_phone = view.findViewById(R.id.tv_customer_profile_phone);
        tv_customer_profile_gender = view.findViewById(R.id.tv_customer_profile_gender);
        tv_customer_profile_date = view.findViewById(R.id.tv_customer_profile_date);

        bkash_number = view.findViewById(R.id.bkash_nmbr);
        rocket_number = view.findViewById(R.id.rocket_nmbr);
        surecash_number = view.findViewById(R.id.surecash_nmbr);
        mcash_number = view.findViewById(R.id.mcash_nmbr);
        mycash_number = view.findViewById(R.id.mycash_nmbr);
        ucash_number = view.findViewById(R.id.ucash_nmbr);
        nogod_number = view.findViewById(R.id.nogod_nmbr);

        civ_profile_pic_customer = view.findViewById(R.id.civ_profile_pic_customer);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        retrieveDataFromFirebase();
    return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_customer_profile:
                Intent intent = new Intent(getActivity(), ProfileActivityCustomerEditable.class);
                startActivity(intent);
                break;
        }
    }
    public void retrieveDataFromFirebase (){
        firebaseAuth = FirebaseAuth.getInstance();
        String uid = CurrentUser_Uid;
        databaseReference.child("customer").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer registrationModelCustomer = dataSnapshot.getValue(RegistrationModelCustomer.class);
                tv_customer_profile_name.setText(registrationModelCustomer.getName());
                tv_customer_profile_email.setText(registrationModelCustomer.getEmail());
                tv_customer_profile_phone.setText(registrationModelCustomer.getPhone());
                tv_customer_profile_gender.setText(registrationModelCustomer.getGender());
                tv_customer_profile_date.setText(registrationModelCustomer.getDate_of_birth());
                bkash_number.setText(registrationModelCustomer.getBiksh_number());
                rocket_number.setText(registrationModelCustomer.getRocket_number());
                surecash_number.setText(registrationModelCustomer.getSureCash_number());
                mcash_number.setText(registrationModelCustomer.getmCash_number());
                mycash_number.setText(registrationModelCustomer.getMyCash_number());
                ucash_number.setText(registrationModelCustomer.getuCash_number());
                nogod_number.setText(registrationModelCustomer.getNogod_number());
                Picasso.with(getActivity()).load(registrationModelCustomer.getProfile_pic_url()).placeholder(R.drawable.loading_image).into(civ_profile_pic_customer);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
