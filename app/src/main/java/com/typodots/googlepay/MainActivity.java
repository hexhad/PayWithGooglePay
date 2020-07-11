package com.typodots.googlepay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.typodots.googlepay.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String GOOGLE_PAY_PKG_NAME="com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQ_CODE = 123;
    private ActivityMainBinding binding;
    String amount;
    String name = "hashan";
    String upiId = "hashandharmapriya@gmail.com";
    String transactionNote = "pay test";
    String status;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = binding.amount.getText().toString();
                if (!amount.isEmpty()){
                    uri = getUpiPaymentUri(name,upiId,transactionNote,amount);
                    payWithGPay();
                }
            }
        });
    }

    private void payWithGPay() {
        if (isAppInstalled(this,GOOGLE_PAY_PKG_NAME)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PKG_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQ_CODE);
        } else {
            Toast.makeText(this, "Please Install Google Pay", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resCode, Intent data) {
        super.onActivityResult(requestCode, resCode, data);
        if (data!=null){
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == requestCode) && status.equals("success")){
            Toast.makeText(this, "Transactions Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transactions Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        }catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",transactionNote)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("pa","LKR")
                .build();

    }
}