package alamin.game.discountappofshopers.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;

public class CustomerOfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List items = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_shop_logo_customer_offer_item_layout;
        private TextView tv_shop_name_customer_offer_item_layout;
        private TextView tv_shop_discount_customer_offer_item_layout;
        public OriginalViewHolder(View v) {
            super(v);
            iv_shop_logo_customer_offer_item_layout = v.findViewById(R.id.iv_shop_logo_customer_offer_item_layout);
            tv_shop_name_customer_offer_item_layout = v.findViewById(R.id.tv_shop_name_customer_offer_item_layout);
            tv_shop_discount_customer_offer_item_layout = v.findViewById(R.id.tv_shop_discount_customer_offer_item_layout);
        }
    }
}
