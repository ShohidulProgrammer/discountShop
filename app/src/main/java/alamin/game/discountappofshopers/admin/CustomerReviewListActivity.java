package alamin.game.discountappofshopers.admin;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.CustomerReviewAdapter;
import alamin.game.discountappofshopers.model.ReviewModel;

public class CustomerReviewListActivity extends AppCompatActivity {
    private RecyclerView rv_review_list;
    private DatabaseReference databaseReference;
    private List<ReviewModel> reviewModelList = new ArrayList<>();
    private CustomerReviewAdapter adapter;
    private Toolbar toolbar_review;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_review_list);
        rv_review_list = findViewById(R.id.rv_review_list);
        toolbar_review = findViewById(R.id.toolbar_review);
        setSupportActionBar(toolbar_review);
        toolbar_review.setTitle("Customer Review List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("user_review");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReviewModel reviewModel = dataSnapshot1.getValue(ReviewModel.class);
                    reviewModelList.add(reviewModel);
                }
                if (reviewModelList.size()>0){
                    adapter = new CustomerReviewAdapter(CustomerReviewListActivity.this,reviewModelList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CustomerReviewListActivity.this);
                    rv_review_list.setLayoutManager(mLayoutManager);
                    rv_review_list.setItemAnimator(new DefaultItemAnimator());
                    rv_review_list.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
