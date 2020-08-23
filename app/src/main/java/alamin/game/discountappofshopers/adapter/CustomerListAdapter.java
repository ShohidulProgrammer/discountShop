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
import alamin.game.discountappofshopers.admin.CustomerProfileActivity;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerListHolder> implements Filterable {
    private Context context;
    private List<RegistrationModelCustomer> registrationModelCustomerList = new ArrayList<RegistrationModelCustomer>();
    private List<RegistrationModelCustomer> registrationModelCustomerFilter = new ArrayList<RegistrationModelCustomer>();
    private RegistrationModelCustomer registrationModelCustomer;

    public CustomerListAdapter(Context context, List<RegistrationModelCustomer> registrationModelCustomerList) {
        this.context = context;
        this.registrationModelCustomerList = registrationModelCustomerList;
        this.registrationModelCustomerFilter = registrationModelCustomerList;
    }
    @NonNull
    @Override
    public CustomerListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.customer_list_item_layout, parent,false);
        return new CustomerListHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull CustomerListHolder holder, final int position) {
        registrationModelCustomer = registrationModelCustomerFilter.get(position);
        holder.tv_shop_name_adminList.setText(registrationModelCustomer.getName());
        holder.tv_shop_phone_adminList.setText(registrationModelCustomer.getPhone());
        holder.tv_shop_address_adminList.setText(registrationModelCustomer.getGender());
        holder.rl_customer_profile_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomerProfileActivity.class);
                intent.putExtra("uid",registrationModelCustomerFilter.get(position).getFb_id());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return registrationModelCustomerFilter.size();
    }

    class CustomerListHolder extends RecyclerView.ViewHolder{
        private TextView tv_shop_name_adminList;
        private TextView tv_shop_phone_adminList;
        private TextView tv_shop_address_adminList;
        private RelativeLayout rl_customer_profile_activity;
        private CustomerListHolder(@NonNull View itemView) {
            super(itemView);
            tv_shop_name_adminList = itemView.findViewById(R.id.tv_shop_name_adminList);
            tv_shop_phone_adminList = itemView.findViewById(R.id.tv_shop_phone_adminList);
            tv_shop_address_adminList = itemView.findViewById(R.id.tv_shop_address_adminList);
            rl_customer_profile_activity = itemView.findViewById(R.id.rl_customer_profile_activity);
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
            if (charSequence == null || charSequence.length() == 0){
                Log.d("QueryString", charSequence + " If true Character Data");
                registrationModelCustomerFilter = registrationModelCustomerList;
            }else {
                Log.d("QueryString", charSequence + " If false Character Data");
                List<RegistrationModelCustomer> filteredList = new ArrayList<>();
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (RegistrationModelCustomer registrationModelCustomer1 : registrationModelCustomerList){
                    if (registrationModelCustomer1.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(registrationModelCustomer1);
                    }
                }
                registrationModelCustomerFilter = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = registrationModelCustomerFilter;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            registrationModelCustomerFilter = (ArrayList<RegistrationModelCustomer>) filterResults.values;
            notifyDataSetChanged();
        }
    };

}
