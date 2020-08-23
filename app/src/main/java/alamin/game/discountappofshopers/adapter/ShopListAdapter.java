package alamin.game.discountappofshopers.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.admin.ShopperProfileActivity;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopListHolder> implements Filterable{
    private Context context;
    private List<RegistrationModelShopper> registrationModelShopperList;
    private List<RegistrationModelShopper> customerfilteredList;
    private RegistrationModelShopper registrationModelShopper;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private AdapterView.OnItemClickListener listener;


    public ShopListAdapter(Context context, List<RegistrationModelShopper> registrationModelShopperList) {
        this.context = context;
        this.registrationModelShopperList = registrationModelShopperList;
        this.customerfilteredList = registrationModelShopperList;
    }

    @NonNull
    @Override
    public ShopListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopper_list_item_layout, parent,false);
        return new ShopListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopListHolder holder, final int position) {
        registrationModelShopper = customerfilteredList.get(position);
        holder.tv_shop_name_adminList.setText(registrationModelShopper.getShop_name());
        holder.tv_shop_phone_adminList.setText(registrationModelShopper.getShopper_phone());
        holder.tv_shop_address_adminList.setText(registrationModelShopper.getShop_location_manually());
        holder.tv_shop_discount_adminList.setText(String.valueOf("Discount : "+registrationModelShopper.getDiscount()+"%"));
        holder.tv_shop_discount_adminList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialog(String.valueOf(registrationModelShopperList.get(position).getFb_id()));
            }
        });

        holder.tv_shop_history_adminList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "history", Toast.LENGTH_SHORT).show();
            }
        });
//        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "this is parent layout from parent", Toast.LENGTH_SHORT).show();
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShopperProfileActivity.class);
                intent.putExtra("uid",registrationModelShopperList.get(position).getFb_id());
                context.startActivity(intent);
                Toast.makeText(context, ""+registrationModelShopperList.get(position).getShop_name(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void OpenDialog(final String uid) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.review_submit_from_customer_in_shopper_profile_item_layout);
        dialog.setCancelable(true);
        final EditText discount_value = dialog.findViewById(R.id.et_post);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("shopper").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelShopper modelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                if (modelShopper != null) {
                    Picasso.with(context).load(modelShopper.getShop_pic_url()).placeholder(R.drawable.defaultpic).into(((CircularImageView) dialog.findViewById(R.id.civ_profile_picture_customer_in_dialog_box)));
                    ((TextView) dialog.findViewById(R.id.tv_customer_name_in_dialog_box)).setText(modelShopper.getShop_name());
                    ((TextView) dialog.findViewById(R.id.tv_customer_phone_number_in_dialog_box)).setText(modelShopper.getShopper_phone());
                    discount_value.setText(String.valueOf(modelShopper.getDiscount()));
                    if(discount_value.getText() != null && !discount_value.getText().toString().equals("")){
                        discount_value.setSelection(discount_value.getText().length());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog.dismiss();
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        discount_value.setInputType(InputType.TYPE_CLASS_NUMBER);
        discount_value.setHint("Enter discount Value");
        discount_value.setMaxLines(3);
        discount_value.setMinLines(2);

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discount = discount_value.getText().toString().trim();
                if (TextUtils.isEmpty(discount)){
                    discount_value.setError("Empty");
                    discount_value.requestFocus();
                    return;
                }else {
                    databaseReference.child("shopper").child(uid).child("discount").setValue(Integer.valueOf(((EditText) dialog.findViewById(R.id.et_post)).getText().toString().trim()));
                    Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return customerfilteredList.size();
    }

    class ShopListHolder extends RecyclerView.ViewHolder {
        private TextView tv_shop_name_adminList;
        private TextView tv_shop_phone_adminList;
        private TextView tv_shop_address_adminList;

        private TextView tv_shop_discount_adminList;
        private TextView tv_shop_history_adminList;
        private RelativeLayout ll_parent;

        public ShopListHolder(@NonNull View itemView) {
            super(itemView);
            tv_shop_name_adminList = itemView.findViewById(R.id.tv_shop_name_adminList);
            tv_shop_phone_adminList = itemView.findViewById(R.id.tv_shop_phone_adminList);
            tv_shop_address_adminList = itemView.findViewById(R.id.tv_shop_address_adminList);

            tv_shop_discount_adminList = itemView.findViewById(R.id.tv_shop_discount_adminList);
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
}
