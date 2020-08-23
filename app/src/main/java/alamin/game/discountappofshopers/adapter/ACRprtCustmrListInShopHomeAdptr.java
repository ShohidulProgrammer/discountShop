package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.ReceiptDetailsActivity;
import alamin.game.discountappofshopers.model.ReceiptModelShopper;

public class ACRprtCustmrListInShopHomeAdptr extends RecyclerView.Adapter<ACRprtCustmrListInShopHomeAdptr.ACReportCustomerListInShopHomeHolder>{

    private Context context;
    private List<ReceiptModelShopper> receiptModelShopperList = new ArrayList<>();
    private List<ReceiptModelShopper> receiptModelShopperListFilter = new ArrayList<>();
    private ReceiptModelShopper receiptModelShopper;

    public ACRprtCustmrListInShopHomeAdptr(Context context, List<ReceiptModelShopper> receiptModelShopperList) {
        this.context = context;
        this.receiptModelShopperList = receiptModelShopperList;
        this.receiptModelShopperListFilter = receiptModelShopperList;
    }
    @NonNull
    @Override
    public ACReportCustomerListInShopHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_report_item_list, parent, false);
        return new ACRprtCustmrListInShopHomeAdptr.ACReportCustomerListInShopHomeHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final ACReportCustomerListInShopHomeHolder holder, final int position) {
        receiptModelShopper = receiptModelShopperListFilter.get(position);
        holder.tv_invoice_number_adminList.setText(receiptModelShopper.getReceipt_no());
        holder.tv_back_amount_adminList.setText(receiptModelShopper.getCustomer_return_value());
        holder.tv_status_adminList.setText(checkStatus(receiptModelShopper.getPaid_status()));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReceiptDetailsActivity.class);
                intent.putExtra("uid", String.valueOf(receiptModelShopperList.get(position).getRandom_key()));
                intent.putExtra("uid_customer", String.valueOf(receiptModelShopperList.get(position).getReceipt_customer_uid()));
                context.startActivity(intent);
            }
        });
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }
    private String checkStatus(Boolean status) {
        if (status) {
            return "paid";
        } else {
            return "unpaid";
        }
    }
    @Override
    public int getItemCount() {
        return receiptModelShopperListFilter.size();
    }
    class ACReportCustomerListInShopHomeHolder extends RecyclerView.ViewHolder{
        TextView tv_invoice_number_adminList;
        TextView tv_back_amount_adminList;
        TextView tv_status_adminList;
        RelativeLayout relativeLayout;
        private ACReportCustomerListInShopHomeHolder(@NonNull View itemView) {
            super(itemView);
            tv_invoice_number_adminList = itemView.findViewById(R.id.tv_invoice_number_adminList);
            tv_back_amount_adminList = itemView.findViewById(R.id.tv_back_amount_adminList);
            tv_status_adminList = itemView.findViewById(R.id.tv_status_adminList);
            relativeLayout = itemView.findViewById(R.id.ll_parent);
        }
    }
}
