package com.example.quangtv.xmppdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.quangtv.xmppdemo.R;
import com.example.quangtv.xmppdemo.component.ColorPickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by QuangTV on 11/4/15.
 */
public class TouchDraw extends Activity implements View.OnTouchListener{
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;
    LinearLayout mDrawingPad;
    File fp;
    Bitmap backgroundImage;
    Drawable d;
    DrawingView mDrawingView;
    ColorPickerDialog dialog;

    int currentColor = Color.BLUE;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawingView = new DrawingView(this);
        mDrawingView.mBitmapPaint.setColor(Color.BLUE);
        setContentView(R.layout.screen_drawing_room);
        Button button = (Button) findViewById(R.id.button);
        Button saveButton = (Button) findViewById(R.id.btnSave);
        Button chooseColor = (Button) findViewById(R.id.chooseColor);
        Button btnUndo = (Button) findViewById(R.id.btnUndo);
        mDrawingPad =(LinearLayout)findViewById(R.id.view_drawing_pad);
        mDrawingPad.addView(mDrawingView);
        chooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ColorPickerDialog(TouchDraw.this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(int color) {
                        Log.d("Color Change", "Color change to" + color);
                        mDrawingView.mPaint.setColor(color);
                        currentColor = color;
                    }
                }, currentColor);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecordIntent();
                //mDrawingView.onClickUndo();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagefrmGallery();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout save = (LinearLayout) findViewById(R.id.view_drawing_pad);

                save.setDrawingCacheEnabled(true);

                save.buildDrawingCache(true);

                Bitmap saveBm = Bitmap.createBitmap(save.getDrawingCache());

                    save.setDrawingCacheEnabled(false);


                OutputStream stream = null;
                String path = "/sdcard/test121.jpg";
                try {
                    stream = new FileOutputStream(path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                scanFile(path);
            }
        });

    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                upx = event.getX();
                upy = event.getY();
                canvas.drawLine(downx, downy, upx, upy, paint);
                imageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    public void  setImagefrmGallery() {
        // To open up a gallery browser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), 1);
        // To handle when an image is selected from the browser, add the following to your Activity
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // currImageURI is the global variable I�m using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                Log.d("Hello=======", getRealPathFromURI(currImageURI));
                String s= getRealPathFromURI(currImageURI);
                File file = new File(s);

                if (file.exists()) {
                    //fp=file.getAbsolutePath();
                    if (backgroundImage != null) {
                        backgroundImage.recycle();
                    }
                    backgroundImage = decodeScaledBitmapFromSdCard(file.getAbsolutePath(), 600, getHeightScreenSize());
                    BitmapDrawable background = new BitmapDrawable(getResources(),backgroundImage);
                    mDrawingPad.setBackground(background);
                    Log.d("Create", "Create Successful!");
                } else {
                    System.out.println("File Not Found");
                }
            } else if (requestCode == 2) {
                Log.d("Create", "Create Successful!");
            }
        }
    }

    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {
        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public  int getHeightScreenSize() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return displaymetrics.heightPixels;

    }

    public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private void scanFile(String path) {

        MediaScannerConnection.scanFile(this,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }

    public void startRecordIntent() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, 2);
    }

}
