package com.example.android.BluetoothMain.app;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.BluetoothMain.BluetoothChatService;
import com.example.android.BluetoothMain.BluetoothMain;
import com.example.android.BluetoothMain.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by aeo on 2016/2/13.
 */
public class CcdActivity extends Activity {
    // Debugging
    private static final String TAG = "CCDActivity";
    private static final boolean D = true;




    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 3;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        setContentView(R.layout.ccd_activity);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //add actionbar back button
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        }else{
            // Initialize the BluetoothChatService to perform bluetooth connections
            if (BluetoothMain.mChatService == null)
              this.finish();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (BluetoothMain.mChatService != null) {
            if (BluetoothMain.mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            }
            BluetoothMain.mChatService.setAppState(BluetoothMain.mChatService.APP_CCD);
        }

    }
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    //setupChat();
                    ;
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    //put back buton to mainactivity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ccd_menu, menu);
        return true;
    }
    private static final int WIDTH=128;
    private static final int HEIGHT=60;
    private static final int STRIDE=128;//must be >=WIDTH
    int[] img=new int[128*60];
    int[] img1=new int[128];
    private void init_img(int[] a){
       for(int i=0;i<128;i++){
            img1[i]=i<<9;
        }
        for(int i=0;i<60;i++){
         System.arraycopy(img1, 0, img, 128*i, 128);
        }

        Bitmap[] pic=new Bitmap[1];
        pic[0]=Bitmap.createBitmap(img,0, STRIDE, WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        ImageView ig=(ImageView) findViewById(R.id.single_ccd);

        ig.setImageBitmap(pic[0]);
    }
    public static Bitmap bitmap2Gray(Bitmap bmSrc)
    {
        int width, height;
        height = bmSrc.getHeight();
        width = bmSrc.getWidth();
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);
        return bmpGray;

    }

    //put actionbar back buton to mainactivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.ccd_num:
                init_img(null);
                return true;
            case R.id.ccd_mode:
                ;
                return true;
            case R.id.ccd_save:
                ;
                return true;
        }
        return false;
    }

//    /**
//     * @param 将字节数组转换为ImageView可调用的Bitmap对象
//     * @param bytes
//     * @param opts
//     * @return Bitmap
//     */
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
//    /**
//     * @param 图片缩放
//     * @param bitmap 对象
//     * @param w 要缩放的宽度
//     * @param h 要缩放的高度
//     * @return newBmp 新 Bitmap对象
//     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    /**
     * 把字节数组保存为一个文件
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    static byte[] RX_Data = new byte[200];	//接收到的数据，AA开头
    static int rxstate = 0;
    static int rxcnt = 0;
    static boolean ccdDataAnl(byte[] data, int len) {
        for (int i = 0; i < len; i++) {
            if (rxstate == 0)//寻找开头
            {
                if (data[i] == (byte) 2) {
                    rxstate = 1;

                }
            } else if (rxstate == 1)//寻找第二个AA
            {
                if (data[i] == (byte) ~2) {
                    rxstate = 2;

                } else
                    rxstate = 0;
            }else if(rxstate == 2){
                RX_Data[rxcnt] = data[i];
                rxcnt++;
                if(rxcnt==128)
                    rxstate=3;
            }else if(rxstate ==3){
                if (data[i] == (byte) ~2) {
                    rxstate = 4;
                    RX_Data[0] = (byte) ~2;
                }
            }else if(rxstate ==4){
                if (data[i] == (byte) 2) {
                    rxstate = 0;
                    RX_Data[0] = (byte) 2;
                    return true;
                }
            }

        }
        return false;
    }

}
