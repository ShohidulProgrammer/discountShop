package alamin.game.discountappofshopers.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.ReviewModel;

public class CustomerReviewAdapter extends RecyclerView.Adapter<CustomerReviewAdapter.ReviewListHolder>{
    private Context context;
    private List<ReviewModel> reviewModelList;
    private ReviewModel reviewModel;
    DatabaseReference databaseReference ;
    String Name;

    public CustomerReviewAdapter(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }

    @NonNull
    @Override
    public ReviewListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_show_to_admin, parent,false);
        return new ReviewListHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewListHolder holder, int position) {
        reviewModel = reviewModelList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

//        databaseReference.child("shopper").child(reviewModel.getUid()).child("shop_name").addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshot.getValue();
//                //holder.tv_shop_name.setText("("+dataSnapshot.getValue().toString()+")");
//                holder.tv_shop_name.setText("abc");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        holder.tv_shop_name.setText("abc");

        holder.tv_review_text.setText(reviewModel.getReview_message());
        holder.tv_review_time.setText(reviewModel.getReview_date());
        //holder.tv_reviewer_name.setText(customerName(reviewModel.getReviewr_fb_id()));
        holder.tv_reviewer_name.setText("alamin");

    }

//    private String customerName( String reviewr_fb_id) {
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference.child("customer").child(reviewr_fb_id).child("name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Name = dataSnapshot.getValue().toString();
//                return;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//        });
//        //return dataSnapshot.getValue().toString();
//        if (Name != null){
//            return Name;
//        }else {
//            return null;
//        }
//
//    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }
    class ReviewListHolder extends RecyclerView.ViewHolder{

        private TextView tv_reviewer_name;
        private TextView tv_shop_name;
        private TextView tv_review_text;
        private TextView tv_review_time;

        public ReviewListHolder(@NonNull View itemView) {
            super(itemView);
            tv_reviewer_name = itemView.findViewById(R.id.tv_reviewer_name);
            tv_shop_name = itemView.findViewById(R.id.tv_shop_name);
            tv_review_text = itemView.findViewById(R.id.tv_review_text);
            tv_review_time = itemView.findViewById(R.id.tv_review_time);
        }
    }
}
