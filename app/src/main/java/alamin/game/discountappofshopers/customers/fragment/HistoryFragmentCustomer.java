package alamin.game.discountappofshopers.customers.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.adapter.CustomerWithdrawalStatusAdapter;
import alamin.game.discountappofshopers.customers.WithdrawActivity;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;

public class HistoryFragmentCustomer extends Fragment {
    private RecyclerView rv_customer_history;
    private DatabaseReference databaseReference;
    private List<ReceiptModelCustomer> receiptModelCustomerList = new ArrayList<>();
    private CustomerWithdrawalStatusAdapter customerWithdrawalStatusAdapter;
    private Button btn_withdraw;
    float customer_discound_money = 0;
    private Button total_balance;
    private boolean isTouch = false;
    private FirebaseAuth firebaseAuth;
    private String current_uid;


    public HistoryFragmentCustomer() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_fragment_customer, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            current_uid = firebaseAuth.getCurrentUser().getUid();
        }

        btn_withdraw = view.findViewById(R.id.btn_withdraw);
        total_balance  = view.findViewById(R.id.total_balance);
        rv_customer_history = view.findViewById(R.id.rv_customer_history);

        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ReceiptModelCustomer receiptModelCustomer = dataSnapshot1.getValue(ReceiptModelCustomer.class);
                    if (receiptModelCustomer.getCustomer_uid().toString().equals(current_uid)){
                        receiptModelCustomerList.add(receiptModelCustomer);
                        if (receiptModelCustomer.getCollect_money_from_shop_status() && !receiptModelCustomer.getWithdrawal_request_status()){
                            customer_discound_money = customer_discound_money + Float.valueOf(receiptModelCustomer.getDiscount_money());
                        }
                    }
                }
                if (receiptModelCustomerList.size() > 0){
                    customerWithdrawalStatusAdapter = new CustomerWithdrawalStatusAdapter(getActivity(),receiptModelCustomerList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rv_customer_history.setLayoutManager(layoutManager);
                    rv_customer_history.setItemAnimator(new DefaultItemAnimator());
                    rv_customer_history.setAdapter(customerWithdrawalStatusAdapter);
                }
                priority_of_withdraw_btn(btn_withdraw,customer_discound_money);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        total_balance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN: // Action you you want on finger down.
                        total_balance.setText(String.valueOf(customer_discound_money));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        total_balance.setText(String.valueOf("Tab To Balance"));
                        break;
                }
                return false;
            }
        });

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer_discound_money>=100){
                    Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                    intent.putExtra("User_balance",String.valueOf(customer_discound_money));
                    startActivity(intent);
                }else {
                    insufficient_balanceDialog();
                }
            }
        });

        return view;
    }

    @SuppressLint("NewApi")
    private void priority_of_withdraw_btn(Button withdrawbtn, float customer_discound_money) {
        if (customer_discound_money < 100){
            withdrawbtn.setBackground(getResources().getDrawable(R.color.bg_text));
        }
//        if (customer_discound_money>100){
//            
//        }
    }

    private void insufficient_balanceDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.insufficient_attention_layout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
