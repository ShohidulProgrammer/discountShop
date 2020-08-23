package alamin.game.discountappofshopers.shoppers.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.ReviewRetrieveAdapter;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.ReviewModel;

public class ReviewInfoByCustmrFrgmnt extends Fragment {
    private FloatingActionButton fla_review_by_customer;
    private RecyclerView rcv_review_itemList;
    private String CurrentUser_Uid,Select_shop_uid;
    private PreferenceData preferenceData;
    String customer_name,customer_mobile,customer_profile_pic,shop_name,shop_location,shopper_phone,shop_pic;
    private DatabaseReference databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    public ReviewInfoByCustmrFrgmnt() {

    }
    public static ReviewInfoByCustmrFrgmnt newInstance() {
        ReviewInfoByCustmrFrgmnt fragment = new ReviewInfoByCustmrFrgmnt();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_review_info_bycustomer, container, false);

        this.preferenceData = new PreferenceData(getActivity());
        CurrentUser_Uid = preferenceData.getStringValue("CurrentUser_Uid");
        Select_shop_uid = preferenceData.getStringValue("aea");

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("shopper").child(preferenceData.getStringValue("aea"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shop_name = dataSnapshot.child("shop_name").getValue().toString();
                shopper_phone = dataSnapshot.child("shopper_phone").getValue().toString();
                shop_pic = dataSnapshot.child("shop_pic_url").getValue().toString();
                shop_location = dataSnapshot.child("shop_location_manually").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rcv_review_itemList = view.findViewById(R.id.rcv_review_itemList);
        retrievedAllReview(rcv_review_itemList);

        fla_review_by_customer = view.findViewById(R.id.fla_review_by_customer);
        fla_review_by_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
        return view;
    }
    private void retrievedAllReview(final RecyclerView rcv_review_itemList){

       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_review");
       final ArrayList<ReviewModel> reviewModelArrayList = new ArrayList<>();
        final ReviewRetrieveAdapter[] retrieveAdapter = new ReviewRetrieveAdapter[1];
       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReviewModel reviewModel = dataSnapshot1.getValue(ReviewModel.class);
                    reviewModelArrayList.add(reviewModel);
                }



                if (reviewModelArrayList.size()>0){
                    retrieveAdapter[0] = new ReviewRetrieveAdapter(reviewModelArrayList,getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rcv_review_itemList.setLayoutManager(mLayoutManager);
                    rcv_review_itemList.setItemAnimator(new DefaultItemAnimator());
                    rcv_review_itemList.setAdapter(retrieveAdapter[0]);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showCustomDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.review_submit_from_customer_in_shopper_profile_item_layout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView profile = dialog.findViewById(R.id.civ_profile_picture_customer_in_dialog_box);
        TextView name = dialog.findViewById(R.id.tv_customer_name_in_dialog_box);
        TextView phone = dialog.findViewById(R.id.tv_customer_phone_number_in_dialog_box);
        retreivedCustomerInfo(profile,name,phone);

        final EditText et_post = (EditText) dialog.findViewById(R.id.et_post);
        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewMsg = et_post.getText().toString().trim();
                if (reviewMsg.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill review text", Toast.LENGTH_SHORT).show();
                }else {
                    customer_reviewUpload(reviewMsg);
                    Intent intent = new Intent(getActivity(), CustomerHomeActivity.class);
                    //getContext().startActivity(intent);
                    dialog.dismiss();
                }
                dialog.dismiss();
                Toast.makeText(getActivity(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void retreivedCustomerInfo (final ImageView profileImg, final TextView name, final TextView phone){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = CurrentUser_Uid;
        databaseReference.child("customer").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer registrationModelCustomer = dataSnapshot.getValue(RegistrationModelCustomer.class);
                name.setText(registrationModelCustomer.getName());
                phone.setText(registrationModelCustomer.getPhone());
                Picasso.with(getActivity()).load(registrationModelCustomer.getProfile_pic_url()).placeholder(R.drawable.defaultpic).into(profileImg);
                customer_name = registrationModelCustomer.getName();
                customer_mobile = registrationModelCustomer.getPhone();
                customer_profile_pic = registrationModelCustomer.getProfile_pic_url();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void customer_reviewUpload(String review_mas){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_review");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = CurrentUser_Uid;
        //pic up current date
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        // pic up current time
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calForTime.getTime());

//        Map<String,Object> taskMap = new HashMap<>();
//        taskMap.put("name",customer_name);
//        taskMap.put("mobile",customer_mobile);
//        taskMap.put("profile_pic",customer_profile_pic);
//        taskMap.put("user_uid",uid);
//        taskMap.put("shop_name",shop_name);
//        taskMap.put("shop_location",shop_location);
//        taskMap.put("shop_profile_pic",shop_pic);
//        taskMap.put("shop_uid",Select_shop_uid);
//        taskMap.put("msg",review_mas);
//
//        databaseReference.child("Review").child(CurrentUser_Uid).push().setValue(taskMap);

        String random_key = uid + saveCurrentDate + saveCurrentTime;
        ReviewModel reviewModel = new ReviewModel(uid,review_mas,random_key,saveCurrentDate);
        databaseReference.child(random_key).setValue(reviewModel);
    }


    /*
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ReviewModel> options =
                new FirebaseRecyclerOptions.Builder<ReviewModel>()
                .setQuery(databaseReference,ReviewModel.class)
                .build();

        FirebaseRecyclerAdapter<ReviewModel,ViewHolder> adapter = new FirebaseRecyclerAdapter<ReviewModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull ReviewModel reviewModel) {
                viewHolder.tv_reviewer_name_retrieve_page.setText(reviewModel.);
            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_retreived_layout,parent,false);
                return new ViewHolder(view);
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_reviewer_name_retrieve_page;
        TextView tv_review_time_retrieve_page;
        TextView tv_review_text_retrieve_page;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_reviewer_name_retrieve_page = itemView.findViewById(R.id.tv_reviewer_name_retrieve_page);
            tv_review_time_retrieve_page = itemView.findViewById(R.id.tv_review_time_retrieve_page);
            tv_review_text_retrieve_page = itemView.findViewById(R.id.tv_review_text_retrieve_page);
        }
    }

     */
}
