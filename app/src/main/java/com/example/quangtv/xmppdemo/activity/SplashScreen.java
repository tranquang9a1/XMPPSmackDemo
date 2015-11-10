package com.example.quangtv.xmppdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quangtv.xmppdemo.R;
import com.example.quangtv.xmppdemo.utils.DialogUtils;
import com.example.quangtv.xmppdemo.utils.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by QuangTV on 11/10/15.
 */
public class SplashScreen extends Activity {
    ImageView image;
    final int CHECK_TIMEOUT = 1000;
    int SPLASH_SCREEN_WAIT_TIME = 1000;
    private boolean NO_NEED_INTERNET = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check_connection);

        image = (ImageView) findViewById(R.id.image);

        Bitmap bm = null;
        try {
            bm = getBitmapFromAsset("laucher.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(bm);


        startCheck();
    }

    void startCheck() {

        final Handler handler = new Handler();
        try {


            handler.postDelayed(new Runnable() {
                final Runnable self = this;

                @Override
                public void run() {
                    if (Utils.isOnline() || NO_NEED_INTERNET) {
                        openMainActivity();
                    } else {
                        DialogUtils.showAlert(SplashScreen.this, getResources().getString(R.string.alertConnection), new DialogUtils.IOnOkClicked() {
                            @Override
                            public void onClick() {
                                (new Handler()).postDelayed(self, SPLASH_SCREEN_WAIT_TIME);
                            }
                        }, new DialogUtils.IOnCancelClicked() {
                            @Override
                            public void onClick() {
                                finish();
                            }
                        });
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
    private Bitmap getBitmapFromAsset(String strName) throws IOException
    {
        AssetManager assetManager = getAssets();
        InputStream istr = assetManager.open(strName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

}
