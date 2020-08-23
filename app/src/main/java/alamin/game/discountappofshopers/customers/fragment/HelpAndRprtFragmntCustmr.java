package alamin.game.discountappofshopers.customers.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.ReportAndHelpAdapter;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.ReportAndHelpModel;

public class HelpAndRprtFragmntCustmr extends Fragment {

    private CircularImageView civ_profile_picture;
    private TextView tv_customer_name,tv_customer_phone;

    private EditText et_reportMsg;
    private ImageView iv_reportMsgSend;
    private RecyclerView recyclerViewMsg;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ReportAndHelpModel> reportAndHelpModelArrayList = new ArrayList<>();
    private ReportAndHelpAdapter reportAndHelpAdapter;
    private PreferenceData preferenceData;
    private String CurrentUser_Uid;

    public HelpAndRprtFragmntCustmr() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help_and_report_fragment_customer, container, false);
        this.preferenceData = new PreferenceData(getActivity());
        CurrentUser_Uid = preferenceData.getStringValue("CurrentUser_Uid");
        firebaseAuth = FirebaseAuth.getInstance();
        civ_profile_picture = view.findViewById(R.id.civ_profile_picture);
        tv_customer_name = view.findViewById(R.id.tv_customer_name);
        tv_customer_phone = view.findViewById(R.id.tv_customer_phone);
        et_reportMsg = view.findViewById(R.id.et_reportMsg);
        iv_reportMsgSend = view.findViewById(R.id.iv_reportMsgSend);
        recyclerViewMsg = view.findViewById(R.id.recyclerViewMsg);
        databaseReference = FirebaseDatabase.getInstance().getReference("ReportAndHelp");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportAndHelpModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReportAndHelpModel reportAndHelpModel = dataSnapshot1.getValue(ReportAndHelpModel.class);
                    reportAndHelpModelArrayList.add(reportAndHelpModel);
                }
                if (reportAndHelpModelArrayList.size()>0){
                    reportAndHelpAdapter = new ReportAndHelpAdapter(getActivity(),reportAndHelpModelArrayList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewMsg.setLayoutManager(layoutManager);
                    recyclerViewMsg.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewMsg.setAdapter(reportAndHelpAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        iv_reportMsgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Msg = et_reportMsg.getText().toString().trim();

                if (TextUtils.isEmpty(Msg)){
                    Toast.makeText(getActivity(), "Massage is  empty", Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    submitReport(CurrentUser_Uid,Msg);
                    et_reportMsg.setText("");
                }
            }
        });
        retrieveUserInfo(CurrentUser_Uid);
        return view;
    }
    private void retrieveUserInfo(String uid){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer customer = dataSnapshot.getValue(RegistrationModelCustomer.class);

                if (customer != null){
                    Picasso.with(getActivity()).load(customer.getProfile_pic_url()).placeholder(R.drawable.defaultpic).into(civ_profile_picture);
                    tv_customer_name.setText(customer.getName());
                    tv_customer_phone.setText(customer.getPhone());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void submitReport(String uid,String msg){

        databaseReference = FirebaseDatabase.getInstance().getReference("ReportAndHelp");

        //pic up current date from my user device
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        String CurrentDate = currentDate.format(calForDate.getTime());

        // pic up current time from my user device
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String CurrentTime = currentTime.format(calForTime.getTime());

        final String random_key = uid + CurrentDate + CurrentTime;

        ReportAndHelpModel reportAndHelpModel = new ReportAndHelpModel(uid,CurrentTime,CurrentDate,msg);
        databaseReference.child(random_key).setValue(reportAndHelpModel);

    }

}
