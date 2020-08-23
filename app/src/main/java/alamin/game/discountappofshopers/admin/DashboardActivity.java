package alamin.game.discountappofshopers.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.AcReportListShopperAdapter;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private static ArrayList<ReceiptModelCustomer> shopList = new ArrayList<>();
    private static ArrayList<String> list = new ArrayList<>();
    DatabaseReference databaseReference;
    TextView tv_shopperList_admin_dashboard,tv_customerList_admin_dashboard,tv_reportList_admin_dashboard,tv_customer_review,tv_notification_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        shop_List();

        tv_notification_btn = findViewById(R.id.tv_notification_btn);
        tv_notification_btn.setOnClickListener(this);

        tv_shopperList_admin_dashboard = findViewById(R.id.tv_shopperList_admin_dashboard);
        tv_shopperList_admin_dashboard.setOnClickListener(this);

        tv_customerList_admin_dashboard = findViewById(R.id.tv_customerList_admin_dashboard);
        tv_customerList_admin_dashboard.setOnClickListener(this);

        tv_reportList_admin_dashboard = findViewById(R.id.tv_reportList_admin_dashboard);
        tv_reportList_admin_dashboard.setOnClickListener(this);

        tv_customer_review = findViewById(R.id.tv_customer_review);
        tv_customer_review.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_shopperList_admin_dashboard:
                nextActivity(ShopperListActivity.class);
                break;
            case R.id.tv_customerList_admin_dashboard:
                nextActivity(CustomerListActivity.class);
                break;
            case R.id.tv_reportList_admin_dashboard:
                openDialogBox();
                break;
           case R.id.tv_customer_review:
                nextActivity(MerchantCreateActivity.class);
                break;
           case R.id.tv_notification_btn:
                nextActivity(NotificationActivity.class);
                break;
        }
    }
    public void nextActivity(Class selectedClass){
        Intent intent = new Intent(DashboardActivity.this,selectedClass);
        startActivity(intent);
    }
    public void openDialogBox(){
        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_and_shoper);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ImageView ib_close_dialog = dialog.findViewById(R.id.ib_close_dialog);
        ib_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((TextView)dialog.findViewById(R.id.tv_shopper)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(ReportListShopperActivity.class);
                Toast.makeText(DashboardActivity.this, "shopper", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        ((TextView)dialog.findViewById(R.id.tv_customer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(ReportListCustomerActivity.class);
                Toast.makeText(DashboardActivity.this, "customer", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    //todo:: i don't use this two method that is shop_list() and isShopFound(abc)
    private void shop_List(){
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReceiptModelCustomer receiptModelCustomer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                    shopList.add(receiptModelCustomer);
                }
                Toast.makeText(DashboardActivity.this, "total : "+shopList.size(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static Boolean isShopFound(String shop_id){
        for (int a =0; a<shopList.size(); a++){
            if (!shopList.get(a).getShop_uid().equals(shop_id)){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

}
