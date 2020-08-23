package alamin.game.discountappofshopers.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;

public class UserSelectActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_customer,btn_shopper;
    PreferenceData preferenceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);
        this.preferenceData = new PreferenceData(UserSelectActivity.this);

        btn_customer = findViewById(R.id.btn_customer);
        btn_customer.setOnClickListener(this);

        btn_shopper = findViewById(R.id.btn_shopper);
        btn_shopper.setOnClickListener(this);
        movedNextActivity(LoginActivity.class,"customer");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_customer:
                movedNextActivity(LoginActivity.class,"customer");
                break;
            case R.id.btn_shopper:
                movedNextActivity(LoginActivity.class,"shopper");
                break;

        }
    }
    private void movedNextActivity(Class seletedActivity,String selete) {

        Intent intent = new Intent(UserSelectActivity.this,seletedActivity);
        intent.putExtra("selectFromUserSelectActivity",selete);
        startActivity(intent);
        finish();
    }

}
