package com.example.quangtv.xmppdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.quangtv.xmppdemo.R;
import com.example.quangtv.xmppdemo.utils.Utils;

/**
 * Created by QuangTV on 11/10/15.
 */
public class SplashScreen extends Activity {
    TextView txtStatus;
    Button btnTryAgain;
    final int CHECK_TIMEOUT = 1000;
    private boolean NO_NEED_INTERNET = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check_connection);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCheck();
            }
        });

        startCheck();
    }

    void startCheck() {

        btnTryAgain.setVisibility(View.INVISIBLE);
        txtStatus.setText(getApplicationContext().getText(R.string.checkingConnection));

        final Handler handler = new Handler();
        try {


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Utils.isOnline() || NO_NEED_INTERNET) {
                        openMainActivity();
                    } else {
                        btnTryAgain.setVisibility(View.VISIBLE);
                        txtStatus.setText(getApplicationContext().getText(R.string.noConnection));
                    }
                }
            }, CHECK_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openMainActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

}
