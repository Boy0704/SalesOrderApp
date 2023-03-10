package com.apps.salesorder.ui.so.detail;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps.salesorder.R;
import com.apps.salesorder.data.constants.Constants;
import com.apps.salesorder.data.preferences.Preferences;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerViewActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    int currentApiVersion = Build.VERSION.SDK_INT;
    String accNo,soNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_view);

        //getSupportActionBar().setHomeButtonEnabled(true); //for back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        //getSupportActionBar().setTitle("QR or Barcode Scanner");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            soNo = extras.getString("so_no");
            accNo = extras.getString("acc_no");
            //The key argument here must match that used in the other activity
        }

        if (currentApiVersion >= Build.VERSION_CODES.M) {
            requestCameraPermission();
        }

        scannerView = new ZXingScannerView(ScannerViewActivity.this);
        setContentView(scannerView);
        scannerView.startCamera();
        scannerView.setResultHandler(ScannerViewActivity.this);


    }


    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();

        //set result in main activity or previous activity


        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        Intent intent = new Intent(ScannerViewActivity.this, AddDetailActivity.class);
        intent.putExtra("code_barcode", myResult);
        intent.putExtra("so_no", soNo);
        intent.putExtra("acc_no", accNo);
        finish();
        startActivity(intent);


    }


    //Runtime permission
    private void requestCameraPermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        scannerView = new ZXingScannerView(ScannerViewActivity.this);
                        setContentView(scannerView);
                        scannerView.startCamera();
                        scannerView.setResultHandler(ScannerViewActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Toasty.info(ScannerViewActivity.this, "Izin kamera tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();
                    }
                }).check();
    }


    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
