package com.example.android.BluetoothMain.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import static android.graphics.Bitmap.createBitmap;

//视图内部类
class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    public  static Bitmap draw_bitmap,draw_bitmapdual;
    private SurfaceHolder holder;
    private MyThread myThread;

    public  MySurfaceView(Context context, AttributeSet attrs) {
        super(context);
        // TODO Auto-generated constructor stub
        holder = this.getHolder();
        holder.addCallback(this);
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        myThread = new MyThread(holder);//创建一个绘图线程
        myThread.isRun = true;
        myThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        myThread.isRun = false;
    }



    public static void Set_Pic(Bitmap pic){
        draw_bitmap=pic;
    }
    public static void Set_Picdual(Bitmap pic){
        draw_bitmapdual=pic;
    }
    static int count=0;
    public void doDraw(Canvas canvas) {
        if(holder == null){
            return;
        }
        try{
            //bitmap=draw_bitmap;
            Paint paint = new Paint();
            if(draw_bitmap!=null){
                //画布宽和高
                //int width  = getWidth();
                //生成合适的图像
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                //画图
                //canvas.drawColor(Color.WHITE);
                canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
                canvas.drawBitmap(draw_bitmap, null, new Rect(0, 0, 720, draw_bitmap.getHeight()), null);
                //canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
                //canvas.drawBitmap(draw_bitmap, 1, 1, paint);
            }else {
                paint.setAntiAlias(true);//清屏
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                //canvas.drawColor(Color.WHITE);
                //canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
            }
            /**绘制结束后解锁显示在屏幕上**/
            holder.unlockCanvasAndPost(canvas);
        }catch(Exception ex){
            Log.e("ImageSurfaceView", ex.getMessage());
            return;
        }
// finally{
//            //资源回收
////            if(bitmap!=null){;
////                bitmap.recycle();
////            }
//        }
    }

//线程内部类
class MyThread extends Thread {
    private SurfaceHolder holder;
    public boolean isRun;

    public MyThread(SurfaceHolder holder) {
        this.holder = holder;
        isRun = true;
    }
    /**每30帧刷新一次屏幕**/
    public static final int TIME_IN_FRAME = 50;

    @Override
    public void run() {
        while (isRun) {
            Canvas c = null;
            try {
                /**取得更新游戏之前的时间**/
                long startTime = System.currentTimeMillis();
                synchronized (holder) {
                    /**取得更新游戏之前的时间**/
                    c = holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                    doDraw(c);

                }
                /**取得更新游戏结束的时间**/
                long endTime = System.currentTimeMillis();
                    /**计算出游戏一次更新的毫秒数**/
                int diffTime  = (int)(endTime - startTime);
                /**确保每次更新时间为30帧**/
                while(diffTime <=TIME_IN_FRAME) {
                    diffTime = (int)(System.currentTimeMillis() - startTime);
                    /**线程等待**/
                    Thread.yield();
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }
  }
}