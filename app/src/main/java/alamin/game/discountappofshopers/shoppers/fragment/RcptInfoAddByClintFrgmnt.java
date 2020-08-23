package alamin.game.discountappofshopers.shoppers.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import alamin.game.discountappofshopers.PreferenceData;
import alamin.game.discountappofshopers.R;
import alamin.game.discountappofshopers.model.ReceiptModelCustomer;
import alamin.game.discountappofshopers.model.RegistrationModelShopper;

import static android.app.Activity.RESULT_OK;


public class RcptInfoAddByClintFrgmnt extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int CAMERA_PIC_REQUEST = 420 ;
    private EditText et_received_number,et_received_amount,et_customer_choose_item,et_time;
    private TextView tv_account_number;
    private ImageView iv_received_pic,iv_capture_received_pic;
    private Button btn_received_submit;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri mImageUri = null;
    private ProgressDialog progressDialog;
    private PreferenceData preferenceData;
    private String discount_value;
    private String Payment_method;
    private String payment_number;
//    private String[] method = { "Bank", "Bkash", "Rocket", "SureCash", "mCash","Nogot"};
    private String payment_gateway="";
    private String bankAccountNumber;
    private String receipt_discount;
    private String CurrentUser_Uid;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Context context;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    public RcptInfoAddByClintFrgmnt() {
        // Required empty public constructor
    }
    public static RcptInfoAddByClintFrgmnt newInstance() {
        RcptInfoAddByClintFrgmnt fragment = new RcptInfoAddByClintFrgmnt();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_received_info_add_by_cleient, container, false);
        this.preferenceData = new PreferenceData(getActivity());
        context = getActivity();

        CurrentUser_Uid = preferenceData.getStringValue("CurrentUser_Uid");

        DatabaseReference databaseReferencediscount = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencediscount.child("shopper").child(preferenceData.getStringValue("aea")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegistrationModelShopper registrationModelShopper = dataSnapshot.getValue(RegistrationModelShopper.class);
                if (registrationModelShopper != null) {
                    discount_value =  String.valueOf(registrationModelShopper.getDiscount());
                    receipt_discount = String.valueOf(registrationModelShopper.getDiscount())+"%";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        et_received_number = view.findViewById(R.id.et_received_number);
        et_received_amount = view.findViewById(R.id.et_received_amount);
        et_time = view.findViewById(R.id.et_time);

        iv_received_pic = view.findViewById(R.id.iv_received_pic);

        iv_capture_received_pic = view.findViewById(R.id.iv_capture_received_pic);
        et_customer_choose_item = view.findViewById(R.id.et_customer_choose_item);

        iv_capture_received_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(getContext(), RcptInfoAddByClintFrgmnt.this);
            }
        });

        btn_received_submit = view.findViewById(R.id.btn_received_submit);
        btn_received_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiptAddIntoFirebase();
            }
        });

        tv_account_number = view.findViewById(R.id.tv_account_number);

        //todo??we was use it for spinner

//        Spinner spin = view.findViewById(R.id.payment_gateway_method);
//        spin.setOnItemSelectedListener(this);
//
//        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,method);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        spin.setAdapter(aa);

        et_received_number.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);



               DatePickerDialog dialog = new DatePickerDialog(
                       context,
                       android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                       mDateSetListener,
                       mYear,mMonth,mDay);
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog.setTitle("Select Date");
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                et_received_number.setText(year+"-"+month+"-"+dayOfMonth);

            }
        };

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                mImageUri = result.getUri();
                iv_received_pic.setImageURI(mImageUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(getActivity(), "Possible error is "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void receiptAddIntoFirebase(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Submitting...");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("receipt_pic");

        final String receipt_pic_url = "pic url";
        final String receipt_shop_uid = preferenceData.getStringValue("aea");
        final String receipt_customer_uid = CurrentUser_Uid;
        final String receipt_date;
        boolean reject_status = false;

        String collect_money_from_shop_date = "";
        Boolean collect_money_from_shop_status = false;
        String back_money_to_customer_date = "";
        boolean back_money_to_customer_status = false;

        Boolean withdrawal_request_status = false;
        String withdraw_request_date = "";
        String withdraw_request_time = "";

        Boolean shopper_rejected_request_status = false;
        String shopper_rejected_request_date = "";
        String shopper_rejected_request_time = "";

        //pic up current date from my user device
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        receipt_date = currentDate.format(calForDate.getTime());

        // pic up current time from my user device
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calForTime.getTime());
        final String random_key = receipt_customer_uid + receipt_customer_uid + receipt_date + saveCurrentTime;

        //this data we get from input fill i means from edit text (ui page)
        final String received_number = et_received_number.getText().toString().trim();
        final String received_amount = et_received_amount.getText().toString().trim();

        //final String customer_return_value = String.valueOf(calculateCustomerReturnValue(received_amount,receipt_shop_uid));
        final String paymentNumber ="";
        String customer_choose_item = et_customer_choose_item.getText().toString().trim();


//        if (Payment_method.equals("Bank")){
//            payment_number = bankAccountNumber;
//        }
//        if (payment_method_container.equals("Bkash") || payment_method_container.equals("Rocket") || payment_method_container.equals("SureCash") || payment_method_container.equals("mCash") || payment_method_container.equals("Nogot")){
//            payment_number = et_account_number.getText().toString().trim();
//        }
        if (TextUtils.isEmpty(received_number) && TextUtils.isEmpty(received_amount) && TextUtils.isEmpty(payment_number)){
            Toast.makeText(getActivity(), "Empty Body", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;

        }
        else {

            String customer_return_value = String.valueOf(calculateCustomerReturnValue(received_amount,receipt_shop_uid));
           // payment_method_container = payment_number+ "( " + payment_method_container+ " )";
            uploadimage(random_key);
            ReceiptModelCustomer receiptModelShopper = new ReceiptModelCustomer(receipt_customer_uid,receipt_shop_uid,received_number,receipt_pic_url,received_amount,receipt_discount,customer_return_value,customer_choose_item,receipt_date,collect_money_from_shop_date,collect_money_from_shop_status,back_money_to_customer_date,back_money_to_customer_status,reject_status,payment_number,payment_gateway,withdrawal_request_status,withdraw_request_date,withdraw_request_time,shopper_rejected_request_status,shopper_rejected_request_date,shopper_rejected_request_time,random_key);
            databaseReference.child(random_key).setValue(receiptModelShopper) ;
            iv_received_pic.setImageResource(R.drawable.defaultpic);
            et_received_number.setText("");
            et_received_amount.setText("");
            progressDialog.dismiss();
        }

    }
    private double calculateCustomerReturnValue(String received_amount, String receipt_discount) {
        double amount = Integer.valueOf(received_amount);
        double discount = Integer.valueOf(discount_value);
        return ((amount*discount)/100);
    }
    private void uploadimage(final String uid) {
        databaseReference = FirebaseDatabase.getInstance().getReference("receiptBYCustomer");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("receipt_pic");
        firebaseAuth = FirebaseAuth.getInstance();
        final String user_uid = CurrentUser_Uid;

        if (mImageUri != null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child(uid+".jpg");
            ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //todo:: need collect pic url from here
                            databaseReference.child(uid).child("receipt_pic_url").setValue(uri.toString());
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading "+(int)progress+"%");
                }
            });
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setTitle("loading...");
//        progressDialog.show();
//        Payment_method = method[position];
//        if (Payment_method.equals("Bank")) {
//            et_account_number.setVisibility(View.GONE);
//            tv_account_number.setVisibility(View.VISIBLE);
//
//            String uid = CurrentUser_Uid;
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//            databaseReference.child("customer").child(uid).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    RegistrationModelCustomer registrationModelCustomer = dataSnapshot.getValue(RegistrationModelCustomer.class);
//                    payment_method_container =  registrationModelCustomer.getPrayment_gateway();
//                    bankAccountNumber = registrationModelCustomer.getPrayment_number();
//                    tv_account_number.setText(payment_method_container);
//                    progressDialog.dismiss();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }else if (Payment_method.equals("Bkash") || Payment_method.equals("Rocket") || Payment_method.equals("SureCash") || Payment_method.equals("mCash") || Payment_method.equals("Nogot")){
//            et_account_number.setVisibility(View.VISIBLE);
//            tv_account_number.setVisibility(View.GONE);
//            payment_method_container = Payment_method;
//
//            progressDialog.dismiss();
//
//
//        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

}

