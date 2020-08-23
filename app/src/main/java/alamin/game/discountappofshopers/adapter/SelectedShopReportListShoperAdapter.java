package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.ReceiptDetailsShopperActivity;
import alamin.game.discountappofshopers.admin.SelectCustomerAndCalculateValue;
import alamin.game.discountappofshopers.admin.SelectedShopReceiptListActivity;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;

public class SelectedShopReportListShoperAdapter extends RecyclerView.Adapter<SelectedShopReportListShoperAdapter.AcReportListHolder> implements Filterable , SelectCustomerAndCalculateValue {
    private Context context;
    private ReceiptModelCustomer receiptModelCustomer;
    private List<ReceiptModelCustomer> receiptModelCustomerArrayList = new ArrayList<ReceiptModelCustomer>();
    private List<ReceiptModelCustomer> receiptModelCustomerArrayFilter = new ArrayList<ReceiptModelCustomer>();
    private List<String> shopIdList = new ArrayList<String>();
    private DatabaseReference databaseReference;
    public double result = 0.0;
    private Map<String,String> selectedList = new HashMap<>();
    public SelectedShopReportListShoperAdapter(Context context, List<ReceiptModelCustomer> receiptModelCustomerArrayList) {
        this.context = context;
        this.receiptModelCustomerArrayList = receiptModelCustomerArrayList;
        this.receiptModelCustomerArrayFilter = receiptModelCustomerArrayList;
    }
    @NonNull
    @Override
    public AcReportListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_shop_receipt_item_list, parent,false);
        return new AcReportListHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final AcReportListHolder holder, final int position) {
        receiptModelCustomer = receiptModelCustomerArrayFilter.get(position);
        holder.tv_invoice_number_adminList.setText(receiptModelCustomer.getInvoiceNumber());
        holder.tv_back_amount_adminList.setText(receiptModelCustomer.getDiscount_money());
        //todo::First we was use this for checking status now we use this for customer name same field so we don't need this and next time if we need to use this then we will check it
        //holder.tv_status_adminList.setText(checkStatus(receiptModelCustomer.getCollect_money_from_shop_status(),receiptModelCustomer.getRejected_status()));

        //todo::after change status we use this
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").child(receiptModelCustomer.getCustomer_uid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.tv_status_adminList.setText(dataSnapshot.child("name").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.checkBox.isChecked()) {
                    result = result + Double.parseDouble(receiptModelCustomerArrayFilter.get(position).getDiscount_money());
                    SelectedShopReceiptListActivity.total_amount(result);
                    // tempCalculationValueStore( result,receiptModelCustomerArrayFilter.get(position).getRandom_key(),receiptModelCustomerArrayFilter.get(position).getCollect_money_from_shop_status());
                    //selectedList.put(receiptModelCustomerArrayFilter.get(position).getRandom_key(),receiptModelCustomerArrayFilter.get(position).getDiscount_money());
                    selectedList.put(receiptModelCustomerArrayFilter.get(position).getRandom_key(),receiptModelCustomerArrayFilter.get(position).getShop_uid());
                    shopIdList.add(receiptModelCustomerArrayFilter.get(position).getRandom_key());

                    Toast.makeText(context, "total : "+shopIdList.size(), Toast.LENGTH_SHORT).show();
                }
                if ( !holder.checkBox.isChecked()) {
                    result = result - Double.parseDouble(receiptModelCustomerArrayFilter.get(position).getDiscount_money());
                    shopIdList.remove(receiptModelCustomerArrayFilter.get(position).getRandom_key());
                    Toast.makeText(context, "total : "+shopIdList.size(), Toast.LENGTH_SHORT).show();
                    SelectedShopReceiptListActivity.total_amount(result);
                   if (selectedList.get(receiptModelCustomerArrayFilter.get(position).getRandom_key()) != null){
                       selectedList.remove(receiptModelCustomerArrayFilter.get(position).getRandom_key());
                   }
                }

            }
        });
        SelectedShopReceiptListActivity.btn_submit_selected_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopIdList.size()>0){
                    for (int a=0;a<shopIdList.size();a++){
                        changeSelectedShopMoney(shopIdList.get(a));
                    }

                }else {

                    Toast.makeText(context, "! First Select Item", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReceiptDetailsShopperActivity.class);
                intent.putExtra("uid",String.valueOf(receiptModelCustomerArrayList.get(position).getRandom_key()));
                intent.putExtra("uid_shopper",String.valueOf(receiptModelCustomerArrayList.get(position).getShop_uid()));
                intent.putExtra("uid_customer",String.valueOf(receiptModelCustomerArrayList.get(position).getCustomer_uid()));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return receiptModelCustomerArrayFilter.size();
    }

    @Override
    public void Customerinfo(String interfaceValue) {
        databaseReference = FirebaseDatabase.getInstance().getReference("CalculateCheck");
        databaseReference.child("value").setValue(interfaceValue);
    }

    @Override
    public Map<String, String> selectMoney() {
        return selectedList;
    }

    class AcReportListHolder extends RecyclerView.ViewHolder {
        TextView tv_invoice_number_adminList;
        TextView tv_back_amount_adminList;
        TextView tv_status_adminList;
        CheckBox checkBox;
        RelativeLayout relativeLayout;
        AcReportListHolder(@NonNull View itemView) {
            super(itemView);
            tv_invoice_number_adminList = itemView.findViewById(R.id.tv_invoice_number_adminList);
            tv_back_amount_adminList = itemView.findViewById(R.id.tv_back_amount_adminList);
            tv_status_adminList = itemView.findViewById(R.id.tv_status_adminList);
            checkBox = itemView.findViewById(R.id.calculateCheck);
            relativeLayout = itemView.findViewById(R.id.ll_parent);
        }
    }

    private Boolean isPaid(Boolean status){
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

    private String checkStatus(Boolean paidStatus, Boolean rejectStatus){
        if (isPaid(paidStatus) && !isReject(rejectStatus))
            return "Collected";
        else if (!isPaid(paidStatus) && !isReject(rejectStatus))
            return "Need Collect";
        else
            return "Rejected";

    }

    private void tempCalculationValueStore(double value,String id,Boolean status){
//        databaseReference = FirebaseDatabase.getInstance().getReference("CalculateCheck");
//        databaseReference.child("value").setValue(value);
//        databaseReference.child("id").setValue(id);
//        databaseReference.child("status").setValue(status);
    }

//    private String checkStatus(Boolean status){
//        if(status){
//            return "Collected";
//        }else{
//            return "Need Collect";
//        }
//    }

    @Override
    public Filter getFilter() {
        return filterShopper;
    }
    private Filter filterShopper = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Log.d("QueryString", charSequence + " Character Data");
            if (charSequence == null || charSequence.length() == 0){
                Log.d("QueryString", charSequence + " If true Character Data");
                receiptModelCustomerArrayFilter = receiptModelCustomerArrayList;
            }else {
                Log.d("QueryString", charSequence + " If false Character Data");
                List<ReceiptModelCustomer> filteredList = new ArrayList<>();
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ReceiptModelCustomer receiptModelCustomer1 : receiptModelCustomerArrayList){
                    if (receiptModelCustomer1.getInvoiceNumber().toLowerCase().contains(filterPattern) && searchforPaidStatus(receiptModelCustomer1.getCollect_money_from_shop_status(),receiptModelCustomer1.getRejected_status(),receiptModelCustomer1.getBack_money_to_customer_status()).toLowerCase().contains(filterPattern)){
                        filteredList.add(receiptModelCustomer1);
                    }
                }
                receiptModelCustomerArrayFilter = filteredList;
            }
            FilterResults results = new FilterResults();
            results.values = receiptModelCustomerArrayFilter;
            return results;

        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            receiptModelCustomerArrayFilter = (ArrayList<ReceiptModelCustomer>) filterResults.values;
            notifyDataSetChanged();
        }
    };
    private String searchforPaidStatus(Boolean collect_money_from_shop_status, Boolean rejected_status, Boolean back_money_to_customer_status) {
        if (collect_money_from_shop_status)

            if (back_money_to_customer_status && !rejected_status)
                return "Collected";
            else if (!back_money_to_customer_status && !rejected_status)
                return "Need Collect";
            else
                return "Rejected";
        return null;
    }

    private void changeSelectedShopMoney(String uid){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("receiptBYCustomer").child(uid);
        //pic up current date from my user device
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        String date = currentDate.format(calForDate.getTime());

        // pic up current time from my user device
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String time = currentTime.format(calForTime.getTime());

        databaseReference.child("collect_money_from_shop_date").setValue(date+" | "+time);
        databaseReference.child("collect_money_from_shop_status").setValue(true);


    }
}
