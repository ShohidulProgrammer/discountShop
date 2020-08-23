package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.ReceiptDetailsShopperActivity;
import alamin.game.discountappofshopers.admin.SelectedShopReceiptListActivity;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.ReceiptModelShopper;

public class AcReportListShopperAdapter extends RecyclerView.Adapter<AcReportListShopperAdapter.AcReportListHolder> implements Filterable {
    private Context context;
    private ReceiptModelCustomer receiptModelCustomer;
    private List<ReceiptModelCustomer> receiptModelCustomerArrayList = new ArrayList<ReceiptModelCustomer>();
    private List<ReceiptModelCustomer> receiptModelCustomerArrayFilter = new ArrayList<ReceiptModelCustomer>();


    public AcReportListShopperAdapter(Context context, List<ReceiptModelCustomer> receiptModelCustomerArrayList) {
        this.context = context;
        this.receiptModelCustomerArrayList = receiptModelCustomerArrayList;
        this.receiptModelCustomerArrayFilter = receiptModelCustomerArrayList;
    }
    @NonNull
    @Override
    public AcReportListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_report_item_list, parent,false);
        return new AcReportListHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final AcReportListHolder holder, final int position) {
        receiptModelCustomer = receiptModelCustomerArrayFilter.get(position);
        holder.tv_invoice_number_adminList.setText(receiptModelCustomer.getInvoiceNumber());
        holder.tv_back_amount_adminList.setText(receiptModelCustomer.getDiscount_money());
//      holder.tv_status_adminList.setText(checkStatus(receiptModelCustomer.getCollect_money_from_shop_status(),receiptModelCustomer.getRejected_status()));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(receiptModelCustomer.getShop_uid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.tv_status_adminList.setText(dataSnapshot.child("shop_name").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.tv_status_adminList.setText(receiptModelCustomer.getShop_uid());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectedShopReceiptListActivity.class);
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
    class AcReportListHolder extends RecyclerView.ViewHolder {
        TextView tv_invoice_number_adminList;
        TextView tv_back_amount_adminList;
        TextView tv_status_adminList;
        RelativeLayout relativeLayout;
        AcReportListHolder(@NonNull View itemView) {
            super(itemView);
            tv_invoice_number_adminList = itemView.findViewById(R.id.tv_invoice_number_adminList);
            tv_back_amount_adminList = itemView.findViewById(R.id.tv_back_amount_adminList);
            tv_status_adminList = itemView.findViewById(R.id.tv_status_adminList);
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
}
