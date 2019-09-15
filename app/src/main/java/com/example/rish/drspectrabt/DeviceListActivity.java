package com.example.rish.drspectrabt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {

    Button pairedDeviceButton;
    ListView deviceList;
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    private Boolean isUpdateAvailable = false;
    private String query = null,updateDate = null;
    public static String EXTRA_ADDRESS = "Device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);



        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), " Device Does Not Support Bluetooth", Toast.LENGTH_LONG).show();
            finish();
        }
         else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        deviceList = findViewById(R.id.deviceListLV);
        pairedDeviceButton = findViewById(R.id.pairedDeviceButton);
        //pairedDeviceButton.setEnabled(false);
//        checkUpdate();
        pairedDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // if(isUpdateAvailable) {
                    pairedDeviceButton.setVisibility(View.GONE);
                    pairedDevicesList();
                    deviceList.setVisibility(View.VISIBLE);
               // }
//                else{
//                    Toast.makeText(DeviceListActivity.this, "No update is available", Toast.LENGTH_SHORT).show();
//                }

            }
        });



    }

//    private void checkUpdate() {
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Device_Details")
//                .child("198675");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                query = dataSnapshot.child("Query").getValue().toString();
//                updateDate = dataSnapshot.child("updation_time").getValue().toString();
//                setTitle("Last Updated :"+updateDate);
//                isUpdateAvailable = true;
//               // pairedDevice.setBackgroundColor(getResources().getColor(R.color.orange));
//                pairedDevice.setEnabled(true);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void pairedDevicesList() {

        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                if(bt.getName().contains("HC"))
                list.add("Tinnitus Device \n"+bt.getAddress());
                else
                    list.add(bt.getName()+"\n"+bt.getAddress());
                //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Log.e("address",address);
          //  Log.e("query",query);
            // Make an intent to start next activity.
            Intent i = new Intent(DeviceListActivity.this, MainActivity.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address);
           // i.putExtra("Query",query+",#");
            //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:

                startActivity(new Intent(DeviceListActivity.this,PatientProfileActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(DeviceListActivity.this,SelectModeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
