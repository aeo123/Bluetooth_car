package com.example.android.BluetoothMain.app;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.BluetoothMain.BluetoothChatService;
import com.example.android.BluetoothMain.BluetoothMain;
import com.example.android.BluetoothMain.R;

/**
 * Created by aeo on 2016/2/13.
 */
public class DataSendActivity extends Activity {
    // Debugging
    private static final String TAG = "DATA_Activity";
    private static final boolean D = true;

    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 3;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    public static int VAL_ACC_X = 0;
    public static int VAL_ACC_Y = 0;
    public static int VAL_ACC_Z = 0;
    public static int VAL_GYR_X = 0;
    public static int VAL_GYR_Y = 0;
    public static int VAL_GYR_Z = 0;
    public static float VAL_ANG_X = 0;
    public static float VAL_ANG_Y = 0;
    public static float VAL_ANG_Z = 0;
    public static float VAL_PID_PID1_P = 0, VAL_PID_PID1_I = 0, VAL_PID_PID1_D = 0,
            VAL_PID_PID2_P = 0, VAL_PID_PID2_I = 0, VAL_PID_PID2_D = 0,
            VAL_PID_PID3_P = 0, VAL_PID_PID3_I = 0, VAL_PID_PID3_D = 0,
            VAL_PID_PID4_P = 0, VAL_PID_PID4_I = 0, VAL_PID_PID4_D = 0,
            VAL_PID_PID5_P = 0, VAL_PID_PID5_I = 0, VAL_PID_PID5_D = 0,
            VAL_PID_PID6_P = 0, VAL_PID_PID6_I = 0, VAL_PID_PID6_D = 0;
    public static int VAL_VOTAGE1 = 0;

    private static EditText Pid1_P, Pid1_I, Pid1_D;
    private static EditText Pid2_P, Pid2_I, Pid2_D;
    private static EditText Pid3_P, Pid3_I, Pid3_D;
    private static EditText Pid4_P, Pid4_I, Pid4_D;
    private static EditText Pid5_P, Pid5_I, Pid5_D;
    private static EditText Pid6_P, Pid6_I, Pid6_D;
    private static TextView ax, ay, az, gx, gy, gz;
    private static Button button_send, button_read, button_check, button_checkg, button_start, button_save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        setContentView(R.layout.datasend_activity);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //add actionbar back button
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Pid1_P = (EditText) findViewById(R.id.pid1_p);
        Pid1_I = (EditText) findViewById(R.id.pid1_i);
        Pid1_D = (EditText) findViewById(R.id.pid1_d);

        Pid2_P = (EditText) findViewById(R.id.pid2_p);
        Pid2_I = (EditText) findViewById(R.id.pid2_i);
        Pid2_D = (EditText) findViewById(R.id.pid2_d);

        Pid3_P = (EditText) findViewById(R.id.pid3_p);
        Pid3_I = (EditText) findViewById(R.id.pid3_i);
        Pid3_D = (EditText) findViewById(R.id.pid3_d);

        Pid4_P = (EditText) findViewById(R.id.pid4_p);
        Pid4_I = (EditText) findViewById(R.id.pid4_i);
        Pid4_D = (EditText) findViewById(R.id.pid4_d);

        Pid5_P = (EditText) findViewById(R.id.pid5_p);
        Pid5_I = (EditText) findViewById(R.id.pid5_i);
        Pid5_D = (EditText) findViewById(R.id.pid5_d);

        Pid6_P = (EditText) findViewById(R.id.pid6_p);
        Pid6_I = (EditText) findViewById(R.id.pid6_i);
        Pid6_D = (EditText) findViewById(R.id.pid6_d);

        ax = (TextView) findViewById(R.id.ax);
        ay = (TextView) findViewById(R.id.ay);
        az = (TextView) findViewById(R.id.az);
        gx = (TextView) findViewById(R.id.gx);
        gy = (TextView) findViewById(R.id.gy);
        gz = (TextView) findViewById(R.id.gz);

        button_send = (Button) findViewById(R.id.b_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_PID1();
                Send_PID2();
            }
        });
        button_read = (Button) findViewById(R.id.b_read);
        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Command02((byte) 0x01);
            }
        });
        button_check = (Button) findViewById(R.id.b_check);
        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Command((byte) 0x01);
            }
        });
        button_checkg = (Button) findViewById(R.id.b_checkg);
        button_checkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Command((byte) 0x02);
            }
        });
        button_save = (Button) findViewById(R.id.b_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Command((byte) 0x05);
            }
        });
        button_start = (Button) findViewById(R.id.b_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Command((byte) 0x04);
            }
        });

        ui_handler.postDelayed(ui_task, 200);
    }


    private final Handler ui_handler = new Handler();
    private final Runnable ui_task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            ui_handler.postDelayed(this, 200);

            ax.setText("" + VAL_ANG_X);
            ay.setText("" + VAL_ANG_Y);
            az.setText("" + VAL_ANG_Z);
            gx.setText("" + VAL_GYR_X);
            gy.setText("" + VAL_GYR_X);
            gz.setText("" + VAL_GYR_X);
        }
    };


    private static void ShowPID1() {
        Pid1_P.setText("" + VAL_PID_PID1_P);
        Pid1_I.setText("" + VAL_PID_PID1_I);
        Pid1_D.setText("" + VAL_PID_PID1_D);

        Pid2_P.setText("" + VAL_PID_PID2_P);
        Pid2_I.setText("" + VAL_PID_PID2_I);
        Pid2_D.setText("" + VAL_PID_PID2_D);

        Pid3_P.setText("" + VAL_PID_PID3_P);
        Pid3_I.setText("" + VAL_PID_PID3_I);
        Pid3_D.setText("" + VAL_PID_PID3_D);
    }

    private static void ShowPID2() {
        Pid4_P.setText("" + VAL_PID_PID4_P);
        Pid4_I.setText("" + VAL_PID_PID4_I);
        Pid4_D.setText("" + VAL_PID_PID4_D);

        Pid5_P.setText("" + VAL_PID_PID5_P);
        Pid5_I.setText("" + VAL_PID_PID5_I);
        Pid5_D.setText("" + VAL_PID_PID5_D);

        Pid6_P.setText("" + VAL_PID_PID6_P);
        Pid6_I.setText("" + VAL_PID_PID6_I);
        Pid6_D.setText("" + VAL_PID_PID6_D);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        } else {
            // Initialize the BluetoothChatService to perform bluetooth connections
            if (BluetoothMain.mChatService == null)
                this.finish();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (BluetoothMain.mChatService != null) {
            if (BluetoothMain.mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            }
            BluetoothMain.mChatService.setAppState(BluetoothMain.mChatService.APP_DATA);
        }

    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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

    //put actionbar back buton to mainactivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.ccd_num:
                ;
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

    static void SendData(String message) {
        // Check that we're actually connected before trying anything
        if (BluetoothMain.mChatService.getState() != BluetoothMain.mChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothRfcommClient to write
            byte[] send = message.getBytes();
            BluetoothMain.mChatService.write(send);
        }
    }

    static void SendData_Byte(byte[] data) {
        // Check that we're actually connected before trying anything
        if (BluetoothMain.mChatService.getState() != BluetoothMain.mChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothMain.mChatService.write(data);
    }

    static void Send_Command(byte data) {
        byte[] bytes = new byte[6];
        byte sum = 0;
        // Check that we're actually connected before trying anything
        if (BluetoothMain.mChatService.getState() != BluetoothMain.mChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        bytes[0] = (byte) 0xaa;
        bytes[1] = (byte) 0xaf;
        bytes[2] = (byte) 0x01;
        bytes[3] = (byte) 0x01;
        bytes[4] = data;
        for (int i = 0; i < 5; i++) sum += bytes[i];
        bytes[5] = sum;
        SendData_Byte(bytes);
    }

    static void Send_Command02(byte data) {
        byte[] bytes = new byte[6];
        byte sum = 0;
        // Check that we're actually connected before trying anything
        if (BluetoothMain.mChatService.getState() != BluetoothMain.mChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        bytes[0] = (byte) 0xaa;
        bytes[1] = (byte) 0xaf;
        bytes[2] = (byte) 0x02;
        bytes[3] = (byte) 0x01;
        bytes[4] = data;
        for (int i = 0; i < 5; i++) sum += bytes[i];
        bytes[5] = sum;
        SendData_Byte(bytes);
    }

    static void Send_PID1() {
        byte[] bytes = new byte[23];
        byte sum = 0;
        // Check that we're actually connected before trying anything
        if (BluetoothMain.mChatService.getState() != BluetoothMain.mChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        VAL_PID_PID1_P = Float.valueOf(Pid1_P.getText().toString());
        VAL_PID_PID1_I = Float.valueOf(Pid1_I.getText().toString());
        VAL_PID_PID1_D = Float.valueOf(Pid1_D.getText().toString());
        VAL_PID_PID2_P = Float.valueOf(Pid2_P.getText().toString());
        VAL_PID_PID2_I = Float.valueOf(Pid2_I.getText().toString());
        VAL_PID_PID2_D = Float.valueOf(Pid2_D.getText().toString());
        VAL_PID_PID3_P = Float.valueOf(Pid3_P.getText().toString());
        VAL_PID_PID3_I = Float.valueOf(Pid3_I.getText().toString());
        VAL_PID_PID3_D = Float.valueOf(Pid3_D.getText().toString());

        bytes[0] = (byte) 0xaa;
        bytes[1] = (byte) 0xaf;
        bytes[2] = (byte) 0x10;
        bytes[3] = (byte) 18;
        int temp = (int) (VAL_PID_PID1_P * 10);
        bytes[4] = (byte) (temp / 256);
        bytes[5] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID1_I * 100);
        bytes[6] = (byte) (temp / 256);
        bytes[7] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID1_D * 100);
        bytes[8] = (byte) (temp / 256);
        bytes[9] = (byte) (temp % 256);

        temp = (int) (VAL_PID_PID2_P * 10);
        bytes[10] = (byte) (temp / 256);
        bytes[11] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID2_I * 100);
        bytes[12] = (byte) (temp / 256);
        bytes[13] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID2_D * 100);
        bytes[14] = (byte) (temp / 256);
        bytes[15] = (byte) (temp % 256);

        temp = (int) (VAL_PID_PID3_P * 10);
        bytes[16] = (byte) (temp / 256);
        bytes[17] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID3_I * 100);
        bytes[18] = (byte) (temp / 256);
        bytes[19] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID3_D * 100);
        bytes[20] = (byte) (temp / 256);
        bytes[21] = (byte) (temp % 256);

        for (int i = 0; i < 22; i++) sum += bytes[i];
        bytes[22] = sum;

        SendData_Byte(bytes);
    }

    static void Send_PID2() {
        byte[] bytes = new byte[23];
        byte sum = 0;
        // Check that we're actually connected before trying anything
        if (BluetoothMain.mChatService.getState() != BluetoothMain.mChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        bytes[0] = (byte) 0xaa;
        bytes[1] = (byte) 0xaf;
        bytes[2] = (byte) 0x11;
        bytes[3] = (byte) 18;

        VAL_PID_PID4_P = Float.valueOf(Pid4_P.getText().toString());
        VAL_PID_PID4_I = Float.valueOf(Pid4_I.getText().toString());
        VAL_PID_PID4_D = Float.valueOf(Pid4_D.getText().toString());
        VAL_PID_PID5_P = Float.valueOf(Pid5_P.getText().toString());
        VAL_PID_PID5_I = Float.valueOf(Pid5_I.getText().toString());
        VAL_PID_PID5_D = Float.valueOf(Pid5_D.getText().toString());
        VAL_PID_PID6_P = Float.valueOf(Pid6_P.getText().toString());
        VAL_PID_PID6_I = Float.valueOf(Pid6_I.getText().toString());
        VAL_PID_PID6_D = Float.valueOf(Pid6_D.getText().toString());

        int temp = (int) (VAL_PID_PID4_P * 100);
        bytes[4] = (byte) (temp / 256);
        bytes[5] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID4_I * 100);
        bytes[6] = (byte) (temp / 256);
        bytes[7] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID4_D * 100);
        bytes[8] = (byte) (temp / 256);
        bytes[9] = (byte) (temp % 256);

        temp = (int) (VAL_PID_PID5_P * 100);
        bytes[10] = (byte) (temp / 256);
        bytes[11] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID5_I * 100);
        bytes[12] = (byte) (temp / 256);
        bytes[13] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID5_D * 100);
        bytes[14] = (byte) (temp / 256);
        bytes[15] = (byte) (temp % 256);

        temp = (int) (VAL_PID_PID6_P * 100);
        bytes[16] = (byte) (temp / 256);
        bytes[17] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID6_I * 100);
        bytes[18] = (byte) (temp / 256);
        bytes[19] = (byte) (temp % 256);
        temp = (int) (VAL_PID_PID6_D * 100);
        bytes[20] = (byte) (temp / 256);
        bytes[21] = (byte) (temp % 256);

        for (int i = 0; i < 22; i++) sum += bytes[i];
        bytes[22] = sum;

        SendData_Byte(bytes);
    }

    static int COM_BUF_LEN = 1000;
    static byte[] RX_Data = new byte[COM_BUF_LEN];    //接收到的数据，AA开头
    static int rxstate = 0;
    static int rxlen = 0;//该帧已经接收到的长度
    static int rxcnt = 0;//该写入哪字节

    public static void DataAnl(byte[] data, int len) {
        for (int i = 0; i < len; i++) {
            if (rxstate == 0)//寻找开头AA
            {
                if (data[i] == (byte) 0xaa) {
                    rxstate = 1;
                    RX_Data[0] = (byte) 0xaa;
                }
            } else if (rxstate == 1)//寻找第二个AA
            {
                if (data[i] == (byte) 0xaa) {
                    rxstate = 2;
                    RX_Data[1] = (byte) 0xaa;
                } else
                    rxstate = 0;
            } else if (rxstate == 2)//接收功能字
            {
                rxstate = 3;
                RX_Data[2] = data[i];
            } else if (rxstate == 3)//接收len
            {
                if (data[i] > 45)
                    rxstate = 0;
                else {
                    rxstate = 4;
                    RX_Data[3] = data[i];
                    rxlen = RX_Data[3];
                    if (rxlen < 0)
                        rxlen = -rxlen;
                    rxcnt = 4;
                }
            } else if (rxstate == 4) {
                rxlen--;
                RX_Data[rxcnt] = data[i];
                rxcnt++;
                if (rxlen <= 0)
                    rxstate = 5;
            } else if (rxstate == 5)//接收sum
            {
                RX_Data[rxcnt] = data[i];
                if (rxcnt <= (COM_BUF_LEN - 1))
                    FrameAnl(rxcnt + 1);
                //Toast.makeText(getApplicationContext(), "DataAnl OK", Toast.LENGTH_SHORT).show();
                rxstate = 0;
            }
        }
    }

    static void FrameAnl(int len) {
        byte sum = 0;
        for (int i = 0; i < (len - 1); i++)
            sum += RX_Data[i];
        if (sum == RX_Data[len - 1]) {
            //Toast.makeText(getApplicationContext(), "FrameAnl OK", Toast.LENGTH_SHORT).show();
            if (RX_Data[2] == 1)//status
            {
                VAL_ANG_X = ((float) (BytetoUint(4))) / 100;
                VAL_ANG_Y = ((float) (BytetoUint(6))) / 100;
                VAL_ANG_Z = ((float) (BytetoUint(8))) / 100;
            }
            if (RX_Data[2] == 2)//senser
            {
                VAL_ACC_X = BytetoUint(4);
                VAL_ACC_Y = BytetoUint(6);
                VAL_ACC_Z = BytetoUint(8);
                VAL_GYR_X = BytetoUint(10);
                VAL_GYR_Y = BytetoUint(12);
                VAL_GYR_Z = BytetoUint(14);
            }
            if (RX_Data[2] == 5)//votage
            {
                VAL_VOTAGE1 = BytetoUint(4);
            }
            if (RX_Data[2] == (byte) 0x10) {
                VAL_PID_PID1_P = ((float) BytetoUint(4)) / 10;
                VAL_PID_PID1_I = ((float) BytetoUint(6)) / 100;
                VAL_PID_PID1_D = ((float) BytetoUint(8)) / 100;

                VAL_PID_PID2_P = ((float) BytetoUint(10)) / 10;
                VAL_PID_PID2_I = ((float) BytetoUint(12)) / 100;
                VAL_PID_PID2_D = ((float) BytetoUint(14)) / 100;

                VAL_PID_PID3_P = ((float) BytetoUint(16)) / 10;
                VAL_PID_PID3_I = ((float) BytetoUint(18)) / 100;
                VAL_PID_PID3_D = ((float) BytetoUint(20)) / 100;

                ShowPID1();
            }
            if (RX_Data[2] == (byte) 0x11) {
                VAL_PID_PID4_P = ((float) BytetoUint(4)) / 100;
                VAL_PID_PID4_I = ((float) BytetoUint(6)) / 100;
                VAL_PID_PID4_D = ((float) BytetoUint(8)) / 100;

                VAL_PID_PID5_P = ((float) BytetoUint(10)) / 100;
                VAL_PID_PID5_I = ((float) BytetoUint(12)) / 100;
                VAL_PID_PID5_D = ((float) BytetoUint(14)) / 100;

                VAL_PID_PID6_P = ((float) BytetoUint(16)) / 100;
                VAL_PID_PID6_I = ((float) BytetoUint(18)) / 100;
                VAL_PID_PID6_D = ((float) BytetoUint(20)) / 100;
                ShowPID2();
            }
        }
    }

    static short BytetoUint(int cnt) {
        short r = 0;
        r <<= 8;
        r |= (RX_Data[cnt] & 0x00ff);
        r <<= 8;
        r |= (RX_Data[cnt + 1] & 0x00ff);
        return r;
    }
}
