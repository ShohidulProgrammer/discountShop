package alamin.game.discountappofshopers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.RegistrationModelCustomer;
import alamin.game.discountappofshopers.model.ReviewModel;

public class ReviewRetrieveAdapter extends RecyclerView.Adapter<ReviewRetrieveAdapter.ViewHolder>{

    private ArrayList<ReviewModel> list = new ArrayList<>();
    private ReviewModel reviewModel;
    private Context context;

    public ReviewRetrieveAdapter(ArrayList<ReviewModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_retreived_layout,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        reviewModel = list.get(position);
//        holder.tv_reviewer_name_retrieve_page.setText("alamin");
        holder.tv_review_time_retrieve_page.setText("("+reviewModel.getReview_date()+")");
        holder.tv_review_text_retrieve_page.setText(reviewModel.getReview_message());


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("customer").child(reviewModel.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelCustomer customer = dataSnapshot.getValue(RegistrationModelCustomer.class);
                if (customer != null) {
                    holder.tv_reviewer_name_retrieve_page.setText(customer.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_reviewer_name_retrieve_page;
        TextView tv_review_time_retrieve_page;
        TextView tv_review_text_retrieve_page;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_reviewer_name_retrieve_page = itemView.findViewById(R.id.tv_reviewer_name_retrieve_page);
            tv_review_time_retrieve_page = itemView.findViewById(R.id.tv_review_time_retrieve_page);
            tv_review_text_retrieve_page = itemView.findViewById(R.id.tv_review_text_retrieve_page);
        }
    }
}
