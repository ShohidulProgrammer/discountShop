package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.MarketingPeopleModel;

public class MarketingPeopleAdapter extends RecyclerView.Adapter<MarketingPeopleAdapter.MarketingPeopleViewHolder> {
    List<MarketingPeopleModel> list = new ArrayList<>();
    Context context;
    MarketingPeopleModel marketingPeopleModel;

    public MarketingPeopleAdapter(List<MarketingPeopleModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MarketingPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.marchant_list_item,parent,false);
        return new MarketingPeopleViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MarketingPeopleViewHolder holder, int position) {
        marketingPeopleModel = list.get(position);

        holder.referral.setText(marketingPeopleModel.getReferral());
        holder.name.setText(marketingPeopleModel.getName());
        holder.phone.setText(marketingPeopleModel.getPhone());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MarketingPeopleViewHolder extends RecyclerView.ViewHolder{
        TextView referral;
        TextView name;
        TextView phone;

       public MarketingPeopleViewHolder(@NonNull View itemView) {
           super(itemView);
           referral = itemView.findViewById(R.id.referral_number);
           name = itemView.findViewById(R.id.name);
           phone = itemView.findViewById(R.id.phone);
       }
   }
}
