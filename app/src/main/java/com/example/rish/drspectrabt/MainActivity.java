package com.example.rish.drspectrabt;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button connect;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private String address;
    private RadioGroup freqRG,earRG,modeRG;
    private TextInputEditText soundET,volET,sessionET,timerET;
    private String query = null;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceListActivity.EXTRA_ADDRESS);
        query = newint.getStringExtra("Query");

        connect = findViewById(R.id.connectBT);
        freqRG = findViewById(R.id.frequencyRG);
        earRG = findViewById(R.id.earRG);
        modeRG = findViewById(R.id.modeRG);
        soundET = findViewById(R.id.SoundET);
        volET = findViewById(R.id.volumeET);
        sessionET = findViewById(R.id.SessionET);
        timerET = findViewById(R.id.TimerET);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        new ConnectBT().execute();

        checkForAutoUpdate();



        connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                if (btSocket!=null)
                {
                    try
                    {
//                        if(soundET.getText().toString().isEmpty())
//                            soundET.setError("Sound can not be empty");
//                        else if(volET.getText().toString().isEmpty())
//                            volET.setError("Volume can not be empty");
//                        else if(sessionET.getText().toString().isEmpty())
//                            sessionET.setError("Session can not be empty");
//                        else if(timerET.getText().toString().isEmpty())
//                            timerET.setError("Timer can not be empty");
//                        else if(Integer.parseInt(soundET.getText().toString())>10)
//                            soundET.setError("Sound should be in range of 1-10");
//                        else if(Integer.parseInt(volET.getText().toString())>21)
//                        volET.setError("Volume should be in range of 1-21");
//                        else if(Integer.parseInt(sessionET.getText().toString())>250)
//                        sessionET.setError("Session should be in range of 1-250");
//                        else if(Integer.parseInt(timerET.getText().toString())>45)
//                        timerET.setError("Sound should be in range of 1-45");
//                        else {
//
//                            String freq = "$,1",ear = "1",mode = "1",session,timer,vol,sound;
//                            switch(freqRG.getCheckedRadioButtonId()){
//
//                                case R.id.RB11:
//                                    freq = "$,1";
//                                    break;
//
//                                case R.id.RB12:
//                                    freq = "$,2";
//                                    break;
//                                case R.id.RB13:
//                                    freq = "$,3";
//                                    break;
//                            }
//
//                            switch(earRG.getCheckedRadioButtonId()){
//
//                                case R.id.RB21:
//                                    ear = "1";
//                                    break;
//
//                                case R.id.RB22:
//                                    ear = "2";
//                                    break;
//                                case R.id.RB23:
//                                    ear = "3";
//                                    break;
//                            }
//
//                            switch(modeRG.getCheckedRadioButtonId()){
//
//                                case R.id.RB31:
//                                    mode = "1";
//                                    break;
//
//                                case R.id.RB32:
//                                    mode = "2";
//                                    break;
//                            }
//
//                            sound = soundET.getText().toString();
//                            session = sessionET.getText().toString();
//                            timer = timerET.getText().toString()+",#";
//                            vol = volET.getText().toString();
//
//                            query = freq+","+ear+","+sound+","+vol+","+session+","+mode+","+timer;
                            btSocket.getOutputStream().write(query.getBytes());
                            msg("Data Sent");
                        //}
                    }
                    catch (Exception e)
                    {
                        msg("Error");
                    }
                }
                else
                    msg("Bluetooth is not connected");
            }
        });
    }

    private void checkForAutoUpdate() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Device_Details")
                .child("198675");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("connection_status").getValue().toString().equals("True"))
                {
                    if(dataSnapshot.child("Update_Now").getValue().toString().equals("True")){

                        if (btSocket!=null)
                        {
                            try
                            {
//                        if(soundET.getText().toString().isEmpty())
//                            soundET.setError("Sound can not be empty");
//                        else if(volET.getText().toString().isEmpty())
//                            volET.setError("Volume can not be empty");
//                        else if(sessionET.getText().toString().isEmpty())
//                            sessionET.setError("Session can not be empty");
//                        else if(timerET.getText().toString().isEmpty())
//                            timerET.setError("Timer can not be empty");
//                        else if(Integer.parseInt(soundET.getText().toString())>10)
//                            soundET.setError("Sound should be in range of 1-10");
//                        else if(Integer.parseInt(volET.getText().toString())>21)
//                        volET.setError("Volume should be in range of 1-21");
//                        else if(Integer.parseInt(sessionET.getText().toString())>250)
//                        sessionET.setError("Session should be in range of 1-250");
//                        else if(Integer.parseInt(timerET.getText().toString())>45)
//                        timerET.setError("Sound should be in range of 1-45");
//                        else {
//
//                            String freq = "$,1",ear = "1",mode = "1",session,timer,vol,sound;
//                            switch(freqRG.getCheckedRadioButtonId()){
//
//                                case R.id.RB11:
//                                    freq = "$,1";
//                                    break;
//
//                                case R.id.RB12:
//                                    freq = "$,2";
//                                    break;
//                                case R.id.RB13:
//                                    freq = "$,3";
//                                    break;
//                            }
//
//                            switch(earRG.getCheckedRadioButtonId()){
//
//                                case R.id.RB21:
//                                    ear = "1";
//                                    break;
//
//                                case R.id.RB22:
//                                    ear = "2";
//                                    break;
//                                case R.id.RB23:
//                                    ear = "3";
//                                    break;
//                            }
//
//                            switch(modeRG.getCheckedRadioButtonId()){
//
//                                case R.id.RB31:
//                                    mode = "1";
//                                    break;
//
//                                case R.id.RB32:
//                                    mode = "2";
//                                    break;
//                            }
//
//                            sound = soundET.getText().toString();
//                            session = sessionET.getText().toString();
//                            timer = timerET.getText().toString()+",#";
//                            vol = volET.getText().toString();
//
//                            query = freq+","+ear+","+sound+","+vol+","+session+","+mode+","+timer;
                                btSocket.getOutputStream().write(query.getBytes());
                                msg("Data Sent");
                                FirebaseDatabase.getInstance().getReference().child("Device_Details")
                                        .child("198675").child("Update_Now").setValue("False");
                                //}
                            }
                            catch (Exception e)
                            {
                                msg("Error");
                            }
                        }
                    }
                }

//                query = dataSnapshot.child("Query").getValue().toString();
//                updateDate = dataSnapshot.child("updation_time").getValue().toString();
//                setTitle("Last Updated :"+updateDate);
//                isUpdateAvailable = true;
//                // pairedDevice.setBackgroundColor(getResources().getColor(R.color.orange));
//                pairedDevice.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
             //Device found
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
             //Device is now connected
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
             //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
             //Device is about to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
             //Device has disconnected
                FirebaseDatabase.getInstance().getReference().child("Device_Details").child("98675").child("connection_status").setValue("False");
                msg("Disconnected from the device");
                onBackPressed();
            }


        }
    };

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }






private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
{
    private boolean ConnectSuccess = true; //if it's here, it's almost connected

    @Override
    protected Void doInBackground(Void... voids) {

        try
        {
            if (btSocket == null || !isBtConnected)
            {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(dispositivo.getUuids()[0].getUuid());//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        }
        catch (IOException e)
        {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }
        return null;
        //return null;
    }

    @Override
    protected void onPreExecute()
    {
        progress = ProgressDialog.show(MainActivity.this, "Connecting ", "Please wait!!!");  //show a progress dialog
    }

    @Override
    protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
    {
        super.onPostExecute(result);

        if (!ConnectSuccess)
        {
            msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            finish();
        }
        else
        {
            msg("Connected.");

            FirebaseDatabase.getInstance().getReference().child("Device_Details").child("98675").child("connection_status").setValue("True");

            isBtConnected = true;
        }
        progress.dismiss();
    }
}

    @Override
    protected void onResume() {
        super.onResume();
    }
}
