package alamin.game.discountappofshopers.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.customers.CustomerHomeActivity;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class CustomerWithdrawalStatusAdapter extends RecyclerView.Adapter<CustomerWithdrawalStatusAdapter.CustomerWithdrawalStatusHolder>{
    private Context context;
    private List<ReceiptModelCustomer> receiptModelCustomerList = new ArrayList<>();
    private ReceiptModelCustomer receiptModelCustomer;
    private FirebaseAuth firebaseAuth;
    private String Current_user_uid;
    boolean rejected_status;
    DatabaseReference CustomerReference,receiptReference;


    float customer_discound_money = 0;

    public CustomerWithdrawalStatusAdapter(Context context, List<ReceiptModelCustomer> receiptModelCustomerList) {
        this.context = context;
        this.receiptModelCustomerList = receiptModelCustomerList;
    }

    @NonNull
    @Override
    public CustomerWithdrawalStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_withdrawal_status, parent, false);
        return new CustomerWithdrawalStatusHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final CustomerWithdrawalStatusHolder holder, final int position) {
        receiptModelCustomer = receiptModelCustomerList.get(position);
        receiptModelCustomer.getShop_uid();
        firebaseAuth = FirebaseAuth.getInstance();
        Current_user_uid = firebaseAuth.getCurrentUser().getUid().toString();


                holder.tv_invoice_number_withdrawal.setText(receiptModelCustomer.getInvoiceNumber());
        holder.tv_back_amount_withdrawal.setText(receiptModelCustomer.getDiscount_money()+"/=");

        holder.tv_status_withdrawal.setText(status(receiptModelCustomer.getBack_money_to_customer_status(),receiptModelCustomer.getRejected_status(),receiptModelCustomer.getCollect_money_from_shop_status(),receiptModelCustomer.getWithdrawal_request_status()));

//        customer_discound_money = customer_discound_money+Float.valueOf(receiptModelCustomer.getBack_money_to_customer_date());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(receiptModelCustomer.getShop_uid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelShopper modelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                if (modelShopper != null) {
                    holder.tv_resturant_name_withdrawal.setText(modelShopper.getShop_name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Payment Gateway : "+receiptModelCustomerList.get(position).getPaymentMethod()+""+receiptModelCustomerList.get(position).getGateway(), Toast.LENGTH_SHORT).show();
            }
        });

        rejected_status = receiptModelCustomerList.get(position).getShopper_rejected_request_status();
        if (rejected_status){
            holder.ll_parent.setBackgroundResource(R.drawable.rejected_request);
        }

        if (Current_user_uid.equals(receiptModelCustomer.getShop_uid())){
            holder.ll_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   // holder.ll_parent.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    SubmitBadCustomerReportToAdminDialog(receiptModelCustomer.getCustomer_uid(),receiptModelCustomer.getRandom_key(),receiptModelCustomer.getRandom_key(),receiptModelCustomer.getInvoiceNumber(),receiptModelCustomer.getDiscount_percentage(),receiptModelCustomer.getReceipt_amount(),receiptModelCustomer.getDiscount_money());
                    Toast.makeText(context, "long press", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        Paidstatus(receiptModelCustomer.getBack_money_to_customer_status(),receiptModelCustomer.getRejected_status(),receiptModelCustomer.getCollect_money_from_shop_status(),receiptModelCustomer.getWithdrawal_request_status(),holder.iv_paid_status);
    }

    @Override
    public int getItemCount() {
        return receiptModelCustomerList.size();
    }

    class CustomerWithdrawalStatusHolder extends RecyclerView.ViewHolder {
        TextView tv_invoice_number_withdrawal;
        TextView tv_resturant_name_withdrawal;
        TextView tv_back_amount_withdrawal;
        TextView tv_status_withdrawal;
        ImageView iv_paid_status;
        RelativeLayout ll_parent;

        CustomerWithdrawalStatusHolder(@NonNull View itemView) {
            super(itemView);
            tv_invoice_number_withdrawal = itemView.findViewById(R.id.tv_invoice_number_withdrawal);
            tv_resturant_name_withdrawal = itemView.findViewById(R.id.tv_resturant_name_withdrawal);
            tv_back_amount_withdrawal = itemView.findViewById(R.id.tv_back_amount_withdrawal);
            tv_status_withdrawal = itemView.findViewById(R.id.tv_status_withdrawal);
            iv_paid_status = itemView.findViewById(R.id.iv_paid_status);
            ll_parent = itemView.findViewById(R.id.ll_parent);
        }
    }
    private Boolean isPaid(Boolean status){
        if (status)
            return true;
        else
            return false;
    }
    private Boolean isCollect(Boolean status){
        if (status)
            return true;
        else
            return false;
    }
    private Boolean isReject(Boolean status){
        if (status)
            return true;
        else
            return false;
    }
    private Boolean iswithdraw(Boolean status){
        if (status)
            return true;
        else
            return false;
    }
    private String status(Boolean paidStatus, Boolean rejectStatus, Boolean collectStatus,Boolean withdrawStatus){
        if (!isReject(rejectStatus)){
            if ( isCollect(collectStatus)){
                if (isPaid(paidStatus)){
                    return "Paid";
                }else if (iswithdraw(withdrawStatus)){
                    return "Withdraw Requested";
                }else{
                    return "Collected";
                }
            }else{
                return "Pending";
            }
        }else{
            return "Rejected";
        }
    }
    private Boolean isCollectMoney(final String uid){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CollectedReceiptMoneyFromCustomer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ReceiptModelCustomer customer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                    if (customer != null) {
                        if (customer.getCustomer_uid().equals(uid)) {

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return false;
    }

    @SuppressLint("ResourceAsColor")
    private void Paidstatus(Boolean paid,Boolean reject,Boolean collect,Boolean withdraw,ImageView iv_paid_status){
        String paidText = status(paid,reject,collect,withdraw);
        Animation animation;
        animation = new RotateAnimation(45.0f, 45.0f, 0, 0);
        //"Save" the results of the animation
        animation.setFillAfter(true);
        //Set the animation duration to zero, just in case
        animation.setDuration(0);
        //Assign the animation to the TextView
       // tv.setAnimation(animation);

        if (paidText.equals("Paid")){
            iv_paid_status.setImageResource(R.drawable.paid);
        }else if (paidText.equals("Pending")){
            iv_paid_status.setImageResource(R.drawable.pending);
        }else if (paidText.equals("Rejected")){
            iv_paid_status.setImageResource(R.drawable.rejected);
        }else if (paidText.equals("Collected")){
            iv_paid_status.setImageResource(R.drawable.collected);
        }else if (paidText.equals("Withdraw Requested")){
            iv_paid_status.setImageResource(R.drawable.withdrawrequest);
        }
    }

    private void SubmitBadCustomerReportToAdminDialog(String uid, final String receipt_id, String user_id, String user_invoice, String user_discount, String user_total_val, String user_discount_val){

        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Users").child("customer").child(uid);
        receiptReference = FirebaseDatabase.getInstance().getReference().child("receiptBYCustomer").child(receipt_id);



        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bad_customer_report_submnit_to_customer);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final CircularImageView pic = dialog.findViewById(R.id.civ_profile_picture_customer_in_dialog_box);
        final TextView name = dialog.findViewById(R.id.tv_customer_name_in_dialog_box);
        final TextView phone = dialog.findViewById(R.id.tv_customer_phone_number_in_dialog_box);

        TextView id = dialog.findViewById(R.id.id);
        id.setText(user_id);
        TextView invoice = dialog.findViewById(R.id.invoice_number);
        invoice.setText(user_invoice);
        TextView discount = dialog.findViewById(R.id.discount);
        discount.setText(user_discount);
        TextView total_val = dialog.findViewById(R.id.total_balance);
        total_val.setText(user_total_val+"/=");
        TextView discount_val = dialog.findViewById(R.id.discount_value);
        discount_val.setText(user_discount_val+"/=");




        CustomerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Picasso.with(context).load(dataSnapshot.child("profile_pic_url").getValue().toString()).placeholder(R.drawable.defaultpic).into(pic);
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    phone.setText(dataSnapshot.child("phone").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //pic up current date from my user device
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        final String date = currentDate.format(calForDate.getTime());

        // pic up current time from my user device
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        final String time = currentTime.format(calForTime.getTime());




        ImageButton close_btn = dialog.findViewById(R.id.bt_close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button cancel_btn = dialog.findViewById(R.id.bt_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button submit_btn = dialog.findViewById(R.id.bt_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!rejected_status){
                    receiptReference.child("shopper_rejected_request_status").setValue(true);
                    receiptReference.child("shopper_rejected_request_date").setValue(date);
                    receiptReference.child("shopper_rejected_request_time").setValue(time);
                    Toast.makeText(context, "Submit", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Already Send Rejected status", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

}
