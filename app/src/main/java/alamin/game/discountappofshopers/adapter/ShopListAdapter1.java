package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.SelectedShopReceiptListActivity;
import alamin.game.discountappofshopers.admin.ShopperProfileActivity;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class ShopListAdapter1 extends RecyclerView.Adapter<ShopListAdapter1.ShopListHolder> implements Filterable{

    private Context context;
    private List<RegistrationModelShopper> registrationModelShopperList;
    private List<RegistrationModelShopper> customerfilteredList;
    private RegistrationModelShopper registrationModelShopper;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private AdapterView.OnItemClickListener listener;

    public ShopListAdapter1(Context context, List<RegistrationModelShopper> registrationModelShopperList) {
        this.context = context;
        this.registrationModelShopperList = registrationModelShopperList;
        this.customerfilteredList = registrationModelShopperList;
    }

    @NonNull
    @Override
    public ShopListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list, parent,false);
        return new ShopListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopListHolder holder, final int position) {
        registrationModelShopper = customerfilteredList.get(position);
        holder.tv_shop_name_adminList.setText(registrationModelShopper.getShop_name());
        holder.tv_shop_phone_adminList.setText(registrationModelShopper.getShopper_phone());
        holder.tv_shop_address_adminList.setText(registrationModelShopper.getShop_location_manually());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectedShopReceiptListActivity.class);
                intent.putExtra("uid",registrationModelShopperList.get(position).getFb_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerfilteredList.size();
    }

    class ShopListHolder extends RecyclerView.ViewHolder {
        private TextView tv_shop_name_adminList;
        private TextView tv_shop_phone_adminList;
        private TextView tv_shop_address_adminList;

        private TextView tv_total_due;

        private TextView tv_shop_history_adminList;
        private RelativeLayout ll_parent;

        public ShopListHolder(@NonNull View itemView) {
            super(itemView);
            tv_shop_name_adminList = itemView.findViewById(R.id.tv_shop_name_adminList);
            tv_shop_phone_adminList = itemView.findViewById(R.id.tv_shop_phone_adminList);
            tv_shop_address_adminList = itemView.findViewById(R.id.tv_shop_address_adminList);

            tv_total_due = itemView.findViewById(R.id.tv_total_due);

            tv_shop_history_adminList = itemView.findViewById(R.id.tv_shop_history_adminList);

            ll_parent = itemView.findViewById(R.id.ll_parent);
        }


    }
    @Override
    public Filter getFilter() {
        return shopListFilter;
    }

    private Filter shopListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null || constraint.length() == 0){
                customerfilteredList = registrationModelShopperList;
            }else {
                List<RegistrationModelShopper> filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (RegistrationModelShopper registrationModelShopper1 : registrationModelShopperList){
                    if (registrationModelShopper1.getShop_name().toLowerCase().contains(filterPattern)){
                        filteredList.add(registrationModelShopper1);
                    }
                }
                customerfilteredList = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = customerfilteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            customerfilteredList = (ArrayList<RegistrationModelShopper>) results.values;
           notifyDataSetChanged();
        }
    };

    private void totaldue(TextView tv_total_due){

        final ArrayList<String> receiptModelCustomerArrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("receiptBYCustomer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiptModelCustomerArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    ReceiptModelCustomer receiptModelCustomer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                    receiptModelCustomerArrayList.add(receiptModelCustomer.getShop_uid());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
