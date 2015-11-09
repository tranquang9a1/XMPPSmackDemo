package com.example.quangtv.xmppdemo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import com.example.quangtv.xmppdemo.component.ColorPickerDialog;

import java.util.ArrayList;

/**
 * Created by QuangTV on 11/4/15.
 */
public class DrawingView extends View implements ColorPickerDialog.OnColorChangedListener {
    Paint       mPaint;
    //MaskFilter  mEmboss;
    //MaskFilter  mBlur;
    Bitmap mBitmap;
    Canvas mCanvas;
    Path mPath;
    Paint mBitmapPaint = new Paint();
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();

    public DrawingView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

        mPath = new Path();
        //paths.add(mPath);
        mBitmapPaint.setColor(Color.BLUE);
    }

    @Override
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        /*canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);*/
        for (Path p : paths){
            canvas.drawPath(p, mPaint);
        }
        canvas.drawPath(mPath, mPaint);
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        //mPath.reset();
        undonePaths.clear();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        // kill this so we don't double draw
        paths.add(mPath);
        mPath = new Path();

    }

    public void onClickUndo () {
        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
        else
        {

        }
        //toast the user
    }

    public void onClickRedo (){
        if (undonePaths.size()>0)
        {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }
        else
        {

        }
        //toast the user
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


}