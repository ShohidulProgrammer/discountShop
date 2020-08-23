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

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.ReceiptDetailsActivity;
import alamin.game.discountappofshopers.admin.ReportListCustomerActivity;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.ReceiptModelShopper;

public class AcReportListAdapter extends RecyclerView.Adapter<AcReportListAdapter.AcReportListHolder> implements Filterable {
    private Context context;
    private ReceiptModelCustomer receiptModelCustomer;
    private List<ReceiptModelCustomer> receiptModelCustomerArrayList = new ArrayList<ReceiptModelCustomer>();
    private List<ReceiptModelCustomer> receiptModelCustomerArrayListFilter = new ArrayList<ReceiptModelCustomer>();

    public AcReportListAdapter(Context context, ArrayList<ReceiptModelCustomer> receiptModelCustomerArrayList) {
        this.context = context;
        this.receiptModelCustomerArrayList = receiptModelCustomerArrayList;
        this.receiptModelCustomerArrayListFilter = receiptModelCustomerArrayList;
    }

    @NonNull
    @Override
    public AcReportListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_report_item_list, parent, false);
        return new AcReportListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AcReportListHolder holder, final int position) {
        receiptModelCustomer = receiptModelCustomerArrayListFilter.get(position);
        holder.tv_invoice_number_adminList.setText(receiptModelCustomer.getInvoiceNumber());
        holder.tv_back_amount_adminList.setText(receiptModelCustomer.getDiscount_money());
        holder.tv_status_adminList.setText(checkStatus(receiptModelCustomer.getBack_money_to_customer_status()));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReceiptDetailsActivity.class);
                intent.putExtra("uid", String.valueOf(receiptModelCustomerArrayList.get(position).getRandom_key()));
                intent.putExtra("uid_customer", String.valueOf(receiptModelCustomerArrayList.get(position).getCustomer_uid()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiptModelCustomerArrayListFilter.size();
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

    private String checkStatus(Boolean status) {
        if (status) {
            return "paid";
        } else {
            return "unpaid";
        }
    }

    @Override
    public Filter getFilter() {
        return customerListFilter;
    }

    private Filter customerListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Log.d("QueryString", charSequence + " Character Data");
            if (charSequence == null || charSequence.length() == 0) {
                Log.d("QueryString", charSequence + " If true Character Data");
                receiptModelCustomerArrayListFilter = receiptModelCustomerArrayList;
            } else {
                Log.d("QueryString", charSequence + " If false Character Data");
                List<ReceiptModelCustomer> filteredList = new ArrayList<>();
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ReceiptModelCustomer receiptModelCustomer1 : receiptModelCustomerArrayList) {
                    if (receiptModelCustomer1.getInvoiceNumber().toLowerCase().contains(filterPattern)) {
                        filteredList.add(receiptModelCustomer1);
                    }
                }
                receiptModelCustomerArrayListFilter = filteredList;
            }
            FilterResults results = new FilterResults();
            results.values = receiptModelCustomerArrayListFilter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            receiptModelCustomerArrayListFilter = (ArrayList<ReceiptModelCustomer>)filterResults.values;
            notifyDataSetChanged();

        }
    };

}
