package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;
import alamin.game.discountappofshopers.shoppers.ShopProfileCustomerViewActivity;

public class ShopListWithDiscountAdapter extends RecyclerView.Adapter<ShopListWithDiscountAdapter.ShopListWithDiscountHolder> implements Filterable {
    private Context context;
    private List<RegistrationModelShopper> registrationModelShopperList = new ArrayList<>();
    private List<RegistrationModelShopper> registrationModelShopperFilter = new ArrayList<>();
    private RegistrationModelShopper registrationModelShopper;

    public ShopListWithDiscountAdapter(Context context, List<RegistrationModelShopper> registrationModelShopperList) {
        this.context = context;
        this.registrationModelShopperList = registrationModelShopperList;
        this.registrationModelShopperFilter = registrationModelShopperList;
    }
    @NonNull
    @Override
    public ShopListWithDiscountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopper_list_item_in_customer_home_layout, parent,false);
        return new ShopListWithDiscountHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final ShopListWithDiscountHolder holder, final int position) {
        registrationModelShopper = registrationModelShopperFilter.get(position);
        holder.tv_shopper_name_Customer_home.setText(registrationModelShopper.getShop_name());
        holder.tv_shopper_address_Customer_home.setText(registrationModelShopper.getShop_location_manually());
        holder.tv_shopper_discount_Customer_home.setText("Discount "+String.valueOf(registrationModelShopper.getDiscount())+"%");
        Picasso.with(context).load(registrationModelShopper.getShop_pic_url()).placeholder(R.drawable.shop).into(holder.iv_shopper_select_Customer_home);
        holder.iv_shopper_select_Customer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ShopProfileCustomerViewActivity.class);
                intent.putExtra("selected_shop_uid",String.valueOf(registrationModelShopperList.get(position).getFb_id()));
                context.startActivity(intent);
                //Toast.makeText(context, "id : "+registrationModelShopperList.get(position).getFb_id(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return registrationModelShopperFilter.size();
    }

    public void filteredList(ArrayList<RegistrationModelShopper> filterList) {
        registrationModelShopperList = filterList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return shopListFilter;
    }
    private Filter shopListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null || constraint.length() == 0){
                Log.d("QueryString", constraint + " If true Character Data");
                registrationModelShopperFilter = registrationModelShopperList;
            }else {
                Log.d("QueryString", constraint + " If false Character Data");
                List<RegistrationModelShopper> filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (RegistrationModelShopper shopperfiltarable : registrationModelShopperList){
                    if (shopperfiltarable.getShop_name().toLowerCase().contains(filterPattern)){
                        filteredList.add(shopperfiltarable);
                    }
                }
                registrationModelShopperFilter = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = registrationModelShopperFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            registrationModelShopperFilter = (ArrayList<RegistrationModelShopper>) results.values;
            notifyDataSetChanged();
        }
    };

    class ShopListWithDiscountHolder extends RecyclerView.ViewHolder {
        TextView tv_shopper_name_Customer_home;
        TextView tv_shopper_address_Customer_home;
        TextView tv_shopper_discount_Customer_home;
        ImageView iv_shopper_select_Customer_home;
        public ShopListWithDiscountHolder(@NonNull View itemView) {
            super(itemView);
            tv_shopper_name_Customer_home = itemView.findViewById(R.id.tv_shopper_name_Customer_home);
            tv_shopper_address_Customer_home = itemView.findViewById(R.id.tv_shopper_address_Customer_home);
            tv_shopper_discount_Customer_home = itemView.findViewById(R.id.tv_shopper_discount_Customer_home);
            iv_shopper_select_Customer_home = itemView.findViewById(R.id.iv_shopper_select_Customer_home);
        }
    }
}
