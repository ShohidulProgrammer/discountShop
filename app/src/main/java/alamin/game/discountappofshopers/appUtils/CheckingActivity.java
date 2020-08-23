package alamin.game.discountappofshopers.appUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.shoppers.ShopperHomeActivity;

public class CheckingActivity extends AppCompatActivity {

    Button btn_update;
    String serverversion;
    String deviceVirsion =  "1";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);
        btn_update = findViewById(R.id.btn_update);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serverversion = dataSnapshot.child("version").getValue().toString();
                if (serverversion == deviceVirsion){
                    Intent intent = new Intent(CheckingActivity.this, ShopperHomeActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }
    private void update(){
        Toast.makeText(CheckingActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
    }
}
